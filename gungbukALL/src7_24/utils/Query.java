package utils;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import javax.swing.table.DefaultTableModel;


public enum Query {

	MainName("select * from user where uno = ?"),
	MainNameTEST("select * from user"),
	//AMain
	//SELECT product.pno, count(`order`.pno) AS co FROM product LEFT JOIN `order`  ON `order`.pno = product.pno group by product.pno order by co desc;
	productCountCategory("SELECT product.pno, count(`order`.pno) AS co, product.cno FROM product "
			+ "LEFT JOIN `order`  ON `order`.pno = product.pno where product.cno = ? group by product.pno order by co desc, product.pno asc;"),
	productCount("SELECT product.pno, count(`order`.pno) AS co FROM product LEFT JOIN `order`  ON `order`.pno = product.pno group by product.pno order by co desc,product.pno asc;"),
	productAll("SELECT * FROM roupang.product where pno = ?"),
	reviewAvg("SELECT AVG(rating) FROM roupang.`order` join review on review.ono = `order`.ono where pno = ?"),
	////HOME
	
	category("SELECT cnam FROM category"),
	
	productANDreview("SELECT product.*, AVG(review.rating) AS average_rating, category.cnam"
			+ "			FROM product"
			+ "         LEFT JOIN `order` ON product.pno = `order`.pno"
			+ "			LEFT JOIN review ON `order`.ono = review.ono"
			+ "         LEFT JOIN category ON product.cno = category.cno"
			+ "         GROUP BY product.pno;"),
	productANDreviewWCNO("SELECT product.*, AVG(review.rating) AS average_rating "
			+ "FROM product LEFT JOIN `order` ON product.pno = `order`.pno "
			+ "LEFT JOIN review ON `order`.ono = review.ono where cno = ? GROUP BY product.pno;"),
	
	
	
	productANDreviewWPNO("SELECT product.*, AVG(review.rating) AS average_rating, category.cnam"
			+ "			FROM product"
			+ "         LEFT JOIN `order` ON product.pno = `order`.pno"
			+ "			LEFT JOIN review ON `order`.ono = review.ono"
			+ "         LEFT JOIN category ON product.cno = category.cno"
			+ "			WHERE product.pno = ?"
			+ "         GROUP BY product.pno;"),
	productSerchisEm("SELECT * FROM roupang.product where pname  LIKE CONCAT('%',?,'%');"),
	//Serch
	
	insert("insert into cart set uno = ?, pno = ?, quantity = ?;"),
	개수update("update product set product.quantity = ? where pno = ?;"),
	orderinsert("insert into `order` values(0, ?, ?, ?, ?, 1,0);"),
	//Detaill
	
	cart("SELECT cart.*, product.pname, product.price "
			+ "FROM roupang.cart "
			+ "LEFT JOIN product ON cart.pno = product.pno "
			+ "where uno = ?;"),
	cartdelete("delete from cart where ctno = ?;"),
	cart개수("select product.quantity from product where pno = ?"),
	//Cart
	
	GumeList(" SELECT `order`.*, product.price, product.pname FROM roupang.`order` "
			+ " LEFT JOIN product "
			+ " ON `order`.pno = product.pno where `order`.uno = ? "
			+ " group by `order`.ono;"),
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
			+ "	where uno = ? group by cno order by co desc");
	//Login
	
	String string;

	Query(String string){
		this.string = string;
	}
	public String label() {
		return string;
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
