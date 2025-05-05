package controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.spec.InvalidKeySpecException;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

import model.RSA;
import utils.ViewUtils;
import view.RSAView;

public class RSAController {
	private RSA model;
	private RSAView view;

	public RSAController() {
		super();
		
		this.model = new RSA();
		this.view = new RSAView();
		this.view.getGenKey().addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				String instance = view.getAlgorithm().substring(0, 3);
				try {
					int keySize = view.getKeySize();
					model.genKey(instance, keySize);
				} catch (NoSuchAlgorithmException | NoSuchProviderException e1 ) {
					view.showDialogMessage("Không hỗ trợ tạo khoá với " + instance, "ERROR");
				}
				view.setPublicKey(model.getPublicKey());
				view.setPrivateKey(model.getPrivateKey());
			}
		});

		this.view.getSaveKey().addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				saveKey(true);
			}
		});

		this.view.getLoadKey().addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				loadKey(true);

			}
		});
		
		this.view.getSavePrivateKey().addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				saveKey(false);
			}
		});
		
		this.view.getLoadPrivateKey().addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				loadKey(false);

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
	
	// Sets the public key by retrieving it from the view and processing it
	private void setPublicKey() {
	    String publicKey = this.view.getPublicKey(); // Get the public key from the view
	    this.model.clearPublicKey(); // Clear any previous public key

	    // Check if the public key is not empty
	    if (!publicKey.trim().isEmpty()) {
	        try {
	            this.model.setPublicKey(publicKey); // Set the public key in the model
	        } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
	            this.view.showDialogMessage("Khoá không hợp lệ:\n" + e.getMessage(), "ERROR");
	        } catch (Exception e) {
	            this.view.showDialogMessage("Đã xảy ra lỗi không xác định khi xử lý khoá.", "ERROR");
	        }
	    } else {
	        // Show error if the public key input is empty
	        this.view.showDialogMessage("Vui lòng nhập khoá công khai.", "ERROR");
	    }
	}

	// Sets the private key by retrieving it from the view and processing it
	private void setPrivateKey() {
	    String privateKey = this.view.getPrivateKey(); // Get the private key from the view
	    this.model.clearPrivateKey(); // Clear any previous private key

	    // Check if the private key is not empty
	    if (!privateKey.trim().isEmpty()) {
	        try {
	            this.model.setPrivateKey(privateKey); // Set the private key in the model
	        } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
	            this.view.showDialogMessage("Khoá không hợp lệ:\n" + e.getMessage(), "ERROR");
	        } catch (Exception e) {
	            this.view.showDialogMessage("Đã xảy ra lỗi không xác định khi xử lý khoá.", "ERROR");
	        }
	    } else {
	        // Show error if the private key input is empty
	        this.view.showDialogMessage("Vui lòng nhập hoá riêng tư.", "ERROR");
	    }
	}

	// Handles text encryption or decryption based on the mode (ENCRYPT or DECRYPT)
	private void handleText(String mode) {
	    String algorithm = this.view.getAlgorithm(); // Get the selected algorithm from the view
	    String input = this.view.getInputText(); // Get the input text from the view
	    String output;

	    try {
	        // If mode is ENCRYPT, use the public key to encrypt the text
	        if (mode.equals("ENCRYPT")) {
	            setPublicKey(); // Set the public key
	            output = this.model.encryptString(input, algorithm);
	        } else {
	            // If mode is DECRYPT, use the private key to decrypt the text
	            setPrivateKey(); // Set the private key
	            output = this.model.decryptString(input, algorithm);
	        }

	        // Set the output text in the view
	        this.view.setOutputText(output);
	    } //handle error 
	     catch (NoSuchProviderException e) {
			this.view.showDialogMessage("Không hỗ trợ thuật toán:" +algorithm , "ERROR");
		} catch (InvalidKeyException e) {
	        // Handle error if key is invalid (from setKey())
	        this.view.setOutputText(""); 
	    } catch (NoSuchAlgorithmException e) {
	        this.view.showDialogMessage("Thuật toán không tồn tại: " + e.getMessage(), "ERROR");
	        this.view.setOutputText(""); 
	    } catch (NoSuchPaddingException e) {
	        this.view.showDialogMessage("Padding không hợp lệ: " + e.getMessage(), "ERROR");
	        this.view.setOutputText(""); 
	    } catch (IllegalBlockSizeException e) {
	        this.view.showDialogMessage("Kích thước khối sai: " + e.getMessage(), "ERROR");
	        this.view.setOutputText("");
	    } catch (BadPaddingException e) {
	        this.view.showDialogMessage("Sai padding hoặc dữ liệu không hợp lệ: " + e.getMessage(), "ERROR");
	        this.view.setOutputText("");
	    } catch (UnsupportedEncodingException e) {
	        this.view.showDialogMessage("Encoding không hỗ trợ: " + e.getMessage(), "ERROR");
	        this.view.setOutputText("");
	    } catch(IllegalArgumentException e){
	        this.view.showDialogMessage("Dữ liệu đầu vào không phải Base64 hợp lệ. " + e.getMessage(), "ERROR");
	        this.view.setOutputText("");
	    }
	}

	// Handles file encryption or decryption based on the mode (ENCRYPT or DECRYPT)
	private void handleFile(String mode) {
	    String algorithm = this.view.getAlgorithm();
	    String srcFile = view.getSrcFile();
	    String destFile = view.getDestFile();

	    // Check if source or destination file is empty or null
	    if (srcFile == null || srcFile.isEmpty() || destFile == null || destFile.isEmpty()) {
	        this.view.showDialogMessage("Vui lòng chọn file nguồn và file đích.", "WARNING");
	        return;
	    }

	    if (mode.equals("ENCRYPT")) {
	        model.genSecretKey("AES", 128); // Generate a secret key for encryption (AES, 128-bit)
	        try {
	            setPublicKey(); // Set the public key for encryption
	            this.model.encryptFile(srcFile, destFile, algorithm); // Encrypt the file
	            this.view.showDialogMessage("Mã hoá file thành công. File được lưu tại "+ destFile, "INFO"); // Show success message
	        } catch (InvalidKeyException e) {
	            // Handle error in setKey()
	        } catch (Exception e) {
	            this.view.showDialogMessage("Lỗi " + e.getMessage(), "ERROR"); // Show generic error message
	        }
	    } else {
	        try {
	            setPrivateKey(); // Set the private key for decryption
	            this.model.decryptFile(srcFile, destFile, algorithm); // Decrypt the file
	            this.view.showDialogMessage("Giải mã file thành công. File được lưu tại "+ destFile, "INFO"); // Show success message
	        } catch (InvalidKeyException e) {
	            // Handle error in setKey()
	        } catch (Exception e) {
	            this.view.showDialogMessage("Lỗi " + e.getMessage(), "ERROR"); // Show generic error message
	        }
	    }
	}

	// Saves the public or private key based on the provided boolean flag
	private void saveKey(boolean savePulicKey) {
	    if(savePulicKey) {
	        // Show file dialog to select a path for saving the public key
	        String filePath = view.showFileDialog("Chọn file chứa khoá công khai", true);
	        try {
	            // Save the public key to the selected file
	            model.savePublicKey(view.getPublicKey(), filePath);
	        } catch (IOException e) {
	            // Handle exception if there is an error reading the file
	            view.showDialogMessage("Lỗi đọc file" + e.getMessage(), "ERROR");
	        }
	    } else {
	        // Show file dialog to select a path for saving the private key
	        String filePath = view.showFileDialog("Chọn file chứa khoá riêng tư", true);
	        try {
	            // Save the private key to the selected file
	            model.savePrivateKey(view.getPrivateKey(), filePath);
	        } catch (IOException e) {
	            // Handle exception if there is an error reading the file
	            view.showDialogMessage("Lỗi đọc file" + e.getMessage(), "ERROR");
	        }
	    }
	}

	// Loads the public or private key based on the provided boolean flag
	private void loadKey(boolean loadPublicKey) {
	    if (loadPublicKey) {
	        // Show file dialog to select a file for loading the public key
	        String filePath = this.view.showFileDialog("Chọn file để tải khoá công khai", false);
	        if (!filePath.isEmpty()) {
	            try {
	                // Load the public key from the selected file
	                this.model.loadPublicKey(filePath);
	                this.view.setPublicKey(this.model.getPublicKey());
	            } // handle exception
				  catch (NoSuchAlgorithmException e) {
					this.view.showDialogMessage("Khoá không được hỗ trợ.", "ERROR");
				} catch (InvalidKeySpecException | IllegalArgumentException e) {
					this.view.showDialogMessage("Khoá bị lỗi.", "ERROR");
				} catch (IOException e) {
					this.view.showDialogMessage("Lỗi khi đọc file: " + e.getMessage(), "ERROR");
				}
	        }
	    } else {
	        // Show file dialog to select a file for loading the private key
	        String filePath = this.view.showFileDialog("Chọn file để tải khoá riêng tư", false);
	        if (!filePath.isEmpty()) {
	            try {
	                // Load the private key from the selected file
	                this.model.loadPrivateKey(filePath);
	                this.view.setPrivateKey(this.model.getPrivateKey());
	            }// handle exception
	              catch (NoSuchAlgorithmException e) {
	                this.view.showDialogMessage("Khoá không được hỗ trợ.", "ERROR");
	            } catch (InvalidKeySpecException | IllegalArgumentException e) {
	                this.view.showDialogMessage("Khoá bị lỗi.", "ERROR");
	            } catch (IOException e) {
	                this.view.showDialogMessage("Lỗi khi đọc file: " + e.getMessage(), "ERROR");
	            }
	        }
	    }
	}
	
	public static void main(String[] args) {
		new RSAController();
	}

}
