package io.github.vantiv.sdk.samples;
import io.github.vantiv.sdk.*;
import io.github.vantiv.sdk.generate.*;

public class CreditExample {
    public static void main(String[] args) {
        Credit credit = new Credit();
        //litleTxnId contains the Litle Transaction Id returned on the deposit
        credit.setLitleTxnId(100000000000000011L);
        CreditResponse response = new LitleOnline().credit(credit);
        //Display Results
        System.out.println("Response: " + response.getResponse());
        System.out.println("Message: " + response.getMessage());
        System.out.println("Litle Transaction ID: " + response.getLitleTxnId());
	if(!response.getMessage().equals("Approved"))
        throw new RuntimeException(" The CreditExample does not give the right response");
    }
}
