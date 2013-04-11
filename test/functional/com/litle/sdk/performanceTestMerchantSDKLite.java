package com.litle.sdk;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Statement;

import com.litle.sdk.LitleBatchFileRequest;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.junit.BeforeClass;
import org.junit.Test;

import com.litle.sdk.generate.Authorization;
import com.litle.sdk.generate.Capture;
import com.litle.sdk.generate.CaptureResponse;
import com.litle.sdk.generate.CardTokenType;
import com.litle.sdk.generate.CardType;
import com.litle.sdk.generate.MethodOfPaymentTypeEnum;
import com.litle.sdk.generate.OrderSourceType;
import com.litle.sdk.generate.PayPal;
import com.litle.sdk.generate.Sale;
import com.litle.sdk.generate.SaleResponse;

public class performanceTestMerchantSDKLite {
	private static LitleBatchFileRequest litleBatchFileRequest;
	
	
    public static void main(String[] args) throws Exception {
    	litleBatchFileRequest = new LitleBatchFileRequest("testFile.xml");
    	LitleBatchRequest litleBatchRequest = litleBatchFileRequest.createBatch("101");
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

