package bg.sofia.uni.fmi.mjt.space.algorithm;

import bg.sofia.uni.fmi.mjt.space.exception.CipherException;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;

import java.io.InputStream;
import java.io.OutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

import java.nio.charset.StandardCharsets;
import java.security.NoSuchAlgorithmException;

import static bg.sofia.uni.fmi.mjt.space.algorithm.Rijndael.ENCRYPTION_ALGORITHM;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class RijndaelTest {
    private static final int KEY_SIZE_IN_BITS = 128;
    private static SymmetricBlockCipher blockCipher;

    @BeforeAll
    static void initialize() throws NoSuchAlgorithmException {
        KeyGenerator keyGenerator = KeyGenerator.getInstance(ENCRYPTION_ALGORITHM);
        keyGenerator.init(KEY_SIZE_IN_BITS);

        SecretKey secretKey = keyGenerator.generateKey();

        blockCipher = new Rijndael(secretKey);
    }

    @Test
    void testEncryptionAndDecryptionIsCorrect() throws CipherException, IOException {
        String text = "I want to break free";
        InputStream inputStream = new ByteArrayInputStream(text.getBytes(StandardCharsets.UTF_8));
        ByteArrayOutputStream encryptedOutputStream = new ByteArrayOutputStream();

        blockCipher.encrypt(inputStream, encryptedOutputStream);

        byte[] bytes = encryptedOutputStream.toByteArray();

        InputStream encryptedInputStream = new ByteArrayInputStream(bytes);
        OutputStream decryptedOutputStream = new ByteArrayOutputStream();

        blockCipher.decrypt(encryptedInputStream, decryptedOutputStream);

        String decryptedText = decryptedOutputStream.toString();

        inputStream.close();
        encryptedOutputStream.close();
        encryptedInputStream.close();
        decryptedOutputStream.close();

        assertEquals(text, decryptedText,
                "Expected : " + text + ", but it was : " + decryptedText);
    }
}
