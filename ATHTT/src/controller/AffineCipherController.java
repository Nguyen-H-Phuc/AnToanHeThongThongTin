package controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileNotFoundException;
import java.io.IOException;

import javax.swing.JOptionPane;

import model.classicialcipher.AffineCipher;
import utils.ViewUtils;
import view.AffineCipherView;

public class AffineCipherController {
	private AffineCipher model;
	private AffineCipherView view;
	
	public AffineCipherController(AffineCipher model, AffineCipherView view) {
		this.model = model;
		this.view = view;
		
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
		
		this.view.getGenKey().addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				genKey();
			}
		});
		
		this.view.getLoadKey().addActionListener(new ActionListener() {	
			@Override
			public void actionPerformed(ActionEvent e) {
				loadKey();
			}
		});
		
		this.view.getSaveKey().addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				saveKey();
			}
		});
		
		ViewUtils.setupClearButton(view.getClearTextPanelBtn(), view.getInputTextArea(), view.getOutputTextArea());
		ViewUtils.setupSwapButton(view.getSwapBtn(), view.getInputTextArea(), view.getOutputTextArea());
		ViewUtils.setSaveResultBtn(view.getSaveResultBtn(), view.getOutputTextArea(), view.getFrame());
	}
	
	// Handles text encryption and decryption based on the mode (ENCRYPT/DECRYPT)
	public void handleText(String mode) {
	    int a = this.view.getValueSpinner1();
	    int b = this.view.getValueSpinner2();

	    // Check if 'a' is not congruent to 178, warning user if true
	    if (!this.model.checkKey(a)) {
	        int decision = this.view.showYesNoDialog(
	            "Số a không đồng dư với 178 có thể dẫn tới không thể mã hoá!", 
	            "Vẫn tiếp tục"
	        );
	        if (decision != JOptionPane.YES_OPTION) {
	            return; // User chose No → stop
	        }
	    }

	    // Set input text for the model
	    this.model.setInput(this.view.getInputText());

	    // Encrypt or decrypt based on the mode
	    if (mode.equals("ENCRYPT")) {
	        this.model.encryptText(a, b);
	    } else {
	        try {
	            this.model.decryptText(a, b);
	        } catch (ArithmeticException e) {
	            // Handle exception
	            this.view.showDialogMessage("Không thể giải mã", "ERROR");
	            this.model.setOutput("");
	        }
	    }

	    // Set the output text after processing
	    this.view.setOutputText(this.model.getOutput());
	}

	// Generates a new key and sets spinner values
	public void genKey() {
	    this.model.genKey();
	    this.view.setValueSpinner1(this.model.getA());
	    this.view.setValueSpinner2(this.model.getB());
	}

	// Loads a key from a file
	public void loadKey() {
	    String filePath = this.view.showFileDialog("Chọn file", true);
	    if (!filePath.isEmpty()) {
	        String message;
	        try {
	            // Load key from file and show a message
	            message = this.model.loadKey(this.view.showFileDialog("Chọn file", false));
	            this.view.showDialogMessage(message, "INFO");
	            this.view.setValueSpinner1(this.model.getA());
	            this.view.setValueSpinner2(this.model.getB());
	        }// Handle exception 
	          catch (FileNotFoundException e) {
	            this.view.showDialogMessage("Không tìm thấy file: " + filePath, "ERROR");
	        } catch (IOException e) {
	            this.view.showDialogMessage("Lỗi khi đọc khoá" + e.getMessage(), "ERROR");
	        }
	    }
	}

	// Saves the current key to a file
	public void saveKey() {
	    String filePath = this.view.showFileDialog("Chọn file", true);
	    if (!filePath.isEmpty()) {
	        String message;
	        try {
	            // Set values for 'a' and 'b' and save the key
	            this.model.setA(this.view.getValueSpinner1());
	            this.model.setB(this.view.getValueSpinner2());
	            message = this.model.saveKey(filePath);
	            this.view.showDialogMessage(message, "INFO");
	        } // Handle exception
	          catch (IOException e) {
	            this.view.showDialogMessage("Lỗi khi lưu khoá: " + e.getMessage(), "ERROR");
	        }
	    }
	}


	public static void main(String[] args) {
		new AffineCipherController(new AffineCipher(), new AffineCipherView());
	}
}
