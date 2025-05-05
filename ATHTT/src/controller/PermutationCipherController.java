package controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileNotFoundException;
import java.io.IOException;

import javax.swing.JOptionPane;

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

	    // Check if any value exceeds its allowed index
	    if (this.model.hasValueGreaterThanIndex(permutationTable)) {
	        this.view.showDialogMessage("Bảng hoán vị không hợp lệ. Không thể tiếp tục.", "ERROR");
	        return;
	    }

	    int decision = JOptionPane.YES_NO_OPTION;

	    // Check for duplicate positions in permutation table
	    if (this.model.hasDuplicates(permutationTable)) {
	        decision = this.view.showYesNoDialog(
	                "Phát hiện vị trí hoán vị trùng nhau. Việc giải mã có thể bị lỗi hoặc không giải mã được. Bạn có muốn tiếp tục?",
	                "WARN");
	    }

	    if (decision == JOptionPane.YES_OPTION) {
	        // Perform encryption or decryption
	        if (mode.equals("ENCRYPT")) {
	            this.model.encryptText(permutationTable);
	        } else {
	            this.model.decryptText(permutationTable);
	        }
	        this.view.setOutputText(this.model.getOutput());
	    }
	}

	private void genKey() {
	    int keyLength = this.view.getValueSpinner();

	    // Generate permutation key and update view
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
	        }// Handle exception 
	          catch (NumberFormatException e) {
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
			} // Handle exception
			catch (IOException e) {
				this.view.showDialogMessage("Lỗi khi lưu khoá: " + e.getMessage(), "ERROR");
			}
		}
	}


	public static void main(String[] args) {
		new PermutationCipherController(new PermutationCipher(), new PermutationCipherView());
	}
}
