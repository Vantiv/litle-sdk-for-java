//package com.litle.sdk;
//
//import static org.junit.Assert.*;
//import static org.junit.Assert.assertEquals;
//
//import java.io.BufferedOutputStream;
//import java.io.BufferedReader;
//import java.io.File;
//import java.io.FileReader;
//import java.io.OutputStreamWriter;
//import java.util.ArrayList;
//import java.util.Iterator;
//import java.util.List;
//import java.util.Scanner;
//
//import javax.xml.bind.JAXBElement;
//
//import org.junit.Before;
//import org.junit.Test;
//import static org.mockito.Mockito.*;
//
//
//import com.litle.sdk.generate.TransactionTypeWithReportGroup;
//
//public class TestLitleBatchResponseTransactionTypeIterator {
//
//	private static LitleBatchResponseTransactionTypeIterator lbrtti;
//	List<JAXBElement<? extends TransactionTypeWithReportGroup>> mockList = mock(List.class);
//	Iterator<JAXBElement<? extends TransactionTypeWithReportGroup>> mockIterator = mock(Iterator.class);
//	
//	@Before
//	public void before() throws Exception {
//		when(mockList.iterator()).thenReturn(mockIterator);
//		lbrtti = new LitleBatchResponseTransactionTypeIterator(mockList);	
//	}
//	
//	@Test
//	public void testHasNext() throws Exception {
//		when(mockIterator.hasNext()).thenReturn(true);
//		assertTrue(lbrtti.hasNext());
//	}
//	
//	@Test
//	public void testNext() throws Exception {
//		JAXBElement mock = mock(JAXBElement.class);
//		when(mockIterator.next()).thenReturn(mock);
//		when(mock.getValue()).thenReturn(null);
//		
//		TransactionTypeWithReportGroup ttwrg = new TransactionTypeWithReportGroup();
//		ttwrg.setCustomerId("hello");
//		
//	}
//}
