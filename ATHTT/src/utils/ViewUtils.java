package utils;

import java.awt.FileDialog;
import java.awt.Frame;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

public class ViewUtils {
	public static void setupClearButton(JButton button, JTextArea input, JTextArea output) {
        button.addActionListener(e -> {
            input.setText("");
            output.setText("");
        });
    }
	
	public static void setupSwapButton(JButton button, JTextArea input, JTextArea output) {
        button.addActionListener(e -> {
            String swapText = input.getText();
            input.setText(output.getText());
            output.setText(swapText);
        });
    }
	
	public static void setSaveResultBtn(JButton button, JTextArea output, Frame frame) {
		button.addActionListener(e -> {
			FileDialog fileDialog = new FileDialog(frame, "Chọn file lưu", FileDialog.SAVE);
			fileDialog.setVisible(true);

			String directory = fileDialog.getDirectory();
			String filename = fileDialog.getFile();
			if (directory != null && filename != null) {
				String filePath = directory + filename;
				String content = output.getText();
				try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
					writer.write(content);
					JOptionPane.showMessageDialog(null, "Lưu thành công vào file: " + filePath, "Thông báo",
							JOptionPane.INFORMATION_MESSAGE);
				} catch (IOException ex) {
					JOptionPane.showMessageDialog(null, "Lỗi khi lưu file: " + ex.getMessage(), "Lỗi",
							JOptionPane.ERROR_MESSAGE);
				}
			}
		});
	}
	
	public static void setFilePath(Frame frame, JButton button, JTextField textField) {
		button.addActionListener(e -> {
			FileDialog fileDialog = new FileDialog((Frame) null, "Chọn file", FileDialog.LOAD);
			fileDialog.setVisible(true);
			if (fileDialog.getFile() != null) {
				String selectedFile = fileDialog.getDirectory() + fileDialog.getFile();
				textField.setText(selectedFile);
			}
		});
	}
	
	public static void setSaveFilePath(Frame frame, JButton button, JTextField textField) {
		button.addActionListener(e -> {
			FileDialog fileDialog = new FileDialog((Frame) null, "Chọn file", FileDialog.SAVE);
			fileDialog.setVisible(true);
			if (fileDialog.getFile() != null) {
				String selectedFile = fileDialog.getDirectory() + fileDialog.getFile();
				textField.setText(selectedFile);
			}
		});
	}
}
