package com.litle.sdk;

import static org.junit.Assert.*;
import static org.junit.Assert.assertEquals;

import java.math.BigInteger;

import javax.xml.bind.JAXBElement;

import org.junit.Test;

import com.litle.sdk.generate.Contact;
import com.litle.sdk.generate.EcheckAccountTypeEnum;
import com.litle.sdk.generate.EcheckTokenType;
import com.litle.sdk.generate.EcheckType;
import com.litle.sdk.generate.EcheckVerification;
import com.litle.sdk.generate.EcheckVerificationResponse;
import com.litle.sdk.generate.ObjectFactory;

public class TestEcheckVerification {

	@Test
	public void simpleEcheckVerification() throws Exception{
		EcheckVerification echeckverification = new EcheckVerification();
		echeckverification.setAmount(BigInteger.valueOf(123456L));
		echeckverification.setOrderId("12345");
		echeckverification.setOrderSource("ecommerce");
		EcheckType echeck = new EcheckType();
		echeck.setAccType(EcheckAccountTypeEnum.CHECKING);
		echeck.setAccNum("12345657890");
		echeck.setRoutingNum("123456789");
		echeck.setCheckNum("123455");
		JAXBElement<EcheckType> createEcheckOrEcheckToken = new ObjectFactory().createEcheck(echeck);
		echeckverification.setEcheckOrEcheckToken(createEcheckOrEcheckToken);
		Contact contact = new Contact();
		contact.setName("Bob");
		contact.setCity("lowell");
		contact.setState("MA");
		contact.setEmail("litle.com");
		echeckverification.setBillToAddress(contact);
		EcheckVerificationResponse response = new LitleOnline().echeckVerification(echeckverification);
		assertEquals("Approved", response.getMessage());
	}
	
	@Test
	public void echeckVerificationWithEcheckToken() throws Exception{
		EcheckVerification echeckverification = new EcheckVerification();
		echeckverification.setAmount(BigInteger.valueOf(123456L));
		echeckverification.setOrderId("12345");
		echeckverification.setOrderSource("ecommerce");
		EcheckTokenType echeck = new EcheckTokenType();
		echeck.setAccType(EcheckAccountTypeEnum.CHECKING);
		echeck.setLitleToken("1234565789012");
		echeck.setRoutingNum("123456789");
		echeck.setCheckNum("123455");
		JAXBElement<EcheckTokenType> echeckToken = new ObjectFactory().createEcheckToken(echeck); 
		echeckverification.setEcheckOrEcheckToken(echeckToken);
		Contact contact = new Contact();
		contact.setName("Bob");
		contact.setCity("lowell");
		contact.setState("MA");
		contact.setEmail("litle.com");
		echeckverification.setBillToAddress(contact);
		EcheckVerificationResponse response = new LitleOnline().echeckVerification(echeckverification);
		assertEquals("Approved", response.getMessage());
	}
	
	@Test
	public void testMissingBillingField() throws Exception {
		EcheckVerification echeckVerification = new EcheckVerification();
		echeckVerification.setReportGroup("Planets");
		echeckVerification.setAmount(BigInteger.valueOf(123L));
		EcheckType echeck = new EcheckType();
		echeck.setAccType(EcheckAccountTypeEnum.CHECKING);
		echeck.setAccNum("12345657890");
		echeck.setRoutingNum("123456789");
		echeck.setCheckNum("123455");
		JAXBElement<?> invalidEcheck = new ObjectFactory().createEcheck(echeck);
		echeckVerification.setEcheckOrEcheckToken(invalidEcheck);
		echeckVerification.setOrderId("12345");
		echeckVerification.setOrderSource("ecommerce");
		try {
			new LitleOnline().echeckVerification(echeckVerification);
			fail("Expected exception");
		} catch(LitleOnlineException e) {
			assertEquals("Error validating xml data against the schema", e.getMessage());
		}
		
	}

}

