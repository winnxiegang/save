package com.android.xjq.utils.encrypt;


import org.apache.commons.codec.binary.Base64;

import java.security.SecureRandom;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESedeKeySpec;

/**
 * Created by zhouyi on 2017/4/1.
 */
public class DesedeCryptor {


    private byte[] keyContent;

    /**
     * 二进制格式的key
     *
     * @param keyContent
     */
    public DesedeCryptor(String keyContent) {
        this.keyContent = Base64.decodeBase64(keyContent.getBytes());
    }

    /**
     * 二进制格式的key
     *
     * @param keyContent
     */
    public DesedeCryptor(byte[] keyContent) {
        this.keyContent = keyContent;
    }

    private Cipher getDecryptCipher() {
        try {
            DESedeKeySpec dks = new DESedeKeySpec(keyContent);
            SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DESede");
            SecretKey key = keyFactory.generateSecret(dks);
            SecureRandom sr = new SecureRandom();
            Cipher decryptCipher = Cipher.getInstance("DESede");
            decryptCipher.init(Cipher.DECRYPT_MODE, key, sr);
            return decryptCipher;
        } catch (Exception e) {

        }
        return null;
    }

    public String decrypt(String content) {
        // TODO Auto-generated method stub
        try {
            return new String(decrypt(Base64.decodeBase64(content.getBytes())));
        } catch (Exception e) {
            return null;
        }
    }

    public byte[] decrypt(byte[] content) {
        try {
            return getDecryptCipher().doFinal(content);
        } catch (Exception e) {
            return null;
        }
    }


}
