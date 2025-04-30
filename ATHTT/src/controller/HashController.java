package controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;

import model.HashUtil;
import view.HashView;

public class HashController {
	private HashUtil model;
	private HashView view;

	public HashController() {
		this.model = new HashUtil();
		this.view = new HashView();

		this.view.getHashTextBtn().addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				String instance = view.getAlgorithms();
				try {
					String output = model.hashText(view.getInputText(), instance);
					view.setOutputText(output);
				} catch (NoSuchAlgorithmException e1) {
					view.showDialogMessage("Thuật toán không được hỗ trợ: " + e1.getMessage(), "ERROR");
				} catch (NoSuchProviderException e1) {
					view.showDialogMessage("Thuật toán không được hỗ trợ: " + e1.getMessage(), "ERROR");
				}catch (Exception e1) {
					// Bắt tất cả lỗi còn lại, nếu có
					view.showDialogMessage("Đã xảy ra lỗi không xác định: " + e1.getMessage(), "ERROR");
				}
			}
		});

		this.view.getHashFileBtn().addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {

				try {
					String instance = view.getAlgorithms();
					String srcFile = view.getSrcFilePath();
					if (!srcFile.isEmpty()) {
						String output = model.hashFile(srcFile, instance);
						view.setOutputFile(output);
					} else {
						view.showDialogMessage("Vui lòng chọn file trước khi hash.", "WARNING");
					}
				} catch (NoSuchAlgorithmException e1) {
					view.showDialogMessage("Thuật toán không hợp lệ: " + e1.getMessage(), "ERROR");
				} catch (NoSuchProviderException e1) {
					view.showDialogMessage("Không tìm thấy provider phù hợp: " + e1.getMessage(), "ERROR");
				} catch (IOException e1) {
					view.showDialogMessage("Lỗi đọc file: " + e1.getMessage(), "ERROR");
				} catch (Exception e1) {
					// Bắt tất cả lỗi còn lại, nếu có
					view.showDialogMessage("Đã xảy ra lỗi không xác định: " + e1.getMessage(), "ERROR");
				}

			}
		});
		
		this.view.getClearTextBtn().addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				clear("TEXT");
				
			}
		});
		
		this.view.getClearFileBtn().addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				clear("FILE");
				
			}
		});
	}
	
	private void clear(String panel) {
		if(panel.equals("TEXT")) {
			view.setInputText("");
			view.setOutputText("");
		} else {view.setOutputFile("");
		view.setSrcFilePath("");
		}
		
	}

	public static void main(String[] args) {
		new HashController();
	}
}
