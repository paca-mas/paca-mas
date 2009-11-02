<%@ page import="jade.core.*"%>
<%@ page import="javax.servlet.jsp.*"%>
<%@ page import="javax.servlet.*"%>
<%@ page import="java.util.*"%>
<%@ page import="es.urjc.ia.paca.util.*"%>

<jsp:useBean id="interfazGestor" class="es.urjc.ia.paca.util.AgentBeanGestor" scope="session"/>

<!doctype html public "-//w3c//dtd html 4.0 transitional//en">
<html>

	<head>
		<title>
			Fin de la aplicaci&oacute;n
		</title>
		<LINK REL=STYLESHEET TYPE="text/css" HREF="/estilos/estiloPaca.css">

	</head>
	<body>

		<%@ include file="cab.html"%>
		<%
		interfazGestor.getAgentController().kill();
		session.invalidate();
		%>

		<br>
		<br>
		<br>
		<br>
		<p style="text-align: center">
			Gracias por utilizar la plataforma de correcci&oacute;n autom&aacute;tica.<br>
		</p>
	</body>
</html>

