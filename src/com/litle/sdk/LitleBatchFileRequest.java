package com.litle.sdk;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;

import com.litle.sdk.generate.Authentication;
import com.litle.sdk.generate.LitleRequest;

public class LitleBatchFileRequest {

	private JAXBContext jc;
	private Properties properties;
	private Communication communication;
	private List<LitleBatchRequest> litleBatchRequestList;
	private String requestFileName;
	private File requestFile;
	private File responseFile;
	private String requestId;
	
	protected int maxAllowedTransactionsPerFile;
	
	/**
	 * Recommend NOT to change this value. 
	 */
	protected final int litleLimit_maxAllowedTransactionsPerFile = 500000;

	/**
	 * Construct a LitleBatchFileRequest using the configuration specified in
	 * $HOME/.litle_SDK_config.properties
	 */
	public LitleBatchFileRequest(String requestFileName) {
		intializeMembers(requestFileName);
	}
	
	/**
	 * Construct a LitleBatchFileRequest specifying the file name for the request (ex: filename: TestFile.xml
	 * the extension should be provided if the file has to generated in certain format like xml or txt etc) and 
	 * configuration in code. This should
	 * be used by integrations that have another way to specify their
	 * configuration settings (ofbiz, etc)
	 * 
	 * Properties that *must* be set are:
	 * 
	 * batchHost (eg https://payments.litle.com) batchPort (eg 8080) 
	 * username merchantId password version (eg
	 * 8.10) batchTcpTimeout (in seconds) batchUseSSL
	 * BatchRequestPath folder - specify the absolute path
	 * BatchResponsePath folder - specify the absolute path
	 * Optional properties are: proxyHost proxyPort
	 * printxml (possible values "true" and "false" - defaults to false)
	 * 
	 * @param RequestFileName, config
	 */
	public LitleBatchFileRequest(String requestFileName, Properties config) {
		intializeMembers(requestFileName, config);
	}
	
	private void intializeMembers(String requestFileName){
		intializeMembers(requestFileName, null);
	}
	
	public void intializeMembers(String requestFileName, Properties config){
		try {
			this.jc = JAXBContext.newInstance("com.litle.sdk.generate");
			
			this.communication = new Communication();
			this.litleBatchRequestList = new ArrayList<LitleBatchRequest>();
			this.requestFileName = requestFileName;
			
			if( config == null || config.isEmpty() ){
				this.properties = new Properties();
				this.properties.load(new FileInputStream(Configuration.location()));
			} else {
				fillInMissingFieldsFromConfig(config);
				this.properties = config;
			}
			
			this.maxAllowedTransactionsPerFile = Integer.parseInt(properties.getProperty("maxAllowedTransactionsPerFile"));
			if( maxAllowedTransactionsPerFile > litleLimit_maxAllowedTransactionsPerFile ){
				throw new LitleBatchException("maxAllowedTransactionsPerFile property value cannot exceed " + String.valueOf(litleLimit_maxAllowedTransactionsPerFile));
			}
			
			responseFile = getFileToWrite("batchResponseFolder");
			
		} catch (FileNotFoundException e) {
			throw new LitleBatchException("Configuration file not found. If you are not using the .litle_SDK_config.properties file, please use the LitleOnline(Properties) constructor.  If you are using .litle_SDK_config.properties, you can generate one using java -jar litle-sdk-for-java-8.10.jar", e);
		} catch (IOException e) {
			throw new LitleBatchException(
					"Configuration file could not be loaded.  Check to see if the user running this has permission to access the file",
					e);
		} catch (JAXBException e) {
			throw new LitleBatchException("Unable to load jaxb dependencies.  Perhaps a classpath issue?", e);
		}
	}

	protected void setCommunication(Communication communication) {
		this.communication = communication;
	}
	
	Properties getConfig(){
		return this.properties;
	}

	public LitleBatchRequest createBatch(String merchantId) {
		LitleBatchRequest litleBatchRequest = new LitleBatchRequest(merchantId, this);
		litleBatchRequestList.add(litleBatchRequest);
		return litleBatchRequest;
	}

