package controller;

import model.SubstitutionCipher;
import view.CipherView;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class CipherController {
    private CipherView view;
    private SubstitutionCipher model;

    public CipherController(CipherView view, SubstitutionCipher model) {
        this.view = view;
        this.model = model;

        // Đăng ký sự kiện cho nút "Mã hóa" và "Giải mã"
        this.view.getEncryptButton().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                handleEncrypt();
            }
        });

        this.view.getDecryptButton().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                handleDecrypt();
            }
        });
    }

    private void handleEncrypt() {
        String plaintext = view.getInputField().getText();
        String encryptedText = this.model.encrypt(plaintext);
        view.getEncryptedTextArea().setText(encryptedText);
    }

    private void handleDecrypt() {
        String encryptedText = view.getEncryptedTextArea().getText();
        String decryptedText = model.decrypt(encryptedText);
        view.getDecryptedTextArea().setText(decryptedText);
    }
    
    public static void main(String[] args) {
        CipherView view = new CipherView();
        SubstitutionCipher model = new SubstitutionCipher();
        new CipherController(view, model);
        System.out.println(model.encrypt("Hello"));
    }
}
