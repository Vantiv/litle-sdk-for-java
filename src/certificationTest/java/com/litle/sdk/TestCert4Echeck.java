package com.litle.sdk;


import static org.junit.Assert.assertEquals;

import java.io.FileInputStream;
import java.util.Properties;

import org.junit.BeforeClass;
import org.junit.Test;

import com.litle.sdk.generate.Contact;
import com.litle.sdk.generate.EcheckAccountTypeEnum;
import com.litle.sdk.generate.EcheckCredit;
import com.litle.sdk.generate.EcheckCreditResponse;
import com.litle.sdk.generate.EcheckSale;
import com.litle.sdk.generate.EcheckSalesResponse;
import com.litle.sdk.generate.EcheckType;
import com.litle.sdk.generate.EcheckVerification;
import com.litle.sdk.generate.EcheckVerificationResponse;
import com.litle.sdk.generate.OrderSourceType;

public class TestCert4Echeck {

	private static LitleOnline litle;

	@BeforeClass
	public static void beforeClass() throws Exception {
        Properties config = new Properties();
        FileInputStream fileInputStream = new FileInputStream((new Configuration()).location());
        config.load(fileInputStream);
        config.setProperty("url", "https://prelive.litle.com/vap/communicator/online");
		config.setProperty("proxyHost", "");
		config.setProperty("proxyPort", "");
        litle = new LitleOnline(config);
	}

	@Test
	public void test37() throws Exception {
		EcheckVerification verification = new EcheckVerification();
		verification.setOrderId("37");
		verification.setAmount(3001L);
		verification.setOrderSource(OrderSourceType.TELEPHONE);
		Contact billToAddress = new Contact();
		billToAddress.setFirstName("Tom");
		billToAddress.setLastName("Black");
		verification.setBillToAddress(billToAddress);
		EcheckType echeck = new EcheckType();
		echeck.setAccNum("10@BC99999");
		echeck.setAccType(EcheckAccountTypeEnum.CHECKING);
		echeck.setRoutingNum("053100300");
		verification.setEcheck(echeck);
		verification.setId("id");

		EcheckVerificationResponse response = litle.echeckVerification(verification);
		assertEquals(response.getMessage(),"301", response.getResponse());
		assertEquals(response.getMessage(),"Invalid Account Number", response.getMessage());
	}

	@Test
	public void test38() throws Exception {
		EcheckVerification verification = new EcheckVerification();
		verification.setOrderId("38");
		verification.setAmount(3002L);
		verification.setOrderSource(OrderSourceType.TELEPHONE);
		Contact billToAddress = new Contact();
		billToAddress.setFirstName("John");
		billToAddress.setLastName("Smith");
		billToAddress.setPhone("999-999-9999");
		verification.setBillToAddress(billToAddress);
		EcheckType echeck = new EcheckType();
		echeck.setAccNum("1099999999");
		echeck.setAccType(EcheckAccountTypeEnum.CHECKING);
		echeck.setRoutingNum("053000219");
		verification.setEcheck(echeck);
		verification.setId("id");


		EcheckVerificationResponse response = litle.echeckVerification(verification);
		assertEquals(response.getMessage(),"000", response.getResponse());
		assertEquals(response.getMessage(),"Approved", response.getMessage());
	}

	@Test
	public void test39() throws Exception {
		EcheckVerification verification = new EcheckVerification();
		verification.setOrderId("39");
		verification.setAmount(3003L);
		verification.setOrderSource(OrderSourceType.TELEPHONE);
		Contact billToAddress = new Contact();
		billToAddress.setFirstName("Robert");
		billToAddress.setLastName("Jones");
		billToAddress.setCompanyName("Good Goods Inc");
		billToAddress.setPhone("9999999999");
		verification.setBillToAddress(billToAddress);
		EcheckType echeck = new EcheckType();
		echeck.setAccNum("3099999999");
		echeck.setAccType(EcheckAccountTypeEnum.CORPORATE);
		echeck.setRoutingNum("053100300");
		verification.setEcheck(echeck);
		verification.setId("id");

		EcheckVerificationResponse response = litle.echeckVerification(verification);
		assertEquals(response.getMessage(),"950", response.getResponse());
		assertEquals(response.getMessage(),"Decline - Negative Information on File", response.getMessage());
	}

