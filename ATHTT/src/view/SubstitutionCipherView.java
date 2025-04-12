package view;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FileDialog;
import java.awt.Frame;
import java.awt.GridLayout;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;

public class SubstitutionCipherView {
	private JFrame frame;
	private JPanel keyPanel, textPanel, filePanel, btnTextPanel, btnFilePanel;
	private JButton encryptTextBtn, decryptTextBtn, encryptFileBtn, decryptFileBtn, createKey, saveKey, loadKey, saveResultBtn;
	private JTextArea inputText, outputText;
	private JTextField sourceFilePath, destFilePath;
	private JLabel labelInputText, labelOutPutText, labelSourceFile, labelDestFile;
	
	public SubstitutionCipherView() {
		init();
	}

    private void init() {
        frame = new JFrame("Substitution Cipher Tool");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setSize(700, 600);
        frame.setLayout(new BorderLayout(10, 10));

        // ===== NORTH: Key Panel =====
        JPanel keyPanel = new JPanel();
        createKey = new JButton("Tạo khóa");
        loadKey = new JButton("Tải khóa");
        saveKey = new JButton("Lưu khóa");
        keyPanel.add(createKey);
        keyPanel.add(loadKey);
        keyPanel.add(saveKey);
        frame.add(keyPanel, BorderLayout.NORTH);

        // ===== CENTER: Text Panel =====
        JPanel textPanel = new JPanel();
        textPanel.setLayout(new BoxLayout(textPanel, BoxLayout.Y_AXIS));
        textPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Input Panel
        JPanel inputPanel = new JPanel(new BorderLayout());
        JLabel labelInput = new JLabel("Nhập văn bản", SwingConstants.LEFT);
        inputText = new JTextArea(8, 50);
        inputText.setLineWrap(true);
        inputText.setWrapStyleWord(true);
        JScrollPane scrollInput = new JScrollPane(inputText);
        scrollInput.setPreferredSize(new Dimension(600, 100));
        inputPanel.add(labelInput, BorderLayout.NORTH);
        inputPanel.add(scrollInput, BorderLayout.CENTER);

        // Output Panel
        JPanel outputPanel = new JPanel(new BorderLayout());
        JLabel labelOutput = new JLabel("Kết quả", SwingConstants.LEFT);
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
        textBtnPanel.add(encryptTextBtn);
        textBtnPanel.add(decryptTextBtn);
        textBtnPanel.add(saveResultBtn);

        textPanel.add(inputPanel);
        textPanel.add(Box.createVerticalStrut(10));
        textPanel.add(outputPanel);
        textPanel.add(Box.createVerticalStrut(10));
        textPanel.add(textBtnPanel);

        frame.add(textPanel, BorderLayout.CENTER);

     // ===== SOUTH: File Panel =====
        JPanel filePanel = new JPanel();
        filePanel.setLayout(new GridLayout(5, 1, 5, 5));
        filePanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // === Source file panel with button ===
        JLabel labelSource = new JLabel("Đường dẫn file nguồn:");
        JPanel sourcePanel = new JPanel(new BorderLayout());
        sourceFilePath = new JTextField();
        JButton browseSourceBtn = new JButton("...");
        sourcePanel.add(sourceFilePath, BorderLayout.CENTER);
        sourcePanel.add(browseSourceBtn, BorderLayout.EAST);

        // === Dest file panel with button ===
        JLabel labelDest = new JLabel("Đường dẫn file đích:");
        JPanel destPanel = new JPanel(new BorderLayout());
        destFilePath = new JTextField();
        JButton browseDestBtn = new JButton("...");
        destPanel.add(destFilePath, BorderLayout.CENTER);
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
                sourceFilePath.setText(selectedFile);
            }
        });


        browseDestBtn.addActionListener(e -> {
            JFileChooser fileChooser = new JFileChooser();
            int result = fileChooser.showSaveDialog(frame);
            if (result == JFileChooser.APPROVE_OPTION) {
                destFilePath.setText(fileChooser.getSelectedFile().getAbsolutePath());
            }
        });


        // ===== Hiển thị giao diện =====
        frame.setLocationRelativeTo(null); // hiện giữa màn hình
        frame.setVisible(true);
    }
    
    public void showWarningDialog(String message) {
    	JOptionPane.showMessageDialog(null, message, "Thông báo", JOptionPane.WARNING_MESSAGE);
    }
    
    public void showErrorDiaglog(String message) {
    	JOptionPane.showMessageDialog(null, message, "Lỗi", JOptionPane.ERROR_MESSAGE);
    }
    
    public String showFileChooser() {
        JFileChooser fileChooser = new JFileChooser();
        int result = fileChooser.showSaveDialog(frame);
        if (result == JFileChooser.APPROVE_OPTION) {
        	return fileChooser.getSelectedFile().getAbsolutePath();
        }
        return "";
    }
	
	public JButton getCreateKey() {
		return createKey;
	}

	public JButton getSaveKey() {
		return saveKey;
	}

	public JButton getLoadKey() {
		return loadKey;
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

	public JTextArea getInputText() {
		return inputText;
	}

	public void setInputText(JTextArea inputText) {
		this.inputText = inputText;
	}

	public JTextArea getOutputText() {
		return outputText;
	}

	public void setOutputText(JTextArea outputText) {
		this.outputText = outputText;
	}

	public static void main(String[] args) {		
		new SubstitutionCipherView();
    }

	}

