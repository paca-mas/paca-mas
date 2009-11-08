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
                if((document.formTest.DescripcionPractica.value=="") || (document.formTest.FechaPractica.value=="") ||
                    (document.formTest.NombrePractica.value=="")){
                    alert("Debe rellenar todos los datos del formulario")
                    return false
                 }
                 else{
                     return true
                 }
            }
            function valida(){
                if((document.formTest.DescripcionPractica.value=="") || (document.formTest.FechaPractica.value=="") ||
                    (document.formTest.NombrePractica.value=="")){
                    alert("Debe rellenar todos los datos del formulario")
                    return false
                }
                else{
                    alert("Debe guardar la practica")
                    return false
                }
            }

            //-->
        </SCRIPT>
    </head>
    <body onUnload="exit();">

        <p class="derecha" > <a href="mostrarPracticas.jsp" class="menu"  onclick="javascript:salida=false;">[Listado de Practicas]</a> |
            <a href="salida.jsp" class="menu"  onclick="javascript:salida=false;">[Salir]</a> </p>
        <h1 class="center"  class="color">
			Creaci&oacute;n de la pr&aacute;ctica.
        </h1>

        <div id="cuerpo">
            <form method="post" name="formTest" action="guardarPractica.jsp" onclick="javascript:salida=false;" onsubmit="return comprobar();">
                <p> Nombre: <input type="text" name="NombrePractica" size="25"> </p>
                <p> Descripci&oacute;n: <input type="text" name="DescripcionPractica" size="25">  </p>
                <p> Fecha Entrega: <input type="text" name="FechaPractica" size="25"> </p>
                <input type="hidden" name="operacion" value="crear">
                <input type="submit" name="seleccionar" value="Guardar Practica" onclick="javascript:salida=false;">
            </form>
            <div id="enlaces">
                <table border="0">
                    <tbody>
                        <tr>
                            <td>
                                <form method="post" name="formAnadir" action="peticionTest2.jsp" onsubmit="return valida();">
                                    <input type="submit" name="seleccionar" value="A&ntilde;adir Test" onclick="javascript:salida=false;">
                                </form>
                            </td>
                            <td>
                                <form method="post" name="formAnadir" action="peticionTest2.jsp" onsubmit="return valida();">
                                    <input type="submit" name="seleccionar" value="Seleccionar Test" onclick="javascript:salida=false;">
                                </form> </td>
                        </tr>

                    </tbody>
                </table>
            </div>
        </div>

    </body>
</html>
