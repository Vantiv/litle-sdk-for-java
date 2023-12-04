package io.github.vantiv.sdk;


import static org.junit.Assert.assertEquals;

import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.Assume;

import io.github.vantiv.sdk.generate.AffluenceTypeEnum;
import io.github.vantiv.sdk.generate.Authorization;
import io.github.vantiv.sdk.generate.AuthorizationResponse;
import io.github.vantiv.sdk.generate.CardType;
import io.github.vantiv.sdk.generate.FundingSourceTypeEnum;
import io.github.vantiv.sdk.generate.HealthcareAmounts;
import io.github.vantiv.sdk.generate.HealthcareIIAS;
import io.github.vantiv.sdk.generate.IIASFlagType;
import io.github.vantiv.sdk.generate.MethodOfPaymentTypeEnum;
import io.github.vantiv.sdk.generate.OrderSourceType;

import java.io.FileInputStream;
import java.util.Properties;

public class TestCert2AuthEnhanced {

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
	public void test14() throws Exception { 
		Assume.assumeFalse(this.preliveStatus.equalsIgnoreCase("down"));
		Authorization authorization = new Authorization();
		authorization.setOrderId("14");
		authorization.setAmount(3000L);
		authorization.setOrderSource(OrderSourceType.ECOMMERCE);
		CardType card = new CardType();
		card.setType(MethodOfPaymentTypeEnum.VI);
		card.setNumber("4457010200000247");
		card.setExpDate("0812");
		authorization.setCard(card);

		AuthorizationResponse response = litle.authorize(authorization);
		assertEquals(response.getMessage(), "000",response.getResponse());
		assertEquals(response.getMessage(), "Approved",response.getMessage());
		assertEquals(response.getMessage(), FundingSourceTypeEnum.PREPAID,response.getEnhancedAuthResponse().getFundingSource().getType());
		assertEquals(response.getMessage(), "2000",response.getEnhancedAuthResponse().getFundingSource().getAvailableBalance());
		assertEquals(response.getMessage(), "NO",response.getEnhancedAuthResponse().getFundingSource().getReloadable());
		assertEquals(response.getMessage(), "GIFT",response.getEnhancedAuthResponse().getFundingSource().getPrepaidCardType());
	}

	@Test
	public void test15() throws Exception { 
		Assume.assumeFalse(this.preliveStatus.equalsIgnoreCase("down"));
		Authorization authorization = new Authorization();
		authorization.setOrderId("15");
		authorization.setAmount(3000L);
		authorization.setOrderSource(OrderSourceType.ECOMMERCE);
		CardType card = new CardType();
		card.setType(MethodOfPaymentTypeEnum.MC);
		card.setNumber("5500000254444445");
		card.setExpDate("0312");
		authorization.setCard(card);

		AuthorizationResponse response = litle.authorize(authorization);
		assertEquals(response.getMessage(), "000",response.getResponse());
		assertEquals(response.getMessage(), "Approved",response.getMessage());
		assertEquals(response.getMessage(), FundingSourceTypeEnum.PREPAID,response.getEnhancedAuthResponse().getFundingSource().getType());
		assertEquals(response.getMessage(), "2000",response.getEnhancedAuthResponse().getFundingSource().getAvailableBalance());
		assertEquals(response.getMessage(), "YES",response.getEnhancedAuthResponse().getFundingSource().getReloadable());
		assertEquals(response.getMessage(), "PAYROLL",response.getEnhancedAuthResponse().getFundingSource().getPrepaidCardType());
	}

	@Test
	public void test16() throws Exception { 
		Assume.assumeFalse(this.preliveStatus.equalsIgnoreCase("down"));
		Authorization authorization = new Authorization();
		authorization.setOrderId("16");
		authorization.setAmount(3000L);
		authorization.setOrderSource(OrderSourceType.ECOMMERCE);
		CardType card = new CardType();
		card.setType(MethodOfPaymentTypeEnum.MC);
		card.setNumber("5592106621450897");
		card.setExpDate("0312");
		authorization.setCard(card);

		AuthorizationResponse response = litle.authorize(authorization);
		assertEquals(response.getMessage(), "000",response.getResponse());
		assertEquals(response.getMessage(), "Approved",response.getMessage());
		assertEquals(response.getMessage(), FundingSourceTypeEnum.PREPAID,response.getEnhancedAuthResponse().getFundingSource().getType());
		assertEquals(response.getMessage(), "0",response.getEnhancedAuthResponse().getFundingSource().getAvailableBalance());
		assertEquals(response.getMessage(), "YES",response.getEnhancedAuthResponse().getFundingSource().getReloadable());
		assertEquals(response.getMessage(), "PAYROLL",response.getEnhancedAuthResponse().getFundingSource().getPrepaidCardType());
	}

