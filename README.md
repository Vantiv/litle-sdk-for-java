Litle Online Java SDK
=====================

About Litle
------------
[Litle &amp; Co.](http://www.litle.com) powers the payment processing engines for leading companies that sell directly to consumers through  internet retail, direct response marketing (TV, radio and telephone), and online services. Litle & Co. is the leading, independent authority in card-not-present (CNP) commerce, transaction processing and merchant services.


About this SDK
--------------
The Litle Java SDK is a Java implementation of the [Litle &amp; Co.](http://www.litle.com). XML API. This SDK was created to make it as easy as possible to connect process your payments with Litle.  This SDK utilizes  the HTTPS protocol to securely connect to Litle.  Using the SDK requires coordination with the Litle team in order to be provided with credentials for accessing our systems.

Our Java SDK supports all of the functionality present in Litle XML v8. Please see the online copy of our XSD for Litle XML to get more details on what is supported by the Litle payments engine.

This SDK is implemented to support the Java programming language and was created by Litle & Co. It is intended use is for online transactions processing utilizing your account on the Litle payments engine.

See LICENSE file for details on using this software.

Source Code available from : https://github.com/LitleCo/litle-sdk-for-java

Please contact [Litle &amp; Co.](http://www.litle.com) to receive valid merchant credentials in order to run tests successfully or if you require assistance in any way.  We are reachable at sdksupport@litle.com

Setup
-----

1) Place the litle-sdk-for-java.jar in your classpath 

2) Once the sdk is installed run our setup program to generate a configuration file.  The configuration file resides in your home directory
$HOME/.litle_SDK_config.properties

3.) Create a java class similar to:  

```java
import com.litle.sdk.*;
import com.litle.sdk.generated.*

public class SampleLitleTxn {

	public static void main(String[] args) {

		// Visa $10 Sale
		Sale sale = new Sale();
		sale.setReportGroup("Planets");
		sale.setOrderId("12344");
		sale.setAmount(1000L);
		sale.setOrderSource(OrderSourceType.ECOMMERCE);
		CardType card = new CardType();
		card.setType("VI");
		card.setNumber("4100000000000001");
		card.setExpDate("1210");
		sale.setCard(card);
		
		# Peform the transaction on the Litle Platform
		SaleResponse response = new LitleOnline().sale(sale);

		# display result
		System.out.println("Message: " + response.getMessage());
		System.out.println("Litle Transaction ID: " + response.getLitleTxnId());
	}
}
```

4) Compile and run this file.  You should see the following result provided you have connectivity to the Litle certification environment.  You will see an HTTP error if you don't have access to the Litle URL

    Message: Approved
    Litle Transaction ID: <your-numeric-litle-txn-id>


Please contact Lilte & Co. with any further questions.   You can reach us at sdksupport@litle.com.
