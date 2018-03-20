package com.litle.sdk;


import static org.junit.Assert.assertEquals;

import org.junit.BeforeClass;
import org.junit.Test;

import com.litle.sdk.generate.Authorization;
import com.litle.sdk.generate.AuthorizationResponse;
import com.litle.sdk.generate.CardTokenType;
import com.litle.sdk.generate.CardType;
import com.litle.sdk.generate.Contact;
import com.litle.sdk.generate.EcheckAccountTypeEnum;
import com.litle.sdk.generate.EcheckForTokenType;
import com.litle.sdk.generate.EcheckSale;
import com.litle.sdk.generate.EcheckSalesResponse;
import com.litle.sdk.generate.EcheckType;
import com.litle.sdk.generate.MethodOfPaymentTypeEnum;
import com.litle.sdk.generate.OrderSourceType;
import com.litle.sdk.generate.RegisterTokenRequestType;
import com.litle.sdk.generate.RegisterTokenResponse;

import java.io.FileInputStream;
import java.util.Properties;

public class TestCert5Token {

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
	public void test50() throws Exception {
		RegisterTokenRequestType request = new RegisterTokenRequestType();
		request.setOrderId("50");
		request.setAccountNumber("4457119922390123");
		
		RegisterTokenResponse response = litle.registerToken(request);
		assertEquals(response.getMessage(), "445711", response.getBin());
		assertEquals(response.getMessage(), MethodOfPaymentTypeEnum.VI, response.getType());
		assertEquals(response.getMessage(), "801", response.getResponse());
		assertEquals(response.getMessage(), "1111222233330123", response.getLitleToken());
		assertEquals(response.getMessage(), "Account number was successfully registered", response.getMessage());
	}
	
	@Test
	public void test51() throws Exception {
		RegisterTokenRequestType request = new RegisterTokenRequestType();
		request.setOrderId("51");
		request.setAccountNumber("4457119999999999");
		
		RegisterTokenResponse response = litle.registerToken(request);
		assertEquals(response.getMessage(), "820", response.getResponse());
		assertEquals(response.getMessage(), "Credit card number was invalid", response.getMessage());
	}

	@Test
	public void test52() throws Exception {
		RegisterTokenRequestType request = new RegisterTokenRequestType();
		request.setOrderId("52");
		request.setAccountNumber("4457119922390123");
		
		RegisterTokenResponse response = litle.registerToken(request);
		assertEquals(response.getMessage(), "445711", response.getBin());
		assertEquals(response.getMessage(), MethodOfPaymentTypeEnum.VI, response.getType());
		assertEquals(response.getMessage(), "802", response.getResponse());
		assertEquals(response.getMessage(), "1111222233330123", response.getLitleToken());
		assertEquals(response.getMessage(), "Account number was previously registered", response.getMessage());
	}
	
	@Test
	public void test53() throws Exception {
		RegisterTokenRequestType request = new RegisterTokenRequestType();
		request.setOrderId("53");
		EcheckForTokenType echeck = new EcheckForTokenType();
		echeck.setAccNum("1099999998");
		echeck.setRoutingNum("114567895");
		request.setEcheckForToken(echeck);
		
		RegisterTokenResponse response = litle.registerToken(request);
		assertEquals(response.getMessage(), MethodOfPaymentTypeEnum.EC, response.getType());
		assertEquals(response.getMessage(), "998", response.getECheckAccountSuffix());
		assertEquals(response.getMessage(), "801", response.getResponse());
		assertEquals(response.getMessage(), "Account number was successfully registered", response.getMessage());
		assertEquals(response.getMessage(), "111922223333000998", response.getLitleToken());
	}
	
	@Test
	public void test54() throws Exception {
		RegisterTokenRequestType request = new RegisterTokenRequestType();
		request.setOrderId("54");
		EcheckForTokenType echeck = new EcheckForTokenType();
		echeck.setAccNum("1022222102");
		echeck.setRoutingNum("1145_7895");
		request.setEcheckForToken(echeck);
		
		RegisterTokenResponse response = litle.registerToken(request);
		assertEquals(response.getMessage(), "900", response.getResponse());
		assertEquals(response.getMessage(), "Invalid Bank Routing Number", response.getMessage());
	}
	
