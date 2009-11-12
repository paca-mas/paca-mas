<%@ page import="jade.core.*"%>
<%@ page import="jade.core.Agent.*"%>
<%@ page import="jade.core.Runtime"%>
<%@ page import="jade.wrapper.*"%>
<%@ page import="javax.servlet.jsp.*"%>
<%@ page import="javax.servlet.*"%>
<%@ page import="java.util.*"%>
<%@ page import="es.urjc.ia.paca.util.*"%>
<%@ page import="es.urjc.ia.paca.ontology.pacaOntology"%>
<%@ page import="es.urjc.ia.paca.ontology.Practica"%>
<%@ page import="es.urjc.ia.paca.ontology.Test"%>
<%@ page import="es.urjc.ia.paca.agents.InterfazGestor"%>
<%@ page import="es.urjc.ia.paca.agents.InterfazJSPGestor"%>



<jsp:useBean id="interfazGestor" class="es.urjc.ia.paca.util.AgentBeanGestor" scope="session"/>


<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01//EN" "http://www.w3.org/TR/html4/strict.dtd">
<html>

    <head>
        <title>
            Seleccionar Test.
        </title>
        <LINK REL=STYLESHEET TYPE="text/css" HREF="estilos/estiloInterfazGestor.css">
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

        <p class="derecha" > <a href="mostrarPracticas.jsp" class="menu"  onclick="javascript:salida=false;">[Listado de Practicas]</a> |
            <a href="modificarPractica.jsp" class="menu" onclick="javascript:salida=false;"> [Practica] | 
                <a href="salida.jsp" class="menu"  onclick="javascript:salida=false;">[Salir]</a> </p>

        <h1 class="center" class="color">
            Selecci&oacute;n de Test.  </h1>



        <%

        Testigo resultado2 = new Testigo();
        resultado2.setOperacion(Testigo.Operaciones.pedirPracticas);

        interfazGestor.sendTestigo(resultado2);

        while (!resultado2.isRelleno()) {
        }

        Practica[] pract = (Practica[]) resultado2.getResultado();


        %>

        <%
        for (int i = 0; i < pract.length; i++) {

            Testigo resultado = new Testigo();
            resultado.setOperacion(Testigo.Operaciones.seleccionarTest);
            resultado.setParametro(pract[i].getId());


            interfazGestor.sendTestigo(resultado);

            while (!resultado.isRelleno()) {
            }

            Test[] tests = (Test[]) resultado.getResultado();

        %>

        <div id="cuerpo">
            <h3> <%= pract[i].getId()%> </h3>
            <div id="central3">
            <% for (int z = 0; z < tests.length; z++) {%>
            <table border="0">
                <tbody>
                    <tr>
                        <td> <%= tests[z].getId()%>  </td>
                        <td>
                            <form class="center" method="post" name="formVer" action="modificarPractica.jsp">
                                <input  type="hidden" value="<%= tests[z].getId()%>" name="NombreTest">
                                <input  type="hidden" value="<%= pract[i].getId()%>" name="NombrePracticaACopiar">
                                <input type="hidden" value="<%= tests[z].getDescripcion()%>" name="DescripcionTest">
                                <input type="hidden" value="copiar" name="operacion">
                                <input type="submit" name="Seleccionar" value="Seleccionar" onclick="javascript:salida=false;">
                            </form>
                        </td>
                    </tr>

                </tbody>
            </table>
            <%
                }
            %>
            </div>
        </div>

        <% }
        %>
    </body>
</html>

