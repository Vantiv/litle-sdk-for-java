package io.github.vantiv.sdk;

import io.github.vantiv.sdk.generate.AccountUpdateResponse;
import io.github.vantiv.sdk.generate.ActivateResponse;
import io.github.vantiv.sdk.generate.AuthReversalResponse;
import io.github.vantiv.sdk.generate.AuthorizationResponse;
import io.github.vantiv.sdk.generate.BalanceInquiryResponse;
import io.github.vantiv.sdk.generate.CancelSubscriptionResponse;
import io.github.vantiv.sdk.generate.CaptureGivenAuthResponse;
import io.github.vantiv.sdk.generate.CaptureResponse;
import io.github.vantiv.sdk.generate.CreatePlanResponse;
import io.github.vantiv.sdk.generate.CreditResponse;
import io.github.vantiv.sdk.generate.DeactivateResponse;
import io.github.vantiv.sdk.generate.EcheckCreditResponse;
import io.github.vantiv.sdk.generate.EcheckPreNoteCreditResponse;
import io.github.vantiv.sdk.generate.EcheckPreNoteSaleResponse;
import io.github.vantiv.sdk.generate.EcheckRedepositResponse;
import io.github.vantiv.sdk.generate.EcheckSalesResponse;
import io.github.vantiv.sdk.generate.EcheckVerificationResponse;
import io.github.vantiv.sdk.generate.ForceCaptureResponse;
import io.github.vantiv.sdk.generate.FundingInstructionVoidResponse;
import io.github.vantiv.sdk.generate.LoadResponse;
import io.github.vantiv.sdk.generate.PayFacCreditResponse;
import io.github.vantiv.sdk.generate.PayFacDebitResponse;
import io.github.vantiv.sdk.generate.PhysicalCheckCreditResponse;
import io.github.vantiv.sdk.generate.PhysicalCheckDebitResponse;
import io.github.vantiv.sdk.generate.RegisterTokenResponse;
import io.github.vantiv.sdk.generate.ReserveCreditResponse;
import io.github.vantiv.sdk.generate.ReserveDebitResponse;
import io.github.vantiv.sdk.generate.SaleResponse;
import io.github.vantiv.sdk.generate.SubmerchantCreditResponse;
import io.github.vantiv.sdk.generate.SubmerchantDebitResponse;
import io.github.vantiv.sdk.generate.UnloadResponse;
import io.github.vantiv.sdk.generate.UpdateCardValidationNumOnTokenResponse;
import io.github.vantiv.sdk.generate.UpdatePlanResponse;
import io.github.vantiv.sdk.generate.UpdateSubscriptionResponse;
import io.github.vantiv.sdk.generate.VendorCreditResponse;
import io.github.vantiv.sdk.generate.VendorDebitResponse;

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