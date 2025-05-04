package view;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;


public class SubstitutionCipherView extends ClassicalCipherView {
	private JPanel keyPanel;
	private JTable keyTable;
	private DefaultTableModel tableModel;
	
	public SubstitutionCipherView(int row,String charset) {
		super();
		createFrame(700, 600, "Shift cipher tool");
		createKeyPanel(row, charset);
		createPanelTextCipher();
		
		this.getFrame().setLayout(new BorderLayout(10, 10));
		this.getFrame().add(keyPanel, BorderLayout.NORTH);
		this.getFrame().add(this.getTextPanel(), BorderLayout.CENTER);
		this.getFrame().setVisible(true);
	}

	public void createKeyPanel(int row, String charset) {
		JPanel optionKey = new JPanel(new FlowLayout());
		this.setGenKey("Tạo khoá");
		this.setSaveKey("Lưu khoá");
		this.setLoadKey("Tải khoá");
		optionKey.add(this.getGenKey());
		optionKey.add(this.getLoadKey());
		optionKey.add(this.getSaveKey());
		
		JPanel tablePanel = new JPanel(new FlowLayout());
		JScrollPane scroll = createTabel(row, charset);
		scroll.setPreferredSize(new Dimension(300, 200));
		tablePanel.add(scroll);
		
		JPanel northPanel = new JPanel();
		
		keyPanel = new JPanel(new BorderLayout());
		
		keyPanel.add(northPanel, BorderLayout.NORTH);
		keyPanel.add(tablePanel, BorderLayout.CENTER);
		keyPanel.add(optionKey, BorderLayout.SOUTH);

	}
	
	private JScrollPane createTabel(int row, String charset) {
	    String[] columnNames = {"Kí tự gốc", "Kí tự thay thế"};
	    Object[][] data = new Object[row][2];

	    for (int i = 0; i < row; i++) {
	        data[i][0] = charset.charAt(i);
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
	
	public void updateTableValues(Map<Character, Character> newValues) {
	    int rowCount = tableModel.getRowCount();

	    for (int i = 0; i < rowCount; i++) {
	        Object col0Value = tableModel.getValueAt(i, 0);

	        if (col0Value instanceof Character && newValues != null && newValues.containsKey((Character) col0Value)) {
	            Character newValue = newValues.get((Character) col0Value);
	            tableModel.setValueAt(newValue, i, 1);  // cập nhật cột 1 bằng value từ hashmap
	        } else {
	            tableModel.setValueAt("", i, 1);  // để trống nếu không có trong hashmap
	        }
	    }
	}

	public Map<Character, Character> getTableValues() {
	    if (keyTable.isEditing()) {
	    	keyTable.getCellEditor().stopCellEditing();
	    }

	    int rowCount = tableModel.getRowCount();
	    Map<Character, Character> resultMap = new HashMap<>();

	    for (int i = 0; i < rowCount; i++) {
	        Object keyObj = tableModel.getValueAt(i, 0);  // cột 0
	        Object valueObj = tableModel.getValueAt(i, 1);  // cột 1

	        System.out.println("Row " + i + ": keyObj=" + keyObj + ", valueObj=" + valueObj);

	        if (keyObj != null && valueObj != null) {
	            String keyStr = keyObj.toString().trim();
	            String valueStr = valueObj.toString().trim();

	            if (keyStr.length() == 1 && valueStr.length() == 1) {
	                Character key = keyStr.charAt(0);
	                Character value = valueStr.charAt(0);
	                resultMap.put(key, value);
	            }
	        }
	    }

	    return resultMap;
	}

	@Override
	public void createKeyPanel() {
		// TODO Auto-generated method stub
		
	}
	
	}

