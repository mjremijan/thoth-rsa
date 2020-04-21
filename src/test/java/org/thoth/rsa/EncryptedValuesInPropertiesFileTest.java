package org.thoth.rsa;

import java.util.Properties;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 *
 * @author Michael Remijan mjremijan@yahoo.com @mjremijan
 */
public class EncryptedValuesInPropertiesFileTest {

    protected Rsa4096 rsa;

    @BeforeEach
    public void setUp() throws Exception {
        rsa = new Rsa4096(
              "./private_key_rsa_4096_pkcs8-generated.pem"
            , "./public_key_rsa_4096_pkcs8-exported.pem"
        );
    }

    @Test
    public void test_encrypted_values_in_properties_file()
        throws Exception {
        // Setup
        Properties encryptedAndEncoded
            = new Properties();
        encryptedAndEncoded.load(
            this
            .getClass()
            .getClassLoader()
            .getResourceAsStream(
                "./some_app.properties"
            )
        );

        // Test
        String passwordActual
            = rsa.decryptFromBase64(
                encryptedAndEncoded.getProperty("password")
            );
        String ssnActual
            = rsa.decryptFromBase64(
                encryptedAndEncoded.getProperty("ssn")
            );

        // Assert
        Assertions.assertEquals("SECRET", passwordActual);
        Assertions.assertEquals("123-45-7890", ssnActual);
    }
}