	@Test
	public void test17() throws Exception { 
		Assume.assumeFalse(this.preliveStatus.equalsIgnoreCase("down"));
		Authorization authorization = new Authorization();
		authorization.setOrderId("17");
		authorization.setAmount(3000L);
		authorization.setOrderSource(OrderSourceType.ECOMMERCE);
		CardType card = new CardType();
		card.setType(MethodOfPaymentTypeEnum.MC);
		card.setNumber("5590409551104142");
		card.setExpDate("0312");
		authorization.setCard(card);

		AuthorizationResponse response = litle.authorize(authorization);
		assertEquals(response.getMessage(), "000",response.getResponse());
		assertEquals(response.getMessage(), "Approved",response.getMessage());
		assertEquals(response.getMessage(), FundingSourceTypeEnum.PREPAID,response.getEnhancedAuthResponse().getFundingSource().getType());
		assertEquals(response.getMessage(), "6500",response.getEnhancedAuthResponse().getFundingSource().getAvailableBalance());
		assertEquals(response.getMessage(), "YES",response.getEnhancedAuthResponse().getFundingSource().getReloadable());
		assertEquals(response.getMessage(), "PAYROLL",response.getEnhancedAuthResponse().getFundingSource().getPrepaidCardType());
	}

	@Test
	public void test18() throws Exception { 
		Assume.assumeFalse(this.preliveStatus.equalsIgnoreCase("down"));
		Authorization authorization = new Authorization();
		authorization.setOrderId("18");
		authorization.setAmount(3000L);
		authorization.setOrderSource(OrderSourceType.ECOMMERCE);
		CardType card = new CardType();
		card.setType(MethodOfPaymentTypeEnum.MC);
		card.setNumber("5587755665222179");
		card.setExpDate("0312");
		authorization.setCard(card);

		AuthorizationResponse response = litle.authorize(authorization);
		assertEquals(response.getMessage(), "000",response.getResponse());
		assertEquals(response.getMessage(), "Approved",response.getMessage());
		assertEquals(response.getMessage(), FundingSourceTypeEnum.PREPAID,response.getEnhancedAuthResponse().getFundingSource().getType());
		assertEquals(response.getMessage(), "12200",response.getEnhancedAuthResponse().getFundingSource().getAvailableBalance());
		assertEquals(response.getMessage(), "YES",response.getEnhancedAuthResponse().getFundingSource().getReloadable());
		assertEquals(response.getMessage(), "PAYROLL",response.getEnhancedAuthResponse().getFundingSource().getPrepaidCardType());
	}

	@Test
	public void test19() throws Exception { 
		Assume.assumeFalse(this.preliveStatus.equalsIgnoreCase("down"));
		Authorization authorization = new Authorization();
		authorization.setOrderId("19");
		authorization.setAmount(3000L);
		authorization.setOrderSource(OrderSourceType.ECOMMERCE);
		CardType card = new CardType();
		card.setType(MethodOfPaymentTypeEnum.MC);
		card.setNumber("5445840176552850");
		card.setExpDate("0312");
		authorization.setCard(card);

		AuthorizationResponse response = litle.authorize(authorization);
		assertEquals(response.getMessage(), "000",response.getResponse());
		assertEquals(response.getMessage(), "Approved",response.getMessage());
		assertEquals(response.getMessage(), FundingSourceTypeEnum.PREPAID,response.getEnhancedAuthResponse().getFundingSource().getType());
		assertEquals(response.getMessage(), "20000",response.getEnhancedAuthResponse().getFundingSource().getAvailableBalance());
		assertEquals(response.getMessage(), "YES",response.getEnhancedAuthResponse().getFundingSource().getReloadable());
		assertEquals(response.getMessage(), "PAYROLL",response.getEnhancedAuthResponse().getFundingSource().getPrepaidCardType());
	}