	@Test
	public void test55() throws Exception {
		Authorization auth = new Authorization();
		auth.setOrderId("55");
		auth.setAmount(15000L);
		auth.setOrderSource(OrderSourceType.ECOMMERCE);
		CardType card = new CardType();
		card.setNumber("5435101234510196");
		card.setExpDate("1112");
		card.setCardValidationNum("987");
		card.setType(MethodOfPaymentTypeEnum.MC);
		auth.setCard(card);
		
		AuthorizationResponse response = litle.authorize(auth);
		assertEquals(response.getMessage(), "000", response.getResponse());
		assertEquals(response.getMessage(), "Approved", response.getMessage());
		assertEquals(response.getMessage(), "801", response.getTokenResponse().getTokenResponseCode());
		assertEquals(response.getMessage(), "Account number was successfully registered", response.getTokenResponse().getTokenMessage());
		assertEquals(response.getMessage(), MethodOfPaymentTypeEnum.MC, response.getTokenResponse().getType());
		assertEquals(response.getMessage(), "543510", response.getTokenResponse().getBin());
	}
	
	@Test
	public void test56() throws Exception {
		Authorization auth = new Authorization();
		auth.setOrderId("56");
		auth.setAmount(15000L);
		auth.setOrderSource(OrderSourceType.ECOMMERCE);
		CardType card = new CardType();
		card.setNumber("5435109999999999");
		card.setExpDate("1112");
		card.setCardValidationNum("987");
		card.setType(MethodOfPaymentTypeEnum.MC);
		auth.setCard(card);
		
		AuthorizationResponse response = litle.authorize(auth);
		assertEquals(response.getMessage(), "301", response.getResponse());
		assertEquals(response.getMessage(), "Invalid Account Number", response.getMessage());
	}
	
	@Test
	public void test57() throws Exception {
		Authorization auth = new Authorization();
		auth.setOrderId("57");
		auth.setAmount(15000L);
		auth.setOrderSource(OrderSourceType.ECOMMERCE);
		CardType card = new CardType();
		card.setNumber("5435101234510196");
		card.setExpDate("1112");
		card.setCardValidationNum("987");
		card.setType(MethodOfPaymentTypeEnum.MC);
		auth.setCard(card);
		
		AuthorizationResponse response = litle.authorize(auth);
		assertEquals(response.getMessage(), "000", response.getResponse());
		assertEquals(response.getMessage(), "Approved", response.getMessage());
		assertEquals(response.getMessage(), "802", response.getTokenResponse().getTokenResponseCode());
		assertEquals(response.getMessage(), "Account number was previously registered", response.getTokenResponse().getTokenMessage());
		assertEquals(response.getMessage(), MethodOfPaymentTypeEnum.MC, response.getTokenResponse().getType());
		assertEquals(response.getMessage(), "543510", response.getTokenResponse().getBin());
	}
	
	@Test
	public void test59() throws Exception {
		Authorization auth = new Authorization();
		auth.setOrderId("59");
		auth.setAmount(15000L);
		auth.setOrderSource(OrderSourceType.ECOMMERCE);
		CardTokenType token = new CardTokenType();
		token.setLitleToken("1712990000040196");
		token.setExpDate("1112");
		auth.setToken(token);
		
		AuthorizationResponse response = litle.authorize(auth);
		assertEquals(response.getMessage(), "822", response.getResponse());
		assertEquals(response.getMessage(), "Token was not found", response.getMessage());
	}
	
	@Test
	public void test60() throws Exception {
		Authorization auth = new Authorization();
		auth.setOrderId("60");
		auth.setAmount(15000L);
		auth.setOrderSource(OrderSourceType.ECOMMERCE);
		CardTokenType token = new CardTokenType();
		token.setLitleToken("1712999999999999");
		token.setExpDate("1112");
		auth.setToken(token);
		
		AuthorizationResponse response = litle.authorize(auth);
		assertEquals(response.getMessage(), "823", response.getResponse());
		assertEquals(response.getMessage(), "Token was invalid", response.getMessage());
	}
	
