package com.litle.sdk;

import static org.junit.Assert.assertEquals;

import java.math.BigInteger;

import org.junit.Test;

import com.litle.sdk.generate.Capture;
import com.litle.sdk.generate.CaptureResponse;

public class TestCapture {

	@Test
	public void simpleCapture() throws Exception{
		Capture capture = new Capture();
		capture.setLitleTxnId(123456000);
		capture.setAmount(BigInteger.valueOf(106L));
		capture.setPayPalNotes("Notes");
		
		CaptureResponse response = new LitleOnline().capture(capture);
		assertEquals("Approved", response.getMessage());
	}


}
