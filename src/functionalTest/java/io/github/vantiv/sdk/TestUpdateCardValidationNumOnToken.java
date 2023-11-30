package io.github.vantiv.sdk;

import static org.junit.Assert.assertEquals;

import org.junit.BeforeClass;
import org.junit.Test;

import io.github.vantiv.sdk.generate.UpdateCardValidationNumOnToken;
import io.github.vantiv.sdk.generate.UpdateCardValidationNumOnTokenResponse;

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
		
		UpdateCardValidationNumOnTokenResponse response = litle.updateCardValidationNumOnToken(update);
		assertEquals(response.getMessage(), "805",response.getResponse());
	}
}