	@Test
	public void test40() throws Exception {
		EcheckVerification verification = new EcheckVerification();
		verification.setOrderId("40");
		verification.setAmount(3004L);
		verification.setOrderSource(OrderSourceType.TELEPHONE);
		Contact billToAddress = new Contact();
		billToAddress.setFirstName("Peter");
		billToAddress.setLastName("Green");
		billToAddress.setCompanyName("Green Co");
		billToAddress.setPhone("9999999999");
		verification.setBillToAddress(billToAddress);
		EcheckType echeck = new EcheckType();
		echeck.setAccNum("8099999999");
		echeck.setAccType(EcheckAccountTypeEnum.CORPORATE);
		echeck.setRoutingNum("063102152");
		verification.setEcheck(echeck);
		verification.setId("id");

		EcheckVerificationResponse response = litle.echeckVerification(verification);
		assertEquals(response.getMessage(),"951", response.getResponse());
		assertEquals(response.getMessage(),"Absolute Decline", response.getMessage());
	}

	@Test
	public void test41() throws Exception {
		EcheckSale sale = new EcheckSale();
		sale.setOrderId("41");
		sale.setAmount(2008L);
		sale.setOrderSource(OrderSourceType.TELEPHONE);
		Contact billToAddress = new Contact();
		billToAddress.setFirstName("Mike");
		billToAddress.setMiddleInitial("J");
		billToAddress.setLastName("Hammer");
		sale.setBillToAddress(billToAddress);
		EcheckType echeck = new EcheckType();
		echeck.setAccNum("10@BC99999");
		echeck.setAccType(EcheckAccountTypeEnum.CHECKING);
		echeck.setRoutingNum("053100300");
		sale.setEcheck(echeck);
		sale.setId("id");


		EcheckSalesResponse response = litle.echeckSale(sale);
		assertEquals(response.getMessage(),"301", response.getResponse());
		assertEquals(response.getMessage(),"Invalid Account Number", response.getMessage());
	}

	@Test
	public void test42() throws Exception {
		EcheckSale sale = new EcheckSale();
		sale.setOrderId("42");
		sale.setAmount(2004L);
		sale.setOrderSource(OrderSourceType.TELEPHONE);
		Contact billToAddress = new Contact();
		billToAddress.setFirstName("Tom");
		billToAddress.setLastName("Black");
		sale.setBillToAddress(billToAddress);
		EcheckType echeck = new EcheckType();
		echeck.setAccNum("4099999992");
		echeck.setAccType(EcheckAccountTypeEnum.CHECKING);
		echeck.setRoutingNum("211370545");
		sale.setEcheck(echeck);
		sale.setId("id");

		EcheckSalesResponse response = litle.echeckSale(sale);
		assertEquals(response.getMessage(),"000", response.getResponse());
		assertEquals(response.getMessage(),"Approved", response.getMessage());
	}

	@Test
	public void test43() throws Exception {
		EcheckSale sale = new EcheckSale();
		sale.setOrderId("43");
		sale.setAmount(2007L);
		sale.setOrderSource(OrderSourceType.TELEPHONE);
		Contact billToAddress = new Contact();
		billToAddress.setFirstName("Peter");
		billToAddress.setLastName("Green");
		billToAddress.setCompanyName("Green Co");
		sale.setBillToAddress(billToAddress);
		EcheckType echeck = new EcheckType();
		echeck.setAccNum("6099999992");
		echeck.setAccType(EcheckAccountTypeEnum.CORPORATE);
		echeck.setRoutingNum("211370545");
		sale.setEcheck(echeck);
		sale.setId("id");

		EcheckSalesResponse response = litle.echeckSale(sale);
		assertEquals(response.getMessage(),"000", response.getResponse());
		assertEquals(response.getMessage(),"Approved", response.getMessage());
	}

