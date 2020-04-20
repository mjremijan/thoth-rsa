package org.thoth.rsa;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 *
 * @author Michael Remijan mjremijan@yahoo.com @mjremijan
 */
public class PrivateKeyPkcs8GeneratedTest {

    @BeforeEach
    public void setUp() {
    }

    @Test
    public void read_pkcs8_private_key_directly_generated() throws Exception
    {
        String cpr = "./private_key_rsa_4096_pkcs8-generated.pem";

        MyKeyFactory mkf = new MyKeyFactory();
        MyPrivateKey mpk = mkf.getPrivateKey(cpr);

        Assertions.assertNotNull(mpk);
    }


}
