package com.litle.sdk;

import com.litle.sdk.generate.*;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.File;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

public class TestSDKhttpPooling {

	@BeforeClass
	public static void beforeClass() throws Exception {
	    System.setOut(new PrintStream(new File("/tmp/thread.out")));
	}

	@Test
	public void testThreads() throws Exception {
	    List<GetThread> threadList = new ArrayList<>();
	    for (int i = 0; i < 30; i++) {
	        threadList.add(new GetThread());
        }
	    for (GetThread thread: threadList) {
	       thread.start();
        }

        for(GetThread thread: threadList) {
	        thread.join();
        }
	}

	static class GetThread extends Thread {
        public LitleOnline litleOnline = new LitleOnline();
        private final int MAX_LOOPS = 10;
        long start = 0;
		@Override
		public void run() {
			try {
                int count = 0;
                long start = 0;

			    while (count < MAX_LOOPS) {
                    Authorization authorization = new Authorization();
                    authorization.setReportGroup(String.valueOf(this.getId()));
                    authorization.setOrderId(String.valueOf(count));
                    authorization.setAmount(106L);
                    authorization.setOrderSource(OrderSourceType.ECOMMERCE);
                    authorization.setId("id");
                    CardType card = new CardType();
                    card.setType(MethodOfPaymentTypeEnum.VI);
                    card.setNumber("4100000000000000");
                    card.setExpDate("1210");
                    authorization.setCard(card);

                    start = System.currentTimeMillis();
                    AuthorizationResponse response = litleOnline.authorize(authorization);
                    long end = System.currentTimeMillis();

                    assertEquals(String.valueOf(count), response.getOrderId());
                    assertEquals(String.valueOf(this.getId()), response.getReportGroup());

                    System.out.println("Auth duration: <" + (end - start) + "> millis for thread: <" +
                            this.getId() + "> Completed loop # <" + count + ">\n");
                    count++;
                }
			} catch (Exception ex) {
			    fail("Exception in thread: <" + this.getId() + ">");
                long duration = (System.currentTimeMillis() - start);
                System.out.println("**Exception in thread <" + this.getId() + "> duration: <" + duration + "> millis.\n" +
                        "       Exception running auth <" + ex.getStackTrace() + "> message: " + ex.getMessage());
			}
		}

	}



}
