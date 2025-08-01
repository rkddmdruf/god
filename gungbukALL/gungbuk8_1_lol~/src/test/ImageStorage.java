package test;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class ImageStorage {

    
    public static void main(String[] args) throws SQLException {
    	ImageStorage img = new ImageStorage();
    	img.saveImage("src/img/1.jpg", DriverManager.getConnection("jdbc:mysql://localhost/roupang?serverTimezone=UTC&allowLoadLocalInfile=true","root","1234"));
	}
    public void saveImage(String imagePath, Connection connection) {
        try {
            File imageFile = new File(imagePath);
            FileInputStream fis = new FileInputStream(imageFile);
            byte[] imageData = fis.readAllBytes(); // 파일 내용을 바이트 배열로 읽어옴
            fis.close();

            String sql = "INSERT INTO product set img = ?";
            PreparedStatement pstmt = connection.prepareStatement(sql);
            pstmt.setBytes(1, imageData); // 바이트 배열을 BLOB 컬럼에 설정

            pstmt.executeUpdate();
            pstmt.close();
            connection.close();

        } catch (IOException | SQLException e) {
            e.printStackTrace();
        }
    }
}