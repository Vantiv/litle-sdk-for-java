package com.litle.sdk;

import java.math.BigInteger;

import javax.xml.bind.JAXBElement;

import com.litle.sdk.generate.Authorization;
import com.litle.sdk.generate.BatchRequest;
import com.litle.sdk.generate.ObjectFactory;
import com.litle.sdk.generate.TransactionType;

import com.litle.sdk.generate.TransactionTypeWithReportGroup;
import com.litle.sdk.generate.Sale;

public class LitleBatchRequest {
	private BatchRequest batchRequest;
	ObjectFactory objFac;
	
	LitleBatchRequest(String merchantId){
		this.batchRequest = new BatchRequest();
		this.batchRequest.setMerchantId(merchantId);
		this.objFac = new ObjectFactory();
	}
	
	public BatchRequest getBatchRequest(){
		return batchRequest;
	}
	
	public TransactionCodeEnum addTransaction(TransactionType transactionType) {
		//Adding 1 to the number of transaction. This is on the assumption that we are adding one transaction to the batch at a time.
		BigInteger numToAdd = new BigInteger("1");
		boolean transactionAdded = false;
		
//		if((Sale)transactionType){
//			int abc = 0;
//			abc ++;
//		}
//		else{
//			int abc = 0;
//			abc ++;
//		}
		
		if(transactionType instanceof Sale) {
			batchRequest.setNumSales(batchRequest.getNumSales().add(numToAdd));
			Sale sale = (Sale) transactionType;
			BigInteger saleAmount = BigInteger.valueOf(sale.getAmount());
			batchRequest.setSaleAmount(batchRequest.getSaleAmount().add(saleAmount));
			batchRequest.getTransactions().add(objFac.createSale(sale));
			transactionAdded = true;
		}
		else if(transactionType instanceof Authorization) {
			batchRequest.setNumAuths(batchRequest.getNumAuths().add(numToAdd));
			Authorization auth = (Authorization) transactionType;
			BigInteger authAmount = BigInteger.valueOf(auth.getAmount());
			batchRequest.setAuthAmount(batchRequest.getAuthAmount().add(authAmount));
			batchRequest.getTransactions().add(objFac.createAuthorization(auth));
			transactionAdded = true;
		}
		
		if (transactionAdded)
			return TransactionCodeEnum.SUCCESS;
		else
			return TransactionCodeEnum.FAILURE;
	}
	
}
