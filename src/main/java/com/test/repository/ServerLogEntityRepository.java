package com.test.repository;

import com.test.entity.ServerLogEntity;
import com.test.exception.DatabaseException;
import com.test.utils.JDBCUtils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ServerLogEntityRepository {

    private static final String INSERT_SQL = "INSERT INTO server_logs" +
            "  (id, duration, type, host, alert) VALUES " +
            " (?, ?, ?, ?, ?);";

    private static final String EXISTS_SQL = "SELECT COUNT(id) FROM server_logs WHERE id = ?";

    public void insert(ServerLogEntity serverLogEntity) {
        try (Connection connection = JDBCUtils.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(INSERT_SQL)) {
            preparedStatement.setString(1, serverLogEntity.getId());
            preparedStatement.setLong(2, serverLogEntity.getDuration());
            preparedStatement.setString(3, serverLogEntity.getType());
            preparedStatement.setString(4, serverLogEntity.getHost());
            preparedStatement.setBoolean(5, serverLogEntity.getAlert());

            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new DatabaseException("Error inserting server log entity", e);
        }
    }

    public boolean exists(String id) {
        try (Connection connection = JDBCUtils.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(EXISTS_SQL)) {
            preparedStatement.setString(1, id);

            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                Integer count = resultSet.getInt(1);
                return count > 0;
            } else {
                return false;
            }
        } catch (SQLException e) {
            throw new DatabaseException("Error inserting server log entity", e);
        }
    }

}