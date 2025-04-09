package com.function.infraestructure.db;

import com.function.domain.model.User;
import com.function.infraestructure.repository.UserRepository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class UserRepositoryImpl implements UserRepository {

    @Override
    public boolean create(User user) {
        String sql = "INSERT INTO usuarios (nombre, email) VALUES (?, ?)";
        try (Connection conn = OracleDBConnection.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, user.getNombre());
            ps.setString(2, user.getEmail());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            return false;
        }
    }

    @Override
    public List<User> findAll() {
        List<User> users = new ArrayList<>();
        String sql = "SELECT * FROM usuarios";

        try (Connection conn = OracleDBConnection.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql);
                ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                users.add(new User(
                        rs.getInt("id"),
                        rs.getString("nombre"),
                        rs.getString("email")));
            }

        } catch (SQLException e) {
            e.printStackTrace(); // ⚠️ Asegúrate que esté esto para ver el error
            return users;
        }

        return users;
    }

    @Override
    public Optional<User> findById(int id) {
        String sql = "SELECT * FROM usuarios WHERE id = ?";
        try (Connection conn = OracleDBConnection.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(new User(rs.getInt("id"), rs.getString("nombre"), rs.getString("email")));
                }
            }
        } catch (SQLException e) {
            return Optional.empty();
        }
        return Optional.empty();
    }

    @Override
    public boolean update(User user) {
        String sql = "UPDATE usuarios SET nombre = ?, email = ? WHERE id = ?";
        try (Connection conn = OracleDBConnection.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, user.getNombre());
            ps.setString(2, user.getEmail());
            ps.setInt(3, user.getId());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            return false;
        }
    }

    @Override
    public boolean delete(int id) {
        String sql = "DELETE FROM usuarios WHERE id = ?";
        try (Connection conn = OracleDBConnection.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            return false;
        }
    }
}