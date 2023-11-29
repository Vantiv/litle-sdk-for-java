package io.github.vantiv.sdk;

import static org.junit.Assert.assertEquals;

import java.io.File;

import org.junit.Before;
import org.junit.Test;

import io.github.vantiv.sdk.generate.*;

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

	public Sale createTestSale(Long amount, String orderId){
		Sale sale = new Sale();
		sale.setAmount(amount);
		sale.setOrderId(orderId);
		sale.setOrderSource(OrderSourceType.ECOMMERCE);
		CardType card = new CardType();
		card.setType(MethodOfPaymentTypeEnum.VI);
		card.setNumber("4100000000000002");
		card.setExpDate("1210");
		sale.setCard(card);
		sale.setReportGroup("test");
		return sale;
	}


}
