package controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.swing.JOptionPane;

import model.RSA;
import view.RSAView;

public class RSAController {
	private RSA model;
	private RSAView view;

	public RSAController() {
		this.model = new RSA();
		this.view = new RSAView();

		this.view.getGenKey().addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					model.genKey(view.getAlgorithm().substring(0, 3));
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
				// TODO Auto-generated method stub

			}
		});

		this.view.getLoadKey().addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub

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
				try {
					handleFile("ENCRYPT");
				} catch (Exception e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}

			}
		});

		this.view.getDecryptFileBtn().addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					handleFile("DECRYPT");
				} catch (Exception e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}

			}
		});
	}
	
	private void setKey() {
		String privateKey = this.view.getPrivateKey();
		String publicKey = this.view.getPublicKey();
		if (!privateKey.isEmpty() && !publicKey.isEmpty()) {
			this.model.setPublicKey(publicKey);
			this.model.setPrivateKey(privateKey);
		}
	}

	private void handleText(String mode) {
		setKey();

		String algorithm = this.view.getAlgorithm();
		String input = this.view.getInputText();
		String output;
		try {
			if (mode.equals("ENCRYPT")) {
				output = this.model.encryptString(input, algorithm);
			} else {
				output = this.model.decryptString(input, algorithm);
			}
			this.view.setOutputText(output);
		} catch (InvalidKeyException e) {
			if (this.view.getPublicKey().isEmpty() || this.view.getPrivateKey().isEmpty()) {
				this.view.showDialogMessage(
						"Vui lòng tạo khoá công khai và khoá riêng tư trước khi mã hoá hoặc giải mã.", "WARNING");
			} else {
				JOptionPane.showMessageDialog(null, "Lỗi khóa không hợp lệ: " + e.getMessage());
			}
		} catch (NoSuchAlgorithmException e) {
			this.view.showDialogMessage("Thuật toán không tồn tại: " + e.getMessage(), "ERROR");
		} catch (NoSuchPaddingException e) {
			this.view.showDialogMessage("Padding không hợp lệ: " + e.getMessage(), "ERROR");
		} catch (IllegalBlockSizeException e) {
			this.view.showDialogMessage("Kích thước khối sai: " + e.getMessage(), "ERROR");
		} catch (BadPaddingException e) {
			this.view.showDialogMessage("Sai padding hoặc dữ liệu không hợp lệ: " + e.getMessage(), "ERROR");
		} catch (UnsupportedEncodingException e) {
			this.view.showDialogMessage("Encoding không hỗ trợ: " + e.getMessage(), "ERROR");
		}
	}

	private void handleFile(String mode) throws Exception {
		setKey();
		String algorithm = this.view.getAlgorithm();
		String srcFile = view.getSrcFile();
		String destFile = view.getDestFile();
		if(mode.equals("ENCRYPT")) {
			model.genSecretKey("AES", 128);
			try {
				this.model.encryptFile(srcFile, destFile);
			} catch (InvalidKeyException | NoSuchAlgorithmException | NoSuchPaddingException | IllegalBlockSizeException
					| BadPaddingException | IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} else {try {
			this.model.decryptFile(srcFile, destFile);
		} catch (InvalidKeyException | NoSuchAlgorithmException | NoSuchPaddingException | IllegalBlockSizeException
				| BadPaddingException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
			}
	}

}
