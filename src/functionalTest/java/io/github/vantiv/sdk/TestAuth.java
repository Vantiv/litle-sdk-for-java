package io.github.vantiv.sdk;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.junit.BeforeClass;
import org.junit.Test;

import io.github.vantiv.sdk.generate.ApplepayHeaderType;
import io.github.vantiv.sdk.generate.ApplepayType;
import io.github.vantiv.sdk.generate.Authorization;
import io.github.vantiv.sdk.generate.AuthorizationResponse;
import io.github.vantiv.sdk.generate.CardType;
import io.github.vantiv.sdk.generate.Contact;
import io.github.vantiv.sdk.generate.DetailTax;
import io.github.vantiv.sdk.generate.EnhancedData;
import io.github.vantiv.sdk.generate.MethodOfPaymentTypeEnum;
import io.github.vantiv.sdk.generate.OrderSourceType;
import io.github.vantiv.sdk.generate.PayPal;
import io.github.vantiv.sdk.generate.Pos;
import io.github.vantiv.sdk.generate.PosCapabilityTypeEnum;
import io.github.vantiv.sdk.generate.PosCardholderIdTypeEnum;
import io.github.vantiv.sdk.generate.PosEntryModeTypeEnum;
import io.github.vantiv.sdk.generate.ProcessingTypeEnum;

public class TestAuth {

	private static LitleOnline litle;

	@BeforeClass
	public static void beforeClass() throws Exception {
		litle = new LitleOnline();
	}

	@Test
	public void simpleAuthWithCard() throws Exception {
		Authorization authorization = new Authorization();
		authorization.setReportGroup("Planets");
		authorization.setOrderId("12344");
		authorization.setAmount(106L);
		authorization.setOrderSource(OrderSourceType.ECOMMERCE);
		CardType card = new CardType();
		card.setType(MethodOfPaymentTypeEnum.VI);
		card.setNumber("4100000000000000");
		card.setExpDate("1210");
		authorization.setCard(card);

		AuthorizationResponse response = litle.authorize(authorization);
		assertEquals(response.getMessage(), "000",response.getResponse());
	}

	@Test
	public void simpleAuthWithPaypal() throws Exception {
		Authorization authorization = new Authorization();
		authorization.setReportGroup("Planets");
		authorization.setOrderId("123456");
		authorization.setAmount(106L);
		authorization.setOrderSource(OrderSourceType.ECOMMERCE);
		PayPal paypal = new PayPal();
		paypal.setPayerId("1234");
		paypal.setToken("1234");
		paypal.setTransactionId("123456");
		authorization.setPaypal(paypal);

		AuthorizationResponse response = litle.authorize(authorization);
		assertEquals(response.getMessage(), "Approved",response.getMessage());
	}

	@Test
    public void simpleAuthWithApplepay() throws Exception {
        Authorization authorization = new Authorization();
        authorization.setReportGroup("Planets");
        authorization.setOrderId("12344");
        authorization.setAmount(110L);
        authorization.setOrderSource(OrderSourceType.ECOMMERCE);
        ApplepayType applepayType = new ApplepayType();
        ApplepayHeaderType applepayHeaderType = new ApplepayHeaderType();
        applepayHeaderType.setApplicationData("454657413164");
        applepayHeaderType.setEphemeralPublicKey("e3b0c44298fc1c149afbf4c8996fb92427ae41e4649b934ca495991b7852b855");
        applepayHeaderType.setPublicKeyHash("e3b0c44298fc1c149afbf4c8996fb92427ae41e4649b934ca495991b7852b855");
        applepayHeaderType.setTransactionId("1234");
        applepayType.setHeader(applepayHeaderType);
        applepayType.setData("user");
        applepayType.setSignature("sign");
        applepayType.setVersion("12345");
        authorization.setApplepay(applepayType);

        AuthorizationResponse response = litle.authorize(authorization);

        assertEquals(new Long(110),response.getApplepayResponse().getTransactionAmount());
    }

	@Test
	public void posWithoutCapabilityAndEntryMode() throws Exception {
		Authorization authorization = new Authorization();
		authorization.setReportGroup("Planets");
		authorization.setOrderId("12344");
		authorization.setAmount(106L);
		authorization.setOrderSource(OrderSourceType.ECOMMERCE);
		Pos pos = new Pos();
		pos.setCardholderId(PosCardholderIdTypeEnum.PIN);
		authorization.setPos(pos);
		CardType card = new CardType();
		card.setType(MethodOfPaymentTypeEnum.VI);
		card.setNumber("4100000000000002");
		card.setExpDate("1210");
		authorization.setCard(card);
		try {
			litle.authorize(authorization);
			fail("expected exception");
		} catch(LitleOnlineException e) {
			assertTrue(e.getMessage(),e.getMessage().startsWith("Error validating xml data against the schema"));
		}
	}

	@Test
	public void accountUpdate() throws Exception {
		Authorization authorization = new Authorization();
		authorization.setReportGroup("Planets");
		authorization.setOrderId("12344");
		authorization.setAmount(106L);
		authorization.setOrderSource(OrderSourceType.ECOMMERCE);
		CardType card = new CardType();
		card.setType(MethodOfPaymentTypeEnum.VI);
		card.setNumber("4100100000000000");
		card.setExpDate("1210");
		authorization.setCard(card);

		AuthorizationResponse response = litle.authorize(authorization);
		assertEquals("4100100000000000", response.getAccountUpdater().getOriginalCardInfo().getNumber());
	}

