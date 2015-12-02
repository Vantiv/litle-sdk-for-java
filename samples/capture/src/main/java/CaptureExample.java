package com.litle.sdk.samples;
import com.litle.sdk.*;
import com.litle.sdk.generate.*;
 
public class CaptureExample {
    public static void main(String[] args) {
        Capture capture = new Capture();
        //litleTxnId contains the Litle Transaction Id returned on the authorization
        capture.setLitleTxnId(100000000000000011L);
        capture.setId("id");
        CaptureResponse response = new LitleOnline().capture(capture);
        //Display Results
        System.out.println("Response: " + response.getResponse());
        System.out.println("Message: " + response.getMessage());
        System.out.println("Litle Transaction ID: " + response.getLitleTxnId());
	if(!response.getMessage().equals("Transaction Received"))
        throw new RuntimeException(" The CaptureExample does not give the right response");
    }
}



