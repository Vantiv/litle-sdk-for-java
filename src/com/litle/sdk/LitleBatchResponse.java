package com.litle.sdk;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.JAXBElement;

import com.litle.sdk.generate.BatchResponse;
import com.litle.sdk.generate.TransactionTypeWithReportGroup;

public class LitleBatchResponse {
	private BatchResponse batchResponse;
	//private Unmarshaller unmarshaller;
	private List<JAXBElement<? extends TransactionTypeWithReportGroup>> responeList;
	
	public LitleBatchResponse(){
		batchResponse = new BatchResponse();
		responeList = new ArrayList<JAXBElement<? extends TransactionTypeWithReportGroup>>();
	}

//	public void convertFileToObject() {
//		batchResponse = (BatchResponse)unmarshaller.unmarshal(new StringReader(xmlResponse));
//		if("1".equals(batchResponse.getResponse())) {
//			throw new LitleOnlineException(batchResponse.getMessage());
//		}
//	}
	
	protected void addTransactionToResponse(JAXBElement<? extends TransactionTypeWithReportGroup> transactionType) {
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

	public List<JAXBElement<? extends TransactionTypeWithReportGroup>> getResponseList() {
		return this.responeList;
	}
	
	public void setResponseList(List<JAXBElement<? extends TransactionTypeWithReportGroup>> txns) {
		for(JAXBElement<? extends TransactionTypeWithReportGroup> txn : txns) {
			this.responeList.add(txn);
		}
	}
	
}
