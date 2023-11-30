package io.github.vantiv.sdk;

import static org.junit.Assert.assertEquals;

import org.junit.BeforeClass;
import org.junit.Test;

import io.github.vantiv.sdk.generate.Capture;
import io.github.vantiv.sdk.generate.CaptureResponse;
import io.github.vantiv.sdk.generate.EnhancedData;
import io.github.vantiv.sdk.generate.EnhancedData.DeliveryType;

public class TestCapture {

	private static LitleOnline litle;

	@BeforeClass
	public static void beforeClass() throws Exception {
		litle = new LitleOnline();
	}

	@Test
	public void simpleCapture() throws Exception{
		Capture capture = new Capture();
		capture.setLitleTxnId(123456000L);
		capture.setAmount(106L);
		capture.setPayPalNotes("Notes");
		capture.setId("id");

		CaptureResponse response = litle.capture(capture);
		assertEquals("Transaction Received", response.getMessage());
	}

	@Test
	public void simpleCaptureWithPartial() throws Exception{
		Capture capture = new Capture();
		capture.setLitleTxnId(123456000L);
		capture.setAmount(106L);
		capture.setPartial(true);
		capture.setPayPalNotes("Notes");
		capture.setId("id");

		CaptureResponse response = litle.capture(capture);
		assertEquals("Transaction Received", response.getMessage());
	}

	@Test
	public void complexCapture() throws Exception{
		Capture capture = new Capture();
		capture.setLitleTxnId(123456000L);
		capture.setAmount(106L);
		capture.setPayPalNotes("Notes");
		EnhancedData enhancedData = new EnhancedData();
		enhancedData.setCustomerReference("Litle");
		enhancedData.setSalesTax(50L);
		enhancedData.setDeliveryType(DeliveryType.TBD);
		capture.setEnhancedData(enhancedData);
		capture.setPayPalOrderComplete(true);
		capture.setId("id");

		CaptureResponse response = litle.capture(capture);
		assertEquals("Transaction Received", response.getMessage());
	}


}


