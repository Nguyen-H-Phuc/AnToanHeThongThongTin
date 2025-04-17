package model.classicialcipher;

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
			int index = getVietnameseAlphabet().indexOf(c);
			if (index != -1) {
				int newIndex = (index + shift) % getVietnameseAlphabet().length();
				result.append(getVietnameseAlphabet().charAt(newIndex));
			} else {
				result.append(c);
			}
		}
		setOutput(result.toString());
	}

    // Hàm giải mã
    public void decryptText(int shift) {
        encryptText(getVietnameseAlphabet().length() - shift);
    }

//     Hàm tạo key ngẫu nhiên và lưu vào file
	public int genKey() {
        Random random = new Random();
        int key = random.nextInt(getVietnameseAlphabet().length()); 
        return key;
    }
	
	public String saveKey(String filePath, int key) {
	    File file = new File(filePath);
	    try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
	        writer.write(String.valueOf(key));
	        return "SUCCESS";
	    } catch (IOException e) {
	        return "Lỗi khi ghi key: " + e.getMessage();
	    }
	}



    // Hàm load key từ file
//    public static int loadKey() {
//        try {
//            String content = Files.readString(Paths.get(KEY_FILE)).trim();
//            return Integer.parseInt(content);
//        } catch (IOException | NumberFormatException e) {
//            System.err.println("Lỗi khi đọc key: " + e.getMessage());
//            return -1;
//        }
//    }

    public static void main(String[] args) {
//        genKey(); // Tạo và lưu key
//        int key = 1; // Đọc key từ file
//
//
//            String input = "Xin chào, đây là mã hóa Tiếng Việt!";
//            ShiftCipher sc = new ShiftCipher();
//            sc.setInput(input);
//            String cipherText = sc.encryptText(key);
//            System.out.println(cipherText);
//            sc.setInput(cipherText);
//            System.out.println(sc.decryptText(key));

//            System.out.println("Văn bản gốc: " + text);
//            System.out.println("Văn bản mã hóa: " + encryptedText);
//            System.out.println("Văn bản giải mã: " + decryptedText);
       

    }

}
