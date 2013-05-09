package com.litle.sdk;

import java.io.File;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import com.litle.sdk.generate.BatchResponse;

import com.litle.sdk.generate.LitleResponse;


public class LitleBatchFileResponse {
	private JAXBContext jc;
	private LitleResponse litleResponse;
	//private List<LitleBatchResponse> litleBatchResponseList;
	private Unmarshaller unmarshaller;
	private File xmlFile;
	ResponseFileParser responseFileParser = null;
	
	/**
	 * This constructor initializes the LitleBatchResponseList to the Response values.
	 * @param xmlFile
	 * @throws JAXBException
	 */
	
	public LitleBatchFileResponse(File xmlFile) throws LitleBatchException{
		// convert from xml to objects
		
		try {
			this.xmlFile = xmlFile;
			responseFileParser = new ResponseFileParser(xmlFile);
			String litleResponseXml = responseFileParser.getNextTag("litleResponse");
			
			jc = JAXBContext.newInstance("com.litle.sdk.generate");
			unmarshaller = jc.createUnmarshaller();
			litleResponse = (LitleResponse) unmarshaller.unmarshal(new StringReader(litleResponseXml));
		} catch (JAXBException e) {
			throw new LitleBatchException("There was an exception while unmarshalling the response file. Check your JAXB dependencies.", e);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
//		wrapBatchResponses(litleResponse.getBatchResponses());
	}
	
//	/**
//	 * This constructor initializes the LitleBatchResponseList to the Response values.
//	 * @param xmlResponse
//	 * @throws JAXBException
//	 */
//	public LitleBatchFileResponse(String xmlResponse) throws LitleBatchException {
//		try {
//			jc = JAXBContext.newInstance("com.litle.sdk.generate");
//			this.unmarshaller = jc.createUnmarshaller();
//			this.litleResponse = (LitleResponse) unmarshaller.unmarshal(new StringReader(xmlResponse));
//		} catch (JAXBException e) {
//			throw new LitleBatchException("There was an exception while unmarshalling the response file. Check your JAXB dependencies.", e);
//		}
//		
//		wrapBatchResponses(litleResponse.getBatchResponses());
//	}
	
//	public List<LitleBatchResponse> getBatchResponseList(){
//		return this.litleBatchResponseList;
//	}
	
	public LitleBatchResponse getNextLitleBatchResponse(){
		LitleBatchResponse retObj = null;
		
		retObj = new LitleBatchResponse(responseFileParser);
		
		return retObj;
	}
	
	public File getFile() {
		return xmlFile;
	}
	
	public long getLitleSessionId() {
		return this.litleResponse.getLitleSessionId();
	}
	
	public String getVersion() {
		return this.litleResponse.getVersion();
	}
	
	public String getResponse() {
		return this.litleResponse.getResponse();
	}
	
	public String getMessage() {
		return this.litleResponse.getMessage();
	}
	
	public String getId() {
		return this.litleResponse.getId();
	}
}
