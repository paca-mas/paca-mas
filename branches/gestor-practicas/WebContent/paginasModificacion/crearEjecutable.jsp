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
        <title>Creaci&oacute;n del Ejecutable</title>
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
                if(((document.formTest.LeerFichero.value=="") &&
                    (document.formTest.CodigoFichero.value==""))){
                    alert("Debe rellenar alguno de los datos del formulario")
                    return false
                }
                else{
                    return true
                }
            }


            //-->
        </SCRIPT>
    </head>
    <body onUnload="exit();">


        <p class="derecha" > <a href="mostrarPracticas.jsp" class="menu"  onclick="javascript:salida=false;">[Listado de Practicas]</a> |
            <a href="modificarPractica.jsp" class="menu" onclick="javascript:salida=false;"> [Practica] </a>|
            <a href="modificarTest.jsp" class="menu" onclick="javascript:salida=false;"> [Test] </a>|
            <a href="salida.jsp" class="menu"  onclick="javascript:salida=false;">[Salir]</a> </p>
        <h1 class="center">
			Creaci&oacute;n del Ejecutable.
        </h1>
        <div id="cuerpo">
            <form method="post" name="formTest" enctype="multipart/form-data" action="guardarEjecutable.jsp" onsubmit="return comprobar();">
                <p> C&oacute;digo:</p>
                <p> <TEXTAREA NAME="CodigoFichero" ROWS=10 COLS=80></TEXTAREA>
                </p>
                <p> <input type="file" name="LeerFichero" size="30">
                </p>
                <p><input type="hidden" name="operacion" value="crear">
                    <input type="submit" name="seleccionar" value="Guardar Ejecutable" onclick="javascript:salida=false;"></p>
            </form>
        </div>
    </body>
</html>

