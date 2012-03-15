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
import com.litle.sdk.generate.EcheckCredit;
import com.litle.sdk.generate.EcheckCreditResponse;

public class TestEcheckCredit {

	@Test
	public void simpleEcheckCredit() throws Exception{
		EcheckCredit echeckcredit = new EcheckCredit();
		echeckcredit.setAmount(BigInteger.valueOf(12L));
		echeckcredit.setLitleTxnId(123456789101112L);
		EcheckCreditResponse response = new LitleOnline().echeckcredit(echeckcredit);
		assertEquals("Approved", response.getMessage());
	}

}

