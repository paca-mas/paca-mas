package es.urjc.ia.informes;

import java.sql.Connection;
import java.io.*;
import java.util.HashMap;

import es.urjc.ia.web.Configuracion;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;

public class LanzarInforme {
	
	public static void CrearDir(String alumno, String practica){
		String rutaAlumno = es.urjc.ia.web.Configuracion.DIRDATA + alumno + "/";
		String rutaPractica = rutaAlumno + practica + "/";
		File f_rutaAlumno   = new File (rutaAlumno);
		File f_rutaPractica   = new File (rutaPractica);
		if (!f_rutaAlumno.exists()) {
			f_rutaAlumno.mkdir();
		}
		if (!f_rutaPractica.exists()){
			f_rutaPractica.mkdir();
		}
	}
	
	public static void LanzarInforme( HashMap parametros, String rutaHtml, String rutaJrxml){

		Connection conn = null;
		try {
				conn = es.urjc.ia.baseDatos.Conexion_bbdd.getConexion(conn);		
				JasperReport report = JasperCompileManager.compileReport(rutaJrxml);	
		   		JasperPrint jasperPrint = JasperFillManager.fillReport(report, parametros, conn);
		   		JasperExportManager.exportReportToHtmlFile(jasperPrint, rutaHtml);
		   		es.urjc.ia.baseDatos.Conexion_bbdd.closeConexion(conn);
		}catch (Exception e){ e.printStackTrace();}
	}

//***************************************************************************************	
// INFORMES ALUMNO
//***************************************************************************************	
	// ************************************************************
	// Parametros: user (usuario)  p1 (practica) p2 (idalumnos)
	// ************************************************************
	public static void InformePracticaAlumno (String user,Object p1, Object p2){
		CrearDir(user,p1.toString()); // alumno + practica
		// Parametros para el informe
		HashMap parametros = new HashMap();
		parametros.put("IDPRACTICA",p1);
		parametros.put("IDALUMNO",p2);
		String rutaHtml =  es.urjc.ia.web.Configuracion.DIRDATA + user + "/" + p1.toString() + "/" + es.urjc.ia.web.Configuracion.N_Informe_Usuario_Practica;
	 	String rutaJrxml = es.urjc.ia.web.Configuracion.DIRINFORMEALUMNO + es.urjc.ia.web.Configuracion.N_Informe_Usuario_Practica_JRXML;
		LanzarInforme (parametros,rutaHtml,rutaJrxml);
	}
	
	// ***************************************************************
	// Parametros: user (usuario)  p1 (alumno) p2 (practica) p3 (test)
	// ***************************************************************
	public static void InformeTestAlumno (String user,Object p1, Object p2, Object p3){
		CrearDir(user,p2.toString()); // alumno + practica
	 	// Parametros para el informe
		HashMap parametros = new HashMap();
		parametros.put("IDALUMNO",p1);
		parametros.put("IDPRACTICA",p2);
		parametros.put("IDTEST", p3);
		String rutaHtml = es.urjc.ia.web.Configuracion.DIRDATA + user + "/" + p2.toString() + "/" + es.urjc.ia.web.Configuracion.N_Informe_Usuario_Test;
	 	String rutaJrxml = es.urjc.ia.web.Configuracion.DIRINFORMEALUMNO + es.urjc.ia.web.Configuracion.N_Informe_Usuario_Test_JRXML;
	 	LanzarInforme (parametros,rutaHtml,rutaJrxml);
	}

//***************************************************************************************	
// INFORMES ENTREGA
//***************************************************************************************	
	// ************************************************************
	// Parametros: user (usuario)  p1 (alumnos) p2 (practica) p3(grupo)
	// ************************************************************
	public static void InformePracticaEntrega (String user,Object p1, Object p2, Object p3){
		CrearDir(user,p2.toString()); // alumno + practica
		// Parametros para el informe
		HashMap parametros = new HashMap();
		parametros.put("IDGRUPO",p3);
		parametros.put("IDPRACTICA",p2);		
		String rutaHtml = es.urjc.ia.web.Configuracion.DIRDATA + user + "/" + p2.toString() + "/" + es.urjc.ia.web.Configuracion.N_Informe_Entrega;
	 	String rutaJrxml = es.urjc.ia.web.Configuracion.DIRINFORMEENTREGA + es.urjc.ia.web.Configuracion.N_Informe_Entrega_JRXML;
	 	LanzarInforme(parametros,rutaHtml,rutaJrxml);
	}

//***************************************************************************************	
// INFORMES GRUPO
//***************************************************************************************	
	// ************************************************************
	// Parametros: user (usuario)  p1 (alumnos) p2 (practica)
	// ************************************************************
	public static void InformePracticaGrupo (String user,Object p1, Object p2){
		CrearDir(user,p2.toString()); // alumno + practica
		// Parametros para el informe
		HashMap parametros = new HashMap();
		parametros.put("IDALUMNO",p1);
		parametros.put("IDPRACTICA",p2);
		String rutaHtml = es.urjc.ia.web.Configuracion.DIRDATA + user + "/" + p2.toString() + "/" + es.urjc.ia.web.Configuracion.N_Informe_Practica_Grupo;
	 	String rutaJrxml = es.urjc.ia.web.Configuracion.DIRINFORMEGRUPO + es.urjc.ia.web.Configuracion.N_Informe_Practica_Grupo_JRXML;
	 	LanzarInforme(parametros,rutaHtml,rutaJrxml);
	}

