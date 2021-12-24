package com.framework.base.utils;

import android.util.Base64;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

/**
 * AES加解密
 */
public class AESUtils {
    private static final String KEY = "liuchangkeji123!";
    private static final String IV = "!@#liuchangkeji1";

    public static String deCode(String s) {
        String returnString = null;
        try {

            returnString = URLDecoder.decode(s, "UTF-8");

        } catch (UnsupportedEncodingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return returnString;
    }

    public static String encryptIt(String s) {
        return encryptIt(s, KEY, IV);
    }

    public static String decryptIt(String s) {
        return decryptIt(s, KEY, IV);
    }


    private static String encryptIt(String s, String key, String iv) {
        try {
            Cipher cipher = Cipher.getInstance("AES/CBC/NoPadding");
            int blockSize = cipher.getBlockSize();

            byte[] dataBytes = s.getBytes();
            int plaintextLength = dataBytes.length;
            if (plaintextLength % blockSize != 0) {
                plaintextLength = plaintextLength + (blockSize - (plaintextLength % blockSize));
            }

            byte[] plaintext = new byte[plaintextLength];
            System.arraycopy(dataBytes, 0, plaintext, 0, dataBytes.length);

            SecretKeySpec secretKeySpec = new SecretKeySpec(key.getBytes(), "AES");
            IvParameterSpec ivParameterSpec = new IvParameterSpec(iv.getBytes());

            cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec, ivParameterSpec);
            byte[] encrypted = cipher.doFinal(plaintext);

            return Base64.encodeToString(encrypted, Base64.DEFAULT);

        } catch (Exception e) {
            System.out.println("AES ENCRYPT ERROR:" + s);
            e.printStackTrace();
            return null;
        }
    }

    private static String decryptIt(String s, String key, String iv) {
        try {
            byte[] encrypted = Base64.decode(s, Base64.DEFAULT);

            Cipher cipher = Cipher.getInstance("AES/CBC/NoPadding");
            SecretKeySpec secretKeySpec = new SecretKeySpec(key.getBytes(), "AES");
            IvParameterSpec ivParameterSpec = new IvParameterSpec(iv.getBytes());

            cipher.init(Cipher.DECRYPT_MODE, secretKeySpec, ivParameterSpec);

            byte[] original = cipher.doFinal(encrypted);
            String originalString = new String(original);
            return originalString;
        } catch (Exception e) {
            System.out.println("AES DECRYPT ERROR:" + s);
            e.printStackTrace();
            return null;
        }
    }
}
