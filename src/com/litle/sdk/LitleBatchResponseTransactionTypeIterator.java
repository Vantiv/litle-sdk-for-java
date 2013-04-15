package com.litle.sdk;

import java.util.Iterator;
import java.util.List;

import javax.xml.bind.JAXBElement;

import com.litle.sdk.generate.TransactionTypeWithReportGroup;

public class LitleBatchResponseTransactionTypeIterator implements Iterator<TransactionTypeWithReportGroup> {

	private List<JAXBElement<? extends TransactionTypeWithReportGroup>> batchTxnResponseList = null;
	private Iterator<JAXBElement<? extends TransactionTypeWithReportGroup>> batchTxnResponseListItr = null;
	
	LitleBatchResponseTransactionTypeIterator(List<JAXBElement<? extends TransactionTypeWithReportGroup>> baseList){
		this.batchTxnResponseList = baseList;
		this.batchTxnResponseListItr = this.batchTxnResponseList.iterator();
	}
	
	public boolean hasNext() {
		return this.batchTxnResponseListItr.hasNext();
	}

	public TransactionTypeWithReportGroup next() {
		return (TransactionTypeWithReportGroup)this.batchTxnResponseListItr.next().getValue();
	}

	public void remove() {
		this.batchTxnResponseListItr.remove();
	}

}
