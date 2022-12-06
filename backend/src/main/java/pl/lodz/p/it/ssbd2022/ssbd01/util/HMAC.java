package pl.lodz.p.it.ssbd2022.ssbd01.util;

import lombok.SneakyThrows;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.util.Formatter;

public class HMAC {
    private static final String HMAC_SHA256 = "HmacSHA256";

    private static final String SECRET_KEY = "Al-BNmoM80nHDaE2rCMlFAzRxXJtvRC6jzblTKr1vOc";

    @SneakyThrows
    public static String calculateHMAC(String login, Long version) {
        String data = login + version.toString();

        SecretKeySpec secretKeySpec = new SecretKeySpec(SECRET_KEY.getBytes(), HMAC_SHA256);
        Mac mac = Mac.getInstance(HMAC_SHA256);
        mac.init(secretKeySpec);
        return toHexString(mac.doFinal(data.getBytes()));
    }

    @SneakyThrows
    public static String calculateHMAC(Long id, Long version) {
        String data = id.toString() + version.toString();

        SecretKeySpec secretKeySpec = new SecretKeySpec(SECRET_KEY.getBytes(), HMAC_SHA256);
        Mac mac = Mac.getInstance(HMAC_SHA256);
        mac.init(secretKeySpec);
        return toHexString(mac.doFinal(data.getBytes()));
    }

    private static String toHexString(byte[] bytes) {
        Formatter formatter = new Formatter();
        for (byte b : bytes) {
            formatter.format("%02x", b);
        }
        return formatter.toString();
    }


}
