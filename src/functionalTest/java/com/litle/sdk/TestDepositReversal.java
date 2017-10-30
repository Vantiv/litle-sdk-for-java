
package com.litle.sdk;
import static org.junit.Assert.assertEquals;

import java.util.GregorianCalendar;

import org.junit.BeforeClass;
import org.junit.Test;

import com.litle.sdk.generate.DepositReversal;
import com.litle.sdk.generate.DepositReversalResponse;
import com.litle.sdk.generate.GiftCardCardType;
import com.litle.sdk.generate.MethodOfPaymentTypeEnum;
import com.sun.org.apache.xerces.internal.jaxp.datatype.XMLGregorianCalendarImpl;

public class TestDepositReversal {

	private static LitleOnline litle;

	@BeforeClass
	public static void beforeClass() throws Exception {
		litle = new LitleOnline();
	}

    @Test
    public void simpleDepositReversal() throws Exception {
        DepositReversal depositReversal=new DepositReversal();
     //   DeactivateReversal deactivateReversal=new DeactivateReversal();
        GiftCardCardType giftCard = new GiftCardCardType();
        giftCard.setType(MethodOfPaymentTypeEnum.GC);
        giftCard.setCardValidationNum("411");
        giftCard.setExpDate("0655");
        giftCard.setNumber("4100000000000000");
        giftCard.setPin("9999");

        depositReversal.setReportGroup("rptGrp");
        depositReversal.setId("id");



        depositReversal.setLitleTxnId(369852147l);
        depositReversal.setCard(giftCard);
        depositReversal.setOriginalRefCode("ref");
        depositReversal.setOriginalAmount(44455l);
        depositReversal.setOriginalTxnTime(new XMLGregorianCalendarImpl(new GregorianCalendar()));
        depositReversal.setOriginalSystemTraceId(0);
        depositReversal.setOriginalSequenceNumber("333333");

        DepositReversalResponse response=litle.depositReversal(depositReversal);
        assertEquals("Approved", response.getMessage());


    }



}
