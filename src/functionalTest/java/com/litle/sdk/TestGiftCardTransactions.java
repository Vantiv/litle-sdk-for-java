package com.litle.sdk;

import static org.junit.Assert.assertEquals;

import java.util.GregorianCalendar;

import org.junit.Before;
import org.junit.Test;

import com.litle.sdk.generate.GiftCardAuthReversal;
import com.litle.sdk.generate.GiftCardAuthReversalResponse;
import com.litle.sdk.generate.GiftCardCapture;
import com.litle.sdk.generate.GiftCardCaptureResponse;
import com.litle.sdk.generate.GiftCardCardType;
import com.litle.sdk.generate.GiftCardCredit;
import com.litle.sdk.generate.GiftCardCreditResponse;
import com.litle.sdk.generate.MethodOfPaymentTypeEnum;
import com.litle.sdk.generate.OrderSourceType;
import com.sun.org.apache.xerces.internal.jaxp.datatype.XMLGregorianCalendarImpl;

public class TestGiftCardTransactions {

	LitleOnline litle;
	
	@Before
	public void setup() {
		litle = new LitleOnline();
	}
	
	@Test
	public void testGiftCardCapture() {
		GiftCardCapture gcCapture = new GiftCardCapture();
        GiftCardCardType giftCard = new GiftCardCardType();
        giftCard.setType(MethodOfPaymentTypeEnum.GC);
        giftCard.setCardValidationNum("411");
        giftCard.setExpDate("0655");
        giftCard.setNumber("4100000000000000");
        giftCard.setPin("9999");
        
        gcCapture.setLitleTxnId(123L);
        gcCapture.setId("id");
        gcCapture.setReportGroup("rptGrp");
        gcCapture.setCaptureAmount(2434l);
        gcCapture.setCard(giftCard);
        gcCapture.setOriginalRefCode("ref");
        gcCapture.setOriginalAmount(44455l);
        gcCapture.setOriginalTxnTime(new XMLGregorianCalendarImpl(new GregorianCalendar()));
        
        GiftCardCaptureResponse response = litle.giftCardCapture(gcCapture);
        assertEquals("Approved", response.getMessage());
	}
	
	@Test
	public void testGiftCardCredit_withLitleTxnId() {
		GiftCardCredit gcCredit = new GiftCardCredit();
		GiftCardCardType giftCard = new GiftCardCardType();
        giftCard.setType(MethodOfPaymentTypeEnum.GC);
        giftCard.setCardValidationNum("411");
        giftCard.setExpDate("0655");
        giftCard.setNumber("4100000000000000");
        giftCard.setPin("9999");
        
        gcCredit.setLitleTxnId(369852147l);
        gcCredit.setId("id");
        gcCredit.setReportGroup("rptGrp1");
        gcCredit.setCustomerId("customer_22");
        gcCredit.setCreditAmount(1942l);
        gcCredit.setCard(giftCard);
        
        GiftCardCreditResponse response = litle.giftCardCredit(gcCredit);
        assertEquals("Approved", response.getMessage());
	}
	
	@Test
	public void testGiftCardCredit_withOrderId() {
		GiftCardCredit gcCredit = new GiftCardCredit();
		GiftCardCardType giftCard = new GiftCardCardType();
        giftCard.setType(MethodOfPaymentTypeEnum.GC);
        giftCard.setCardValidationNum("411");
        giftCard.setExpDate("0655");
        giftCard.setNumber("4100000000000000");
        giftCard.setPin("9999");
        
        gcCredit.setId("id");
        gcCredit.setReportGroup("rptGrp1");
        gcCredit.setCustomerId("customer_22");
        gcCredit.setOrderSource(OrderSourceType.ECOMMERCE);
        gcCredit.setCreditAmount(1942l);
        gcCredit.setOrderId("order 4");
        gcCredit.setCard(giftCard);
        
        GiftCardCreditResponse response = litle.giftCardCredit(gcCredit);
        assertEquals("Approved", response.getMessage());
        assertEquals("123456", response.getGiftCardResponse().getSequenceNumber());
	}
	
	@Test
	public void testGiftCardAuthReversal() {
		GiftCardAuthReversal gcAuthReversal = new GiftCardAuthReversal();
		GiftCardCardType giftCard = new GiftCardCardType();
        giftCard.setType(MethodOfPaymentTypeEnum.GC);
        giftCard.setCardValidationNum("411");
        giftCard.setExpDate("0655");
        giftCard.setNumber("4100000000000000");
        giftCard.setPin("9999");
        
        gcAuthReversal.setId("979797");
        gcAuthReversal.setCustomerId("customer_23");
        gcAuthReversal.setLitleTxnId(8521478963210145l);
        gcAuthReversal.setReportGroup("rptGrp2");
        gcAuthReversal.setOriginalAmount(45l);
        gcAuthReversal.setOriginalSequenceNumber("333333");
        gcAuthReversal.setOriginalTxnTime(new XMLGregorianCalendarImpl(new GregorianCalendar()));
        gcAuthReversal.setOriginalSystemTraceId(0);
        gcAuthReversal.setOriginalRefCode("ref");
        gcAuthReversal.setCard(giftCard);
        
        GiftCardAuthReversalResponse response = litle.giftCardAuthReversal(gcAuthReversal);
        assertEquals("Approved", response.getMessage());
        assertEquals(0, (int)response.getGiftCardResponse().getSystemTraceId());
	}

}
