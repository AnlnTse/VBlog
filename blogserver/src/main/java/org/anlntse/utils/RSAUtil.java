package org.anlntse.utils;

import org.apache.commons.codec.binary.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.crypto.Cipher;
import java.security.Key;
import java.security.KeyFactory;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

public class RSAUtil {
    private static final String KEY_ALGORITHM = "RSA";

    public static final String pubKeyLicense = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEA2Jm16UfXKQiVzD8/h9Z1eIoUzimw/R6rbyqH6KA+TIpHyb01MEr4Pd/D8DJA2jDTdkRi2uyeZh21+zCr4vpprKgen2tDgEW00XU6D/WTdn8LebjxgMcT1xnFkm+EOBIp1l9LBiOr3VROooZfXvbE7xd8TKKgyiLOhwQ4I0gaYV40fa58WcyUk6AAPONe156kaMAoQghrsKuvfOZjLZH371TJC4RRrQaYG5JCfB6JNIfu7PRbUyDImfuuZ6sXWUYBkV6h364bt/uS91RtVUBjg+s1oap8brnwPPLpxMjkJ5Umc3KItQengqwBtmr0We3K0YzhhKCKzC/QQk+1AOhJrQIDAQAB";
    //    public static final String priKeyLicense = "MIIEvwIBADANBgkqhkiG9w0BAQEFAASCBKkwggSlAgEAAoIBAQDYmbXpR9cpCJXMPz+H1nV4ihTOKbD9HqtvKofooD5MikfJvTUwSvg938PwMkDaMNN2RGLa7J5mHbX7MKvi+mmsqB6fa0OARbTRdToP9ZN2fwt5uPGAxxPXGcWSb4Q4EinWX0sGI6vdVE6ihl9e9sTvF3xMoqDKIs6HBDgjSBphXjR9rnxZzJSToAA8417XnqRowChCCGuwq6985mMtkffvVMkLhFGtBpgbkkJ8Hok0h+7s9FtTIMiZ+65nqxdZRgGRXqHfrhu3+5L3VG1VQGOD6zWhqnxuufA88unEyOQnlSZzcoi1B6eCrAG2avRZ7crRjOGEoIrML9BCT7UA6EmtAgMBAAECggEBALBNbSRUtW1Q750wXiXTCgrzWbv1c3qUXDZACB/xTsQ+SfCLcZ+9ZhH28SWxwdfXpke9ojlQIB4+UMIE++Pkr985eLOKO4dNNcDIClqzKTRqdIy/XNPVNvD+qTJoUtbQ8fzr08VLg7jDxr3DYh2J/wIdu68lMHtXmYOMi6UsK0zUQcCOstdG2Lofhb5yICIU4LfH3gS/1Jx/Zk55xhWx1hCln/qEHjftvLoUWy8OgwuhAt1hdbEMO5LoQ8zGzZaMOoeAbmA2WsOcLGthVIWPMkQHEIC437RO7K//jb8DlNbQcJbgh2k0eIpH5GO5zuANM4Q8gnuufJK17PXeayGkCoECgYEA73GFyE/zcYR12GioVYvLTbvlAktuFV/Slhlu37isU+pLyXrnrgFxWPwHtwKmr4t3P0K4RGYhjw0HHMtBr8vxRiuc/pg+itPl3+On2U5HTAuPZccACcZcpH9q/sn80tlTtB6Lcx0CSUuBubP8XivR8xeTDwq5toOWT0pw0/UugUMCgYEA55PV1hooJfjvL4Rp4btenMuuqYL5vE2uPWkmBeqR45XfJc2PKPzDhn7TY2BjzKKYhoQg+EbTZaEN88taYbqp4YqjIEgHAmhZ9f8u9YK7ZWQel8OV3hfJGMKxi8YKKvqI+XAAlccIblikJRfwuWB/kZ19F3dc9hAmorJnFxoWok8CgYBqm3XAIPC32w0JBFfmdq5W9tBYvDLQK3C8SaiWzUdUuV1y4FjknVnVD6lzryd9wy+dp9wZAAvzAA7poAmn9rp+zo5AwJIgB4Yyr86crwCJeCApZXe74mh/CQgfVtlNG1vsNylJYGITNh4VC3DSkNoSTK26JcFX5LB4ryEbDe/dtwKBgQDJZ5Pkv58HV3d+1iTYRANHTwY7O0RD9zOBCYAeSSJU0zXeROcreJn80TS8C0sA4LDS3PtnswGMNARUchnLQXMaS18QZlRiTatf/W4z7F1bEzZgO6SOIhWtyTZtH/Ib6wVSG40GtrGodsNb13RvZXPNizEORau9/dH8UjjCqNfilQKBgQCHGIB5FwSt/dudpGwd7P3bc+eWIfPRcObjwwYCU8AKe7DI+8ix8nflMGTyeI9oe29SP3vjT5kl1s2lf6leCR1nJYmy/31snV8TO+QrBU2mLEJhzYdYL8capxkumrHQ62ciX7iLuJ7Tm9o5qgdcqAbUEi+14siIXdVajWAMTZP9ZA==";
    public static final String pubKeyEnc = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQDvs7Nxlx0/hQw8UvkLGYrNCfwbHXVuTLIK9eemwNhuKr9QOAGzovn318ZpPGxslospbsyjZfM5LE8gOqjmSoswkNK/tVBUvll7Js9R6Qd7/wIAmaf3q47X9bNzwh9XjeOGRHEn7RClWo+OQJa2+j/ivuFBBCWQwziJe9ut7MdM9wIDAQAB";
    public static final String priKeyEnc = "MIICeAIBADANBgkqhkiG9w0BAQEFAASCAmIwggJeAgEAAoGBAO+zs3GXHT+FDDxS+QsZis0J/BsddW5Msgr156bA2G4qv1A4AbOi+ffXxmk8bGyWiyluzKNl8zksTyA6qOZKizCQ0r+1UFS+WXsmz1HpB3v/AgCZp/erjtf1s3PCH1eN44ZEcSftEKVaj45Alrb6P+K+4UEEJZDDOIl7263sx0z3AgMBAAECgYEAik7/MIDIUJl9iOU7bCstyseDH0YtNxqr9OUU6EQH3fFueGQIn1VftKFdi+VgjnuDCsIy8+lkoU2uzmLqiA7lJl+4ghg1Nlg81fHKRuQ0bBbu2VhUDpcH8jbbMZQ9SIXmD7OaTNwcv9wUzxdaPlzXyI1Bhrf5saV8H9fgjiUmm+ECQQD/dwLLvajUd2rvQBOaDOwHjEk+9nscJhIVLHpN/yTucBcH0Rljf4KY/dBUNOJDGXSSCUuLpNQkiSY5ZFGMm5unAkEA8DQ8xomCHSLQtI4+Xb3Xj8ffZX0I4k8BZEdBDgt9k6ydbe+6OTZJBSsCTdTG/FYkKSIaFc2cRcWofNN1AZWuMQJBAJfx7VPJZtWYgZ3z+rSx4uFKa3ZrnCXN7wtw/P3PN+Qp/0jC8drgSIk+zd6H5dwLE+6YYLqaOyPP/1A7ftWm6BkCQQCgTpyI+9iETWnwNkZVFY+5e3ESMGIvdv68x/kYwH5sgfUHG8iyyhHtiwicnPa4DV1QvlueVXyH7CRIOm+KQTThAkARLoHvnmOf1Z6DqeTAobH2erVILCejArIsqN7EQDlVu17JIf1yGTIzGa01f3YpNfrAnk0MLci5U9QmHPjV2U/O";

