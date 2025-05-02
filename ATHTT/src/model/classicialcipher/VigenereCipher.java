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

	public void encryptText() {
		StringBuilder result = new StringBuilder();
		int positionKey = 0;
		for (int i = 0; i < this.getInput().length(); i++) {
			char c = this.getInput().charAt(i);
			if (Character.isLetter(c)) {
				char k = key.charAt(positionKey);
				int shift = VIETNAMESE_ALPHABET.indexOf(k);
				if (shift == -1) {
					positionKey = (positionKey + 1) % key.length();
				} 
					int charPosition = VIETNAMESE_ALPHABET.indexOf(c);
					char encrypted = VIETNAMESE_ALPHABET
							.charAt(((charPosition + shift) % VIETNAMESE_ALPHABET.length()));
					result.append(encrypted);
					positionKey = (positionKey + 1) % key.length();
				
			} else {
				result.append(c);
			}
		}
		this.setOutput(result.toString());
	}

	public void decryptText() {
		StringBuilder result = new StringBuilder();
		int positionKey = 0;
		for (int i = 0; i < this.getInput().length(); i++) {
			char c = this.getInput().charAt(i);
			if (Character.isLetter(c)) {
				char k = key.charAt(positionKey);
				int shift = VIETNAMESE_ALPHABET.indexOf(k);
				if (shift == -1) {
					positionKey = (positionKey + 1) % key.length();
				}
				
				int charPosition = VIETNAMESE_ALPHABET.indexOf(c);
				char encrypted = VIETNAMESE_ALPHABET
						.charAt(((charPosition - shift + VIETNAMESE_ALPHABET.length()) % VIETNAMESE_ALPHABET.length()));
				result.append(encrypted);
				positionKey = (positionKey + 1) % key.length();
				
			} else {
				result.append(c);
			}
		}
		this.setOutput(result.toString());
	}

	public String genKey() {
		StringBuilder key = new StringBuilder();
		Random random = new Random();
		for (int i = 0; i < keyLength; i++) {
			int index = random.nextInt(VIETNAMESE_ALPHABET.length());
			key.append(VIETNAMESE_ALPHABET.charAt(index));
		}
		setKey(key.toString());
		if (this.key.length() == this.keyLength) {
			return "SUCCESS";
		}
		else {
			return "FAIL";
		}
	}
	
	public String loadKey(String filePath) throws FileNotFoundException, IOException {
		StringBuilder loadedKey = new StringBuilder();
		try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
			String line;
			while ((line = reader.readLine()) != null) {
				loadedKey.append(line);
			}
			setKey(loadedKey.toString());
			return "SUCCESS";
		} 
	}

	public String saveKey(String filePath) throws IOException {
		try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            writer.write(getKey());
            return "SUCCESS";
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
