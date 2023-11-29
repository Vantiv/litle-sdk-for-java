package io.github.vantiv.sdk;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.Properties;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;

import io.github.vantiv.sdk.generate.Activate;
import io.github.vantiv.sdk.generate.ActivateResponse;
import io.github.vantiv.sdk.generate.ActivateReversal;
import io.github.vantiv.sdk.generate.ActivateReversalResponse;
import io.github.vantiv.sdk.generate.AuthReversal;
import io.github.vantiv.sdk.generate.AuthReversalResponse;
import io.github.vantiv.sdk.generate.Authentication;
import io.github.vantiv.sdk.generate.Authorization;
import io.github.vantiv.sdk.generate.AuthorizationResponse;
import io.github.vantiv.sdk.generate.BalanceInquiry;
import io.github.vantiv.sdk.generate.BalanceInquiryResponse;
import io.github.vantiv.sdk.generate.CancelSubscription;
import io.github.vantiv.sdk.generate.CancelSubscriptionResponse;
import io.github.vantiv.sdk.generate.Capture;
import io.github.vantiv.sdk.generate.CaptureGivenAuth;
import io.github.vantiv.sdk.generate.CaptureGivenAuthResponse;
import io.github.vantiv.sdk.generate.CaptureResponse;
import io.github.vantiv.sdk.generate.CreatePlan;
import io.github.vantiv.sdk.generate.CreatePlanResponse;
import io.github.vantiv.sdk.generate.Credit;
import io.github.vantiv.sdk.generate.CreditResponse;
import io.github.vantiv.sdk.generate.Deactivate;
import io.github.vantiv.sdk.generate.DeactivateResponse;
import io.github.vantiv.sdk.generate.DeactivateReversal;
import io.github.vantiv.sdk.generate.DeactivateReversalResponse;
import io.github.vantiv.sdk.generate.DepositReversal;
import io.github.vantiv.sdk.generate.DepositReversalResponse;
import io.github.vantiv.sdk.generate.EcheckCredit;
import io.github.vantiv.sdk.generate.EcheckCreditResponse;
import io.github.vantiv.sdk.generate.EcheckRedeposit;
import io.github.vantiv.sdk.generate.EcheckRedepositResponse;
import io.github.vantiv.sdk.generate.EcheckSale;
import io.github.vantiv.sdk.generate.EcheckSalesResponse;
import io.github.vantiv.sdk.generate.EcheckVerification;
import io.github.vantiv.sdk.generate.EcheckVerificationResponse;
import io.github.vantiv.sdk.generate.EcheckVoid;
import io.github.vantiv.sdk.generate.EcheckVoidResponse;
import io.github.vantiv.sdk.generate.ForceCapture;
import io.github.vantiv.sdk.generate.ForceCaptureResponse;
import io.github.vantiv.sdk.generate.LitleOnlineRequest;
import io.github.vantiv.sdk.generate.LitleOnlineResponse;
import io.github.vantiv.sdk.generate.Load;
import io.github.vantiv.sdk.generate.LoadResponse;
import io.github.vantiv.sdk.generate.LoadReversal;
import io.github.vantiv.sdk.generate.LoadReversalResponse;
import io.github.vantiv.sdk.generate.RecurringTransactionResponseType;
import io.github.vantiv.sdk.generate.RefundReversal;
import io.github.vantiv.sdk.generate.RefundReversalResponse;
import io.github.vantiv.sdk.generate.RegisterTokenRequestType;
import io.github.vantiv.sdk.generate.RegisterTokenResponse;
import io.github.vantiv.sdk.generate.Sale;
import io.github.vantiv.sdk.generate.SaleResponse;
import io.github.vantiv.sdk.generate.TransactionTypeWithReportGroup;
import io.github.vantiv.sdk.generate.TransactionTypeWithReportGroupAndPartial;
import io.github.vantiv.sdk.generate.Unload;
import io.github.vantiv.sdk.generate.UnloadResponse;
import io.github.vantiv.sdk.generate.UnloadReversal;
import io.github.vantiv.sdk.generate.UnloadReversalResponse;
import io.github.vantiv.sdk.generate.UpdateCardValidationNumOnToken;
import io.github.vantiv.sdk.generate.UpdateCardValidationNumOnTokenResponse;
import io.github.vantiv.sdk.generate.UpdatePlan;
import io.github.vantiv.sdk.generate.UpdatePlanResponse;
import io.github.vantiv.sdk.generate.UpdateSubscription;
import io.github.vantiv.sdk.generate.UpdateSubscriptionResponse;
import io.github.vantiv.sdk.generate.VoidResponse;
import io.github.vantiv.sdk.generate.Void;

