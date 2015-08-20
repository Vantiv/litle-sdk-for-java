package com.litle.sdk;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;

import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.params.ConnRoutePNames;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.CoreConnectionPNames;
import org.apache.http.util.EntityUtils;

import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import com.jcraft.jsch.SftpException;

public class Communication {

	private DefaultHttpClient httpclient;
	private StreamData streamData;

	public Communication() {
		httpclient = new DefaultHttpClient();
		streamData = new StreamData();
	}

	public String requestToServer(String xmlRequest, Properties configuration) {
		String xmlResponse = null;
		String proxyHost = configuration.getProperty("proxyHost");
		String proxyPort = configuration.getProperty("proxyPort");
		if (proxyHost != null && proxyHost.length() > 0 && proxyPort != null
				&& proxyHost.length() > 0) {
			HttpHost proxy = new HttpHost(proxyHost, Integer.valueOf(proxyPort));
			httpclient.getParams().setParameter(ConnRoutePNames.DEFAULT_PROXY, proxy);
			httpclient.getParams().setParameter(CoreConnectionPNames.SO_LINGER, 0);
		}

		String httpTimeout = configuration.getProperty("timeout");
		String httpReadTimeout = configuration.getProperty("readTimeout");
		if (httpTimeout != null && httpTimeout.length() > 0) {
			httpclient.getParams().setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT,Integer.valueOf(httpTimeout));
		}
		if (httpReadTimeout != null && httpReadTimeout.length() > 0) {
		    httpclient.getParams().setParameter(CoreConnectionPNames.SO_TIMEOUT,Integer.valueOf(httpReadTimeout));
		}

		HttpPost post = new HttpPost(configuration.getProperty("url"));
//		HttpPost post = new HttpPost("http://127.0.0.1:8081/sandbox/communicator/online");
		post.setHeader("Content-Type", "text/xml");
		post.setHeader("Connection","close");
		HttpEntity entity = null;
		try {
			boolean printxml = configuration.getProperty("printxml") != null
					&& configuration.getProperty("printxml").equalsIgnoreCase(
							"true");
			if (printxml) {
				System.out.println("Request XML: " + xmlRequest);
			}
			post.setEntity(new StringEntity(xmlRequest));

			HttpResponse response = httpclient.execute(post);
			if(response.getStatusLine().getStatusCode() != 200) {
				throw new LitleOnlineException(response.getStatusLine().getStatusCode() + ":" + response.getStatusLine().getReasonPhrase());
			}
			entity = response.getEntity();
			xmlResponse = EntityUtils.toString(entity);

			if (printxml) {
				System.out.println("Response XML: " + xmlResponse);
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
	 * @param requestFile
	 * @param responseFile
	 * @param configuration
	 * @throws IOException
	 */
	public void sendLitleBatchFileToIBC(File requestFile, File responseFile, Properties configuration) throws IOException {
		String hostName = configuration.getProperty("batchHost");
		String hostPort = configuration.getProperty("batchPort");
		int tcpTimeout = Integer.parseInt(configuration.getProperty("batchTcpTimeout"));
		boolean useSSL = configuration.getProperty("batchUseSSL") != null
				&& configuration.getProperty("batchUseSSL").equalsIgnoreCase("true");
		streamData.init(hostName, hostPort, tcpTimeout, useSSL);

		streamData.dataOut(requestFile);

		streamData.dataIn(responseFile);

		streamData.closeSocket();
	}

	/**
	 * This method sends the request file to Litle's server sFTP
	 * @param requestFile
	 * @param configuration
	 * @throws IOException
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
	 * @param requestFile
	 * @param responseFile
	 * @param configuration
	 * @throws IOException
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
}
