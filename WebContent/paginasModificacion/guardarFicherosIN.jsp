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


            //-->
        </SCRIPT>
    </head>
    <body onUnload="exit();">


        <p class="derecha" > <a href="salida.jsp" class="menu"  onclick="javascript:salida=false;">[Salir]</a> </p>
        <h1 class="center"  class="color">
			Modificaci&oacute;n del FicheroIN.
        </h1>


        <%

            boolean autenticado = false;

            HttpServletRequest param1 = (HttpServletRequest) request;
            MultipartParser parser = new MultipartParser(param1, 1000000);
            // Empezamos a leer.
            Part parte = parser.readNextPart();
            String nombre = "";
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
                    if(!(sw.toString().equalsIgnoreCase(""))){

                        codigo = sw.toString();

                    }
                    sw.close();
                }
                if (parte.isParam()) {

                    if (parte.getName().equals("NombreFichero")) {
                        ParamPart parampart = (ParamPart) parte;
                        nombre = parampart.getStringValue();
                    } else if (parte.getName().equals("ContenidoFichero")) {
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
                resultado.setOperacion(Testigo.Operaciones.modificarFicherosIN);
                resultado.setParametro(nombre + "#" + codigo);

                interfazGestor.sendTestigo(resultado);


                autenticado = resultado.isResultadoB();
            }
            else{
                autenticado = true;
            }

        %>

        <%
            if (autenticado) {

        %>

        <div id="cuerpo">
            <form method="post" name="formTest" enctype="multipart/form-data" action="guardarFicherosIN.jsp" onsubmit="desactivarBoton();">
                <h2> <%= nombre%> </h2>
                <p> C&oacute;digo: <TEXTAREA NAME="ContenidoFichero" ROWS=3 COLS=40><%= codigo%></TEXTAREA>
                </p>
                <p> <input type="file" name="LeerFichero" size="30">
                </p>
                <input  type="hidden" value="<%= codigo%>" name="ContenidoAntiguo">
                <input  type="hidden" value="<%= nombre%>" name="NombreFichero">
                <input type="submit" name="seleccionar" value="Guardar Fichero" onclick="javascript:salida=false;">
            </form>
        </div>

        <% } else {
        %>

        <h2 class="error" align="center">
            ERROR!!! En la base de datos </h2>
        <br>
        <p class="error" align="center">
            Ha ocurrido un problema en la base de datos al intentar modificar el FicheroPropio.
        </p>
        <br>
        <br>


        <% }
        %>

    </body>
</html>