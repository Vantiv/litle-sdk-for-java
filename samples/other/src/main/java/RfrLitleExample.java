package com.litle.sdk.samples;
import com.litle.sdk.*;
import com.litle.sdk.generate.AccountUpdateFileRequestData;
import com.litle.sdk.generate.RFRRequest;
import java.util.Calendar;
public class RfrLitleExample {
    public static void main(String[] args) {
        String merchantId = "0180";
        String requestFileName = "litleSdk-testRFRFile-fileConfigSFTP.xml";
        RFRRequest rfrRequest = new RFRRequest();
        AccountUpdateFileRequestData data = new AccountUpdateFileRequestData();
        data.setMerchantId(merchantId);
        data.setPostDay(Calendar.getInstance());
        rfrRequest.setAccountUpdateFileRequestData(data);
         
        LitleRFRFileRequest request = new LitleRFRFileRequest(requestFileName, rfrRequest); 
        try{
            LitleRFRFileResponse response = request.sendToLitleSFTP();
            String message=response.getLitleRFRResponse().getRFRResponseMessage();
        }
        catch(Exception e){       
        }
    }
}
