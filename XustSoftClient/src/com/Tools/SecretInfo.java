package com.Tools;

public class SecretInfo {

 private static byte[] Key;//对称AES密钥
 private static byte[] publicKey;//非对称RSA公钥
 private static byte[] privateKey;//非对称RSA私钥
 public static byte[] getKey() {
		return Key;
	}
	public static void setKey(byte[] key) {
		Key = key;
	}
	public static byte[] getPublicKey() {
		return publicKey;
	}
	public static void setPublicKey(byte[] publicKey) {
		SecretInfo.publicKey = publicKey;
	}
	public static byte[] getPrivateKey() {
		return privateKey;
	}
	public static void setPrivateKey(byte[] privateKey) {
		SecretInfo.privateKey = privateKey;
	}
 
}
