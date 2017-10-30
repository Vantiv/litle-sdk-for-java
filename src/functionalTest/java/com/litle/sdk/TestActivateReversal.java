package com.litle.sdk;

import static org.junit.Assert.*;

import java.util.GregorianCalendar;

import org.junit.BeforeClass;
import org.junit.Test;

import com.litle.sdk.generate.ActivateReversal;
import com.litle.sdk.generate.ActivateReversalResponse;
import com.litle.sdk.generate.GiftCardCardType;
import com.litle.sdk.generate.MethodOfPaymentTypeEnum;
import com.sun.org.apache.xerces.internal.jaxp.datatype.XMLGregorianCalendarImpl;

public class TestActivateReversal {
    private static LitleOnline litle;

    @BeforeClass
    public static void beforeClass() throws Exception {
        litle = new LitleOnline();
    }

    @Test
    public void simpleActivate() throws Exception {

       ActivateReversal activateReversal=new ActivateReversal();
        GiftCardCardType giftCard = new GiftCardCardType();
        giftCard.setType(MethodOfPaymentTypeEnum.GC);
        giftCard.setCardValidationNum("411");
        giftCard.setExpDate("0655");
        giftCard.setNumber("4100000000000000");
        giftCard.setPin("9999");

        activateReversal.setReportGroup("rptGrp");
        activateReversal.setId("id");



       // activateReversal.setLitleTxnId(369852147l);
        activateReversal.setCard(giftCard);
        activateReversal.setOriginalRefCode("ref");
        activateReversal.setOriginalAmount(44455l);
        activateReversal.setOriginalTxnTime(new XMLGregorianCalendarImpl(new GregorianCalendar()));
        activateReversal.setOriginalSystemTraceId(0);
        activateReversal.setOriginalSequenceNumber("333333");

        ActivateReversalResponse response=litle.activateReversal(activateReversal);
        assertEquals("Approved", response.getMessage());

    }




}