	@Test
	public void test61() throws Exception {
		EcheckSale sale = new EcheckSale();
		sale.setOrderId("61");
		sale.setAmount(15000L);
		sale.setOrderSource(OrderSourceType.ECOMMERCE);
		Contact billToAddress = new Contact();
		billToAddress.setFirstName("Tom");
		billToAddress.setLastName("Black");
		sale.setBillToAddress(billToAddress);
		EcheckType echeck = new EcheckType();
		echeck.setAccType(EcheckAccountTypeEnum.CHECKING);
		echeck.setAccNum("1099999003");
		echeck.setRoutingNum("114567895");
		sale.setEcheck(echeck);
		
		EcheckSalesResponse response = litle.echeckSale(sale);
		assertEquals(response.getMessage(), "801", response.getTokenResponse().getTokenResponseCode());
		assertEquals(response.getMessage(), "Account number was successfully registered", response.getTokenResponse().getTokenMessage());
		assertEquals(response.getMessage(), MethodOfPaymentTypeEnum.EC, response.getTokenResponse().getType());
		assertEquals(response.getMessage(), "111922223333444003", response.getTokenResponse().getLitleToken());
	}
	
	@Test
	public void test62() throws Exception {
		EcheckSale sale = new EcheckSale();
		sale.setOrderId("62");
		sale.setAmount(15000L);
		sale.setOrderSource(OrderSourceType.ECOMMERCE);
		Contact billToAddress = new Contact();
		billToAddress.setFirstName("Tom");
		billToAddress.setLastName("Black");
		sale.setBillToAddress(billToAddress);
		EcheckType echeck = new EcheckType();
		echeck.setAccType(EcheckAccountTypeEnum.CHECKING);
		echeck.setAccNum("1099999999");
		echeck.setRoutingNum("114567895");
		sale.setEcheck(echeck);
		
		EcheckSalesResponse response = litle.echeckSale(sale);
		assertEquals(response.getMessage(), "801", response.getTokenResponse().getTokenResponseCode());
		assertEquals(response.getMessage(), "Account number was successfully registered", response.getTokenResponse().getTokenMessage());
		assertEquals(response.getMessage(), MethodOfPaymentTypeEnum.EC, response.getTokenResponse().getType());
		assertEquals(response.getMessage(), "999", response.getTokenResponse().getECheckAccountSuffix());
		assertEquals(response.getMessage(), "111922223333444999", response.getTokenResponse().getLitleToken());
	}
	
	@Test
	public void test63() throws Exception {
		EcheckSale sale = new EcheckSale();
		sale.setOrderId("63");
		sale.setAmount(15000L);
		sale.setOrderSource(OrderSourceType.ECOMMERCE);
		Contact billToAddress = new Contact();
		billToAddress.setFirstName("Tom");
		billToAddress.setLastName("Black");
		sale.setBillToAddress(billToAddress);
		EcheckType echeck = new EcheckType();
		echeck.setAccType(EcheckAccountTypeEnum.CHECKING);
		echeck.setAccNum("1099999999");
		echeck.setRoutingNum("214567892");
		sale.setEcheck(echeck);
		
		EcheckSalesResponse response = litle.echeckSale(sale);
		assertEquals(response.getMessage(), "801", response.getTokenResponse().getTokenResponseCode());
		assertEquals(response.getMessage(), "Account number was successfully registered", response.getTokenResponse().getTokenMessage());
		assertEquals(response.getMessage(), MethodOfPaymentTypeEnum.EC, response.getTokenResponse().getType());
		assertEquals(response.getMessage(), "999", response.getTokenResponse().getECheckAccountSuffix());
		assertEquals(response.getMessage(), "111922223333555999", response.getTokenResponse().getLitleToken());
	}
	
}
