package io.github.vantiv.sdk;


import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.Socket;
import java.security.GeneralSecurityException;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

import javax.net.ssl.SSLContext;

import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpRequestRetryHandler;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.ConnectionKeepAliveStrategy;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.LayeredConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.*;
import org.apache.http.impl.conn.BasicHttpClientConnectionManager;
import org.apache.http.protocol.HttpContext;
import org.apache.http.ssl.SSLContexts;
import org.apache.http.util.EntityUtils;

import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import com.jcraft.jsch.SftpException;

public class Communication {

    private static final String[] SUPPORTED_PROTOCOLS = new String[]{"TLSv1.2", "TLSv1.1"};
    public static final String CONTENT_TYPE_TEXT_XML_UTF8 = "text/xml; charset=UTF-8";
    private String protocol;
    private CloseableHttpClient httpClient;
    private StreamData streamData;
    private final int KEEP_ALIVE_DURATION = 8000;
    private static final String NEUTER_STR = "NEUTERED";

    public Communication() {
        try {
            protocol = getBestProtocol(SSLContext.getDefault().getSupportedSSLParameters().getProtocols());
            if (protocol == null) {
                throw new IllegalStateException("No supported TLS protocols available");
            }
            SSLContext ctx = SSLContexts.custom().useProtocol(protocol).build();
            ConnectionSocketFactory plainSocketFactory = new PlainConnectionSocketFactory();
            LayeredConnectionSocketFactory sslSocketFactory = new SSLConnectionSocketFactory(ctx);

            Registry<ConnectionSocketFactory> registry = RegistryBuilder.<ConnectionSocketFactory>create()
                    .register("http", plainSocketFactory)
                    .register("https", sslSocketFactory)
                    .build();

            BasicHttpClientConnectionManager manager = new BasicHttpClientConnectionManager(registry);

            HttpRequestRetryHandler requestRetryHandler = new DefaultHttpRequestRetryHandler(0, true);
            // Vantiv will a close an idle connection, so we define our Keep-alive strategy to be below that threshold
            ConnectionKeepAliveStrategy keepAliveStrategy = new ConnectionKeepAliveStrategy() {
                public long getKeepAliveDuration(HttpResponse response, HttpContext context) {
                    return KEEP_ALIVE_DURATION;
                }
            };
            httpClient = HttpClients.custom().setConnectionManager(manager)
                    .setRetryHandler(requestRetryHandler)
                    .setKeepAliveStrategy(keepAliveStrategy)
                    .build();
            streamData = new StreamData();
        } catch (GeneralSecurityException ex) {
            throw new IllegalStateException(ex);
        }
    }

    public static String getBestProtocol(final String[] availableProtocols) {
        String bestProtocol = null;
        if (availableProtocols == null || availableProtocols.length == 0) {
            return bestProtocol;
        }
        List<String> availableProtocolsList = Arrays.asList(availableProtocols);
        for (String supportedProtocol: SUPPORTED_PROTOCOLS) {
            if (availableProtocolsList.contains(supportedProtocol)) {
                bestProtocol = supportedProtocol;
                break;
            }
        }
        return bestProtocol;
    }

