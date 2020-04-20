# Thoth RSA

Research on using `ssh-keygen` or `openssl` to generate public/private keys
which a Java application can use.

See blog for all information:

<https://javadigest.wordpress.com/2012/08/26/rsa-encryption-example/>


# Encrypt something with the key

```text
## This reads in a file
openssl rsautl -encrypt -inkey public_key_rsa_4096_pkcs8.pem -pubin -in plaintext.txt -out foo.dat 
```

```text
## This uses echo
echo "does the ECHO work?" | openssl rsautl -encrypt -inkey public_key_rsa_4096_pkcs8.pem -pubin -out foo.dat
```

```text
## This uses echo and base64
## This dat file has multiple lines. If the newlines are removed it decodes fine
## got to figure if multiple lines can be avoided.
echo "abc123" | openssl rsautl -encrypt -inkey public_key_rsa_4096_pkcs8.pem -pubin | openssl enc -A -base64 > base64.dat
```

```text
## This reads a file and then base64
openssl rsautl -encrypt -inkey public_key_rsa_4096_pkcs8.pem -pubin -in plaintext.txt | openssl enc -A -base64 > base64.dat
```

```text
## This is echo, base64, sed, and a properties file. This seems to work OK
## but echo adds a newline at the end...don't want a newline
sed "s|YYY|`echo "abc123" | openssl rsautl -encrypt -inkey public_key_rsa_4096_pkcs8.pem -pubin | openssl enc -A -base64`|g" app.properties > app2.properties
```

```text
## This is printf, base64, sed, and a properties file. printf won't
## add a newline if you don't want it to.
sed "s|YYY|`printf "ending-with newline" | openssl rsautl -encrypt -inkey public_key_rsa_4096_pkcs8.pem -pubin | openssl enc -A -base64`|g" app.properties > app3.properties
```

## References
1. Using OpenSSL to encrypt messages and files on Linux. <https://linuxconfig.org/using-openssl-to-encrypt-messages-and-files-on-linux>

2. How to remove newline from output. <https://stackoverflow.com/questions/35799684/how-to-remove-newline-from-output>