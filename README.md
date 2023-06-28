Vantiv eCommerce Java SDK
=====================
<span style="color:red">

### Important Support Note
</span>

***Worldpay provides SDK updates for releases in the latest major version stream. We do not provide SDK updates to older releases, unless mandated by the card brands. All SDKs are open source, allowing you to update older versions as needed.***

#### Warning:
#### All version changes require recertification to the new version. Once certified for the use of a new version, Vantiv modifies your Merchant Profile, allowing you to submit transaction to the Production Environment using the new version. Updating your code without recertification and modification of your Merchant Profile will result in transaction declines. Please consult you Implementation Analyst for additional information about this process.
About Vantiv eCommerce
------------
[Vantiv eCommerce](https://developer.vantiv.com/community/ecommerce) powers the payment processing engines for leading companies that sell directly to consumers through  internet retail, direct response marketing (TV, radio and telephone), and online services. Vantiv eCommerce is the leading authority in card-not-present (CNP) commerce, transaction processing and merchant services.


About this SDK
--------------
The Vantiv eCommerce Java SDK is a Java implementation of the [Vantiv eCommerce](https://developer.vantiv.com/community/ecommerce) XML API. This SDK was created to make it as easy as possible to process your payments with Vantiv eCommerce. This SDK utilizes the HTTPS protocol to securely connect to Vantiv eCommerce. Using the SDK requires coordination with the Vantiv eCommerce team in order to be provided with credentials for accessing our systems.

See LICENSE file for details on using this software.

Please contact [Vantiv eCommerce](https://developer.vantiv.com/community/ecommerce) to receive valid merchant credentials in order to run tests successfully or if you require assistance in any way.  We are reachable at sdksupport@fisglobal.com

Setup
-----

1. Add JCenter repository to your Maven or Gradle build:
	1. For Maven, please read instructions at: https://bintray.com/bintray/jcenter
	2. For Gradle, add `jcenter()` to your `repositories { ... }`
2. Add the dependency
    1. For Maven:
        ```xml
            <dependency>
                <groupId>com.litle</groupId>
                <artifactId>litle-sdk-for-java</artifactId>
                <version>8.29.0</version>
            </dependency>
        ```

    2. For Gradle:
        ```groovy
            compile(group: 'com.litle', name: 'litle-sdk-for-java', version: '8.29.0')
        ```
        
3. Create your configuration file with one of the following
    * Run `java -jar /path/to/litle-sdk-for-java.jar` and answer the questions, or
    * Add a file `.litle_SDK_config.properties` to your home directory with the correct properties in it
4. Use it:

```java
import com.litle.sdk.*;
import com.litle.sdk.generated.*;

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
		
		// Peform the transaction on the Vantiv eCommerce Platform
		SaleResponse response = new LitleOnline().sale(sale);

		// display result
		System.out.println("Message: " + response.getMessage());
		System.out.println("Vantiv eCommerce Transaction ID: " + response.getLitleTxnId());
	}
}
```

More examples can be found here [Java Gists](https://gist.github.com/vantivSDK)
