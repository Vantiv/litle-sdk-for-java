package com.litle.sdk;


import static org.junit.Assert.assertEquals;

import org.junit.BeforeClass;
import org.junit.Test;

import com.litle.sdk.generate.CreatePlan;
import com.litle.sdk.generate.CreatePlanResponse;
import com.litle.sdk.generate.IntervalTypeEnum;

public class TestCreatePlan {

    private static LitleOnline litle;

    @BeforeClass
    public static void beforeClass() throws Exception {
        litle = new LitleOnline();
    }

    @Test
    public void simpleCreatePlan() throws Exception {
       CreatePlan createPlan=new CreatePlan();
       createPlan.setPlanCode("Monthly");
       createPlan.setName("abc");
       createPlan.setIntervalType(IntervalTypeEnum.MONTHLY);
       createPlan.setAmount(1995l);
       createPlan.setNumberOfPayments(5);
       createPlan.setTrialNumberOfIntervals(2);
       CreatePlanResponse response=litle.createPlan(createPlan);
      
         assertEquals("Approved", response.getMessage());

    }


}
