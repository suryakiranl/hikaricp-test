package com.ssn.test.client;

import java.sql.Connection;
import java.sql.SQLException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ssn.test.ConnectionPool;
import com.ssn.test.dao.util.DBManager;

public class SampleClient {
	private static final Logger LOG = LoggerFactory.getLogger(SampleClient.class);
	public static void main(String[] args) {
		try(Connection conn = ConnectionPool.getInstance().getConnection()) {
			LOG.debug("Table scores exists in db? = " + DBManager.doesTableExistInDB(conn, "scores"));			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
