package com.litle.sdk;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.apache.commons.httpclient.HostConfiguration;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpHost;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.cxf.helpers.IOUtils;


public class Communication {

	public String requestToServer(String xmlRequest, Properties configuration) {
		String xmlResponse = null;
		String proxyHost = configuration.getProperty("proxyHost");
		String proxyPort = configuration.getProperty("proxyPort");
		HttpClient httpclient = new HttpClient();
		if(proxyHost != null && proxyHost.length() > 0 && proxyPort != null && proxyHost.length() > 0) {
			HostConfiguration proxy = new HostConfiguration();
			proxy.setProxy(proxyHost, Integer.parseInt(proxyPort));
			httpclient.setHostConfiguration(proxy);
		}
	
		PostMethod httppost = new PostMethod(configuration.getProperty("url"));
		httppost.setRequestHeader("content-type", "text/xml");

		try {
			boolean printxml = configuration.getProperty("printxml") != null && configuration.getProperty("printxml").equalsIgnoreCase("true");
			if(printxml) {
				System.out.println("Request XML: " + xmlRequest);
			}
			httppost.setRequestBody(xmlRequest);
			httpclient.executeMethod(httppost);
			InputStream in = httppost.getResponseBodyAsStream();
			xmlResponse = IOUtils.readStringFromStream(in);
			if(printxml) {
				System.out.println("Response XML: " + xmlResponse);
			}

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return xmlResponse;
	}
	
	
}
