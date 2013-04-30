package com.litle.sdk;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.math.BigInteger;

import java.sql.Timestamp;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;


import com.litle.sdk.generate.Authorization;
import com.litle.sdk.generate.BatchRequest;
import com.litle.sdk.generate.ObjectFactory;
import com.litle.sdk.generate.TransactionType;

import com.litle.sdk.generate.Sale;

public class LitleBatchRequest {
	private BatchRequest batchRequest;
	private JAXBContext jc;
	private File file;
	private Marshaller marshaller;
	ObjectFactory objFac;
	TransactionType txn;
	String filePath;
	OutputStream osWrttxn;
	
	int numOfTxn;
	
	
	private final int maxTransactionsPerBatch;
	protected int litleLimit_maxTransactionsPerBatch = 100000;
	private final LitleBatchFileRequest lbfr;
	
	
	/** 
	 * This method initializes the batch level attributes of the XML and checks if the maxTransactionsPerBatch is not more than the value provided in the properties file
	 * @param merchantId
	 * @param lbfr
	 * @throws JAXBException 
	 * @throws FileNotFoundException 
	 */
	LitleBatchRequest(String merchantId, LitleBatchFileRequest lbfr) throws JAXBException, FileNotFoundException{
		this.batchRequest = new BatchRequest();
		this.batchRequest.setMerchantId(merchantId);
		this.objFac = new ObjectFactory();
		this.lbfr = lbfr;
		File tmpFile = new File(lbfr.getConfig().getProperty("batchRequestFolder")+"/tmp");
		if(!tmpFile.exists()) {
			tmpFile.mkdir();
		}
		java.util.Date date= new java.util.Date();
		filePath = new String(lbfr.getConfig().getProperty("batchRequestFolder")+ "/tmp/Transactions" +merchantId + new Timestamp(date.getTime()));
		numOfTxn = 0;
		this.jc = JAXBContext.newInstance("com.litle.sdk.generate");
		marshaller = jc.createMarshaller();
		marshaller.setProperty(Marshaller.JAXB_FRAGMENT, true);
		marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
		this.maxTransactionsPerBatch = Integer.parseInt(lbfr.getConfig().getProperty("maxTransactionsPerBatch"));
		
		if( maxTransactionsPerBatch > litleLimit_maxTransactionsPerBatch ){
			throw new LitleBatchException("maxTransactionsPerBatch property value cannot exceed " + String.valueOf(litleLimit_maxTransactionsPerBatch));
		}
	}
	
	BatchRequest getBatchRequest(){
		return batchRequest;
	}
	
	/**
	 * This method is used to add transaction to a particular batch
	 * @param transactionType
	 * @return
	 * @throws FileNotFoundException 
	 * @throws JAXBException 
	 */
	public TransactionCodeEnum addTransaction(TransactionType transactionType) throws FileNotFoundException, JAXBException {
		if (numOfTxn == 0) {
			this.file = new File(filePath);
			osWrttxn = new FileOutputStream(file.getAbsolutePath());
		}
		
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
			//Write the txn to a file
			marshaller.marshal(objFac.createSale(sale), osWrttxn);
			transactionAdded = true;
			numOfTxn ++;
		}
		else if(transactionType instanceof Authorization) {
			batchRequest.setNumAuths(batchRequest.getNumAuths().add(numToAdd));
			Authorization auth = (Authorization) transactionType;
			BigInteger authAmount = BigInteger.valueOf(auth.getAmount());
			batchRequest.setAuthAmount(batchRequest.getAuthAmount().add(authAmount));
			//Write the txn to a file
			
			marshaller.marshal(objFac.createAuthorization(auth), osWrttxn);
			transactionAdded = true;
			numOfTxn ++;
		}
		
		batchFileStatus = verifyFileThresholds();
		if( batchFileStatus == TransactionCodeEnum.FILEFULL){
			return TransactionCodeEnum.FILEFULL;
		} else if( batchFileStatus == TransactionCodeEnum.BATCHFULL ){
			return TransactionCodeEnum.BATCHFULL;
		}
		
		if (transactionAdded) {
			return TransactionCodeEnum.SUCCESS;
		}
		else
			return TransactionCodeEnum.FAILURE;
	}
	
	/**
	 * This method makes sure that the maximum number of transactions per batch and file is not exceeded
	 * This is to ensure Performance.
	 * @return
	 */
	TransactionCodeEnum verifyFileThresholds(){
		if( this.lbfr.getNumberOfTransactionInFile() == this.lbfr.getMaxAllowedTransactionsPerFile()){
			return TransactionCodeEnum.FILEFULL;
		}
		else if( getNumberOfTransactions() == this.maxTransactionsPerBatch ){
			return TransactionCodeEnum.BATCHFULL;
		}
		return TransactionCodeEnum.SUCCESS;
	}
	
	public int getNumberOfTransactions(){
		return (numOfTxn);
	}
	
	public boolean isFull() {
		return (getNumberOfTransactions() == this.maxTransactionsPerBatch);
	}

	public void closeFile() throws IOException {
		osWrttxn.close();
	}
	
	public File getFile() {
		return this.file;
	}

	
}
