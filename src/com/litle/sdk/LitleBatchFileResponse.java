package com.litle.sdk;

import java.io.File;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import com.litle.sdk.generate.BatchResponse;
import com.litle.sdk.generate.LitleOnlineResponse;
import com.litle.sdk.generate.LitleResponse;
import com.litle.sdk.generate.TransactionTypeWithReportGroup;
import com.litle.sdk.generate.UpdateCardValidationNumOnTokenResponse;

public class LitleBatchFileResponse {
	private JAXBContext jc;
	private LitleResponse litleResponse;
	private List<BatchResponse> batchResponseList;
	private Unmarshaller unmarshaller;
	private File xmlFile;
	private String xmlFileResponse;
	
	public LitleBatchFileResponse(File xmlFile){
		// convert from xml to objects
		this.litleResponse = new LitleResponse();
		this.batchResponseList = new ArrayList<BatchResponse>();
		this.xmlFile = xmlFile;
	}
	
	public LitleBatchFileResponse(String xmlResponse) {
		try {
			jc = JAXBContext.newInstance("com.litle.sdk.generate");
			this.unmarshaller = jc.createUnmarshaller();
		} catch (JAXBException e) {
			e.printStackTrace();
		}
		this.xmlFileResponse = xmlResponse;
		this.litleResponse = new LitleResponse();
		this.batchResponseList = new ArrayList<BatchResponse>();
	}
	
	public List<BatchResponse> getBatchResponseList(){
		return batchResponseList;
	}
	
	public File getFile(){
		return xmlFile;
	}
	
	public void convertFileToObject(String xmlResponseFile) throws JAXBException {
		this.litleResponse = (LitleResponse)unmarshaller.unmarshal(new StringReader(xmlResponseFile));
		LitleBatchResponse litleBatchResponse = new LitleBatchResponse();
		for(int i = 0; i< litleResponse.getBatchResponses().size(); i++) {
			//batchResponseList.set(i, response.getBatchResponses().get(i));
			for(int j=0; j< litleResponse.getBatchResponses().get(i).getTransactionResponses().size(); j++) {
				litleBatchResponse.addTransactionToResponse(litleResponse.getBatchResponses().get(i).getTransactionResponses().get(j).getValue());
				//System.out.println(litleResponse.getBatchResponses().get(i).getTransactionResponses().get(j).getValue());
			}
		}
	}
}
