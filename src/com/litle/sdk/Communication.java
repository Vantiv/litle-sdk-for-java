package com.litle.sdk;

import java.io.IOException;
import java.util.Properties;

import org.apache.cxf.helpers.IOUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.params.ConnRoutePNames;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;

public class Communication {

	public String requestToServer(String xmlRequest, Properties configuration) {
		String xmlResponse = null;
		String proxyHost = configuration.getProperty("proxyHost");
		String proxyPort = configuration.getProperty("proxyPort");
		HttpClient httpclient = new DefaultHttpClient();
		if(proxyHost != null && proxyHost.length() > 0 && proxyPort != null && proxyHost.length() > 0) {
			HttpHost proxy = new HttpHost(proxyHost, Integer.parseInt(proxyPort));
			httpclient.getParams().setParameter(ConnRoutePNames.DEFAULT_PROXY, proxy);
		}
	
		HttpPost httppost = new HttpPost(configuration.getProperty("url"));
		httppost.setHeader("content-type", "text/xml");

		try {
			boolean printxml = configuration.getProperty("printxml") != null && configuration.getProperty("printxml").equalsIgnoreCase("true");
			if(printxml) {
				System.out.println("Request XML: " + xmlRequest);
			}
			HttpEntity content = new StringEntity(xmlRequest); 
			httppost.setEntity(content);
			HttpResponse httpResponse = httpclient.execute(httppost);
			xmlResponse = IOUtils.readStringFromStream(httpResponse.getEntity().getContent());
			if(printxml) {
				System.out.println("Response XML: " + xmlResponse);
			}

		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return xmlResponse;
	}
	
	
}
