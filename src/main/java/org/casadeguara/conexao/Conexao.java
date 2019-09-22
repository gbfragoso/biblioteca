package org.casadeguara.conexao;

import java.beans.PropertyVetoException;
import java.sql.Connection;
import java.sql.SQLException;

import com.mchange.v2.c3p0.ComboPooledDataSource;

/**
 * Esta classe se responsabiliza pelo gerenciamento do pool de conexões com o jdbc.
 * @author Gustavo
 */
public class Conexao {
	
	private static final String CONNECTION_URL = "jdbc:postgresql://localhost/biblioteca";
	private static final String DRIVER_NAME = "org.postgresql.Driver";
	private static final String PASSWORD = "j3RdOTZ9";
	private static final String USERNAME = "postgres";
	private static ComboPooledDataSource ds = new ComboPooledDataSource();
	
	static {
		try {
			ds.setDriverClass(DRIVER_NAME);
			ds.setJdbcUrl(CONNECTION_URL);
			ds.setUser(USERNAME);
			ds.setPassword(PASSWORD);
			
			ds.setInitialPoolSize(5);
			ds.setMinPoolSize(5);                                     
			ds.setAcquireIncrement(5);
			ds.setMaxPoolSize(15);
			ds.setMaxStatementsPerConnection(3);
			ds.setIdleConnectionTestPeriod(3000);
		} catch (PropertyVetoException e) {
			e.printStackTrace();
		}
	}
	
	private Conexao() {}
	
	public static Connection abrir() throws SQLException{
		return ds.getConnection();
	}
}
