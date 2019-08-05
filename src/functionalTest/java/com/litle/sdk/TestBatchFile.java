package com.litle.sdk;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Calendar;
import java.util.Properties;

import javax.xml.bind.JAXBException;

import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestRule;
import org.junit.runner.Description;
import org.junit.runners.model.Statement;
import org.junit.Assume;

import com.litle.sdk.generate.AccountUpdate;
import com.litle.sdk.generate.AccountUpdateResponse;
import com.litle.sdk.generate.Activate;
import com.litle.sdk.generate.ActivateResponse;
import com.litle.sdk.generate.AuthInformation;
import com.litle.sdk.generate.AuthReversal;
import com.litle.sdk.generate.AuthReversalResponse;
import com.litle.sdk.generate.Authorization;
import com.litle.sdk.generate.AuthorizationResponse;
import com.litle.sdk.generate.BalanceInquiry;
import com.litle.sdk.generate.BalanceInquiryResponse;
import com.litle.sdk.generate.CancelSubscription;
import com.litle.sdk.generate.CancelSubscriptionResponse;
import com.litle.sdk.generate.Capture;
import com.litle.sdk.generate.CaptureGivenAuth;
import com.litle.sdk.generate.CaptureGivenAuthResponse;
import com.litle.sdk.generate.CaptureResponse;
import com.litle.sdk.generate.CardType;
import com.litle.sdk.generate.Contact;
import com.litle.sdk.generate.CreatePlan;
import com.litle.sdk.generate.CreatePlanResponse;
import com.litle.sdk.generate.Credit;
import com.litle.sdk.generate.CreditResponse;
import com.litle.sdk.generate.Deactivate;
import com.litle.sdk.generate.DeactivateResponse;
import com.litle.sdk.generate.EcheckAccountTypeEnum;
import com.litle.sdk.generate.EcheckCredit;
import com.litle.sdk.generate.EcheckCreditResponse;
import com.litle.sdk.generate.EcheckRedeposit;
import com.litle.sdk.generate.EcheckRedepositResponse;
import com.litle.sdk.generate.EcheckSale;
import com.litle.sdk.generate.EcheckSalesResponse;
import com.litle.sdk.generate.EcheckType;
import com.litle.sdk.generate.EcheckVerification;
import com.litle.sdk.generate.EcheckVerificationResponse;
import com.litle.sdk.generate.ForceCapture;
import com.litle.sdk.generate.ForceCaptureResponse;
import com.litle.sdk.generate.IntervalTypeEnum;
import com.litle.sdk.generate.LitleTransactionInterface;
import com.litle.sdk.generate.Load;
import com.litle.sdk.generate.LoadResponse;
import com.litle.sdk.generate.MethodOfPaymentTypeEnum;
import com.litle.sdk.generate.ObjectFactory;
import com.litle.sdk.generate.OrderSourceType;
import com.litle.sdk.generate.RegisterTokenRequestType;
import com.litle.sdk.generate.RegisterTokenResponse;
import com.litle.sdk.generate.Sale;
import com.litle.sdk.generate.SaleResponse;
import com.litle.sdk.generate.Unload;
import com.litle.sdk.generate.UnloadResponse;
import com.litle.sdk.generate.UpdateCardValidationNumOnToken;
import com.litle.sdk.generate.UpdateCardValidationNumOnTokenResponse;
import com.litle.sdk.generate.UpdatePlan;
import com.litle.sdk.generate.UpdatePlanResponse;
import com.litle.sdk.generate.UpdateSubscription;
import com.litle.sdk.generate.UpdateSubscriptionResponse;

public class TestBatchFile {

    String merchantId = "0180";
    
    private String preliveStatus = System.getenv("preliveStatus");
    
    public static class FailedRule implements TestRule
    {       
        public Statement apply(final Statement base, final Description description)
        {
             return new Statement()
             {
                    @Override
                    public void evaluate() throws Throwable
                    {
                        try
                        {
                            base.evaluate();
                        }
                        catch (Throwable t)
                        {
                            System.out.println(description.getDisplayName() + " failure caused by :");
                            t.printStackTrace();
                            retry.setNotGood();
                            if (retry.isLastTry())
                            {
                                throw t;
                            }
                            else
                            {
                                System.out.println("Retrying.");
                            }
                        }
                    }
            };
        }
    }

    public static class RetryRule implements TestRule
    {
        private int retryCount, currentTry;

        private boolean allGood = false;

        public RetryRule(int retryCount)
        {
            this.retryCount = retryCount;
            this.currentTry = 1;
        }

        public boolean isLastTry()
        {
            return currentTry == retryCount;
        }

        public void setNotGood()
        {
            allGood = false;
        }

        public Statement apply(final Statement base, final Description description)
        {
            return new Statement()
            {
                @Override
                public void evaluate() throws Throwable
                {
                    // implement retry logic here
                    for (; currentTry <= retryCount && !allGood; currentTry++)
                    {
                        allGood = true;
                        base.evaluate();
                    }
                }
            };
        }
    }

    @ClassRule
    public static RetryRule retry = new RetryRule(3);

    @Rule
    public FailedRule onFailed = new FailedRule();

