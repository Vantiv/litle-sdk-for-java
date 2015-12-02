package com.litle.sdk.samples;
import com.litle.sdk.*;
import com.litle.sdk.generate.*;
 
import java.util.Properties;
 
public class MultiCurrencyExample {
    public static void main(String[] args) {
        LitleOnline usdCurrency = new LitleOnline(); //This will use the default merchant setup in .litle_SDK_config.properties supporting purchases in USD
 
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
	authorization.setId("id");
 
        AuthorizationResponse response = usdCurrency.authorize(authorization);
        //Display Results
        System.out.println("Response: " + response.getResponse());
        System.out.println("Message: " + response.getMessage());
        System.out.println("Litle Transaction ID: " + response.getLitleTxnId());
 
        Properties cdnProps = new Properties();
        cdnProps.setProperty("merchantId","1002");
        cdnProps.setProperty("url","https://www.testlitle.com/sandbox/communicator/online");
        cdnProps.setProperty("username","username");
        cdnProps.setProperty("password","topsecret"); 
        cdnProps.setProperty("proxyHost","iwp1.lowell.litle.com");  
         cdnProps.setProperty("proxyPort","8080");      
        cdnProps.setProperty("version","8.10");
        cdnProps.setProperty("timeout","65");
        LitleOnline cdnCurrency = new LitleOnline(cdnProps); //Override the default merchant setup in .litle_SDK_config.properties to force purchase in CDN
 
        AuthorizationResponse response2 = cdnCurrency.authorize(authorization);  //Perform the same authorization using CDN instead of USD
        //Display Results
        System.out.println("Response: " + response2.getResponse());
        System.out.println("Message: " + response2.getMessage());
        System.out.println("Litle Transaction ID: " + response2.getLitleTxnId());
 
        Properties yenProps = new Properties();
        yenProps.setProperty("merchantId","1003"); //Notice that 1003 is a different merchant.  In our system, they could be setup for YEN purchases
        yenProps.setProperty("url","https://www.testlitle.com/sandbox/communicator/online");
        yenProps.setProperty("username","username");
        yenProps.setProperty("password","topsecret");    
        yenProps.setProperty("proxyHost","iwp1.lowell.litle.com");  
        yenProps.setProperty("proxyPort","8080");     
        yenProps.setProperty("version","8.10");
        yenProps.setProperty("timeout","65");
        LitleOnline yenCurrency = new LitleOnline(yenProps); //Override the default merchant setup in .litle_SDK_config.properties to force purchase in YEN
        
        AuthorizationResponse response3 = yenCurrency.authorize(authorization);  //Perform the same authorization using YEN instead of USD
        //Display Results
        System.out.println("Response: " + response3.getResponse());
        System.out.println("Message: " + response3.getMessage());
        System.out.println("Litle Transaction ID: " + response3.getLitleTxnId());
	if(!response.getMessage().equals("Approved")||!response2.getMessage().equals("Approved")||!response3.getMessage().equals("Approved"))
        throw new RuntimeException(" The MultiCurrencyExample does not give the right response");
 
    }
}

