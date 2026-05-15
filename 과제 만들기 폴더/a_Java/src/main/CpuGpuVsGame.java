package main;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import utils.CFrame;
import utils.Connections;
import utils.Data;
import utils.Html;
import utils.getter;

public class CpuGpuVsGame extends CFrame{

	Data game;
	Data CG;
	List<Integer> minList = new ArrayList<>();
	List<Integer> nomalList = new ArrayList<>();
	List<Integer> myList = new ArrayList<>();
	JButton button = new JButton("내 컴퓨터 사양보기");
	public CpuGpuVsGame(int gno) {
		game = Connections.select("select * from game where gno = ?", gno).get(0);
		CG = Connections.select("SELECT gno, Cm.cpuname, Gm.gcname, minRam, Cn.cpuname, Gn.gcname, nomalRam, Cm.cpuno, Gm.gcno, Cn.cpuno, Gn.gcno  FROM game.cpugc \r\n"
				+ "join gc Gm on Gm.gcno = cpugc.minGc\r\n"
				+ "join gc Gn on Gn.gcno = cpugc.nomalGc\r\n"
				+ "join cpu Cm on Cm.cpuno = cpugc.minCpu\r\n"
				+ "join cpu Cn on Cn.cpuno = cpugc.nomalCpu\r\n"
				+ "where gno = ?", gno).get(0);
		System.out.println(CG);
		borderPanel.setBorder(BorderFactory.createEmptyBorder(25, 25, 25, 25));
		JPanel mainPanel = new JPanel(new GridLayout(2, 1, 10, 10));
		mainPanel.setBackground(getter.color);
		borderPanel.add(new JPanel(new BorderLayout(10, 10)) {{
			setBackground(getter.color);
			add(new JLabel(game.getString(1)) {{
				setForeground(Color.white);
				setVerticalAlignment(JLabel.TOP);
				setFont(new Font("맑은 고딕" , 1, 24));
			}});
			add(button, BorderLayout.EAST);
		}}, BorderLayout.NORTH);
		borderPanel.add(mainPanel);
		minPanel(mainPanel);
		
		button.addActionListener(e -> {
			getter.infor(new Html()
					.add("CPU : " + Connections.select("select * from cpu where cpuno = ?", myList.get(0)).get(0).getString(1))
					.nextLine()
					.add("GPU : " + Connections.select("select * from gc where gcno = ?", myList.get(1)).get(0).getString(1))
					.nextLine()
					.add("RAM : " + myList.get(2) + "GB")
					.getString());
		});
		setFrameCg("컴퓨터 사양비교", 600, 400, () -> {});
		setIconImage(new ImageIcon("datafiles/games/" + gno + ".jpg").getImage());
	}
	
	private void minPanel(JPanel mainPanel) {
		String[] str = "최소,권장".split(",");
		runPS("(Get-CimInstance Win32_Processor).Name", "CPU: ");
		runPS("(Get-CimInstance Win32_VideoController).Name", "GPU: ");
		runPS("(Get-CimInstance Win32_ComputerSystem).TotalPhysicalMemory", "RAM: ");
		
		JLabel[] label2 = new JLabel[2];
		int n = 0;
		for(int i = 0; i < 2; i++) {
			JPanel panel = new JPanel(new BorderLayout());
			panel.setBackground(getter.color);
			
			panel.add(new JLabel(str[i] + "사양") {{
				setForeground(Color.WHITE);
				setFont(new Font("맑은 고딕", 1, 20));
			}}, BorderLayout.NORTH);
			panel.add(new JLabel(new Html()
					.add("CPU : " + CG.getString(1 + n))
					.nextLine()
					.nextLine()
					.add("GPU : " + CG.getString(2 + n))
					.nextLine()
					.nextLine()
					.add("RAM : " + CG.getString(3 + n) + "GB")
					.getString()
				) {
					private static final long serialVersionUID = 1L;
				{
					setForeground(Color.white);
				}});
			
			label2[i] = new JLabel(new Html().getString()) {{
				setBackground(Color.white);
				setOpaque(true);
				setPreferredSize(new Dimension(250, 0));
			}};
			panel.add(label2[i], BorderLayout.EAST);
			
			List<Integer> test = (i == 0 ? minList : nomalList);
			for(int j = 0; j < 2; j++) {
				test.add(CG.getInt(7 + j + (i * 2)));
			}
			test.add(CG.getInt(3 + n));
			mainPanel.add(panel);
			n += 3;
		}
		
		List<String> cgInfor = new ArrayList<>();
		for(int i = 0; i < 2; i++) {
			List<Integer> test = (i == 0 ? minList : nomalList);
			String s = (i == 0 ? "최소사양" : "권장사양") + " 기준 : ";
			if(myList.get(0) < test.get(0)) {
				cgInfor.add("CPU가 좋지 않습니다");
			}
			if(myList.get(1) < test.get(1)) {
				cgInfor.add("GPU가 좋지 않습니다");
			}
			if(myList.get(2) < test.get(2)) {
				cgInfor.add("RAM이 부족합니다");
			}
			s += String.join(", ", cgInfor);
			label2[i].setText(new Html()
					.add(s)
					.getString());
			cgInfor.clear();
		}
        System.out.println(minList);
        System.out.println(nomalList);
        System.out.println(myList);
	}
	
	private void runPS(String command, String prefix) {
        try {
            Process p = new ProcessBuilder(
                    "powershell",
                    "-Command",
                    command
            ).start();

            BufferedReader br = new BufferedReader(new InputStreamReader(p.getInputStream()));
            String line;

            while ((line = br.readLine()) != null) {
                if (!line.trim().isEmpty()) {

                    // RAM 처리
                    if (prefix.equals("RAM: ")) {
                        Long gb = Long.parseLong(line.trim()) / (1024 * 1024 * 1024);
                        myList.add(gb.intValue() + 1);
                    } else {
                    	String[] str = "cpu,gc".split(",");
                    	int n = prefix.equals("CPU: ") ? 0 : 1;
                    	List<Data> list = Connections.select("select * from " + str[n]);
                    	for(Data data : list) {
                    		if(line.trim().contains(data.getString(1))) {
                    			myList.add(data.getInt(0));
                    			break;
                    		}
                    	}
                    }
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
	
	public static void main(String[] args) {
		new CpuGpuVsGame(14);
	}
}
