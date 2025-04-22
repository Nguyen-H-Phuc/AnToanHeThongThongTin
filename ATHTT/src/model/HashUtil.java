package model;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigInteger;
import java.security.DigestInputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.Security;
import java.util.Set;

public class HashUtil {
	public String hash(String data, String instance) throws NoSuchAlgorithmException {
	MessageDigest md = MessageDigest.getInstance(instance);
	byte[]  hashBytes = md.digest(data.getBytes());
	BigInteger value = new BigInteger(1, hashBytes);
	return value.toString();
	}
	
	public String hashFile(String srcFile, String instance) throws NoSuchAlgorithmException, IOException {
	    MessageDigest md = MessageDigest.getInstance(instance);
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
	 public static void main(String[] args) {
	        Set<String> algorithms = Security.getAlgorithms("MessageDigest");
	        for (String algo : algorithms) {
	            System.out.println(algo);
	        }
	    }
}
