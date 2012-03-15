package com.litle.sdk;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.litle.sdk.generate.EcheckAccountTypeEnum;
import com.litle.sdk.generate.EcheckRedeposit;
import com.litle.sdk.generate.EcheckRedepositResponse;
import com.litle.sdk.generate.EcheckTokenType;
import com.litle.sdk.generate.EcheckType;
import com.litle.sdk.generate.ObjectFactory;

public class TestEcheckRedeposit {

	@Test
	public void simpleEcheckRedeposit() throws Exception{
		EcheckRedeposit echeckredeposit = new EcheckRedeposit();
		echeckredeposit.setLitleTxnId(123456L);
		EcheckRedepositResponse response = new LitleOnline().echeckRedeposit(echeckredeposit);
		assertEquals("Approved", response.getMessage());
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
		
		echeckredeposit.setEcheckOrEcheckToken(new ObjectFactory().createEcheck(echeck));
		EcheckRedepositResponse response = new LitleOnline().echeckRedeposit(echeckredeposit);
		assertEquals("Approved", response.getMessage());
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
		
		echeckredeposit.setEcheckOrEcheckToken(new ObjectFactory().createEcheckToken(echeckToken));
		EcheckRedepositResponse response = new LitleOnline().echeckRedeposit(echeckredeposit);
		assertEquals("Approved", response.getMessage());
	}


}
