package model.classicialcipher;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;

public class SubstitutionCipher extends ClasscialCipher {
	private Map<Character, Character> encryptionMap = new HashMap<>();
	private Map<Character, Character> decryptionMap = new HashMap<>();


	public void generateSubstitutionTable() {
	    // Create a list to hold characters for shuffling
	    List<Character> shuffledChars = new ArrayList<>();

	    // Add each character from the Vietnamese alphabet into the list
	    for (char c : VIETNAMESE_ALPHABET.toCharArray()) {
	        shuffledChars.add(c);
	    }

	    // Randomly shuffle the list to create a substitution mapping
	    Collections.shuffle(shuffledChars);

	    // Build the encryption and decryption maps
	    for (int i = 0; i < VIETNAMESE_ALPHABET.length(); i++) {
	        char originalChar = VIETNAMESE_ALPHABET.charAt(i);
	        char shuffledChar = shuffledChars.get(i);           

	        
	        encryptionMap.put(originalChar, shuffledChar);
	        decryptionMap.put(shuffledChar, originalChar);
	    }
	}


	// Save the substitution table to a text file
	public String saveSubstitutionTable(String filePath) throws IOException {
	    try (Writer writer = new FileWriter(filePath)) {
	        int count = 0;  // Counter to track how many pairs are written

	        // Loop through each key-value pair in the encryption map
	        for (Map.Entry<Character, Character> entry : encryptionMap.entrySet()) {
	            // Write the original character and its mapped (substitute) character to the file
	            writer.write(entry.getKey() + " " + entry.getValue() + "\n");
	            count++;
	        }

	        // Check if all expected pairs were saved
	        if (count == VIETNAMESE_ALPHABET.length()) {
	            return "Số cặp key lưu được: " + count;
	        } else {
	            return "Số cặp key lưu được" + count + " / " + VIETNAMESE_ALPHABET.length();
	        }
	    }
	}

	// Read the substitution table from a TXT file
	public String loadSubstitutionTable(String filePath) throws FileNotFoundException, IOException {
	    // Clear the existing encryption and decryption maps to avoid mixing old data
	    encryptionMap.clear();
	    decryptionMap.clear();

	   
	    try (Reader reader = new FileReader(filePath); Scanner scanner = new Scanner(reader)) {

	        int count = 0;  // Counter to track how many pairs are loaded

	        // Read the file line by line
	        while (scanner.hasNextLine()) {
	            String[] parts = scanner.nextLine().split(" ");  // Split each line by space

	            // Check if the line has exactly two parts: original and substituted character
	            if (parts.length == 2) {
	                char original = parts[0].charAt(0);
	                char substituted = parts[1].charAt(0);  

	                // Add the pair to both encryption and decryption maps
	                encryptionMap.put(original, substituted);
	                decryptionMap.put(substituted, original);

	                count++;
	            }
	        }

	        // Check if all expected pairs were successfully loaded
	        if (count == VIETNAMESE_ALPHABET.length()) {
	            return "Tải khoá thành cồng! Số cặp khoá tải được: " + count;
	        } else {
	            return "Không tải đủ số khoá! Chỉ tải được: " + count + " / " + VIETNAMESE_ALPHABET.length();
	        }
	    }
	}


	// Encrypt the input text using the substitution map
	public void encryptText() {
	    StringBuilder encryptedText = new StringBuilder();  // To store the result

	    // Loop through each character in the input string
	    for (char c : this.getInput().toCharArray()) {
	        // Replace the character with its mapped value if it exists,
	        // otherwise keep the original character
	        encryptedText.append(encryptionMap.getOrDefault(c, c));
	    }

	    // Save the final encrypted string as the output
	    this.setOutput(encryptedText.toString());
	}


	// Decrypt the input text using the substitution map
	public void decryptText() {
	    StringBuilder decryptedText = new StringBuilder();  // To store the result

	    // Loop through each character in the input string
	    for (char c : this.getInput().toCharArray()) {
	        // Replace the character with its mapped original value if it exists,
	        decryptedText.append(decryptionMap.getOrDefault(c, c));
	    }

	    // Save the final decrypted string as the output
	    this.setOutput(decryptedText.toString());
	}



	public Map<Character, Character> getEncryptionMap() {
		return encryptionMap;
	}

	public Map<Character, Character> getDecryptionMap() {
		return decryptionMap;
	}

	// Set the encryption map and automatically generate the corresponding decryption map
	public void setEncryptionMap(Map<Character, Character> encryptionMap) {
	    this.encryptionMap = encryptionMap;  // Set the given encryption map
	    
	    // Iterate over each entry in the encryption map to generate the decryption map
	    for (Map.Entry<Character, Character> entry : encryptionMap.entrySet()) {
	        Character key = entry.getKey();  // The original character (plain text)
	        Character value = entry.getValue();  // The substituted character (cipher text)

	        // Reverse the key-value pair and add to the decryption map
	        decryptionMap.put(value, key);  // In the decryption map, substitute cipher text with original text
	    }
	}

	// Check if there are any duplicate values in the encryption map
	public boolean hasDuplicateValues() {
	    Set<Character> seenValues = new HashSet<>();

	    for (Character value : encryptionMap.values()) {
	        // Try to add the value to the set; if it already exists, add() returns false
	        if (!seenValues.add(value)) {
	            // Return true if a duplicate value is found
	            return true; 
	        }
	    }
	    return false;
	}

}
