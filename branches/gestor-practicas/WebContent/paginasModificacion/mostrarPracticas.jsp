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
<%@ page import="es.urjc.ia.paca.agents.InterfazGestor"%>
<%@ page import="es.urjc.ia.paca.agents.InterfazJSPGestor"%>



<jsp:useBean id="interfazGestor" class="es.urjc.ia.paca.util.AgentBeanGestor" scope="session"/>


<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01//EN" "http://www.w3.org/TR/html4/strict.dtd">
<html>

    <head>
        <title>
            Practicas Disponibles.
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


            function desactivarBoton() {
                document.formpracticas.seleccionar.disabled=true;
            }

            //-->
        </SCRIPT>
    </head>

    <body onUnload="exit();">




        <%

                Testigo resultado2 = new Testigo();
                resultado2.setOperacion(Testigo.Operaciones.pedirPracticas);

                interfazGestor.sendTestigo(resultado2);


                Practica[] pract = (Practica[]) resultado2.getResultado();


        %>

        <p class="derecha" > <a href="salida.jsp" class="menu"  onclick="javascript:salida=false;">[Salir]</a> </p>

        <h1 class="center" class="color">
            Listado de pr&aacute;cticas.  </h1>
        <div id="cuerpo">
            <div id="central2">
                <table border="6" cellspacing="6" cellpadding="6" width="380" class="center">
                    <tbody>

                        <% for (int i = 0; i < pract.length; i++) {%>

                        <tr>
                            <td> <%= pract[i].getId()%></td>
                            <td>
                                <form method="post" name="formpracticas" action="modificarPractica.jsp" onsubmit="desactivarBoton();">
                                    <input  type="hidden" value=<%= pract[i].getId()%> name="NombrePractica">
                                    <input  type="hidden" value=<%= pract[i].getDescripcion()%> name="DescripcionPractica">
                                    <input  type="hidden" value=<%= pract[i].getFechaEntrega()%> name="FechaPractica">
                                    <input type="submit" name="Ver" value="Ver" onclick="javascript:salida=false;">
                                </form>
                            </td>
                            <td>
                                <form method="post" name="formpracticas" action="eliminarPractica.jsp" onclick="javascript:salida=false;">
                                    <input  type="hidden" value=<%= pract[i].getId()%> name="NombrePractica">
                                    <input type="submit" name="Eliminar" value="Eliminar" onclick="javascript:salida=false;">
                                </form>
                            </td>
                        </tr>
                        <%
                }
                        %>
                    </tbody>
                </table>
            </div>
            <form method="post" name="formpracticas" action="crearPractica.jsp" onclick="javascript:salida=false;">
                <p class="center">  <input type="submit" name="Crear" value="Crear Nueva Practica"> </p>
            </form>

        </div>
    </body>
</html>

