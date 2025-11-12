package com.hoianweb.model.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import org.mindrot.jbcrypt.BCrypt;
import com.hoianweb.model.bean.User;
import com.hoianweb.util.DBContext;

public class UserDAO {
    public boolean create(User user) {
        String sql = "INSERT INTO user (username, password, email) VALUES (?, ?, ?)";
        // Mã hóa mật khẩu trước khi lưu
        String hashedPassword = BCrypt.hashpw(user.getPassword(), BCrypt.gensalt());

        try (Connection conn = DBContext.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, user.getUsername());
            pstmt.setString(2, hashedPassword);
            pstmt.setString(3, user.getEmail());
            return pstmt.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace(); 
            return false;
        }
    }
     // Kiểm tra đăng nhập người dùng
    public User checkLogin(String username, String password) {
        String sql = "SELECT * FROM user WHERE username = ?";
        try (Connection conn = DBContext.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, username);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    String hashedPassword = rs.getString("password");
                    if (BCrypt.checkpw(password, hashedPassword)) {
                        return new User(
                            rs.getInt("id"), 
                            rs.getString("username"), 
                            rs.getString("email")
                        );
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();    
        }
        return null;
    }
}