	@Test
	public void test20() throws Exception { 
		Assume.assumeFalse(this.preliveStatus.equalsIgnoreCase("down"));
		Authorization authorization = new Authorization();
		authorization.setOrderId("20");
		authorization.setAmount(3000L);
		authorization.setOrderSource(OrderSourceType.ECOMMERCE);
		CardType card = new CardType();
		card.setType(MethodOfPaymentTypeEnum.MC);
		card.setNumber("5390016478904678");
		card.setExpDate("0312");
		authorization.setCard(card);

		AuthorizationResponse response = litle.authorize(authorization);
		assertEquals(response.getMessage(), "000",response.getResponse());
		assertEquals(response.getMessage(), "Approved",response.getMessage());
		assertEquals(response.getMessage(), FundingSourceTypeEnum.PREPAID,response.getEnhancedAuthResponse().getFundingSource().getType());
		assertEquals(response.getMessage(), "10050",response.getEnhancedAuthResponse().getFundingSource().getAvailableBalance());
		assertEquals(response.getMessage(), "YES",response.getEnhancedAuthResponse().getFundingSource().getReloadable());
		assertEquals(response.getMessage(), "PAYROLL",response.getEnhancedAuthResponse().getFundingSource().getPrepaidCardType());
	}

	@Test
	public void test21() throws Exception { 
		Assume.assumeFalse(this.preliveStatus.equalsIgnoreCase("down"));
		Authorization authorization = new Authorization();
		authorization.setOrderId("21");
		authorization.setAmount(5000L);
		authorization.setOrderSource(OrderSourceType.ECOMMERCE);
		CardType card = new CardType();
		card.setType(MethodOfPaymentTypeEnum.VI);
		card.setNumber("4457010201000246");
		card.setExpDate("0912");
		authorization.setCard(card);

		AuthorizationResponse response = litle.authorize(authorization);
		assertEquals(response.getMessage(), "000",response.getResponse());
		assertEquals(response.getMessage(), "Approved",response.getMessage());
		assertEquals(response.getMessage(), AffluenceTypeEnum.AFFLUENT,response.getEnhancedAuthResponse().getAffluence());

	}

	@Test
	public void test22() throws Exception { 
		Assume.assumeFalse(this.preliveStatus.equalsIgnoreCase("down"));
		Authorization authorization = new Authorization();
		authorization.setOrderId("22");
		authorization.setAmount(5000L);
		authorization.setOrderSource(OrderSourceType.ECOMMERCE);
		CardType card = new CardType();
		card.setType(MethodOfPaymentTypeEnum.VI);
		card.setNumber("4457010202000245");
		card.setExpDate("1111");
		authorization.setCard(card);

		AuthorizationResponse response = litle.authorize(authorization);
		assertEquals(response.getMessage(), "000",response.getResponse());
		assertEquals(response.getMessage(), "Approved",response.getMessage());
		assertEquals(response.getMessage(), AffluenceTypeEnum.MASS_AFFLUENT,response.getEnhancedAuthResponse().getAffluence());

	}

	@Test
	public void test23() throws Exception { 
		Assume.assumeFalse(this.preliveStatus.equalsIgnoreCase("down"));
		Authorization authorization = new Authorization();
		authorization.setOrderId("23");
		authorization.setAmount(5000L);
		authorization.setOrderSource(OrderSourceType.ECOMMERCE);
		CardType card = new CardType();
		card.setType(MethodOfPaymentTypeEnum.MC);
		card.setNumber("5112010201000109");
		card.setExpDate("0412");
		authorization.setCard(card);

		AuthorizationResponse response = litle.authorize(authorization);
		assertEquals(response.getMessage(), "000",response.getResponse());
		assertEquals(response.getMessage(), "Approved",response.getMessage());
		assertEquals(response.getMessage(), AffluenceTypeEnum.AFFLUENT,response.getEnhancedAuthResponse().getAffluence());

	}

	@Test
	public void test24() throws Exception { 
		Assume.assumeFalse(this.preliveStatus.equalsIgnoreCase("down"));
		Authorization authorization = new Authorization();
		authorization.setOrderId("24");
		authorization.setAmount(5000L);
		authorization.setOrderSource(OrderSourceType.ECOMMERCE);
		CardType card = new CardType();
		card.setType(MethodOfPaymentTypeEnum.MC);
		card.setNumber("5112010202000108");
		card.setExpDate("0812");
		authorization.setCard(card);

		AuthorizationResponse response = litle.authorize(authorization);
		assertEquals(response.getMessage(), "000",response.getResponse());
		assertEquals(response.getMessage(), "Approved",response.getMessage());
		assertEquals(response.getMessage(), AffluenceTypeEnum.MASS_AFFLUENT,response.getEnhancedAuthResponse().getAffluence());

	}