    private static Logger logger = LoggerFactory.getLogger(RSAUtil.class);

    public static String encrypt(String text) {
        return encrypt(text, priKeyEnc);
    }

    public static String encrypt(String text, String key) {
        try {
            byte[] encrpytedBytes = encrypt(text.getBytes("UTF-8"), key);
            byte[] encodedBytes = Base64.encodeBase64(encrpytedBytes);
            return new String(encodedBytes, "ISO-8859-1");
        } catch (Exception e) {
            logger.error("encrypt: " + text + ", " + key, e);
            throw new RuntimeException(e);
        }
    }

    public static String decrypt(String text) {
        return decrypt(text, pubKeyEnc);
    }

    public static String decrypt(String text, String key) {
        try {
            byte[] decodedBytes = Base64.decodeBase64(text.getBytes("ISO-8859-1"));
            byte[] decryptedBytes = decrypt(decodedBytes, key);
            return new String(decryptedBytes, "UTF-8");
        } catch (Exception e) {
            logger.error("decrypt: " + text + ", " + key, e);
            throw new RuntimeException(e);
        }
    }

    private static byte[] encrypt(byte[] data, String key) throws Exception {
        byte[] keyBytes = Base64.decodeBase64(key);

        PKCS8EncodedKeySpec pkcs8KeySpec = new PKCS8EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
        Key privateKey = keyFactory.generatePrivate(pkcs8KeySpec);

        Cipher cipher = Cipher.getInstance(keyFactory.getAlgorithm());
        cipher.init(Cipher.ENCRYPT_MODE, privateKey);

        return cipher.doFinal(data);
    }

    private static byte[] decrypt(byte[] data, String key) throws Exception {
        byte[] keyBytes = Base64.decodeBase64(key);

        X509EncodedKeySpec x509KeySpec = new X509EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
        Key publicKey = keyFactory.generatePublic(x509KeySpec);

        Cipher cipher = Cipher.getInstance(keyFactory.getAlgorithm());
        cipher.init(Cipher.DECRYPT_MODE, publicKey);

        return cipher.doFinal(data);
    }


    public static void main(String[] args) {

        String ss = "system-admin";

        System.out.println(encrypt(ss));
        System.out.println("");


    }
}
