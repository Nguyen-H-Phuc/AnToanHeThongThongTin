package model;

import java.io.DataInputStream;
import java.io.DataOutputStream;
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
import java.security.NoSuchProviderException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Security;
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

import org.bouncycastle.jce.provider.BouncyCastleProvider;


public class RSA {
	private PublicKey publicKey;
	private PrivateKey privateKey;
	
	public RSA() {
		super();
		Security.addProvider(new BouncyCastleProvider());
	}
	
	// Method to generate a public-private key pair using a specified algorithm and key size
	public void genKey(String instance, int keySize) throws NoSuchAlgorithmException, NoSuchProviderException {
	    KeyPairGenerator kpg;
	    try {
	        kpg = KeyPairGenerator.getInstance(instance);
	    } catch (NoSuchAlgorithmException e) {
	        kpg = KeyPairGenerator.getInstance(instance, "BC");
	    }
	    kpg.initialize(keySize);
	    KeyPair kp = kpg.genKeyPair();
	    publicKey = kp.getPublic();
	    privateKey = kp.getPrivate();
	}



	
	// Method to save the public key to a file
	public String savePublicKey(String publicKey, String filePath) throws IOException {
	    FileWriter writer = new FileWriter(filePath);
	    
	    // Write the public key in the PEM format
	    writer.write("-----BEGIN PUBLIC KEY-----\n");
	    writer.write(publicKey);
	    writer.write("\n-----END PUBLIC KEY-----\n");
	    
	    writer.close();
	    
	    return "Lưu khoá công khai thành công " + "\n" + "Khoá công khai được lưu tại: " + filePath;
	}

	// Method to save the private key to a file
	public String savePrivateKey(String privateKey, String filePath) throws IOException {
	    FileWriter writer = new FileWriter(filePath);
	    
	    // Write the private key in the PEM format
	    writer.write("-----BEGIN PRIVATE KEY-----\n");
	    writer.write(privateKey);
	    writer.write("\n-----END PRIVATE KEY-----\n");
	    
	    writer.close();
	    
	    return "Lưu khoá riêng tư thành công " + "\n" + "Khoá riêng tư được lưu tại: " + filePath;
	}

	// Method to load the public key from a file and remove unnecessary parts
	public void loadPublicKey(String filePath) throws IOException, NoSuchAlgorithmException, InvalidKeySpecException {
	    String content = Files.readString(Paths.get(filePath));
	    
	    // Clean up the key content by removing the PEM header, footer, and whitespaces
	    String keyBase64 = content
	        .replace("-----BEGIN PUBLIC KEY-----", "")
	        .replace("-----END PUBLIC KEY-----", "")
	        .replaceAll("\\s", "");
	    
	    // Set the public key using the cleaned base64 string
	    setPublicKey(keyBase64);
	}

