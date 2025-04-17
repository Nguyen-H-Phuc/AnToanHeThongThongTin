package model.classicialcipher;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
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
				int shift = getVietnameseAlphabet().indexOf(k);
				if (shift == -1) {
					positionKey = (positionKey + 1) % key.length();
				} 
					int charPosition = getVietnameseAlphabet().indexOf(c);
					char encrypted = getVietnameseAlphabet()
							.charAt(((charPosition + shift) % getVietnameseAlphabet().length()));
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
				int shift = getVietnameseAlphabet().indexOf(k);
				if (shift == -1) {
					positionKey = (positionKey + 1) % key.length();
				}
				
				int charPosition = getVietnameseAlphabet().indexOf(c);
				char encrypted = getVietnameseAlphabet()
						.charAt(((charPosition - shift + getVietnameseAlphabet().length()) % getVietnameseAlphabet().length()));
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
			int index = random.nextInt(getVietnameseAlphabet().length());
			key.append(getVietnameseAlphabet().charAt(index));
		}
		setKey(key.toString());
		if (this.key.length() == this.keyLength) {
			return "SUCCESS";
		}
		else {
			return "FAIL";
		}
	}
	
	public String loadKey(String filePath) {
		StringBuilder loadedKey = new StringBuilder();
		try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
			String line;
			while ((line = reader.readLine()) != null) {
				loadedKey.append(line);
			}
			setKey(loadedKey.toString());
			return "SUCCESS";
		} catch (IOException e) {
			return "Lỗi khi đọc key: " + e.getMessage();
		}
	}

	public String saveKey(String filePath) {
		try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            writer.write(getKey());
            return "SUCCESS";
        } catch (IOException e) {
        	 return "Lỗi khi ghi key: " + e.getMessage();
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
	
	public static void main(String[] args) {
		
	}
}
