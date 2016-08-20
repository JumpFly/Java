package com.Secret;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.security.GeneralSecurityException;
import java.security.Key; 

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.codec.binary.Base64;

public class AESCoder {
	/*
	 * ��Կ�㷨
	 */
	private static final String KEY_ALGORITHM="AES";
	/*
	 * �ӽ����㷨/����ģʽ/��䷽ʽ
	 */
	private static final String CIPHER_ALGORITHM="AES/ECB/PKCS5Padding";
	
	/*
	 * �����Կ
	 * key Base64��Կ
	 * byte[] ��Կ
	 */
	private static byte[] getKey(String key)throws Exception{
		return Base64.decodeBase64(key);
	}
	
	/*
	 * ת����Կ
	 * key ��������Կ
	 * Key ��Կ
	 */
	private static Key toKey(byte[] key)throws Exception{
		//ʵ������Կ����
		SecretKey secretKey=new SecretKeySpec(key, KEY_ALGORITHM);
		return secretKey;
	}
	/* ����
	 * data����������
	 * key ��������Կ
	 * byte[] ��������
	 */
	public static byte[] decrypt(byte[] data,byte[] key)throws Exception{
		//��ԭ��Կ
		Key K=toKey(key);
		Cipher cipher=Cipher.getInstance(CIPHER_ALGORITHM);
		cipher.init(Cipher.DECRYPT_MODE, K);
		
			
		return cipher.doFinal(data);
	}
	public static byte[] decrypt(byte[] data,String key)throws Exception{
		
		return decrypt(data, getKey(key));
	}
	
	/* ����
	 * data����������
	 * key ��������Կ
	 * byte[] ��������
	 */
	public static byte[] encrypt(byte[] data,byte[] key)throws Exception{
		//��ԭ��Կ
		Key K=toKey(key);
		Cipher cipher=Cipher.getInstance(CIPHER_ALGORITHM);
		cipher.init(Cipher.ENCRYPT_MODE, K);
		return cipher.doFinal(data);
	}
	public static byte[] encrypt(byte[] data,String key)throws Exception{
		return encrypt(data, getKey(key));
	}
	
	public static void encryptFile(String FilePath,String EnFilePath,byte[] key)throws Exception{
		
		FileInputStream fileIn= new FileInputStream(FilePath);
		FileOutputStream fileOut=new FileOutputStream(EnFilePath);
		Key K=toKey(key);
		Cipher cipher=Cipher.getInstance(CIPHER_ALGORITHM);
		cipher.init(Cipher.ENCRYPT_MODE, K);
		crypt(fileIn, fileOut, cipher);
		
	}
	public static void encryptFile(String FilePath,String EnFilePath,String key)throws Exception{
		encryptFile(FilePath, EnFilePath, getKey(key));
	}
	public static void decryptFile(String EnFilePath,String DeFilePath,byte[] key)throws Exception{
		
		FileInputStream fileIn= new FileInputStream(EnFilePath);
		FileOutputStream fileOut=new FileOutputStream(DeFilePath);
		Key K=toKey(key);
		Cipher cipher=Cipher.getInstance(CIPHER_ALGORITHM);
		cipher.init(Cipher.DECRYPT_MODE, K);
		crypt(fileIn, fileOut, cipher);
		
	}
	public static void decryptFile(String EnFilePath,String DeFilePath,String key)throws Exception{
		decryptFile(EnFilePath, DeFilePath, getKey(key));
	}
	/* ������Կ
	 * byte[] ��������Կ
	 */
	public static byte[] initKey()throws Exception{
		KeyGenerator kg=KeyGenerator.getInstance(KEY_ALGORITHM);
		kg.init(256);
		//����������Կ
		SecretKey secretKey=kg.generateKey();
		return secretKey.getEncoded();
	}
	public static String initKeyString()throws Exception{
		return Base64.encodeBase64String(initKey());
	}
	
	/*
	 * �ļ����ܲ����ɼ����ļ�
	 */
	public static void crypt(FileInputStream in,FileOutputStream out,Cipher cipher)throws IOException,GeneralSecurityException{
		FileChannel fcIn=null;
		MappedByteBuffer mbbfIn=null;
		int blockSize=cipher.getBlockSize();
		int outputSize=cipher.getOutputSize(blockSize);
		byte[] inBytes=new byte[blockSize];
		byte[] outBytes=new byte[outputSize];
		int len=0,outLenth;
		fcIn=in.getChannel();
		mbbfIn=fcIn.map(FileChannel.MapMode.READ_ONLY, 0, fcIn.size());
		boolean more=true;
			while(more){
				len=mbbfIn.limit()-mbbfIn.position();
				if(len>blockSize)
				{	mbbfIn.get(inBytes, 0, blockSize);
					outLenth=cipher.update(inBytes, 0, blockSize, outBytes);
				  out.write(outBytes,0,outLenth);
				}else{
					more=false;
				}
			} 
			if(len>0){mbbfIn.get(inBytes, 0, len);
					  outBytes=cipher.doFinal(inBytes,0,len);
			     }else outBytes=cipher.doFinal();
			out.write(outBytes);
		if(fcIn!=null)
			fcIn.close();
	}
	
	public static void main(String[] args)throws Exception{
		String inputStr="AES";
		byte[] inputData=inputStr.getBytes();
		System.out.println("ԭ��:\t"+inputStr);
		//��ʼ����Կ
	//	byte[] key=AESCoder.initKey();
	//	System.out.println("��Կ:\t"+Base64.encodeBase64String(key));
		
		String key=AESCoder.initKeyString();
		System.out.println("��Կ:\t"+key);
		//����
		inputData=AESCoder.encrypt(inputData, key);
		System.out.println("���ܺ�:\t"+Base64.encodeBase64String(inputData));
		//����
		byte[] outputData=AESCoder.decrypt(inputData, key);
		String outputStr=new String(outputData);
		System.out.println("���ܺ�:\t"+outputStr);
		//У��
		System.out.println(inputStr.equals(outputStr));
		
		System.out.println("�ļ�����");
		byte[] kkey=AESCoder.getKey(key);
		
		AESCoder.encryptFile("D:\\FileBox\\JAVA3D.zip", "D:\\FileBox\\EnJAVA3D.zip", kkey);
		System.out.println("ok encrypt");
		AESCoder.decryptFile("D:\\FileBox\\EnJAVA3D.zip", "D:\\FileBox\\DeJAVA3D.zip", kkey);
		System.out.println("ok decrypt");
	}

}
