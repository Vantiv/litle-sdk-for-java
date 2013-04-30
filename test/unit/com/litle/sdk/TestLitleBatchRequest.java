package com.litle.sdk;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.matches;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Properties;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.litle.sdk.generate.AuthInformation;
import com.litle.sdk.generate.AuthReversal;
import com.litle.sdk.generate.AuthReversalResponse;
import com.litle.sdk.generate.Authentication;
import com.litle.sdk.generate.Authorization;
import com.litle.sdk.generate.AuthorizationResponse;
import com.litle.sdk.generate.BatchRequest;
import com.litle.sdk.generate.Capture;
import com.litle.sdk.generate.CaptureGivenAuth;
import com.litle.sdk.generate.CaptureGivenAuthResponse;
import com.litle.sdk.generate.CaptureResponse;
import com.litle.sdk.generate.CardType;
import com.litle.sdk.generate.Contact;
import com.litle.sdk.generate.Credit;
import com.litle.sdk.generate.CreditResponse;
import com.litle.sdk.generate.CustomerInfo;
import com.litle.sdk.generate.EcheckAccountTypeEnum;
import com.litle.sdk.generate.EcheckCredit;
import com.litle.sdk.generate.EcheckCreditResponse;
import com.litle.sdk.generate.EcheckRedeposit;
import com.litle.sdk.generate.EcheckRedepositResponse;
import com.litle.sdk.generate.EcheckSale;
import com.litle.sdk.generate.EcheckSalesResponse;
import com.litle.sdk.generate.EcheckType;
import com.litle.sdk.generate.EcheckVerification;
import com.litle.sdk.generate.EcheckVerificationResponse;
import com.litle.sdk.generate.EcheckVoid;
import com.litle.sdk.generate.EcheckVoidResponse;
import com.litle.sdk.generate.ForceCapture;
import com.litle.sdk.generate.ForceCaptureResponse;
import com.litle.sdk.generate.LitleOnlineRequest;
import com.litle.sdk.generate.LitleRequest;
import com.litle.sdk.generate.MethodOfPaymentTypeEnum;
import com.litle.sdk.generate.ObjectFactory;
import com.litle.sdk.generate.OrderSourceType;
import com.litle.sdk.generate.RegisterTokenRequestType;
import com.litle.sdk.generate.RegisterTokenResponse;
import com.litle.sdk.generate.Sale;
import com.litle.sdk.generate.SaleResponse;
import com.litle.sdk.generate.TransactionType;
import com.litle.sdk.generate.TransactionTypeWithReportGroup;

public class TestLitleBatchRequest {

	private static LitleBatchFileRequest litleBatchFileRequest;
	private static LitleBatchRequest litleBatchRequest;

	@Before
	public void before() throws Exception {
		Properties property = new Properties();
		property.setProperty("username", "PHXMLTEST");
		property.setProperty("password", "password");
		property.setProperty("version", "8.18");
		property.setProperty("maxAllowedTransactionsPerFile", "5");
		property.setProperty("maxTransactionsPerBatch", "3");
		property.setProperty("batchHost", "localhost");
		property.setProperty("batchPort", "2104");
		property.setProperty("batchTcpTimeout", "10000");
		property.setProperty("batchUseSSL", "false");
		property.setProperty("merchantId", "101");
		litleBatchFileRequest = new LitleBatchFileRequest("testFile", property);
		
		litleBatchRequest = litleBatchFileRequest.createBatch("101");
	}
	
	@Test
	public void testGetNumberOfTransactions() throws FileNotFoundException, JAXBException{
		assertEquals(0, litleBatchRequest.getNumberOfTransactions());
		litleBatchRequest.addTransaction(createTestSale(100L, "100"));
		assertEquals(1, litleBatchRequest.getNumberOfTransactions());
	}
	
	@Test
	public void testIsFull() throws FileNotFoundException, JAXBException{
		assertTrue(!litleBatchRequest.isFull());
		litleBatchRequest.addTransaction(createTestSale(100L, "100"));
		litleBatchRequest.addTransaction(createTestSale(100L, "100"));
		litleBatchRequest.addTransaction(createTestSale(100L, "100"));
		assertTrue(litleBatchRequest.isFull());
	}
	
	@Test
	public void testVerifyFileThresholds() throws FileNotFoundException, JAXBException{

		litleBatchRequest.addTransaction(createTestSale(100L, "100"));
		litleBatchRequest.addTransaction(createTestSale(100L, "100"));
		assertEquals(litleBatchRequest.verifyFileThresholds(), TransactionCodeEnum.SUCCESS);
		litleBatchRequest.addTransaction(createTestSale(100L, "100"));
		assertEquals(litleBatchRequest.verifyFileThresholds(), TransactionCodeEnum.BATCHFULL);
		
		LitleBatchRequest batchRequest2 = litleBatchFileRequest.createBatch("102");
		batchRequest2.addTransaction(createTestSale(100L, "100"));
		batchRequest2.addTransaction(createTestSale(100L, "100"));
		assertEquals(litleBatchRequest.verifyFileThresholds(), TransactionCodeEnum.FILEFULL);
	}
	
	@Test
	public void testAddTransaction() throws FileNotFoundException, JAXBException{
		assertEquals(litleBatchRequest.addTransaction(createTestSale(100L, "100")), TransactionCodeEnum.SUCCESS);
		assertEquals(litleBatchRequest.addTransaction(createTestSale(100L, "100")), TransactionCodeEnum.SUCCESS);
		assertEquals(litleBatchRequest.addTransaction(createTestSale(100L, "100")), TransactionCodeEnum.BATCHFULL);
		
		boolean batchFullException = false;
		try{
			litleBatchRequest.addTransaction(createTestSale(100L, "100"));
		} catch (LitleBatchException e){
			batchFullException = true;
		}
		
		assertTrue(batchFullException);
		
		LitleBatchRequest batchRequest2 = litleBatchFileRequest.createBatch("102");
		assertEquals(batchRequest2.addTransaction(createTestSale(100L, "100")),TransactionCodeEnum.SUCCESS);
		assertEquals(batchRequest2.addTransaction(createTestSale(100L, "100")),TransactionCodeEnum.FILEFULL);
		
		boolean fileFullException = false;
		try{
			batchRequest2.addTransaction(createTestSale(100L, "100"));
		} catch (LitleBatchException e){
			fileFullException = true;
		}
		
		assertTrue(fileFullException);
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
