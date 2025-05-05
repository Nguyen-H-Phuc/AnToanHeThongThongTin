package controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

import model.Des;
import utils.ViewUtils;
import view.SymmetricCipherView;

public class SymmetricCipherController {
	Des model;
	SymmetricCipherView view;

	public SymmetricCipherController() {
		model = new Des();
		view = new SymmetricCipherView();
		this.view.getGenKey().addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				createKey();

			}
		});

		this.view.getSaveKey().addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				saveKey();

			}
		});

		this.view.getLoadKey().addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				loadKey();

			}
		});

		this.view.getEncryptTextBtn().addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				handleText("ENCRYPT");
			}
		});

		this.view.getDecryptTextBtn().addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				handleText("DECRYPT");
			}
		});

		this.view.getEncryptFileBtn().addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				handleFile("ENCRYPT");

			}
		});

		this.view.getDecryptFileBtn().addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				handleFile("DECRYPT");

			}
		});

		ViewUtils.setupClearButton(view.getClearTextPanelBtn(), view.getInputTextArea(), view.getOutputTextArea());
		ViewUtils.setupSwapButton(view.getSwapBtn(), view.getInputTextArea(), view.getOutputTextArea());
		ViewUtils.setSaveResultBtn(view.getSaveResultBtn(), view.getOutputTextArea(), view.getFrame());
		ViewUtils.setFilePath(view.getFrame(), view.getBrowseSrcBtn(), view.getSrcFileTextField());
		ViewUtils.setSaveFilePath(view.getFrame(), view.getBrowseDestBtn(), view.getDestFileTextField());
	}

	// Handles encryption or decryption based on the operation
	private void handleText(String handleText) {
	    // Retrieve user inputs from the view
	    String algorithm = view.getAlgorithm();
	    String charSet = view.getCharset();
	    String padding = view.getPadding();
	    String key = view.getKey();
	    String mode = view.getMode();
	    int blockSize = Integer.parseInt(view.getBlockSize()) / 8; // Convert block size from bits to bytes
	    String input = view.getInputText();
	    String output;

	    try {
	        model.clearKey(); // Clear previous key
	        model.setKey(key, algorithm); // Set new key in the model

	        // Perform encryption or decryption based on the operation
	        if (handleText.equals("ENCRYPT")) {
	            output = model.encryptString(input, algorithm, mode, padding, blockSize, charSet);
	        } else {
	            output = model.decryptString(input, algorithm, mode, padding, blockSize, charSet);
	        }

	        // Set the output text in the view
	        view.setOutputText(output);

	    // Handle various exceptions and display error messages
	    } catch (IllegalArgumentException e) {
	        view.showDialogMessage("Khóa không hợp lệ. ", "ERROR"); // Invalid key error
	        this.view.setOutputText("");
	    } catch (InvalidKeyException e) {
	        this.view.setOutputText("");
	        view.showDialogMessage("Khóa không hợp lệ. " + e.getMessage(), "ERROR");
	    } catch (NoSuchAlgorithmException e) {
	        this.view.setOutputText("");
	        view.showDialogMessage("Thuật toán mã hóa không tồn tại. " + e.getMessage(), "ERROR");
	    } catch (NoSuchProviderException e) {
	        this.view.setOutputText("");
	        view.showDialogMessage("Nhà cung cấp mã hóa không tồn tại. " + e.getMessage(), "ERROR");
	    } catch (NoSuchPaddingException e) {
	        this.view.setOutputText("");
	        view.showDialogMessage("Cơ chế Padding không được hỗ trợ. " + e.getMessage(), "ERROR");
	    } catch (InvalidAlgorithmParameterException e) {
	        this.view.setOutputText("");
	        view.showDialogMessage("Tham số thuật toán không hợp lệ. " + e.getMessage(), "ERROR");
	    } catch (IllegalBlockSizeException e) {
	        this.view.setOutputText("");
	        view.showDialogMessage("Kích thước khối dữ liệu không hợp lệ. " + e.getMessage(), "ERROR");
	    } catch (BadPaddingException e) {
	        this.view.setOutputText("");
	        view.showDialogMessage("Dữ liệu Padding không hợp lệ. " + e.getMessage(), "ERROR");
	    } catch (UnsupportedEncodingException e) {
	        this.view.setOutputText("");
	        view.showDialogMessage("Bảng mã ký tự không được hỗ trợ. " + e.getMessage(), "ERROR");
	    } catch(Exception e) {
	        this.view.setOutputText("");
	        view.showDialogMessage("Lỗi Không xác định: " + e.getMessage(), "ERROR"); // Catch any other unexpected errors
	    }
	}


	// Handles file encryption or decryption
	private void handleFile(String mode) {
	    // Retrieve user inputs from the view
	    String srcFile = this.view.getSrcFile();
	    String destFile = this.view.getDestFile();
	    String algorithm = view.getAlgorithm();
	    String modeCipher = view.getMode();
	    String padding = view.getPadding();
	    String key = view.getKey();
	    String instance = algorithm + '/' + modeCipher + '/' + padding;

	    // Check if both source and destination file paths are provided
	    if (!srcFile.trim().isEmpty() && !destFile.trim().isEmpty()) {
	        try {
	            model.clearKey(); // Clear any previous keys
	            model.setKey(key, algorithm); // Set the new key in the model

	            // Perform encryption or decryption based on the mode
	            if (mode.equals("ENCRYPT")) {
	                this.model.encryptFile(srcFile, destFile, instance, modeCipher); // Encrypt the file
	                this.view.showDialogMessage("Mã hoá file thành công. File được lưu tại: " + destFile, "INFO");
	            } else {
	                this.model.decryptFile(srcFile, destFile, instance, modeCipher); // Decrypt the file
	                this.view.showDialogMessage("Giải mã file thành công. File được lưu tại: " + destFile, "INFO");
	            }
	        // Handle various exceptions and display appropriate error messages
	        } catch (IllegalArgumentException e) {
	            view.showDialogMessage("Khóa mã hóa không hợp lệ. ", "ERROR"); // Invalid key error
	        } catch (InvalidKeyException e) {
	            view.showDialogMessage("Khóa mã hóa không hợp lệ. " + e.getMessage(), "ERROR");
	        } catch (NoSuchAlgorithmException e) {
	            view.showDialogMessage("Thuật toán mã hóa không tồn tại. " + e.getMessage(), "ERROR");
	        } catch (NoSuchProviderException e) {
	            view.showDialogMessage("Nhà cung cấp mã hóa không tồn tại. " + e.getMessage(), "ERROR");
	        } catch (NoSuchPaddingException e) {
	            view.showDialogMessage("Cơ chế Padding không được hỗ trợ. " + e.getMessage(), "ERROR");
	        } catch (FileNotFoundException e) {
	            // Check if the source or destination file does not exist
	            if (new File(srcFile).exists())
	                view.showDialogMessage("Không tìm thấy file: " + srcFile, "ERROR");
	            if (new File(destFile).exists())
	                view.showDialogMessage("Không tìm thấy file: " + destFile, "ERROR");
	        } catch (InvalidAlgorithmParameterException e) {
	            view.showDialogMessage("Tham số thuật toán không hợp lệ. " + e.getMessage(), "ERROR");
	        } catch (IOException e) {
	            view.showDialogMessage("Lỗi không xác định: " + e.getMessage(), "ERROR"); // Unknown error
	        }
	    }
	}

	private void createKey() {
		String alogrithm = view.getAlgorithm();
		int keySize = Integer.parseInt(view.getKeySize());
		try {
			model.genKey(alogrithm, keySize);
			view.setKey(model.getKey());
		} catch (NoSuchAlgorithmException | NoSuchProviderException e2) {
			this.view.showDialogMessage("Tool không hỗ trợ thuật toán " + alogrithm, "ERROR");
		}
	}
	
	private void saveKey() {
		String filePath = this.view.showFileDialog("Chọn file", true);
		if (!filePath.isEmpty()) {
			String algorithm = this.view.getAlgorithm();
			String mode = this.view.getMode();
			String padding = this.view.getPadding();
			String charset = this.view.getCharset();
			String keySize = this.view.getKeySize();
			String blockSize = this.view.getBlockSize();
			String keyEncoded = this.view.getKey();
			try {
				this.model.saveKey(filePath, algorithm, mode, padding, charset, keySize, blockSize, keyEncoded);
				this.view.showDialogMessage("Lưu khoá thành công.", "INFO");
			} catch (IOException e) {
				this.view.showDialogMessage("Lỗi khi lưu khoá: " + e.getMessage(), "ERROR");
			}
		}
	}
	
	// Loads the encryption key from a selected file
	private void loadKey() {
	    // Show file dialog for selecting a key file
	    String filePath = this.view.showFileDialog("Chọn file", false);

	    // Proceed if the file path is not empty
	    if (!filePath.isEmpty()) {
	        try {
	            this.model.loadKey(filePath); // Load the key from the file
	        } catch (IOException e) {
	            // Show an error if there's an issue loading the key
	            this.view.showDialogMessage("Lỗi khi đọc khoá: " + e.getMessage(), "ERROR");
	        }
	        
	        // Retrieve key algorithm details if available
	        String algorithm = this.model.getAlgorithm();
	        
	        // If the algorithm is valid, load the necessary data to the view
	        if (algorithm != null && !algorithm.isEmpty()) {
	            this.model.clearKey(); // Clear any existing key information

	            // Retrieve additional algorithm-related details
	            String mode = this.model.getMode();
	            String padding = this.model.getPadding();
	            String charset = this.model.getCharset();
	            String keySize = this.model.getKeySize();
	            String blockSize = this.model.getBlockSize();
	            String key = this.model.getEncodedKey();

	            // Set the values in the view
	            this.view.setAlgorithm(algorithm);
	            this.view.setCharSet(charset);
	            this.view.setMode(algorithm, mode);
	            this.view.setPadding(padding);
	            this.view.setKeySize(algorithm, keySize);
	            this.view.setBlockSize(algorithm, blockSize);
	            this.view.setKey(key);

	            this.model.clearLoadKey(); // Clear any additional load key data
	            this.view.showDialogMessage("Tải khoá thành công.", "INFO"); // Inform the user of success
	        } else {
	            // If the key file format is incorrect, show an error
	            this.view.showDialogMessage("File chứa khoá không đúng định dạng. Không thể tải khoá.", "ERROR");
	        }
	    }
	}

	public static void main(String[] args) {
		new SymmetricCipherController();
	}
}
