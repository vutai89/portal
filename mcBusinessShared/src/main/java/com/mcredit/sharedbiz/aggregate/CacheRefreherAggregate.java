package com.mcredit.sharedbiz.aggregate;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

import com.mcredit.model.enums.CacheTableName;
import com.mcredit.model.enums.ParametersName;
import com.mcredit.sharedbiz.cache.CacheManager;
import com.mcredit.util.StringUtils;

import oracle.jdbc.OracleConnection;
import oracle.jdbc.OracleDriver;
import oracle.jdbc.OracleStatement;
import oracle.jdbc.dcn.DatabaseChangeEvent;
import oracle.jdbc.dcn.DatabaseChangeListener;
import oracle.jdbc.dcn.DatabaseChangeRegistration;

public class CacheRefreherAggregate {
	private String USERNAME = "";
	private String PASSWORD = "";
	private String URL = "";
	private DatabaseChangeRegistration dcr = null;
	private OracleConnection conn = null;
	private CacheTableName[] cacheTableName;
	
	public CacheRefreherAggregate(CacheTableName[] tables) throws Throwable{
		init();
		
		this.cacheTableName = tables;
	}
	
	public void listen() throws Throwable {
		
		try {
			/**
			 * DCN listener: it prints out the event details in stdout.
			 */
			DatabaseChangeListener list = new DatabaseChangeListener() {	
				@Override
				public void onDatabaseChangeNotification(DatabaseChangeEvent e) {
					System.out.println("DCNDemoListener: GOT AN EVENT: ");
					
					for (int i = 0; i < e.getQueryChangeDescription().length; i++) {
						for (int j = 0; j < e.getQueryChangeDescription()[i].getTableChangeDescription().length; j++) {
							for (int k = 0; k < e.getQueryChangeDescription()[i].getTableChangeDescription()[j].getRowChangeDescription().length; k++) {
								String rowId = e.getQueryChangeDescription()[i].getTableChangeDescription()[j].getRowChangeDescription()[k].getRowid().toString();       
			                	String table = e.getQueryChangeDescription()[i].getTableChangeDescription()[j].getTableName().toString();
								System.out.println("Changed row id : " + rowId);
			                	System.out.println("Table name : "+ table);
							}
						}
					}
					
                	
                	
				}
			};
			dcr.addListener(list);

			// second step: add objects in the registration:
			Statement stmt = conn.createStatement();
			// associate the statement with the registration:
			((OracleStatement) stmt).setDatabaseChangeRegistration(dcr);
			
			ResultSet rs = null;
			for (CacheTableName table : cacheTableName) {
				rs = stmt.executeQuery("select * from " + table.value());
				while (rs.next()) {}
			}
			if(rs != null)
				rs.close();
			
			
			String[] tableNames = dcr.getTables();
			for (int i = 0; i < tableNames.length; i++)
				System.out.println(tableNames[i] + " IS PART OF THE REGISTRATION.");
			
			
			stmt.close();
			
		} catch (Throwable ex) {
			// if an exception occurs, we need to close the registration in
			// order
			// to interrupt the thread otherwise it will be hanging around.
			if (conn != null)
				conn.unregisterDatabaseChangeNotification(dcr);
			
			throw ex;
		} finally {
			try {
				// Note that we close the connection!
				conn.close();
			} catch (Exception innerex) {
				innerex.printStackTrace();
			}
		}
	}

	private String consolidateTable(String input) {

		if (StringUtils.isNullOrEmpty(input))
			return null;

		if (input.indexOf(".") != -1)
			return input.substring(input.indexOf(".") + 1, input.length());

		return null;
	}
	
	/**
	 * Creates a connection the database.
	 */
	private OracleConnection connect() throws SQLException {
		OracleDriver dr = new OracleDriver();
		Properties prop = new Properties();
		prop.setProperty("user", this.USERNAME);
		prop.setProperty("password", this.PASSWORD);
		return (OracleConnection) dr.connect(this.URL, prop);
	}
	
	private void init() throws SQLException{
		
		this.USERNAME = CacheManager.Parameters().findParamValueAsString(ParametersName.DB_CHANGES_CONNECTION_USER);
		this.PASSWORD = CacheManager.Parameters().findParamValueAsString(ParametersName.DB_CHANGES_CONNECTION_PASSWORD);
		this.URL = CacheManager.Parameters().findParamValueAsString(ParametersName.DB_CHANGES_CONNECTION_URL);
		
		conn = connect();
		Properties prop = new Properties();
		prop.setProperty(OracleConnection.DCN_NOTIFY_ROWIDS, "true");
		prop.setProperty(OracleConnection.DCN_QUERY_CHANGE_NOTIFICATION, "true");
		dcr = conn.registerDatabaseChangeNotification(prop);
	}
	
	public static void main(String[] argv) throws Throwable {
		CacheRefreherAggregate demo = new CacheRefreherAggregate(new CacheTableName[] {CacheTableName.CACHE_CHANGE});
		try {
			demo.listen();
		} catch (SQLException mainSQLException) {
			mainSQLException.printStackTrace();
		}
	}
}