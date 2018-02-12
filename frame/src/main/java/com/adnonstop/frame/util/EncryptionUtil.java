package com.adnonstop.frame.util;

import android.text.TextUtils;
import android.util.Base64;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;


/**
 * 加密Util
 */
public class EncryptionUtil {

    /**
     * HMACSHA256加密
     * Base64加密
     *
     * @param data
     * @param key
     * @return
     */
    public static String hmacSHA256(String data, String key) {
        if (!TextUtils.isEmpty(data) && !TextUtils.isEmpty(key)) {
            return hmacSHA256(data.getBytes(), key.getBytes());
        }
        return null;
    }

    /**
     * HMACSHA256加密
     * Base64加密
     *
     * @param data
     * @param key
     * @return
     */
    public static String hmacSHA256(byte[] data, byte[] key) {
        try {
            SecretKeySpec signingKey = new SecretKeySpec(key, "HmacSHA256");
            Mac mac = Mac.getInstance("HmacSHA256");
            mac.init(signingKey);
            byte[] b = mac.doFinal(data);
            return Base64.encodeToString(b, Base64.DEFAULT).trim();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 使用md5加密
     *
     * @param data 待加密的数据
     * @return 加密后的数据
     */
    public static String md5Encrypt(String data) {
        return encrypt("MD5", data);
    }

    /**
     * 加密
     *
     * @param algorithm 加密算法
     * @param data      待加密数据
     * @return 加密结果
     */
    public static String encrypt(String algorithm, String data) {
        try {
            MessageDigest md = MessageDigest.getInstance(algorithm);
            md.update(data.getBytes());
            return StringUtil.byte2HexStr(md.digest());
        } catch (Throwable e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Base64加密
     *
     * @param str 文本
     * @return 加密结果
     */
    public static String encryptBase64(String str) {
        String result = "";
        if (!TextUtils.isEmpty(str)) {
            try {
                result = new String(Base64.encode(str.getBytes("utf-8"), Base64.NO_WRAP), "utf-8");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }
        return result;
    }
}
