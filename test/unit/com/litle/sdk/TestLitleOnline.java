package com.litle.sdk;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.matches;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Properties;

import org.junit.BeforeClass;
import org.junit.Test;

import com.litle.sdk.generate.AuthReversal;
import com.litle.sdk.generate.AuthReversalResponse;
import com.litle.sdk.generate.Authorization;
import com.litle.sdk.generate.CardType;
import com.litle.sdk.generate.MethodOfPaymentTypeEnum;
import com.litle.sdk.generate.OrderSourceType;

public class TestLitleOnline {

	private static LitleOnline litle;

	@BeforeClass
	public static void beforeClass() throws Exception {
		litle = new LitleOnline();
	}

	@Test
	public void testAuth() throws Exception {

		Authorization authorization = new Authorization();
		authorization.setReportGroup("Planets");
		authorization.setOrderId("12344");
		authorization.setAmount(106L);
		authorization.setOrderSource(OrderSourceType.ECOMMERCE);
		CardType card = new CardType();
		card.setType(MethodOfPaymentTypeEnum.VI);
		card.setNumber("4100000000000002");
		card.setExpDate("1210");
		authorization.setCard(card);

		Communication mockedCommunication = mock(Communication.class);
		when(
				mockedCommunication
						.requestToServer(
								matches(".*?<litleOnlineRequest.*?<authorization.*?<card>.*?<number>4100000000000002</number>.*?</card>.*?</authorization>.*?"),
								any(Properties.class)))
				.thenReturn(
						"<litleOnlineResponse version='8.10' response='0' message='Valid Format' xmlns='http://www.litle.com/schema'><authorizationResponse></authorizationResponse></litleOnlineResponse>");
		litle.setCommunication(mockedCommunication);
		litle.authorize(authorization);
		verify(mockedCommunication);
	}
	
//	@Test
//	public void testAuthReversal() throws Exception {
//
//		AuthReversal reversal = new AuthReversal();
//		reversal.setLitleTxnId(12345678000L);
//		reversal.setAmount(106L);
//		reversal.setPayPalNotes("Notes");
//		
//
//		Communication mockedCommunication = mock(Communication.class);
//		when(
//				mockedCommunication
//						.requestToServer(
//								matches(".*?<litleOnlineRequest.*?<authReversal.*?</authReversal>.*?"),
//								any(Properties.class)))
//				.thenReturn(
//						"<litleOnlineResponse version='8.10' response='0' message='Valid Format' xmlns='http://www.litle.com/schema'><AuthReversalResponse></AuthReversalResponse></litleOnlineResponse>");
//		litle.setCommunication(mockedCommunication);
//		litle.authReversal(reversal);
//		verify(mockedCommunication);
//	}

}
