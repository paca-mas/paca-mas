<%@ page import="jade.core.*"%>
<%@ page import="javax.servlet.jsp.*"%>
<%@ page import="javax.servlet.*"%>
<%@ page import="java.util.*"%>
<%@ page import="jade.core.Runtime"%>
<%@ page import="jade.wrapper.*"%>
<%@ page import="es.urjc.ia.paca.util.*"%>
<%@page import="es.urjc.ia.paca.ontology.Practica" %>
<%@page import="es.urjc.ia.paca.ontology.Test" %>
<%@page import="es.urjc.ia.paca.ontology.FicheroPropio" %>
<%@page import="es.urjc.ia.paca.ontology.Caso" %>
<%@page import="es.urjc.ia.paca.ontology.FicheroAlumno" %>
<%@page import="es.urjc.ia.paca.ontology.FicheroIN" %>
<%@page import="es.urjc.ia.paca.ontology.FicheroOUT" %>

<jsp:useBean id="interfazGestor" class="es.urjc.ia.paca.util.AgentBeanGestor" scope="session"/>


<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01//EN" "http://www.w3.org/TR/html4/strict.dtd">

<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Modificaci&oacute;n de test</title>
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

            function valida(){
                if(!(document.formTest.descripcion.value==document.formTest.DescripcionAntigua.value)){
                    alert("Debe guardar la practica antes de continuar")
                    return false
                }
                else{
                    document.formTest.seleccionar.disabled=true;
                    document.formAnadir.seleccionar.disabled=true;
                    document.formEliminar.Eliminar.disabled=true;
                    document.formSeleccionar.seleccionar.disabled=true;
                    document.formVer.Ver.disabled=true;
                    document.formVer.Ver.disabled=true;
                    return true
                }
            }

            function desactivarBoton(){
                document.formTest.seleccionar.disabled=true;
                document.formAnadir.seleccionar.disabled=true;
                document.formEliminar.Eliminar.disabled=true;
                document.formSeleccionar.seleccionar.disabled=true;
                document.formVer.Ver.disabled=true;
            }


            //-->
        </SCRIPT>
    </head>
    <body onUnload="exit();">


        <p class="derecha" > <a href="salida.jsp" class="menu"  onclick="javascript:salida=false;">[Salir]</a> </p>
        <h1 class="center"  class="color">
			Modificaci&oacute;n del test.
        </h1>



        <%

            String nombre = request.getParameter("NombreCaso");

            Testigo resultado2 = new Testigo();
            resultado2.setOperacion(Testigo.Operaciones.pedirFicherosIN);
            resultado2.setParametro(nombre);


            interfazGestor.sendTestigo(resultado2);

            while (!resultado2.isRelleno()) {
            }

            FicheroIN[] fis = (FicheroIN[]) resultado2.getResultado();

            Testigo resultado3 = new Testigo();
            resultado3.setOperacion(Testigo.Operaciones.pedirFicherosOUT);
            resultado3.setParametro(nombre);


            interfazGestor.sendTestigo(resultado3);

            while (!resultado3.isRelleno()) {
            }

            FicheroOUT[] fos = (FicheroOUT[]) resultado3.getResultado();



        %>
        <div id="cuerpo">
            <h2> <%= nombre%> </h2>
            <div id="enlaces">
                <div id="izquierda">
                    <table border="0">
                        <tbody>
                            <tr>
                                <td>
                                    <form method="post" name="formAnadir" action="peticionTest2.jsp" onsubmit="return valida();">
                                        <input type="submit" name="seleccionar" value="A&ntilde;adir FicheroIN" onclick="javascript:salida=false;">
                                    </form>
                                </td>
                                <td>
                                    <form method="post" name="formAnadir" action="peticionTest2.jsp" onsubmit="return valida();">
                                        <input type="submit" name="seleccionar" value="Seleccionar FicheroIN" onclick="javascript:salida=false;">
                                    </form> </td>
                            </tr>

                        </tbody>
                    </table>
                </div>

                <div id="derecha">
                    <table border="0">
                        <tbody>
                            <tr>
                                <td>
                                    <form method="post" name="formAnadir" action="peticionTest2.jsp" onsubmit="return valida();">
                                        <input type="submit" name="seleccionar" value="A&ntilde;adir FicheroOUT" onclick="javascript:salida=false;">
                                    </form>
                                </td>
                                <td>
                                    <form method="post" name="formAnadir" action="peticionTest2.jsp" onsubmit="return valida();">
                                        <input type="submit" name="seleccionar" value="Seleccionar FicheroOUT" onclick="javascript:salida=false;">
                                    </form> </td>
                            </tr>

                        </tbody>
                    </table>
                </div>
            </div>
        </div>
        <div id="central">

            <div id="izquierda2">
                <table border="4" cellspacing="4" cellpadding="4" width="380" class="center">
                    <tbody>

                        <% for (int i = 0; i < fis.length; i++) {%>

                        <tr>
                            <td> <%= fis[i].getNombre()%></td>
                            <td>
                                <form method="post" name="formVer" action="modificarFicherosIN.jsp" onsubmit="return valida();">
                                    <input  type="hidden" value=<%= fis[i].getNombre()%> name="NombreFichero">
                                    <input  type="hidden" value=<%= fis[i].getContenido()%> name="ContenidoFichero">
                                    <input type="submit" name="Ver" value="Ver" onclick="javascript:salida=false;">
                                </form>
                            </td>
                            <td>
                                <form method="post" name="formEliminar" action="eliminarTest.jsp" onsubmit="return valida();">
                                    <input  type="hidden" value=<%= fis[i].getNombre()%> name="NombreFichero">
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

            <div id="derecha2">
                <table border="4" cellspacing="4" cellpadding="4" width="380" class="center">
                    <tbody>

                        <% for (int i = 0; i < fos.length; i++) {%>

                        <tr>
                            <td> <%= fos[i].getNombre()%></td>
                            <td>
                                <form method="post" name="formVer" action="modificarFicherosOUT.jsp" onsubmit="return valida();">
                                    <input  type="hidden" value=<%= fos[i].getNombre()%> name="NombreFichero">
                                    <input  type="hidden" value=<%= fos[i].getContenido()%> name="ContenidoFichero">
                                    <input type="submit" name="Ver" value="Ver" onclick="javascript:salida=false;">
                                </form>
                            </td>
                            <td>
                                <form method="post" name="formEliminar" action="eliminarTest.jsp" onsubmit="return valida();">
                                    <input  type="hidden" value=<%= fos[i].getNombre()%> name="NombreFichero">
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
        </div>

    </body>
</html>
