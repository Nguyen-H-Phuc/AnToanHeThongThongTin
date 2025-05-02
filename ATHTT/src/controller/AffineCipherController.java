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
	
	public void handleText(String mode) {
	    int a = this.view.getValueSpinner1();
	    int b = this.view.getValueSpinner2();

	    if (!this.model.checkKey(a)) {
	        int decision = this.view.showYesNoDialog(
	            "Số a không đồng dư với 178 có thể dẫn tới không thể mã hoá!", 
	            "Vẫn tiếp tục"
	        );
	        if (decision != JOptionPane.YES_OPTION) {
	            return; // người dùng chọn No → dừng
	        }
	    }

	    this.model.setInput(this.view.getInputText());
	    if (mode.equals("ENCRYPT")) {
	        this.model.encryptText(a, b);
	    } else {
	        try {
	            this.model.decryptText(a, b);
	        } catch (ArithmeticException e) {
	            this.view.showDialogMessage("Không thể giải mã", "ERROR");
	            this.model.setOutput("");
	        }
	    }
	    this.view.setOutputText(this.model.getOutput());
	}

	


	public void genKey() {
		this.model.genKey();
		this.view.setValueSpinner1(this.model.getA());
		this.view.setValueSpinner2(this.model.getB());
	}
	
	public void loadKey() {
		String filePath = this.view.showFileDialog("Chọn file", true);
		if (!filePath.isEmpty()) {
			String message;
			try {
				message = this.model.loadKey(this.view.showFileDialog("Chọn file", false));
				this.view.showDialogMessage(message, "INFO");
				this.view.setValueSpinner1(this.model.getA());
				this.view.setValueSpinner2(this.model.getB());
			} catch (FileNotFoundException e) {
				this.view.showDialogMessage("Không tìm thấy file: " + filePath, "ERROR");
			} catch (IOException e) {
				this.view.showDialogMessage("Lỗi khi đọc khoá" + e.getMessage(), "ERROR");
			}
		}
	}
	
	public void saveKey() {
		String filePath = this.view.showFileDialog("Chọn file", true);
		if (!filePath.isEmpty()) {
			String message;
			try {
				this.model.setA(this.view.getValueSpinner1());
				this.model.setB(this.view.getValueSpinner2());
				message = this.model.saveKey(filePath);
				this.view.showDialogMessage(message, "INFO");
			} catch (IOException e) {
				this.view.showDialogMessage("Lỗi khi lưu khoá: " + e.getMessage(), "ERROR");
			}
		}
	}

	public static void main(String[] args) {
		new AffineCipherController(new AffineCipher(), new AffineCipherView());
	}
}
