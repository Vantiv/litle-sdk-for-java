package com.litle.sdk;

import com.litle.sdk.generate.CardType;
import com.litle.sdk.generate.MethodOfPaymentTypeEnum;
import com.litle.sdk.generate.OrderSourceType;
import com.litle.sdk.generate.Sale;

public class performanceTestMerchantSDKLite {
	private static LitleBatchFileRequest litleBatchFileRequest;

    static String merchantId = "07103229";

    public static void main(String[] args) throws Exception {
    	litleBatchFileRequest = new LitleBatchFileRequest("testFile.xml");
    	LitleBatchRequest litleBatchRequest = litleBatchFileRequest.createBatch(merchantId);
    	for (int i =0; i < 50 ; i++)
		{
    		Sale sale = new Sale();
    		sale.setAmount(106L);
    		sale.setOrderId("12344");
    		sale.setOrderSource(OrderSourceType.ECOMMERCE);
    		CardType card = new CardType();
    		card.setType(MethodOfPaymentTypeEnum.VI);
    		card.setNumber("4100000000000002");
    		card.setExpDate("1210");
    		sale.setCard(card);
    		sale.setId("id");

    		litleBatchRequest.addTransaction(sale);
		}

		litleBatchFileRequest.sendToLitle();
 		System.out.println("done");
 	}

}

