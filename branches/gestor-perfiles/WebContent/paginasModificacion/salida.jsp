<%@ page import="jade.core.*"%>
<%@ page import="javax.servlet.jsp.*"%>
<%@ page import="javax.servlet.*"%>
<%@ page import="java.util.*"%>
<%@ page import="es.urjc.ia.paca.util.*"%>

<jsp:useBean id="interfazGestor" class="es.urjc.ia.paca.util.AgentBeanGestor" scope="session"/>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01//EN" "http://www.w3.org/TR/html4/strict.dtd">
<html>

    <head>
        <title>
			Fin de la aplicaci&oacute;n
        </title>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">

        <LINK REL=STYLESHEET TYPE="text/css" HREF="/estilos/estiloPaca.css">

    </head>
    <body>

        <%@ include file="cab.html"%>

        <p>
            <a href="http://validator.w3.org/check?uri=referer"><img
                    style="border:0;width:88px;height:31px" src="http://www.w3.org/Icons/valid-html401-blue"
                    alt="Valid HTML 4.01 Strict" height="31" width="88"></a>
            <a href="http://jigsaw.w3.org/css-validator/check/referer">
                <img style="border:0;width:88px;height:31px"
                     src="http://jigsaw.w3.org/css-validator/images/vcss-blue"
                     alt="¡CSS Válido!" >
            </a>
        </p>


        <%
    interfazGestor.getAgentController().kill();
    session.invalidate();
        %>

        <p style="text-align: center">
			Gracias por utilizar la plataforma de correcci&oacute;n autom&aacute;tica.<br>
        </p>
    </body>
</html>

