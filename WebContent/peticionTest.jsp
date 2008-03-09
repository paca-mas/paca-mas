  <%@ page import="jade.core.*"%>
  <%@ page import="javax.servlet.jsp.*"%>
  <%@ page import="javax.servlet.*"%>
  <%@ page import="java.util.*"%>
  <%@ page import="jade.core.Runtime"%>
  <%@ page import="jade.wrapper.*"%>
  <%@ page import="PACA.util.*"%>

  <jsp:useBean id="interfaz" class="PACA.util.AgentBean" scope="session"/>
  
  

<!doctype html public "-//w3c//dtd html 4.0 transitional//en">
<html>
  
  <head>
    <title>
	Petición de Tests para la práctica
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


      function comprobar_entrega(form){
       
        if (!form.EntregaFin.checked){
          alert("Por favor, seleccione la casilla de entrega de práctica.");
          salida = true;
        }
        else{
	  desactivarBoton();
          salida = false;
        }
        return form.EntregaFin.checked;
      }



      function desactivarBoton() {
         document.formentregar.entregar.disabled=true;
	 document.formseleccionar.seleccionar.disabled=true;
      }


      //-->
    </SCRIPT>
  </head>
<body onUnload="exit();">
<%@ include file="barra1.html"%> 
<br><br><br>  <p align="center"  class="color">
    Seleccione los test que serán evaluados.
  </p><br>

  <p align="center">
    <form method="post" name="formseleccionar" action="peticionFicheros.jsp" onsubmit="desactivarBoton();">
   <P class="form"><BR>
   <table width="100%" border="0" cellspacing="0" cellpadding="0" class="color">
<%
  // Hacemos la consulta de los tests necesarios y rellenamos el formulario
	  
  //String[] tests = interfaz.doPeticionTestPracticaRequest(request);  
	
	Testigo resultado2=new Testigo();
	resultado2.setOperacion(Testigo.Operaciones.pedirTests);
	resultado2.setParametro((HttpServletRequest)request);
	
	//interfaz.getAtributo().putO2AObject(resultado2,AgentController.SYNC);
	interfaz.sendTestigo(resultado2);

	while(!resultado2.isRelleno()){
		}

	String [] tests = (String [])resultado2.getResultado();
  
  for (int i=0; i < tests.length; i+=2) 
	{
%>
	    <tr>

    <td width="10%">
<p align="center"><INPUT NAME="<%= tests[i] %>" TYPE=CHECKBOX></p>
    </td>
    <td width="10%">
     &nbsp;
    </td>
    <td width="80%">
<p align="left"><%= tests[i+1]%></p>
    </td>
   </tr>
<%
   }
%>
   </table><BR>
</P>
	<br>
	<p align="right"><input type="submit" name="seleccionar" value="Seleccionar Test" onclick="javascript:salida=false;"></p>
    </form>
  </p>    

  <HR> 
  <p align="center">
    <form method="post" name="formentregar" action="peticionFicherosFinal.jsp" onsubmit="return comprobar_entrega(this);">
   <div class="form"><BR>
   <table width="100%" border="0" cellspacing="0" cellpadding="0" class="color">
    <tr>
    <td width="10%">
       <p align="center"><INPUT NAME="EntregaFin" TYPE=CHECKBOX></p>
    </td>
    <td width="10%">
     &nbsp;
    </td>
    <td width="80%">
      <p align="left">Entrega definitiva de la práctica.</p>
    </td>
   </tr>
   </table><BR>
</div>
	<br>
	<p align="right"><input type="submit" name="entregar" value="Entregar" onclick="javascript:salida=false;"></p>
    </form>
  </p>
</body>
</html>



