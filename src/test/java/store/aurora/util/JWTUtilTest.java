package store.aurora.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.test.util.ReflectionTestUtils;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.Date;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

class JWTUtilTest {

    private JWTUtil jwtUtil;

    private AESUtil aesUtilMock;
    private SecretKey secretKey;


    @BeforeEach
    void setUp() {
        aesUtilMock = Mockito.mock(AESUtil.class);
        String secret = "myVeryLongSecretKeyForJwtWithMoreThan32Characters1234567890"; // 32 bytes
        jwtUtil = new JWTUtil(secret, aesUtilMock);
        secretKey = new SecretKeySpec(secret.getBytes(StandardCharsets.UTF_8), Jwts.SIG.HS256.key().build().getAlgorithm());
        ReflectionTestUtils.setField(jwtUtil, "secretKey", secretKey);
    }

    @Test
    void testCreateJwt() throws Exception {
        // Given
        String username = "testUser";
        long expirationMs = 1000 * 60 * 10; // 10 minutes
        String encryptedUsername = "encryptedTestUser";

        when(aesUtilMock.encrypt(username)).thenReturn(encryptedUsername);

        // When
        String jwt = jwtUtil.createJwt(username, expirationMs);

        // Then
        Claims payload = Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(jwt).getPayload();

        assertThat(payload.get("username", String.class)).isEqualTo(encryptedUsername);
        assertThat(payload.getExpiration()).isAfter(new Date(System.currentTimeMillis()));
    }

    @Test
    void testCreateJwtWithNullExpiration() throws Exception {
        // Given
        String username = "testUser";
        Long expirationMs = null; // Default expiration
        String encryptedUsername = "encryptedTestUser";

        when(aesUtilMock.encrypt(username)).thenReturn(encryptedUsername);

        // When
        String jwt = jwtUtil.createJwt(username, expirationMs);

        // Then
        Claims payload = Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(jwt).getPayload();

        assertThat(payload.get("username", String.class)).isEqualTo(encryptedUsername);
        assertThat(payload.getExpiration()).isAfter(new Date(System.currentTimeMillis()));
    }

    @Test
    void testCreateJwtThrowsExceptionOnEncryptionError() throws Exception {
        // Given
        String username = "testUser";
        long expirationMs = 1000 * 60 * 10; // 10 minutes

        when(aesUtilMock.encrypt(username)).thenThrow(new RuntimeException("Encryption error"));

        // When / Then
        Throwable thrown = org.assertj.core.api.Assertions.catchThrowable(() -> jwtUtil.createJwt(username, expirationMs));
        assertThat(thrown).isInstanceOf(RuntimeException.class);
    }
}
