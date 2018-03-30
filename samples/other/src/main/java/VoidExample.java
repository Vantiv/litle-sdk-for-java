package com.litle.sdk.samples;
import com.litle.sdk.*;
import com.litle.sdk.generate.*;
 
public class VoidExample {
    public static void main(String[] args) {
        com.litle.sdk.generate.Void theVoid = new com.litle.sdk.generate.Void();
        theVoid.setId("id");
        //litleTxnId contains the Litle Transaction Id returned on the deposit
        theVoid.setLitleTxnId(100000000000000000L);
        VoidResponse response = new LitleOnline().dovoid(theVoid);
        //Display Results
        System.out.println("Response: " + response.getResponse());
        System.out.println("Message: " + response.getMessage());
        System.out.println("Litle Transaction ID: " + response.getLitleTxnId());
	if(!response.getMessage().equals("Approved"))
        throw new RuntimeException(" The VoidExample does not give the right response");
    }
} 

