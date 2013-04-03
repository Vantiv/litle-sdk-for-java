package com.litle.sdk;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import com.litle.sdk.generate.LitleResponse;

public class LitleBatchFileResponse {
	private LitleResponse litleResponse;
	private List<LitleBatchResponse> batchResponseList;
	private File xmlFile;
	
	public LitleBatchFileResponse(File xmlFile){
		// convert from xml to objects
		this.litleResponse = new LitleResponse();
		this.batchResponseList = new ArrayList<LitleBatchResponse>();
		this.xmlFile = xmlFile;
	}
	
	public List<LitleBatchResponse> getBatchResponseList(){
		return batchResponseList;
	}
	
	public File getFile(){
		return xmlFile;
	}
}
