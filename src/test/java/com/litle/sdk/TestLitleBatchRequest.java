package com.litle.sdk;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.FileNotFoundException;
import java.math.BigInteger;
import java.util.Properties;

import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import com.litle.sdk.generate.AccountUpdate;
import com.litle.sdk.generate.ApplepayHeaderType;
import com.litle.sdk.generate.ApplepayType;
import com.litle.sdk.generate.AuthReversal;
import com.litle.sdk.generate.Authorization;
import com.litle.sdk.generate.Capture;
import com.litle.sdk.generate.CaptureGivenAuth;
import com.litle.sdk.generate.CardType;
import com.litle.sdk.generate.Credit;
import com.litle.sdk.generate.EcheckCredit;
import com.litle.sdk.generate.EcheckPreNoteSale;
import com.litle.sdk.generate.EcheckRedeposit;
import com.litle.sdk.generate.EcheckSale;
import com.litle.sdk.generate.EcheckType;
import com.litle.sdk.generate.EcheckVerification;
import com.litle.sdk.generate.ForceCapture;
import com.litle.sdk.generate.MethodOfPaymentTypeEnum;
import com.litle.sdk.generate.OrderSourceType;
import com.litle.sdk.generate.PayFacCredit;
import com.litle.sdk.generate.PayFacDebit;
import com.litle.sdk.generate.PhysicalCheckCredit;
import com.litle.sdk.generate.PhysicalCheckDebit;
import com.litle.sdk.generate.RegisterTokenRequestType;
import com.litle.sdk.generate.ReserveCredit;
import com.litle.sdk.generate.ReserveDebit;
import com.litle.sdk.generate.Sale;
import com.litle.sdk.generate.SubmerchantCredit;
import com.litle.sdk.generate.SubmerchantDebit;
import com.litle.sdk.generate.UpdateCardValidationNumOnToken;
import com.litle.sdk.generate.VendorCredit;
import com.litle.sdk.generate.VendorDebit;

public class TestLitleBatchRequest {

    private static LitleBatchFileRequest litleBatchFileRequest;
    private static LitleBatchRequest litleBatchRequest;
    Properties property;

    @Before
    public void before() throws Exception {
        property = new Properties();
        property.setProperty("username", "PHXMLTEST");
        property.setProperty("password", "password");
        property.setProperty("version", "8.18");
        property.setProperty("maxAllowedTransactionsPerFile", "13");
        property.setProperty("maxTransactionsPerBatch", "11");
        property.setProperty("batchHost", "localhost");
        property.setProperty("batchPort", "2104");
        property.setProperty("batchTcpTimeout", "10000");
        property.setProperty("batchUseSSL", "false");
        property.setProperty("merchantId", "101");
        property.setProperty("proxyHost", "");
        property.setProperty("proxyPort", "");
        property.setProperty("batchRequestFolder", "test/unit/");
        property.setProperty("batchResponseFolder", "test/unit/");
        property.setProperty("sftpUsername", "");
        property.setProperty("sftpPassword", "");
        litleBatchFileRequest = new LitleBatchFileRequest("testFile", property);

        litleBatchRequest = litleBatchFileRequest.createBatch("101");
        Marshaller mockMarshaller = Mockito.mock(Marshaller.class);
        litleBatchRequest.setMarshaller(mockMarshaller);
        litleBatchRequest.setNumOfTxn(1);
    }

    @Test
    public void testGetNumberOfTransactions() throws FileNotFoundException,
            JAXBException {
        assertEquals(1, litleBatchRequest.getNumberOfTransactions());
        litleBatchRequest.addTransaction(createTestSale(100L, "100"));
        assertEquals(2, litleBatchRequest.getNumberOfTransactions());
    }

    @Test
    public void testIsFull() throws FileNotFoundException, JAXBException {
        assertTrue(!litleBatchRequest.isFull());
        for (int i = 0; i < Integer.valueOf(property
                .getProperty("maxTransactionsPerBatch")) - 1; i++) {
            litleBatchRequest.addTransaction(createTestSale(100L, "100"));
        }
        assertTrue(litleBatchRequest.isFull());

    }

