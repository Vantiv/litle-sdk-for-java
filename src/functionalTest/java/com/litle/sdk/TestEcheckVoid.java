package com.litle.sdk;

import static org.junit.Assert.assertEquals;

import org.junit.BeforeClass;
import org.junit.Test;

import com.litle.sdk.generate.EcheckVoid;
import com.litle.sdk.generate.EcheckVoidResponse;

public class TestEcheckVoid {

	private static LitleOnline litle;

	@BeforeClass
	public static void beforeClass() throws Exception {
		litle = new LitleOnline();
	}

	@Test
	public void simpleEcheckVoid() throws Exception{
		EcheckVoid echeckvoid = new EcheckVoid();
		echeckvoid.setLitleTxnId(123456789101112L);
		echeckvoid.setId("id");
		EcheckVoidResponse response = litle.echeckVoid(echeckvoid);
		assertEquals("Approved", response.getMessage());
	}
}

