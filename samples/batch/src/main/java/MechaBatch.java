package com.litle.sdk.samples;
import com.litle.sdk.*;
import com.litle.sdk.generate.*;
import java.util.Calendar;
import java.util.Properties;
 
/*
An example of the batch functionality of Litle Java SDK. We create one of each transaction type, add them to a batch, and then deliver the batch over sFTP to Litle. Note the use of an anonymous class to process responses.
 */
public class MechaBatch {
    public static void main(String[] args) {    	
    	String requestFileName = "litleSdk-testBatchFile-MECHA.xml";
	LitleBatchFileRequest request = new LitleBatchFileRequest(requestFileName);
	String merchantId = "0180";
	Properties configFromFile = request.getConfig();
	 
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
        System.out.println("Response Message:"+fileResponse.getMessage());
	 if(!fileResponse.getMessage().equals("Valid Format"))
         throw new RuntimeException(" The MechaBatch does not give the right response");

	}	
}

