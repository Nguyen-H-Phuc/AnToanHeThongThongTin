package model.classicialcipher;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

public abstract class ClasscialCipher {
	private static final String VIETNAMESE_ALPHABET = "AĂÂBCDĐEÊGHIKLMNOÔƠPQRSTUƯVXY" + "aăâbcdđeêghiklmnoôơpqrstuưvxy"
			+ "ÁÀẢÃẠẮẰẲẴẶẤẦẨẪẬÉÈẺẼẸẾỀỂỄỆÍÌỈĨỊ" + "ÓÒỎÕỌỐỒỔỖỘỚỜỞỠỢÚÙỦŨỤỨỪỬỮỰÝỲỶỸỴ";
	private String input;
	private String output;
	
	public abstract String encryptText();
	public abstract String decryptText();
	
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
	
}
