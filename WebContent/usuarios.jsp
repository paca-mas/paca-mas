<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
------------------------------------------------------------------------------------------------------------------------------>
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

<html xmlns="http://www.w3.org/1999/xhtml">
<head>
	<meta http-equiv="content-type" content="text/html; charset=utf-8" />
	<title>Generacion Web de Informes</title>
	<meta name="keywords" content="" />
	<meta name="description" content="" />
	<link href="style.css" rel="stylesheet" type="text/css" media="screen" />
</head>
<%
	// alumno
	String idUsuario = "1610";
	if(request.getParameter("user")!=null) {
		idUsuario = request.getParameter("user"); 
	}
	// **************************************************************
	String nombre_usuario = ObtenerDatos.DatosAlumnos(idUsuario);	
	String tipoUsuario = ObtenerDatos.TipoUsuario(idUsuario);
%>

<body>
<%
	// Ruta
	String dirLeer = LeerHtml.CrearRutaCompleta(idUsuario);
	String dir = LeerHtml.CrearRutaRelativa(idUsuario);
%>
	<div id="logo">
		<h1>ESTRUCTURA DE LOS DATOS Y DE LA INFORMACION</h1>
	  	<p>&nbsp;</p>
		<p><em> http://zenon.etsii.urjc.es/grupo/docencia/edi/Gestion09-10/doku.php</em></p>
	</div>
	<!-- end #logo -->
	
    <div id="header">
		<div id="search">
			<%
				out.println(nombre_usuario);
			%>
		</div><!-- end #search -->		
	</div><!-- end #header -->
	
    <div id="page">
		<div id="content2">
			<div class="post2">
				<div class="title">Estadisticas de Usuarios</div>
				<div class="entry">
				<%
					List <String> ListaAlumnos = ObtenerDatos.ListaAlumnos(1);
								int i = 0;
								while (i < ListaAlumnos.size()) {
									String nombre = ListaAlumnos.get(i+1) + " "  + ListaAlumnos.get(i+2);
				%>
					<input type="checkbox" name="<%=ListaAlumnos.get(i)%>" value="<%=nombre%>"><%=nombre%></input>
					<br/>
					<%i = i + 3;
				}%>	     		
				
				</div>
		  	</div>
		</div><!-- end #content -->		
	</div><!-- end #page -->
</body>
</html>
