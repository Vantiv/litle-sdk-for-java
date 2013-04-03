package com.litle.sdk;

import com.litle.sdk.generate.BatchRequest;
import com.litle.sdk.generate.LitleRequest;


public class LitleBatch {
	
	private String merchantId;
	private BatchRequest batchRequest;
	
	public static LitleBatch createLitleBatch(String merchantId) {
		
		return new LitleBatch(merchantId);
	}
	
	LitleBatch(String merchantId){
		this.merchantId = merchantId;
		this.batchRequest = new BatchRequest();
	}
	
	public TransactionCodeEnum addTransaction() {
		return TransactionCodeEnum.SUCCESS;
	}
	
	public BatchRequest getBatchRequest() {
		return this.batchRequest;
	}

}
