package model.classicialcipher;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Random;

public class AffineCipher extends ClasscialCipher {
	private int a, b;

	public void genKey() {
		Random random = new Random();
		int m = getLengthVietnameseAlphabet();
		do {
			int c = random.nextInt(m);
			a = c;
		}
		while (gcdEuclid(a, m) != 1);
		b = random.nextInt(m);
	}
	
	public String saveKey(String filename) {
		try (BufferedWriter writer = new BufferedWriter(new FileWriter(filename))) {
			writer.write("a=" + a);
			writer.newLine();
			writer.write("b=" + b);
			writer.newLine();
			return "Đã lưu khóa vào " + filename;
		} catch (IOException e) {
			return "Lỗi khi lưu khóa: " + e.getMessage();
		}
	}

	public String loadKey(String filename) {
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
		} catch (IOException e) {
			return "Lỗi khi đọc khóa: " + e.getMessage();
		}
	}

	public void encryptText(int a, int b) {
		StringBuilder result = new StringBuilder();
		for (int i = 0; i < this.getInput().length(); i++) {
			char c = this.getInput().charAt(i);
			int postionChar = getVietnameseAlphabet().indexOf(c);
			if (postionChar != -1) {
				result.append(getVietnameseAlphabet().charAt((a * postionChar + b) % getLengthVietnameseAlphabet()));
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
				int postionChar = getVietnameseAlphabet().indexOf(c);
				result.append(getVietnameseAlphabet().charAt(aInverse * (postionChar - b+getLengthVietnameseAlphabet()) % getLengthVietnameseAlphabet()));
			} else
				result.append(c);
		}

		this.setOutput(result.toString());
	}

	public int gcdEuclid(int a, int b) {
		if (b == 0)
			return a;
		else
			return gcdEuclid(b, a % b);
	}

	public int modInverse(int a) {
		int m = getLengthVietnameseAlphabet();
		int m0 = getLengthVietnameseAlphabet();
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
		if (gcdEuclid(a, getLengthVietnameseAlphabet())==1) {
			return true;
		}
		else return false;
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

	public static void main(String[] args) {
		String text = "Mật mã học (Cryptography) là ngành khoa học nghiên cứu về việc đảm bảo an toàn thông tin. Mật mã học gắn liền với quá trình mã hóa nghĩa là chuyển đổi thông tin từ dạng \"có thể hiểu được\" thành dạng \"không thể hiểu được\" và ngược lại là quá trình giải mã.";
		AffineCipher ac = new AffineCipher();
		ac.setInput(text);
		ac.encryptText(7, 2);
		System.out.println(ac.getOutput());
		ac.setInput(ac.getOutput());
		ac.decryptText(7, 2);
		System.out.println(ac.getOutput());
	}
}
