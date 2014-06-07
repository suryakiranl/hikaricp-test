package com.ssn.test.dao.util;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ssn.test.ConnectionPool;

public class ScoresDAOImpl {
	private static final Logger LOG = LoggerFactory.getLogger(ScoresDAOImpl.class);
	public static boolean save(float scoreValue) throws SQLException {
		LOG.debug("Inside save method; scoreValue = " + scoreValue); 
		boolean savedSuccessfully = false;
		final String INSERT_QUERY = "insert into scores(score_value) values (?)";
		
		try(Connection conn = ConnectionPool.getInstance().getConnection();
				PreparedStatement stmt = conn.prepareStatement(INSERT_QUERY);) {
			stmt.setFloat(1, scoreValue);
			
			int rows = stmt.executeUpdate();
			if(rows == 1) {
				savedSuccessfully = true;
			}
		}
		
		LOG.debug("Exiting save method with value: " + savedSuccessfully);
		
		return savedSuccessfully;
	}
	
	public static int countRows() throws SQLException {
		LOG.debug("Inside loadAll method.");
		int rowCount = 0;
		final String SELECT_QUERY = "select score_value from scores";
			
		try(Connection conn = ConnectionPool.getInstance().getConnection(); 
				PreparedStatement stmt = conn.prepareStatement(SELECT_QUERY);
				ResultSet rs = stmt.executeQuery();) {
			float score_value;
			while(rs.next()) {
				score_value = rs.getFloat("score_value");
				score_value = score_value + 1;
				rowCount++;
			}
		}
		
		LOG.debug("Exiting loadAll method with " + rowCount + " rows.");
		
		return rowCount;
	}
}
