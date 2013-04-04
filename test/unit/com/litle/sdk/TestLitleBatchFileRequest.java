package com.litle.sdk;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.matches;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Calendar;
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

public class TestLitleBatchFileRequest {

	private static LitleBatchFileRequest litleBatchFileRequest;

	@BeforeClass
	public static void beforeClass() throws Exception {
		litleBatchFileRequest = new LitleBatchFileRequest("testFile.xml");
	}
	
	@Test
	public void testCreateBatch() {
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
		
		litleBatchRequest.addTransaction(sale);
		
		Authorization sale2 = new Authorization();
		sale2.setAmount(106L);
		sale2.setOrderId("12344");
		sale2.setOrderSource(OrderSourceType.ECOMMERCE);
		CardType card2 = new CardType();
		card2.setType(MethodOfPaymentTypeEnum.VI);
		card2.setNumber("4242424242424242");
		card2.setExpDate("1210");
		sale2.setCard(card2);
		
		litleBatchRequest.addTransaction(sale2);
		
		LitleBatchRequest litleBatchRequest2 = litleBatchFileRequest.createBatch("102");
		
		Sale sale3 = new Sale();
		sale3.setAmount(106L);
		sale3.setOrderId("12344");
		sale3.setOrderSource(OrderSourceType.ECOMMERCE);
		CardType card3 = new CardType();
		card3.setType(MethodOfPaymentTypeEnum.VI);
		card3.setNumber("4100000000000002");
		card3.setExpDate("1210");
		sale3.setCard(card3);
		
		litleBatchRequest2.addTransaction(sale3);
		
		Authorization sale4 = new Authorization();
		sale4.setAmount(106L);
		sale4.setOrderId("12344");
		sale4.setOrderSource(OrderSourceType.ECOMMERCE);
		CardType card4 = new CardType();
		card4.setType(MethodOfPaymentTypeEnum.VI);
		card4.setNumber("4242424242424242");
		card4.setExpDate("1210");
		sale4.setCard(card4);
		
		litleBatchRequest2.addTransaction(sale4);
		
		
		litleBatchFileRequest.sendToLitle();
	}

}
