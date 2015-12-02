package com.litle.sdk.samples;
import com.litle.sdk.*;
import com.litle.sdk.generate.*;
 
/*
 * This example assumes the following prerequisite set up in Litle's system.
 * MerchantId: 1 is configured for USD
 * MerchantId: 2 is configured for CDN
 */
public class MultiCurrencyMultiReportGroupSale {
    public static void main(String[] args) {    	
    	LitleOnline litle = new LitleOnline(); //Loads the configuration from .litle_SDK_config.properties.  This will get your username, password, and connectivity settings
    	
    	//Process sale (combined auth/capture) for the Mens Health product line 
    	Sale sale = new Sale();
    	sale.setOrderId("1");
    	sale.setOrderSource(OrderSourceType.TELEPHONE);
    	sale.setReportGroup("Men's Health"); //Probably set up in Litle's system to rollup into Magazines
    	CardType card = new CardType();
    	card.setNumber("375001010000003");
    	card.setExpDate("0112");
    	card.setCardValidationNum("349");
    	card.setType(MethodOfPaymentTypeEnum.AX);
    	sale.setCard(card);
    	sale.setId("id");
    	LitleOnlineRequest overrides = new LitleOnlineRequest();
    	Contact obj=new Contact();
        obj.setCountry(CountryTypeEnum.values()[0]);
        System.out.println(obj.getCountry());
    	if(obj.getCountry().name().equalsIgnoreCase("USA")) {
            sale.setAmount(5748L); 
            //1 is configured in Litle's system for USD, making 5748 above USD 57.48
    	    overrides.setMerchantId("1"); 
    	}
    	else if(obj.getCountry().name().equalsIgnoreCase("CA")) {
    	    sale.setAmount(5773L);
            //2 is configured in Litle's system for CDN, making 5773 above CDN 57.73 (if you want to charge CDN different from USD for exchange rates)
    	    overrides.setMerchantId("2"); 
    	}
        
        SaleResponse response = litle.sale(sale, overrides);
        //Display Results
        System.out.println("Response: " + response.getResponse());
        System.out.println("Message: " + response.getMessage());
        System.out.println("Litle Transaction ID: " + response.getLitleTxnId());
	if(!response.getMessage().equals("Approved"))
        throw new RuntimeException(" The MultiCurrencyMultiReportGroupSale does not give the right response");
    }
}

