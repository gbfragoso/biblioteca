package org.casadeguara.conexao;

import java.sql.Connection;
import java.sql.SQLException;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

/**
 * Esta classe se responsabiliza pelo gerenciamento do pool de conexões com o jdbc.
 * @author Gustavo
 */
public class Conexao {
	
	private static final String CONNECTION_URL = "jdbc:postgresql://localhost/biblioteca";
	private static final String PASSWORD = "j3RdOTZ9";
	private static final String USERNAME = "postgres";
	private static HikariConfig config = new HikariConfig();
	private static HikariDataSource ds;
	
	static {
		config.setJdbcUrl(CONNECTION_URL);
		config.setUsername(USERNAME);
		config.setPassword(PASSWORD);
		config.addDataSourceProperty("cachePrepStmts", "true");
		config.addDataSourceProperty("prepStmtCacheSize", "250");
		config.addDataSourceProperty("prepStmtCacheSqlLimit", "2048");
		config.setMaximumPoolSize(5);
		ds = new HikariDataSource(config);
	}
	
	private Conexao() {}
	
	public static Connection abrir() throws SQLException{
		return ds.getConnection();
	}
}
