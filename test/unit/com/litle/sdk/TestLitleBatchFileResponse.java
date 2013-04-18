package com.litle.sdk;

import static org.junit.Assert.assertEquals;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

public class TestLitleBatchFileResponse {

	private static LitleBatchFileResponse litleBatchFileResponse;

	@Before
	public void before() throws Exception {
		File file = new File("testSendToLitleReturnFile.txt");
		litleBatchFileResponse = new LitleBatchFileResponse(file);
	}
	
	@Test
	public void testgetBatchResponseList() throws Exception {
		 
		List<LitleBatchResponse> litleBatchResponseList = new ArrayList<LitleBatchResponse>();
		litleBatchResponseList = litleBatchFileResponse.getBatchResponseList();
		
		for(LitleBatchResponse list: litleBatchResponseList) {
			assertEquals("101",list.getBatchResponse().getMerchantId());
		}

	}
}
