package com.litle.sdk;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.Properties;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.UnmarshalException;
import javax.xml.bind.Unmarshaller;

import com.litle.sdk.generate.AuthReversal;
import com.litle.sdk.generate.AuthReversalResponse;
import com.litle.sdk.generate.Authentication;
import com.litle.sdk.generate.Authorization;
import com.litle.sdk.generate.AuthorizationResponse;
import com.litle.sdk.generate.Capture;
import com.litle.sdk.generate.CaptureGivenAuth;
import com.litle.sdk.generate.CaptureGivenAuthResponse;
import com.litle.sdk.generate.CaptureResponse;
import com.litle.sdk.generate.Credit;
import com.litle.sdk.generate.CreditResponse;
import com.litle.sdk.generate.EcheckRedeposit;
import com.litle.sdk.generate.EcheckRedepositResponse;
import com.litle.sdk.generate.LitleOnlineRequest;
import com.litle.sdk.generate.LitleOnlineResponse;
import com.litle.sdk.generate.ObjectFactory;
import com.litle.sdk.generate.TransactionTypeWithReportGroup;

public class LitleOnline {
	
	private static JAXBContext jc;
	private static Properties config;
	static {
		try {
			jc = JAXBContext.newInstance("com.litle.sdk.generate");
		} catch (JAXBException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			config = new Properties();
			config.load(new FileInputStream(Configuration.location()));
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	//TODO This shouldn't throw "Exception"
	public AuthorizationResponse authorize(Authorization auth) throws Exception {
		LitleOnlineRequest request = new LitleOnlineRequest();
		request.setMerchantId(config.getProperty("merchantId"));
		request.setVersion(config.getProperty("version"));
		Authentication authentication = new Authentication();
		authentication.setPassword(config.getProperty("password"));
		authentication.setUser(config.getProperty("username"));
		if(auth.getReportGroup() == null) {
			auth.setReportGroup(config.getProperty("reportGroup")); 
		}
		request.setAuthentication(authentication);
		
		ObjectFactory o = new ObjectFactory();
		request.setTransaction(o.createAuthorization(auth));
		
		Marshaller m = jc.createMarshaller();
		StringWriter sw = new StringWriter();
		m.marshal(request, sw);
		String xmlRequest = sw.toString();
		
		String xmlResponse = new Communication().requestToServer(xmlRequest, config);
		Unmarshaller u = jc.createUnmarshaller();
		try {
			LitleOnlineResponse response = (LitleOnlineResponse)u.unmarshal(new StringReader(xmlResponse));
			JAXBElement<? extends TransactionTypeWithReportGroup> newresponse = response.getTransactionResponse();
			return (AuthorizationResponse)newresponse.getValue();
		} catch(UnmarshalException ume) {
			AuthorizationResponse response = new AuthorizationResponse();
			response.setMessage("Error validating xml data against the schema: " + ume.getMessage());
			return response;
		}
	}

	public AuthReversalResponse authReversal(AuthReversal reversal) throws Exception {
		LitleOnlineRequest request = new LitleOnlineRequest();
		request.setMerchantId(config.getProperty("merchantId"));
		request.setVersion(config.getProperty("version"));
		Authentication authentication = new Authentication();
		authentication.setPassword(config.getProperty("password"));
		authentication.setUser(config.getProperty("username"));
		if(reversal.getReportGroup() == null) {
			reversal.setReportGroup(config.getProperty("reportGroup")); 
		}
		request.setAuthentication(authentication);
		
		ObjectFactory o = new ObjectFactory();
		request.setTransaction(o.createAuthReversal(reversal));
		
		Marshaller m = jc.createMarshaller();
		StringWriter sw = new StringWriter();
		m.marshal(request, sw);
		String xmlRequest = sw.toString();
		
		String xmlResponse = new Communication().requestToServer(xmlRequest, config);
		Unmarshaller u = jc.createUnmarshaller();
		try {
			LitleOnlineResponse response = (LitleOnlineResponse)u.unmarshal(new StringReader(xmlResponse));
			JAXBElement<? extends TransactionTypeWithReportGroup> newresponse = response.getTransactionResponse();
			return (AuthReversalResponse)newresponse.getValue();
		} catch(UnmarshalException ume) {
			AuthReversalResponse response = new AuthReversalResponse();
			response.setMessage("Error validating xml data against the schema: " + ume.getMessage());
			return response;
		}
	}
	
	public CaptureResponse capture(Capture capture) throws Exception {
		LitleOnlineRequest request = new LitleOnlineRequest();
		request.setMerchantId(config.getProperty("merchantId"));
		request.setVersion(config.getProperty("version"));
		Authentication authentication = new Authentication();
		authentication.setPassword(config.getProperty("password"));
		authentication.setUser(config.getProperty("username"));
		if(capture.getReportGroup() == null) {
			capture.setReportGroup(config.getProperty("reportGroup")); 
		}
		request.setAuthentication(authentication);
		
		ObjectFactory o = new ObjectFactory();
		request.setTransaction(o.createCapture(capture));
		
		Marshaller m = jc.createMarshaller();
		StringWriter sw = new StringWriter();
		m.marshal(request, sw);
		String xmlRequest = sw.toString();
		
		String xmlResponse = new Communication().requestToServer(xmlRequest, config);
		Unmarshaller u = jc.createUnmarshaller();
		try {
			LitleOnlineResponse response = (LitleOnlineResponse)u.unmarshal(new StringReader(xmlResponse));
			JAXBElement<? extends TransactionTypeWithReportGroup> newresponse = response.getTransactionResponse();
			return (CaptureResponse)newresponse.getValue();
		} catch(UnmarshalException ume) {
			CaptureResponse response = new CaptureResponse();
			response.setMessage("Error validating xml data against the schema: " + ume.getMessage());
			return response;
		}
	}
	
	public CaptureGivenAuthResponse capturegivenauth(CaptureGivenAuth capturegivenauth) throws Exception {
		LitleOnlineRequest request = new LitleOnlineRequest();
		request.setMerchantId(config.getProperty("merchantId"));
		request.setVersion(config.getProperty("version"));
		Authentication authentication = new Authentication();
		authentication.setPassword(config.getProperty("password"));
		authentication.setUser(config.getProperty("username"));
		if(capturegivenauth.getReportGroup() == null) {
			capturegivenauth.setReportGroup(config.getProperty("reportGroup")); 
		}
		request.setAuthentication(authentication);
		
		ObjectFactory o = new ObjectFactory();
		request.setTransaction(o.createCaptureGivenAuth(capturegivenauth));
		
		Marshaller m = jc.createMarshaller();
		StringWriter sw = new StringWriter();
		m.marshal(request, sw);
		String xmlRequest = sw.toString();
		
		String xmlResponse = new Communication().requestToServer(xmlRequest, config);
		Unmarshaller u = jc.createUnmarshaller();
		try {
			LitleOnlineResponse response = (LitleOnlineResponse)u.unmarshal(new StringReader(xmlResponse));
			JAXBElement<? extends TransactionTypeWithReportGroup> newresponse = response.getTransactionResponse();
			return (CaptureGivenAuthResponse)newresponse.getValue();
		} catch(UnmarshalException ume) {
			CaptureGivenAuthResponse response = new CaptureGivenAuthResponse();
			response.setMessage("Error validating xml data against the schema: " + ume.getMessage());
			return response;
		}
	}
	
	public CreditResponse credit(Credit credit) throws Exception {
		LitleOnlineRequest request = new LitleOnlineRequest();
		request.setMerchantId(config.getProperty("merchantId"));
		request.setVersion(config.getProperty("version"));
		Authentication authentication = new Authentication();
		authentication.setPassword(config.getProperty("password"));
		authentication.setUser(config.getProperty("username"));
		if(credit.getReportGroup() == null) {
			credit.setReportGroup(config.getProperty("reportGroup")); 
		}
		request.setAuthentication(authentication);
		
		ObjectFactory o = new ObjectFactory();
		request.setTransaction(o.createCredit(credit));
		
		Marshaller m = jc.createMarshaller();
		StringWriter sw = new StringWriter();
		m.marshal(request, sw);
		String xmlRequest = sw.toString();
		
		String xmlResponse = new Communication().requestToServer(xmlRequest, config);
		Unmarshaller u = jc.createUnmarshaller();
		try {
			LitleOnlineResponse response = (LitleOnlineResponse)u.unmarshal(new StringReader(xmlResponse));
			JAXBElement<? extends TransactionTypeWithReportGroup> newresponse = response.getTransactionResponse();
			return (CreditResponse)newresponse.getValue();
		} catch(UnmarshalException ume) {
			CreditResponse response = new CreditResponse();
			response.setMessage("Error validating xml data against the schema: " + ume.getMessage());
			return response;
		}
	}
	
	public EcheckRedepositResponse echeckredeposit(EcheckRedeposit echeckredeposit) throws Exception {
		LitleOnlineRequest request = new LitleOnlineRequest();
		request.setMerchantId(config.getProperty("merchantId"));
		request.setVersion(config.getProperty("version"));
		Authentication authentication = new Authentication();
		authentication.setPassword(config.getProperty("password"));
		authentication.setUser(config.getProperty("username"));
		if(echeckredeposit.getReportGroup() == null) {
			echeckredeposit.setReportGroup(config.getProperty("reportGroup")); 
		}
		request.setAuthentication(authentication);
		
		ObjectFactory o = new ObjectFactory();
		request.setTransaction(o.createEcheckRedeposit(echeckredeposit));
		
		Marshaller m = jc.createMarshaller();
		StringWriter sw = new StringWriter();
		m.marshal(request, sw);
		String xmlRequest = sw.toString();
		
		String xmlResponse = new Communication().requestToServer(xmlRequest, config);
		Unmarshaller u = jc.createUnmarshaller();
		try {
			LitleOnlineResponse response = (LitleOnlineResponse)u.unmarshal(new StringReader(xmlResponse));
			JAXBElement<? extends TransactionTypeWithReportGroup> newresponse = response.getTransactionResponse();
			return (EcheckRedepositResponse)newresponse.getValue();
		} catch(UnmarshalException ume) {
			EcheckRedepositResponse response = new EcheckRedepositResponse();
			response.setMessage("Error validating xml data against the schema: " + ume.getMessage());
			return response;
		}
	}
	

}