    @Test
	public void testSendToLitle_WithFileConfig() throws Exception { 
		Assume.assumeFalse(this.preliveStatus.equalsIgnoreCase("down"));
		String requestFileName = "litleSdk-testBatchFile-fileConfig.xml";
		LitleBatchFileRequest request = new LitleBatchFileRequest(requestFileName);

        // request file is being set in the constructor
        assertNotNull(request.getFile());

		Properties configFromFile = request.getConfig();

		// pre-assert the config file has required param values
		assertEquals("prelive.litle.com", configFromFile.getProperty("batchHost"));
		//assertEquals("15000", configFromFile.getProperty("batchPort"));

		String workingDirRequests = configFromFile.getProperty("batchRequestFolder");
		prepDir(workingDirRequests);

		String workingDirResponses = configFromFile.getProperty("batchResponseFolder");
		prepDir(workingDirResponses);

		prepareTestRequest(request);

		/* call method under test */
		LitleBatchFileResponse response = request.sendToLitle();

		// assert response can be processed through Java API
		assertJavaApi(request, response);

		// assert request and response files were created properly
		assertGeneratedFiles(workingDirRequests, workingDirResponses, requestFileName, request, response);
	}

	@Test
	public void testSendToLitle_WithConfigOverrides() throws Exception { 
		Assume.assumeFalse(this.preliveStatus.equalsIgnoreCase("down"));
		String workingDir = System.getProperty("java.io.tmpdir");

		String workingDirRequests = workingDir + File.separator + "litleSdkTestBatchRequests";
		prepDir(workingDirRequests);

		String workingDirResponses = workingDir + File.separator + "litleSdkTestBatchResponses";
		prepDir(workingDirResponses);

		Properties configOverrides = new Properties();
		configOverrides.setProperty("batchHost", "prelive.litle.com");  // TODO - point this to the www.testlitle.com sandbox instead of prelive 
		configOverrides.setProperty("batchPort", "15000");


		configOverrides.setProperty("batchRequestFolder", workingDirRequests);
		configOverrides.setProperty("batchResponseFolder", workingDirResponses);

		String requestFileName = "litleSdk-testBatchFile-configOverrides.xml";
		LitleBatchFileRequest request = new LitleBatchFileRequest(requestFileName, configOverrides);

        // request file is being set in the constructor
        assertNotNull(request.getFile());

		prepareTestRequest(request);

		//actually add a transaction


		/* call method under test */
		LitleBatchFileResponse response = request.sendToLitle();

		// assert response can be processed through Java API
		assertJavaApi(request, response);

		// assert request and response files were created properly
		assertGeneratedFiles(workingDirRequests, workingDirResponses, requestFileName, request, response);
	}

	@Test
	public void testSendToLitleSFTP_WithPreviouslyCreatedFile() throws Exception { 
		Assume.assumeFalse(this.preliveStatus.equalsIgnoreCase("down"));
	    String requestFileName = "litleSdk-testBatchFile-fileConfigSFTP.xml";
        LitleBatchFileRequest request = new LitleBatchFileRequest(requestFileName);

        // request file is being set in the constructor
        assertNotNull(request.getFile());

        Properties configFromFile = request.getConfig();

        // pre-assert the config file has required param values
        assertEquals("prelive.litle.com", configFromFile.getProperty("batchHost"));
        //assertEquals("15000", configFromFile.getProperty("batchPort"));

        String workingDirRequests = configFromFile.getProperty("batchRequestFolder");
        prepDir(workingDirRequests);

        String workingDirResponses = configFromFile.getProperty("batchResponseFolder");
        prepDir(workingDirResponses);

        prepareTestRequest(request);

        // This should have generated the file
        request.prepareForDelivery();

        // Make sure the file exists
        File requestFile = request.getFile();
        assertTrue(requestFile.exists());
        assertTrue(requestFile.length() > 0);

        LitleBatchFileRequest request2 = new LitleBatchFileRequest(requestFileName);

        LitleBatchFileResponse response = request2.sendToLitleSFTP(true);

        // Assert response matches what was requested
        assertJavaApi(request2, response);

        // Make sure files were created correctly
        assertGeneratedFiles(workingDirRequests, workingDirResponses, requestFileName, request2, response);
	}

	@Test
    public void testSendOnlyToLitleSFTP_WithPreviouslyCreatedFile() throws Exception { 
		Assume.assumeFalse(this.preliveStatus.equalsIgnoreCase("down"));
	    // --- Prepare the batch file ---
        String requestFileName = "litleSdk-testBatchFile-fileConfigSFTP.xml";
        LitleBatchFileRequest request1 = new LitleBatchFileRequest(requestFileName);

        // request file is being set in the constructor
        assertNotNull(request1.getFile());

        Properties configFromFile = request1.getConfig();

        // pre-assert the config file has required param values
        assertEquals("prelive.litle.com", configFromFile.getProperty("batchHost"));
        //assertEquals("15000", configFromFile.getProperty("batchPort"));

        String workingDirRequests = configFromFile.getProperty("batchRequestFolder");
        prepDir(workingDirRequests);

        String workingDirResponses = configFromFile.getProperty("batchResponseFolder");
        prepDir(workingDirResponses);

        prepareTestRequest(request1);

        // This should have generated the file
        request1.prepareForDelivery();

        // Make sure the file exists
        File requestFile1 = request1.getFile();
        assertTrue(requestFile1.exists());
        assertTrue(requestFile1.length() > 0);

        // --- Send the batch to Litle's SFTP ---

        // Move request file to temporary location
        File requestFile2 = File.createTempFile("litle", "xml");
        copyFile(requestFile1, requestFile2);

        Properties configForRequest2 = (Properties) configFromFile.clone();
        configForRequest2.setProperty("batchRequestFolder", requestFile2.getParentFile().getCanonicalPath());

        LitleBatchFileRequest request2 = new LitleBatchFileRequest(requestFile2.getName(), configForRequest2);
        request2.sendOnlyToLitleSFTP(true);

        // --- Retrieve response ---
        LitleBatchFileRequest request3 = new LitleBatchFileRequest(requestFile2.getName(), configForRequest2);
        LitleBatchFileResponse response = request3.retrieveOnlyFromLitleSFTP();

        // Assert response matches what was requested
        assertJavaApi(request3, response);

        // Make sure files were created correctly
        assertGeneratedFiles(requestFile2.getParentFile().getCanonicalPath(), workingDirResponses, requestFile2.getName(), request3, response);
    }

