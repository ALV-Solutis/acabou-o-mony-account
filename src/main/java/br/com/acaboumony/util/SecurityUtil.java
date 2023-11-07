package br.com.acaboumony.util;

import java.security.SecureRandom;
import java.util.Random;

public class SecurityUtil {

    private static final String ALPHABET = "ABCDEFGHJKLMNPQRSTUVWXYZ23456789";
    private static final int CONFIRMATION_CODE_SIZE = 20;

    public static String generateConfirmationCode() {
        SecureRandom random = new SecureRandom();
        StringBuilder sb = new StringBuilder(CONFIRMATION_CODE_SIZE);

        for (int i = 0; i < CONFIRMATION_CODE_SIZE; i++) {
            int index = random.nextInt(ALPHABET.length());
            char character = ALPHABET.charAt(index);
            sb.append(character);

            if (i % 4 == 3 && i < CONFIRMATION_CODE_SIZE - 1) {
                sb.append('-');
            }
        }
        return sb.toString();
    }

    public static String generateVerificationCode() {
        int a = new Random().nextInt(999999);
        return String.format("%06d", a);
    }
}
