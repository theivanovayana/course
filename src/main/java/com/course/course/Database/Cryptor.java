package com.course.course.Database;


import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Cryptor {
    private static final String SHA_256 = "SHA-256";

    private Cryptor() {}


    public static String hashSHA256(String input) {
        try {
            MessageDigest digest = MessageDigest.getInstance(SHA_256);
            byte[] hashBytes = digest.digest(input.getBytes(StandardCharsets.UTF_8));
            return bytesToHex(hashBytes);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("SHA-256 algorithm not available", e);
        }
    }

    private static String bytesToHex(byte[] bytes) {
        StringBuilder hexString = new StringBuilder();
        for (byte b : bytes) {
            String hex = Integer.toHexString(0xff & b);
            if (hex.length() == 1) {
                hexString.append('0');
            }
            hexString.append(hex);
        }
        return hexString.toString();
    }


    public static boolean verifySHA256(String input, String hashHex) {
        String inputHash = hashSHA256(input);
        return inputHash.equals(hashHex);
    }
}