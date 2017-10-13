package com.xuanwu.msggate.common.encode;

public class HexUtils {
	public static final String HexCode[] = { "0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "A", "B", "C", "D", "E", "F" };

	public static String byteArrayToHexString(byte b[]) {
		if (b == null) {
			return null;
		}
		String result = "";
		for (int i = 0; i < b.length; i++) {
			result = result + byteToHexString(b[i]);
		}
		return result;
	}

	public static String byteToHexString(byte b) {
		int n = b;
		if (n < 0) {
			n = 256 + n;
		}
		int d1 = n / 16;
		int d2 = n % 16;
		return HexCode[d1] + HexCode[d2];
	}

	public static byte[] hexStringToByteArray(String text) {
		if (text == null) {
			return null;
		}
		byte[] result = new byte[text.length() / 2];
		for (int i = 0; i < result.length; ++i) {
			int x = Integer.parseInt(text.substring(i * 2, i * 2 + 2), 16);
			result[i] = x <= 127 ? (byte) x : (byte) (x - 256);
		}
		return result;
	}

}
