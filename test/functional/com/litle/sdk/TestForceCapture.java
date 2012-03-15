package com.litle.sdk;

import static org.junit.Assert.*;

import java.math.BigInteger;

import org.junit.Test;

import com.litle.sdk.generate.AuthReversal;
import com.litle.sdk.generate.AuthReversalResponse;
import com.litle.sdk.generate.AuthorizationResponse;
import com.litle.sdk.generate.CardType;
import com.litle.sdk.generate.Credit;
import com.litle.sdk.generate.CreditResponse;
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
		ForceCaptureResponse response = new LitleOnline().forcecapture(forcecapture);
		assertEquals("Approved", response.getMessage());
	}

}

