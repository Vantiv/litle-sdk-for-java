package com.litle.sdk;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.File;
import java.io.IOException;
import java.util.Properties;

import org.junit.Before;
import org.junit.Test;

import com.litle.sdk.generate.RFRRequest;

public class TestLitleRFRFileRequest {

    private static LitleRFRFileRequest litleRFRFileRequest;
    RFRRequest rfr;

    @Before
    public void before() throws Exception {
        Properties property = new Properties();
        property.setProperty("username", "PHXMLTEST");
        property.setProperty("password", "password");
        property.setProperty("version", "8.18");
        property.setProperty("maxAllowedTransactionsPerFile", "1000");
        property.setProperty("maxTransactionsPerBatch", "500");
        property.setProperty("batchHost", "localhost");
        property.setProperty("batchPort", "2104");
        property.setProperty("batchTcpTimeout", "10000");
        property.setProperty("batchUseSSL", "false");
        property.setProperty("merchantId", "101");
        property.setProperty("proxyHost", "");
        property.setProperty("proxyPort", "");
        property.setProperty("reportGroup", "test");
        property.setProperty("batchRequestFolder", "test/unit/requestFolder/");
        property.setProperty("batchResponseFolder", "test/unit/responseFolder/");
        rfr = new RFRRequest();
        rfr.setLitleSessionId(8675309999L);
        litleRFRFileRequest = new LitleRFRFileRequest("testFile.xml", rfr, property);
    }

    @Test
    public void testInitializeMembers() throws Exception {
        Properties configToPass = new Properties();

        configToPass.setProperty("username", "usr1");
        configToPass.setProperty("password", "pass");

        litleRFRFileRequest.initializeMembers("testFile.xml", configToPass);

        assertEquals(litleRFRFileRequest.getConfig().getProperty("username"), "usr1");
        assertEquals(litleRFRFileRequest.getConfig().getProperty("password"), "pass");
    }

    @Test
    public void testInitializeMembers_withoutPropertiesOverridden() throws Exception {
        Configuration mockedConfig = mock(Configuration.class);

        File fileToReturn = new File("test/unit/testProperties/testProperties.txt");
        when(mockedConfig.location()).thenReturn(fileToReturn);

    	LitleRFRFileRequest lbfr = new LitleRFRFileRequest("testFile.xml", rfr, mockedConfig);
        boolean hadException = false;

        try{
            lbfr.initializeMembers("testFile.xml", null);
        } catch (Exception e){
            hadException = true;
        }

        assertTrue(!hadException);
    }

    @Test
    public void testSendToLitleStream() throws IOException {

        File fileToBeWritten = litleRFRFileRequest.getFileToWrite("batchRequestFolder");
        // make sure the file doesn't exist before running sendToLitle.
        if(fileToBeWritten.exists()) {
            assertTrue(fileToBeWritten.delete());
        }
        assertTrue(!fileToBeWritten.exists());

        Communication mockedCommunication = mock(Communication.class);
        doNothing().when(mockedCommunication).sendLitleBatchFileToIBC(any(File.class), any(File.class), any(Properties.class));

        File responseFile = new File("test/unit/responseFolder/testFile.xml");

        litleRFRFileRequest.setResponseFile(responseFile);
        litleRFRFileRequest.setCommunication(mockedCommunication);

        litleRFRFileRequest.sendToLitleStream();
        verify(mockedCommunication).sendLitleBatchFileToIBC(any(File.class), any(File.class), any(Properties.class));
    }


    @Test
    public void testSendToLitleSFTP() throws IOException {
        File fileToBeWritten = litleRFRFileRequest.getFileToWrite("batchRequestFolder");
        // make sure the file doesn't exist before running sendToLitle.
        if(fileToBeWritten.exists()) {
            assertTrue(fileToBeWritten.delete());
        }
        assertTrue(!fileToBeWritten.exists());

        Communication mockedCommunication = mock(Communication.class);
        doNothing().when(mockedCommunication).sendLitleBatchFileToIBC(any(File.class), any(File.class), any(Properties.class));

        File responseFile = new File("test/unit/responseFolder/testFile.xml");

        litleRFRFileRequest.setResponseFile(responseFile);
        litleRFRFileRequest.setCommunication(mockedCommunication);

        litleRFRFileRequest.sendToLitleSFTP();
        verify(mockedCommunication).sendLitleRequestFileToSFTP(any(File.class), any(Properties.class));
        verify(mockedCommunication).receiveLitleRequestResponseFileFromSFTP(any(File.class), any(File.class), any(Properties.class));
    }



}