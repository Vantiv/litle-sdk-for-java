package com.litle.sdk;

import static org.junit.Assert.*;

import java.io.File;

import org.junit.BeforeClass;
import org.junit.Test;



public class TestResponseFileParser {

	private static ResponseFileParser responseFileParser;

	@BeforeClass
	public static void beforeClass() throws Exception {
		File fileToReturn = new File("test/unit/responseFolder/testfile.xml");
		responseFileParser = new ResponseFileParser(fileToReturn);
	}
	
	@Test
    public void testgetNextTag() throws Exception {
       
		String retString = responseFileParser.getNextTag("litleResponse");
		String outputString = "<litleResponse version=\"8.18\" xmlns=\"http://www.litle.com/schema\" response=\"0\" message=\"Valid Format\" litleSessionId=\"82822223274065939\"></litleResponse>";
        assertEquals (outputString,retString);
        retString = responseFileParser.getNextTag("batchResponse");
        outputString = "<batchResponse litleBatchId=\"82822223274065947\" merchantId=\"101\"></batchResponse>";
        assertEquals(outputString,retString);
        retString = responseFileParser.getNextTag("transactionResponse");
        System.out.println(retString);
        retString = responseFileParser.getNextTag("batchResponse");
        outputString = "<batchResponse litleBatchId=\"82822223274065954\" merchantId=\"101\"></batchResponse>";
        assertEquals(outputString,retString);
        retString = responseFileParser.getNextTag("transactionResponse");
        System.out.println("\n\n\n");
        System.out.println(retString);
        
        //TODO Check the values returned for saleResponse and AuthorizationResponse
    }
	
	@Test
	public void testOkToStartRecordingString() {
		assertFalse(responseFileParser.okToStartRecordingString("litleResponse", "saleResponse"));
		assertTrue(responseFileParser.okToStartRecordingString("SaleResponse", "saleResponse"));
	}
	
	@Test
	public void testokToStopRecordingString() {
		//assertTrue(responseFileParser.okToStopRecordingString("saleResponse", "</saleResponse>"));
		assertFalse(responseFileParser.okToStopRecordingString("litleResponse", "saleResponse"));
		assertFalse(responseFileParser.okToStopRecordingString("SaleResponse", "<saleResponse>"));
	}
}
