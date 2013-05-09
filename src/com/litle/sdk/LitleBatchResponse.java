package com.litle.sdk;

import java.io.StringReader;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import com.litle.sdk.generate.BatchResponse;
import com.litle.sdk.generate.TransactionType;

/**
 * Wrapper class to initialize the batch Responses
 */
public class LitleBatchResponse {
	private BatchResponse batchResponse;
	ResponseFileParser responseFileParser;
	private JAXBContext jc;
	private Unmarshaller unmarshaller;
	
	private boolean allTransactionsRetrieved = false;
	
	LitleBatchResponse(BatchResponse batchResponse) {
		setBatchResponse(batchResponse);
	}
	
	public LitleBatchResponse(ResponseFileParser responseFileParser) throws LitleBatchException{
		this.responseFileParser = responseFileParser;
		
		try {
			String batchResponseXML = responseFileParser.getNextTag("batchResponse");
			jc = JAXBContext.newInstance("com.litle.sdk.generate");
			unmarshaller = jc.createUnmarshaller();
			batchResponse = (BatchResponse) unmarshaller.unmarshal(new StringReader(batchResponseXML));
		} catch (JAXBException e) {
			throw new LitleBatchJAXBException("There was an exception while trying to create objects for response file. Check the JAXB dependency.", e);
		} catch (Exception e) {
			
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
		if( allTransactionsRetrieved ){
			throw new LitleBatchNoMoreBatchTransactionException("All transactions from this batch have already been retrieved");
		}
		
		TransactionType objToRet = null;
		String txnXML = "";
		
		try{
			txnXML = responseFileParser.getNextTag("transactionResponse");
		} catch (Exception e) {
			allTransactionsRetrieved = true;
			throw new LitleBatchNoMoreBatchTransactionException("All transactions from this batch have already been retrieved");
		}
		
		try {
			objToRet = (TransactionType) unmarshaller.unmarshal(new StringReader(txnXML));
		} catch (JAXBException e) {
			throw new LitleBatchJAXBException("There was an exception while reading the transaction response. Check your JAXB dependencies.", e);
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
