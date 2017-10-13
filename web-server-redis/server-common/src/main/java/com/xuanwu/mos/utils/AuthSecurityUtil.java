package com.xuanwu.mos.utils;

import org.apache.commons.codec.binary.Base64;

/**
 * Maintain user's Auth-Token security encryption rules.
 *
 * @author <a href="mailto:liqihui@wxchina.com">Qihui.Li</a>
 * @version 1.0.0
 * @date 2016年10月8日
 */
public class AuthSecurityUtil {
    /**
     * The token secret key, !!WARNING!!: don't change this value unless you're sure of what you're doing.
     */
    private static final String TOKEN_SECRET_KEY = "rS6JAvWR7wesSxh3";

    private static final ThreadLocal<AesCryptoTool> AES_CRYPTO = new ThreadLocal<AesCryptoTool>() {
        @Override
        protected AesCryptoTool initialValue() {
            return new AesCryptoTool(TOKEN_SECRET_KEY);
        }
    };

    /**
     * Encrypt the token
     */
    public static String encrypt(String encryptData) throws Exception {
        byte[] encrypted = AES_CRYPTO.get().encrypt(encryptData.getBytes());
        return Base64.encodeBase64String(encrypted);
    }

    /**
     * Decrypt the token
     */
    public static String decrypt(String decryptData) throws Exception {
        byte[] source = Base64.decodeBase64(decryptData);
        return new String(AES_CRYPTO.get().decrypt(source));
    }
}
