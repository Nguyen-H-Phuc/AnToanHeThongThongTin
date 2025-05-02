package model.classicialcipher;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Random;

public class AffineCipher extends ClasscialCipher {
	private int a, b;
	
	public int gcdEuclid(int a, int b) {
		if (b == 0) {
			return a;
		} else {
			return gcdEuclid(b, a % b);
		}
	}
	public void genKey() {
		Random random = new Random();
		int m = VIETNAMESE_ALPHABET.length();
		do {
			int c = random.nextInt(m);
			a = c;
		}
		while (gcdEuclid(a, m) != 1);
		b = random.nextInt(m);
	}
	
	public String saveKey(String filename) throws IOException {
		try (BufferedWriter writer = new BufferedWriter(new FileWriter(filename))) {
			writer.write("a=" + a);
			writer.newLine();
			writer.write("b=" + b);
			writer.newLine();
			return "Đã lưu khóa vào " + filename;
		} 
	}

	public String loadKey(String filename) throws FileNotFoundException, IOException {
		try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
			String line;
			while ((line = reader.readLine()) != null) {
				if (line.startsWith("a=")) {
					a = Integer.parseInt(line.substring(2));
				} else if (line.startsWith("b=")) {
					b = Integer.parseInt(line.substring(2));
				}
			}
			return "Đã tải khóa từ " + filename;
		} 
	}

	public void encryptText(int a, int b) {
		StringBuilder result = new StringBuilder();
		for (int i = 0; i < this.getInput().length(); i++) {
			char c = this.getInput().charAt(i);
			int postionChar = VIETNAMESE_ALPHABET.indexOf(c);
			if (postionChar != -1) {
				result.append(VIETNAMESE_ALPHABET.charAt((a * postionChar + b) % VIETNAMESE_ALPHABET.length()));
			} else {
				result.append(c);
			}
		}
		this.setOutput(result.toString());
	}

	public void decryptText(int a, int b) {
		StringBuilder result = new StringBuilder();
		int aInverse = modInverse(a);
		for (int i = 0; i < this.getInput().length(); i++) {
			char c = this.getInput().charAt(i);
			if (Character.isLetter(c)) {
				int postionChar = VIETNAMESE_ALPHABET.indexOf(c);
				result.append(VIETNAMESE_ALPHABET.charAt(aInverse * (postionChar - b+VIETNAMESE_ALPHABET.length()) % VIETNAMESE_ALPHABET.length()));
			} else
				result.append(c);
		}

		this.setOutput(result.toString());
	}

	public int modInverse(int a) {
		int m = VIETNAMESE_ALPHABET.length();
		int m0 = VIETNAMESE_ALPHABET.length();
		int x0 = 0, x1 = 1;

		while (a > 1) {
			int q = a / m;
			int t = m;

			m = a % m;
			a = t;
			t = x0;

			x0 = x1 - q * x0;
			x1 = t;
		}

		if (x1 < 0)
			x1 += m0;

		return x1;
	}
	
	public boolean checkKey(int a) {
		if (gcdEuclid(a, VIETNAMESE_ALPHABET.length())==1) {
			return true;
		} else {
			return false;
		}
	}

	public int getA() {
		return a;
	}

	public void setA(int a) {
		this.a = a;
	}

	public int getB() {
		return b;
	}

	public void setB(int b) {
		this.b = b;
	}

}
