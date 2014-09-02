package com.litle.sdk.samples;
import com.litle.sdk.*;
import com.litle.sdk.generate.*;
 
import java.util.Properties;
 
public class MultiCurrencyExample2 {
    public static void main(String[] args) {
        LitleOnline litle = new LitleOnline();
 
        Authorization authorization = new Authorization();
        authorization.setReportGroup("Planets");
        authorization.setOrderId("12344");
        authorization.setAmount(106L);
        authorization.setOrderSource(OrderSourceType.ECOMMERCE);
        CardType card = new CardType();
        card.setType(MethodOfPaymentTypeEnum.VI);
        card.setNumber("4100000000000002");
        card.setExpDate("1210");
        authorization.setCard(card);
        Contact obj=new Contact();
        obj.setCountry(CountryTypeEnum.values()[0]);
        LitleOnlineRequest overrides = new LitleOnlineRequest();
        if(obj.getCountry().name().equalsIgnoreCase("USA")) {
            overrides.setMerchantId("1001"); //configured in our system for USD
        } else if(obj.getCountry().name().equalsIgnoreCase("CA")) {
            overrides.setMerchantId("1002"); //configured in our system for CDN
        }
 
        AuthorizationResponse response = litle.authorize(authorization, overrides);
        //Display Results
        System.out.println("Response: " + response.getResponse());
        System.out.println("Message: " + response.getMessage());
        System.out.println("Litle Transaction ID: " + response.getLitleTxnId());
	if(!response.getMessage().equals("Approved"))
        throw new RuntimeException(" The MultiCurrencyExample does not give the right response");
    }
}
