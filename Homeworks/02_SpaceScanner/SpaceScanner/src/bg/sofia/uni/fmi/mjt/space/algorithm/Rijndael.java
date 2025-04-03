package bg.sofia.uni.fmi.mjt.space.algorithm;

import bg.sofia.uni.fmi.mjt.space.exception.CipherException;
import bg.sofia.uni.fmi.mjt.space.exception.ExceptionMessages;

import javax.crypto.Cipher;
import javax.crypto.CipherOutputStream;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;

import java.io.OutputStream;
import java.io.InputStream;
import java.io.IOException;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

public class Rijndael implements SymmetricBlockCipher {
    public static final String ENCRYPTION_ALGORITHM = "AES";
    private static final int KILOBYTE = 1024;
    private final SecretKey secretKey;

    public Rijndael(SecretKey secretKey) {
        this.secretKey = secretKey;
    }

    @Override
    public void encrypt(InputStream inputStream, OutputStream outputStream) throws CipherException {
        try {
            Cipher cipher = Cipher.getInstance(ENCRYPTION_ALGORITHM);
            cipher.init(Cipher.ENCRYPT_MODE, secretKey);

            transferBytes(inputStream, outputStream, cipher, ExceptionMessages.PROBLEM_WITH_ENCRYPTING);

        } catch (NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException e) {
            throw new CipherException(ExceptionMessages.PROBLEM_WITH_ENCRYPTING, e);
        }
    }

    @Override
    public void decrypt(InputStream inputStream, OutputStream outputStream) throws CipherException {
        try {
            Cipher cipher = Cipher.getInstance(ENCRYPTION_ALGORITHM);
            cipher.init(Cipher.DECRYPT_MODE, secretKey);

            transferBytes(inputStream, outputStream, cipher, ExceptionMessages.PROBLEM_WITH_DECRYPTING);

        } catch (NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException e) {
            throw new CipherException(ExceptionMessages.PROBLEM_WITH_DECRYPTING, e);
        }
    }

    private void transferBytes(InputStream inputStream, OutputStream outputStream,
                               Cipher cipher, String exceptionMessage) throws CipherException {
        try (OutputStream cryptedOutputStream = new CipherOutputStream(outputStream, cipher)) {
            byte[] buffer = new byte[KILOBYTE];
            int bytesRead;

            while ((bytesRead = inputStream.read(buffer)) != -1) {
                cryptedOutputStream.write(buffer, 0, bytesRead);
            }

        } catch (IOException e) {
            throw new CipherException(exceptionMessage, e);
        }
    }
}