	@Test
	public void test25() throws Exception { 
		Assume.assumeFalse(this.preliveStatus.equalsIgnoreCase("down"));
		Authorization authorization = new Authorization();
		authorization.setOrderId("25");
		authorization.setAmount(5000L);
		authorization.setOrderSource(OrderSourceType.ECOMMERCE);
		CardType card = new CardType();
		card.setType(MethodOfPaymentTypeEnum.VI);
		card.setNumber("4100204446270000");
		card.setExpDate("1112");
		authorization.setCard(card);

		AuthorizationResponse response = litle.authorize(authorization);
		assertEquals(response.getMessage(), "000",response.getResponse());
		assertEquals(response.getMessage(), "Approved",response.getMessage());
		assertEquals(response.getMessage(), "BRA",response.getEnhancedAuthResponse().getIssuerCountry());

	}

	@Test
	public void test26() throws Exception { 
		Assume.assumeFalse(this.preliveStatus.equalsIgnoreCase("down"));
		Authorization authorization = new Authorization();
		authorization.setOrderId("26");
		authorization.setAmount(18698L);
		authorization.setOrderSource(OrderSourceType.ECOMMERCE);
		CardType card = new CardType();
		card.setType(MethodOfPaymentTypeEnum.MC);
		card.setNumber("5194560012341234");
		card.setExpDate("1212");
		authorization.setCard(card);
		authorization.setAllowPartialAuth(true);
		HealthcareIIAS healthcareiias = new HealthcareIIAS();
		HealthcareAmounts healthcareamounts = new HealthcareAmounts();
		healthcareamounts.setTotalHealthcareAmount(20000L);
		healthcareiias.setHealthcareAmounts(healthcareamounts);
		healthcareiias.setIIASFlag(IIASFlagType.Y);
		authorization.setHealthcareIIAS(healthcareiias);

		AuthorizationResponse response = litle.authorize(authorization);
		assertEquals(response.getMessage(), "341",response.getResponse());
		assertEquals(response.getMessage().toLowerCase(), "Invalid healthcare amounts".toLowerCase());
	}

	@Test
	public void test27() throws Exception { 
		Assume.assumeFalse(this.preliveStatus.equalsIgnoreCase("down"));
		Authorization authorization = new Authorization();
		authorization.setOrderId("27");
		authorization.setAmount(18698L);
		authorization.setOrderSource(OrderSourceType.ECOMMERCE);
		CardType card = new CardType();
		card.setType(MethodOfPaymentTypeEnum.MC);
		card.setNumber("5194560012341234");
		card.setExpDate("1212");
		authorization.setCard(card);
		authorization.setAllowPartialAuth(true);
		HealthcareIIAS healthcareiias = new HealthcareIIAS();
		HealthcareAmounts healthcareamounts = new HealthcareAmounts();
		healthcareamounts.setTotalHealthcareAmount(15000L);
		healthcareamounts.setRxAmount(16000L);
		healthcareiias.setHealthcareAmounts(healthcareamounts);
		healthcareiias.setIIASFlag(IIASFlagType.Y);
		authorization.setHealthcareIIAS(healthcareiias);

		AuthorizationResponse response = litle.authorize(authorization);
		assertEquals(response.getMessage(), "341",response.getResponse());
		assertEquals(response.getMessage().toLowerCase(), "Invalid healthcare amounts".toLowerCase());
	}

	@Test
	public void test28() throws Exception { 
		Assume.assumeFalse(this.preliveStatus.equalsIgnoreCase("down"));
		Authorization authorization = new Authorization();
		authorization.setOrderId("28");
		authorization.setAmount(15000L);
		authorization.setOrderSource(OrderSourceType.ECOMMERCE);
		CardType card = new CardType();
		card.setType(MethodOfPaymentTypeEnum.MC);
		card.setNumber("5194560012341234");
		card.setExpDate("1212");
		authorization.setCard(card);
		authorization.setAllowPartialAuth(true);
		HealthcareIIAS healthcareiias = new HealthcareIIAS();
		HealthcareAmounts healthcareamounts = new HealthcareAmounts();
		healthcareamounts.setTotalHealthcareAmount(15000L);
		healthcareamounts.setRxAmount(3698L);
		healthcareiias.setHealthcareAmounts(healthcareamounts);
		healthcareiias.setIIASFlag(IIASFlagType.Y);
		authorization.setHealthcareIIAS(healthcareiias);

		AuthorizationResponse response = litle.authorize(authorization);
		assertEquals(response.getMessage(), "000",response.getResponse());
		assertEquals(response.getMessage(), "Approved",response.getMessage());
	}