	// ************************************************************
	// Parametros: user(usuario) p1 -- alumno // Parametro p2 -- practica// Parametro p3 -- test
	// ************************************************************
	public static void InformeTestGrupo (String user, Object p1, Object p2, Object p3){
		CrearDir(user,p2.toString()); // alumno + practica
		// Parametros para el informe
		HashMap parametros = new HashMap();
		parametros.put("IDTEST",p3);
		parametros.put("IDPRACTICA", p2);
		parametros.put("IDALUMNO",p1);
		String rutaHtml = es.urjc.ia.web.Configuracion.DIRDATA + user + "/" + p2.toString() + "/" + es.urjc.ia.web.Configuracion.N_Informe_Test_Grupo;
	 	String rutaJrxml = es.urjc.ia.web.Configuracion.DIRINFORMEGRUPO + es.urjc.ia.web.Configuracion.N_Informe_Test_Grupo_JRXML;
	 	LanzarInforme(parametros,rutaHtml,rutaJrxml);
	}

	// ************************************************************
	// Parametro p3 -- alumno // Parametro p4 -- grupo 
	// ************************************************************
	public static void InformeCasoGrupo (String user,Object p1, Object p2, Object p3){
		CrearDir(user,p1.toString()); // alumno + practica
		// Parametros para el informe
		HashMap parametros = new HashMap();
		parametros.put("IDPRACTICA", p1);
		parametros.put("IDTEST",p2);
		parametros.put("IDGRUPO",p3);
		String rutaHtml = es.urjc.ia.web.Configuracion.DIRDATA + user + "/" + p1.toString() + "/" + es.urjc.ia.web.Configuracion.N_Informe_Caso_Grupo;
	 	String rutaJrxml = es.urjc.ia.web.Configuracion.DIRINFORMEGRUPO + es.urjc.ia.web.Configuracion.N_Informe_Caso_Grupo_JRXML;
	 	LanzarInforme(parametros,rutaHtml,rutaJrxml);
	}

//***************************************************************************************	
// INFORMES RESUMEN
//***************************************************************************************
	// ***********************************************************
	// Parametros: user + lista user/user
	// ***********************************************************
	public static void InformeEvaluacionResumen (String user,Object p1){
		CrearDir(user,""); // alumno + practica
		// Parametros para el informe
		HashMap parametros = new HashMap();
		parametros.put("IDALUMNO", p1);
		String rutaHtml = es.urjc.ia.web.Configuracion.DIRDATA + user + "/" + es.urjc.ia.web.Configuracion.N_Informe_Usuarios_Resumen;
	 	String rutaJrxml = es.urjc.ia.web.Configuracion.DIRINFORMERESUMEN + es.urjc.ia.web.Configuracion.N_Informe_Usuarios_Resumen_JRXML;
	 	LanzarInforme(parametros,rutaHtml,rutaJrxml);
	}

	// ***********************************************************
	// Parametros: user + grupo
	// ***********************************************************
	public static void InformeEntregaResumen (String User,Object p2){
		CrearDir(User,""); // alumno + practica
		// Parametros para el informe
		HashMap parametros = new HashMap();
		parametros.put("IDGRUPO", p2);
		String rutaHtml = es.urjc.ia.web.Configuracion.DIRDATA + User + "/" + es.urjc.ia.web.Configuracion.N_Informe_Entrega_Resumen;
	 	String rutaJrxml = es.urjc.ia.web.Configuracion.DIRINFORMERESUMEN + es.urjc.ia.web.Configuracion.N_Informe_Entrega_Resumen_JRXML;	 	
	 	LanzarInforme(parametros,rutaHtml,rutaJrxml);
	}

	// ***********************************************************
	// Parametros: user + grupo
	// ***********************************************************
	public static void InformeGrupoResumen (String user,Object p2){
		CrearDir(user,""); // alumno + practica
		// Parametros para el informe
		String rutaHtml = es.urjc.ia.web.Configuracion.DIRDATA + user + "/" + es.urjc.ia.web.Configuracion.N_Informe_Grupo_Resumen;
	 	String rutaJrxml = es.urjc.ia.web.Configuracion.DIRINFORMERESUMEN + es.urjc.ia.web.Configuracion.N_Informe_Grupo_Resumen_JRXML;
		// Parametros para el informe
		HashMap parametros = new HashMap();
		parametros.put("IDGRUPO",p2);	 	
		LanzarInforme(parametros,rutaHtml,rutaJrxml);
	}

}
