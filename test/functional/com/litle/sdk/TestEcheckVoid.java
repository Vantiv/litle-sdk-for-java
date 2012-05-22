package com.litle.sdk;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

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
		EcheckVoidResponse response = litle.echeckVoid(echeckvoid);
		assertEquals("Approved", response.getMessage());
	}
	
	@Test
	public void noLitleTxnId() throws Exception{
		EcheckVoid echeckvoid = new EcheckVoid();
		try {
			litle.echeckVoid(echeckvoid);
			fail("Expected exception");
		} catch(LitleOnlineException e) {
			assertTrue(e.getMessage(),e.getMessage().startsWith("Error validating xml data against the schema"));
		}
	}

}

