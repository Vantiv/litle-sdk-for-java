package com.litle.sdk;


import static org.junit.Assert.assertEquals;

import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.Assume;

import com.litle.sdk.generate.Authorization;
import com.litle.sdk.generate.AuthorizationResponse;
import com.litle.sdk.generate.Capture;
import com.litle.sdk.generate.CaptureResponse;
import com.litle.sdk.generate.CardType;
import com.litle.sdk.generate.Contact;
import com.litle.sdk.generate.CountryTypeEnum;
import com.litle.sdk.generate.Credit;
import com.litle.sdk.generate.CreditResponse;
import com.litle.sdk.generate.FraudCheckType;
import com.litle.sdk.generate.MethodOfPaymentTypeEnum;
import com.litle.sdk.generate.OrderSourceType;
import com.litle.sdk.generate.Sale;
import com.litle.sdk.generate.SaleResponse;
import com.litle.sdk.generate.VoidResponse;

import java.io.FileInputStream;
import java.util.Properties;

public class TestCert1Base {

	private static LitleOnline litle;
	
	private String preliveStatus = System.getenv("preliveStatus");

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
	public void test1Auth() throws Exception { 
		Assume.assumeFalse(this.preliveStatus.equalsIgnoreCase("down"));
		Authorization authorization = new Authorization();
		authorization.setOrderId("1");
		authorization.setAmount(10010L);
		authorization.setOrderSource(OrderSourceType.ECOMMERCE);
		Contact contact = new Contact();
		contact.setName("John Smith");
		contact.setAddressLine1("1 Main St.");
		contact.setCity("Burlington");
		contact.setState("MA");
		contact.setZip("01803-3747");
		contact.setCountry(CountryTypeEnum.US);
		authorization.setBillToAddress(contact);
		CardType card = new CardType();
		card.setType(MethodOfPaymentTypeEnum.VI);
		card.setNumber("4457010000000009");
		card.setExpDate("0112");
		card.setCardValidationNum("349");
		authorization.setCard(card);
		
		AuthorizationResponse response = litle.authorize(authorization);
		assertEquals(response.getMessage(), "000",response.getResponse());
		assertEquals(response.getMessage(), "Approved",response.getMessage());
		assertEquals(response.getMessage(), "11111 ",response.getAuthCode());
		assertEquals(response.getMessage(), "01",response.getFraudResult().getAvsResult());
		assertEquals(response.getMessage(), "M",response.getFraudResult().getCardValidationResult());
		
		Capture capture = new Capture();
		capture.setLitleTxnId(response.getLitleTxnId());
		CaptureResponse captureresponse = litle.capture(capture);
		assertEquals(captureresponse.getMessage(), "000",captureresponse.getResponse());
		assertEquals(captureresponse.getMessage(), "Approved",captureresponse.getMessage());
		
		Credit credit = new Credit();
		credit.setLitleTxnId(captureresponse.getLitleTxnId());
		CreditResponse creditresponse = litle.credit(credit);
		assertEquals(creditresponse.getMessage(), "000",creditresponse.getResponse());
		assertEquals(creditresponse.getMessage(), "Approved",creditresponse.getMessage());
		
		com.litle.sdk.generate.Void newvoid = new com.litle.sdk.generate.Void();
		newvoid.setLitleTxnId(creditresponse.getLitleTxnId());
		VoidResponse voidresponse = litle.dovoid(newvoid);
		assertEquals(voidresponse.getMessage(), "000",voidresponse.getResponse());
		assertEquals(voidresponse.getMessage(), "Approved",voidresponse.getMessage());
	}
	
	@Test
	public void test1AVS() throws Exception { 
		Assume.assumeFalse(this.preliveStatus.equalsIgnoreCase("down"));
		Authorization authorization = new Authorization();
		authorization.setOrderId("1");
		authorization.setAmount(000L);
		authorization.setOrderSource(OrderSourceType.ECOMMERCE);
		Contact contact = new Contact();
		contact.setName("John Smith");
		contact.setAddressLine1("1 Main St.");
		contact.setCity("Burlington");
		contact.setState("MA");
		contact.setZip("01803-3747");
		contact.setCountry(CountryTypeEnum.US);
		authorization.setBillToAddress(contact);
		CardType card = new CardType();
		card.setType(MethodOfPaymentTypeEnum.VI);
		card.setNumber("4457010000000009");
		card.setExpDate("0112");
		card.setCardValidationNum("349");
		authorization.setCard(card);
		
		AuthorizationResponse response = litle.authorize(authorization);
		assertEquals(response.getMessage(), "000",response.getResponse());
		assertEquals(response.getMessage(), "Approved",response.getMessage());
		assertEquals(response.getMessage(), "11111 ",response.getAuthCode());
		assertEquals(response.getMessage(), "01",response.getFraudResult().getAvsResult());
		assertEquals(response.getMessage(), "M",response.getFraudResult().getCardValidationResult());
		
	}
	
	@Test
	public void test1Sale() throws Exception { 
		Assume.assumeFalse(this.preliveStatus.equalsIgnoreCase("down"));
		Sale sale = new Sale();
		sale.setOrderId("1");
		sale.setAmount(10010L);
		sale.setOrderSource(OrderSourceType.ECOMMERCE);
		Contact contact = new Contact();
		contact.setName("John Smith");
		contact.setAddressLine1("1 Main St.");
		contact.setCity("Burlington");
		contact.setState("MA");
		contact.setZip("01803-3747");
		contact.setCountry(CountryTypeEnum.US);
		sale.setBillToAddress(contact);
		CardType card = new CardType();
		card.setType(MethodOfPaymentTypeEnum.VI);
		card.setNumber("4457010000000009");
		card.setExpDate("0112");
		card.setCardValidationNum("349");
		sale.setCard(card);
		
		SaleResponse response = litle.sale(sale);
		assertEquals(response.getMessage(), "000",response.getResponse());
		assertEquals(response.getMessage(), "Approved",response.getMessage());
		assertEquals(response.getMessage(), "11111 ",response.getAuthCode());
		assertEquals(response.getMessage(), "01",response.getFraudResult().getAvsResult());
		assertEquals(response.getMessage(), "M",response.getFraudResult().getCardValidationResult());
		
		Credit credit = new Credit();
		credit.setLitleTxnId(response.getLitleTxnId());
		CreditResponse creditresponse = litle.credit(credit);
		assertEquals(creditresponse.getMessage(), "000",creditresponse.getResponse());
		assertEquals(creditresponse.getMessage(), "Approved",creditresponse.getMessage());
		
		com.litle.sdk.generate.Void newvoid = new com.litle.sdk.generate.Void();
		newvoid.setLitleTxnId(creditresponse.getLitleTxnId());
		VoidResponse voidresponse = litle.dovoid(newvoid);
		assertEquals(voidresponse.getMessage(), "000",voidresponse.getResponse());
		assertEquals(voidresponse.getMessage(), "Approved",voidresponse.getMessage());
	}
	
