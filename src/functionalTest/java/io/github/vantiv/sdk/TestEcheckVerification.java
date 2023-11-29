package io.github.vantiv.sdk;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.junit.BeforeClass;
import org.junit.Test;

import io.github.vantiv.sdk.generate.Contact;
import io.github.vantiv.sdk.generate.EcheckAccountTypeEnum;
import io.github.vantiv.sdk.generate.EcheckTokenType;
import io.github.vantiv.sdk.generate.EcheckType;
import io.github.vantiv.sdk.generate.EcheckVerification;
import io.github.vantiv.sdk.generate.EcheckVerificationResponse;
import io.github.vantiv.sdk.generate.OrderSourceType;

public class TestEcheckVerification {

	private static LitleOnline litle;

	@BeforeClass
	public static void beforeClass() throws Exception {
		litle = new LitleOnline();
	}
	
	@Test
	public void simpleEcheckVerification() throws Exception{
		EcheckVerification echeckverification = new EcheckVerification();
		echeckverification.setAmount(123456L);
		echeckverification.setOrderId("12345");
		echeckverification.setOrderSource(OrderSourceType.ECOMMERCE);
		EcheckType echeck = new EcheckType();
		echeck.setAccType(EcheckAccountTypeEnum.CHECKING);
		echeck.setAccNum("12345657890");
		echeck.setRoutingNum("123456789");
		echeck.setCheckNum("123455");
		echeckverification.setEcheck(echeck);
		Contact contact = new Contact();
		contact.setName("Bob");
		contact.setCity("lowell");
		contact.setState("MA");
		contact.setEmail("litle.com");
		echeckverification.setBillToAddress(contact);
		EcheckVerificationResponse response = litle.echeckVerification(echeckverification);
		assertEquals("Approved", response.getMessage());
	}
	
	@Test
	public void echeckVerificationWithEcheckToken() throws Exception{
		EcheckVerification echeckverification = new EcheckVerification();
		echeckverification.setAmount(123456L);
		echeckverification.setOrderId("12345");
		echeckverification.setOrderSource(OrderSourceType.ECOMMERCE);
		EcheckTokenType token = new EcheckTokenType();
		token.setAccType(EcheckAccountTypeEnum.CHECKING);
		token.setLitleToken("1234565789012");
		token.setRoutingNum("123456789");
		token.setCheckNum("123455");
		echeckverification.setEcheckToken(token);
		Contact contact = new Contact();
		contact.setName("Bob");
		contact.setCity("lowell");
		contact.setState("MA");
		contact.setEmail("litle.com");
		echeckverification.setBillToAddress(contact);
		EcheckVerificationResponse response = litle.echeckVerification(echeckverification);
		assertEquals("Approved", response.getMessage());
	}
	
	@Test
	public void testMissingBillingField() throws Exception {
		EcheckVerification echeckVerification = new EcheckVerification();
		echeckVerification.setReportGroup("Planets");
		echeckVerification.setAmount(123L);
		EcheckType echeck = new EcheckType();
		echeck.setAccType(EcheckAccountTypeEnum.CHECKING);
		echeck.setAccNum("12345657890");
		echeck.setRoutingNum("123456789");
		echeck.setCheckNum("123455");
		echeckVerification.setEcheck(echeck);
		echeckVerification.setOrderId("12345");
		echeckVerification.setOrderSource(OrderSourceType.ECOMMERCE);
		try {
			litle.echeckVerification(echeckVerification);
			fail("Expected exception");
		} catch(LitleOnlineException e) {
			assertTrue(e.getMessage(),e.getMessage().startsWith("Error validating xml data against the schema"));
		}
		
	}

}

