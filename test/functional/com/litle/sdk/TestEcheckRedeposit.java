package com.litle.sdk;

import static org.junit.Assert.*;

import java.math.BigInteger;
import java.util.Calendar;

import org.junit.Test;

import com.litle.sdk.generate.AuthInformation;
import com.litle.sdk.generate.AuthReversal;
import com.litle.sdk.generate.AuthReversalResponse;
import com.litle.sdk.generate.AuthorizationResponse;
import com.litle.sdk.generate.CaptureGivenAuth;
import com.litle.sdk.generate.CaptureGivenAuthResponse;
import com.litle.sdk.generate.CardType;
import com.litle.sdk.generate.EcheckRedeposit;
import com.litle.sdk.generate.EcheckRedepositResponse;

public class TestEcheckRedeposit {

	@Test
	public void simpleEcheckRedeposit() throws Exception{
		EcheckRedeposit echeckredeposit = new EcheckRedeposit();
		echeckredeposit.setLitleTxnId(123456L);
		EcheckRedepositResponse response = new LitleOnline().echeckredeposit(echeckredeposit);
		assertEquals("Approved", response.getMessage());
	}

}
