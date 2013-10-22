package com.litle.sdk;

import com.litle.sdk.generate.AccountUpdateResponse;
import com.litle.sdk.generate.AuthReversalResponse;
import com.litle.sdk.generate.AuthorizationResponse;
import com.litle.sdk.generate.CancelSubscriptionResponse;
import com.litle.sdk.generate.CaptureGivenAuthResponse;
import com.litle.sdk.generate.CaptureResponse;
import com.litle.sdk.generate.CreditResponse;
import com.litle.sdk.generate.EcheckCreditResponse;
import com.litle.sdk.generate.EcheckRedepositResponse;
import com.litle.sdk.generate.EcheckSalesResponse;
import com.litle.sdk.generate.EcheckVerificationResponse;
import com.litle.sdk.generate.ForceCaptureResponse;
import com.litle.sdk.generate.RegisterTokenResponse;
import com.litle.sdk.generate.SaleResponse;
import com.litle.sdk.generate.UpdateCardValidationNumOnTokenResponse;
import com.litle.sdk.generate.UpdateSubscriptionResponse;

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

}