	@Test
    public void testSendToLitleSFTP_WithFileConfig() throws Exception { 
		Assume.assumeFalse(this.preliveStatus.equalsIgnoreCase("down"));
        String requestFileName = "litleSdk-testBatchFile-fileConfigSFTP.xml";
        LitleBatchFileRequest request = new LitleBatchFileRequest(requestFileName);

        // request file is being set in the constructor
        assertNotNull(request.getFile());

        Properties configFromFile = request.getConfig();

        // pre-assert the config file has required param values
        assertEquals("prelive.litle.com", configFromFile.getProperty("batchHost"));
        //assertEquals("15000", configFromFile.getProperty("batchPort"));

        String workingDirRequests = configFromFile.getProperty("batchRequestFolder");
        prepDir(workingDirRequests);

        String workingDirResponses = configFromFile.getProperty("batchResponseFolder");
        prepDir(workingDirResponses);

        prepareTestRequest(request);

        /* call method under test */
        LitleBatchFileResponse response = request.sendToLitleSFTP();

        // assert response can be processed through Java API
        assertJavaApi(request, response);

        // assert request and response files were created properly
        assertGeneratedFiles(workingDirRequests, workingDirResponses, requestFileName, request, response);
    }

    @Test
    public void testSendToLitleSFTP_WithConfigOverrides() throws Exception { 
		Assume.assumeFalse(this.preliveStatus.equalsIgnoreCase("down"));
        String workingDir = System.getProperty("java.io.tmpdir");

        String workingDirRequests = workingDir + File.separator + "litleSdkTestBatchRequests";
        prepDir(workingDirRequests);

        String workingDirResponses = workingDir + File.separator + "litleSdkTestBatchResponses";
        prepDir(workingDirResponses);

        Properties configOverrides = new Properties();
        configOverrides.setProperty("batchHost", "prelive.litle.com");  // TODO - point this to the www.testlitle.com sandbox instead of prelive 
        configOverrides.setProperty("sftpTimeout", "720000");

        configOverrides.setProperty("batchRequestFolder", workingDirRequests);
        configOverrides.setProperty("batchResponseFolder", workingDirResponses);

        String requestFileName = "litleSdk-testBatchFile-configOverridesSFTP.xml";
        LitleBatchFileRequest request = new LitleBatchFileRequest(requestFileName, configOverrides);

        // request file is being set in the constructor
        assertNotNull(request.getFile());

        prepareTestRequest(request);

        /* call method under test */
        LitleBatchFileResponse response = request.sendToLitleSFTP();

        // assert response can be processed through Java API
        assertJavaApi(request, response);

        // assert request and response files were created properly
    }


