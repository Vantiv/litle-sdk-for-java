package com.litle.sdk;

import java.math.BigInteger;
import java.util.Properties;

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
	
	private final int maxTransactionsPerBatch;
	private final LitleBatchFileRequest lbfr;
	
	LitleBatchRequest(String merchantId, LitleBatchFileRequest lbfr){
		this.batchRequest = new BatchRequest();
		this.batchRequest.setMerchantId(merchantId);
		this.objFac = new ObjectFactory();
		this.lbfr = lbfr;
		this.maxTransactionsPerBatch = Integer.parseInt(lbfr.getConfig().getProperty("maxTransactionsPerBatch"));
	}
	
	public BatchRequest getBatchRequest(){
		return batchRequest;
	}
	
	public TransactionCodeEnum addTransaction(TransactionType transactionType) {
		TransactionCodeEnum batchFileStatus = verifyFileThresholds();
		if( batchFileStatus == TransactionCodeEnum.FILEFULL){
			Exception e = new Exception();
			throw new LitleBatchException("Batch File is already full -- it has reached the maximum number of transactions allowed per batch file.", e);
		} else if( batchFileStatus == TransactionCodeEnum.BATCHFULL ){
			Exception e = new Exception();
			throw new LitleBatchException("Batch is already full -- it has reached the maximum number of transactions allowed per batch.", e);
		}
		
		//Adding 1 to the number of transaction. This is on the assumption that we are adding one transaction to the batch at a time.
		BigInteger numToAdd = new BigInteger("1");
		boolean transactionAdded = false;
		
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
		
		batchFileStatus = verifyFileThresholds();
		if( batchFileStatus == TransactionCodeEnum.FILEFULL){
			return TransactionCodeEnum.FILEFULL;
		} else if( batchFileStatus == TransactionCodeEnum.BATCHFULL ){
			return TransactionCodeEnum.BATCHFULL;
		}
		
		if (transactionAdded)
			return TransactionCodeEnum.SUCCESS;
		else
			return TransactionCodeEnum.FAILURE;
	}
	
	public TransactionCodeEnum verifyFileThresholds(){
		if( this.lbfr.getNumberOfTransactionInFile() == this.lbfr.getMaxAllowedTransactionsPerFile()){
			return TransactionCodeEnum.FILEFULL;
		}
		else if( getNumberOfTransactions() == this.maxTransactionsPerBatch ){
			return TransactionCodeEnum.BATCHFULL;
		}
		return TransactionCodeEnum.SUCCESS;
	}
	
	public int getNumberOfTransactions(){
		return this.batchRequest.getTransactions().size();
	}
	
	public boolean isFull() {
		return (getNumberOfTransactions() == this.maxTransactionsPerBatch);
	}
	
}
