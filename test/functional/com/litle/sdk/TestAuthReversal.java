package com.litle.sdk;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.math.BigInteger;

import org.junit.Test;

import com.litle.sdk.generate.AuthReversal;
import com.litle.sdk.generate.AuthReversalResponse;

public class TestAuthReversal {

	@Test
	public void simpleAuthReversal() {
		AuthReversal reversal = new AuthReversal();
		reversal.setLitleTxnId(12345678000L);
		reversal.setAmount(BigInteger.valueOf(106L));
		reversal.setPayPalNotes("Notes");
		
		AuthReversalResponse response = new LitleOnline().authReversal(reversal);
		assertEquals("Approved", response.getMessage());
	}

}
