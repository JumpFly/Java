package com.Tools;

public class SecretInfo {

 private static byte[] Key;//�Գ�AES��Կ
 private static byte[] publicKey;//�ǶԳ�RSA��Կ
 private static byte[] privateKey;//�ǶԳ�RSA˽Կ
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
