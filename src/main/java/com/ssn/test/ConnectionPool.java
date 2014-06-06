package com.ssn.test;

import java.sql.Connection;
import java.sql.SQLException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ssn.test.dao.util.DBManager;
import com.zaxxer.hikari.HikariDataSource;

public class ConnectionPool {
	private static final Logger LOG = LoggerFactory.getLogger(ConnectionPool.class);
	private static ConnectionPool instance = null;
	private HikariDataSource ds = null;

	static {
		try {
			LOG.info("Initializing the connection pool ... ");
			instance = new ConnectionPool();
			LOG.info("Connection pool initialized successfully.");
		} catch (Exception e) {
			LOG.error("Exception when trying to initialize the connection pool",e);
		}
		
		// For now we can initialize database from here. But technically, it should be 
		// done from a better place - preferably the application load servlet etc.
		try {
			DBManager.initializeDatabase();
		} catch (SQLException e) {
			LOG.error("Error when trying to create tables in the database ::", e);
		}
	}

	private ConnectionPool() {
		ds = new HikariDataSource();
		ds.setMaximumPoolSize(100);
		ds.setDataSourceClassName("org.postgresql.ds.PGPoolingDataSource");
		ds.addDataSourceProperty("serverName", "localhost");
		ds.addDataSourceProperty("databaseName", "ssn_db");
		ds.addDataSourceProperty("user", "surya");
		ds.addDataSourceProperty("password", "kiran");
	}

	public static ConnectionPool getInstance() {
		return instance;
	}

	public Connection getConnection() throws SQLException {
		return ds.getConnection();
	}
}