public class LitleOnline {

    private Properties config;
    private Communication communication;
    private Boolean removeStubs = false;

    /**
     * Construct a LitleOnline using the configuration specified in $HOME/.litle_SDK_config.properties
     */
    public LitleOnline() {

        communication = new Communication();

        try {
            config = new Properties();
            config.load(new FileInputStream((new Configuration()).location()));
        } catch (FileNotFoundException e) {
            throw new LitleOnlineException("Configuration file not found." +
                    " If you are not using the .litle_SDK_config.properties file," +
                    " please use the " + LitleOnline.class.getSimpleName() + "(Properties) constructor." +
                    " If you are using .litle_SDK_config.properties, you can generate one using java -jar litle-sdk-for-java-x.xx.jar", e);
        } catch (IOException e) {
            throw new LitleOnlineException("Configuration file could not be loaded.  Check to see if the user running this has permission to access the file", e);
        }
    }

    /**
     * Construct a LitleOnline specifying the configuration in code.  This should be used by integrations that
     * have another way to specify their configuration settings (ofbiz, etc)
     *
     * Properties that *must* be set are:
     *
     *  url (eg https://payments.litle.com/vap/communicator/online)
     *  reportGroup (eg "Default Report Group")
     *  username
     *  merchantId
     *  password
     *  version (eg 8.10)
     *  timeout (in milliseconds)
     *  Optional properties are:
     *  proxyHost
     *  proxyPort
     *  printxml (possible values "true" and "false" - defaults to false)
     *
     * @param config
     */
    public LitleOnline(Properties config) {
        this.config = config;
        communication = new Communication();
    }

    public LitleOnline(Properties config, Boolean removeStubs) {
        this.config = config;
        this.removeStubs = removeStubs;
        communication = new Communication();
    }

    protected void setCommunication(Communication communication) {
        this.communication = communication;
    }

    public AuthorizationResponse authorize(Authorization auth) throws LitleOnlineException {
        LitleOnlineRequest request = createLitleOnlineRequest();
        return authorize(auth, request);
    }

    public AuthorizationResponse authorize(Authorization auth, LitleOnlineRequest overrides) throws LitleOnlineException {
        LitleOnlineRequest request = fillInMissingFieldsFromConfig(overrides);
        fillInReportGroup(auth);

        request.setTransaction(LitleContext.getObjectFactory().createAuthorization(auth));
        LitleOnlineResponse response = sendToLitle(request);
        JAXBElement<? extends TransactionTypeWithReportGroup> newresponse = response.getTransactionResponse();
        return (AuthorizationResponse)newresponse.getValue();
    }

    public AuthReversalResponse authReversal(AuthReversal reversal) throws LitleOnlineException {
        LitleOnlineRequest request = createLitleOnlineRequest();
        return authReversal(reversal, request);
    }

    public AuthReversalResponse authReversal(AuthReversal reversal, LitleOnlineRequest overrides) throws LitleOnlineException {
        LitleOnlineRequest request = fillInMissingFieldsFromConfig(overrides);
        fillInReportGroup(reversal);

        request.setTransaction(LitleContext.getObjectFactory().createAuthReversal(reversal));
        LitleOnlineResponse response = sendToLitle(request);
        JAXBElement<? extends TransactionTypeWithReportGroup> newresponse = response.getTransactionResponse();
        return (AuthReversalResponse)newresponse.getValue();
    }

    public CaptureResponse capture(Capture capture) throws LitleOnlineException {
        LitleOnlineRequest request = createLitleOnlineRequest();
        return capture(capture, request);
    }

