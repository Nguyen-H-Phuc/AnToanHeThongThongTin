package controller;

import javax.swing.JOptionPane;

import model.classicialcipher.SubstitutionCipher;
import view.SubstitutionCipherView;
import view.View;

public class ViewController {
	private View view;

	public ViewController(View view) {
		this.view = view;
		initController();
	}

	private void initController() {
		view.getClassicEncryptBtn().addActionListener(e -> openClassicEncryptView());
		view.getSymmetricEncryptionBtn().addActionListener(e -> showNotImplemented());
		view.getAsymmetricEncryptionBtn().addActionListener(e -> showNotImplemented());
	}

	private void openClassicEncryptView() {
		SubstitutionCipherView subView = new SubstitutionCipherView();
		SubstitutionCipher subModel = new SubstitutionCipher();
		new SubstitutionCipherController(subView, subModel);
	}

	private void showNotImplemented() {
		JOptionPane.showMessageDialog(null, "Tính năng này chưa được triển khai.");
	}
	public static void main(String[] args) {
		View v = new View();
		ViewController vc = new ViewController(v);
	}
}

