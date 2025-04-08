package view;

import javax.swing.*;
import java.awt.*;

public class View extends JFrame {
	private JButton classicEncryptBtn, symmetricEncryptionbtn, asymmetricEncryptionbtn ;
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
		
		classicEncryptBtn = new JButton("Mã hoá cổ điển");
		symmetricEncryptionbtn = new JButton("Mã hoá đối xứng");
		asymmetricEncryptionbtn = new JButton("Mã hoá bất đối xứng");
		
		mainPanel.add(classicEncryptBtn);
		mainPanel.add(symmetricEncryptionbtn);
		mainPanel.add(asymmetricEncryptionbtn);
		
		mainFrame.add(mainPanel);
		mainFrame.setLocationRelativeTo(null);
		mainFrame.setVisible(true);
	}
	
	public static void main(String[] args) {
		View view = new View();
	}
}

