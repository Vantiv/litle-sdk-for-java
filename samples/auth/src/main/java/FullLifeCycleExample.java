package com.litle.sdk.samples;
import com.litle.sdk.*;
import com.litle.sdk.generate.*;
 
//Full Lifecycle
public class FullLifeCycleExample {
    public static void main(String[] args) {
        LitleOnline litle = new LitleOnline();
 
        Authorization auth = new Authorization();
        auth.setOrderId("1");
        auth.setAmount(10010L);
        auth.setOrderSource(OrderSourceType.ECOMMERCE);
        Contact billToAddress = new Contact();
        billToAddress.setName("John Smith");
        billToAddress.setAddressLine1("1 Main St.");
        billToAddress.setCity("Burlington");
        billToAddress.setState("MA");
        billToAddress.setCountry(CountryTypeEnum.US);
        billToAddress.setZip("01803-3747");
        auth.setBillToAddress(billToAddress);
        CardType card = new CardType();
        card.setNumber("375001010000003");
        card.setExpDate("0112");
        card.setCardValidationNum("349");
        card.setType(MethodOfPaymentTypeEnum.AX);
        auth.setCard(card);
 
        AuthorizationResponse authResponse = litle.authorize(auth);
        System.out.println("Response: " + authResponse.getResponse());
        System.out.println("Message: " + authResponse.getMessage());
        System.out.println("Litle Transaction ID: " + authResponse.getLitleTxnId());
 
        Capture capture = new Capture();
        capture.setLitleTxnId(authResponse.getLitleTxnId());
        CaptureResponse captureResponse = litle.capture(capture);  //Capture the Auth
        System.out.println("Response: " + captureResponse.getResponse());
        System.out.println("Message: " + captureResponse.getMessage());
        System.out.println("Litle Transaction ID: " + captureResponse.getLitleTxnId());
 
        Credit credit = new Credit();
        credit.setLitleTxnId(captureResponse.getLitleTxnId());     
        CreditResponse creditResponse = litle.credit(credit); //Refund the capture
        System.out.println("Response: " + creditResponse.getResponse());
        System.out.println("Message: " + creditResponse.getMessage());
        System.out.println("Litle Transaction ID: " + creditResponse.getLitleTxnId());
	// In your sample, you can ignore this 	
	if(!creditResponse.getMessage().equals("Approved")||!captureResponse.getMessage().equals("Approved")||!authResponse.getMessage().equals("Approved"))
        throw new RuntimeException(" The FullLifeCycleExample does not give the right response");
 
        //TODO - Fix the void here
       /* Void credit = new Credit();
        capture.setLitleTxnId(captureResponse.getLitleTxnId());
        CreditResponse creditResponse = litle.credit(credit); //Refund the capture
        System.out.println("Response: " + creditResponse.getResponse());
        System.out.println("Message: " + creditResponse.getMessage());
        System.out.println("Litle Transaction ID: " + creditResponse.getLitleTxnId());*/
 
    }
}
