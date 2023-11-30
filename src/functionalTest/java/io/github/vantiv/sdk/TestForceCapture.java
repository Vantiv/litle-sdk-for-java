package io.github.vantiv.sdk;

import static org.junit.Assert.assertEquals;

import org.junit.BeforeClass;
import org.junit.Test;

import io.github.vantiv.sdk.generate.CardTokenType;
import io.github.vantiv.sdk.generate.CardType;
import io.github.vantiv.sdk.generate.ForceCapture;
import io.github.vantiv.sdk.generate.ForceCaptureResponse;
import io.github.vantiv.sdk.generate.MethodOfPaymentTypeEnum;
import io.github.vantiv.sdk.generate.OrderSourceType;
import io.github.vantiv.sdk.generate.ProcessingTypeEnum;

public class TestForceCapture {

	private static LitleOnline litle;

	@BeforeClass
	public static void beforeClass() throws Exception {
		litle = new LitleOnline();
	}

	@Test
	public void simpleForceCaptureWithCard() throws Exception{
		ForceCapture forcecapture = new ForceCapture();
		forcecapture.setAmount(106L);
		forcecapture.setOrderId("12344");
		forcecapture.setOrderSource(OrderSourceType.ECOMMERCE);
		CardType card = new CardType();
		card.setType(MethodOfPaymentTypeEnum.VI);
		card.setNumber("4100000000000001");
		card.setExpDate("1210");
		forcecapture.setCard(card);
		ForceCaptureResponse response = litle.forceCapture(forcecapture);
		assertEquals("Approved", response.getMessage());
	}

	@Test
	public void simpleForceCaptureWithToken() throws Exception{
		ForceCapture forcecapture = new ForceCapture();
		forcecapture.setAmount(106L);
		forcecapture.setOrderId("12344");
		forcecapture.setOrderSource(OrderSourceType.ECOMMERCE);
		CardTokenType token = new CardTokenType();
		token.setLitleToken("123456789101112");
		token.setExpDate("1210");
		token.setCardValidationNum("555");
		token.setType(MethodOfPaymentTypeEnum.VI);
		forcecapture.setToken(token);
		ForceCaptureResponse response = litle.forceCapture(forcecapture);
		assertEquals("Approved", response.getMessage());
	}

	@Test
    public void simpleForceCaptureWithSecondaryAmount() throws Exception{
        ForceCapture forcecapture = new ForceCapture();
        forcecapture.setAmount(106L);
        forcecapture.setOrderId("12344");
        forcecapture.setOrderSource(OrderSourceType.ECOMMERCE);
        forcecapture.setSecondaryAmount(50L);
        CardType card = new CardType();
        card.setType(MethodOfPaymentTypeEnum.VI);
        card.setNumber("4100000000000001");
        card.setExpDate("1210");
        forcecapture.setCard(card);
        ForceCaptureResponse response = litle.forceCapture(forcecapture);
        assertEquals("Approved", response.getMessage());
    }
	
	@Test
	public void testForceCaptureWithProcssingType() throws Exception{
		ForceCapture forcecapture = new ForceCapture();
		forcecapture.setAmount(106L);
		forcecapture.setOrderId("12344");
		forcecapture.setOrderSource(OrderSourceType.ECOMMERCE);
		forcecapture.setProcessingType(ProcessingTypeEnum.INITIAL_INSTALLMENT);
		CardType card = new CardType();
		card.setType(MethodOfPaymentTypeEnum.VI);
		card.setNumber("4100000000000001");
		card.setExpDate("1210");
		forcecapture.setCard(card);
		ForceCaptureResponse response = litle.forceCapture(forcecapture);
		assertEquals("Approved", response.getMessage());
	}

}

