package model;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.InvalidKeyException;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.CipherInputStream;
import javax.crypto.CipherOutputStream;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;


public class RSA {
	private PublicKey publicKey;
	private PrivateKey privateKey;
	
	public void genKey(String instance, int keySize) throws NoSuchAlgorithmException {
			KeyPairGenerator kpg = KeyPairGenerator.getInstance(instance);
			kpg.initialize(keySize);
			KeyPair kp = kpg.genKeyPair();
			publicKey = kp.getPublic();
			privateKey = kp.getPrivate();
	}
	
	public String savePublicKey(String publicKey, String filePath) throws IOException {
		FileWriter writer = new FileWriter(filePath);
		writer.write("-----BEGIN PUBLIC KEY-----\n");
		writer.write(publicKey);
		writer.write("\n-----END PUBLIC KEY-----\n");
		writer.close();
		return "Lưu khoá công khai thành công " + "\n" + "Khoá công khai được lưu tại: " + filePath;
	}
	
	public String savePrivateKey(String privateKey, String filePath) throws IOException {
		FileWriter writer = new FileWriter(filePath);
		writer.write("-----BEGIN PRIVATE KEY-----\n");
		writer.write(privateKey);
		writer.write("\n-----END PRIVATE KEY-----\n");
		writer.close();
		return "Lưu khoá riêng tư thành công " + "\n" + "Khoá riêng tư được lưu tại: " + filePath;
	}
	
	public void loadPublicKey(String filePath) throws IOException, NoSuchAlgorithmException, InvalidKeySpecException {
	    String content = Files.readString(Paths.get(filePath));
	    String keyBase64 = content
	        .replace("-----BEGIN PUBLIC KEY-----", "")
	        .replace("-----END PUBLIC KEY-----", "")
	        .replaceAll("\\s", "");

	    setPublicKey(keyBase64);
	}
	
	public void loadPrivateKey(String filePath) throws IOException, NoSuchAlgorithmException, InvalidKeySpecException  {
	    String content = Files.readString(Paths.get(filePath));
	    String keyBase64 = content
	        .replace("-----BEGIN PRIVATE KEY-----", "")
	        .replace("-----END PRIVATE KEY-----", "")
	        .replaceAll("\\s", "");

	    setPrivateKey(keyBase64);
	}

	public SecretKey genSecretKey(String instance, int keySize) {
		try {
			KeyGenerator generator = KeyGenerator.getInstance(instance);
			generator.init(keySize);
			return generator.generateKey();
		} catch (NoSuchAlgorithmException e) {
			throw new RuntimeException(e);
		}
	}

	public byte[] encryptByte(String data, String instance) throws NoSuchAlgorithmException, NoSuchPaddingException,
			InvalidKeyException, IllegalBlockSizeException, BadPaddingException, UnsupportedEncodingException {
		Cipher cipher = Cipher.getInstance(instance);
		cipher.init(Cipher.ENCRYPT_MODE, publicKey);
		return cipher.doFinal(data.getBytes("UTF-8"));
	}

	public String encryptString(String data, String instance) throws InvalidKeyException, NoSuchAlgorithmException,
			NoSuchPaddingException, IllegalBlockSizeException, BadPaddingException, UnsupportedEncodingException {
		return Base64.getEncoder().encodeToString(encryptByte(data, instance));
	}

	public String decryptByte(byte[] encryptedData, String instance) throws NoSuchAlgorithmException, NoSuchPaddingException,
			InvalidKeyException, IllegalBlockSizeException, BadPaddingException, UnsupportedEncodingException {
		Cipher cipher = Cipher.getInstance(instance);
		cipher.init(Cipher.DECRYPT_MODE, privateKey);
		byte[] decryptedBytes = cipher.doFinal(encryptedData);
		return new String(decryptedBytes, "UTF-8");
	}

	public String decryptString(String encryptedBase64, String instance) throws NoSuchAlgorithmException, NoSuchPaddingException,
			InvalidKeyException, IllegalBlockSizeException, BadPaddingException, UnsupportedEncodingException, IllegalArgumentException {
		byte[] encryptedBytes = Base64.getDecoder().decode(encryptedBase64);
		return decryptByte(encryptedBytes, instance);
	}

