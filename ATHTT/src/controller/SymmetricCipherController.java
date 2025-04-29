package controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.UnsupportedEncodingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

import model.Des;
import view.SymmetricCipherView;

public class SymmetricCipherController {
	Des model;
	SymmetricCipherView view;

	public SymmetricCipherController() {
		model = new Des();
		view = new SymmetricCipherView();

		view.getEncryptTextBtn().addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				handleText("ENCRYPT");
			}
		});

		view.getDecryptTextBtn().addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				handleText("DECRYPT");
			}
		});
		
		view.getGenKey().addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				createKey();
				
			}
		});
	}
	
	private void handleText(String handleText) {
		String alogrithm = view.getAlgorithm();
		String charSet = view.getCharset();
		String padding = view.getPadding();
		String key = view.getKey();
		String mode = view.getMode();
		int keySize = Integer.parseInt(view.getKeySize());
		int blockSize = Integer.parseInt(view.getBlockSize())/8;
		String input = view.getInputText();
		model.setKey(key, alogrithm, keySize);
		String output;		
			try {
				if(handleText.equals("ENCRYPT")) {
				output = model.encryptString(input, alogrithm, mode, padding, blockSize, charSet);
				}else {
					output = model.decryptString(input, alogrithm, mode, padding, blockSize, charSet);
				}
				view.setOutputText(output);
			} catch (InvalidKeyException e) {
			    view.showDialogMessage("Khóa mã hóa không hợp lệ."+e.getMessage(), "ERROR");
			} catch (NoSuchAlgorithmException e) {
			    view.showDialogMessage("Thuật toán mã hóa không tồn tại."+e.getMessage(), "ERROR");
			} catch (NoSuchProviderException e) {
			    view.showDialogMessage("Nhà cung cấp mã hóa không tồn tại."+e.getMessage(), "ERROR");
			} catch (NoSuchPaddingException e) {
			    view.showDialogMessage("Cơ chế Padding không được hỗ trợ."+e.getMessage(), "ERROR");
			} catch (InvalidAlgorithmParameterException e) {
			    view.showDialogMessage("Tham số thuật toán không hợp lệ."+e.getMessage(), "ERROR");
			} catch (IllegalBlockSizeException e) {
			    view.showDialogMessage("Kích thước khối dữ liệu không hợp lệ."+e.getMessage(), "ERROR");
			} catch (BadPaddingException e) {
			    view.showDialogMessage("Dữ liệu Padding không hợp lệ."+e.getMessage(), "ERROR");
			} catch (UnsupportedEncodingException e) {
			    view.showDialogMessage("Bảng mã ký tự không được hỗ trợ."+e.getMessage(), "ERROR");
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
	
	public static void main(String[] args) {
		new SymmetricCipherController();
	}
}
