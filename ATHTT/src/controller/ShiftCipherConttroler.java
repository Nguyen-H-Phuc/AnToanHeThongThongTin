package controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

import model.classicialcipher.ShiftCipher;
import utils.ViewUtils;
import view.ShiftCipherView;

public class ShiftCipherConttroler {
	private ShiftCipher model;
	private ShiftCipherView view;
	
	public ShiftCipherConttroler(ShiftCipher model, ShiftCipherView view) {
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
	
	private void handleText(String mode) {
		int shift = view.getValueSpinner();
		this.model.setInput(this.view.getInputText());
		if(mode.equals("ENCRYPT")) {
		this.model.encryptText(shift);}
		else {this.model.decryptText(shift);}
		this.view.setOutputText(this.model.getOutput());
	}
	
	private void genKey() {
		int shift = this.model.genKey();
		this.view.setValueSpinner(shift);
	}
		
	private void saveKey() {
		String filePath = view.showFileDialog("Chọn file", true);
		if (!filePath.isEmpty()) {
			this.view.showDialogMessage(model.saveKey(filePath, this.view.getValueSpinner()), "INFO");
		}

	}
	
	private void loadKey() {
		String filePath = view.showFileDialog("Chọn file", false);
		if (!filePath.isEmpty()) {
			try {
				int shift = this.model.loadKey(filePath);
				this.view.setValueSpinner(shift);
				this.view.showDialogMessage("Tải khoá thành công", "INFO");
			} catch (NumberFormatException e) {
				this.view.showDialogMessage("Khoá bị lỗi: Khoá phải là số nguyên", "ERROR");
			} catch (IOException e) {
				this.view.showDialogMessage("Lỗi khi đọc file: " + e.getMessage(), "ERROR");
			}
		}
	}

}
