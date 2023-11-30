package io.github.vantiv.sdk.samples;
import io.github.vantiv.sdk.*;
import io.github.vantiv.sdk.generate.*;

public class CapturePartialExample {
    public static void main(String[] args) {
        Capture capture = new Capture();
        //litleTxnId contains the Litle Transaction Id returned on the authorization 
        capture.setLitleTxnId(100000000000000011L);
        capture.setAmount(1200L); //Capture $12 dollars of a previous authorization
        capture.setId("id");
        CaptureResponse response = new LitleOnline().capture(capture);
        //Display Results
        System.out.println("Response: " + response.getResponse());
        System.out.println("Message: " + response.getMessage());
        System.out.println("Litle Transaction ID: " + response.getLitleTxnId());
	if(!response.getMessage().equals("Transaction Received"))
        throw new RuntimeException(" The CapturePartialExample does not give the right response");
    }
}

