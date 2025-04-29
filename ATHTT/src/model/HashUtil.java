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
	
	public HashUtil() {
		super();
		Security.addProvider(new BouncyCastleProvider());
	}
	
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
	
	public String hashFile(String srcFile, String instance) throws NoSuchAlgorithmException, NoSuchProviderException, IOException{
		 MessageDigest md;
	        try {
	            md = MessageDigest.getInstance(instance);
	        } catch (NoSuchAlgorithmException e) {
	            // Thử với BouncyCastle
	            md = MessageDigest.getInstance(instance, "BC");
	        }
	        
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
