package io.github.vantiv.sdk;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Before;
import org.junit.Test;

public class TestCommunication {

	private Communication communication;

	@Before
	public void setup() throws Exception {
		communication = new Communication();
	}

	@Test
	public void testNeuterXml() {
		String xml = null;
		assertNull(communication.neuterXml(xml));

		xml = "";
		assertEquals("", communication.neuterXml(xml));

		xml = "<?xml version=1.0 encoding=UTF-8 standalone=yes?>" +
				"<litleOnlineRequest merchantId=123456 merchantSdk=Java;11.3.0 version=11.3 xmlns=http://www.litle.com/schema>" +
				"<authentication>" +
				"<user>DummyUser</user>" +
				"<password>DummyPass</password>" +
				"</authentication>" +
				"<authorization reportGroup=Planets id=id>" +
				"<orderId>12344</orderId>" +
				"<amount>106</amount>" +
				"<orderSource>ecommerce</orderSource>" +
				"<card>" +
				"<type>VI</type>" +
				"<number>4100000000000000</number>" +
				"<track>dummy track data</track>" +
				"<expDate>1210</expDate>" +
				"</card>" +
				"<echeck>" +
				"<accType>Checking</accType>" +
				"<accNum>1234567890</accNum>" +
				"</echeck>" +
				"</authorization>" +
				"</litleOnlineRequest>";
		String neuteredXml = "<?xml version=1.0 encoding=UTF-8 standalone=yes?>" +
				"<litleOnlineRequest merchantId=123456 merchantSdk=Java;11.3.0 version=11.3 xmlns=http://www.litle.com/schema>" +
				"<authentication>" +
				"<user>NEUTERED</user>" +
				"<password>NEUTERED</password>" +
				"</authentication>" +
				"<authorization reportGroup=Planets id=id>" +
				"<orderId>12344</orderId>" +
				"<amount>106</amount>" +
				"<orderSource>ecommerce</orderSource>" +
				"<card>" +
				"<type>VI</type>" +
				"<number>NEUTERED</number>" +
				"<track>NEUTERED</track>" +
				"<expDate>1210</expDate>" +
				"</card>" +
				"<echeck>" +
				"<accType>Checking</accType>" +
				"<accNum>NEUTERED</accNum>" +
				"</echeck>" +
				"</authorization>" +
				"</litleOnlineRequest>";
		assertEquals(neuteredXml, communication.neuterXml(xml));
	}

	@Test
	public void testGetBestProtocol() {
		assertEquals("TLSv1.2", Communication.getBestProtocol(new String[] {"TLSv1.1", "TLSv1.2"}));
	}

}