package com.litle.sdk;

import java.io.IOException;
import java.util.Properties;

import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.params.ConnRoutePNames;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.CoreConnectionPNames;
import org.apache.http.util.EntityUtils;

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
		post.setHeader("content-type", "text/xml");
		post.setHeader("Connection","close");

		try {
			boolean printxml = configuration.getProperty("printxml") != null
					&& configuration.getProperty("printxml").equalsIgnoreCase(
							"true");
			if (printxml) {
				System.out.println("Request XML: " + xmlRequest);
			}
			post.setEntity(new StringEntity(xmlRequest));
			
			xmlResponse = sendPostToLitle(post);

			if (printxml) {
				System.out.println("Response XML: " + xmlResponse);
			}
		} catch (IOException e) {
			throw new LitleOnlineException("Exception connection to Litle", e);
		} finally {
			httpclient.getConnectionManager().shutdown();
		}
		return xmlResponse;
	}

	private String sendPostToLitle(HttpPost post) throws IOException,
			ClientProtocolException {
		int counter = 0;
		HttpResponse response = httpclient.execute(post);
		while(response.getStatusLine().getStatusCode() == 403) {
			System.err.println("Got a transient 403 for the " + counter + " time in a row");
			if(counter < 10) {
				counter++;
				try {
					long sleepTime = counter * 1000L;
					String temporaryFailureMsg = "";
					for(int i = 0; i < counter; i++) {
						temporaryFailureMsg += ".";
					}
					temporaryFailureMsg += "Trying again but will sleep for " + sleepTime/1000L + "s first";
					System.err.println(temporaryFailureMsg);
					Thread.sleep(sleepTime);
				} catch (InterruptedException e) {
					System.err.println("Got a transient interruption.  Ignoring");
				}
			}
			else {
				String failureMsg = "..........Tried " + counter + "times but kept getting 403, even with sleeping.  Response from Litle " + response.getStatusLine().getReasonPhrase();
				System.err.println(failureMsg);
				throw new LitleOnlineException(failureMsg);
			}
			response = httpclient.execute(post);
		}
		HttpEntity entity = response.getEntity();
		String xmlResponse = EntityUtils.toString(entity);			
		return xmlResponse;
	}
}
