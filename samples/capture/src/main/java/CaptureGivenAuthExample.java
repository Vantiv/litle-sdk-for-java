package io.github.vantiv.sdk.samples;
import io.github.vantiv.sdk.*;
import java.util.Calendar;
import io.github.vantiv.sdk.generate.*;

public class CaptureGivenAuthExample {
    public static void main(String[] args) {
        CaptureGivenAuth capturegivenauth = new CaptureGivenAuth();
	capturegivenauth.setAmount(106L);
	capturegivenauth.setOrderId("12344");
	AuthInformation authInfo = new AuthInformation();
	Calendar authDate = Calendar.getInstance();
	authDate.set(2002, Calendar.OCTOBER, 9);
	authInfo.setAuthDate(authDate);
	authInfo.setAuthCode("543216");
	authInfo.setAuthAmount(12345L);
	capturegivenauth.setAuthInformation(authInfo);
	capturegivenauth.setOrderSource(OrderSourceType.ECOMMERCE);
	CardType card = new CardType();
	card.setType(MethodOfPaymentTypeEnum.VI);
	card.setNumber("4100000000000000");
	card.setExpDate("1210");
	capturegivenauth.setCard(card);
	capturegivenauth.setId("id");
        CaptureGivenAuthResponse response = new LitleOnline().captureGivenAuth(capturegivenauth);
        //Display Results
        System.out.println("Response: " + response.getResponse());
        System.out.println("Message: " + response.getMessage());
        System.out.println("Litle Transaction ID: " + response.getLitleTxnId());
	if(!response.getMessage().equals("Approved"))
        throw new RuntimeException(" The CaptureGivenAuthExample does not give the right response");
    }
}
