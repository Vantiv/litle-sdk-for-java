package io.github.vantiv.sdk;

import static org.junit.Assert.assertEquals;

import org.junit.BeforeClass;
import org.junit.Test;

import io.github.vantiv.sdk.generate.ApplepayHeaderType;
import io.github.vantiv.sdk.generate.ApplepayType;
import io.github.vantiv.sdk.generate.CardTokenType;
import io.github.vantiv.sdk.generate.CardType;
import io.github.vantiv.sdk.generate.MethodOfPaymentTypeEnum;
import io.github.vantiv.sdk.generate.OrderSourceType;
import io.github.vantiv.sdk.generate.PayPal;
import io.github.vantiv.sdk.generate.ProcessingTypeEnum;
import io.github.vantiv.sdk.generate.Sale;
import io.github.vantiv.sdk.generate.SaleResponse;

public class TestSale {

	private static LitleOnline litle;

	@BeforeClass
	public static void beforeClass() throws Exception {
		litle = new LitleOnline();
	}

	@Test
	public void simpleSaleWithCard() throws Exception{
		Sale sale = new Sale();
		sale.setAmount(106L);
		sale.setLitleTxnId(123456L);
		sale.setOrderId("12344");
		sale.setOrderSource(OrderSourceType.ECOMMERCE);
		CardType card = new CardType();
		card.setType(MethodOfPaymentTypeEnum.VI);
		card.setNumber("4100000000000000");
		card.setExpDate("1210");
		sale.setCard(card);
		SaleResponse response = litle.sale(sale);
		assertEquals("Approved", response.getMessage());
	}

	@Test
	public void simpleSaleWithPayPal() throws Exception{
		Sale sale = new Sale();
		sale.setAmount(106L);
		sale.setLitleTxnId(123456L);
		sale.setOrderId("12344");
		sale.setOrderSource(OrderSourceType.ECOMMERCE);
		PayPal paypal = new PayPal();
		paypal.setPayerId("1234");
		paypal.setToken("1234");
		paypal.setTransactionId("123456");
		sale.setPaypal(paypal);
		SaleResponse response = litle.sale(sale);
		assertEquals("Approved", response.getMessage());
	}

	@Test
	public void simpleSaleWithToken() throws Exception {
		Sale sale = new Sale();
		sale.setAmount(106L);
		sale.setOrderId("12344");
		sale.setOrderSource(OrderSourceType.ECOMMERCE);
		CardTokenType token = new CardTokenType();
		token.setCardValidationNum("349");
		token.setExpDate("1214");
		token.setLitleToken("1111222233334000");
		token.setType(MethodOfPaymentTypeEnum.VI);
		sale.setToken(token);
		SaleResponse response = litle.sale(sale);
		assertEquals("Approved", response.getMessage());
	}

	@Test
    public void simpleSaleWithApplepay() throws Exception{
        Sale sale = new Sale();
        sale.setAmount(110L);
        sale.setLitleTxnId(123456L);
        sale.setOrderId("12347");
        sale.setOrderSource(OrderSourceType.ECOMMERCE);

        ApplepayType applepayType = new ApplepayType();
        ApplepayHeaderType applepayHeaderType = new ApplepayHeaderType();
        applepayHeaderType.setApplicationData("e3b0c44298fc1c149afbf4c8996fb92427ae41e4649b934ca495991b7852b855");
        applepayHeaderType.setEphemeralPublicKey("e3b0c44298fc1c149afbf4c8996fb92427ae41e4649b934ca495991b7852b855");
        applepayHeaderType.setPublicKeyHash("e3b0c44298fc1c149afbf4c8996fb92427ae41e4649b934ca495991b7852b855");
        applepayHeaderType.setTransactionId("1234");
        applepayType.setHeader(applepayHeaderType);
        applepayType.setData("user");
        applepayType.setSignature("sign");
        applepayType.setVersion("12345");

        sale.setApplepay(applepayType);
        SaleResponse response = litle.sale(sale);
        assertEquals("Insufficient Funds", response.getMessage());
//        assertEquals(new Long(110),response.getApplepayResponse().getTransactionAmount());
    }

	@Test
    public void simpleSaleWithSecondaryAmount() throws Exception{
        Sale sale = new Sale();
        sale.setAmount(106L);
        sale.setSecondaryAmount(50L);
        sale.setLitleTxnId(123456L);
        sale.setOrderId("12344");
        sale.setOrderSource(OrderSourceType.ECOMMERCE);
        CardType card = new CardType();
        card.setType(MethodOfPaymentTypeEnum.VI);
        card.setNumber("4100000000000000");
        card.setExpDate("1210");
        sale.setCard(card);
        SaleResponse response = litle.sale(sale);
        assertEquals("Approved", response.getMessage());
    }
	
	@Test
	public void testSaleWithProcesingType() throws Exception{
		Sale sale = new Sale();
		sale.setAmount(106L);
		sale.setLitleTxnId(123456L);
		sale.setOrderId("12344");
		sale.setOrderSource(OrderSourceType.ECOMMERCE);
		sale.setProcessingType(ProcessingTypeEnum.ACCOUNT_FUNDING);
		CardType card = new CardType();
		card.setType(MethodOfPaymentTypeEnum.VI);
		card.setNumber("4100000000000000");
		card.setExpDate("1210");
		sale.setCard(card);
		SaleResponse response = litle.sale(sale);
		assertEquals("Approved", response.getMessage());
	}

	@Test
	public void testSaleWithProcessingTypeCOF() throws Exception{
		Sale sale = new Sale();
		sale.setAmount(106L);
		sale.setLitleTxnId(123456L);
		sale.setOrderId("12344");
		sale.setOrderSource(OrderSourceType.ECOMMERCE);
		CardType card = new CardType();
		card.setType(MethodOfPaymentTypeEnum.VI);
		card.setNumber("4100000000000000");
		card.setExpDate("1210");
		sale.setCard(card);
		sale.setId("id");
		sale.setProcessingType(ProcessingTypeEnum.INITIAL_COF);
		SaleResponse response = litle.sale(sale);
		assertEquals("Approved", response.getMessage());

		sale.setProcessingType(ProcessingTypeEnum.MERCHANT_INITIATED_COF);
		response = litle.sale(sale);
		assertEquals("Approved", response.getMessage());

		sale.setProcessingType(ProcessingTypeEnum.CARDHOLDER_INITIATED_COF);
		response = litle.sale(sale);
		assertEquals("Approved", response.getMessage());
	}
	
	@Test
	public void testSaleWithOrigNetworkTxnIdandAmount() throws Exception{
		Sale sale = new Sale();
		sale.setAmount(106L);
		sale.setLitleTxnId(123456L);
		sale.setOrderId("12344");
		sale.setOrderSource(OrderSourceType.ECOMMERCE);
		sale.setOriginalNetworkTransactionId("9876543210");
		sale.setOriginalTransactionAmount(5523l);
		CardType card = new CardType();
		card.setType(MethodOfPaymentTypeEnum.VI);
		card.setNumber("4100000000000000");
		card.setExpDate("1210");
		sale.setCard(card);
		SaleResponse response = litle.sale(sale);
		assertEquals("Approved", response.getMessage());
		assertEquals("63225578415568556365452427825", response.getNetworkTransactionId());
	}
	
}
