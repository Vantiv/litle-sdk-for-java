package com.litle.sdk;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.HttpHost;
import org.apache.commons.httpclient.methods.InputStreamRequestEntity;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.StringRequestEntity;

public class Communication {

	private String user;
	private String password;
	private String merchantId[][];
	private String proxy_addr;
	private String proxy_port;
	private String litle_url;
	private String default_report_group;
	private String version;
	private String timeout;
	private String printxml;

	public Communication() {
		this.user = "";
		this.password = "";
		this.merchantId[0][0] = "";
		this.merchantId[0][1] = "";
		this.proxy_addr = "";
		this.proxy_port = "";
		this.litle_url = "https://www.testlitle.com/sandbox/communicator/online";
		this.default_report_group = "";
		this.version = "";
		this.timeout = "65";
		this.printxml = "false";
	}

	public String getUser() {
		return user;
	}

	public void setUser(String user) {
		this.user = user;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String[][] getMerchantId() {
		return merchantId;
	}

	// To set merchantId array, you need to get the existing array first by calling 'getMerchantId()',
	// and then append to that array. You will lose configured merchantIds if these instructions are ignored.
	public void setMerchantId(String[][] merchantId) {
		this.merchantId = merchantId;
	}

	public String getProxy_addr() {
		return proxy_addr;
	}

	public void setProxy_addr(String proxy_addr) {
		this.proxy_addr = proxy_addr;
	}

	public String getProxy_port() {
		return proxy_port;
	}

	public void setProxy_port(String proxy_port) {
		this.proxy_port = proxy_port;
	}

	public String getLitle_url() {
		return litle_url;
	}

	public void setLitle_url(String litle_url) {
		this.litle_url = litle_url;
	}

	public String getDefault_report_group() {
		return default_report_group;
	}

	public void setDefault_report_group(String default_report_group) {
		this.default_report_group = default_report_group;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public String getTimeout() {
		return timeout;
	}

	public void setTimeout(String timeout) {
		this.timeout = timeout;
	}

	public String getPrintxml() {
		return printxml;
	}

	public void setPrintxml(String printxml) {
		this.printxml = printxml;
	}

	public void loadConfig() {
		// read from the config file -- hard coded for now
		this.user = "ding";
		this.password = "something";
		this.merchantId[0][0] = "DEFAULT";
		this.merchantId[0][1] = "101";
		this.proxy_addr = "smoothproxy";
		this.proxy_port = "8080";
		this.litle_url = "https://www.testlitle.com/sandbox/communicator/online";
		this.default_report_group = "Default Report Group";
		this.version = "8.10";
		this.timeout = "65";
		this.printxml = "false";
	}

	public String requestToServer(String xmlRequest) {
		String retVal = "";

		if( this.printxml == "true" ){
			System.out.println("xmlRequest: " + xmlRequest);
		}
		// Prepare HTTP post
		PostMethod post = new PostMethod(this.litle_url);
		try {
			post.setRequestEntity(new StringRequestEntity(xmlRequest,
					"text/xml", "UTF-8"));
			post.setRequestHeader("Content-type", "text/xml; charset=UTF-8");
		} catch (UnsupportedEncodingException e) {
			// e.printStackTrace();
			retVal = "Encountered an unsupported encoding exception: "
					+ e.toString();
		}

		if (retVal.isEmpty()) {
			HttpClient httpclient = new HttpClient();
			// Add proxy information if available
			if(!this.proxy_addr.isEmpty()){
				HttpHost proxy = new HttpHost(this.proxy_addr, Integer.parseInt(this.proxy_port));
				httpclient.getParams().setParameter(ConnRoutePNames.DEFAULT_PROXY, proxy);
			}
			
			try {

				int result = 0;
				try {
					result = httpclient.executeMethod(post);
					retVal = post.getResponseBodyAsString();
					if( retVal == "" ){
						retVal = Integer.toString(result);
					}
				} catch (HttpException e) {
					//e.printStackTrace();
					retVal = "Encountered an httpexception: " + e.toString();
				} catch (IOException e) {
					//e.printStackTrace();
					retVal = "Encountered an IOException: " + e.toString();
				}
			} finally {
				post.releaseConnection();
			}
		}

		if( this.printxml == "true" ){
			System.out.println("retVal: " + retVal);
		}
		return retVal;
	}
}
