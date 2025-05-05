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
	
	// Method to hash a text string using a specified hashing algorithm and provider
	public String hashText(String data, String instance) throws NoSuchAlgorithmException, NoSuchProviderException {
	    MessageDigest md;
	    try {
	        // Try to get MessageDigest instance using the specified algorithm
	        md = MessageDigest.getInstance(instance);
	    } catch (NoSuchAlgorithmException e) {
	        // If the algorithm is not found, try using BouncyCastle
	        md = MessageDigest.getInstance(instance, "BC");
	    }
	    
	    // Perform the hash calculation on the input text
	    byte[] hashBytes = md.digest(data.getBytes()); // Convert text to bytes and hash it
	    
	    // Convert the byte array to a BigInteger and then to a hexadecimal string
	    BigInteger value = new BigInteger(1, hashBytes);
	    String hashText = value.toString(16);
	    
	    return hashText;
	}

    
 // Method to hash a file using a specified hashing algorithm and provider
    public String hashFile(String srcFile, String instance) throws NoSuchAlgorithmException, NoSuchProviderException, IOException {
        MessageDigest md;
        try {
            // Try to get MessageDigest instance using the specified algorithm
            md = MessageDigest.getInstance(instance);
        } catch (NoSuchAlgorithmException e) {
            // If the algorithm is not found, try using BouncyCastle
            md = MessageDigest.getInstance(instance, "BC");
        }

        // Read the file to calculate the hash
        InputStream is = new BufferedInputStream(new FileInputStream(srcFile)); // Open the file for reading
        DigestInputStream dis = new DigestInputStream(is, md); // Wrap input stream with DigestInputStream to calculate hash as data is read
        byte[] buffer = new byte[1024]; // Buffer to hold data while reading the file
        while (dis.read(buffer) != -1); // Read file until the end
        
        dis.close();
        byte[] digest = md.digest(); // Finalize the hash calculation

        // Convert the resulting byte array to a BigInteger and then to a hexadecimal string
        BigInteger value = new BigInteger(1, digest);
        String hash = value.toString(16);
        
        dis.close();
        return hash;
    }

}
