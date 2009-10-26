<%@ page import="jade.core.*"%>
<%@ page import="javax.servlet.jsp.*"%>
<%@ page import="javax.servlet.*"%>
<%@ page import="java.util.*"%>
<%@ page import="jade.core.Runtime"%>
<%@ page import="jade.wrapper.*"%>
<%@ page import="es.urjc.ia.paca.util.*"%>
<%@page import="es.urjc.ia.paca.ontology.Practica" %>
<%@page import="es.urjc.ia.paca.ontology.FicheroPropio" %>
<%@page import="es.urjc.ia.paca.ontology.Test" %>

<jsp:useBean id="interfaz" class="es.urjc.ia.paca.util.AgentBean" scope="session"/>


<!doctype html public "-//w3c//dtd html 4.0 transitional//en">

<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Modificaci&oacute;n de Test</title>
        <LINK REL=STYLESHEET TYPE="text/css" HREF="./estilos/estiloPaca.css">
        <SCRIPT TYPE="text/javascript">
            <!--

            salida = true;


            function exit()
            {
                if (salida)
                    window.open("salida.jsp",'frame','width=580,height=480,scrollbars=yes,resizable=yes');
                return true;
            }


            function desactivarBoton() {
                document.formentregar.entregar.disabled=true;
                document.formseleccionar.seleccionar.disabled=true;
            }


            //-->
        </SCRIPT>
    </head>
    <body onUnload="exit();">
        <%@ include file="cab.html"%>
        <%@ include file="barraModificarTest.html"%>

        <br><br><br>
        <p class="center"  class="color">
			Modificaci&oacute;n del Test.
        </p>
        <br>

        <p class="center">
        <form method="post" name="formpractica" action="peticionCasos.jsp" onsubmit="desactivarBoton();">
            <P class="form"><BR>
            <table style="width: 100%;" class="color">
                <%


            Testigo resultado2 = new Testigo();
            resultado2.setOperacion(Testigo.Operaciones.descripcionTest);
            resultado2.setParametro((HttpServletRequest) request);

//interfaz.getAtributo().putO2AObject(resultado2,AgentController.SYNC);
            interfaz.sendTestigo(resultado2);

            while (!resultado2.isRelleno()) {
            }

            Test ts = (Test) resultado2.getResultado();

            Testigo resultado3 = new Testigo();
            resultado3.setOperacion(Testigo.Operaciones.pedirFicherosPropios);
            resultado3.setParametro((HttpServletRequest) request);
            interfaz.sendTestigo(resultado3);

            while (!resultado3.isRelleno()) {
            }

            FicheroPropio[] fp = (FicheroPropio[]) resultado3.getResultado();

                %>
                <tr>
                    <td>
                        <p> <%= ts.getId()%> </p>
                        <p> Descripci&oacute;n: <input type="text" name="descripcion" size="25" value="<%= ts.getDescripcion()%>">  </p>
                        <p> Ficheros Propios a modificar </p>
                        <% for (int i=0; i< fp.length; i++){ %>
                        <p> <%= fp[i].getNombre() %> </p>
                        <TEXTAREA NAME="<%= fp[i].getNombre()%>" ROWS=3 COLS=40><%= fp[i].getCodigo() %></TEXTAREA>

                        <% } %>
                    </td>
                </tr>
            </table><BR>
            <br>
            <p style="text-align: right;">
                <input type="submit" name="seleccionar" value="Cambiar Valores" onclick="javascript:salida=false;">
            </p>
        </form>
    </body>
</html>
