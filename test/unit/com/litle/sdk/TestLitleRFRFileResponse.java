package com.litle.sdk;

import static org.junit.Assert.assertEquals;

import java.io.File;

import org.junit.Before;
import org.junit.Test;

public class TestLitleRFRFileResponse {

	private static LitleRFRFileResponse litleRFRFileResponse;
	File file;

	@Before
	public void before() throws Exception {
		file = new File("test/unit/responseFolder/testParseResponseFile.xml");
		litleRFRFileResponse = new LitleRFRFileResponse(file);
	}

	@Test
	public void testConstructorForException() {
		file = new File("test/unit/responseFolder/testEmptyFile.xml");
		try {
			litleRFRFileResponse = new LitleRFRFileResponse(file);
		} catch(LitleBatchException e) {
			assertEquals("There was an exception while unmarshalling the response file. Check your JAXB dependencies.", e.getMessage());
		}

		file = new File("test/unit/responseFolder/File.xml");
		try {
			litleRFRFileResponse = new LitleRFRFileResponse(file);
		} catch(Exception e) {
			assertEquals("There was an exception while reading the Litle response file. The response file might not have been generated. Try re-sending the request file or contact us.", e.getMessage());
		}
	}


	@Test
	public void testGetLitleResponseAttributes() {
		assertEquals(82822223274065939L, litleRFRFileResponse.getLitleSessionId());
		assertEquals("8.18", litleRFRFileResponse.getVersion());
		assertEquals("0", litleRFRFileResponse.getResponse());
		assertEquals("Valid Format", litleRFRFileResponse.getMessage());
	}
}