	// Method to load the private key from a file and remove unnecessary parts
	public void loadPrivateKey(String filePath) throws IOException, NoSuchAlgorithmException, InvalidKeySpecException {
	    String content = Files.readString(Paths.get(filePath));
	    
	    // Clean up the key content by removing the PEM header, footer, and whitespaces
	    String keyBase64 = content
	        .replace("-----BEGIN PRIVATE KEY-----", "")
	        .replace("-----END PRIVATE KEY-----", "")
	        .replaceAll("\\s", "");
	    
	    // Set the private key using the cleaned base64 string
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
	
	public byte[] encryptByte(String data, String instance) 
	        throws  UnsupportedEncodingException, NoSuchAlgorithmException, NoSuchProviderException, NoSuchPaddingException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException {
	    Cipher cipher = getCipher(instance);
	    cipher.init(Cipher.ENCRYPT_MODE, publicKey);
	    return cipher.doFinal(data.getBytes("UTF-8"));
	}



	public String encryptString(String data, String instance) throws InvalidKeyException, NoSuchAlgorithmException,
    NoSuchPaddingException, IllegalBlockSizeException, BadPaddingException, UnsupportedEncodingException, NoSuchProviderException {
		return Base64.getEncoder().encodeToString(encryptByte(data, instance));
}

	// Method to decrypt byte array data using a specified encryption instance
	public String decryptByte(byte[] encryptedData, String instance)
			throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, IllegalBlockSizeException,
			BadPaddingException, UnsupportedEncodingException, NoSuchProviderException {
		Cipher cipher = getCipher(instance);
		cipher.init(Cipher.DECRYPT_MODE, privateKey);
		byte[] decryptedBytes = cipher.doFinal(encryptedData);
		// Convert decrypted bytes
		return new String(decryptedBytes, "UTF-8");
	}

	// Method to decrypt a Base64-encoded encrypted string
	public String decryptString(String encryptedBase64, String instance) throws NoSuchAlgorithmException, NoSuchPaddingException,
	        InvalidKeyException, IllegalBlockSizeException, BadPaddingException, UnsupportedEncodingException, IllegalArgumentException, NoSuchProviderException {
	    // Decode Base64 string to get encrypted bytes
	    byte[] encryptedBytes = Base64.getDecoder().decode(encryptedBase64);
	    return decryptByte(encryptedBytes, instance);
	}


	public String encryptSercetKey(SecretKey secretKey, String instance) throws NoSuchAlgorithmException,
			NoSuchPaddingException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException, NoSuchProviderException {
		Cipher cipher = getCipher(instance);
		cipher.init(Cipher.ENCRYPT_MODE, publicKey);
		// Get raw bytes of the secret key
		byte[] keyBytes = secretKey.getEncoded();
		// Encrypt the secret key bytes
		byte[] encryptedKey = cipher.doFinal(keyBytes);
		// Encode the encrypted key as a Base64 string
		String encryptKey = Base64.getEncoder().encodeToString(encryptedKey);
		return encryptKey;
	}

	
	public void encryptFile(String srcFile, String destFile, String instance) throws Exception {
	    FileOutputStream fos = new FileOutputStream(destFile);
	    DataOutputStream dos = new DataOutputStream(fos);
	    
	    // Generate a secret AES key and encrypt it using the given instance
	    SecretKey secretKey = genSecretKey("AES", 128);
	    String encryptedKey = encryptSercetKey(secretKey, instance);
	    byte[] encryptedKeyBytes = encryptedKey.getBytes(StandardCharsets.UTF_8);
	    
	    // Write the length and content of the encrypted secret key to the output file
	    dos.writeInt(encryptedKeyBytes.length); 
	    dos.write(encryptedKeyBytes); 
	    
	    // Set up AES cipher for encryption
	    Cipher cipher = Cipher.getInstance("AES");
	    cipher.init(Cipher.ENCRYPT_MODE, secretKey);
	    CipherOutputStream cos = new CipherOutputStream(fos, cipher); 
	    
	    // Read the source file and write encrypted data to the destination file
	    FileInputStream fis = new FileInputStream(srcFile);
	    byte[] buffer = new byte[1024];
	    int bytesRead;
	    while ((bytesRead = fis.read(buffer)) != -1) {
	        cos.write(buffer, 0, bytesRead);
	    }

	    fis.close();
	    cos.close();
	}


	public void decryptFile(String srcFile, String destFile, String instance) throws Exception {
	    FileInputStream fis = new FileInputStream(srcFile);
	    DataInputStream dis = new DataInputStream(fis);

	    // Read the length of the encrypted key and the encrypted key bytes
	    int keyLength = dis.readInt();
	    byte[] encryptedKeyBytes = new byte[keyLength];
	    dis.readFully(encryptedKeyBytes);
	    String encryptedKey = new String(encryptedKeyBytes, StandardCharsets.UTF_8);

	    // Decrypt the secret key using the given instance
	    SecretKey originalKey = decryptSercetKey(encryptedKey, instance);

	    // Set up AES cipher for decryption
	    Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
	    cipher.init(Cipher.DECRYPT_MODE, originalKey);

	    // Create CipherInputStream to read and decrypt the file content
	    CipherInputStream cis = new CipherInputStream(fis, cipher);
	    FileOutputStream fos = new FileOutputStream(destFile);
	    byte[] buffer = new byte[1024];
	    int bytesRead;
	    
	    // Write decrypted data to the destination file
	    while ((bytesRead = cis.read(buffer)) != -1) {
	        fos.write(buffer, 0, bytesRead);
	    }

	    cis.close();
	    fos.close();
	}


	private SecretKey decryptSercetKey(String encryptedKey, String instance) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException, NoSuchProviderException {
	    Cipher cipher = getCipher(instance);
	    cipher.init(Cipher.DECRYPT_MODE, privateKey);   
	    // Decode the Base64-encoded encrypted key string to get the raw encrypted bytes
	    byte[] encryptedKeyBytes = Base64.getDecoder().decode(encryptedKey);
	    // Decrypt the key bytes
	    byte[] decryptedKeyBytes = cipher.doFinal(encryptedKeyBytes);
	    // Create a SecretKey object from the decrypted bytes using AES
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
	
	private Cipher getCipher(String transformation)
			throws NoSuchAlgorithmException, NoSuchProviderException, NoSuchPaddingException {
		try {
			// Thử với provider mặc định
			return Cipher.getInstance(transformation);
		} catch (NoSuchAlgorithmException | NoSuchPaddingException e) {
			// Fallback sang Bouncy Castle
			return Cipher.getInstance(transformation, "BC");

		}
	}

}
