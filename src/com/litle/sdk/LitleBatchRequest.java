package com.litle.sdk;

import com.litle.sdk.generate.BatchRequest;
import com.litle.sdk.generate.LitleRequest;


public class LitleBatchRequest {
	
	private String merchantId;
	private BatchRequest batchRequest;
	
	LitleBatchRequest(String merchantId){
		this.merchantId = merchantId;
		this.batchRequest = new BatchRequest();
	}
	
	public TransactionCodeEnum addTransaction() {
		// add to the count and amount based on txn info.
		// need to use instance of to figure out the type of txn.
		return TransactionCodeEnum.SUCCESS;
	}
	
}
