package org.anlntse.utils;


import org.anlntse.bean.IPassword;
import org.springframework.util.ObjectUtils;

import java.util.Base64;
import java.util.List;

public class EncryptUtil {

    private static final Base64.Encoder base64_encoder = Base64.getUrlEncoder().withoutPadding();
    private static final Base64.Decoder base64_decoder = Base64.getUrlDecoder();

    public static void convertPassword(List<IPassword> dataList, boolean isEncryptOrDecrypt) {
        if (!ObjectUtils.isEmpty(dataList)) {
            for (IPassword data : dataList) {
                if (data != null && !ObjectUtils.isEmpty(data.getPassword())) {
                    if (isEncryptOrDecrypt) {
                        data.setPassword(EncryptUtil.encryptWithRSA(data.getPassword()));
                    } else {
                        data.setPassword(EncryptUtil.decryptWithRSA(data.getPassword()));
                    }
                }
            }
        }
    }

    public static String encryptWithRSA(String text) {
        if (ObjectUtils.isEmpty(text)) {
            return text;
        }
        if (!text.startsWith("ENC(") || !text.endsWith(")")) {
            text = "ENC(" + RSAUtil.encrypt(text) + ")";
        }
        return text;
    }

    public static String decryptWithRSA(String text) {
        if (ObjectUtils.isEmpty(text)) {
            return text;
        }
        if (text.startsWith("ENC(") && text.endsWith(")")) {
            text = text.substring("ENC(".length(), text.length() - 1);
            text = RSAUtil.decrypt(text);
        }
        return text;
    }

    public static String encryptWithBase64(String text) {
        if (ObjectUtils.isEmpty(text)) {
            return text;
        }
        if (!text.startsWith("ENC(") || !text.endsWith(")")) {
            text = "ENC(" + base64_encoder.encodeToString(text.getBytes()) + ")";
        }
        return text;
    }

    public static String decryptWithBase64(String text) {
        if (ObjectUtils.isEmpty(text)) {
            return text;
        }
        if (text.startsWith("ENC(") && text.endsWith(")")) {
            text = text.substring("ENC(".length(), text.length() - 1);
            text = new String(base64_decoder.decode(text));
        }
        return text;
    }

}
