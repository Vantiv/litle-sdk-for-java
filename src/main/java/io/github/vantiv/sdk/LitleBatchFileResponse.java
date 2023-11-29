package io.github.vantiv.sdk;

import java.io.File;

import javax.xml.bind.JAXBException;


public class LitleBatchFileResponse extends LitleFileResponse{


	/**
	 * This constructor initializes the LitleBatchResponseList to the Response values.
	 * @param xmlFile
	 * @throws JAXBException
	 */

	public LitleBatchFileResponse(File xmlFile) throws LitleBatchException{
		super(xmlFile);
	}

	/**
	 * Retrieves the response object for the next batch in the response file
	 * @return
	 */
	public LitleBatchResponse getNextLitleBatchResponse(){
		LitleBatchResponse retObj = null;
		retObj = new LitleBatchResponse(super.responseFileParser);
		return retObj;
	}
}
