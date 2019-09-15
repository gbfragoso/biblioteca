package org.casadeguara.conexao;

import java.beans.PropertyVetoException;
import java.sql.Connection;
import java.sql.SQLException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import com.mchange.v2.c3p0.ComboPooledDataSource;

/**
 * Esta classe se responsabiliza pelo gerenciamento do pool de conexões com o jdbc.
 * @author Gustavo
 */
public class Conexao {
	
    private static final Logger logger = LogManager.getLogger(Conexao.class);
	private static final String CONNECTION_URL = "jdbc:postgresql://localhost/biblioteca";
	private static final String DRIVER_NAME = "org.postgresql.Driver";
	private static final String PASSWORD = "j3RdOTZ9";
	private static final String USERNAME = "postgres";
	private static ComboPooledDataSource cpds = new ComboPooledDataSource();
	
	public Conexao() {
	    try {
			cpds.setDriverClass(DRIVER_NAME);
			cpds.setJdbcUrl(CONNECTION_URL);
			cpds.setUser(USERNAME);
			cpds.setPassword(PASSWORD);
			
			cpds.setInitialPoolSize(5);
			cpds.setMinPoolSize(5);                                     
			cpds.setAcquireIncrement(5);
			cpds.setMaxPoolSize(15);
			cpds.setMaxStatementsPerConnection(3);
			cpds.setIdleConnectionTestPeriod(3000);
		} catch (PropertyVetoException e) {
			logger.fatal("Ocorreu um erro durante a criação do pool de conexões.", e);
		}
	}
	
	public static Connection abrir() {
        try{        	          
            return cpds.getConnection();
        } catch (SQLException ex) {
        	logger.fatal("A conexão não pôde ser estabelecida.", ex);
        }
        return null;
	}
	
	public void encerrar() {
		cpds.close();
	}
	
}
