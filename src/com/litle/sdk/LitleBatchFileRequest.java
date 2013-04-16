package com.litle.sdk;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStream;

import java.io.StringWriter;
import java.math.BigInteger;


import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import com.litle.sdk.generate.Authentication;
import com.litle.sdk.generate.LitleOnlineRequest;
import com.litle.sdk.generate.LitleRequest;

public class LitleBatchFileRequest {

	private JAXBContext jc;
	private Properties config;
	private Marshaller marshaller;
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
	
	public void intializeMembers(String requestFileName, Properties config){
		try {
			this.jc = JAXBContext.newInstance("com.litle.sdk.generate");
			this.marshaller = jc.createMarshaller();
			
			this.communication = new Communication();
			this.litleRequest = new LitleRequest();
			this.litleBatchRequestList = new ArrayList<LitleBatchRequest>();
			this.requestFileName = requestFileName;
			
			if( config == null || config.isEmpty() ){
				this.config = new Properties();
				this.config.load(new FileInputStream(Configuration.location()));
			} else {
				fillInMissingFieldsFromConfig(config);
				this.config = config;
			}
			Authentication authentication = new Authentication();
			authentication.setPassword(this.config.getProperty("password"));
			authentication.setUser(this.config.getProperty("username"));
			this.litleRequest.setAuthentication(authentication);
			this.litleRequest.setVersion(this.config.getProperty("version"));
			
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
	
	public Properties getConfig(){
		return this.config;
	}

	public LitleBatchRequest createBatch(String merchantId) {
		LitleBatchRequest litleBatchRequest = new LitleBatchRequest(merchantId, this);
		litleBatchRequestList.add(litleBatchRequest);
		return litleBatchRequest;
	}

	public void generateRawFile() {

	}

	public int getMaxAllowedTransactionsPerFile(){
		return this.maxAllowedTransactionsPerFile;
	}
	
	public void fillInMissingFieldsFromConfig(Properties config) {
		Properties localConfig = new Properties();
		try {
			localConfig.load(new FileInputStream(Configuration.location()));
			String[] allProperties = {"username","password","merchantId","url","proxyHost","proxyPort","version","timeout","reportGroup","printxml","batchHost","batchPort","batchTcpTimeout","batchUseSSL","maxAllowedTransactionsPerFile","maxTransactionsPerBatch"};
			for(String prop : allProperties){
				if(config.getProperty(prop) == null) {
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

	public LitleBatchFileResponse sendToLitle() throws LitleBatchException {
		long countOfBatches = this.litleBatchRequestList.size();
		BigInteger numOfBatches = BigInteger.valueOf(countOfBatches);
		litleRequest.setNumBatchRequests(numOfBatches);
		for(LitleBatchRequest lbr : this.litleBatchRequestList) {
			this.litleRequest.getBatchRequests().add(lbr.getBatchRequest());
		}
		
		File file = getFileToWrite("Request");
		try {
//			Code to write to the file directly 
			OutputStream os = new FileOutputStream(file.getAbsolutePath()); 
			marshaller.marshal(litleRequest, os);
			
			File fileResponse = getFileToWrite("Response");
			
			if (!fileResponse.exists()) {
				fileResponse.createNewFile();
			}
		
			fileResponse = communication.sendLitleBatchFileToIBC(file, fileResponse.getAbsolutePath(), config);
//			FileWriter fwResponse = new FileWriter(fileResponse.getAbsoluteFile());
//			BufferedWriter bwResponse = new BufferedWriter(fwResponse);
//			bwResponse.write(xmlResponse);
//			bwResponse.close();
			
			LitleBatchFileResponse retObj = new LitleBatchFileResponse(fileResponse);
			return retObj;
		} catch (JAXBException ume) {
			throw new LitleBatchException(
					"Error validating xml data against the schema", ume);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			throw new LitleBatchException(
					"Error while sending batch", e);
		}
	}

	public File getFileToWrite(String subFolderName) {
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

	public boolean isEmpty() {
		return (getNumberOfTransactionInFile() == 0) ? true : false;
	}
	
	public boolean isFull() {
		return (getNumberOfTransactionInFile() == this.maxAllowedTransactionsPerFile);
	}
	
}
