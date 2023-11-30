package io.github.vantiv.sdk;

import java.io.File;

import javax.xml.bind.JAXBException;

/**
 * Container class holding the RFR response
 *
 */
public class LitleRFRFileResponse extends LitleFileResponse{
	/**
	 * This constructor initializes the LitleBatchResponseList to the Response values.
	 * @param xmlFile
	 * @throws JAXBException
	 */
	public LitleRFRFileResponse(File xmlFile) throws LitleBatchException{
		super(xmlFile);
	}

	public LitleRFRResponse getLitleRFRResponse(){
		LitleRFRResponse retObj = null;
		retObj = new LitleRFRResponse(super.responseFileParser);
		return retObj;
	}
}
