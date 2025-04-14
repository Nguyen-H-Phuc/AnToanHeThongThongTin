package controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JOptionPane;

import model.classicialcipher.ShiftCipher;
import view.ShiftCipherView;

public class ShiftCipherConttroler {
	ShiftCipher model;
	ShiftCipherView view;
	
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
	
//		this.view.getLoadKey().addActionListener(new ActionListener() {
//			@Override
//			public void actionPerformed(ActionEvent e) {
//				loadKey();
//				
//			}
//		});
//		
		this.view.getSaveKey().addActionListener(new ActionListener() {	
			@Override
			public void actionPerformed(ActionEvent e) {
				saveKey();
			}
		});
	}
	
	private void handleText(String mode) {
		int shift = view.getValueSpinner();
		this.model.setInput(this.view.getText());
		if(mode.equals("ENCRYPT")) {
		this.model.encryptText(shift);}
		else {this.model.decryptText(shift);}
		this.view.getOutputText().setText(this.model.getOutput());
	}
	
	private void genKey() {
		int shift = this.model.genKey();
		this.view.setValueSpinner(shift);
	}
	
	private void saveResult() {
		String filePath = view.showFileDialog("Chọn file");
		if (!filePath.isEmpty()) {
			this.view.showDialogMessage(this.model.saveOutputToFile(this.view.getOutputText().getText(), filePath),
					"INFO");
		}
	}
	
	private void saveKey() {
		String filePath = view.showFileDialog("Chọn file");
		if (!filePath.isEmpty()) {
			this.view.showDialogMessage(model.saveKey(filePath, this.view.getValueSpinner()), "INFO");
		}

	}

	
	public static void main(String[] args) {
		new ShiftCipherConttroler(new ShiftCipher(), new ShiftCipherView());
	}
}
