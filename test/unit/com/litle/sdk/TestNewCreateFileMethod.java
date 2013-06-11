package com.litle.sdk;

import java.io.FileNotFoundException;
import java.util.Properties;

import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import com.litle.sdk.generate.CardType;
import com.litle.sdk.generate.MethodOfPaymentTypeEnum;
import com.litle.sdk.generate.OrderSourceType;
import com.litle.sdk.generate.Sale;

public class TestNewCreateFileMethod {

	private  LitleBatchRequest litleBatchRequest;
	private  LitleBatchFileRequest litleBatchFileRequest;

	@Before
	public void before() throws Exception {
		Properties property = new Properties();
		property.setProperty("username", "PHXMLTEST");
		property.setProperty("password", "password");
		property.setProperty("version", "8.18");
		property.setProperty("maxAllowedTransactionsPerFile", "1000");
		property.setProperty("maxTransactionsPerBatch", "500");
		property.setProperty("batchHost", "localhost");
		property.setProperty("batchPort", "2104");
		property.setProperty("batchTcpTimeout", "10000");
		property.setProperty("batchUseSSL", "false");
		property.setProperty("merchantId", "101");
		property.setProperty("proxyHost", "");
		property.setProperty("proxyPort", "");
		property.setProperty("reportGroup", "test");
		property.setProperty("batchRequestFolder", "test/unit/");
		property.setProperty("batchResponseFolder", "test/unit/");

	}

	@Test
	public void testAddAFileToTestNewFileMethod() throws FileNotFoundException, JAXBException {
		litleBatchFileRequest = new LitleBatchFileRequest("testFile.xml");
		litleBatchRequest = litleBatchFileRequest.createBatch("101");
		//litleBatchRequest = new LitleBatchRequest("101", new LitleBatchFileRequest(""));
		Sale sale = new Sale();
		sale.setAmount(100L);
		sale.setOrderId("256");
		sale.setOrderSource(OrderSourceType.ECOMMERCE);
		CardType card = new CardType();
		card.setType(MethodOfPaymentTypeEnum.VI);
		card.setNumber("4100000000000002");
		card.setExpDate("1210");
		sale.setCard(card);
		sale.setReportGroup("test");
		litleBatchRequest.setNumOfTxn(1);
        Marshaller mockMarshaller = Mockito.mock(Marshaller.class);
        litleBatchRequest.setMarshaller(mockMarshaller);


		litleBatchRequest.addTransaction(sale);

		Sale sale1 = new Sale();
		sale1.setAmount(101L);
		sale1.setOrderId("257");
		sale1.setOrderSource(OrderSourceType.ECOMMERCE);
		CardType card1 = new CardType();
		card1.setType(MethodOfPaymentTypeEnum.VI);
		card1.setNumber("4100000000000002");
		card1.setExpDate("1211");
		sale1.setCard(card1);
		sale1.setReportGroup("test1");
		litleBatchRequest.addTransaction(sale1);

		//litleBatchRequest = new LitleBatchRequest("102", new LitleBatchFileRequest(""));
		litleBatchRequest = litleBatchFileRequest.createBatch("101");
		Sale sale3 = new Sale();
		sale3.setAmount(100L);
		sale3.setOrderId("256");
		sale3.setOrderSource(OrderSourceType.ECOMMERCE);
		CardType card3 = new CardType();
		card3.setType(MethodOfPaymentTypeEnum.VI);
		card3.setNumber("4100000000000002");
		card3.setExpDate("1210");
		sale3.setCard(card3);
		sale3.setReportGroup("test");
		litleBatchRequest.addTransaction(sale3);
		litleBatchFileRequest.sendToLitle();
	}
}
