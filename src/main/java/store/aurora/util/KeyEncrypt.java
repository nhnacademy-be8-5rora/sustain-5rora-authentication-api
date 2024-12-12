package store.aurora.util;

import org.springframework.stereotype.Component;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

// https://velog.io/@coals_0329/Spring-%EC%95%94%ED%98%B8%ED%99%94%EB%B3%B5%ED%98%B8%ED%99%94-%ED%95%98%EA%B8%B0

@Component
public class KeyEncrypt {
    private final static String shaAlg = "SHA-256";
    private final static String aesAlg = "AES/CBC/PKCS5Padding";
    private final static String key = "MyTestCode-32CharacterTestAPIKey"; // 16, 24, 32 바이트(영문 32자) 이어야함. todo : 따로 빼기
    private final static String iv = key.substring(0, 16);

    public String encrypt(String text) {
        try {
            String shaKey = getShaKey(text);
            return getAesKey(shaKey);
        } catch (Exception e) {
            throw new RuntimeException(String.format("암호화 처리중에 에러가 발생했습니다. e = %s", e.getMessage()));
        }
    }

    // SHA-256 키 만들기
    private String getShaKey(String text) throws NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance(shaAlg);
        md.update(text.getBytes());
        return bytesToHex(md.digest());
    }

    // AES 키 만들기
    private String getAesKey(String text) throws Exception {
        Cipher cipher = Cipher.getInstance(aesAlg);
        SecretKeySpec keySpec = new SecretKeySpec(key.getBytes(), "AES");
        IvParameterSpec ivParameterSpec = new IvParameterSpec(iv.getBytes());
        cipher.init(Cipher.ENCRYPT_MODE, keySpec, ivParameterSpec);

        byte[] encodedBytes = cipher.doFinal(text.getBytes());
        byte[] encrypted = Base64.getEncoder().encode(encodedBytes);
        return new String(encrypted).trim();
    }

    private String bytesToHex(byte[] bytes) {
        StringBuilder builder = new StringBuilder();
        for (byte b : bytes) {
            builder.append(String.format("%02x", b));
        }
        return builder.toString();
    }
}
