/**
 * 2009-5-20
 */
package com.Secret;

import java.io.FileInputStream;
import java.security.KeyStore;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Signature;
import java.security.cert.Certificate;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;

import javax.crypto.Cipher;

/**
 * ֤�����
 * 
 * @author ����
 * @version 1.0
 */
public abstract class CertificateCoder {

	/**
	 * ����֤��X509
	 */
	public static final String CERT_TYPE = "X.509";

	/**
	 * ��KeyStore���˽Կ
	 * 
	 * @param keyStorePath
	 *            ��Կ��·��
	 * @param alias
	 *            ����
	 * @param password
	 *            ����
	 * @return PrivateKey ˽Կ
	 * @throws Exception
	 */
	private static PrivateKey getPrivateKeyByKeyStore(String keyStorePath,
			String alias, String password) throws Exception {

		// �����Կ��
		KeyStore ks = getKeyStore(keyStorePath, password);

		// ���˽Կ
		return (PrivateKey) ks.getKey(alias, password.toCharArray());

	}

	/**
	 * ��Certificate��ù�Կ
	 * 
	 * @param certificatePath
	 *            ֤��·��
	 * @return PublicKey ��Կ
	 * @throws Exception
	 */
	private static PublicKey getPublicKeyByCertificate(String certificatePath)
			throws Exception {

		// ���֤��
		Certificate certificate = getCertificate(certificatePath);

		// ��ù�Կ
		return certificate.getPublicKey();

	}

	/**
	 * ���Certificate
	 * 
	 * @param certificatePath
	 *            ֤��·��
	 * @return Certificate ֤��
	 * @throws Exception
	 */
	private static Certificate getCertificate(String certificatePath)
			throws Exception {

		// ʵ����֤�鹤��
		CertificateFactory certificateFactory = CertificateFactory
				.getInstance(CERT_TYPE);

		// ȡ��֤���ļ���
		FileInputStream in = new FileInputStream(certificatePath);

		// ����֤��
		Certificate certificate = certificateFactory.generateCertificate(in);

		// �ر�֤���ļ���
		in.close();

		return certificate;
	}

	/**
	 * ���Certificate
	 * 
	 * @param keyStorePath
	 *            ��Կ��·��
	 * @param alias
	 *            ����
	 * @param password
	 *            ����
	 * @return Certificate ֤��
	 * @throws Exception
	 */
	private static Certificate getCertificate(String keyStorePath,
			String alias, String password) throws Exception {

		// �����Կ��
		KeyStore ks = getKeyStore(keyStorePath, password);

		// ���֤��
		return ks.getCertificate(alias);
	}

	/**
	 * ���KeyStore
	 * 
	 * @param keyStorePath
	 *            ��Կ��·��
	 * @param password
	 *            ����
	 * @return KeyStore ��Կ��
	 * @throws Exception
	 */
	private static KeyStore getKeyStore(String keyStorePath, String password)
			throws Exception {

		// ʵ������Կ��
		KeyStore ks = KeyStore.getInstance(KeyStore.getDefaultType());

		// �����Կ���ļ���
		FileInputStream is = new FileInputStream(keyStorePath);

		// ������Կ��
		ks.load(is, password.toCharArray());

		// �ر���Կ���ļ���
		is.close();

		return ks;
	}

	/**
	 * ˽Կ����
	 * 
	 * @param data
	 *            ����������
	 * @param keyStorePath
	 *            ��Կ��·��
	 * @param alias
	 *            ����
	 * @param password
	 *            ����
	 * @return byte[] ��������
	 * @throws Exception
	 */
	public static byte[] encryptByPrivateKey(byte[] data, String keyStorePath,
			String alias, String password) throws Exception {

		// ȡ��˽Կ
		PrivateKey privateKey = getPrivateKeyByKeyStore(keyStorePath, alias,
				password);

		// �����ݼ���
		Cipher cipher = Cipher.getInstance(privateKey.getAlgorithm());
		cipher.init(Cipher.ENCRYPT_MODE, privateKey);

		return cipher.doFinal(data);

	}

