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
        <title>Copiar Test</title>
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
                if((document.formTest.NombreFichero.value=="")){
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
        <%


            String NombreFicheroACopiar = request.getParameter("NombreFicheroACopiar");
            String CodigoFicheroACopiar = request.getParameter("CodigoFicheroACopiar");


        %>
        <p class="derecha" > <a href="mostrarPracticas.jsp" class="menu"  onclick="javascript:salida=false;">[Listado de Practicas]</a> |
            <a href="modificarPractica.jsp" class="menu" onclick="javascript:salida=false;"> [Practica] |
                <a href="modificarTest.jsp" class="menu" onclick="javascript:salida=false;"> [Test] |
                    <a href="salida.jsp" class="menu"  onclick="javascript:salida=false;">[Salir]</a> </p>
        <h1 class="center"  class="color">
			Copiar Fichero Propio.
        </h1>

        <div id="cuerpo">
                    <div id="cuerpo">
                        <form method="post" name="formTest" action="modificarTest.jsp" onsubmit="return comprobar();">
                            <p> Nuevo nombre del Fichero: <input  type="text" name="NombreFichero" value="<%= NombreFicheroACopiar%>"> </p>
                            <input type="hidden" value="<%= CodigoFicheroACopiar%>" name="CodigoFicheroACopiar">
                            <input type="hidden" name="operacion" value="copiarFicheroPropio">
                            <input type="submit" name="Copiar" value="Copiar Fichero" onclick="javascript:salida=false;">
                        </form>
                    </div>

        </div>

    </body>
</html>
