package view;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class RSAView extends ClassicalCipherView {
	private JComboBox<String> algorithm;
	private JComboBox<Integer> keySize;
	private JPanel keyPanel;
	private JTextField publicKey, privateKey;
	private JLabel publicKeyLabel, privateKeyLabel, algorithmLable, keySizeLabel;
	private JButton savePrivateKey, loadPublicKey, loadPrivateKey;
	
	public RSAView() {
		super();
		createFrame(700, 600, "RSA cipher tool");
		
		createPanelTextCipher();
		createFileCipherPanel();
		createKeyPanel();
		
		this.getFrame().setLayout(new BorderLayout(10, 10));
		this.getFrame().add(keyPanel, BorderLayout.NORTH);
		this.getFrame().add(this.getTextPanel(), BorderLayout.CENTER);
		this.getFrame().add(this.getFilePanel(), BorderLayout.SOUTH);
		this.getFrame().setVisible(true);
	}
	
	@Override
	public void createKeyPanel() {
		String[] rsaInstance = {"RSA/ECB/PKCS1Padding", "RSA/ECB/OAEPWithSHA-1AndMGF1Padding",
				"RSA/ECB/OAEPWithSHA-256AndMGF1Padding"};

		algorithm = new JComboBox<String>(rsaInstance);
		algorithm.setSelectedIndex(0);
		algorithm.setSize(new Dimension(20, 20));
		algorithmLable = new JLabel("Thuật toán:");
		
		Integer[] keySizes = {1024, 2048, 3072, 4096};
		this.keySize = new JComboBox<Integer>(keySizes);
		keySizeLabel = new JLabel("Chiều dài khoá:");
		
		JPanel algorithmPanel = new JPanel();
		algorithmPanel.setLayout(new FlowLayout());
		algorithmPanel.add(algorithmLable);
		algorithmPanel.add(algorithm);
		algorithmPanel.add(keySizeLabel);
		algorithmPanel.add(keySize);

		setGenKey("Tạo khoá");
		setLoadKey("Tải khoá công khai");
		setSaveKey("Lưu khoá công khai");
		loadPrivateKey = new JButton("Tải khoá riêng tư");
		savePrivateKey = new JButton("Lưu khoá riêng tư");
		
		JPanel optionKeyBtn = new JPanel(new GridLayout(3,2,5,5));
		optionKeyBtn.add(getGenKey());
		optionKeyBtn.add(getLoadKey());
		optionKeyBtn.add(getSaveKey());
		optionKeyBtn.add(loadPrivateKey);
		optionKeyBtn.add(savePrivateKey);
		
		publicKeyLabel = new JLabel("Khoá công khai (Base64):");
		publicKey = new JTextField();
		
		privateKeyLabel = new JLabel("Khoá riêng tư (Base64):");
		privateKey = new JTextField();
		
		JPanel optionKeyPanel = new JPanel(new GridLayout(2,2,5,10));
		optionKeyPanel.add(publicKeyLabel);
		optionKeyPanel.add(publicKey);
		optionKeyPanel.add(privateKeyLabel);
		optionKeyPanel.add(privateKey);
		
		keyPanel= new JPanel(new BorderLayout());
		keyPanel.add(algorithmPanel, BorderLayout.NORTH);
		keyPanel.add(optionKeyPanel, BorderLayout.CENTER);
		keyPanel.add(optionKeyBtn, BorderLayout.SOUTH);
		
	}
	
	public String getAlgorithm() {
		return (String) this.algorithm.getSelectedItem();
	}
	
	public int getKeySize() {
		return (int) keySize.getSelectedItem();
	}
	
	public String getPublicKey() {
		return this.publicKey.getText();
	}
	
	public void setPublicKey(String publicKey) {
		this.publicKey.setText(publicKey);
	}
	
	public String getPrivateKey() {
		return this.privateKey.getText();
	}
	
	public void setPrivateKey(String privateKey) {
		this.privateKey.setText(privateKey);
	}

	public JButton getSavePrivateKey() {
		return savePrivateKey;
	}

	public JButton getLoadPublicKey() {
		return loadPublicKey;
	}

	public JButton getLoadPrivateKey() {
		return loadPrivateKey;
	}
	
}
