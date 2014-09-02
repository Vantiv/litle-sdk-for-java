package com.litle.sdk.samples;
import com.litle.sdk.*;
import com.litle.sdk.generate.*;
 
public class PaypageRegistrationIdToTokenExample {
    public static void main(String[] args) {
	RegisterTokenRequestType tokenRequest = new RegisterTokenRequestType();
	tokenRequest.setOrderId("12345");
        //The paypageRegistrationId is received in the form posted from your checkout page with paypage enabled
	tokenRequest.setPaypageRegistrationId("123456789012345678901324567890abcdefghi");
	RegisterTokenResponse tokenResponse = new LitleOnline().registerToken(tokenRequest);
        //Display Results
        System.out.println("Token: " + tokenResponse.getLitleToken());
	System.out.println("Response: " + tokenResponse.getMessage());
        if(!tokenResponse.getMessage().equals("Account number was successfully registered"))
        throw new RuntimeException(" The PaypageRegistrationIdToTokenExample does not give the right response");
    }
}
