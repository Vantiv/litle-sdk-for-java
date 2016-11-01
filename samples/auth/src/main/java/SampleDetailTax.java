package com.litle.sdk.samples;
import com.litle.sdk.*;
import com.litle.sdk.generate.*;

public class SampleDetailTax {
    public static void main(String[] args) {
	  Authorization authorization = new Authorization();
	  authorization.setId("12345");
	  authorization.setReportGroup("Default");
	  authorization.setOrderId("67890");
	  authorization.setAmount(10000L);
	  authorization.setOrderSource(OrderSourceType.ECOMMERCE);
	  EnhancedData enhanced = new EnhancedData();
	  DetailTax dt1 = new DetailTax();
	  dt1.setTaxAmount(100L);
	  enhanced.getDetailTaxes().add(dt1);
	  DetailTax dt2 = new DetailTax();
	  dt2.setTaxAmount(200L);
	  enhanced.getDetailTaxes().add(dt2);
	  authorization.setEnhancedData(enhanced);
	  CardType card = new CardType();
	  card.setNumber("4100000000000000");
	  card.setExpDate("1215");
	  card.setType(MethodOfPaymentTypeEnum.VI);
	  authorization.setCard(card);
	 
	  AuthorizationResponse response = new LitleOnline().authorize(authorization);
	  System.out.println("Response: " + response.getMessage());
	  // In your sample, you can ignore this 	
          if(!response.getMessage().equals("Transaction Received"))
          throw new RuntimeException("The SampleDetailTax does not give the right response");
	
}
}
