Litle Online Java SDK
=====================

About Litle
------------
[Litle &amp; Co.](http://www.litle.com) powers the payment processing engines for leading companies that sell directly to consumers through  internet retail, direct response marketing (TV, radio and telephone), and online services. Litle & Co. is the leading, independent authority in card-not-present (CNP) commerce, transaction processing and merchant services.


About this SDK
--------------
The Litle Java SDK is a Java implementation of the [Litle &amp; Co.](http://www.litle.com) XML API. This SDK was created to make it as easy as possible to process your payments with Litle.  This SDK utilizes  the HTTPS protocol to securely connect to Litle.  Using the SDK requires coordination with the Litle team in order to be provided with credentials for accessing our systems.

See LICENSE file for details on using this software.

Please contact [Litle &amp; Co.](http://www.litle.com) to receive valid merchant credentials in order to run tests successfully or if you require assistance in any way.  We are reachable at sdksupport@litle.com

Setup
-----

1. Add our Bintray repository to your Maven or Gradle build: `http://dl.bintray.com/litlesdk/maven`
2. Add the dependency
    1. For Maven:
        ```
            <dependency>
                <groupId>com.litle</groupId>
                <artifactId>litle-sdk-for-java</artifactId>
                <version>8.25.4</version>
            </dependency>
        ```
    2. For Gradle:
        `compile(group: 'com.litle', name: 'litle-sdk-for-java', version: '8.25.4')`
    
3. Create your configuration file with one of the following
    * Run `java -jar /path/to/litle-sdk-for-java.jar` and answer the questions, or
    * Add a file `.litle_SDK_config.properties` to your home directory with the correct properties in it
4. Use it:

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
		card.setType(MethodOfPaymentTypeEnum.VI);
		card.setNumber("4100000000000002");
		card.setExpDate("1210");
		sale.setCard(card);
		
		// Peform the transaction on the Litle Platform
		SaleResponse response = new LitleOnline().sale(sale);

		// display result
		System.out.println("Message: " + response.getMessage());
		System.out.println("Litle Transaction ID: " + response.getLitleTxnId());
	}
}
```

More examples can be found here [Java Gists](https://gist.github.com/litleSDK)