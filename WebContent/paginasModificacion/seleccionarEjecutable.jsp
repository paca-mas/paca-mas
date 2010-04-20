<%@ page import="jade.core.*"%>
<%@ page import="jade.core.Agent.*"%>
<%@ page import="jade.core.Runtime"%>
<%@ page import="jade.wrapper.*"%>
<%@ page import="javax.servlet.jsp.*"%>
<%@ page import="javax.servlet.*"%>
<%@ page import="java.util.*"%>
<%@ page import="es.urjc.ia.paca.util.*"%>
<%@ page import="es.urjc.ia.paca.ontology.pacaOntology"%>
<%@ page import="es.urjc.ia.paca.ontology.Practica"%>
<%@ page import="es.urjc.ia.paca.ontology.Test"%>
<%@ page import="es.urjc.ia.paca.agents.InterfazGestor"%>
<%@ page import="es.urjc.ia.paca.agents.InterfazJSPGestor"%>



<jsp:useBean id="interfazGestor" class="es.urjc.ia.paca.util.AgentBeanGestor" scope="session"/>


<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01//EN" "http://www.w3.org/TR/html4/strict.dtd">
<html>

    <head>
        <title>
            Seleccionar Ejecutable.
        </title>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <LINK REL=STYLESHEET TYPE="text/css" HREF="estilos/estiloInterfazGestor.css">
        <SCRIPT TYPE="text/javascript">
            <!--

            salida = true;

            function exit()
            {
                if (salida)
                    window.open("salida.jsp",'frame','width=580,height=480,scrollbars=yes,resizable=yes')
                return true;
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
            <a href="salida.jsp" class="menu"  onclick="javascript:salida=false;">[Salir]</a> </p>

        <h1 class="center">
            Selecci&oacute;n del Ejecutable.  </h1>



        <%

            Testigo resultado2 = new Testigo();
            resultado2.setOperacion(Testigo.Operaciones.pedirPracticas);

            interfazGestor.sendTestigo(resultado2);

            while (!resultado2.isRelleno()) {
            }

            Practica[] pract = (Practica[]) resultado2.getResultado();


        %>
        <div id="cuerpo2">


        <%
            for (int i = 0; i < pract.length; i++) {

                Testigo resultado = new Testigo();
                resultado.setOperacion(Testigo.Operaciones.seleccionarTest);
                resultado.setParametro(pract[i].getId());


                interfazGestor.sendTestigo(resultado);

                while (!resultado.isRelleno()) {
                }

                Test[] tests = (Test[]) resultado.getResultado();

        %>

        
            <h3> <%= pract[i].getId()%> </h3>
            
                <% for (int z = 0; z < tests.length; z++) {
                    if (!tests[z].getEjecutable().equalsIgnoreCase("")) {

                %>

                <table border="0" class="seleccionTest">

                    <tbody>
                        <tr>
                    
                        <td> <%= tests[z].getId()%>  </td>
                        <td>
                        <form class="center" method="post" name="formVer" action="modificarTest.jsp">
                            <p class="tabla"> <input type="hidden" value="<%= tests[z].getEjecutable()%>" name="EjecutableTest">
                            <input type="hidden" value="copiarEjecutable" name="operacion">
                            <input type="submit" name="Seleccionar" value="Copiar" onclick="javascript:salida=false;"> </p>
                        </form>
                        </td>
                    

                    </tr>

                    </tbody>
                </table>
                <%
                    }
                }
                %>
            
        

        <% }
        %>
        </div>
    </body>
</html>
