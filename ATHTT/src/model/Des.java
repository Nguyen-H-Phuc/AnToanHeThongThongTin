package model;

import javax.crypto.*;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import org.bouncycastle.jce.provider.BouncyCastleProvider;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.SecureRandom;
import java.security.Security;
import java.util.Base64;
import java.util.Properties;

public class Des {
	private SecretKey key;
	private String algorithm, mode, padding, charset, keySize, blockSize, encodedKey;

	public Des() {
		super();
		Security.addProvider(new BouncyCastleProvider());
	}
	
	public void genKey(String instance, int keySize) throws NoSuchAlgorithmException, NoSuchProviderException {
        try {
            // Thử với provider mặc định
            KeyGenerator generator = KeyGenerator.getInstance(instance);
            generator.init(keySize);
            key = generator.generateKey();
        } catch (NoSuchAlgorithmException e1) {
                // Nếu thất bại, thử với provider "BC"
                KeyGenerator generator = KeyGenerator.getInstance(instance, "BC");
                generator.init(keySize);
                key = generator.generateKey();           
        }
    }
	
	public void saveKey(String filePath, String algorithm, String mode, String padding, String charset, String keySize, String blockSize, String encodedKey) throws IOException {
	    try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
	        writer.write("algorithm=" + algorithm);
	        writer.newLine();
	        writer.write("mode=" + mode);
	        writer.newLine();
	        writer.write("padding=" + padding);
	        writer.newLine();
	        writer.write("charset=" + charset);
	        writer.newLine();
	        writer.write("keySize=" + keySize);
	        writer.newLine();
	        writer.write("blockSize=" + blockSize);
	        writer.newLine();
	        writer.write("key=" + encodedKey);
	    }
	}
	
	public void loadKey(String file) throws IOException {
	    Properties props = new Properties();
	    try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
	        props.load(reader);
	    }
	    algorithm = props.getProperty("algorithm");
	    mode = props.getProperty("mode");
	    padding = props.getProperty("padding");
	    charset = props.getProperty("charset");
	    keySize = props.getProperty("keySize");
	    blockSize = props.getProperty("blockSize");
	    encodedKey = props.getProperty("key");
	}
	
	// Generate an IV (Initialization Vector) if the mode requires it
	public IvParameterSpec generateIV(String mode, int blockSize) {
		if (requiresIV(mode)) {
			if (mode.equalsIgnoreCase("CCM")) {
				// CCM mode uses a fixed IV size of 12 bytes
				blockSize = 12;
			}
			byte[] ivBytes = new byte[blockSize];

			// Fill IV with random bytes
			new SecureRandom().nextBytes(ivBytes);
			return new IvParameterSpec(ivBytes);
		} else {
			// No IV needed for this mode
			return null;
		}
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

	// Encrypts a string and returns the encrypted bytes
	public byte[] encryptByte(String data, String algorithm, String mode, String padding, int blockSize, String charSet)
	        throws NoSuchAlgorithmException, NoSuchProviderException, NoSuchPaddingException, InvalidKeyException,
	               InvalidAlgorithmParameterException, IllegalBlockSizeException, BadPaddingException,
	               UnsupportedEncodingException {

	    String transformation = algorithm + "/" + mode + "/" + padding;
	    Cipher cipher = getCipher(transformation);

	    IvParameterSpec iv = generateIV(mode, blockSize);

	    // Initialize cipher with or without IV
	    if (iv != null)
	        cipher.init(Cipher.ENCRYPT_MODE, key, iv);
	    else
	        cipher.init(Cipher.ENCRYPT_MODE, key);

	    // Encrypt the data
	    byte[] encrypted = cipher.doFinal(data.getBytes(charSet));

	    // If IV is used, combine IV and ciphertext
	    if (iv != null) {
	        byte[] ivBytes = iv.getIV();
	        byte[] output = new byte[ivBytes.length + encrypted.length];

	        System.arraycopy(ivBytes, 0, output, 0, ivBytes.length);
	        System.arraycopy(encrypted, 0, output, ivBytes.length, encrypted.length);

	        return output;
	    }

	    return encrypted;
	}

	// Encrypts a string and returns the result as a Base64-encoded string
	public String encryptString(String data, String algorithm, String mode, String padding, int blockSize,
	        String charSet) throws InvalidKeyException, NoSuchAlgorithmException, NoSuchProviderException,
	        NoSuchPaddingException, InvalidAlgorithmParameterException, IllegalBlockSizeException, BadPaddingException,
	        UnsupportedEncodingException {

	    byte[] bytes = encryptByte(data, algorithm, mode, padding, blockSize, charSet);
	    String dataEncrypt = Base64.getEncoder().encodeToString(bytes);
	    return dataEncrypt;
	}

	// Decrypts byte array and returns the plaintext string
	private String decryptByte(byte[] data, String algorithm, String mode, String padding, int blockSize,
	        String charSet) throws NoSuchAlgorithmException, NoSuchProviderException, NoSuchPaddingException,
	        InvalidKeyException, InvalidAlgorithmParameterException, IllegalBlockSizeException, BadPaddingException,
	        UnsupportedEncodingException {

	    String transformation = algorithm + "/" + mode + "/" + padding;
	    Cipher cipher = getCipher(transformation); // Use fallback version if needed

	    IvParameterSpec iv = null;
	    byte[] actualEncrypted;

	    if (requiresIV(mode)) {
	        if (mode.equalsIgnoreCase("CCM")) {
	            blockSize = 12; // Adjust block size for CCM mode
	        }

	        // Extract IV from the start of the data
	        byte[] ivBytes = new byte[blockSize];
	        System.arraycopy(data, 0, ivBytes, 0, blockSize);
	        iv = new IvParameterSpec(ivBytes);

	        // Extract the actual encrypted data (excluding IV)
	        actualEncrypted = new byte[data.length - blockSize];
	        System.arraycopy(data, blockSize, actualEncrypted, 0, actualEncrypted.length);
	    } else {
	        actualEncrypted = data;
	    }

	    // Initialize cipher with or without IV
	    if (iv != null)
	        cipher.init(Cipher.DECRYPT_MODE, key, iv);
	    else
	        cipher.init(Cipher.DECRYPT_MODE, key);

	    // Perform decryption
	    byte[] decrypted = cipher.doFinal(actualEncrypted);
	    return new String(decrypted, charSet);
	}

	// Decrypts a Base64-encoded string and returns the plaintext string
	public String decryptString(String data, String algorithm, String mode, String padding, int blockSize,
	        String charSet) throws InvalidKeyException, NoSuchAlgorithmException, NoSuchProviderException,
	        NoSuchPaddingException, InvalidAlgorithmParameterException, IllegalBlockSizeException, BadPaddingException,
	        UnsupportedEncodingException {

	    return decryptByte(Base64.getDecoder().decode(data), algorithm, mode, padding, blockSize, charSet);
	}

	private boolean requiresIV(String mode) {
	    // Return true if the given mode requires an initialization vector (IV)
	    return mode.equalsIgnoreCase("CBC") ||
	           mode.equalsIgnoreCase("CFB") ||
	           mode.equalsIgnoreCase("OFB") ||
	           mode.equalsIgnoreCase("CTR") ||
	           mode.equalsIgnoreCase("GCM") ||
	           mode.equalsIgnoreCase("CCM") ||
	           mode.equalsIgnoreCase("PCBC");
	}
	
	// Encrypt file with given cipher mode and instance
	public void encryptFile(String srcFile, String destFile, String instance, String mode)
	        throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException,
	               InvalidAlgorithmParameterException, IOException, NoSuchProviderException {

	    Cipher cipher = getCipher(instance);
	    int blockSize = cipher.getBlockSize();
	    byte[] ivBytes = new byte[blockSize];
	    IvParameterSpec iv = null;

	    if (requiresIV(mode)) {
	        // Generate random IV if mode needs it
	        SecureRandom random = new SecureRandom();
	        random.nextBytes(ivBytes);
	        iv = new IvParameterSpec(ivBytes);
	        cipher.init(Cipher.ENCRYPT_MODE, key, iv);
	    } else {
	        cipher.init(Cipher.ENCRYPT_MODE, key);
	    }

	    try (BufferedInputStream bis = new BufferedInputStream(new FileInputStream(srcFile));
	         BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(destFile))) {

	        // Write IV at the start of the file if used
	        if (requiresIV(mode)) {
	            bos.write(ivBytes);
	        }

	        try (CipherOutputStream cos = new CipherOutputStream(bos, cipher)) {
	            byte[] buffer = new byte[1024];
	            int bytesRead;
	            while ((bytesRead = bis.read(buffer)) != -1) {
	                cos.write(buffer, 0, bytesRead);
	            }
	            cos.flush();
	        }
	    }
	}

	// Decrypt file with given cipher mode and instance
	public void decryptFile(String srcFile, String destFile, String instance, String mode)
	        throws NoSuchAlgorithmException, NoSuchPaddingException, IOException,
	               InvalidKeyException, InvalidAlgorithmParameterException, NoSuchProviderException {

	    Cipher cipher = getCipher(instance);
	    int blockSize = cipher.getBlockSize();
	    byte[] ivBytes = new byte[blockSize];
	    IvParameterSpec iv = null;

	    try (BufferedInputStream bis = new BufferedInputStream(new FileInputStream(srcFile));
	         BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(destFile))) {

	        if (requiresIV(mode)) {
	            // Read IV from start of the file
	            if (bis.read(ivBytes) != blockSize) {
	                throw new IOException("Could not read full IV from file");
	            }
	            iv = new IvParameterSpec(ivBytes);
	            cipher.init(Cipher.DECRYPT_MODE, key, iv);
	        } else {
	            cipher.init(Cipher.DECRYPT_MODE, key);
	        }

	        // Use CipherInputStream after reading IV
	        try (CipherInputStream cis = new CipherInputStream(bis, cipher)) {
	            byte[] buffer = new byte[1024];
	            int bytesRead;
	            while ((bytesRead = cis.read(buffer)) != -1) {
	                bos.write(buffer, 0, bytesRead);
	            }
	        }
	        bos.flush();
	    }
	}

	public String getKey() {
		if(key!=null) {
		return Base64.getEncoder().encodeToString(key.getEncoded());
		}return "";
	}
	
	public void setKey(String encodedKey, String algorithm) {
		byte[] decodedKey = Base64.getDecoder().decode(encodedKey);
		key = new SecretKeySpec(decodedKey, algorithm);
	}
	
	public void clearKey() {
		this.key = null;
	}
	
	public String getAlgorithm() {
		return algorithm;
	}

	public String getMode() {
		return mode;
	}

	public String getPadding() {
		return padding;
	}

	public String getCharset() {
		return charset;
	}

	public String getKeySize() {
		return keySize;
	}

	public String getBlockSize() {
		return blockSize;
	}
	
	public String getEncodedKey() {
		return encodedKey;
	}

	public void clearLoadKey() {
		this.algorithm = null;
		 this.mode = null; this.padding=null; this.charset=null;
		this.keySize=null; this.blockSize=null; this.encodedKey=null;
	}

}
