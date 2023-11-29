package io.github.vantiv.sdk;

import static org.junit.Assert.assertEquals;

import java.util.Calendar;

import org.junit.BeforeClass;
import org.junit.Test;

import io.github.vantiv.sdk.generate.AuthInformation;
import io.github.vantiv.sdk.generate.CaptureGivenAuth;
import io.github.vantiv.sdk.generate.CaptureGivenAuthResponse;
import io.github.vantiv.sdk.generate.CardTokenType;
import io.github.vantiv.sdk.generate.CardType;
import io.github.vantiv.sdk.generate.Contact;
import io.github.vantiv.sdk.generate.FraudResult;
import io.github.vantiv.sdk.generate.MethodOfPaymentTypeEnum;
import io.github.vantiv.sdk.generate.OrderSourceType;
import io.github.vantiv.sdk.generate.ProcessingInstructions;
import io.github.vantiv.sdk.generate.ProcessingTypeEnum;


public class TestCaptureGivenAuth {

	private static LitleOnline litle;

	@BeforeClass
	public static void beforeClass() throws Exception {
		litle = new LitleOnline();
	}

	@Test
	public void simpleCaptureGivenAuthWithCard() throws Exception{
		CaptureGivenAuth capturegivenauth = new CaptureGivenAuth();
		capturegivenauth.setAmount(106L);
		capturegivenauth.setOrderId("12344");
		AuthInformation authInfo = new AuthInformation();
		Calendar authDate = Calendar.getInstance();
		authDate.set(2002, Calendar.OCTOBER, 9);
		authInfo.setAuthDate(authDate);
		authInfo.setAuthCode("543216");
		authInfo.setAuthAmount(12345L);
		capturegivenauth.setAuthInformation(authInfo);
		capturegivenauth.setOrderSource(OrderSourceType.ECOMMERCE);
		CardType card = new CardType();
		card.setType(MethodOfPaymentTypeEnum.VI);
		card.setNumber("4100000000000000");
		card.setExpDate("1210");
		capturegivenauth.setCard(card);
		CaptureGivenAuthResponse response = litle.captureGivenAuth(capturegivenauth);
		assertEquals("Approved", response.getMessage());
	}

	@Test
	public void simpleCaptureGivenAuthWithToken() throws Exception{
		CaptureGivenAuth capturegivenauth = new CaptureGivenAuth();
		capturegivenauth.setAmount(106L);
		capturegivenauth.setOrderId("12344");
		AuthInformation authInfo = new AuthInformation();
		Calendar authDate = Calendar.getInstance();
		authDate.set(2002, Calendar.OCTOBER, 9);
		authInfo.setAuthDate(authDate);
		authInfo.setAuthCode("543216");
		authInfo.setAuthAmount(12345L);
		capturegivenauth.setAuthInformation(authInfo);
		capturegivenauth.setOrderSource(OrderSourceType.ECOMMERCE);
		CardTokenType cardtoken = new CardTokenType();
		cardtoken.setLitleToken("123456789101112");
		cardtoken.setExpDate("1210");
		cardtoken.setCardValidationNum("555");
		cardtoken.setType(MethodOfPaymentTypeEnum.VI);
		capturegivenauth.setToken(cardtoken);
		CaptureGivenAuthResponse response = litle.captureGivenAuth(capturegivenauth);
		assertEquals("Approved", response.getMessage());
	}

	@Test
	public void complexCaptureGivenAuth() throws Exception{
		CaptureGivenAuth capturegivenauth = new CaptureGivenAuth();
		capturegivenauth.setAmount(106L);
		capturegivenauth.setOrderId("12344");
		AuthInformation authInfo = new AuthInformation();
		Calendar authDate = Calendar.getInstance();
		authDate.set(2002, Calendar.OCTOBER, 9);
		authInfo.setAuthDate(authDate);
		authInfo.setAuthCode("543216");
		authInfo.setAuthAmount(12345L);
		capturegivenauth.setAuthInformation(authInfo);
		Contact contact = new Contact();
		contact.setName("Bob");
		contact.setCity("lowell");
		contact.setState("MA");
		contact.setEmail("litle.com");
		capturegivenauth.setBillToAddress(contact);
		ProcessingInstructions processinginstructions = new ProcessingInstructions();
		processinginstructions.setBypassVelocityCheck(true);
		capturegivenauth.setProcessingInstructions(processinginstructions);
		capturegivenauth.setOrderSource(OrderSourceType.ECOMMERCE);
		CardType card = new CardType();
		card.setType(MethodOfPaymentTypeEnum.VI);
		card.setNumber("4100000000000000");
		card.setExpDate("1210");
		capturegivenauth.setCard(card);
		CaptureGivenAuthResponse response = litle.captureGivenAuth(capturegivenauth);
		assertEquals("Approved", response.getMessage());
	}
	@Test
	public void authInfo() throws Exception{
		CaptureGivenAuth capturegivenauth = new CaptureGivenAuth();
		capturegivenauth.setAmount(106L);
		capturegivenauth.setOrderId("12344");
		AuthInformation authInfo = new AuthInformation();
		Calendar authDate = Calendar.getInstance();
		authDate.set(2002, Calendar.OCTOBER, 9);
		authInfo.setAuthDate(authDate);
		authInfo.setAuthCode("543216");
		authInfo.setAuthAmount(12345L);
		FraudResult fraudresult = new FraudResult();
		fraudresult.setAvsResult("12");
		fraudresult.setCardValidationResult("123");
		fraudresult.setAuthenticationResult("1");
		fraudresult.setAdvancedAVSResult("123");
		authInfo.setFraudResult(fraudresult);
		capturegivenauth.setAuthInformation(authInfo);
		capturegivenauth.setOrderSource(OrderSourceType.ECOMMERCE);
		CardType card = new CardType();
		card.setType(MethodOfPaymentTypeEnum.VI);
		card.setNumber("4100000000000000");
		card.setExpDate("1210");
		capturegivenauth.setCard(card);
		CaptureGivenAuthResponse response = litle.captureGivenAuth(capturegivenauth);
		assertEquals("Approved", response.getMessage());
	}

