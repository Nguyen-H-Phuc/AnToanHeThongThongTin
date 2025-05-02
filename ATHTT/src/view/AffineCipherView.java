package view;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.text.NumberFormat;

import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import javax.swing.text.DefaultFormatterFactory;
import javax.swing.text.NumberFormatter;

public class AffineCipherView extends ClassicalCipherView {
	JPanel keyPanel;
	JSpinner spinner1, spinner2;

	public AffineCipherView() {
		super();
		createFrame(700, 600, "Affine cipher tool");
		
		createKeyPanel();
		createPanelTextCipher();
		
		this.getFrame().setLayout(new BorderLayout(10, 10));
		this.getFrame().add(keyPanel, BorderLayout.NORTH);
		this.getFrame().add(this.getTextPanel(), BorderLayout.CENTER);
		
		this.getFrame().setVisible(true);
	}

	@Override
	public void createKeyPanel() {
		SpinnerNumberModel model1 = new SpinnerNumberModel(1, 1, 178, 1);
		SpinnerNumberModel model2 = new SpinnerNumberModel(1, 1, 178, 1);
		spinner1 = new JSpinner(model1);
		spinner2 = new JSpinner(model2);
		
		// Tạo formatter để chỉ cho nhập số
		NumberFormat format = NumberFormat.getIntegerInstance();
		format.setGroupingUsed(false);

		NumberFormatter formatter = new NumberFormatter(format);
		formatter.setAllowsInvalid(false);
		formatter.setMinimum(0);
		formatter.setMaximum(178);

		JFormattedTextField tf1 = ((JSpinner.NumberEditor) spinner1.getEditor()).getTextField();
		JFormattedTextField tf2 = ((JSpinner.NumberEditor) spinner2.getEditor()).getTextField();
		tf1.setFormatterFactory(new DefaultFormatterFactory(formatter));
		tf2.setFormatterFactory(new DefaultFormatterFactory(formatter));
		
		this.setGenKey("Tạo khoá");
		this.setLoadKey("Tải khoá");
		this.setSaveKey("Lưu khoá");
		
		JLabel a = new JLabel(" a:");
		JLabel b = new JLabel("  b:");
		
		keyPanel = new JPanel(new FlowLayout());
		keyPanel.add(a);
		keyPanel.add(spinner1);
		keyPanel.add(b);
		keyPanel.add(spinner2);
		keyPanel.add(this.getGenKey());
		keyPanel.add(this.getLoadKey());
		keyPanel.add(this.getSaveKey());

	}
	
	public void setValueSpinner1(int value) {
		spinner1.setValue(value);
	}
	
	public int getValueSpinner1() {
		return (int) spinner1.getValue();
	}
	
	public void setValueSpinner2(int value) {
		spinner2.setValue(value);
	}
	
	public int getValueSpinner2() {
		return (int) spinner2.getValue();
	}

}
