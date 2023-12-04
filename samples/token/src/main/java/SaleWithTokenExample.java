package io.github.vantiv.sdk.samples;
import io.github.vantiv.sdk.*;
import io.github.vantiv.sdk.generate.*;

public class SaleWithTokenExample {
    public static void main(String[] args) {
        Sale sale = new Sale();
        sale.setOrderId("1");
        sale.setAmount(10010L);
        sale.setOrderSource(OrderSourceType.ECOMMERCE);
        CardTokenType token = new CardTokenType();
        token.setCardValidationNum("349");
        token.setExpDate("1214");
        token.setLitleToken("1111222233334000");
        token.setType(MethodOfPaymentTypeEnum.VI);
        sale.setToken(token);
        sale.setId("id");
 
        SaleResponse response = new LitleOnline().sale(sale);
        //Display Results 
        System.out.println("Response: " + response.getResponse());
        System.out.println("Message: " + response.getMessage());
        System.out.println("Litle Transaction ID: " + response.getLitleTxnId());
        if(!response.getMessage().equals("Transaction Received"))
        throw new RuntimeException(" The SaleWithTokenExample does not give the right response");
    }
}
