package org.thoth.rsa;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 *
 * @author Michael Remijan mjremijan@yahoo.com @mjremijan
 */
public class PrivateKeyPkcs8ExportTest {

    @BeforeEach
    public void setUp() {
    }

    @Test
    public void read_pkcs8_private_key_exported_from_pkcs1_key() throws Exception
    {
        String cpr = "./private_key_rsa_4096_pkcs8-exported.pem";

        MyKeyFactory mkf = new MyKeyFactory();
        MyPrivateKey mpk = mkf.getPrivateKey(cpr);

        Assertions.assertNotNull(mpk);
    }


}
