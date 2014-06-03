package com.litle.sdk;

import static org.junit.Assert.assertSame;

import javax.xml.bind.JAXBContext;

import org.junit.Test;

import com.litle.sdk.generate.ObjectFactory;

public class TestLitleContext {

    @Test
    public void testGetJAXBContextReturnsSameObject() {
        JAXBContext context1 = LitleContext.getJAXBContext();
        JAXBContext context2 = LitleContext.getJAXBContext();

        assertSame(context1, context2);
    }

    @Test
    public void testGetObjectFactoryReturnsSameObject() {
        ObjectFactory factory1 = LitleContext.getObjectFactory();
        ObjectFactory factory2 = LitleContext.getObjectFactory();

        assertSame(factory1, factory2);
    }

}