	/**
	 * ˽Կ����
	 * 
	 * @param data
	 *            ����������
	 * @param keyStorePath
	 *            ��Կ��·��
	 * @param alias
	 *            ����
	 * @param password
	 *            ����
	 * @return byte[] ��������
	 * @throws Exception
	 */
	public static byte[] decryptByPrivateKey(byte[] data, String keyStorePath,
			String alias, String password) throws Exception {

		// ȡ��˽Կ
		PrivateKey privateKey = getPrivateKeyByKeyStore(keyStorePath, alias,
				password);

		// �����ݼ���
		Cipher cipher = Cipher.getInstance(privateKey.getAlgorithm());
		cipher.init(Cipher.DECRYPT_MODE, privateKey);

		return cipher.doFinal(data);

	}

	/**
	 * ��Կ����
	 * 
	 * @param data
	 *            ����������
	 * @param certificatePath
	 *            ֤��·��
	 * @return byte[] ��������
	 * @throws Exception
	 */
	public static byte[] encryptByPublicKey(byte[] data, String certificatePath)
			throws Exception {

		// ȡ�ù�Կ
		PublicKey publicKey = getPublicKeyByCertificate(certificatePath);

		// �����ݼ���
		Cipher cipher = Cipher.getInstance(publicKey.getAlgorithm());
		cipher.init(Cipher.ENCRYPT_MODE, publicKey);

		return cipher.doFinal(data);

	}

	/**
	 * ��Կ����
	 * 
	 * @param data
	 *            ����������
	 * @param certificatePath
	 *            ֤��·��
	 * @return byte[] ��������
	 * @throws Exception
	 */
	public static byte[] decryptByPublicKey(byte[] data, String certificatePath)
			throws Exception {

		// ȡ�ù�Կ
		PublicKey publicKey = getPublicKeyByCertificate(certificatePath);

		// �����ݼ���
		Cipher cipher = Cipher.getInstance(publicKey.getAlgorithm());
		cipher.init(Cipher.DECRYPT_MODE, publicKey);

		return cipher.doFinal(data);

	}

	/**
	 * ǩ��
	 * 
	 * @param keyStorePath
	 *            ��Կ��·��
	 * @param alias
	 *            ����
	 * @param password
	 *            ����
	 * @return byte[] ǩ��
	 * @throws Exception
	 */
	public static byte[] sign(byte[] sign, String keyStorePath, String alias,
			String password) throws Exception {

		// ���֤��
		X509Certificate x509Certificate = (X509Certificate) getCertificate(
				keyStorePath, alias, password);

		// ����ǩ������֤��ָ��ǩ���㷨
		Signature signature = Signature.getInstance(x509Certificate
				.getSigAlgName());

		// ��ȡ˽Կ
		PrivateKey privateKey = getPrivateKeyByKeyStore(keyStorePath, alias,
				password);

		// ��ʼ��ǩ������˽Կ����
		signature.initSign(privateKey);

		signature.update(sign);

		return signature.sign();
	}

	/**
	 * ��֤ǩ��
	 * 
	 * @param data
	 *            ����
	 * @param sign
	 *            ǩ��
	 * @param certificatePath
	 *            ֤��·��
	 * @return boolean ��֤ͨ��Ϊ��
	 * @throws Exception
	 */
	public static boolean verify(byte[] data, byte[] sign,
			String certificatePath) throws Exception {

		// ���֤��
		X509Certificate x509Certificate = (X509Certificate) getCertificate(certificatePath);

		// ��֤�鹹��ǩ��
		Signature signature = Signature.getInstance(x509Certificate
				.getSigAlgName());

		// ��֤���ʼ��ǩ����ʵ������ʹ����֤���еĹ�Կ
		signature.initVerify(x509Certificate);

		signature.update(data);

		return signature.verify(sign);

	}

}
