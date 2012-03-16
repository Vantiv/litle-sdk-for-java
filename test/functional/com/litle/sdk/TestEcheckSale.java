package com.litle.sdk;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.math.BigInteger;

import javax.xml.bind.JAXBElement;

import org.junit.BeforeClass;
import org.junit.Test;

import com.litle.sdk.generate.Contact;
import com.litle.sdk.generate.CustomBilling;
import com.litle.sdk.generate.EcheckAccountTypeEnum;
import com.litle.sdk.generate.EcheckSale;
import com.litle.sdk.generate.EcheckSalesResponse;
import com.litle.sdk.generate.EcheckTokenType;
import com.litle.sdk.generate.EcheckType;
import com.litle.sdk.generate.ObjectFactory;

public class TestEcheckSale {

	private static LitleOnline litle;

	@BeforeClass
	public static void beforeClass() throws Exception {
		litle = new LitleOnline();
	}
	
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
		EcheckSalesResponse response = litle.echeckSale(echecksale);
		assertEquals("Approved", response.getMessage());
	}
	
	@Test
	public void noAmount() throws Exception {
		EcheckSale echeckSale = new EcheckSale();
		echeckSale.setReportGroup("Planets");
		try {
			litle.echeckSale(echeckSale);
			fail("Expected exception");
		} catch(LitleOnlineException e) {
			assertEquals("Error validating xml data against the schema", e.getMessage());
		}
	}

	@Test
	public void echeckSaleWithShipTo() throws Exception{
		EcheckSale echecksale = new EcheckSale();
		echecksale.setReportGroup("Planets");
		echecksale.setAmount(BigInteger.valueOf(123456L));
		echecksale.setVerify(true);
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
		echecksale.setShipToAddress(contact);
		EcheckSalesResponse response = litle.echeckSale(echecksale);
		assertEquals("Approved", response.getMessage());
	}
	
	@Test
	public void echeckSaleWithEcheckToken() throws Exception{
		EcheckSale echecksale = new EcheckSale();
		echecksale.setReportGroup("Planets");
		echecksale.setAmount(BigInteger.valueOf(123456L));
		echecksale.setVerify(true);
		echecksale.setOrderId("12345");
		echecksale.setOrderSource("ecommerce");
		EcheckTokenType echeck = new EcheckTokenType();
		echeck.setAccType(EcheckAccountTypeEnum.CHECKING);
		echeck.setLitleToken("1234565789012");
		echeck.setRoutingNum("123456789");
		echeck.setCheckNum("123455");
		JAXBElement<EcheckTokenType> createEcheckOrEcheckToken = new ObjectFactory().createEcheckToken(echeck);
		echecksale.setEcheckOrEcheckToken(createEcheckOrEcheckToken);
		CustomBilling customBilling = new CustomBilling();
		customBilling.setPhone("123456789");
		customBilling.setDescriptor("good");
		echecksale.setCustomBilling(customBilling);
		Contact contact = new Contact();
		contact.setName("Bob");
		contact.setCity("lowell");
		contact.setState("MA");
		contact.setEmail("litle.com");
		echecksale.setBillToAddress(contact);
		EcheckSalesResponse response = litle.echeckSale(echecksale);
		assertEquals("Approved", response.getMessage());
	}
	
	@Test
	public void echeckSaleMissingBilling() throws Exception{
		EcheckSale echecksale = new EcheckSale();
		echecksale.setReportGroup("Planets");
		echecksale.setAmount(BigInteger.valueOf(123456L));
		EcheckTokenType echeck = new EcheckTokenType();
		echeck.setAccType(EcheckAccountTypeEnum.CHECKING);
		echeck.setLitleToken("1234565789012");
		echeck.setRoutingNum("123456789");
		echeck.setCheckNum("123455");
		JAXBElement<EcheckTokenType> createEcheckOrEcheckToken = new ObjectFactory().createEcheckToken(echeck);
		echecksale.setEcheckOrEcheckToken(createEcheckOrEcheckToken);
		echecksale.setVerify(true);
		echecksale.setOrderId("12345");
		echecksale.setOrderSource("ecommerce");
		
		try {
			litle.echeckSale(echecksale);
			fail("Expected exception");
		} catch(LitleOnlineException e) {
			assertEquals("Error validating xml data against the schema", e.getMessage());
		}
	}
	
	@Test
	public void simpleEcheckSale() throws Exception {
		EcheckSale echecksale = new EcheckSale();
		echecksale.setReportGroup("Planets");
		echecksale.setLitleTxnId(123456789101112L);
		echecksale.setAmount(BigInteger.valueOf(12L));
		EcheckSalesResponse response = litle.echeckSale(echecksale);
		assertEquals("Approved", response.getMessage());
	}

}

