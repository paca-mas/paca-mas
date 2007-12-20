  <%@ page import="jade.core.*"%>
  <%@ page import="jade.core.Agent.*"%>
  <%@ page import="jade.core.Runtime"%>
  <%@ page import="jade.wrapper.*"%>
  <%@ page import="javax.servlet.jsp.*"%>
  <%@ page import="javax.servlet.*"%>
  <%@ page import="java.util.*"%>
  <%@ page import="auth.util.*"%> 
  
  
   
 
  <jsp:useBean id="fake" class="java.util.Random" scope="application">
  <%
     //String[] args = {"-container"}; 
     //jade.Boot.main(args);
     //System.out.println("Contenedor Jade Iniciado.");
     
  %>
  </jsp:useBean>


  <jsp:useBean id="interfaz" class="auth.util.Niapa" scope="session">

    <% 
  
    String nombre = "USER"+ session.getId();
    Runtime rt;
    Profile p;
    ContainerController cc;
    AgentController agentInterfaz=null;
            
    try {
    	//interfaz.doStart(nombre);
    	rt = Runtime.instance();
    	p = new ProfileImpl(false); 
    	cc = rt.createAgentContainer(p);
    	agentInterfaz = cc.createNewAgent(nombre,"PACA.agents.InterfazJSP", new Object[0]);
    	if (agentInterfaz!=null){
    		agentInterfaz.start();
    		System.out.println("Nombre Agente: "+agentInterfaz.getName());
    		System.out.println("Agente Arrancado: "+agentInterfaz.getState().getName());
    		interfaz.setAtributo(agentInterfaz);
    		//interfaz.setAgent(null);
    		//agentInterfaz.putO2AObject(interfaz,AgentController.SYNC);
    		System.out.println("Ponemos algo en agente... ");
    		System.out.println("INTERFAZ: "+interfaz.toString());
    		System.out.println("AGENTE INTERFAZ: "+agentInterfaz.toString());
    		    	}
    	else{
    		System.out.println("Agente no Arrancado"+agentInterfaz.getState().getName());
    	}
    }
    
    catch (Exception ex) {
       out.println(ex);
       System.out.println("Excepcion en respAuth.jsp");
       ex.printStackTrace();
       System.out.println("Fin Excepcion en respAuth.jsp");
    }
 
    %>    
  </jsp:useBean>


<!doctype html public "-//w3c//dtd html 4.0 transitional//en">
<%@page import="PACA.ontology.pacaOntology"%>
<%@page import="PACA.agents.Interfaz"%>
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
  
	System.out.println("Intentamos autenticarnos... ");  
  	//autenticado = interfaz.doAutenticacionRequest(request);
  	//autenticado=false;
  	Testigo resultado=new Testigo();
  	resultado.setOperacion(Testigo.Operaciones.autenticar);
  	resultado.setParametro((HttpServletRequest) request);
  	
  	interfaz.getAtributo().putO2AObject(resultado,AgentController.SYNC);
  	
  	System.out.println("Comienzo del bucle");
  	while(!resultado.isRelleno()){
  	}
  	
  	System.out.println("RESULTADO RELLENOOOOOOOOOO: "+resultado.isResultadoB());
  	autenticado = resultado.isResultadoB();
  	System.out.println("AUTENTICADOOOOOOOOOO: "+autenticado);
  	
  	%>
  	<h1>Chivato <%= resultado.getResultado() %> </h1>
  	
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
      //interfaz.setAlumnoID(request.getParameter("user_id"));
      //interfaz.setAlumnoPass(request.getParameter("password"));
%>

<br><br><br>
  <p align="center" class="color">
     Seleccione la práctica a evaluar.  </p><br>

<% 
    // Aquí hacemos la petición de las prácticas disponibles y las
    // mostramos en un formulario
	
    //String[] pract = interfaz.doPeticion();


	System.out.println("Buscamos Corrector... ");
	Testigo resultado3=new Testigo();
	resultado3.setOperacion(Testigo.Operaciones.buscarCorrector);

	interfaz.getAtributo().putO2AObject(resultado3,AgentController.SYNC);
	while(!resultado3.isRelleno()){
	}

	
	
	//-----------------------------------------------------------------------

	System.out.println("Pedimos las practicas");
	Testigo resultado2=new Testigo();
	resultado2.setOperacion(Testigo.Operaciones.pedirPracticas);
	System.out.println("Creamos el testigo con su operacion");
	
	interfaz.getAtributo().putO2AObject(resultado2,AgentController.SYNC);
	
	System.out.println("Comienzo del bucle de practicas");
	while(!resultado2.isRelleno()){
	}
	
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
	<p align="right"><input type="submit" name="seleccionar" value="Seleccionar" onclick="javascript:salida=false;"></p>
    </form>


<%
   }
%>


</body>
</html>



