package org.duh102.duhbot.shout;

import java.sql.*;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.text.ParseException;
import org.sqlite.SQLiteConfig;

import org.duh102.duhbot.moolah.exceptions.*;

public class ShoutDB {
  private static final String DEFAULT_DB = "shout.db";
  private static ConcurrentHashMap<String, ShoutDB> instanceMap;
  private String myDBFile;
  private Connection connection = null;
  static {
    instanceMap = new ConcurrentHashMap<String, ShoutDB>();
  }

  private ShoutDB(String dbFile) throws InvalidDBConfiguration, InvalidEnvironment {
    myDBFile = dbFile;
    createTables();
  }

  private static ShoutDB getInstance(String dbFile) throws InvalidDBConfiguration, InvalidEnvironment {
    ShoutDB instance = null;
    instance = instanceMap.get(dbFile);
    if( instance == null ) {
      instance = new ShoutDB(dbFile);
      instanceMap.put(dbFile, instance);
    }
    return instance;
  }

  public static ShoutDB getDBInstance() throws InvalidDBConfiguration, InvalidEnvironment {
    return getInstance(DEFAULT_DB);
  }
  public static ShoutDB getMemoryInstance() throws InvalidDBConfiguration, InvalidEnvironment {
    return getInstance(":memory:");
  }

  private void createTables() throws InvalidDBConfiguration, InvalidEnvironment {
    Connection conn = null;
    try {
      conn = makeDBConnection();
    } catch( DBAlreadyConnected dac ) {
      conn = connection;
    }
    try {
      Statement stat = conn.createStatement();
      stat.executeUpdate("CREATE TABLE IF NOT EXISTS shout ( id INTEGER PRIMARY KEY, shout TEXT, username TEXT, when INTEGER );");
    } catch(SQLException sqle) {
      throw new InvalidDBConfiguration(sqle);
    }
  }

  public synchronized Connection makeDBConnection() throws InvalidEnvironment, InvalidDBConfiguration, DBAlreadyConnected {
    if( connection != null )
      throw new DBAlreadyConnected();
    try {
      Class.forName("org.sqlite.JDBC");
    } catch( java.lang.ClassNotFoundException cnfe ) {
      throw new InvalidEnvironment(cnfe);
    }
    try {
      SQLiteConfig config = new SQLiteConfig();
      config.enforceForeignKeys(true);
      connection = DriverManager.getConnection("jdbc:sqlite:" + myDBFile, config.toProperties());
      return connection;
    } catch(SQLException sqle) {
      throw new InvalidDBConfiguration(sqle);
    }
  }

  public synchronized void destroyDBConnection() throws SQLException, DBNotConnected {
    if( connection == null )
      throw new DBNotConnected();
    connection.close();
    connection = null;
  }

  public synchronized Connection getDBConnection() throws RecordFailure {
    if( connection != null )
      return connection;
    try {
      return makeDBConnection();
    } catch( InvalidEnvironment ie ) {
      throw new RecordFailure(ie);
    } catch( InvalidDBConfiguration idc ) {
      throw new RecordFailure(idc);
    } catch( DBAlreadyConnected dac ) {
      // this should not happen
      return connection;
    }
  }

  public synchronized String getShout() throws RecordFailure {
    Connection conn = getDBConnection();

    try {
      String shout = null;
      PreparedStatement stat = conn.prepareStatement(
        "SELECT ( id, shout, username, when ) FROM shout ORDER BY RANDOM() LIMIT 1;"
      );
      ResultSet rs = stat.executeQuery();
      while (rs.next()) {
        shout = rs.getString("shout");
      }
      rs.close();
      return shout;
    } catch( SQLException sqle ) {
      throw new RecordFailure(sqle);
    }
  }
  public synchronized addShout(String shout, String username, long when) throws RecordFailure {
    Connection conn = getDBConnection();

    try {
      PreparedStatement stat = conn.prepareStatement(
        "INSERT INTO shout (shout, username, when) VALUES (?, ?, ?);",
        Statement.RETURN_GENERATED_KEYS
      );
      stat.setString(1, shout);
      stat.setString(2, username);
      stat.setLong(3, when);
      stat.executeUpdate();
      ResultSet rs = stat.getGeneratedKeys();
      if (! rs.next()) {
        throw new RecordFailure("No rows inserted");
      }
      rs.close();
      return;
    } catch( SQLException sqle ) {
      throw new RecordFailure(sqle);
    }
  }
}
