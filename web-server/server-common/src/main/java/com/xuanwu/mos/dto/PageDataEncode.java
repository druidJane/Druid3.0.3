package com.xuanwu.mos.dto;

public interface PageDataEncode {
	public String getDecode(java.security.PrivateKey key, String str);
	public RsaParams init();
	
	public class RsaParams{
		private String exponent;
		private String modulus;
		private java.security.PrivateKey key;
		public String getExponent() {
			return exponent;
		}
		public void setExponent(String exponent) {
			this.exponent = exponent;
		}
		public String getModulus() {
			return modulus;
		}
		public void setModulus(String modulus) {
			this.modulus = modulus;
		}
		public java.security.PrivateKey getKey() {
			return key;
		}
		public void setKey(java.security.PrivateKey key) {
			this.key = key;
		}	
	}
}
