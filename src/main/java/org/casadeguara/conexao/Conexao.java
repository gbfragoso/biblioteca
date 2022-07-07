package org.casadeguara.conexao;

import java.beans.PropertyVetoException;
import java.sql.Connection;
import java.sql.SQLException;

import com.mchange.v2.c3p0.ComboPooledDataSource;

/**
 * Esta classe se responsabiliza pelo gerenciamento do pool de conexões com o
 * jdbc.
 * 
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

			ds.setMinPoolSize(3);
			ds.setMaxPoolSize(10);
			ds.setAcquireIncrement(1);
			ds.setMaxStatementsPerConnection(3);
			ds.setIdleConnectionTestPeriod(300);
			ds.setPreferredTestQuery("SELECT 1");
			ds.setTestConnectionOnCheckin(true);
			ds.setMaxIdleTimeExcessConnections(240);
		} catch (PropertyVetoException e) {
			e.printStackTrace();
		}
	}

	private Conexao() {
	}

	public static Connection abrir() throws SQLException {
		return ds.getConnection();
	}
}
