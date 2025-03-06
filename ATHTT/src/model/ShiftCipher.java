package model;

public class ShiftCipher {
	public static String encryption(String text, int shift) {
		StringBuilder result = new StringBuilder();
		for(char c : text.toCharArray()) {
			if(Character.isLetter(c)) {
				char base = Character.isUpperCase(c) ? 'A' : 'a';
				result.append((char) ((c - base + shift) % 26 + base));
			}else {
				result.append(c);
			}
		}
		return result.toString();
	}
	
	public String decryption(String text, int shift) {
		return encryption(text, 26 - shift);
	}
	
	public static void main(String[] args) {
		System.out.println(encryption("Hello", 3));
	}
}
