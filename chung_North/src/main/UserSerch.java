package main;

import java.awt.BorderLayout;
import java.awt.GridLayout;

import utils.sp;
import utils.sp.cp;

public class UserSerch extends MovieSerch{
	public static void main(String[] args) {
		northPanel.add(new cp(new BorderLayout(), null, null) {{
			add(logo, sp.w);
			add(new cp(new GridLayout(0, 2, 5, 5), null, null) {{
				add(loginAll); add(movieSerch);
			}}, sp.e);
		}}, sp.n);
		new UserSerch();
	}
}
