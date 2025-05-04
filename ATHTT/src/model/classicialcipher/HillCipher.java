package model.classicialcipher;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Random;

public class HillCipher extends ClasscialCipher {
	int [][] matrix;
	int [][] invMatrix;
		
	public HillCipher() {
		this.matrix = new int[2][2];
		this.invMatrix = new int[2][2];
	}
	
	public void genKey() {
		Random random = new Random();
		int det = 0;
		do {
			for(int i = 0; i<matrix.length; i++) {
				for(int j = 0; j <matrix[i].length; j++) {
					matrix[i][j] = random.nextInt(50);
				}
			}
			
			det = determinant2(matrix);
		}
		while(gcd(det, VIETNAMESE_ALPHABET.length()) != 1);
	}
	
	public void createInvMatrix() {
		int m = VIETNAMESE_ALPHABET.length();
		int det = determinant2(matrix);
		Integer detInv = modInverse(det, m);
		if(checkMatrix(detInv)) {
		this.invMatrix = new int[2][2];
		
		invMatrix[0][0] = (matrix[1][1] * detInv) % m;
		invMatrix[0][1] = (-matrix[0][1] * detInv) % m;
		invMatrix[1][0] = (-matrix[1][0] * detInv) % m;
		invMatrix[1][1] = (matrix[0][0] * detInv) % m;

		for (int i = 0; i < 2; i++) {
			for (int j = 0; j < 2; j++) {
				invMatrix[i][j] = (invMatrix[i][j] + m) % m;
			}
		}
	}
		else {clearInvMatrix();}
}
	
	public String loadKey(String filePath) throws FileNotFoundException, IOException, NumberFormatException {
	    try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
	        for (int i = 0; i < 2; i++) {
	            String line = reader.readLine();
	            if (line == null) {
	                return "Lỗi: file không đủ dữ liệu";
	            }
	            String[] parts = line.trim().split("\\s+");
	            if (parts.length < 2) {
	                return "Lỗi: dòng " + (i + 1) + " không đủ dữ liệu";
	            }
	            for (int j = 0; j < 2; j++) {
	                matrix[i][j] = Integer.parseInt(parts[j]);
	            }
	        }
	        return "Đã load thành công";
	    } 
	}
	
	public String saveKey(String filePath) throws IOException  {
	    try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
	        for (int i = 0; i < matrix.length; i++) {
	            for (int j = 0; j < matrix[i].length; j++) {
	            	System.out.println(matrix[i][j]);
	                writer.write(matrix[i][j] + " ");
	            }
	            writer.newLine();
	        }
	        return "Đã lưu thành công";
	    } 
	}
	
	public void encryptText() {
		StringBuilder result = new StringBuilder();
		String alphabet = VIETNAMESE_ALPHABET;
		int m = VIETNAMESE_ALPHABET.length();
		if (this.getInput().length() % 2 == 1) {
			this.setInput(this.getInput() + "Xlk,.");
		}

		for (int i = 0; i < getInput().length(); i += 2) {
			char c1 = getInput().charAt(i);
			char c2 = getInput().charAt(i + 1);
			int p1 = alphabet.indexOf(c1);
			int p2 = alphabet.indexOf(c2);
			if (p1 != -1 && p2 != -1) {
				int c1_enc = (p1 * matrix[0][0] + p2 * matrix[0][1]) % m;
				int c2_enc = (p1 * matrix[1][0] + p2 * matrix[1][1]) % m;
				result.append(alphabet.charAt(c1_enc));
				result.append(alphabet.charAt(c2_enc));
			} else {
				result.append(c1);
				result.append(c2);
			}
		}

		this.setOutput(result.toString());
	}

	
	public void decryptText() {
	    StringBuilder result = new StringBuilder();
	    String alphabet = VIETNAMESE_ALPHABET;
	    int m = VIETNAMESE_ALPHABET.length();
	    for (int i = 0; i < getInput().length(); i += 2) {
	        if (i + 1 < getInput().length()) {
	            char c1 = getInput().charAt(i);
	            char c2 = getInput().charAt(i + 1);
	            int p1 = alphabet.indexOf(c1);
	            int p2 = alphabet.indexOf(c2);
	            if (p1 != -1 && p2 != -1) {
	                int c1_dec = (p1 * invMatrix[0][0] + p2 * invMatrix[0][1]) % m;
	                int c2_dec = (p1 * invMatrix[1][0] + p2 * invMatrix[1][1]) % m;
	                result.append(alphabet.charAt(c1_dec));
	                result.append(alphabet.charAt(c2_dec));
	            } else {
	                result.append(c1);
	                result.append(c2);
	            }
	        }
	    }
	    if (result.length() >= 5 && result.substring(result.length() - 5).equals("Xlk,.")) {
	        result.delete(result.length() - 5, result.length());
	    }

	    
	    this.setOutput(result.toString());
	}
	
	public int determinant2(int [][]matrix) {
	    int m = VIETNAMESE_ALPHABET.length();
	    int det = ((matrix[0][0] * matrix[1][1] - matrix[0][1] * matrix[1][0]) % m + m) % m;
	    return det;
	}

	public Integer modInverse(int a, int m) {
	    a = a % m;
	    for (int x = 1; x < m; x++) {
	        if ((a * x) % m == 1)
	            return x;
	    }
	    return null;
	}
	
	public boolean checkMatrix(Integer i) {
		return i != null;
	}

	 public int[][] getMatrix(){
		 return this.matrix;
	 }
	 
	 public int[][] getInvMatrix(){
		 return this.invMatrix;
	 }
	 
	 public void setMatrix(int[][] matrix) {
		 if(matrix!=null) {
		 this.matrix = matrix;
		 createInvMatrix();
		 }
	 }
	 
	 public void clearInvMatrix() {
		 this.invMatrix = null;
	 }
	 
	 public static void main(String[] args) throws IOException {
		HillCipher hc = new HillCipher();
//		hc.genKey();		
//		hc.createInvMatrix();
//		hc.setInput("Chúng tôi");
//		hc.encryptText();
//		System.out.println(hc.getOutput());
//		
//		hc.setInput(hc.getOutput());
//		hc.decryptText();
//		System.out.println(hc.getOutput());
		int[][] array = {{221, 5}, {37, 15}};
		hc.setMatrix(array);
		hc.saveKey("D:\\test\\hillKey.txt");
	}
	 
}
