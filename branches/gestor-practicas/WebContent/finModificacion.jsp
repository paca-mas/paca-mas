<%@ page import="jade.core.*"%>
<%@ page import="javax.servlet.jsp.*"%>
<%@ page import="javax.servlet.*"%>
<%@ page import="java.util.*"%>
<%@ page import="jade.core.Runtime"%>
<%@ page import="jade.wrapper.*"%>
<%@ page import="es.urjc.ia.paca.util.*"%>
<%@ page import="es.urjc.ia.paca.ontology.Caso"%>

<jsp:useBean id="interfaz" class="es.urjc.ia.paca.util.AgentBean" scope="session"/>
<!doctype html public "-//w3c//dtd html 4.0 transitional//en">

<html>
    <head>
        <title>
            Fin modificaciones.
        </title>
        <LINK REL=STYLESHEET TYPE="text/css" HREF="http://platon.escet.urjc.es/estilos/estiloPACA.css">
        <SCRIPT TYPE="text/javascript">
            <!--

            salida = true;

            function exit()
            {
                if (salida)
                    window.open("salida.jsp",'frame','width=580,height=480,scrollbars=yes,resizable=yes')
                return true;
            }


            function desactivarBoton() {
                document.formpracticas.seleccionar.disabled=true;
            }

            //-->
        </SCRIPT>
    </head>
    <body onUnload="exit();">
        <%@ include file="cab.html"%>
        <%@ include file="barra1.html"%>

        <%

            //boolean autenticado = false;

            Testigo resultado = new Testigo();
            resultado.setOperacion(Testigo.Operaciones.modificarFicherosIN);
            resultado.setParametro((HttpServletRequest) request);

            interfaz.sendTestigo(resultado);

            Testigo resultado2 = new Testigo();
            resultado2.setOperacion(Testigo.Operaciones.modificarFicherosOUT);
            resultado2.setParametro((HttpServletRequest) request);
            interfaz.sendTestigo(resultado2);

            //autenticado = resultado.isResultadoB();

        %>


        <br><br><br>
        <p class="center"  class="color">
			Ficheros IN y Ficheros OUT modificados correctamente.
        </p>

    </body>
</html>
