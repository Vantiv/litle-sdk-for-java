package com.litle.sdk;

import java.io.StringReader;
import java.util.Iterator;
import java.util.List;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import com.litle.sdk.generate.BatchResponse;
import com.litle.sdk.generate.LitleResponse;
import com.litle.sdk.generate.TransactionType;
import com.litle.sdk.generate.TransactionTypeWithReportGroup;

/**
 * Wrapper class to initialize the batch Responses
 */
public class LitleBatchResponse {
	private BatchResponse batchResponse;
	ResponseFileParser responseFileParser;
	private JAXBContext jc;
	private Unmarshaller unmarshaller;
	
	private int numberOfTransactionsRequests = 0;
	
	LitleBatchResponse(BatchResponse batchResponse) {
		setBatchResponse(batchResponse);
	}
	
	public LitleBatchResponse(ResponseFileParser responseFileParser) {
		this.responseFileParser = responseFileParser;
		String batchResponseXML = responseFileParser.getNextTag("batchResponse");
		
		try {
			jc = JAXBContext.newInstance("com.litle.sdk.generate");
			unmarshaller = jc.createUnmarshaller();
			batchResponse = (BatchResponse) unmarshaller.unmarshal(new StringReader(batchResponseXML));
		} catch (JAXBException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	void setBatchResponse(BatchResponse batchResponse) {
		this.batchResponse = batchResponse;
	}
	
	BatchResponse getBatchResponse() {
		return this.batchResponse;
	}
	
	public long getLitleBatchId() {
		return this.batchResponse.getLitleBatchId();
	}
	
	public String getMerchantId() {
		return this.batchResponse.getMerchantId();
	}
	
	public TransactionType getNextTransaction(){
		numberOfTransactionsRequests++;
		
		//TODO: count of transactions needs to be handled.
		
		TransactionType objToRet = null;
		
		try {
			String txnXML = responseFileParser.getNextTag("transactionResponse");
			objToRet = (TransactionType) unmarshaller.unmarshal(new StringReader(txnXML));
		} catch (JAXBException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return objToRet;
	}
	
//	public int getNumberOfTransactions(){
//		return this.batchResponse.getTransactionResponses().size();
//		int totalTxns = 0;
//		
//		totalTxns += this.batchResponse.
//	}
	
//	public TransactionTypeIterator getTransactionResponses(){
//		return new TransactionTypeIterator(batchResponse.getTransactionResponses());
//	}
	
//	/** 
//	 * This sub class is helps the user to navigate through the objects to access the values of the
//	 * transaction responses.
//	 * This class also provides the iterator to navigate through the objects.
//	 *
//	 */
//	public static class TransactionTypeIterator implements Iterator<TransactionTypeWithReportGroup> {
//
//		private Iterator<JAXBElement<? extends TransactionTypeWithReportGroup>> baseIterator;
//		
//		TransactionTypeIterator(List<JAXBElement<? extends TransactionTypeWithReportGroup>> baseList){
//			baseIterator = baseList.iterator();
//		}
//		
//		public boolean hasNext() {
//			return baseIterator.hasNext();
//		}
//
//		public TransactionTypeWithReportGroup next() {
//			return (TransactionTypeWithReportGroup) baseIterator.next().getValue();
//		}
//
//		public void remove() {
//			baseIterator.remove();
//		}
//	}
}
