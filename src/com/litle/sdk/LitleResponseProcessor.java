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

}