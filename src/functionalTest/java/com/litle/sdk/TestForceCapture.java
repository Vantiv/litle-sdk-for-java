package com.litle.sdk;

import static org.junit.Assert.assertEquals;

import org.junit.BeforeClass;
import org.junit.Test;

import com.litle.sdk.generate.CardTokenType;
import com.litle.sdk.generate.CardType;
import com.litle.sdk.generate.ForceCapture;
import com.litle.sdk.generate.ForceCaptureResponse;
import com.litle.sdk.generate.MethodOfPaymentTypeEnum;
import com.litle.sdk.generate.OrderSourceType;

public class TestForceCapture {
	
	private static LitleOnline litle;

	@BeforeClass
	public static void beforeClass() throws Exception {
		litle = new LitleOnline();
	}
	
	@Test
	public void simpleForceCaptureWithCard() throws Exception{
		ForceCapture forcecapture = new ForceCapture();
		forcecapture.setAmount(106L);
		forcecapture.setOrderId("12344");
		forcecapture.setOrderSource(OrderSourceType.ECOMMERCE);
		CardType card = new CardType();
		card.setType(MethodOfPaymentTypeEnum.VI);
		card.setNumber("4100000000000001");
		card.setExpDate("1210");
		forcecapture.setCard(card);
		ForceCaptureResponse response = litle.forceCapture(forcecapture);
		assertEquals("Approved", response.getMessage());
	}
	
	@Test
    public void simpleForceCaptureWithSecondaryAmount() throws Exception{
        ForceCapture forcecapture = new ForceCapture();
        forcecapture.setAmount(106L);
        forcecapture.setSecondaryAmount(20L);
        forcecapture.setOrderId("12344");
        forcecapture.setOrderSource(OrderSourceType.ECOMMERCE);
        CardType card = new CardType();
        card.setType(MethodOfPaymentTypeEnum.VI);
        card.setNumber("4100000000000001");
        card.setExpDate("1210");
        forcecapture.setCard(card);
        ForceCaptureResponse response = litle.forceCapture(forcecapture);
        assertEquals("Approved", response.getMessage());
    }
	
	@Test
	public void simpleForceCaptureWithToken() throws Exception{
		ForceCapture forcecapture = new ForceCapture();
		forcecapture.setAmount(106L);
		forcecapture.setOrderId("12344");
		forcecapture.setOrderSource(OrderSourceType.ECOMMERCE);
		CardTokenType token = new CardTokenType();
		token.setLitleToken("123456789101112");
		token.setExpDate("1210");
		token.setCardValidationNum("555");
		token.setType(MethodOfPaymentTypeEnum.VI);
		forcecapture.setToken(token);
		ForceCaptureResponse response = litle.forceCapture(forcecapture);
		assertEquals("Approved", response.getMessage());
	}

}

