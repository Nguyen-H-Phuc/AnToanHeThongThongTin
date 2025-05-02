package model.classicialcipher;

import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class SubstitutionCipher extends ClasscialCipher {
	private Map<Character, Character> encryptionMap = new HashMap<>();
	private Map<Character, Character> decryptionMap = new HashMap<>();


	// Tạo bảng mã thay thế ngẫu nhiên
	public void generateSubstitutionTable() {
		List<Character> shuffledChars = new ArrayList<>();
		for (char c : VIETNAMESE_ALPHABET.toCharArray()) {
			shuffledChars.add(c);
		}
		Collections.shuffle(shuffledChars);

		for (int i = 0; i < VIETNAMESE_ALPHABET.length(); i++) {
			encryptionMap.put(VIETNAMESE_ALPHABET.charAt(i), shuffledChars.get(i));
			decryptionMap.put(shuffledChars.get(i), VIETNAMESE_ALPHABET.charAt(i));
		}
	}

	// Lưu bảng mã thay thế vào file txt
	public String saveSubstitutionTable(String filePath) throws IOException {
		try (Writer writer = new FileWriter(filePath)) {
			int count = 0;
			for (Map.Entry<Character, Character> entry : encryptionMap.entrySet()) {
				writer.write(entry.getKey() + " " + entry.getValue() + "\n");
				count++;
			}

			// Kiểm tra số lượng cặp đã ghi
			if (count == VIETNAMESE_ALPHABET.length()) {
				return "Lưu key thành công! Số cặp đã lưu: " + count;
			} else {
				return "Lưu key KHÔNG đầy đủ! Chỉ lưu được " + count + " / " + VIETNAMESE_ALPHABET.length();
			}
		}
	}

	// Đọc bảng mã thay thế từ file TXT
	public String loadSubstitutionTable(String filePath) throws FileNotFoundException, IOException {
		encryptionMap.clear();
		decryptionMap.clear();

		try (Reader reader = new FileReader(filePath); Scanner scanner = new Scanner(reader)) {

			int count = 0;
			while (scanner.hasNextLine()) {
				String[] parts = scanner.nextLine().split(" ");
				if (parts.length == 2) {
					char original = parts[0].charAt(0);
					char substituted = parts[1].charAt(0);
					encryptionMap.put(original, substituted);
					decryptionMap.put(substituted, original);
					count++;
				}
			}

			if (count == VIETNAMESE_ALPHABET.length()) {
				return "Tải key thành công! Số cặp đã nạp: " + count;
			} else {
				return "Tải key KHÔNG đầy đủ! Chỉ nạp được " + count + " / " + VIETNAMESE_ALPHABET.length();
			}
		}
	}

	// Mã hoá văn bản
	public void encryptText() {
		StringBuilder encryptedText = new StringBuilder();
		for (char c : this.getInput().toCharArray()) {
			encryptedText.append(encryptionMap.getOrDefault(c, c));
		}
		this.setOutput( encryptedText.toString());
		
	}

	// Giải mã văn bản
	public void decryptText() {
		StringBuilder decryptedText = new StringBuilder();
		for (char c : this.getInput().toCharArray()) {
			decryptedText.append(decryptionMap.getOrDefault(c, c));
		}
		this.setOutput(decryptedText.toString());
	}

	public String saveOutputToFile(String output, String filePath) {
		try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
			writer.write(output);
			writer.flush();
			return "Lưu file thành công: " + filePath;
		} catch (IOException e) {
			e.printStackTrace();
			return "Lỗi khi lưu file: " + e.getMessage();
		}
	}


	public Map<Character, Character> getEncryptionMap() {
		return encryptionMap;
	}

	public Map<Character, Character> getDecryptionMap() {
		return decryptionMap;
	}

	public void setEncryptionMap(Map<Character, Character> encryptionMap) {
	    this.encryptionMap = encryptionMap;

	    
	    for (Map.Entry<Character, Character> entry : encryptionMap.entrySet()) {
	        Character key = entry.getKey();
	        Character value = entry.getValue();

	        // Đảo chiều
	        decryptionMap.put(value, key);
	    }
	}
	

}
