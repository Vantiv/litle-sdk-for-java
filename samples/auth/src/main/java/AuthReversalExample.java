package com.litle.sdk.samples;
import com.litle.sdk.*;
import com.litle.sdk.generate.*;
 
public class AuthReversalExample {
    public static void main(String[] args) {
        AuthReversal authReversal = new AuthReversal();
        //litleTxnId contains the Litle Transaction Id returned on the auth
        authReversal.setLitleTxnId(100000000000000001L);
        AuthReversalResponse response = new LitleOnline().authReversal(authReversal);
        //Display Results
        System.out.println("Response: " + response.getResponse());
        System.out.println("Message: " + response.getMessage());
        System.out.println("Litle Transaction ID: " + response.getLitleTxnId());

	// In your sample, you can ignore this
        if(!response.getMessage().equals("Transaction Received"))
        throw new RuntimeException(" The AuthReversalExample does not give the right response");
       
    }
}


