package org.thoth.rsa;

import java.io.InputStream;
import java.util.Base64;
import java.util.Properties;

public class Main {




    public static String getAlgorithm() {
        return "RSA";
    }

    /**
     * Test the EncryptionUtil
     */
    public static void main(String[] args) {

        try {
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
     * Decrypt text using private key.
     *
     * @param text :encrypted text
     * @param key :The private key
     * @return plain text
     * @throws java.lang.Exception
     */


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
