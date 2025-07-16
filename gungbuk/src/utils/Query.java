package utils;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;


public enum Query {

	
	product_gu_moon_count("SELECT count(pno) FROM `order` where pno = ?"),
	productCount("select count(pno) from product"),
	productAll("SELECT * FROM roupang.product where pno = ?"),
	reviewAvg("SELECT AVG(rating) FROM roupang.`order` join review on review.ono = `order`.ono where pno = ?"),
	productANDreview("SELECT product.*, AVG(review.rating) AS average_rating "
			+ "FROM product LEFT JOIN `order` ON product.pno = `order`.pno "
			+ "LEFT JOIN review ON `order`.ono = review.ono GROUP BY product.pno;"),
	productANDreviewW("SELECT product.*, AVG(review.rating) AS average_rating "
			+ "FROM product LEFT JOIN `order` ON product.pno = `order`.pno "
			+ "LEFT JOIN review ON `order`.ono = review.ono where cno = ? GROUP BY product.pno;"),
	category("SELECT cnam FROM category"),
	MainName("select * from user where uno = ?"),
	Login("SELECT * FROM roupang.user where uid = ? and upw = ?"),
	LoginOrder("SELECT * FROM roupang.`order` where uno = ?"),
	LoginJOp("SELECT count(ono) AS co, category.* "
			+ "	FROM `order` LEFT join product on `order`.pno = product.pno LEFT join category on category.cno = product.cno "
			+ "	where uno = ? group by cno order by co desc");
	
	String string;

	Query(String string){
		this.string = string;
	}
	public String label() {
		return string;
	}
	
	public List<Row> select(String...val) {
		List<Row> list = new ArrayList<Row>();
		try {
			Connection c = DriverManager.getConnection("jdbc:mysql://localhost/roupang?serverTimezone=UTC&allowLoadLocalInfile=true","root","1234");
			PreparedStatement ps = c.prepareStatement(string);
			for(int i = 0; i < val.length; i++) {
				ps.setString(i+1, val[i]);
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
}