	@Test
	public void test2Auth() throws Exception { 
		Assume.assumeFalse(this.preliveStatus.equalsIgnoreCase("down"));
		Authorization authorization = new Authorization();
		authorization.setOrderId("2");
		authorization.setAmount(20020L);
		authorization.setOrderSource(OrderSourceType.ECOMMERCE);
		Contact contact = new Contact();
		contact.setName("Mike J. Hammer");
		contact.setAddressLine1("2 Main St.");
		contact.setAddressLine2("Apt. 222");
		contact.setCity("Riverside");
		contact.setState("RI");
		contact.setZip("02915");
		contact.setCountry(CountryTypeEnum.US);
		authorization.setBillToAddress(contact);
		CardType card = new CardType();
		card.setType(MethodOfPaymentTypeEnum.MC);
		card.setNumber("5112010000000003");
		card.setExpDate("0212");
		card.setCardValidationNum("261");
		authorization.setCard(card);
		FraudCheckType authenticationvalue = new FraudCheckType();
		authenticationvalue.setAuthenticationValue("BwABBJQ1AgAAAAAgJDUCAAAAAAA=");
		authorization.setCardholderAuthentication(authenticationvalue);
		
		AuthorizationResponse response = litle.authorize(authorization);
		assertEquals(response.getMessage(), "000",response.getResponse());
		assertEquals(response.getMessage(), "Approved",response.getMessage());
		assertEquals(response.getMessage(), "22222 ",response.getAuthCode());
		assertEquals(response.getMessage(), "10",response.getFraudResult().getAvsResult());
		assertEquals(response.getMessage(), "M",response.getFraudResult().getCardValidationResult());
		
		Capture capture = new Capture();
		capture.setLitleTxnId(response.getLitleTxnId());
		CaptureResponse captureresponse = litle.capture(capture);
		assertEquals(captureresponse.getMessage(), "000",captureresponse.getResponse());
		assertEquals(captureresponse.getMessage(), "Approved",captureresponse.getMessage());
		
		Credit credit = new Credit();
		credit.setLitleTxnId(captureresponse.getLitleTxnId());
		CreditResponse creditresponse = litle.credit(credit);
		assertEquals(creditresponse.getMessage(), "000",creditresponse.getResponse());
		assertEquals(creditresponse.getMessage(), "Approved",creditresponse.getMessage());
		
		com.litle.sdk.generate.Void newvoid = new com.litle.sdk.generate.Void();
		newvoid.setLitleTxnId(creditresponse.getLitleTxnId());
		VoidResponse voidresponse = litle.dovoid(newvoid);
		assertEquals(voidresponse.getMessage(), "000",voidresponse.getResponse());
		assertEquals(voidresponse.getMessage(), "Approved",voidresponse.getMessage());
	}
	
	@Test
	public void test2AVS() throws Exception { 
		Assume.assumeFalse(this.preliveStatus.equalsIgnoreCase("down"));
		Authorization authorization = new Authorization();
		authorization.setOrderId("2");
		authorization.setAmount(000L);
		authorization.setOrderSource(OrderSourceType.ECOMMERCE);
		Contact contact = new Contact();
		contact.setName("Mike J. Hammer");
		contact.setAddressLine1("2 Main St.");
		contact.setAddressLine2("Apt. 222");
		contact.setCity("Riverside");
		contact.setState("RI");
		contact.setZip("02915");
		contact.setCountry(CountryTypeEnum.US);
		authorization.setBillToAddress(contact);
		CardType card = new CardType();
		card.setType(MethodOfPaymentTypeEnum.MC);
		card.setNumber("5112010000000003");
		card.setExpDate("0212");
		card.setCardValidationNum("261");
		authorization.setCard(card);
		FraudCheckType authenticationvalue = new FraudCheckType();
		authenticationvalue.setAuthenticationValue("BwABBJQ1AgAAAAAgJDUCAAAAAAA=");
		authorization.setCardholderAuthentication(authenticationvalue);
		
		AuthorizationResponse response = litle.authorize(authorization);
		assertEquals(response.getMessage(), "000",response.getResponse());
		assertEquals(response.getMessage(), "Approved",response.getMessage());
		assertEquals(response.getMessage(), "22222 ",response.getAuthCode());
		assertEquals(response.getMessage(), "10",response.getFraudResult().getAvsResult());
		assertEquals(response.getMessage(), "M",response.getFraudResult().getCardValidationResult());
		
	}
	
