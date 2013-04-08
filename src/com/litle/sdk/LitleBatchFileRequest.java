package com.litle.sdk;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.math.BigInteger;
import java.nio.charset.Charset;
//import java.io.file.Files;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

import com.litle.sdk.generate.Authentication;
import com.litle.sdk.generate.Authorization;
import com.litle.sdk.generate.AuthorizationResponse;
import com.litle.sdk.generate.BatchRequest;
import com.litle.sdk.generate.Capture;
import com.litle.sdk.generate.CaptureResponse;

import com.litle.sdk.generate.LitleOnlineResponse;
import com.litle.sdk.generate.LitleRequest;
import com.litle.sdk.generate.ObjectFactory;
import com.litle.sdk.generate.TransactionTypeWithReportGroup;
import com.litle.sdk.generate.TransactionTypeWithReportGroupAndPartial;

public class LitleBatchFileRequest {
	
	private JAXBContext jc;
	private Properties config;
	private ObjectFactory objectFactory;
	private Marshaller marshaller;
	private Unmarshaller unmarshaller;
	private Communication communication;
	private LitleRequest litleRequest;
	private List<LitleBatchRequest> litleBatchRequestList;
	private String requestFileName;
	
	/**
	 * Construct a LitleOnline using the configuration specified in $HOME/.litle_SDK_config.properties
	 */
	public LitleBatchFileRequest(String requestFileName) {
		try {
			this.jc = JAXBContext.newInstance("com.litle.sdk.generate");
			this.marshaller = jc.createMarshaller();
			this.unmarshaller = jc.createUnmarshaller();
			this.communication = new Communication();
			this.objectFactory = new ObjectFactory();
			this.litleRequest = new LitleRequest();
			this.litleBatchRequestList = new ArrayList<LitleBatchRequest>();
//			if(!(requestFileName.isEmpty())){
//				this.requestFileName = requestFileName;
//			}
//			else{
//				throw new LitleBatchException("You need to supply a filename for the file.");
//			}
			
			try {
				config = new Properties();
				config.load(new FileInputStream(Configuration.location()));
			} catch (FileNotFoundException e) {
				throw new LitleBatchException("Configuration file not found. If you are not using the .litle_SDK_config.properties file, please use the LitleOnline(Properties) constructor.  If you are using .litle_SDK_config.properties, you can generate one using java -jar litle-sdk-for-java-8.10.jar", e);
			} catch (IOException e) {
				throw new LitleBatchException("Configuration file could not be loaded.  Check to see if the user running this has permission to access the file", e);
			}
			
			Authentication authentication = new Authentication();
			authentication.setPassword(config.getProperty("password"));
			authentication.setUser(config.getProperty("username"));
			this.litleRequest.setAuthentication(authentication);
			
		} catch (JAXBException e) {
			throw new LitleBatchException("Unable to load jaxb dependencies.  Perhaps a classpath issue?", e);
		}
	}
	
	/**
	 * Construct a LitleOnline specifying the configuration in code.  This should be used by integrations that
	 * have another way to specify their configuration settings (ofbiz, etc)
	 * 
	 * Properties that *must* be set are:
	 * 
	 * 	url (eg https://payments.litle.com/vap/communicator/online)
	 *	reportGroup (eg "Default Report Group")
	 *	username
	 *	merchantId
	 *	password
	 *	version (eg 8.10)
	 *	timeout (in seconds)
	 *	Optional properties are: 
	 *	proxyHost
	 *	proxyPort
	 *	printxml (possible values "true" and "false" - defaults to false)
	 *
	 * @param config
	 */
	public LitleBatchFileRequest(Properties config) {
		this.config = config;
		try {
			jc = JAXBContext.newInstance("com.litle.sdk.generate");
			marshaller = jc.createMarshaller();
			unmarshaller = jc.createUnmarshaller();
			communication = new Communication();
			objectFactory = new ObjectFactory();
		} catch (JAXBException e) {
			throw new LitleOnlineException("Unable to load jaxb dependencies.  Perhaps a classpath issue?", e);
		}
	}

	protected void setCommunication(Communication communication) {
		this.communication = communication;
	}
	
	
	public LitleBatchRequest createBatch(String merchantId) {
		LitleBatchRequest litleBatchRequest = new LitleBatchRequest(merchantId);
		litleBatchRequestList.add(litleBatchRequest);
		return litleBatchRequest;
	}
	
//	public LitleBatchRequest sendBatchFileToLitle(String batchResponseFile) {
//		LitleBatchRequest litleBatch = new LitleBatchRequest(batchResponseFile);
//		return litleBatch;
//	}

