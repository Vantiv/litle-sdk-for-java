package com.litle.sdk;

import java.io.File;
import java.io.InputStream;

import javax.xml.bind.JAXBException;


public class LitleBatchFileResponse extends LitleFileResponse {


    /**
     * This constructor initializes the LitleBatchResponseList to the Response values.
     *
     * @param xmlFile
     * @throws JAXBException
     */

    public LitleBatchFileResponse(File xmlFile) throws LitleBatchException {
        super(xmlFile);
    }

    public LitleBatchFileResponse(InputStream xmlIs) throws LitleBatchException {
        super(xmlIs);
    }

    /**
     * Retrieves the response object for the next batch in the response file
     *
     * @return
     */
    public LitleBatchResponse getNextLitleBatchResponse() {
        return new LitleBatchResponse(super.responseFileParser);
    }
}
