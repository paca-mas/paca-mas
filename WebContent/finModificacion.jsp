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



            //-->
        </SCRIPT>
    </head>
    <body onUnload="exit();">
        <%@ include file="cab.html"%>
        <%@ include file="barraFinModificacion.html"%>


        <%

            boolean autenticado = false;
            boolean autenticado2 = false;

            Testigo resultado = new Testigo();
            resultado.setOperacion(Testigo.Operaciones.modificarFicherosIN);
            resultado.setParametro((HttpServletRequest) request);
            interfaz.sendTestigo(resultado);

            autenticado = resultado.isResultadoB();

            Testigo resultado2 = new Testigo();
            resultado2.setOperacion(Testigo.Operaciones.modificarFicherosOUT);
            resultado2.setParametro((HttpServletRequest) request);
            interfaz.sendTestigo(resultado2);

            autenticado2 = resultado2.isResultadoB();

        %>

        <% if (autenticado && autenticado2) {

        %>


        <%
     Testigo resultado3 = new Testigo();
     resultado3.setOperacion(Testigo.Operaciones.pedirPracticas);

     interfaz.sendTestigo(resultado3);

     while (!resultado3.isRelleno()) {
     }


     String[] pract = (String[]) resultado3.getResultado();

     //autenticado = resultado.isResultadoB();

        %>

        <br><br><br>
        <p align="center" class="color">
            Ficheros IN y OUT modificados correctamente.  </p><br>

        <br><br><br>
        <p align="center" class="color">
            Seleccione la pr&aacute;ctica a modificar.  </p><br>
        <p  align="center">
        <form method="post" name="formpracticas" action="modificarPractica.jsp">
            <div class="form"><BR>
                <table width="100%" border="0" cellspacing="0" cellpadding="0" class="color" align="center">
                    <tr>

                        <td width="20%">
                            &nbsp;
                        </td>
                        <td width="60%" align="center">
                            <SELECT size=1 cols=<%= pract.length%> NAME=practica>
                                <%
     // Rellenamos todas las opciones

     for (int i = 0; i < pract.length; i++) {
                                %>
                                <OPTION value=<%= pract[i]%>> <%= pract[i]%>
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
     if (pract.length != 0) {
            %>
            <p align="right"><input type="submit" name="seleccionar" value="Seleccionar" onclick="javascript:salida=false;"></p>
                <%     }
                %>
        </form>

        <% } else {
        %>

        <h2 class="error" align="center">
            ERROR!!! En la base de datos </h2>
        <br>
        <p class="error" align="center">
            Ha ocurrido un problema en la base de datos al intentar modificar los Ficheros IN y OUT.
        </p>
        <br>
        <br>


        <% }
        %>

    </body>
</html>
