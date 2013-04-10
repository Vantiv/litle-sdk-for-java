package com.litle.sdk;

import java.io.File;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import com.litle.sdk.generate.BatchResponse;
import com.litle.sdk.generate.LitleOnlineResponse;
import com.litle.sdk.generate.LitleResponse;
import com.litle.sdk.generate.TransactionTypeWithReportGroup;
import com.litle.sdk.generate.UpdateCardValidationNumOnTokenResponse;

public class LitleBatchFileResponse {
	private LitleResponse litleResponse;
	private List<BatchResponse> batchResponseList;
	private Unmarshaller unmarshaller;
	private File xmlFile;
	
	public LitleBatchFileResponse(File xmlFile){
		// convert from xml to objects
		this.litleResponse = new LitleResponse();
		this.batchResponseList = new ArrayList<BatchResponse>();
		this.xmlFile = xmlFile;
	}
	
	public List<BatchResponse> getBatchResponseList(){
		return batchResponseList;
	}
	
	public File getFile(){
		return xmlFile;
	}
	
	public void convertFileToObject(String xmlResponseFile) throws JAXBException {
		LitleBatchResponse litleBatchResponse = new LitleBatchResponse();
		LitleResponse response = (LitleResponse)unmarshaller.unmarshal(new StringReader(xmlResponseFile));
		for(int i = 0; i< response.getBatchResponses().size(); i++) {
			batchResponseList.set(i, response.getBatchResponses().get(i));
			for(int j=0; j< response.getBatchResponses().get(i).getTransactionResponses().size(); j++) {
				litleBatchResponse.addTransactionToResponse(response.getBatchResponses().get(i).getTransactionResponses().get(j).getValue());
			}
		}
	}
}
