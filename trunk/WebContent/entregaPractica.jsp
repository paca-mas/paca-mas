    <%@ page import="jade.core.*"%>
    <%@ page import="javax.servlet.jsp.*"%>
    <%@ page import="javax.servlet.*"%>
    <%@ page import="java.util.*"%>
    <%@ page import="PACA.util.*"%>
    <%@page import="jade.wrapper.*"%>

      <jsp:useBean id="interfaz" class="PACA.util.AgentBean" scope="session"/>


<!doctype html public "-//w3c//dtd html 4.0 transitional//en">
<html>
  
  <head>
    <title>
	Entrega de la pr�ctica
    </title>
 <LINK REL=STYLESHEET TYPE="text/css" HREF="http://platon.escet.urjc.es/estilos/estiloPACA.css">
    <SCRIPT TYPE="text/javascript">
      <!--
      salida = true;

      function exit()
      {
       if (salida)
        window.open("salida.jsp",'frame','width=580,height=480,scrollbars=yes,resizable=yes');
       return true;
      }

      //-->
    </SCRIPT>
  </head>
<body onUnload="exit();">
<%@ include file="barra0.html"%>
<br>
<br>
<br>
<%
     
  //String eval = interfaz.doEntregaFinalRequest(request);
	System.out.println("Entregamos la practica");
	Testigo resultado=new Testigo();
	resultado.setOperacion(Testigo.Operaciones.entregarPractica);
	resultado.setParametro((HttpServletRequest)request);
	System.out.println("Creamos el testigo con su operacion");

	//interfaz.getAtributo().putO2AObject(resultado,AgentController.SYNC);
	interfaz.sendTestigo(resultado);

	System.out.println("Comienzo del bucle de la practica entregada");
	while(!resultado.isRelleno()){
		}

	String eval1 = (String)resultado.getResultado();

	//================== A�adido para parsear el resultado aparte ===============

	System.out.println("Instertamos el XML");
	Testigo resultado2=new Testigo();
	resultado2.setOperacion(Testigo.Operaciones.parsear);
	resultado2.setParametro(eval1);
	System.out.println("Creamos el testigo con su operacion");

	//interfaz.getAtributo().putO2AObject(resultado2,AgentController.SYNC);
	interfaz.sendTestigo(resultado2);

	System.out.println("Comienzo del bucle de la salida parseada");
	while(!resultado2.isRelleno()){
		}

	String eval = (String)resultado2.getResultado();



%>
	<%= eval %>
<br>

</body>
</html>



