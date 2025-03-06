package model;

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

public class SubstitutionCipher {
	 private static final String VIETNAMESE_ALPHABET = 
		        "AĂÂBCDĐEÊGHIKLMNOÔƠPQRSTUƯVXY" +
		        "aăâbcdđeêghiklmnoôơpqrstuưvxy" +
		        "ÁÀẢÃẠẮẰẲẴẶẤẦẨẪẬÉÈẺẼẸẾỀỂỄỆÍÌỈĨỊ" +
		        "ÓÒỎÕỌỐỒỔỖỘỚỜỞỠỢÚÙỦŨỤỨỪỬỮỰÝỲỶỸỴ";

		    private static Map<Character, Character> encryptionMap = new HashMap<>();
		    private static Map<Character, Character> decryptionMap = new HashMap<>();
		    private static final String FILE_NAME = "substitution_table.txt";

		    // Tạo bảng mã thay thế ngẫu nhiên
		    private void generateSubstitutionTable() {
		        List<Character> shuffledChars = new ArrayList<>();
		        for (char c : VIETNAMESE_ALPHABET.toCharArray()) {
		            shuffledChars.add(c);
		        }
		        Collections.shuffle(shuffledChars);

		        for (int i = 0; i < VIETNAMESE_ALPHABET.length(); i++) {
		            encryptionMap.put(VIETNAMESE_ALPHABET.charAt(i), shuffledChars.get(i));
		            decryptionMap.put(shuffledChars.get(i), VIETNAMESE_ALPHABET.charAt(i));
		        }

		        saveSubstitutionTable();
		    }

		    // Lưu bảng mã thay thế vào file txt
		    private void saveSubstitutionTable() {
		        try (Writer writer = new FileWriter(FILE_NAME)) {
		            for (Map.Entry<Character, Character> entry : encryptionMap.entrySet()) {
		                writer.write(entry.getKey() + " " + entry.getValue() + "\n");
		            }
		        } catch (IOException e) {
		            e.printStackTrace();
		        }
		    }

		 // Đọc bảng mã thay thế từ file TXT
		    private void loadSubstitutionTable() {
		        encryptionMap.clear();
		        decryptionMap.clear();
		        
		        try (Reader reader = new FileReader(FILE_NAME);
		             Scanner scanner = new Scanner(reader)) {
		            
		            while (scanner.hasNextLine()) {
		                String[] parts = scanner.nextLine().split(" ");
		                if (parts.length == 2) {
		                    char original = parts[0].charAt(0);
		                    char substituted = parts[1].charAt(0);
		                    encryptionMap.put(original, substituted);
		                    decryptionMap.put(substituted, original);
		                }
		            }
		        } catch (IOException e) {
		            System.out.println("Không tìm thấy bảng mã. Tạo mới...");
		            generateSubstitutionTable();
		        }
		    }


		    // Mã hóa văn bản
		    public String encrypt(String text) {
		    	loadSubstitutionTable();
		        StringBuilder encryptedText = new StringBuilder();
		        for (char c : text.toCharArray()) {
		            encryptedText.append(encryptionMap.getOrDefault(c, c));
		        }
		        return encryptedText.toString();
		    }

		    // Giải mã văn bản
		    public String decrypt(String text) {
		        StringBuilder decryptedText = new StringBuilder();
		        for (char c : text.toCharArray()) {
		            decryptedText.append(decryptionMap.getOrDefault(c, c));
		        }
		        return decryptedText.toString();
		    }
public static void main(String[] args) {
	SubstitutionCipher vd = new SubstitutionCipher();
	System.out.println(vd.encrypt("Hello"));
}
		}
