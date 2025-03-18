package model;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Random;

public class ShiftCipher {
	private static final String ALPHABET = "AĂÂBCDĐEÊGHIKLMNOÔƠPQRSTUƯVXYaăâbcdđeêghiklmnoôơpqrstuưvxy";
    private static final String KEY_FILE = "key.txt";

    // Hàm mã hóa
    public static String encryption(String text, int shift) {
        StringBuilder result = new StringBuilder();
        for (char c : text.toCharArray()) {
            int index = ALPHABET.indexOf(c);
            if (index != -1) {
                int newIndex = (index + shift) % ALPHABET.length();
                result.append(ALPHABET.charAt(newIndex));
            } else {
                result.append(c);
            }
        }
        return result.toString();
    }

    // Hàm giải mã
    public static String decryption(String text, int shift) {
        return encryption(text, ALPHABET.length() - shift);
    }

    // Hàm tạo key ngẫu nhiên và lưu vào file
    public static void genKey() {
        Random random = new Random();
        int key = random.nextInt(ALPHABET.length()); // Tạo key từ 0 đến độ dài bảng chữ cái
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(KEY_FILE))) {
            writer.write(String.valueOf(key));
            System.out.println("Key đã được tạo và lưu vào " + KEY_FILE);
        } catch (IOException e) {
            System.err.println("Lỗi khi ghi key: " + e.getMessage());
        }
    }

    // Hàm load key từ file
    public static int loadKey() {
        try {
            String content = Files.readString(Paths.get(KEY_FILE)).trim();
            return Integer.parseInt(content);
        } catch (IOException | NumberFormatException e) {
            System.err.println("Lỗi khi đọc key: " + e.getMessage());
            return -1;
        }
    }

    public static void main(String[] args) {
        genKey(); // Tạo và lưu key
        int key = loadKey(); // Đọc key từ file

        if (key != -1) {
            String text = "Xin chào, đây là mã hóa Tiếng Việt!";
            String encryptedText = encryption(text, key);
            String decryptedText = decryption(encryptedText, key);

            System.out.println("Văn bản gốc: " + text);
            System.out.println("Văn bản mã hóa: " + encryptedText);
            System.out.println("Văn bản giải mã: " + decryptedText);
        }
    }
}