	private void prepareTestRequest(LitleBatchFileRequest request) throws FileNotFoundException, JAXBException {
		LitleBatchRequest batchRequest1 = request.createBatch(merchantId);
		Sale sale11 = new Sale();
		sale11.setReportGroup("reportGroup11");
		sale11.setOrderId("orderId11");
		sale11.setAmount(1099L);
		sale11.setOrderSource(OrderSourceType.ECOMMERCE);

		CardType card = new CardType();
		card.setType(MethodOfPaymentTypeEnum.VI);
		card.setNumber("4457010000000009");
		card.setExpDate("0114");
		sale11.setCard(card);

		batchRequest1.addTransaction(sale11);
	}
	@Test
	public void testMechaBatchAndProcess(){
	    String requestFileName = "litleSdk-testBatchFile-MECHA.xml";
        LitleBatchFileRequest request = new LitleBatchFileRequest(requestFileName);

        Properties configFromFile = request.getConfig();

        // pre-assert the config file has required param values
        assertEquals("prelive.litle.com", configFromFile.getProperty("batchHost"));
        //assertEquals("15000", configFromFile.getProperty("batchPort"));

        LitleBatchRequest batch = request.createBatch(merchantId);

        //card
        CardType card = new CardType();
        card.setNumber("4100000000000001");
        card.setExpDate("1210");
        card.setType(MethodOfPaymentTypeEnum.VI);

        //echeck
        EcheckType echeck = new EcheckType();
        echeck.setAccNum("1234567890");
        echeck.setAccType(EcheckAccountTypeEnum.CHECKING);
        echeck.setRoutingNum("123456789");
        echeck.setCheckNum("123455");

        //billto address
        Contact contact = new Contact();
        contact.setName("Bob");
        contact.setCity("Lowell");
        contact.setState("MA");
        contact.setEmail("Bob@litle.com");

        Authorization auth = new Authorization();
        auth.setReportGroup("Planets");
        auth.setOrderId("12344");
        auth.setAmount(106L);
        auth.setOrderSource(OrderSourceType.ECOMMERCE);
        auth.setCard(card);
        batch.addTransaction(auth);

        Sale sale = new Sale();
        sale.setReportGroup("Planets");
        sale.setOrderId("12344");
        sale.setAmount(6000L);
        sale.setOrderSource(OrderSourceType.ECOMMERCE);
        sale.setCard(card);
        batch.addTransaction(sale);

        Credit credit = new Credit();
        credit.setReportGroup("Planets");
        credit.setOrderId("12344");
        credit.setAmount(106L);
        credit.setOrderSource(OrderSourceType.ECOMMERCE);
        credit.setCard(card);
        batch.addTransaction(credit);

        AuthReversal authReversal = new AuthReversal();
        authReversal.setReportGroup("Planets");
        authReversal.setLitleTxnId(12345678000L);
        authReversal.setAmount(106L);
        authReversal.setPayPalNotes("Notes");
        batch.addTransaction(authReversal);

        RegisterTokenRequestType registerTokenRequestType = new RegisterTokenRequestType();
        registerTokenRequestType.setReportGroup("Planets");
        registerTokenRequestType.setOrderId("12344");
        registerTokenRequestType.setAccountNumber("1233456789103801");
        batch.addTransaction(registerTokenRequestType);

        UpdateCardValidationNumOnToken cardValidationNumOnToken = new UpdateCardValidationNumOnToken();
        cardValidationNumOnToken.setReportGroup("Planets");
        cardValidationNumOnToken.setId("12345");
        cardValidationNumOnToken.setCustomerId("0987");
        cardValidationNumOnToken.setOrderId("12344");
        cardValidationNumOnToken.setLitleToken("1233456789103801");
        cardValidationNumOnToken.setCardValidationNum("123");
        batch.addTransaction(cardValidationNumOnToken);

        ForceCapture forceCapture = new ForceCapture();
        forceCapture.setReportGroup("Planets");
        forceCapture.setId("123456");
        forceCapture.setOrderId("12344");
        forceCapture.setAmount(106L);
        forceCapture.setOrderSource(OrderSourceType.ECOMMERCE);
        forceCapture.setCard(card);
        batch.addTransaction(forceCapture);

        Capture capture = new Capture();
        capture.setReportGroup("Planets");
        capture.setLitleTxnId(123456000L);
        capture.setAmount(106L);
        batch.addTransaction(capture);

        CaptureGivenAuth captureGivenAuth = new CaptureGivenAuth();
        captureGivenAuth.setReportGroup("Planets");
        captureGivenAuth.setOrderId("12344");
        captureGivenAuth.setAmount(106L);
        AuthInformation authInformation = new AuthInformation();
        authInformation.setAuthDate(Calendar.getInstance());
        authInformation.setAuthAmount(12345L);
        authInformation.setAuthCode("543216");
        captureGivenAuth.setAuthInformation(authInformation);
        captureGivenAuth.setOrderSource(OrderSourceType.ECOMMERCE);
        captureGivenAuth.setCard(card);
        batch.addTransaction(captureGivenAuth);

        EcheckVerification echeckVerification = new EcheckVerification();
        echeckVerification.setReportGroup("Planets");
        echeckVerification.setAmount(123456L);
        echeckVerification.setOrderId("12345");
        echeckVerification.setOrderSource(OrderSourceType.ECOMMERCE);
        echeckVerification.setBillToAddress(contact);
        echeckVerification.setEcheck(echeck);
        batch.addTransaction(echeckVerification);

        EcheckCredit echeckCredit = new EcheckCredit();
        echeckCredit.setReportGroup("Planets");
        echeckCredit.setLitleTxnId(1234567890L);
        echeckCredit.setAmount(12L);
        batch.addTransaction(echeckCredit);

        EcheckRedeposit echeckRedeposit = new EcheckRedeposit();
        echeckRedeposit.setReportGroup("Planets");
        echeckRedeposit.setLitleTxnId(124321341412L);
        batch.addTransaction(echeckRedeposit);

        EcheckSale echeckSale = new EcheckSale();
        echeckSale.setReportGroup("Planets");
        echeckSale.setAmount(123456L);
        echeckSale.setOrderId("12345");
        echeckSale.setOrderSource(OrderSourceType.ECOMMERCE);
        echeckSale.setBillToAddress(contact);
        echeckSale.setEcheck(echeck);
        echeckSale.setVerify(true);
        batch.addTransaction(echeckSale);

        int transactionCount = batch.getNumberOfTransactions();

        LitleBatchFileResponse fileResponse = request.sendToLitle();
        LitleBatchResponse batchResponse = fileResponse.getNextLitleBatchResponse();
        int txns = 0;

        ResponseValidatorProcessor processor = new ResponseValidatorProcessor();

        while (batchResponse.processNextTransaction(processor)) {
            txns++;
        }

        assertEquals(transactionCount, txns);
        assertEquals(transactionCount, processor.responseCount);

	}

