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
				
				try {
					// Get the selected hashing algorithm from the user interface (e.g., "SHA-256", "MD5", etc.)
					String instance = view.getAlgorithms();
				    // Call the model's method to hash the input text using the selected algorithm
				    String output = model.hashText(view.getInputText(), instance);				    
				    // Display the hashed result in the output text area on the UI
				    view.setOutputText(output);
				} 
				// Handle exception
				catch (NoSuchAlgorithmException e1) {
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
					// Get the selected hashing algorithm and source file path from the UI
					String instance = view.getAlgorithms();
					String srcFile = view.getSrcFilePath();

					// Check if a file is selected for hashing
					if (!srcFile.isEmpty()) {
					    // Call model to hash the file and display the output
					    String output = model.hashFile(srcFile, instance);
					    view.setOutputFile(output);
					} else {
					    // Prompt the user to select a file before hashing
					    view.showDialogMessage("Vui lòng chọn file để hash.", "WARNING");
					}
				} 
				// Handle exception
				  catch (NoSuchAlgorithmException e1) {
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
	
}
