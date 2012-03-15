package com.litle.sdk;

import static org.junit.Assert.*;

import java.math.BigInteger;

import org.junit.Test;

import com.litle.sdk.generate.AuthReversal;
import com.litle.sdk.generate.AuthReversalResponse;
import com.litle.sdk.generate.AuthorizationResponse;
import com.litle.sdk.generate.CardType;
import com.litle.sdk.generate.RegisterTokenRequestType;
import com.litle.sdk.generate.RegisterTokenResponse;
import com.litle.sdk.generate.Sale;
import com.litle.sdk.generate.SaleResponse;

public class TestToken {

	@Test
	public void simpleToken() throws Exception{
		RegisterTokenRequestType token = new RegisterTokenRequestType();
		token.setOrderId("12344");
		token.setAccountNumber("1233456789103801");
		RegisterTokenResponse response = new LitleOnline().registertoken(token);
		assertEquals("Account number was successfully registered", response.getMessage());
	}

}

