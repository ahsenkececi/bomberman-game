package com.bomberman.repository;

import com.bomberman.database.DatabaseManager;
import com.bomberman.models.User;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class UserRepository implements IRepository<User> {

    private Connection connection;

    // Constructor
    public UserRepository() {
        this.connection = DatabaseManager.getInstance().getConnection();
    }

    @Override
    public List<User> getAll() {
        List<User> users = new ArrayList<>();
        String query = "SELECT * FROM users";

        try {
            Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery(query);

            while (rs.next()) {
                User user = new User();
                user.setId(rs.getInt("id"));
                user.setUsername(rs.getString("username"));
                user.setPasswordHash(rs.getString("password_hash"));
                user.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());
                users.add(user);
            }

            rs.close();
            stmt.close();

        } catch (SQLException e) {
            System.err.println("❌ Error getting all users!");
            e.printStackTrace();
        }

        return users;
    }

    @Override
    public User getById(int id) {
        String query = "SELECT * FROM users WHERE id = ?";
        User user = null;

        try {
            PreparedStatement pstmt = connection.prepareStatement(query);
            pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                user = new User();
                user.setId(rs.getInt("id"));
                user.setUsername(rs.getString("username"));
                user.setPasswordHash(rs.getString("password_hash"));
                user.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());
            }

            rs.close();
            pstmt.close();

        } catch (SQLException e) {
            System.err.println("❌ Error getting user by ID!");
            e.printStackTrace();
        }

        return user;
    }

    @Override
    public void add(User user) {
        String query = "INSERT INTO users (username, password_hash) VALUES (?, ?)";

        try {
            PreparedStatement pstmt = connection.prepareStatement(query);
            pstmt.setString(1, user.getUsername());
            pstmt.setString(2, user.getPasswordHash());

            int rowsAffected = pstmt.executeUpdate();

            if (rowsAffected > 0) {
                System.out.println("✅ User added: " + user.getUsername());
            }

            pstmt.close();

        } catch (SQLException e) {
            System.err.println("❌ Error adding user!");
            e.printStackTrace();
        }
    }

    @Override
    public void update(User user) {
        String query = "UPDATE users SET username = ?, password_hash = ? WHERE id = ?";

        try {
            PreparedStatement pstmt = connection.prepareStatement(query);
            pstmt.setString(1, user.getUsername());
            pstmt.setString(2, user.getPasswordHash());
            pstmt.setInt(3, user.getId());

            int rowsAffected = pstmt.executeUpdate();

            if (rowsAffected > 0) {
                System.out.println("✅ User updated: " + user.getUsername());
            }

            pstmt.close();

        } catch (SQLException e) {
            System.err.println("❌ Error updating user!");
            e.printStackTrace();
        }
    }

    @Override
    public void delete(int id) {
        String query = "DELETE FROM users WHERE id = ?";

        try {
            PreparedStatement pstmt = connection.prepareStatement(query);
            pstmt.setInt(1, id);

            int rowsAffected = pstmt.executeUpdate();

            if (rowsAffected > 0) {
                System.out.println("✅ User deleted with ID: " + id);
            }

            pstmt.close();

        } catch (SQLException e) {
            System.err.println("❌ Error deleting user!");
            e.printStackTrace();
        }
    }

    // Özel metot: Username'e göre kullanıcı getir (Login için)
    public User getByUsername(String username) {
        String query = "SELECT * FROM users WHERE username = ?";
        User user = null;

        try {
            PreparedStatement pstmt = connection.prepareStatement(query);
            pstmt.setString(1, username);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                user = new User();
                user.setId(rs.getInt("id"));
                user.setUsername(rs.getString("username"));
                user.setPasswordHash(rs.getString("password_hash"));
                user.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());
            }

            rs.close();
            pstmt.close();

        } catch (SQLException e) {
            System.err.println("❌ Error getting user by username!");
            e.printStackTrace();
        }

        return user;
    }
}