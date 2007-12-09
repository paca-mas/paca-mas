  <%@ page import="jade.core.*"%>
  <%@ page import="javax.servlet.jsp.*"%>
  <%@ page import="javax.servlet.*"%>
  <%@ page import="java.util.*"%>

  <jsp:useBean id="interfaz" class="PACA.agents.InterfazJSP" scope="session"/>


 
<!doctype html public "-//w3c//dtd html 4.0 transitional//en">
<html>
  
  <head>
    <title>
	Evaluación de la práctica
    </title>
   <LINK REL=STYLESHEET TYPE="text/css" HREF="estiloPACA.css"/>
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
	
  String[] contenidoFicheros = new String[interfaz.ficherosUltimaPractica.length];

  for (int i=0; i < interfaz.ficherosUltimaPractica.length; i++) {

    contenidoFicheros[i] = request.getParameter(interfaz.ficherosUltimaPractica[i]);
  }
	
	 
  //String eval = interfaz.doCorreccion(contenidoFicheros);  
  String eval = interfaz.doCorreccionRequest(request);

%>
	<br>
	<%= eval %>


<br>

</body>
</html>



