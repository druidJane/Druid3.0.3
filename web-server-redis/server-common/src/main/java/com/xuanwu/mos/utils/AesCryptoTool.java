package com.xuanwu.mos.utils;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

/**
 * Not thread safe AES encrypt and decrypt tool,
 * key length: 128 bit
 */
public class AesCryptoTool {
	
	private static final String KEY_ALGORITHM = "AES";
	private static final String CIPHER_ALGORITHM = "AES/CBC/PKCS5Padding"; // Algorithm/Mode/Padding
	
	/**
	 * 128 bit IV, !!WARNING!!: don't change this value unless you're sure of what you're doing.
	 */
	private static final IvParameterSpec IV = new IvParameterSpec("d3cLUveYlaPsqcGx".getBytes());
	
	private SecretKeySpec sKeySpec = null;
	private Cipher cipher = null;
	
	public AesCryptoTool(String key) {
		sKeySpec = new SecretKeySpec(key.getBytes(), KEY_ALGORITHM);
		try {
			cipher = Cipher.getInstance(CIPHER_ALGORITHM);
		} catch (Exception e) {
			throw new RuntimeException("Creat cipher instance error", e);
		}
	}
	
	public byte[] encrypt(byte[] text) throws Exception {
		cipher.init(Cipher.ENCRYPT_MODE, sKeySpec, IV);
		return cipher.doFinal(text);
	}
	
	public byte[] decrypt(byte[] content) throws Exception {
		cipher.init(Cipher.DECRYPT_MODE, sKeySpec, IV);
		return cipher.doFinal(content);
	}
	
}
