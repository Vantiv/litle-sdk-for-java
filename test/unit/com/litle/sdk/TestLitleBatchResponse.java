package com.litle.sdk;

import static org.junit.Assert.assertEquals;

import java.io.File;

import org.junit.Before;
import org.junit.Test;

import com.litle.sdk.generate.BatchResponse;

public class TestLitleBatchResponse {

	File file;
	
	@Before
	public void before() throws Exception {
	}
	
	@Test
	public void testSetBatchResponse() throws Exception {
		BatchResponse batchResponse = new BatchResponse();
		batchResponse.setId("101");
		batchResponse.setLitleBatchId(562L);
		batchResponse.setMerchantId("101");
		LitleBatchResponse litleBatchResponse = new LitleBatchResponse(batchResponse);
		assertEquals("101", litleBatchResponse.getBatchResponse().getId());
		assertEquals(562L, litleBatchResponse.getBatchResponse().getLitleBatchId());
		assertEquals("101", litleBatchResponse.getBatchResponse().getMerchantId());
	}
	
//	@Test
//	public void testGetNumberOfTransactions() throws Exception {
//		BatchResponse batchResponse = new BatchResponse();
//		batchResponse.setId("101");
//		batchResponse.setLitleBatchId(562L);
//		batchResponse.setMerchantId("101");
//		litleBatchResponse.setBatchResponse(batchResponse);
//		assertEquals(0, litleBatchResponse.getNumberOfTransactions());
//	}
	
}
