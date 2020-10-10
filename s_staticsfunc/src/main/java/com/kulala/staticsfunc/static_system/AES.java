package com.kulala.staticsfunc.static_system;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

public class AES {

    /**
     * AES加密
     * @param info 待加密的内容
     * @param encryptKey 加密密钥
     * @return 加密后的byte[]
     * @throws Exception
     */
    public static byte[] AESgenerator(String info, String encryptKey) throws Exception {
        if(encryptKey == null  || encryptKey.equals(""))return null;
        try{
            String iv        = "0102030405060708";
            Cipher cipher    = Cipher.getInstance("AES/CBC/NoPadding");
            int    blockSize = cipher.getBlockSize();

            byte[] dataBytes = info.getBytes();
            int plaintextLength = dataBytes.length;
            if (plaintextLength % blockSize != 0) {
                plaintextLength = plaintextLength + (blockSize - (plaintextLength % blockSize));
            }
            byte[] plaintext = new byte[plaintextLength];
            System.arraycopy(dataBytes, 0, plaintext, 0, dataBytes.length);
            SecretKeySpec   keyspec = new SecretKeySpec(encryptKey.getBytes(), "AES");
            IvParameterSpec ivspec  = new IvParameterSpec(iv.getBytes());
            cipher.init(Cipher.ENCRYPT_MODE, keyspec, ivspec);
            byte[] encrypted = cipher.doFinal(plaintext);

            return encrypted;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

    }
}