	@Test
	public void testTrackData() throws Exception {
		Authorization authorization = new Authorization();
		authorization.setId("AX54321678");
		authorization.setReportGroup("RG27");
		authorization.setOrderId("12z58743y1");
		authorization.setAmount(12522L);
		authorization.setOrderSource(OrderSourceType.RETAIL);
		Contact billToAddress = new Contact();
		billToAddress.setZip("95032");
		authorization.setBillToAddress(billToAddress);
		CardType card = new CardType();
		card.setTrack("%B40000001^Doe/JohnP^06041...?;40001=0604101064200?");
		authorization.setCard(card);
		Pos pos = new Pos();
		pos.setCapability(PosCapabilityTypeEnum.MAGSTRIPE);
		pos.setEntryMode(PosEntryModeTypeEnum.COMPLETEREAD);
		pos.setCardholderId(PosCardholderIdTypeEnum.SIGNATURE);
		authorization.setPos(pos);

		AuthorizationResponse response = litle.authorize(authorization);
		assertEquals(response.getMessage(), "Approved",response.getMessage());
	}

	@Test
	public void testListOfTaxAmounts() throws Exception {
	    Authorization authorization = new Authorization();
	    authorization.setId("12345");
	    authorization.setReportGroup("Default");
	    authorization.setOrderId("67890");
	    authorization.setAmount(10000L);
	    authorization.setOrderSource(OrderSourceType.ECOMMERCE);
	    EnhancedData enhanced = new EnhancedData();
	    DetailTax dt1 = new DetailTax();
	    dt1.setTaxAmount(100L);
	    enhanced.getDetailTaxes().add(dt1);
	    DetailTax dt2 = new DetailTax();
	    dt2.setTaxAmount(200L);
	    enhanced.getDetailTaxes().add(dt2);
	    authorization.setEnhancedData(enhanced);
	    CardType card = new CardType();
	    card.setNumber("4100100000000000");
	    card.setExpDate("1215");
	    card.setType(MethodOfPaymentTypeEnum.VI);
        authorization.setCard(card);

        AuthorizationResponse response = litle.authorize(authorization);
        assertEquals(response.getMessage(), "Approved", response.getMessage());
	}

	@Test
	public void testSecondaryAmount(){
	    Authorization authorization = new Authorization();
        authorization.setId("12347");
        authorization.setReportGroup("Default");
        authorization.setOrderId("67890");
        authorization.setAmount(10000L);
        authorization.setSecondaryAmount(500L);
        authorization.setOrderSource(OrderSourceType.ECOMMERCE);
        EnhancedData enhanced = new EnhancedData();
        DetailTax dt1 = new DetailTax();
        dt1.setTaxAmount(100L);
        enhanced.getDetailTaxes().add(dt1);
        DetailTax dt2 = new DetailTax();
        dt2.setTaxAmount(200L);
        enhanced.getDetailTaxes().add(dt2);
        authorization.setEnhancedData(enhanced);
        CardType card = new CardType();
        card.setNumber("4100100000000000");
        card.setExpDate("1215");
        card.setType(MethodOfPaymentTypeEnum.VI);
        authorization.setCard(card);

        AuthorizationResponse response = litle.authorize(authorization);
        assertEquals(response.getMessage(), "Approved", response.getMessage());
	}
	
	@Test
	public void testAuthWithProcessingType() throws Exception {
		Authorization authorization = new Authorization();
		authorization.setReportGroup("Planets");
		authorization.setOrderId("12344");
		authorization.setAmount(106L);
		authorization.setOrderSource(OrderSourceType.ECOMMERCE);
		authorization.setProcessingType(ProcessingTypeEnum.ACCOUNT_FUNDING);
		CardType card = new CardType();
		card.setType(MethodOfPaymentTypeEnum.VI);
		card.setNumber("4100000000000000");
		card.setExpDate("1210");
		authorization.setCard(card);

		AuthorizationResponse response = litle.authorize(authorization);
		assertEquals(response.getMessage(), "000",response.getResponse());
	}

	@Test
	public void simpleAuthWithProcessngTypeCOF() throws Exception {
		Authorization authorization = new Authorization();
		authorization.setReportGroup("Planets");
		authorization.setOrderId("12344");
		authorization.setAmount(106L);
		authorization.setOrderSource(OrderSourceType.ECOMMERCE);
		authorization.setId("id");
		authorization.setProcessingType(ProcessingTypeEnum.INITIAL_COF);
		CardType card = new CardType();
		card.setType(MethodOfPaymentTypeEnum.VI);
		card.setNumber("4100000000000000");
		card.setExpDate("1210");
		authorization.setCard(card);

		AuthorizationResponse response = litle.authorize(authorization);
		assertEquals(response.getMessage(), "000",response.getResponse());

		authorization.setProcessingType(ProcessingTypeEnum.MERCHANT_INITIATED_COF);
		response = litle.authorize(authorization);
		assertEquals(response.getMessage(), "000",response.getResponse());

		authorization.setProcessingType(ProcessingTypeEnum.CARDHOLDER_INITIATED_COF);
		response = litle.authorize(authorization);
		assertEquals(response.getMessage(), "000",response.getResponse());

	}
	
	@Test
	public void testAuthWithOrigNetworkTxnIdAndOrigTxnAmount() throws Exception {
		Authorization authorization = new Authorization();
		authorization.setReportGroup("Planets");
		authorization.setOrderId("12344");
		authorization.setAmount(106L);
		authorization.setOrderSource(OrderSourceType.ECOMMERCE);
		authorization.setOriginalNetworkTransactionId("1345678900");
		authorization.setOriginalTransactionAmount(1799l);
		CardType card = new CardType();
		card.setType(MethodOfPaymentTypeEnum.VI);
		card.setNumber("4100000000000000");
		card.setExpDate("1210");
		authorization.setCard(card);

		AuthorizationResponse response = litle.authorize(authorization);
		assertEquals(response.getMessage(), "000",response.getResponse());
		assertEquals("63225578415568556365452427825", response.getNetworkTransactionId());
	}

}
