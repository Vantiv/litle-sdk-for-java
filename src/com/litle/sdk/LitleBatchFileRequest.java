package com.litle.sdk;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
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
import com.litle.sdk.generate.LitleRequest;

public class LitleBatchFileRequest{

	private JAXBContext jc;
	private Properties properties;
	private Communication communication;
	private List<LitleBatchRequest> litleBatchRequestList;
	private String requestFileName;
	private File requestFile;
	private File responseFile;
	private File tempBatchRequestFile;
	//private File tempLitleRequestFile;
	private String requestId;
	private Marshaller marshaller;
	private Configuration config = null;

	protected int maxAllowedTransactionsPerFile;

	/**
	 * Recommend NOT to change this value.
	 */
	protected final int litleLimit_maxAllowedTransactionsPerFile = 500000;

	/**
	 * Construct a LitleBatchFileRequest using the configuration specified in location specified by requestFileName
	 */
	public LitleBatchFileRequest(String requestFileName) {
		intializeMembers(requestFileName);
	}

	/**
	 * Construct a LitleBatchFileRequest specifying the file name for the
	 * request (ex: filename: TestFile.xml the extension should be provided if
	 * the file has to generated in certain format like xml or txt etc) and
	 * configuration in code. This should be used by integrations that have
	 * another way to specify their configuration settings (ofbiz, etc)
	 *
	 * Properties that *must* be set are:
	 *
	 * batchHost (eg https://payments.litle.com)
	 * batchPort (eg 8080) username
	 * merchantId
	 * password
	 * batchTcpTimeout (in seconds)
	 * batchUseSSL
	 * BatchRequestPath folder - specify the absolute path
	 * BatchResponsePath folder - specify the absolute path
	 * sftpUsername
	 * sftpPassword
	 * Optional properties
	 * are:
	 * proxyHost
	 * proxyPort
	 * printxml (possible values "true" and "false" defaults to false)
	 *
	 * @param RequestFileName
	 *            , config
	 */
	public LitleBatchFileRequest(String requestFileName, Properties properties) {
		intializeMembers(requestFileName, properties);
	}

	/**
	 * This constructor is primarily here for test purposes only.
	 * @param requestFileName
	 * @param config
	 */
	public LitleBatchFileRequest(String requestFileName, Configuration config) {
		this.config = config;
		intializeMembers(requestFileName, null);
	}

	private void intializeMembers(String requestFileName) {
		intializeMembers(requestFileName, null);
	}

