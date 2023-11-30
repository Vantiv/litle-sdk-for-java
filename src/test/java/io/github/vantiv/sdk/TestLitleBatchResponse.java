package io.github.vantiv.sdk;

import static org.junit.Assert.assertEquals;

import io.github.vantiv.sdk.generate.AuthorizationResponse;
import io.github.vantiv.sdk.generate.SaleResponse;
import org.junit.Before;
import org.junit.Test;

import io.github.vantiv.sdk.generate.BatchResponse;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

public class TestLitleBatchResponse {

    @Before
    public void before() {
    }

    @Test
    public void testSetBatchResponse() {
        BatchResponse batchResponse = new BatchResponse();
        batchResponse.setId("101");
        batchResponse.setLitleBatchId(562L);
        batchResponse.setMerchantId("101");
        LitleBatchResponse litleBatchResponse = new LitleBatchResponse(batchResponse);
        assertEquals("101", litleBatchResponse.getBatchResponse().getId());
        assertEquals(562L, litleBatchResponse.getBatchResponse().getLitleBatchId());
        assertEquals("101", litleBatchResponse.getBatchResponse().getMerchantId());
    }

    @Test
    public void testInputStreamParser() {
        String responseXml = "<litleResponse version=\"10.8\" xmlns=\"http://www.litle.com/schema\" response=\"0\" message=\"Valid Format\" litleSessionId=\"82923061899755536\">\n" +
                "<batchResponse litleBatchId=\"82923061899755544\" merchantId=\"0180-xml10\">\n" +
                "<authorizationResponse id=\"id\" reportGroup=\"Planets\">\n" +
                "    <litleTxnId>82923061899755684</litleTxnId>\n" +
                "    <orderId>12344</orderId>\n" +
                "    <response>301</response>\n" +
                "    <responseTime>2018-05-01T02:28:34</responseTime>\n" +
                "    <message>Invalid Account Number</message>\n" +
                "    <fraudResult>\n" +
                "        <avsResult>34</avsResult>\n" +
                "    </fraudResult>\n" +
                "</authorizationResponse>\n" +
                "<saleResponse id=\"id\" reportGroup=\"Planets\">\n" +
                "    <litleTxnId>82923061899755692</litleTxnId>\n" +
                "    <orderId>12344</orderId>\n" +
                "    <response>301</response>\n" +
                "    <responseTime>2018-05-01T02:28:34</responseTime>\n" +
                "    <message>Invalid Account Number</message>\n" +
                "    <fraudResult>\n" +
                "        <avsResult>34</avsResult>\n" +
                "    </fraudResult>\n" +
                "</saleResponse>\n" +
                "</batchResponse>\n" +
                "</litleResponse>";
        InputStream is = new ByteArrayInputStream(responseXml.getBytes());
        LitleBatchFileResponse response = new LitleBatchFileResponse(is);
        assertEquals("0", response.getResponse());
        assertEquals("Valid Format", response.getMessage());
        LitleBatchResponse batchResponse = response.getNextLitleBatchResponse();
        AuthorizationResponse auth = (AuthorizationResponse) batchResponse.getNextTransaction();
        assertEquals("301", auth.getResponse());
        SaleResponse sale = (SaleResponse) batchResponse.getNextTransaction();
        assertEquals("301", sale.getResponse());
    }
}