    public CaptureResponse capture(Capture capture, LitleOnlineRequest overrides) throws LitleOnlineException {
        LitleOnlineRequest request = fillInMissingFieldsFromConfig(overrides);
        fillInReportGroup(capture);

        request.setTransaction(LitleContext.getObjectFactory().createCapture(capture));
        LitleOnlineResponse response = sendToLitle(request);
        JAXBElement<? extends TransactionTypeWithReportGroup> newresponse = response.getTransactionResponse();
        return (CaptureResponse)newresponse.getValue();
    }

    public CaptureGivenAuthResponse captureGivenAuth(CaptureGivenAuth captureGivenAuth) throws LitleOnlineException {
        LitleOnlineRequest request = createLitleOnlineRequest();
        return captureGivenAuth(captureGivenAuth, request);
    }

    public CaptureGivenAuthResponse captureGivenAuth(CaptureGivenAuth captureGivenAuth, LitleOnlineRequest overrides) throws LitleOnlineException {
        LitleOnlineRequest request = fillInMissingFieldsFromConfig(overrides);
        fillInReportGroup(captureGivenAuth);

        request.setTransaction(LitleContext.getObjectFactory().createCaptureGivenAuth(captureGivenAuth));
        LitleOnlineResponse response = sendToLitle(request);
        JAXBElement<? extends TransactionTypeWithReportGroup> newresponse = response.getTransactionResponse();
        return (CaptureGivenAuthResponse)newresponse.getValue();
    }
    public CreditResponse credit(Credit credit) throws LitleOnlineException {
        LitleOnlineRequest request = createLitleOnlineRequest();
        return credit(credit, request);
    }

    public CreditResponse credit(Credit credit, LitleOnlineRequest overrides) throws LitleOnlineException {
        LitleOnlineRequest request = fillInMissingFieldsFromConfig(overrides);
        fillInReportGroup(credit);

        request.setTransaction(LitleContext.getObjectFactory().createCredit(credit));
        LitleOnlineResponse response = sendToLitle(request);
        JAXBElement<? extends TransactionTypeWithReportGroup> newresponse = response.getTransactionResponse();
        return (CreditResponse)newresponse.getValue();
    }

    public EcheckCreditResponse echeckCredit(EcheckCredit echeckcredit) throws LitleOnlineException {
        LitleOnlineRequest request = createLitleOnlineRequest();
        return echeckCredit(echeckcredit, request);
    }

    public EcheckCreditResponse echeckCredit(EcheckCredit echeckcredit, LitleOnlineRequest overrides) throws LitleOnlineException {
        LitleOnlineRequest request = fillInMissingFieldsFromConfig(overrides);
        fillInReportGroup(echeckcredit);

        request.setTransaction(LitleContext.getObjectFactory().createEcheckCredit(echeckcredit));
        LitleOnlineResponse response = sendToLitle(request);
        JAXBElement<? extends TransactionTypeWithReportGroup> newresponse = response.getTransactionResponse();
        return (EcheckCreditResponse)newresponse.getValue();
    }

    public EcheckRedepositResponse echeckRedeposit(EcheckRedeposit echeckRedeposit) throws LitleOnlineException {
        LitleOnlineRequest request = createLitleOnlineRequest();
        return echeckRedeposit(echeckRedeposit, request);
    }

    public EcheckRedepositResponse echeckRedeposit(EcheckRedeposit echeckRedeposit, LitleOnlineRequest overrides) throws LitleOnlineException {
        LitleOnlineRequest request = fillInMissingFieldsFromConfig(overrides);
        fillInReportGroup(echeckRedeposit);

        request.setTransaction(LitleContext.getObjectFactory().createEcheckRedeposit(echeckRedeposit));
        LitleOnlineResponse response = sendToLitle(request);
        JAXBElement<? extends TransactionTypeWithReportGroup> newresponse = response.getTransactionResponse();
        return (EcheckRedepositResponse)newresponse.getValue();
    }

    public EcheckSalesResponse echeckSale(EcheckSale echeckSale) throws LitleOnlineException {
        LitleOnlineRequest request = createLitleOnlineRequest();
        return echeckSale(echeckSale, request);
    }

