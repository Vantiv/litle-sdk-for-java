package com.litle.sdk;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Calendar;
import java.util.Properties;

import javax.xml.bind.JAXBException;

import org.junit.Test;

import com.litle.sdk.generate.AccountUpdate;
import com.litle.sdk.generate.AccountUpdateResponse;
import com.litle.sdk.generate.AuthInformation;
import com.litle.sdk.generate.AuthReversal;
import com.litle.sdk.generate.AuthReversalResponse;
import com.litle.sdk.generate.Authorization;
import com.litle.sdk.generate.AuthorizationResponse;
import com.litle.sdk.generate.Capture;
import com.litle.sdk.generate.CaptureGivenAuth;
import com.litle.sdk.generate.CaptureGivenAuthResponse;
import com.litle.sdk.generate.CaptureResponse;
import com.litle.sdk.generate.CardType;
import com.litle.sdk.generate.Contact;
import com.litle.sdk.generate.Credit;
import com.litle.sdk.generate.CreditResponse;
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
import com.litle.sdk.generate.MethodOfPaymentTypeEnum;
import com.litle.sdk.generate.ObjectFactory;
import com.litle.sdk.generate.OrderSourceType;
import com.litle.sdk.generate.RegisterTokenRequestType;
import com.litle.sdk.generate.RegisterTokenResponse;
import com.litle.sdk.generate.Sale;
import com.litle.sdk.generate.SaleResponse;
import com.litle.sdk.generate.TransactionType;
import com.litle.sdk.generate.UpdateCardValidationNumOnToken;

public class TestBatchFile {

    String merchantId = "07103229";

    @Test
	public void testSendToLitle_WithFileConfig() throws Exception {
		String requestFileName = "litleSdk-testBatchFile-fileConfig.xml";
		LitleBatchFileRequest request = new LitleBatchFileRequest(requestFileName);

		Properties configFromFile = request.getConfig();

		// pre-assert the config file has required param values
		assertEquals("cert.litle.com", configFromFile.getProperty("batchHost"));
		assertEquals("15000", configFromFile.getProperty("batchPort"));

		String workingDirRequests = configFromFile.getProperty("batchRequestFolder");
		prepDir(workingDirRequests);

		String workingDirResponses = configFromFile.getProperty("batchResponseFolder");
		prepDir(workingDirResponses);

		prepareTestRequest(request);

		// request file is not generated before calling sendToLitle();
		assertNull(request.getFile());

		/* call method under test */
		LitleBatchFileResponse response = request.sendToLitle();

		// assert response can be processed through Java API
		assertJavaApi(request, response);

		// assert request and response files were created properly
		assertGeneratedFiles(workingDirRequests, workingDirResponses, requestFileName, request, response);
	}

	@Test
	public void testSendToLitle_WithConfigOverrides() throws Exception {
		String workingDir = System.getProperty("java.io.tmpdir");

		String workingDirRequests = workingDir + File.separator + "litleSdkTestBatchRequests";
		prepDir(workingDirRequests);

		String workingDirResponses = workingDir + File.separator + "litleSdkTestBatchResponses";
		prepDir(workingDirResponses);

		Properties configOverrides = new Properties();
		configOverrides.setProperty("batchHost", "cert.litle.com");  // TODO - point this to the www.testlitle.com sandbox instead of cert
		configOverrides.setProperty("batchPort", "15000");


		configOverrides.setProperty("batchRequestFolder", workingDirRequests);
		configOverrides.setProperty("batchResponseFolder", workingDirResponses);

		String requestFileName = "litleSdk-testBatchFile-configOverrides.xml";
		LitleBatchFileRequest request = new LitleBatchFileRequest(requestFileName, configOverrides);

		prepareTestRequest(request);

		// request file is not generated before calling sendToLitle();
		assertNull(request.getFile());

		//actually add a transaction


		/* call method under test */
		LitleBatchFileResponse response = request.sendToLitle();

		// assert response can be processed through Java API
		assertJavaApi(request, response);

		// assert request and response files were created properly
		assertGeneratedFiles(workingDirRequests, workingDirResponses, requestFileName, request, response);
	}
	@Test
    public void testSendToLitleSFTP_WithFileConfig() throws Exception {
        String requestFileName = "litleSdk-testBatchFile-fileConfigSFTP.xml";
        LitleBatchFileRequest request = new LitleBatchFileRequest(requestFileName);

        Properties configFromFile = request.getConfig();

        // pre-assert the config file has required param values
        assertEquals("cert.litle.com", configFromFile.getProperty("batchHost"));
        assertEquals("15000", configFromFile.getProperty("batchPort"));

        String workingDirRequests = configFromFile.getProperty("batchRequestFolder");
        prepDir(workingDirRequests);

        String workingDirResponses = configFromFile.getProperty("batchResponseFolder");
        prepDir(workingDirResponses);

        prepareTestRequest(request);

        // request file is not generated before calling sendToLitle();
        assertNull(request.getFile());

        /* call method under test */
        LitleBatchFileResponse response = request.sendToLitleSFTP();

        // assert response can be processed through Java API
        assertJavaApi(request, response);

        // assert request and response files were created properly
        assertGeneratedFiles(workingDirRequests, workingDirResponses, requestFileName, request, response);
    }

