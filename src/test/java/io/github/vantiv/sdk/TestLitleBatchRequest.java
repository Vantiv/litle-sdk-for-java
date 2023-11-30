package io.github.vantiv.sdk;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.FileNotFoundException;
import java.math.BigInteger;
import java.util.Properties;

import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import io.github.vantiv.sdk.generate.AccountUpdate;
import io.github.vantiv.sdk.generate.ApplepayHeaderType;
import io.github.vantiv.sdk.generate.ApplepayType;
import io.github.vantiv.sdk.generate.AuthReversal;
import io.github.vantiv.sdk.generate.Authorization;
import io.github.vantiv.sdk.generate.Capture;
import io.github.vantiv.sdk.generate.CaptureGivenAuth;
import io.github.vantiv.sdk.generate.CardType;
import io.github.vantiv.sdk.generate.Credit;
import io.github.vantiv.sdk.generate.EcheckCredit;
import io.github.vantiv.sdk.generate.EcheckRedeposit;
import io.github.vantiv.sdk.generate.EcheckSale;
import io.github.vantiv.sdk.generate.EcheckVerification;
import io.github.vantiv.sdk.generate.ForceCapture;
import io.github.vantiv.sdk.generate.MethodOfPaymentTypeEnum;
import io.github.vantiv.sdk.generate.OrderSourceType;
import io.github.vantiv.sdk.generate.RegisterTokenRequestType;
import io.github.vantiv.sdk.generate.Sale;
import io.github.vantiv.sdk.generate.UpdateCardValidationNumOnToken;

public class TestLitleBatchRequest {

	private static LitleBatchFileRequest litleBatchFileRequest;
	private static LitleBatchRequest litleBatchRequest;
	Properties property;
	@Before
	public void before() throws Exception {
		property = new Properties();
		property.setProperty("username", "PHXMLTEST");
		property.setProperty("password", "password");
		property.setProperty("version", "8.18");
		property.setProperty("maxAllowedTransactionsPerFile", "6");
		property.setProperty("maxTransactionsPerBatch", "4");
		property.setProperty("batchHost", "localhost");
		property.setProperty("batchPort", "2104");
		property.setProperty("batchTcpTimeout", "10000");
		property.setProperty("batchUseSSL", "false");
		property.setProperty("merchantId", "101");
		property.setProperty("proxyHost", "");
        property.setProperty("proxyPort", "");
		property.setProperty("batchRequestFolder", "test/unit/");
        property.setProperty("batchResponseFolder", "test/unit/");
        property.setProperty("sftpUsername","");
        property.setProperty("sftpPassword","");
		litleBatchFileRequest = new LitleBatchFileRequest("testFile", property);

		litleBatchRequest = litleBatchFileRequest.createBatch("101");
		Marshaller mockMarshaller = Mockito.mock(Marshaller.class);
		litleBatchRequest.setMarshaller(mockMarshaller);
		litleBatchRequest.setNumOfTxn(1);
	}

	@Test
	public void testGetNumberOfTransactions() throws FileNotFoundException, JAXBException{
		assertEquals(1, litleBatchRequest.getNumberOfTransactions());
		litleBatchRequest.addTransaction(createTestSale(100L, "100"));
		assertEquals(2, litleBatchRequest.getNumberOfTransactions());
	}

	@Test
	public void testIsFull() throws FileNotFoundException, JAXBException{
		assertTrue(!litleBatchRequest.isFull());
		litleBatchRequest.addTransaction(createTestSale(100L, "100"));
		litleBatchRequest.addTransaction(createTestSale(100L, "100"));
		litleBatchRequest.addTransaction(createTestSale(100L, "100"));
		assertTrue(litleBatchRequest.isFull());
	}

	@Test
	public void testVerifyFileThresholds() throws FileNotFoundException, JAXBException{

		litleBatchRequest.addTransaction(createTestSale(100L, "100"));
		litleBatchRequest.addTransaction(createTestSale(100L, "100"));
		assertEquals(litleBatchRequest.verifyFileThresholds(), TransactionCodeEnum.SUCCESS);
		litleBatchRequest.addTransaction(createTestSale(100L, "100"));
		assertEquals(litleBatchRequest.verifyFileThresholds(), TransactionCodeEnum.BATCHFULL);

		LitleBatchRequest batchRequest2 = litleBatchFileRequest.createBatch("102");
		Marshaller mockMarshaller = Mockito.mock(Marshaller.class);
        batchRequest2.setMarshaller(mockMarshaller);
        batchRequest2.setNumOfTxn(1);

		batchRequest2.addTransaction(createTestSale(100L, "100"));
		assertEquals(litleBatchRequest.verifyFileThresholds(), TransactionCodeEnum.FILEFULL);
	}

