package com.litle.sdk;

import static org.junit.Assert.*;

import java.math.BigInteger;

import javax.xml.bind.JAXBElement;

import org.junit.Test;

import com.litle.sdk.generate.AuthReversal;
import com.litle.sdk.generate.AuthReversalResponse;
import com.litle.sdk.generate.AuthorizationResponse;
import com.litle.sdk.generate.Contact;
import com.litle.sdk.generate.EcheckAccountTypeEnum;
import com.litle.sdk.generate.EcheckSale;
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
		EcheckVerificationResponse response = new LitleOnline().echeckverification(echeckverification);
		assertEquals("Approved", response.getMessage());
	}

}

