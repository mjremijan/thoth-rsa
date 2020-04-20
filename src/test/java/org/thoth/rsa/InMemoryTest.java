package org.thoth.rsa;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 *
 * @author Michael Remijan mjremijan@yahoo.com @mjremijan
 */
public class InMemoryTest {

    @BeforeEach
    public void setUp() {
    }

    @Test
    public void read_pkcs8_private_key_exported_from_pkcs1_key() throws Exception
    {
        // Setup
        Rsa4096 rsa = new Rsa4096(
              "./private_key_rsa_4096_pkcs8-generated.pem"
            , "./public_key_rsa_4096_pkcs8-exported.pem"
        );
        String expected
            = "Text to be encrypted";

        // Test
        String encryptedAndEncoded
            = rsa.encryptToBase64(expected);
        String actual
            = rsa.decryptFromBase64(encryptedAndEncoded);

        // Assert
        Assertions.assertEquals(expected, actual);
    }
}
