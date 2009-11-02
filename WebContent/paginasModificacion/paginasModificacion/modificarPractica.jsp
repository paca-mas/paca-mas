<%@ page import="jade.core.*"%>
<%@ page import="javax.servlet.jsp.*"%>
<%@ page import="javax.servlet.*"%>
<%@ page import="java.util.*"%>
<%@ page import="jade.core.Runtime"%>
<%@ page import="jade.wrapper.*"%>
<%@ page import="es.urjc.ia.paca.util.*"%>
<%@page import="es.urjc.ia.paca.ontology.Practica" %>
<%@page import="es.urjc.ia.paca.ontology.Test" %>

<jsp:useBean id="interfazGestor" class="es.urjc.ia.paca.util.AgentBeanGestor" scope="session"/>


<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01//EN" "http://www.w3.org/TR/html4/strict.dtd">

<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Modificaci&oacute;n de pr&aacute;ctica</title>
        <LINK REL=STYLESHEET TYPE="text/css" HREF="estilos/estiloInterfazGestor.css">
        <SCRIPT TYPE="text/javascript">
            <!--

            salida = true;


            function exit()
            {
                if (salida)
                    window.open("salida.jsp",'frame','width=580,height=480,scrollbars=yes,resizable=yes');
                return true;
            }


            //-->
        </SCRIPT>
    </head>
    <body onUnload="exit();">

        <p class="derecha" > <a href="salida.jsp" class="menu"  onclick="javascript:salida=false;">[Salir]</a> </p>
        <p class="center"  class="color">
			Modificaci&oacute;n de la pr&aacute;ctica.
        </p>



        <%

            String nombre = request.getParameter("NombrePractica");
            String descripcion = request.getParameter("DescripcionPractica");
            String fechaEntrega = request.getParameter("FechaPractica");

            Testigo resultado2 = new Testigo();
            resultado2.setOperacion(Testigo.Operaciones.pedirTests);
            resultado2.setParametro(nombre);

//interfaz.getAtributo().putO2AObject(resultado2,AgentController.SYNC);
            interfazGestor.sendTestigo(resultado2);

            while (!resultado2.isRelleno()) {
            }

            Test[] tests = (Test[]) resultado2.getResultado();

        %>
        <div id="cuerpo">
            <form method="post" name="formTest" action="peticionTest.jsp" onclick="javascript:salida=false;">
                <p> <%= nombre%> </p>
                <p> Descripci&oacute;n: <input type="text" name="descripcion" size="25" value="<%= descripcion%>">  </p>
                <p> Fecha Entrega: <input type="text" name="fechaEntrega" size="25" value="<%= fechaEntrega%>">  </p>

                <div id="central">
                    <table border="4" cellspacing="4" cellpadding="4" width="380" class="center">
                        <tbody>

                            <% for (int i = 0; i < tests.length; i++) {%>

                            <tr>
                                <td> <%= tests[i].getId()%></td>
                                <td>
                                    <form method="post" name="formpracticas" action="modificarTest.jsp" onclick="javascript:salida=false;">
                                        <p> <input  type="hidden" value=<%= tests[i].getId()%> name="NombreTest">
                                            <input  type="hidden" value=<%= tests[i].getDescripcion()%> name="DescripcionTest">
                                            <input type="submit" name="Ver" value="Ver" onclick="javascript:salida=false;"> </p>
                                    </form>
                                </td>
                                <td>
                                    <form method="post" name="formpracticas" action="eliminarTest.jsp" onclick="javascript:salida=false;">
                                        <p> <input  type="hidden" value=<%= tests[i].getId()%> name="NombrePractica">
                                            <input type="submit" name="Eliminar" value="Eliminar" onclick="javascript:salida=false;"> </p>
                                    </form>
                                </td>
                            </tr>
                            <%
            }
                            %>
                        </tbody>
                    </table>
                </div>
                <div id="enlaces">
                    <p>
                    <input type="submit" name="Cambiar Practica" value="Cambiar Practica" onclick="javascript:salida=false;">
                    </p>
                </div>
            </form>
            <div id="enlaces">
                <p> <form method="post" name="formCrear" action="crearTest.jsp" onclick="javascript:salida=false;">
                <input type="submit" name="seleccionar" value="A&ntilde;adir Test">
                </form>
                <form method="post" name="formSeleccionar" action="seleccionarTest.jsp" onclick="javascript:salida=false;">
                <input type="submit" name="seleccionar" value="Seleccionar Test"> </p>
                </form>
            </div>



        </div>
    </body>
</html>


