package com.litle.sdk;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)
@Suite.SuiteClasses({
	TestCert1Base.class,
	TestCert2AuthEnhanced.class,
	TestCert3AuthReversal.class,
	TestCert4Echeck.class,
	TestCert5Token.class
})
public class CertificationSuite {
}
