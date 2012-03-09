package com.litle.sdk;

import java.io.File;
import java.io.FileInputStream;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.InputStreamRequestEntity;
import org.apache.commons.httpclient.methods.PostMethod;

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
	
	
	public Communication(){
		
	}
	
	public void loadConfig(){
		
	}
    /**
     *
     * Usage:
     * java PostXML http://mywebserver:80/ c:\foo.xml
     *
     * @param args command line arguments
     * Argument 0 is a URL to a web server
     * Argument 1 is a local filename
     *
     */
    public static void main(String[] args) throws Exception {

        if (args.length != 2) {
            System.out.println(
                "Usage: java -classpath <classpath> [-Dorg.apache.commons."+
                "logging.simplelog.defaultlog=<loglevel>]" +
                " PostXML <url> <filename>]");

            System.out.println("<classpath> - must contain the "+
                "commons-httpclient.jar and commons-logging.jar");

            System.out.println("<loglevel> - one of error, "+
                    "warn, info, debug, trace");

            System.out.println("<url> - the URL to post the file to");
            System.out.println("<filename> - file to post to the URL");
            System.out.println();
            System.exit(1);
        }

        // Get target URL
        String strURL = args[0];

        // Get file to be posted
        String strXMLFilename = args[1];
        File input = new File(strXMLFilename);

        // Prepare HTTP post
        PostMethod post = new PostMethod(strURL);

        // Request content will be retrieved directly
        // from the input stream
        // Per default, the request content needs to be buffered
        // in order to determine its length.
        // Request body buffering can be avoided when
        // content length is explicitly specified
        post.setRequestEntity(new InputStreamRequestEntity(
                new FileInputStream(input), input.length()));

        // Specify content type and encoding
        // If content encoding is not explicitly specified
        // ISO-8859-1 is assumed
        post.setRequestHeader(
                "Content-type", "text/xml; charset=ISO-8859-1");

        // Get HTTP client
        HttpClient httpclient = new HttpClient();

        // Execute request
        try {

            int result = httpclient.executeMethod(post);

            // Display status code
            System.out.println("Response status code: " + result);

            // Display response
            System.out.println("Response body: ");
            System.out.println(post.getResponseBodyAsString());

        } finally {
            // Release current connection to the connection pool 
            // once you are done
            post.releaseConnection();
        }
    }
}
