package io.github.vantiv.sdk;

import static org.junit.Assert.assertEquals;

import org.junit.BeforeClass;
import org.junit.Test;

import io.github.vantiv.sdk.generate.AdvancedFraudChecksType;
import io.github.vantiv.sdk.generate.AdvancedFraudResultsType;
import io.github.vantiv.sdk.generate.FraudCheck;
import io.github.vantiv.sdk.generate.FraudCheckResponse;

public class TestFraudCheck {

    private static LitleOnline litle;

    @BeforeClass
    public static void beforeClass() throws Exception {
        litle = new LitleOnline();
    }
    
    @Test
    public void testFraudCheck() throws Exception {
        FraudCheck fraudCheck = new FraudCheck();
        AdvancedFraudChecksType advancedFraudChecks = new AdvancedFraudChecksType();
        advancedFraudChecks.setThreatMetrixSessionId("123");
        fraudCheck.setAdvancedFraudChecks(advancedFraudChecks);
        FraudCheckResponse fraudCheckResponse = litle.fraudCheck(fraudCheck);
        
        //System.out.println(fraudCheckResponse.getMessage());
        
        AdvancedFraudResultsType advancedFraudResultsType = fraudCheckResponse.getAdvancedFraudResults();
        assertEquals("pass", advancedFraudResultsType.getDeviceReviewStatus());
        assertEquals(new Integer(42), advancedFraudResultsType.getDeviceReputationScore());
        assertEquals(1, advancedFraudResultsType.getTriggeredRules().size());
    }
}
