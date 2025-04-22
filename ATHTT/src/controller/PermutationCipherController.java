package controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import model.classicialcipher.PermutationCipher;
import view.PermutationCipherView;

public class PermutationCipherController {
	private PermutationCipher model;
	private PermutationCipherView view;

	public PermutationCipherController(PermutationCipher model, PermutationCipherView view) {
		super();
		this.model = model;
		this.view = view;
		
		this.view.getEncryptTextBtn().addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				handleText("ENCRYPT");
			}
		});

		this.view.getDecryptTextBtn().addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				handleText("DECRYPT");
			}
		});

		this.view.getSaveResultBtn().addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				saveResult();
			}
		});

//		this.view.getGenKey().addActionListener(new ActionListener() {
//			@Override
//			public void actionPerformed(ActionEvent e) {
//				genKey();
//
//			}
//		});
//	
//		this.view.getLoadKey().addActionListener(new ActionListener() {
//			@Override
//			public void actionPerformed(ActionEvent e) {
//				loadKey();
//				
//			}
//		});
//		
//		this.view.getSaveKey().addActionListener(new ActionListener() {	
//			@Override
//			public void actionPerformed(ActionEvent e) {
//				saveKey();
//			}
//		});
//	}
	}

private void handleText(String string) {
	// TODO Auto-generated method stub
	
}


private void saveResult() {
	// TODO Auto-generated method stub
	
}

}
