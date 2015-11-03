package com.litle.sdk;

import com.litle.sdk.generate.AccountUpdateResponse;
import com.litle.sdk.generate.ActivateResponse;
import com.litle.sdk.generate.AuthReversalResponse;
import com.litle.sdk.generate.AuthorizationResponse;
import com.litle.sdk.generate.BalanceInquiryResponse;
import com.litle.sdk.generate.CancelSubscriptionResponse;
import com.litle.sdk.generate.CaptureGivenAuthResponse;
import com.litle.sdk.generate.CaptureResponse;
import com.litle.sdk.generate.CreatePlanResponse;
import com.litle.sdk.generate.CreditResponse;
import com.litle.sdk.generate.DeactivateResponse;
import com.litle.sdk.generate.EcheckCreditResponse;
import com.litle.sdk.generate.EcheckPreNoteCreditResponse;
import com.litle.sdk.generate.EcheckPreNoteSaleResponse;
import com.litle.sdk.generate.EcheckRedepositResponse;
import com.litle.sdk.generate.EcheckSalesResponse;
import com.litle.sdk.generate.EcheckVerificationResponse;
import com.litle.sdk.generate.ForceCaptureResponse;
import com.litle.sdk.generate.FundingInstructionVoid;
import com.litle.sdk.generate.FundingInstructionVoidResponse;
import com.litle.sdk.generate.LoadResponse;
import com.litle.sdk.generate.PayFacCreditResponse;
import com.litle.sdk.generate.PayFacDebitResponse;
import com.litle.sdk.generate.PhysicalCheckCreditResponse;
import com.litle.sdk.generate.PhysicalCheckDebitResponse;
import com.litle.sdk.generate.RegisterTokenResponse;
import com.litle.sdk.generate.ReserveCreditResponse;
import com.litle.sdk.generate.ReserveDebitResponse;
import com.litle.sdk.generate.SaleResponse;
import com.litle.sdk.generate.SubmerchantCreditResponse;
import com.litle.sdk.generate.SubmerchantDebitResponse;
import com.litle.sdk.generate.UnloadResponse;
import com.litle.sdk.generate.UpdateCardValidationNumOnTokenResponse;
import com.litle.sdk.generate.UpdatePlanResponse;
import com.litle.sdk.generate.UpdateSubscriptionResponse;
import com.litle.sdk.generate.VendorCreditResponse;
import com.litle.sdk.generate.VendorDebitResponse;

/**
 * Implement this interface in order to process transactions on LitleBatchResponse objects with the .processNextTransaction method.
 * @author ahammond
 */
public interface LitleResponseProcessor {

    void processAuthorizationResponse(AuthorizationResponse authorizationResponse);

    void processCaptureResponse(CaptureResponse captureResponse);

    void processForceCaptureResponse(ForceCaptureResponse forceCaptureResponse);

    void processCaptureGivenAuthResponse(CaptureGivenAuthResponse captureGivenAuthResponse);

    void processSaleResponse(SaleResponse saleResponse);

    void processCreditResponse(CreditResponse creditResponse);

    void processEcheckSalesResponse(EcheckSalesResponse echeckSalesResponse);

    void processEcheckCreditResponse(EcheckCreditResponse echeckCreditResponse);

    void processEcheckVerificationResponse(EcheckVerificationResponse echeckVerificationResponse);

    void processEcheckRedepositResponse(EcheckRedepositResponse echeckRedepositResponse);

    void processAuthReversalResponse(AuthReversalResponse authReversalResponse);

    void processRegisterTokenResponse(RegisterTokenResponse registerTokenResponse);

    void processAccountUpdate(AccountUpdateResponse accountUpdateResponse);

    void processUpdateSubscriptionResponse(UpdateSubscriptionResponse updateSubscriptionResponse);

    void processCancelSubscriptionResponse(CancelSubscriptionResponse cancelSubscriptionResponse);

    void processUpdateCardValidationNumOnTokenResponse(
            UpdateCardValidationNumOnTokenResponse updateCardValidationNumOnTokenResponse);

    void processCreatePlanResponse(CreatePlanResponse createPlanResponse);

    void processUpdatePlanResponse(UpdatePlanResponse updatePlanResponse);

    void processActivateResponse(ActivateResponse activateResponse);

    void processDeactivateResponse(DeactivateResponse deactivateResponse);

    void processLoadResponse(LoadResponse loadResponse);

    void processUnloadResponse(UnloadResponse unloadResponse);

    void processBalanceInquiryResponse(BalanceInquiryResponse balanceInquiryResponse);

    void processEcheckPreNoteSaleResponse(EcheckPreNoteSaleResponse echeckPreNoteSaleResponse);
    
    void processEcheckPreNoteCreditResponse(EcheckPreNoteCreditResponse echeckPreNoteCreditResponse);
    
    void processSubmerchantCreditResponse(SubmerchantCreditResponse submerchantCreditResponse);
    
    void processPayFacCreditResponse(PayFacCreditResponse payFacCreditResponse);
    
    void processVendorCreditRespsonse(VendorCreditResponse vendorCreditResponse);
    
    void processReserveCreditResponse(ReserveCreditResponse reserveCreditResponse);
    
    void processPhysicalCheckCreditResponse(PhysicalCheckCreditResponse checkCreditResponse);
    
    void processSubmerchantDebitResponse(SubmerchantDebitResponse submerchantDebitResponse);
    
    void processPayFacDebitResponse(PayFacDebitResponse payFacDebitResponse);
    
    void processVendorDebitResponse(VendorDebitResponse vendorDebitResponse);
    
    void processReserveDebitResponse(ReserveDebitResponse reserveDebitResponse);
    
    void processPhysicalCheckDebitResponse(PhysicalCheckDebitResponse checkDebitResponse);
    
    void processFundingInstructionVoidResponse(FundingInstructionVoidResponse fundingInstructionVoidResponse);
}