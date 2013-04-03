package com.litle.sdk;

import com.litle.sdk.generate.BatchRequest;
import com.litle.sdk.generate.LitleRequest;


public class LitleBatch {
	
	private String merchantId;
	private BatchRequest batchRequest;
	
	
	LitleBatch(String merchantId){
		this.merchantId = merchantId;
		this.batchRequest = new BatchRequest();
	}
	
	public TransactionCodeEnum addTransaction() {
		return TransactionCodeEnum.SUCCESS;
	}
	
	

}
