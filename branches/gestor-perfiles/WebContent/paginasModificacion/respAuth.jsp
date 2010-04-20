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
<%@ page import="es.urjc.ia.paca.agents.InterfazGestor"%>
<%@ page import="es.urjc.ia.paca.agents.InterfazJSPGestor"%>



<jsp:useBean id="fake" class="java.util.Random" scope="application">
    <%
            //String[] args = {"-container"};
            //jade.Boot.main(args);
            //System.out.println("Contenedor Jade Iniciado.");

    %>
</jsp:useBean>


<jsp:useBean id="interfazGestor" class="es.urjc.ia.paca.util.AgentBeanGestor" scope="session">

    <%

            String nombre = "USER" + session.getId();
            Runtime rt;
            Profile p;
            ContainerController cc;
            AgentController agentInterfaz = null;
            InterfazJSPGestor agent = null;

            try {
                //interfaz.doStart(nombre);
                rt = Runtime.instance();
                p = new ProfileImpl(false);
                cc = rt.createAgentContainer(p);
                agent = new InterfazJSPGestor();
                agentInterfaz = cc.acceptNewAgent(nombre, agent);
                if (agentInterfaz != null) {
                    agentInterfaz.start();
                    interfazGestor.setAgentInterfaz(agent);
                    interfazGestor.setAgentController(agentInterfaz);
                    while (!agent.isFinSetup()) {
                    }


                } else {
                    System.out.println("Agente no Arrancado" + agentInterfaz.getState().getName());
                }
            } catch (Exception ex) {
                out.println(ex);
                ex.printStackTrace();
            }

    %>
</jsp:useBean>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01//EN" "http://www.w3.org/TR/html4/strict.dtd">
<html>

    <head>
        <title>
            Practicas Disponibles.
        </title>
        <meta content="text/html; charset=ISO-8859-1" http-equiv="content-type">
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


            function desactivarBoton() {
                document.formpracticas.seleccionar.disabled=true;
            }

            //-->
        </SCRIPT>
    </head>

    <body onUnload="exit();">
        <p> <a class="validadorhtml" href="http://validator.w3.org/check?uri=referer"><img
                    style="border:0;width:88px;height:31px" src="http://www.w3.org/Icons/valid-html401-blue"
                    alt="Valid HTML 4.01 Strict" height="31" width="88"></a>
            <a class="validadorcss"href="http://jigsaw.w3.org/css-validator/check/referer">
                <img style="border:0;width:88px;height:31px"
                     src="http://jigsaw.w3.org/css-validator/images/vcss-blue"
                     alt="¡CSS Válido!" >
            </a>
        </p>

        <%

            boolean autenticado = false;

            Testigo resultado = new Testigo();
            resultado.setOperacion(Testigo.Operaciones.autenticar);
            resultado.setParametro((HttpServletRequest) request);

            interfazGestor.sendTestigo(resultado);

            autenticado = resultado.isResultadoB();

        %>


        <%

            if (!autenticado) {
        %>

        <h2 class="error">
            align="center">ERROR!!! Usuario no autenticado  </h2>
        <p class="error">
            El usuario "<%= request.getParameter("user_id")%>" no ha podido ser validado.
	Por favor, utilice su nombre de usuario y password personal o
	revise su nombre de usuario y contraseña.
        </p>

        <p>
            <a href="javascript:history.go(-1)" onclick="javascript:salida=false;">Volver a la pagina de autenticaci&oacute;n.</a>
        </p>
        <%
            } else {

        %>



        <%

                Testigo resultado2 = new Testigo();
                resultado2.setOperacion(Testigo.Operaciones.pedirPracticas);

                interfazGestor.sendTestigo(resultado2);

                while (!resultado2.isRelleno()) {
                }

                Practica[] pract = (Practica[]) resultado2.getResultado();


        %>

        <p class="derecha" > <a href="salida.jsp" class="menu"  onclick="javascript:salida=false;">[Salir]</a> </p>

        <h1 class="center">
            Listado de pr&aacute;cticas.  </h1>
        <div id="cuerpo">
            <div id="central2">
                <div id="parrafo">
                <table border="6" cellspacing="6" cellpadding="6" width="380" class="center">
                    <tbody>
                        
                        <% for (int i = 0; i < pract.length; i++) {%>

                        <tr>
                            <td> <p class="tabla"> <%= pract[i].getId()%> </p> </td>
                            <td>
                                <form method="post" name="formpracticas" action="modificarPractica.jsp" onsubmit="desactivarBoton();">
                                    <p class="tabla"> <input  type="hidden" value="<%= pract[i].getId()%>" name="NombrePractica">
                                    <input  type="hidden" value="<%= pract[i].getDescripcion()%>" name="DescripcionPractica">
                                    <input  type="hidden" value="<%= pract[i].getFechaEntrega()%>" name="FechaPractica">
                                    <input type="submit" name="Ver" value="Ver" onclick="javascript:salida=false;"> </p>
                                </form>
                            </td>
                            <td>
                                <form method="post" name="formpracticas" action="mostrarPracticas.jsp" onsubmit="desactivarBoton();">
                                    <p class="tabla"> <input  type="hidden" value="<%= pract[i].getId()%>" name="NombrePractica">
                                    <input  type="hidden" value="<%= pract[i].getDescripcion()%>" name="DescripcionPractica">
                                    <input  type="hidden" value="<%= pract[i].getFechaEntrega()%>" name="FechaPractica">
                                    <input type="hidden" value="EliminarPractica" name="operacion">
                                    <input type="submit" name="Eliminar" value="Eliminar" onclick="javascript:salida=false;"> </p>
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
            <form method="post" name="formpracticas" action="crearPractica.jsp" onclick="javascript:salida=false;">
                <p class="center">  <input type="submit" name="Crear" value="Crear Nueva Practica"> </p>
            </form>

        </div>

        <%
            }
        %>


    </body>
</html>

