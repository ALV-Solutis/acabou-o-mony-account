package br.com.acaboumony.util;

import java.security.SecureRandom;
import java.util.Random;

public class SecurityUtil {

    public static String generateVerificationCode() {
        int a = new Random().nextInt(999999);
        return String.format("%06d", a);
    }
}