	   @Test
	    public void testGiftCardTransactions(){
	        String requestFileName = "litleSdk-testBatchFile-RECURRING.xml";
	        LitleBatchFileRequest request = new LitleBatchFileRequest(requestFileName);

	        Properties configFromFile = request.getConfig();

	        // pre-assert the config file has required param values
	        assertEquals("prelive.litle.com", configFromFile.getProperty("batchHost"));
	        //assertEquals("15000", configFromFile.getProperty("batchPort"));

	        LitleBatchRequest batch = request.createBatch(merchantId);

	        CardType giftCard = new CardType();
	        giftCard.setType(MethodOfPaymentTypeEnum.GC);
	        giftCard.setExpDate("1218");
	        giftCard.setNumber("4100000000000001");

	        Activate activate = new Activate();
	        activate.setReportGroup("Planets");
	        activate.setOrderSource(OrderSourceType.ECOMMERCE);
	        activate.setAmount(100L);
	        activate.setOrderId("abc");
	        activate.setCard(giftCard);
	        batch.addTransaction(activate);

	        Deactivate deactivate = new Deactivate();
	        deactivate.setReportGroup("Planets");
	        deactivate.setOrderId("def");
	        deactivate.setOrderSource(OrderSourceType.ECOMMERCE);
	        deactivate.setCard(giftCard);
	        batch.addTransaction(deactivate);

	        Load load = new Load();
	        load.setReportGroup("Planets");
	        load.setOrderId("ghi");
	        load.setAmount(100L);
	        load.setOrderSource(OrderSourceType.ECOMMERCE);
	        load.setCard(giftCard);
	        batch.addTransaction(load);

	        Unload unload = new Unload();
	        unload.setReportGroup("Planets");
	        unload.setOrderId("jkl");
	        unload.setAmount(100L);
	        unload.setOrderSource(OrderSourceType.ECOMMERCE);
	        unload.setCard(giftCard);
	        batch.addTransaction(unload);

	        BalanceInquiry balanceInquiry = new BalanceInquiry();
	        balanceInquiry.setReportGroup("Planets");
	        balanceInquiry.setOrderId("mno");
	        balanceInquiry.setOrderSource(OrderSourceType.ECOMMERCE);
	        balanceInquiry.setCard(giftCard);
	        batch.addTransaction(balanceInquiry);

	        LitleBatchFileResponse fileResponse = request.sendToLitle();
	        LitleBatchResponse batchResponse = fileResponse.getNextLitleBatchResponse();
	        int txns = 0;
	        // iterate over all transactions in the file with a custom response
	        // processor
	        while (batchResponse.processNextTransaction(new LitleResponseProcessor() {
	            public void processAuthorizationResponse(AuthorizationResponse authorizationResponse) {
	                assertNotNull(authorizationResponse.getLitleTxnId());
	            }
	            public void processCaptureResponse(CaptureResponse captureResponse) {
	                assertNotNull(captureResponse.getLitleTxnId());
	            }
	            public void processForceCaptureResponse(ForceCaptureResponse forceCaptureResponse) {
	                assertNotNull(forceCaptureResponse.getLitleTxnId());
	            }
	            public void processCaptureGivenAuthResponse(CaptureGivenAuthResponse captureGivenAuthResponse) {
	                assertNotNull(captureGivenAuthResponse.getLitleTxnId());
	            }
	            public void processSaleResponse(SaleResponse saleResponse) {
	                assertNotNull(saleResponse.getLitleTxnId());
	            }
	            public void processCreditResponse(CreditResponse creditResponse) {
	                assertNotNull(creditResponse.getLitleTxnId());
	            }
	            public void processEcheckSalesResponse(EcheckSalesResponse echeckSalesResponse) {
	                assertNotNull(echeckSalesResponse.getLitleTxnId());
	            }
	            public void processEcheckCreditResponse(EcheckCreditResponse echeckCreditResponse) {
	                assertNotNull(echeckCreditResponse.getLitleTxnId());
	            }
	            public void processEcheckVerificationResponse(EcheckVerificationResponse echeckVerificationResponse) {
	                assertNotNull(echeckVerificationResponse.getLitleTxnId());
	            }
	            public void processEcheckRedepositResponse(EcheckRedepositResponse echeckRedepositResponse) {
	                assertNotNull(echeckRedepositResponse.getLitleTxnId());
	            }
	            public void processAuthReversalResponse(AuthReversalResponse authReversalResponse) {
	                assertNotNull(authReversalResponse.getLitleTxnId());
	            }
	            public void processRegisterTokenResponse(RegisterTokenResponse registerTokenResponse) {
	                assertNotNull(registerTokenResponse.getLitleTxnId());
	            }
	            public void processUpdateSubscriptionResponse(UpdateSubscriptionResponse updateSubscriptionResponse) {
	                assertNotNull(updateSubscriptionResponse.getLitleTxnId());
	            }
	            public void processCancelSubscriptionResponse(CancelSubscriptionResponse cancelSubscriptionResponse) {
	                assertNotNull(cancelSubscriptionResponse.getLitleTxnId());
	            }
	            public void processUpdateCardValidationNumOnTokenResponse(UpdateCardValidationNumOnTokenResponse updateCardValidationNumOnTokenResponse) {
	                assertNotNull(updateCardValidationNumOnTokenResponse.getLitleTxnId());
	            }
	            public void processAccountUpdate(AccountUpdateResponse accountUpdateResponse) {
	                assertNotNull(accountUpdateResponse.getLitleTxnId());
	            }
	            public void processCreatePlanResponse(CreatePlanResponse createPlanResponse) {
	                assertNotNull(createPlanResponse.getLitleTxnId());
	            }
	            public void processUpdatePlanResponse(UpdatePlanResponse updatePlanResponse) {
	                assertNotNull(updatePlanResponse.getLitleTxnId());
	            }
	            public void processActivateResponse(ActivateResponse activateResponse) {
	                assertNotNull(activateResponse.getLitleTxnId());
	            }
	            public void processDeactivateResponse(DeactivateResponse deactivateResponse) {
	                assertNotNull(deactivateResponse.getLitleTxnId());
	            }
	            public void processLoadResponse(LoadResponse loadResponse) {
	                assertNotNull(loadResponse.getLitleTxnId());
	            }
	            public void processUnloadResponse(UnloadResponse unloadResponse) {
	                assertNotNull(unloadResponse.getLitleTxnId());
	            }
	            public void processBalanceInquiryResponse(BalanceInquiryResponse balanceInquiryResponse) {
	                assertNotNull(balanceInquiryResponse.getLitleTxnId());
	            }

	        })) {
	            txns++;
	        }

	        assertEquals(5, txns);

	    }

