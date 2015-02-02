package com.litle.sdk;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.junit.BeforeClass;
import org.junit.Test;

import com.litle.sdk.generate.Contact;
import com.litle.sdk.generate.EcheckAccountTypeEnum;
import com.litle.sdk.generate.EcheckCredit;
import com.litle.sdk.generate.EcheckCreditResponse;
import com.litle.sdk.generate.EcheckTokenType;
import com.litle.sdk.generate.EcheckType;
import com.litle.sdk.generate.OrderSourceType;

public class TestEcheckCredit {

	private static LitleOnline litle;

	@BeforeClass
	public static void beforeClass() throws Exception {
		litle = new LitleOnline();
	}

	@Test
	public void simpleEcheckCredit() throws Exception{
		EcheckCredit echeckcredit = new EcheckCredit();
		echeckcredit.setAmount(12L);
		echeckcredit.setLitleTxnId(123456789101112L);
		EcheckCreditResponse response = litle.echeckCredit(echeckcredit);
		assertEquals("Approved", response.getMessage());
	}
	
	@Test
	public void noLitleTxnId() throws Exception{
		EcheckCredit echeckcredit = new EcheckCredit();
		try {
			litle.echeckCredit(echeckcredit);
			fail("Expected exception");
		} catch(LitleOnlineException e) {
			assertTrue(e.getMessage(),e.getMessage().startsWith("Error validating xml data against the schema"));
		}
	}
	
	@Test
	public void echeckCreditWithEcheck() throws Exception{
		EcheckCredit echeckcredit = new EcheckCredit();
		echeckcredit.setAmount(12L);
		echeckcredit.setOrderId("12345");
		echeckcredit.setOrderSource(OrderSourceType.ECOMMERCE);
		EcheckType echeck = new EcheckType();
		echeck.setAccType(EcheckAccountTypeEnum.CHECKING);
		echeck.setAccNum("12345657890");
		echeck.setRoutingNum("123456789");
		echeck.setCheckNum("123455");
		echeckcredit.setEcheck(echeck);
		Contact billToAddress = new Contact();
		billToAddress.setName("Bob");
		billToAddress.setCity("Lowell");
		billToAddress.setState("MA");
		billToAddress.setEmail("litle.com");
		echeckcredit.setBillToAddress(billToAddress);
		EcheckCreditResponse response = litle.echeckCredit(echeckcredit);
		assertEquals("Approved", response.getMessage());
	}
	
	@Test
	public void echeckCreditWithToken() throws Exception{
		EcheckCredit echeckcredit = new EcheckCredit();
		echeckcredit.setAmount(12L);
		echeckcredit.setOrderId("12345");
		echeckcredit.setOrderSource(OrderSourceType.ECOMMERCE);
		EcheckTokenType token = new EcheckTokenType();
		token.setAccType(EcheckAccountTypeEnum.CHECKING);
		token.setLitleToken("1234565789012");
		token.setRoutingNum("123456789");
		token.setCheckNum("123455");
		echeckcredit.setEcheckToken(token);
		Contact billToAddress = new Contact();
		billToAddress.setName("Bob");
		billToAddress.setCity("Lowell");
		billToAddress.setState("MA");
		billToAddress.setEmail("litle.com");
		echeckcredit.setBillToAddress(billToAddress);
		EcheckCreditResponse response = litle.echeckCredit(echeckcredit);
		assertEquals("Approved", response.getMessage());
	}
	
	@Test
	public void missingBilling() throws Exception{
		EcheckCredit echeckcredit = new EcheckCredit();
		echeckcredit.setAmount(12L);
		echeckcredit.setOrderId("12345");
		echeckcredit.setOrderSource(OrderSourceType.ECOMMERCE);
		EcheckType echeck = new EcheckType();
		echeck.setAccType(EcheckAccountTypeEnum.CHECKING);
		echeck.setAccNum("12345657890");
		echeck.setRoutingNum("123456789");
		echeck.setCheckNum("123455");
		echeckcredit.setEcheck(echeck);
		try {
			litle.echeckCredit(echeckcredit);
			fail("Expected exception");
		} catch(LitleOnlineException e) {
			assertTrue(e.getMessage(),e.getMessage().startsWith("Error validating xml data against the schema"));
		}
	}
	
	@Test
    public void echeckCreditWithSecondryAmount() throws Exception{
        EcheckCredit echeckcredit = new EcheckCredit();
        echeckcredit.setAmount(12L);
        echeckcredit.setSecondaryAmount(50L);
        echeckcredit.setOrderId("12345");
        echeckcredit.setOrderSource(OrderSourceType.ECOMMERCE);
        EcheckType echeck = new EcheckType();
        echeck.setAccType(EcheckAccountTypeEnum.CHECKING);
        echeck.setAccNum("12345657890");
        echeck.setRoutingNum("123456789");
        echeck.setCheckNum("123455");
        echeckcredit.setEcheck(echeck);
        Contact billToAddress = new Contact();
        billToAddress.setName("Bob");
        billToAddress.setCity("Lowell");
        billToAddress.setState("MA");
        billToAddress.setEmail("litle.com");
        echeckcredit.setBillToAddress(billToAddress);
        EcheckCreditResponse response = litle.echeckCredit(echeckcredit);
        assertEquals("Approved", response.getMessage());
    }
	
	@Test
    public void echeckCreditWithLitleTxnIdAndSecondryAmount() throws Exception{
	    EcheckCredit echeckcredit = new EcheckCredit();
        echeckcredit.setAmount(12L);
        echeckcredit.setLitleTxnId(123456789101112L);
        echeckcredit.setSecondaryAmount(50L);
        EcheckCreditResponse response = litle.echeckCredit(echeckcredit);
        assertEquals("Approved", response.getMessage());
    }

}

