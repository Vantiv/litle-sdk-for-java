package com.litle.sdk;

import static org.junit.Assert.*;
import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.litle.sdk.generate.EcheckForTokenType;
import com.litle.sdk.generate.RegisterTokenRequestType;
import com.litle.sdk.generate.RegisterTokenResponse;

public class TestToken {

	@Test
	public void simpleToken() throws Exception{
		RegisterTokenRequestType token = new RegisterTokenRequestType();
		token.setOrderId("12344");
		token.setAccountNumber("1233456789103801");
		RegisterTokenResponse response = new LitleOnline().registertoken(token);
		assertEquals("Account number was successfully registered", response.getMessage());
	}
	
	@Test
	public void simpleTokenWithPaypage() throws Exception{
		RegisterTokenRequestType token = new RegisterTokenRequestType();
		token.setOrderId("12344");
		token.setPaypageRegistrationId("1233456789101112");
		RegisterTokenResponse response = new LitleOnline().registertoken(token);
		assertEquals("Account number was successfully registered", response.getMessage());
	}
	
	@Test
	public void simpleTokenWithEcheck() throws Exception{
		RegisterTokenRequestType token = new RegisterTokenRequestType();
		token.setOrderId("12344");
		EcheckForTokenType echeck = new EcheckForTokenType();
		echeck.setAccNum("12344565");
		echeck.setRoutingNum("123476545");
		token.setEcheckForToken(echeck);
		RegisterTokenResponse response = new LitleOnline().registertoken(token);
		assertEquals("Account number was successfully registered", response.getMessage());
	}
	
	@Test
	public void tokenEcheckMissingRequiredField() throws Exception{
		RegisterTokenRequestType token = new RegisterTokenRequestType();
		token.setOrderId("12344");
		EcheckForTokenType echeck = new EcheckForTokenType();
		echeck.setRoutingNum("123476545");
		token.setEcheckForToken(echeck);
		try {
			new LitleOnline().registertoken(token);
			fail("expected exception");
		} catch(LitleOnlineException e) {
			assertEquals("Error validating xml data against the schema", e.getMessage());
		}
	}

}