	@Test
	public void test44() throws Exception {
		EcheckSale sale = new EcheckSale();
		sale.setOrderId("44");
		sale.setAmount(2009L);
		sale.setOrderSource(OrderSourceType.TELEPHONE);
		Contact billToAddress = new Contact();
		billToAddress.setFirstName("Peter");
		billToAddress.setLastName("Green");
		billToAddress.setCompanyName("Green Co");
		sale.setBillToAddress(billToAddress);
		EcheckType echeck = new EcheckType();
		echeck.setAccNum("9099999992");
		echeck.setAccType(EcheckAccountTypeEnum.CORPORATE);
		echeck.setRoutingNum("053133052");
		sale.setEcheck(echeck);
		sale.setId("id");

		EcheckSalesResponse response = litle.echeckSale(sale);
		assertEquals(response.getMessage(),"900", response.getResponse());
		assertEquals(response.getMessage(),"Invalid Bank Routing Number", response.getMessage());
	}

	@Test
	public void test45() throws Exception {
		EcheckCredit credit = new EcheckCredit();
		credit.setOrderId("45");
		credit.setAmount(1001L);
		credit.setOrderSource(OrderSourceType.TELEPHONE);
		Contact billToAddress = new Contact();
		billToAddress.setFirstName("John");
		billToAddress.setLastName("Smith");
		credit.setBillToAddress(billToAddress);
		EcheckType echeck = new EcheckType();
		echeck.setAccNum("10@BC99999");
		echeck.setAccType(EcheckAccountTypeEnum.CHECKING);
		echeck.setRoutingNum("053100300");
		credit.setEcheck(echeck);
		credit.setId("id");

		EcheckCreditResponse response = litle.echeckCredit(credit);
		assertEquals(response.getMessage(),"301", response.getResponse());
		assertEquals(response.getMessage(),"Invalid Account Number", response.getMessage());
	}

	@Test
	public void test46() throws Exception {
		EcheckCredit credit = new EcheckCredit();
		credit.setOrderId("46");
		credit.setAmount(1003L);
		credit.setOrderSource(OrderSourceType.TELEPHONE);
		Contact billToAddress = new Contact();
		billToAddress.setFirstName("Robert");
		billToAddress.setLastName("Jones");
		billToAddress.setCompanyName("Widget Inc");
		credit.setBillToAddress(billToAddress);
		EcheckType echeck = new EcheckType();
		echeck.setAccNum("3099999999");
		echeck.setAccType(EcheckAccountTypeEnum.CORPORATE);
		echeck.setRoutingNum("063102152");
		credit.setEcheck(echeck);
		credit.setId("id");

		EcheckCreditResponse response = litle.echeckCredit(credit);
		assertEquals(response.getMessage(),"000", response.getResponse());
		assertEquals(response.getMessage(),"Approved", response.getMessage());
	}

	@Test
	public void test47() throws Exception {
		EcheckCredit credit = new EcheckCredit();
		credit.setOrderId("47");
		credit.setAmount(1007L);
		credit.setOrderSource(OrderSourceType.TELEPHONE);
		Contact billToAddress = new Contact();
		billToAddress.setFirstName("Peter");
		billToAddress.setLastName("Green");
		billToAddress.setCompanyName("Green Co");
		credit.setBillToAddress(billToAddress);
		EcheckType echeck = new EcheckType();
		echeck.setAccNum("6099999993");
		echeck.setAccType(EcheckAccountTypeEnum.CORPORATE);
		echeck.setRoutingNum("211370545");
		credit.setEcheck(echeck);
		credit.setId("id");

		EcheckCreditResponse response = litle.echeckCredit(credit);
		assertEquals(response.getMessage(),"000", response.getResponse());
		assertEquals(response.getMessage(),"Approved", response.getMessage());
	}

	@Test
	public void test48() throws Exception {
		EcheckCredit credit = new EcheckCredit();
		credit.setLitleTxnId(430000000000000001L);
		credit.setId("id");

		EcheckCreditResponse response = litle.echeckCredit(credit);
		assertEquals(response.getMessage(),"000", response.getResponse());
		assertEquals(response.getMessage(),"Approved", response.getMessage());
	}

	@Test
	public void test49() throws Exception {
		EcheckCredit credit = new EcheckCredit();
		credit.setLitleTxnId(2L);
		credit.setId("id");

		EcheckCreditResponse response = litle.echeckCredit(credit);
		assertEquals(response.getMessage(),"000", response.getResponse());
		assertEquals(response.getMessage(),"Approved", response.getMessage());
	}

}