	@Test
	public void testAddTransaction() throws FileNotFoundException, JAXBException{
		assertEquals(litleBatchRequest.addTransaction(createTestSale(100L, "100")), TransactionCodeEnum.SUCCESS);
		assertEquals(litleBatchRequest.addTransaction(createTestSale(100L, "100")), TransactionCodeEnum.SUCCESS);
		assertEquals(litleBatchRequest.addTransaction(createTestSale(100L, "100")), TransactionCodeEnum.BATCHFULL);

		boolean batchFullException = false;
		try{
			litleBatchRequest.addTransaction(createTestSale(100L, "100"));
		} catch (LitleBatchException e){
			batchFullException = true;
		}

		assertTrue(batchFullException);

		LitleBatchRequest batchRequest2 = litleBatchFileRequest.createBatch("102");
		batchRequest2.setNumOfTxn(1);
        Marshaller mockMarshaller = Mockito.mock(Marshaller.class);
        batchRequest2.setMarshaller(mockMarshaller);

		assertEquals(batchRequest2.addTransaction(createTestSale(100L, "100")),TransactionCodeEnum.FILEFULL);

		boolean fileFullException = false;
		try{
			batchRequest2.addTransaction(createTestSale(100L, "100"));
		} catch (LitleBatchException e){
			fileFullException = true;
		}

		assertTrue(fileFullException);
	}

	public Sale createTestSale(Long amount, String orderId){
		Sale sale = new Sale();
		sale.setAmount(amount);
		sale.setOrderId(orderId);
		sale.setOrderSource(OrderSourceType.ECOMMERCE);
		CardType card = new CardType();
		card.setType(MethodOfPaymentTypeEnum.VI);
		card.setNumber("4100000000000002");
		card.setExpDate("1210");
		sale.setCard(card);
		sale.setReportGroup("test");
		return sale;
	}


	 @Test
	    public void testAddSale(){
	        litleBatchFileRequest = new LitleBatchFileRequest("testFile", property);
	        litleBatchRequest = litleBatchFileRequest.createBatch("101");

	        litleBatchRequest.setNumOfTxn(1);
	        Marshaller mockMarshaller = Mockito.mock(Marshaller.class);
	        litleBatchRequest.setMarshaller(mockMarshaller);

	        Sale sale = new Sale();
	        sale.setAmount(25L);
	        sale.setSecondaryAmount(10L);
	        sale.setApplepay(createApplepay());
	        litleBatchRequest.addTransaction(sale);
	        assertEquals(25, litleBatchRequest.getBatchRequest().getSaleAmount().intValue());
	        assertEquals(1, litleBatchRequest.getBatchRequest().getNumSales().intValue());
	        assertEquals(2, litleBatchRequest.getNumberOfTransactions());

	    }


	@Test
	public void testAddAuth(){
	    litleBatchFileRequest = new LitleBatchFileRequest("testFile", property);
        litleBatchRequest = litleBatchFileRequest.createBatch("101");

        litleBatchRequest.setNumOfTxn(1);
        Marshaller mockMarshaller = Mockito.mock(Marshaller.class);
        litleBatchRequest.setMarshaller(mockMarshaller);

        Authorization auth = new Authorization();
        auth.setAmount(25L);
        auth.setSecondaryAmount(10L);
        auth.setApplepay(createApplepay());
        litleBatchRequest.addTransaction(auth);
        assertEquals(25, litleBatchRequest.getBatchRequest().getAuthAmount().intValue());
        assertEquals(1, litleBatchRequest.getBatchRequest().getNumAuths().intValue());
        assertEquals(2, litleBatchRequest.getNumberOfTransactions());
	}