    public EcheckSalesResponse echeckSale(EcheckSale echeckSale, LitleOnlineRequest overrides) throws LitleOnlineException {
        LitleOnlineRequest request = fillInMissingFieldsFromConfig(overrides);
        fillInReportGroup(echeckSale);

        request.setTransaction(LitleContext.getObjectFactory().createEcheckSale(echeckSale));
        LitleOnlineResponse response = sendToLitle(request);
        JAXBElement<? extends TransactionTypeWithReportGroup> newresponse = response.getTransactionResponse();
        return (EcheckSalesResponse)newresponse.getValue();
    }

    public EcheckVerificationResponse echeckVerification(EcheckVerification echeckVerification) throws LitleOnlineException {
        LitleOnlineRequest request = createLitleOnlineRequest();
        return echeckVerification(echeckVerification, request);
    }

    public EcheckVerificationResponse echeckVerification(EcheckVerification echeckVerification, LitleOnlineRequest overrides) throws LitleOnlineException {
        LitleOnlineRequest request = fillInMissingFieldsFromConfig(overrides);
        fillInReportGroup(echeckVerification);

        request.setTransaction(LitleContext.getObjectFactory().createEcheckVerification(echeckVerification));
        LitleOnlineResponse response = sendToLitle(request);
        JAXBElement<? extends TransactionTypeWithReportGroup> newresponse = response.getTransactionResponse();
        return (EcheckVerificationResponse)newresponse.getValue();
    }

    public ForceCaptureResponse forceCapture(ForceCapture forceCapture) throws LitleOnlineException {
        LitleOnlineRequest request = createLitleOnlineRequest();
        return forceCapture(forceCapture, request);
    }

    public ForceCaptureResponse forceCapture(ForceCapture forceCapture, LitleOnlineRequest overrides) throws LitleOnlineException {
        LitleOnlineRequest request = fillInMissingFieldsFromConfig(overrides);
        fillInReportGroup(forceCapture);

        request.setTransaction(LitleContext.getObjectFactory().createForceCapture(forceCapture));
        LitleOnlineResponse response = sendToLitle(request);
        JAXBElement<? extends TransactionTypeWithReportGroup> newresponse = response.getTransactionResponse();
        return (ForceCaptureResponse)newresponse.getValue();
    }

    public SaleResponse sale(Sale sale) throws LitleOnlineException {
        LitleOnlineRequest request = createLitleOnlineRequest();
        return sale(sale, request);
    }

    public SaleResponse sale(Sale sale, LitleOnlineRequest overrides) throws LitleOnlineException {
        LitleOnlineRequest request = fillInMissingFieldsFromConfig(overrides);
        fillInReportGroup(sale);

        request.setTransaction(LitleContext.getObjectFactory().createSale(sale));
        LitleOnlineResponse response = sendToLitle(request);
        JAXBElement<? extends TransactionTypeWithReportGroup> newresponse = response.getTransactionResponse();
        return (SaleResponse)newresponse.getValue();
    }

    public RegisterTokenResponse registerToken(RegisterTokenRequestType tokenRequest) throws LitleOnlineException {
        LitleOnlineRequest request = createLitleOnlineRequest();
        return registerToken(tokenRequest, request);
    }

    public RegisterTokenResponse registerToken(RegisterTokenRequestType tokenRequest, LitleOnlineRequest overrides) throws LitleOnlineException {
        LitleOnlineRequest request = fillInMissingFieldsFromConfig(overrides);
        fillInReportGroup(tokenRequest);

        request.setTransaction(LitleContext.getObjectFactory().createRegisterTokenRequest(tokenRequest));
        LitleOnlineResponse response = sendToLitle(request);
        JAXBElement<? extends TransactionTypeWithReportGroup> newresponse = response.getTransactionResponse();
        return (RegisterTokenResponse)newresponse.getValue();
    }

    public VoidResponse dovoid(Void v) throws LitleOnlineException {
        LitleOnlineRequest request = createLitleOnlineRequest();
        return dovoid(v, request);
    }

    public VoidResponse dovoid(Void v, LitleOnlineRequest overrides) throws LitleOnlineException {
        LitleOnlineRequest request = fillInMissingFieldsFromConfig(overrides);
        fillInReportGroup(v);

        request.setTransaction(LitleContext.getObjectFactory().createVoid(v));
        LitleOnlineResponse response = sendToLitle(request);
        JAXBElement<? extends TransactionTypeWithReportGroup> newresponse = response.getTransactionResponse();
        return (VoidResponse)newresponse.getValue();
    }

