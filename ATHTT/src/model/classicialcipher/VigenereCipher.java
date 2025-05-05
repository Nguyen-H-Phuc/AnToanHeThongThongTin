package model.classicialcipher;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Random;

public class VigenereCipher extends ClasscialCipher {
	private int keyLength;
	private String key;

	// Encrypts the input text 
	public void encryptText() {
	    StringBuilder result = new StringBuilder();
	    int positionKey = 0;
	    
	    for (int i = 0; i < this.getInput().length(); i++) {
	        char c = this.getInput().charAt(i);
	        
	        if (Character.isLetter(c)) {
	            char k = key.charAt(positionKey);
	            int shift = VIETNAMESE_ALPHABET.indexOf(k);
	            
	            if (shift == -1) {
	                // skip invalid key character
	                positionKey = (positionKey + 1) % key.length();
	            }
	            
	            int charPosition = VIETNAMESE_ALPHABET.indexOf(c);
	            char encrypted = VIETNAMESE_ALPHABET
	                    .charAt(((charPosition + shift) % VIETNAMESE_ALPHABET.length()));
	            result.append(encrypted);
	            
	            // move to next key character
	            positionKey = (positionKey + 1) % key.length();
	        } else {
	            // Keep non-letter characters as is
	            result.append(c);
	        }
	    }
	    
	    this.setOutput(result.toString());
	}


	// decrypts the input text 
	public void decryptText() {
	    StringBuilder result = new StringBuilder();
	    int positionKey = 0;
	    
	    for (int i = 0; i < this.getInput().length(); i++) {
	        char c = this.getInput().charAt(i);
	        
	        if (Character.isLetter(c)) {
	            char k = key.charAt(positionKey);
	            int shift = VIETNAMESE_ALPHABET.indexOf(k);
	            
	            if (shift == -1) {
	                // skip invalid key character
	                positionKey = (positionKey + 1) % key.length();
	            }
	            
	            int charPosition = VIETNAMESE_ALPHABET.indexOf(c);
	            // calculate decrypted character
	            char encrypted = VIETNAMESE_ALPHABET.charAt(
	                ((charPosition - shift + VIETNAMESE_ALPHABET.length()) % VIETNAMESE_ALPHABET.length()));
	            result.append(encrypted);
	            
	            // Move to next key character
	            positionKey = (positionKey + 1) % key.length();
	        } else {
	            // keep non-letter characters as is
	            result.append(c);
	        }
	    }
	    
	    this.setOutput(result.toString());
	}


	// Generates a random key from the Vietnamese alphabet
	public void genKey() {
	    StringBuilder key = new StringBuilder();
	    Random random = new Random();
	    
	    for (int i = 0; i < keyLength; i++) {
	        // Pick a random character from the Vietnamese alphabet
	        int index = random.nextInt(VIETNAMESE_ALPHABET.length());
	        key.append(VIETNAMESE_ALPHABET.charAt(index));
	    }
	    
	    // Set the generated key
	    setKey(key.toString());
	}
	
	// Loads a key from a file
	public String loadKey(String filePath) throws FileNotFoundException, IOException {
	    StringBuilder loadedKey = new StringBuilder();
	    
	    try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
	        String line;
	        while ((line = reader.readLine()) != null) {
	            // Append each line to the loaded key
	            loadedKey.append(line);
	        }
	        // Set the loaded key
	        setKey(loadedKey.toString());
	        return "Tải khoá thành công";
	    }
	}
	// Saves a key from a file
	public String saveKey(String filePath) throws IOException {
		try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            writer.write(getKey());
            return "Lưu khoá thành công vào file: " + filePath;
        } 
	}
	
	public String getKey() {
		return this.key;
	}

	public void setKey(String key) {
		this.key = key;
	}
	
	public int getKeyLength() {
		return this.keyLength;
	}
	
	public void setKeyLength(int keyLength) {
		this.keyLength = keyLength;
	}
	
}
