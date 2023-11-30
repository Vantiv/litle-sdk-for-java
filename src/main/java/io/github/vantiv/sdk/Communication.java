package io.github.vantiv.sdk;

import java.io.*;
import java.net.Socket;
import java.security.GeneralSecurityException;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

import javax.net.ssl.SSLContext;

import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
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
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.protocol.BasicHttpContext;
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

	private static Communication instance = null;

    private static final String[] SUPPORTED_PROTOCOLS = new String[] {"TLSv1.2", "TLSv1.1"};
    public static final String CONTENT_TYPE_TEXT_XML_UTF8 = "text/xml; charset=UTF-8";

    private CloseableHttpClient httpclient;
    private StreamData streamData;
    private Properties config;
    private String protocol;
    private final int DEFAULT_MAX_IN_POOL = 3;
    private final int KEEP_ALIVE_DURATION = 8000;
    private int maxHttpConnections;
    private boolean httpKeepAlive = false;
    private static final String NEUTER_STR = "NEUTERED";

    private Communication() { }

    /**
     * Used to get a handle to the Singleton instance
     * @return Singleton instance of the {@link Communication}
     */
    public static Communication getInstance() {
    	if (instance == null) {
    		instance = new Communication();
    		instance.init();
		}
		return instance;
	}

    private void init() {
        setConfig();
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

            PoolingHttpClientConnectionManager manager = new PoolingHttpClientConnectionManager(registry);
            manager.setDefaultMaxPerRoute(maxHttpConnections);
            manager.setMaxTotal(maxHttpConnections);
            manager.setValidateAfterInactivity(KEEP_ALIVE_DURATION);

            ConnectionKeepAliveStrategy keepAliveStrategy = new ConnectionKeepAliveStrategy() {
                public long getKeepAliveDuration(HttpResponse response, HttpContext context) {
                    return KEEP_ALIVE_DURATION;
                }
            };

            httpclient = HttpClients.custom()
                    .setConnectionManager(manager)
                    .setKeepAliveStrategy(keepAliveStrategy)
                    .build();
            streamData = new StreamData();
        } catch (GeneralSecurityException ex) {
            throw new IllegalStateException(ex);
        }
    }

    private void setConfig() {
        FileInputStream fileInputStream = null;
        try {
            config = new Properties();
            fileInputStream = new FileInputStream((new Configuration()).location());
            config.load(fileInputStream);
            maxHttpConnections = Integer.valueOf(config.getProperty("httpConnPoolSize", String.valueOf(DEFAULT_MAX_IN_POOL)));
            httpKeepAlive = Boolean.valueOf(config.getProperty("httpKeepAlive", "false"));
        } catch (FileNotFoundException e) {
            maxHttpConnections = Integer.valueOf(DEFAULT_MAX_IN_POOL);
        } catch (IOException e) {
            throw new LitleOnlineException("Configuration file could not be loaded. " +
                    "Check to see if the user running this has permission to access the file", e);
        } catch (ClassCastException ce){
            throw new LitleOnlineException("Configuration contained an invalid 'httpConnPoolSize' or 'httpKeepAlive'.",
                    ce);
        } finally {
            if (fileInputStream != null){
                try {
                    fileInputStream.close();
                } catch (IOException e) {
                    throw new LitleOnlineException("Configuration FileInputStream could not be closed.", e);
                }
            }
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

    /**
     * Method used to send an online transaction to Vantiv eCommerce
     *
     * @param xmlRequest Vantiv eCommerce online XML request
     * @param configuration {@link Properties} configuration for HTTP connection
     * @param context {@link BasicHttpContext} belonging to the calling class
     * @param reqCfg {@link RequestConfig} will always take precedence over {@link Properties} configuration
     * @return {@link String} Vantiv eCommerce XML response
     */
	public String requestToServer(String xmlRequest, Properties configuration, BasicHttpContext context, RequestConfig reqCfg) {
		String xmlResponse;
		String proxyHost = configuration.getProperty("proxyHost");
		String proxyPort = configuration.getProperty("proxyPort");
        int httpTimeout = Integer.valueOf(configuration.getProperty("timeout", "6000"));
        HttpHost proxy;
        RequestConfig requestConfig = null;
        if (reqCfg == null) {
            if (proxyHost != null && proxyHost.length() > 0 && proxyPort != null
                    && proxyHost.length() > 0) {
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
        } else {
            requestConfig = reqCfg;
        }

		HttpPost post = new HttpPost(configuration.getProperty("url"));
		post.setHeader("Content-Type", CONTENT_TYPE_TEXT_XML_UTF8);
        if (httpKeepAlive) {
            // we want to leave this connection open for reuse
            post.setHeader("Connection","keep-alive");
        } else {
            post.setHeader("Connection","close");
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
			post.setEntity(new StringEntity(xmlRequest));

			HttpResponse response = httpclient.execute(post, context);
			if(response.getStatusLine().getStatusCode() != 200) {
				throw new LitleOnlineException(response.getStatusLine().getStatusCode() + ":" + response.getStatusLine().getReasonPhrase());
			}
			entity = response.getEntity();
			xmlResponse = EntityUtils.toString(entity);

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
			if(entity != null) {
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

        boolean printxml = configuration.getProperty("printxml") != null
                && configuration.getProperty("printxml").equalsIgnoreCase(
                "true");

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

        if(printxml){
            BufferedReader reader = new BufferedReader(new FileReader(requestFile));
            String line = "";
            while((line = reader.readLine()) != null){
                System.out.println(line);
            }
            reader.close();
        }

		streamData.dataOut(requestFile);

		streamData.dataIn(responseFile);

        if(printxml){
            BufferedReader reader = new BufferedReader(new FileReader(responseFile));
            String line = "";
            while((line = reader.readLine()) != null){
                System.out.println(line);
            }
            reader.close();
        }

		streamData.closeSocket();
	}

	/**
	 * This method sends the request file to Litle's server sFTP
	 * @param requestFile Vantiv eCommerce XML batch file
	 * @param configuration {@link Properties} configuration for processing
	 * @throws IOException exception from file operations
	 */
	public void sendLitleRequestFileToSFTP(File requestFile, Properties configuration) throws IOException{
	    String username = configuration.getProperty("sftpUsername");
	    String password = configuration.getProperty("sftpPassword");
	    String hostname = configuration.getProperty("batchHost");

	    java.util.Properties config = new java.util.Properties();
	    config.put("StrictHostKeyChecking", "no");
	    JSch jsch = null;
	    Session session = null;
	    try{
    	    jsch = new JSch();
    	    session = jsch.getSession(username, hostname);
    	    session.setConfig(config);
    	    session.setPassword(password);

    	    session.connect();
	    }
	    catch(JSchException e){
	        throw new LitleBatchException("Exception connection to Litle", e);
	    }

	    Channel channel = null;

	    try{
	        channel = session.openChannel("sftp");
	        channel.connect();
	    }
	    catch(JSchException e){
	        throw new LitleBatchException("Exception connection to Litle", e);
	    }

	    ChannelSftp sftp = (ChannelSftp) channel;
	    boolean printxml = configuration.getProperty("printxml") != null
                && configuration.getProperty("printxml").equalsIgnoreCase(
                        "true");

	    if(printxml){
            BufferedReader reader = new BufferedReader(new FileReader(requestFile));
            String line = "";
            while((line = reader.readLine()) != null){
                System.out.println(line);
            }
	    }

	    try {
            sftp.put(requestFile.getAbsolutePath(), "inbound/" + requestFile.getName() + ".prg");
            sftp.rename("inbound/" + requestFile.getName() + ".prg", "inbound/" + requestFile.getName() + ".asc");
        }
	    catch (SftpException e) {
            throw new LitleBatchException("Exception SFTP operation", e);
        }

	    channel.disconnect();
	    session.disconnect();
	}

	/**
	 * Grabs the response file from Litle's sFTP server. This method is blocking! It will continue to poll until the timeout has elapsed
	 * or the file has been retrieved!
     * @param requestFile {@link File} containing Vantiv eCommerce XML batch
     * @param responseFile {@link File} containing Vantiv eCommerce XML response
     * @param configuration {@link Properties} configuration for processing
     * @throws IOException exception from file operations
	 */
	public void receiveLitleRequestResponseFileFromSFTP(File requestFile, File responseFile, Properties configuration) throws IOException{
	    String username = configuration.getProperty("sftpUsername");
        String password = configuration.getProperty("sftpPassword");
        String hostname = configuration.getProperty("batchHost");

        java.util.Properties config = new java.util.Properties();
        config.put("StrictHostKeyChecking", "no");
        JSch jsch = null;
        Session session = null;
        try{
            jsch = new JSch();
            session = jsch.getSession(username, hostname);
            session.setConfig(config);
            session.setPassword(password);

            session.connect();
        }
        catch(JSchException e){
            throw new LitleBatchException("Exception connection to Litle", e);
        }

        Channel channel = null;

        try{
            channel = session.openChannel("sftp");
            channel.connect();
        }
        catch(JSchException e){
            throw new LitleBatchException("Exception connection to Litle", e);
        }

        ChannelSftp sftp = (ChannelSftp) channel;

        Long start = System.currentTimeMillis();
        Long timeout = Long.parseLong(configuration.getProperty("sftpTimeout"));
        System.out.println("Retrieving from sFTP...");
        while(System.currentTimeMillis() - start < timeout){
            try {
                Thread.sleep(45000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            boolean success = true;
            try{
                sftp.get("outbound/" + requestFile.getName() + ".asc", responseFile.getAbsolutePath());
            }
            catch(SftpException e){
                success = false;
                System.out.println(e);
            }
            if(success) {
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
        if(printxml){
            BufferedReader reader = new BufferedReader(new FileReader(responseFile));
            String line = "";
            while((line = reader.readLine()) != null){
                System.out.println(line);
            }
        }

        channel.disconnect();
        session.disconnect();
	}


	void setStreamData(StreamData streamData) {
		this.streamData = streamData;
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
