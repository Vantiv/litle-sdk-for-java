package com.litle.sdk;

import java.util.ArrayList;
import java.util.List;

import com.litle.sdk.generate.BatchResponse;
import com.litle.sdk.generate.TransactionTypeWithReportGroup;

public class LitleBatchResponse {
	private BatchResponse batchResponse;
	//private Unmarshaller unmarshaller;
	private List<TransactionTypeWithReportGroup> responeList;
	
	public LitleBatchResponse(){
		batchResponse = new BatchResponse();
		responeList = new ArrayList<TransactionTypeWithReportGroup>();
	}

//	public void convertFileToObject() {
//		batchResponse = (BatchResponse)unmarshaller.unmarshal(new StringReader(xmlResponse));
//		if("1".equals(batchResponse.getResponse())) {
//			throw new LitleOnlineException(batchResponse.getMessage());
//		}
//	}
	
	protected void addTransactionToResponse(TransactionTypeWithReportGroup transactionType) {
		responeList.add(transactionType);
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
	
	public TransactionTypeWithReportGroup returnTransactionTypeWithReportGroup() {
		TransactionTypeWithReportGroup transReportGroup = new TransactionTypeWithReportGroup();
		return transReportGroup;
	}

	public List<TransactionTypeWithReportGroup> getResponseList() {
		return responeList;
	}
	
	
}
