package com.litle.sdk.samples;
import com.litle.sdk.*;
import com.litle.sdk.generate.*;
 
//Authorization
public class AuthExample {
    
    public static void main(String[] args) {
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
        auth.setId("id");
        
        AuthorizationResponse response = new LitleOnline().authorize(auth);
        //Display Results
        System.out.println("Response: " + response.getResponse());
        System.out.println("Message: " + response.getMessage());
        System.out.println("Litle Transaction ID: " + response.getLitleTxnId());
        
        // In your sample, you can ignore this
        if(!response.getMessage().equals("Approved"))
         throw new RuntimeException(" The AuthSample does not give the right response");
        
    }
}
