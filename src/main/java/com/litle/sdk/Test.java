package com.litle.sdk;

import java.util.Calendar;

import com.litle.sdk.generate.*;

public class Test {

    /**
     * @param args
     */
    public static void main(String[] args) {
        String merchantId = "07103229";

        RFRRequest rfr = new RFRRequest();
        AccountUpdateFileRequestData data = new AccountUpdateFileRequestData();
        data.setMerchantId(merchantId);
        data.setPostDay(Calendar.getInstance());
        rfr.setAccountUpdateFileRequestData(data);

        LitleRFRFileRequest rfrFile = new LitleRFRFileRequest("test-rfr.xml", rfr);
        LitleRFRFileResponse responseFile = rfrFile.sendToLitleSFTP();
        LitleRFRResponse response = responseFile.getLitleRFRResponse();
        System.out.println("RFR Response Code: " + response.getRFRResponseCode());
        System.out.println("RFR Response Message: " + response.getRFRResponseMessage());



        LitleBatchFileRequest batchfile = new LitleBatchFileRequest("testFile.xml");

        LitleBatchRequest batch = batchfile.createBatch(merchantId);

        Credit credit = new Credit();
        credit.setAmount(106L);
        credit.setOrderId("hurrrr");
        credit.setOrderSource(OrderSourceType.TELEPHONE);
        credit.setReportGroup("Planets");
        CardType card = new CardType();
        card.setType(MethodOfPaymentTypeEnum.VI);
        card.setNumber("4100000000000001");
        card.setExpDate("1210");

        credit.setCard(card);
        batch.addTransaction(credit);

        LitleBatchFileResponse fileResponse = batchfile.sendToLitleSFTP();
        LitleBatchResponse batchResponse;

        //iterate over all batch responses in the file
        while((batchResponse = fileResponse.getNextLitleBatchResponse()) != null){
            //iterate over all transactions in the file with a custom response processor
            while(batchResponse.processNextTransaction(new LitleResponseProcessor(){
                public void processAuthorizationResponse(AuthorizationResponse authorizationResponse) { }

                public void processCaptureResponse(CaptureResponse captureResponse) { }

                public void processForceCaptureResponse(ForceCaptureResponse forceCaptureResponse) { }

                public void processCaptureGivenAuthResponse(CaptureGivenAuthResponse captureGivenAuthResponse) { }

                public void processSaleResponse(SaleResponse saleResponse) { }

                public void processCreditResponse(CreditResponse creditResponse) {
                    System.out.println(creditResponse.getLitleTxnId()); 
                    System.out.println(creditResponse.getResponseTime().toString());
                    System.out.println(creditResponse.getMessage());
                }

                public void processEcheckSalesResponse(EcheckSalesResponse echeckSalesResponse) { }

                public void processEcheckCreditResponse(EcheckCreditResponse echeckCreditResponse) { }

                public void processEcheckVerificationResponse(EcheckVerificationResponse echeckVerificationResponse) { }

                public void processEcheckRedepositResponse(EcheckRedepositResponse echeckRedepositResponse) { }

                public void processAuthReversalResponse(AuthReversalResponse authReversalResponse) { }

                public void processRegisterTokenResponse(RegisterTokenResponse registerTokenResponse) { }

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
                
                public void processGiftCardAuthReversalResponse(
                		GiftCardAuthReversalResponse giftCardAuthReversalResponse) {
                }
                
				public void processGiftCardCaptureResponse(GiftCardCaptureResponse giftCardCaptureResponse) {
				}
				
				public void processGiftCardCreditResponse(GiftCardCreditResponse giftCardCreditResponse) {
				}
                	
            })){
                System.out.println("Processed another txn!");
            }
        }

    }

}