	   @Test
	    public void testAddCredit(){
	        litleBatchFileRequest = new LitleBatchFileRequest("testFile", property);
	        litleBatchRequest = litleBatchFileRequest.createBatch("101");

	        litleBatchRequest.setNumOfTxn(1);
            Marshaller mockMarshaller = Mockito.mock(Marshaller.class);
            litleBatchRequest.setMarshaller(mockMarshaller);

	        Credit credit = new Credit();
	        credit.setAmount(25L);
	        credit.setSecondaryAmount(10L);
	        litleBatchRequest.addTransaction(credit);
	        assertEquals(25, litleBatchRequest.getBatchRequest().getCreditAmount().intValue());
	        assertEquals(1, litleBatchRequest.getBatchRequest().getNumCredits().intValue());
	        assertEquals(2, litleBatchRequest.getNumberOfTransactions());

	    }

       @Test
       public void testAddRegisterTokenRequestType(){
           litleBatchFileRequest = new LitleBatchFileRequest("testFile", property);
           litleBatchRequest = litleBatchFileRequest.createBatch("101");

           litleBatchRequest.setNumOfTxn(1);
           Marshaller mockMarshaller = Mockito.mock(Marshaller.class);
           litleBatchRequest.setMarshaller(mockMarshaller);

           RegisterTokenRequestType registerTokenRequest = new RegisterTokenRequestType();
           registerTokenRequest.setApplepay(createApplepay());
           litleBatchRequest.addTransaction(registerTokenRequest);
           assertEquals(1, litleBatchRequest.getBatchRequest().getNumTokenRegistrations().intValue());
           assertEquals(2, litleBatchRequest.getNumberOfTransactions());

       }

       @Test
       public void testAddCaptureGivenAuth(){
           litleBatchFileRequest = new LitleBatchFileRequest("testFile", property);
           litleBatchRequest = litleBatchFileRequest.createBatch("101");

           litleBatchRequest.setNumOfTxn(1);
           Marshaller mockMarshaller = Mockito.mock(Marshaller.class);
           litleBatchRequest.setMarshaller(mockMarshaller);

           CaptureGivenAuth captureGivenAuth = new CaptureGivenAuth();
           captureGivenAuth.setAmount(25L);
           captureGivenAuth.setSecondaryAmount(10L);
           litleBatchRequest.addTransaction(captureGivenAuth);
           assertEquals(25, litleBatchRequest.getBatchRequest().getCaptureGivenAuthAmount().intValue());
           assertEquals(1, litleBatchRequest.getBatchRequest().getNumCaptureGivenAuths().intValue());
           assertEquals(2, litleBatchRequest.getNumberOfTransactions());

       }


       @Test
       public void testAddForceCapture(){
           litleBatchFileRequest = new LitleBatchFileRequest("testFile", property);
           litleBatchRequest = litleBatchFileRequest.createBatch("101");

           litleBatchRequest.setNumOfTxn(1);
           Marshaller mockMarshaller = Mockito.mock(Marshaller.class);
           litleBatchRequest.setMarshaller(mockMarshaller);

           ForceCapture forceCapture = new ForceCapture();
           forceCapture.setAmount(25L);
           forceCapture.setSecondaryAmount(10L);
           litleBatchRequest.addTransaction(forceCapture);
           assertEquals(25, litleBatchRequest.getBatchRequest().getForceCaptureAmount().intValue());
           assertEquals(1, litleBatchRequest.getBatchRequest().getNumForceCaptures().intValue());
           assertEquals(2, litleBatchRequest.getNumberOfTransactions());

       }

       @Test
       public void testAddAuthReversal(){
           litleBatchFileRequest = new LitleBatchFileRequest("testFile", property);
           litleBatchRequest = litleBatchFileRequest.createBatch("101");

           litleBatchRequest.setNumOfTxn(1);
           Marshaller mockMarshaller = Mockito.mock(Marshaller.class);
           litleBatchRequest.setMarshaller(mockMarshaller);

           AuthReversal authReversal = new AuthReversal();
           authReversal.setAmount(25L);
           litleBatchRequest.addTransaction(authReversal);
           assertEquals(25, litleBatchRequest.getBatchRequest().getAuthReversalAmount().intValue());
           assertEquals(1, litleBatchRequest.getBatchRequest().getNumAuthReversals().intValue());
           assertEquals(2, litleBatchRequest.getNumberOfTransactions());

       }


