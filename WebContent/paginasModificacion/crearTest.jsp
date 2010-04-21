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

            function comprobar(){
                if((document.formTest.DescripcionTest.value=="") ||
                    (document.formTest.NombreTest.value=="")){
                    alert("Debe rellenar todos los datos del formulario")
                    return false
                }
                else{
                    return true
                }
            }
            function valida(){
                if((document.formTest.DescripcionTest.value=="") ||
                    (document.formTest.NombreTest.value=="")){
                    alert("Debe rellenar todos los datos del formulario")
                    return false
                }
                else{
                    alert("Debe guardar el test")
                    return false
                }
            }

            //-->
        </SCRIPT>
    </head>
    <body onUnload="exit();">
        <p class="derecha" >
            <a class="validadorhtml" href="http://validator.w3.org/check?uri=referer"><img
                    style="border:0;width:88px;height:31px" src="http://www.w3.org/Icons/valid-html401-blue"
                    alt="Valid HTML 4.01 Strict" height="31" width="88"></a>
            <a class="validadorcss"href="http://jigsaw.w3.org/css-validator/check/referer">
                <img style="border:0;width:88px;height:31px"
                     src="http://jigsaw.w3.org/css-validator/images/vcss-blue"
                     alt="¡CSS Válido!" >
            </a>
            <a href="mostrarPracticas.jsp" class="menu"  onclick="javascript:salida=false;">[Listado de Practicas]</a> |
            <a href="modificarPractica.jsp" class="menu" onclick="javascript:salida=false;"> [Practica] </a>|
            <a href="salida.jsp" class="menu"  onclick="javascript:salida=false;">[Salir]</a> </p>
        <h1 class="center">
			Creaci&oacute;n del Test.
        </h1>

        <div id="cuerpo">
            <form method="post" name="formTest" action="guardarTest.jsp" onclick="javascript:salida=false;" onsubmit="return comprobar();">
                <p> Nombre: <input type="text" name="NombreTest" size="25"> </p>
                <p> Descripci&oacute;n: <input type="text" name="DescripcionTest" size="25">  </p>
                <p> <input type="hidden" value="" name="EjecutableTest"> </p>
                <p> <input type="hidden" name="operacion" value="crear">
                    <input type="submit" name="seleccionar" value="Guardar Test" onclick="javascript:salida=false;"> </p>
            </form>

            <h3> Ejecutable </h3>
            <div id="centro3">
                <table border="0">
                    <tbody>
                        <tr>
                            <td>
                                <form method="post" name="formAnadir" action="crearEjecutable.jsp" onsubmit="return valida();">
                                    <p> <input type="submit" name="seleccionar" value="Crear Ejecutable" onclick="javascript:salida=false;"> </p>
                                </form>
                            </td>
                            <td>
                                <form method="post" name="formAnadir" action="seleccionarEjecutable.jsp" onsubmit="return valida();">
                                    <p><input type="submit" name="seleccionar" value="A&ntilde;adir copia de Ejecutable existente" onclick="javascript:salida=false;"></p>
                                </form> </td>
                        </tr>

                    </tbody>
                </table>
            </div>
            <div id="enlaces">
                <div id="izquierda">
                    <h3 class="miniTitulo"> Ficheros Propios </h3>
                    <table border="0">
                        <tbody>
                            <tr>
                                <td>
                                    <form method="post" name="formAnadir" action="peticionTest2.jsp" onsubmit="return valida();">
                                        <p> <input type="submit" name="seleccionar" value="A&ntilde;adir FicheroPropio" onclick="javascript:salida=false;"> </p>
                                    </form>
                                </td>
                                <td>
                                    <form method="post" name="formAnadir" action="peticionTest2.jsp" onsubmit="return valida();">
                                        <p><input type="submit" name="seleccionar" value="A&ntilde;adir copia de FicheroPropio existente" onclick="javascript:salida=false;"></p>
                                    </form> </td>
                            </tr>

                        </tbody>
                    </table>
                </div>

                <div id="centro">
                    <h3> Casos </h3>
                    <table border="0">
                        <tbody>
                            <tr>
                                <td>
                                    <form method="post" name="formAnadir" action="peticionTest2.jsp" onsubmit="return valida();">
                                        <p><input type="submit" name="seleccionar" value="A&ntilde;adir Caso" onclick="javascript:salida=false;"></p>
                                    </form>
                                </td>
                                <td>
                                    <form method="post" name="formAnadir" action="peticionTest2.jsp" onsubmit="return valida();">
                                        <p><input type="submit" name="seleccionar" value="A&ntilde;adir copia de Caso existente" onclick="javascript:salida=false;"></p>
                                    </form> </td>
                            </tr>

                        </tbody>
                    </table>
                </div>

                <div id="derecha">
                    <h3 class="miniTitulo"> Ficheros Alumno </h3>
                    <table border="0">
                        <tbody>
                            <tr>
                                <td>
                                    <form method="post" name="formAnadir" action="peticionTest2.jsp" onsubmit="return valida();">
                                        <p><input type="submit" name="seleccionar" value="A&ntilde;adir FicheroAlumno" onclick="javascript:salida=false;"></p>
                                    </form>
                                </td>
                                <td>
                                    <form method="post" name="formAnadir" action="peticionTest2.jsp" onsubmit="return valida();">
                                        <p><input type="submit" name="seleccionar" value="A&ntilde;adir copia de FicheroAlumno existente" onclick="javascript:salida=false;"></p>
                                    </form> </td>
                            </tr>

                        </tbody>
                    </table>
                </div>
            </div>
        </div>

    </body>
</html>
