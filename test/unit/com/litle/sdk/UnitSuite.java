package com.litle.sdk;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)
@Suite.SuiteClasses({
	TestEnumerations.class,
	TestLitleOnline.class,
	TestLitleBatchFileRequest.class,
	TestLitleBatchRequest.class,
	TestLitleBatchResponse.class,
	TestLitleRFRResponse.class
})
public class UnitSuite {
}
