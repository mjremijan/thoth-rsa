package org.thoth.rsa;

import java.io.InputStream;
import java.security.KeyFactory;
import java.security.spec.KeySpec;
import java.security.spec.PKCS8EncodedKeySpec;
import java.util.Base64;

/**
 *
 * @author Michael Remijan mjremijan@yahoo.com @mjremijan
 */
public class MyKeyFactory {

    private final KeyFactory keyFactory;

    public MyKeyFactory() throws Exception {
        this.keyFactory = KeyFactory.getInstance(getAlgorithm());
    }

    public final String getAlgorithm() {
        return "RSA";
    }

    public MyPrivateKey getPrivateKey(String classpathResource) throws Exception {

        InputStream is = this
            .getClass()
            .getClassLoader()
            .getResourceAsStream(classpathResource);

        String stringBefore
            = new String(is.readAllBytes());

//        String stringAfter = stringBefore
//            .replaceAll("\\n", "")
//            .replaceAll("-----BEGIN PRIVATE KEY-----", "")
//            .replaceAll("-----END PRIVATE KEY-----", "")
//            .trim();

        byte[] decoded = Base64
            .getDecoder()
            .decode(stringAfter);

        KeySpec keySpec
            = new PKCS8EncodedKeySpec(decoded);

        return new MyPrivateKey(keyFactory.generatePrivate(keySpec));
    }
}
