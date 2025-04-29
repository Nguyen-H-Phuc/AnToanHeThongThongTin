package view;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FileDialog;
import java.awt.FlowLayout;
import java.awt.Frame;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

public class HashView {
	private JFrame frame;
	private JComboBox<String> algorithms;
	private JLabel optionAlgorithms, inputTextLabel, outputTextLabel, inputFileLabel, outputFileLabel;
	private JTextArea inputText, outputText, outputFile ;
	private JTextField srcFilePath;
	private JButton hashTextBtn, hashFileBtn;
	private JPanel algorithmPanel, textPanel, filePanel;
	
	public HashView() {
		this.frame = new JFrame("Hash");
		this.frame.setLayout(new BorderLayout());
		this.frame.setSize(500, 500);
		this.frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

		setAlgorithmPanel();
		setHashTextPanel();
		setHashFilePanel();

		frame.add(algorithmPanel, BorderLayout.NORTH);
		frame.add(textPanel, BorderLayout.CENTER);
		frame.add(filePanel, BorderLayout.SOUTH);
		this.frame.setVisible(true);
	}
	
	public void setAlgorithmPanel() {
		String[] hashAlgorithms = {
			    "BLAKE2B-160", "BLAKE2B-256", "BLAKE2B-384", "BLAKE2B-512",
			    "BLAKE2S-128", "BLAKE2S-160", "BLAKE2S-224", "BLAKE2S-256",
			    "DSTU7564-256", "DSTU7564-384", "DSTU7564-512",
			    "GOST3411", "GOST3411-2012-256", "GOST3411-2012-512",
			    "HARAKA-256", "HARAKA-512",
			    "KECCAK-224", "KECCAK-256", "KECCAK-288", "KECCAK-384", "KECCAK-512",
			    "MD2", "MD4", "MD5",
			    "RIPEMD128", "RIPEMD160", "RIPEMD256", "RIPEMD320",
			    "SHA-1", "SHA-224", "SHA-256", "SHA-384", "SHA-512", "SHA-512/224", "SHA-512/256",
			    "SHA3-224", "SHA3-256", "SHA3-384", "SHA3-512",
			    "SHAKE128-256", "SHAKE256-512",
			    "Skein-1024-1024", "Skein-1024-384", "Skein-1024-512",
			    "Skein-256-128", "Skein-256-160", "Skein-256-224", "Skein-256-256",
			    "Skein-512-128", "Skein-512-160", "Skein-512-224", "Skein-512-256", "Skein-512-384", "Skein-512-512",
			    "SM3",
			    "TIGER",
			    "WHIRLPOOL"
			};
		algorithms = new JComboBox<>(hashAlgorithms);
		algorithms.setSelectedIndex(0);
		optionAlgorithms = new JLabel("Thuật toán hash:");
		algorithmPanel = new JPanel(new FlowLayout());
		algorithmPanel.add(optionAlgorithms);
		algorithmPanel.add(algorithms);
	}
	
	public void setHashTextPanel() {
		textPanel = new JPanel();
        textPanel.setLayout(new BoxLayout(textPanel, BoxLayout.Y_AXIS));
        textPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Input Panel
        JPanel inputPanel = new JPanel(new BorderLayout());
        this.inputTextLabel = new JLabel("Nhập văn bản", SwingConstants.LEFT);
        inputText = new JTextArea(8, 50);
        inputText.setLineWrap(true);
        inputText.setWrapStyleWord(true);
        JScrollPane scrollInput = new JScrollPane(inputText);
        scrollInput.setPreferredSize(new Dimension(600, 100));
        inputPanel.add(inputTextLabel, BorderLayout.NORTH);
        inputPanel.add(scrollInput, BorderLayout.CENTER);

        // Output Panel
        JPanel outputPanel = new JPanel(new BorderLayout());
        outputTextLabel = new JLabel("Kết quả", SwingConstants.LEFT);
        outputText = new JTextArea(8, 50);
        outputText.setLineWrap(true);
        outputText.setWrapStyleWord(true);
        JScrollPane scrollOutput = new JScrollPane(outputText);
        scrollOutput.setPreferredSize(new Dimension(600, 100));
        outputPanel.add(outputTextLabel, BorderLayout.NORTH);
        outputPanel.add(scrollOutput, BorderLayout.CENTER);
        
        JPanel hashTextBtnPanel = new JPanel();
        hashTextBtn = new JButton("Hash chuỗi");
        hashTextBtnPanel.add(hashTextBtn);
        
        textPanel.add(inputPanel);
        textPanel.add(Box.createVerticalStrut(10));
        textPanel.add(outputPanel);
        textPanel.add(Box.createVerticalStrut(10)); 
        textPanel.add(hashTextBtnPanel);
	}	
	
