package es.urjc.ia.baseDatos;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Conexion_bbdd {
	public static void closeConexion (Connection c) throws SQLException {
		c.close();		
	}
	
	public static Connection getConexion(Connection conn) {
		
		try {
			Class.forName("com.mysql.jdbc.Driver");
		} catch (ClassNotFoundException e) {
			System.out.println("MySQL JDBC Driver not found. ");
		    System.exit(1);
		}
		
		// Se conecta con la Base de Datos
		try {
			conn = DriverManager.getConnection("jdbc:mysql://localhost/Prueba","root", "cobi");
		} catch (SQLException e) {
			System.out.println("Error de conexión: " + e.getMessage());
		    System.exit(4);			
		}
		return conn;
	}

}
