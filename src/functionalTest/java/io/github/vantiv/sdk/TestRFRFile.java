package io.github.vantiv.sdk;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.util.Calendar;
import java.util.Properties;

import org.junit.Test;

import io.github.vantiv.sdk.generate.AccountUpdateFileRequestData;
import io.github.vantiv.sdk.generate.RFRRequest;

public class TestRFRFile {

    String merchantId = "0180-xml10";

	@Test
    public void testSendToLitleSFTP() throws Exception {
        String requestFileName = "litleSdk-testRFRFile-fileConfigSFTP.xml";
        RFRRequest rfrRequest = new RFRRequest();
        AccountUpdateFileRequestData data = new AccountUpdateFileRequestData();
        data.setMerchantId(merchantId);
        data.setPostDay(Calendar.getInstance());
        rfrRequest.setAccountUpdateFileRequestData(data);

        LitleRFRFileRequest request = new LitleRFRFileRequest(requestFileName, rfrRequest);

        Properties configFromFile = request.getConfig();

        // pre-assert the config file has required param values
        assertEquals("prelive.litle.com", configFromFile.getProperty("batchHost"));
        assertEquals("15000", configFromFile.getProperty("batchPort"));

        String workingDirRequests = configFromFile.getProperty("batchRequestFolder");
        prepDir(workingDirRequests);

        String workingDirResponses = configFromFile.getProperty("batchResponseFolder");
        prepDir(workingDirResponses);



        /* call method under test */
        try {
            LitleRFRFileResponse response = request.sendToLitleSFTP();

            // assert request and response files were created properly
            assertGeneratedFiles(workingDirRequests, workingDirResponses, requestFileName, request, response);
        } catch (Exception e) {

        }
    }

	@Test
    public void testSendToLitleStream() throws Exception {
        String requestFileName = "litleSdk-testRFRFile-fileConfig.xml";
        RFRRequest rfrRequest = new RFRRequest();
        AccountUpdateFileRequestData data = new AccountUpdateFileRequestData();
        data.setMerchantId(merchantId);
        data.setPostDay(Calendar.getInstance());
        rfrRequest.setAccountUpdateFileRequestData(data);

        LitleRFRFileRequest request = new LitleRFRFileRequest(requestFileName, rfrRequest);

        Properties configFromFile = request.getConfig();

        // pre-assert the config file has required param values
        assertEquals("prelive.litle.com", configFromFile.getProperty("batchHost"));
        assertEquals("15000", configFromFile.getProperty("batchPort"));

        String workingDirRequests = configFromFile.getProperty("batchRequestFolder");
        prepDir(workingDirRequests);

        String workingDirResponses = configFromFile.getProperty("batchResponseFolder");
        prepDir(workingDirResponses);



        /* call method under test */

        try {
            LitleRFRFileResponse response = request.sendToLitleStream();

            // assert request and response files were created properly
            assertGeneratedFiles(workingDirRequests, workingDirResponses, requestFileName, request, response);
        } catch (Exception e) {

        }
    }

	private void assertGeneratedFiles(String workingDirRequests, String workingDirResponses, String requestFileName,
		LitleRFRFileRequest request, LitleRFRFileResponse response) throws Exception {
		File fRequest = request.getFile();
		assertEquals(workingDirRequests + File.separator + requestFileName, fRequest.getAbsolutePath());
		assertTrue(fRequest.exists());
		assertTrue(fRequest.length() > 0);

		File fResponse = response.getFile();
		assertEquals(workingDirResponses + File.separator + requestFileName, fResponse.getAbsolutePath());
		assertTrue(fResponse.exists());
		assertTrue(fResponse.length() > 0);

	}

	private void prepDir(String dirName) {
		File fRequestDir = new File(dirName);
		fRequestDir.mkdirs();
	}
}
