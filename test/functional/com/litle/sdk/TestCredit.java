package com.litle.sdk;

import static org.junit.Assert.assertEquals;

import org.junit.BeforeClass;
import org.junit.Test;

import com.litle.sdk.generate.CardType;
import com.litle.sdk.generate.Credit;
import com.litle.sdk.generate.Credit.Paypal;
import com.litle.sdk.generate.CreditResponse;
import com.litle.sdk.generate.OrderSourceType;
import com.litle.sdk.generate.ProcessingInstructions;

public class TestCredit {

	private static LitleOnline litle;

	@BeforeClass
	public static void beforeClass() throws Exception {
		litle = new LitleOnline();
	}

	
	@Test
	public void simpleCreditWithCard() throws Exception{
		Credit credit = new Credit();
		credit.setAmount(106L);
		credit.setOrderId("12344");
		credit.setOrderSource(OrderSourceType.ECOMMERCE);
		CardType card = new CardType();
		card.setType("VI");
		card.setNumber("4100000000000001");
		card.setExpDate("1210");
		credit.setCard(card);
		CreditResponse response = litle.credit(credit);
		assertEquals("Approved", response.getMessage());
	}
	
	@Test
	public void simpleCreditWithPaypal() throws Exception{
		Credit credit = new Credit();
		credit.setAmount(106L);
		credit.setOrderId("123456");
		credit.setOrderSource(OrderSourceType.ECOMMERCE);
		Paypal paypal = new Paypal();
		paypal.setPayerId("1234");
		credit.setPaypal(paypal);
		CreditResponse response = litle.credit(credit);
		assertEquals("Approved", response.getMessage());
	}
	
	@Test
	public void paypalNotes() throws Exception{
		Credit credit = new Credit();
		credit.setAmount(106L);
		credit.setOrderId("12344");
		credit.setPayPalNotes("Hello");
		credit.setOrderSource(OrderSourceType.ECOMMERCE);
		CardType card = new CardType();
		card.setType("VI");
		card.setNumber("4100000000000001");
		card.setExpDate("1210");
		credit.setCard(card);
		CreditResponse response = litle.credit(credit);
		assertEquals("Approved", response.getMessage());
	}
	
	@Test
	public void processingInstructionAndAmexData() throws Exception{
		Credit credit = new Credit();
		credit.setAmount(2000L);
		credit.setOrderId("12344");
		credit.setOrderSource(OrderSourceType.ECOMMERCE);
		ProcessingInstructions processinginstructions = new ProcessingInstructions();
		processinginstructions.setBypassVelocityCheck(true);
		credit.setProcessingInstructions(processinginstructions);
		CardType card = new CardType();
		card.setType("VI");
		card.setNumber("4100000000000001");
		card.setExpDate("1210");
		credit.setCard(card);
		CreditResponse response = litle.credit(credit);
		assertEquals("Approved", response.getMessage());
	}

}



