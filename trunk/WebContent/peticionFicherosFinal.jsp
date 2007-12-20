  <%@ page import="jade.core.*"%>
  <%@ page import="javax.servlet.jsp.*"%>
  <%@ page import="javax.servlet.*"%>
  <%@ page import="java.util.*"%>

  <jsp:useBean id="interfaz" class="auth.util.Niapa" scope="session"/>

  
<html>
  
  <head>
    <title>
	Entrega final.
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
            if (!(obj.elements[i].name=="login" || obj.elements[i].name=="pass"))
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
         document.formentregar.entregar.disabled=true;
      }

      //-->
    </SCRIPT>
  </head>

<body onUnload="exit();">
    <%@ include file="barra2.html"%>	
    <br>
      <% 
      String[] testSeleccionados = interfaz.doTestEntregaFinal(request);
       
      if (testSeleccionados.length==0){
      %>
       <H3 class="error">
         <P align="center">
           Hubo un error, no se puede proceder a la entrega final.<BR>Por favor,<a href="salida.jsp" onclick="javascript:salida=false;"> abandone</a> la aplicación.
         </P>
       </H3>
      <%
      }
      else{   
      %>
      <br>
      <br>
      <p align="center" class="color">
         Va a realizar la entrega de su práctica. Por favor, recuerde:<BR>
     <p class="error">
      <OL TYPE="I">
        <LI>La entrega final, <b>sólo</b> podrá ser realizada una vez.</LI>
        <LI>Al realizar la entrega, la práctica no será evaluada.</LI>
        <LI>Tendrá que introducir el login y el passwd del compañero de grupo (en su caso).</LI>
        <LI>Si no tiene compañero, deje los campos correspondientes a los datos del mismo en blanco.</LI>
      </OL>
     </p>
    </p> 
    <p align="center">
    <HR width="50%">
    </p>
     <p align="center"><BR>
      <form method="post" enctype="multipart/form-data" action="entregaPractica.jsp" name="formentregar" onsubmit="return comprobar_form(this);">

     <%
      // Hacemos la consulta de los ficheros necesarios y rellenamos el formulario
        
	  String[] ficheros = interfaz.doFicherosPractica(testSeleccionados);
     %>

   <br>

    <p align="center">
       Compañero de Prácticas:		      
    <div class="form"><br> 
    <table width="80%" border="0" cellspacing="0" cellpadding="0" class="color">
     <tr>
      <td width="20%">
        <p align="center">
          &nbsp;
        </p>
      </td>
      <td width="20%">
         Login
      </td>
      <td width="40%">
       <p align="center">
         <input type="text" name="login" value="login" size="30"></p>
      </td>
     </tr>
     <tr>
      <td width="20%">
        <p align="center">
          &nbsp;
        </p>
      </td>
      <td width="20%">
        Password
      </td>
      <td width="40%">
        <p align="center">
         <input type="password" name="pass" value="pass" size="30">
        </p>
      </td>
     </tr>
     </table>
     <BR>
     </div>
     </p>
     <br>
     <p align="center">
     Ficheros a entregar:		      
     <div class="form"><br> 
     <table width="100%" border="0" cellspacing="0" cellpadding="0" class="color">
     <tr>
      <td width="40%">
        <p align="left">&nbsp;</p>
      </td>
      <td width="20%">
        &nbsp;
      </td>
      <td width="40%">
        <p align="center">&nbsp;</p>
      </td>
     </tr>

    <%   //Insertamos los nombres de los ficheros.
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
       <p align="center">
         <input type="file" name="<%= ficheros[0] %>" value="<%= ficheros[0] %>" size="30">
       </p>
     </td>
    </tr>
    <%  
    }
    %>
    
   </table>
   <br>
   </div>
   </p>
      <br><br><br><p align="right">
	<input type="submit" name="entregar" value="Entregar" onclick="javascript:salida=false;"></p>
    </form>
  </p>
   <% 
       } //del else
   %>

 <br>

 </body>
</html>



