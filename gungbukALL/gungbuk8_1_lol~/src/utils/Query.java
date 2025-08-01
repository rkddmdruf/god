package utils;

import java.awt.*;
import java.io.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.ImageIcon.*;
import javax.swing.table.DefaultTableModel;


public enum Query {

	MainName("select * from user where uno = ?"),
	MainNameTEST("select * from user"),
	product("SELECT * FROM roupang.product;"),
	//AMain
	
	productCountCategory("SELECT product.pno, count(`order`.pno) AS co, product.cno FROM product "
			+ "LEFT JOIN `order`  ON `order`.pno = product.pno where product.cno = ? group by product.pno order by co desc, product.pno asc;"),
	productCount("SELECT product.pno, count(`order`.pno) AS co FROM product "
			+ " LEFT JOIN `order`  ON `order`.pno = product.pno group by product.pno order by co desc,product.pno asc;"),
	productAll("SELECT product.*, sum(`order`.quantity), AVG(rating) FROM roupang.product  "
			+ "LEFT JOIN `order` ON product.pno = `order`.pno left join review on `order`.ono = review.ono "
			+ "where product.pno = ?;"),
	////HOME
	
	category("SELECT cnam FROM category"),
	productANDreviewWCNO("SELECT product.*, AVG(review.rating) AS average_rating, sum(`order`.quantity) AS sums"
			+ "		FROM product LEFT JOIN `order` ON product.pno = `order`.pno "
			+ "		LEFT JOIN review ON `order`.ono = review.ono where cno = ? GROUP BY product.pno order by pno asc;"),
	
	
	productSerchisEm("SELECT product.*, AVG(review.rating) AS average_rating "
			+ "		FROM product LEFT JOIN `order` ON product.pno = `order`.pno "
			+ "		LEFT JOIN review ON `order`.ono = review.ono "
			+ "        where pname like concat('%', ?, '%') GROUP BY product.pno order by pno asc;"),
	productPriceMxMn("SELECT product.*, AVG(review.rating) AS average_rating , sum(`order`.quantity) AS sums"
			+ "			FROM product LEFT JOIN `order` ON product.pno = `order`.pno"
			+ "			LEFT JOIN review ON `order`.ono = review.ono "
			+ "			where price >= ? and price <= ? GROUP BY product.pno order by pno asc;"),
	//Serch
	productANDreviewWPNO("SELECT product.*, AVG(review.rating) AS average_rating, category.cnam"///이거 안쓸지도?
			+ "			FROM product"
			+ "         LEFT JOIN `order` ON product.pno = `order`.pno"
			+ "			LEFT JOIN review ON `order`.ono = review.ono"
			+ "         LEFT JOIN category ON product.cno = category.cno"
			+ "			WHERE product.pno = ?"
			+ "         GROUP BY product.pno;"),
	insert("insert into cart set uno = ?, pno = ?, quantity = ?;"),
	개수update("update product set product.quantity = ? where pno = ?;"),
	orderinsert("insert into `order` values(0, ?, ?, ?, ?, 0,0);"),
	//Detaill
	
	cart("SELECT cart.*, product.pname, product.price "
			+ "FROM roupang.cart "
			+ "LEFT JOIN product ON cart.pno = product.pno "
			+ "where uno = ?;"),
	cartdelete("delete from cart where ctno = ?;"),
	cart개수("select product.quantity from product where pno = ?"),
	//Cart
	
	GumeList("SELECT product.img,product.pname , `order`.quantity, product.price, product.price * `order`.quantity AS PQ, `order`.date, `order`.ono, product.pno"
			+ "		FROM roupang.`order`"
			+ "		LEFT JOIN product"
			+ "		ON `order`.pno = product.pno where `order`.uno = ?"
			+ "		group by `order`.ono;"),
	GumeDelet("delete from `order` where ono = ?"),
	reviewDelet("delete from review where ono = ?"),
	// GumeList
	Roupanglsit(" SELECT `order`.*, product.price, product.pname FROM roupang.`order` "
			+ " LEFT JOIN product "
			+ " ON `order`.pno = product.pno where `order`.uno = ? "
			+ " group by `order`.ono;"),
	//Roupang
	Login("SELECT * FROM roupang.user where uid = ? and upw = ?"),
	LoginOrder("SELECT * FROM roupang.`order` where uno = ?"),
	LoginJOp("SELECT count(ono) AS co, category.* "
			+ "	FROM `order` LEFT join product on `order`.pno = product.pno LEFT join category on category.cno = product.cno "
			+ "	where uno = ? group by cno order by co desc"),
	//Login
	
