package view;

import javax.swing.*;
import java.awt.*;

public class CipherView extends JFrame {
    private JTextField inputField;
    private JTextArea encryptedTextArea;
    private JTextArea decryptedTextArea;
    private JButton encryptButton;
    private JButton decryptButton;

    public CipherView() {
        setTitle("Substitution Cipher");
        setSize(500, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());
        
        // Panel nhập văn bản
        JPanel inputPanel = new JPanel(new BorderLayout());
        inputField = new JTextField();
        inputPanel.add(new JLabel("Nhập văn bản:"), BorderLayout.NORTH);
        inputPanel.add(inputField, BorderLayout.CENTER);
        
        // Panel chứa văn bản đã mã hóa và giải mã
        JPanel textPanel = new JPanel(new GridLayout(2, 1));
        encryptedTextArea = new JTextArea();
        decryptedTextArea = new JTextArea();
        encryptedTextArea.setEditable(false);
        decryptedTextArea.setEditable(false);
        textPanel.add(new JScrollPane(encryptedTextArea));
        textPanel.add(new JScrollPane(decryptedTextArea));
        
        // Panel chứa nút bấm
        JPanel buttonPanel = new JPanel();
        encryptButton = new JButton("Mã hóa");
        decryptButton = new JButton("Giải mã");
        buttonPanel.add(encryptButton);
        buttonPanel.add(decryptButton);
        
        // Thêm vào frame
        add(inputPanel, BorderLayout.NORTH);
        add(textPanel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);
        setVisible(true);
    }
    
    // Getter cho các thành phần giao diện
    public JTextField getInputField() { return inputField; }
    public JTextArea getEncryptedTextArea() { return encryptedTextArea; }
    public JTextArea getDecryptedTextArea() { return decryptedTextArea; }
    public JButton getEncryptButton() { return encryptButton; }
    public JButton getDecryptButton() { return decryptButton; }

}