	@Test
	public void test2Sale() throws Exception { 
		Assume.assumeFalse(this.preliveStatus.equalsIgnoreCase("down"));
		Sale sale = new Sale();
		sale.setOrderId("2");
		sale.setAmount(20020L);
		sale.setOrderSource(OrderSourceType.ECOMMERCE);
		Contact contact = new Contact();
		contact.setName("Mike J. Hammer");
		contact.setAddressLine1("2 Main St.");
		contact.setAddressLine2("Apt. 222");
		contact.setCity("Riverside");
		contact.setState("RI");
		contact.setZip("02915");
		contact.setCountry(CountryTypeEnum.US);
		sale.setBillToAddress(contact);
		CardType card = new CardType();
		card.setType(MethodOfPaymentTypeEnum.MC);
		card.setNumber("5112010000000003");
		card.setExpDate("0212");
		card.setCardValidationNum("261");
		sale.setCard(card);
		FraudCheckType authenticationvalue = new FraudCheckType();
		authenticationvalue.setAuthenticationValue("BwABBJQ1AgAAAAAgJDUCAAAAAAA=");
		sale.setCardholderAuthentication(authenticationvalue);
		
		SaleResponse response = litle.sale(sale);
		assertEquals(response.getMessage(), "000",response.getResponse());
		assertEquals(response.getMessage(), "Approved",response.getMessage());
		assertEquals(response.getMessage(), "22222 ",response.getAuthCode());
		assertEquals(response.getMessage(), "10",response.getFraudResult().getAvsResult());
		assertEquals(response.getMessage(), "M",response.getFraudResult().getCardValidationResult());
		
		Credit credit = new Credit();
		credit.setLitleTxnId(response.getLitleTxnId());
		CreditResponse creditresponse = litle.credit(credit);
		assertEquals(creditresponse.getMessage(), "000",creditresponse.getResponse());
		assertEquals(creditresponse.getMessage(), "Approved",creditresponse.getMessage());
		
		com.litle.sdk.generate.Void newvoid = new com.litle.sdk.generate.Void();
		newvoid.setLitleTxnId(creditresponse.getLitleTxnId());
		VoidResponse voidresponse = litle.dovoid(newvoid);
		assertEquals(voidresponse.getMessage(), "000",voidresponse.getResponse());
		assertEquals(voidresponse.getMessage(), "Approved",voidresponse.getMessage());
	}
	
	@Test
	public void test3Auth() throws Exception { 
		Assume.assumeFalse(this.preliveStatus.equalsIgnoreCase("down"));
		Authorization authorization = new Authorization();
		authorization.setOrderId("3");
		authorization.setAmount(30030L);
		authorization.setOrderSource(OrderSourceType.ECOMMERCE);
		Contact contact = new Contact();
		contact.setName("Eileen Jones");
		contact.setAddressLine1("3 Main St.");
		contact.setCity("Bloomfield");
		contact.setState("CT");
		contact.setZip("06002");
		contact.setCountry(CountryTypeEnum.US);
		authorization.setBillToAddress(contact);
		CardType card = new CardType();
		card.setType(MethodOfPaymentTypeEnum.DI);
		card.setNumber("6011010000000003");
		card.setExpDate("0312");
		card.setCardValidationNum("758");
		authorization.setCard(card);
		
		AuthorizationResponse response = litle.authorize(authorization);
		assertEquals(response.getMessage(), "000",response.getResponse());
		assertEquals(response.getMessage(), "Approved",response.getMessage());
		assertEquals(response.getMessage(), "33333 ",response.getAuthCode());
		assertEquals(response.getMessage(), "10",response.getFraudResult().getAvsResult());
		assertEquals(response.getMessage(), "M",response.getFraudResult().getCardValidationResult());
		
		Capture capture = new Capture();
		capture.setLitleTxnId(response.getLitleTxnId());
		CaptureResponse captureresponse = litle.capture(capture);
		assertEquals(captureresponse.getMessage(), "000",captureresponse.getResponse());
		assertEquals(captureresponse.getMessage(), "Approved",captureresponse.getMessage());
		
		Credit credit = new Credit();
		credit.setLitleTxnId(captureresponse.getLitleTxnId());
		CreditResponse creditresponse = litle.credit(credit);
		assertEquals(creditresponse.getMessage(), "000",creditresponse.getResponse());
		assertEquals(creditresponse.getMessage(), "Approved",creditresponse.getMessage());
		
		com.litle.sdk.generate.Void newvoid = new com.litle.sdk.generate.Void();
		newvoid.setLitleTxnId(creditresponse.getLitleTxnId());
		VoidResponse voidresponse = litle.dovoid(newvoid);
		assertEquals(voidresponse.getMessage(), "000",voidresponse.getResponse());
		assertEquals(voidresponse.getMessage(), "Approved",voidresponse.getMessage());
	}
	
	@Test
	public void test3AVS() throws Exception { 
		Assume.assumeFalse(this.preliveStatus.equalsIgnoreCase("down"));
		Authorization authorization = new Authorization();
		authorization.setOrderId("3");
		authorization.setAmount(000L);
		authorization.setOrderSource(OrderSourceType.ECOMMERCE);
		Contact contact = new Contact();
		contact.setName("Eileen Jones");
		contact.setAddressLine1("3 Main St.");
		contact.setCity("Bloomfield");
		contact.setState("CT");
		contact.setZip("06002");
		contact.setCountry(CountryTypeEnum.US);
		authorization.setBillToAddress(contact);
		CardType card = new CardType();
		card.setType(MethodOfPaymentTypeEnum.DI);
		card.setNumber("6011010000000003");
		card.setExpDate("0312");
		card.setCardValidationNum("758");
		authorization.setCard(card);
		
		AuthorizationResponse response = litle.authorize(authorization);
		assertEquals(response.getMessage(), "000",response.getResponse());
		assertEquals(response.getMessage(), "Approved",response.getMessage());
		assertEquals(response.getMessage(), "33333 ",response.getAuthCode());
		assertEquals(response.getMessage(), "10",response.getFraudResult().getAvsResult());
		assertEquals(response.getMessage(), "M",response.getFraudResult().getCardValidationResult());
		
	}
	
