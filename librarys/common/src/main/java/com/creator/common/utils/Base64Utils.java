package com.creator.common.utils;

import java.util.Base64;

public class Base64Utils {

    // 编码方法
    public static String encode(String plainText) {
        byte[] encodedBytes = Base64.getEncoder().encode(plainText.getBytes());
        return new String(encodedBytes);
    }

    // 解码方法
    public static String decode(String base64Text) {
        byte[] decodedBytes = Base64.getDecoder().decode(base64Text.getBytes());
        return new String(decodedBytes);
    }

    /*public static void main(String[] args) {
        String originalText = "Hello, World!";

        // 编码
        String encodedText = encode(originalText);
        System.out.println("Encoded: " + encodedText);

        // 解码
        String decodedText = decode(encodedText);
        System.out.println("Decoded: " + decodedText);
    }*/
}
