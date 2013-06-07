package com.litle.sdk;

import java.io.File;
import java.io.IOException;
import java.util.Properties;

import net.sf.opensftp.SftpException;
import net.sf.opensftp.SftpResult;
import net.sf.opensftp.SftpSession;
import net.sf.opensftp.SftpUtil;
import net.sf.opensftp.SftpUtilFactory;

import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.params.ConnRoutePNames;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.CoreConnectionPNames;
import org.apache.http.util.EntityUtils;

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

		HttpPost post = new HttpPost(configuration.getProperty("url"));
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

	public void sendLitleRequestFileToSFTP(File requestFile, Properties configuration) throws IOException{
	    String username = configuration.getProperty("sftpUsername");
	    String password = configuration.getProperty("sftpPassword");
	    String hostname = configuration.getProperty("batchHost");


	    SftpUtil util = SftpUtilFactory.getSftpUtil();
	    SftpSession session = null;
	    try{
	        session = util.connectByPasswdAuth(hostname, username, password, SftpUtil.STRICT_HOST_KEY_CHECKING_OPTION_NO);
	    } catch(SftpException e){
	        throw new LitleBatchException("Exception connection to Litle", e);
	    }

	    util.put(session, requestFile.getAbsolutePath(), "inbound/" + requestFile.getName() + ".prg");
	    util.rename(session, "inbound/" + requestFile.getName() + ".prg", "inbound/" + requestFile.getName() + ".asc");
	    util.disconnect(session);

	    System.out.println("SFTPing at " + username + " to " + hostname + " with " + password );
	}

	public void receiveLitleRequestResponseFileFromSFTP(File requestFile, File responseFile, Properties configuration) throws IOException{
	    String username = configuration.getProperty("sftpUsername");
        String password = configuration.getProperty("sftpPassword");
        String hostname = configuration.getProperty("batchHost");


        SftpUtil util = SftpUtilFactory.getSftpUtil();
        SftpSession session = null;
        try{
            session = util.connectByPasswdAuth(hostname, username, password, SftpUtil.STRICT_HOST_KEY_CHECKING_OPTION_NO);
        } catch(SftpException e){
            throw new LitleBatchException("Exception connection to Litle", e);
        }
        Long start = System.currentTimeMillis();
        Long timeout = Long.parseLong(configuration.getProperty("sftpTimeout"));
        while(System.currentTimeMillis() - start < timeout){
            System.out.println("Checking...");
            try {
                Thread.sleep(15000);
            } catch (InterruptedException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            SftpResult res = util.get(session, "outbound/" + requestFile.getName() + ".asc", responseFile.getAbsolutePath());
            if(res.getSuccessFlag()) {
                System.out.println("We graped in the mouth~!");
                break;
            }
            System.out.println("We missed it :(");
        }
        util.disconnect(session);
	}


	void setStreamData(StreamData streamData) {
		this.streamData = streamData;
	}
}
