package com.security;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import java.nio.charset.StandardCharsets;
import java.security.*;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;
import java.util.logging.Logger;

public class RSA {

    private static Logger logger = Logger.getLogger(RSA.class.getName());
    private PrivateKey privateKey;
    private PublicKey publicKey;

    public RSA() {
        KeyPairGenerator generator = null;
        try {
            generator = KeyPairGenerator.getInstance("RSA");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        generator.initialize(2048, new SecureRandom());
        KeyPair pair = generator.generateKeyPair();
        this.privateKey = pair.getPrivate();
        this.publicKey = pair.getPublic();
    }

    public static PublicKey getPublicKey(String based64PublicKey) {
        PublicKey publicKey = null;
        X509EncodedKeySpec keySpec = new X509EncodedKeySpec(Base64.getDecoder().decode(based64PublicKey));
        KeyFactory keyFactory = null;
        try {
            keyFactory = KeyFactory.getInstance("RSA");
            publicKey = keyFactory.generatePublic(keySpec);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return publicKey;

    }

    public static String encrypt(String message, String publicKey) throws BadPaddingException, IllegalBlockSizeException {
        Cipher cipher = null;
        try {
            cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
            cipher.init(Cipher.ENCRYPT_MODE, getPublicKey(publicKey));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Base64.getEncoder().encodeToString(cipher.doFinal(message.getBytes()));
    }

    public static boolean verify(String plainText, String signature, String publicKey) throws Exception {
        Signature publicSignature = Signature.getInstance("SHA256withRSA");
        publicSignature.initVerify(getPublicKey(publicKey));
        publicSignature.update(plainText.getBytes(StandardCharsets.UTF_8));
        byte[] sign = Base64.getDecoder().decode(signature);
        return publicSignature.verify(sign);
    }

    public void setPublicKey(String based64PublicKey) {
        this.publicKey = getPublicKey(based64PublicKey);
    }

    public String decrypt(String encryptedMessage) throws BadPaddingException, IllegalBlockSizeException {
        byte[] message = Base64.getDecoder().decode(encryptedMessage.getBytes());
        Cipher cipher = null;
        try {
            cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
            cipher.init(Cipher.DECRYPT_MODE, getPrivateKey());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new String(cipher.doFinal(message));
    }

    public String sign(String plainText) throws Exception {
        Signature privateSignature = Signature.getInstance("SHA256withRSA");
        privateSignature.initSign(getPrivateKey());
        privateSignature.update(plainText.getBytes(StandardCharsets.UTF_8));
        byte[] sign = privateSignature.sign();
        return Base64.getEncoder().encodeToString(sign);
    }

    public String getPublicKeyAsString() {
        return Base64.getEncoder().encodeToString(publicKey.getEncoded());
    }

    public PrivateKey getPrivateKey() {
        return privateKey;
    }

    public String decryptChatMessage(String input) {
        logger.info(input);
        try {
            input = decrypt(input);
            logger.info("Decrypted " + input);

        } catch (IllegalArgumentException e) {
            logger.info(e.getMessage());
            return input;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return input;
    }

}
