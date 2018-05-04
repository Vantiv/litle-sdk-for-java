package com.litle.sdk;

import java.io.*;
import java.security.NoSuchProviderException;
import java.security.SecureRandom;
import java.security.Security;
import java.util.Date;
import java.util.Iterator;

import org.bouncycastle.bcpg.ArmoredOutputStream;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.openpgp.*;
import org.bouncycastle.openpgp.jcajce.JcaPGPObjectFactory;
import org.bouncycastle.openpgp.operator.PBESecretKeyDecryptor;
import org.bouncycastle.openpgp.operator.jcajce.*;

public class PgpHelper {

    public static PGPPublicKey readPublicKey(InputStream in) throws IOException, PGPException {
        InputStream decoderStream = PGPUtil.getDecoderStream(in);
        PGPPublicKeyRingCollection pgpPub = new PGPPublicKeyRingCollection(decoderStream, new JcaKeyFingerprintCalculator());
        PGPPublicKey pgpPublicKey = null;

        Iterator<PGPPublicKeyRing> pgpPublicKeyRingIterator = pgpPub.getKeyRings();

        while (pgpPublicKey == null && pgpPublicKeyRingIterator.hasNext()) {
            PGPPublicKeyRing kRing = pgpPublicKeyRingIterator.next();
            Iterator<PGPPublicKey> publicKeys = kRing.getPublicKeys();
            while (pgpPublicKey == null && publicKeys.hasNext()) {
                PGPPublicKey publicKey = publicKeys.next();

                if (publicKey.isEncryptionKey()) {
                    pgpPublicKey = publicKey;
                }
            }
        }
        decoderStream.close();

        if (pgpPublicKey == null) {
            throw new IllegalArgumentException("Can't find encryption key in key ring.");
        }

        return pgpPublicKey;
    }


    /**
     * Load a secret key ring collection from keyIn and find the secret key corresponding to
     * keyID if it exists.
     *
     * @param keyIn input stream representing a key ring collection.
     * @param keyID keyID we want.
     * @param pass  passphrase to decrypt secret key with.
     * @return
     * @throws IOException
     * @throws PGPException
     */
    public static PGPPrivateKey findSecretKey(InputStream keyIn, long keyID, char[] pass)
            throws IOException, PGPException {
        PGPSecretKeyRingCollection pgpSecretKeyRingCollection = new PGPSecretKeyRingCollection(PGPUtil.getDecoderStream(keyIn), new JcaKeyFingerprintCalculator());

        PGPSecretKey pgpSecretKeyKey = pgpSecretKeyRingCollection.getSecretKey(keyID);

        if (pgpSecretKeyKey == null) {
            return null;
        }


        PBESecretKeyDecryptor secretKeyDecryptor = new JcePBESecretKeyDecryptorBuilder(new JcaPGPDigestCalculatorProviderBuilder().setProvider("BC").build()).setProvider("BC").build(pass);

        return pgpSecretKeyKey.extractPrivateKey(secretKeyDecryptor);
    }

    /**
     * decrypt the passed in message stream
     *  String passphrase
     */
    public static void decrypt(String inputFilepath, String decryptedOutputFilepath, String privateKeyPath, String passphrase)
            throws IOException, PGPException {
        InputStream fileInputStream = new FileInputStream(inputFilepath);
        OutputStream fileOutputStream = new FileOutputStream(decryptedOutputFilepath);
        Security.addProvider(new BouncyCastleProvider());
        fileInputStream = PGPUtil.getDecoderStream(fileInputStream);
        JcaPGPObjectFactory jcaPGPObjectFactory = new JcaPGPObjectFactory(fileInputStream);
        PGPEncryptedDataList encryptedDataList;
        Object obj = jcaPGPObjectFactory.nextObject();

        if (obj instanceof PGPEncryptedDataList) {
            encryptedDataList = (PGPEncryptedDataList) obj;
        } else {
            encryptedDataList = (PGPEncryptedDataList) jcaPGPObjectFactory.nextObject();
        }

        Iterator<PGPPublicKeyEncryptedData> pgpPublicKeyEncryptedDataIterator = encryptedDataList.getEncryptedDataObjects();
        PGPPrivateKey pgpPrivateKey = null;
        PGPPublicKeyEncryptedData pgpPublicKeyEncryptedData = null;

        while (pgpPrivateKey == null && pgpPublicKeyEncryptedDataIterator.hasNext()) {
            pgpPublicKeyEncryptedData = pgpPublicKeyEncryptedDataIterator.next();
            pgpPrivateKey = findSecretKey(new FileInputStream(privateKeyPath), pgpPublicKeyEncryptedData.getKeyID(), passphrase.toCharArray());
        }

        if (pgpPrivateKey == null) {
            throw new IllegalArgumentException("Secret key for message not found.");
        }

        InputStream clear = pgpPublicKeyEncryptedData.getDataStream(new JcePublicKeyDataDecryptorFactoryBuilder().setProvider("BC").build(pgpPrivateKey));

        JcaPGPObjectFactory plainFact = new JcaPGPObjectFactory(clear);

        Object message = plainFact.nextObject();

        if (message instanceof PGPCompressedData) {
            PGPCompressedData compressedData = (PGPCompressedData) message;
            JcaPGPObjectFactory pgpFact = new JcaPGPObjectFactory(compressedData.getDataStream());

            message = pgpFact.nextObject();
        }

        if (message instanceof PGPLiteralData) {
            PGPLiteralData literalData = (PGPLiteralData) message;
            InputStream literalDataInputStream = literalData.getInputStream();
            byte[] clearData = new byte[2097152];
            int len;
            while ((len = literalDataInputStream.read(clearData)) > 0) {
                fileOutputStream.write(clearData, 0, len);
            }
            literalDataInputStream.close();
        }
        fileInputStream.close();
        clear.close();
        fileOutputStream.close();
    }


