package com.litle.sdk.samples;
import com.litle.sdk.*;
import com.litle.sdk.generate.*;
 
public class EcheckVerificationExample {
    public static void main(String[] args) {
        EcheckVerification echeckVerification = new EcheckVerification();
	echeckVerification.setAmount(123456L);
	echeckVerification.setOrderId("12345");
	echeckVerification.setOrderSource(OrderSourceType.ECOMMERCE);
	EcheckType echeck = new EcheckType();
	echeck.setAccType(EcheckAccountTypeEnum.CHECKING);
	echeck.setAccNum("12345657890");
	echeck.setRoutingNum("123456789");
	echeck.setCheckNum("123455");
	echeckVerification.setEcheck(echeck);
	Contact contact = new Contact();
	contact.setName("Bob");
	contact.setCity("lowell");
	contact.setState("MA");
	contact.setEmail("litle.com");
	echeckVerification.setBillToAddress(contact);
 
        EcheckVerificationResponse response = new LitleOnline().echeckVerification(echeckVerification);
        //Display Results
        System.out.println("Response: " + response.getResponse());
        System.out.println("Message: " + response.getMessage());
        System.out.println("Litle Transaction ID: " + response.getLitleTxnId());
	if(!response.getMessage().equals("Approved"))
        throw new RuntimeException(" The EcheckVerificationExample does not give the right response");
    }
}
