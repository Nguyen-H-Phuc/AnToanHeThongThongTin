package view;

import javax.swing.*;
import java.awt.*;

public class View  {
	private JButton substitutionCipherBtn, shiftCipherBtn, vigenereCipherBtn, permutationCipherBtn, hillCipherBtn,
			affineCipherBtn, symmetricCipherBtn, asymmetricCipherBtn, hashBtn;
	private JFrame mainFrame;
	private JPanel mainPanel;

	public View() {
		init();
	}
	
	private void init() {
		mainFrame = new JFrame("Cipher Tool");
		mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		mainFrame.setSize(500, 300);
		
		mainPanel = new JPanel();
		mainPanel.setLayout(new FlowLayout());
		
		substitutionCipherBtn = new JButton("Mã hoá thay thế");
		shiftCipherBtn = new JButton("Mã hoá dịch chuyển");
		vigenereCipherBtn = new JButton("Mã hoá vigenere");
		permutationCipherBtn = new JButton("Mã hoá hoán vị");
		affineCipherBtn = new JButton("Mã hoá Affine");
		hillCipherBtn = new JButton("Mã hoá hill");
		
		symmetricCipherBtn = new JButton("Mã hoá đối xứng");
		asymmetricCipherBtn = new JButton("Mã hoá bất đối xứng");
		hashBtn = new JButton("Hash");
		
		mainPanel.add(substitutionCipherBtn);
		mainPanel.add(shiftCipherBtn);
		mainPanel.add(vigenereCipherBtn);
		mainPanel.add(permutationCipherBtn);
		mainPanel.add(affineCipherBtn);
		mainPanel.add(hillCipherBtn);
		
		mainPanel.add(symmetricCipherBtn);
		mainPanel.add(asymmetricCipherBtn);
		mainPanel.add(hashBtn);
		
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

	public JButton getVigenereCipherBtn() {
		return vigenereCipherBtn;
	}

	public JButton getPermutationCipher() {
		return permutationCipherBtn;
	}
	
	public JButton getHillCipherBtn() {
		return hillCipherBtn;
	}

	public JButton getAffineCipherBtn() {
		return affineCipherBtn;
	}

	public JButton getSymmetricCipherBtn(){
		return this.symmetricCipherBtn;
	}
	
	public JButton getAsymmetricCipherBtn() {
		return asymmetricCipherBtn;
	}
	
	public JButton getHashBtn() {
		return hashBtn;
	}

}

