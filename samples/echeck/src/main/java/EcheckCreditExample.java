package com.litle.sdk.samples;
import com.litle.sdk.*;
import com.litle.sdk.generate.*;
 
public class EcheckCreditExample {
    public static void main(String[] args) {
	EcheckCredit echeckcredit = new EcheckCredit();
	echeckcredit.setId("id");
        //LitleTxnId from an earlier echeck sale
	echeckcredit.setLitleTxnId(123456789101112L);
 
        EcheckCreditResponse response = new LitleOnline().echeckCredit(echeckcredit);
        //Display Results
        System.out.println("Response: " + response.getResponse());
        System.out.println("Message: " + response.getMessage());
        System.out.println("Litle Transaction ID: " + response.getLitleTxnId());
	if(!response.getMessage().equals("Transaction Received"))
        throw new RuntimeException(" The EcheckCreditExample does not give the right response");
    }
}