	   @Test
	    public void testMechaBatchAndProcess_RecurringDemonstratesUseOfProcessorAdapter(){
	        String requestFileName = "litleSdk-testBatchFile-RECURRING.xml";
	        LitleBatchFileRequest request = new LitleBatchFileRequest(requestFileName);

	        Properties configFromFile = request.getConfig();

	        // pre-assert the config file has required param values
	        assertEquals("prelive.litle.com", configFromFile.getProperty("batchHost"));
	        //assertEquals("15000", configFromFile.getProperty("batchPort"));

	        LitleBatchRequest batch = request.createBatch(merchantId);
	        CancelSubscription cancelSubscription = new CancelSubscription();
	        cancelSubscription.setSubscriptionId(12345L);
	        batch.addTransaction(cancelSubscription);

	        UpdateSubscription updateSubscription = new UpdateSubscription();
	        updateSubscription.setSubscriptionId(12345L);
	        batch.addTransaction(updateSubscription);

	        CreatePlan createPlan = new CreatePlan();
	        createPlan.setPlanCode("abc");
	        createPlan.setName("name");
	        createPlan.setIntervalType(IntervalTypeEnum.ANNUAL);
	        createPlan.setAmount(100L);
	        batch.addTransaction(createPlan);

	        UpdatePlan updatePlan = new UpdatePlan();
	        updatePlan.setPlanCode("def");
	        updatePlan.setActive(true);
	        batch.addTransaction(updatePlan);

	        LitleBatchFileResponse fileResponse = request.sendToLitle();
	        LitleBatchResponse batchResponse = fileResponse.getNextLitleBatchResponse();
	        int txns = 0;
	        // iterate over all transactions in the file with a custom response
	        // processor
	        while (batchResponse.processNextTransaction(new LitleResponseProcessorAdapter() {
	            @Override
	            public void processUpdateSubscriptionResponse(UpdateSubscriptionResponse updateSubscriptionResponse) {
	                assertEquals(12345L, updateSubscriptionResponse.getSubscriptionId());
	            }
	            @Override
	            public void processCancelSubscriptionResponse(CancelSubscriptionResponse cancelSubscriptionResponse) {
	                assertEquals(12345L, cancelSubscriptionResponse.getSubscriptionId());
	            }
	            @Override
	            public void processCreatePlanResponse(CreatePlanResponse createPlanResponse) {
	                assertEquals("abc",createPlanResponse.getPlanCode());
	            }
	            @Override
	            public void processUpdatePlanResponse(UpdatePlanResponse updatePlanResponse) {
	                assertEquals("def", updatePlanResponse.getPlanCode());
	            }
	        })) {
	            txns++;
	        }

	        assertEquals(4, txns);

	    }


