package com.litle.sdk;

import static org.junit.Assert.assertEquals;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import org.junit.Before;
import org.junit.Test;

import com.litle.sdk.generate.TransactionTypeWithReportGroup;

public class TestLitleBatchFileResponse {

	private static LitleBatchFileResponse litleBatchFileResponse;
	File file;
	
	@Before
	public void before() throws Exception {
		file = new File("test/unit/responseFolder/testFile.xml");
		litleBatchFileResponse = new LitleBatchFileResponse(file);
	}
	
	@Test
	public void testgetBatchResponseList() throws Exception {
		List<LitleBatchResponse> litleBatchResponseList = new ArrayList<LitleBatchResponse>();
		litleBatchResponseList = litleBatchFileResponse.getBatchResponseList();
	    assertEquals(1, litleBatchResponseList.size());
		LitleBatchResponse batchResponse1 = litleBatchResponseList.get(0);
		assertEquals("101",batchResponse1.getBatchResponse().getMerchantId());
		LitleBatchResponse.TransactionTypeIterator it = batchResponse1.getTransactionResponses();
		TransactionTypeWithReportGroup txn1 = it.next();
		// TODO - verify some txn fields
		TransactionTypeWithReportGroup txn2 = it.next();
	}
	
	@Test
	public void testConstructorWithString() throws Exception {
		List<LitleBatchResponse> litleBatchResponseList = new ArrayList<LitleBatchResponse>();
		String xmlResponseString = new Scanner(new File("test/unit/responseFolder/testFile.xml")).useDelimiter("\\Z").next();
		litleBatchFileResponse = new LitleBatchFileResponse(xmlResponseString);
		litleBatchResponseList = litleBatchFileResponse.getBatchResponseList();
	    assertEquals(1, litleBatchResponseList.size());
	    // TODO - refactor me
		for(LitleBatchResponse list: litleBatchResponseList) {
			assertEquals("101",list.getBatchResponse().getMerchantId());
//			assertEquals(2,list.getNumberOfTransactions());
		}
		
	}
}
