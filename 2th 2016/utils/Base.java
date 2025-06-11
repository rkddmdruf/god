package utils;

import java.awt.Font;

public interface Base {
	void desgin();
	void action();
	default Font setBoldFont(int size) {
		return new Font("맑은 고딕", Font.BOLD, size);
	}
}