	public String encryptSercetKey(SecretKey secretKey, String instance) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException,
			IllegalBlockSizeException, BadPaddingException {
		Cipher cipher = Cipher.getInstance(instance);
		cipher.init(Cipher.ENCRYPT_MODE, publicKey);
		byte[] keyBytes = secretKey.getEncoded();
		byte[] encryptedKey = cipher.doFinal(keyBytes);
		String encryptKey = Base64.getEncoder().encodeToString(encryptedKey);
		return encryptKey;
	}
	
	public void encryptFile(String srcFile, String destFile, String instance) throws Exception {
	    FileOutputStream fos = new FileOutputStream(destFile);
	    DataOutputStream dos = new DataOutputStream(fos);
	    
	    // Ghi key
	    SecretKey secretKey = genSecretKey("AES", 128);
	    String encryptedKey = encryptSercetKey(secretKey, instance);
	    byte[] encryptedKeyBytes = encryptedKey.getBytes(StandardCharsets.UTF_8);
	    dos.writeInt(encryptedKeyBytes.length); // ghi độ dài key
	    dos.write(encryptedKeyBytes); // ghi key
	    
	    // Ghi dữ liệu file đã mã hóa AES
	    Cipher cipher = Cipher.getInstance("AES");
	    cipher.init(Cipher.ENCRYPT_MODE, secretKey);
	    CipherOutputStream cos = new CipherOutputStream(fos, cipher); // dùng lại fos, không dùng dos nữa
	    
	    FileInputStream fis = new FileInputStream(srcFile);
	    byte[] buffer = new byte[1024];
	    int bytesRead;
	    while ((bytesRead = fis.read(buffer)) != -1) {
	        cos.write(buffer, 0, bytesRead);
	    }

	    fis.close();
	    cos.close(); // tự đóng fos
	}

	public void decryptFile(String srcFile, String destFile, String instance) throws Exception {
	    FileInputStream fis = new FileInputStream(srcFile);
	    DataInputStream dis = new DataInputStream(fis);

	    // Đọc độ dài và key
	    int keyLength = dis.readInt();
	    byte[] encryptedKeyBytes = new byte[keyLength];
	    dis.readFully(encryptedKeyBytes);
	    String encryptedKey = new String(encryptedKeyBytes, StandardCharsets.UTF_8);

	    SecretKey originalKey = decryptSercetKey(encryptedKey, instance);
	    Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
	    cipher.init(Cipher.DECRYPT_MODE, originalKey);

	    CipherInputStream cis = new CipherInputStream(fis, cipher);
	    FileOutputStream fos = new FileOutputStream(destFile);
	    byte[] buffer = new byte[1024];
	    int bytesRead;
	    while ((bytesRead = cis.read(buffer)) != -1) {
	        fos.write(buffer, 0, bytesRead);
	    }

	    cis.close();
	    fos.close();
	}

	private SecretKey decryptSercetKey(String encryptedKey, String instance) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException {
		Cipher cipher = Cipher.getInstance(instance);
		cipher.init(Cipher.DECRYPT_MODE, privateKey);
		byte[] encryptedKeyBytes = Base64.getDecoder().decode(encryptedKey);
		byte[] decryptedKeyBytes = cipher.doFinal(encryptedKeyBytes);
		SecretKey originalKey = new SecretKeySpec(decryptedKeyBytes, "AES");
		return originalKey;
	}

	public String getPublicKey() {
	    return Base64.getEncoder().encodeToString(publicKey.getEncoded());
	}
	
	public String getPrivateKey() {
	    return Base64.getEncoder().encodeToString(privateKey.getEncoded());
	}

	public void setPublicKey(String base64PublicKey) throws NoSuchAlgorithmException, InvalidKeySpecException, IllegalArgumentException {
	        byte[] decodedKey = Base64.getDecoder().decode(base64PublicKey);
	        X509EncodedKeySpec keySpec = new X509EncodedKeySpec(decodedKey);
	        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
	        this.publicKey = keyFactory.generatePublic(keySpec);
	   
	}

	public void setPrivateKey(String base64PrivateKey) throws NoSuchAlgorithmException, InvalidKeySpecException, IllegalArgumentException {
	        byte[] decodedKey = Base64.getDecoder().decode(base64PrivateKey);
	        PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(decodedKey);
	        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
	        this.privateKey = keyFactory.generatePrivate(keySpec);
	}

	public void clearPrivateKey() {
	    this.privateKey = null;
	}
	
	public void clearPublicKey() {
		this.publicKey = null;
	}

}
