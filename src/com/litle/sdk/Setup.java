package com.litle.sdk;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.util.HashMap;
import java.util.Properties;

import org.apache.commons.codec.binary.StringUtils;

public class Setup {

	@SuppressWarnings("serial")
	private static final HashMap<String,String> URL_MAP = new HashMap<String,String>() {
		{
			put("sandbox","https://www.testlitle.com/sandbox/communicator/online");
			put("cert","https://cert.litle.com/vap/communicator/online");
			put("precert","https://precert.litle.com/vap/communicator/online");
			put("production","https://payments.litle.com/vap/communicator/online");
			put("batchSandbox","https://www.testlitle.com/sandbox");
			put("batchCert","https://cert.litle.com");
			put("batchPrecert","https://precert.litle.com");
			put("batchProduction", "https://payments.litle.com");
		}
	};
	
	@SuppressWarnings("serial")
	private static final HashMap<String,String> PORT_MAP = new HashMap<String,String>() {
		{
			put("batchSandbox","15000");
			put("batchCert","15000");
			put("batchPrecert","15000");
			put("batchProduction", "15000");
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
		String lastUserInput;
		
		BufferedReader stdin = new BufferedReader
	      (new InputStreamReader(System.in));

		System.out.println("Welcome to Litle Java_SDK");
		System.out.print("Please input your user name: ");
		config.put("username", stdin.readLine());
		System.out.print("Please input your password: ");
		config.put("password", stdin.readLine());
		System.out.print("Please input your merchantId: ");
		config.put("merchantId", stdin.readLine());
		System.out.println("Please choose an environment from the following list (example: 'cert') or directly input another URL:");
		System.out.println("\tsandbox => https://www.testlitle.com/sandbox/communicator/online");
		System.out.println("\tcert => https://cert.litle.com/vap/communicator/online");
		System.out.println("\tprecert => https://precert.litle.com/vap/communicator/online");
		System.out.println("\tproduction => https://payments.litle.com/vap/communicator/online");
		lastUserInput = stdin.readLine();
		config.put("url", (URL_MAP.get(lastUserInput) == null ? lastUserInput : URL_MAP.get(lastUserInput)));
		System.out.print("Please input the proxy host, if no proxy hit enter: ");
		config.put("proxyHost", stdin.readLine());
		System.out.print("Please input the proxy port, if no proxy hit enter: ");
		config.put("proxyPort", stdin.readLine());
		config.put("version", "8.18");
		config.put("timeout", "65");
		config.put("reportGroup", "Default Report Group");
		config.put("printxml", "true");
		
		//These properties are for batch
		System.out.println("Please choose Litle URL from the following list (example: 'batchCert') or directly input another URL:");
		System.out.println("\tbatchSandbox => https://www.testlitle.com/sandbox");
		System.out.println("\tbatchPrecert => https://precert.litle.com");
		System.out.println("\tbatchCert => https://payments.litle.com");
		System.out.println("\tbatchProduction => https://payments.litle.com");
		lastUserInput = stdin.readLine();
		config.put("batchHost", (URL_MAP.get(lastUserInput) == null ? lastUserInput : URL_MAP.get(lastUserInput)));
		//config.put("batchHost", URL_MAP.get(batchHostInput));
		if( URL_MAP.get(lastUserInput) == null ){
			System.out.println("Please input the port for batchHost:");
			config.put("batchPort", stdin.readLine());
		}
		else{
			config.put("batchPort", PORT_MAP.get(lastUserInput));
		}
		
		System.out.print("Please input the batch TCP timeout (leave blank for default (5000000)): ");
		lastUserInput = stdin.readLine();
		config.put("batchTcpTimeout", ((lastUserInput.length() == 0) ? "5000000" : lastUserInput));
		config.put("batchUseSSL", "true");
		config.put("maxAllowedTransactionsPerFile", "500000");
		config.put("maxTransactionsPerBatch", "100000");

		config.store(configFile, "");
		System.out.println("The Litle configuration file has been generated, the file is located at " + file.getAbsolutePath());
	}
}
