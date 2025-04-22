package controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import model.classicialcipher.AffineCipher;
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
		
		this.view.getSaveResultBtn().addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				saveResult();
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
	}
	
	public void handleText(String mode) {
		int a = this.view.getValueSpinner1();
		if (this.model.checkKey(a)) {
			this.model.setInput(this.view.getInputText());
			if (mode.equals("ENCRYPT")) {
				this.model.encryptText(a, this.view.getValueSpinner2());
			} else {
				this.model.decryptText(a, this.view.getValueSpinner2());
			}
			this.view.setOutputText(this.model.getOutput());
		} else {
			this.view.showDialogMessage("Số a không hợp lệ! Vui lòng chọn số đồng dư với 178", "ERROR");
		}
	}
	

	private void saveResult() {
		String filePath = view.showFileDialog("Chọn file", false);
		if (!filePath.isEmpty()) {
			this.view.showDialogMessage(this.model.saveOutputToFile(this.view.getOutputText(), filePath), "INFO");
		}
	}

	public void genKey() {
		this.model.genKey();
		this.view.setValueSpinner1(this.model.getA());
		this.view.setValueSpinner2(this.model.getB());
	}
	
	public void loadKey() {
		String filePath = this.view.showFileDialog("Chọn file", true);
		if(!filePath.isEmpty()) {
		String message = this.model.loadKey(this.view.showFileDialog("Chọn file", false));
		this.view.showDialogMessage(message, "INFO");
		this.view.setValueSpinner1(this.model.getA());
		this.view.setValueSpinner2(this.model.getB());
		}
	}
	
	public void saveKey() {
		String filePath = this.view.showFileDialog("Chọn file", true);
		if(!filePath.isEmpty()) {
		String message = this.model.saveKey(filePath);
		this.view.showDialogMessage(message, "INFO");
		this.view.setValueSpinner1(this.model.getA());
		this.view.setValueSpinner2(this.model.getB());
		}
	}
	
	public static void main(String[] args) {
		new AffineCipherController(new AffineCipher(), new AffineCipherView());
	}
}