    @Test
    public void testSendToLitleSFTP_WithConfigOverrides() throws Exception {
        String workingDir = System.getProperty("java.io.tmpdir");

        String workingDirRequests = workingDir + File.separator + "litleSdkTestBatchRequests";
        prepDir(workingDirRequests);

        String workingDirResponses = workingDir + File.separator + "litleSdkTestBatchResponses";
        prepDir(workingDirResponses);

        Properties configOverrides = new Properties();
        configOverrides.setProperty("batchHost", "cert.litle.com");  // TODO - point this to the www.testlitle.com sandbox instead of cert
        configOverrides.setProperty("sftpTimeout", "720000");

        configOverrides.setProperty("batchRequestFolder", workingDirRequests);
        configOverrides.setProperty("batchResponseFolder", workingDirResponses);

        String requestFileName = "litleSdk-testBatchFile-configOverridesSFTP.xml";
        LitleBatchFileRequest request = new LitleBatchFileRequest(requestFileName, configOverrides);

        prepareTestRequest(request);

        // request file is not generated before calling sendToLitle();
        assertNull(request.getFile());

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
        assertEquals("cert.litle.com", configFromFile.getProperty("batchHost"));
        assertEquals("15000", configFromFile.getProperty("batchPort"));

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

        LitleBatchFileResponse fileResponse = request.sendToLitleSFTP();
        LitleBatchResponse batchResponse = fileResponse.getNextLitleBatchResponse();
        int txns = 1;
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
                // TODO Auto-generated method stub

            }
        })) {
            txns++;
        }

        assertEquals(12, txns);

	}

	@Test
    public void testBatch_AU(){
        String requestFileName = "litleSdk-testBatchFile_AU.xml";
        LitleBatchFileRequest request = new LitleBatchFileRequest(requestFileName);

        Properties configFromFile = request.getConfig();

        // pre-assert the config file has required param values
        assertEquals("cert.litle.com", configFromFile.getProperty("batchHost"));
        assertEquals("15000", configFromFile.getProperty("batchPort"));

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

        LitleBatchFileResponse fileResponse = request.sendToLitleSFTP();
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
		assertEquals(request.getConfig().getProperty("version"), response.getVersion());

		LitleBatchResponse batchResponse1 = response.getNextLitleBatchResponse();
		assertNotNull(batchResponse1);
		assertNotNull(batchResponse1.getLitleBatchId());
		assertEquals(merchantId, batchResponse1.getMerchantId());

		TransactionType txnResponse = batchResponse1.getNextTransaction();
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

	public static void main(String[] args) throws Exception {
		TestBatchFile t = new TestBatchFile();

		t.testSendToLitle_WithFileConfig();
		t.testSendToLitle_WithConfigOverrides();
	}
}
