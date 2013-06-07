package com.litle.sdk;

import com.litle.sdk.generate.CardType;
import com.litle.sdk.generate.Credit;
import com.litle.sdk.generate.MethodOfPaymentTypeEnum;
import com.litle.sdk.generate.OrderSourceType;

public class Test {

    /**
     * @param args
     */
    public static void main(String[] args) {
        LitleBatchFileRequest batchfile = new LitleBatchFileRequest("testFile.xml");

        LitleBatchRequest batch = batchfile.createBatch("101");

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

        LitleBatchFileResponse resp = batchfile.sendToLitleSFTP();
//        LitleBatchResponse resp2 = resp.getNextLitleBatchResponse();
//        while(resp2.processNextTransaction(new LitleResponseProcessor(){
//            public void processAuthorizationResponse(AuthorizationResponse authorizationResponse) { }
//
//            public void processCaptureResponse(CaptureResponse captureResponse) { }
//
//            public void processForceCaptureResponse(ForceCaptureResponse forceCaptureResponse) { }
//
//            public void processCaptureGivenAuthResponse(CaptureGivenAuthResponse captureGivenAuthResponse) { }
//
//            public void processSaleResponse(SaleResponse saleResponse) { }
//
//            public void processCreditResponse(CreditResponse creditResponse) {
//                System.out.println(creditResponse.getLitleTxnId());
//                System.out.println(creditResponse.getResponseTime().toString());
//                System.out.println(creditResponse.getMessage());
//            }
//
//            public void processEcheckSalesResponse(EcheckSalesResponse echeckSalesResponse) { }
//
//            public void processEcheckCreditResponse(EcheckCreditResponse echeckCreditResponse) { }
//
//            public void processEcheckVerificationResponse(EcheckVerificationResponse echeckVerificationResponse) { }
//
//            public void processEcheckRedepositResponse(EcheckRedepositResponse echeckRedepositResponse) { }
//
//            public void processAuthReversalResponse(AuthReversalResponse authReversalResponse) { }
//
//            public void processRegisterTokenResponse(RegisterTokenResponse registerTokenResponse) { }
//        })){
//            System.out.println("Processed another txn!");
//        }

    }

}
