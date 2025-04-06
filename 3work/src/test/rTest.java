package test;

import java.awt.dnd.*;

import javax.swing.JFrame;
import javax.swing.JTextArea;

public class rTest implements DropTargetListener{
	JFrame f = new JFrame();
	DropTarget dt;
	JTextArea img = new JTextArea();
	rTest(){
		//dt = new DropTarget(img, DnDConstants.ACTION_COPY_OR_MOVE, this, true, null);
		
		f.add(img);
		f.setSize(500, 500);
		f.setDefaultCloseOperation(f.EXIT_ON_CLOSE);
		f.setVisible(true);
		
	}
	public static void main(String[] args) {
		new rTest();
		double i = 4;
		double ii = 11;
		int iii = ((int) ((i / ii) * 100));
	
		System.out.println(((int) ((i / ii) * 100)));
	}
	@Override
	public void dragEnter(DropTargetDragEvent dtde) {}

	@Override
	public void dragOver(DropTargetDragEvent dtde) {System.out.println("dragOver");}

	@Override
	public void dropActionChanged(DropTargetDragEvent dtde) {}

	@Override
	public void dragExit(DropTargetEvent dte) {}

	@Override
	public void drop(DropTargetDropEvent dtde) {}

}
