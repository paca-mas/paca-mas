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
            Test Disponibles.
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
                  resultado.setOperacion(Testigo.Operaciones.modificarPractica);
                  resultado.setParametro((HttpServletRequest) request);

                  interfaz.sendTestigo(resultado);

                  //autenticado = resultado.isResultadoB();

        %>


        <br><br><br>  
        <p class="center"  class="color">
			Seleccione los test que ser&aacute;n evaluados.
        </p>
        <br>
        <%--
        <p class="center">
        <form method="post" name="formseleccionar" action="peticionFicheros.jsp" onsubmit="desactivarBoton();">
            <P class="form"><BR>
            <table style="width: 100%;" class="color">
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

for (int i = 0; i < tests.length; i += 2) {
                %>
                <tr>

                    <td style="width: 10%;">
                        <p class="center">
                            <INPUT NAME="<%= tests[i]%>" TYPE=CHECKBOX>
                        </p>
                    </td>
                    <td style="width: 10%;">
                        &nbsp;
                    </td>
                    <td style="width: 80%;">
                        <p style="text-align: left;">
                            <%= tests[i + 1]%>
                        </p>
                    </td>
                </tr>
                <%
}
                %>
            </table><BR>
            </P>
            <br>
            <p style="text-align: right;">
                <input type="submit" name="seleccionar" value="Seleccionar Test" onclick="javascript:salida=false;">
            </p>
        </form>--%>
    </p>
</body>
</html>
