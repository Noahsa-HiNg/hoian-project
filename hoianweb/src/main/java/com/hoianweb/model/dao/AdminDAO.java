package com.hoianweb.model.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement; 
import org.mindrot.jbcrypt.BCrypt; 
import com.hoianweb.model.bean.Admin;
import com.hoianweb.util.DBContext;

public class AdminDAO {
    public Admin checkLogin(String username, String password) {
        String sql = "SELECT * FROM admin WHERE username = ?";
        try (Connection conn = DBContext.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)){
            pstmt.setString(1, username);
            try (ResultSet rs = pstmt.executeQuery()){
                if (rs.next()) {
                    String hashedPassword = rs.getString("password");
                    // Dùng BCrypt để kiểm tra mật khẩu
                    if (BCrypt.checkpw(password, hashedPassword)) {
                        return new Admin(rs.getInt("id"), username);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();    
        }
        return null;
    }
    //tạo tk admin
    public int create(Admin admin) {
        String sql = "INSERT INTO admin (username, password) VALUES (?, ?)"; 
        // Mã hóa mật khẩu trước khi lưu
        String hashedPassword = BCrypt.hashpw(admin.getPassword(), BCrypt.gensalt());
        try (Connection conn = DBContext.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {      
            pstmt.setString(1, admin.getUsername());
            pstmt.setString(2, hashedPassword);
            int affectedRows = pstmt.executeUpdate();
            if (affectedRows > 0) {
                try (ResultSet rs = pstmt.getGeneratedKeys()) {
                    if (rs.next()) {
                        return rs.getInt(1); 
                    }
                }
            }
            
        } catch (SQLException e) {
            // Lỗi SQL (ví dụ: 1062 - Duplicate entry)
            System.err.println("Lỗi AdminDAO - hàm create(): " + e.getMessage());
            e.printStackTrace();
        }
        return -1; 
    }
}