package model.classicialcipher;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

public abstract class ClasscialCipher {
	private String input;
	private String output;
	private static final String VIETNAMESE_ALPHABET = "AĂÂBCDĐEÊGHIKLMNOÔƠPQRSTUƯVXY" + "aăâbcdđeêghiklmnoôơpqrstuưvxy"
			+ "ÁÀẢÃẠẮẰẲẴẶẤẦẨẪẬÉÈẺẼẸẾỀỂỄỆÍÌỈĨỊ" + "áàảãạắằẳẵặấầẩẫậéèẻẽẹếềểễệíìỉĩị" + "ÓÒỎÕỌỐỒỔỖỘỚỜỞỠỢÚÙỦŨỤỨỪỬỮỰÝỲỶỸỴ"
			+ "óòỏõọốồổỗộớờởỡợúùủũụứừửữựýỳỷỹỵ";	
	private static final int LENGTH_VIETNAMESE_ALPHABET = VIETNAMESE_ALPHABET.length();
	
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

	public String getInput() {
		return input;
	}

	public void setInput(String input) {
		this.input = input;
	}

	public String getOutput() {
		return output;
	}

	public void setOutput(String output) {
		this.output = output;
	}

	public static String getVietnameseAlphabet() {
		return VIETNAMESE_ALPHABET;
	}

	
	public static int getLengthVietnameseAlphabet() {
		return LENGTH_VIETNAMESE_ALPHABET;
	}

}
