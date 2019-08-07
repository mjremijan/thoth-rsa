package org.thoth.rsa;

import java.io.InputStream;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.KeySpec;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;
import java.util.Properties;
import javax.crypto.Cipher;

public class Main {

    private static KeyFactory keyFactory;

    public static KeyFactory getKeyFactory() throws Exception {
        if (keyFactory == null) {
            keyFactory = KeyFactory.getInstance("RSA");
        }
        return keyFactory;
    }

    private static PublicKey publicKey;

    public static PublicKey getPublicKey() throws Exception {
        if (publicKey == null) {
            String resource = "./public_key_rsa_4096_pkcs8.pem";
            //System.out.printf("Resource = \"%s\"%n", resource);

            InputStream is = Main.class.getClassLoader().getResourceAsStream(resource);
            //System.out.printf("InputStream = %s%n", is.toString());

            byte[] bytes = is.readAllBytes();
            //System.out.printf("bytes.length = %d%n", bytes.length);

            String stringBefore = new String(bytes);
            //System.out.printf("stringBefore = \"%s\"%n", stringBefore);

            String stringAfter = stringBefore
                .replaceAll("\\n", "")
                .replaceAll("-----BEGIN PUBLIC KEY-----", "")
                .replaceAll("-----BEGIN RSA PUBLIC KEY-----", "")
                .replaceAll("-----END PUBLIC KEY-----", "")
                .replaceAll("-----END RSA PUBLIC KEY-----", "")
                .trim();
            //System.out.printf("stringAfter = \"%s\"%n", stringAfter);

            byte[] decoded = Base64.getDecoder().decode(stringAfter);
            //System.out.printf("decoded.length = %d%n", decoded.length);

            KeySpec keySpec = new X509EncodedKeySpec(decoded);
            //System.out.printf("keySpec = %s%n", keySpec.toString());

            publicKey = getKeyFactory().generatePublic(keySpec);
            //System.out.printf("publicKey = %s%n", publicKey.toString());
        }
        return publicKey;
    }

    private static PrivateKey privateKey;

    public static PrivateKey getPrivateKey() throws Exception {
        if (privateKey == null) {
            String resource = "./private_key_rsa_4096_pkcs8.pem";
            //System.out.printf("Resource = \"%s\"%n", resource);

            InputStream is = Main.class.getClassLoader().getResourceAsStream(resource);
            //System.out.printf("InputStream = %s%n", is.toString());

            byte[] bytes = is.readAllBytes();
            //System.out.printf("bytes.length = %d%n", bytes.length);

            String stringBefore = new String(bytes);
            //System.out.printf("stringBefore = \"%s\"%n", stringBefore);

            String stringAfter = stringBefore
                .replaceAll("\\n", "")
                .replaceAll("-----BEGIN PRIVATE KEY-----", "")
                .replaceAll("-----BEGIN RSA PRIVATE KEY-----", "")
                .replaceAll("-----END PRIVATE KEY-----", "")
                .replaceAll("-----END RSA PRIVATE KEY-----", "")
                .trim();
            //System.out.printf("stringAfter = \"%s\"%n", stringAfter);

            byte[] decoded = Base64.getDecoder().decode(stringAfter);
            //System.out.printf("decoded.length = %d%n", decoded.length);

            KeySpec keySpec = new PKCS8EncodedKeySpec(decoded);
            //System.out.printf("keySpec = %s%n", keySpec.toString());

            privateKey = getKeyFactory().generatePrivate(keySpec);
            //System.out.printf("privateKey = %s%n", privateKey.toString());
        }
        return privateKey;
    }

    public static String getAlgorithm() {
        return "RSA";
    }

