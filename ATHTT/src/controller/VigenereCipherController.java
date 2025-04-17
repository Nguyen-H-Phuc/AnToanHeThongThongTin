package controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import model.classicialcipher.VigenereCipher;
import view.VigenereCipherView;

public class VigenereCipherController {
	VigenereCipher model;
	VigenereCipherView view;
	
	public VigenereCipherController(VigenereCipher model, VigenereCipherView view) {
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
	
	private void handleText(String mode) {
		String key = view.getKey();
		this.model.setKey(key);
		this.model.setInput(this.view.getInputText());
		if(mode.equals("ENCRYPT")) {
		this.model.encryptText();}
		else {this.model.decryptText();}
		this.view.setOutputText(this.model.getOutput());
	}
	
	private void saveResult() {
		String filePath = view.showFileDialog("Chọn file", true);
		if (!filePath.isEmpty()) {
			this.view.showDialogMessage(this.model.saveOutputToFile(this.view.getOutputText(), filePath),
					"INFO");
		}
	}
	
	private void genKey() {
		int keyLength = view.getValueSpinner();
		this.model.setKeyLength(keyLength);
		this.model.genKey();
		this.view.setKey(model.getKey());
	}
	
	
	
	private void loadKey() {
		String filePath = view.showFileDialog("Chọn file", false);
		if (!filePath.isEmpty()) {
			this.view.showDialogMessage(model.loadKey(filePath), "INFO");
			this.view.setKey(this.model.getKey());
		}
	}
	
	private void saveKey() {
		this.model.setKey(this.view.getKey());
		String filePath = view.showFileDialog("Chọn file", true);
		if (!filePath.isEmpty()) {
			this.view.showDialogMessage(model.saveKey(filePath), "INFO");
		}
	}
	
	public static void main(String[] args) {
		VigenereCipherController vc = new VigenereCipherController(new VigenereCipher(), new VigenereCipherView());
	}
}
