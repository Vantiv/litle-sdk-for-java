package com.litle.sdk;

import java.io.File;
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

//import com.phoenix.common.util.FileFunctions;
//import com.phoenix.common.util.StreamData;

public class Communication {

	private DefaultHttpClient httpclient;

	public Communication() {
		httpclient = new DefaultHttpClient();
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

//	public void sendLitleBatchFileToIBC(File file) {
//
//	        //System.out.println("sendFileToIBC(" + inputFile + ")");
//	        		
//	        // read the merchant's file from disk
//	        String data = file.toString();
//	        String string;
//
//	        // connect to the ibc and send the data
//	        StreamData streamData = new StreamData();
//	        streamData.init(environment.getHostName(), environment.getPort(), tcpTimeout, shouldSSLEncrypt);
//	        streamData.dataOut(data);
//
//	        // get the responses from the ibc
//	        string = streamData.dataIn();
//	        // write out the results to disk
//	        file.openFileForWriting(actualResultsFile);
//	        file.write(string);
//	        file.closeFile();
//	        streamData.closeSocket();
//	}
//	
}