    public EcheckVoidResponse echeckVoid(EcheckVoid echeckVoid) throws LitleOnlineException {
        LitleOnlineRequest request = createLitleOnlineRequest();
        return echeckVoid(echeckVoid, request);
    }

    public EcheckVoidResponse echeckVoid(EcheckVoid echeckVoid, LitleOnlineRequest overrides) throws LitleOnlineException {
        LitleOnlineRequest request = fillInMissingFieldsFromConfig(overrides);
        fillInReportGroup(echeckVoid);

        request.setTransaction(LitleContext.getObjectFactory().createEcheckVoid(echeckVoid));
        LitleOnlineResponse response = sendToLitle(request);
        JAXBElement<? extends TransactionTypeWithReportGroup> newresponse = response.getTransactionResponse();
        return (EcheckVoidResponse)newresponse.getValue();
    }

    public UpdateCardValidationNumOnTokenResponse updateCardValidationNumOnToken(UpdateCardValidationNumOnToken update) {
        LitleOnlineRequest request = createLitleOnlineRequest();
        return updateCardValidationNumOnToken(update, request);
    }

    public UpdateCardValidationNumOnTokenResponse updateCardValidationNumOnToken(UpdateCardValidationNumOnToken update, LitleOnlineRequest overrides) {
        LitleOnlineRequest request = fillInMissingFieldsFromConfig(overrides);
        fillInReportGroup(update);

        request.setTransaction(LitleContext.getObjectFactory().createUpdateCardValidationNumOnToken(update));
        LitleOnlineResponse response = sendToLitle(request);
        JAXBElement<? extends TransactionTypeWithReportGroup> newresponse = response.getTransactionResponse();
        return (UpdateCardValidationNumOnTokenResponse)newresponse.getValue();
    }

    public CancelSubscriptionResponse cancelSubscription(CancelSubscription cancellation) {
        LitleOnlineRequest request = createLitleOnlineRequest();
        return cancelSubscription(cancellation, request);
    }

    public CancelSubscriptionResponse cancelSubscription(CancelSubscription cancellation, LitleOnlineRequest overrides) {
        LitleOnlineRequest request = fillInMissingFieldsFromConfig(overrides);

        request.setRecurringTransaction(LitleContext.getObjectFactory().createCancelSubscription(cancellation));
        LitleOnlineResponse response = sendToLitle(request);
        JAXBElement<? extends RecurringTransactionResponseType> newresponse = response.getRecurringTransactionResponse();
        return (CancelSubscriptionResponse)newresponse.getValue();
    }

    public UpdateSubscriptionResponse updateSubscription(UpdateSubscription update) {
        LitleOnlineRequest request = createLitleOnlineRequest();
        return updateSubscription(update, request);
    }

    public UpdateSubscriptionResponse updateSubscription(UpdateSubscription update, LitleOnlineRequest overrides) {
        LitleOnlineRequest request = fillInMissingFieldsFromConfig(overrides);

        request.setRecurringTransaction(LitleContext.getObjectFactory().createUpdateSubscription(update));
        LitleOnlineResponse response = sendToLitle(request);
        JAXBElement<? extends RecurringTransactionResponseType> newresponse = response.getRecurringTransactionResponse();
        return (UpdateSubscriptionResponse)newresponse.getValue();
    }

    public CreatePlanResponse createPlan(CreatePlan create) {
        LitleOnlineRequest request = createLitleOnlineRequest();
        return createPlan(create, request);
    }

    public CreatePlanResponse createPlan(CreatePlan create, LitleOnlineRequest overrides) {
        LitleOnlineRequest request = fillInMissingFieldsFromConfig(overrides);

        request.setRecurringTransaction(LitleContext.getObjectFactory().createCreatePlan(create));
        LitleOnlineResponse response = sendToLitle(request);
        JAXBElement<? extends RecurringTransactionResponseType> newresponse = response.getRecurringTransactionResponse();
        return (CreatePlanResponse)newresponse.getValue();
    }

