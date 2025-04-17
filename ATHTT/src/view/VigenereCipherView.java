package view;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.text.NumberFormat;

import javax.swing.JButton;
import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.SpinnerNumberModel;
import javax.swing.text.DefaultFormatterFactory;
import javax.swing.text.NumberFormatter;

public class VigenereCipherView extends ClassicalCipherView {
	JPanel keyPanel;
	JButton genKey, saveKey, loadKey;
	JSpinner spinner;
	JTextField key;

	public VigenereCipherView() {
		super();
		createFrame(700, 600, "Vigenere cipher tool");
		createPanelTextCipher();
		createFileCipherPanel();
		createKeyPanel();
		this.getFrame().setLayout(new BorderLayout(10, 10));
		this.getFrame().add(this.getTextPanel(), BorderLayout.CENTER);
		this.getFrame().add(this.getFilePanel(), BorderLayout.SOUTH);
		this.getFrame().add(keyPanel, BorderLayout.NORTH);
		this.getFrame().setVisible(true);
	}

	@Override
	public void createKeyPanel() {
		SpinnerNumberModel model = new SpinnerNumberModel(1, 1, 100, 1);
		spinner = new JSpinner(model);
		NumberFormat format = NumberFormat.getIntegerInstance();
		format.setGroupingUsed(false);

		NumberFormatter formatter = new NumberFormatter(format);
		formatter.setAllowsInvalid(false);
		formatter.setMinimum(0);
		formatter.setMaximum(100);

		JFormattedTextField tf = ((JSpinner.NumberEditor) spinner.getEditor()).getTextField();
		tf.setFormatterFactory(new DefaultFormatterFactory(formatter));
		
		key = new JTextField();
		genKey = new JButton("Tạo khoá");
		saveKey = new JButton("Lưu khoá");
		loadKey = new JButton("Tải khoá");
		keyPanel = new JPanel(new GridLayout(2,1));
		JPanel optionKeyPanel = new JPanel(new FlowLayout());
		key.setPreferredSize(new Dimension(200, 25));
		JLabel labelKey = new JLabel("Khoá");
		JLabel keyLengthLabel = new JLabel("Chiều dài khoá");
		optionKeyPanel.add(keyLengthLabel);
		optionKeyPanel.add(spinner);
		optionKeyPanel.add(labelKey);
		optionKeyPanel.add(key);
		
		JPanel keyBtnPanel = new JPanel(new FlowLayout());
		keyBtnPanel.add(genKey);
		keyBtnPanel.add(loadKey);
		keyBtnPanel.add(saveKey);
		keyPanel.add(optionKeyPanel);
		keyPanel.add(keyBtnPanel);
	}
	
	public JButton getGenKey() {
		return genKey;
	}

	public void setGenKey(JButton genKey) {
		this.genKey = genKey;
	}

	public JButton getSaveKey() {
		return saveKey;
	}

	public void setSaveKey(JButton saveKey) {
		this.saveKey = saveKey;
	}

	public JButton getLoadKey() {
		return loadKey;
	}

	public void setLoadKey(JButton loadKey) {
		this.loadKey = loadKey;
	}

	public void setValueSpinner(int value) {
		spinner.setValue(value);
	}
	
	public int getValueSpinner() {
		return (int) spinner.getValue();
	}
	
	public String getKey() {
		return this.key.getText();
	}
	
	public void setKey(String key) {
		this.key.setText(key);
	}

	public static void main(String[] args) {
		new VigenereCipherView();
	}
}
