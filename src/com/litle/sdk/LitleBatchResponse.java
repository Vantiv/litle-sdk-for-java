package com.litle.sdk;

import com.litle.sdk.generate.BatchResponse;

public class LitleBatchResponse {
	private BatchResponse batchResponse;
	
	public LitleBatchResponse(){
	}
	
	public void setBatchResponse(BatchResponse batchResponse) {
		this.batchResponse = batchResponse;
	}
	
	public BatchResponse getBatchResponse() {
		return this.batchResponse;
	}
	
	public int getNumberOfTransactions(){
		return this.batchResponse.getTransactionResponses().size();
	}
	
	public LitleBatchResponseTransactionTypeIterator getTransactionResponses(){
		return new LitleBatchResponseTransactionTypeIterator(this.batchResponse.getTransactionResponses());
	}
}
