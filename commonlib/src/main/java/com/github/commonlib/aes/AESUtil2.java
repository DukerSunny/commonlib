package com.github.commonlib.aes;

import android.util.Log;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

/**
 * @Auther:
 * @Date: 2018/10/19 15:49
 * @Description:  AES加密算法
 */
public class AESUtil2 {
    private static String charset = "utf-8";
    // 偏移量
    private static int offset = 16;
    // 加密器类型:加密算法为AES,加密模式为CBC,补码方式为PKCS5Padding
    private static String transformation = "AES/CBC/PKCS5Padding";
    // 算法类型：用于指定生成AES的密钥
    private static String algorithm = "AES";


    public static String generateDesKey() throws Exception {
        return generateDesKey(256);
    }
    /**
     * 生成符合AES要求的密钥
     * @param length
     * @return
     * @throws Exception
     */
    public static String generateDesKey(int length) throws Exception {
        //实例化
        KeyGenerator kgen = null;
        kgen = KeyGenerator.getInstance("AES");
        //设置密钥长度
        kgen.init(length);
        //生成密钥
        SecretKey skey = kgen.generateKey();
        //返回密钥的二进制编码
        return Base64Util.encodeBase64String(skey.getEncoded());
    }

    /**
     * 加密
     *
     * @param content 需要加密的内容
     * @param key     加密密码
     * @return
     */
    public static String encrypt(String content, String key) {
        return encrypt(content,key.getBytes());
    }

    public static String encrypt(String content, byte[] key) {
        try {
            //构造密钥
            SecretKeySpec skey = new SecretKeySpec(key, algorithm);
            //创建初始向量iv用于指定密钥偏移量(可自行指定但必须为128位)，因为AES是分组加密，下一组的iv就用上一组加密的密文来充当
            IvParameterSpec iv = new IvParameterSpec(key, 0, offset);
            //创建AES加密器
            Cipher cipher = Cipher.getInstance(transformation);
            byte[] byteContent = content.getBytes(charset);
            //使用加密器的加密模式
            cipher.init(Cipher.ENCRYPT_MODE, skey, iv);
            // 加密
            byte[] result = cipher.doFinal(byteContent);
            //使用BASE64对加密后的二进制数组进行编码
            return Base64Util.encodeBase64String(result);
        } catch (Exception e) {
            Log.e("AES加密异常", e.getMessage());
        }
        return null;
    }

    /**
     * AES（256）解密
     *
     * @param content 待解密内容
     * @param key     解密密钥
     * @return 解密之后
     * @throws Exception
     */
    public static String decrypt(String content, String key) {
        return decrypt(content,key.getBytes());
    }
    public static String decrypt(String content,  byte[] key) {
        try {

            SecretKeySpec skey = new SecretKeySpec(key, algorithm);
            IvParameterSpec iv = new IvParameterSpec(key, 0, offset);
            Cipher cipher = Cipher.getInstance(transformation);
            //解密时使用加密器的解密模式
            // 初始化
            cipher.init(Cipher.DECRYPT_MODE, skey, iv);
            byte[] result = cipher.doFinal(Base64Util.decodeBase64(content));
            // 解密
            return new String(result);
        } catch (Exception e) {
            Log.e("AES解密异常:{}", e.getMessage());
        }
        return null;
    }
}