    @Test
    public void testVerifyFileThresholds() throws FileNotFoundException,
            JAXBException {

        for (int i = 0; i < Integer.valueOf(property
                .getProperty("maxTransactionsPerBatch")) - 2; i++) {
            litleBatchRequest.addTransaction(createTestSale(100L, "100"));
        }
        assertEquals(litleBatchRequest.verifyFileThresholds(),
                TransactionCodeEnum.SUCCESS);
        litleBatchRequest.addTransaction(createTestSale(100L, "100"));
        assertEquals(litleBatchRequest.verifyFileThresholds(),
                TransactionCodeEnum.BATCHFULL);

        LitleBatchRequest batchRequest2 = litleBatchFileRequest
                .createBatch("102");
        Marshaller mockMarshaller = Mockito.mock(Marshaller.class);
        batchRequest2.setMarshaller(mockMarshaller);
        batchRequest2.setNumOfTxn(1);

        batchRequest2.addTransaction(createTestSale(100L, "100"));
        assertEquals(litleBatchRequest.verifyFileThresholds(),
                TransactionCodeEnum.FILEFULL);
    }

    @Test
    public void testAddTransaction() throws FileNotFoundException,
            JAXBException {
        for (int i = 0; i < Integer.valueOf(property
                .getProperty("maxTransactionsPerBatch")) - 2; i++) {
            assertEquals(litleBatchRequest.addTransaction(createTestSale(100L,
                    "100")), TransactionCodeEnum.SUCCESS);
        }
        assertEquals(
                litleBatchRequest
                        .addTransaction(createTestEcheckPreNoteSale("100")),
                TransactionCodeEnum.BATCHFULL);

        boolean batchFullException = false;
        try {
            litleBatchRequest.addTransaction(createTestSale(100L, "100"));
        } catch (LitleBatchException e) {
            batchFullException = true;
        }

        assertTrue(batchFullException);

        LitleBatchRequest batchRequest2 = litleBatchFileRequest
                .createBatch("102");
        batchRequest2.setNumOfTxn(1);
        Marshaller mockMarshaller = Mockito.mock(Marshaller.class);
        batchRequest2.setMarshaller(mockMarshaller);

        assertEquals(batchRequest2.addTransaction(createTestSale(100L, "100")),
                TransactionCodeEnum.FILEFULL);

        boolean fileFullException = false;
        try {
            batchRequest2.addTransaction(createTestSale(100L, "100"));
        } catch (LitleBatchException e) {
            fileFullException = true;
        }

        assertTrue(fileFullException);
    }

