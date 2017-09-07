package com.litle.sdk;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.junit.BeforeClass;
import org.junit.Test;

import com.litle.sdk.generate.ApplepayHeaderType;
import com.litle.sdk.generate.ApplepayType;
import com.litle.sdk.generate.EcheckForTokenType;
import com.litle.sdk.generate.RegisterTokenRequestType;
import com.litle.sdk.generate.RegisterTokenResponse;

public class TestToken {

	private static LitleOnline litle;

	@BeforeClass
	public static void beforeClass() throws Exception {
		litle = new LitleOnline();
	}

	@Test
	public void simpleToken() throws Exception{
		RegisterTokenRequestType token = new RegisterTokenRequestType();
		token.setOrderId("12344");
		token.setAccountNumber("1233456789103801");
		
		RegisterTokenResponse response = litle.registerToken(token);
		
		assertEquals("Account number was successfully registered", response.getMessage());
	}

	@Test
	public void simpleTokenWithPaypage() throws Exception{
		RegisterTokenRequestType token = new RegisterTokenRequestType();
		token.setOrderId("12344");
		token.setPaypageRegistrationId("1233456789101112");
		
		RegisterTokenResponse response = litle.registerToken(token);
		
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
		
		RegisterTokenResponse response = litle.registerToken(token);
		
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
			litle.registerToken(token);
			fail("expected exception");
		} catch(LitleOnlineException e) {
			assertTrue(e.getMessage(),e.getMessage().startsWith("Error validating xml data against the schema"));
		}
	}

	@Test
    public void simpleTokenWithApplepay() throws Exception{
        RegisterTokenRequestType token = new RegisterTokenRequestType();
        token.setOrderId("12344");
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
        token.setApplepay(applepayType);
        
        RegisterTokenResponse response = litle.registerToken(token);
        
        assertEquals("Account number was successfully registered", response.getMessage());
        assertEquals(new Long(0),response.getApplepayResponse().getTransactionAmount());
    }

	@Test
	public void convertPaypageRegistrationIdIntoToken() throws Exception {
		RegisterTokenRequestType tokenRequest = new RegisterTokenRequestType();
		tokenRequest.setOrderId("12345");
		tokenRequest.setPaypageRegistrationId("123456789012345678901324567890abcdefghi");
		
		RegisterTokenResponse tokenResponse = litle.registerToken(tokenRequest);
		
		assertEquals("1111222233334444", tokenResponse.getLitleToken()); //all paypage registration ids return the same token
	}
	
	@Test
	public void testTokenWithAndroidpayResponse() throws Exception{
		RegisterTokenRequestType token = new RegisterTokenRequestType();
		token.setOrderId("androidpay");
		token.setAccountNumber("1233456789103801");
		
		RegisterTokenResponse response = litle.registerToken(token);
		
		assertEquals("Account number was successfully registered", response.getMessage());
//		assertEquals("01", response.getAndroidpayResponse().getExpMonth());
//		assertEquals("2050", response.getAndroidpayResponse().getExpYear());
//		assertEquals("aHR0cHM6Ly93d3cueW91dHViZS5jb20vd2F0Y2g/dj1kUXc0dzlXZ1hjUQ0K", response.getAndroidpayResponse().getCryptogram());
	}
}