	product글자비교("SELECT * FROM roupang.product where pname = ?;"),
	productinsert("insert into product values(0,?,?,?,?,?,1);"),
	productPnameW("select * from product where pname = ?"),
	productUpdate("update product set cno = ?, pname = ?, description = ?, price = ?, quantity = ? where pno = ?;"),
	
	
	getOrder("select `order`.ono, product.pname, user.uname, `order`.quantity, product.price, product.price * `order`.quantity, `order`.pay, `order`.delivery "
			+"from `order` left join product ON `order`.pno = product.pno left join user ON `order`.uno = user.uno where delivery != 3;"),
	getOrderDW("select `order`.ono, product.pname, user.uname, `order`.quantity, product.price, product.price * `order`.quantity, `order`.pay, `order`.delivery "
			+"from `order` left join product ON `order`.pno = product.pno left join user ON `order`.uno = user.uno where delivery = ?;"),
	getOrderPW("select `order`.ono, product.pname, user.uname, `order`.quantity, product.price, product.price * `order`.quantity, `order`.pay, `order`.delivery "
			+"from `order` left join product ON `order`.pno = product.pno left join user ON `order`.uno = user.uno where delivery != 3 and pay = ?;"),
	
	deliveryUpdate("update `order` set delivery = ? where ono = ?;"),
	payUpdate("update `order` set pay = ? where ono = ?;"),
	
	chat("SELECT product.*, sum(`order`.quantity) AS sums FROM roupang.product "
			+ "LEFT JOIN `order` ON product.pno = `order`.pno where product.cno = ? group by pno order by sums desc, pno asc;"),
	// admin 관리자
	getImge("SELECT img FROM roupang.product where pno = ?;"),
	updateimg("update product set img = ? where pno = ?");
	
	//////img
	String string;

	Query(String string){
		this.string = string;
	}
	public String label() {
		return string;
	}
	public void upImg(String imagePath, int pno) {
		try {
			Connection c = DriverManager.getConnection("jdbc:mysql://localhost/roupang?serverTimezone=UTC&allowLoadLocalInfile=true","root","1234");
			File imageFile = new File(imagePath);
            FileInputStream fis = new FileInputStream(imageFile);
            byte[] imageData = fis.readAllBytes(); // 파일 내용을 바이트 배열로 읽어옴
            fis.close();
            PreparedStatement pstmt = c.prepareStatement(string);
            pstmt.setBytes(1, imageData); // 바이트 배열을 BLOB 컬럼에 설정
            pstmt.setObject(2, pno);
            pstmt.executeUpdate();
            pstmt.close();
            c.close();
		} catch (Exception e) {
			// TODO: handle exception
		}
	}
	public Image getImge(Object...val) {
		Image imageIcon = null;
		try {
			Connection c = DriverManager.getConnection("jdbc:mysql://localhost/roupang?serverTimezone=UTC&allowLoadLocalInfile=true","root","1234");
			PreparedStatement ps = c.prepareStatement(string);
			for(int i = 0; i < val.length; i++) {
				ps.setObject(i+1, val[i]);
			}
			ResultSet rs = ps.executeQuery();
			if (rs.next()) {
	            // BLOB 데이터 읽기
	            byte[] imageBytes = rs.getBytes("img");

	            // ImageIcon 생성 및 설정
	            if (imageBytes != null) {
	                ByteArrayInputStream bis = new ByteArrayInputStream(imageBytes);
	                try {
	                    Image image = ImageIO.read(bis);
	                    imageIcon = new ImageIcon(image).getImage();
	                } catch (IOException e) {
	                    e.printStackTrace();
	                }
	            } else {
	                System.out.println("이미지 데이터가 없습니다.");
	            }
	        } else {
	            System.out.println("데이터를 찾을 수 없습니다.");
	        }
			ps.close();c.close();
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		return imageIcon;
		
	}
	public List<Row> select(Object...val) {
		List<Row> list = new ArrayList<Row>();
		try {
			Connection c = DriverManager.getConnection("jdbc:mysql://localhost/roupang?serverTimezone=UTC&allowLoadLocalInfile=true","root","1234");
			PreparedStatement ps = c.prepareStatement(string);
			for(int i = 0; i < val.length; i++) {
				ps.setObject(i+1, val[i]);
			}
			
			ResultSet rs = ps.executeQuery();
			ResultSetMetaData rsm = rs.getMetaData();
			while(rs.next()) {
				Row row = new Row();
				for(int i = 0; i < rsm.getColumnCount(); i++) {
					row.add(rs.getObject(i+1));
				}
				list.add(row);
			}
			rs.close();ps.close();c.close();
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		return list;
	}
	
	public void update(Object...val) {
		try {
			Connection c = DriverManager.getConnection("jdbc:mysql://localhost/roupang?serverTimezone=UTC&allowLoadLocalInfile=true","root","1234");
			PreparedStatement ps = c.prepareStatement(string);
			for(int i = 0; i < val.length; i++) {
				ps.setObject(i+1, val[i]);
			}
			ps.executeUpdate();
			ps.close();
			c.close();
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}
	public static void setTable(DefaultTableModel model, List<Row> list) {
		int count = model.getRowCount();
		for(int i = 0; i < count; i++) {
			model.removeRow(0);
		}
		for(int i = 0; i < list.size(); i++) {
			Vector<Object> row = new Vector<Object>();
			for(int j = 0; j < list.get(i).size(); j++) {
				row.add(list.get(i).get(j));
			}
			model.addRow(row);
		}
	}
}
