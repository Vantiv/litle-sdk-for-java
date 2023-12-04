package io.github.vantiv.sdk;


import static org.junit.Assert.assertEquals;

import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.Assume;

import io.github.vantiv.sdk.generate.Contact;
import io.github.vantiv.sdk.generate.EcheckAccountTypeEnum;
import io.github.vantiv.sdk.generate.EcheckCredit;
import io.github.vantiv.sdk.generate.EcheckCreditResponse;
import io.github.vantiv.sdk.generate.EcheckSale;
import io.github.vantiv.sdk.generate.EcheckSalesResponse;
import io.github.vantiv.sdk.generate.EcheckType;
import io.github.vantiv.sdk.generate.EcheckVerification;
import io.github.vantiv.sdk.generate.EcheckVerificationResponse;
import io.github.vantiv.sdk.generate.OrderSourceType;

import java.io.FileInputStream;
import java.util.Properties;

public class TestCert4Echeck {

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
	public void test37() throws Exception { 
		Assume.assumeFalse(this.preliveStatus.equalsIgnoreCase("down"));
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
		
		EcheckVerificationResponse response = litle.echeckVerification(verification);
		assertEquals(response.getMessage(),"301", response.getResponse());
		assertEquals(response.getMessage(),"Invalid Account Number", response.getMessage());
	}
	
	@Test
	public void test38() throws Exception { 
		Assume.assumeFalse(this.preliveStatus.equalsIgnoreCase("down"));
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
		
		EcheckVerificationResponse response = litle.echeckVerification(verification);
		assertEquals(response.getMessage(),"000", response.getResponse());
		assertEquals(response.getMessage(),"Approved", response.getMessage());
	}
	
	@Test
	public void test39() throws Exception { 
		Assume.assumeFalse(this.preliveStatus.equalsIgnoreCase("down"));
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
		
		EcheckVerificationResponse response = litle.echeckVerification(verification);
		assertEquals(response.getMessage(),"950", response.getResponse());
		assertEquals(response.getMessage(),"Decline - Negative Information on File", response.getMessage());
	}
	
	@Test
	public void test40() throws Exception { 
		Assume.assumeFalse(this.preliveStatus.equalsIgnoreCase("down"));
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
		
		EcheckVerificationResponse response = litle.echeckVerification(verification);
		assertEquals(response.getMessage(),"951", response.getResponse());
		assertEquals(response.getMessage(),"Absolute Decline", response.getMessage());
	}
	
	@Test
	public void test41() throws Exception { 
		Assume.assumeFalse(this.preliveStatus.equalsIgnoreCase("down"));
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
		
		EcheckSalesResponse response = litle.echeckSale(sale);
		assertEquals(response.getMessage(),"301", response.getResponse());
		assertEquals(response.getMessage(),"Invalid Account Number", response.getMessage());
	}
	
	@Test
	public void test42() throws Exception { 
		Assume.assumeFalse(this.preliveStatus.equalsIgnoreCase("down"));
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
		
		EcheckSalesResponse response = litle.echeckSale(sale);
		assertEquals(response.getMessage(),"000", response.getResponse());
		assertEquals(response.getMessage(),"Approved", response.getMessage());
	}
	
	@Test
	public void test43() throws Exception { 
		Assume.assumeFalse(this.preliveStatus.equalsIgnoreCase("down"));
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
		
		EcheckSalesResponse response = litle.echeckSale(sale);
		assertEquals(response.getMessage(),"000", response.getResponse());
		assertEquals(response.getMessage(),"Approved", response.getMessage());

		// Test 49
		EcheckCredit credit = new EcheckCredit();
		credit.setLitleTxnId(response.getLitleTxnId());

		EcheckCreditResponse creditResponse = litle.echeckCredit(credit);
		assertEquals(creditResponse.getMessage(),"000", creditResponse.getResponse());
		assertEquals(creditResponse.getMessage(),"Approved", creditResponse.getMessage());
	}
	
	@Test
	public void test44() throws Exception { 
		Assume.assumeFalse(this.preliveStatus.equalsIgnoreCase("down"));
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
		
		EcheckSalesResponse response = litle.echeckSale(sale);
		assertEquals(response.getMessage(),"900", response.getResponse());
		assertEquals(response.getMessage(),"Invalid Bank Routing Number", response.getMessage());
	}
	
	@Test
	public void test45() throws Exception { 
		Assume.assumeFalse(this.preliveStatus.equalsIgnoreCase("down"));
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
		
		EcheckCreditResponse response = litle.echeckCredit(credit);
		assertEquals(response.getMessage(),"301", response.getResponse());
		assertEquals(response.getMessage(),"Invalid Account Number", response.getMessage());
	}
	
	@Test
	public void test46() throws Exception { 
		Assume.assumeFalse(this.preliveStatus.equalsIgnoreCase("down"));
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
		
		EcheckCreditResponse response = litle.echeckCredit(credit);
		assertEquals(response.getMessage(),"000", response.getResponse());
		assertEquals(response.getMessage(),"Approved", response.getMessage());
	}
	
	@Test
	public void test47() throws Exception { 
		Assume.assumeFalse(this.preliveStatus.equalsIgnoreCase("down"));
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
		
		EcheckCreditResponse response = litle.echeckCredit(credit);
		assertEquals(response.getMessage(),"000", response.getResponse());
		assertEquals(response.getMessage(),"Approved", response.getMessage());
	}
	
	@Test
	public void test49() throws Exception { 
		Assume.assumeFalse(this.preliveStatus.equalsIgnoreCase("down"));
		EcheckCredit credit = new EcheckCredit();
		credit.setLitleTxnId(2L);
		
		EcheckCreditResponse response = litle.echeckCredit(credit);
		assertEquals(response.getMessage(),"360", response.getResponse());
		assertEquals(response.getMessage(),"No transaction found with specified transaction Id", response.getMessage());
	}

}
