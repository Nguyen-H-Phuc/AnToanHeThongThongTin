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
import java.security.Provider;
import java.security.Security;
import java.util.Set;

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
	
	
	 public static void main(String[] args) throws NoSuchAlgorithmException, NoSuchProviderException {
//	        Set<String> algorithms = Security.getAlgorithms("MessageDigest");
//	        for (String algo : algorithms) {
//	            System.out.println(algo);
//	        }

//	        HashUtil hash = new HashUtil();
//	        System.out.println(hash.hashText("50 năm ngày giải phóng miền nam", "1.2.804.2.1.1.1.1.2.2.1"));
//	        int i =0;
//	        for (Provider provider : Security.getProviders()) {
//	            System.out.println("Provider: " + provider.getName());
//	            Set<Provider.Service> services = provider.getServices();
//	            for (Provider.Service service : services) {
//	                if ("MessageDigest".equalsIgnoreCase(service.getType())) {
//	                	i++;
//	                    System.out.println(i+". " + service.getAlgorithm());
//	                }
//	            }
//	        }

	    }
}
