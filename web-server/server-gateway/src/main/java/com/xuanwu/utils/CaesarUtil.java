package com.xuanwu.utils;

/**
 * 
 * @author liangjiandong
 *
 */
public class CaesarUtil {

	public static String encrypt(String str, int k)
	{
		StringBuilder result = new StringBuilder();
		for (char c : str.toCharArray())
		{
			if (c >= 97 && c <= 122)
			{
				c += k % 26;
				if (c < 97) c += 26;
				if (c > 122) c -= 26;
			}
			else if (c >= 65 && c <= 90)
			{
				c += k % 26;
				if (c < 65) c += 26;
				if (c > 90) c -= 26;
			}
			result.append(c);
		}
		return result.toString();
	}

	public static String decrypt(String str, int k)
	{
		k = 0 - k;
		return encrypt(str, k);
	}

}
