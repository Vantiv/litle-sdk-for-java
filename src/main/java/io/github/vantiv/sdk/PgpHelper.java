package io.github.vantiv.sdk;

import java.io.*;
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

    /**
     *
     * @param in InputStream to file containing Pgp Public key
     * @return PGPPublicKey
     * @throws IOException
     * @throws PGPException
     */
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
     * @return PGPPrivate key that corresponds to the given public keyID and passphrase.
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
     * Decrypt a file using a Pgp private key and a passphrase
     *
     * @param inputFilepath path to encrypted file.
     * @param outputFilepath path to decrypted file
     * @param privateKeyPath path to Pgp Private key
     * @param passphrase passphrse to access provided Pgp Private key
     * @throws IOException
     * @throws PGPException
     */
    public static void decrypt(String inputFilepath, String outputFilepath, String privateKeyPath, String passphrase)
            throws IOException, PGPException {
        InputStream decryptionInputStream = decryptionStream(inputFilepath, privateKeyPath, passphrase);
        OutputStream fileOutputStream = new FileOutputStream(outputFilepath);
        byte[] clearData = new byte[2097152];
        int len;
        while ((len = decryptionInputStream.read(clearData)) > 0) {
            fileOutputStream.write(clearData, 0, len);
        }
        fileOutputStream.close();
        decryptionInputStream.close();
    }


    /**
     *
     * @param inputFilepath path to encrypted file.
     * @param privateKeyPath path to Pgp Private key
     * @param passphrase passphrse to access provided Pgp Private key
     * @return An InputStream to read encrypted content in given encrypted file as decrypted raw data.
     * @throws IOException
     * @throws PGPException
     */
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

        InputStream clearDataStream = pgpPublicKeyEncryptedData.getDataStream(new JcePublicKeyDataDecryptorFactoryBuilder().setProvider("BC").build(pgpPrivateKey));

        JcaPGPObjectFactory plainFact = new JcaPGPObjectFactory(clearDataStream);

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
            return new DecryptedInputStream(literalDataInputStream, fileInputStream, clearDataStream);
        }
        else if (message instanceof PGPOnePassSignatureList) {
            throw new PGPException("Encrypted message contains a signed message - not literal data.");
        }
        else {
            throw new PGPException("Message is not a simple encrypted file - type unknown.");
        }
    }


    /**
     * Encrypt a file using a Pgp Public key
     *
     * @param inputFilePath Path to file containing unencrypted raw data
     * @param outputFilepath Path to store resulting encrypted file
     * @param publicKeyPath Path to Public key required to encrypt raw data.
     * @throws IOException
     * @throws PGPException
     */
    public static void encrypt(String inputFilePath, String outputFilepath, String publicKeyPath)
            throws IOException, PGPException {
        OutputStream encryptionOutputStream = encryptionStream(outputFilepath, publicKeyPath);
        InputStream fileInputStream = new FileInputStream(inputFilePath);
        byte[] clearData = new byte[2097152];
        int len;
        while ((len = fileInputStream.read(clearData)) > 0) {
            encryptionOutputStream.write(clearData, 0, len);
        }
        encryptionOutputStream.close();
        fileInputStream.close();
    }


    /**
     *
     * @param outputFilepath Path to store encrypted content
     * @param publicKeyPath Path to Public key required to encrypt unencrypted raw data
     * @return An OutputStream to write unencrypted raw data to 'outputFilepath' as encrypted content
     * @throws IOException
     * @throws PGPException
     */
    public static OutputStream encryptionStream (String outputFilepath, String publicKeyPath)
            throws IOException, PGPException {
        OutputStream fileOutputStream = new FileOutputStream(outputFilepath);
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
