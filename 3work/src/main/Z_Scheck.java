package main;

import javax.swing.JTextArea;

public class Z_Scheck {

	
	private static String[] str = "씨*,시*,썅,병*,새*,또라*,개새*,존*".split(",");
	
	public static boolean 체크(String s) {
		for (String string : str) {
			if (s.contains(string)) {
				return true;
			}
		}
		return false;
	}
	}

