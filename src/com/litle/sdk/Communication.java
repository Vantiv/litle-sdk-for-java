package com.litle.sdk;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
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

	public LitleBatchFileResponse sendLitleBatchFileToIBC(File requestFile, String resposeFilePath, Properties configuration) throws Exception {
		String hostName = configuration.getProperty("batchHost");
		String hostPort = configuration.getProperty("batchPort");
		int tcpTimeout = Integer.parseInt(configuration.getProperty("batchTcpTimeout"));
		boolean useSSL = configuration.getProperty("batchUseSSL") != null
				&& configuration.getProperty("batchUseSSL").equalsIgnoreCase("true");
		streamData.init(hostName, hostPort, tcpTimeout, useSSL);

		streamData.dataOut(requestFile);

		String content = streamData.dataIn();
		File responseFile = new File(resposeFilePath);

		// if file doesnt exists, then create it
		if (!responseFile.exists()) {
			responseFile.createNewFile();
		}

		FileWriter fw = new FileWriter(responseFile.getAbsoluteFile());
		BufferedWriter bw = new BufferedWriter(fw);
		bw.write(content);
		bw.close();

		streamData.closeSocket();

		LitleBatchFileResponse retObj = new LitleBatchFileResponse(null);

		return retObj;
	}
	
	void setStreamData(StreamData streamData) {
		this.streamData = streamData;
	}
}
