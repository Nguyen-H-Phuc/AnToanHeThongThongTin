package view;

import javax.swing.*;
import java.awt.*;

public class View extends JFrame {
	private JButton substitutionCipherBtn, shiftCipherBtn, symmetricEncryptionBtn, asymmetricEncryptionBtn ;
	private JFrame mainFrame;
	private JPanel mainPanel;

	public View() {
		init();
	}
	
	private void init() {
		mainFrame = new JFrame("Cipher Tool");
		mainFrame.setDefaultCloseOperation(EXIT_ON_CLOSE);
		mainFrame.setSize(600, 500);
		
		mainPanel = new JPanel();
		mainPanel.setLayout(new FlowLayout());
		
		substitutionCipherBtn = new JButton("Mã hoá thay thế");
		shiftCipherBtn = new JButton("Mã hoá dịch chuyển");
		symmetricEncryptionBtn = new JButton("Mã hoá đối xứng");
		asymmetricEncryptionBtn = new JButton("Mã hoá bất đối xứng");
		
		mainPanel.add(substitutionCipherBtn);
		mainPanel.add(shiftCipherBtn);
		mainPanel.add(symmetricEncryptionBtn);
		mainPanel.add(asymmetricEncryptionBtn);
		
		mainFrame.add(mainPanel);
		mainFrame.setLocationRelativeTo(null);
		mainFrame.setVisible(true);
	}
	
	public JButton getSubstitutionCipherBtn() {
		return substitutionCipherBtn;
	}

	public JButton getShiftCipherBtn() {
		return shiftCipherBtn;
	}

	public static void main(String[] args) {
		View view = new View();
	}
}

