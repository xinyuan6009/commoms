package com.xinyuan.utils;


import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.binary.Hex;
import org.apache.commons.codec.digest.DigestUtils;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

public class EncryptUtil {

    /**
     * md5加密
     */
    public static String md5(String data) {
        return DigestUtils.md5Hex(data);
    }

    /**
     * md5加密
     */
    public static String md5(byte[] data) {
        return DigestUtils.md5Hex(data);
    }

    /**
     * sha1加密40位
     */
    public static String sha1(String data) {
        return DigestUtils.sha1Hex(data);
    }

    /**
     * base64 encode
     */
    public static String base64Encode(String data) {
        String result = "";
        try {
            byte[] bytes = data.getBytes("utf-8");
            result = Base64.encodeBase64String(bytes);
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }
        return result;
    }
    
    /**
     * 对图片进行base64加密 并返回加密字符串
     * @return
     */
    public static String base64ImgEncode(byte[] data){
        //对字节数组Base64编码
        BASE64Encoder encoder = new BASE64Encoder();
        return encoder.encode(data);//返回Base64编码过的字节数组字符串
    }
    
    /**
     * base64 encode
     */
    public static String base64Decode(String data) {
        byte[] bytes = Base64.decodeBase64(data);
        String result = "";
        try {
            result = new String(bytes, "utf-8");
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }

        return result;
    }
    
    /**
     * 对图片进行base64加密 并返回加密字符串
     * @return
     */
    public static String base64ImgEncode(String file){
        InputStream in = null;
        byte[] data = null;
        try 
        {
            in = new FileInputStream(file);        
            data = new byte[in.available()];
            in.read(data);
            in.close();
        } 
        catch (IOException e) 
        {
            e.printStackTrace();
        }
        //对字节数组Base64编码
      //返回Base64编码过的字节数组字符串
        return base64ImgEncode(data);
    }
    
    /**
     * 对加密后的图片进行解密
     * @param imgStr
     * @return
     */
    public static byte[] base64ImgDncode(String imgStr){
        if (imgStr == null) //图像数据为空
            return null;
        BASE64Decoder decoder = new BASE64Decoder();
        try{
            byte[] data = decoder.decodeBuffer(imgStr);
            for(int i=0;i<data.length;++i){
                if(data[i]<0)
                {//调整异常数据
                	data[i]+=256;
                }
            }
            return data;
        } 
        catch (Exception e) 
        {
        	e.printStackTrace();
            return null;
        }
    }    
    
    /**
     * 对加密后的图片进行解密
     * @param imgStr	加密后的图片字符串
     * @param disFile	图片恢复路径
     * @return
     */
    public static boolean base64ImgDncode(String imgStr,String disFile){
        if (imgStr == null) //图像数据为空
            return false;
        try 
        {
            byte[] b = base64ImgDncode(imgStr);
            OutputStream out = new FileOutputStream(disFile);    
            out.write(b);
            out.flush();
            out.close();
            return true;
        } 
        catch (Exception e) 
        {
        	e.printStackTrace();
            return false;
        }
    }
    


    /**
     * des加密
     */
    public static String desEncrypt(String key, String data) throws Exception {
        Cipher cipher = initDesCipher(key.getBytes(), Cipher.ENCRYPT_MODE);
        byte[] result = cipher.doFinal(data.getBytes());
        return Hex.encodeHexString(result);
    }

    /**
     * des解密
     */
    public static String desDecrypt(String key, String data) throws Exception {
        byte[] bytes = Hex.decodeHex(data.toCharArray());
        Cipher cipher = initDesCipher(key.getBytes(), Cipher.DECRYPT_MODE);
        byte[] result = cipher.doFinal(bytes);
        return new String(result);
    }

    private static final String DesAlgorithm = "DES";
    private static Cipher initDesCipher(byte[] key, int mode) throws Exception {
        /** 创建一个密钥工厂，然后用它把DESKeySpec转换成SecretKey对象**/
        SecretKeyFactory factory = SecretKeyFactory.getInstance(DesAlgorithm);
        DESKeySpec dks = new DESKeySpec(key);
        SecretKey secret = factory.generateSecret(dks);

        /** Cipher对象实际完成加密或解密操作**/
        Cipher cipher = Cipher.getInstance(DesAlgorithm);
        /**用密钥初始化Cipher对象**/
        cipher.init(mode, secret);

        return cipher;
    }

    public static void main(String[] args) throws Exception {
        String text = "md5(data)";
        System.out.println("md5text = " + md5(text));
        System.out.println("md5byte = " + md5(text.getBytes("utf8")));
        System.out.println("sha1txt = " + sha1(text));

        String base64 = "base64";
        String encode = base64Encode(base64);
        System.out.println("base64encode = " + encode);
        String decode = base64Decode(encode);
        System.out.println("base64decode = " + decode);

        String key = "arfi!@#$";
        System.out.println("key = " + JsonUtil.fastToJson(key.getBytes()));
        String encrypt = desEncrypt(key, "这是一个测试");
        //key = "G#F$J%U^";
        String decrypt = desDecrypt(key, encrypt);
        System.out.println("encrypt = " + encrypt);
        System.out.println("decrypt = " + decrypt);
    }
}
