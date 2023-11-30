package io.github.vantiv.sdk.samples;
import io.github.vantiv.sdk.*;
import io.github.vantiv.sdk.generate.*;

//AVS Only
public class AvsOnlyExample {
    public static void main(String[] args) {
        Authorization auth = new Authorization();
        auth.setOrderId("1");
        auth.setAmount(0L);
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
 
        AuthorizationResponse response = new LitleOnline().authorize(auth);
        FraudResult fraudresult = new FraudResult();
	fraudresult.setAvsResult("12");
        response.setFraudResult(fraudresult);
        //Display Results
        System.out.println("Response: " + response.getResponse());
        System.out.println("Message: " + response.getMessage());
        System.out.println("Litle Transaction ID: " + response.getLitleTxnId());
        
        System.out.println("AVS Result: " + response.getFraudResult().getAvsResult());
	if(!response.getMessage().equals("Approved"))
        throw new RuntimeException(" The AvsOnlyExample does not give the right response");
    }
}