	@Test
    public void simpleCaptureGivenAuthWithSecondaryAmount() throws Exception{
        CaptureGivenAuth capturegivenauth = new CaptureGivenAuth();
        capturegivenauth.setAmount(106L);
        capturegivenauth.setOrderId("12344");
        capturegivenauth.setSecondaryAmount(50L);
        AuthInformation authInfo = new AuthInformation();
        Calendar authDate = Calendar.getInstance();
        authDate.set(2002, Calendar.OCTOBER, 9);
        authInfo.setAuthDate(authDate);
        authInfo.setAuthCode("543216");
        authInfo.setAuthAmount(12345L);
        capturegivenauth.setAuthInformation(authInfo);
        capturegivenauth.setOrderSource(OrderSourceType.ECOMMERCE);
        CardType card = new CardType();
        card.setType(MethodOfPaymentTypeEnum.VI);
        card.setNumber("4100000000000000");
        card.setExpDate("1210");
        capturegivenauth.setCard(card);
        CaptureGivenAuthResponse response = litle.captureGivenAuth(capturegivenauth);
        assertEquals("Approved", response.getMessage());
    }
	
	@Test
	public void testCaptureGivenAuthWithProcessingType() throws Exception{
		CaptureGivenAuth capturegivenauth = new CaptureGivenAuth();
		capturegivenauth.setAmount(106L);
		capturegivenauth.setOrderId("12344");
		AuthInformation authInfo = new AuthInformation();
		Calendar authDate = Calendar.getInstance();
		authDate.set(2002, Calendar.OCTOBER, 9);
		authInfo.setAuthDate(authDate);
		authInfo.setAuthCode("543216");
		authInfo.setAuthAmount(12345L);
		capturegivenauth.setAuthInformation(authInfo);
		capturegivenauth.setOrderSource(OrderSourceType.ECOMMERCE);
		capturegivenauth.setProcessingType(ProcessingTypeEnum.INITIAL_RECURRING);
		CardType card = new CardType();
		card.setType(MethodOfPaymentTypeEnum.VI);
		card.setNumber("4100000000000000");
		card.setExpDate("1210");
		capturegivenauth.setCard(card);
		CaptureGivenAuthResponse response = litle.captureGivenAuth(capturegivenauth);
		assertEquals("Approved", response.getMessage());
	}

	@Test
	public void testCaptureGivenAuthWithProcessingTypeCOF() throws Exception{
		CaptureGivenAuth capturegivenauth = new CaptureGivenAuth();
		capturegivenauth.setAmount(106L);
		capturegivenauth.setOrderId("12344");
		capturegivenauth.setProcessingType(ProcessingTypeEnum.INITIAL_COF);
		AuthInformation authInfo = new AuthInformation();
		Calendar authDate = Calendar.getInstance();
		authDate.set(2002, Calendar.OCTOBER, 9);
		authInfo.setAuthDate(authDate);
		authInfo.setAuthCode("543216");
		authInfo.setAuthAmount(12345L);
		capturegivenauth.setAuthInformation(authInfo);
		capturegivenauth.setOrderSource(OrderSourceType.ECOMMERCE);
		CardType card = new CardType();
		card.setType(MethodOfPaymentTypeEnum.VI);
		card.setNumber("4100000000000000");
		card.setExpDate("1210");
		capturegivenauth.setCard(card);
		capturegivenauth.setId("id");
		CaptureGivenAuthResponse response = litle.captureGivenAuth(capturegivenauth);
		assertEquals("Approved", response.getMessage());

		capturegivenauth.setProcessingType(ProcessingTypeEnum.MERCHANT_INITIATED_COF);
		response = litle.captureGivenAuth(capturegivenauth);
		assertEquals("Approved", response.getMessage());

		capturegivenauth.setProcessingType(ProcessingTypeEnum.CARDHOLDER_INITIATED_COF);
		response = litle.captureGivenAuth(capturegivenauth);
		assertEquals("Approved", response.getMessage());
	}
	
	@Test
	public void testCaptureGivenAuthWithOrigNetworkTxnIdandAmount() throws Exception{
		CaptureGivenAuth capturegivenauth = new CaptureGivenAuth();
		capturegivenauth.setAmount(106L);
		capturegivenauth.setOrderId("12344");
		AuthInformation authInfo = new AuthInformation();
		Calendar authDate = Calendar.getInstance();
		authDate.set(2002, Calendar.OCTOBER, 9);
		authInfo.setAuthDate(authDate);
		authInfo.setAuthCode("543216");
		authInfo.setAuthAmount(12345L);
		capturegivenauth.setAuthInformation(authInfo);
		capturegivenauth.setOrderSource(OrderSourceType.ECOMMERCE);
		capturegivenauth.setOriginalNetworkTransactionId("1212124545787");
		capturegivenauth.setOriginalTransactionAmount(9966l);
		CardType card = new CardType();
		card.setType(MethodOfPaymentTypeEnum.VI);
		card.setNumber("4100000000000000");
		card.setExpDate("1210");
		capturegivenauth.setCard(card);
		CaptureGivenAuthResponse response = litle.captureGivenAuth(capturegivenauth);
		assertEquals("Approved", response.getMessage());
	}

}

