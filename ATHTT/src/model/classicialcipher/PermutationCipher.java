package model.classicialcipher;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

public class PermutationCipher extends ClasscialCipher {
	private int [] permutationTable;
	
	// Generates a random permutation key of given length
	public int[] genKey(int keyLength) {
	    int[] permutationTable = new int[keyLength];
	    Random random = new Random();
	    int i = 0;

	    // Fill the permutation table with unique random numbers
	    while (i < keyLength) {
	        int number = random.nextInt(keyLength);

	        for (int j = 0; j <= i; j++) {
	            if (j == i) {
	                permutationTable[i] = number;
	                i++;
	            }
	            if (permutationTable[j] == number) {
	                break; // Skip if the number is already used
	            }
	        }
	    }

	    return permutationTable;
	}
	
	// Loads a key from a file
	public String loadKey(String filePath) throws FileNotFoundException, IOException, NumberFormatException {
	    try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
	        String line = reader.readLine();
	        
	        if (line != null) {
	            // Split the line by commas and parse into integers
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
	 // Save key from a file
	public String saveKey(String filePath) throws IOException {
		try (FileWriter writer = new FileWriter(filePath);) {
			for (int i = 0; i < permutationTable.length; i++) {
				writer.write(permutationTable[i] + (i < permutationTable.length - 1 ? "," : ""));
			}
			writer.close();
			return "Key đã được lưu tại: " + filePath;
		}
	}
		
	// Encrypts the input text 
	public void encryptText(int[] permutationTable) {
	    StringBuilder result = new StringBuilder();
	    int col = permutationTable.length;
	    String input = getInput();
	    int inputLength = input.length();

	    // Calculate the number of rows (fill with '-' if needed)
	    int row = (int) Math.ceil((double) inputLength / col);

	    // Loop through each row
	    for (int i = 0; i < row; i++) {
	        // Loop through each column based on the permutation table
	        for (int j = 0; j < col; j++) {
	            // Determine the actual index in the input
	            int indexInInput = i * col + permutationTable[j];

	            if (indexInInput >= inputLength) {
	                // If out of bounds fill with '-'
	                result.append('-');
	            } else {
	                // If within bounds take the corresponding character
	                result.append(input.charAt(indexInInput));
	            }
	        }
	    }

	    setOutput(result.toString());
	}

	public void decryptText(int[] permutationTable) {
	    StringBuilder result = new StringBuilder();
	    String input = getInput();
	    int col = permutationTable.length;
	    int inputLength = input.length();

	    // Calculate the number of rows (rounded up if needed)
	    int row = (int) Math.ceil((double) inputLength / col);

	    // Create a reversed permutation array
	    int[] reverseArray = reverseArrayPermutation(permutationTable);

	    // Loop over each row
	    for (int i = 0; i < row; i++) {
	        // Loop over each column
	        for (int j = 0; j < col; j++) {
	            // Calculate the index in the input string
	            int indexInInput = i * col + reverseArray[j];

	            // Check if we are within the actual input length
	            if (indexInInput < inputLength) {
	                char c = input.charAt(indexInInput);

	                // Ignore padding characters '-'
	                if (c != '-') {
	                    result.append(c);
	                }
	            }
	        }
	    }

	    setOutput(result.toString());
	}

	
	// Reverses the given permutation table
	public int[] reverseArrayPermutation(int[] permutationTable) {
	    int[] reverseArray = new int[permutationTable.length];
	    
	    for (int i = 0; i < permutationTable.length; i++) {
	        for (int j = 0; j < permutationTable.length; j++) {
	            if (i == permutationTable[j]) {
	                // Store the reversed position
	                reverseArray[i] = j;
	                break;
	            }
	        }
	    }
	    
	    return reverseArray;
	}
	
	public int[] getPermutationTable() {
		return this.permutationTable;
	}
	
	public void setPermutationTable(int [] permutationTable) {
		this.permutationTable = permutationTable;
	}
	
	public boolean hasDuplicates(int[] array) {
	    Set<Integer> seen = new HashSet<>();
	    for (int num : array) {
	        if (!seen.add(num)) {
	            return true; // duplicate
	        }
	    }
	    return false; // not duplicate
	}

	// Checks if the array has any value greater than the array length
	public boolean hasValueGreaterThanIndex(int[] array) {
	    for (int i = 0; i < array.length; i++) {
	        if (array[i] > array.length) {
	            return true; // Found a value greater than length
	        }
	    }
	    return false; // No such value found
	}


}
