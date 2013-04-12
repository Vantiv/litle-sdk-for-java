package com.litle.sdk;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.matches;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.File;
import java.util.Calendar;
import java.util.List;
import java.util.Properties;

import org.junit.BeforeClass;
import org.junit.Test;

import com.litle.sdk.generate.AuthInformation;
import com.litle.sdk.generate.AuthReversal;
import com.litle.sdk.generate.AuthReversalResponse;
import com.litle.sdk.generate.Authentication;
import com.litle.sdk.generate.Authorization;
import com.litle.sdk.generate.AuthorizationResponse;
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
import com.litle.sdk.generate.MethodOfPaymentTypeEnum;
import com.litle.sdk.generate.OrderSourceType;
import com.litle.sdk.generate.RegisterTokenRequestType;
import com.litle.sdk.generate.RegisterTokenResponse;
import com.litle.sdk.generate.Sale;
import com.litle.sdk.generate.SaleResponse;
import com.litle.sdk.generate.TransactionType;
import com.litle.sdk.generate.TransactionTypeWithReportGroup;

public class TestLitleBatchFileRequest {

	private static LitleBatchFileRequest litleBatchFileRequest;

	@BeforeClass
	public static void beforeClass() throws Exception {
		Properties property = new Properties();
		property.setProperty("username", "PHXMLTEST");
		property.setProperty("password", "password");
		property.setProperty("version", "8.18");
		property.setProperty("maxAllowedTransactionsPerFile", "1000");
		property.setProperty("maxTransactionsPerBatch", "500");
		property.setProperty("batchHost", "l-rraman-ws490");
		property.setProperty("batchPort", "2104");
		property.setProperty("batchTcpTimeout", "10000");
		property.setProperty("batchUseSSL", "false");
		property.setProperty("merchantId", "101");
		litleBatchFileRequest = new LitleBatchFileRequest("testFile", property);
	}
	
