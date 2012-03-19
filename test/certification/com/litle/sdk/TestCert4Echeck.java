package com.litle.sdk;


import static org.junit.Assert.fail;

import org.junit.BeforeClass;
import org.junit.Test;

import com.litle.sdk.generate.EcheckVerification;

public class TestCert4Echeck {

	private static LitleOnline litle;

	@BeforeClass
	public static void beforeClass() throws Exception {
		litle = new LitleOnline();
	}
	
	@Test
	public void test37() {
		EcheckVerification verification = new EcheckVerification();
		verification.setOrderId("37");
		//verification.setAmount(BigInteger.valueOf(3001));
	}

}
