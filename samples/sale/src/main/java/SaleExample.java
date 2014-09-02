package com.litle.sdk.samples;
import com.litle.sdk.*;
import com.litle.sdk.generate.*;
 
public class SaleExample {
    public static void main(String[] args) {
        Sale sale = new Sale();
        sale.setOrderId("1");
        sale.setAmount(10010L);
        sale.setOrderSource(OrderSourceType.ECOMMERCE);
        Contact billToAddress = new Contact();
        billToAddress.setName("John Smith");
        billToAddress.setAddressLine1("1 Main St.");
        billToAddress.setCity("Burlington");
        billToAddress.setState("MA");
        billToAddress.setCountry(CountryTypeEnum.US);
        billToAddress.setZip("01803-3747");
        sale.setBillToAddress(billToAddress);
        CardType card = new CardType();
        card.setNumber("375001010000003");
        card.setExpDate("0112");
        card.setCardValidationNum("349");
        card.setType(MethodOfPaymentTypeEnum.AX);
        sale.setCard(card);
 
        SaleResponse response = new LitleOnline().sale(sale);
        //Display Results
        System.out.println("Response: " + response.getResponse());
        System.out.println("Message: " + response.getMessage());
        System.out.println("Litle Transaction ID: " + response.getLitleTxnId());
        if(!response.getMessage().equals("Approved"))
        throw new RuntimeException(" The SaleExample does not give the right response");
    }
}