	public void setHashFilePanel() {
	    filePanel = new JPanel();
	    filePanel.setLayout(new BoxLayout(filePanel, BoxLayout.Y_AXIS));
	    filePanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

	    // === Source file panel ===
	    inputFileLabel = new JLabel("Đường dẫn file nguồn:",SwingConstants.LEFT);
	    srcFilePath = new JTextField();
	    JButton browseSourceBtn = new JButton("...");

	    JPanel sourceInputPanel = new JPanel(new BorderLayout());
	    sourceInputPanel.add(srcFilePath, BorderLayout.CENTER);
	    sourceInputPanel.add(browseSourceBtn, BorderLayout.EAST);

	    JPanel sourcePanel = new JPanel();
	    sourcePanel.setLayout(new BorderLayout());
	    sourcePanel.add(inputFileLabel, BorderLayout.NORTH);
	    sourcePanel.add(Box.createVerticalStrut(5));
	    sourcePanel.add(sourceInputPanel, BorderLayout.CENTER);

	    // === Output file panel ===
	    outputFileLabel = new JLabel("Kết quả hash file:", SwingConstants.LEFT);
	    outputFile = new JTextArea(4, 50);
	    outputFile.setLineWrap(true);
	    outputFile.setWrapStyleWord(true);
	    JScrollPane outputScrollPane = new JScrollPane(outputFile);

	    JPanel outputPanel = new JPanel();
	    outputPanel.setLayout(new BorderLayout());
	    outputPanel.add(outputFileLabel, BorderLayout.NORTH);
	    outputPanel.add(Box.createVerticalStrut(5));
	    outputPanel.add(outputScrollPane, BorderLayout.CENTER);

	    // === Button panel ===
	    JPanel fileBtnPanel = new JPanel();
	    hashFileBtn = new JButton("Hash file");
	    fileBtnPanel.add(hashFileBtn);

	    // === Add to file panel ===
	    filePanel.add(sourcePanel);
	    filePanel.add(Box.createVerticalStrut(10));
	    filePanel.add(outputPanel);
	    filePanel.add(Box.createVerticalStrut(10));
	    filePanel.add(fileBtnPanel);


	    // === Browse button action ===
	    browseSourceBtn.addActionListener(e -> {
	        FileDialog fileDialog = new FileDialog((Frame) null, "Chọn file nguồn", FileDialog.LOAD);
	        fileDialog.setVisible(true);
	        if (fileDialog.getFile() != null) {
	            String selectedFile = fileDialog.getDirectory() + fileDialog.getFile();
	            srcFilePath.setText(selectedFile);
	        }
	    });
	}
		
	public String getInputText() {
		return inputText.getText();
	}

	public void setInputText(JTextArea inputText) {
		this.inputText = inputText;
	}

	public JTextArea getOutputText() {
		return outputText;
	}

	public void setOutputText(String outputText) {
		this.outputText.setText(outputText);;
	}

	public JTextArea getOutputFile() {
		return outputFile;
	}

	public void setOutputFile(String outputFile) {
		this.outputFile.setText(outputFile);
	}

	public String getSrcFilePath() {
		return srcFilePath.getText();
	}

	public JButton getHashTextBtn() {
		return hashTextBtn;
	}

	public JButton getHashFileBtn() {
		return hashFileBtn;
	}

	public String getAlgorithms() {
		return (String) algorithms.getSelectedItem();
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
}
