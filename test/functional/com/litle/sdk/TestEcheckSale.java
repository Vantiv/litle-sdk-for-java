package com.litle.sdk;

import static org.junit.Assert.*;

import java.math.BigInteger;

import javax.xml.bind.JAXBElement;

import org.junit.Test;

import com.litle.sdk.generate.AuthReversal;
import com.litle.sdk.generate.AuthReversalResponse;
import com.litle.sdk.generate.AuthorizationResponse;
import com.litle.sdk.generate.CardType;
import com.litle.sdk.generate.Contact;
import com.litle.sdk.generate.EcheckAccountTypeEnum;
import com.litle.sdk.generate.EcheckRedeposit;
import com.litle.sdk.generate.EcheckRedepositResponse;
import com.litle.sdk.generate.EcheckSale;
import com.litle.sdk.generate.EcheckSalesResponse;
import com.litle.sdk.generate.EcheckType;
import com.litle.sdk.generate.ObjectFactory;

public class TestEcheckSale {

	@Test
	public void simpleEcheckSaleWithEcheck() throws Exception{
		EcheckSale echecksale = new EcheckSale();
		echecksale.setAmount(BigInteger.valueOf(123456L));
		echecksale.setOrderId("12345");
		echecksale.setOrderSource("ecommerce");
		EcheckType echeck = new EcheckType();
		echeck.setAccType(EcheckAccountTypeEnum.CHECKING);
		echeck.setAccNum("12345657890");
		echeck.setRoutingNum("123456789");
		echeck.setCheckNum("123455");
		JAXBElement<EcheckType> createEcheckOrEcheckToken = new ObjectFactory().createEcheck(echeck);
		echecksale.setEcheckOrEcheckToken(createEcheckOrEcheckToken);
		Contact contact = new Contact();
		contact.setName("Bob");
		contact.setCity("lowell");
		contact.setState("MA");
		contact.setEmail("litle.com");
		echecksale.setBillToAddress(contact);
		EcheckSalesResponse response = new LitleOnline().echecksale(echecksale);
		assertEquals("Approved", response.getMessage());
	}

}

