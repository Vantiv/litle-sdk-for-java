package com.litle.sdk;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.util.HashMap;
import java.util.Properties;

public class Setup {

	@SuppressWarnings("serial")
	private static final HashMap<String,String> URL_MAP = new HashMap<String,String>() {
		{
			put("sandbox","https://www.testlitle.com/sandbox/communicator/online");
			put("cert","https://cert.litle.com/vap/communicator/online");
			put("precert","https://precert.litle.com/vap/communicator/online");
			put("production","https://payments.litle.com/vap/communicator/online");
		}
	};
	
	/**
	 * @param args
	 * @throws IOException 
	 */
	public static void main(String[] args) throws IOException {
		File file = Configuration.location();
		Properties config = new Properties();
		PrintStream configFile = new PrintStream(file);
		
		BufferedReader stdin = new BufferedReader
	      (new InputStreamReader(System.in));

		System.out.println("Welcome to Litle Java_SDK");
		System.out.print("Please input your user name: ");
		config.put("username", stdin.readLine());
		System.out.print("Please input your password: ");
		config.put("password", stdin.readLine());
		System.out.print("Please input your merchantId: ");
		config.put("merchantId", stdin.readLine());
		System.out.println("Please choose Litle URL from the following list (example: 'cert') or directly input another URL:");
		System.out.println("\tsandbox => https://www.testlitle.com/sandbox/communicator/online");
		System.out.println("\tcert => https://cert.litle.com/vap/communicator/online");
		System.out.println("\tprecert => https://precert.litle.com/vap/communicator/online");
		System.out.println("\tproduction => https://payments.litle.com/vap/communicator/online");
		config.put("url", URL_MAP.get(stdin.readLine()));
		System.out.print("Please input the proxy host, if no proxy hit enter: ");
		config.put("proxyHost", stdin.readLine());
		System.out.print("Please input the proxy port, if no proxy hit enter: ");
		config.put("proxyPort", stdin.readLine());
		config.put("version", "8.18");
		config.put("timeout", "65");
		config.put("reportGroup", "Default Report Group");
		config.put("printxml", "true");
		
		//These properties are for batch
		System.out.print("Please enter the Batch Host Name: ");
		//Entering the my system name for testing
		config.put("batchHost",stdin.readLine());
		System.out.print("Please enter the Batch Port Name: ");
		config.put("batchPort", stdin.readLine());
		System.out.print("Please enter the Batch TCP Timeout: ");
		config.put("batchTcpTimeout", stdin.readLine());
		System.out.print("Please enter the Batch Use SSL (True/False): ");
		config.put("batchUSeSSL", stdin.readLine());
		config.put("maxAllowedTransactionsPerFile", "500000");
		config.put("maxTransactionsPerBatch", "100000");

		config.store(configFile, "");
		System.out.println("The Litle configuration file has been generated, the file is located at " + file.getAbsolutePath());
	}
}
