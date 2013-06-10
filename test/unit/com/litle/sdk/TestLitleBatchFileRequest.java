package com.litle.sdk;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

import javax.xml.bind.JAXBException;

import org.junit.Before;
import org.junit.Test;

import com.litle.sdk.generate.CardType;
import com.litle.sdk.generate.MethodOfPaymentTypeEnum;
import com.litle.sdk.generate.OrderSourceType;
import com.litle.sdk.generate.Sale;

public class TestLitleBatchFileRequest {

    private static LitleBatchFileRequest litleBatchFileRequest;

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
        litleBatchFileRequest = new LitleBatchFileRequest("testFile.xml", property);
    }

    @Test
    public void testInitializeMembers() throws Exception {
        Properties configToPass = new Properties();

        configToPass.setProperty("username", "usr1");
        configToPass.setProperty("password", "pass");

        litleBatchFileRequest.intializeMembers("testFile.xml", configToPass);

        assertEquals(litleBatchFileRequest.getConfig().getProperty("username"), "usr1");
        assertEquals(litleBatchFileRequest.getConfig().getProperty("password"), "pass");
    }

    @Test
    public void testInitializeMembers_withoutPropertiesOverridden() throws Exception {
        Configuration mockedConfig = mock(Configuration.class);

        File fileToReturn = new File("test/unit/testProperties/testProperties.txt");
        when(mockedConfig.location()).thenReturn(fileToReturn);

    	LitleBatchFileRequest lbfr = new LitleBatchFileRequest("testFile.xml", mockedConfig);
        boolean hadException = false;

        try{
            lbfr.intializeMembers("testFile.xml", null);
        } catch (Exception e){
            hadException = true;
        }

        assertTrue(!hadException);
    }

    @Test
    public void testCreateBatchAndGetNumberOfBatches() throws FileNotFoundException, JAXBException {
        assertEquals(litleBatchFileRequest.getNumberOfBatches(), 0);

        LitleBatchRequest testBatch = litleBatchFileRequest.createBatch("101");
        assertNotNull(testBatch);

        assertEquals(litleBatchFileRequest.getNumberOfBatches(), 1);
    }

    @Test
    public void testGetNumberOfTransactionInFile() throws FileNotFoundException, JAXBException {
        assertEquals(litleBatchFileRequest.getNumberOfTransactionInFile(), 0);

        LitleBatchRequest testBatch = litleBatchFileRequest.createBatch("101");
        testBatch.addTransaction(createTestSale(101L,"101"));
        testBatch.addTransaction(createTestSale(102L,"102"));
        testBatch.addTransaction(createTestSale(103L,"103"));

        LitleBatchRequest testBatch2 = litleBatchFileRequest.createBatch("101");
        testBatch2.addTransaction(createTestSale(104L,"104"));
        testBatch2.addTransaction(createTestSale(105L,"105"));
        testBatch2.addTransaction(createTestSale(106L,"106"));

        assertEquals(litleBatchFileRequest.getNumberOfTransactionInFile(), 6);
    }

    @Test
    public void testSendToLitle() throws IOException {
        File fileToBeWritten = litleBatchFileRequest.getFileToWrite("batchRequestFolder");
        // make sure the file doesn't exist before running sendToLitle.
        if(fileToBeWritten.exists()) {
            assertTrue(fileToBeWritten.delete());
        }
        assertTrue(!fileToBeWritten.exists());

        Communication mockedCommunication = mock(Communication.class);
        doNothing().when(mockedCommunication).sendLitleBatchFileToIBC(any(File.class), any(File.class), any(Properties.class));

        File responseFile = new File("test/unit/responseFolder/testFile.xml");

        litleBatchFileRequest.setResponseFile(responseFile);
        litleBatchFileRequest.setCommunication(mockedCommunication);

        litleBatchFileRequest.sendToLitle();
        verify(mockedCommunication).sendLitleBatchFileToIBC(any(File.class), any(File.class), any(Properties.class));

        File fileWritten = litleBatchFileRequest.getFileToWrite("batchRequestFolder");
        assertTrue(fileWritten.exists());
    }

    @Test
    public void testSendToLitleStream() throws IOException {

        File fileToBeWritten = litleBatchFileRequest.getFileToWrite("batchRequestFolder");
        // make sure the file doesn't exist before running sendToLitle.
        if(fileToBeWritten.exists()) {
            assertTrue(fileToBeWritten.delete());
        }
        assertTrue(!fileToBeWritten.exists());

        Communication mockedCommunication = mock(Communication.class);
        doNothing().when(mockedCommunication).sendLitleBatchFileToIBC(any(File.class), any(File.class), any(Properties.class));

        File responseFile = new File("test/unit/responseFolder/testFile.xml");

        litleBatchFileRequest.setResponseFile(responseFile);
        litleBatchFileRequest.setCommunication(mockedCommunication);

        litleBatchFileRequest.sendToLitleStream();
        verify(mockedCommunication).sendLitleBatchFileToIBC(any(File.class), any(File.class), any(Properties.class));

        File fileWritten = litleBatchFileRequest.getFileToWrite("batchRequestFolder");
        assertTrue(fileWritten.exists());

    }


    @Test
    public void testSendToLitleSFTP() throws IOException {
        File fileToBeWritten = litleBatchFileRequest.getFileToWrite("batchRequestFolder");
        // make sure the file doesn't exist before running sendToLitle.
        if(fileToBeWritten.exists()) {
            assertTrue(fileToBeWritten.delete());
        }
        assertTrue(!fileToBeWritten.exists());

        Communication mockedCommunication = mock(Communication.class);
        doNothing().when(mockedCommunication).sendLitleBatchFileToIBC(any(File.class), any(File.class), any(Properties.class));

        File responseFile = new File("test/unit/responseFolder/testFile.xml");

        litleBatchFileRequest.setResponseFile(responseFile);
        litleBatchFileRequest.setCommunication(mockedCommunication);

        litleBatchFileRequest.sendToLitleSFTP();
        verify(mockedCommunication).sendLitleRequestFileToSFTP(any(File.class), any(Properties.class));
        verify(mockedCommunication).receiveLitleRequestResponseFileFromSFTP(any(File.class), any(File.class), any(Properties.class));

        File fileWritten = litleBatchFileRequest.getFileToWrite("batchRequestFolder");
        assertTrue(fileWritten.exists());
    }


    @Test
    public void testIsEmpty() throws FileNotFoundException, JAXBException{
        // right off from scratch, batchFileRequest should have no records.
        assertTrue(litleBatchFileRequest.isEmpty());

        LitleBatchRequest testBatch = litleBatchFileRequest.createBatch("101");
        testBatch.addTransaction(createTestSale(101L,"101"));

        assertTrue(!litleBatchFileRequest.isEmpty());
    }

    @Test
    public void testIsFull() throws FileNotFoundException, JAXBException{
        // right off from scratch, batchFileRequest should have no records and hence not full.
        Properties property = new Properties();
        property.setProperty("username", "PHXMLTEST");
        property.setProperty("password", "password");
        property.setProperty("version", "8.18");
        property.setProperty("maxAllowedTransactionsPerFile", "3");
        property.setProperty("maxTransactionsPerBatch", "3");
        property.setProperty("batchHost", "localhost");
        property.setProperty("batchPort", "2104");
        property.setProperty("batchTcpTimeout", "10000");
        property.setProperty("batchUseSSL", "false");
        property.setProperty("proxyHost", "");
        property.setProperty("proxyPort", "");
        property.setProperty("merchantId", "101");
        property.setProperty("batchRequestFolder", "test/unit/requestFolder/");
        property.setProperty("batchResponseFolder", "test/unit/responseFolder/");

        litleBatchFileRequest = new LitleBatchFileRequest("testFile.xml", property);

        assertTrue(!litleBatchFileRequest.isFull());

        LitleBatchRequest testBatch = litleBatchFileRequest.createBatch("101");
        testBatch.addTransaction(createTestSale(101L,"101"));
        testBatch.addTransaction(createTestSale(102L,"102"));
        testBatch.addTransaction(createTestSale(103L,"103"));

        assertTrue(litleBatchFileRequest.isFull());
    }

    public Sale createTestSale(Long amount, String orderId){
        Sale sale = new Sale();
        sale.setAmount(amount);
        sale.setOrderId(orderId);
        sale.setOrderSource(OrderSourceType.ECOMMERCE);
        CardType card = new CardType();
        card.setType(MethodOfPaymentTypeEnum.VI);
        card.setNumber("4100000000000002");
        card.setExpDate("1210");
        sale.setCard(card);
        sale.setReportGroup("test");
        return sale;
    }

}