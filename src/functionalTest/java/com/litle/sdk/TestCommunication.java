package com.litle.sdk;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.FileInputStream;
import java.util.Properties;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.litle.sdk.Communication;
import com.litle.sdk.Configuration;
import com.litle.sdk.LitleOnline;
import com.litle.sdk.LitleOnlineException;
import com.litle.sdk.generate.ApplepayHeaderType;
import com.litle.sdk.generate.ApplepayType;
import com.litle.sdk.generate.Authorization;
import com.litle.sdk.generate.AuthorizationResponse;
import com.litle.sdk.generate.CardType;
import com.litle.sdk.generate.Contact;
import com.litle.sdk.generate.DetailTax;
import com.litle.sdk.generate.EnhancedData;
import com.litle.sdk.generate.MethodOfPaymentTypeEnum;
import com.litle.sdk.generate.OrderSourceType;
import com.litle.sdk.generate.PayPal;
import com.litle.sdk.generate.Pos;
import com.litle.sdk.generate.PosCapabilityTypeEnum;
import com.litle.sdk.generate.PosCardholderIdTypeEnum;
import com.litle.sdk.generate.PosEntryModeTypeEnum;
import com.litle.sdk.generate.ProcessingTypeEnum;

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

}