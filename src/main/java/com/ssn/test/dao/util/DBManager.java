package com.ssn.test.dao.util;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ssn.test.ConnectionPool;

public class DBManager {
	private static final Logger LOG = LoggerFactory.getLogger(DBManager.class);
	private static List<String> CREATE_TABLE_LST;
	private static boolean DB_TABLES_EXIST = false;
	
	static {
		CREATE_TABLE_LST = new ArrayList<String>();
		CREATE_TABLE_LST.add("create table scores ( id bigserial not null primary key, score_value numeric )");
	}
	
	public static void initializeDatabase() throws SQLException {
		DBManager.createTablesInDB();
	}
	
	protected static void createTablesInDB() throws SQLException {
		LOG.debug("Inside createTablesInDB method");
		if(DB_TABLES_EXIST) {
			return;
		}
		
		final String CORE_TABLE_NAME = "scores";
		
		try (Connection conn = ConnectionPool.getInstance().getConnection(); 
				Statement stmt = conn.createStatement();) {
			if(!doesTableExistInDB(conn, CORE_TABLE_NAME)) {
				LOG.info("Creating tables in database ...");
				
				for(String query : CREATE_TABLE_LST) {
					LOG.trace("Executing query: " + query);
					boolean status = stmt.execute(query);
					LOG.trace("Query execution completed with status: " + status);
				}
				
				LOG.info("Tables creation successful");
			} else {
				LOG.info("Tables already exist in database. Not performing any action.");
			}
			
			DB_TABLES_EXIST = true;
		}
		LOG.debug("Exiting createTablesInDB method");
	}
	
	public static boolean doesTableExistInDB(Connection conn, String tableName) throws SQLException {
		LOG.debug("Inside doesTableExistInDB with params: tableName = " + tableName);
		if(conn == null || tableName == null || "".equals(tableName.trim())) {
			LOG.warn("Invalid input parameters. Returning doesTableExistInDB() method with FALSE.");
			return false;
		}
		
		boolean doExist = false;
		
		final String SELECT_QUERY = "SELECT EXISTS( SELECT 1 FROM information_schema.tables "
				+ " WHERE table_schema = current_schema() AND table_name = ? )";
		
		ResultSet rs = null;
		try (PreparedStatement selectStmt = conn.prepareStatement(SELECT_QUERY)) {
			selectStmt.setString(1, tableName);
			rs = selectStmt.executeQuery();
			if(rs.next()) {
				doExist = rs.getBoolean(1);
			}
		} finally {
			if(rs !=null) {
				rs.close();
			}
		}
		
		LOG.debug("Exiting doesTableExistInDB method with value = " + doExist);
		
		return doExist;
	}
}
