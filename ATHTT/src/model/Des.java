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
import java.security.Provider;
import java.security.SecureRandom;
import java.security.Security;
import java.util.Base64;
import java.util.HashSet;
import java.util.Properties;
import java.util.Set;

public class Des {
	private SecretKey key;
	private String algorithm, mode, padding, charset;
	private int keySize, blockSize;

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
	
	public void saveKey(String filePath) throws IOException {
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
	        
	        // Key là dạng bytes ➔ encode base64 trước
	        String encodedKey = Base64.getEncoder().encodeToString(key.getEncoded());
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
	    keySize = Integer.parseInt(props.getProperty("keySize"));
	    blockSize = Integer.parseInt(props.getProperty("blockSize"));

	    String encodedKey = props.getProperty("key");
	    byte[] decodedKey = Base64.getDecoder().decode(encodedKey);
	    key = new SecretKeySpec(decodedKey, algorithm);
	}
	
	public IvParameterSpec generateIV(String mode, int blockSize) {
		if (requiresIV(mode)) {
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
	           mode.equalsIgnoreCase("GCM"); //||
//	           mode.equalsIgnoreCase("CCM");
	}

//	private byte[] encryptFile(String src, String dest)
//			throws NoSuchAlgorithmException, NoSuchPaddingException, FileNotFoundException {
//		Cipher cipher = Cipher.getInstance("DES");
//		CipherInputStream cis = new CipherInputStream(new FileInputStream(src), cipher);
//		return null;
//	}
	
	public void encryptFile(String data, String dest) throws Exception {
	       Cipher cipher = Cipher.getInstance("AES/CTR/NoPadding");
	       // Tạo IV ngẫu nhiên
	       byte[] iv = new byte[16];
	       new SecureRandom().nextBytes(iv);
	       IvParameterSpec ivSpec = new IvParameterSpec(iv);
	       cipher.init(Cipher.ENCRYPT_MODE, key, ivSpec);
	       try (BufferedInputStream bis = new BufferedInputStream(new FileInputStream(data));
	            BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(dest))) {
	           // Ghi IV vào đầu file
	           bos.write(iv);
	           CipherOutputStream cos = new CipherOutputStream(bos, cipher);
	           byte[] buffer = new byte[1024];
	           int bytesRead;
	           while ((bytesRead = bis.read(buffer)) != -1) {
	               cos.write(buffer, 0, bytesRead);
	           }
	           cos.flush();
	       }
	   }
	   // Giải mã file
	   public void decryptFile(String data, String dest) throws Exception {
	       Cipher cipher = Cipher.getInstance("AES/CTR/NoPadding");
	       try (BufferedInputStream bis = new BufferedInputStream(new FileInputStream(data));
	            BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(dest))) {
	           // Đọc IV từ file
	           byte[] iv = new byte[16];
	           bis.read(iv);
	           IvParameterSpec ivSpec = new IvParameterSpec(iv);
	           cipher.init(Cipher.DECRYPT_MODE, key, ivSpec);
	           CipherInputStream cis = new CipherInputStream(bis, cipher);
	           byte[] buffer = new byte[1024];
	           int bytesRead;
	           while ((bytesRead = cis.read(buffer)) != -1) {
	               bos.write(buffer, 0, bytesRead);
	           }
	           bos.flush();
	       }
	   }

	
	public String getKey() {
		return Base64.getEncoder().encodeToString(key.getEncoded());
	}
	
	public String setKey(String encodedKey, String algorithm, int keyLength) {
		byte[] decodedKey = Base64.getDecoder().decode(encodedKey);
		if (decodedKey.length > keyLength) {
	        return encodedKey + " dài hơn " + keyLength + " bytes, dư " + (decodedKey.length - keyLength) + " byte";
	    }
	    if (decodedKey.length < keyLength) {
	        return encodedKey + " ngắn hơn " + keyLength + " bytes, thiếu " + (keyLength - decodedKey.length) + " byte";
	    }
		key = new SecretKeySpec(decodedKey, algorithm);
		return null;
	}
	

	public static void main(String[] args) throws Exception {
//		String data = "Bị dẫn 0-3, nhưng CLB TP HCM vẫn thắng ngược đại diện UAE Abu Dhabi Country 5-4 ở tứ kết AFC Champions League nữ 2024 trên sân Thống Nhất tối 22/3.";
//		Des des = new Des();
//		des.genKey("ChaCha20",256);
//		String dataEncrypt = des.encryptString(data, "ChaCha20", "ECB", "NoPadding", 128, "UTF-8");
//		System.out.println(dataEncrypt);
//		String dataDe = des.decryptString(dataEncrypt,"ChaCha20", "ECB", "NoPadding", 128, "UTF-8");
//		System.out.println(dataDe);
//		System.out.println(des.getKey());
		Security.addProvider(new BouncyCastleProvider());
//		  for (Provider provider : Security.getProviders()) {
//	            System.out.println("Provider: " + provider.getName());
//	            for (Provider.Service service : provider.getServices()) {
//	                if (service.getType().equalsIgnoreCase("Cipher")) {
//	                    System.out.println("Cipher: " + service.getAlgorithm());
//	                }
//	            }
//	        }
		 Set<String> symmetricAlgorithms = new HashSet<>();
	        Provider[] providers = Security.getProviders();

	        System.out.println("Các thuật toán mã hóa đối xứng được hỗ trợ bởi Java (các provider mặc định):");

	        for (Provider provider : providers) {
	            for (Provider.Service service : provider.getServices()) {
	                if (service.getType().equalsIgnoreCase("Cipher")) {
	                    String algorithm = service.getAlgorithm().toUpperCase();
	                    // Lọc các thuật toán đối xứng phổ biến (có thể không đầy đủ)
	                    if (algorithm.contains("AES") //||
//	                        algorithm.contains("DES") ||
//	                        algorithm.contains("BLOWFISH") ||
//	                        algorithm.contains("TWOFISH") ||
//	                        algorithm.contains("RC2") ||
//	                        algorithm.contains("RC4") ||
//	                        algorithm.contains("RC5") ||
//	                        algorithm.contains("RC6") ||
//	                        algorithm.contains("SERPENT") ||
//	                        algorithm.contains("SKIPJACK") ||
//	                        algorithm.contains("IDEA") ||
//	                        algorithm.contains("CAST5") ||
//	                        algorithm.contains("CAST6") ||
//	                        algorithm.contains("SEED") ||
//	                        algorithm.contains("SM4") ||
//	                        algorithm.contains("GOST28147") // GOST thường được coi là đối xứng
	                    ) {
	                        symmetricAlgorithms.add(algorithm);
	                    }
	                }
	            }
	        }

	        for (String algorithm : symmetricAlgorithms) {
	            System.out.println("- " + algorithm);
	        }
	}
}
