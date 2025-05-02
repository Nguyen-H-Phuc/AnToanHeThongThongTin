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
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JTable;
import javax.swing.SpinnerNumberModel;
import javax.swing.table.DefaultTableModel;
import javax.swing.text.DefaultFormatterFactory;
import javax.swing.text.NumberFormatter;

public class PermutationCipherView extends ClassicalCipherView {
	private JPanel keyPanel;
	private JSpinner spinner;
	private JTable keyTable;
	private DefaultTableModel tableModel;

	public PermutationCipherView() {
		super();
		
		createFrame(700, 600, "Permutation cipher tool");
		createKeyPanel();
		createPanelTextCipher();
		
		
		this.getFrame().setLayout(new BorderLayout(10, 10));
		this.getFrame().add(keyPanel, BorderLayout.NORTH);
		this.getFrame().add(this.getTextPanel(), BorderLayout.CENTER);		
		this.getFrame().setVisible(true);
	}

	@Override
	public void createKeyPanel() {
		SpinnerNumberModel model = new SpinnerNumberModel(1, 1, 20, 1);
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
		
		JPanel optionKey = new JPanel(new FlowLayout());
		this.setGenKey("Tạo khoá");
		this.setSaveKey("Lưu khoá");
		this.setLoadKey("Tải khoá");
		optionKey.add(this.getGenKey());
		optionKey.add(this.getLoadKey());
		optionKey.add(this.getSaveKey());
		
		JPanel tablePanel = new JPanel(new FlowLayout());
		JScrollPane scroll = createTabel(20);
		scroll.setPreferredSize(new Dimension(300, 200));
		tablePanel.add(scroll);
		
		JPanel northPanel = new JPanel();
		JLabel keyLength = new JLabel("Độ dài khoá:");
		northPanel.add(keyLength);
		northPanel.add(spinner);
		
		keyPanel = new JPanel(new BorderLayout());
		
		keyPanel.add(northPanel, BorderLayout.NORTH);
		keyPanel.add(tablePanel, BorderLayout.CENTER);
		keyPanel.add(optionKey, BorderLayout.SOUTH);

	}
	
	private JScrollPane createTabel(int row) {
	    String[] columnNames = {"Index", "Value"};
	    Object[][] data = new Object[row][2];

	    for (int i = 0; i < row; i++) {
	        data[i][0] = i;
	        data[i][1] = "";     // value để trống
	    }

	    tableModel = new DefaultTableModel(data, columnNames) {
	        @Override
	        public boolean isCellEditable(int row, int column) {
	            return column == 1;  // chỉ cột "Value" cho phép chỉnh sửa
	        }
	    };

	    keyTable = new JTable(tableModel);
	    JScrollPane scrollPane = new JScrollPane(keyTable);
	    return scrollPane;
	}
	
	public void updateTableValues(int[] newValues) {
	    int rowCount = tableModel.getRowCount();

	    for (int i = 0; i < rowCount; i++) {
	        if (newValues == null || i >= newValues.length) {
	            tableModel.setValueAt("", i, 1);
	        } else {
	            tableModel.setValueAt(newValues[i], i, 1);  // cột 1 là "Value"
	        }
	    }
	}

	
	public int[] getTableValues() {
	    int rowCount = tableModel.getRowCount();
	    int[] values = new int[rowCount];
	    int actualLength = 0;

	    for (int i = 0; i < rowCount; i++) {
	        Object cellValue = tableModel.getValueAt(i, 1);  // cột 1 là "Value"
	        if (cellValue == null || cellValue.toString().trim().isEmpty()) {
	            break;
	        } else {
	            try {
	                values[i] = Integer.parseInt(cellValue.toString());
	                actualLength++;  // đếm số phần tử hợp lệ
	            } catch (NumberFormatException e) {
	                showDialogMessage("Vị trí " + i + " không phải số.", "ERROR");
	                break;
	            }
	        }
	    }

	    // Tạo mảng có độ dài đúng bằng actualLength
	    int[] result = new int[actualLength];
	    System.arraycopy(values, 0, result, 0, actualLength);

	    return result;
	}
	
	public int getValueSpinner() {
		return (int) spinner.getValue();
	}
	



public static void main(String[] args) {
	new PermutationCipherView();
}
}