       @Test
       public void testAddCapture(){
           litleBatchFileRequest = new LitleBatchFileRequest("testFile", property);
           litleBatchRequest = litleBatchFileRequest.createBatch("101");

           litleBatchRequest.setNumOfTxn(1);
           Marshaller mockMarshaller = Mockito.mock(Marshaller.class);
           litleBatchRequest.setMarshaller(mockMarshaller);

           Capture capture = new Capture();
           capture.setAmount(25L);
           litleBatchRequest.addTransaction(capture);
           assertEquals(25, litleBatchRequest.getBatchRequest().getCaptureAmount().intValue());
           assertEquals(1, litleBatchRequest.getBatchRequest().getNumCaptures().intValue());
           assertEquals(2, litleBatchRequest.getNumberOfTransactions());

       }


       @Test
       public void testAddEcheckVerification(){
           litleBatchFileRequest = new LitleBatchFileRequest("testFile", property);
           litleBatchRequest = litleBatchFileRequest.createBatch("101");

           litleBatchRequest.setNumOfTxn(1);
           Marshaller mockMarshaller = Mockito.mock(Marshaller.class);
           litleBatchRequest.setMarshaller(mockMarshaller);

           EcheckVerification echeckVerification = new EcheckVerification();
           echeckVerification.setAmount(25L);
           litleBatchRequest.addTransaction(echeckVerification);
           assertEquals(25, litleBatchRequest.getBatchRequest().getEcheckVerificationAmount().intValue());
           assertEquals(1, litleBatchRequest.getBatchRequest().getNumEcheckVerification().intValue());
           assertEquals(2, litleBatchRequest.getNumberOfTransactions());

       }

       @Test
       public void testAddEcheckCredit(){
           litleBatchFileRequest = new LitleBatchFileRequest("testFile", property);
           litleBatchRequest = litleBatchFileRequest.createBatch("101");

           litleBatchRequest.setNumOfTxn(1);
           Marshaller mockMarshaller = Mockito.mock(Marshaller.class);
           litleBatchRequest.setMarshaller(mockMarshaller);

           EcheckCredit echeckCredit = new EcheckCredit();
           echeckCredit.setAmount(25L);
           echeckCredit.setSecondaryAmount(10L);
           litleBatchRequest.addTransaction(echeckCredit);
           assertEquals(25, litleBatchRequest.getBatchRequest().getEcheckCreditAmount().intValue());
           assertEquals(1, litleBatchRequest.getBatchRequest().getNumEcheckCredit().intValue());
           assertEquals(2, litleBatchRequest.getNumberOfTransactions());

       }


       @Test
       public void testAddEcheckRedeposit(){
           litleBatchFileRequest = new LitleBatchFileRequest("testFile", property);
           litleBatchRequest = litleBatchFileRequest.createBatch("101");

           litleBatchRequest.setNumOfTxn(1);
           Marshaller mockMarshaller = Mockito.mock(Marshaller.class);
           litleBatchRequest.setMarshaller(mockMarshaller);

           EcheckRedeposit echeckRedeposit = new EcheckRedeposit();
           litleBatchRequest.addTransaction(echeckRedeposit);
           assertEquals(1, litleBatchRequest.getBatchRequest().getNumEcheckRedeposit().intValue());
           assertEquals(2, litleBatchRequest.getNumberOfTransactions());

       }

       @Test
       public void testAddEcheckSale(){
           litleBatchFileRequest = new LitleBatchFileRequest("testFile", property);
           litleBatchRequest = litleBatchFileRequest.createBatch("101");

           litleBatchRequest.setNumOfTxn(1);
           Marshaller mockMarshaller = Mockito.mock(Marshaller.class);
           litleBatchRequest.setMarshaller(mockMarshaller);

           EcheckSale echeckSale = new EcheckSale();
           echeckSale.setAmount(25L);
           echeckSale.setSecondaryAmount(10L);
           litleBatchRequest.addTransaction(echeckSale);
           assertEquals(25, litleBatchRequest.getBatchRequest().getEcheckSalesAmount().intValue());
           assertEquals(1, litleBatchRequest.getBatchRequest().getNumEcheckSales().intValue());
           assertEquals(2, litleBatchRequest.getNumberOfTransactions());

       }


