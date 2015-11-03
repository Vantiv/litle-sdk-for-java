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
