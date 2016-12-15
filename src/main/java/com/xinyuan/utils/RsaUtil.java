package com.xinyuan.utils;


import org.apache.commons.codec.binary.Base64;

import javax.crypto.Cipher;
import java.io.ByteArrayOutputStream;
import java.io.UnsupportedEncodingException;
import java.security.*;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

/**
 * Created by Menong on 2015/2/2.
 */
public class RsaUtil {
    private static final String EncodeCharset = "utf-8";
    private static final int EncryptMaxBlock = 53;
    private static final int DecryptMaxBlock = 64;

    /**
     * 重构rsa密钥对
     * @param length	密钥长度
     * @return			新的密钥对，array长度为2，privateKey = array[0], publicKey = array[1]
     */
    public static String[] rebuildKey(int length) throws NoSuchAlgorithmException, UnsupportedEncodingException {
        KeyPairGenerator keyPairGen = KeyPairGenerator.getInstance("RSA");
        keyPairGen.initialize(length);
        KeyPair keyPair = keyPairGen.generateKeyPair();
        PublicKey publicKey = keyPair.getPublic();
        PrivateKey privateKey = keyPair.getPrivate();
        return new String[] {
                Base64.encodeBase64String(privateKey.getEncoded()),
                Base64.encodeBase64String(publicKey.getEncoded())
            };
    }

    /**
     * 公钥加密
     */
    public static String publicEncrypt(String str, String publicKey) throws Exception {
        Key key = getPublicKey(publicKey);
        Cipher cipher = getCipher(key, Cipher.ENCRYPT_MODE);

        return encrypt(str, cipher);
    }

    /**
     * 公钥解密
     */
    public static String publicDecrypt(String str, String publicKey) throws Exception {
        Key pubKey = getPublicKey(publicKey);
        Cipher cipher = getCipher(pubKey, Cipher.DECRYPT_MODE);

        return decrypt(str, cipher);
    }

    /**
     * 私钥加密
     */
    public static String privateEncrypt(String str, String privateKey) throws Exception {
        Key priKey = getPrivateKey(privateKey);
        Cipher cipher = getCipher(priKey, Cipher.ENCRYPT_MODE);

        return encrypt(str, cipher);
    }

    /**
     * 私钥解密
     */
    public static String privateDecrypt(String str, String privateKey) throws Exception {
        Key priKey = getPrivateKey(privateKey);
        Cipher cipher = getCipher(priKey, Cipher.DECRYPT_MODE);

        return decrypt(str, cipher);
    }

    private static Key getPrivateKey(String privateKey) throws Exception {
        byte[] keyBytes = Base64.decodeBase64(privateKey);
        PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(keyBytes);

        KeyFactory factory = KeyFactory.getInstance("RSA");
        return factory.generatePrivate(keySpec);
    }

    private static Key getPublicKey(String publicKey) throws Exception {
        byte[] keyBytes = Base64.decodeBase64(publicKey);
        X509EncodedKeySpec keySpec = new X509EncodedKeySpec(keyBytes);

        KeyFactory factory = KeyFactory.getInstance("RSA");
        return factory.generatePublic(keySpec);
    }

    private static Cipher getCipher(Key priKey, int mode) throws Exception {
        Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
        cipher.init(mode, priKey);
        return cipher;
    }

    /**
     * 加密
     */
    private static String encrypt(String str, Cipher cipher) throws Exception {
        byte[] data = str.getBytes();
        int length = data.length;
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        int offSet = 0;
        byte[] buffer;
        int i = 0;
        // 对数据分段加密
        while (length - offSet > 0) {
            buffer = (length - offSet > EncryptMaxBlock)
                   ? cipher.doFinal(data, offSet, EncryptMaxBlock)
                   : cipher.doFinal(data, offSet, length - offSet)
            ;
            output.write(buffer, 0, buffer.length);
            i++;
            offSet = i * EncryptMaxBlock;
        }
        byte[] bytes = output.toByteArray();
        output.close();
        return Base64.encodeBase64String(bytes);
    }