    public UpdatePlanResponse updatePlan(UpdatePlan update) {
        LitleOnlineRequest request = createLitleOnlineRequest();
        return updatePlan(update, request);
    }

    public UpdatePlanResponse updatePlan(UpdatePlan update, LitleOnlineRequest overrides) {
        LitleOnlineRequest request = fillInMissingFieldsFromConfig(overrides);

        request.setRecurringTransaction(LitleContext.getObjectFactory().createUpdatePlan(update));
        LitleOnlineResponse response = sendToLitle(request);
        JAXBElement<? extends RecurringTransactionResponseType> newresponse = response.getRecurringTransactionResponse();
        return (UpdatePlanResponse)newresponse.getValue();
    }

    public ActivateResponse activate(Activate activate) {
        LitleOnlineRequest request = createLitleOnlineRequest();
        return activate(activate, request);
    }

    public ActivateResponse activate(Activate activate, LitleOnlineRequest overrides) {
        LitleOnlineRequest request = fillInMissingFieldsFromConfig(overrides);
        fillInReportGroup(activate);

        request.setTransaction(LitleContext.getObjectFactory().createActivate(activate));
        LitleOnlineResponse response = sendToLitle(request);
        JAXBElement<? extends TransactionTypeWithReportGroup> newresponse = response.getTransactionResponse();
        return (ActivateResponse)newresponse.getValue();
    }

    public DeactivateResponse deactivate(Deactivate deactivate) {
        LitleOnlineRequest request = createLitleOnlineRequest();
        return deactivate(deactivate, request);
    }

    public DeactivateResponse deactivate(Deactivate deactivate, LitleOnlineRequest overrides) {
        LitleOnlineRequest request = fillInMissingFieldsFromConfig(overrides);
        fillInReportGroup(deactivate);
        
        request.setTransaction(LitleContext.getObjectFactory().createDeactivate(deactivate));
        LitleOnlineResponse response = sendToLitle(request);
        JAXBElement<? extends TransactionTypeWithReportGroup> newresponse = response.getTransactionResponse();
        return (DeactivateResponse)newresponse.getValue();
    }

    public LoadResponse load(Load load) {
        LitleOnlineRequest request = createLitleOnlineRequest();
        return load(load, request);
    }

    public LoadResponse load(Load load, LitleOnlineRequest overrides) {
        LitleOnlineRequest request = fillInMissingFieldsFromConfig(overrides);
        fillInReportGroup(load);
        
        request.setTransaction(LitleContext.getObjectFactory().createLoad(load));
        LitleOnlineResponse response = sendToLitle(request);
        JAXBElement<? extends TransactionTypeWithReportGroup> newresponse = response.getTransactionResponse();
        return (LoadResponse)newresponse.getValue();
    }

    public UnloadResponse unload(Unload unload) {
        LitleOnlineRequest request = createLitleOnlineRequest();
        return unload(unload, request);
    }

    public UnloadResponse unload(Unload unload, LitleOnlineRequest overrides) {
        LitleOnlineRequest request = fillInMissingFieldsFromConfig(overrides);
        fillInReportGroup(unload);

        request.setTransaction(LitleContext.getObjectFactory().createUnload(unload));
        LitleOnlineResponse response = sendToLitle(request);
        JAXBElement<? extends TransactionTypeWithReportGroup> newresponse = response.getTransactionResponse();
        return (UnloadResponse)newresponse.getValue();
    }

    public BalanceInquiryResponse balanceInquiry(BalanceInquiry balanceInquiry) {
        LitleOnlineRequest request = createLitleOnlineRequest();
        return balanceInquiry(balanceInquiry, request);
    }

    public BalanceInquiryResponse balanceInquiry(BalanceInquiry balanceInquiry, LitleOnlineRequest overrides) {
        LitleOnlineRequest request = fillInMissingFieldsFromConfig(overrides);
        fillInReportGroup(balanceInquiry);
        
        request.setTransaction(LitleContext.getObjectFactory().createBalanceInquiry(balanceInquiry));
        LitleOnlineResponse response = sendToLitle(request);
        JAXBElement<? extends TransactionTypeWithReportGroup> newresponse = response.getTransactionResponse();
        return (BalanceInquiryResponse)newresponse.getValue();
    }

