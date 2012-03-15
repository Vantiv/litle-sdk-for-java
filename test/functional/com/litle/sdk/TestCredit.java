package com.litle.sdk;

import static org.junit.Assert.*;

import java.math.BigInteger;
import java.util.Calendar;

import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;

import org.junit.Test;

import com.litle.sdk.generate.AuthInformation;
import com.litle.sdk.generate.AuthReversal;
import com.litle.sdk.generate.AuthReversalResponse;
import com.litle.sdk.generate.AuthorizationResponse;
import com.litle.sdk.generate.CaptureGivenAuth;
import com.litle.sdk.generate.CaptureGivenAuthResponse;
import com.litle.sdk.generate.CardType;
import com.litle.sdk.generate.Credit;
import com.litle.sdk.generate.CreditResponse;

public class TestCredit {

	@Test
	public void simpleCreditWithCard() throws Exception{
		Credit credit = new Credit();
		credit.setAmount(BigInteger.valueOf(106L));
		credit.setOrderId("12344");
		credit.setOrderSource("ecommerce");
		CardType card = new CardType();
		card.setType("VI");
		card.setNumber("4100000000000001");
		card.setExpDate("1210");
		credit.setCard(card);
		CreditResponse response = new LitleOnline().credit(credit);
		assertEquals("Approved", response.getMessage());
	}

}


