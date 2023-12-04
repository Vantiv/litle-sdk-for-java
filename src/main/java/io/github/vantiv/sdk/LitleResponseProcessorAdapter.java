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

public class LitleResponseProcessorAdapter implements LitleResponseProcessor {

    public void processAuthorizationResponse(AuthorizationResponse authorizationResponse) {
    }

    public void processCaptureResponse(CaptureResponse captureResponse) {
    }

    public void processForceCaptureResponse(ForceCaptureResponse forceCaptureResponse) {
    }

    public void processCaptureGivenAuthResponse(CaptureGivenAuthResponse captureGivenAuthResponse) {
    }

    public void processSaleResponse(SaleResponse saleResponse) {
    }

    public void processCreditResponse(CreditResponse creditResponse) {
    }

    public void processEcheckSalesResponse(EcheckSalesResponse echeckSalesResponse) {
    }

    public void processEcheckCreditResponse(EcheckCreditResponse echeckCreditResponse) {
    }

    public void processEcheckVerificationResponse(EcheckVerificationResponse echeckVerificationResponse) {
    }

    public void processEcheckRedepositResponse(EcheckRedepositResponse echeckRedepositResponse) {
    }

    public void processAuthReversalResponse(AuthReversalResponse authReversalResponse) {
    }

    public void processRegisterTokenResponse(RegisterTokenResponse registerTokenResponse) {
    }

    public void processAccountUpdate(AccountUpdateResponse accountUpdateResponse) {
    }

    public void processUpdateSubscriptionResponse(UpdateSubscriptionResponse updateSubscriptionResponse) {
    }

    public void processCancelSubscriptionResponse(CancelSubscriptionResponse cancelSubscriptionResponse) {
    }

    public void processUpdateCardValidationNumOnTokenResponse(
            UpdateCardValidationNumOnTokenResponse updateCardValidationNumOnTokenResponse) {
    }

    public void processCreatePlanResponse(CreatePlanResponse createPlanResponse) {
    }

    public void processUpdatePlanResponse(UpdatePlanResponse updatePlanResponse) {
    }

    public void processActivateResponse(ActivateResponse activateResponse) {
    }

    public void processDeactivateResponse(DeactivateResponse deactivateResponse) {
    }

    public void processLoadResponse(LoadResponse loadResponse) {
    }

    public void processUnloadResponse(UnloadResponse unloadResponse) {
    }

    public void processBalanceInquiryResponse(BalanceInquiryResponse balanceInquiryResponse) {
    }

    public void processEcheckPreNoteSaleResponse(
            EcheckPreNoteSaleResponse echeckPreNoteSaleResponse) {
    }

    public void processEcheckPreNoteCreditResponse(
            EcheckPreNoteCreditResponse echeckPreNoteCreditResponse) {
    }

    public void processSubmerchantCreditResponse(
            SubmerchantCreditResponse submerchantCreditResponse) {
        }

    public void processPayFacCreditResponse(
            PayFacCreditResponse payFacCreditResponse) {
        }

    public void processVendorCreditRespsonse(
            VendorCreditResponse vendorCreditResponse) {
        }

    public void processReserveCreditResponse(
            ReserveCreditResponse reserveCreditResponse) {
        }

    public void processPhysicalCheckCreditResponse(
            PhysicalCheckCreditResponse checkCreditResponse) {
        }

    public void processSubmerchantDebitResponse(
            SubmerchantDebitResponse submerchantDebitResponse) {
        }

    public void processPayFacDebitResponse(
            PayFacDebitResponse payFacDebitResponse) {
        }

    public void processVendorDebitResponse(
            VendorDebitResponse vendorDebitResponse) {
        }

    public void processReserveDebitResponse(
            ReserveDebitResponse reserveDebitResponse) {
        }

    public void processPhysicalCheckDebitResponse(
            PhysicalCheckDebitResponse checkDebitResponse) {
        }
    
    public void processFundingInstructionVoidResponse(
            FundingInstructionVoidResponse fundingInstructionVoidResponse) {
        }

}
