package com.mage.crm.util;




import org.apache.commons.codec.binary.Base64;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Md5Util {
    public static String encode(String str){
        try {
            MessageDigest messageDigest = MessageDigest.getInstance("md5");
            return Base64.encodeBase64String(messageDigest.digest(str.getBytes()));
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return null;
    }
}
