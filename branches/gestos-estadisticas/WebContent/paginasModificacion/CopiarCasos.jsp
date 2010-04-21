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
        <title>Copiar Casos</title>
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
                if((document.formTest.NombreCaso.value=="")){
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


            String NombreCasoACopiar = request.getParameter("NombreCasoACopiar");
            String NombreTestACopiar = request.getParameter("NombreTestACopiar");
            String NombrePracticaACopiar = request.getParameter("NombrePracticaACopiar");


        %>
        <p class="derecha" >
            <a class="validadorhtml" href="http://validator.w3.org/check?uri=referer"><img
                    style="border:0;width:88px;height:31px" src="http://www.w3.org/Icons/valid-html401-blue"
                    alt="Valid HTML 4.01 Strict" height="31" width="88"></a>
            <a class="validadorcss"href="http://jigsaw.w3.org/css-validator/check/referer">
                <img style="border:0;width:88px;height:31px"
                     src="http://jigsaw.w3.org/css-validator/images/vcss-blue"
                     alt="�CSS V�lido!" >
            </a>
            <a href="mostrarPracticas.jsp" class="menu"  onclick="javascript:salida=false;">[Listado de Practicas]</a> |
            <a href="modificarPractica.jsp" class="menu" onclick="javascript:salida=false;"> [Practica] </a>|
                <a href="modificarTest.jsp" class="menu" onclick="javascript:salida=false;"> [Test] </a>|
                    <a href="salida.jsp" class="menu"  onclick="javascript:salida=false;">[Salir]</a> </p>
        <h1 class="center">
			Copiar Caso.
        </h1>

        
                    <div id="cuerpo">
                        <form method="post" name="formTest" action="modificarTest.jsp" onsubmit="return comprobar();">
                            <p> Nuevo nombre del Caso: <input  type="text" name="NombreCaso" value="<%= NombreCasoACopiar%>"> </p>
                            <p> <input type="hidden" name="NombrePracticaACopiar" value="<%= NombrePracticaACopiar%>">
                            <input type="hidden" name="NombreTestACopiar" value="<%= NombreTestACopiar%>">
                            <input type="hidden" name="NombreCasoACopiar" value="<%= NombreCasoACopiar%>">
                            <input type="hidden" name="operacion" value="copiarCaso">
                            <input type="submit" name="Copiar" value="Copiar Caso" onclick="javascript:salida=false;"> </p>
                        </form>
                    </div>

    </body>
</html>
