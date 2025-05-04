package controller;

import view.SubstitutionCipherView;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileNotFoundException;
import java.io.IOException;

import javax.swing.JOptionPane;

import model.classicialcipher.SubstitutionCipher;
import utils.ViewUtils;

public class SubstitutionCipherController {
	private SubstitutionCipherView view;
	private SubstitutionCipher model;

	public SubstitutionCipherController(SubstitutionCipherView view, SubstitutionCipher model) {
		this.view= view;
		this.model = model;

		this.view.getEncryptTextBtn().addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				handleEncrypt();
			}
		});

		this.view.getDecryptTextBtn().addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				handleDecrypt();
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

	private void genKey() {
		this.model.generateSubstitutionTable();
		this.view.updateTableValues(this.model.getEncryptionMap());
	}
	
	private void loadKey() {
		String filePath= this.view.showFileDialog("Chọn file", false);
		if(!filePath.isEmpty()) {
		try {
			this.view.updateTableValues(this.model.getEncryptionMap());
			this.view.showDialogMessage(this.model.loadSubstitutionTable(filePath),"INFO");
		} catch (FileNotFoundException e) {
			this.view.showDialogMessage("Không tìm thấy file: " +filePath, "ERROR");
		} catch (IOException e) {
			this.view.showDialogMessage("Lỗi khi đọc file: " +e.getMessage(), "ERROR");
		}
		}
	}
	
	private void saveKey() {
		String filePath= this.view.showFileDialog("Chọn file", true);
		if(!filePath.isEmpty()) {
			try {
				this.model.setEncryptionMap(this.view.getTableValues());
				this.view.showDialogMessage(this.model.saveSubstitutionTable(filePath),"INFO");
			} catch (IOException e) {
				this.view.showDialogMessage("Lỗi khi lưu: " +e.getMessage(), "ERROR");
			}
		}
	}

	private void handleEncrypt() {
		this.model.setEncryptionMap(this.view.getTableValues());
		int decision = JOptionPane.YES_OPTION;
		if (this.model.hasDuplicateValues()) {
			decision = this.view.showYesNoDialog(
					"Có kí tự thay thế bị trùng. Việc giải mã có thể bị lỗi hoặc không giải mã được. Bạn có muốn tiếp tục?",
					"WARN");
			if (decision == JOptionPane.YES_OPTION) {
				if (this.model.getEncryptionMap().isEmpty()) {
					this.view.showDialogMessage("Khoá chưa được tạo!", "ERROR");
				} else {
					this.model.setInput(view.getInputText());
					this.model.encryptText();
					if (this.model.getOutput().equals("ERROR")) {
					} else {
						view.setOutputText(this.model.getOutput());
					}
				}
			}
		}
	}

	private void handleDecrypt() {
		this.model.setEncryptionMap(this.view.getTableValues());
		if(this.model.getDecryptionMap().isEmpty()) {
			this.view.showDialogMessage("Khoá chưa được tạo!", "ERROR");
		} else {
		this.model.setInput(view.getInputText());
		this.model.decryptText();
		view.setOutputText(this.model.getOutput());
		}
	}


}
