package model;

import javax.crypto.*;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

public class Des {
	private SecretKey key;

	private void genKey(String instance, int keySize) {
		try {
			KeyGenerator generator = KeyGenerator.getInstance(instance);
			generator.init(keySize);
			key = generator.generateKey();
		} catch (NoSuchAlgorithmException e) {
			throw new RuntimeException(e);
		}
	}

	private byte[] encryptByte(String data) throws NoSuchPaddingException, NoSuchAlgorithmException,
			InvalidKeyException, IllegalBlockSizeException, BadPaddingException, UnsupportedEncodingException {
		Cipher cipher = Cipher.getInstance("DES");
		cipher.init(Cipher.ENCRYPT_MODE, key);
		byte[] bytes = cipher.doFinal(data.getBytes("UTF-8"));
		return bytes;
	}

	public String encryptString(String data) throws NoSuchPaddingException, NoSuchAlgorithmException,
			InvalidKeyException, IllegalBlockSizeException, BadPaddingException, UnsupportedEncodingException {
		byte[] bytes = encryptByte(data);
		String dataEncypt = Base64.getEncoder().encodeToString(bytes);
		return dataEncypt;
	}

	private String decryptByte(byte[] data) throws NoSuchPaddingException, NoSuchAlgorithmException,
			InvalidKeyException, IllegalBlockSizeException, BadPaddingException, UnsupportedEncodingException {
		Cipher cipher = Cipher.getInstance("DES");
		cipher.init(Cipher.DECRYPT_MODE, key);
		byte[] bytes = cipher.doFinal(data);
		return new String(bytes, "UTF-8");
	}

	public String decryptString(String data) throws NoSuchPaddingException, NoSuchAlgorithmException,
			InvalidKeyException, IllegalBlockSizeException, BadPaddingException, UnsupportedEncodingException {
		return decryptByte(Base64.getDecoder().decode(data));
	}

	private byte[] encryptFile(String src, String dest)
			throws NoSuchAlgorithmException, NoSuchPaddingException, FileNotFoundException {
		Cipher cipher = Cipher.getInstance("DES");
		CipherInputStream cis = new CipherInputStream(new FileInputStream(src), cipher);
		return null;
	}

	public static void main(String[] args) throws NoSuchPaddingException, IllegalBlockSizeException,
			NoSuchAlgorithmException, BadPaddingException, InvalidKeyException, UnsupportedEncodingException {
		String data = "Bị dẫn 0-3, nhưng CLB TP HCM vẫn thắng ngược đại diện UAE Abu Dhabi Country 5-4 ở tứ kết AFC Champions League nữ 2024 trên sân Thống Nhất tối 22/3.";
		Des des = new Des();
		des.genKey("a",1);
		String dataEncrypt = des.encryptString(data);
// System.out.println(dataEncrypt);
		String dataDecrypt = des.decryptString(dataEncrypt);
		System.out.println(dataDecrypt);
	}
}