	public void intializeMembers(String requestFileName, Properties in_properties) throws LitleBatchException{
		try {
			this.jc = JAXBContext.newInstance("com.litle.sdk.generate");
			if(config == null){
				config = new Configuration();
			}
			this.communication = new Communication();
			this.litleBatchRequestList = new ArrayList<LitleBatchRequest>();
			this.requestFileName = requestFileName;
			marshaller = jc.createMarshaller();
			// JAXB_FRAGMENT property required to prevent unnecessary XML info from being printed in the file during marshal.
			marshaller.setProperty(Marshaller.JAXB_FRAGMENT, true);
			// Proper formatting of XML purely for aesthetic purposes.
			marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);

			if (in_properties == null || in_properties.isEmpty()) {
				this.properties = new Properties();
				this.properties.load(new FileInputStream(config.location()));
			} else {
				fillInMissingFieldsFromConfig(in_properties);
				this.properties = in_properties;
			}

			this.maxAllowedTransactionsPerFile = Integer.parseInt(properties.getProperty("maxAllowedTransactionsPerFile", "1000"));
			if (maxAllowedTransactionsPerFile > litleLimit_maxAllowedTransactionsPerFile) {
				throw new LitleBatchException("maxAllowedTransactionsPerFile property value cannot exceed "
								+ String.valueOf(litleLimit_maxAllowedTransactionsPerFile));
			}

			responseFile = getFileToWrite("batchResponseFolder");

		} catch (FileNotFoundException e) {
			throw new LitleBatchException(
					"Configuration file not found." +
							" If you are not using the .litle_SDK_config.properties file," +
							" please use the " + LitleBatchFileRequest.class.getSimpleName() + "(String, Properties) constructor." +
							" If you are using .litle_SDK_config.properties, you can generate one using java -jar litle-sdk-for-java-x.xx.jar", e);
		} catch (IOException e) {
			throw new LitleBatchException(
					"Configuration file could not be loaded.  Check to see if the current user has permission to access the file", e);
		} catch (JAXBException e) {
			throw new LitleBatchException(
					"Unable to load jaxb dependencies.  Perhaps a classpath issue?", e);
		}
	}

	protected void setCommunication(Communication communication) {
		this.communication = communication;
	}

	Properties getConfig() {
		return this.properties;
	}

	/**
	 * Returns a LitleBatchRequest object, the container for transactions.
	 * @param merchantId
	 * @return
	 * @throws LitleBatchException
	 */
	public LitleBatchRequest createBatch(String merchantId)
			throws LitleBatchException {
		LitleBatchRequest litleBatchRequest = new LitleBatchRequest(merchantId, this);
		litleBatchRequestList.add(litleBatchRequest);
		return litleBatchRequest;
	}

	/**
	 * This method generates the request file alone. To generate the response
	 * object call sendToLitle method.
	 *
	 * @throws LitleBatchException
	 * @throws JAXBException
	 */
	public void generateRequestFile() throws LitleBatchException {
		try {
			LitleRequest litleRequest = buildLitleRequest();

			// Code to write to the file directly
			File localFile = getFileToWrite("batchRequestFolder");
			StringWriter sw = new StringWriter();
			Marshaller marshaller;
			try {
				marshaller = jc.createMarshaller();
				marshaller.marshal(litleRequest, sw);
			} catch (JAXBException e) {
				throw new LitleBatchException("Unable to load jaxb dependencies.  Perhaps a classpath issue?");
			}
			String xmlRequest = sw.toString();

			xmlRequest = xmlRequest.replace("</litleRequest>", " ");

			OutputStream litleReqWriter = new FileOutputStream(localFile);
			FileInputStream fis = new FileInputStream(tempBatchRequestFile);
			byte[] readData = new byte[1024];
			litleReqWriter.write(xmlRequest.getBytes());
			int i = fis.read(readData);

			while (i != -1) {
				litleReqWriter.write(readData, 0, i);
				i = fis.read(readData);
			}
			litleReqWriter.write(("</litleRequest>\n").getBytes());
			//marshaller.marshal(litleRequest, os);
			requestFile = localFile;
			fis.close();
			tempBatchRequestFile.delete();
			litleReqWriter.close();
		} catch (IOException e) {
			throw new LitleBatchException("Error while creating a batch request file. Check to see if the current user has permission to read and write to " + this.properties.getProperty("batchRequestFolder"), e);
		}

	}

	public File getFile() {
		return requestFile;
	}

	public int getMaxAllowedTransactionsPerFile() {
		return this.maxAllowedTransactionsPerFile;
	}

	void fillInMissingFieldsFromConfig(Properties config) throws LitleBatchException{
		Properties localConfig = new Properties();
		boolean propertiesReadFromFile = false;
		try {
			String[] allProperties = { "username", "password", "proxyHost",
					"proxyPort", "batchHost", "batchPort",
					"batchTcpTimeout", "batchUseSSL",
					"maxAllowedTransactionsPerFile", "maxTransactionsPerBatch",
					"batchRequestFolder", "batchResponseFolder", "sftpUsername", "sftpPassword"};

			for (String prop : allProperties) {
				// if the value of a property is not set, look at the Properties member of the class first, and the .properties file next.
				if (config.getProperty(prop) == null) {
					if ( this.properties != null && this.properties.get(prop) != null ){
						config.setProperty(prop, this.properties.getProperty(prop));
					}
					else{
						if (!propertiesReadFromFile) {
							localConfig.load(new FileInputStream((new Configuration()).location()));
							propertiesReadFromFile = true;
						}
						config.setProperty(prop, localConfig.getProperty(prop));
					}
				}
			}
		} catch (FileNotFoundException e) {
			throw new LitleBatchException("File .litle_SDK_config.properties was not found. Please run the Setup.java application to create the file at location "+ (new Configuration()).location(), e);
		} catch (IOException e) {
			throw new LitleBatchException("There was an exception while reading the .litle_SDK_config.properties file.", e);
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
			totalNumberOfTransactions += lbr.getNumberOfTransactions();
		}
		return totalNumberOfTransactions;
	}

	/**
	 * This method generates the request and response file and the objects to access the
	 * transaction responses.
	 *
	 * @throws LitleBatchException
	 */
	public LitleBatchFileResponse sendToLitle() throws LitleBatchException {
	    return sendToLitleStream();
	}

	/**
	 * Sends the file to Litle over a TCP socket, the less favorable method of sending batches to Litle.
	 * @return A response object for the batch file
	 * @throws LitleBatchException
	 */
	public LitleBatchFileResponse sendToLitleStream() throws LitleBatchException{
	    try {
            prepareForDelivery();

            communication.sendLitleBatchFileToIBC(requestFile, responseFile, properties);
            LitleBatchFileResponse retObj = new LitleBatchFileResponse(responseFile);
            return retObj;

        } catch (IOException e) {
            throw new LitleBatchException("There was an exception while creating the Litle Request file. Check to see if the current user has permission to read and write to " + this.properties.getProperty("batchRequestFolder"), e);
        }
	}

	/**
	 * Sends the file to Litle over sFTP, the preferred method of sending batches to Litle.
	 * @return A response object for the batch file
	 * @throws LitleBatchException
	 */
	public LitleBatchFileResponse sendToLitleSFTP() throws LitleBatchException{
	    try {
	        prepareForDelivery();
            communication.sendLitleRequestFileToSFTP(requestFile, properties);
            communication.receiveLitleRequestResponseFileFromSFTP(requestFile, responseFile, properties);

            LitleBatchFileResponse retObj = new LitleBatchFileResponse(responseFile);
            return retObj;
        } catch (IOException e) {
            throw new LitleBatchException("There was an exception while creating the Litle Request file. Check to see if the current user has permission to read and write to " + this.properties.getProperty("batchRequestFolder"), e);
        }
	}

	/**
	 * Only sends the file to Litle after sFTP. This method requires separate invocation of the retrieve method.
	 * @throws LitleBatchException
	 */
	public void sendOnlyToLitleSFTP() throws LitleBatchException{
        try {
            prepareForDelivery();
            communication.sendLitleRequestFileToSFTP(requestFile, properties);
        } catch (IOException e) {
            throw new LitleBatchException("There was an exception while creating the Litle Request file. Check to see if the current user has permission to read and write to " + this.properties.getProperty("batchRequestFolder"), e);
        }
    }

	/**
	 * Only retrieves the file from Litle over sFTP. This method requires separate invocation of the send method.
	 * @return A response object for the file
	 * @throws LitleBatchException
	 */
	public LitleBatchFileResponse retrieveOnlyFromLitleSFTP() throws LitleBatchException{
        try {
            communication.receiveLitleRequestResponseFileFromSFTP(requestFile, responseFile, properties);
            LitleBatchFileResponse retObj = new LitleBatchFileResponse(responseFile);
            return retObj;
        } catch (IOException e) {
            throw new LitleBatchException("There was an exception while creating the Litle Request file. Check to see if the current user has permission to read and write to " + this.properties.getProperty("batchRequestFolder"), e);
        }
    }


	private void prepareForDelivery() {
        try {
            String writeFolderPath = this.properties.getProperty("batchRequestFolder");

            tempBatchRequestFile = new File(writeFolderPath + "/tmp/tempBatchFileTesting");
            OutputStream batchReqWriter = new FileOutputStream(tempBatchRequestFile.getAbsoluteFile());
            // close the all the batch files
            byte[] readData = new byte[1024];
            for (LitleBatchRequest batchReq : litleBatchRequestList) {
                batchReq.closeFile();
                StringWriter sw = new StringWriter();
                marshaller.marshal(batchReq.getBatchRequest(), sw);
                String xmlRequest = sw.toString();

                xmlRequest = xmlRequest.replaceFirst("/>", ">");

                FileInputStream fis = new FileInputStream(batchReq.getFile());

                batchReqWriter.write(xmlRequest.getBytes());
                int i = fis.read(readData);

                while (i != -1) {
                    batchReqWriter.write(readData, 0, i);
                    i = fis.read(readData);
                }

                batchReqWriter.write(("</batchRequest>\n").getBytes());
                fis.close();
                batchReq.getFile().delete();
            }
            // close the file
            batchReqWriter.close();
            generateRequestFile();
            File tmpFile = new File(writeFolderPath + "/tmp");
            if (tmpFile.exists()) {
                tmpFile.delete();
            }
        } catch (JAXBException e) {
            throw new LitleBatchException(
                    "There was an exception while marshalling BatchRequest or LitleRequest objects.", e);
        } catch (IOException e) {
            throw new LitleBatchException(
                    "There was an exception while creating the Litle Request file. Check to see if the current user has permission to read and write to "
                            + this.properties.getProperty("batchRequestFolder"), e);
        }
    }

	void setResponseFile(File inFile) {
		this.responseFile = inFile;
	}

	void setId(String id) {
		this.requestId = id;
	}

	/**
	 * This method initializes the high level properties for the XML(ex:
	 * initializes the user name and password for the presenter)
	 *
	 * @return
	 */
	private LitleRequest buildLitleRequest() {
		Authentication authentication = new Authentication();
		authentication.setPassword(this.properties.getProperty("password"));
		authentication.setUser(this.properties.getProperty("username"));
		LitleRequest litleRequest = new LitleRequest();

		if(requestId != null && requestId.length() != 0) {
			litleRequest.setId(requestId);
		}
		litleRequest.setAuthentication(authentication);
		litleRequest.setVersion(Versions.XML_VERSION);
		BigInteger numOfBatches = BigInteger.valueOf(this.litleBatchRequestList.size());
		litleRequest.setNumBatchRequests(numOfBatches);
		return litleRequest;
	}

	/**
	 * This method gets the folder path of either the request or reposne.
	 *
	 * @param locationKey
	 * @return
	 */
	File getFileToWrite(String locationKey) {
		String fileName = this.requestFileName;
		String writeFolderPath = this.properties.getProperty(locationKey);
		File fileToReturn = new File(writeFolderPath + File.separator
				+ fileName);

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