	@Test
    public void testBatch_AU(){
        String requestFileName = "litleSdk-testBatchFile_AU.xml";
        LitleBatchFileRequest request = new LitleBatchFileRequest(requestFileName);

        Properties configFromFile = request.getConfig();

        // pre-assert the config file has required param values
        assertEquals("prelive.litle.com", configFromFile.getProperty("batchHost"));
        //assertEquals("15000", configFromFile.getProperty("batchPort"));

        LitleBatchRequest batch = request.createBatch(merchantId);

        //card
        CardType card = new CardType();
        card.setNumber("4100000000000001");
        card.setExpDate("1210");
        card.setType(MethodOfPaymentTypeEnum.VI);


        ObjectFactory objectFactory = new ObjectFactory();
       AccountUpdate accountUpdate = new AccountUpdate();
       accountUpdate.setReportGroup("Planets");
       accountUpdate.setId("12345");
       accountUpdate.setCustomerId("0987");
       accountUpdate.setOrderId("1234");
       accountUpdate.setCardOrToken(objectFactory.createCard(card));

       batch.addTransaction(accountUpdate);

        LitleBatchFileResponse fileResponse = request.sendToLitle();
        LitleBatchResponse batchResponse = fileResponse.getNextLitleBatchResponse();
        int txns = 0;
        // iterate over all transactions in the file with a custom response
        // processor
        while (batchResponse.processNextTransaction(new LitleResponseProcessor() {
            public void processAuthorizationResponse(AuthorizationResponse authorizationResponse) {

            }

            public void processCaptureResponse(CaptureResponse captureResponse) {
            }

            public void processForceCaptureResponse(ForceCaptureResponse forceCaptureResponse) {
            }

            public void processCaptureGivenAuthResponse(CaptureGivenAuthResponse captureGivenAuthResponse) {
            }

            public void processSaleResponse(SaleResponse saleResponse) {
            }

            public void processCreditResponse(CreditResponse creditResponse) {
            }

            public void processEcheckSalesResponse(EcheckSalesResponse echeckSalesResponse) {
            }

            public void processEcheckCreditResponse(EcheckCreditResponse echeckCreditResponse) {
            }

            public void processEcheckVerificationResponse(EcheckVerificationResponse echeckVerificationResponse) {
            }

            public void processEcheckRedepositResponse(EcheckRedepositResponse echeckRedepositResponse) {
            }

            public void processAuthReversalResponse(AuthReversalResponse authReversalResponse) {
            }

            public void processRegisterTokenResponse(RegisterTokenResponse registerTokenResponse) {
            }
            public void processAccountUpdate(AccountUpdateResponse accountUpdateResponse) {
                assertEquals("Planets", accountUpdateResponse.getReportGroup());
                assertEquals("12345", accountUpdateResponse.getId());
                assertEquals("0987", accountUpdateResponse.getCustomerId());
                assertEquals("1234", accountUpdateResponse.getOrderId());
            }

            public void processUpdateSubscriptionResponse(UpdateSubscriptionResponse updateSubscriptionResponse) {
            }

            public void processCancelSubscriptionResponse(CancelSubscriptionResponse cancelSubscriptionResponse) {
            }

            public void processUpdateCardValidationNumOnTokenResponse(
                    UpdateCardValidationNumOnTokenResponse updateCardValidationNumOnTokenResponse) {
            }

            public void processCreatePlanResponse(CreatePlanResponse createPlanResponse) {
            }

            public void processUpdatePlanResponse(UpdatePlanResponse updatePlanResponse) {
            }

            public void processActivateResponse(ActivateResponse activateResponse) {
            }

            public void processDeactivateResponse(DeactivateResponse deactivateResponse) {
            }

            public void processLoadResponse(LoadResponse loadResponse) {
            }

            public void processUnloadResponse(UnloadResponse unloadResponse) {
            }

            public void processBalanceInquiryResponse(BalanceInquiryResponse balanceInquiryResponse) {
            }
        })) {
            txns++;
        }

        assertEquals(1, txns);

    }
	private void assertJavaApi(LitleBatchFileRequest request, LitleBatchFileResponse response) {
		assertNotNull(response);
		assertNotNull(response.getLitleSessionId());
		assertEquals("0", response.getResponse());
		assertEquals("Valid Format", response.getMessage());
		assertEquals(Versions.XML_VERSION, response.getVersion());

		LitleBatchResponse batchResponse1 = response.getNextLitleBatchResponse();
		assertNotNull(batchResponse1);
		assertNotNull(batchResponse1.getLitleBatchId());
		assertEquals(merchantId, batchResponse1.getMerchantId());

		LitleTransactionInterface txnResponse = batchResponse1.getNextTransaction();
		SaleResponse saleResponse11 = (SaleResponse) txnResponse;
		assertEquals("000", saleResponse11.getResponse());
		assertEquals("Approved", saleResponse11.getMessage());
		assertNotNull(saleResponse11.getLitleTxnId());
		assertEquals("orderId11", saleResponse11.getOrderId());
		assertEquals("reportGroup11", saleResponse11.getReportGroup());

//		assertNull("expected no more than one transaction in batchResponse1", batchResponse1.getNextTransaction());
//
//		assertNull("expected no more than one batch in file", response.getNextLitleBatchResponse());
	}

	private void assertGeneratedFiles(String workingDirRequests, String workingDirResponses, String requestFileName,
		LitleBatchFileRequest request, LitleBatchFileResponse response) throws Exception {
		File fRequest = request.getFile();
		assertEquals(workingDirRequests + File.separator + requestFileName, fRequest.getAbsolutePath());
		assertTrue(fRequest.exists());
		assertTrue(fRequest.length() > 0);

		File fResponse = response.getFile();
		assertEquals(workingDirResponses + File.separator + requestFileName, fResponse.getAbsolutePath());
		assertTrue(fResponse.exists());
		assertTrue(fResponse.length() > 0);

		// assert contents of the response file by reading it through the Java API again
		LitleBatchFileResponse responseFromFile = new LitleBatchFileResponse(fResponse);
		assertJavaApi(request, responseFromFile);
	}

	private void prepDir(String dirName) {
		File fRequestDir = new File(dirName);
		fRequestDir.mkdirs();
	}

