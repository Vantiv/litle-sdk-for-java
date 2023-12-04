package io.github.vantiv.sdk;

import static org.junit.Assert.assertEquals;

import org.junit.BeforeClass;
import org.junit.Test;
import io.github.vantiv.sdk.generate.*;

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
		assertEquals("Transaction Received", response.getMessage());
	}
}