    private Sale createTestSale(Long amount, String orderId) {
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

    private EcheckPreNoteSale createTestEcheckPreNoteSale(String orderId) {
        EcheckPreNoteSale echeckPreNoteSale = new EcheckPreNoteSale();
        // In unit test, we don't fill all the required fields
        echeckPreNoteSale.setOrderId(orderId);
        return echeckPreNoteSale;
    }

    @Test
    public void testAddSale() {
        litleBatchFileRequest = new LitleBatchFileRequest("testFile", property);
        litleBatchRequest = litleBatchFileRequest.createBatch("101");

        litleBatchRequest.setNumOfTxn(1);
        Marshaller mockMarshaller = Mockito.mock(Marshaller.class);
        litleBatchRequest.setMarshaller(mockMarshaller);

        Sale sale = new Sale();
        sale.setAmount(25L);
        sale.setSecondaryAmount(10L);
        sale.setApplepay(createApplepay());
        litleBatchRequest.addTransaction(sale);
        assertEquals(25, litleBatchRequest.getBatchRequest().getSaleAmount()
                .intValue());
        assertEquals(1, litleBatchRequest.getBatchRequest().getNumSales()
                .intValue());
        assertEquals(2, litleBatchRequest.getNumberOfTransactions());

    }

    @Test
    public void testAddAuth() {
        litleBatchFileRequest = new LitleBatchFileRequest("testFile", property);
        litleBatchRequest = litleBatchFileRequest.createBatch("101");

        litleBatchRequest.setNumOfTxn(1);
        Marshaller mockMarshaller = Mockito.mock(Marshaller.class);
        litleBatchRequest.setMarshaller(mockMarshaller);

        Authorization auth = new Authorization();
        auth.setAmount(25L);
        auth.setSecondaryAmount(10L);
        auth.setApplepay(createApplepay());
        litleBatchRequest.addTransaction(auth);
        assertEquals(25, litleBatchRequest.getBatchRequest().getAuthAmount()
                .intValue());
        assertEquals(1, litleBatchRequest.getBatchRequest().getNumAuths()
                .intValue());
        assertEquals(2, litleBatchRequest.getNumberOfTransactions());

    }

    @Test
    public void testAddCredit() {
        litleBatchFileRequest = new LitleBatchFileRequest("testFile", property);
        litleBatchRequest = litleBatchFileRequest.createBatch("101");

        litleBatchRequest.setNumOfTxn(1);
        Marshaller mockMarshaller = Mockito.mock(Marshaller.class);
        litleBatchRequest.setMarshaller(mockMarshaller);

        Credit credit = new Credit();
        credit.setAmount(25L);
        credit.setSecondaryAmount(10L);
        litleBatchRequest.addTransaction(credit);
        assertEquals(25, litleBatchRequest.getBatchRequest().getCreditAmount()
                .intValue());
        assertEquals(1, litleBatchRequest.getBatchRequest().getNumCredits()
                .intValue());
        assertEquals(2, litleBatchRequest.getNumberOfTransactions());

    }

    @Test
    public void testAddRegisterTokenRequestType() {
        litleBatchFileRequest = new LitleBatchFileRequest("testFile", property);
        litleBatchRequest = litleBatchFileRequest.createBatch("101");

        litleBatchRequest.setNumOfTxn(1);
        Marshaller mockMarshaller = Mockito.mock(Marshaller.class);
        litleBatchRequest.setMarshaller(mockMarshaller);

        RegisterTokenRequestType registerTokenRequest = new RegisterTokenRequestType();
        registerTokenRequest.setApplepay(createApplepay());
        litleBatchRequest.addTransaction(registerTokenRequest);
        assertEquals(1, litleBatchRequest.getBatchRequest()
                .getNumTokenRegistrations().intValue());
        assertEquals(2, litleBatchRequest.getNumberOfTransactions());

    }

    @Test
    public void testAddCaptureGivenAuth() {
        litleBatchFileRequest = new LitleBatchFileRequest("testFile", property);
        litleBatchRequest = litleBatchFileRequest.createBatch("101");

        litleBatchRequest.setNumOfTxn(1);
        Marshaller mockMarshaller = Mockito.mock(Marshaller.class);
        litleBatchRequest.setMarshaller(mockMarshaller);

        CaptureGivenAuth captureGivenAuth = new CaptureGivenAuth();
        captureGivenAuth.setAmount(25L);
        captureGivenAuth.setSecondaryAmount(10L);
        litleBatchRequest.addTransaction(captureGivenAuth);
        assertEquals(25, litleBatchRequest.getBatchRequest()
                .getCaptureGivenAuthAmount().intValue());
        assertEquals(1, litleBatchRequest.getBatchRequest()
                .getNumCaptureGivenAuths().intValue());
        assertEquals(2, litleBatchRequest.getNumberOfTransactions());

    }

    @Test
    public void testAddForceCapture() {
        litleBatchFileRequest = new LitleBatchFileRequest("testFile", property);
        litleBatchRequest = litleBatchFileRequest.createBatch("101");

        litleBatchRequest.setNumOfTxn(1);
        Marshaller mockMarshaller = Mockito.mock(Marshaller.class);
        litleBatchRequest.setMarshaller(mockMarshaller);

        ForceCapture forceCapture = new ForceCapture();
        forceCapture.setAmount(25L);
        forceCapture.setSecondaryAmount(10L);
        litleBatchRequest.addTransaction(forceCapture);
        assertEquals(25, litleBatchRequest.getBatchRequest()
                .getForceCaptureAmount().intValue());
        assertEquals(1, litleBatchRequest.getBatchRequest()
                .getNumForceCaptures().intValue());
        assertEquals(2, litleBatchRequest.getNumberOfTransactions());

    }

    @Test
    public void testAddAuthReversal() {
        litleBatchFileRequest = new LitleBatchFileRequest("testFile", property);
        litleBatchRequest = litleBatchFileRequest.createBatch("101");

        litleBatchRequest.setNumOfTxn(1);
        Marshaller mockMarshaller = Mockito.mock(Marshaller.class);
        litleBatchRequest.setMarshaller(mockMarshaller);

        AuthReversal authReversal = new AuthReversal();
        authReversal.setAmount(25L);
        litleBatchRequest.addTransaction(authReversal);
        assertEquals(25, litleBatchRequest.getBatchRequest()
                .getAuthReversalAmount().intValue());
        assertEquals(1, litleBatchRequest.getBatchRequest()
                .getNumAuthReversals().intValue());
        assertEquals(2, litleBatchRequest.getNumberOfTransactions());

    }

    @Test
    public void testAddCapture() {
        litleBatchFileRequest = new LitleBatchFileRequest("testFile", property);
        litleBatchRequest = litleBatchFileRequest.createBatch("101");

        litleBatchRequest.setNumOfTxn(1);
        Marshaller mockMarshaller = Mockito.mock(Marshaller.class);
        litleBatchRequest.setMarshaller(mockMarshaller);

        Capture capture = new Capture();
        capture.setAmount(25L);
        litleBatchRequest.addTransaction(capture);
        assertEquals(25, litleBatchRequest.getBatchRequest().getCaptureAmount()
                .intValue());
        assertEquals(1, litleBatchRequest.getBatchRequest().getNumCaptures()
                .intValue());
        assertEquals(2, litleBatchRequest.getNumberOfTransactions());

    }

    @Test
    public void testAddEcheckVerification() {
        litleBatchFileRequest = new LitleBatchFileRequest("testFile", property);
        litleBatchRequest = litleBatchFileRequest.createBatch("101");

        litleBatchRequest.setNumOfTxn(1);
        Marshaller mockMarshaller = Mockito.mock(Marshaller.class);
        litleBatchRequest.setMarshaller(mockMarshaller);

        EcheckVerification echeckVerification = new EcheckVerification();
        echeckVerification.setAmount(25L);
        litleBatchRequest.addTransaction(echeckVerification);
        assertEquals(25, litleBatchRequest.getBatchRequest()
                .getEcheckVerificationAmount().intValue());
        assertEquals(1, litleBatchRequest.getBatchRequest()
                .getNumEcheckVerification().intValue());
        assertEquals(2, litleBatchRequest.getNumberOfTransactions());

    }

    @Test
    public void testAddEcheckCredit() {
        litleBatchFileRequest = new LitleBatchFileRequest("testFile", property);
        litleBatchRequest = litleBatchFileRequest.createBatch("101");

        litleBatchRequest.setNumOfTxn(1);
        Marshaller mockMarshaller = Mockito.mock(Marshaller.class);
        litleBatchRequest.setMarshaller(mockMarshaller);

        EcheckCredit echeckCredit = new EcheckCredit();
        echeckCredit.setAmount(25L);
        echeckCredit.setSecondaryAmount(10L);
        litleBatchRequest.addTransaction(echeckCredit);
        assertEquals(25, litleBatchRequest.getBatchRequest()
                .getEcheckCreditAmount().intValue());
        assertEquals(1, litleBatchRequest.getBatchRequest()
                .getNumEcheckCredit().intValue());
        assertEquals(2, litleBatchRequest.getNumberOfTransactions());

    }

    @Test
    public void testAddEcheckRedeposit() {
        litleBatchFileRequest = new LitleBatchFileRequest("testFile", property);
        litleBatchRequest = litleBatchFileRequest.createBatch("101");

        litleBatchRequest.setNumOfTxn(1);
        Marshaller mockMarshaller = Mockito.mock(Marshaller.class);
        litleBatchRequest.setMarshaller(mockMarshaller);

        EcheckRedeposit echeckRedeposit = new EcheckRedeposit();
        litleBatchRequest.addTransaction(echeckRedeposit);
        assertEquals(1, litleBatchRequest.getBatchRequest()
                .getNumEcheckRedeposit().intValue());
        assertEquals(2, litleBatchRequest.getNumberOfTransactions());

    }

    @Test
    public void testAddEcheckSale() {
        litleBatchFileRequest = new LitleBatchFileRequest("testFile", property);
        litleBatchRequest = litleBatchFileRequest.createBatch("101");

        litleBatchRequest.setNumOfTxn(1);
        Marshaller mockMarshaller = Mockito.mock(Marshaller.class);
        litleBatchRequest.setMarshaller(mockMarshaller);

        EcheckSale echeckSale = new EcheckSale();
        echeckSale.setAmount(25L);
        echeckSale.setSecondaryAmount(10L);
        litleBatchRequest.addTransaction(echeckSale);
        assertEquals(25, litleBatchRequest.getBatchRequest()
                .getEcheckSalesAmount().intValue());
        assertEquals(1, litleBatchRequest.getBatchRequest().getNumEcheckSales()
                .intValue());
        assertEquals(2, litleBatchRequest.getNumberOfTransactions());

    }

    @Test
    public void testAddUpdateCardValidationNumOnToken() {
        litleBatchFileRequest = new LitleBatchFileRequest("testFile", property);
        litleBatchRequest = litleBatchFileRequest.createBatch("101");

        litleBatchRequest.setNumOfTxn(1);
        Marshaller mockMarshaller = Mockito.mock(Marshaller.class);
        litleBatchRequest.setMarshaller(mockMarshaller);

        UpdateCardValidationNumOnToken updateCardValidationNumOnToken = new UpdateCardValidationNumOnToken();
        litleBatchRequest.addTransaction(updateCardValidationNumOnToken);
        assertEquals(1, litleBatchRequest.getBatchRequest()
                .getNumUpdateCardValidationNumOnTokens().intValue());
        assertEquals(2, litleBatchRequest.getNumberOfTransactions());

    }

    @Test
    public void testAddAccountUpdate() {
        litleBatchFileRequest = new LitleBatchFileRequest("testFile", property);
        litleBatchRequest = litleBatchFileRequest.createBatch("101");

        litleBatchRequest.setNumOfTxn(1);
        litleBatchRequest.getBatchRequest().setNumAccountUpdates(
                BigInteger.valueOf(1l));
        Marshaller mockMarshaller = Mockito.mock(Marshaller.class);
        litleBatchRequest.setMarshaller(mockMarshaller);

        AccountUpdate accountUpdate = new AccountUpdate();
        litleBatchRequest.addTransaction(accountUpdate);
        assertEquals(2, litleBatchRequest.getBatchRequest()
                .getNumAccountUpdates().intValue());
        assertEquals(2, litleBatchRequest.getNumberOfTransactions());

    }

    @Test
    public void testPFIFInstructionTxn() {
        litleBatchFileRequest = new LitleBatchFileRequest("testFile", property);
        litleBatchRequest = litleBatchFileRequest.createBatch("101");

        litleBatchRequest.setNumOfTxn(1);
        Marshaller mockMarshaller = Mockito.mock(Marshaller.class);
        litleBatchRequest.setMarshaller(mockMarshaller);

        SubmerchantCredit submerchantCredit = new SubmerchantCredit();
        submerchantCredit.setFundingSubmerchantId("12345");
        submerchantCredit.setSubmerchantName("submerchant");
        submerchantCredit.setFundsTransferId("1234567");
        submerchantCredit.setAmount(106L);
        submerchantCredit.setAccountInfo(new EcheckType());
        litleBatchRequest.addTransaction(submerchantCredit);

        VendorCredit vendorCredit = new VendorCredit();
        vendorCredit.setFundingSubmerchantId("12345");
        vendorCredit.setVendorName("vendor");
        vendorCredit.setFundsTransferId("1234567");
        vendorCredit.setAmount(107L);
        vendorCredit.setAccountInfo(new EcheckType());
        litleBatchRequest.addTransaction(vendorCredit);

        PayFacCredit payFacCredit = new PayFacCredit();
        payFacCredit.setFundingSubmerchantId("12345");
        payFacCredit.setFundsTransferId("1234567");
        payFacCredit.setAmount(108L);
        litleBatchRequest.addTransaction(payFacCredit);

        ReserveCredit reserveCredit = new ReserveCredit();
        reserveCredit.setFundingSubmerchantId("12345");
        reserveCredit.setFundsTransferId("1234567");
        reserveCredit.setAmount(109L);
        litleBatchRequest.addTransaction(reserveCredit);

        PhysicalCheckCredit physicalCheckCredit = new PhysicalCheckCredit();
        physicalCheckCredit.setFundingSubmerchantId("12345");
        physicalCheckCredit.setFundsTransferId("1234567");
        physicalCheckCredit.setAmount(110L);
        litleBatchRequest.addTransaction(physicalCheckCredit);

        SubmerchantDebit submerchantDebit = new SubmerchantDebit();
        submerchantDebit.setFundingSubmerchantId("12345");
        submerchantDebit.setSubmerchantName("submerchant");
        submerchantDebit.setFundsTransferId("1234567");
        submerchantDebit.setAmount(106L);
        submerchantDebit.setAccountInfo(new EcheckType());
        litleBatchRequest.addTransaction(submerchantDebit);

        VendorDebit vendorDebit = new VendorDebit();
        vendorDebit.setFundingSubmerchantId("12345");
        vendorDebit.setVendorName("vendor");
        vendorDebit.setFundsTransferId("1234567");
        vendorDebit.setAmount(107L);
        vendorDebit.setAccountInfo(new EcheckType());
        litleBatchRequest.addTransaction(vendorDebit);

        PayFacDebit payFacDebit = new PayFacDebit();
        payFacDebit.setFundingSubmerchantId("12345");
        payFacDebit.setFundsTransferId("1234567");
        payFacDebit.setAmount(108L);
        litleBatchRequest.addTransaction(payFacDebit);

        ReserveDebit reserveDebit = new ReserveDebit();
        reserveDebit.setFundingSubmerchantId("12345");
        reserveDebit.setFundsTransferId("1234567");
        reserveDebit.setAmount(109L);
        litleBatchRequest.addTransaction(reserveDebit);

        PhysicalCheckDebit physicalCheckDebit = new PhysicalCheckDebit();
        physicalCheckDebit.setFundingSubmerchantId("12345");
        physicalCheckDebit.setFundsTransferId("1234567");
        physicalCheckDebit.setAmount(110L);
        litleBatchRequest.addTransaction(physicalCheckDebit);
      

        assertEquals(1, litleBatchRequest.getBatchRequest()
                .getNumSubmerchantCredit().intValue());
        assertEquals(106, litleBatchRequest.getBatchRequest()
                .getSubmerchantCreditAmount().intValue());
        assertEquals(1, litleBatchRequest.getBatchRequest()
                .getNumVendorCredit().intValue());
        assertEquals(107, litleBatchRequest.getBatchRequest()
                .getVendorCreditAmount().intValue());
        assertEquals(1, litleBatchRequest.getBatchRequest()
                .getNumPayFacCredit().intValue());
        assertEquals(108, litleBatchRequest.getBatchRequest()
                .getPayFacCreditAmount().intValue());
        assertEquals(1, litleBatchRequest.getBatchRequest()
                .getNumReserveCredit().intValue());
        assertEquals(109, litleBatchRequest.getBatchRequest()
                .getReserveCreditAmount().intValue());
        assertEquals(1, litleBatchRequest.getBatchRequest()
                .getNumPhysicalCheckCredit().intValue());
        assertEquals(110, litleBatchRequest.getBatchRequest()
                .getPhysicalCheckCreditAmount().intValue());
        assertEquals(1, litleBatchRequest.getBatchRequest()
                .getNumSubmerchantDebit().intValue());
        assertEquals(106, litleBatchRequest.getBatchRequest()
                .getSubmerchantDebitAmount().intValue());
        assertEquals(1, litleBatchRequest.getBatchRequest().getNumVendorDebit()
                .intValue());
        assertEquals(107, litleBatchRequest.getBatchRequest()
                .getVendorDebitAmount().intValue());
        assertEquals(1, litleBatchRequest.getBatchRequest().getNumPayFacDebit()
                .intValue());
        assertEquals(108, litleBatchRequest.getBatchRequest()
                .getPayFacDebitAmount().intValue());
        assertEquals(1, litleBatchRequest.getBatchRequest()
                .getNumReserveDebit().intValue());
        assertEquals(109, litleBatchRequest.getBatchRequest()
                .getReserveDebitAmount().intValue());
        assertEquals(1, litleBatchRequest.getBatchRequest()
                .getNumPhysicalCheckDebit().intValue());
        assertEquals(110, litleBatchRequest.getBatchRequest()
                .getPhysicalCheckDebitAmount().intValue());

        assertEquals(11, litleBatchRequest.getNumberOfTransactions());

    }

    @Test(expected = LitleBatchException.class)
    public void testAddAUBlock_AU_side() {

        litleBatchFileRequest = new LitleBatchFileRequest("testFile", property);
        litleBatchRequest = litleBatchFileRequest.createBatch("101");

        AccountUpdate accountUpdate = new AccountUpdate();
        litleBatchRequest.setNumOfTxn(1);
        litleBatchRequest.getBatchRequest().setNumAccountUpdates(
                BigInteger.valueOf(1l));
        Marshaller mockMarshaller = Mockito.mock(Marshaller.class);
        litleBatchRequest.setMarshaller(mockMarshaller);

        litleBatchRequest.addTransaction(accountUpdate);

        assertEquals(2, litleBatchRequest.getBatchRequest()
                .getNumAccountUpdates().intValue());
        assertEquals(2, litleBatchRequest.getNumberOfTransactions());

        Sale sale = new Sale();
        sale.setAmount(5L);

        litleBatchRequest.addTransaction(sale);
    }

    @Test(expected = LitleBatchException.class)
    public void testAddAUBlock_Sale_side() {

        litleBatchFileRequest = new LitleBatchFileRequest("testFile", property);
        litleBatchRequest = litleBatchFileRequest.createBatch("101");
        Sale sale = new Sale();
        sale.setAmount(5L);

        litleBatchRequest.setNumOfTxn(1);
        Marshaller mockMarshaller = Mockito.mock(Marshaller.class);
        litleBatchRequest.setMarshaller(mockMarshaller);

        litleBatchRequest.addTransaction(sale);
        assertEquals(1, litleBatchRequest.getBatchRequest().getNumSales()
                .intValue());
        assertEquals(5, litleBatchRequest.getBatchRequest().getSaleAmount()
                .intValue());
        assertEquals(2, litleBatchRequest.getNumberOfTransactions());

        AccountUpdate accountUpdate = new AccountUpdate();

        litleBatchRequest.addTransaction(accountUpdate);
    }

    private ApplepayType createApplepay() {
        ApplepayType applepayType = new ApplepayType();
        ApplepayHeaderType applepayHeaderType = new ApplepayHeaderType();
        applepayHeaderType
                .setApplicationData("e3b0c44298fc1c149afbf4c8996fb92427ae41e4649b934ca495991b7852b855");
        applepayHeaderType
                .setEphemeralPublicKey("e3b0c44298fc1c149afbf4c8996fb92427ae41e4649b934ca495991b7852b855");
        applepayHeaderType
                .setPublicKeyHash("e3b0c44298fc1c149afbf4c8996fb92427ae41e4649b934ca495991b7852b855");
        applepayHeaderType.setTransactionId("1234");
        applepayType.setHeader(applepayHeaderType);
        applepayType.setData("user");
        applepayType.setSignature("sign");
        applepayType.setVersion("1");
        return applepayType;
    }

}
