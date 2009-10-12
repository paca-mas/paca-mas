  <%@ page import="jade.core.*"%>
  <%@ page import="jade.core.Agent.*"%>
  <%@ page import="jade.core.Runtime"%>
  <%@ page import="jade.wrapper.*"%>
  <%@ page import="javax.servlet.jsp.*"%>
  <%@ page import="javax.servlet.*"%>
  <%@ page import="java.util.*"%>
  <%@ page import="es.urjc.ia.paca.util.*"%> 
  <%@ page import="es.urjc.ia.paca.ontology.pacaOntology"%>
  <%@ page import="es.urjc.ia.paca.agents.Interfaz"%>
  <%@ page import="es.urjc.ia.paca.agents.InterfazJSP"%>  
  
   
 
  <jsp:useBean id="fake" class="java.util.Random" scope="application">
  <%
     //String[] args = {"-container"}; 
     //jade.Boot.main(args);
     //System.out.println("Contenedor Jade Iniciado.");
     
  %>
  </jsp:useBean>


  <jsp:useBean id="interfaz" class="es.urjc.ia.paca.util.AgentBean" scope="session">

    <% 
  
    String nombre = "USER"+ session.getId();
    Runtime rt;
    Profile p;
    ContainerController cc;
    AgentController agentInterfaz=null;
    InterfazJSP agent=null;
            
	try {
			//interfaz.doStart(nombre);
			rt = Runtime.instance();
			p = new ProfileImpl(false);
			cc = rt.createAgentContainer(p);
			agent = new InterfazJSP();
			agentInterfaz = cc.acceptNewAgent(nombre, agent);
			if (agentInterfaz != null) {
				agentInterfaz.start();
				interfaz.setAgentInterfaz(agent);
				interfaz.setAgentController(agentInterfaz);
				while (!agent.isFinSetup()) {
					
				}
				
				
			} 
			else {
				System.out.println("Agente no Arrancado" + agentInterfaz.getState().getName());
			}
		} 
	catch (Exception ex) {
			out.println(ex);
			ex.printStackTrace();
	}
  
    %>    
  </jsp:useBean>


<!doctype html public "-//w3c//dtd html 4.0 transitional//en">
<html>
  
  <head>
    <title>
      Practicas Disponibles.
    </title>
    <LINK REL=STYLESHEET TYPE="text/css" HREF="http://platon.escet.urjc.es/estilos/estiloPACA.css">
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

<%@ include file="barra0.html"%>

  <%
  
  	boolean autenticado = false;
  
	Testigo resultado=new Testigo();
  	resultado.setOperacion(Testigo.Operaciones.autenticar);
  	resultado.setParametro((HttpServletRequest) request);
  	 
  	interfaz.sendTestigo(resultado);
	
  	autenticado = resultado.isResultadoB();
  	 	
  	%>
  	
  	
  	<%
  
  if (!autenticado)
	{
  %>
 
  <h2 class="error">
    <p align="center">ERROR!!! Usuario no autenticado  </p> </h2>
<br>
  <p class="error" align="center">
    El usuario "<%= request.getParameter("user_id") %>" no ha podido ser validado. 
	Por favor, utilice su nombre de usuario y password personal o 
	revise su nombre de usuario y contraseña.
  </p>
	<br>
	<br>
  <p align="center">
	<a href="javascript:history.go(-1)" onclick="javascript:salida=false;">Volver a la pagina de autenticaci&oacute;n.</a>
  </p>
<%
  }
  else {
      
%>

<br><br><br>
  <p align="center" class="color">
     Seleccione la práctica a evaluar.  </p><br>

<% 
    // Aquí hacemos la petición de las prácticas disponibles y las
    // mostramos en un formulario
	
    Testigo resultado3=new Testigo();
	resultado3.setOperacion(Testigo.Operaciones.buscarCorrector);

	interfaz.sendTestigo(resultado3);
	
	while(!resultado3.isRelleno()){
	}

	
	
	//-----------------------------------------------------------------------

	Testigo resultado2=new Testigo();
	resultado2.setOperacion(Testigo.Operaciones.pedirPracticas);
		
	interfaz.sendTestigo(resultado2);
	
	
	String [] pract = (String [])resultado2.getResultado();
		
	
	




%>
  <p  align="center">
    <form method="post" name="formpracticas" action="peticionTest.jsp" onsubmit="desactivarBoton();">
<div class="form"><BR>
    <table width="100%" border="0" cellspacing="0" cellpadding="0" class="color" align="center">
    <tr>

    <td width="20%">
    &nbsp;
    </td>
    <td width="60%" align="center">
    <SELECT size=1 cols=<%= pract.length %> NAME=practica>
<%
   // Rellenamos todas las opciones
    
   for (int i=0; i < pract.length; i++) {
%>
	    <OPTION value=<%= pract[i] %>> <%= pract[i] %>
<%
   }
%>

        </SELECT>
    </td>
    <td width="20%">
    &nbsp;
    </td>
   </tr>
   </table>
<BR>
</div>
<BR><BR><BR>
<%
if (pract.length!=0){
%>
	<p align="right"><input type="submit" name="seleccionar" value="Seleccionar" onclick="javascript:salida=false;"></p>
        <%
}
%>
    </form>


<%
   }
%>


</body>
</html>



