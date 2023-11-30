package io.github.vantiv.sdk;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

import io.github.vantiv.sdk.generate.RFRResponse;

public class TestLitleRFRResponse {

	@Before
	public void before() throws Exception {
	}

	@Test
	public void testSetRFRResponse() throws Exception {
		RFRResponse rfrResponse = new RFRResponse();
		rfrResponse.setMessage("Hurrdurrburrrrrr");
		rfrResponse.setResponse("Nuh uh. :(");
		LitleRFRResponse litleRFRResponse = new LitleRFRResponse(rfrResponse);
		assertEquals("Nuh uh. :(", litleRFRResponse.getRFRResponseCode());
		assertEquals("Hurrdurrburrrrrr", litleRFRResponse.getRFRResponseMessage());
	}

}
