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
			Modificaci&oacute;n del Caso.
        </h1>


        <%
            boolean autenticado = true;
            String operacion = request.getParameter("operacion");
            if (operacion != null) {
                if (operacion.equalsIgnoreCase("crear")) {
                    if (request.getParameter("NombreCaso") != null) {
                        Testigo resultado = new Testigo();
                        resultado.setOperacion(Testigo.Operaciones.crearCaso);
                        resultado.setParametro((HttpServletRequest) request);
                        interfazGestor.sendTestigo(resultado);
                        autenticado = resultado.isResultadoB();
                    }
                } else {
                    Testigo resultado = new Testigo();
                    if (operacion.equalsIgnoreCase("eliminarFicheroIN")) {
                        resultado.setOperacion(Testigo.Operaciones.eliminarFicheroIN);
                    } else if (operacion.equalsIgnoreCase("eliminarFicheroOUT")) {
                        resultado.setOperacion(Testigo.Operaciones.eliminarFicheroOUT);
                    } else if (operacion.equalsIgnoreCase("copiarFicheroIN")) {
                        resultado.setOperacion(Testigo.Operaciones.copiarFicherosIN);
                    } else if (operacion.equalsIgnoreCase("copiarFicheroOUT")) {
                        resultado.setOperacion(Testigo.Operaciones.copiarFicherosOUT);
                    }
                    resultado.setParametro((HttpServletRequest) request);
                    interfazGestor.sendTestigo(resultado);
                    autenticado = resultado.isResultadoB();
                }
            }

        %>

        <%
            if (autenticado) {
        %>

        <%

    String nombre = request.getParameter("NombreCaso");
    if (nombre == null) {
        Testigo resultado = new Testigo();
        resultado.setOperacion(Testigo.Operaciones.ultimoCaso);

        interfazGestor.sendTestigo(resultado);

        while (!resultado.isRelleno()) {
        }

        Caso ca = (Caso) resultado.getResultado();
        nombre = ca.getId();

    }

    Testigo resultado2 = new Testigo();
    resultado2.setOperacion(Testigo.Operaciones.pedirFicherosIN);
    resultado2.setParametro((HttpServletRequest) request);


    interfazGestor.sendTestigo(resultado2);

    while (!resultado2.isRelleno()) {
    }

    FicheroIN[] fis = (FicheroIN[]) resultado2.getResultado();

    Testigo resultado3 = new Testigo();
    resultado3.setOperacion(Testigo.Operaciones.pedirFicherosOUT);
    resultado3.setParametro((HttpServletRequest) request);


    interfazGestor.sendTestigo(resultado3);

    while (!resultado3.isRelleno()) {
    }

    FicheroOUT[] fos = (FicheroOUT[]) resultado3.getResultado();



        %>
        <div id="cuerpo">
            <h2> <%= nombre%> </h2>
            <div id="enlaces">
                <div id="izquierda3">
                    <h3 class="miniTitulo"> FicherosIN </h3>
                    <table border="0">
                        <tbody>
                            <tr>
                                <td>
                                    <form method="post" name="formAnadir" action="crearFicheroIN.jsp" onsubmit="return valida();">
                                        <p> <input type="submit" name="seleccionar" value="A&ntilde;adir FicheroIN" onclick="javascript:salida=false;"> </p>
                                    </form>
                                </td>
                                <td>
                                    <form method="post" name="formAnadir" action="seleccionarFicherosIN.jsp" onsubmit="return valida();">
                                        <p> <input type="submit" name="seleccionar" value="A&ntilde;adir Copia de FicheroIN ya existente" onclick="javascript:salida=false;"> </p>
                                    </form> </td>
                            </tr>

                        </tbody>
                    </table>
                </div>

                <div id="derecha4">
                    <h3 class="miniTitulo"> FicherosOUT </h3>
                    <table border="0">
                        <tbody>
                            <tr>
                                <td>
                                    <form method="post" name="formAnadir" action="crearFicheroOUT.jsp" onsubmit="return valida();">
                                        <p><input type="submit" name="seleccionar" value="A&ntilde;adir FicheroOUT" onclick="javascript:salida=false;"></p>
                                    </form>
                                </td>
                                <td>
                                    <form method="post" name="formAnadir" action="seleccionarFicherosOUT.jsp" onsubmit="return valida();">
                                        <p><input type="submit" name="seleccionar" value="A&ntilde;adir Copia de FicheroOUT ya existente" onclick="javascript:salida=false;"></p>
                                    </form> </td>
                            </tr>

                        </tbody>
                    </table>
                </div>
            </div>
        </div>
        <div id="central">

            <div id="izquierda2">
                <% if (fis.length > 0) {%>
                <table border="4" cellspacing="4" cellpadding="4" width="380" class="center">
                    <tbody>

                        <% for (int i = 0; i < fis.length; i++) {%>

                        <tr>
                            <td> <%= fis[i].getNombre()%></td>
                            <td>
                                <form method="post" name="formVer" action="modificarFicherosIN.jsp" onsubmit="return valida();">
                                    <p class="tabla"><input  type="hidden" value="<%= fis[i].getNombre()%>" name="NombreFichero">
                                        <input  type="hidden" value="<%= fis[i].getContenido()%>" name="ContenidoFichero">
                                        <input type="submit" name="Ver" value="Ver" onclick="javascript:salida=false;"></p>
                                </form>
                            </td>
                            <td>
                                <form method="post" name="formEliminar" action="modificarCasos.jsp" onsubmit="return valida();">
                                    <p class="tabla"><input  type="hidden" value="<%= fis[i].getNombre()%>" name="NombreFichero">
                                        <input  type="hidden" value="<%= fis[i].getContenido()%>" name="ContenidoFichero">
                                        <input type="hidden" value="eliminarFicheroIN" name="operacion">
                                        <input type="submit" name="Eliminar" value="Eliminar" onclick="javascript:salida=false;"></p>
                                </form>
                            </td>
                        </tr>
                        <%
     }
                        %>
                    </tbody>
                </table>
                <%
}
                %>
            </div>

            <div id="derecha2">
                <% if (fos.length > 0) {%>
                <table border="4" cellspacing="4" cellpadding="4" width="380" class="center">
                    <tbody>

                        <% for (int i = 0; i < fos.length; i++) {%>

                        <tr>
                            <td> <%= fos[i].getNombre()%></td>
                            <td>
                                <form method="post" name="formVer" action="modificarFicherosOUT.jsp" onsubmit="return valida();">
                                    <p class="tabla"><input  type="hidden" value="<%= fos[i].getNombre()%>" name="NombreFichero">
                                        <input  type="hidden" value="<%= fos[i].getContenido()%>" name="ContenidoFichero">
                                        <input type="submit" name="Ver" value="Ver" onclick="javascript:salida=false;"></p>
                                </form>
                            </td>
                            <td>
                                <form method="post" name="formEliminar" action="modificarCasos.jsp" onsubmit="return valida();">
                                    <p class="tabla"><input  type="hidden" value="<%= fos[i].getNombre()%>" name="NombreFichero">
                                        <input  type="hidden" value="<%= fos[i].getContenido()%>" name="ContenidoFichero">
                                        <input type="hidden" value="eliminarFicheroOUT" name="operacion">
                                        <input type="submit" name="Eliminar" value="Eliminar" onclick="javascript:salida=false;"></p>
                                </form>
                            </td>
                        </tr>
                        <%
     }
                        %>
                    </tbody>
                </table>
                <%
}
                %>
            </div>
        </div>

        <% } else {
        %>

        <h2 class="error">
            ERROR!!! En la base de datos </h2>

        <p class="error">
            Ha ocurrido un problema en la base de datos.
        </p>



        <% }
        %>

    </body>
</html>