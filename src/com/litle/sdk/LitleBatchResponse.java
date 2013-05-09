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
		String batchResponseXML = "";
		
		try {
//			batchResponseXML = responseFileParser.getNextTag("batchResponse");
			batchResponseXML = "<batchResponse litleBatchId=\"1431\" merchantId=\"101\"></batchResponse>";
			jc = JAXBContext.newInstance("com.litle.sdk.generate");
			unmarshaller = jc.createUnmarshaller();
			batchResponse = (BatchResponse) unmarshaller.unmarshal(new StringReader(batchResponseXML));
		} catch (JAXBException e) {
			throw new LitleBatchException("There was an exception while trying to unmarshall batchResponse: " + batchResponseXML, e);
		} catch (Exception e) {
			throw new LitleBatchException("There was an unknown error while parsing the response file.", e);
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
			throw new LitleBatchException("There was an exception while trying to unmarshall transactionResponse: " + txnXML, e);
		}
		
		return objToRet;
	}
	
}
