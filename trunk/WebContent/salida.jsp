
<%@ page import="jade.core.*"%>
<%@ page import="javax.servlet.jsp.*"%>
<%@ page import="javax.servlet.*"%>
<%@ page import="java.util.*"%>
<%@ page import="PACA.util.*"%>

<jsp:useBean id="interfaz" class="PACA.util.AgentBean" scope="session"/>

<!doctype html public "-//w3c//dtd html 4.0 transitional//en">
<html>
	
	<head>
		<title>
			Fin de la aplicaci&oacute;n
		</title>
		<LINK REL=STYLESHEET TYPE="text/css" HREF="http://platon.escet.urjc.es/estilos/estiloPaca.css">
		
	</head>
	<body>
		
		<%@ include file="cab.html"%>
		<%
		interfaz.getAgentController().kill();
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



