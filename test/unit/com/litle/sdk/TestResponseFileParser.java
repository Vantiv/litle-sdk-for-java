package com.litle.sdk;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.File;

import org.junit.BeforeClass;
import org.junit.Test;



public class TestResponseFileParser {

	private static ResponseFileParser responseFileParser;

	@BeforeClass
	public static void beforeClass() throws Exception {
		File fileToReturn = new File("test/unit/testParseResponseFile.xml");
		responseFileParser = new ResponseFileParser(fileToReturn);
	}

	@Test
    public void testgetNextTag() throws Exception {

		String retString = responseFileParser.getNextTag("litleResponse");
		String outputString = "<litleResponse version=\"8.18\" xmlns=\"http://www.litle.com/schema\" response=\"0\" message=\"Valid Format\" litleSessionId=\"82822223274065939\"></litleResponse>";
        assertEquals (outputString,retString);
        retString = responseFileParser.getNextTag("batchResponse");
        outputString = "<batchResponse xmlns=\"http://www.litle.com/schema\" litleBatchId=\"82822223274065947\" merchantId=\"101\"></batchResponse>";
        assertEquals(outputString,retString);
		retString = responseFileParser.getNextTag("transactionResponse");
        retString = responseFileParser.getNextTag("transactionResponse");
        //verifyException - The test response file has only 2 transactions in the first batch
        try {
        	retString = responseFileParser.getNextTag("transactionResponse");
        }catch(Exception e) {
        	assertEquals("All payments in this batch have already been retrieved.", e.getMessage());
        }

        retString = responseFileParser.getNextTag("batchResponse");
        outputString = "<batchResponse xmlns=\"http://www.litle.com/schema\" litleBatchId=\"82822223274065954\" merchantId=\"101\"></batchResponse>";
        assertEquals(outputString,retString);
        retString = responseFileParser.getNextTag("transactionResponse");

    }

	@Test
	public void testOkToStartRecordingString() {
		assertFalse(responseFileParser.okToStartRecordingString("litleResponse", "saleResponse"));
		assertTrue(responseFileParser.okToStartRecordingString("SaleResponse", "saleResponse"));
		assertTrue(responseFileParser.okToStartRecordingString("<transactionResponse", "<saleResponse"));
	}

	@Test
	public void testokToStopRecordingString() {
		assertTrue(responseFileParser.okToStopRecordingString("</transactionResponse>", "</saleResponse>"));
		assertTrue(responseFileParser.okToStopRecordingString("</saleResponse>", "</saleResponse>"));
		assertFalse(responseFileParser.okToStopRecordingString("<transactionResponse>", "</saleResponse>"));
		assertFalse(responseFileParser.okToStopRecordingString("litleResponse", "saleResponse"));
		assertFalse(responseFileParser.okToStopRecordingString("SaleResponse", "<saleResponse>"));
	}
}
