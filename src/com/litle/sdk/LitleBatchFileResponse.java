package com.litle.sdk;

import java.io.File;
import java.io.StringReader;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import com.litle.sdk.generate.LitleResponse;


public class LitleBatchFileResponse {
	private JAXBContext jc;
	private LitleResponse litleResponse;
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
			throw new LitleBatchException("There was an exception while reading the Litle response file. The response file might not have been generated. Try re-sending the request file or contact us.", e);
		}
	}
	
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
