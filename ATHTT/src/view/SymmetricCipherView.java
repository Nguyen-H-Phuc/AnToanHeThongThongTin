package view;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.util.HashMap;
import java.util.Map;

import javax.swing.BorderFactory;
import javax.swing.ComboBoxModel;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class SymmetricCipherView extends ClassicalCipherView {
	private JComboBox<String> algorithmBox;
	private JComboBox<String> keySizeBox;
	private JComboBox<String> modeBox;
	private JComboBox<String> paddingBox;
	private JComboBox<String> blockSizeBox;
	private JComboBox<String> charsetBox;
	private JPanel keyPanel;	
	private final Map<String, String[]> keySizes = createKeySizes();
	private final Map<String, String[]> modes = createModes();
//	private final Map<String, String[]> paddings = createPaddings();
	private final Map<String, String[]> blockSizes = createBlockSizes();
	private JTextField key;
	
	public SymmetricCipherView() {
		super();
		createFrame(700, 600, "Mã hoá đối xứng");
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
	    keyPanel = new JPanel(new BorderLayout());
	    
	    JPanel optionAlgorithmPanel = new JPanel(new GridLayout(7,2,5,2));
	    algorithmBox = new JComboBox<>(new String[]{
	        "AES", "DES", "Blowfish", "Camellia", "CAST5", "RC5", "TEA",
	        "DESede", "Twofish", "XTEA"});
	    
	    key = new JTextField();	 
	    keySizeBox = new JComboBox<>();
	    modeBox = new JComboBox<>();
	    paddingBox = new JComboBox<>(new String[]{"PKCS5Padding", "PKCS7Padding", "ISO10126Padding", "NoPadding"});
	    blockSizeBox = new JComboBox<>();
	    charsetBox = new JComboBox<>(new String[]{"UTF-8","UTF-16", "ISO-8859-1", "US-ASCII"});

	    algorithmBox.addActionListener(e -> updateAlgorithmDetails());

	    JLabel algoLabel = new JLabel("Thuật toán:");
	    algoLabel.setBorder(BorderFactory.createEmptyBorder(0, 5, 0, 0));
	    optionAlgorithmPanel.add(algoLabel);
	    optionAlgorithmPanel.add(algorithmBox);

	    JLabel keySizeLabel = new JLabel("Kích thước khóa (bits):");
	    keySizeLabel.setBorder(BorderFactory.createEmptyBorder(0, 5, 0, 0));
	    optionAlgorithmPanel.add(keySizeLabel);
	    optionAlgorithmPanel.add(keySizeBox);

	    JLabel modeLabel = new JLabel("Mode:");
	    modeLabel.setBorder(BorderFactory.createEmptyBorder(0, 5, 0, 0));
	    optionAlgorithmPanel.add(modeLabel);
	    optionAlgorithmPanel.add(modeBox);

	    JLabel paddingLabel = new JLabel("Padding:");
	    paddingLabel.setBorder(BorderFactory.createEmptyBorder(0, 5, 0, 0));
	    optionAlgorithmPanel.add(paddingLabel);
	    optionAlgorithmPanel.add(paddingBox);

	    JLabel blockSizeLabel = new JLabel("Block size:");
	    blockSizeLabel.setBorder(BorderFactory.createEmptyBorder(0, 5, 0, 0));
	    optionAlgorithmPanel.add(blockSizeLabel);
	    optionAlgorithmPanel.add(blockSizeBox);

	    JLabel charsetLabel = new JLabel("Charset:");
	    charsetLabel.setBorder(BorderFactory.createEmptyBorder(0, 5, 0, 0));
	    optionAlgorithmPanel.add(charsetLabel);
	    optionAlgorithmPanel.add(charsetBox);
	    
	    JLabel keyLabel = new JLabel("Khoá:");
	    keyLabel.setBorder(BorderFactory.createEmptyBorder(0, 5, 0, 0));   
	    optionAlgorithmPanel.add(keyLabel);
	    optionAlgorithmPanel.add(key);

	    keyPanel.add(optionAlgorithmPanel, BorderLayout.CENTER);
	    
	    setGenKey("Tạo khoá");
	    setSaveKey("Lưu khoá");
	    setLoadKey("Tải khoá");
	    JPanel optionKeyPanel = new JPanel(new FlowLayout());
	    optionKeyPanel.add(getGenKey());
	    optionKeyPanel.add(getSaveKey());
	    optionKeyPanel.add(getLoadKey());
	    
	    keyPanel.add(optionKeyPanel, BorderLayout.SOUTH);
	    keyPanel.setBorder(BorderFactory.createTitledBorder("Cài đặt thuật toán"));
	    updateAlgorithmDetails();
	}

	private Map<String, String[]> createKeySizes() {
	    Map<String, String[]> map = new HashMap<>();
	    
	    map.put("AES", new String[]{"128", "192", "256"});
	    map.put("DES", new String[]{"56"});
	    map.put("Blowfish", new String[]{"32", "64", "96", "128", "160", "192", "224", "256", "320", "384", "448"});
	    map.put("Camellia", new String[]{"128", "192", "256"});
	    map.put("CAST5", new String[]{"40", "48", "56", "64", "72", "80", "88", "96", "104", "112", "120", "128"});
	    map.put("TEA", new String[]{"128"});
	    map.put("RC5", new String[]{"64", "128", "192", "256"});
	    map.put("DESede", new String[]{"112", "168"});
	    map.put("Twofish", new String[]{"128", "192", "256"});
	    map.put("XTEA", new String[]{"128"});
	    
	    return map;
	}
   
    private Map<String, String[]> createModes() {
        Map<String, String[]> modes = new HashMap<>();

        modes.put("AES", new String[]{"ECB", "CBC", "CFB", "OFB", "CTR", "GCM", "CCM", "PCBC"});
        modes.put("DES", new String[]{"ECB", "CBC", "CFB", "OFB", "CTR","PCBC"});
        modes.put("Blowfish", new String[]{"ECB", "CBC", "CFB", "OFB", "CTR"});
        modes.put("Camellia", new String[]{"ECB", "CBC", "CFB", "OFB", "CTR", "CCM"});
        modes.put("CAST5", new String[]{"ECB", "CBC", "CFB", "OFB"});
        modes.put("RC5", new String[]{"ECB", "CBC", "CFB", "OFB", "CTR"});
        modes.put("TEA", new String[]{"ECB", "CBC", "CFB", "OFB", "CTR"});
        modes.put("DESede", new String[]{"ECB", "CBC", "CFB", "OFB", "CTR"});
        modes.put("Twofish", new String[]{"ECB", "CBC", "CFB", "OFB", "CTR"});
        modes.put("XTEA", new String[]{"ECB", "CBC", "CFB", "OFB", "CTR"});
        
        return modes;
    }

//    private Map<String, String[]> createPaddings() {
//        Map<String, String[]> paddings = new HashMap<>();
//
//        paddings.put("AES", new String[]{"PKCS5Padding", "PKCS7Padding", "ISO10126Padding", "NoPadding"});
//        paddings.put("DES", new String[]{"PKCS5Padding", "PKCS7Padding", "ISO10126Padding", "NoPadding"});
//        paddings.put("Blowfish", new String[]{"PKCS5Padding", "PKCS7Padding","ISO10126Padding", "NoPadding"});
//        paddings.put("Camellia", new String[]{"PKCS5Padding", "PKCS7Padding", "ISO10126Padding", "NoPadding"});
//        paddings.put("CAST5", new String[]{"PKCS5Padding", "PKCS7Padding", "ISO10126Padding", "NoPadding"});
//        paddings.put("RC5", new String[]{"PKCS5Padding", "PKCS7Padding", "ISO10126Padding","NoPadding"});
//        paddings.put("TEA", new String[]{"PKCS5Padding","PKCS7Padding", "ISO10126Padding","NoPadding"});
//        paddings.put("DESede", new String[]{"PKCS5Padding", "PKCS7Padding", "ISO10126Padding", "NoPadding"});
//        paddings.put("Twofish", new String[]{"PKCS5Padding", "PKCS7Padding", "ISO10126Padding", "NoPadding"});
//        paddings.put("XTEA", new String[]{"PKCS5Padding","PKCS7Padding","ISO10126Padding", "NoPadding"});
//        
//        return paddings;
//    }

	private Map<String, String[]> createBlockSizes() {
		Map<String, String[]> blockSizes = new HashMap<>();
		
		blockSizes.put("AES", new String[] { "128" });
		blockSizes.put("DES", new String[] { "64" });
		blockSizes.put("Blowfish", new String[] { "64" });
		blockSizes.put("Camellia", new String[] { "128" });
		blockSizes.put("CAST5", new String[] { "64" });
		blockSizes.put("RC5", new String[]{"32", "64", "128"});
		blockSizes.put("TEA", new String[] { "64" });
		blockSizes.put("DESede", new String[] { "64" });
		blockSizes.put("Twofish", new String[] { "128" });
		blockSizes.put("XTEA", new String[] { "64" });
		
		return blockSizes;
	}
    
    private void updateAlgorithmDetails() {
        String algorithm = (String) algorithmBox.getSelectedItem();
        keySizeBox.setModel(new DefaultComboBoxModel<>(keySizes.get(algorithm)));
        modeBox.setModel(new DefaultComboBoxModel<>(modes.get(algorithm)));
//        paddingBox.setModel(new DefaultComboBoxModel<>(paddings.get(algorithm)));
        blockSizeBox.setModel(new DefaultComboBoxModel<>(blockSizes.get(algorithm)));
	}
    
    
    
    public String getAlgorithm() {
		return (String) algorithmBox.getSelectedItem();
	}
    
    public String getKeySize() {
    	return (String) keySizeBox.getSelectedItem();
    }
    
    public String getMode() {
    	return (String) modeBox.getSelectedItem();
    }

	public String getBlockSize() {
		return (String) blockSizeBox.getSelectedItem();
	}

	public String getPadding() {
		return (String) paddingBox.getSelectedItem();
	}
	public String getCharset() {
		return (String) charsetBox.getSelectedItem();
	}

	public String getKey() {
		return key.getText();
	}
	
	public void setKey(String key) {
		this.key.setText(key);
	}
	
	public void setAlgorithm(String algorithm) {
		ComboBoxModel<String> model = algorithmBox.getModel();
		boolean found = false;

		for (int i = 0; i < model.getSize(); i++) {
			if (model.getElementAt(i).equals(algorithm)) {
				algorithmBox.setSelectedItem(algorithm);
				found = true;
				break;
			}
		}

		if (!found) {
			algorithmBox.setSelectedIndex(0); // chọn giá trị đầu tiên
		}
	}
	
	public void setCharSet(String charset) {
		ComboBoxModel<String> model = charsetBox.getModel();
		boolean found = false;

		for (int i = 0; i < model.getSize(); i++) {
			if (model.getElementAt(i).equals(charset)) {
				charsetBox.setSelectedItem(charset);
				found = true;
				break;
			}
		}

		if (!found) {
			charsetBox.setSelectedIndex(0); // chọn giá trị đầu tiên
		}
	}
	
	
	public void setKeySize(String algorithm, String keySize) {
	    String[] sizes = keySizes.get(algorithm);
	    if (sizes == null) {
	        
	        return;
	    }
	    boolean found = false;
	    for (String size : sizes) {
	        if (size.equals(keySize)) {
	            keySizeBox.setSelectedItem(keySize);
	            found = true;
	            break;
	        }
	    }
	    if (!found) {
	        
	        // Có thể set về giá trị đầu tiên nếu muốn:
	         keySizeBox.setSelectedIndex(0);
	    }
	}
	
	public void setMode(String algorithm, String mode) {
	    String[] modeS = modes.get(algorithm);

	    if (modeS == null) {
	        
	        return;
	    }

	    boolean found = false;
	    for (String modeCipher : modeS) {
	        if (modeCipher.equals(mode)) {
	            modeBox.setSelectedItem(mode);
	            found = true;
	            break;
	        }
	    }

	    if (!found) {
	        
	        // Có thể set về giá trị đầu tiên nếu muốn:
	         modeBox.setSelectedIndex(0);
	    }
	}
	
	public void setBlockSize(String algorithm, String blockSize) {
		String[] blocks = blockSizes.get(algorithm);

	    if (blocks == null) {
	        
	        return;
	    }

	    boolean found = false;
	    for (String block : blocks) {
	        if (block.equals(blockSize)) {
	            modeBox.setSelectedItem(block);
	            found = true;
	            break;
	        }
	    }

	    if (!found) {
	        
	        // Có thể set về giá trị đầu tiên nếu muốn:
	         blockSizeBox.setSelectedIndex(0);
	    }
	}
	
//	public void setPadding(String algorithm, String padding) {
//		String[] paddingS = paddings.get(algorithm);
//
//		if (paddingS == null) {
//			return;
//		}
//
//		boolean found = false;
//		for (String padding1 : paddingS) {
//			if (padding1.equals(padding)) {
//				modeBox.setSelectedItem(padding);
//				found = true;
//				break;
//			}
//		}
//
//		if (!found) {
//			// Có thể set về giá trị đầu tiên nếu muốn:
//			paddingBox.setSelectedIndex(0);
//		}
//	}
	
	public void setPadding(String padding) {
		ComboBoxModel<String> model = paddingBox.getModel();
		boolean found = false;

		for (int i = 0; i < model.getSize(); i++) {
			if (model.getElementAt(i).equals(padding)) {
				paddingBox.setSelectedItem(padding);
				found = true;
				break;
			}
		}

		if (!found) {
			paddingBox.setSelectedIndex(0); // chọn giá trị đầu tiên
		}
	}

}

