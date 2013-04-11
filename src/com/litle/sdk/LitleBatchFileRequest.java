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
	
	protected final int maxAllowedTransactionsPerFile;

	/**
	 * Construct a LitleBatchFileRequest using the configuration specified in
	 * $HOME/.litle_SDK_config.properties
	 */
	public LitleBatchFileRequest(String requestFileName) {
		intializeMembers(requestFileName);
		// initializeMembers will initialize this.config
		this.maxAllowedTransactionsPerFile = Integer.parseInt(config.getProperty("maxAllowedTransactionsPerFile"));
	}
	
	/**
	 * Construct a LitleBatchFileRequest specifying the configuration in code. This should
	 * be used by integrations that have another way to specify their
	 * configuration settings (ofbiz, etc)
	 * 
	 * Properties that *must* be set are:
	 * 
	 * batchHost (eg https://payments.litle.com) batchPort (eg 8080) 
	 * username merchantId password version (eg
	 * 8.10) batchTcpTimeout (in seconds) batchUseSSL
	 * Optional properties are: proxyHost proxyPort
	 * printxml (possible values "true" and "false" - defaults to false)
	 * 
	 * @param config
	 */
	public LitleBatchFileRequest(String requestFileName, Properties config) {
		intializeMembers(requestFileName, config);
		// initializeMembers will initialize this.config
		this.maxAllowedTransactionsPerFile = Integer.parseInt(config.getProperty("maxAllowedTransactionsPerFile"));
		
	}
	
	private void intializeMembers(String requestFileName){
		intializeMembers(requestFileName, null);
	}
	
	private void intializeMembers(String requestFileName, Properties config){
		try {
			this.jc = JAXBContext.newInstance("com.litle.sdk.generate");
			this.marshaller = jc.createMarshaller();
			this.unmarshaller = jc.createUnmarshaller();
			this.communication = new Communication();
			this.objectFactory = new ObjectFactory();
			this.litleRequest = new LitleRequest();
			this.litleBatchRequestList = new ArrayList<LitleBatchRequest>();
			this.requestFileName = requestFileName;
			
//			if (!(requestFileName.equals(null))) {
//				this.requestFileName = requestFileName;
//			} else {
//				throw new LitleBatchException("You need to supply a filename for the file.");
//			}

			if( config == null || config.isEmpty() ){
				this.config = new Properties();
				this.config.load(new FileInputStream(Configuration.location()));
			} else {
				this.config = config;
			}
			Authentication authentication = new Authentication();
			authentication.setPassword(this.config.getProperty("password"));
			authentication.setUser(this.config.getProperty("username"));
			this.litleRequest.setAuthentication(authentication);
			this.litleRequest.setVersion(config.getProperty("version"));
		} catch (FileNotFoundException e) {
			throw new LitleBatchException("Configuration file not found. If you are not using the .litle_SDK_config.properties file, please use the LitleOnline(Properties) constructor.  If you are using .litle_SDK_config.properties, you can generate one using java -jar litle-sdk-for-java-8.10.jar", e);
		} catch (IOException e) {
			throw new LitleBatchException(
					"Configuration file could not be loaded.  Check to see if the user running this has permission to access the file",
					e);
		} catch (JAXBException e) {
			throw new LitleOnlineException("Unable to load jaxb dependencies.  Perhaps a classpath issue?", e);
		}
	}

	
	protected void setCommunication(Communication communication) {
		this.communication = communication;
	}
	
	public Properties getConfig(){
		return this.config;
	}

	public LitleBatchRequest createBatch(String merchantId) {
		LitleBatchRequest litleBatchRequest = new LitleBatchRequest(merchantId, this);
		litleBatchRequestList.add(litleBatchRequest);
		//this.litleRequest.getBatchRequests().add(litleBatchRequest);
		return litleBatchRequest;
	}

	public void generateRawFile() {

	}

	public int getMaxAllowedTransactionsPerFile(){
		return this.maxAllowedTransactionsPerFile;
	}
	
	//TODO: Fix the merge logic
	private LitleRequest fillInMissingFieldsFromConfig(LitleRequest request) {
		LitleRequest retVal = new LitleRequest();
		retVal.setAuthentication(new Authentication());
		if (request.getAuthentication() == null) {
			Authentication authentication = new Authentication();
			authentication.setPassword(config.getProperty("password"));
			authentication.setUser(config.getProperty("username"));
			retVal.setAuthentication(authentication);
		} else {
			if (request.getAuthentication().getUser() == null) {
				retVal.getAuthentication().setUser(
						config.getProperty("username"));
			} else {
				retVal.getAuthentication().setUser(
						request.getAuthentication().getUser());
			}
			if (request.getAuthentication().getPassword() == null) {
				retVal.getAuthentication().setPassword(
						config.getProperty("password"));
			} else {
				retVal.getAuthentication().setPassword(
						request.getAuthentication().getPassword());
			}
		}

		if (request.getVersion() == null) {
			retVal.setVersion(config.getProperty("version"));
		} else {
			retVal.setVersion(request.getVersion());
		}

		return retVal;
	}

	public int getNumberOfBatches() {
		return this.litleBatchRequestList.size();
	}

	public int getNumberOfTransactionInFile() {
		int i = 0;
		int totalNumberOfTransactions = 0;
		for (i = 0; i < getNumberOfBatches(); i++) {
			LitleBatchRequest lbr = litleBatchRequestList.get(i);
			totalNumberOfTransactions += lbr.getBatchRequest()
					.getTransactions().size();
		}
		return totalNumberOfTransactions;
	}

	public LitleBatchFileResponse sendToLitle() throws Exception {
		long countOfBatches = this.litleBatchRequestList.size();
		BigInteger numOfBatches = BigInteger.valueOf(countOfBatches);
		litleRequest.setNumBatchRequests(numOfBatches);
		for(LitleBatchRequest lbr : this.litleBatchRequestList) {
			this.litleRequest.getBatchRequests().add(lbr.getBatchRequest());
		}
		
		File file = getFileToWrite("Request");
		try {
			StringWriter sw = new StringWriter();
			marshaller.marshal(litleRequest, sw);
			String xmlRequest = sw.toString();

			// if file doesnt exists, then create it
			if (!file.exists()) {
				file.createNewFile();
			}

			FileWriter fw = new FileWriter(file.getAbsoluteFile());
			BufferedWriter bw = new BufferedWriter(fw);
			bw.write(xmlRequest);
			bw.close();
			
			String xmlResponse = communication.sendLitleBatchFileToIBC(file, config);
			
			File fileResponse = getFileToWrite("Response");
			
			if (!fileResponse.exists()) {
				fileResponse.createNewFile();
			}
			
			FileWriter fwResponse = new FileWriter(fileResponse.getAbsoluteFile());
			BufferedWriter bwResponse = new BufferedWriter(fwResponse);
			bwResponse.write(xmlResponse);
			bwResponse.close();
			
			LitleBatchFileResponse retObj = new LitleBatchFileResponse(xmlResponse);
			retObj.convertFileToObject(xmlResponse);
			
			//LitleBatchFileResponse response = (LitleBatchFileResponse)unmarshaller.unmarshal(new StringReader(xmlResponse));
			int abc =0 ;
			abc++;
			
		} catch (JAXBException ume) {
			throw new LitleBatchException(
					"Error validating xml data against the schema", ume);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		//retObj = (LitleBatchFileResponse)unmarshaller.unmarshal(communication.sendLitleBatchFileToIBC(file, "", config));
		LitleBatchFileResponse retObj = new LitleBatchFileResponse("");
		//retObj.convertFileToObject(xmlResponse);
		return retObj;
	}

	public File getFileToWrite(String subFolderName) {
		java.util.Date date = new java.util.Date();
		String fileName = this.requestFileName + ".xml";

		// get the location to write the file in from config
		File fileToReturn = new File(System.getProperty("user.home")
				+ File.separator + subFolderName + File.separator + fileName);
		if (System.getProperty("java.specification.version").equals("1.4")) {
			if (System.getProperty("LITLE_BATCH_DIR") != null) {
				fileToReturn = new File(System.getProperty("LITLE_BATCH_DIR")
						+ File.separator + subFolderName + File.separator
						+ fileName);
			}
		} else {
			if (System.getenv("LITLE_BATCH_DIR") != null) {
				fileToReturn = new File(System.getenv("LITLE_BATCH_DIR")
						+ File.separator + subFolderName + File.separator
						+ fileName);
			}
		}

		if (!fileToReturn.getParentFile().exists()) {
			fileToReturn.getParentFile().mkdir();
		}

		return fileToReturn;
	}

	private boolean isEmpty() {
		return (getNumberOfTransactionInFile() == 0) ? true : false;
	}
	
	private boolean isFull() {
		return (getNumberOfTransactionInFile() == this.maxAllowedTransactionsPerFile);
	}
	
	private void fillInReportGroup(TransactionTypeWithReportGroup txn) {
		if (txn.getReportGroup() == null) {
			txn.setReportGroup(config.getProperty("reportGroup"));
		}
	}

	private void fillInReportGroup(TransactionTypeWithReportGroupAndPartial txn) {
		if (txn.getReportGroup() == null) {
			txn.setReportGroup(config.getProperty("reportGroup"));
		}
	}

}
