package es.urjc.ia.web;

import java.util.*;

public class ObtenerDatos {
	
	public static List<String> listUser (String auxiliar){
		int inicio = 0;
		List<String> ListId = new ArrayList<String>();
		while ( auxiliar.length() != 0 ){
			int fin = auxiliar.indexOf(",");
			if (fin != -1){
				ListId.add(auxiliar.substring(inicio,fin));
				auxiliar = auxiliar.substring(fin+1,auxiliar.length());	
			} else {
				ListId.add(auxiliar);
				auxiliar = "";
			}
		}
		return ListId;
	}
	
	public static List<String> Login (String id) {
		String sentencia = "select id_usuario, pass from usuarios where id_usuario = " + id;
		List<String> ListId = Sentencias.EjecutarSql(sentencia, 2);
		return ListId;
	}
	
	public static List<String> ListaAlumnos (int grupo) {
		String sentencia = "select id_usuario, nombre, apellidos from usuarios where id_grupo =" + grupo + " and tipo = 'A'";
		List<String> ListaUser = Sentencias.EjecutarSql(sentencia,3);
		return ListaUser;
	}
	
	public static String DatosAlumnos (String id){
		String nombre_alumno = "";
		String sentencia = "select nombre, apellidos from usuarios where id_usuario=" + id;
		List<String> ListaAlumno = new ArrayList<String>();
		ListaAlumno = Sentencias.EjecutarSql(sentencia,2);					
		for (String alumno:ListaAlumno){
			nombre_alumno += " " + alumno;
		}
		return nombre_alumno;
	}
	
	public static String TipoUsuario (String id){
		String tipo = "";
		String sentencia = "select tipo from usuarios where id_usuario =" + id;
		List<String> TipoUsuario = new ArrayList<String>();
		TipoUsuario = Sentencias.EjecutarSql(sentencia,1);					
		for (String t:TipoUsuario){
			tipo = t;
		}
		return tipo;
	}
	
	public static int DatosGrupo (String id){
		String sentencia = "select id_grupo from usuarios where id_usuario=" + id;	    
		List<String> ListaGrupo = Sentencias.EjecutarSql(sentencia,1);										
		int numgrupo = 0;
		for (String grupo: ListaGrupo){
			numgrupo = Integer.parseInt(grupo);
		}
		return numgrupo;
	}

	public static List<String> ListaPracticas (){
		String sentencia = "select id_practica from x_practica";	    
		List<String> ListaPractica = new ArrayList<String>();
		ListaPractica = Sentencias.EjecutarSql(sentencia,1);
		return ListaPractica;
	}

	public static List<String> ListaTest (int id){

		String sentencia = "select id_test from x_test where id_practica=" + id;
		List<String> ListaTest = Sentencias.EjecutarSql(sentencia,1);					
		return ListaTest;
	}

	public static List<String> ListaCaso (int id){
		String sentencia = "select id_caso from x_caso where id_practica="+id;
		List<String> ListaCaso = Sentencias.EjecutarSql(sentencia,1);					
		return ListaCaso;
	}
	
	public static int NumeroCasos (int id1, int id2){
		String sentencia = "select count(*) from x_caso where id_practica =" + id1 + " and id_test =" + id2;
	 	List<String> num_casos = Sentencias.EjecutarSql(sentencia,1);
	 	int n = 0;
	 	for (String num:num_casos) {
	 		n = Integer.parseInt(num);
	 	}
	 	return n;
	}
	
	public static List<String> LanzarAlumnoCaso_S1 (String id1, int id2, int id3){
		String sentencia1 = "SELECT  xec.evaluacion AS xec_evaluacion, gec.porcentaje_caso AS gec_porcentaje_caso ";
		String sentencia2 = "FROM x_evaluacion_caso xec INNER JOIN alumno_evaluacion_test_01 aet ON (xec.fecha = aet.fecha AND xec.id_test = aet.id_test AND xec.id_alumno = aet.id_alumno AND xec.id_practica = aet.id_practica) INNER JOIN grupo_evaluacion_caso gec ON (xec.id_grupo = gec.id_grupo AND gec.id_caso = xec.id_caso AND gec.id_practica = xec.id_practica AND gec.id_test = xec.id_test AND aet.id_grupo = gec.id_grupo) ";
		String sentencia3 = "WHERE xec.id_alumno =" + id1 + " AND xec.id_practica =" + id2 + " AND gec.id_test =" + id3;
		List<String> resultado = Sentencias.EjecutarSql(sentencia1 + sentencia2 + sentencia3 , 2);
		return resultado;
	}
}
