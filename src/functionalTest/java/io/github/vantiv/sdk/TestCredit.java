package io.github.vantiv.sdk;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.junit.BeforeClass;
import org.junit.Test;

import io.github.vantiv.sdk.generate.CardType;
import io.github.vantiv.sdk.generate.Credit;
import io.github.vantiv.sdk.generate.Credit.Paypal;
import io.github.vantiv.sdk.generate.CreditResponse;
import io.github.vantiv.sdk.generate.MethodOfPaymentTypeEnum;
import io.github.vantiv.sdk.generate.OrderSourceType;
import io.github.vantiv.sdk.generate.ProcessingInstructions;

public class TestCredit {

    private static LitleOnline litle;

    @BeforeClass
    public static void beforeClass() throws Exception {
        litle = new LitleOnline();
    }

    @Test
    public void simpleCreditWithCard() throws Exception {
        Credit credit = new Credit();
        credit.setAmount(106L);
        credit.setOrderId("12344");
        credit.setOrderSource(OrderSourceType.ECOMMERCE);
        CardType card = new CardType();
        card.setType(MethodOfPaymentTypeEnum.VI);
        card.setNumber("4100000000000001");
        card.setExpDate("1210");
        credit.setCard(card);
        credit.setId("id");
        CreditResponse response = litle.credit(credit);
        assertEquals("Approved", response.getMessage());
    }

    @Test
    public void simpleCreditWithPaypal() throws Exception {
        Credit credit = new Credit();
        credit.setAmount(106L);
        credit.setOrderId("123456");
        credit.setOrderSource(OrderSourceType.ECOMMERCE);
        Paypal paypal = new Paypal();
        paypal.setPayerId("1234");
        credit.setPaypal(paypal);
        credit.setId("id");
        CreditResponse response = litle.credit(credit);
        assertEquals("Approved", response.getMessage());
    }

    @Test
    public void simpleCreditWithCardAndSecondaryAmount() throws Exception {
        Credit credit = new Credit();
        credit.setAmount(106L);
        credit.setSecondaryAmount(20L);
        credit.setOrderId("12344");
        credit.setOrderSource(OrderSourceType.ECOMMERCE);
        CardType card = new CardType();
        card.setType(MethodOfPaymentTypeEnum.VI);
        card.setNumber("4100000000000001");
        card.setExpDate("1210");
        credit.setCard(card);
        credit.setId("id");
        CreditResponse response = litle.credit(credit);
        assertEquals("Approved", response.getMessage());
    }

    @Test
    public void simpleCreditWithTxnAndSecondaryAmount() throws Exception {
        Credit credit = new Credit();
        credit.setAmount(106L);
        credit.setSecondaryAmount(20L);
        credit.setLitleTxnId(1234L);
        credit.setId("id");
        CreditResponse response = litle.credit(credit);
        assertEquals("Transaction Received", response.getMessage());
    }

    @Test
    public void simpleCreditConflictWithTxnAndOrderId() throws Exception {
        Credit credit = new Credit();
        credit.setOrderId("12344");
        credit.setAmount(106L);
        credit.setSecondaryAmount(20L);
        credit.setLitleTxnId(1234L);
        credit.setId("id");
        try {
            litle.credit(credit);
            fail("Litle Txn and Order Id should conflict, fail to throw a exception");
        } catch (Exception e) {
            assertTrue(e.getMessage(),e.getMessage().startsWith("Error validating xml data against the schema"));
        }
    }

    @Test
    public void paypalNotes() throws Exception {
        Credit credit = new Credit();
        credit.setAmount(106L);
        credit.setOrderId("12344");
        credit.setPayPalNotes("Hello");
        credit.setOrderSource(OrderSourceType.ECOMMERCE);
        CardType card = new CardType();
        card.setType(MethodOfPaymentTypeEnum.VI);
        card.setNumber("4100000000000001");
        card.setExpDate("1210");
        credit.setCard(card);
        credit.setId("id");
        CreditResponse response = litle.credit(credit);
        assertEquals("Approved", response.getMessage());
    }

    @Test
    public void processingInstructionAndAmexData() throws Exception {
        Credit credit = new Credit();
        credit.setAmount(2000L);
        credit.setOrderId("12344");
        credit.setOrderSource(OrderSourceType.ECOMMERCE);
        ProcessingInstructions processinginstructions = new ProcessingInstructions();
        processinginstructions.setBypassVelocityCheck(true);
        credit.setProcessingInstructions(processinginstructions);
        CardType card = new CardType();
        card.setType(MethodOfPaymentTypeEnum.VI);
        card.setNumber("4100000000000001");
        card.setExpDate("1210");
        credit.setCard(card);
        credit.setId("id");
        CreditResponse response = litle.credit(credit);
        assertEquals("Approved", response.getMessage());
    }
    
    @Test
    public void testCreditWithLitleTxnId() throws Exception {
    	Credit credit = new Credit();
    	credit.setId("id_required");
    	credit.setLitleTxnId(112233445566778001l);
    	credit.setAmount(2000L);
    	
    	CreditResponse response = litle.credit(credit);
    	
    	assertEquals("Transaction Received", response.getMessage());
    }
    
    @Test
    public void testCreditWithLitleTxnId_andOrderId() throws Exception {
    	Credit credit = new Credit();
    	credit.setId("id_required");
    	credit.setLitleTxnId(112233445566778899l);
    	credit.setOrderId("cannot have order and txn Id");
    	credit.setAmount(2000L);
    	
    	try {
    		litle.credit(credit);
    		fail("Shoule throw exception!");
    	} catch (LitleOnlineException lole) {
    		String exception = "Error validating xml data against the schema: cvc-maxLength-valid: Value 'cannot have order and txn Id' with length = '28' is not facet-valid with respect to maxLength '25' for type 'string25Type'.";
    		assertEquals(exception, lole.getMessage());
    	}
    	
    }

}
