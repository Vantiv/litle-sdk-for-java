package com.litle.sdk;

import java.io.BufferedReader;
import java.io.InputStreamReader;

public class PgpHelper {

    public static void encrypt(String encryptInput, String encryptOutput, String publicKey) {
        String command = "gpg --batch --yes --quiet --no-secmem-warning --armor --output " +encryptOutput+
                " --recipient "+publicKey+
                " --trust-model always --encrypt "+encryptInput;

        String s = null;
        StringBuilder response = new StringBuilder();
        int exitVal = 0;
        try {
            Process p = Runtime.getRuntime().exec(command);
            BufferedReader stdError = new BufferedReader(new InputStreamReader(p.getErrorStream()));

            while ((s = stdError.readLine()) != null) {
                response.append(s).append("\n");
            }
            exitVal = p.waitFor();
        } catch (Exception e) {
            throw new LitleBatchException("The batch file could not be encrypted. Check the public key entered. ", e);
        }
        if(0 != exitVal){
            throw new LitleBatchException("The batch file could not be encrypted. Check the public key entered. " + response);
        }
    }

    public static void decrypt(String decryptInput, String decryptOutput, String passphrase){
        String command = "gpg --batch --yes --quiet --no-secmem-warning --no-mdc-warning  --output "+decryptOutput+
                " --passphrase "+passphrase+
                " --decrypt "+decryptInput;

        String s = null;
        StringBuilder response = new StringBuilder();
        int exitVal = 0;
        try {

            Process p = Runtime.getRuntime().exec(command);

            BufferedReader stdError = new BufferedReader(new InputStreamReader(p.getErrorStream()));

            while ((s = stdError.readLine()) != null) {
                response.append(s).append("\n");
            }
            exitVal = p.waitFor();
        }
        catch (Exception e) {
            throw new LitleBatchException("The response could not be decrypted. ", e);
        }
        if(0 != exitVal){
            throw new LitleBatchException("The response could not be decrypted. " + response);
        }
    }

    public static String importKey(String keyFile){
        String command = "gpg --import "+keyFile;

        String s = null;
        StringBuilder response = new StringBuilder();
        String success = null;
        int exitVal = 0;
        try {

            Process p = Runtime.getRuntime().exec(command);

            BufferedReader stdError = new BufferedReader(new InputStreamReader(p.getErrorStream()));

            while ((s = stdError.readLine()) != null) {
                response.append(s).append("\n");
            }
            exitVal = p.waitFor();
        }
        catch (Exception e) {
            throw new LitleBatchException("The key could not be imported. ", e);
        }
        if(0 != exitVal){
            throw new LitleBatchException("The key could not be imported. " + response);
        }
        success = new String(response);
        success = success.split(" ")[2].replace(":","");
        return success;
    }

}
