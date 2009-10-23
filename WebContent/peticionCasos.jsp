<%@ page import="jade.core.*"%>
<%@ page import="javax.servlet.jsp.*"%>
<%@ page import="javax.servlet.*"%>
<%@ page import="java.util.*"%>
<%@ page import="jade.core.Runtime"%>
<%@ page import="jade.wrapper.*"%>
<%@ page import="es.urjc.ia.paca.util.*"%>

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
        <%@ include file="barra1.html"%>

        <%

            //boolean autenticado = false;

            Testigo resultado = new Testigo();
            resultado.setOperacion(Testigo.Operaciones.modificarTest);
            resultado.setParametro((HttpServletRequest) request);

            interfaz.sendTestigo(resultado);

            Testigo resultado2 = new Testigo();
            resultado2.setOperacion(Testigo.Operaciones.modificarFicherosPropios);
            resultado2.setParametro((HttpServletRequest) request);
            interfaz.sendTestigo(resultado2);

            //autenticado = resultado.isResultadoB();

        %>


        <br><br><br>
        <p class="center"  class="color">
			Test modificado correctamente.
        </p>
        <br>
        <p align="center" class="color">
            Seleccione el Caso del test a modificar.  </p><br>
        <%--
        <%
// Hacemos la consulta de los tests necesarios y rellenamos el formulario

//String[] tests = interfaz.doPeticionTestPracticaRequest(request);

    Testigo resultado2 = new Testigo();
    resultado2.setOperacion(Testigo.Operaciones.pedirTests);
    resultado2.setParametro((HttpServletRequest) request);

//interfaz.getAtributo().putO2AObject(resultado2,AgentController.SYNC);
    interfaz.sendTestigo(resultado2);

    while (!resultado2.isRelleno()) {
    }

    String[] tests = (String[]) resultado2.getResultado();


        %>

        <p  align="center">
        <form method="post" name="formpracticas" action="modificarTest.jsp" onsubmit="desactivarBoton();">
            <div class="form"><BR>
                <table width="100%" border="0" cellspacing="0" cellpadding="0" class="color" align="center">
                    <tr>

                        <td width="20%">
                            &nbsp;
                        </td>
                        <td width="60%" align="center">
                            <SELECT size=1 NAME=test>
                                <%
                                            // Rellenamos todas las opciones

                                            for (int i = 0; i < tests.length; i += 2) {
                                %>
                                <OPTION value=<%= tests[i]%>> <%= tests[i]%>
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
                        if (tests.length != 0) {
            %>
            <p align="right"><input type="submit" name="seleccionar" value="Seleccionar" onclick="javascript:salida=false;"></p>
                <%            }
                %>
        </form> --%>
    </body>
</html>