	public void generateRawFile() {
		
	}
	private LitleRequest createLitleRequest() {
		LitleRequest request = new LitleRequest();
		BatchRequest batchRequest = new BatchRequest();
		batchRequest.setMerchantId(config.getProperty("merchantId"));
		request.setVersion(config.getProperty("version"));
		Authentication authentication = new Authentication();
		authentication.setPassword(config.getProperty("password"));
		authentication.setUser(config.getProperty("username"));
		//request.setLoggedInUser(config.getProperty("loggedInUser",null));
	
		return request;
	}
	
	private LitleRequest fillInMissingFieldsFromConfig(LitleRequest request) {
		LitleRequest retVal = new LitleRequest();
		retVal.setAuthentication(new Authentication());
		if(request.getAuthentication() == null) {
			Authentication authentication = new Authentication();
			authentication.setPassword(config.getProperty("password"));
			authentication.setUser(config.getProperty("username"));
			retVal.setAuthentication(authentication);			
		}
		else {
			if(request.getAuthentication().getUser() == null) {
				retVal.getAuthentication().setUser(config.getProperty("username"));
			}
			else {
				retVal.getAuthentication().setUser(request.getAuthentication().getUser());
			}
			if(request.getAuthentication().getPassword() == null) {
				retVal.getAuthentication().setPassword(config.getProperty("password"));
			}
			else {
				retVal.getAuthentication().setPassword(request.getAuthentication().getPassword());
			}
		}
		
		
		if(request.getVersion() == null) {
			retVal.setVersion(config.getProperty("version"));
		}
		else {
			retVal.setVersion(request.getVersion());
		}
	
		return retVal;
	}
	
	public LitleBatchFileResponse sendToLitle() throws LitleOnlineException {
		int i = 0;
		for(i = 0; i < litleBatchRequestList.size(); i++){
			litleRequest.getBatchRequests().add(litleBatchRequestList.get(i).getBatchRequest());
		}
		BigInteger numOfBatches = BigInteger.valueOf(litleBatchRequestList.size());
		litleRequest.setNumBatchRequests(numOfBatches);

		try {
			StringWriter sw = new StringWriter();
			marshaller.marshal(litleRequest, sw);
			String xmlRequest = sw.toString();
			
			File file = getFileToWrite("Requests");
			// if file doesnt exists, then create it
			if (!file.exists()) {
				file.createNewFile();
			}
 
			FileWriter fw = new FileWriter(file.getAbsoluteFile());
			BufferedWriter bw = new BufferedWriter(fw);
			bw.write(xmlRequest);
			bw.close();
			
		} catch(JAXBException ume) {
			throw new LitleOnlineException("Error validating xml data against the schema", ume);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
		}
		
		LitleBatchFileResponse retObj = new LitleBatchFileResponse(null);
		return retObj;
	}
	
	public File getFileToWrite(String subFolderName) {
		java.util.Date date= new java.util.Date();
		String fileName = "fileToPass"+ date.getTime()+".xml";
		File fileToReturn = new File(System.getProperty("user.home") + File.separator + subFolderName + File.separator + fileName);
		if(System.getProperty("java.specification.version").equals("1.4")) {
			if(System.getProperty("LITLE_BATCH_DIR") != null) {
				fileToReturn = new File(System.getProperty("LITLE_BATCH_DIR") + File.separator + subFolderName + File.separator + fileName);
			}
		}
		else {
			if(System.getenv("LITLE_BATCH_DIR") != null) {
				fileToReturn = new File(System.getenv("LITLE_BATCH_DIR") + File.separator + subFolderName + File.separator + fileName);
			}
		}
		
		if(!fileToReturn.getParentFile().exists()){
			fileToReturn.getParentFile().mkdir();
		}
		
		return fileToReturn;
	}

	private void fillInReportGroup(TransactionTypeWithReportGroup txn) {
		if(txn.getReportGroup() == null) {
			txn.setReportGroup(config.getProperty("reportGroup")); 
		}
	}
	
	private void fillInReportGroup(TransactionTypeWithReportGroupAndPartial txn) {
		if(txn.getReportGroup() == null) {
			txn.setReportGroup(config.getProperty("reportGroup")); 
		}
	}

}
