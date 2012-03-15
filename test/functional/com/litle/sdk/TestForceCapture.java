package com.litle.sdk;

import static org.junit.Assert.assertEquals;

import java.math.BigInteger;

import org.junit.Test;

import com.litle.sdk.generate.CardTokenType;
import com.litle.sdk.generate.CardType;
import com.litle.sdk.generate.ForceCapture;
import com.litle.sdk.generate.ForceCaptureResponse;

public class TestForceCapture {

	@Test
	public void simpleForceCaptureWithCard() throws Exception{
		ForceCapture forcecapture = new ForceCapture();
		forcecapture.setAmount(BigInteger.valueOf(106L));
		forcecapture.setOrderId("12344");
		forcecapture.setOrderSource("ecommerce");
		CardType card = new CardType();
		card.setType("VI");
		card.setNumber("4100000000000001");
		card.setExpDate("1210");
		forcecapture.setCard(card);
		ForceCaptureResponse response = new LitleOnline().forceCapture(forcecapture);
		assertEquals("Approved", response.getMessage());
	}
	
	@Test
	public void simpleForceCaptureWithToken() throws Exception{
		ForceCapture forcecapture = new ForceCapture();
		forcecapture.setAmount(BigInteger.valueOf(106L));
		forcecapture.setOrderId("12344");
		forcecapture.setOrderSource("ecommerce");
		CardTokenType token = new CardTokenType();
		token.setLitleToken("123456789101112");
		token.setExpDate("1210");
		token.setCardValidationNum("555");
		token.setType("VI");
		forcecapture.setToken(token);
		ForceCaptureResponse response = new LitleOnline().forceCapture(forcecapture);
		assertEquals("Approved", response.getMessage());
	}

}