    public static InputStream decryptionStream(String inputFilepath,  String privateKeyPath, String passphrase)
            throws IOException, PGPException {
        InputStream fileInputStream = new FileInputStream(inputFilepath);
        Security.addProvider(new BouncyCastleProvider());
        fileInputStream = PGPUtil.getDecoderStream(fileInputStream);
        JcaPGPObjectFactory jcaPGPObjectFactory = new JcaPGPObjectFactory(fileInputStream);
        PGPEncryptedDataList encryptedDataList;
        Object obj = jcaPGPObjectFactory.nextObject();

        if (obj instanceof PGPEncryptedDataList) {
            encryptedDataList = (PGPEncryptedDataList) obj;
        } else {
            encryptedDataList = (PGPEncryptedDataList) jcaPGPObjectFactory.nextObject();
        }

        Iterator<PGPPublicKeyEncryptedData> pgpPublicKeyEncryptedDataIterator = encryptedDataList.getEncryptedDataObjects();
        PGPPrivateKey pgpPrivateKey = null;
        PGPPublicKeyEncryptedData pgpPublicKeyEncryptedData = null;

        while (pgpPrivateKey == null && pgpPublicKeyEncryptedDataIterator.hasNext()) {
            pgpPublicKeyEncryptedData = pgpPublicKeyEncryptedDataIterator.next();
            pgpPrivateKey = findSecretKey(new FileInputStream(privateKeyPath), pgpPublicKeyEncryptedData.getKeyID(), passphrase.toCharArray());
        }

        if (pgpPrivateKey == null) {
            throw new IllegalArgumentException("Secret key for message not found.");
        }

        InputStream clear = pgpPublicKeyEncryptedData.getDataStream(new JcePublicKeyDataDecryptorFactoryBuilder().setProvider("BC").build(pgpPrivateKey));

        JcaPGPObjectFactory plainFact = new JcaPGPObjectFactory(clear);

        Object message = plainFact.nextObject();

        if (message instanceof PGPCompressedData) {
            PGPCompressedData compressedData = (PGPCompressedData) message;
            JcaPGPObjectFactory pgpFact = new JcaPGPObjectFactory(compressedData.getDataStream());

            message = pgpFact.nextObject();
        }

        if (message instanceof PGPLiteralData){
            PGPLiteralData literalData = (PGPLiteralData) message;
            InputStream literalDataInputStream = literalData.getInputStream();
        }

        if (message instanceof PGPLiteralData) {
            PGPLiteralData literalData = (PGPLiteralData) message;
            InputStream literalDataInputStream = literalData.getInputStream();
            return new DecryptedInputStream(literalDataInputStream, fileInputStream, clear);
        }
        else if (message instanceof PGPOnePassSignatureList) {
            throw new PGPException("Encrypted message contains a signed message - not literal data.");
        }
        else {
            throw new PGPException("Message is not a simple encrypted file - type unknown.");
        }
    }

