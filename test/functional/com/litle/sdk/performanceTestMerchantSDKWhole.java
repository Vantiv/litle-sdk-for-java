package com.litle.sdk;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Statement;
import java.util.Calendar;

import com.litle.sdk.LitleBatchFileRequest;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.junit.BeforeClass;
import org.junit.Test;

import com.litle.sdk.generate.Authorization;
import com.litle.sdk.generate.Capture;
import com.litle.sdk.generate.CaptureResponse;
import com.litle.sdk.generate.CardTokenType;
import com.litle.sdk.generate.CardType;
import com.litle.sdk.generate.Contact;
import com.litle.sdk.generate.CountryTypeEnum;
import com.litle.sdk.generate.CurrencyCodeEnum;
import com.litle.sdk.generate.CustomerInfo;
import com.litle.sdk.generate.CustomerInfo.CustomerType;
import com.litle.sdk.generate.CustomerInfo.ResidenceStatus;
import com.litle.sdk.generate.MethodOfPaymentTypeEnum;
import com.litle.sdk.generate.OrderSourceType;
import com.litle.sdk.generate.PayPal;
import com.litle.sdk.generate.Sale;
import com.litle.sdk.generate.SaleResponse;

public class performanceTestMerchantSDKWhole {
	private static LitleBatchFileRequest litleBatchFileRequest;
	
	
    public static void main(String[] args) {
    	litleBatchFileRequest = new LitleBatchFileRequest("testFile.xml");
    	LitleBatchRequest litleBatchRequest = litleBatchFileRequest.createBatch("101");
    	String litleTxnId = "82818409740294007";
    	String surcharge = "100000000";
    	String customerIncome = "100000"; 
    	Calendar date = Calendar.getInstance();
    	date.set(Calendar.YEAR, 1999);
    	date.set(Calendar.MONTH, 7);
    	date.set(Calendar.DAY_OF_MONTH, 26);
    	Contact ct = null;
    	
    	for (int i =0; i < 50 ; i++)
		{
    		Sale sale = new Sale();
    		sale.setReportGroup("1111111111111111111111111");
    		sale.setId("1234567890123456789012345");
    		sale.setLitleTxnId(Long.valueOf(litleTxnId));
    		sale.setAmount(106L);
    		sale.setSurchargeAmount(Long.valueOf(surcharge));
    		sale.setOrderId("1234567890123456789012345");
    		sale.setOrderSource(OrderSourceType.ECOMMERCE);
    		
    		CustomerInfo ci = new CustomerInfo();
    		ci.setSsn("033894657");
    		ci.setDob(date);
    		ci.setCustomerCheckingAccount(false);
    	    ci.setCustomerRegistrationDate(date);
            ci.setCustomerType(CustomerType.EXISTING);
    	    ci.setCustomerSavingAccount(true);
    	    ci.setCustomerWorkTelephone("789-999-0000");
    	    ci.setEmployerName("Johnnnnnnnnnnnnnnnnnnnnnnnnnnnn");
    	    ci.setIncomeAmount(Long.valueOf(customerIncome));
    	    ci.setIncomeCurrency(CurrencyCodeEnum.CAD);
    	    ci.setResidenceStatus(ResidenceStatus.OWN);
    	    ci.setYearsAtEmployer(10);
    	    ci.setYearsAtResidence(10);
    	    
    	    sale.setCustomerInfo(ci);
    	    sale.setAllowPartialAuth(true);
    	    
    	   
    	    
    	    ct.setAddressLine1("aaaaaAAAAABBBBBGGGGGGGGGGKKKKKKKKKK");
    	    ct.setAddressLine2("aaaaaAAAAABBBBBGGGGGGGGGGKKKKKKKKKK");
    	    ct.setAddressLine3("aaaaaAAAAABBBBBGGGGGGGGGGKKKKKKKKKK");
    	    ct.setCity("aaaaaAAAAABBBBBGGGGGGGGGGKKKKKKKKKKXXXXX");
    	    ct.setCompanyName("AAAAABBBBBNNNNNMMMMMLLLLLLLLLLUUUUUTTTTT");
    	    ct.setCountry(CountryTypeEnum.AD);
    	    ct.setEmail("ssssssssssssssssssssssssssssssssssssssssssssssstest@emailcsdfafdasdf.com");
    	   /* ct.setFirstName(value);
    	    ct.setlas
    	    
    	    
    	    sale.setBillToAddress(value)
    	    
    	    */
    		CardType card = new CardType();
    		card.setType(MethodOfPaymentTypeEnum.VI);
    		card.setNumber("4100000000000002");
    		card.setExpDate("1210");
    		sale.setCard(card);
		
    		litleBatchRequest.addTransaction(sale);
		}
		
		litleBatchFileRequest.sendToLitle();
 		System.out.println("done");
 	}
	
}

