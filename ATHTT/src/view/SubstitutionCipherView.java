package view;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridLayout;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
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
	private JButton encryptTextBtn, decryptTextBtn, encryptFileBtn, decryptFileBtn, createKey, saveKey, loadKey, saveResult;
	private JTextArea inputText, outputText;
	private JTextField sourceFilePath, destFilePath;
	private JLabel labelInputText, labelOutPutText, labelSourceFile, labelDestFile;
	
	public SubstitutionCipherView() {
		init();
	}

    private void init() {
        frame = new JFrame("Substitution Cipher Tool");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
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
        saveResult = new JButton("Lưu kết quả");
        textBtnPanel.add(encryptTextBtn);
        textBtnPanel.add(decryptTextBtn);
        textBtnPanel.add(saveResult);

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

        JLabel labelSource = new JLabel("Đường dẫn file nguồn:");
        sourceFilePath = new JTextField();
        JLabel labelDest = new JLabel("Đường dẫn file đích:");
        destFilePath = new JTextField();

        JPanel fileBtnPanel = new JPanel();
        encryptFileBtn = new JButton("Mã hóa file");
        decryptFileBtn = new JButton("Giải mã file");
        fileBtnPanel.add(encryptFileBtn);
        fileBtnPanel.add(decryptFileBtn);

        filePanel.add(labelSource);
        filePanel.add(sourceFilePath);
        filePanel.add(labelDest);
        filePanel.add(destFilePath);
        filePanel.add(fileBtnPanel);

        frame.add(filePanel, BorderLayout.SOUTH);

        // ===== Hiển thị giao diện =====
        frame.setLocationRelativeTo(null); // hiện giữa màn hình
        frame.setVisible(true);
    }
	
	public static void main(String[] args) {
		SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Substitution Cipher Tool");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(600, 500);
            frame.setLayout(new BorderLayout(10, 10));

            // Panel trên cùng - các nút key
            JPanel keyPanel = new JPanel();
            JButton loadKeyButton = new JButton("Load Key");
            JButton createKeyButton = new JButton("Create Key");
            JButton saveKeyButton = new JButton("Save Key");
            keyPanel.add(loadKeyButton);
            keyPanel.add(createKeyButton);
            keyPanel.add(saveKeyButton);

            // Panel giữa - văn bản & kết quả
            JPanel textPanel = new JPanel(new GridLayout(2, 1, 5, 5));
            JTextArea inputArea = new JTextArea("Nhập văn bản tại đây...");
            JTextArea outputArea = new JTextArea("Kết quả...");
            inputArea.setLineWrap(true);
            outputArea.setLineWrap(true);
            textPanel.add(new JScrollPane(inputArea));
            textPanel.add(new JScrollPane(outputArea));

            // Panel nhập link file và chọn chế độ
            JPanel bottomPanel = new JPanel(new GridLayout(3, 1, 5, 5));
            JPanel filePanel = new JPanel(new BorderLayout(5, 5));
            JLabel fileLabel = new JLabel("File path:");
            JTextField fileField = new JTextField();
            filePanel.add(fileLabel, BorderLayout.WEST);
            filePanel.add(fileField, BorderLayout.CENTER);

            // Panel chế độ mã hoá / giải mã
            JPanel modePanel = new JPanel();
            JRadioButton encryptButton = new JRadioButton("Mã hoá");
            JRadioButton decryptButton = new JRadioButton("Giải mã");
            ButtonGroup modeGroup = new ButtonGroup();
            modeGroup.add(encryptButton);
            modeGroup.add(decryptButton);
            encryptButton.setSelected(true);
            modePanel.add(encryptButton);
            modePanel.add(decryptButton);

            // Panel nút thực hiện
            JPanel actionPanel = new JPanel();
            JButton executeButton = new JButton("Thực hiện");
            actionPanel.add(executeButton);

            bottomPanel.add(filePanel);
            bottomPanel.add(modePanel);
            bottomPanel.add(actionPanel);

            // Thêm vào Frame chính
            frame.add(keyPanel, BorderLayout.NORTH);
            frame.add(textPanel, BorderLayout.CENTER);
            frame.add(bottomPanel, BorderLayout.SOUTH);

            frame.setVisible(true);
        });
		new SubstitutionCipherView();
    }

	}

