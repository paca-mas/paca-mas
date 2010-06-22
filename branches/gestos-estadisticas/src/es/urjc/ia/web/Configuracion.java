package es.urjc.ia.web;

public class Configuracion {
	
	public static final String DIR 			= "C:/Users/sandrita/workspace_PFC";
	public static final String DIRBASE		= DIR + "/gestor-estadisticas/WebContent/";
	public static final String DIRINFORME	= DIRBASE + "/paginasInformes/Informes/";
	public static final String DIRDATA		= DIRBASE + "/paginasInformes/Data/";

	public static final String DIRRELATIVA = "./Data/";
	
	public static final String DIRIMAGES =   "./estilos/images/";
	
	public static final String DIRINFORMEALUMNO 	= DIRINFORME + "Usuarios/";
	public static final String DIRINFORMEGRUPO  	= DIRINFORME + "Grupo/";
	public static final String DIRINFORMERESUMEN 	= DIRINFORME + "Resumen/";
	public static final String DIRINFORMEENTREGA	= DIRINFORME + "Entrega/";
	
	// Usuarios
	public static final String N_Informe_Usuario_Practica 		= "Informe_Usuarios_Practica.html";
	public static final String N_Informe_Usuario_Practica_JRXML = "Informe_Usuarios_Practica.jrxml";
	public static final String N_Informe_Usuario_Test 			= "Informe_Usuarios_Test.html";
	public static final String N_Informe_Usuario_Test_JRXML 	= "Informe_Usuarios_Test.jrxml";

	// Entrega
	public static final String N_Informe_Entrega 		= "Informe_Entrega_Practica.html";
	public static final String N_Informe_Entrega_JRXML 	= "Informe_Entrega_Practica.jrxml";
	
	// Grupo
	public static final String N_Informe_Practica_Grupo 		= "Informe_Grupo_Practica.html";
	public static final String N_Informe_Practica_Grupo_JRXML 	= "Informe_Grupo_Practica.jrxml";
	public static final String N_Informe_Test_Grupo 			= "Informe_Grupo_Test.html";
	public static final String N_Informe_Test_Grupo_JRXML 		= "Informe_Grupo_Test.jrxml";
	public static final String N_Informe_Caso_Grupo 			= "Informe_Grupo_Caso.html";
	public static final String N_Informe_Caso_Grupo_JRXML 		= "Informe_Grupo_Caso.jrxml";

	// Resumen
	public static final String N_Informe_Usuarios_Resumen 		= "Informe_Usuarios_Resumen.html";
	public static final String N_Informe_Usuarios_Resumen_JRXML = "Informe_Usuarios_Resumen.jrxml";
	public static final String N_Informe_Entrega_Resumen 		= "Informe_Entrega_Resumen.html";
	public static final String N_Informe_Entrega_Resumen_JRXML 	= "Informe_Entrega_Resumen.jrxml";
	public static final String N_Informe_Grupo_Resumen 			= "Informe_Grupo_Resumen.html";
	public static final String N_Informe_Grupo_Resumen_JRXML 	= "Informe_Grupo_Resumen.jrxml";

	// Jsp
	public static final String DIRPAGINFORMES = "paginasInformes/";
	public static final String Jsp_Inicio = "index.jsp";
	public static final String Jsp_Entrega =  "Entrega.jsp";
	public static final String Jsp_Evaluacion =   "Evaluacion.jsp";
	public static final String Jsp_Grupo =    "Grupo.jsp";
	public static final String Jsp_AlumnosP =  "LanzarAlumnoPractica.jsp";
	public static final String Jsp_AlumnosT =  "LanzarAlumnoTest.jsp";
	public static final String Jsp_AlumnosC =  "LanzarAlumnoCaso.jsp";
	public static final String Jsp_GrupoP =  "LanzarGrupoPractica.jsp";
	public static final String Jsp_GrupoT =  "LanzarGrupoTest.jsp";
	public static final String Jsp_GrupoC =  "LanzarGrupoCaso.jsp";
	public static final String Jsp_EntregaP =  "LanzarEntregaPractica.jsp";

	// Css
	public static final String Css = "./estilos/styleEstadisticas.css";

	// Datos bbdd
	public static final String usuario = "root";
	public static final String pass = "estadisticas";
	public static final String bbdd = "BBDDEstadisticas";
}