	@Test
	public void test3Sale() throws Exception { 
		Assume.assumeFalse(this.preliveStatus.equalsIgnoreCase("down"));
		Sale sale = new Sale();
		sale.setOrderId("3");
		sale.setAmount(30030L);
		sale.setOrderSource(OrderSourceType.ECOMMERCE);
		Contact contact = new Contact();
		contact.setName("Eileen Jones");
		contact.setAddressLine1("3 Main St.");
		contact.setCity("Bloomfield");
		contact.setState("CT");
		contact.setZip("06002");
		contact.setCountry(CountryTypeEnum.US);
		sale.setBillToAddress(contact);
		CardType card = new CardType();
		card.setType(MethodOfPaymentTypeEnum.DI);
		card.setNumber("6011010000000003");
		card.setExpDate("0312");
		card.setCardValidationNum("758");
		sale.setCard(card);
		
		SaleResponse response = litle.sale(sale);
		assertEquals(response.getMessage(), "000",response.getResponse());
		assertEquals(response.getMessage(), "Approved",response.getMessage());
		assertEquals(response.getMessage(), "33333 ",response.getAuthCode());
		assertEquals(response.getMessage(), "10",response.getFraudResult().getAvsResult());
		assertEquals(response.getMessage(), "M",response.getFraudResult().getCardValidationResult());
		
		Credit credit = new Credit();
		credit.setLitleTxnId(response.getLitleTxnId());
		CreditResponse creditresponse = litle.credit(credit);
		assertEquals(creditresponse.getMessage(), "000",creditresponse.getResponse());
		assertEquals(creditresponse.getMessage(), "Approved",creditresponse.getMessage());
		
		com.litle.sdk.generate.Void newvoid = new com.litle.sdk.generate.Void();
		newvoid.setLitleTxnId(creditresponse.getLitleTxnId());
		VoidResponse voidresponse = litle.dovoid(newvoid);
		assertEquals(voidresponse.getMessage(), "000",voidresponse.getResponse());
		assertEquals(voidresponse.getMessage(), "Approved",voidresponse.getMessage());
	}
	
	@Test
	public void test4Auth() throws Exception { 
		Assume.assumeFalse(this.preliveStatus.equalsIgnoreCase("down"));
		Authorization authorization = new Authorization();
		authorization.setOrderId("4");
		authorization.setAmount(10100L);
		authorization.setOrderSource(OrderSourceType.ECOMMERCE);
		Contact contact = new Contact();
		contact.setName("Bob Black");
		contact.setAddressLine1("4 Main St.");
		contact.setCity("Laurel");
		contact.setState("MD");
		contact.setZip("20708");
		contact.setCountry(CountryTypeEnum.US);
		authorization.setBillToAddress(contact);
		CardType card = new CardType();
		card.setType(MethodOfPaymentTypeEnum.AX);
		card.setNumber("375001000000005");
		card.setExpDate("0421");
//		card.setCardValidationNum("758");
		authorization.setCard(card);
		
		AuthorizationResponse response = litle.authorize(authorization);
		assertEquals(response.getMessage(), "000",response.getResponse());
		assertEquals(response.getMessage(), "Approved",response.getMessage());
		assertEquals(response.getMessage(), "44444 ",response.getAuthCode());
		assertEquals(response.getMessage(), "13",response.getFraudResult().getAvsResult());
		
		Capture capture = new Capture();
		capture.setLitleTxnId(response.getLitleTxnId());
		CaptureResponse captureresponse = litle.capture(capture);
		assertEquals(captureresponse.getMessage(), "000",captureresponse.getResponse());
		assertEquals(captureresponse.getMessage(), "Approved",captureresponse.getMessage());
		
		Credit credit = new Credit();
		credit.setLitleTxnId(captureresponse.getLitleTxnId());
		CreditResponse creditresponse = litle.credit(credit);
		assertEquals(creditresponse.getMessage(), "000",creditresponse.getResponse());
		assertEquals(creditresponse.getMessage(), "Approved",creditresponse.getMessage());
		
		com.litle.sdk.generate.Void newvoid = new com.litle.sdk.generate.Void();
		newvoid.setLitleTxnId(creditresponse.getLitleTxnId());
		VoidResponse voidresponse = litle.dovoid(newvoid);
		assertEquals(voidresponse.getMessage(), "000",voidresponse.getResponse());
		assertEquals(voidresponse.getMessage(), "Approved",voidresponse.getMessage());
	}
	
	@Test
	public void test4AVS() throws Exception { 
		Assume.assumeFalse(this.preliveStatus.equalsIgnoreCase("down"));
		Authorization authorization = new Authorization();
		authorization.setOrderId("4");
		authorization.setAmount(10100L);
		authorization.setOrderSource(OrderSourceType.ECOMMERCE);
		Contact contact = new Contact();
		contact.setName("Bob Black");
		contact.setAddressLine1("4 Main St.");
		contact.setCity("Laurel");
		contact.setState("MD");
		contact.setZip("20708");
		contact.setCountry(CountryTypeEnum.US);
		authorization.setBillToAddress(contact);
		CardType card = new CardType();
		card.setType(MethodOfPaymentTypeEnum.AX);
		card.setNumber("375001000000005");
		card.setExpDate("0421");
//		card.setCardValidationNum("758");
		authorization.setCard(card);
		
		AuthorizationResponse response = litle.authorize(authorization);
		assertEquals(response.getMessage(), "000",response.getResponse());
		assertEquals(response.getMessage(), "Approved",response.getMessage());
		assertEquals(response.getMessage(), "44444 ",response.getAuthCode());
		assertEquals(response.getMessage(), "13",response.getFraudResult().getAvsResult());
		
	}
	
	@Test
	public void test4Sale() throws Exception { 
		Assume.assumeFalse(this.preliveStatus.equalsIgnoreCase("down"));
		Sale sale = new Sale();
		sale.setOrderId("4");
		sale.setAmount(10100L);
		sale.setOrderSource(OrderSourceType.ECOMMERCE);
		Contact contact = new Contact();
		contact.setName("Bob Black");
		contact.setAddressLine1("4 Main St.");
		contact.setCity("Laurel");
		contact.setState("MD");
		contact.setZip("20708");
		contact.setCountry(CountryTypeEnum.US);
		sale.setBillToAddress(contact);
		CardType card = new CardType();
		card.setType(MethodOfPaymentTypeEnum.AX);
		card.setNumber("375001000000005");
		card.setExpDate("0412");
//		card.setCardValidationNum("758");
		sale.setCard(card);
		
		SaleResponse response = litle.sale(sale);
		assertEquals(response.getMessage(), "000",response.getResponse());
		assertEquals(response.getMessage(), "Approved",response.getMessage());
		assertEquals(response.getMessage(), "44444 ",response.getAuthCode());
		assertEquals(response.getMessage(), "13",response.getFraudResult().getAvsResult());
		
		Credit credit = new Credit();
		credit.setLitleTxnId(response.getLitleTxnId());
		CreditResponse creditresponse = litle.credit(credit);
		assertEquals(creditresponse.getMessage(), "000",creditresponse.getResponse());
		assertEquals(creditresponse.getMessage(), "Approved",creditresponse.getMessage());
		
		com.litle.sdk.generate.Void newvoid = new com.litle.sdk.generate.Void();
		newvoid.setLitleTxnId(creditresponse.getLitleTxnId());
		VoidResponse voidresponse = litle.dovoid(newvoid);
		assertEquals(voidresponse.getMessage(), "000",voidresponse.getResponse());
		assertEquals(voidresponse.getMessage(), "Approved",voidresponse.getMessage());
	}
	
