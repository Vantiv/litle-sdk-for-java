package io.github.vantiv.sdk;

import static org.junit.Assert.assertEquals;

import org.junit.BeforeClass;
import org.junit.Test;
import io.github.vantiv.sdk.generate.*;

public class TestFraudCheck {

    private static LitleOnline litle;

    @BeforeClass
    public static void beforeClass() throws Exception {
        litle = new LitleOnline();
    }
    
    @Test
    public void testFraudCheck() throws Exception {
        FraudCheck fraudCheck = new FraudCheck();
        fraudCheck.setId("1");
        AdvancedFraudChecksType advancedFraudChecks = new AdvancedFraudChecksType();
        advancedFraudChecks.setThreatMetrixSessionId("123");
        advancedFraudChecks.setCustomAttribute1("pass");
        advancedFraudChecks.setCustomAttribute2("42");
        advancedFraudChecks.setCustomAttribute3("5");
        fraudCheck.setAdvancedFraudChecks(advancedFraudChecks);
        FraudCheckResponse fraudCheckResponse = litle.fraudCheck(fraudCheck);
        
        //System.out.println(fraudCheckResponse.getMessage());
        
        AdvancedFraudResultsType advancedFraudResultsType = fraudCheckResponse.getAdvancedFraudResults();
        assertEquals("pass", advancedFraudResultsType.getDeviceReviewStatus());
        assertEquals(new Integer(42), advancedFraudResultsType.getDeviceReputationScore());
        assertEquals(5, advancedFraudResultsType.getTriggeredRules().size());
    }
}
