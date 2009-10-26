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
<%@page import="es.urjc.ia.paca.ontology.FicheroIN" %>
<%@page import="es.urjc.ia.paca.ontology.FicheroOUT" %>

<jsp:useBean id="interfaz" class="es.urjc.ia.paca.util.AgentBean" scope="session"/>


<!doctype html public "-//w3c//dtd html 4.0 transitional//en">

<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Modificaci&oacute;n de Caso</title>
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
        <%@ include file="barraModificarCasos.html"%>

        <br><br><br>
        <p class="center"  class="color">
			Modificaci&oacute;n del Caso.
        </p>
        <br>

        <p class="center">
        <form method="post" name="formpractica" action="finModificacion.jsp">
            <P class="form"><BR>
            <table style="width: 100%;" class="color">
                <%


            Testigo resultado2 = new Testigo();
            resultado2.setOperacion(Testigo.Operaciones.pedirFicherosIN);
            resultado2.setParametro((HttpServletRequest) request);

//interfaz.getAtributo().putO2AObject(resultado2,AgentController.SYNC);
            interfaz.sendTestigo(resultado2);

            while (!resultado2.isRelleno()) {
            }

            FicheroIN[] fi = (FicheroIN[]) resultado2.getResultado();

            Testigo resultado3 = new Testigo();
            resultado3.setOperacion(Testigo.Operaciones.pedirFicherosOUT);
            resultado3.setParametro((HttpServletRequest) request);
            interfaz.sendTestigo(resultado3);

            while (!resultado3.isRelleno()) {
            }

            FicheroOUT[] fo = (FicheroOUT[]) resultado3.getResultado();


                %>
                <tr>
                    <td>
                        <p> FicherosIN a modificar </p>
                        <% for (int i = 0; i < fi.length; i++) { %>
                        <p> <%= fi[i].getNombre() %>
                            <input type="text" name="<%= fi[i].getNombre() %>" size="25" value="<%= fi[i].getContenido() %>"></p>
                            <% } %>
                        <br>
                        <br>
                        <br>
                        <p> FicherosOUT a modificar </p>
                        <% for (int i = 0; i < fo.length; i++) {%>
                        <p> <%= fo[i].getNombre()%>
                            <input type="text" name="<%= fo[i].getNombre() %>" size="25" value="<%= fo[i].getContenido() %>"></p>
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

