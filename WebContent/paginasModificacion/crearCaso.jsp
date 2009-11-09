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



            function desactivarBoton(){
                document.formTest.seleccionar.disabled=true;
            }


            function comprobar(){
                if((document.formTest.NombreCaso.value=="")){
                    alert("Debe rellenar todos los datos del formulario")
                    return false
                }
                else{
                    return true
                }
            }

            function valida(){
                if((document.formTest.NombreCaso.value=="")){
                    alert("Debe rellenar todos los datos del formulario")
                    return false
                }
                else{
                    alert("Debe guardar antes el caso")
                    return false
                }
            }


            //-->
        </SCRIPT>
    </head>
    <body onUnload="exit();">


        <p class="derecha" > <a href="mostrarPracticas.jsp" class="menu"  onclick="javascript:salida=false;">[Listado de Practicas]</a> |
            <a href="modificarPractica.jsp" class="menu" onclick="javascript:salida=false;"> [Practica] |
                <a href="modificarTest.jsp" class="menu" onclick="javascript:salida=false;"> [Test] |
                    <a href="salida.jsp" class="menu"  onclick="javascript:salida=false;">[Salir]</a> </p>
                    <h1 class="center"  class="color">
			Creaci&oacute;n del Caso.
                    </h1>
                    <div id="cuerpo">
                        <form method="post" name="formTest" action="modificarCasos.jsp" onsubmit="return comprobar();">
                            <p> Nombre del Caso: <input  type="text" name="NombreCaso"> </p>
                            <input type="hidden" name="operacion" value="crear">
                            <input type="submit" name="seleccionar" value="Guardar Caso" onclick="javascript:salida=false;">
                        </form>
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

                    </body>
                    </html>
