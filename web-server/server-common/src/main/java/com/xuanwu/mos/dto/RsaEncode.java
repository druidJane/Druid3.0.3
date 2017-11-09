package com.xuanwu.mos.dto;

import com.xuanwu.mos.utils.RSAUtil;

import java.security.KeyPair;
import java.security.interfaces.RSAPublicKey;

public class RsaEncode implements PageDataEncode {
	private Integer bit = 0x10;
	
	@Override	
	public RsaParams init(){
		try {
			KeyPair keyPair = RSAUtil.generateKeyPair();
			RSAPublicKey rsap = (RSAPublicKey) keyPair.getPublic();
			RsaParams params = new RsaParams();
			params.setExponent(rsap.getPublicExponent().toString(bit));
			params.setModulus(rsap.getModulus().toString(bit));
			params.setKey(keyPair.getPrivate());
			return params;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public String getDecode(java.security.PrivateKey key,String str) {
		try {
			byte[] en_result = hexStringToBytes(str); //new BigInteger(str, bit).toByteArray();
			byte[] de_result = RSAUtil.decrypt(key, en_result);
			StringBuilder sb = new StringBuilder();
			sb.append(new String(de_result));
			return sb.reverse().toString();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return str;
	}
	
	public static byte[] hexStringToBytes(String hexString) {
        if (hexString == null || hexString.equals("")) {
            return null;
        }
        hexString = hexString.toUpperCase();
        int length = hexString.length() / 2;
        char[] hexChars = hexString.toCharArray();
        byte[] d = new byte[length];
        for (int i = 0; i < length; i++) {
            int pos = i * 2;
            d[i] = (byte) (charToByte(hexChars[pos]) << 4 | charToByte(hexChars[pos + 1]));
        }
        return d;
    }

    /**
     * Convert char to byte
     * 
     * @param c char
     * @return byte
     */
    private static byte charToByte(char c) {
        return (byte) "0123456789ABCDEF".indexOf(c);
    }
}