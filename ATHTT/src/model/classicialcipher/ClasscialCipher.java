package model.classicialcipher;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

public abstract class ClasscialCipher {
	private String input;
	private String output;
	protected static final String VIETNAMESE_ALPHABET = "AĂÂBCDĐEÊGHIKLMNOÔƠPQRSTUƯVXY" + "aăâbcdđeêghiklmnoôơpqrstuưvxy"
			+ "ÁÀẢÃẠẮẰẲẴẶẤẦẨẪẬÉÈẺẼẸẾỀỂỄỆÍÌỈĨỊ" + "áàảãạắằẳẵặấầẩẫậéèẻẽẹếềểễệíìỉĩị" + "ÓÒỎÕỌỐỒỔỖỘỚỜỞỠỢÚÙỦŨỤỨỪỬỮỰÝỲỶỸỴ"
			+ "óòỏõọốồổỗộớờởỡợúùủũụứừửữựýỳỷỹỵ";	
	
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
	
	 public int gcd(int a, int b) {
	        while (b != 0) {
	            int temp = b;
	            b = a % b;
	            a = temp;
	        }
	        return a;
	    }

	public static String getVietnameseAlphabet() {
		return VIETNAMESE_ALPHABET;
	}
	
}