	private void copyFile(File source, File destination) throws IOException {
	    FileInputStream sourceIn = null;
	    FileOutputStream destOut = null;
	    try {
	        sourceIn = new FileInputStream(source);
	        destOut = new FileOutputStream(destination);

	        byte [] buffer = new byte[2048];
	        int bytesRead = -1;
	        while ( (bytesRead = sourceIn.read(buffer)) != -1 ) {
	            destOut.write(buffer, 0, bytesRead);
	        }
	    } finally {
	        if (sourceIn != null) {
	            sourceIn.close();
	        }
	        if (destOut != null) {
	            destOut.close();
	        }
	    }
	}

	class ResponseValidatorProcessor implements LitleResponseProcessor {
	    int responseCount = 0;

        public void processAuthorizationResponse(AuthorizationResponse authorizationResponse) {
            assertNotNull(authorizationResponse.getLitleTxnId());
            responseCount++;
        }
        public void processCaptureResponse(CaptureResponse captureResponse) {
            assertNotNull(captureResponse.getLitleTxnId());
            responseCount++;
        }
        public void processForceCaptureResponse(ForceCaptureResponse forceCaptureResponse) {
            assertNotNull(forceCaptureResponse.getLitleTxnId());
            responseCount++;
        }
        public void processCaptureGivenAuthResponse(CaptureGivenAuthResponse captureGivenAuthResponse) {
            assertNotNull(captureGivenAuthResponse.getLitleTxnId());
            responseCount++;
        }
        public void processSaleResponse(SaleResponse saleResponse) {
            assertNotNull(saleResponse.getLitleTxnId());
            responseCount++;
        }
        public void processCreditResponse(CreditResponse creditResponse) {
            assertNotNull(creditResponse.getLitleTxnId());
            responseCount++;
        }
        public void processEcheckSalesResponse(EcheckSalesResponse echeckSalesResponse) {
            assertNotNull(echeckSalesResponse.getLitleTxnId());
            responseCount++;
        }
        public void processEcheckCreditResponse(EcheckCreditResponse echeckCreditResponse) {
            assertNotNull(echeckCreditResponse.getLitleTxnId());
            responseCount++;
        }
        public void processEcheckVerificationResponse(EcheckVerificationResponse echeckVerificationResponse) {
            assertNotNull(echeckVerificationResponse.getLitleTxnId());
            responseCount++;
        }
        public void processEcheckRedepositResponse(EcheckRedepositResponse echeckRedepositResponse) {
            assertNotNull(echeckRedepositResponse.getLitleTxnId());
            responseCount++;
        }
        public void processAuthReversalResponse(AuthReversalResponse authReversalResponse) {
            assertNotNull(authReversalResponse.getLitleTxnId());
            responseCount++;
        }
        public void processRegisterTokenResponse(RegisterTokenResponse registerTokenResponse) {
            assertNotNull(registerTokenResponse.getLitleTxnId());
            responseCount++;
        }
        public void processUpdateSubscriptionResponse(UpdateSubscriptionResponse updateSubscriptionResponse) {
            assertNotNull(updateSubscriptionResponse.getLitleTxnId());
            responseCount++;
        }
        public void processCancelSubscriptionResponse(CancelSubscriptionResponse cancelSubscriptionResponse) {
            assertNotNull(cancelSubscriptionResponse.getLitleTxnId());
            responseCount++;
        }
        public void processUpdateCardValidationNumOnTokenResponse(UpdateCardValidationNumOnTokenResponse updateCardValidationNumOnTokenResponse) {
            assertNotNull(updateCardValidationNumOnTokenResponse.getLitleTxnId());
            responseCount++;
        }
        public void processAccountUpdate(AccountUpdateResponse accountUpdateResponse) {
            assertNotNull(accountUpdateResponse.getLitleTxnId());
            responseCount++;
        }
        public void processCreatePlanResponse(CreatePlanResponse createPlanResponse) {
            assertNotNull(createPlanResponse.getLitleTxnId());
            responseCount++;
        }
        public void processUpdatePlanResponse(UpdatePlanResponse updatePlanResponse) {
            assertNotNull(updatePlanResponse.getLitleTxnId());
            responseCount++;
        }
        public void processActivateResponse(ActivateResponse activateResponse) {
            assertNotNull(activateResponse.getLitleTxnId());
            responseCount++;
        }
        public void processDeactivateResponse(DeactivateResponse deactivateResponse) {
            assertNotNull(deactivateResponse.getLitleTxnId());
            responseCount++;
        }
        public void processLoadResponse(LoadResponse loadResponse) {
            assertNotNull(loadResponse.getLitleTxnId());
            responseCount++;
        }
        public void processUnloadResponse(UnloadResponse unloadResponse) {
            assertNotNull(unloadResponse.getLitleTxnId());
            responseCount++;
        }
        public void processBalanceInquiryResponse(BalanceInquiryResponse balanceInquiryResponse) {
            assertNotNull(balanceInquiryResponse.getLitleTxnId());
            responseCount++;
        }
    }

	public static void main(String[] args) throws Exception {
		TestBatchFile t = new TestBatchFile();

		t.testSendToLitle_WithFileConfig();
		t.testSendToLitle_WithConfigOverrides();
	}
}
