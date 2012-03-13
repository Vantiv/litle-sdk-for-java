package com.litle.sdk;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.util.Properties;

import org.apache.cxf.helpers.IOUtils;

public class Setup {

	/**
	 * @param args
	 * @throws IOException 
	 */
	public static void main(String[] args) throws IOException {
		
		Properties config = new Properties();
		PrintStream configFile = new PrintStream(".litle_sdk_config.properties");
		
		BufferedReader stdin = new BufferedReader
	      (new InputStreamReader(System.in));

		System.out.println("Welcome to Litle Java_SDK");
		System.out.println("Please input your user name:");
		String userName = stdin.readLine();
		config.put("username", userName);

		System.out.println(config.get("username"));
		config.list(configFile);

	}

}


//unction initialize(){
//
//	$line = array();
//	$handle = @fopen('./litle_SDK_config.ini', "w");
//	if ($handle) {
//		print "Welcome to Litle PHP_SDK" . PHP_EOL;
//		print "Please input your user name: ";
//		$line['user'] = trim(fgets(STDIN));
//		print "Please input your password: ";
//		$line['password'] = trim(fgets(STDIN));
//		print "Please input your merchantId: ";
//		$line['merchantId'] = trim(fgets(STDIN));
//		print "Please choose Litle url from the following list (example: 'cert') or directly input another URL: \nsandbox => https://www.testlitle.com/sandbox/communicator/online \ncert => https://cert.litle.com/vap/communicator/online \nprecert => https://precert.litle.com/vap/communicator/online \nproduction1 => https://payments.litle.com/vap/communicator/online \nproduction2 => https://payments2.litle.com/vap/communicator/online" . PHP_EOL;
//		$url = urlMapper(trim(fgets(STDIN)));
//		$line['url'] = $url;
//		print "Please input the proxy, if no proxy hit enter key: ";
//		$line['proxy'] = trim(fgets(STDIN));
//
//		foreach ($line as $keys => $values){
//			fwrite($handle, $keys .' = '. $values);
//			fwrite($handle, PHP_EOL);
//		}
//		fwrite($handle, "version = 8.10" .  PHP_EOL);
//		fwrite($handle, "timeout =  65".  PHP_EOL);
//		fwrite($handle, "reportGroup = planets".  PHP_EOL);
//		
//	}
//	fclose($handle);
//	print "The Litle configuration file has been generated, the file is located in the lib directory". PHP_EOL;
//
//}
//
//function urlMapper($litleEnv){
//	$litleOnlineCtx = 'vap/communicator/online';
//	if ($litleEnv == "sandbox")
//		return 'https://www.testlitle.com/sandbox/communicator/online';
//	elseif ($litle_env == "cert")
//		return 'https://cert.litle.com/' . $litleOnlineCtx;
//	elseif ($litleEnv == "precert")
//		return 'https://precert.litle.com/' . $litleOnlineCtx;
//	elseif ($litleEnv == "production1")
//		return 'https://payments.litle.com/' . $litleOnlineCtx;
//	elseif ($litleEnv == "production2")
//		return 'https://payments2.litle.com/' . $litleOnlineCtx;
//	else
//		return 'https://www.testlitle.com/sandbox/communicator/online';
//}