	@Test
	public void testEndToEndMerchantBatchSDK() throws Exception {
		LitleBatchRequest litleBatchRequest = litleBatchFileRequest.createBatch("101");
		
		Sale sale = new Sale();
		sale.setAmount(106L);
		sale.setOrderId("12344");
		sale.setOrderSource(OrderSourceType.ECOMMERCE);
		CardType card = new CardType();
		card.setType(MethodOfPaymentTypeEnum.VI);
		card.setNumber("4100000000000002");
		card.setExpDate("1210");
		sale.setCard(card);
		sale.setReportGroup("ding");
		
		litleBatchRequest.addTransaction(sale);
		
		Authorization auth = new Authorization();
		auth.setAmount(200L);
		auth.setOrderId("12345");
		auth.setOrderSource(OrderSourceType.ECOMMERCE);
		CardType card2 = new CardType();
		card2.setType(MethodOfPaymentTypeEnum.VI);
		card2.setNumber("4242424242424242");
		card2.setExpDate("1014");
		auth.setCard(card2);
		auth.setReportGroup("Ramya");
		
		litleBatchRequest.addTransaction(auth);
		
		LitleBatchRequest litleBatchRequest2 = litleBatchFileRequest.createBatch("101");
		
		Sale sale1 = new Sale();
		sale1.setAmount(2500L);
		sale1.setOrderId("12346");
		sale1.setOrderSource(OrderSourceType.ECOMMERCE);
		CardType card3 = new CardType();
		card3.setType(MethodOfPaymentTypeEnum.VI);
		card3.setNumber("4100000000000002");
		card3.setExpDate("1218");
		sale1.setCard(card3);
		sale1.setReportGroup("abc");
		
		litleBatchRequest2.addTransaction(sale1);
		
		Authorization auth1 = new Authorization();
		auth1.setAmount(8900L);
		auth1.setOrderId("12347");
		auth1.setOrderSource(OrderSourceType.ECOMMERCE);
		CardType card4 = new CardType();
		card4.setType(MethodOfPaymentTypeEnum.VI);
		card4.setNumber("4242424242424242");
		card4.setExpDate("1220");
		auth1.setCard(card4);
		auth1.setReportGroup("checking");
		
		litleBatchRequest2.addTransaction(auth1);
		
		LitleBatchFileResponse litleBatchFileResponse = litleBatchFileRequest.sendToLitle();
		assertNotNull(litleBatchFileResponse);
		
		
//      Generic way for accessing the objects
//		List<LitleBatchResponse> batchList = litleBatchFileResponse.getBatchResponseList();
//		
//		for (LitleBatchResponse batch : batchList) {
//			List<TransactionTypeWithReportGroup> txnList = batch.getResponseList();
//			
//			for(TransactionTypeWithReportGroup txn : txnList) {
//				
//				if (txn instanceof SaleResponse) {
//					SaleResponse saleResponse = (SaleResponse) txn;
//					saleResponse.getOrderId();
//					saleResponse.getResponse();
//					saleResponse.getMessage();
//				}
//				else if (txn instanceof AuthorizationResponse) {
//					AuthorizationResponse authResponse = (AuthorizationResponse) txn;
//					authResponse.getOrderId();
//					authResponse.getResponse();
//					authResponse.getMessage();
//				}
//			}
//		}
		LitleBatchResponse litleBatchResponse0 = litleBatchFileResponse.getBatchResponseList().get(0);
		List<TransactionType> txnList0 = litleBatchResponse0.getResponseList();
		
		SaleResponse saleResponse = (SaleResponse) txnList0.get(0);
		assertEquals("12344", saleResponse.getOrderId());
		assertEquals("000",saleResponse.getResponse());
		assertEquals("Approved",saleResponse.getMessage());
		
		AuthorizationResponse authResponse = (AuthorizationResponse) txnList0.get(1);
		assertEquals("12345",authResponse.getOrderId());
		assertEquals("301",authResponse.getResponse());
		assertEquals("Invalid Account Number",authResponse.getMessage());
		
		LitleBatchResponse litleBatchResponse1 = litleBatchFileResponse.getBatchResponseList().get(1);
		List<TransactionType> txnList1 = litleBatchResponse1.getResponseList();
		
		SaleResponse saleResponse1 = (SaleResponse) txnList1.get(0);
		assertEquals("12346", saleResponse1.getOrderId());
		assertEquals("000",saleResponse1.getResponse());
		assertEquals("Approved",saleResponse1.getMessage());
		
		AuthorizationResponse authResponse1 = (AuthorizationResponse) txnList1.get(1);
		assertEquals("12347",authResponse1.getOrderId());
		assertEquals("301",authResponse1.getResponse());
		assertEquals("Invalid Account Number",authResponse1.getMessage());
		
	
//		litleBatchRequest.getBatchRequest().get(0);
	}
	
	@Test
	public void testEmptyCreateBatch() {
		LitleBatchFileRequest mockedLBFR = mock(LitleBatchFileRequest.class);
		
		Properties prpToPass = new Properties();
		prpToPass.setProperty("maxTransactionsPerBatch", "0");
		
		when(mockedLBFR.getConfig()).thenReturn(prpToPass);
		when(mockedLBFR.getNumberOfTransactionInFile()).thenReturn(0);
		when(mockedLBFR.getMaxAllowedTransactionsPerFile()).thenReturn(1);
		
//		LitleBatchRequest litleBatchRequest = litleBatchFileRequest.createBatch("101");
		LitleBatchRequest objToTest = new LitleBatchRequest("101", mockedLBFR);
		try{
			objToTest.addTransaction(null);
		} catch(LitleBatchException e){
			
		}
		objToTest.addTransaction(null);
		//litleBatchFileRequest.sendToLitle();
	}
	
	@Test
	public void testSendFileToIBC() throws Exception {
		File file = new File("/usr/local/litle-home/rraman/Requests/fileToPass1365454351441.xml");
		//String responsePath = "/usr/local/litle-home/rraman/Responses/LitleResponse.xml";
		Communication comm = new Communication();
		Properties config = new Properties();
		config.setProperty("batchHost", "l-rraman-ws490");
		config.setProperty("batchPort", "2104");
		config.setProperty("batchTcpTimeout", "100000");
		config.setProperty("batchUseSSL", "false");
		//LitleBatchFileResponse litleBatchFileResponse = new LitleBatchFileResponse();
		comm.sendLitleBatchFileToIBC(file, config);
	}
}
