package view;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FileDialog;
import java.awt.Frame;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

public abstract class ClassicalCipherView {
	private JFrame frame;
	private JLabel labelInput, labelOutput;
	private JTextArea inputText, outputText;
	private JTextField srcFile, destFile;
	private JButton encryptTextBtn, decryptTextBtn, saveResultBtn, clearTextPanelBtn, swapBtn,  encryptFileBtn, decryptFileBtn, genKey, loadKey, saveKey;
	private JPanel textPanel, filePanel;
	
	public void createFrame(int height, int width, String title) {
		frame = new JFrame();
		frame.setSize(width, height);
		frame.setTitle(title);
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);		
	}
	
	public void createPanelTextCipher() {
		textPanel = new JPanel();
        textPanel.setLayout(new BoxLayout(textPanel, BoxLayout.Y_AXIS));
        textPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Input Panel
        JPanel inputPanel = new JPanel(new BorderLayout());
        labelInput = new JLabel("Nhập văn bản", SwingConstants.LEFT);
        inputText = new JTextArea(8, 50);
        inputText.setLineWrap(true);
        inputText.setWrapStyleWord(true);
        JScrollPane scrollInput = new JScrollPane(inputText);
        scrollInput.setPreferredSize(new Dimension(600, 100));
        inputPanel.add(labelInput, BorderLayout.NORTH);
        inputPanel.add(scrollInput, BorderLayout.CENTER);

        // Output Panel
        JPanel outputPanel = new JPanel(new BorderLayout());
        labelOutput = new JLabel("Kết quả", SwingConstants.LEFT);
        outputText = new JTextArea(8, 50);
        outputText.setLineWrap(true);
        outputText.setWrapStyleWord(true);
        JScrollPane scrollOutput = new JScrollPane(outputText);
        scrollOutput.setPreferredSize(new Dimension(600, 100));
        outputPanel.add(labelOutput, BorderLayout.NORTH);
        outputPanel.add(scrollOutput, BorderLayout.CENTER);

        // Text Buttons
        JPanel textBtnPanel = new JPanel();
        encryptTextBtn = new JButton("Mã hóa văn bản");
        decryptTextBtn = new JButton("Giải mã văn bản");
        saveResultBtn = new JButton("Lưu kết quả");
        clearTextPanelBtn = new JButton("Clear");
        swapBtn = new JButton("Hoán đổi");
        textBtnPanel.add(encryptTextBtn);
        textBtnPanel.add(decryptTextBtn);
        textBtnPanel.add(saveResultBtn);
        textBtnPanel.add(clearTextPanelBtn);
        textBtnPanel.add(swapBtn);

        textPanel.add(inputPanel);
        textPanel.add(Box.createVerticalStrut(10));
        textPanel.add(outputPanel);
        textPanel.add(Box.createVerticalStrut(10));
        textPanel.add(textBtnPanel);
	}
	
	public void createFileCipherPanel() {
		filePanel = new JPanel();
        filePanel.setLayout(new GridLayout(5, 1, 5, 5));
        filePanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // === Source file panel with button ===
        JLabel labelSource = new JLabel("Đường dẫn file nguồn:");
        JPanel sourcePanel = new JPanel(new BorderLayout());
        srcFile = new JTextField();
        JButton browseSourceBtn = new JButton("...");
        sourcePanel.add(srcFile, BorderLayout.CENTER);
        sourcePanel.add(browseSourceBtn, BorderLayout.EAST);

        // === Dest file panel with button ===
        JLabel labelDest = new JLabel("Đường dẫn file đích:");
        JPanel destPanel = new JPanel(new BorderLayout());
        destFile = new JTextField();
        JButton browseDestBtn = new JButton("...");
        destPanel.add(destFile, BorderLayout.CENTER);
        destPanel.add(browseDestBtn, BorderLayout.EAST);

        // === Button panel ===
        JPanel fileBtnPanel = new JPanel();
        encryptFileBtn = new JButton("Mã hóa file");
        decryptFileBtn = new JButton("Giải mã file");
        fileBtnPanel.add(encryptFileBtn);
        fileBtnPanel.add(decryptFileBtn);

        // === Add to file panel ===
        filePanel.add(labelSource);
        filePanel.add(sourcePanel);
        filePanel.add(labelDest);
        filePanel.add(destPanel);
        filePanel.add(fileBtnPanel);

        // === Add file panel to frame ===
        frame.add(filePanel, BorderLayout.SOUTH);

        // ===== Action for browse buttons =====
        browseSourceBtn.addActionListener(e -> {
            FileDialog fileDialog = new FileDialog((Frame) null, "Chọn file nguồn", FileDialog.LOAD);
            fileDialog.setVisible(true);
            if (fileDialog.getFile() != null) {
                String selectedFile = fileDialog.getDirectory() + fileDialog.getFile();
                srcFile.setText(selectedFile);
            }
        });


        browseDestBtn.addActionListener(e -> {
        	FileDialog fileDialog = new FileDialog((Frame) null, "Chọn file đích", FileDialog.LOAD);
            fileDialog.setVisible(true);
            if (fileDialog.getFile() != null) {
                String selectedFile = fileDialog.getDirectory() + fileDialog.getFile();
                destFile.setText(selectedFile);
            }
        });
        setSwapBtn();
        
	}
	
	public void setSwapBtn() {
		String textSwap = getInputText();
		setInputText(getOutputText());
		setOutputText(textSwap);
	}
	
	public void setClearBtn() {
		this.setInputText("");
		this.setOutputText("");
	}
	
	public abstract void createKeyPanel();
	
	public String showFileDialog(String title, boolean saveDialog) {
		FileDialog fileDialog;
		if(saveDialog) {
	    fileDialog = new FileDialog(frame, title, FileDialog.SAVE);}
		else {
		   fileDialog = new FileDialog(frame, title, FileDialog.LOAD);
		}
	    fileDialog.setVisible(true);

	    String directory = fileDialog.getDirectory();
	    String filename = fileDialog.getFile();

	    if (directory != null && filename != null) {
	        return directory + filename;
	    }
	    return "";
	}

	public void showDialogMessage(String message, String type) {
		switch (type.toUpperCase()) {
		case "ERROR":
			JOptionPane.showMessageDialog(null, message, "Lỗi", JOptionPane.ERROR_MESSAGE);
			break;
		case "WARNING":
			JOptionPane.showMessageDialog(null, message, "Thông báo", JOptionPane.WARNING_MESSAGE);
			break;
		case "INFO":
			JOptionPane.showMessageDialog(null, message, "Thông báo", JOptionPane.INFORMATION_MESSAGE);
			break;
		}
	}

	public int showYesNoDialog(String message1, String message2) {
		int result = JOptionPane.showConfirmDialog(null, message1, message2, JOptionPane.YES_NO_OPTION,
				JOptionPane.WARNING_MESSAGE);
		return result;
	}
		
	public JFrame getFrame() {
		return frame;
	}
	
	public JPanel getTextPanel() {
		return textPanel;
	}

	public JPanel getFilePanel() {
		return filePanel;
	}

	public JButton getEncryptTextBtn() {
		return encryptTextBtn;
	}

	public JButton getDecryptTextBtn() {
		return decryptTextBtn;
	}
	
	public JButton getSaveResultBtn() {
		return saveResultBtn;
	}

	public JButton getEncryptFileBtn() {
		return encryptFileBtn;
	}

	public JButton getDecryptFileBtn() {
		return decryptFileBtn;
	}

	public String getInputText() {
		return this.inputText.getText();
	}

	public void setInputText(String text) {
		this.inputText.setText(text);;
	}

	public String getOutputText() {
		return this.outputText.getText();
	}

	public void setOutputText(String text) {
		this.outputText.setText(text);;
	}

	public JButton getGenKey() {
		return genKey;
	}

	public JButton getLoadKey() {
		return loadKey;
	}

	public JButton getSaveKey() {
		return saveKey;
	}
	
	public JButton getClearTextPanelBtn() {
		return clearTextPanelBtn;
	}

	public JButton getSwapBtn() {
		return swapBtn;
	}

	public void setGenKey(String keyName) {
		this.genKey = new JButton(keyName);
	}
	
	public void setLoadKey(String keyName) {
		this.loadKey = new JButton(keyName);
	}
	
	public void setSaveKey(String keyName) {
		this.saveKey = new JButton(keyName);
	}

	public String getSrcFile() {
		return this.srcFile.getText();
	}
	
	public String getDestFile() {
		return this.destFile.getText();
	}
	
	public JTextArea getInputTextArea() {
		return this.inputText;
	}
	
	public JTextArea getOutputTextArea() {
		return this.outputText;
	}
}