    /**
     * 解密
     */
    private static String decrypt(String str, Cipher cipher) throws Exception {
        byte[] data = Base64.decodeBase64(str);
        int length = data.length;
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        int offSet = 0;
        byte[] buffer;
        int i = 0;
        // 对数据分段解密
        while (length - offSet > 0) {
            buffer = (length - offSet > DecryptMaxBlock)
                    ? cipher.doFinal(data, offSet, DecryptMaxBlock)
                    : cipher.doFinal(data, offSet, length - offSet)
            ;
            output.write(buffer, 0, buffer.length);
            i++;
            offSet = i * DecryptMaxBlock;
        }
        byte[] bytes = output.toByteArray();
        output.close();
        return new String(bytes, EncodeCharset);
    }

    public static void main(String[] args) throws Exception {
/*        String[] keys = rebuildKey(512);
        String privateKey = keys[0];
        String publicKey = keys[1];
        System.out.println("privateKey = " + privateKey);
        System.out.println("publicKey  = " + publicKey)*/;
        String openId= "551b4edde4b0ccd88dcb673d";
        Integer appId = 10000;
        String token = "358b854fe27511e4826d782e7ce30052";
        String text = "{\"userName\":\"jmterusertest097661949\",\"amount\":\"11111\",\"sumNum\":\"1\"}";
        System.out.println("text = " + text);
        String encrypt = privateEncrypt(text, "MIIBVQIBADANBgkqhkiG9w0BAQEFAASCAT8wggE7AgEAAkEAtYHvj2P5PukDY0IlGBVNKraAUOz+iwjFhgxab3A+DinJrY7wUrZ0wgocwVwW/mlMqHJI+hozNkeniOyAihW3qQIDAQABAkAgkSrZsJd1KTv5YFHli1ToOAy3+H4HllBSp/7GBf6sbZb4AEZ5arJwpo5NONeUDO/y1YAaYk26of4Qw/xSRgG9AiEA9t1lCYjBsco3BrBfqE2KDvTs/pgnU8k5agRu9L54PfsCIQC8OWWx5KSglbRpmtWIYeu3L5pjOa+kj06JC30QsvEjqwIhAIO0yNQWX2jQfx0VemObwt2J9Os8GDdOc1BJvzzaZeKDAiBy8QYWJU8XNXRvzPq7Bzkfq7U3BOIqtsy5ycc87FlvcwIhAI0oUAfUlK4703mHRgOaFiJOA94NIkHJ6alg3LovNTCl");
        System.out.println("encrypt = " + encrypt);
        //key = "G#F$J%U^";
        String decrypt = publicDecrypt(encrypt, "MFwwDQYJKoZIhvcNAQEBBQADSwAwSAJBALWB749j+T7pA2NCJRgVTSq2gFDs/osIxYYMWm9wPg4pya2O8FK2dMIKHMFcFv5pTKhySPoaMzZHp4jsgIoVt6kCAwEAAQ==");
        System.out.println("decrypt = " + decrypt);
        
        
       /* String decrypt_1 = publicDecrypt("a8YN7GL3tmRJdpMFOE4w/LLPXDJiTMM/hQfaicFDGLKz0UCtJq+VMWsjg9iMNhcS/7G7A478YtgOPKHfRGdL6pTsLaTTxW183tg24XYAzjlupOg+j7C6lVJJSmpFmVnZCg2mhIE8rtUudOrPttxe1s0HYleENRCAfHQJn82HadYsicSFGd8TpWfwp4SvtvaWjbhfaCTdOdL1QlSsZ76wrMVMCSCg1i38wz64/3H8vRKc3H9allgb8c78b8q7blHo"
        		,"MFwwDQYJKoZIhvcNAQEBBQADSwAwSAJBAKbsMYirgEwR5WbXFqYfz5jBmmkGar63UlhWPyzBSr87yY8nt4OCw3FpPTkf8ncXbCUAUI3nfK6t24KaDrd4lSECAwEAAQ==");
        System.out.println("decrypt = " + decrypt_1);*/
        
        
    }

}
