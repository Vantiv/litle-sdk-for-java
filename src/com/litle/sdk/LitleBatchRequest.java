package com.litle.sdk;

import java.math.BigInteger;

import com.litle.sdk.generate.Authorization;
import com.litle.sdk.generate.BatchRequest;

import com.litle.sdk.generate.TransactionTypeWithReportGroup;
import com.litle.sdk.generate.Sale;

public class LitleBatchRequest {
	private BatchRequest batchRequest;
	
	LitleBatchRequest(String merchantId){
		this.batchRequest = new BatchRequest();
		this.batchRequest.setMerchantId(merchantId);
	}
	
	public BatchRequest getBatchRequest(){
		return batchRequest;
	}
	
	public TransactionCodeEnum addTransaction(TransactionTypeWithReportGroup transactionType) {
		//Adding 1 to the number of transaction. This is on the assumption that we are adding one transaction to the batch at a time.
		BigInteger numToAdd = new BigInteger("1");
		boolean transactionAdded = false;
		
		if(TransactionTypeWithReportGroup.class.isInstance(Sale.class)) {
			batchRequest.setNumSales(batchRequest.getNumAuths().add(numToAdd));
			Sale sale = (Sale) transactionType;
			BigInteger saleAmount = BigInteger.valueOf(sale.getAmount());
			batchRequest.setNumSales(batchRequest.getSaleAmount().add(saleAmount));
			transactionAdded = true;
		}
		else if(TransactionTypeWithReportGroup.class.isInstance(Authorization.class)) {
			batchRequest.setNumAuths(batchRequest.getNumAuths().add(numToAdd));
			Authorization auth = (Authorization) transactionType;
			BigInteger authAmount = BigInteger.valueOf(auth.getAmount());
			batchRequest.setNumSales(batchRequest.getSaleAmount().add(authAmount));
			transactionAdded = true;
		}
		
		if (transactionAdded)
			return TransactionCodeEnum.SUCCESS;
		else
			return TransactionCodeEnum.FAILURE;
	}
	
}
