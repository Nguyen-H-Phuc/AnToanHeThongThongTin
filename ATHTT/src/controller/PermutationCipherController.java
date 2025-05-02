package controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileNotFoundException;
import java.io.IOException;

import model.classicialcipher.PermutationCipher;
import utils.ViewUtils;
import view.PermutationCipherView;

public class PermutationCipherController {
	private PermutationCipher model;
	private PermutationCipherView view;

	public PermutationCipherController(PermutationCipher model, PermutationCipherView view) {
		super();
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
		int[] permutationTable = this.view.getTableValues();
		this.model.setInput(this.view.getInputText());
		if (mode.equals("ENCRYPT")) {
			this.model.encryptText(permutationTable);
		} else {
			this.model.decryptText(permutationTable);
		}
		this.view.setOutputText(this.model.getOutput());

	}

	private void genKey() {
		int keyLength = this.view.getValueSpinner();

		int[] permutationTable = this.model.genKey(keyLength);
		this.view.updateTableValues(permutationTable);
	}

	private void loadKey() {
		String filePath = this.view.showFileDialog("Chọn file", false);
		if (!filePath.isEmpty()) {
			int[] permutationTable = this.model.getPermutationTable();
			String result;
			try {
				result = this.model.loadKey(filePath);
				this.view.showDialogMessage(result, "Info");
				this.view.updateTableValues(permutationTable);
			} catch (NumberFormatException e) {
				this.view.showDialogMessage("Khoá bị lỗi.", "ERROR");
			} catch (FileNotFoundException e) {
				this.view.showDialogMessage("Không tìm thấy file:" + filePath, "ERROR");
			} catch (IOException e) {
				this.view.showDialogMessage("Lỗi không xác định: " + e.getMessage(), "ERROR");
			}
		}
	}
	
	public void saveKey() {
		String filePath = this.view.showFileDialog("Chọn file", true);
		if (!filePath.isEmpty()) {
			int[] permutationTable = this.view.getTableValues();
			this.model.setPermutationTable(permutationTable);
			try {
				String result = this.model.saveKey(filePath);
				this.view.showDialogMessage(result, "INFO");
			} catch (IOException e) {
				this.view.showDialogMessage("Lỗi khi lưu khoá: " + e.getMessage(), "ERROR");
			}
		}
	}

	public static void main(String[] args) {
		new PermutationCipherController(new PermutationCipher(), new PermutationCipherView());
	}
}