    public ActivateReversalResponse activateReversal(ActivateReversal activateReversal) {
        LitleOnlineRequest request = createLitleOnlineRequest();
        return activateReversal(activateReversal, request);
    }

    public ActivateReversalResponse activateReversal(ActivateReversal activateReversal, LitleOnlineRequest overrides) {
        LitleOnlineRequest request = fillInMissingFieldsFromConfig(overrides);
        fillInReportGroup(activateReversal);
        
        request.setTransaction(LitleContext.getObjectFactory().createActivateReversal(activateReversal));
        LitleOnlineResponse response = sendToLitle(request);
        JAXBElement<? extends TransactionTypeWithReportGroup> newresponse = response.getTransactionResponse();
        return (ActivateReversalResponse)newresponse.getValue();
    }

    public DeactivateReversalResponse deactivateReversal(DeactivateReversal deactivateReversal) {
        LitleOnlineRequest request = createLitleOnlineRequest();
        return deactivateReversal(deactivateReversal, request);
    }

    public DeactivateReversalResponse deactivateReversal(DeactivateReversal deactivateReversal, LitleOnlineRequest overrides) {
        LitleOnlineRequest request = fillInMissingFieldsFromConfig(overrides);
        fillInReportGroup(deactivateReversal);
        
        request.setTransaction(LitleContext.getObjectFactory().createDeactivateReversal(deactivateReversal));
        LitleOnlineResponse response = sendToLitle(request);
        JAXBElement<? extends TransactionTypeWithReportGroup> newresponse = response.getTransactionResponse();
        return (DeactivateReversalResponse)newresponse.getValue();
    }

    public LoadReversalResponse loadReversal(LoadReversal loadReversal) {
        LitleOnlineRequest request = createLitleOnlineRequest();
        return loadReversal(loadReversal, request);
    }

    public LoadReversalResponse loadReversal(LoadReversal loadReversal, LitleOnlineRequest overrides) {
        LitleOnlineRequest request = fillInMissingFieldsFromConfig(overrides);
        fillInReportGroup(loadReversal);
        
        request.setTransaction(LitleContext.getObjectFactory().createLoadReversal(loadReversal));
        LitleOnlineResponse response = sendToLitle(request);
        JAXBElement<? extends TransactionTypeWithReportGroup> newresponse = response.getTransactionResponse();
        return (LoadReversalResponse)newresponse.getValue();
    }

    public UnloadReversalResponse unloadReversal(UnloadReversal unloadReversal) {
        LitleOnlineRequest request = createLitleOnlineRequest();
        return unloadReversal(unloadReversal, request);
    }

    public UnloadReversalResponse unloadReversal(UnloadReversal unloadReversal, LitleOnlineRequest overrides) {
        LitleOnlineRequest request = fillInMissingFieldsFromConfig(overrides);
        fillInReportGroup(unloadReversal);
        request.setTransaction(LitleContext.getObjectFactory().createUnloadReversal(unloadReversal));
        LitleOnlineResponse response = sendToLitle(request);
        JAXBElement<? extends TransactionTypeWithReportGroup> newresponse = response.getTransactionResponse();
        return (UnloadReversalResponse)newresponse.getValue();
    }

    public RefundReversalResponse refundReversal(RefundReversal refundReversal) {
        LitleOnlineRequest request = createLitleOnlineRequest();
        return refundReversal(refundReversal, request);
    }

    public RefundReversalResponse refundReversal(RefundReversal refundReversal, LitleOnlineRequest overrides) {
        LitleOnlineRequest request = fillInMissingFieldsFromConfig(overrides);
        fillInReportGroup(refundReversal);
        request.setTransaction(LitleContext.getObjectFactory().createRefundReversal(refundReversal));
        LitleOnlineResponse response = sendToLitle(request);
        JAXBElement<? extends TransactionTypeWithReportGroup> newresponse = response.getTransactionResponse();
        return (RefundReversalResponse)newresponse.getValue();
    }

    public DepositReversalResponse depositReversal(DepositReversal depositReversal) {
        LitleOnlineRequest request = createLitleOnlineRequest();
        return depositReversal(depositReversal, request);
    }

