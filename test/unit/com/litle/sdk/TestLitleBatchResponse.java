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

import com.litle.sdk.generate.BatchResponse;

public class TestLitleBatchResponse {

	private static LitleBatchResponse litleBatchResponse;
	File file;
	
	@Before
	public void before() throws Exception {
		litleBatchResponse = new LitleBatchResponse();
	}
	
	@Test
	public void testSetBatchResponse() throws Exception {
		BatchResponse batchResponse = new BatchResponse();
		batchResponse.setId("101");
		batchResponse.setLitleBatchId(562L);
		batchResponse.setMerchantId("101");
		litleBatchResponse.setBatchResponse(batchResponse);
		assertEquals("101", litleBatchResponse.getBatchResponse().getId());
		assertEquals(562L, litleBatchResponse.getBatchResponse().getLitleBatchId());
		assertEquals("101", litleBatchResponse.getBatchResponse().getMerchantId());
	}
	
	@Test
	public void testGetNumberOfTransactions() throws Exception {
		BatchResponse batchResponse = new BatchResponse();
		batchResponse.setId("101");
		batchResponse.setLitleBatchId(562L);
		batchResponse.setMerchantId("101");
		litleBatchResponse.setBatchResponse(batchResponse);
		assertEquals(0, litleBatchResponse.getNumberOfTransactions());
	}
	
}