	@Test
	public void test5Auth() throws Exception { 
		Assume.assumeFalse(this.preliveStatus.equalsIgnoreCase("down"));
		Authorization authorization = new Authorization();
		authorization.setOrderId("5");
		authorization.setAmount(50050L);
		authorization.setOrderSource(OrderSourceType.ECOMMERCE);
		CardType card = new CardType();
		card.setType(MethodOfPaymentTypeEnum.VI);
		card.setNumber("4457010200000007");
		card.setExpDate("0512");
		card.setCardValidationNum("463");
		authorization.setCard(card);
		FraudCheckType authenticationvalue = new FraudCheckType();
		authenticationvalue.setAuthenticationValue("BwABBJQ1AgAAAAAgJDUCAAAAAAA=");
		authorization.setCardholderAuthentication(authenticationvalue);
		
		AuthorizationResponse response = litle.authorize(authorization);
		assertEquals(response.getMessage(), "000",response.getResponse());
		assertEquals(response.getMessage(), "Approved",response.getMessage());
		assertEquals(response.getMessage(), "55555 ",response.getAuthCode());
		assertEquals(response.getMessage(), "32",response.getFraudResult().getAvsResult());
		assertEquals(response.getMessage(), "M",response.getFraudResult().getCardValidationResult());
		
		Capture capture = new Capture();
		capture.setLitleTxnId(response.getLitleTxnId());
		CaptureResponse captureresponse = litle.capture(capture);
		assertEquals(captureresponse.getMessage(), "000",captureresponse.getResponse());
		assertEquals(captureresponse.getMessage(), "Approved",captureresponse.getMessage());
		
		Credit credit = new Credit();
		credit.setLitleTxnId(captureresponse.getLitleTxnId());
		CreditResponse creditresponse = litle.credit(credit);
		assertEquals(creditresponse.getMessage(), "000",creditresponse.getResponse());
		assertEquals(creditresponse.getMessage(), "Approved",creditresponse.getMessage());
		
		com.litle.sdk.generate.Void newvoid = new com.litle.sdk.generate.Void();
		newvoid.setLitleTxnId(creditresponse.getLitleTxnId());
		VoidResponse voidresponse = litle.dovoid(newvoid);
		assertEquals(voidresponse.getMessage(), "000",voidresponse.getResponse());
		assertEquals(voidresponse.getMessage(), "Approved",voidresponse.getMessage());
	}
	
	@Test
	public void test5AVS() throws Exception { 
		Assume.assumeFalse(this.preliveStatus.equalsIgnoreCase("down"));
		Authorization authorization = new Authorization();
		authorization.setOrderId("5");
		authorization.setAmount(000L);
		authorization.setOrderSource(OrderSourceType.ECOMMERCE);
		CardType card = new CardType();
		card.setType(MethodOfPaymentTypeEnum.VI);
		card.setNumber("4457010200000007");
		card.setExpDate("0512");
		card.setCardValidationNum("463");
		authorization.setCard(card);
		FraudCheckType authenticationvalue = new FraudCheckType();
		authenticationvalue.setAuthenticationValue("BwABBJQ1AgAAAAAgJDUCAAAAAAA=");
		authorization.setCardholderAuthentication(authenticationvalue);
		
		AuthorizationResponse response = litle.authorize(authorization);
		assertEquals(response.getMessage(), "000",response.getResponse());
		assertEquals(response.getMessage(), "Approved",response.getMessage());
		assertEquals(response.getMessage(), "55555 ",response.getAuthCode());
		assertEquals(response.getMessage(), "32",response.getFraudResult().getAvsResult());
		assertEquals(response.getMessage(), "M",response.getFraudResult().getCardValidationResult());
		
	}
	
	@Test
	public void test5Sale() throws Exception { 
		Assume.assumeFalse(this.preliveStatus.equalsIgnoreCase("down"));
		Sale sale = new Sale();
		sale.setOrderId("5");
		sale.setAmount(50050L);
		sale.setOrderSource(OrderSourceType.ECOMMERCE);
		CardType card = new CardType();
		card.setType(MethodOfPaymentTypeEnum.VI);
		card.setNumber("4457010200000007");
		card.setExpDate("0512");
		card.setCardValidationNum("463");
		sale.setCard(card);
		FraudCheckType authenticationvalue = new FraudCheckType();
		authenticationvalue.setAuthenticationValue("BwABBJQ1AgAAAAAgJDUCAAAAAAA=");
		sale.setCardholderAuthentication(authenticationvalue);
		
		SaleResponse response = litle.sale(sale);
		assertEquals(response.getMessage(), "000",response.getResponse());
		assertEquals(response.getMessage(), "Approved",response.getMessage());
		assertEquals(response.getMessage(), "55555 ",response.getAuthCode());
		assertEquals(response.getMessage(), "32",response.getFraudResult().getAvsResult());
		assertEquals(response.getMessage(), "M",response.getFraudResult().getCardValidationResult());
		
		Credit credit = new Credit();
		credit.setLitleTxnId(response.getLitleTxnId());
		CreditResponse creditresponse = litle.credit(credit);
		assertEquals(creditresponse.getMessage(), "000",creditresponse.getResponse());
		assertEquals(creditresponse.getMessage(), "Approved",creditresponse.getMessage());
		
		com.litle.sdk.generate.Void newvoid = new com.litle.sdk.generate.Void();
		newvoid.setLitleTxnId(creditresponse.getLitleTxnId());
		VoidResponse voidresponse = litle.dovoid(newvoid);
		assertEquals(voidresponse.getMessage(), "000",voidresponse.getResponse());
		assertEquals(voidresponse.getMessage(), "Approved",voidresponse.getMessage());
	}
	
