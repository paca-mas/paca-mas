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

            function valida(){
                if(!(document.formTest.DescripcionTest.value==document.formTest.DescripcionAntigua.value)){
                    alert("Debe guardar la practica antes de continuar")
                    return false
                }
                else{
                    document.formTest.seleccionar.disabled=true;
                    document.formAnadir.seleccionar.disabled=true;
                    document.formEliminar.Eliminar.disabled=true;
                    document.formSeleccionar.seleccionar.disabled=true;
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


        <p class="derecha" > <a href="mostrarPracticas.jsp" class="menu"  onclick="javascript:salida=false;">[Listado de Practicas]</a> |
            <a href="modificarPractica.jsp" class="menu" onclick="javascript:salida=false;"> [Practica] |
                <a href="salida.jsp" class="menu"  onclick="javascript:salida=false;">[Salir]</a> </p>
        <h1 class="center"  class="color">
			Modificaci&oacute;n del test.
        </h1>


        <%

 boolean autenticado = false;

 Testigo resultado = new Testigo();
 if (request.getParameter("operacion").equalsIgnoreCase("crear")) {
     resultado.setOperacion(Testigo.Operaciones.crearTest);
 } else {
     resultado.setOperacion(Testigo.Operaciones.modificarTest);
 }
 resultado.setParametro((HttpServletRequest) request);

 interfazGestor.sendTestigo(resultado);


 autenticado = resultado.isResultadoB();

        %>

        <%
        if (autenticado) {

        %>



        <%

            String nombre = request.getParameter("NombreTest");
            String descripcion = request.getParameter("DescripcionTest");

            Testigo resultado2 = new Testigo();
            resultado2.setOperacion(Testigo.Operaciones.pedirFicherosPropios);
            resultado2.setParametro((HttpServletRequest) request);

//interfaz.getAtributo().putO2AObject(resultado2,AgentController.SYNC);
            interfazGestor.sendTestigo(resultado2);

            while (!resultado2.isRelleno()) {
            }

            FicheroPropio[] fps = (FicheroPropio[]) resultado2.getResultado();

            Testigo resultado3 = new Testigo();
            resultado3.setOperacion(Testigo.Operaciones.pedirCasos);
            resultado3.setParametro((HttpServletRequest) request);

//interfaz.getAtributo().putO2AObject(resultado2,AgentController.SYNC);
            interfazGestor.sendTestigo(resultado3);

            while (!resultado3.isRelleno()) {
            }

            Caso[] cas = (Caso[]) resultado3.getResultado();

            Testigo resultado4 = new Testigo();
            resultado4.setOperacion(Testigo.Operaciones.pedirFicherosAlumno);
            resultado4.setParametro((HttpServletRequest) request);

//interfaz.getAtributo().putO2AObject(resultado2,AgentController.SYNC);
            interfazGestor.sendTestigo(resultado4);

            while (!resultado4.isRelleno()) {
            }

            FicheroAlumno[] fas = (FicheroAlumno[]) resultado4.getResultado();


        %>
        <div id="cuerpo">
            <form method="post" name="formTest" action="guardarTest.jsp" onsubmit="desactivarBoton();">
                <h2> <%= nombre%> </h2>
                <p> Descripci&oacute;n: <input type="text" name="DescripcionTest" size="25" value="<%= descripcion%>">
                </p>
                <input  type="hidden" value="<%= descripcion%>" name="DescripcionAntigua">
                <input  type="hidden" value="<%= nombre%>" name="NombreTest">
                <input type="hidden" value="modificar" name="operacion">
                <input type="submit" name="seleccionar" value="Guardar Test" onclick="javascript:salida=false;">
            </form>
            <div id="enlaces">
                <div id="izquierda">
                    <table border="0">
                        <tbody>
                            <tr>
                                <td>
                                    <form method="post" name="formAnadir" action="crearFicheroPropio.jsp" onsubmit="return valida();">
                                        <input type="submit" name="seleccionar" value="A&ntilde;adir FicheroPropio" onclick="javascript:salida=false;">
                                    </form>
                                </td>
                                <td>
                                    <form method="post" name="formSeleccionar" action="peticionTest2.jsp" onsubmit="return valida();">
                                        <input type="submit" name="seleccionar" value="Seleccionar FicheroPropio" onclick="javascript:salida=false;">
                                    </form> </td>
                            </tr>

                        </tbody>
                    </table>
                </div>

                <div id="centro">
                    <table border="0">
                        <tbody>
                            <tr>
                                <td>
                                    <form method="post" name="formAnadir" action="peticionTest2.jsp" onsubmit="return valida();">
                                        <input type="submit" name="seleccionar" value="A&ntilde;adir Caso" onclick="javascript:salida=false;">
                                    </form>
                                </td>
                                <td>
                                    <form method="post" name="formSeleccionar" action="peticionTest2.jsp" onsubmit="return valida();">
                                        <input type="submit" name="seleccionar" value="Seleccionar Caso" onclick="javascript:salida=false;">
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
                                        <input type="submit" name="seleccionar" value="A&ntilde;adir FicheroAlumno" onclick="javascript:salida=false;">
                                    </form>
                                </td>
                                <td>
                                    <form method="post" name="formSeleccionar" action="peticionTest2.jsp" onsubmit="return valida();">
                                        <input type="submit" name="seleccionar" value="Seleccionar FicheroAlumno" onclick="javascript:salida=false;">
                                    </form> </td>
                            </tr>

                        </tbody>
                    </table>
                </div>
            </div>
        </div>
        <div id="central">
            <div id="izquierda2">
                <table border="4" cellspacing="4" cellpadding="4" width="380" class="center">
                    <tbody>

                        <% for (int i = 0; i < fps.length; i++) {%>

                        <tr>
                            <td> <%= fps[i].getNombre()%></td>
                            <td>
                                <form method="post" name="formVer" action="modificarFicherosPropios.jsp" onsubmit="return valida();">
                                    <input  type="hidden" value=<%= fps[i].getNombre()%> name="NombreFichero">
                                    <input  type="hidden" value=<%= fps[i].getCodigo()%> name="CodigoFichero">
                                    <input type="submit" name="Ver" value="Ver" onclick="javascript:salida=false;">
                                </form>
                            </td>
                            <td>
                                <form method="post" name="formEliminar" action="eliminarTest.jsp" onsubmit="return valida();">
                                    <input  type="hidden" value=<%= fps[i].getNombre()%> name="NombrePractica">
                                    <input type="submit" name="seleccionar" value="Eliminar" onclick="javascript:salida=false;">
                                </form>
                            </td>
                        </tr>
                        <%
            }
                        %>
                    </tbody>
                </table>
            </div>

            <div id="centro2">
                <table border="4" cellspacing="4" cellpadding="4" width="380" class="center">
                    <tbody>

                        <% for (int i = 0; i < cas.length; i++) {%>

                        <tr>
                            <td> <%= cas[i].getId()%></td>
                            <td>
                                <form method="post" name="formVer" action="modificarCasos.jsp" onsubmit="return valida();">
                                    <input  type="hidden" value=<%= cas[i].getId()%> name="NombreCaso">
                                    <input type="submit" name="Ver" value="Ver" onclick="javascript:salida=false;">
                                </form>
                            </td>
                            <td>
                                <form method="post" name="formEliminar" action="eliminarTest.jsp" onsubmit="return valida();">
                                    <input  type="hidden" value=<%= cas[i].getId()%> name="NombreCaso">
                                    <input type="submit" name="seleccionar" value="Eliminar" onclick="javascript:salida=false;">
                                </form>
                            </td>
                        </tr>
                        <%
            }
                        %>
                    </tbody>
                </table>
            </div>

            <div id="derecha2">
                <table border="4" cellspacing="4" cellpadding="4" width="380" class="center">
                    <tbody>

                        <% for (int i = 0; i < fas.length; i++) {%>

                        <tr>
                            <td> <%= fas[i].getNombre()%></td>
                            <td>
                                <form method="post" name="formEliminar" action="eliminarTest.jsp" onsubmit="return valida();">
                                    <input  type="hidden" value=<%= fas[i].getNombre()%> name="NombreFichero">
                                    <input type="submit" name="seleccionar" value="Eliminar" onclick="javascript:salida=false;">
                                </form>
                            </td>
                        </tr>
                        <%
            }
                        %>
                    </tbody>
                </table>
            </div>
        </div>
            <% } else {
    %>

    <h2 class="error" align="center">
        ERROR!!! En la base de datos </h2>
    <br>
    <p class="error" align="center">
        Ha ocurrido un problema en la base de datos al intentar crear la practica.
        Revise el nombre de la practica.
    </p>
    <br>
    <br>


    <% }
    %>
    </body>
</html>