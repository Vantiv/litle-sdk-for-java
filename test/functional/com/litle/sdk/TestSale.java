package com.litle.sdk;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.math.BigInteger;

import org.junit.BeforeClass;
import org.junit.Test;

import com.litle.sdk.generate.CardType;
import com.litle.sdk.generate.PayPal;
import com.litle.sdk.generate.Sale;
import com.litle.sdk.generate.SaleResponse;

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
		sale.setOrderSource("ecommerce");
		CardType card = new CardType();
		card.setType("VI");
		card.setNumber("4100000000000002");
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
		sale.setOrderSource("ecommerce");
		PayPal paypal = new PayPal();
		paypal.setPayerId("1234");
		paypal.setToken("1234");
		paypal.setTransactionId("123456");
		sale.setPaypal(paypal);
		SaleResponse response = litle.sale(sale);
		assertEquals("Approved", response.getMessage());
	}

	@Test
	public void illegalOrderSource() throws Exception {
		Sale sale = new Sale();
		sale.setReportGroup("Planets");
		sale.setOrderId("12344");
		sale.setAmount(106L);
		sale.setOrderSource("ecomerce"); //This order source is mispelled on purpose!
		CardType card = new CardType();
		card.setType("VI");
		card.setNumber("4100000000000002");
		card.setExpDate("1210");
		sale.setCard(card);
		
		try {
			litle.sale(sale);
			fail("expected exception");
		} catch(LitleOnlineException e) {
			assertEquals("Error validating xml data against the schema", e.getMessage());
		}
	}
	
	@Test
	public void illegalCardType() throws Exception {
		Sale sale = new Sale();
		sale.setReportGroup("Planets");
		sale.setOrderId("12344");
		sale.setAmount(106L);
		sale.setLitleTxnId(123456L);
		sale.setOrderSource("ecomerce"); //This order source is mispelled on purpose!
		CardType card = new CardType();
		card.setType("NO");
		card.setNumber("4100000000000002");
		card.setExpDate("1210");
		sale.setCard(card);
		
		try {
			litle.sale(sale);
			fail("expected exception");
		} catch(LitleOnlineException e) {
			assertEquals("Error validating xml data against the schema", e.getMessage());
		}
	}
}