	@Test
	public void test6Auth() throws Exception { 
		Assume.assumeFalse(this.preliveStatus.equalsIgnoreCase("down"));
		Authorization authorization = new Authorization();
		authorization.setOrderId("6");
		authorization.setAmount(60060L);
		authorization.setOrderSource(OrderSourceType.ECOMMERCE);
		Contact contact = new Contact();
		contact.setName("Joe Green");
		contact.setAddressLine1("6 Main St.");
		contact.setCity("Derry");
		contact.setState("NH");
		contact.setZip("03038");
		contact.setCountry(CountryTypeEnum.US);
		authorization.setBillToAddress(contact);
		CardType card = new CardType();
		card.setType(MethodOfPaymentTypeEnum.VI);
		card.setNumber("4457010100000008");
		card.setExpDate("0612");
		card.setCardValidationNum("992");
		authorization.setCard(card);
		
		AuthorizationResponse response = litle.authorize(authorization);
		assertEquals(response.getMessage(), "110",response.getResponse());
		assertEquals(response.getMessage(), "Insufficient Funds",response.getMessage());
		assertEquals(response.getMessage(), "34",response.getFraudResult().getAvsResult());
		assertEquals(response.getMessage(), "P",response.getFraudResult().getCardValidationResult());
		
	}
	
	@Test
	public void test6Sale() throws Exception { 
		Assume.assumeFalse(this.preliveStatus.equalsIgnoreCase("down"));
		Sale sale = new Sale();
		sale.setOrderId("6");
		sale.setAmount(60060L);
		sale.setOrderSource(OrderSourceType.ECOMMERCE);
		Contact contact = new Contact();
		contact.setName("Joe Green");
		contact.setAddressLine1("6 Main St.");
		contact.setCity("Derry");
		contact.setState("NH");
		contact.setZip("03038");
		contact.setCountry(CountryTypeEnum.US);
		sale.setBillToAddress(contact);
		CardType card = new CardType();
		card.setType(MethodOfPaymentTypeEnum.VI);
		card.setNumber("4457010100000008");
		card.setExpDate("0612");
		card.setCardValidationNum("992");
		sale.setCard(card);
		
		SaleResponse response = litle.sale(sale);
		assertEquals(response.getMessage(), "110",response.getResponse());
		assertEquals(response.getMessage(), "Insufficient Funds",response.getMessage());
		assertEquals(response.getMessage(), "34",response.getFraudResult().getAvsResult());
		assertEquals(response.getMessage(), "P",response.getFraudResult().getCardValidationResult());
		
		com.litle.sdk.generate.Void newvoid = new com.litle.sdk.generate.Void();
		newvoid.setLitleTxnId(response.getLitleTxnId());
		VoidResponse voidresponse = litle.dovoid(newvoid);
		assertEquals(voidresponse.getMessage(), "360",voidresponse.getResponse());
		assertEquals(voidresponse.getMessage(), "No transaction found with specified transaction Id",voidresponse.getMessage());
	}
	
	@Test
	public void test7Auth() throws Exception { 
		Assume.assumeFalse(this.preliveStatus.equalsIgnoreCase("down"));
		Authorization authorization = new Authorization();
		authorization.setOrderId("7");
		authorization.setAmount(70070L);
		authorization.setOrderSource(OrderSourceType.ECOMMERCE);
		Contact contact = new Contact();
		contact.setName("Jane Murray");
		contact.setAddressLine1("7 Main St.");
		contact.setCity("Amesbury");
		contact.setState("MA");
		contact.setZip("01913");
		contact.setCountry(CountryTypeEnum.US);
		authorization.setBillToAddress(contact);
		CardType card = new CardType();
		card.setType(MethodOfPaymentTypeEnum.MC);
		card.setNumber("5112010100000002");
		card.setExpDate("0712");
		card.setCardValidationNum("251");
		authorization.setCard(card);
		
		AuthorizationResponse response = litle.authorize(authorization);
		assertEquals(response.getMessage(), "301",response.getResponse());
		assertEquals(response.getMessage(), "Invalid Account Number",response.getMessage());
		assertEquals(response.getMessage(), "34",response.getFraudResult().getAvsResult());
		assertEquals(response.getMessage(), "N",response.getFraudResult().getCardValidationResult());
		
	}
	
	@Test
	public void test7AVS() throws Exception { 
		Assume.assumeFalse(this.preliveStatus.equalsIgnoreCase("down"));
		Authorization authorization = new Authorization();
		authorization.setOrderId("7");
		authorization.setAmount(000L);
		authorization.setOrderSource(OrderSourceType.ECOMMERCE);
		Contact contact = new Contact();
		contact.setName("Jane Murray");
		contact.setAddressLine1("7 Main St.");
		contact.setCity("Amesbury");
		contact.setState("MA");
		contact.setZip("01913");
		contact.setCountry(CountryTypeEnum.US);
		authorization.setBillToAddress(contact);
		CardType card = new CardType();
		card.setType(MethodOfPaymentTypeEnum.MC);
		card.setNumber("5112010100000002");
		card.setExpDate("0712");
		card.setCardValidationNum("251");
		authorization.setCard(card);
		
		AuthorizationResponse response = litle.authorize(authorization);
		assertEquals(response.getMessage(), "301",response.getResponse());
		assertEquals(response.getMessage(), "Invalid Account Number",response.getMessage());
		assertEquals(response.getMessage(), "34",response.getFraudResult().getAvsResult());
		assertEquals(response.getMessage(), "N",response.getFraudResult().getCardValidationResult());
		
	}
	
