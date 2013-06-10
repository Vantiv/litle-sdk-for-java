package com.litle.sdk;

import java.io.StringReader;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import com.litle.sdk.generate.AccountUpdateResponse;
import com.litle.sdk.generate.AuthReversalResponse;
import com.litle.sdk.generate.AuthorizationResponse;
import com.litle.sdk.generate.BatchResponse;
import com.litle.sdk.generate.CaptureGivenAuthResponse;
import com.litle.sdk.generate.CaptureResponse;
import com.litle.sdk.generate.CreditResponse;
import com.litle.sdk.generate.EcheckCreditResponse;
import com.litle.sdk.generate.EcheckRedepositResponse;
import com.litle.sdk.generate.EcheckSalesResponse;
import com.litle.sdk.generate.EcheckVerificationResponse;
import com.litle.sdk.generate.ForceCaptureResponse;
import com.litle.sdk.generate.RegisterTokenResponse;
import com.litle.sdk.generate.SaleResponse;
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
			batchResponseXML = responseFileParser.getNextTag("batchResponse");
			//batchResponseXML = "<batchResponse litleBatchId=\"1431\" merchantId=\"101\" xmlns=\"http://www.litle.com/schema\"></batchResponse>";
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

		String txnXML = "";

		try{
			txnXML = responseFileParser.getNextTag("transactionResponse");
		} catch (Exception e) {
			allTransactionsRetrieved = true;
			throw new LitleBatchNoMoreBatchTransactionException("All transactions from this batch have already been retrieved");
		}
		System.out.println(txnXML);
		try {
			@SuppressWarnings("unchecked")
			TransactionType objToRet = ((JAXBElement<TransactionType>)unmarshaller.unmarshal(new StringReader(txnXML))).getValue();
			return objToRet;
		} catch (JAXBException e) {
			throw new LitleBatchException("There was an exception while trying to unmarshall transactionResponse: " + txnXML, e);
		}
	}


	public boolean processNextTransaction(LitleResponseProcessor processor){
	    String txnXml = "";
	    TransactionType objToRet;
	    try {
            txnXml = responseFileParser.getNextTag("transactionResponse");
        } catch (Exception e) {
            return false;
        }

	    try{
	        objToRet = ((JAXBElement<TransactionType>)unmarshaller.unmarshal(new StringReader(txnXml))).getValue();
	    } catch (JAXBException e){
	        throw new LitleBatchException("There was an exception while trying to unmarshall transactionResponse: " + txnXml, e);
	    }

	    if(objToRet instanceof SaleResponse){
            processor.processSaleResponse((SaleResponse) objToRet);
        } else if (objToRet instanceof AuthorizationResponse){
            processor.processAuthorizationResponse((AuthorizationResponse) objToRet);
        } else if (objToRet instanceof CreditResponse){
            processor.processCreditResponse((CreditResponse) objToRet);
        } else if (objToRet instanceof RegisterTokenResponse){
            processor.processRegisterTokenResponse((RegisterTokenResponse) objToRet);
        } else if (objToRet instanceof CaptureGivenAuthResponse){
            processor.processCaptureGivenAuthResponse((CaptureGivenAuthResponse) objToRet);
        } else if (objToRet instanceof ForceCaptureResponse){
            processor.processForceCaptureResponse( (ForceCaptureResponse) objToRet);
        } else if (objToRet instanceof AuthReversalResponse){
            processor.processAuthReversalResponse( (AuthReversalResponse) objToRet);
        } else if (objToRet instanceof CaptureResponse){
            processor.processCaptureResponse((CaptureResponse) objToRet);
        } else if (objToRet instanceof EcheckVerificationResponse){
            processor.processEcheckVerificationResponse( (EcheckVerificationResponse) objToRet);
        } else if (objToRet instanceof EcheckCreditResponse){
            processor.processEcheckCreditResponse( (EcheckCreditResponse) objToRet);
        } else if (objToRet instanceof EcheckRedepositResponse){
            processor.processEcheckRedepositResponse( (EcheckRedepositResponse) objToRet);
        } else if (objToRet instanceof EcheckSalesResponse){
            processor.processEcheckSalesResponse((EcheckSalesResponse) objToRet);
        } else if (objToRet instanceof AccountUpdateResponse){
            processor.processAccountUpdate((AccountUpdateResponse) objToRet);
        }
	    return true;

//       else if (objToRet instanceof UpdateCardValidationNumOnToken){
//
//        } else if (objToRet instanceof AccountUpdate){
//        }
//
	}

}
