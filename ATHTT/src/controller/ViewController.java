package controller;

import javax.swing.JOptionPane;

import model.classicialcipher.PermutationCipher;
import model.classicialcipher.ShiftCipher;
import model.classicialcipher.SubstitutionCipher;
import model.classicialcipher.VigenereCipher;
import view.PermutationCipherView;
import view.ShiftCipherView;
import view.SubstitutionCipherView;
import view.View;
import view.VigenereCipherView;

public class ViewController {
	private View view;

	public ViewController(View view) {
		this.view = view;
		initController();
	}

	private void initController() {
		view.getSubstitutionCipherBtn().addActionListener(e ->  openSubstitutionView());
		view.getShiftCipherBtn().addActionListener(e -> openShiftCipherView());
		view.getVigenereCipherBtin().addActionListener(e -> openShiftCipherView());
	}

	private void openSubstitutionView() {
		SubstitutionCipherView subView = new SubstitutionCipherView();
		SubstitutionCipher subModel = new SubstitutionCipher();
		new SubstitutionCipherController(subView, subModel);
	}
	
	private void openShiftCipherView() {
		ShiftCipherView shiftView = new ShiftCipherView();
		ShiftCipher shiftModel = new ShiftCipher();
		new ShiftCipherConttroler(shiftModel, shiftView); 
	}
	
	private void openVigenereCipherView() {
		VigenereCipherView vigenereView = new VigenereCipherView();
		VigenereCipher vigenereCipher = new VigenereCipher();
		new VigenereCipherController(vigenereCipher, vigenereView);
	}
	
	private void openPermuationCipherView() {
		PermutationCipherView permuationView = new PermutationCipherView();
		PermutationCipher perCipher = new PermutationCipher();
		new PermutationCipherController(perCipher, permuationView);
	}

	private void showNotImplemented() {
		JOptionPane.showMessageDialog(null, "Tính năng này chưa được triển khai.");
	}
	public static void main(String[] args) {
		View v = new View();
		ViewController vc = new ViewController(v);
	}
}

