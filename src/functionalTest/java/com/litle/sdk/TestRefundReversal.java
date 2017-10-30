package com.litle.sdk;

import static org.junit.Assert.assertEquals;

import java.util.GregorianCalendar;

import org.junit.BeforeClass;
import org.junit.Test;

import com.litle.sdk.generate.GiftCardCardType;
import com.litle.sdk.generate.MethodOfPaymentTypeEnum;
import com.litle.sdk.generate.RefundReversal;
import com.litle.sdk.generate.RefundReversalResponse;
import com.sun.org.apache.xerces.internal.jaxp.datatype.XMLGregorianCalendarImpl;

public class TestRefundReversal {

    private static LitleOnline litle;

    @BeforeClass
    public static void beforeClass() throws Exception {
        litle = new LitleOnline();
    }

    @Test
    public void simpleRefunfreversal() throws Exception {
     RefundReversal refundReversal=new RefundReversal();
        GiftCardCardType giftCard = new GiftCardCardType();
        giftCard.setType(MethodOfPaymentTypeEnum.GC);
        giftCard.setCardValidationNum("411");
        giftCard.setExpDate("0655");
        giftCard.setNumber("4100000000000000");
        giftCard.setPin("9999");

        refundReversal.setReportGroup("rptGrp");
        refundReversal.setId("id");



        refundReversal.setLitleTxnId(369852147l);
        refundReversal.setCard(giftCard);
        refundReversal.setOriginalRefCode("ref");
        refundReversal.setOriginalAmount(44455l);
        refundReversal.setOriginalTxnTime(new XMLGregorianCalendarImpl(new GregorianCalendar()));
        refundReversal.setOriginalSystemTraceId(0);
        refundReversal.setOriginalSequenceNumber("333333");

        RefundReversalResponse response=litle.refundReversal(refundReversal);
        assertEquals("Approved", response.getMessage());


    }



}
