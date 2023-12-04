package io.github.vantiv.sdk;

import io.github.vantiv.sdk.generate.*;

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

    		litleBatchRequest.addTransaction(sale);
		}

		litleBatchFileRequest.sendToLitle();
 		System.out.println("done");
 	}

}

