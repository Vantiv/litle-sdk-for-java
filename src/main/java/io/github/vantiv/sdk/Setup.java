package io.github.vantiv.sdk;

import java.io.*;
import java.util.HashMap;
import java.util.Properties;

public class Setup {


    @SuppressWarnings("serial")
    private static final HashMap<String, String> URL_MAP = new HashMap<String, String>() {
        {
            put("sandbox", "https://www.testlitle.com/sandbox/communicator/online");
            put("cert", "https://prelive.litle.com/vap/communicator/online");
            put("precert", "https://postlive.litle.com/vap/communicator/online");
            put("production", "https://payments.litle.com/vap/communicator/online");
            put("batchsandbox", "https://www.testlitle.com/sandbox");
            put("batchcert", "cert.litle.com");
            put("batchprecert", "precert.litle.com");
            put("batchproduction", "payments.litle.com");
        }
    };

    @SuppressWarnings("serial")
    private static final HashMap<String, String> PORT_MAP = new HashMap<String, String>() {
        {
            put("batchsandbox", "15000");
            put("batchcert", "15000");
            put("batchprecert", "15000");
            put("batchproduction", "15000");
        }
    };

    /**
     * @param args
     * @throws IOException
     */
    public static void main(String[] args) throws IOException {
        File file = (new Configuration()).location();
        Properties config = new Properties();
        PrintStream configFile = new PrintStream(file);
        String lastUserInput;

        BufferedReader stdin = new BufferedReader
                (new InputStreamReader(System.in));

        System.out.println("Welcome to Litle Java_SDK");
        System.out.print("Please input your presenter user name: ");
        config.put("username", stdin.readLine());
        System.out.print("Please input your presenter password: ");
        config.put("password", stdin.readLine());
        System.out.print("Please input your merchantId: ");
        config.put("merchantId", stdin.readLine());
        boolean badInput = false;
        do {
            if (badInput) {
                System.out.println("====== Invalid choice entered ======");
            }
            System.out.println("Please choose an environment from the following list (example: 'prelive'):");
            System.out.println("\tsandbox => www.testlitle.com");
            System.out.println("\tcert => cert.litle.com");
            System.out.println("\tprecert => precert.litle.com");
            System.out.println("\tproduction => payments.litle.com");
            System.out.println("\tother => You will be asked for all the values");
            lastUserInput = stdin.readLine();
            if (
                    lastUserInput.compareToIgnoreCase("cert") == 0 ||
                            lastUserInput.compareToIgnoreCase("sandbox") == 0 ||
                            lastUserInput.compareToIgnoreCase("precert") == 0 ||
                            lastUserInput.compareToIgnoreCase("production") == 0
                    ) {
                // standard predefined cases
                config.put("url", URL_MAP.get(lastUserInput.toLowerCase()));
                config.put("batchHost", URL_MAP.get(("batch" + lastUserInput).toLowerCase()));
                config.put("batchPort", PORT_MAP.get(("batch" + lastUserInput).toLowerCase()));
                badInput = false;
            } else if (lastUserInput.compareToIgnoreCase("other") == 0) {
                // user wants to enter custom values
                System.out.println("Please input the URL for online transactions (ex: https://www.testlitle.com/sandbox/communicator/online):");
                config.put("url", stdin.readLine());
                System.out.println("Please input the Host name for batch transactions (ex: payments.litle.com):");
                config.put("batchHost", stdin.readLine());
                System.out.println("Please input the port number for batch transactions (ex: 15000):");
                config.put("batchPort", stdin.readLine());
                badInput = false;
            } else {
                // error condition
                badInput = true;
            }
        } while (badInput);

        System.out.print("Values set for host: ");
        System.out.print("\n\tURL for online transactions: " + config.getProperty("url"));
        System.out.print("\n\tHost for batch transactions: " + config.getProperty("batchHost"));
        System.out.print("\n\tPort for batch transactions: " + config.getProperty("batchPort") + "\n");

        config.put("batchUseSSL", "true");
        System.out.print("Please input the batch TCP timeout in milliseconds (leave blank for default (7200000)): ");
        lastUserInput = stdin.readLine();
        config.put("batchTcpTimeout", ((lastUserInput.length() == 0) ? "7200000" : lastUserInput));

        System.out.print("\nBatch SDK generates files for Requests and Responses. You may leave these blank if you do not plan to use \nbatch processing. Please input the absolute path to the folder with write permissions for: \n");
        System.out.print("\tRequests: ");
        config.put("batchRequestFolder", stdin.readLine());

        System.out.print("\tResponses: ");
        config.put("batchResponseFolder", stdin.readLine());

        System.out.print("\nPlease input your credentials for sFTP access for batch delivery. You may leave these blank if you do not plan to use sFTP.\n");
        System.out.print("\tUsername: ");
        config.put("sftpUsername", stdin.readLine());
        System.out.print("\tPassword: ");
        config.put("sftpPassword", stdin.readLine());
        System.out.print("Please input the sFTP timeout in milliseconds (leave blank for default (7200000)): ");
        lastUserInput = stdin.readLine();
        config.put("sftpTimeout", ((lastUserInput.length() == 0) ? "7200000" : lastUserInput));


        System.out.print("\nPlease input the proxy host, if no proxy hit enter: ");
        lastUserInput = stdin.readLine();
        config.put("proxyHost", (lastUserInput == null ? "" : lastUserInput));
        System.out.print("Please input the proxy port, if no proxy hit enter: ");
        lastUserInput = stdin.readLine();
        config.put("proxyPort", (lastUserInput == null ? "" : lastUserInput));
        //default http timeout set to 500 ms
        config.put("timeout", "500");
        config.put("reportGroup", "Default Report Group");
        config.put("printxml", "false");

        config.put("maxAllowedTransactionsPerFile", "500000");
        config.put("maxTransactionsPerBatch", "100000");

        System.out.print("Use PGP encryption for batch files? (No encryption by default): ");
        lastUserInput = stdin.readLine();
        if ("true".equals(lastUserInput) || "yes".equals(lastUserInput) || "y".equals(lastUserInput)) {
            config.put("useEncryption", "true");

            System.out.print("Please input path to Vantiv's public key (for encryption of batch requests) : ");
            lastUserInput = stdin.readLine();
            config.put("VantivPublicKeyPath", lastUserInput);

            System.out.print("Please input path to your merchant public key (To keep temp file encrypted) : ");
            lastUserInput = stdin.readLine();
            config.put("MerchantPublicKeyPath", lastUserInput);

            System.out.print("Please input path to your merchant private key (for decryption of batch responses) : ");
            lastUserInput = stdin.readLine();
            config.put("PrivateKeyPath", lastUserInput);

            System.out.print("Passphrase for decryption : ");
            config.put("gpgPassphrase", stdin.readLine());
        } else {
            config.put("useEncryption", "false");
            config.put("PublicKeyPath", "");
            config.put("PrivateKeyPath", "");
            config.put("gpgPassphrase", "");
        }

        config.put("deleteBatchFiles", "false");

        config.store(configFile, "");
        System.out.println("The Litle configuration file has been generated, the file is located at " + file.getAbsolutePath());

        configFile.close();
    }
}
