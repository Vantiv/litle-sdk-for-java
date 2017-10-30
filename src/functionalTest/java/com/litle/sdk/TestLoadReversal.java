package com.litle.sdk;

import static org.junit.Assert.assertEquals;

import java.util.GregorianCalendar;

import org.junit.BeforeClass;
import org.junit.Test;

import com.litle.sdk.generate.GiftCardCardType;
import com.litle.sdk.generate.LoadReversal;
import com.litle.sdk.generate.LoadReversalResponse;
import com.litle.sdk.generate.MethodOfPaymentTypeEnum;
import com.sun.org.apache.xerces.internal.jaxp.datatype.XMLGregorianCalendarImpl;

public class TestLoadReversal {

    private static LitleOnline litle;

    @BeforeClass
    public static void beforeClass() throws Exception {
        litle = new LitleOnline();
    }

    @Test
    public void simpleLoad() throws Exception {
     LoadReversal loadReversal=new LoadReversal();
        GiftCardCardType giftCard = new GiftCardCardType();
        giftCard.setType(MethodOfPaymentTypeEnum.GC);
        giftCard.setCardValidationNum("411");
        giftCard.setExpDate("0655");
        giftCard.setNumber("4100000000000000");
        giftCard.setPin("9999");

        loadReversal.setReportGroup("rptGrp");
        loadReversal.setId("id");



        loadReversal.setLitleTxnId(369852147l);
        loadReversal.setCard(giftCard);
        loadReversal.setOriginalRefCode("ref");
        loadReversal.setOriginalAmount(44455l);
        loadReversal.setOriginalTxnTime(new XMLGregorianCalendarImpl(new GregorianCalendar()));
        loadReversal.setOriginalSystemTraceId(0);
        loadReversal.setOriginalSequenceNumber("333333");

        LoadReversalResponse response=litle.loadReversal(loadReversal);
      //  DeactivateReversalResponse response=litle.deactivateReversal(deactivateReversal);
        assertEquals("Approved", response.getMessage());


    }



}
