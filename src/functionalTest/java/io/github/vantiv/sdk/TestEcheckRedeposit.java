package io.github.vantiv.sdk;

import static org.junit.Assert.assertEquals;

import org.junit.BeforeClass;
import org.junit.Test;
import io.github.vantiv.sdk.generate.*;
public class TestEcheckRedeposit {

	private static LitleOnline litle;

	@BeforeClass
	public static void beforeClass() throws Exception {
		litle = new LitleOnline();
	}
	
	@Test
	public void simpleEcheckRedeposit() throws Exception{
		EcheckRedeposit echeckredeposit = new EcheckRedeposit();
		echeckredeposit.setLitleTxnId(123456L);
		echeckredeposit.setId("id");
		EcheckRedepositResponse response = litle.echeckRedeposit(echeckredeposit);
		assertEquals("Transaction Received", response.getMessage());
	}
	
	@Test
	public void echeckRedepositWithEcheck() throws Exception{
		EcheckRedeposit echeckredeposit = new EcheckRedeposit();
		echeckredeposit.setLitleTxnId(123456L);
		EcheckType echeck = new EcheckType();
		echeck.setAccType(EcheckAccountTypeEnum.CHECKING);
		echeck.setAccNum("12345657890");
		echeck.setRoutingNum("123456789");
		echeck.setCheckNum("123455");
	    echeckredeposit.setId("id");
		
		echeckredeposit.setEcheck(echeck);
		EcheckRedepositResponse response = litle.echeckRedeposit(echeckredeposit);
		assertEquals("Transaction Received", response.getMessage());
	}
	
	@Test
	public void echeckRedepositWithEcheckToken() throws Exception{
		EcheckRedeposit echeckredeposit = new EcheckRedeposit();
		echeckredeposit.setLitleTxnId(123456L);
		EcheckTokenType echeckToken = new EcheckTokenType();
		echeckToken.setAccType(EcheckAccountTypeEnum.CHECKING);
		echeckToken.setLitleToken("1234565789012");
		echeckToken.setRoutingNum("123456789");
		echeckToken.setCheckNum("123455");
	    echeckredeposit.setId("id");
		
		echeckredeposit.setEcheckToken(echeckToken);
		EcheckRedepositResponse response = litle.echeckRedeposit(echeckredeposit);
		assertEquals("Transaction Received", response.getMessage());
	}


}
