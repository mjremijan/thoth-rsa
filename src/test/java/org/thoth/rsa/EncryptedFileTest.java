package org.thoth.rsa;

import java.io.InputStream;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 *
 * @author Michael Remijan mjremijan@yahoo.com @mjremijan
 */
public class EncryptedFileTest {

    protected Rsa4096 rsa;

    @BeforeEach
    public void setUp() throws Exception {
        rsa = new Rsa4096(
              "./private_key_rsa_4096_pkcs8-generated.pem"
            , "./public_key_rsa_4096_pkcs8-exported.pem"
        );
    }

    @Test
    public void test_encrypted_file()
        throws Exception {
        // Setup
        String expected
            = getFileAsString("./file_unencrypted.txt");

        String encryptedAndEncoded
            = getFileAsString("./file_encrypted_and_encoded.txt");

        // Test
        String actual
            = rsa.decryptFromBase64(encryptedAndEncoded);

        // Assert
        Assertions.assertEquals(expected, actual);
    }

    public String getFileAsString(String classPathResourceLocation)
    throws Exception {
        InputStream is = this
            .getClass()
            .getClassLoader()
            .getResourceAsStream(
                classPathResourceLocation
            );

        byte[] bytes = is.readAllBytes();
        is.close();

        return new String(bytes);
    }
}
