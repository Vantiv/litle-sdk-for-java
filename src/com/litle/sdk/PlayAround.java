package com.litle.sdk;

import java.io.FileInputStream;
import java.io.StringReader;
import java.io.StringWriter;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

import com.litle.sdk.generate.Authentication;
import com.litle.sdk.generate.Authorization;
import com.litle.sdk.generate.CardType;
import com.litle.sdk.generate.LitleOnlineRequest;
import com.litle.sdk.generate.LitleOnlineResponse;
import com.litle.sdk.generate.ObjectFactory;

public class PlayAround {
	
	private static JAXBContext jc;

	public static void main(String[] args) throws Exception {
//		jc = JAXBContext.newInstance("com.litle.sdk.generate");
//		
//		Unmarshaller u = jc.createUnmarshaller();
//		LitleOnlineResponse response810 = (LitleOnlineResponse)u.unmarshal(new FileInputStream("samples/authResponse8.10.0.xml"));
//		System.out.println(response810.getMessage());
//		
//		LitleOnlineResponse response81Element = (LitleOnlineResponse)u.unmarshal(new FileInputStream("samples/authResponse8.10.withNewElement.xml"));
//		System.out.println(response81Element.getMessage());
//
//		LitleOnlineResponse response81Attribute = (LitleOnlineResponse)u.unmarshal(new FileInputStream("samples/authResponse8.10.withNewAttribute.xml"));
//		System.out.println(response81Attribute.getMessage());
//
//		Authorization authorization = new Authorization();
//		authorization.setLitleTxnId(111122223333444455L);
//		CardType card = new CardType();
//		card.setNumber("4100000000000001");
//		authorization.setCard(card);
//		authorize(authorization);
	}
}
