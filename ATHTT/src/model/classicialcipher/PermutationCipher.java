package model.classicialcipher;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Random;

public class PermutationCipher extends ClasscialCipher {
	private int [] permutationTable;
	
	
	
	public int[] genKey(int keyLength) {
		int []permutationTable = new int[keyLength];
		Random random = new Random();
		int i = 0;
		while (i < keyLength) {
			int number = random.nextInt(keyLength);
			for (int j = 0; j <= i; j++) {
				if (j == i) {
					permutationTable[i] = number;
					i++;
				}
				if (permutationTable[j] == number) {
					break;
				}
			}
		}
		return permutationTable;
	}
	
	public String loadKey(String filePath) throws FileNotFoundException, IOException, NumberFormatException {
	    try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
	        String line = reader.readLine();
	        if (line != null) {
	            String[] parts = line.split(",");
	            permutationTable = new int[parts.length];
	            for (int i = 0; i < parts.length; i++) {
	                permutationTable[i] = Integer.parseInt(parts[i]);
	            }
	            return "Load key thành công";
	        } else {
	            return "File rỗng.";
	        }
	    } 
	}

	public String saveKey(String filePath) throws IOException {
		try (FileWriter writer = new FileWriter(filePath);) {
			for (int i = 0; i < permutationTable.length; i++) {
				writer.write(permutationTable[i] + (i < permutationTable.length - 1 ? "," : ""));
			}
			writer.close();
			return "Key đã được lưu tại: " + filePath;
		}
	}
		
	public void encryptText(int [] permutationTable) {
		StringBuilder result = new StringBuilder();
		int col = permutationTable.length;
		int row = (int) Math.ceil((double) getInput().length()/col);
		char[][] plainText = new char[row][col];
		int lengthText = 0;
		
		for(int i = 0; i < plainText.length; i++) {
			for(int j = 0; j<plainText[i].length; j++) {
				if(lengthText >= this.getInput().length()) {
					plainText[i][j] = '-';
				}
				else {
				plainText[i][j] = this.getInput().charAt(lengthText);
				lengthText++;
				}
			}
		}
		
		for(int i = 0; i < plainText.length; i++) {
			for(int j = 0; j < col; j++) {
				result.append(plainText[i][permutationTable[j]]);
			}
		}
		
		setOutput(result.toString());
	}
	
	public void decryptText(int [] permutationTable) {
		StringBuilder result = new StringBuilder();
		int col = permutationTable.length;
		int row = (int) Math.ceil((double) getInput().length() / col);
		char[][] cipherText = new char[row][col];
		int lengthText = 0;
		
		for (int i = 0; i < cipherText.length; i++) {
			for (int j = 0; j < cipherText[i].length; j++) {
				if (lengthText >= this.getInput().length()) {
					cipherText[i][j] = '-';
				} else {
					cipherText[i][j] = this.getInput().charAt(lengthText);
					lengthText++;
				}
			}
		}
		
		int []reverseArray = reverseArrayPermutation(permutationTable);
		for(int i = 0; i < cipherText.length; i++) {
			for(int j = 0; j < col; j++) {
				char c = cipherText[i][reverseArray[j]];
				if(c != '-') {
				result.append(c);
				}
			}
		}
		
		setOutput(result.toString());
	}
	
	public int[] reverseArrayPermutation (int [] permutationTable) {
		int [] reverseArray = new int[permutationTable.length];
		for (int i = 0; i< permutationTable.length; i++) {
			for(int j = 0; i< permutationTable.length; j++) {
				if(i == permutationTable[j]) {
			
			reverseArray[i] = j;
			break;
		}
			}}
		return reverseArray;
	}
	
	public int[] getPermutationTable() {
		return this.permutationTable;
	}
	
	public void setPermutationTable(int [] permutationTable) {
		this.permutationTable = permutationTable;
	}
	
	public static void main(String[] args) throws IOException {
		 PermutationCipher pc = new PermutationCipher();
		 pc.genKey(7);
		 System.out.println(pc.loadKey("D:\\test\\key.txt"));
	}
}
