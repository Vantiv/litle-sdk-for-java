package com.litle.sdk;

import static org.junit.Assert.assertEquals;

import java.math.BigInteger;
import java.util.Calendar;

import org.junit.Test;

import com.litle.sdk.generate.AuthInformation;
import com.litle.sdk.generate.CaptureGivenAuth;
import com.litle.sdk.generate.CaptureGivenAuthResponse;
import com.litle.sdk.generate.CardType;


public class TestCaptureGivenAuth {

	@Test
	public void simpleCaptureGivenAuthWithCard() throws Exception{
		CaptureGivenAuth capturegivenauth = new CaptureGivenAuth();
		capturegivenauth.setAmount(BigInteger.valueOf(106L));
		capturegivenauth.setOrderId("12344");
		AuthInformation authInfo = new AuthInformation();
		Calendar authDate = Calendar.getInstance();
		authDate.set(2002, Calendar.OCTOBER, 9);
		authInfo.setAuthDate(authDate);
		authInfo.setAuthCode("543216");
		authInfo.setAuthAmount(BigInteger.valueOf(12345L));
		capturegivenauth.setAuthInformation(authInfo);
		capturegivenauth.setOrderSource("ecommerce");
		CardType card = new CardType();
		card.setType("VI");
		card.setNumber("4100000000000001");
		card.setExpDate("1210");
		capturegivenauth.setCard(card);
		CaptureGivenAuthResponse response = new LitleOnline().capturegivenauth(capturegivenauth);
		assertEquals("Approved", response.getMessage());
	}

}
