package com.litle.sdk;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.File;
import java.util.Iterator;
import java.util.List;

import javax.xml.bind.JAXBElement;

import org.junit.Before;
import org.junit.Test;

import com.litle.sdk.generate.BatchResponse;
import com.litle.sdk.generate.CardType;
import com.litle.sdk.generate.MethodOfPaymentTypeEnum;
import com.litle.sdk.generate.ObjectFactory;
import com.litle.sdk.generate.OrderSourceType;
import com.litle.sdk.generate.Sale;
import com.litle.sdk.generate.TransactionTypeWithReportGroup;

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
	
	@SuppressWarnings("unchecked")
	@Test
	public void testTransactionTypeIterator_hasNext(){
		List<JAXBElement<? extends TransactionTypeWithReportGroup>> mockList = mock(List.class);
		Iterator<JAXBElement<? extends TransactionTypeWithReportGroup>> mockIterator = mock(Iterator.class);
		
		when(mockList.iterator()).thenReturn(mockIterator);
		
		LitleBatchResponse.TransactionTypeIterator testObj = new LitleBatchResponse.TransactionTypeIterator(mockList); 
		
		when(mockIterator.hasNext()).thenReturn(true);
		assertTrue(testObj.hasNext());
	}
	
	@SuppressWarnings("unchecked")
	@Test
	public void testTransactionTypeIterator_next(){
		List<JAXBElement<? extends TransactionTypeWithReportGroup>> mockList = mock(List.class);
		Iterator<JAXBElement<? extends TransactionTypeWithReportGroup>> mockIterator = mock(Iterator.class);
		
		@SuppressWarnings("rawtypes")
		JAXBElement mockedJaxbElem = mock(JAXBElement.class);
		when(mockList.iterator()).thenReturn(mockIterator);
		when(mockIterator.next()).thenReturn(mockedJaxbElem);
		when(mockedJaxbElem.getValue()).thenReturn(createTestSale(100L, "100"));		
		
		LitleBatchResponse.TransactionTypeIterator testObj = new LitleBatchResponse.TransactionTypeIterator(mockList); 
		
		boolean verified = false;
		TransactionTypeWithReportGroup ttwrg = testObj.next();
		if( ttwrg instanceof Sale ){
			Sale txnFromIterator = (Sale)ttwrg;
			assertEquals(new Long(100), txnFromIterator.getAmount());
			assertEquals(txnFromIterator.getOrderId(), "100");
			verified = true;
		}
		
		assertTrue(verified);
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
