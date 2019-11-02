package com.mage.crm.util;

import org.apache.commons.codec.binary.Base64;

public class Base64Util {

    public static String enCode(String str){

        return  Base64.encodeBase64String(str.getBytes());
    }
    public static String deCode(String str){
        byte[] bytes = Base64.decodeBase64(str);
        return new String(bytes);
    }
}