    public String requestToServer(String xmlRequest, Properties configuration) {
        String xmlResponse;
        String proxyHost = configuration.getProperty("proxyHost");
        String proxyPort = configuration.getProperty("proxyPort");
        boolean httpKeepAlive = Boolean.valueOf(configuration.getProperty("httpKeepAlive", "false"));
        int httpTimeout = Integer.valueOf(configuration.getProperty("timeout", "6000"));
        HttpHost proxy;
      //  int httpSocketTimeout=4000;
        RequestConfig requestConfig;
        if (proxyHost != null && proxyHost.length() > 0 && proxyPort != null && proxyHost.length() > 0) {
            proxy = new HttpHost(proxyHost, Integer.valueOf(proxyPort));
            requestConfig = RequestConfig.copy(RequestConfig.DEFAULT)
                    .setProxy(proxy)
                    .setSocketTimeout(httpTimeout)
                    .setConnectTimeout(httpTimeout)
                    .build();
        } else {
            requestConfig = RequestConfig.copy(RequestConfig.DEFAULT)
                    .setSocketTimeout(httpTimeout)
                    .setConnectTimeout(httpTimeout)
                    .build();
        }

        HttpPost post = new HttpPost(configuration.getProperty("url"));
        post.setHeader("Content-Type", CONTENT_TYPE_TEXT_XML_UTF8);
        if(!httpKeepAlive) {
            post.setHeader("Connection", "close");
        }

        post.setConfig(requestConfig);
        HttpEntity entity = null;
        try {
            boolean printxml = "true".equalsIgnoreCase(configuration.getProperty("printxml"));
            boolean neuterXml = "true".equalsIgnoreCase(configuration.getProperty("neuterXml"));
            if (printxml) {
                String xmlToLog = xmlRequest;
                if (neuterXml) {
                    xmlToLog = neuterXml(xmlToLog);
                }
                System.out.println("Request XML: " + xmlToLog);
            }
            post.setEntity(new StringEntity(xmlRequest,"UTF-8"));

            HttpResponse response = httpClient.execute(post);
            entity = response.getEntity();
            if (response.getStatusLine().getStatusCode() != 200) {
                throw new LitleOnlineException(response.getStatusLine().getStatusCode() + ":" +
                        response.getStatusLine().getReasonPhrase());
            }
            xmlResponse = EntityUtils.toString(entity,"UTF-8");

            if (printxml) {
                String xmlToLog = xmlResponse;
                if (neuterXml) {
                    xmlToLog = neuterXml(xmlToLog);
                }
                System.out.println("Response XML: " + xmlToLog);
            }
        } catch (IOException e) {
            throw new LitleOnlineException("Exception connection to Litle", e);
        } finally {
            if (entity != null) {
                EntityUtils.consumeQuietly(entity);
            }
            post.abort();
        }
        return xmlResponse;
    }

    /**
     * This method is exclusively used for sending batch file to the communicator.
     * @param requestFile input transaction file
     * @param responseFile response file from Vantiv eCommerce
     * @param configuration {@link Properties} configuration
     * @throws IOException exception from file operations
     */
    public void sendLitleBatchFileToIBC(File requestFile, File responseFile, Properties configuration) throws IOException {
        String hostName = configuration.getProperty("batchHost");
        String hostPort = configuration.getProperty("batchPort");
        int tcpTimeout = Integer.parseInt(configuration.getProperty("batchTcpTimeout"));
        boolean useSSL = configuration.getProperty("batchUseSSL") != null
                && configuration.getProperty("batchUseSSL").equalsIgnoreCase("true");

        Socket socket = null;
        try {
            SSLContext ctx = SSLContexts.custom().useProtocol(protocol).build();
            socket = new Socket(hostName, Integer.parseInt(hostPort));
            socket = ctx.getSocketFactory().createSocket(socket, hostName, Integer.parseInt(hostPort), false);
            socket.setSoTimeout(tcpTimeout);
        } catch (Exception e) {
            throw new LitleBatchException("There was an exception while sending batch file to IBC. Please check the batchHost and batchPort", e);
        }

        streamData.init(socket);

        streamData.dataOut(requestFile);

        streamData.dataIn(responseFile);

        streamData.closeSocket();
    }

