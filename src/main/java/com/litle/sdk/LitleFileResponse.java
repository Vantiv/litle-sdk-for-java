package com.litle.sdk;

import java.io.File;
import java.io.IOException;
import java.io.StringReader;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import com.litle.sdk.generate.LitleResponse;

/**
 * Provides abstraction over Litle Requests containing  batch requests and Litle Requests containing RFR requests
 * @author ahammond
 *
 */

abstract class LitleFileResponse {

    protected JAXBContext jc;
    protected LitleResponse litleResponse;
    protected Unmarshaller unmarshaller;
    protected File xmlFile;
    ResponseFileParser responseFileParser;

    public LitleFileResponse(File xmlFile) throws LitleBatchException{
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


    public ResponseFileParser getResponseFileParser() {
        return responseFileParser;
    }


    public void setResponseFileParser(ResponseFileParser responseFileParser) {
        this.responseFileParser = responseFileParser;
    }

    public void closeResources() throws IOException{
        this.responseFileParser.closeResources();
    }
}
