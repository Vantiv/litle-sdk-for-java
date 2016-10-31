package com.litle.sdk;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.BeforeClass;
import org.junit.Test;

import com.litle.sdk.generate.ApplepayHeaderType;
import com.litle.sdk.generate.ApplepayType;
import com.litle.sdk.generate.CardTokenType;
import com.litle.sdk.generate.CardType;
import com.litle.sdk.generate.Contact;
import com.litle.sdk.generate.CountryTypeEnum;
import com.litle.sdk.generate.MethodOfPaymentTypeEnum;
import com.litle.sdk.generate.OrderSourceType;
import com.litle.sdk.generate.PayPal;
import com.litle.sdk.generate.ProcessingTypeEnum;
import com.litle.sdk.generate.Sale;
import com.litle.sdk.generate.SaleResponse;
import com.litle.sdk.generate.Wallet;
import com.litle.sdk.generate.WalletSourceType;

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
    public void simpleSaleWithApplepayAndSecondaryAmount() throws Exception{
        Sale sale = new Sale();
        sale.setAmount(110L);
        sale.setSecondaryAmount(20L);
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
        assertEquals(new Long(110),response.getApplepayResponse().getTransactionAmount());
    }

	@Test
	public void simpleSaleWithAndroidPay() {
	    Sale sale = new Sale();
        sale.setAmount(2400L);
        sale.setLitleTxnId(123456L);
        sale.setOrderId("151124_APVIOLSaleeComTkn");
        sale.setOrderSource(OrderSourceType.ANDROIDPAY);
        Contact value = new Contact();
        value.setName("Raymond J. Johnson Jr.");
        value.setAddressLine1("123 Main Street");
        value.setCity("McLean");
        value.setState("VA");
        value.setZip("22102");
        value.setCountry(CountryTypeEnum.USA);
        value.setEmail("ray@rayjay.com");
        value.setPhone("978-275-0000");
        sale.setBillToAddress(value);
        sale.setShipToAddress(value);
        CardTokenType token = new CardTokenType();
        token.setLitleToken("1111000100071112");
        token.setExpDate("0150");
        sale.setToken(token);

        SaleResponse response = litle.sale(sale);
        
        assertEquals("Approved", response.getMessage());
        assertEquals("aHR0cHM6Ly93d3cueW91dHViZS5jb20vd2F0Y2g/dj1kUXc0dzlXZ1hjUQ0K", response.getAndroidpayResponse().getCryptogram());
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
	public void testSaleWithWallet_Visa() throws Exception{
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
		Wallet wallet = new Wallet();
		wallet.setWalletSourceType(WalletSourceType.VISA_CHECKOUT);
		
		SaleResponse response = litle.sale(sale);
		
		assertEquals("Approved", response.getMessage());
	}
	
	@Test
	public void testSaleWithWallet_Mastercard() throws Exception{
		Sale sale = new Sale();
		sale.setAmount(106L);
		sale.setLitleTxnId(123456L);
		sale.setOrderId("12344");
		sale.setOrderSource(OrderSourceType.ECOMMERCE);
		CardType card = new CardType();
		card.setType(MethodOfPaymentTypeEnum.MC);
		card.setNumber("5400000000000000");
		card.setExpDate("1210");
		sale.setCard(card);
		Wallet wallet = new Wallet();
		wallet.setWalletSourceType(WalletSourceType.MASTER_PASS);
		
		SaleResponse response = litle.sale(sale);
		
		assertEquals("Approved", response.getMessage());
	}
	
	@Test
	public void testSaleWithProcessingType() throws Exception{
		Sale sale = new Sale();
		sale.setAmount(106L);
		sale.setLitleTxnId(123456L);
		sale.setOrderId("12344");
		sale.setOrderSource(OrderSourceType.ECOMMERCE);
		CardType card = new CardType();
		card.setType(MethodOfPaymentTypeEnum.MC);
		card.setNumber("5400000000000000");
		card.setExpDate("1210");
		sale.setCard(card);
		sale.setProcessingType(ProcessingTypeEnum.INITIAL_INSTALLMENT);
		
		SaleResponse response = litle.sale(sale);
		
		assertEquals("Approved", response.getMessage());
	}
	
	@Test
	public void testSaleWithOrigTxnIdAndOrigAmount() throws Exception{
		Sale sale = new Sale();
		sale.setAmount(106L);
		sale.setLitleTxnId(123456L);
		sale.setOrderId("12344");
		sale.setOrderSource(OrderSourceType.ECOMMERCE);
		CardType card = new CardType();
		card.setType(MethodOfPaymentTypeEnum.MC);
		card.setNumber("5400700000000000");
		card.setExpDate("1210");
		sale.setCard(card);
		sale.setOriginalNetworkTransactionId("98765432109876543210");
		sale.setOriginalTransactionAmount(700l);
		
		SaleResponse response = litle.sale(sale);
		
		assertEquals("Approved", response.getMessage());
		// no network txn Id because it's a MC transaction, only VI returns this value from the Sandbox
		assertNull(response.getNetworkTransactionId());
	}
	
	@Test
	public void testSaleWithNetworkTxnIdResponseAndCardSuffixResponse() throws Exception{
		Sale sale = new Sale();
		sale.setAmount(106L);
		sale.setLitleTxnId(123456L);
		sale.setOrderId("12344");
		sale.setOrderSource(OrderSourceType.ECOMMERCE);
		CardType card = new CardType();
		card.setType(MethodOfPaymentTypeEnum.VI);
		card.setNumber("4100700000000000");
		card.setExpDate("1210");
		card.setPin("1111");
		sale.setCard(card);
		sale.setOriginalNetworkTransactionId("98765432109876543210");
		sale.setOriginalTransactionAmount(700l);
		
		SaleResponse response = litle.sale(sale);
		
		assertEquals("Approved", response.getMessage());
		assertEquals("63225578415568556365452427825", response.getNetworkTransactionId());
		assertEquals("123456", response.getCardSuffix());
	}
}
