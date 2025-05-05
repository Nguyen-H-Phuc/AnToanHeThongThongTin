package model.classicialcipher;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Random;

public class ShiftCipher extends ClasscialCipher {

    // encrypt text
	public void encryptText(int shift) {
		StringBuilder result = new StringBuilder();

		// Loop through each character in the input
		for (char c : getInput().toCharArray()) {
			int index = VIETNAMESE_ALPHABET.indexOf(c);

			if (index != -1) {
				// Shift the character within the alphabet and wrap around if needed
				int newIndex = (index + shift) % VIETNAMESE_ALPHABET.length();
				result.append(VIETNAMESE_ALPHABET.charAt(newIndex));
			} else {
				// Keep the character unchanged if it’s not in the alphabet
				result.append(c);
			}
		}

		setOutput(result.toString());
	}

    // decrypt text
	public void decryptText(int shift) {
		encryptText(VIETNAMESE_ALPHABET.length() - shift);
	}

//     create random number
	public int genKey() {
		Random random = new Random();
		int key = random.nextInt(VIETNAMESE_ALPHABET.length());
		return key;
	}

	// save key from file 
	public String saveKey(String filePath, int key) {
		File file = new File(filePath);
		try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
			writer.write(String.valueOf(key));
			return "Lưu khoá thành công.";
		} catch (IOException e) {
			return "Lỗi khi ghi key: " + e.getMessage();
		}
	}

//     load key from file
	public int loadKey(String filePath) throws IOException, NumberFormatException {
		try (BufferedReader reader = Files.newBufferedReader(Paths.get(filePath))) {
			String line = reader.readLine();
			return Integer.parseInt(line.trim());
		}
	}

}
