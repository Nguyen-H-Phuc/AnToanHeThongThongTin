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
import java.io.FileNotFoundException;
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
	
	public void saveKey(String filePath, String algorithm, String mode, String padding, String charset, String keySize, String blockSize) throws IOException {
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
	        if(key!=null) {
	        // Key là dạng bytes ➔ encode base64 trước
	        String encodedKey = Base64.getEncoder().encodeToString(key.getEncoded());
	        writer.write("key=" + encodedKey);
	        }
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
	    
//	    byte[] decodedKey = Base64.getDecoder().decode(encodedKey);
//	    key = new SecretKeySpec(decodedKey, algorithm);
	}
	
	public IvParameterSpec generateIV(String mode, int blockSize) {
		if (requiresIV(mode)) {
			if (mode.equalsIgnoreCase("CCM")) {
				blockSize = 12;
			}
			byte[] ivBytes = new byte[blockSize];

			new SecureRandom().nextBytes(ivBytes);
			return new IvParameterSpec(ivBytes);
		} else
			return null;
	}
	
	private Cipher getCipher(String transformation) throws NoSuchAlgorithmException, NoSuchProviderException, NoSuchPaddingException  {
		try {
			// Thử với provider mặc định
			return Cipher.getInstance(transformation);
		} catch (NoSuchAlgorithmException | NoSuchPaddingException e) {			
				// Fallback sang Bouncy Castle nếu cần
				return Cipher.getInstance(transformation, "BC");
			
		}
	}

	public byte[] encryptByte(String data, String algorithm, String mode, String padding, int blockSize, String charSet)
			throws NoSuchAlgorithmException, NoSuchProviderException, NoSuchPaddingException, InvalidKeyException,
			InvalidAlgorithmParameterException, IllegalBlockSizeException, BadPaddingException,
			UnsupportedEncodingException {

		String transformation = algorithm + "/" + mode + "/" + padding;
		Cipher cipher = getCipher(transformation);

		IvParameterSpec iv = generateIV(mode, blockSize);

		if (iv != null)
			cipher.init(Cipher.ENCRYPT_MODE, key, iv);
		else
			cipher.init(Cipher.ENCRYPT_MODE, key);

		byte[] encrypted = cipher.doFinal(data.getBytes(charSet));

		// Gộp IV + ciphertext nếu cần
		if (iv != null) {
			byte[] ivBytes = iv.getIV();
			byte[] output = new byte[ivBytes.length + encrypted.length];

			System.arraycopy(ivBytes, 0, output, 0, ivBytes.length);
			System.arraycopy(encrypted, 0, output, ivBytes.length, encrypted.length);

			return output;
		}

		return encrypted;
	}

	public String encryptString(String data, String alogrithm, String mode, String padding, int blockSize,
			String charSet) throws InvalidKeyException, NoSuchAlgorithmException, NoSuchProviderException,
			NoSuchPaddingException, InvalidAlgorithmParameterException, IllegalBlockSizeException, BadPaddingException,
			UnsupportedEncodingException {
		byte[] bytes = encryptByte(data, alogrithm, mode, padding, blockSize, charSet);
		String dataEncypt = Base64.getEncoder().encodeToString(bytes);
		return dataEncypt;
	}

	private String decryptByte(byte[] data, String algorithm, String mode, String padding, int blockSize,
			String charSet) throws NoSuchAlgorithmException, NoSuchProviderException, NoSuchPaddingException,
			InvalidKeyException, InvalidAlgorithmParameterException, IllegalBlockSizeException, BadPaddingException,
			UnsupportedEncodingException {
		String transformation = algorithm + "/" + mode + "/" + padding;
		Cipher cipher = getCipher(transformation); // Sử dụng phiên bản fallback

		IvParameterSpec iv = null;
		byte[] actualEncrypted;

		if (requiresIV(mode)) {
			if(mode.equalsIgnoreCase("CCM")) {
				blockSize =12;
			}
			byte[] ivBytes = new byte[blockSize];
			System.arraycopy(data, 0, ivBytes, 0, blockSize);
			iv = new IvParameterSpec(ivBytes);

			actualEncrypted = new byte[data.length - blockSize];
			System.arraycopy(data, blockSize, actualEncrypted, 0, actualEncrypted.length);
		} else {
			actualEncrypted = data;
		}

		if (iv != null)
			cipher.init(Cipher.DECRYPT_MODE, key, iv);
		else
			cipher.init(Cipher.DECRYPT_MODE, key);

		byte[] decrypted = cipher.doFinal(actualEncrypted);
		return new String(decrypted, charSet);
	}

	public String decryptString(String data, String alogrithm, String mode, String padding, int blockSize,
			String charSet) throws InvalidKeyException, NoSuchAlgorithmException, NoSuchProviderException,
			NoSuchPaddingException, InvalidAlgorithmParameterException, IllegalBlockSizeException, BadPaddingException,
			UnsupportedEncodingException {
		return decryptByte(Base64.getDecoder().decode(data), alogrithm, mode, padding, blockSize, charSet);
	}

	private boolean requiresIV(String mode) {
	    return mode.equalsIgnoreCase("CBC") ||
	           mode.equalsIgnoreCase("CFB") ||
	           mode.equalsIgnoreCase("OFB") ||
	           mode.equalsIgnoreCase("CTR") ||
	           mode.equalsIgnoreCase("GCM") ||
	           mode.equalsIgnoreCase("CCM") ||
	           mode.equalsIgnoreCase("PCBC");
	}
	
	public void encryptFile(String srcFile, String destFile, String instance) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, InvalidAlgorithmParameterException, FileNotFoundException, IOException, NoSuchProviderException, IllegalBlockSizeException, BadPaddingException {
	       Cipher cipher = getCipher(instance);
	       // Tạo IV ngẫu nhiên
	       byte[] iv = new byte[16];
	       new SecureRandom().nextBytes(iv);
	       IvParameterSpec ivSpec = new IvParameterSpec(iv);
	       cipher.init(Cipher.ENCRYPT_MODE, key, ivSpec);
	       try (BufferedInputStream bis = new BufferedInputStream(new FileInputStream(srcFile));
	            BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(destFile))) {
	           // Ghi IV vào đầu file
	           bos.write(iv);
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
	
	public void encryptFile(String srcFile, String destFile, String instance, String mode)
	        throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException,
	               InvalidAlgorithmParameterException, IOException, NoSuchProviderException {

	    Cipher cipher = getCipher(instance);
	    int blockSize = cipher.getBlockSize();
	    byte[] ivBytes = new byte[blockSize];
	    IvParameterSpec iv = null;

	    if (requiresIV(mode)) {
	        SecureRandom random = new SecureRandom();
	        random.nextBytes(ivBytes);
	        iv = new IvParameterSpec(ivBytes);
	        cipher.init(Cipher.ENCRYPT_MODE, key, iv);
	    } else {
	        cipher.init(Cipher.ENCRYPT_MODE, key);
	    }

	    try (BufferedInputStream bis = new BufferedInputStream(new FileInputStream(srcFile));
	         BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(destFile))) {

	        // Ghi IV vào đầu file nếu có
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

	   // Giải mã file
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
	            // Đọc IV từ đầu file
	            if (bis.read(ivBytes) != blockSize) {
	                throw new IOException("Không thể đọc đủ IV từ file");
	            }
	            iv = new IvParameterSpec(ivBytes);
	            cipher.init(Cipher.DECRYPT_MODE, key, iv);
	        } else {
	            cipher.init(Cipher.DECRYPT_MODE, key);
	        }

	        // Bọc CipherInputStream sau khi đã đọc IV
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
