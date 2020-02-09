package org.duh102.duhbot.shout.db.dao;

import org.duh102.duhbot.shout.db.DBConfig;
import org.duh102.duhbot.shout.db.dto.Shout;

import java.sql.*;

public class ShoutDAO {
    private DBConfig databaseConfig;

    public ShoutDAO(DBConfig databaseConfig) {
        this.databaseConfig = databaseConfig;
    }

    public boolean save(Shout shout) {
        try {
            String insertSQL = "insert into shout (shout, channel, nick, is_action, recorded) values (?, ?, ?, ?, ?)";
            Connection connection = databaseConfig.getConnection();
            PreparedStatement statement = connection.prepareStatement(insertSQL);
            statement.setString(1, shout.getShout());
            statement.setString(2, shout.getChannel());
            statement.setString(3, shout.getUser());
            statement.setBoolean(4, shout.isAction());
            statement.setTimestamp(5, new Timestamp(shout.getTimestamp()));
            int affectedRows = statement.executeUpdate();

            if (affectedRows == 0) {
                throw new SQLException("Creating shout failed, no rows affected.");
            }

            try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    shout.setId(generatedKeys.getLong(1));
                }
                else {
                    throw new SQLException("Creating shout failed, no ID obtained.");
                }
            }
            return true;
        } catch(SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    // specific one
    public Shout get(Long id) {
        String selectSql = "select id, shout, channel, nick, is_action, recorded from shout where id = ? order by rand() limit 1";
        try {
            Connection connection = databaseConfig.getConnection();
            PreparedStatement statement = connection.prepareStatement(selectSql);
            statement.setLong(1, id);
            ResultSet rs = statement.executeQuery();
            return getOneFromResultSet(rs, true);
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }
    // random
    public Shout get(String channel, Boolean isAction, Long avoidId) {
        String selectSql = "select id, shout, channel, nick, is_action, recorded from shout where channel = ? and is_action = ? and id != ? order by RANDOM() limit 1";
        try {
            Connection connection = databaseConfig.getConnection();
            PreparedStatement statement = connection.prepareStatement(selectSql);
            statement.setString(1, channel);
            statement.setBoolean(2, isAction);
            statement.setLong(3, avoidId);
            ResultSet rs = statement.executeQuery();
            return getOneFromResultSet(rs, true);
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    private Shout getOneFromResultSet(ResultSet rs, boolean autoclose) throws SQLException {
        try {
            if(!rs.next()) {
                throw new SQLException("ResultSet did not contain any rows");
            }
            Long id = rs.getLong(1);
            String shoutText = rs.getString(2);
            String channel = rs.getString(3);
            String nick = rs.getString(4);
            Boolean isAction = rs.getBoolean(5);
            Long recorded = rs.getLong(6);
            return new Shout(id, shoutText, channel, nick, isAction, recorded);
        } finally {
            if(autoclose) {
                rs.close();
            }
        }
    }
}
