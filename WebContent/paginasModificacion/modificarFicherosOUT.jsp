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
                if (document.formTest.LeerFichero.value=="" && document.formTest.ContenidoFichero.value==""){
                    alert("El Fichero debe tener algun contenido")
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
            <a href="modificarPractica.jsp" class="menu" onclick="javascript:salida=false;"> [Practica]</a> |
            <a href="modificarTest.jsp" class="menu" onclick="javascript:salida=false;"> [Test]</a> |
            <a href="modificarCasos.jsp" class="menu" onclick="javascript:salida=false;"> [Caso] </a> |
            <a href="salida.jsp" class="menu"  onclick="javascript:salida=false;">[Salir]</a> </p>
        <h1 class="center">
			Modificaci&oacute;n del Fichero OUT.
        </h1>



        <%
            String nombre = request.getParameter("NombreFichero");
            String codigo = request.getParameter("ContenidoFichero");
        %>
        <div id="cuerpo">
            <form method="post" name="formTest" enctype="multipart/form-data" action="guardarFicherosOUT.jsp" onsubmit="return comprobar();">
                <h2> <%= nombre%> </h2>
                <p> C&oacute;digo: </p>
                <p> <TEXTAREA NAME="ContenidoFichero" ROWS=3 COLS=50><%= codigo%></TEXTAREA>
                </p>
                <p> <input type="file" name="LeerFichero" size="30">
                </p>
                <p><input  type="hidden" value="<%= codigo%>" name="CodigoAntiguo">
                <input  type="hidden" value="<%= nombre%>" name="NombreFichero">
                <input type="hidden" value="guardar" name="operacion">
                <input type="submit" name="seleccionar" value="Guardar Fichero" onclick="javascript:salida=false;"></p>
            </form>
        </div>
    </body>
</html>
