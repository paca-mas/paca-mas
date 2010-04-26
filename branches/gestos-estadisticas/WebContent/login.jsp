<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<!-- Los import -->
<%@ page language="java" %>
<%@ page import = "javax.swing.*"%>
<%@ page import = "es.urjc.ia.web.*" %>

<html xmlns="http://www.w3.org/1999/xhtml">
<head>
	<meta http-equiv="content-type" content="text/html; charset=utf-8" />
	<title>Generacion Web de Informes</title>
	<meta name="keywords" content="" />
	<meta name="description" content="" />
	<link href="<%=Configuracion.Css%>" rel="stylesheet" type="text/css" media="screen" />
</head>
<body>
	<div id="logo">
		<h1>ESTRUCTURA DE LOS DATOS Y DE LA INFORMACION</h1>
	  	<p>&nbsp;</p>
		<p><em> http://zenon.etsii.urjc.es/grupo/docencia/edi/Gestion09-10/doku.php</em></p>
	</div>
	<!-- end #logo -->
		
    <div id="page">
		<div id="content2">
			<div class="post2">
				<div class="title">Login Estadisticas Web Practicas</div>
				<div class="entry">
					<form action="validaruser.jsp" method="post">
						Usuario
      					<input type="text" name="usuario" size="20" maxlength="20" /> <br />
      					Password
      					<input type="password" name="password" size="10" maxlength="10" /> <br />
      					<input type="submit" value="Ingresar" />
      				</form>
		     	</div>
		  	</div>
		</div><!-- end #content -->		
	</div><!-- end #page -->
</body>
</html>