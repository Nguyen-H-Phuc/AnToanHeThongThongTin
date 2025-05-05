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
	
	// Generates a random key matrix with a valid determinant
	public void genKey() {
	    Random random = new Random();
	    int det = 0;
	    do {
	        // Fill the matrix with random values between 0 and 49
	        for (int i = 0; i < matrix.length; i++) {
	            for (int j = 0; j < matrix[i].length; j++) {
	                matrix[i][j] = random.nextInt(50); // Assign random values to the matrix
	            }
	        }

	        // Calculate the determinant of the matrix
	        det = determinant2(matrix);
	    }
	    // Repeat until the determinant and the length of the alphabet are coprime (gcd == 1)
	    while (gcd(det, VIETNAMESE_ALPHABET.length()) != 1);
	}

	
	// Creates the inverse matrix based on the original matrix
	public void createInvMatrix() {
	    int m = VIETNAMESE_ALPHABET.length();
	    int det = determinant2(matrix); // Calculate the determinant of the original matrix
	    Integer detInv = modInverse(det, m); // Find the modular inverse of the determinant

	    // Check if the inverse determinant is valid
	    if (checkMatrix(detInv)) {
	        this.invMatrix = new int[2][2]; // Initialize the inverse matrix

	        // Calculate the inverse matrix elements
	        invMatrix[0][0] = (matrix[1][1] * detInv) % m;
	        invMatrix[0][1] = (-matrix[0][1] * detInv) % m;
	        invMatrix[1][0] = (-matrix[1][0] * detInv) % m;
	        invMatrix[1][1] = (matrix[0][0] * detInv) % m;

	        // Ensure all matrix values are positive by adjusting with modulo
	        for (int i = 0; i < 2; i++) {
	            for (int j = 0; j < 2; j++) {
	                invMatrix[i][j] = (invMatrix[i][j] + m) % m;
	            }
	        }
	    } else {
	        clearInvMatrix(); // Clear the inverse matrix if invalid
	    }
	}

	// Loads a key from a file
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
	        return "Đã load khoá thành công";
	    } 
	}
	
	// save key from a file
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
	
	// Encrypts the input text using Hill cipher
	public void encryptText() {
	    StringBuilder result = new StringBuilder(); // To store the encrypted result
	    String alphabet = VIETNAMESE_ALPHABET; // Vietnamese alphabet for encryption
	    int m = VIETNAMESE_ALPHABET.length(); // Length of the Vietnamese alphabet

	    // If the input length is odd, append a filler character to make it even
	    if (this.getInput().length() % 2 == 1) {
	        this.setInput(this.getInput() + "Xlk,.");
	    }

	    // Process the input two characters at a time
	    for (int i = 0; i < getInput().length(); i += 2) {
	        char c1 = getInput().charAt(i); // First character
	        char c2 = getInput().charAt(i + 1); // Second character
	        int p1 = alphabet.indexOf(c1); // Find the position of the first character in the alphabet
	        int p2 = alphabet.indexOf(c2); // Find the position of the second character in the alphabet

	        // Encrypt the characters if both are valid
	        if (p1 != -1 && p2 != -1) {
	            // Apply the Hill cipher encryption formula
	            int c1_enc = (p1 * matrix[0][0] + p2 * matrix[0][1]) % m;
	            int c2_enc = (p1 * matrix[1][0] + p2 * matrix[1][1]) % m;

	            // Append the encrypted characters to the result
	            result.append(alphabet.charAt(c1_enc));
	            result.append(alphabet.charAt(c2_enc));
	        } else {
	            // If characters are invalid, append them as they are
	            result.append(c1);
	            result.append(c2);
	        }
	    }

	    this.setOutput(result.toString()); // Set the encrypted output
	}


	
	// Decrypts the input text using Hill cipher
	public void decryptText() {
	    StringBuilder result = new StringBuilder(); // To store the decrypted result
	    String alphabet = VIETNAMESE_ALPHABET; // Vietnamese alphabet for decryption
	    int m = VIETNAMESE_ALPHABET.length(); // Length of the Vietnamese alphabet

	    // Process the input two characters at a time
	    for (int i = 0; i < getInput().length(); i += 2) {
	        if (i + 1 < getInput().length()) {
	            char c1 = getInput().charAt(i); // First character
	            char c2 = getInput().charAt(i + 1); // Second character
	            int p1 = alphabet.indexOf(c1); // Find the position of the first character in the alphabet
	            int p2 = alphabet.indexOf(c2); // Find the position of the second character in the alphabet

	            // Decrypt the characters if both are valid
	            if (p1 != -1 && p2 != -1) {
	                // Apply the Hill cipher decryption formula using the inverse matrix
	                int c1_dec = (p1 * invMatrix[0][0] + p2 * invMatrix[0][1]) % m;
	                int c2_dec = (p1 * invMatrix[1][0] + p2 * invMatrix[1][1]) % m;

	                // Append the decrypted characters to the result
	                result.append(alphabet.charAt(c1_dec));
	                result.append(alphabet.charAt(c2_dec));
	            } else {
	                // If characters are invalid, append them as they are
	                result.append(c1);
	                result.append(c2);
	            }
	        }
	    }

	    // Remove filler characters ("Xlk,.") if they are present at the end
	    if (result.length() >= 5 && result.substring(result.length() - 5).equals("Xlk,.")) {
	        result.delete(result.length() - 5, result.length());
	    }

	    this.setOutput(result.toString()); // Set the decrypted output
	}

	
	// Calculates the determinant of a 2x2 matrix modulo m
	public int determinant2(int[][] matrix) {
	    int m = VIETNAMESE_ALPHABET.length();
	    // Calculate the determinant using the formula for a 2x2 matrix and modulo m
	    int det = ((matrix[0][0] * matrix[1][1] - matrix[0][1] * matrix[1][0]) % m + m) % m;
	    return det;
	}

	// Finds the modular inverse of a number a modulo m
	public Integer modInverse(int a, int m) {
	    a = a % m; // Ensure a is within the range of [0, m)
	    for (int x = 1; x < m; x++) {
	        // Check if a * x is congruent to 1 modulo m
	        if ((a * x) % m == 1)
	            return x; // Return the modular inverse if found
	    }
	    return null; // Return null if no modular inverse exists
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
	 	 
}
