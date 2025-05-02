package model.classicialcipher;

import java.util.Random;

public class HillCipher extends ClasscialCipher {
	int [][] matrix;
	int [][] invMatrix;
	
	public void genKey(int dimension) {
		matrix = new int [dimension][dimension];
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
		int detInv = modInverse(det, m); 

		invMatrix = new int[2][2];

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

	
	public String loadKey(String filePath) {
		String message = "";
		return message;
	}
	
	public String saveKey(String filePath) {
		String message = "";
		return message;
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

	
	public int determinant3(int [][]matrix) {
		int det = (matrix[0][0]* (matrix[1][1] * matrix[2][2] - matrix[2][1] * matrix[1][2]))
				+ (matrix[0][1]* (matrix[1][0] * matrix[2][2] - matrix[1][2] * matrix[2][0]))
				+ (matrix[0][2]* (matrix[0][0] * matrix[2][1] - matrix[2][0] * matrix[1][1])) % VIETNAMESE_ALPHABET.length();
		if (det < 0) det += VIETNAMESE_ALPHABET.length();
        return det;
	}
	
	public int modInverse(int a, int m) {
	    a = a % m;
	    for (int x = 1; x < m; x++) {
	        if ((a * x) % m == 1)
	            return x;
	    }
	    throw new ArithmeticException("Modular inverse does not exist");
	}

	
	 public int gcd(int a, int b) {
	        while (b != 0) {
	            int temp = b;
	            b = a % b;
	            a = temp;
	        }
	        return a;
	    }
	 
	 public static void main(String[] args) {
		HillCipher hc = new HillCipher();
		hc.genKey(2);		
		hc.createInvMatrix();
		hc.setInput("Chúng tôi");
		hc.encryptText();
		System.out.println(hc.getOutput());
		
		hc.setInput(hc.getOutput());
		hc.decryptText();
		System.out.println(hc.getOutput());

	}
	 
}
