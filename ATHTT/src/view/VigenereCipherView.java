package view;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.text.NumberFormat;

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
		this.setGenKey("Tạo khoá");
		this.setSaveKey("Lưu khoá");
		this.setSaveKey("Tải khoá");
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
		keyPanel.add(this.getGenKey());
		keyPanel.add(this.getLoadKey());
		keyPanel.add(this.getSaveKey());
		keyPanel.add(optionKeyPanel);
		keyPanel.add(keyBtnPanel);
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
