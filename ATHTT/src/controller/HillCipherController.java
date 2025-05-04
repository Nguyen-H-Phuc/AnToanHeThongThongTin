package controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileNotFoundException;
import java.io.IOException;

import javax.swing.JOptionPane;

import model.classicialcipher.HillCipher;
import utils.ViewUtils;
import view.HillCipherView;

public class HillCipherController {
	private HillCipher model;
	private HillCipherView view;
	
	public HillCipherController(HillCipher model, HillCipherView view) {
		this.model = model;
		this.view = view;
		
		view.getGenKey().addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				genKey();
				
			}
		});
		
		view.getLoadKey().addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				loadKey();
				
			}
		});
		
		view.getSaveKey().addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				saveKey();
				
			}
		});
		
		view.getEncryptTextBtn().addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				handleText("ENCRYPT");
				
			}
		});
		
		view.getDecryptTextBtn().addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				handleText("DECRYPT");
				
			}
		});
		
		ViewUtils.setupClearButton(view.getClearTextPanelBtn(), view.getInputTextArea(), view.getOutputTextArea());
		ViewUtils.setupSwapButton(view.getSwapBtn(), view.getInputTextArea(), view.getOutputTextArea());
		ViewUtils.setSaveResultBtn(view.getSaveResultBtn(), view.getOutputTextArea(), view.getFrame());
	}
	
	private void genKey() {
		this.model.genKey();
		int[][] values = this.model.getMatrix();
		this.view.updateValuesTable(values);
	}
	
	private void saveKey() {
		String filePath = this.view.showFileDialog("Chọn file", true);
		int[][] values = this.view.getValuesTable();
		for(int []i : values) {
			for(int j : i) {
				System.out.println(j);
			}
		}
		if (!filePath.isEmpty() && values != null) {
			this.model.setMatrix(values);
			try {
				this.view.showDialogMessage(this.model.saveKey(filePath), "INFO");
			} catch (IOException e) {
				this.view.showDialogMessage("Lỗi khi ghi khoá: " + e.getMessage(), "ERROR");
			} 
		} else if(values == null) {
			this.view.showDialogMessage("Không thể lưu khoá lỗi.", "ERROR");
		}
	}
	
	private void loadKey() {
		String filePath = this.view.showFileDialog("Chọn file", false);
		if (!filePath.isEmpty()) {
			try {
				this.view.showDialogMessage(this.model.loadKey(filePath), "INFO");
				this.view.updateValuesTable(this.model.getMatrix());
			} catch (FileNotFoundException e) {
				this.view.showDialogMessage("Không tìm thấy file: " + filePath, "ERROR");
			} catch (IOException e) {
				this.view.showDialogMessage("Lỗi khi đọc khoá: " + e.getMessage(), "ERROR");
			} catch (NumberFormatException e) {
				this.view.showDialogMessage("Khoá lỗi! Không thể tải.", "ERROR");
			}
		}
	}
	
	private void handleText(String mode) {
		int[][] values = this.view.getValuesTable();
		for(int []i : values) {
			for(int j : i) {
				System.out.println(j);
			}
		}
		String input = this.view.getInputText();
		int decision = JOptionPane.YES_OPTION;
		if (values == null) {
			return;
		}
			this.model.setMatrix(values);

		 if(this.model.getInvMatrix()==null){
			this.model.clearInvMatrix();
			decision = this.view.showYesNoDialog(
					"Không thể tạo ma trận nghịch đảo. Việc giải mã có thể bị lỗi hoặc không giải mã được. Bạn có muốn tiếp tục?",
					"WARN");
		}
		if (decision == JOptionPane.YES_OPTION) {
			this.model.setInput(input);
			if (mode.equals("ENCRYPT")) {
				this.model.encryptText();
			} else {
				if (this.model.getInvMatrix() != null) {
					this.model.decryptText();
					this.view.setOutputText(this.model.getOutput());
				} else {
					this.view.showDialogMessage("Không thể giải mã", "ERROR");
					this.model.setOutput("");
				}
			}
			this.view.setOutputText(this.model.getOutput());
		} else {
			this.view.setOutputText("");
		}
	}

	public static void main(String[] args) {
		new HillCipherController(new HillCipher(), new  HillCipherView(2));
	}
	
}
