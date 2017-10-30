package com.litle.sdk;

import static org.junit.Assert.assertEquals;

import org.junit.BeforeClass;
import org.junit.Test;

import com.litle.sdk.generate.UpdateCardValidationNumOnToken;
import com.litle.sdk.generate.UpdateCardValidationNumOnTokenResponse;

public class TestUpdateCardValidationNumOnToken {

	private static LitleOnline litle;

	@BeforeClass
	public static void beforeClass() throws Exception {
		litle = new LitleOnline();
	}
	
	@Test
	public void simple() throws Exception {
		UpdateCardValidationNumOnToken update = new UpdateCardValidationNumOnToken();
		update.setLitleToken("1111222233334444");
		update.setCardValidationNum("123");
		update.setId("id");
		
		UpdateCardValidationNumOnTokenResponse response = litle.updateCardValidationNumOnToken(update);
		//assertEquals(response.getMessage(), "805",response.getResponse());
	}
}