	@Test
	public void test7Sale() throws Exception { 
		Assume.assumeFalse(this.preliveStatus.equalsIgnoreCase("down"));
		Sale sale = new Sale();
		sale.setOrderId("7");
		sale.setAmount(70070L);
		sale.setOrderSource(OrderSourceType.ECOMMERCE);
		Contact contact = new Contact();
		contact.setName("Jane Murray");
		contact.setAddressLine1("7 Main St.");
		contact.setCity("Amesbury");
		contact.setState("MA");
		contact.setZip("01913");
		contact.setCountry(CountryTypeEnum.US);
		sale.setBillToAddress(contact);
		CardType card = new CardType();
		card.setType(MethodOfPaymentTypeEnum.MC);
		card.setNumber("5112010100000002");
		card.setExpDate("0712");
		card.setCardValidationNum("251");
		sale.setCard(card);
		
		SaleResponse response = litle.sale(sale);
		assertEquals(response.getMessage(), "301",response.getResponse());
		assertEquals(response.getMessage(), "Invalid Account Number",response.getMessage());
		assertEquals(response.getMessage(), "34",response.getFraudResult().getAvsResult());
		assertEquals(response.getMessage(), "N",response.getFraudResult().getCardValidationResult());
		
	}
	
	@Test
	public void test8Auth() throws Exception { 
		Assume.assumeFalse(this.preliveStatus.equalsIgnoreCase("down"));
		Authorization authorization = new Authorization();
		authorization.setOrderId("8");
		authorization.setAmount(80080L);
		authorization.setOrderSource(OrderSourceType.ECOMMERCE);
		Contact contact = new Contact();
		contact.setName("Mark Johnson");
		contact.setAddressLine1("8 Main St.");
		contact.setCity("Manchester");
		contact.setState("NH");
		contact.setZip("03101");
		contact.setCountry(CountryTypeEnum.US);
		authorization.setBillToAddress(contact);
		CardType card = new CardType();
		card.setType(MethodOfPaymentTypeEnum.DI);
		card.setNumber("6011010100000002");
		card.setExpDate("0812");
		card.setCardValidationNum("184");
		authorization.setCard(card);
		
		AuthorizationResponse response = litle.authorize(authorization);
		assertEquals(response.getMessage(), "123",response.getResponse());
		assertEquals(response.getMessage(), "Call Discover",response.getMessage());
		assertEquals(response.getMessage(), "34",response.getFraudResult().getAvsResult());
		assertEquals(response.getMessage(), "P",response.getFraudResult().getCardValidationResult());
		
	}
	
	@Test
	public void test8AVS() throws Exception { 
		Assume.assumeFalse(this.preliveStatus.equalsIgnoreCase("down"));
		Authorization authorization = new Authorization();
		authorization.setOrderId("8");
		authorization.setAmount(000L);
		authorization.setOrderSource(OrderSourceType.ECOMMERCE);
		Contact contact = new Contact();
		contact.setName("Mark Johnson");
		contact.setAddressLine1("8 Main St.");
		contact.setCity("Manchester");
		contact.setState("NH");
		contact.setZip("03101");
		contact.setCountry(CountryTypeEnum.US);
		authorization.setBillToAddress(contact);
		CardType card = new CardType();
		card.setType(MethodOfPaymentTypeEnum.DI);
		card.setNumber("6011010100000002");
		card.setExpDate("0812");
		card.setCardValidationNum("184");
		authorization.setCard(card);
		
		AuthorizationResponse response = litle.authorize(authorization);
		assertEquals(response.getMessage(), "123",response.getResponse());
		assertEquals(response.getMessage(), "Call Discover",response.getMessage());
		assertEquals(response.getMessage(), "34",response.getFraudResult().getAvsResult());
		assertEquals(response.getMessage(), "P",response.getFraudResult().getCardValidationResult());
		
	}
	
	@Test
	public void test8Sale() throws Exception { 
		Assume.assumeFalse(this.preliveStatus.equalsIgnoreCase("down"));
		Sale sale = new Sale();
		sale.setOrderId("8");
		sale.setAmount(80080L);
		sale.setOrderSource(OrderSourceType.ECOMMERCE);
		Contact contact = new Contact();
		contact.setName("Mark Johnson");
		contact.setAddressLine1("8 Main St.");
		contact.setCity("Manchester");
		contact.setState("NH");
		contact.setZip("03101");
		contact.setCountry(CountryTypeEnum.US);
		sale.setBillToAddress(contact);
		CardType card = new CardType();
		card.setType(MethodOfPaymentTypeEnum.DI);
		card.setNumber("6011010100000002");
		card.setExpDate("0812");
		card.setCardValidationNum("184");
		sale.setCard(card);
		
		SaleResponse response = litle.sale(sale);
		assertEquals(response.getMessage(), "123",response.getResponse());
		assertEquals(response.getMessage(), "Call Discover",response.getMessage());
		assertEquals(response.getMessage(), "34",response.getFraudResult().getAvsResult());
		assertEquals(response.getMessage(), "P",response.getFraudResult().getCardValidationResult());
		
	}
	
	@Test
	public void test9Auth() throws Exception { 
		Assume.assumeFalse(this.preliveStatus.equalsIgnoreCase("down"));
		Authorization authorization = new Authorization();
		authorization.setOrderId("9");
		authorization.setAmount(90090L);
		authorization.setOrderSource(OrderSourceType.ECOMMERCE);
		Contact contact = new Contact();
		contact.setName("James Miller");
		contact.setAddressLine1("9 Main St.");
		contact.setCity("Boston");
		contact.setState("MA");
		contact.setZip("02134");
		contact.setCountry(CountryTypeEnum.US);
		authorization.setBillToAddress(contact);
		CardType card = new CardType();
		card.setType(MethodOfPaymentTypeEnum.AX);
		card.setNumber("375001010000003");
		card.setExpDate("0912");
		card.setCardValidationNum("0421");
		authorization.setCard(card);
		
		AuthorizationResponse response = litle.authorize(authorization);
		assertEquals(response.getMessage(), "303",response.getResponse());
		assertEquals(response.getMessage(), "Pick Up Card",response.getMessage());
		assertEquals(response.getMessage(), "34",response.getFraudResult().getAvsResult());
		
	}
	
