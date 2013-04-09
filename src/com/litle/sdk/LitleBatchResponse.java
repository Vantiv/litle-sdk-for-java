package com.litle.sdk;

import java.io.StringReader;
import java.math.BigInteger;

import javax.xml.bind.Unmarshaller;
import javax.xml.datatype.XMLGregorianCalendar;

import com.litle.sdk.generate.AccountInfoType;
import com.litle.sdk.generate.Authorization;
import com.litle.sdk.generate.AuthorizationResponse;
import com.litle.sdk.generate.BatchResponse;
import com.litle.sdk.generate.BillMeLaterResponseData;
import com.litle.sdk.generate.FraudResult;
import com.litle.sdk.generate.LitleOnlineResponse;
import com.litle.sdk.generate.MethodOfPaymentTypeEnum;
import com.litle.sdk.generate.Sale;
import com.litle.sdk.generate.SaleResponse;
import com.litle.sdk.generate.TransactionType;

public class LitleBatchResponse {
	private BatchResponse batchResponse;
	//private Unmarshaller unmarshaller;
	
	public LitleBatchResponse(){
		batchResponse = new BatchResponse();
	}

//	public void convertFileToObject() {
//		batchResponse = (BatchResponse)unmarshaller.unmarshal(new StringReader(xmlResponse));
//		if("1".equals(batchResponse.getResponse())) {
//			throw new LitleOnlineException(batchResponse.getMessage());
//		}
//	}
	
	public TransactionCodeEnum addTransactionToResponse(TransactionType transactionType) {
		//Adding 1 to the number of transaction. This is on the assumption that we are adding one transaction to the batch at a time.
		boolean transactionAdded = false;
		
		batchResponse.setId("batchId");
		batchResponse.setLitleBatchId(0L);
		batchResponse.setMerchantId("");
		
		if(transactionType instanceof Sale) {
			
			SaleResponse saleResponse = new SaleResponse();
			saleResponse.setId("");
			saleResponse.setReportGroup("");
			saleResponse.setLitleTxnId(0L);
			saleResponse.setOrderId("");
			saleResponse.setResponse("");
			//saleResponse.setResponseTime(new XMLGregorianCalendar());
			saleResponse.setMessage("");
			saleResponse.setAuthCode("");
			AccountInfoType accountInfoType = new AccountInfoType();
			accountInfoType.setNumber("");
			accountInfoType.setType(MethodOfPaymentTypeEnum.BL);
			saleResponse.setAccountInformation(accountInfoType);
			saleResponse.setBillMeLaterResponseData(new BillMeLaterResponseData());
			transactionAdded = true;
		}
		else if(transactionType instanceof Authorization) {
			AuthorizationResponse authorizationResponse = new AuthorizationResponse();
			authorizationResponse.setId("");
			authorizationResponse.setReportGroup("");
			authorizationResponse.setLitleTxnId(0L);
			authorizationResponse.setOrderId("");
			authorizationResponse.setResponse("");
			//authorizationResponse.setResponseTime(value);
			authorizationResponse.setMessage("");
			authorizationResponse.setAuthCode("");
			AccountInfoType accountInfoType = new AccountInfoType();
			accountInfoType.setNumber("");
			accountInfoType.setType(MethodOfPaymentTypeEnum.BL);
			authorizationResponse.setAccountInformation(accountInfoType);
			FraudResult fraudResult = new FraudResult();
			fraudResult.setAdvancedAVSResult("");
			fraudResult.setAuthenticationResult("");
			fraudResult.setAvsResult("");
			fraudResult.setCardValidationResult("");
			authorizationResponse.setFraudResult(fraudResult);
			
			authorizationResponse.setBillMeLaterResponseData(new BillMeLaterResponseData());
			
			transactionAdded = true;
		}
	
		if (transactionAdded)
			return TransactionCodeEnum.SUCCESS;
		else
			return TransactionCodeEnum.FAILURE;
	}
	
	public int getNumberOfTransactions(){
		return this.batchResponse.getTransactionResponses().size();
	}
}
