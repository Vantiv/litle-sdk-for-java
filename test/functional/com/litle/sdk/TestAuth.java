package com.litle.sdk;

import static org.junit.Assert.*;

import java.math.BigInteger;

import org.junit.Test;

import com.litle.sdk.generate.Authorization;
import com.litle.sdk.generate.AuthorizationResponse;
import com.litle.sdk.generate.CardType;
import com.litle.sdk.generate.LitleOnlineResponse;

public class TestAuth {

	@Test
	public void simpleAuthWithCard() throws Exception {
		Authorization authorization = new Authorization();
		authorization.setReportGroup("Planets");
		authorization.setOrderId("12344");
		authorization.setAmount(BigInteger.valueOf(106L));
		authorization.setOrderSource("ecommerce");
		CardType card = new CardType();
		card.setType("VI");
		card.setNumber("4100000000000002");
		card.setExpDate("1210");
		authorization.setCard(card);
		
		AuthorizationResponse response = new LitleOnline().authorize(authorization);
		assertEquals("000",response.getResponse());
	}

}