       @Test
       public void testAddUpdateCardValidationNumOnToken(){
           litleBatchFileRequest = new LitleBatchFileRequest("testFile", property);
           litleBatchRequest = litleBatchFileRequest.createBatch("101");

           litleBatchRequest.setNumOfTxn(1);
           Marshaller mockMarshaller = Mockito.mock(Marshaller.class);
           litleBatchRequest.setMarshaller(mockMarshaller);

           UpdateCardValidationNumOnToken updateCardValidationNumOnToken = new UpdateCardValidationNumOnToken();
           litleBatchRequest.addTransaction(updateCardValidationNumOnToken);
           assertEquals(1, litleBatchRequest.getBatchRequest().getNumUpdateCardValidationNumOnTokens().intValue());
           assertEquals(2, litleBatchRequest.getNumberOfTransactions());

       }

       @Test
       public void testAddAccountUpdate(){
           litleBatchFileRequest = new LitleBatchFileRequest("testFile", property);
           litleBatchRequest = litleBatchFileRequest.createBatch("101");

           litleBatchRequest.setNumOfTxn(1);
           litleBatchRequest.getBatchRequest().setNumAccountUpdates(BigInteger.valueOf(1l));
           Marshaller mockMarshaller = Mockito.mock(Marshaller.class);
           litleBatchRequest.setMarshaller(mockMarshaller);

           AccountUpdate accountUpdate = new AccountUpdate();
           litleBatchRequest.addTransaction(accountUpdate);
           assertEquals(2, litleBatchRequest.getBatchRequest().getNumAccountUpdates().intValue());
           assertEquals(2, litleBatchRequest.getNumberOfTransactions());

       }



       @Test(expected=LitleBatchException.class)
       public void testAddAUBlock_AU_side(){

           litleBatchFileRequest = new LitleBatchFileRequest("testFile", property);
           litleBatchRequest = litleBatchFileRequest.createBatch("101");



           AccountUpdate accountUpdate = new AccountUpdate();
           litleBatchRequest.setNumOfTxn(1);
           litleBatchRequest.getBatchRequest().setNumAccountUpdates(BigInteger.valueOf(1l));
           Marshaller mockMarshaller = Mockito.mock(Marshaller.class);
           litleBatchRequest.setMarshaller(mockMarshaller);

           litleBatchRequest.addTransaction(accountUpdate);



           assertEquals(2, litleBatchRequest.getBatchRequest().getNumAccountUpdates().intValue());
           assertEquals(2, litleBatchRequest.getNumberOfTransactions());

           Sale sale = new Sale();
           sale.setAmount(5L);

           litleBatchRequest.addTransaction(sale);
       }


       @Test(expected=LitleBatchException.class)
       public void testAddAUBlock_Sale_side(){

           litleBatchFileRequest = new LitleBatchFileRequest("testFile", property);
           litleBatchRequest = litleBatchFileRequest.createBatch("101");
           Sale sale = new Sale();
           sale.setAmount(5L);

           litleBatchRequest.setNumOfTxn(1);
           Marshaller mockMarshaller = Mockito.mock(Marshaller.class);
           litleBatchRequest.setMarshaller(mockMarshaller);

           litleBatchRequest.addTransaction(sale);
           assertEquals(1, litleBatchRequest.getBatchRequest().getNumSales().intValue());
           assertEquals(5, litleBatchRequest.getBatchRequest().getSaleAmount().intValue());
           assertEquals(2, litleBatchRequest.getNumberOfTransactions());

           AccountUpdate accountUpdate = new AccountUpdate();

           litleBatchRequest.addTransaction(accountUpdate);
       }


       private ApplepayType createApplepay(){
           ApplepayType applepayType = new ApplepayType();
           ApplepayHeaderType applepayHeaderType = new ApplepayHeaderType();
           applepayHeaderType.setApplicationData("e3b0c44298fc1c149afbf4c8996fb92427ae41e4649b934ca495991b7852b855");
           applepayHeaderType.setEphemeralPublicKey("e3b0c44298fc1c149afbf4c8996fb92427ae41e4649b934ca495991b7852b855");
           applepayHeaderType.setPublicKeyHash("e3b0c44298fc1c149afbf4c8996fb92427ae41e4649b934ca495991b7852b855");
           applepayHeaderType.setTransactionId("1234");
           applepayType.setHeader(applepayHeaderType);
           applepayType.setData("user");
           applepayType.setSignature("sign");
           applepayType.setVersion("1");
           return applepayType;
       }

}
