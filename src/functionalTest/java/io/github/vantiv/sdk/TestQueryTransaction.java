package io.github.vantiv.sdk;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.BeforeClass;
import org.junit.Test;

import io.github.vantiv.sdk.generate.*;

public class TestQueryTransaction {

	private static LitleOnline litle;

	@BeforeClass
	public static void beforeClass() throws Exception {
		litle = new LitleOnline();
	}

	@Test
	public void simpleQueryTransaction() throws Exception {
	    QueryTransaction queryTransaction = new QueryTransaction();
        queryTransaction.setId("findId");
        queryTransaction.setCustomerId("customerId");
        queryTransaction.setOrigId("orgId1");
        queryTransaction.setOrigActionType(ActionTypeEnum.A);
        queryTransaction.setReportGroup("default");

        TransactionTypeWithReportGroup response = litle.queryTransaction(queryTransaction);
        QueryTransactionResponse queryTransactionResponse = (QueryTransactionResponse)response;
        assertEquals("findId", queryTransactionResponse.getId());
        assertEquals("customerId", queryTransactionResponse.getCustomerId());
        assertEquals("150", queryTransactionResponse.getResponse());
        assertEquals("Original transaction found",queryTransactionResponse.getMessage());
        assertEquals(1, queryTransactionResponse.getResultsMax10().getTransactionResponses().size());
	}

	@Test
    public void simpleQueryTransaction_multipleResponses() throws Exception {
        QueryTransaction queryTransaction = new QueryTransaction();
        queryTransaction.setId("findId");
        queryTransaction.setCustomerId("customerId");
        queryTransaction.setOrigId("orgId2");
        queryTransaction.setOrigActionType(ActionTypeEnum.D);
        queryTransaction.setReportGroup("default");

        TransactionTypeWithReportGroup response = litle.queryTransaction(queryTransaction);
        QueryTransactionResponse queryTransactionResponse = (QueryTransactionResponse)response;
        assertEquals("findId", queryTransactionResponse.getId());
        assertEquals("customerId", queryTransactionResponse.getCustomerId());
        assertEquals("150", queryTransactionResponse.getResponse());
        assertEquals("Original transaction found",queryTransactionResponse.getMessage());
        assertEquals(2, queryTransactionResponse.getResultsMax10().getTransactionResponses().size());
    }
	
	@Test
    public void simpleQueryTransaction_transactionNotFound() throws Exception {
        QueryTransaction queryTransaction = new QueryTransaction();
        queryTransaction.setId("findId");
        queryTransaction.setCustomerId("customerId");
        queryTransaction.setOrigId("orgId0");
        queryTransaction.setOrigActionType(ActionTypeEnum.A);
        queryTransaction.setReportGroup("default");

        TransactionTypeWithReportGroup response = litle.queryTransaction(queryTransaction);
        QueryTransactionResponse queryTransactionResponse = (QueryTransactionResponse)response;
        assertEquals("findId", queryTransactionResponse.getId());
        assertEquals("customerId", queryTransactionResponse.getCustomerId());
        assertEquals("151", queryTransactionResponse.getResponse());
        assertEquals("Original transaction not found",queryTransactionResponse.getMessage());
        assertNull(queryTransactionResponse.getResultsMax10());
    }
	
	@Test
    public void simpleQueryTransaction_transactionNotFoundWrongActionType() throws Exception {
        QueryTransaction queryTransaction = new QueryTransaction();
        queryTransaction.setId("findId");
        queryTransaction.setCustomerId("customerId");
        queryTransaction.setOrigId("orgId5");
        queryTransaction.setOrigActionType(ActionTypeEnum.R);
        queryTransaction.setReportGroup("default");

        TransactionTypeWithReportGroup response = litle.queryTransaction(queryTransaction);
        QueryTransactionResponse queryTransactionResponse = (QueryTransactionResponse)response;
        assertEquals("findId", queryTransactionResponse.getId());
        assertEquals("customerId", queryTransactionResponse.getCustomerId());
        assertEquals("151", queryTransactionResponse.getResponse());
        assertEquals("Original transaction not found",queryTransactionResponse.getMessage());
        assertNull(queryTransactionResponse.getResultsMax10());
    }

	@Test
    public void simpleQueryTransaction_queryTransactionUnavailaleResponse() throws Exception {
        QueryTransaction queryTransaction = new QueryTransaction();
        queryTransaction.setId("findId");
        queryTransaction.setCustomerId("customerId");
        queryTransaction.setOrigId("orgId");
        queryTransaction.setOrigActionType(ActionTypeEnum.A);
        queryTransaction.setReportGroup("default");

        TransactionTypeWithReportGroup response = litle.queryTransaction(queryTransaction);
        QueryTransactionUnavailableResponse queryTransactionResponse = (QueryTransactionUnavailableResponse)response;
        assertEquals("findId", queryTransactionResponse.getId());
        assertEquals("customerId", queryTransactionResponse.getCustomerId());
        assertEquals("152", queryTransactionResponse.getResponse());
        assertEquals("Original transaction found but response not yet available",queryTransactionResponse.getMessage());
    }
}