    public DepositReversalResponse depositReversal(DepositReversal depositReversal, LitleOnlineRequest overrides) {
        LitleOnlineRequest request = fillInMissingFieldsFromConfig(overrides);
        fillInReportGroup(depositReversal);
        
        request.setTransaction(LitleContext.getObjectFactory().createDepositReversal(depositReversal));
        LitleOnlineResponse response = sendToLitle(request);
        JAXBElement<? extends TransactionTypeWithReportGroup> newresponse = response.getTransactionResponse();
        return (DepositReversalResponse)newresponse.getValue();
    }

    private LitleOnlineRequest createLitleOnlineRequest() {
        LitleOnlineRequest request = new LitleOnlineRequest();
        request.setMerchantId(config.getProperty("merchantId"));
        request.setVersion(config.getProperty("version"));
        Authentication authentication = new Authentication();
        authentication.setPassword(config.getProperty("password"));
        authentication.setUser(config.getProperty("username"));
        request.setLoggedInUser(config.getProperty("loggedInUser",null));
        request.setAuthentication(authentication);
        return request;
    }

    private LitleOnlineRequest fillInMissingFieldsFromConfig(LitleOnlineRequest request) {
        LitleOnlineRequest retVal = new LitleOnlineRequest();
        retVal.setAuthentication(new Authentication());
        if(request.getAuthentication() == null) {
            Authentication authentication = new Authentication();
            authentication.setPassword(config.getProperty("password"));
            authentication.setUser(config.getProperty("username"));
            retVal.setAuthentication(authentication);
        }
        else {
            if(request.getAuthentication().getUser() == null) {
                retVal.getAuthentication().setUser(config.getProperty("username"));
            }
            else {
                retVal.getAuthentication().setUser(request.getAuthentication().getUser());
            }
            if(request.getAuthentication().getPassword() == null) {
                retVal.getAuthentication().setPassword(config.getProperty("password"));
            }
            else {
                retVal.getAuthentication().setPassword(request.getAuthentication().getPassword());
            }
        }
        if(request.getMerchantId() == null) {
            retVal.setMerchantId(config.getProperty("merchantId"));
        }
        else {
            retVal.setMerchantId(request.getMerchantId());
        }
        retVal.setVersion(Versions.XML_VERSION);
        if(request.getMerchantSdk() == null) {
            retVal.setMerchantSdk(Versions.SDK_VERSION);
        }
        else {
            retVal.setMerchantSdk(request.getMerchantSdk());
        }

        if(request.getLoggedInUser() != null) {
            retVal.setLoggedInUser(request.getLoggedInUser());
        }

        return retVal;
    }

    private LitleOnlineResponse sendToLitle(LitleOnlineRequest request) throws LitleOnlineException {
        try {
            StringWriter sw = new StringWriter();
            LitleContext.getJAXBContext().createMarshaller().marshal(request, sw);
            String xmlRequest = sw.toString();

            if(this.removeStubs){
                xmlRequest = xmlRequest.replaceAll("<[A-Za-z]+\\s*/>", "");
            }
            String xmlResponse = communication.requestToServer(xmlRequest, config);
            
            //bandaid for a problem on the backend
            if(xmlResponse.contains("http://www.litle.com/schema/online")){
                xmlResponse = xmlResponse.replace("http://www.litle.com/schema/online", "http://www.litle.com/schema");
            }

            LitleOnlineResponse response = (LitleOnlineResponse)LitleContext.getJAXBContext().createUnmarshaller().unmarshal(new StringReader(xmlResponse));
            if("1".equals(response.getResponse())) {
                throw new LitleOnlineException(response.getMessage());
            }
            return response;
        } catch(JAXBException ume) {
            throw new LitleOnlineException("Error validating xml data against the schema", ume);
        } finally {
        }
    }

    private void fillInReportGroup(TransactionTypeWithReportGroup txn) {
        if(txn.getReportGroup() == null) {
            txn.setReportGroup(config.getProperty("reportGroup"));
        }
    }

    private void fillInReportGroup(TransactionTypeWithReportGroupAndPartial txn) {
        if(txn.getReportGroup() == null) {
            txn.setReportGroup(config.getProperty("reportGroup"));
        }
    }

}
