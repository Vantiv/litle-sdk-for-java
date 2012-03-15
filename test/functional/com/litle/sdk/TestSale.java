package com.litle.sdk;

import static org.junit.Assert.*;

import java.math.BigInteger;

import org.junit.Test;

import com.litle.sdk.generate.AuthReversal;
import com.litle.sdk.generate.AuthReversalResponse;
import com.litle.sdk.generate.AuthorizationResponse;
import com.litle.sdk.generate.CardType;
import com.litle.sdk.generate.ForceCapture;
import com.litle.sdk.generate.ForceCaptureResponse;
import com.litle.sdk.generate.Sale;
import com.litle.sdk.generate.SaleResponse;

public class TestSale {

	@Test
	public void simpleSaleWithCard() throws Exception{
		Sale sale = new Sale();
		sale.setAmount(BigInteger.valueOf(106L));
		sale.setLitleTxnId(123456L);
		sale.setOrderId("12344");
		sale.setOrderSource("ecommerce");
		CardType card = new CardType();
		card.setType("VI");
		card.setNumber("4100000000000002");
		card.setExpDate("1210");
		sale.setCard(card);
		SaleResponse response = new LitleOnline().sale(sale);
		assertEquals("Approved", response.getMessage());
	}

}
