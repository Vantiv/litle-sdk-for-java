package com.litle.sdk;

import java.io.StringReader;
import java.io.StringWriter;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

import com.litle.sdk.generate.Authentication;
import com.litle.sdk.generate.Authorization;
import com.litle.sdk.generate.LitleOnlineRequest;
import com.litle.sdk.generate.LitleOnlineResponse;
import com.litle.sdk.generate.ObjectFactory;

public class LitleOnline {
	
	private static JAXBContext jc;
	static {
		try {
			jc = JAXBContext.newInstance("com.litle.sdk.generate");
		} catch (JAXBException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	//TODO This shouldn't throw "Exception"
	public LitleOnlineResponse authorize(Authorization auth) throws Exception {
		LitleOnlineRequest request = new LitleOnlineRequest();
		request.setMerchantId("101");
		request.setVersion("8.10");
		Authentication authentication = new Authentication();
		authentication.setPassword("password");
		authentication.setUser("user");
		request.setAuthentication(authentication);
		
		ObjectFactory o = new ObjectFactory();
		request.setTransaction(o.createAuthorization(auth));
		
		Marshaller m = jc.createMarshaller();
		StringWriter sw = new StringWriter();
		m.marshal(request, sw);
		String xmlRequest = sw.toString();
		System.out.println(xmlRequest);
		
		String xmlResponse = new Communication().requestToServer(xmlRequest);
		System.out.println(xmlResponse);
		Unmarshaller u = jc.createUnmarshaller();
		LitleOnlineResponse response = (LitleOnlineResponse)u.unmarshal(new StringReader(xmlResponse));
		System.out.println(response.getMessage());
		return response;
	}

}
