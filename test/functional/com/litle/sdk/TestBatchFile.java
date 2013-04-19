package com.litle.sdk;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.util.List;
import java.util.Properties;

import org.junit.Test;

import com.litle.sdk.generate.CardType;
import com.litle.sdk.generate.MethodOfPaymentTypeEnum;
import com.litle.sdk.generate.OrderSourceType;
import com.litle.sdk.generate.Sale;
import com.litle.sdk.generate.SaleResponse;
import com.litle.sdk.generate.TransactionTypeWithReportGroup;

public class TestBatchFile {
	
	@Test
	public void testSendToLitle_WithFileConfig() throws Exception {
		String requestFileName = "litleSdk-testBatchFile-fileConfig.xml";
		LitleBatchFileRequest request = new LitleBatchFileRequest(requestFileName);
		
		Properties configFromFile = request.getConfig();
		
		// pre-assert the config file has required param values  
		assertEquals("cert.litle.com", configFromFile.getProperty("batchHost"));
		assertEquals("15000", configFromFile.getProperty("batchPort"));
		
		String workingDirRequests = configFromFile.getProperty("batchRequestFolder");
		prepDir(workingDirRequests);
		
		String workingDirResponses = configFromFile.getProperty("batchResponseFolder");
		prepDir(workingDirResponses);
		
		prepareTestRequest(request);
		
		// request file is not generated before calling sendToLitle();
		assertNull(request.getFile());
		
		/* call method under test */
		LitleBatchFileResponse response = request.sendToLitle();
		
		// assert response can be processed through Java API
		assertJavaApi(response);
		
		// assert request and response files were created properly
		assertGeneratedFiles(workingDirRequests, workingDirResponses, requestFileName, request, response);
	}
	
	@Test
	public void testSendToLitle_WithConfigOverrides() throws Exception {
		String workingDir = System.getProperty("java.io.tmpdir");
		
		String workingDirRequests = workingDir + File.separator + "requests";
		prepDir(workingDirRequests);
		
		String workingDirResponses = workingDir + File.separator + "responses";
		prepDir(workingDirResponses);
		
		Properties configOverrides = new Properties();
		configOverrides.setProperty("batchHost", "cert.litle.com");  // TODO - point this to the www.testlitle.com sandbox instead of cert
		configOverrides.setProperty("batchPort", "15000");
		configOverrides.setProperty("batchRequestFolder", workingDirRequests);
		configOverrides.setProperty("batchResponseFolder", workingDirResponses);
		
		String requestFileName = "litleSdk-testBatchFile-configOverrides.xml";
		LitleBatchFileRequest request = new LitleBatchFileRequest(requestFileName, configOverrides);
		
		prepareTestRequest(request);
		
		// request file is not generated before calling sendToLitle();
		assertNull(request.getFile());
		
		/* call method under test */
		LitleBatchFileResponse response = request.sendToLitle();
		
		// assert response can be processed through Java API
		assertJavaApi(response);
		
		// assert request and response files were created properly
		assertGeneratedFiles(workingDirRequests, workingDirResponses, requestFileName, request, response);
	}

	private void prepareTestRequest(LitleBatchFileRequest request) {
		LitleBatchRequest batchRequest1 = request.createBatch("101");
		Sale sale11 = new Sale();
		sale11.setReportGroup("reportGroup1");
		sale11.setOrderId("orderId1");
		sale11.setAmount(1099L);
		sale11.setOrderSource(OrderSourceType.ECOMMERCE);
		
		CardType card = new CardType();
		card.setType(MethodOfPaymentTypeEnum.VI);
		card.setNumber("4100000000000001");
		card.setExpDate("1220");
		sale11.setCard(card);
		
		batchRequest1.addTransaction(sale11);
	}

	private void assertJavaApi(LitleBatchFileResponse response) {
		assertNotNull(response);
		List<LitleBatchResponse> batchList = response.getBatchResponseList();
		assertEquals(1, batchList.size());
		
		LitleBatchResponse batchResponse1 = batchList.get(0);
		LitleBatchResponse.TransactionTypeIterator it = batchResponse1.getTransactionResponses();
		
		assertTrue("expected exactly one transaction in batchResponse1", it.hasNext());
		
		TransactionTypeWithReportGroup txnResponse = it.next();
		SaleResponse saleResponse11 = (SaleResponse) txnResponse;
		assertEquals("000", saleResponse11.getResponse());
		assertEquals("Approved", saleResponse11.getMessage());
		assertNotNull(saleResponse11.getLitleTxnId());
		assertEquals("orderId1", saleResponse11.getOrderId());
		assertEquals("reportGroup1", saleResponse11.getReportGroup());
		
		assertFalse("expected only one transaction in batchResponse1", it.hasNext());
	}
	
	private void assertGeneratedFiles(String workingDirRequests, String workingDirResponses, String requestFileName,
			LitleBatchFileRequest request, LitleBatchFileResponse response) throws Exception {
		File fRequest = request.getFile();
		assertEquals(workingDirRequests + File.separator + requestFileName, fRequest.getAbsolutePath());
		assertTrue(fRequest.exists());
		assertTrue(fRequest.length() > 0);
		
		File fResponse = response.getFile();
		assertEquals(workingDirResponses + File.separator + requestFileName, fResponse.getAbsolutePath());
		assertTrue(fResponse.exists());
		assertTrue(fResponse.length() > 0);
		
		// assert contents of the response file by reading it through the Java API again
		LitleBatchFileResponse responseFromFile = new LitleBatchFileResponse(fResponse);
		assertJavaApi(responseFromFile);
	}

	private void prepDir(String dirName) {
		File fRequestDir = new File(dirName);
		deleteDir(fRequestDir);
		fRequestDir.mkdirs();
	}

	private void deleteDir(File fDir) {
		File[] childFiles = fDir.listFiles();
		if(childFiles != null) {
			for (File child : childFiles) {
				if(child.isDirectory()) {
					deleteDir(child);
				}
				else {
					child.delete();
				}
			}
		}
		fDir.delete();
	}
	
	public static void main(String[] args) throws Exception {
		TestBatchFile t = new TestBatchFile();
		
		t.testSendToLitle_WithFileConfig();
		t.testSendToLitle_WithConfigOverrides();
	}
}
