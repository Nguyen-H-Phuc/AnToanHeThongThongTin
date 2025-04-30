package model;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigInteger;
import java.security.DigestInputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.Security;


import org.bouncycastle.jce.provider.BouncyCastleProvider;

public class HashUtil {
	
	// add provider Bouncy Castle
	public HashUtil() {
		super();
		Security.addProvider(new BouncyCastleProvider());
	}
	
	/**
	 * Hashes an input text string using the specified hashing algorithm (e.g., SHA-256, MD5).
	 * 
	 * This method attempts to create a MessageDigest instance using the given algorithm.
	 * If it fails with the default provider, it will retry with the "BC" (Bouncy Castle) provider.
	 * 
	 * @param data The input text to be hashed
	 * @param instance The name of the hashing algorithm (e.g., "SHA-256", "MD5")
	 * @return The hash string in hexadecimal format
	 * @throws NoSuchAlgorithmException If the algorithm is not supported by either the default or Bouncy Castle provider
	 * @throws NoSuchProviderException If the "BC" provider is not available when needed
	 */
	
    public String hashText(String data, String instance) throws NoSuchAlgorithmException, NoSuchProviderException {
        MessageDigest md;
        try {
            md = MessageDigest.getInstance(instance);
        } catch (NoSuchAlgorithmException e) {
            // Thử với BouncyCastle
            md = MessageDigest.getInstance(instance, "BC");
        }
        
        byte[] hashBytes = md.digest(data.getBytes());
        BigInteger value = new BigInteger(1, hashBytes);
        String hashText = value.toString(16);
                
        return hashText;
    }
    
    /**
     * Computes the hash value of a file's content using the specified hashing algorithm.
     * 
     * This method attempts to create a MessageDigest instance using the given algorithm.
     * If it fails with the default provider, it will retry with the "BC" (Bouncy Castle) provider.
     * Then, it reads the file content through a stream and calculates the hash.
     *
     * @param srcFile The path to the file to be hashed
     * @param instance The name of the hashing algorithm (e.g., "SHA-256", "MD5")
     * @return The hash of the file as a hexadecimal string
     * @throws NoSuchAlgorithmException If the algorithm is not supported by either the default or Bouncy Castle provider
     * @throws NoSuchProviderException If the "BC" provider is not available when needed
     * @throws IOException If an error occurs while reading the file
     */
	public String hashFile(String srcFile, String instance) throws NoSuchAlgorithmException, NoSuchProviderException, IOException{
		 MessageDigest md;
	        try {
	            md = MessageDigest.getInstance(instance);
	        } catch (NoSuchAlgorithmException e) {
	            // Thử với BouncyCastle
	            md = MessageDigest.getInstance(instance, "BC");
	        }
	    
	    // read file
	    InputStream is = new BufferedInputStream(new FileInputStream(srcFile));
	    DigestInputStream dis = new DigestInputStream(is, md);
	    byte[] buffer = new byte[1024];
	    while (dis.read(buffer) != -1);

	    byte[] digest = md.digest();

	    BigInteger value = new BigInteger(1, digest);
	    String hash = value.toString(16);
	    dis.close();
	    return hash;
	}
	
}
