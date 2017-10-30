package com.litle.sdk;

import static org.junit.Assert.assertEquals;

import org.junit.BeforeClass;
import org.junit.Test;

import com.litle.sdk.generate.UpdatePlan;
import com.litle.sdk.generate.UpdatePlanResponse;

public class TestUpdatePlan {

    private static LitleOnline litle;

    @BeforeClass
    public static void beforeClass() throws Exception {
        litle = new LitleOnline();
    }

    @Test
    public void simpleupdaatePlan() throws Exception {
       UpdatePlan updatePlan=new UpdatePlan();
       updatePlan.setActive(true);
       updatePlan.setPlanCode("Monthly");


       UpdatePlanResponse response=litle.updatePlan(updatePlan);
         assertEquals("Approved", response.getMessage());

    }


}
