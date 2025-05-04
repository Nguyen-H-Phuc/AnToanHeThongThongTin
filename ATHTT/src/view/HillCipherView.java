package view;

import java.awt.BorderLayout;
import java.awt.FlowLayout;

import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

public class HillCipherView extends ClassicalCipherView {
	private JPanel keyPanel;
	private JTable keyTable;
	private DefaultTableModel tableModel;

	public HillCipherView(int row) {
		super();
		createFrame(700, 600, "Hill cipher tool");
		createKeyPanel(row);
		createPanelTextCipher();
		
		
		this.getFrame().setLayout(new BorderLayout(10, 10));
		this.getFrame().add(keyPanel, BorderLayout.NORTH);
		this.getFrame().add(this.getTextPanel(), BorderLayout.CENTER);		
		this.getFrame().setVisible(true);
	}

	public void createKeyPanel(int row) {
		JPanel optionKey = new JPanel(new FlowLayout());
		this.setGenKey("Tạo khoá");
		this.setSaveKey("Lưu khoá");
		this.setLoadKey("Tải khoá");
		optionKey.add(this.getGenKey());
		optionKey.add(this.getLoadKey());
		optionKey.add(this.getSaveKey());
		
		JPanel tablePanel = new JPanel(new FlowLayout());
		createTabel(2);
		
		tablePanel.add(keyTable);
		
		JPanel northPanel = new JPanel();
		
		keyPanel = new JPanel(new BorderLayout());
		
		keyPanel.add(northPanel, BorderLayout.NORTH);
		keyPanel.add(tablePanel, BorderLayout.CENTER);
		keyPanel.add(optionKey, BorderLayout.SOUTH);

	}
	
	private void createTabel(int row) {
	    String[] columnNames = {"", ""};
	    Object[][] data = new Object[row][2];

	    tableModel = new DefaultTableModel(data, columnNames) {
	        @Override
	        public boolean isCellEditable(int row, int column) {
	            return true;
	        }
	    };

	    keyTable = new JTable(tableModel);

	    // Set tất cả các ô về 0
	    for (int i = 0; i < row; i++) {
	        for (int j = 0; j < 2; j++) {
	            tableModel.setValueAt(0, i, j);
	        }
	    }
	}

	
	public int[][] getValuesTable() {
		if (keyTable.isEditing()) {
	    	keyTable.getCellEditor().stopCellEditing();
	    }
		
	    int rowCount = keyTable.getRowCount();
	    int colCount = keyTable.getColumnCount();
	    int[][] result = new int[rowCount][colCount];

	    for (int row = 0; row < rowCount; row++) {
	        for (int col = 0; col < colCount; col++) {
	            Object value = keyTable.getValueAt(row, col);
	            if (value == null || value.toString().trim().isEmpty()) {
//	                this.showDialogMessage(
//	                    "Ô tại dòng " + (row + 1) + ", cột " + (col + 1) + " đang trống.",
//	                    "ERROR");
	                return null;
	            }
	            try {
	                result[row][col] = Integer.parseInt(value.toString().trim());
	            } catch (NumberFormatException e) {
	            	this.showDialogMessage(
	                    "Ô tại dòng " + (row + 1) + ", cột " + (col + 1) + " không phải số nguyên. Vui lòng cập nhật lại.",
	                    "ERROR");
	                return null;
	            }
	        }
	    }
	    return result;
	}
	
	public void updateValuesTable(int[][] values) {
	    int rowCount = keyTable.getRowCount();
	    int colCount = keyTable.getColumnCount();
	    
	    if(values ==null) {
	    	return;
	    }
	    // Kiểm tra kích thước mảng có khớp không
	    if (values.length != rowCount || values[0].length != colCount) {
	    	this.showDialogMessage(
	            "Kích thước mảng không khớp với bảng.",
	            "ERROR");
	        return;
	    }

	    // Gán giá trị vào bảng
	    for (int row = 0; row < rowCount; row++) {
	        for (int col = 0; col < colCount; col++) {
	            keyTable.setValueAt(values[row][col], row, col);
	        }
	    }
	}


	@Override
	public void createKeyPanel() {
		// TODO Auto-generated method stub
		
	}
public static void main(String[] args) {
	new HillCipherView(2);
}
}
