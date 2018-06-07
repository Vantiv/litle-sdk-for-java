package com.litle.sdk;

import java.io.*;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;

import com.litle.sdk.generate.Authentication;
import com.litle.sdk.generate.LitleRequest;
import org.bouncycastle.openpgp.PGPException;

public class LitleBatchFileRequest {

    private JAXBContext jc;
    private Properties properties;
    private Communication communication;
    private List<LitleBatchRequest> litleBatchRequestList;
    private String requestFileName;
    private File requestFile;
    private File responseFile;
    private File tempBatchRequestFile;
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
        initializeMembers(requestFileName);
    }

    /**
     * Construct a LitleBatchFileRequest specifying the file name for the
     * request (ex: filename: TestFile.xml the extension should be provided if
     * the file has to generated in certain format like xml or txt etc) and
     * configuration in code. This should be used by integrations that have
     * another way to specify their configuration settings (ofbiz, etc)
     * <p>
     * Properties that *must* be set are:
     * <p>
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
     * @param requestFileName
     * @param properties
     */
    public LitleBatchFileRequest(String requestFileName, Properties properties) {
        initializeMembers(requestFileName, properties);
    }

    /**
     * This constructor is primarily here for test purposes only.
     *
     * @param requestFileName
     * @param config
     */
    public LitleBatchFileRequest(String requestFileName, Configuration config) {
        this.config = config;
        initializeMembers(requestFileName, null);
    }

    private void initializeMembers(String requestFileName) {
        initializeMembers(requestFileName, null);
    }

    public void initializeMembers(String requestFileName, Properties in_properties) throws LitleBatchException {
        try {
            this.jc = JAXBContext.newInstance("com.litle.sdk.generate");
            if (config == null) {
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

            requestFile = getFileToWrite("batchRequestFolder");
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

    public Properties getConfig() {
        return this.properties;
    }

    /**
     * Returns a LitleBatchRequest object, the container for transactions.
     *
     * @param merchantId
     * @return LitleBatchFileRequest
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
     */
    public void generateRequestFile() throws LitleBatchException {
        try {
            String litleRequestXml = buildLitleRequestXml();

            litleRequestXml = litleRequestXml.replace("</litleRequest>", " ");

            OutputStream litleReqWriter = new FileOutputStream(requestFile);
            FileInputStream fis = new FileInputStream(tempBatchRequestFile);
            byte[] readData = new byte[1024];
            litleReqWriter.write(litleRequestXml.getBytes());
            int i = fis.read(readData);

            while (i != -1) {
                litleReqWriter.write(readData, 0, i);
                i = fis.read(readData);
            }
            litleReqWriter.write(("</litleRequest>\n").getBytes());
            //marshaller.marshal(litleRequest, os);
            fis.close();
            tempBatchRequestFile.delete();
            litleReqWriter.close();
        } catch (IOException e) {
            throw new LitleBatchException(
                    "Error while creating a batch request file. " +
                            "Check to see if the current user has permission to read and write to "
                            + this.properties.getProperty("batchRequestFolder"), e);
        }
    }


    public File getFile() {
        return requestFile;
    }


    public int getMaxAllowedTransactionsPerFile() {
        return this.maxAllowedTransactionsPerFile;
    }


    void fillInMissingFieldsFromConfig(Properties config) throws LitleBatchException {
        Properties localConfig = new Properties();
        boolean propertiesReadFromFile = false;
        try {
            String[] allProperties = {"username", "password", "proxyHost",
                    "proxyPort", "batchHost", "batchPort",
                    "batchTcpTimeout", "batchUseSSL",
                    "maxAllowedTransactionsPerFile", "maxTransactionsPerBatch",
                    "batchRequestFolder", "batchResponseFolder", "sftpUsername", "sftpPassword", "sftpTimeout",
                    "merchantId", "printxml", "useEncryption", "VantivPublicKeyPath", "PrivateKeyPath", "MerchantPublicKeyPath", "gpgPassphrase", "deleteBatchFiles"};

            for (String prop : allProperties) {
                // if the value of a property is not set,
                // look at the Properties member of the class first, and the .properties file next.
                if (config.getProperty(prop) == null) {
                    if (this.properties != null && this.properties.get(prop) != null) {
                        config.setProperty(prop, this.properties.getProperty(prop));
                    } else {
                        if (!propertiesReadFromFile) {
                            localConfig.load(new FileInputStream((new Configuration()).location()));
                            propertiesReadFromFile = true;
                        }
                        if (localConfig.getProperty(prop) != null) {
                            config.setProperty(prop, localConfig.getProperty(prop));
                        }
                    }
                }
            }
        } catch (FileNotFoundException e) {
            throw new LitleBatchException("File .litle_SDK_config.properties was not found. " +
                    "Please run the Setup.java application to create the file at location " +
                    (new Configuration()).location(), e);
        } catch (IOException e) {
            throw new LitleBatchException(
                    "There was an exception while reading the .litle_SDK_config.properties file.", e);
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
     *
     * @return A response object for the batch file
     * @throws LitleBatchException
     */
    public LitleBatchFileResponse sendToLitleStream() throws LitleBatchException {
        try {
            prepareForDelivery();

            communication.sendLitleBatchFileToIBC(requestFile, responseFile, properties);
            LitleBatchFileResponse retObj = new LitleBatchFileResponse(responseFile);
            return retObj;

        } catch (IOException e) {
            throw new LitleBatchException("There was an exception while creating the Litle Request file. " +
                    "Check to see if the current user has permission to read and write to " +
                    this.properties.getProperty("batchRequestFolder"), e);
        }
    }

    /**
     * Sends the file to Litle over sFTP, the preferred method of sending batches to Litle.
     *
     * @return A response object for the batch file
     * @throws LitleBatchException
     */
    public LitleBatchFileResponse sendToLitleSFTP() throws LitleBatchException {
        return sendToLitleSFTP(false);
    }

    /**
     * Sends the file to Litle over sFTP, the preferred method of sending batches to Litle.
     *
     * @param useExistingFile If the batch file was prepared in an earlier step, this method
     *                        can be told to use the existing file.
     * @return A response object for the batch file
     * @throws LitleBatchException
     */
    public LitleBatchFileResponse sendToLitleSFTP(boolean useExistingFile) throws LitleBatchException {

        sendOnlyToLitleSFTP(useExistingFile);

        LitleBatchFileResponse retObj = retrieveOnlyFromLitleSFTP();

        return retObj;
    }

    /**
     * Only sends the file to Litle after sFTP. This method requires separate invocation of the retrieve method.
     *
     * @throws LitleBatchException
     */
    public void sendOnlyToLitleSFTP() throws LitleBatchException {
        sendOnlyToLitleSFTP(false);
    }

    /**
     * Only sends the file to Litle after sFTP. This method requires separate invocation of the retrieve method.
     *
     * @param useExistingFile If the batch file was prepared in an earlier step, this method
     *                        can be told to use the existing file.
     * @throws LitleBatchException
     */
    public void sendOnlyToLitleSFTP(boolean useExistingFile) throws LitleBatchException {
        try {
            if (useExistingFile != true) {
                prepareForDelivery();
            }

            communication.sendLitleRequestFileToSFTP(requestFile, properties);

            checkDeleteBatchRequestFiles();

        } catch (IOException e) {
            throw new LitleBatchException("There was an exception while creating the Litle Request file. " +
                    "Check to see if the current user has permission to read and write to " +
                    this.properties.getProperty("batchRequestFolder"), e);
        }
    }


    private void checkDeleteBatchRequestFiles() {
        boolean deleteBatchFiles = "true".equalsIgnoreCase(properties.getProperty("deleteBatchFiles"));

        if (deleteBatchFiles) {
            requestFile.delete();
        }
    }

    /**
     * Only retrieves the file from Litle over sFTP. This method requires separate invocation of the send method.
     *
     * @return A response object for the file
     * @throws LitleBatchException
     */
    public LitleBatchFileResponse retrieveOnlyFromLitleSFTP() throws LitleBatchException {
        try {

            boolean useEncryption = "true".equalsIgnoreCase(properties.getProperty("useEncryption"));

            File requestFileToFetch = requestFile;
            File responseFileToRecieve = responseFile;

            if (useEncryption) {
                requestFileToFetch = new File(requestFile.getAbsolutePath());
                responseFileToRecieve = new File(responseFile.getAbsolutePath() + ".encrypted");
            }

            communication.receiveLitleRequestResponseFileFromSFTP(requestFileToFetch, responseFileToRecieve, properties);

            if (useEncryption) {
                decryptResponseFile();
            }

            LitleBatchFileResponse retObj = new LitleBatchFileResponse(responseFile);

            checkDeleteBatchResponseFiles(responseFileToRecieve);

            return retObj;
        } catch (IOException e) {
            throw new LitleBatchException("There was an exception while creating the Litle Request file. " +
                    "Check to see if the current user has permission to read and write to " +
                    this.properties.getProperty("batchRequestFolder"), e);
        }
    }

    private void decryptResponseFile() {
        String encResponseFilename = responseFile.getAbsolutePath() + ".encrypted";
        String passwd = properties.getProperty("gpgPassphrase");
        String privateKeyPath = properties.getProperty("PrivateKeyPath");
        try {
            PgpHelper.decrypt(encResponseFilename, responseFile.getAbsolutePath(), privateKeyPath, passwd);
        } catch (PGPException pgpe) {
            throw new LitleBatchException("Error while decrypting response file. Check if " + privateKeyPath + " contains correct private key." +
                    "and that the gpgPassphrase provided in config file is correct.", pgpe);
        } catch (IOException ioe) {
            throw new LitleBatchException("Error in decrypting response file. Check to see if the current user has permission to read and write to" +
                    this.properties.getProperty("batchRequestFolder") + "." +
                    "Also check if " + privateKeyPath + " contains the private key.");
        }
    }


    private void checkDeleteBatchResponseFiles(File fileToBeDeleted) {
        boolean deleteBatchFiles = "true".equalsIgnoreCase(properties.getProperty("deleteBatchFiles"));
        if (deleteBatchFiles) {
            responseFile.delete();
            fileToBeDeleted.delete();
        }
    }

    /**
     * Prepare final batch request file to be submitted in batch request folder.
     */
    public void prepareForDelivery() {
        if ("true".equalsIgnoreCase(properties.getProperty("useEncryption"))) {
            prepareForEncryptedDelivery();
        }
        else {
            try {
                String writeFolderPath = this.properties.getProperty("batchRequestFolder");

                tempBatchRequestFile = new File(writeFolderPath + "/tmp/tempBatchFileTesting");
                OutputStream batchReqWriter = new FileOutputStream(tempBatchRequestFile.getAbsoluteFile());
                // close the all the batch files
                byte[] readData = new byte[1024];
                for (LitleBatchRequest batchReq : litleBatchRequestList) {
                    batchReq.closeFile();
                    String batchRequestXml = buildBatchRequestXml(batchReq);
                    batchRequestXml = batchRequestXml.replaceFirst("/>", ">");

                    FileInputStream fis = new FileInputStream(batchReq.getFile());

                    batchReqWriter.write(batchRequestXml.getBytes());
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
            }
            catch (IOException ioe) {
                throw new LitleBatchException(
                        "There was an exception while creating the Litle Request file. " +
                                "Check to see if the current user has permission to read and write to " +
                                this.properties.getProperty("batchRequestFolder"), ioe);
            }
        }
    }


    /**
     * Prepare final encrypted batch request file to be submitted in batch request folder.
     */
    private void prepareForEncryptedDelivery() {

        String privateKeyPath = properties.getProperty("PrivateKeyPath");
        String gpgPassphrase = properties.getProperty("gpgPassphrase");
        String vantivPubKeyPath = properties.getProperty("VantivPublicKeyPath");
        String litleRequestXml = buildLitleRequestXml();
        try {
            litleRequestXml = litleRequestXml.replace("</litleRequest>", " ");
            OutputStream encryptedLitleRequestWriter = PgpHelper.encryptionStream(requestFile.getAbsolutePath(), vantivPubKeyPath);
            encryptedLitleRequestWriter.write(litleRequestXml.getBytes());

            byte[] clearData = new byte[2097152];
            for (LitleBatchRequest batchReq : litleBatchRequestList) {
                batchReq.closeFile();
                String batchRequestXml = buildBatchRequestXml(batchReq);
                batchRequestXml = batchRequestXml.replaceFirst("/>", ">");
                encryptedLitleRequestWriter.write(batchRequestXml.getBytes());
                InputStream decryptionStream = PgpHelper.decryptionStream(batchReq.getFile().getAbsolutePath(),
                        privateKeyPath,
                        gpgPassphrase);
                int len;
                while ((len = decryptionStream.read(clearData)) > 0) {
                    encryptedLitleRequestWriter.write(clearData, 0, len);
                }
                decryptionStream.close();
                encryptedLitleRequestWriter.write("</batchRequest>\n".getBytes());
                batchReq.getFile().delete();
            }
            encryptedLitleRequestWriter.write(("</litleRequest>\n").getBytes());
            encryptedLitleRequestWriter.close();
        }
        catch (IOException e) {
            throw new LitleBatchException(
                    "There was an exception while creating the Litle Request file. " +
                            "Check to see if the current user has permission to read and write to " +
                            this.properties.getProperty("batchRequestFolder"), e);
        }
        catch (PGPException pgpe) {
            throw new LitleBatchException("Error in creating encrypted request file. Check if " + privateKeyPath + " contains correct private key." +
                    "and that the gpgPassphrase provided in config file is correct." +
                    "\nAlso check if " + vantivPubKeyPath + " contains correct public key.", pgpe);
        }
    }


    /**
     *
     * @param batchRequest
     * @return BatchRequest header xml containing the summary of the transactions within the  given batch request.
     */
    private String buildBatchRequestXml(LitleBatchRequest batchRequest) {
        try {
            StringWriter sw = new StringWriter();
            marshaller.marshal(batchRequest.getBatchRequest(), sw);
            return sw.toString();
        } catch (JAXBException e) {
            throw new LitleBatchException(
                    "There was an exception while marshalling BatchRequest or LitleRequest objects.", e);
        }
    }

    void setResponseFile(File inFile) {
        this.responseFile = inFile;
    }

    void setId(String id) {
        this.requestId = id;
    }


    /**
     *
     * @return LitleRequest xml header containing authentication information for the presenter.
     */
    private String buildLitleRequestXml() {
        LitleRequest litleRequest = buildLitleRequest();

        // Code to write to the file directly
        StringWriter sw = new StringWriter();
        Marshaller marshaller;
        try {
            marshaller = jc.createMarshaller();
            marshaller.marshal(litleRequest, sw);
        } catch (JAXBException e) {
            throw new LitleBatchException("Unable to load jaxb dependencies.  Perhaps a classpath issue?");
        }
        return sw.toString();
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

        if (requestId != null && requestId.length() != 0) {
            litleRequest.setId(requestId);
        }
        litleRequest.setAuthentication(authentication);
        litleRequest.setVersion(Versions.XML_VERSION);
        BigInteger numOfBatches = BigInteger.valueOf(this.litleBatchRequestList.size());
        litleRequest.setNumBatchRequests(numOfBatches);
        return litleRequest;
    }

    /**
     * This method gets the file of either the request or response. It will also
     * make sure that the folder structure where the file lives will be there.
     *
     * @param locationKey Key to use to get the path to the folder.
     * @return File ready to be written to.
     */
    File getFileToWrite(String locationKey) {
        String fileName = this.requestFileName;
        String writeFolderPath = this.properties.getProperty(locationKey);
        File fileToReturn = new File(writeFolderPath, fileName);

        if (!fileToReturn.getParentFile().exists()) {
            fileToReturn.getParentFile().mkdirs();
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