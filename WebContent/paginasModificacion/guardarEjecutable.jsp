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
<%@page import="com.oreilly.servlet.multipart.FilePart" %>
<%@page import="com.oreilly.servlet.multipart.MultipartParser" %>
<%@page import="com.oreilly.servlet.multipart.ParamPart" %>
<%@page import="com.oreilly.servlet.multipart.Part" %>
<%@page import="java.io.InputStream" %>
<%@page import="java.io.StringWriter" %>

<jsp:useBean id="interfazGestor" class="es.urjc.ia.paca.util.AgentBeanGestor" scope="session"/>


<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01//EN" "http://www.w3.org/TR/html4/strict.dtd">

<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Modificaci&oacute;n del Ejecutable</title>
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


            //-->
        </SCRIPT>
    </head>
    <body onUnload="exit();">


        <p class="derecha" > <a href="mostrarPracticas.jsp" class="menu"  onclick="javascript:salida=false;">[Listado de Practicas]</a> |
            <a href="modificarPractica.jsp" class="menu" onclick="javascript:salida=false;"> [Practica] </a>|
            <a href="modificarTest.jsp" class="menu" onclick="javascript:salida=false;"> [Test] </a>|
            <a href="salida.jsp" class="menu"  onclick="javascript:salida=false;">[Salir]</a> </p>
        <h1 class="center">
			Modificaci&oacute;n del Ejecutable.
        </h1>


        <%

            boolean autenticado = false;

            HttpServletRequest param1 = (HttpServletRequest) request;
            MultipartParser parser = new MultipartParser(param1, 1000000);
// Empezamos a leer.
            Part parte = parser.readNextPart();
            String codigo = "";
            while (parte != null) {
                if (parte.isFile()) {
                    //Es un fichero.

                    FilePart filepart = (FilePart) parte;
                    InputStream is = filepart.getInputStream();
                    StringWriter sw = new StringWriter();
                    int tempo = is.read();
                    while (tempo != -1) {
                        sw.write(tempo);
                        tempo = is.read();
                    }
                    if (!(sw.toString().equalsIgnoreCase(""))) {

                        codigo = sw.toString();

                    }
                    sw.close();
                }
                if (parte.isParam()) {

                    if (parte.getName().equals("CodigoFichero")) {
                        if (codigo.equalsIgnoreCase("")) {

                            ParamPart parampart = (ParamPart) parte;
                            codigo = parampart.getStringValue();

                        }
                    }
                }
                parte = parser.readNextPart();

            }

            if (!codigo.equalsIgnoreCase("")) {

                Testigo resultado = new Testigo();
                resultado.setOperacion(Testigo.Operaciones.modificarEjecutable);
                resultado.setParametro(codigo);

                interfazGestor.sendTestigo(resultado);


                autenticado = resultado.isResultadoB();
            } else {
                autenticado = true;
            }

        %>

        <%
            if (autenticado) {

        %>

        <div id="cuerpo">
            <form method="post" name="formTest" enctype="multipart/form-data" action="guardarEjecutable.jsp" onsubmit="desactivarBoton();">
                <p> C&oacute;digo:</p>
                <p> <TEXTAREA NAME="CodigoFichero" ROWS=10 COLS=80><%= codigo%></TEXTAREA>
                </p>
                <p> <input type="file" name="LeerFichero" size="30">
                </p>
                <p> <input type="submit" name="seleccionar" value="Guardar Ejecutable" onclick="javascript:salida=false;"> </p>
            </form>
        </div>

        <% } else {
        %>

        <h2 class="error">
            ERROR!!! En la base de datos </h2>

        <p class="error">
            Ha ocurrido un problema en la base de datos al intentar crear el Ejecutable.
        </p>



        <% }
        %>

    </body>
</html>

