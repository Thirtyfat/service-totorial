package com.mglj.totorial.framework.common.encryption;

import java.io.UnsupportedEncodingException;

/**
 * @author zhen.ling
 */
public class EncodingUtils {

    public static final String DEFAULT = "UTF-8";
    public static final String UTF_8 = "UTF-8";
    public static final String ISO_8859_1 = "ISO-8859-1";
    public static final String GBK = "GBK";


    public static byte[] encode(String text) {
        try {
            return text.getBytes(DEFAULT);
        } catch (UnsupportedEncodingException e) {
            throw new IllegalArgumentException(e);
        }
    }


    public static String decode(byte[] bytes) {
        try {
            return new String(bytes, DEFAULT);
        } catch (UnsupportedEncodingException e) {
            throw new IllegalArgumentException(e);
        }
    }

}