	@Test
	public void test9AVS() throws Exception { 
		Assume.assumeFalse(this.preliveStatus.equalsIgnoreCase("down"));
		Authorization authorization = new Authorization();
		authorization.setOrderId("9");
		authorization.setAmount(000L);
		authorization.setOrderSource(OrderSourceType.ECOMMERCE);
		Contact contact = new Contact();
		contact.setName("James Miller");
		contact.setAddressLine1("9 Main St.");
		contact.setCity("Boston");
		contact.setState("MA");
		contact.setZip("02134");
		contact.setCountry(CountryTypeEnum.US);
		authorization.setBillToAddress(contact);
		CardType card = new CardType();
		card.setType(MethodOfPaymentTypeEnum.AX);
		card.setNumber("375001010000003");
		card.setExpDate("0912");
		card.setCardValidationNum("0421");
		authorization.setCard(card);
		
		AuthorizationResponse response = litle.authorize(authorization);
		assertEquals(response.getMessage(), "303",response.getResponse());
		assertEquals(response.getMessage(), "Pick Up Card",response.getMessage());
		assertEquals(response.getMessage(), "34",response.getFraudResult().getAvsResult());
		
	}
	
	@Test
	public void test9Sale() throws Exception { 
		Assume.assumeFalse(this.preliveStatus.equalsIgnoreCase("down"));
		Sale sale = new Sale();
		sale.setOrderId("9");
		sale.setAmount(90090L);
		sale.setOrderSource(OrderSourceType.ECOMMERCE);
		Contact contact = new Contact();
		contact.setName("James Miller");
		contact.setAddressLine1("9 Main St.");
		contact.setCity("Boston");
		contact.setState("MA");
		contact.setZip("02134");
		contact.setCountry(CountryTypeEnum.US);
		sale.setBillToAddress(contact);
		CardType card = new CardType();
		card.setType(MethodOfPaymentTypeEnum.AX);
		card.setNumber("375001010000003");
		card.setExpDate("0912");
		card.setCardValidationNum("0421");
		sale.setCard(card);
		
		SaleResponse response = litle.sale(sale);
		assertEquals(response.getMessage(), "303",response.getResponse());
		assertEquals(response.getMessage(), "Pick Up Card",response.getMessage());
		assertEquals(response.getMessage(), "34",response.getFraudResult().getAvsResult());
		
	}
	
	@Test
	public void test10() throws Exception { 
		Assume.assumeFalse(this.preliveStatus.equalsIgnoreCase("down"));
		Authorization authorization = new Authorization();
		authorization.setOrderId("10");
		authorization.setAmount(40000L);
		authorization.setOrderSource(OrderSourceType.ECOMMERCE);
		CardType card = new CardType();
		card.setType(MethodOfPaymentTypeEnum.VI);
		card.setNumber("4457010140000141");
		card.setExpDate("0912");
		authorization.setCard(card);
		authorization.setAllowPartialAuth(true);
		
		AuthorizationResponse response = litle.authorize(authorization);
		assertEquals(response.getMessage(), "010",response.getResponse());
		assertEquals(response.getMessage(), "Partially Approved",response.getMessage());
		assertEquals(response.getMessage(), 32000L,response.getApprovedAmount().longValue());
		
	}
	
	@Test
	public void test11() throws Exception { 
		Assume.assumeFalse(this.preliveStatus.equalsIgnoreCase("down"));
		Authorization authorization = new Authorization();
		authorization.setOrderId("11");
		authorization.setAmount(60000L);
		authorization.setOrderSource(OrderSourceType.ECOMMERCE);
		CardType card = new CardType();
		card.setType(MethodOfPaymentTypeEnum.MC);
		card.setNumber("5112010140000004");
		card.setExpDate("1111");
		authorization.setCard(card);
		authorization.setAllowPartialAuth(true);
		
		AuthorizationResponse response = litle.authorize(authorization);
		assertEquals(response.getMessage(), "010",response.getResponse());
		assertEquals(response.getMessage(), "Partially Approved",response.getMessage());
		assertEquals(response.getMessage(), 48000L,response.getApprovedAmount().longValue());
		
	}
	
	@Test
	public void test12() throws Exception { 
		Assume.assumeFalse(this.preliveStatus.equalsIgnoreCase("down"));
		Authorization authorization = new Authorization();
		authorization.setOrderId("12");
		authorization.setAmount(50000L);
		authorization.setOrderSource(OrderSourceType.ECOMMERCE);
		CardType card = new CardType();
		card.setType(MethodOfPaymentTypeEnum.AX);
		card.setNumber("375001014000009");
		card.setExpDate("0412");
		authorization.setCard(card);
		authorization.setAllowPartialAuth(true);
		
		AuthorizationResponse response = litle.authorize(authorization);
		assertEquals(response.getMessage(), "010",response.getResponse());
		assertEquals(response.getMessage(), "Partially Approved",response.getMessage());
		assertEquals(response.getMessage(), 40000L,response.getApprovedAmount().longValue());
		
	}
	
	@Test
	public void test13() throws Exception { 
		Assume.assumeFalse(this.preliveStatus.equalsIgnoreCase("down"));
		Authorization authorization = new Authorization();
		authorization.setOrderId("13");
		authorization.setAmount(15000L);
		authorization.setOrderSource(OrderSourceType.ECOMMERCE);
		CardType card = new CardType();
		card.setType(MethodOfPaymentTypeEnum.DI);
		card.setNumber("6011010140000004");
		card.setExpDate("0812");
		authorization.setCard(card);
		authorization.setAllowPartialAuth(true);
		
		AuthorizationResponse response = litle.authorize(authorization);
		assertEquals(response.getMessage(), "010",response.getResponse());
		assertEquals(response.getMessage(), "Partially Approved",response.getMessage());
		assertEquals(response.getMessage(), 12000L,response.getApprovedAmount().longValue());
		
	}
	

}



