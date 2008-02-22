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
	Petición de ficheros para la práctica
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



      function comprobar_form(form){
        completo = true
        obj = eval(form)	
        for(i=0;i<obj.length;i++){
          if (obj.elements[i].value == ""){
            completo = false
          }
        }
        if (!completo){
          alert("Por favor, rellene todos los ficheros solicitados.");
          salida = true;
        }
        else{
	  desactivarBoton();
          salida = false;
        }
        return completo;
      }



      function desactivarBoton() {
         document.formtest.botonEnviar.disabled=true;
      }

      //-->
    </SCRIPT>

  </head>
<body onUnload="exit();">

    <%@ include file="barra2.html"%>

    <br>

      <% 
      //String[] testSeleccionados = interfaz.doTestPracticasRequest(request);
      	System.out.println("Pedimos los Ficheros");
  		Testigo resultado=new Testigo();
  		resultado.setOperacion(Testigo.Operaciones.pedirFicheros);
  		resultado.setParametro(request);
  		System.out.println("Creamos el testigo con su operacion");

  		//interfaz.getAtributo().putO2AObject(resultado,AgentController.SYNC);
		interfaz.sendTestigo(resultado);

  		System.out.println("Comienzo del bucle de ficheros");
  		while(!resultado.isRelleno()){
  			}

  		String [] testSeleccionados = (String [])resultado.getResultado();
      
       
      if (testSeleccionados.length==0){ 

      %>
      <H3 class="error"><P  align="center">Debe elegir al menos un test de la lista anterior. Por favor, vuelva hacia
      atrás con el navegador y realice de nuevo su selección.</P></H3>
      <%

      }
      else{
   
      %>
      <br><br>
      <p  align="center" class="color">
        Proporcione los ficheros que se indican a continuación:        
      </p>

     <p  align="center">
      <form method="post" enctype="multipart/form-data" action="evaluacionPractica.jsp" name="formtest" onsubmit="return comprobar_form(this);">

     <%
      // Hacemos la consulta de los ficheros necesarios y rellenamos el formulario
      	//String[] ficheros = interfaz.doFicherosPractica(testSeleccionados);
     	System.out.println("Pedimos los Ficheros");
		Testigo resultado2=new Testigo();
		resultado2.setOperacion(Testigo.Operaciones.insertarFicheros);
		resultado2.setParametro(testSeleccionados); 
		System.out.println("Creamos el testigo con su operacion");

		//interfaz.getAtributo().putO2AObject(resultado2,AgentController.SYNC);
		interfaz.sendTestigo(resultado2);

		System.out.println("Comienzo del bucle de ficheros");
		while(!resultado2.isRelleno()){
			}

		String [] ficheros = (String [])resultado2.getResultado();
     %>

     <br>
     <div class="form"><br> 
     <table width="100%" border="0" cellspacing="0" cellpadding="0" class="color">

      <%
        for (int i=0; i < ficheros.length; i++){     
      %>
      <tr>

      <td width="40%">
        <p align="left"><%= ficheros[i] %></p>
      </td>
      <td width="20%">
       &nbsp;
      </td>
      <td width="40%">
        <p align="center"><input type="file" name="<%= ficheros[i] %>" value="<%= ficheros[i] %>" size="30"></p>
      </td>
     </tr>

      <%
    
      }
 
      %>

      </table><br>
      </div>
        <br><br><br><p align="right">
    	  <input type="submit" value="Evaluar" name="botonEnviar" onclick="javascript:salida=false;"></p>
      </form>
    </p>  

    <%    
     } //del else
    %>

<br>

</body>
</html>



