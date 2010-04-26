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
<%@ page import = "es.urjc.ia.web.*" %>
<%@ page import = "es.urjc.ia.informes.*" %>
<%@ page import = "es.urjc.ia.baseDatos.*" %>

<html xmlns="http://www.w3.org/1999/xhtml">
<head>
	<meta http-equiv="content-type" content="text/html; charset=utf-8" />
	<title>Generacion Web de Informes</title>
	<meta name="keywords" content="" />
	<meta name="description" content="" />
	<link href="<%=Configuracion.Css%>" rel="stylesheet" type="text/css" media="screen" />
</head>
<%
	// alumno
	String idUsuario = "1610";
	String nombre_usuario = "";
	if(request.getParameter("user")!=null) {
		idUsuario = request.getParameter("user"); 
	}
	// **************************************************************
	nombre_usuario = ObtenerDatos.DatosAlumnos(idUsuario);
%>

<body>
	<div id="logo">
		<h1>ESTRUCTURA DE LOS DATOS Y DE LA INFORMACION</h1>
	  	<p>&nbsp;</p>
		<p><em> http://zenon.etsii.urjc.es/grupo/docencia/edi/Gestion09-10/doku.php</em></p>
	</div>	<!-- end #logo -->
		
	<div id="page">
		<div id="content2">
			<div class="post2">
				<div class="title">Login Estadisticas Web Practicas</div>
				<div class="entry">
					Login o password incorrectos
					<br/>
					Por favor, vuelva a introductirlos
					<br/>
					<a href="login.jsp">Login</a>
			  	</div>
			</div>
		</div><!-- end #content -->
	</div><!-- end #page -->
</body>
</html>