	@Test
	public void test29() throws Exception { 
		Assume.assumeFalse(this.preliveStatus.equalsIgnoreCase("down"));
		Authorization authorization = new Authorization();
		authorization.setOrderId("29");
		authorization.setAmount(18699L);
		authorization.setOrderSource(OrderSourceType.ECOMMERCE);
		CardType card = new CardType();
		card.setType(MethodOfPaymentTypeEnum.VI);
		card.setNumber("4024720001231239");
		card.setExpDate("1212");
		authorization.setCard(card);
		authorization.setAllowPartialAuth(true);
		HealthcareIIAS healthcareiias = new HealthcareIIAS();
		HealthcareAmounts healthcareamounts = new HealthcareAmounts();
		healthcareamounts.setTotalHealthcareAmount(31000L);
		healthcareamounts.setRxAmount(1000L);
		healthcareamounts.setVisionAmount(19901L);
		healthcareamounts.setClinicOtherAmount(9050L);
		healthcareamounts.setDentalAmount(1049L);
		healthcareiias.setHealthcareAmounts(healthcareamounts);
		healthcareiias.setIIASFlag(IIASFlagType.Y);
		authorization.setHealthcareIIAS(healthcareiias);

		AuthorizationResponse response = litle.authorize(authorization);
		assertEquals(response.getMessage(), "341",response.getResponse());
		assertEquals(response.getMessage().toLowerCase(), "Invalid healthcare amounts".toLowerCase());
	}

	/*
	@Test
	public void test30() throws Exception { 
		Assume.assumeFalse(this.preliveStatus.equalsIgnoreCase("down"));
		Authorization authorization = new Authorization();
		authorization.setOrderId("30");
		authorization.setAmount(20000L);
		authorization.setOrderSource(OrderSourceType.ECOMMERCE);
		CardType card = new CardType();
		card.setType(MethodOfPaymentTypeEnum.VI);
		card.setNumber("4024720001231239");
		card.setExpDate("1212");
		authorization.setCard(card);
		authorization.setAllowPartialAuth(true);
		HealthcareIIAS healthcareiias = new HealthcareIIAS();
		HealthcareAmounts healthcareamounts = new HealthcareAmounts();
		healthcareamounts.setTotalHealthcareAmount(20000L);
		healthcareamounts.setRxAmount(1000L);
		healthcareamounts.setVisionAmount(19901L);
		healthcareamounts.setClinicOtherAmount(9050L);
		healthcareamounts.setDentalAmount(1049L);
		healthcareiias.setHealthcareAmounts(healthcareamounts);
		healthcareiias.setIIASFlag(IIASFlagType.Y);
		authorization.setHealthcareIIAS(healthcareiias);

		AuthorizationResponse response = litle.authorize(authorization);
		assertEquals(response.getMessage(), "341",response.getResponse());
		assertEquals(response.getMessage().toLowerCase(), "Invalid healthcare amounts".toLowerCase());
	}

	@Test
	public void test31() throws Exception { 
		Assume.assumeFalse(this.preliveStatus.equalsIgnoreCase("down"));
		Authorization authorization = new Authorization();
		authorization.setOrderId("31");
		authorization.setAmount(25000L);
		authorization.setOrderSource(OrderSourceType.ECOMMERCE);
		CardType card = new CardType();
		card.setType(MethodOfPaymentTypeEnum.VI);
		card.setNumber("4024720001231239");
		card.setExpDate("1212");
		authorization.setCard(card);
		authorization.setAllowPartialAuth(true);
		HealthcareIIAS healthcareiias = new HealthcareIIAS();
		HealthcareAmounts healthcareamounts = new HealthcareAmounts();
		healthcareamounts.setTotalHealthcareAmount(18699L);
		healthcareamounts.setRxAmount(1000L);
		healthcareamounts.setVisionAmount(15099L);
		healthcareiias.setHealthcareAmounts(healthcareamounts);
		healthcareiias.setIIASFlag(IIASFlagType.Y);
		authorization.setHealthcareIIAS(healthcareiias);

		AuthorizationResponse response = litle.authorize(authorization);
		assertEquals(response.getMessage(), "010",response.getResponse());
		assertEquals(response.getMessage(), "Partially Approved",response.getMessage());
		assertEquals(response.getMessage(), 18699L,response.getApprovedAmount().longValue());
	}
	*/

}
