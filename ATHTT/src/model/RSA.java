package model;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.Base64;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.CipherOutputStream;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;

public class RSA {
	private PublicKey publicKey;
	private PrivateKey privateKey;
	private SecretKey secretKey;

	public void genKey() {
		try {
			KeyPairGenerator kpg = KeyPairGenerator.getInstance("RSA");
			kpg.initialize(1024);
			KeyPair kp = kpg.genKeyPair();

			publicKey = kp.getPublic();
			privateKey = kp.getPrivate();
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
	}

	public void genSecretKey(String instance, int keySize) {
		try {
			KeyGenerator generator = KeyGenerator.getInstance(instance);
			generator.init(keySize);
			secretKey = generator.generateKey();
		} catch (NoSuchAlgorithmException e) {
			throw new RuntimeException(e);
		}
	}

	public byte[] encrypt(String data) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException,
			IllegalBlockSizeException, BadPaddingException, UnsupportedEncodingException {
		Cipher cipher = Cipher.getInstance("RSA");
		cipher.init(Cipher.ENCRYPT_MODE, publicKey);
		return cipher.doFinal(data.getBytes("UTF-8"));
	}

	public String encryptString(String data) throws InvalidKeyException, NoSuchAlgorithmException,
			NoSuchPaddingException, IllegalBlockSizeException, BadPaddingException, UnsupportedEncodingException {
		return Base64.getEncoder().encodeToString(encrypt(data));
	}

	public String decrypt(byte[] encryptedData) throws NoSuchAlgorithmException, NoSuchPaddingException,
			InvalidKeyException, IllegalBlockSizeException, BadPaddingException, UnsupportedEncodingException {
		Cipher cipher = Cipher.getInstance("RSA");
		cipher.init(Cipher.DECRYPT_MODE, privateKey);
		byte[] decryptedBytes = cipher.doFinal(encryptedData);
		return new String(decryptedBytes, "UTF-8");
	}

	public String decryptString(String encryptedBase64) throws NoSuchAlgorithmException, NoSuchPaddingException,
			InvalidKeyException, IllegalBlockSizeException, BadPaddingException, UnsupportedEncodingException {
		byte[] encryptedBytes = Base64.getDecoder().decode(encryptedBase64);
		return decrypt(encryptedBytes);
	}

	public String encryptSercetKey() throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException,
			IllegalBlockSizeException, BadPaddingException {
		Cipher cipher = Cipher.getInstance("RSA");
		cipher.init(Cipher.ENCRYPT_MODE, publicKey);
		byte[] keyBytes = secretKey.getEncoded();
		byte[] encryptedKey = cipher.doFinal(keyBytes);
		String encryptKey = Base64.getEncoder().encodeToString(encryptedKey);
		return encryptKey;
	}
	
	public void encryptFile(String srcFile, String destFile) throws InvalidKeyException, NoSuchAlgorithmException, NoSuchPaddingException, IllegalBlockSizeException, BadPaddingException, IOException {
		DataOutputStream dos1 = new DataOutputStream(new FileOutputStream(destFile));
		dos1.writeUTF(encryptSercetKey());
		dos1.flush();
		
		DataInputStream dis = new DataInputStream(new FileInputStream(srcFile));
		Cipher c = Cipher.getInstance("AES");
		c.init(Cipher.ENCRYPT_MODE, secretKey);
		
		CipherOutputStream cos = new CipherOutputStream(dos1, c);
		DataOutputStream dos2 = new DataOutputStream(cos);
		byte [] buffer = new byte[1024];
		int bytesRead;
		while((bytesRead = dis.read(buffer))!=-1) {
			dos2.write(buffer, 0, bytesRead);
		}
		dis.close();		
		dos2.close();
		cos.close();
		dos1.close();
		
	}

}
