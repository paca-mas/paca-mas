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
            Test Disponibles para modificaci&oacute;n.
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
        <%@ include file="barra3.html"%>

        <%

            boolean autenticado = false;
            boolean autenticado2 = false;

            Testigo resultado = new Testigo();
            resultado.setOperacion(Testigo.Operaciones.modificarTest);
            resultado.setParametro((HttpServletRequest) request);

            interfaz.sendTestigo(resultado);

            autenticado = resultado.isResultadoB();

            Testigo resultado2 = new Testigo();
            resultado2.setOperacion(Testigo.Operaciones.modificarFicherosPropios);
            resultado2.setParametro((HttpServletRequest) request);
            interfaz.sendTestigo(resultado2);

            autenticado2 = resultado2.isResultadoB();

        %>


        <% if (autenticado && autenticado2) {

        %>

        <br><br><br>
        <p class="center"  class="color">
			Test modificado correctamente.
        </p>
        <br>
        <p align="center" class="color">
            Seleccione el Caso del test a modificar.  </p><br>

        <%
// Hacemos la consulta de los tests necesarios y rellenamos el formulario

//String[] tests = interfaz.doPeticionTestPracticaRequest(request);

     Testigo resultado3 = new Testigo();
     resultado3.setOperacion(Testigo.Operaciones.pedirCasos);
     resultado3.setParametro((HttpServletRequest) request);

//interfaz.getAtributo().putO2AObject(resultado2,AgentController.SYNC);
     interfaz.sendTestigo(resultado3);

     while (!resultado3.isRelleno()) {
     }

     Caso[] caso = (Caso[]) resultado3.getResultado();


        %>

        <p  align="center">
        <form method="post" name="formpracticas" action="modificarCasos.jsp" onsubmit="desactivarBoton();">
            <div class="form"><BR>
                <table width="100%" border="0" cellspacing="0" cellpadding="0" class="color" align="center">
                    <tr>

                        <td width="20%">
                            &nbsp;
                        </td>
                        <td width="60%" align="center">
                            <SELECT size=1 NAME=caso>
                                <%
     // Rellenamos todas las opciones

     for (int i = 0; i < caso.length; i++) {
                                %>
                                <OPTION value=<%= caso[i].getId()%>> <%= caso[i].getId()%>
                                    <%
     }
                                    %>

                            </SELECT>
                        </td>
                        <td width="20%">
                            &nbsp;
                        </td>
                    </tr>
                </table>
                <BR>
            </div>
            <BR><BR><BR>
            <%
     if (caso.length != 0) {
            %>
            <p align="right"><input type="submit" name="seleccionar" value="Seleccionar" onclick="javascript:salida=false;"></p>
                <%            }
                %>
        </form>

        <% } else {
        %>

        <h2 class="error" align="center">
            ERROR!!! En la base de datos </h2>
        <br>
        <p class="error" align="center">
            Ha ocurrido un problema en la base de datos al intentar modificar el test.
        </p>
        <br>
        <br>


        <% }
        %>
    </body>
</html>