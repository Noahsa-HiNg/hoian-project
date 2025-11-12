package com.hoianweb.model.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import com.hoianweb.model.bean.Category;
import com.hoianweb.util.DBContext;

public class CategoryDAO {

    public List<Category> getAll() {
        List<Category> list = new ArrayList<>();
        String sql = "SELECT * FROM category ORDER BY name"; 

        try (Connection conn = DBContext.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                list.add(new Category(rs.getInt("id"), rs.getString("name")));
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return list;
    }
    
    public Category getById(int id) {
        String sql = "SELECT * FROM category WHERE id = ?";
        try (Connection conn = DBContext.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return new Category(rs.getInt("id"), rs.getString("name"));
                }
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return null;
    }
    //create
    public int create(Category category) {
        String sql = "INSERT INTO category (name) VALUES (?)";
        try (Connection conn = DBContext.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            pstmt.setString(1, category.getName());
            
            int affectedRows = pstmt.executeUpdate();
            if (affectedRows > 0) {
                try (ResultSet rs = pstmt.getGeneratedKeys()) {
                    if (rs.next()) {
                        return rs.getInt(1); 
                    }
                }
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return -1; 
    }

    //update
    public boolean update(int id, Category category) {
        String sql = "UPDATE category SET name = ? WHERE id = ?";
        try (Connection conn = DBContext.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, category.getName());
            pstmt.setInt(2, id);
            
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) { e.printStackTrace(); }
        return false;
    }
    
    //delete
    public boolean delete(int id) {
        String sql = "DELETE FROM category WHERE id = ?";
        try (Connection conn = DBContext.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, id);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) { e.printStackTrace(); }
        return false;
    }
}