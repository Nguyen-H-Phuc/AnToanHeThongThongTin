package model;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Scanner;

public class AffineCipher {
	private static final String FILE_KEY = "affine_key.txt";
	private static Map<Character, Character> encryptionMap = new HashMap<>();
    private static Map<Character, Character> decryptionMap = new HashMap<>();
	private static final String VIETNAMESE_ALPHABET = "AĂÂBCDĐEÊGHIKLMNOÔƠPQRSTUƯVXY" +
	        "aăâbcdđeêghiklmnoôơpqrstuưvxy" +
	        "ÁÀẢÃẠẮẰẲẴẶẤẦẨẪẬÉÈẺẼẸẾỀỂỄỆÍÌỈĨỊ" +
	        "áàảãạắằẳẵặấầẩẫậéèẻẽẹếềểễệíìỉĩị" +
	        "ÓÒỎÕỌỐỒỔỖỘỚỜỞỠỢÚÙỦŨỤỨỪỬỮỰÝỲỶỸỴ" +
	        "óòỏõọốồổỗộớờởỡợúùủũụứừửữựýỳỷỹỵ";
		
	public int gcdEuclid(int a, int b) {
		if (b == 0)
			return a;
		else
			return gcdEuclid(b, a % b);
	}

	public void genKey() {
		Random random = new Random();
		int key;
		do {
			key = random.nextInt(VIETNAMESE_ALPHABET.length());
		} while (gcdEuclid(key, VIETNAMESE_ALPHABET.length()) != 1);
		int b = 1 + random.nextInt(VIETNAMESE_ALPHABET.length() - 1);
		List<Character> shuffledChars = new ArrayList<>();
		for (char c : VIETNAMESE_ALPHABET.toCharArray()) {
			int x = VIETNAMESE_ALPHABET.indexOf(c); 
			int y = (key * x + b) % VIETNAMESE_ALPHABET.length();
			char ch = VIETNAMESE_ALPHABET.charAt(y);
			shuffledChars.add(ch);
		}

		for (int i = 0; i < VIETNAMESE_ALPHABET.length(); i++) {
            encryptionMap.put(VIETNAMESE_ALPHABET.charAt(i), shuffledChars.get(i));
            decryptionMap.put(shuffledChars.get(i), VIETNAMESE_ALPHABET.charAt(i));
        }

        saveSubstitutionTable();
    }

    // Lưu bảng mã thay thế vào file txt
    private void saveSubstitutionTable() {
        try (Writer writer = new FileWriter(FILE_KEY)) {
            for (Map.Entry<Character, Character> entry : encryptionMap.entrySet()) {
                writer.write(entry.getKey() + " " + entry.getValue() + "\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
	
	public void loadKey() {
	encryptionMap.clear();
    decryptionMap.clear();
    
    try (Reader reader = new FileReader(FILE_KEY);
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
        genKey();
    }
}
	
	public String encrypt(String text) {
		loadKey();
		StringBuilder encryptedText = new StringBuilder();
		for(char c : text.toCharArray()) {
			encryptedText.append(encryptionMap.getOrDefault(c, c));
		}
		return encryptedText.toString();
	}
	
	public String decrypt(String text) {
		loadKey();
		StringBuilder decryptedText = new StringBuilder();
		for(char c : text.toCharArray()) {
			decryptedText.append(decryptionMap.getOrDefault(c, c));
		}
		return decryptedText.toString();
	}
	
	public static void main(String[] args) {
		String text = "Mật mã học (Cryptography) là ngành khoa học nghiên cứu về việc đảm bảo an toàn thông tin. Mật mã học gắn liền với quá trình mã hóa nghĩa là chuyển đổi thông tin từ dạng \"có thể hiểu được\" thành dạng \"không thể hiểu được\" và ngược lại là quá trình giải mã.";
		AffineCipher ac = new AffineCipher();
		String textEncryption = ac.encrypt(text);
		System.out.println(textEncryption);
		System.out.println(ac.decrypt(textEncryption));
	}
}
