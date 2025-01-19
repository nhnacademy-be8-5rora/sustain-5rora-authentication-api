package store.aurora.util;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.util.ReflectionTestUtils;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.util.Base64;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
class AESUtilTest {

    @InjectMocks
    private AESUtil aesUtil;

    @BeforeEach
    void setUp() {
        // secretKey를 강제로 설정 (Spring @Value 모의)
        ReflectionTestUtils.setField(aesUtil, "SECRET_KEY", "1234567890123456");
    }

    @Test
    void testEncrypt_withValidInput() throws Exception {
        // Given
        String input = "HelloWorld";

        // When
        String encrypted = aesUtil.encrypt(input);

        // Then
        assertThat(encrypted).isNotNull();

        // Decrypt the string manually to verify correctness
        SecretKeySpec keySpec = new SecretKeySpec("1234567890123456".getBytes(), "AES");
        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.DECRYPT_MODE, keySpec);
        byte[] decryptedBytes = cipher.doFinal(Base64.getDecoder().decode(encrypted));
        String decrypted = new String(decryptedBytes);

        assertThat(decrypted).isEqualTo(input);
    }

    @Test
    void testEncrypt_withEmptyInput() throws Exception {
        // Given
        String input = "";

        // When
        String encrypted = aesUtil.encrypt(input);

        // Then
        assertThat(encrypted).isNotNull();

        // Decrypt the string manually to verify correctness
        SecretKeySpec keySpec = new SecretKeySpec("1234567890123456".getBytes(), "AES");
        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.DECRYPT_MODE, keySpec);
        byte[] decryptedBytes = cipher.doFinal(Base64.getDecoder().decode(encrypted));
        String decrypted = new String(decryptedBytes);

        assertThat(decrypted).isEqualTo(input);
    }

    @Test
    void testEncrypt_withNullInput() {
        // Given
        String input = null;

        // When & Then
        assertThatThrownBy(() -> aesUtil.encrypt(input))
                .isInstanceOf(Exception.class)
                .hasMessageContaining("null");
    }
}