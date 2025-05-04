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
		
	public void encryptText(int[] permutationTable) {
	    StringBuilder result = new StringBuilder();
	    int col = permutationTable.length;
	    String input = getInput();
	    int inputLength = input.length();

	    // Tính số hàng cần thiết (có thể dư, điền thêm ký tự '-')
	    int row = (int) Math.ceil((double) inputLength / col);

	    // Duyệt từng hàng
	    for (int i = 0; i < row; i++) {
	        // Duyệt từng cột dựa trên permutationTable
	        for (int j = 0; j < col; j++) {
	            // Xác định chỉ số thực trong input: hàng * số cột + cột thực tế
	            int indexInInput = i * col + permutationTable[j];

	            if (indexInInput >= inputLength) {
	                // Nếu vượt ngoài input → điền ký tự '-'
	                result.append('-');
	            } else {
	                // Nếu trong phạm vi → lấy ký tự tương ứng
	                result.append(input.charAt(indexInInput));
	            }
	        }
	    }

	    // Gán kết quả mã hóa ra output
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
	        // Loop over each column (using the reversed permutation order)
	        for (int j = 0; j < col; j++) {
	            // Calculate the index in the input string
	            int indexInInput = i * col + reverseArray[j];

	            // Check if we are within the actual input length
	            if (indexInInput < inputLength) {
	                char c = input.charAt(indexInInput);

	                // Ignore padding characters (e.g., '-')
	                if (c != '-') {
	                    result.append(c);
	                }
	            }
	        }
	    }

	    // Set the decrypted output
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
	
	public boolean hasDuplicates(int[] array) {
	    Set<Integer> seen = new HashSet<>();
	    for (int num : array) {
	        if (!seen.add(num)) {
	            return true; // Có trùng
	        }
	    }
	    return false; // Không trùng
	}

	public boolean hasValueGreaterThanIndex(int[] array) {
	    for (int i = 0; i < array.length; i++) {
	        if (array[i] > array.length) {
	            return true; // Có phần tử lớn hơn index
	        }
	    }
	    return false; // Không có
	}

}
