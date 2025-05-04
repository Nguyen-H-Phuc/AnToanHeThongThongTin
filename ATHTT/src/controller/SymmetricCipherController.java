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
		ViewUtils.setFilePath(view.getFrame(), view.getBrowseDestBtn(), view.getDestFileTextField());
	}

	private void handleText(String handleText) {
		String alogrithm = view.getAlgorithm();
		String charSet = view.getCharset();
		String padding = view.getPadding();
		String key = view.getKey();
		String mode = view.getMode();
		int blockSize = Integer.parseInt(view.getBlockSize()) / 8;
		String input = view.getInputText();
		String output;
		try {
			model.clearKey();
			model.setKey(key, alogrithm);
			if (handleText.equals("ENCRYPT")) {
				output = model.encryptString(input, alogrithm, mode, padding, blockSize, charSet);
			} else {
				output = model.decryptString(input, alogrithm, mode, padding, blockSize, charSet);
			}
			view.setOutputText(output);
		} catch (IllegalArgumentException e) {
			view.showDialogMessage("Khóa không hợp lệ. ", "ERROR");
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
			view.showDialogMessage("Lỗi Không xác định: " +e.getMessage(), "ERROR");
		}
	}

	private void handleFile(String mode) {
		String srcFile = this.view.getSrcFile();
		String destFile = this.view.getDestFile();
		String alogrithm = view.getAlgorithm();
		String modeCipher = view.getMode();
		String padding = view.getPadding();
		String key = view.getKey();
		String instance = alogrithm + '/' + modeCipher + '/' + padding;
		if (!srcFile.trim().isEmpty() && !destFile.trim().isEmpty()) {
			try {
				model.clearKey();
				model.setKey(key, alogrithm);
				if (mode.equals("ENCRYPT")) {
					this.model.encryptFile(srcFile, destFile, instance, modeCipher);
				} else {
					this.model.decryptFile(srcFile, destFile, instance, modeCipher);
				}
			} catch (IllegalArgumentException e) {
				view.showDialogMessage("Khóa mã hóa không hợp lệ. ", "ERROR");
			} catch (InvalidKeyException e) {
				view.showDialogMessage("Khóa mã hóa không hợp lệ. " + e.getMessage(), "ERROR");
			} catch (NoSuchAlgorithmException e) {
				view.showDialogMessage("Thuật toán mã hóa không tồn tại. " + e.getMessage(), "ERROR");
			} catch (NoSuchProviderException e) {
				view.showDialogMessage("Nhà cung cấp mã hóa không tồn tại. " + e.getMessage(), "ERROR");
			} catch (NoSuchPaddingException e) {
				view.showDialogMessage("Cơ chế Padding không được hỗ trợ. " + e.getMessage(), "ERROR");
			} catch (FileNotFoundException e) {
				if (new File(srcFile).exists())
					view.showDialogMessage("Không tìm thấy file: " + srcFile, "ERROR");
				if (new File(destFile).exists())
					view.showDialogMessage("Không tìm thấy file: " + destFile, "ERROR");
			} catch (InvalidAlgorithmParameterException e) {
				view.showDialogMessage("Tham số thuật toán không hợp lệ. " + e.getMessage(), "ERROR");
			} catch (IOException e) {
				view.showDialogMessage("Lỗi không xác định: " + e.getMessage(), "ERROR");
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
			try {
				this.model.saveKey(filePath, algorithm, mode, padding, charset, keySize, blockSize);
				this.view.showDialogMessage("Lưu khoá thành công.", "INFO");
			} catch (IOException e) {
				this.view.showDialogMessage("Lỗi khi lưu khoá: " + e.getMessage(), "ERROR");
			}
		}
	}
	
	private void loadKey() {
		String filePath = this.view.showFileDialog("Chọn file", false);
		
		if (!filePath.isEmpty()) {
			try {
				this.model.loadKey(filePath);
			} catch (IOException e) {
				this.view.showDialogMessage("Lỗi khi đọc khoá: " + e.getMessage(), "ERROR");
			}
			String algorithm = this.model.getAlgorithm();
			if (algorithm!= null && !algorithm.isEmpty()) {
				this.model.clearKey();
				String mode = this.model.getMode();
				String padding = this.model.getPadding();
				String charset = this.model.getCharset();
				String keySize = this.model.getKeySize();
				String blockSize = this.model.getBlockSize();
				String key = this.model.getEncodedKey();

				this.view.setAlgorithm(algorithm);
				this.view.setCharSet(charset);
				this.view.setMode(algorithm, mode);
				this.view.setPadding(algorithm, padding);
				this.view.setKeySize(algorithm, keySize);
				this.view.setBlockSize(algorithm, blockSize);
				this.view.setKey(key);
				this.model.clearLoadKey();
				this.view.showDialogMessage("Tải khoá thành công.", "INFO");
			} else {
				this.view.showDialogMessage("File chứa khoá không đúng định dạng. Không thể tải khoá.", "ERROR");
			}
		}
	}

	public static void main(String[] args) {
		new SymmetricCipherController();
	}
}
