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
	Evaluación de la práctica
    </title>
   <LINK REL=STYLESHEET TYPE="text/css" HREF="estilos/estiloPaca.css"/>
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
<%@ include file="barra3.html"%>
<%
  // Hacemos la evaluación de la práctica con los fichero leidos del
  // formulario anterior.	
	
  //String[] contenidoFicheros = new String[interfaz.ficherosUltimaPractica.length];

  //for (int i=0; i < interfaz.ficherosUltimaPractica.length; i++) {

    //contenidoFicheros[i] = request.getParameter(interfaz.ficherosUltimaPractica[i]);
  //}
	
	 
   	
  	Testigo resultado=new Testigo();
	resultado.setOperacion(Testigo.Operaciones.corregir);
	resultado.setParametro((HttpServletRequest)request);
	
	//interfaz.getAtributo().putO2AObject(resultado,AgentController.SYNC);
	interfaz.sendTestigo(resultado);

	while(!resultado.isRelleno()){
		}

	String eval1 = (String)resultado.getResultado();
	
	//================== Añadido para parsear el resultado aparte ===============
	
	Testigo resultado2=new Testigo();
	resultado2.setOperacion(Testigo.Operaciones.parsear);
	resultado2.setParametro(eval1);
	
	//interfaz.getAtributo().putO2AObject(resultado2,AgentController.SYNC);
	interfaz.sendTestigo(resultado2);

	while(!resultado2.isRelleno()){
		}

	String eval = (String)resultado2.getResultado();	
  
  
%>
	<br>
	<%= eval %>


<br>

</body>
</html>



