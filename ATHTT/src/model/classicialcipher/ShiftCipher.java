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

    // Hàm mã hóa
	public void encryptText(int shift) {
		StringBuilder result = new StringBuilder();
		for (char c : getInput().toCharArray()) {
			int index = VIETNAMESE_ALPHABET.indexOf(c);
			if (index != -1) {
				int newIndex = (index + shift) % VIETNAMESE_ALPHABET.length();
				result.append(VIETNAMESE_ALPHABET.charAt(newIndex));
			} else {
				result.append(c);
			}
		}
		setOutput(result.toString());
	}

    // Hàm giải mã
    public void decryptText(int shift) {
        encryptText(VIETNAMESE_ALPHABET.length() - shift);
    }

//     Hàm tạo key ngẫu nhiên và lưu vào file
	public int genKey() {
        Random random = new Random();
        int key = random.nextInt(VIETNAMESE_ALPHABET.length()); 
        return key;
    }

	//
	public String saveKey(String filePath, int key) {
	    File file = new File(filePath);
	    try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
	        writer.write(String.valueOf(key));
	        return "SUCCESS";
	    } catch (IOException e) {
	        return "Lỗi khi ghi key: " + e.getMessage();
	    }
	}

//     Hàm load key từ file
	public int loadKey(String filePath) throws IOException, NumberFormatException {
	    try (BufferedReader reader = Files.newBufferedReader(Paths.get(filePath))) {
	        String line = reader.readLine();
	        return Integer.parseInt(line.trim());
	    }
	}

}
