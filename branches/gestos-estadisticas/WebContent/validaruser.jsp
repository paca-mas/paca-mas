<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<!-- Los import -->
<%@ page language="java" %>
<%@ page import = "java.sql.*"%> 
<%@ page import = "java.util.*"%>
<%@ page import = "javax.swing.*"%>
<%@ page import = "net.sf.jasperreports.engine.*" %>
<%@ page import = "net.sf.jasperreports.view.JasperViewer" %>
<%@ page import = "java.sql.Connection" %>
<%@ page import = "java.io.*" %>


<%@page import="es.urjc.ia.web.ObtenerDatos"%><html xmlns="http://www.w3.org/1999/xhtml">
<head>
	<meta http-equiv="content-type" content="text/html; charset=utf-8" />
	<meta name="keywords" content="" />
	<meta name="description" content="" />
</head>
<body>
<%
	String login_db=" ";
	String password_db=" ";
	ResultSet rs = null ;
	String cadena = " ";
	String pagina;
	String login = request.getParameter("usuario");
	String password = request.getParameter("password");

	List<String> DatosUser = ObtenerDatos.Login(login);
	if (DatosUser.size()!=0) {
		login_db = DatosUser.get(0);
		password_db = DatosUser.get(1);
	}			
	
	if (login.equals(login_db) && password.equals(password_db)){
	response.sendRedirect("paginasInformes/index.jsp?user="+login+"&list="+login) ;
	}else{
	response.sendRedirect("errorlogin.jsp") ;
	}
%>		     		
</body>
</html>