	/**
	 * This method generates the response file alone. To generate the response object call
	 * sendToLitle method.
	 * 
	 * @throws LitleBatchException
	 */
	public void generateRawFile() throws LitleBatchException {
		try {
			LitleRequest litleRequest = buildLitleRequest();

			// Code to write to the file directly 
			File localFile = getFileToWrite("batchRequestFolder");
			OutputStream os = new FileOutputStream(localFile.getAbsolutePath());
			Marshaller marshaller = jc.createMarshaller();
			marshaller.marshal(litleRequest, os);
			requestFile = localFile;
			communication.sendLitleBatchFileToIBC(localFile, responseFile, properties);
		}
		catch (JAXBException ume) {
			throw new LitleBatchException(
					"Error validating xml data against the schema", ume);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			throw new LitleBatchException(
					"Error while sending batch", e);
		}
		
	}
	
	public File getFile() {
		return requestFile;
	}

	public int getMaxAllowedTransactionsPerFile(){
		return this.maxAllowedTransactionsPerFile;
	}
	
	void fillInMissingFieldsFromConfig(Properties config) {
		Properties localConfig = new Properties();
		boolean propertiesReadFromFile = false;
		try {
			String[] allProperties = {"username","password","proxyHost","proxyPort","version","batchHost","batchPort","batchTcpTimeout","batchUseSSL","maxAllowedTransactionsPerFile","maxTransactionsPerBatch", "batchRequestFolder", "batchResponseFolder"};
			for(String prop : allProperties){
				if(config.getProperty(prop) == null) {
					if(!propertiesReadFromFile){
						localConfig.load(new FileInputStream(Configuration.location()));
						propertiesReadFromFile = true;
					}
					config.setProperty(prop, localConfig.getProperty(prop));
				}
			}
		} catch (FileNotFoundException e) {
			throw new LitleBatchException("File was not found: " + Configuration.location(), e);
		} catch (IOException e) {
			throw new LitleBatchException("There was an IO exception.", e);
		}
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

	/**
	 * This method generates the response file and the objects to access the transaction responses.
	 * 
	 * @throws LitleBatchException
	 */
	public LitleBatchFileResponse sendToLitle() throws LitleBatchException {
		try {
			generateRawFile();
			LitleBatchFileResponse retObj = new LitleBatchFileResponse(responseFile);
			return retObj;
		} catch (JAXBException e) {
			throw new LitleBatchException("There was a JAXB exception.", e);
		}
	}
	
	void setResponseFile(File inFile){
		this.responseFile = inFile;
	}
	
	void setId(String id){
		this.requestId = id;
	}

	/**
	 * This method initializes the high level properties for the XML(ex: initializes the user name and password for the presenter)
	 * @return
	 */
	private LitleRequest buildLitleRequest() {
		Authentication authentication = new Authentication();
		authentication.setPassword(this.properties.getProperty("password"));
		authentication.setUser(this.properties.getProperty("username"));
		
		LitleRequest litleRequest = new LitleRequest();
		if(requestId == null) {
			if(requestId.length() != 0 ){
				litleRequest.setId(requestId);
			}	
		}
		litleRequest.setAuthentication(authentication);
		litleRequest.setVersion(this.properties.getProperty("version"));
		BigInteger numOfBatches = BigInteger.valueOf(this.litleBatchRequestList.size());
		litleRequest.setNumBatchRequests(numOfBatches);
		for(LitleBatchRequest lbr : this.litleBatchRequestList) {
			litleRequest.getBatchRequests().add(lbr.getBatchRequest());
		}
		return litleRequest;
	}

	/**
	 * This method gets the folder path of either the request or reposne.
	 * @param locationKey
	 * @return
	 */
	File getFileToWrite(String locationKey) {
		String fileName = this.requestFileName;
		String writeFolderPath = this.properties.getProperty(locationKey);
		File fileToReturn = new File(writeFolderPath + File.separator + fileName);
		
		if (!fileToReturn.getParentFile().exists()) {
			fileToReturn.getParentFile().mkdir();
		}

		return fileToReturn;
	}

	public boolean isEmpty() {
		return (getNumberOfTransactionInFile() == 0) ? true : false;
	}
	
	public boolean isFull() {
		return (getNumberOfTransactionInFile() == this.maxAllowedTransactionsPerFile);
	}
	
}
