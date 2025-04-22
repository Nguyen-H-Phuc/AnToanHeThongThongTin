package view;

import java.awt.BorderLayout;
import java.awt.FlowLayout;

import javax.swing.JPanel;
import javax.swing.JSpinner;

public class HillCipherView extends ClassicalCipherView {
	JPanel keyPanel;
	JSpinner spinner;

	public HillCipherView() {
		super();
		createFrame(700, 600, "Hill cipher tool");
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
		this.setGenKey("Tạo khoá");
		this.setSaveKey("Lưu khoá");
		this.setSaveKey("Tải khoá");
		keyPanel = new JPanel(new FlowLayout());
		keyPanel.add(this.getGenKey());
		keyPanel.add(this.getLoadKey());
		keyPanel.add(this.getSaveKey());

	}

}
