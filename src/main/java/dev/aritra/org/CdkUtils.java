package dev.aritra.org;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;



public class CdkUtils {

    public static final String SSM = "SSM";

    
    static String generateRandomString(int len) {
        String chars = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijk"
                + "lmnopqrstuvwxyz";
        Random rnd = new Random();
        StringBuilder sb = new StringBuilder(len);
        for (int i = 0; i < len; i++)
            sb.append(chars.charAt(rnd.nextInt(chars.length())));
        return sb.toString();
    }

}