    /**
     * Test the EncryptionUtil
     */
    public static void main(String[] args) {

        try {
            final String originalText = "Text to be encrypted ";

            // Encrypt the string using the public key
            final PublicKey publicKey = getPublicKey();
            final byte[] cipherText = encrypt(originalText, publicKey);

            // Decrypt the cipher text using the private key.
            final PrivateKey privateKey = getPrivateKey();
            final String plainText = decrypt(cipherText, privateKey);

            // Printing the Original, Encrypted and Decrypted Text
            System.out.println("Original: " + originalText);
            System.out.println("Encrypted: " + cipherText.toString());
            System.out.println("Decrypted: " + plainText);

            outdat("base64_solaris_unix.dat");
            outdat("base64_solaris_dos.dat");
            outdat("base64_windows_unix.dat");
            outdat("base64_windows_dos.dat");

            outdat("base64_solaris_unix_nl.dat");
            outdat("base64_solaris_dos_nl.dat");
            outdat("base64_windows_unix_nl.dat");
            outdat("base64_windows_dos_nl.dat");

            outprop("app2.properties");

            outdat("2010.dat");
            
            outprop("app3.properties");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public static void outprop(String filename) throws Exception {
        System.out.printf("%n%n");
        Properties properties = new Properties();
        properties.load(Main.class.getClassLoader().getResourceAsStream("./" + filename));
        String base64Password = properties.getProperty("password");
        String decryptedPassword = decrypt(Base64.getDecoder().decode(base64Password), privateKey);
        System.out.printf("%s decryptedPassword = \"%s\" %n", filename, decryptedPassword);
    }
    
    
    public static void outdat(String filename) throws Exception {
        System.out.printf("%n%n");
        final byte[] base64EncodedBytes = getFile("./" + filename);
        final byte[] base64DecodedBytes = Base64.getDecoder().decode(base64EncodedBytes);
        System.out.printf("%s.length = %d%n", filename, base64DecodedBytes.length);
        String base64DecryptedString = decrypt(base64DecodedBytes, privateKey);
        System.out.printf("%s: \"%s\"%n", filename, base64DecryptedString);
    }

    public static byte[] getFile(String file) throws Exception {
        String resource = file;
        //System.out.printf("Resource = \"%s\"%n", resource);

        InputStream is = Main.class.getClassLoader().getResourceAsStream(resource);
        //System.out.printf("InputStream = %s%n", is.toString());

        byte[] bytes = is.readAllBytes();
        //System.out.printf("bytes.length = %d%n", bytes.length);

        return bytes;
    }

    /**
     * Encrypt the plain text using public key.
     *
     * @param text : original plain text
     * @param key :The public key
     * @return Encrypted text
     * @throws java.lang.Exception
     */
    public static byte[] encrypt(String text, PublicKey key) {
        byte[] cipherText = null;
        try {
            // get an RSA cipher object and print the provider
            final Cipher cipher = Cipher.getInstance(getAlgorithm());
            // encrypt the plain text using the public key
            cipher.init(Cipher.ENCRYPT_MODE, key);
            cipherText = cipher.doFinal(text.getBytes());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return cipherText;
    }

    /**
     * Decrypt text using private key.
     *
     * @param text :encrypted text
     * @param key :The private key
     * @return plain text
     * @throws java.lang.Exception
     */
    public static String decrypt(byte[] text, PrivateKey key) {
        byte[] dectyptedText = null;
        try {
            // get an RSA cipher object and print the provider
            final Cipher cipher = Cipher.getInstance(getAlgorithm());

            // decrypt the text using the private key
            cipher.init(Cipher.DECRYPT_MODE, key);
            dectyptedText = cipher.doFinal(text);

        } catch (Exception ex) {
            ex.printStackTrace();
        }

        String s = new String(dectyptedText);

        // remove last newline characters if they exist
        return chomp(s);
    }

    public static final char LF = '\n';
    public static final char CR = '\r';

    /**
     * <p>
     * NOTE: This code is from Jakarta commons lang</p>
     * 
     * <p>
     * Removes one newline from end of a String if it's there, otherwise leave
     * it alone. A newline is &quot;{@code \n}&quot;, &quot;{@code \r}&quot;, or
     * &quot;{@code \r\n}&quot;.</p>
     *
     * <p>
     * NOTE: This method changed in 2.0. It now more closely matches Perl
     * chomp.</p>
     *
     * <pre>
     * StringUtils.chomp(null)          = null
     * StringUtils.chomp("")            = ""
     * StringUtils.chomp("abc \r")      = "abc "
     * StringUtils.chomp("abc\n")       = "abc"
     * StringUtils.chomp("abc\r\n")     = "abc"
     * StringUtils.chomp("abc\r\n\r\n") = "abc\r\n"
     * StringUtils.chomp("abc\n\r")     = "abc\n"
     * StringUtils.chomp("abc\n\rabc")  = "abc\n\rabc"
     * StringUtils.chomp("\r")          = ""
     * StringUtils.chomp("\n")          = ""
     * StringUtils.chomp("\r\n")        = ""
     * </pre>
     *
     * @param str the String to chomp a newline from, may be null
     * @return String without newline, {@code null} if null String input
     */
    public static String chomp(String str) {
        if (str == null || str.length() == 0) {
            return str;
        }

        if (str.length() == 1) {
            char ch = str.charAt(0);
            if (ch == CR || ch == LF) {
                return "";
            }
            return str;
        }

        int lastIdx = str.length() - 1;
        char c2 = str.charAt(lastIdx);
        char c1 = str.charAt(lastIdx - 1);
        System.out.printf("last: %d, %d%n", (int)c1, (int)c2);

        if (c2 == LF) {
            if (c1  == CR) {
                lastIdx--;
            }
        } else if (c2 != CR) {
            lastIdx++;
        }
        return str.substring(0, lastIdx);
    }

}
