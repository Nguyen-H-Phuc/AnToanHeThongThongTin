package controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
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
				try {
					int keySize = view.getKeySize();
					model.genKey(view.getAlgorithm().substring(0, 3), keySize);
				} catch (NoSuchAlgorithmException e1) {
					e1.printStackTrace();
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
		ViewUtils.setFilePath(view.getFrame(), view.getBrowseDestBtn(), view.getDestFileTextField());
	}
	
	private void setPublicKey() {
	    String publicKey = this.view.getPublicKey();
	    this.model.clearPublicKey();
	    if (!publicKey.trim().isEmpty()) {
	        try {
	            this.model.setPublicKey(publicKey);
	        } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
	            this.view.showDialogMessage("Khoá không hợp lệ:\n" + e.getMessage(), "ERROR");
	        } catch (Exception e) {
	            this.view.showDialogMessage("Đã xảy ra lỗi không xác định khi xử lý khoá.", "ERROR");
	        }
	    } else {
	        this.view.showDialogMessage("Vui lòng nhập khoá công khai.", "ERROR");
	    }
	}
	
	private void setPrivateKey() {
	    String privateKey = this.view.getPrivateKey();
	    this.model.clearPrivateKey();
	    if (!privateKey.trim().isEmpty()) {
	         // xoá khóa cũ trước
	        try {
	            this.model.setPrivateKey(privateKey);
	        } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
	            this.view.showDialogMessage("Khoá không hợp lệ:\n" + e.getMessage(), "ERROR");
	        } catch (Exception e) {
	            this.view.showDialogMessage("Đã xảy ra lỗi không xác định khi xử lý khoá.", "ERROR");
	        }
	    } else {
	        this.view.showDialogMessage("Vui lòng nhập hoá riêng tư.", "ERROR");
	       
	    }
	}

	private void handleText(String mode) {
		String algorithm = this.view.getAlgorithm();
		String input = this.view.getInputText();
		String output;
		try {
			if (mode.equals("ENCRYPT")) {
				setPublicKey();
				output = this.model.encryptString(input, algorithm);
			} else {
				setPrivateKey();
				output = this.model.decryptString(input, algorithm);
			}
			this.view.setOutputText(output);
		} catch (InvalidKeyException e) {
			// thong bao loi trong setKey();
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

	private void handleFile(String mode) {
		String algorithm = this.view.getAlgorithm();
		String srcFile = view.getSrcFile();
		String destFile = view.getDestFile();

		if (srcFile == null || srcFile.isEmpty() || destFile == null || destFile.isEmpty()) {
			this.view.showDialogMessage("Vui lòng chọn file nguồn và file đích.", "WARNING");
			return;
		}

		if (mode.equals("ENCRYPT")) {
			model.genSecretKey("AES", 128);
			try {
				setPublicKey();
				this.model.encryptFile(srcFile, destFile, algorithm);
				this.view.showDialogMessage("Mã hoá file thành công. File được lưu tại "+ destFile, "INFO");
			} catch (InvalidKeyException e) {
				// thong bao loi trong setKey(); 
			} catch (Exception e) {
				this.view.showDialogMessage("Lỗi " + e.getMessage(), "ERROR");
			}
		} else {
			try {
				setPrivateKey();
				this.model.decryptFile(srcFile, destFile, algorithm);
				this.view.showDialogMessage("Giải mã file thành công. File được lưu tại "+ destFile, "INFO");
			} catch (InvalidKeyException e) {
				// thong bao loi trong setKey(); 
			} catch (Exception e) {
				this.view.showDialogMessage("Lỗi " + e.getMessage(), "ERROR");
			}
		}

	}

	private void saveKey(boolean savePulicKey) {
		if(savePulicKey) {
			String filePath = view.showFileDialog("Chọn file chứa khoá công khai", true);
			try {
				model.savePublicKey(view.getPublicKey(), filePath);
			} catch (IOException e) {
				view.showDialogMessage("Lỗi đọc file" + e.getMessage(), "ERROR");
			}
		}else {
			String filePath = view.showFileDialog("Chọn file chứa khoá riêng tư", true);
			try {
				model.savePrivateKey(view.getPrivateKey(), filePath);
			} catch (IOException e) {
				view.showDialogMessage("Lỗi đọc file" + e.getMessage(), "ERROR");
			}
		}
	}
	
	private void loadKey(boolean loadPublicKey) {
		if (loadPublicKey) {
			String filePath = this.view.showFileDialog("Chọn file để tải khoá công khai", false);
			if (!filePath.isEmpty()) {
				try {
					this.model.loadPublicKey(filePath);
					this.view.setPublicKey(this.model.getPublicKey());
				} catch (NoSuchAlgorithmException e) {
					this.view.showDialogMessage("Khoá không được hỗ trợ.", "ERROR");
					e.printStackTrace();
				} catch (InvalidKeySpecException| IllegalArgumentException e) {
					this.view.showDialogMessage("Khoá bị lỗi.", "ERROR");
				} catch (IOException e) {
					this.view.showDialogMessage("Lỗi khi đọc file: " + e.getMessage(), "ERROR");
				}
			}
		} else {
			String filePath = this.view.showFileDialog("Chọn file để tải khoá riêng tư", false);
			if (!filePath.isEmpty()) {
				try {
					this.model.loadPrivateKey(filePath);
					this.view.setPrivateKey(this.model.getPrivateKey());
				} catch (NoSuchAlgorithmException e) {
					this.view.showDialogMessage("Khoá không được hỗ trợ.", "ERROR");
					e.printStackTrace();
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
