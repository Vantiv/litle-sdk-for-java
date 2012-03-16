package com.litle.sdk;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.math.BigInteger;

import org.junit.Test;

import com.litle.sdk.generate.Contact;
import com.litle.sdk.generate.EcheckAccountTypeEnum;
import com.litle.sdk.generate.EcheckCredit;
import com.litle.sdk.generate.EcheckCreditResponse;
import com.litle.sdk.generate.EcheckTokenType;
import com.litle.sdk.generate.EcheckType;
import com.litle.sdk.generate.ObjectFactory;

public class TestEcheckCredit {

	@Test
	public void simpleEcheckCredit() throws Exception{
		EcheckCredit echeckcredit = new EcheckCredit();
		echeckcredit.setAmount(BigInteger.valueOf(12L));
		echeckcredit.setLitleTxnId(123456789101112L);
		EcheckCreditResponse response = new LitleOnline().echeckCredit(echeckcredit);
		assertEquals("Approved", response.getMessage());
	}
	
	@Test
	public void noLitleTxnId() throws Exception{
		EcheckCredit echeckcredit = new EcheckCredit();
		try {
			new LitleOnline().echeckCredit(echeckcredit);
			fail("Expected exception");
		} catch(LitleOnlineException e) {
			assertEquals("Error validating xml data against the schema", e.getMessage());
		}
	}
	
	@Test
	public void echeckCreditWithEcheck() throws Exception{
		EcheckCredit echeckcredit = new EcheckCredit();
		echeckcredit.setAmount(BigInteger.valueOf(12L));
		echeckcredit.setOrderId("12345");
		echeckcredit.setOrderSource("ecommerce");
		EcheckType echeck = new EcheckType();
		echeck.setAccType(EcheckAccountTypeEnum.CHECKING);
		echeck.setAccNum("12345657890");
		echeck.setRoutingNum("123456789");
		echeck.setCheckNum("123455");
		echeckcredit.setEcheckOrEcheckToken(new ObjectFactory().createEcheck(echeck));
		Contact billToAddress = new Contact();
		billToAddress.setName("Bob");
		billToAddress.setCity("Lowell");
		billToAddress.setState("MA");
		billToAddress.setEmail("litle.com");
		echeckcredit.setBillToAddress(billToAddress);
		EcheckCreditResponse response = new LitleOnline().echeckCredit(echeckcredit);
		assertEquals("Approved", response.getMessage());
	}
	
	@Test
	public void echeckCreditWithToken() throws Exception{
		EcheckCredit echeckcredit = new EcheckCredit();
		echeckcredit.setAmount(BigInteger.valueOf(12L));
		echeckcredit.setOrderId("12345");
		echeckcredit.setOrderSource("ecommerce");
		EcheckTokenType token = new EcheckTokenType();
		token.setAccType(EcheckAccountTypeEnum.CHECKING);
		token.setLitleToken("1234565789012");
		token.setRoutingNum("123456789");
		token.setCheckNum("123455");
		echeckcredit.setEcheckOrEcheckToken(new ObjectFactory().createEcheckToken(token));
		Contact billToAddress = new Contact();
		billToAddress.setName("Bob");
		billToAddress.setCity("Lowell");
		billToAddress.setState("MA");
		billToAddress.setEmail("litle.com");
		echeckcredit.setBillToAddress(billToAddress);
		EcheckCreditResponse response = new LitleOnline().echeckCredit(echeckcredit);
		assertEquals("Approved", response.getMessage());
	}
	
	@Test
	public void missingBilling() throws Exception{
		EcheckCredit echeckcredit = new EcheckCredit();
		echeckcredit.setAmount(BigInteger.valueOf(12L));
		echeckcredit.setOrderId("12345");
		echeckcredit.setOrderSource("ecommerce");
		EcheckType echeck = new EcheckType();
		echeck.setAccType(EcheckAccountTypeEnum.CHECKING);
		echeck.setAccNum("12345657890");
		echeck.setRoutingNum("123456789");
		echeck.setCheckNum("123455");
		echeckcredit.setEcheckOrEcheckToken(new ObjectFactory().createEcheck(echeck));
		try {
			new LitleOnline().echeckCredit(echeckcredit);
			fail("Expected exception");
		} catch(LitleOnlineException e) {
			assertEquals("Error validating xml data against the schema", e.getMessage());
		}
	}
	
	

}

