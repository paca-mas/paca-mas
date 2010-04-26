package es.urjc.ia.web;

import java.sql.*;
import java.util.*;

import es.urjc.ia.baseDatos.Conexion_bbdd;

public class Sentencias {

	public static List<String> EjecutarSql (String sentencia, int num) {
		List<String> Lista = new ArrayList<String>();
		Connection conexion = null;
		try
		{
			conexion = es.urjc.ia.baseDatos.Conexion_bbdd.getConexion(conexion);			
			// Conexion con bd
			if (!conexion.isClosed()){				
				Statement st = conexion.createStatement();
				ResultSet rs = st.executeQuery(sentencia);     		
				int i = 1;
				while (rs.next()){
					for (int aux= 1; aux <= num; aux++){
							Lista.add(rs.getString(aux));
					}
					i ++;
				}
				rs.close();
				st.close();        	    
				// cierre de la conexion
				Conexion_bbdd.closeConexion(conexion);
			}
		}catch (Exception e){e.printStackTrace();}
		return Lista;
	}

	
}