    /**
     * This method sends the request file to Litle's server sFTP
     *
     * @param requestFile input transaction file
     * @param configuration {@link Properties} configuration
     * @throws IOException exception from file operations
     */
    public void sendLitleRequestFileToSFTP(File requestFile, Properties configuration) throws IOException {
        String username = configuration.getProperty("sftpUsername");
        String password = configuration.getProperty("sftpPassword");
        String hostname = configuration.getProperty("batchHost");

        java.util.Properties config = new java.util.Properties();
        config.put("StrictHostKeyChecking", "no");
        JSch jsch;
        Session session;
        try {
            jsch = new JSch();
            session = jsch.getSession(username, hostname);
            session.setConfig(config);
            session.setPassword(password);

            session.connect();
        } catch (JSchException e) {
            throw new LitleBatchException("Exception connection to Litle", e);
        }

        Channel channel;

        try {
            channel = session.openChannel("sftp");
            channel.connect();
        } catch (JSchException e) {
            throw new LitleBatchException("Exception connection to Litle", e);
        }

        ChannelSftp sftp = (ChannelSftp) channel;
        boolean printxml = configuration.getProperty("printxml") != null
                && configuration.getProperty("printxml").equalsIgnoreCase(
                "true");

        if (printxml) {
            BufferedReader reader = new BufferedReader(new FileReader(requestFile));
            String line = "";
            while ((line = reader.readLine()) != null) {
                System.out.println(line);
            }
            reader.close();
        }

        try {
            sftp.put(requestFile.getAbsolutePath(), "inbound/" + requestFile.getName() + ".prg");
            sftp.rename("inbound/" + requestFile.getName() + ".prg", "inbound/" + requestFile.getName() + ".asc");
        } catch (SftpException e) {
            throw new LitleBatchException("Exception SFTP operation", e);
        }

        channel.disconnect();
        session.disconnect();
    }

    /**
     * Grabs the response file from Litle's sFTP server. This method is blocking! It will continue to poll until the timeout has elapsed
     * or the file has been retrieved!
     *
     * @param requestFile input transaction file
     * @param responseFile Vantiv response file
     * @param configuration {@link Properties} configuration
     * @throws IOException exception from file operations
     */
    public void receiveLitleRequestResponseFileFromSFTP(File requestFile, File responseFile, Properties configuration) throws IOException {
        String username = configuration.getProperty("sftpUsername");
        String password = configuration.getProperty("sftpPassword");
        String hostname = configuration.getProperty("batchHost");

        java.util.Properties config = new java.util.Properties();
        config.put("StrictHostKeyChecking", "no");
        JSch jsch;
        Session session;
        try {
            jsch = new JSch();
            session = jsch.getSession(username, hostname);
            session.setConfig(config);
            session.setPassword(password);

            session.connect();
        } catch (JSchException e) {
            throw new LitleBatchException("Exception connection to Litle", e);
        }

        Channel channel;

        try {
            channel = session.openChannel("sftp");
            channel.connect();
        } catch (JSchException e) {
            throw new LitleBatchException("Exception connection to Litle", e);
        }

        ChannelSftp sftp = (ChannelSftp) channel;

        Long start = System.currentTimeMillis();
        Long timeout = Long.parseLong(configuration.getProperty("sftpTimeout"));
        System.out.println("Retrieving from sFTP...");
        while (System.currentTimeMillis() - start < timeout) {
            try {
                Thread.sleep(45000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            boolean success = true;
            try {
                sftp.get("outbound/" + requestFile.getName() + ".asc", responseFile.getAbsolutePath());
            } catch (SftpException e) {
                success = false;
            }
            if (success) {
                try {
                    sftp.rm("outbound/" + requestFile.getName() + ".asc");
                } catch (SftpException e) {
                    throw new LitleBatchException("Exception SFTP operation", e);
                }
                break;
            }
            System.out.print(".");
        }
        boolean printxml = configuration.getProperty("printxml") != null
                && configuration.getProperty("printxml").equalsIgnoreCase(
                "true");
        if (printxml) {
            BufferedReader reader = new BufferedReader(new FileReader(responseFile));
            String line = "";
            while ((line = reader.readLine()) != null) {
                System.out.println(line);
            }
            reader.close();
        }

        channel.disconnect();
        session.disconnect();
    }

    /* Method to neuter out sensitive information from xml */
    public String neuterXml(String xml) {
        if (xml == null) {
            return xml;
        }

        xml = xml.replaceAll("<accNum>.*</accNum>", "<accNum>" + NEUTER_STR + "</accNum>");
        xml = xml.replaceAll("<user>.*</user>", "<user>" + NEUTER_STR + "</user>");
        xml = xml.replaceAll("<password>.*</password>", "<password>" + NEUTER_STR + "</password>");
        xml = xml.replaceAll("<track>.*</track>", "<track>" + NEUTER_STR + "</track>");
        xml = xml.replaceAll("<number>.*</number>", "<number>" + NEUTER_STR + "</number>");
        return xml;

    }

}