package com.litle.sdk;

import static org.junit.Assert.assertEquals;
import java.io.File;
import org.junit.Before;
import org.junit.Test;

public class TestLitleBatchFileResponse {

	private static LitleBatchFileResponse litleBatchFileResponse;
	File file;
	
	@Before
	public void before() throws Exception {
		file = new File("test/unit/responseFolder/testParseResponseFile.xml");
		litleBatchFileResponse = new LitleBatchFileResponse(file);
	}
		
	@Test
	public void testConstructorForException() {
		file = new File("test/unit/responseFolder/testEmptyFile.xml");
		try {
			litleBatchFileResponse = new LitleBatchFileResponse(file);
		} catch(LitleBatchException e) {
			assertEquals("There was an exception while unmarshalling the response file. Check your JAXB dependencies.", e.getMessage());
		}
		
		file = new File("test/unit/responseFolder/File.xml");
		try {
			litleBatchFileResponse = new LitleBatchFileResponse(file);
		} catch(Exception e) {
			assertEquals("There was an exception while reading the Litle response file. The response file might not have been generated. Try re-sending the request file or contact us.", e.getMessage());
		}
	}
	
	@Test
	public void testGetNextLitleBatchResponse() {
		LitleBatchResponse litleBatchResponse = litleBatchFileResponse.getNextLitleBatchResponse();
		assertEquals(litleBatchResponse.getMerchantId(), "101");
	}
	
	@Test
	public void testGetLitleResponseAttributes() {
		assertEquals(82822223274065939L, litleBatchFileResponse.getLitleSessionId());
		assertEquals("8.18", litleBatchFileResponse.getVersion());
		assertEquals("0", litleBatchFileResponse.getResponse());
		assertEquals("Valid Format", litleBatchFileResponse.getMessage());
	}
}
