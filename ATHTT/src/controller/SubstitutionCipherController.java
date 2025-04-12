package controller;

import view.SubstitutionCipherView;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import model.classicialcipher.SubstitutionCipher;

public class SubstitutionCipherController {
	private SubstitutionCipherView scv;
	private SubstitutionCipher model;

	public SubstitutionCipherController(SubstitutionCipherView scv, SubstitutionCipher model) {
		this.scv = scv;
		this.model = model;

		this.scv.getEncryptTextBtn().addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				handleEncrypt();
			}
		});

		this.scv.getDecryptTextBtn().addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				handleDecrypt();
			}
		});

		this.scv.getSaveResultBtn().addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				saveResult();
			}
		});

		this.scv.getCreateKey().addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				genKey();

			}
		});
		
		this.scv.getLoadKey().addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				loadKey();
				
			}
		});
		
		this.scv.getSaveKey().addActionListener(new ActionListener() {	
			@Override
			public void actionPerformed(ActionEvent e) {
				saveKey();
			}
		});

	}

	private void genKey() {
		this.model.generateSubstitutionTable();
	}
	
	private void loadKey() {
		String filePath= this.scv.showFileChooser();
		if(!filePath.isEmpty()) {
		this.scv.showWarningDialog(this.model.loadSubstitutionTable(filePath));
		}
	}
	
	private void saveKey() {
		String filePath= this.scv.showFileChooser();
		if(!filePath.isEmpty()) {
		this.scv.showWarningDialog(this.model.saveSubstitutionTable(filePath));
		}
	}

	private void handleEncrypt() {
		if (this.model.getEncryptionMap().isEmpty()) {
			this.scv.showWarningDialog("Khoá chưa được tạo!");
		} else {
			this.model.setInput(scv.getInputText().getText());
			this.model.encryptText();
			if (this.model.getOutput().equals("ERROR")) {
				this.scv.showErrorDiaglog("Mã hoá thất bại!");
			} else {
				scv.getOutputText().setText(this.model.getOutput());
			}
		}
	}

	private void handleDecrypt() {
		if(this.model.getDecryptionMap().isEmpty()) {
			this.scv.showWarningDialog("Khoá chưa được tạo!");
		} else {
		this.model.setInput(scv.getInputText().getText());
		this.model.decryptText();
		if (this.model.getOutput().equals("ERROR")) {
			this.scv.showErrorDiaglog("Giải mã thất bại!");
		} else {
			scv.getOutputText().setText(this.model.getOutput());
		}
	}
}

	private void saveResult() {
		this.scv.showWarningDialog(
				this.model.saveOutputToFile(this.scv.getOutputText().getText(), scv.showFileChooser()));
	}
}
