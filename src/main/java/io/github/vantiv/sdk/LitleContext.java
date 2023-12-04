package io.github.vantiv.sdk;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;

import io.github.vantiv.sdk.generate.ObjectFactory;

/**
 * A factory that encapsulates singleton instances of a Litle JAXB Context and ObjectFactory.
 * @author stephenhall
 *
 */
public class LitleContext {

	private static final JAXBContext jaxbContext = initJAXBContext();
	
	private static final ObjectFactory objectFactory = initObjectFactory();
	
	private static JAXBContext initJAXBContext() {
		try {
			return JAXBContext.newInstance("io.github.vantiv.sdk.generate");
		} catch (JAXBException e) {
			throw new LitleOnlineException("Unable to load jaxb dependencies.  Perhaps a classpath issue?", e);
		}
	}
	
	private static ObjectFactory initObjectFactory() {
		return new ObjectFactory();
	}

	public static JAXBContext getJAXBContext() {
		return jaxbContext;
	}
	
	public static ObjectFactory getObjectFactory() {
		return objectFactory;
	}
	
}
