package com.litle.sdk.samples;
import com.litle.sdk.*;
import com.litle.sdk.generate.*;
 
public class RegisterTokenExample {
    public static void main(String[] args) {
        RegisterTokenRequestType registerToken = new RegisterTokenRequestType();
	registerToken.setOrderId("12344");
	registerToken.setAccountNumber("1233456789103801");
	RegisterTokenResponse response = new LitleOnline().registerToken(registerToken);
 
        //Display Results
        System.out.println("Response: " + response.getResponse());
        System.out.println("Message: " + response.getMessage());
        System.out.println("Token: " + response.getLitleToken());
        if(!response.getMessage().equals("Account number was successfully registered"))
        throw new RuntimeException(" The RegisterTokenExample does not give the right response");
    }
}
