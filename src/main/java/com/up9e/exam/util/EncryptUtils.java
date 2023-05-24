package com.up9e.exam.util;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

public class EncryptUtils {

    public static String getSecretKey(int n) throws NoSuchAlgorithmException {
        KeyGenerator keyGenerator = KeyGenerator.getInstance("AES");
        keyGenerator.init(n);
        SecretKey originalKey = keyGenerator.generateKey();
        return Base64.getEncoder().encodeToString(originalKey.getEncoded());
    }

    public static String encode(String secret, String message) throws Exception {
        byte[] decodedKey = Base64.getDecoder().decode(secret);
        SecretKey originalKey = new SecretKeySpec(decodedKey, 0, decodedKey.length, "AES");
        byte[] dataInBytes = message.getBytes();
        // rebuild key using SecretKeySpec
        Cipher encryptionCipher = Cipher.getInstance("AES");
        encryptionCipher.init(Cipher.ENCRYPT_MODE, originalKey);
        byte[] encryptedBytes = encryptionCipher.doFinal(dataInBytes);
        return Base64.getEncoder().encodeToString(encryptedBytes);
    }
}
