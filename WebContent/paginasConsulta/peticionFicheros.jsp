<%@ page import="jade.core.*"%>
<%@ page import="javax.servlet.jsp.*"%>
<%@ page import="javax.servlet.*"%>
<%@ page import="java.util.*"%>
<%@ page import="jade.core.Runtime"%>
<%@ page import="jade.wrapper.*"%>
<%@ page import="es.urjc.ia.paca.util.*"%>

<jsp:useBean id="interfaz" class="es.urjc.ia.paca.util.AgentBean" scope="session"/>


   
<!doctype html public "-//w3c//dtd html 4.0 transitional//en">
<html>
	
	<head>
		<title>
			Petición de ficheros para la práctica
		</title>
		<LINK REL=STYLESHEET TYPE="text/css" HREF="./estilos/estiloPaca.css">
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
		
		<%@ include file="cab.html"%>
		<%@ include file="barra2.html"%>
		
		<br>
		
		<%
		//String[] testSeleccionados = interfaz.doTestPracticasRequest(request);
		Testigo resultado = new Testigo();
		resultado.setOperacion(Testigo.Operaciones.pedirFicheros);
		resultado.setParametro(request);

		//interfaz.getAtributo().putO2AObject(resultado,AgentController.SYNC);
		interfaz.sendTestigo(resultado);

		while (!resultado.isRelleno()) {
		}

		String[] testSeleccionados = (String[]) resultado.getResultado();


		if (testSeleccionados.length == 0) {

		%>
		<h3 class="error">
			<p  class="center">
				Debe elegir al menos un test de la lista anterior. Por favor, vuelva hacia
				atr&aacute;s con el navegador y realice de nuevo su selección.
			</p>
		</h3>
		<%

		} else {

		%>
		<br><br>
		<p class="center" class="color">
			Proporcione los ficheros que se indican a continuaci&oacute;n:        
		</p>
		
		<p  class="center">
			<form method="post" enctype="multipart/form-data" action="evaluacionPractica.jsp" name="formtest" onsubmit="return comprobar_form(this);">
				
				<%
			// Hacemos la consulta de los ficheros necesarios y rellenamos el formulario
			//String[] ficheros = interfaz.doFicherosPractica(testSeleccionados);

			Testigo resultado2 = new Testigo();
			resultado2.setOperacion(Testigo.Operaciones.insertarFicheros);
			resultado2.setParametro(testSeleccionados);

			//interfaz.getAtributo().putO2AObject(resultado2,AgentController.SYNC);
			interfaz.sendTestigo(resultado2);

			while (!resultado2.isRelleno()) {
			}

			String[] ficheros = (String[]) resultado2.getResultado();
				%>
				
				<br>
				<div class="form"><br> 
					<table width="100%" border="0" cellspacing="0" cellpadding="0" class="color">
						
						<%
			for (int i = 0; i < ficheros.length; i++) {
						%>
						<tr>
							
							<td style="width: 40%;">
								<p style="text-align: left;">
									<%= ficheros[i] %>
								</p>
							</td>
							<td style="width: 20%;">
								&nbsp;
							</td>
							<td style="width: 40%;">
								<p class="center">
									<input type="file" name="<%= ficheros[i] %>" value="<%= ficheros[i] %>" size="30">
								</p>
							</td>
						</tr>
						
						<%

			}
						
						%>
						
					</table><br>
				</div>
				<br><br><br>
				<p  style="text-align: right;">
					<input type="submit" value="Evaluar" name="botonEnviar" onclick="javascript:salida=false;">
				</p>
			</form>
		</P>  
		
		<%
		} //del else
		%>
		
		<br>
		
	</body>
</html>



