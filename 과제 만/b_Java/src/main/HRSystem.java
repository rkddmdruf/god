package main;
import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.List;
import java.util.stream.*;

public class HRSystem {

    // ══════════════════════════════════════════════
    //  상수 & 공통 데이터
    // ══════════════════════════════════════════════
    static final String[] TEAM_NAMES  = {"개발","생산","관리","영업"};
    static final String[] GRADE_NAMES = {"부장","차장","과장","대리","사원"};
    static final String[] STATE_NAMES = {"재직","휴직","퇴직"};
    static final double[] BASE_SALARY = {5_500_000, 4_800_000, 4_200_000, 3_500_000, 2_800_000};

    static final Color C_NAVY   = new Color(0,0,0);
    static final Color C_NAVY2  = new Color(30,30,30);
    static final Color C_LBLUE  = new Color(235,240,250);
    static final Color C_WHITE  = Color.black;
    static final Color C_ALT    = new Color(245,247,252);
    static final Color C_SEL    = new Color(210,225,250);
    static final Color C_BORDER = new Color(180,180,200);
    static final Color C_GREEN  = new Color(20,120,60);
    static final Color C_AMBER  = new Color(160,100,0);
    static final Color C_RED    = new Color(160,30,30);

    static final Font F_DEF   = new Font("맑은 고딕", Font.PLAIN, 13);
    static final Font F_BOLD  = new Font("맑은 고딕", Font.BOLD,  13);
    static final Font F_TITLE = new Font("맑은 고딕", Font.BOLD,  16);
    static final Font F_SM    = new Font("맑은 고딕", Font.PLAIN, 12);
    static final Font F_LG    = new Font("맑은 고딕", Font.BOLD,  14);

    // ══════════════════════════════════════════════
    //  데이터 모델
    // ══════════════════════════════════════════════
    static class Employee {
        int eno; String code,name,date,id,pw; int team,grade,state;
        Employee(int eno,String code,String name,int team,int grade,String date,int state,String id,String pw){
            this.eno=eno;this.code=code;this.name=name;this.team=team;
            this.grade=grade;this.date=date;this.state=state;this.id=id;this.pw=pw;
        }
        String teamName(){return TEAM_NAMES[team];}
        String gradeName(){return GRADE_NAMES[grade];}
        String stateName(){return STATE_NAMES[state];}
        double baseSalary(){return BASE_SALARY[grade];}
    }

    static class Attendance {
        String attId,empCode,workDate,inTime,outTime,workType;
        Attendance(String attId,String empCode,String workDate,String inTime,String outTime,String workType){
            this.attId=attId;this.empCode=empCode;this.workDate=workDate;
            this.inTime=inTime;this.outTime=outTime;this.workType=workType;
        }
    }

    static class Salary {
        String salId,empCode,yearMonth; double base,overtime,deduction,net; int overtimeHours;
        Salary(String salId,String empCode,String yearMonth,double base,int overtimeHours,double overtime,double deduction,double net){
            this.salId=salId;this.empCode=empCode;this.yearMonth=yearMonth;this.base=base;
            this.overtimeHours=overtimeHours;this.overtime=overtime;this.deduction=deduction;this.net=net;
        }
    }

    // ══════════════════════════════════════════════
    //  샘플 데이터
    // ══════════════════════════════════════════════
    static List<Employee> loadEmployees() {
    	Object[][] raw = {
    		    {1,"EMP-001","강응결",0,0,"2009-05-22",0,"user001","user001!"},
    		    {2,"EMP-002","이정훈",0,1,"2010-03-15",0,"user002","user002!"},
    		    {3,"EMP-003","최민석",0,2,"2011-07-21",0,"user003","user003!"},
    		    {4,"EMP-004","정유진",0,2,"2012-11-05",0,"user004","user004!"},
    		    {5,"EMP-005","박소연",0,3,"2014-02-18",0,"user005","user005!"},
    		    {6,"EMP-006","김태현",0,3,"2015-06-30",0,"user006","user006!"},

    		    {7,"EMP-007","김수미",1,0,"2007-09-11",0,"user007","user007!"},
    		    {8,"EMP-008","배성우",1,1,"2009-12-03",0,"user008","user008!"},
    		    {9,"EMP-009","장민지",1,2,"2011-05-16",0,"user009","user009!"},
    		    {10,"EMP-010","이수현",1,2,"2012-10-09",0,"user010","user010!"},
    		    {11,"EMP-011","김하늘",1,3,"2013-08-25",0,"user011","user011!"},
    		    {12,"EMP-012","최유진",1,3,"2014-11-11",0,"user012","user012!"},

    		    {13,"EMP-013","박지호",2,0,"2007-09-11",0,"user013","user013!"},
    		    {14,"EMP-014","김영훈",2,1,"2009-11-20",0,"user014","user014!"},
    		    {15,"EMP-015","최지은",2,2,"2011-03-14",0,"user015","user015!"},
    		    {16,"EMP-016","박성민",2,2,"2012-06-18",0,"user016","user016!"},
    		    {17,"EMP-017","이은지",2,3,"2013-09-27",0,"user017","user017!"},
    		    {18,"EMP-018","정현우",2,3,"2014-12-05",0,"user018","user018!"},

    		    {19,"EMP-019","홍박사",3,0,"2008-08-25",0,"user019","user019!"},
    		    {20,"EMP-020","한상우",3,1,"2010-01-08",0,"user020","user020!"},
    		    {21,"EMP-021","김다영",3,2,"2011-09-19",0,"user021","user021!"},
    		    {22,"EMP-022","이준호",3,2,"2012-04-23",0,"user022","user022!"},
    		    {23,"EMP-023","박은지",3,3,"2013-06-11",0,"user023","user023!"},
    		    {24,"EMP-024","정민석",3,3,"2014-08-28",0,"user024","user024!"},
    		};
        List<Employee> list = new ArrayList<>();
        for(Object[] r:raw) list.add(new Employee((int)r[0],(String)r[1],(String)r[2],(int)r[3],(int)r[4],(String)r[5],(int)r[6],(String)r[7],(String)r[8]));
        return list;
    }

    static List<Attendance> loadAttendances() {
        List<Attendance> list = new ArrayList<>();
        String[] codes = {"EMP-001","EMP-002","EMP-005","EMP-006","EMP-017","EMP-029","EMP-041"};
        String[][] days = {{"2024-03-04","09:02","18:05","정상"},{"2024-03-05","09:15","18:00","지각"},
                           {"2024-03-06","09:00","17:30","조퇴"},{"2024-03-07","09:00","18:00","정상"},
                           {"2024-03-08","—","—","결근"},{"2024-03-11","09:05","18:10","정상"},
                           {"2024-03-12","09:00","18:00","정상"},{"2024-03-13","09:00","18:00","정상"},
                           {"2024-03-14","09:00","21:00","정상"},{"2024-03-15","09:00","18:00","정상"}};
        int id=1;
        for(String code:codes)
            for(String[] d:days)
                list.add(new Attendance(String.format("ATT-%04d",id++),code,d[0],d[1],d[2],d[3]));
        return list;
    }

    static List<Salary> loadSalaries(List<Employee> emps) {
        List<Salary> list = new ArrayList<>();
        String[] months = {"2024-01","2024-02","2024-03"};
        int id=1;
        Random rnd = new Random(42);
        for(Employee e:emps.subList(0,Math.min(10,emps.size()))){
            for(String m:months){
                int ot = rnd.nextInt(15);
                double otPay = Math.floor(e.baseSalary()/209.0*1.5*ot);
                double ded   = Math.floor((e.baseSalary()+otPay)*0.033);
                double net   = e.baseSalary()+otPay-ded;
                list.add(new Salary(String.format("SAL-%04d",id++),e.code,m,e.baseSalary(),ot,otPay,ded,net));
            }
        }
        return list;
    }

