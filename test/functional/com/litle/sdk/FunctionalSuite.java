package com.litle.sdk;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)
@Suite.SuiteClasses({
	TestAuth.class,
	TestAuthReversal.class,
	TestCapture.class,
	TestCaptureGivenAuth.class,
	TestCredit.class,
	TestEcheckCredit.class,
	TestEcheckRedeposit.class,
	TestEcheckSale.class,
	TestEcheckVerification.class,
	TestForceCapture.class,
	TestSale.class,
	TestToken.class
})
public class FunctionalSuite {
}