    public static void encrypt(String inputFilePath, String encryptedOutputFilepath, String publicKeyPath)
            throws IOException, PGPException {
        OutputStream fileOutputStream = new FileOutputStream(encryptedOutputFilepath);
        fileOutputStream = new ArmoredOutputStream(fileOutputStream);
        PGPPublicKey pgpPublicKey = readPublicKey(new FileInputStream(publicKeyPath));
        Security.addProvider(new BouncyCastleProvider());

        ByteArrayOutputStream bOut = new ByteArrayOutputStream();

        PGPCompressedDataGenerator compressedDataGenerator = new PGPCompressedDataGenerator(
                PGPCompressedData.ZIP);

        File inputFile = new File(inputFilePath);

        org.bouncycastle.openpgp.PGPUtil.writeFileToLiteralData(compressedDataGenerator.open(bOut),
                PGPLiteralData.BINARY, inputFile);

        compressedDataGenerator.close();

        JcePGPDataEncryptorBuilder c = new JcePGPDataEncryptorBuilder(PGPEncryptedData.CAST5).setSecureRandom(new SecureRandom()).setProvider("BC");

        PGPEncryptedDataGenerator cPk = new PGPEncryptedDataGenerator(c);

        JcePublicKeyKeyEncryptionMethodGenerator d = new JcePublicKeyKeyEncryptionMethodGenerator(pgpPublicKey).setProvider(new BouncyCastleProvider()).setSecureRandom(new SecureRandom());

        cPk.addMethod(d);

        byte[] bytes = bOut.toByteArray();

        OutputStream cOut = cPk.open(fileOutputStream, bytes.length);

        cOut.write(bytes);

        cOut.close();

        fileOutputStream.close();
    }


    public static OutputStream encryptionStream (String encryptedOutputFilepath, String publicKeyPath)
            throws IOException, PGPException {
        OutputStream fileOutputStream = new FileOutputStream(encryptedOutputFilepath);
        fileOutputStream = new ArmoredOutputStream(fileOutputStream);
        PGPPublicKey pgpPublicKey = readPublicKey(new FileInputStream(publicKeyPath));
        Security.addProvider(new BouncyCastleProvider());

        JcePGPDataEncryptorBuilder c = new JcePGPDataEncryptorBuilder(PGPEncryptedData.CAST5).setSecureRandom(new SecureRandom()).setProvider("BC");

        PGPEncryptedDataGenerator cPk = new PGPEncryptedDataGenerator(c);

        JcePublicKeyKeyEncryptionMethodGenerator d = new JcePublicKeyKeyEncryptionMethodGenerator(pgpPublicKey).setProvider(new BouncyCastleProvider()).setSecureRandom(new SecureRandom());

        cPk.addMethod(d);

        OutputStream encOut = cPk.open(fileOutputStream, new byte[2097152]);

        PGPLiteralDataGenerator lData = new PGPLiteralDataGenerator();

        OutputStream pOut = lData.open(encOut, // the compressed output stream
                PGPLiteralData.BINARY,
                "iway",  // "filename" to store // length of clear data
                new Date(),  // current time
                new byte[2097152]
        );

        return new EncryptedOutputStream(encOut, fileOutputStream, pOut);
    }
}


class EncryptedOutputStream extends OutputStream {
    private OutputStream encOut;
    private OutputStream out;
    private OutputStream pOut;


    public EncryptedOutputStream(OutputStream encOut, OutputStream out, OutputStream pOut) {
        this.encOut = encOut;
        this.out = out;
        this.pOut = pOut;
    }


    public void write(byte[] clearData) throws IOException {
        pOut.write(clearData);
    }

    public void write(int i){

    }

    public void close()throws IOException{
        pOut.close();
        encOut.close();
        out.close();
    }

    public void flush()throws IOException{
        pOut.flush();
    }
}


class DecryptedInputStream extends InputStream {
    private InputStream literalDataInputStream;
    private InputStream fileInputStream;
    private InputStream clearStream;

    public DecryptedInputStream(InputStream literalDataInputStream, InputStream fileInputStream, InputStream clearStream){
        this.literalDataInputStream = literalDataInputStream;
        this.fileInputStream = fileInputStream;
        this.clearStream = clearStream;
    }

    public int read() throws IOException {
        return literalDataInputStream.read();
    }

    public int read(byte[] buffer) throws IOException{
        return literalDataInputStream.read(buffer);
    }

    public void close() throws IOException{
        literalDataInputStream.close();
        fileInputStream.close();
        clearStream.close();
    }
}