    // ══════════════════════════════════════════════
    //  공통 UI 헬퍼
    // ══════════════════════════════════════════════
    static JButton makePBtn(String t){
        JButton b=new JButton(t); b.setFont(F_BOLD); b.setBackground(C_NAVY);
        b.setForeground(C_WHITE); b.setFocusPainted(false);
        b.setBorder(BorderFactory.createEmptyBorder(5,14,5,14));
        b.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR)); return b;
    }
    static JButton makeSBtn(String t){
        JButton b=new JButton(t); b.setFont(F_DEF); b.setBackground(C_WHITE);
        b.setForeground(new Color(50,50,50)); b.setFocusPainted(false);
        b.setBorder(BorderFactory.createLineBorder(C_BORDER));
        b.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR)); return b;
    }
    static JTextField makeTF(int cols){
        JTextField f=new JTextField(cols); f.setFont(F_DEF);
        f.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(C_BORDER),
            BorderFactory.createEmptyBorder(3,6,3,6))); return f;
    }
    static JTextField makeTF(String val){
        JTextField f=makeTF(12); f.setText(val); return f;
    }
    static JTextField makeROTF(String val){
        JTextField f=makeTF(val); f.setEditable(false);
        f.setBackground(new Color(240,242,248)); return f;
    }
    static JComboBox<String> makeCmb(String[] items){
        JComboBox<String> c=new JComboBox<>(items); c.setFont(F_DEF); return c;
    }
    static JLabel makeLbl(String t){ JLabel l=new JLabel(t); l.setFont(F_DEF); return l; }
    static JPanel makeHeader(String title,String sub){
        JPanel p=new JPanel(new BorderLayout()); p.setBackground(C_NAVY);
        p.setBorder(BorderFactory.createEmptyBorder(10,16,10,16));
        JLabel lt=new JLabel(title); lt.setFont(F_TITLE); lt.setForeground(Color.white);
        JLabel ls=new JLabel(sub); ls.setFont(F_SM); ls.setForeground(new Color(180,200,240));
        JPanel lp=new JPanel(new GridLayout(2,1,0,2)); lp.setOpaque(false);
        lp.add(lt); lp.add(ls); p.add(lp,BorderLayout.WEST); return p;
    }
    static JPanel makeStatusBar(String user){
        JPanel p=new JPanel(new BorderLayout()); p.setBackground(C_LBLUE);
        p.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createMatteBorder(1,0,0,0,C_BORDER),
            BorderFactory.createEmptyBorder(3,10,3,10)));
        JLabel l=new JLabel("로그인: "+user+"  |  인사·급여 관리 시스템");
        l.setFont(F_SM); l.setForeground(new Color(60,80,130)); p.add(l,BorderLayout.WEST); return p;
    }
    static JTable makeTable(DefaultTableModel m){
        JTable t=new JTable(m); t.setFont(F_DEF); t.setRowHeight(24);
        t.setShowGrid(true); t.setGridColor(new Color(220,220,220));
        t.setSelectionBackground(C_SEL); t.setSelectionForeground(Color.BLACK);
        t.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
        t.getTableHeader().setFont(F_BOLD);
        t.getTableHeader().setBackground(C_NAVY2);
        t.getTableHeader().setForeground(C_WHITE);
        t.getTableHeader().setPreferredSize(new Dimension(0,28));
        t.getTableHeader().setReorderingAllowed(false);
        t.setDefaultRenderer(Object.class, new DefaultTableCellRenderer(){
            public Component getTableCellRendererComponent(JTable tbl,Object v,boolean sel,boolean foc,int r,int c){
                Component comp=super.getTableCellRendererComponent(tbl,v,sel,foc,r,c);
                setHorizontalAlignment(CENTER);
                if(!sel){ comp.setBackground(r%2==0?Color.white:C_ALT); comp.setForeground(Color.BLACK); }
                if(!sel&&v!=null){
                    String s=v.toString();
                    if(s.equals("재직")||s.equals("정상")) comp.setForeground(C_GREEN);
                    else if(s.equals("휴직")||s.equals("지각")||s.equals("조퇴")) comp.setForeground(C_AMBER);
                    else if(s.equals("퇴직")||s.equals("결근")) comp.setForeground(C_RED);
                }
                return comp;
            }
        });
        return t;
    }
    static JScrollPane makeScroll(JTable t){
        JScrollPane s=new JScrollPane(t); s.setBorder(BorderFactory.createLineBorder(C_BORDER)); return s;
    }
    static void addGBC(JPanel p,Component c,GridBagConstraints g,int x,int y,int w,double wx){
        g.gridx=x; g.gridy=y; g.gridwidth=w; g.weightx=wx; p.add(c,g);
    }
    static String fmt(double v){ return String.format("%,.0f원",v); }

    // ══════════════════════════════════════════════
    //  JMenuBar 공통
    // ══════════════════════════════════════════════
    static JMenuBar makeMenuBar(JFrame frame){
        JMenuBar mb=new JMenuBar();
        JMenu mf=new JMenu("파일");
        JMenuItem mi=new JMenuItem("종료");
        mi.addActionListener(e->{
            if(JOptionPane.showConfirmDialog(frame,"정말 종료하시겠습니까?","종료",JOptionPane.YES_NO_OPTION)==JOptionPane.YES_OPTION)
                System.exit(0);
        });
        mf.add(mi); mb.add(mf);
        return mb;
    }

    // ══════════════════════════════════════════════
    //  전역 상태 (로그인 사용자)
    // ══════════════════════════════════════════════
    static String loginUser = "";
    static List<Employee>  employees;
    static List<Attendance> attendances;
    static List<Salary>     salaries;

    // ══════════════════════════════════════════════
    //  폼 1 — 로그인
    // ══════════════════════════════════════════════
    static void showLogin(){
        JFrame f=new JFrame("로그인");
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        f.setSize(380,270); f.setLocationRelativeTo(null); f.setResizable(false);
        JPanel root=new JPanel(new BorderLayout());
        root.add(makeHeader("인사·급여 관리 시스템","관리자 로그인"),BorderLayout.NORTH);

        JPanel body=new JPanel(new GridBagLayout());
        body.setBorder(BorderFactory.createEmptyBorder(20,40,20,40));
        GridBagConstraints gc=new GridBagConstraints();
        gc.insets=new Insets(5,5,5,5); gc.fill=GridBagConstraints.HORIZONTAL;

        JTextField txtId=makeTF(15);
        JPasswordField txtPw=new JPasswordField(15); txtPw.setFont(F_DEF);
        txtPw.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(C_BORDER),BorderFactory.createEmptyBorder(3,6,3,6)));
        JLabel lblErr=new JLabel(" "); lblErr.setFont(F_SM); lblErr.setForeground(Color.RED);

        addGBC(body,makeLbl("아이디"),gc,0,0,1,0);
        addGBC(body,txtId,gc,1,0,1,1);
        addGBC(body,makeLbl("비밀번호"),gc,0,1,1,0);
        addGBC(body,txtPw,gc,1,1,1,1);
        addGBC(body,lblErr,gc,0,2,2,1);

        JPanel bp=new JPanel(new FlowLayout(FlowLayout.CENTER,8,0)); bp.setOpaque(false);
        JButton btnLogin=makePBtn("로그인"); JButton btnExit=makeSBtn("종료");
        bp.add(btnLogin); bp.add(btnExit);
        addGBC(body,bp,gc,0,3,2,1);

        root.add(body,BorderLayout.CENTER);
        root.add(makeStatusBar("미로그인"),BorderLayout.SOUTH);
        f.setContentPane(root);

        ActionListener doLogin=e->{
            String id=txtId.getText().trim(); String pw=new String(txtPw.getPassword());
            if(id.isEmpty()||pw.isEmpty()){lblErr.setText("아이디와 비밀번호를 입력하세요.");return;}
            boolean isAdmin=id.equals("admin")&&pw.equals("1234");
            Employee found=employees.stream().filter(em->em.id.equals(id)&&em.pw.equals(pw)).findFirst().orElse(null);
            if(isAdmin||found!=null){
                loginUser=isAdmin?"관리자":(found!=null?found.name:"");
                f.dispose(); showEmpList();
            } else { lblErr.setText("아이디 또는 비밀번호가 올바르지 않습니다."); txtPw.setText(""); }
        };
        btnLogin.addActionListener(doLogin); txtPw.addActionListener(doLogin);
        btnExit.addActionListener(e->{
            if(JOptionPane.showConfirmDialog(f,"정말 종료하시겠습니까?","종료",JOptionPane.YES_NO_OPTION)==JOptionPane.YES_OPTION)
                System.exit(0);
        });
        f.setVisible(true);
    }

    // ══════════════════════════════════════════════
    //  폼 3 — 직원 목록 (메인 허브)
    // ══════════════════════════════════════════════
    static JFrame mainFrame;
    static void showEmpList(){
        if(mainFrame!=null){mainFrame.toFront();return;}
        JFrame f=new JFrame("인사·급여 관리 시스템 — 직원 목록");
        mainFrame=f;
        f.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        f.addWindowListener(new WindowAdapter(){public void windowClosing(WindowEvent e){
            if(JOptionPane.showConfirmDialog(f,"정말 종료하시겠습니까?","종료",JOptionPane.YES_NO_OPTION)==JOptionPane.YES_OPTION)
                System.exit(0);
        }});
        f.setSize(920,580); f.setLocationRelativeTo(null);
        f.setJMenuBar(makeMenuBar(f));

        // 네비게이션 버튼 패널
        JPanel nav=new JPanel(new FlowLayout(FlowLayout.LEFT,6,6));
        nav.setBackground(new Color(240,244,252));
        nav.setBorder(BorderFactory.createMatteBorder(0,0,1,0,C_BORDER));
        String[] navNames={"직원 등록","근태 현황","급여 계산","급여 명세","통계 대시보드"};
        for(String n:navNames){ JButton b=makePBtn(n); b.setFont(F_SM); nav.add(b); }

        JPanel root=new JPanel(new BorderLayout());
        root.add(makeHeader("직원 목록","전체 직원 조회 및 관리"),BorderLayout.NORTH);

        // 검색 패널
        JPanel sp=new JPanel(new FlowLayout(FlowLayout.LEFT,8,6));
        sp.setBackground(new Color(250,251,255));
        sp.setBorder(BorderFactory.createMatteBorder(0,0,1,0,C_BORDER));
        JTextField txtSearch=makeTF(14);
        String[] tf={"전체 부서","개발","생산","관리","영업"};
        String[] sf={"전체 상태","재직","휴직","퇴직"};
        JComboBox<String> cmbTeam=makeCmb(tf); JComboBox<String> cmbState=makeCmb(sf);
        JButton btnSearch=makePBtn("검색"); JButton btnReset=makeSBtn("초기화");
        JLabel lblCnt=new JLabel("총 "+employees.size()+"명"); lblCnt.setFont(F_SM);
        sp.add(makeLbl("검색어:")); sp.add(txtSearch); sp.add(cmbTeam); sp.add(cmbState);
        sp.add(btnSearch); sp.add(btnReset); sp.add(Box.createHorizontalStrut(16)); sp.add(lblCnt);

        JPanel topArea=new JPanel(new BorderLayout());
        topArea.add(nav,BorderLayout.NORTH); topArea.add(sp,BorderLayout.SOUTH);
        root.add(topArea,BorderLayout.NORTH);

        // 테이블
        String[] cols={"번호","사원코드","이름","부서","직책","입사일","상태"};
        DefaultTableModel model=new DefaultTableModel(cols,0){public boolean isCellEditable(int r,int c){return false;}};
        JTable table=makeTable(model);
        int[] widths={45,90,75,70,70,100,65};
        for(int i=0;i<widths.length;i++) table.getColumnModel().getColumn(i).setPreferredWidth(widths[i]);

        Runnable reload=()->{
            model.setRowCount(0);
            String kw=txtSearch.getText().trim();
            int ts=cmbTeam.getSelectedIndex()-1, ss=cmbState.getSelectedIndex()-1; int cnt=0;
            for(Employee e:employees){
                if((!kw.isEmpty()&&!e.name.contains(kw)&&!e.code.contains(kw))) continue;
                if(ts>=0&&e.team!=ts) continue;
                if(ss>=0&&e.state!=ss) continue;
                model.addRow(new Object[]{e.eno,e.code,e.name,e.teamName(),e.gradeName(),e.date,e.stateName()}); cnt++;
            }
            lblCnt.setText("총 "+cnt+"명");
        };
        reload.run();
        btnSearch.addActionListener(e->reload.run());
        btnReset.addActionListener(e->{txtSearch.setText("");cmbTeam.setSelectedIndex(0);cmbState.setSelectedIndex(0);reload.run();});
        txtSearch.addActionListener(e->reload.run());

        // 하단 버튼
        JPanel bp=new JPanel(new FlowLayout(FlowLayout.LEFT,8,6));
        bp.setBackground(new Color(250,251,255));
        bp.setBorder(BorderFactory.createMatteBorder(1,0,0,0,C_BORDER));
        JButton btnAdd=makePBtn("직원 등록"); JButton btnEdit=makeSBtn("상세/수정"); JButton btnDel=makeSBtn("삭제");
        bp.add(btnAdd); bp.add(btnEdit); bp.add(btnDel);

        ActionListener openDetail=e->{
            int row=table.getSelectedRow();
            if(row<0){JOptionPane.showMessageDialog(f,"직원을 선택하세요.");return;}
            String code=(String)model.getValueAt(row,1);
            Employee emp=employees.stream().filter(x->x.code.equals(code)).findFirst().orElse(null);
            if(emp!=null) showEmpDetail(f,emp,reload);
        };
        btnEdit.addActionListener(openDetail);
        table.addMouseListener(new MouseAdapter(){public void mouseClicked(MouseEvent e){if(e.getClickCount()==2)openDetail.actionPerformed(null);}});
        btnAdd.addActionListener(e->showEmpRegister(f,reload));
        btnDel.addActionListener(e->{
            int row=table.getSelectedRow();
            if(row<0){JOptionPane.showMessageDialog(f,"직원을 선택하세요.");return;}
            String name=(String)model.getValueAt(row,2),code=(String)model.getValueAt(row,1);
            if(JOptionPane.showConfirmDialog(f,"["+name+"] 직원을 삭제하시겠습니까?","삭제 확인",JOptionPane.YES_NO_OPTION)==JOptionPane.YES_OPTION){
                employees.removeIf(x->x.code.equals(code));
                attendances.removeIf(x->x.empCode.equals(code));
                salaries.removeIf(x->x.empCode.equals(code));
                reload.run(); JOptionPane.showMessageDialog(f,"삭제되었습니다.");
            }
        });

        // 네비 버튼 이벤트 (버튼 생성 시 직접 리스너 연결)
        for(Component c : nav.getComponents()){
            if(!(c instanceof JButton)) continue;
            JButton nb=(JButton)c;
            String lbl=nb.getText();
            nb.addActionListener(e->{
                switch(lbl){
                    case "직원 등록":    showEmpRegister(f,reload); break;
                    case "근태 등록":    showAttRegister(f); break;
                    case "근태 현황":    showAttList(f); break;
                    case "급여 계산":    showSalaryCalc(f); break;
                    case "급여 명세":    showSalaryList(f); break;
                    case "통계 대시보드": showStats(f); break;
                }
            });
        }

        JPanel south=new JPanel(new BorderLayout());
        south.add(bp,BorderLayout.NORTH); south.add(makeStatusBar(loginUser),BorderLayout.SOUTH);

        root.add(makeScroll(table),BorderLayout.CENTER);
        root.add(south,BorderLayout.SOUTH);
        f.setContentPane(root); f.setVisible(true);
    }

    // ══════════════════════════════════════════════
    //  폼 2 — 직원 등록 (다이얼로그)
    // ══════════════════════════════════════════════
    static void showEmpRegister(JFrame parent, Runnable refresh){
        JDialog dlg=new JDialog(parent,"직원 등록",true);
        dlg.setSize(430,450); dlg.setLocationRelativeTo(parent); dlg.setResizable(false);
        JPanel root=new JPanel(new BorderLayout());
        root.add(makeHeader("직원 등록","신규 직원 정보 입력"),BorderLayout.NORTH);

        int nextNo=employees.stream().mapToInt(e->e.eno).max().orElse(0)+1;
        String nextCode=String.format("EMP-%03d",nextNo);

        JPanel body=new JPanel(new GridBagLayout());
        body.setBorder(BorderFactory.createEmptyBorder(14,24,14,24));
        GridBagConstraints gc=new GridBagConstraints(); gc.insets=new Insets(5,5,5,5); gc.fill=GridBagConstraints.HORIZONTAL;

        JTextField txtCode=makeROTF(nextCode);
        JTextField txtName=makeTF(""), txtDate=makeTF("2024-01-01"), txtId=makeTF(""), txtPw2=makeTF("");
        JComboBox<String> cmbTeam=makeCmb(TEAM_NAMES), cmbGrade=makeCmb(GRADE_NAMES);
        ButtonGroup bg=new ButtonGroup();
        JRadioButton rOn=new JRadioButton("재직",true),rLeave=new JRadioButton("휴직"),rOff=new JRadioButton("퇴직");
        for(JRadioButton r:new JRadioButton[]{rOn,rLeave,rOff}){r.setFont(F_DEF);bg.add(r);}
        JPanel rp=new JPanel(new FlowLayout(FlowLayout.LEFT,4,0)); rp.setOpaque(false);
        rp.add(rOn); rp.add(rLeave); rp.add(rOff);

        Object[][] rows={{"사원코드",txtCode},{"이름 *",txtName},{"부서 *",cmbTeam},{"직책 *",cmbGrade},
                         {"입사일 *",txtDate},{"아이디 *",txtId},{"비밀번호 *",txtPw2},{"상태",rp}};
        for(int i=0;i<rows.length;i++){
            addGBC(body,makeLbl((String)rows[i][0]),gc,0,i,1,0);
            addGBC(body,(Component)rows[i][1],gc,1,i,1,1);
        }
        JPanel bp=new JPanel(new FlowLayout(FlowLayout.RIGHT,8,0)); bp.setOpaque(false);
        JButton btnSave=makePBtn("저장"),btnClear=makeSBtn("초기화"),btnClose=makeSBtn("닫기");
        bp.add(btnSave); bp.add(btnClear); bp.add(btnClose);
        addGBC(body,bp,gc,0,rows.length,2,1);

        btnClear.addActionListener(e->{txtName.setText("");txtDate.setText("2024-01-01");txtId.setText("");txtPw2.setText("");cmbTeam.setSelectedIndex(0);cmbGrade.setSelectedIndex(0);});
        btnClose.addActionListener(e->dlg.dispose());
        btnSave.addActionListener(e->{
            if(txtName.getText().trim().isEmpty()||txtId.getText().trim().isEmpty()){
                JOptionPane.showMessageDialog(dlg,"필수 항목(*)을 모두 입력하세요.");return;
            }
            if(!txtDate.getText().matches("\\d{4}-\\d{2}-\\d{2}")){
                JOptionPane.showMessageDialog(dlg,"입사일 형식을 확인하세요. (YYYY-MM-DD)");return;
            }
            String newId=txtId.getText().trim();
            if(employees.stream().anyMatch(x->x.id.equals(newId))){
                JOptionPane.showMessageDialog(dlg,"이미 사용 중인 아이디입니다.");return;
            }
            int st=rOn.isSelected()?0:rLeave.isSelected()?1:2;
            employees.add(new Employee(nextNo,nextCode,txtName.getText().trim(),
                cmbTeam.getSelectedIndex(),cmbGrade.getSelectedIndex(),
                txtDate.getText().trim(),st,newId,txtPw2.getText()));
            refresh.run(); JOptionPane.showMessageDialog(dlg,"저장되었습니다."); dlg.dispose();
        });
        root.add(body,BorderLayout.CENTER); dlg.setContentPane(root); dlg.setVisible(true);
    }

    // ══════════════════════════════════════════════
    //  폼 4 — 직원 상세/수정 (다이얼로그)
    // ══════════════════════════════════════════════
    static void showEmpDetail(JFrame parent, Employee emp, Runnable refresh){
        JDialog dlg=new JDialog(parent,"직원 상세/수정 — "+emp.code,true);
        dlg.setSize(430,360); dlg.setLocationRelativeTo(parent); dlg.setResizable(false);
        JPanel root=new JPanel(new BorderLayout());
        root.add(makeHeader("직원 상세 / 수정",emp.code+"  "+emp.name),BorderLayout.NORTH);

        JPanel body=new JPanel(new GridBagLayout());
        body.setBorder(BorderFactory.createEmptyBorder(14,24,14,24));
        GridBagConstraints gc=new GridBagConstraints(); gc.insets=new Insets(5,5,5,5); gc.fill=GridBagConstraints.HORIZONTAL;

        JTextField txtCode=makeROTF(emp.code);
        JTextField txtName=makeTF(emp.name), txtDate=makeTF(emp.date);
        JComboBox<String> cmbTeam=makeCmb(TEAM_NAMES); cmbTeam.setSelectedIndex(emp.team);
        JComboBox<String> cmbGrade=makeCmb(GRADE_NAMES); cmbGrade.setSelectedIndex(emp.grade);
        ButtonGroup bg=new ButtonGroup();
        JRadioButton rOn=new JRadioButton("재직"),rLeave=new JRadioButton("휴직"),rOff=new JRadioButton("퇴직");
        for(JRadioButton r:new JRadioButton[]{rOn,rLeave,rOff}){r.setFont(F_DEF);bg.add(r);}
        if(emp.state==0)rOn.setSelected(true); else if(emp.state==1)rLeave.setSelected(true); else rOff.setSelected(true);
        JPanel rp=new JPanel(new FlowLayout(FlowLayout.LEFT,4,0)); rp.setOpaque(false);
        rp.add(rOn); rp.add(rLeave); rp.add(rOff);

        Object[][] rows={{"사원코드 (수정불가)",txtCode},{"이름 *",txtName},{"부서",cmbTeam},
                         {"직책",cmbGrade},{"입사일",txtDate},{"상태",rp}};
        for(int i=0;i<rows.length;i++){
            addGBC(body,makeLbl((String)rows[i][0]),gc,0,i,1,0);
            addGBC(body,(Component)rows[i][1],gc,1,i,1,1);
        }
        JPanel bp=new JPanel(new FlowLayout(FlowLayout.RIGHT,8,0)); bp.setOpaque(false);
        JButton btnSave=makePBtn("수정 저장"),btnClose=makeSBtn("취소");
        bp.add(btnSave); bp.add(btnClose);
        addGBC(body,bp,gc,0,rows.length,2,1);

        btnClose.addActionListener(e->dlg.dispose());
        btnSave.addActionListener(e->{
            if(txtName.getText().trim().isEmpty()){JOptionPane.showMessageDialog(dlg,"이름을 입력하세요.");return;}
            if(!txtDate.getText().matches("\\d{4}-\\d{2}-\\d{2}")){JOptionPane.showMessageDialog(dlg,"입사일 형식 확인 (YYYY-MM-DD)");return;}
            if(JOptionPane.showConfirmDialog(dlg,"수정하시겠습니까?","수정 확인",JOptionPane.YES_NO_OPTION)==JOptionPane.YES_OPTION){
                emp.name=txtName.getText().trim(); emp.team=cmbTeam.getSelectedIndex();
                emp.grade=cmbGrade.getSelectedIndex(); emp.date=txtDate.getText().trim();
                emp.state=rOn.isSelected()?0:rLeave.isSelected()?1:2;
                refresh.run(); JOptionPane.showMessageDialog(dlg,"수정되었습니다."); dlg.dispose();
            }
        });
        root.add(body,BorderLayout.CENTER); dlg.setContentPane(root); dlg.setVisible(true);
    }

    // ══════════════════════════════════════════════
    //  폼 5 — 근태 등록
    // ══════════════════════════════════════════════
    static void showAttRegister(JFrame parent){
        JFrame f=new JFrame("근태 등록");
        f.setSize(480,380); f.setLocationRelativeTo(parent); f.setResizable(false);

        JPanel root=new JPanel(new BorderLayout());
        root.add(makeHeader("근태 등록","출퇴근 기록 입력"),BorderLayout.NORTH);

        JPanel body=new JPanel(new GridBagLayout());
        body.setBorder(BorderFactory.createEmptyBorder(14,30,14,30));
        GridBagConstraints gc=new GridBagConstraints(); gc.insets=new Insets(6,6,6,6); gc.fill=GridBagConstraints.HORIZONTAL;

        List<Employee> active=employees.stream().filter(e->e.state==0).collect(Collectors.toList());
        String[] empItems=active.stream().map(e->e.code+" "+e.name).toArray(String[]::new);
        JComboBox<String> cmbEmp=makeCmb(empItems);
        JTextField txtDate=makeTF("2024-03-15");
        JTextField txtIn=makeTF("09:00"), txtOut=makeTF("18:00");
        String[] types={"정상","지각","조퇴","결근"};
        JComboBox<String> cmbType=makeCmb(types);

        // 결근 선택 시 시각 비활성화
        cmbType.addActionListener(e->{
            boolean absent=cmbType.getSelectedItem().equals("결근");
            txtIn.setEnabled(!absent); txtOut.setEnabled(!absent);
            if(absent){txtIn.setText("—");txtOut.setText("—");}
            else{txtIn.setText("09:00");txtOut.setText("18:00");}
        });

        Object[][] rows={{"직원 선택 *",cmbEmp},{"근무 일자 *",txtDate},{"출근 시각",txtIn},{"퇴근 시각",txtOut},{"근무 유형 *",cmbType}};
        for(int i=0;i<rows.length;i++){
            addGBC(body,makeLbl((String)rows[i][0]),gc,0,i,1,0);
            addGBC(body,(Component)rows[i][1],gc,1,i,1,1);
        }
        JPanel bp=new JPanel(new FlowLayout(FlowLayout.LEFT,8,0)); bp.setOpaque(false);
        JButton btnSave=makePBtn("저장"),btnClear=makeSBtn("초기화"),btnList=makeSBtn("근태 현황 보기");
        bp.add(btnSave); bp.add(btnClear); bp.add(btnList);
        addGBC(body,bp,gc,0,rows.length,2,1);

        btnClear.addActionListener(e->{txtDate.setText("2024-03-15");cmbType.setSelectedIndex(0);txtIn.setText("09:00");txtOut.setText("18:00");});
        btnList.addActionListener(e->{f.dispose();showAttList(parent);});
        btnSave.addActionListener(e->{
            if(cmbEmp.getItemCount()==0){JOptionPane.showMessageDialog(f,"재직 중인 직원이 없습니다.");return;}
            if(!txtDate.getText().matches("\\d{4}-\\d{2}-\\d{2}")){JOptionPane.showMessageDialog(f,"날짜 형식 확인 (YYYY-MM-DD)");return;}
            String selCode=empItems[cmbEmp.getSelectedIndex()].split(" ")[0];
            String date=txtDate.getText().trim();
            boolean dup=attendances.stream().anyMatch(a->a.empCode.equals(selCode)&&a.workDate.equals(date));
            if(dup){JOptionPane.showMessageDialog(f,"이미 등록된 근태 기록이 있습니다.");return;}
            boolean absent=cmbType.getSelectedItem().equals("결근");
            String inT=absent?"—":txtIn.getText().trim(), outT=absent?"—":txtOut.getText().trim();
            if(!absent&&!inT.isEmpty()&&!outT.isEmpty()&&outT.compareTo(inT)<0){
                JOptionPane.showMessageDialog(f,"퇴근 시각이 출근 시각보다 빠릅니다.");return;
            }
            String attId=String.format("ATT-%04d",attendances.size()+1);
            attendances.add(new Attendance(attId,selCode,date,inT,outT,(String)cmbType.getSelectedItem()));
            JOptionPane.showMessageDialog(f,"저장되었습니다."); btnClear.doClick();
        });
        root.add(body,BorderLayout.CENTER);
        root.add(makeStatusBar(loginUser),BorderLayout.SOUTH);
        f.setContentPane(root); f.setVisible(true);
    }

    // ══════════════════════════════════════════════
    //  폼 6 — 근태 현황
    // ══════════════════════════════════════════════
    static void showAttList(JFrame parent){
        JFrame f=new JFrame("근태 현황");
        f.setSize(860,500); f.setLocationRelativeTo(parent);

        JPanel root=new JPanel(new BorderLayout());
        root.add(makeHeader("근태 현황","기간별 근태 내역 조회"),BorderLayout.NORTH);

        JPanel sp=new JPanel(new FlowLayout(FlowLayout.LEFT,8,6));
        sp.setBackground(new Color(250,251,255));
        sp.setBorder(BorderFactory.createMatteBorder(0,0,1,0,C_BORDER));
        List<String> empOpts=new ArrayList<>(); empOpts.add("전체 직원");
        employees.forEach(e->empOpts.add(e.code+" "+e.name));
        JComboBox<String> cmbEmp=makeCmb(empOpts.toArray(new String[0]));
        JTextField txtFrom=makeTF("2024-03-01"), txtTo=makeTF("2024-03-15");
        String[] types={"전체","정상","지각","조퇴","결근"};
        JComboBox<String> cmbType=makeCmb(types);
        JButton btnSearch=makePBtn("조회"); JLabel lblSummary=new JLabel(); lblSummary.setFont(F_SM);
        sp.add(makeLbl("직원:")); sp.add(cmbEmp); sp.add(makeLbl("기간:")); sp.add(txtFrom);
        sp.add(makeLbl("~")); sp.add(txtTo); sp.add(cmbType); sp.add(btnSearch);
        sp.add(Box.createHorizontalStrut(10)); sp.add(lblSummary);

        String[] cols={"근태ID","사원코드","이름","날짜","출근","퇴근","유형"};
        DefaultTableModel model=new DefaultTableModel(cols,0){public boolean isCellEditable(int r,int c){return false;}};
        JTable table=makeTable(model);
        int[] ws={80,90,70,100,70,70,70}; for(int i=0;i<ws.length;i++) table.getColumnModel().getColumn(i).setPreferredWidth(ws[i]);

        Runnable reload=()->{
            model.setRowCount(0);
            String from=txtFrom.getText().trim(), to=txtTo.getText().trim();
            int empIdx=cmbEmp.getSelectedIndex()-1; String typeFilter=(String)cmbType.getSelectedItem();
            int tot=0,n=0,l=0,et=0,ab=0;
            for(Attendance a:attendances){
                if(empIdx>=0&&!a.empCode.equals(employees.get(empIdx<employees.size()?empIdx:0).code)){
                    // 간단히 선택된 직원코드로 비교
                    String selCode=empOpts.get(cmbEmp.getSelectedIndex()).split(" ")[0];
                    if(!a.empCode.equals(selCode)) continue;
                }
                if(a.workDate.compareTo(from)<0||a.workDate.compareTo(to)>0) continue;
                if(!typeFilter.equals("전체")&&!a.workType.equals(typeFilter)) continue;
                Employee em=employees.stream().filter(x->x.code.equals(a.empCode)).findFirst().orElse(null);
                String eName=em!=null?em.name:"?";
                model.addRow(new Object[]{a.attId,a.empCode,eName,a.workDate,a.inTime,a.outTime,a.workType});
                tot++; switch(a.workType){case "정상":n++;break;case "지각":l++;break;case "조퇴":et++;break;case "결근":ab++;break;}
            }
            lblSummary.setText(String.format("총 %d건  |  정상 %d · 지각 %d · 조퇴 %d · 결근 %d",tot,n,l,et,ab));
        };
        reload.run(); btnSearch.addActionListener(e->reload.run());

        JPanel bp=new JPanel(new FlowLayout(FlowLayout.LEFT,8,6)); bp.setBackground(new Color(250,251,255));
        bp.setBorder(BorderFactory.createMatteBorder(1,0,0,0,C_BORDER));
        JButton btnReg=makePBtn("근태 등록"), btnBack=makeSBtn("메인으로");
        bp.add(btnReg); bp.add(btnBack);
        btnReg.addActionListener(e->{f.dispose();showAttRegister(parent);});
        btnBack.addActionListener(e->f.dispose());

        JPanel south=new JPanel(new BorderLayout());
        south.add(bp,BorderLayout.NORTH); south.add(makeStatusBar(loginUser),BorderLayout.SOUTH);
        root.add(sp,BorderLayout.NORTH); root.add(makeScroll(table),BorderLayout.CENTER); root.add(south,BorderLayout.SOUTH);
        f.setContentPane(root); f.setVisible(true);
    }

    // ══════════════════════════════════════════════
    //  폼 7 — 급여 계산
    // ══════════════════════════════════════════════
    static void showSalaryCalc(JFrame parent){
        JFrame f=new JFrame("급여 계산");
        f.setSize(480,480); f.setLocationRelativeTo(parent); f.setResizable(false);

        JPanel root=new JPanel(new BorderLayout());
        root.add(makeHeader("급여 계산","월별 급여 자동 산출"),BorderLayout.NORTH);

        JPanel body=new JPanel(new GridBagLayout());
        body.setBorder(BorderFactory.createEmptyBorder(14,30,14,30));
        GridBagConstraints gc=new GridBagConstraints(); gc.insets=new Insets(6,6,6,6); gc.fill=GridBagConstraints.HORIZONTAL;

        String[] empItems=employees.stream().filter(e->e.state==0).map(e->e.code+" "+e.name).toArray(String[]::new);
        JComboBox<String> cmbEmp=makeCmb(empItems);
        JTextField txtYM=makeROTF(LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM")));
        JTextField txtBase=makeROTF(""), txtOtHour=makeTF("0"), txtOtPay=makeROTF(""), txtDed=makeROTF(""), txtNet=makeROTF("");
        txtNet.setFont(F_LG); txtNet.setForeground(C_NAVY);

        Runnable calcAll=()->{
            try{
                int idx=cmbEmp.getSelectedIndex();
                if(idx<0||empItems.length==0) return;
                String code=empItems[idx].split(" ")[0];
                Employee em=employees.stream().filter(x->x.code.equals(code)).findFirst().orElse(null);
                if(em==null) return;
                double base=em.baseSalary(); txtBase.setText(fmt(base));
                int ot=Integer.parseInt(txtOtHour.getText().trim());
                if(ot<0) throw new NumberFormatException();
                double otPay=Math.floor(base/209.0*1.5*ot);
                double ded=Math.floor((base+otPay)* 0.2);
                double net=base+otPay-ded;
                txtOtPay.setText(fmt(otPay)); txtDed.setText(fmt(ded)); txtNet.setText(fmt(net));
            }catch(NumberFormatException ex){JOptionPane.showMessageDialog(f,"초과근무 시간은 0 이상 숫자로 입력하세요.");}
        };
        cmbEmp.addActionListener(e->calcAll.run());
        if(empItems.length>0) calcAll.run();

        Object[][] rows={{"직원 선택 *",cmbEmp},{"급여 년월 ",txtYM},{"기본급",txtBase},
                         {"초과근무 시간",txtOtHour},{"초과근무 수당",txtOtPay},{"세금 (20%)",txtDed},{"실수령액",txtNet}};
        for(int i=0;i<rows.length;i++){
            addGBC(body,makeLbl((String)rows[i][0]),gc,0,i,1,0);
            addGBC(body,(Component)rows[i][1],gc,1,i,1,1);
        }
        JPanel bp=new JPanel(new FlowLayout(FlowLayout.LEFT,8,0)); bp.setOpaque(false);
        JButton btnCalc=makePBtn("계산"), btnSave=makePBtn("급여 저장"), btnBack=makeSBtn("닫기");
        bp.add(btnCalc); bp.add(btnSave); bp.add(btnBack);
        addGBC(body,bp,gc,0,rows.length,2,1);

        btnCalc.addActionListener(e->calcAll.run());
        btnBack.addActionListener(e->f.dispose());
        btnSave.addActionListener(e->{
            if(empItems.length==0){JOptionPane.showMessageDialog(f,"직원이 없습니다.");return;}
            if(!txtYM.getText().matches("\\d{4}-\\d{2}")){JOptionPane.showMessageDialog(f,"년월 형식 확인 (YYYY-MM)");return;}
            String code=empItems[cmbEmp.getSelectedIndex()].split(" ")[0];
            String ym=txtYM.getText().trim();
            boolean dup=salaries.stream().anyMatch(s->s.empCode.equals(code)&&s.yearMonth.equals(ym));
            if(dup){
                if(JOptionPane.showConfirmDialog(f,"이미 저장된 급여가 있습니다. 덮어쓰시겠습니까?","확인",JOptionPane.YES_NO_OPTION)!=JOptionPane.YES_OPTION) return;
                salaries.removeIf(s->s.empCode.equals(code)&&s.yearMonth.equals(ym));
            }
            try{
                Employee em=employees.stream().filter(x->x.code.equals(code)).findFirst().orElse(null);
                double base=em.baseSalary();
                int ot=Integer.parseInt(txtOtHour.getText().trim());
                double otPay=Math.floor(base/209.0*1.5*ot);
                double ded=Math.floor((base+otPay)*0.033);
                double net=base+otPay-ded;
                String salId=String.format("SAL-%04d",salaries.size()+1);
                salaries.add(new Salary(salId,code,ym,base,ot,otPay,ded,net));
                JOptionPane.showMessageDialog(f,"급여가 저장되었습니다.");
            }catch(Exception ex){JOptionPane.showMessageDialog(f,"저장 오류: "+ex.getMessage());}
        });
        root.add(body,BorderLayout.CENTER);
        root.add(makeStatusBar(loginUser),BorderLayout.SOUTH);
        f.setContentPane(root); f.setVisible(true);
    }

    // ══════════════════════════════════════════════
    //  폼 8 — 급여 명세 조회
    // ══════════════════════════════════════════════
    static void showSalaryList(JFrame parent){
        JFrame f=new JFrame("급여 명세 조회");
        f.setSize(920,520); f.setLocationRelativeTo(parent);

        JPanel root=new JPanel(new BorderLayout());
        root.add(makeHeader("급여 명세 조회","직원별 급여 내역"),BorderLayout.NORTH);

        JPanel sp=new JPanel(new FlowLayout(FlowLayout.LEFT,8,6));
        sp.setBackground(new Color(250,251,255));
        sp.setBorder(BorderFactory.createMatteBorder(0,0,1,0,C_BORDER));
        List<String> opts=new ArrayList<>(); opts.add("전체 직원");
        employees.forEach(e->opts.add(e.code+" "+e.name));
        JComboBox<String> cmbEmp=makeCmb(opts.toArray(new String[0]));
        Set<String> years=salaries.stream().map(s->s.yearMonth.substring(0,4)).collect(Collectors.toCollection(LinkedHashSet::new));
        List<String> yearList=new ArrayList<>(); yearList.add("전체 연도"); yearList.addAll(years);
        JComboBox<String> cmbYear=makeCmb(yearList.toArray(new String[0]));
        JButton btnSearch=makePBtn("조회"); JLabel lblTotal=new JLabel(); lblTotal.setFont(F_SM);
        sp.add(makeLbl("직원:")); sp.add(cmbEmp); sp.add(makeLbl("연도:")); sp.add(cmbYear); sp.add(btnSearch);
        sp.add(Box.createHorizontalStrut(10)); sp.add(lblTotal);

        String[] cols={"사원코드","이름","년월","기본급","수당","공제","실수령액"};
        DefaultTableModel model=new DefaultTableModel(cols,0){public boolean isCellEditable(int r,int c){return false;}};
        JTable table=makeTable(model);
        int[] ws={90,75,80,110,110,100,120}; for(int i=0;i<ws.length;i++) table.getColumnModel().getColumn(i).setPreferredWidth(ws[i]);

        Runnable reload=()->{
            model.setRowCount(0); double totalNet=0;
            int empIdx=cmbEmp.getSelectedIndex()-1;
            String yearSel=(String)cmbYear.getSelectedItem();
            for(Salary s:salaries){
                if(empIdx>=0){
                    String selCode=opts.get(cmbEmp.getSelectedIndex()).split(" ")[0];
                    if(!s.empCode.equals(selCode)) continue;
                }
                if(!yearSel.equals("전체 연도")&&!s.yearMonth.startsWith(yearSel)) continue;
                Employee em=employees.stream().filter(x->x.code.equals(s.empCode)).findFirst().orElse(null);
                String eName=em!=null?em.name:"?";
                model.addRow(new Object[]{s.empCode,eName,s.yearMonth,fmt(s.base),fmt(s.overtime),fmt(s.deduction),fmt(s.net)});
                totalNet+=s.net;
            }
            lblTotal.setText("실수령액 합계: "+fmt(totalNet));
        };
        reload.run(); btnSearch.addActionListener(e->reload.run());

        JPanel bp=new JPanel(new FlowLayout(FlowLayout.LEFT,8,6)); bp.setBackground(new Color(250,251,255));
        bp.setBorder(BorderFactory.createMatteBorder(1,0,0,0,C_BORDER));
        JButton btnPrint=makePBtn("명세서 출력"), btnCalc=makeSBtn("급여 계산 이동"), btnBack=makeSBtn("메인으로");
        bp.add(btnPrint); bp.add(btnCalc); bp.add(btnBack);

        btnPrint.addActionListener(e->{
            int row=table.getSelectedRow();
            if(row<0){JOptionPane.showMessageDialog(f,"명세서를 출력할 행을 선택하세요.");return;}
            String empCode=(String)model.getValueAt(row,0);
            String empName=(String)model.getValueAt(row,1);
            String ym=(String)model.getValueAt(row,2);
            String base=(String)model.getValueAt(row,3);
            String ot=(String)model.getValueAt(row,4);
            String ded=(String)model.getValueAt(row,5);
            String net=(String)model.getValueAt(row,6);
            showPayslip(f,empCode,empName,ym,base,ot,ded,net);
        });
        btnCalc.addActionListener(e->{f.dispose();showSalaryCalc(parent);});
        btnBack.addActionListener(e->f.dispose());

        JPanel south=new JPanel(new BorderLayout());
        south.add(bp,BorderLayout.NORTH); south.add(makeStatusBar(loginUser),BorderLayout.SOUTH);
        root.add(sp,BorderLayout.NORTH); root.add(makeScroll(table),BorderLayout.CENTER); root.add(south,BorderLayout.SOUTH);
        f.setContentPane(root); f.setVisible(true);
    }

    // ── 급여 명세서 다이얼로그 ────────────────────────────────
    static void showPayslip(JFrame parent,String code,String name,String ym,String base,String ot,String ded,String net){
        JDialog dlg=new JDialog(parent,"급여 명세서",true);
        dlg.setSize(340,320); dlg.setLocationRelativeTo(parent); dlg.setResizable(false);
        JPanel root=new JPanel(new BorderLayout());
        root.add(makeHeader("급여 명세서",ym+" — "+name),BorderLayout.NORTH);

        JPanel body=new JPanel(new GridLayout(0,2,0,0));
        body.setBorder(BorderFactory.createEmptyBorder(10,20,10,20));
        String[][] items={{"사원코드",code},{"이름",name},{"년월",ym},{"─────────","─────────"},
                          {"기본급",base},{"초과근무 수당",ot},{"소득세 공제",ded},{"─────────","─────────"},{"실수령액",net}};
        for(String[] row:items){
            JLabel lk=new JLabel(row[0]); lk.setFont(row[0].startsWith("─")?F_SM:F_SM);
            lk.setForeground(row[0].startsWith("─")?Color.LIGHT_GRAY:new Color(80,80,80));
            JLabel lv=new JLabel(row[1],SwingConstants.RIGHT);
            lv.setFont(row[0].equals("실수령액")?F_LG:F_SM);
            lv.setForeground(row[0].equals("실수령액")?C_NAVY:Color.BLACK);
            body.add(lk); body.add(lv);
        }
        JPanel bp=new JPanel(new FlowLayout(FlowLayout.CENTER));
        JButton btnClose=makeSBtn("닫기"); btnClose.addActionListener(e->dlg.dispose());
        bp.add(btnClose);
        root.add(body,BorderLayout.CENTER); root.add(bp,BorderLayout.SOUTH);
        dlg.setContentPane(root); dlg.setVisible(true);
    }

    // ══════════════════════════════════════════════
    //  폼 9 — 통계 대시보드
    // ══════════════════════════════════════════════
    static void showStats(JFrame parent){
        JFrame f=new JFrame("통계 대시보드");
        f.setSize(860,560); f.setLocationRelativeTo(parent);

        JPanel root=new JPanel(new BorderLayout());
        root.add(makeHeader("통계 대시보드","부서별·월별 급여 집계"),BorderLayout.NORTH);

        // 상단 필터
        JPanel sp=new JPanel(new FlowLayout(FlowLayout.LEFT,8,6));
        sp.setBackground(new Color(250,251,255));
        sp.setBorder(BorderFactory.createMatteBorder(0,0,1,0,C_BORDER));
        Set<String> yms=salaries.stream().map(s->s.yearMonth).collect(Collectors.toCollection(LinkedHashSet::new));
        List<String> ymList=new ArrayList<>(yms);
        JComboBox<String> cmbYM=makeCmb(ymList.toArray(new String[0]));
        if(!ymList.isEmpty()) cmbYM.setSelectedIndex(ymList.size()-1);
        JButton btnRefresh=makePBtn("집계");
        sp.add(makeLbl("기준 년월:")); sp.add(cmbYM); sp.add(btnRefresh);

        // 요약 카드
        JPanel cards=new JPanel(new GridLayout(1,3,10,0));
        cards.setBorder(BorderFactory.createEmptyBorder(10,12,10,12));
        cards.setBackground(root.getBackground());
        JLabel lblCnt=makeStatCard("지급 인원","0명",cards);
        JLabel lblSum=makeStatCard("총 실수령액","0원",cards);
        JLabel lblAvg=makeStatCard("1인 평균","0원",cards);

        // 부서별 테이블
        String[] deptCols={"부서","인원","합계","평균","최고","최저"};
        DefaultTableModel deptModel=new DefaultTableModel(deptCols,0){public boolean isCellEditable(int r,int c){return false;}};
        JTable deptTable=makeTable(deptModel);

        // 상위 3명 테이블
        String[] topCols={"순위","사원코드","이름","부서","실수령액"};
        DefaultTableModel topModel=new DefaultTableModel(topCols,0){public boolean isCellEditable(int r,int c){return false;}};
        JTable topTable=makeTable(topModel);

        Runnable reload=()->{
            deptModel.setRowCount(0); topModel.setRowCount(0);
            String ym=(String)cmbYM.getSelectedItem();
            if(ym==null){lblCnt.setText("0명");lblSum.setText("0원");lblAvg.setText("0원");return;}
            List<Salary> sel=salaries.stream().filter(s->s.yearMonth.equals(ym)).collect(Collectors.toList());
            if(sel.isEmpty()){
                JOptionPane.showMessageDialog(f,"해당 월의 급여 데이터가 없습니다."); return;
            }
            int cnt=sel.size(); double sum=sel.stream().mapToDouble(s->s.net).sum(); double avg=sum/cnt;
            lblCnt.setText(cnt+"명"); lblSum.setText(fmt(sum)); lblAvg.setText(fmt(avg));

            // 부서별 집계
            for(int t=0;t<TEAM_NAMES.length;t++){
                final int team=t;
                List<Salary> ds=sel.stream().filter(s->{
                    Employee em=employees.stream().filter(x->x.code.equals(s.empCode)).findFirst().orElse(null);
                    return em!=null&&em.team==team;
                }).collect(Collectors.toList());
                if(ds.isEmpty()) continue;
                double ds_sum=ds.stream().mapToDouble(s->s.net).sum();
                double ds_avg=ds_sum/ds.size();
                double ds_max=ds.stream().mapToDouble(s->s.net).max().orElse(0);
                double ds_min=ds.stream().mapToDouble(s->s.net).min().orElse(0);
                deptModel.addRow(new Object[]{TEAM_NAMES[t],ds.size(),fmt(ds_sum),fmt(ds_avg),fmt(ds_max),fmt(ds_min)});
            }
            // 상위 3명
            sel.stream().sorted((a,b)->Double.compare(b.net,a.net)).limit(3).forEach((s)->{
                Employee em=employees.stream().filter(x->x.code.equals(s.empCode)).findFirst().orElse(null);
                String name=em!=null?em.name:"?"; String team=em!=null?em.teamName():"?";
                int rank=topModel.getRowCount()+1;
                topModel.addRow(new Object[]{rank+"위",s.empCode,name,team,fmt(s.net)});
            });
        };
        if(!ymList.isEmpty()) reload.run();
        btnRefresh.addActionListener(e->reload.run());

        // 레이아웃 조립
        JPanel tables=new JPanel(new GridLayout(1,2,10,0));
        tables.setBorder(BorderFactory.createEmptyBorder(0,10,10,10));
        JPanel lp=new JPanel(new BorderLayout(0,4)); lp.setOpaque(false);
        lp.add(makeSectionLbl("부서별 급여 집계"),BorderLayout.NORTH); lp.add(makeScroll(deptTable),BorderLayout.CENTER);
        JPanel rp2=new JPanel(new BorderLayout(0,4)); rp2.setOpaque(false);
        rp2.add(makeSectionLbl("실수령액 상위 3명"),BorderLayout.NORTH); rp2.add(makeScroll(topTable),BorderLayout.CENTER);
        tables.add(lp); tables.add(rp2);

        JPanel center=new JPanel(new BorderLayout());
        center.add(cards,BorderLayout.NORTH); center.add(tables,BorderLayout.CENTER);

        JPanel bp=new JPanel(new FlowLayout(FlowLayout.LEFT,8,6)); bp.setBackground(new Color(250,251,255));
        bp.setBorder(BorderFactory.createMatteBorder(1,0,0,0,C_BORDER));
        JButton btnBack=makeSBtn("메인으로"); bp.add(btnBack);
        btnBack.addActionListener(e->f.dispose());

        JPanel south=new JPanel(new BorderLayout());
        south.add(bp,BorderLayout.NORTH); south.add(makeStatusBar(loginUser),BorderLayout.SOUTH);

        root.add(sp,BorderLayout.NORTH); root.add(center,BorderLayout.CENTER); root.add(south,BorderLayout.SOUTH);
        f.setContentPane(root); f.setVisible(true);
    }

    static JLabel makeStatCard(String title, String value, JPanel parent){
        JPanel card=new JPanel(new BorderLayout(0,4)); card.setBackground(C_LBLUE);
        card.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(C_BORDER),BorderFactory.createEmptyBorder(10,14,10,14)));
        JLabel lt=new JLabel(title); lt.setFont(F_SM); lt.setForeground(new Color(80,80,120));
        JLabel lv=new JLabel(value); lv.setFont(F_LG); lv.setForeground(C_NAVY);
        card.add(lt,BorderLayout.NORTH); card.add(lv,BorderLayout.CENTER);
        parent.add(card); return lv;
    }
    static JLabel makeSectionLbl(String text){
        JLabel l=new JLabel("  "+text); l.setFont(F_BOLD); l.setForeground(C_NAVY);
        l.setBorder(BorderFactory.createMatteBorder(0,0,1,0,C_BORDER)); return l;
    }

    // ══════════════════════════════════════════════
    //  main
    // ══════════════════════════════════════════════
    public static void main(String[] args){
        try{UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());}catch(Exception e){}
        employees=loadEmployees();
        attendances=loadAttendances();
        salaries=loadSalaries(employees);
        SwingUtilities.invokeLater(()->showLogin());
    }
}