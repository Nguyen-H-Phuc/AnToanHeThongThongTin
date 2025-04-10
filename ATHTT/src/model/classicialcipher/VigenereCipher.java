package model.classicialcipher;

import java.util.Random;

public class VigenereCipher {
	private String key;
	private static final String VIETNAMESE_ALPHABET = "AĂÂBCDĐEÊGHIKLMNOÔƠPQRSTUƯVXY" + "aăâbcdđeêghiklmnoôơpqrstuưvxy"
			+ "ÁÀẢÃẠẮẰẲẴẶẤẦẨẪẬÉÈẺẼẸẾỀỂỄỆÍÌỈĨỊ" + "áàảãạắằẳẵặấầẩẫậéèẻẽẹếềểễệíìỉĩị" + "ÓÒỎÕỌỐỒỔỖỘỚỜỞỠỢÚÙỦŨỤỨỪỬỮỰÝỲỶỸỴ"
			+ "óòỏõọốồổỗộớờởỡợúùủũụứừửữựýỳỷỹỵ";

	public String encryptString(String text) {
		StringBuilder result = new StringBuilder();
		int positionKey = 0;
		for (int i = 0; i < text.length(); i++) {
			char c = text.charAt(i);
			if (Character.isLetter(c)) {
				char base = Character.isUpperCase(c) ? 'A' : 'a';
				char k = key.charAt(positionKey);
				int shift = Character.toUpperCase(k) - 'A';
				char encrypted = (char) ((c - base + shift) % 26 + base);
				result.append(encrypted);
				positionKey = (positionKey + 1) % key.length();
			} else {
				result.append(c);
			}
		}
		return result.toString();
	}

	public String decryptString(String text) {
		StringBuilder result = new StringBuilder();
		int positionKey = 0;
		for (int i = 0; i < text.length(); i++) {
			char c = text.charAt(i);
			if (Character.isLetter(c)) {
				char base = Character.isUpperCase(c) ? 'A' : 'a';
				char k = key.charAt(positionKey);
				int shift = Character.toUpperCase(k) - 'A';
				char decrypted = (char) ((c - base + 26 - shift) % 26 + base);
				result.append(decrypted);
				positionKey = (positionKey + 1) % key.length();
			} else {
				result.append(c);
			}

		}
		return result.toString();
	}

	public String encryptStringVN(String text) {
		StringBuilder result = new StringBuilder();
		int positionKey = 0;
		for (int i = 0; i < text.length(); i++) {
			char c = text.charAt(i);
			if (Character.isLetter(c)) {
				char k = key.charAt(positionKey);
				int shift = VIETNAMESE_ALPHABET.indexOf(k);
				int charPosition = VIETNAMESE_ALPHABET.indexOf(c);
				char encrypted = VIETNAMESE_ALPHABET.charAt(((charPosition + shift) % VIETNAMESE_ALPHABET.length()));
				result.append(encrypted);
				positionKey = (positionKey + 1) % key.length();
			} else {
				result.append(c);
			}
		}
		return result.toString();
	}

	public String decryptStringVN(String text) {
		StringBuilder result = new StringBuilder();
		int positionKey = 0;
		for (int i = 0; i < text.length(); i++) {
			char c = text.charAt(i);
			if (Character.isLetter(c)) {
				char k = key.charAt(positionKey);
				int shift = VIETNAMESE_ALPHABET.indexOf(k);
				int charPosition = VIETNAMESE_ALPHABET.indexOf(c);
				char encrypted = VIETNAMESE_ALPHABET
						.charAt(((charPosition - shift + VIETNAMESE_ALPHABET.length()) % VIETNAMESE_ALPHABET.length()));
				result.append(encrypted);
				positionKey = (positionKey + 1) % key.length();
			} else {
				result.append(c);
			}
		}
		return result.toString();
	}

	public String getKey() {
		return this.key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public void genKey(int keyLength) {
		StringBuilder key = new StringBuilder();
		Random random = new Random();

		for (int i = 0; i < keyLength; i++) {
			int index = random.nextInt(VIETNAMESE_ALPHABET.length());
			key.append(VIETNAMESE_ALPHABET.charAt(index));
		}

		setKey(key.toString());;
	}
	
	public static void main(String[] args) {
		VigenereCipher vc = new VigenereCipher();
		vc.genKey(5);
		System.out.println(vc.getKey());
		String text = "divert troops to east ridge";
		
		String encrypt = vc.encryptString(text);
		System.out.println(encrypt);
		System.out.println(vc.decryptString(encrypt));
		String textVN = "Giả sử ta cần mã hóa câu: divert troops to east ridge (chuyển quân sang bờ phía đông). Đầu tiên chọn ra một cụm từ làm khóa. Ví dụ WHITE. Ta sẽ viết cụm WHITE này lặp lại đến khi bằng độ dài bản rõ. Rồi lập sơ đồ sau:";
		String encryptVn = vc.encryptStringVN(textVN);
		System.out.println(encryptVn);
		System.out.println(vc.decryptStringVN(encryptVn));
	}
}
