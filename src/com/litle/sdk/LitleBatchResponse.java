package com.litle.sdk;

import java.util.Iterator;
import java.util.List;

import javax.xml.bind.JAXBElement;

import com.litle.sdk.generate.BatchResponse;
import com.litle.sdk.generate.TransactionTypeWithReportGroup;

public class LitleBatchResponse {
	private BatchResponse batchResponse;
	
	LitleBatchResponse(BatchResponse batchResponse) {
		setBatchResponse(batchResponse);
	}
	
	void setBatchResponse(BatchResponse batchResponse) {
		this.batchResponse = batchResponse;
	}
	
	BatchResponse getBatchResponse() {
		return this.batchResponse;
	}
	
//	public int getNumberOfTransactions(){
//		return this.batchResponse.getTransactionResponses().size();
//	}
	
	public TransactionTypeIterator getTransactionResponses(){
		return new TransactionTypeIterator(batchResponse.getTransactionResponses());
	}
	
	public static class TransactionTypeIterator implements Iterator<TransactionTypeWithReportGroup> {

		private Iterator<JAXBElement<? extends TransactionTypeWithReportGroup>> baseIterator;
		
		TransactionTypeIterator(List<JAXBElement<? extends TransactionTypeWithReportGroup>> baseList){
			baseIterator = baseList.iterator();
		}
		
		public boolean hasNext() {
			return baseIterator.hasNext();
		}

		public TransactionTypeWithReportGroup next() {
			return (TransactionTypeWithReportGroup) baseIterator.next().getValue();
		}

		public void remove() {
			baseIterator.remove();
		}
	}
}
