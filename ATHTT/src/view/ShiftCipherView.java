package view;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.text.NumberFormat;

import javax.swing.JFormattedTextField;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import javax.swing.text.DefaultFormatterFactory;
import javax.swing.text.NumberFormatter;

public class ShiftCipherView extends ClassicalCipherView {
	JPanel keyPanel;
	JSpinner spinner;
	
	public ShiftCipherView() {
		super();
		createFrame(700, 600, "Shift cipher tool");
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
		SpinnerNumberModel model = new SpinnerNumberModel(0, 0, 100, 1);
		spinner = new JSpinner(model);

		// Tạo formatter để chỉ cho nhập số
		NumberFormat format = NumberFormat.getIntegerInstance();
		format.setGroupingUsed(false);

		NumberFormatter formatter = new NumberFormatter(format);
		formatter.setAllowsInvalid(false);
		formatter.setMinimum(0);
		formatter.setMaximum(100);

		JFormattedTextField tf = ((JSpinner.NumberEditor) spinner.getEditor()).getTextField();
		tf.setFormatterFactory(new DefaultFormatterFactory(formatter));
		keyPanel = new JPanel();
		this.setGenKey("Tạo khoá");
		this.setSaveKey("Lưu khoá");
		this.setLoadKey("Tải khoá");
		keyPanel = new JPanel(new FlowLayout());
		keyPanel.add(spinner);
		keyPanel.add(this.getGenKey());
		keyPanel.add(this.getLoadKey());
		keyPanel.add(this.getSaveKey());

	}
	
	public void setValueSpinner(int value) {
		spinner.setValue(value);
	}
	
	public int getValueSpinner() {
		return (int) spinner.getValue();
	}
	

}
