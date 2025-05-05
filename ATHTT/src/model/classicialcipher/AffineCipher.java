package model.classicialcipher;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Random;

public class AffineCipher extends ClasscialCipher {
	private int a, b;
	
	public int gcdEuclid(int a, int b) {
		if (b == 0) {
			return a;
		} else {
			return gcdEuclid(b, a % b);
		}
	}
	
	// Generates a key with two values, a and b
	public void genKey() {
	    Random random = new Random();
	    int m = VIETNAMESE_ALPHABET.length();
	    
	    do {
	        // Generate a random value for 'a'
	        int c = random.nextInt(m);
	        a = c;
	    } while (gcdEuclid(a, m) != 1); // Ensure 'a' is coprime with m
	    
	    // Generate a random value for 'b'
	    b = random.nextInt(m);
	}
	
	// Saves a key to file
	public String saveKey(String filename) throws IOException {
		try (BufferedWriter writer = new BufferedWriter(new FileWriter(filename))) {
			writer.write("a=" + a);
			writer.newLine();
			writer.write("b=" + b);
			writer.newLine();
			return "Đã lưu khóa vào " + filename;
		} 
	}
	
	//load key from a file
	public String loadKey(String filename) throws FileNotFoundException, IOException {
		try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
			String line;
			while ((line = reader.readLine()) != null) {
				if (line.startsWith("a=")) {
					a = Integer.parseInt(line.substring(2));
				} else if (line.startsWith("b=")) {
					b = Integer.parseInt(line.substring(2));
				}
			}
			return "Đã tải khóa từ " + filename;
		} 
	}

	// Encrypts the input text using the affine cipher
	public void encryptText(int a, int b) {
	    StringBuilder result = new StringBuilder();
	    
	    // Iterate over each character in the input text
	    for (int i = 0; i < this.getInput().length(); i++) {
	        char c = this.getInput().charAt(i);
	        
	        // Find the index of the character in the Vietnamese alphabet
	        int postionChar = VIETNAMESE_ALPHABET.indexOf(c);
	        
	        if (postionChar != -1) {
	            // Apply the affine cipher formula: (a * index + b) % alphabet_length
	            result.append(VIETNAMESE_ALPHABET.charAt((a * postionChar + b) % VIETNAMESE_ALPHABET.length()));
	        } else {
	            // Append non-alphabet characters unchanged
	            result.append(c);
	        }
	    }
	    
	    this.setOutput(result.toString());
	}


	// Decrypts the input text using the affine cipher
	public void decryptText(int a, int b) {
	    StringBuilder result = new StringBuilder();
	    
	    // Calculate the modular inverse of 'a' to decrypt
	    int aInverse = modInverse(a);
	    
	    // Iterate over each character in the input text
	    for (int i = 0; i < this.getInput().length(); i++) {
	        char c = this.getInput().charAt(i);
	        
	        // If the character is a letter
	        if (Character.isLetter(c)) {
	            // Find the index of the character in the Vietnamese alphabet
	            int postionChar = VIETNAMESE_ALPHABET.indexOf(c);
	            
	            // Apply the affine cipher decryption formula: (a^-1 * (index - b)) % alphabet_length
	            result.append(VIETNAMESE_ALPHABET.charAt(aInverse * (postionChar - b + VIETNAMESE_ALPHABET.length()) % VIETNAMESE_ALPHABET.length()));
	        } else {
	            // Append non-alphabet characters unchanged
	            result.append(c);
	        }
	    }

	    // Set the decrypted result as the output
	    this.setOutput(result.toString());
	}

	 //Calculates the modular inverse of 'a' with respect to the length of the alphabet
	 public int modInverse(int a) {
	     int m = VIETNAMESE_ALPHABET.length();
	     int m0 = VIETNAMESE_ALPHABET.length();
	     int x0 = 0, x1 = 1;

	     // Apply the Extended Euclidean Algorithm to find the modular inverse
	     while (a > 1) {
	         int q = a / m;
	         int t = m;

	         m = a % m;
	         a = t;
	         t = x0;

	         x0 = x1 - q * x0;
	         x1 = t;
	     }

	     // If x1 is negative, add m0 to make it positive
	     if (x1 < 0)
	         x1 += m0;

	     return x1;
	 }
	
	public boolean checkKey(int a) {
		if (gcdEuclid(a, VIETNAMESE_ALPHABET.length())==1) {
			return true;
		} else {
			return false;
		}
	}

	public int getA() {
		return a;
	}

	public void setA(int a) {
		this.a = a;
	}

	public int getB() {
		return b;
	}

	public void setB(int b) {
		this.b = b;
	}

}
