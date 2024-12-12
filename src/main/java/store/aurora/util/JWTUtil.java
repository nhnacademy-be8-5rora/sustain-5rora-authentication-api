package store.aurora.util;

import io.jsonwebtoken.Jwts;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Date;

@Component
public class JWTUtil {

    private final SecretKey secretKey;
    private final AESUtil aesUtil;

    public JWTUtil(@Value("${spring.jwt.secret}")String secret, AESUtil aesUtil) {

        secretKey = new SecretKeySpec(secret.getBytes(StandardCharsets.UTF_8), Jwts.SIG.HS256.key().build().getAlgorithm());
        this.aesUtil = aesUtil;
    }

    public String createJwt(String username) {

        String encrypt1 = "";
        try {
            encrypt1 = aesUtil.encrypt(username);
        } catch (Exception e) {
            throw new RuntimeException(String.format("암호화 처리중에 에러가 발생했습니다. e = %s", e.getMessage()));
        }

        return Jwts.builder()
                .claim("username", encrypt1)
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60)) // 1 hour expiration
                .signWith(secretKey)
                .compact();
    }
}