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
			Petici&oacute;n de Tests para la pr&aacute;ctica
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
		
		<%@ include file="cab.html"%>
		<%@ include file="barra1.html"%> 
		
		<br><br><br>  
		<p class="center"  class="color">
			Seleccione los test que ser&aacute;n evaluados.
		</p>
		<br>
		
		<p class="center">
			<form method="post" name="formseleccionar" action="peticionFicheros.jsp" onsubmit="desactivarBoton();">
				<P class="form"><BR>
					<table style="width: 100%;" class="color">
						<%
		// Hacemos la consulta de los tests necesarios y rellenamos el formulario

		//String[] tests = interfaz.doPeticionTestPracticaRequest(request);  

		Testigo resultado2 = new Testigo();
		resultado2.setOperacion(Testigo.Operaciones.pedirTests);
		resultado2.setParametro((HttpServletRequest) request);

		//interfaz.getAtributo().putO2AObject(resultado2,AgentController.SYNC);
		interfaz.sendTestigo(resultado2);

		while (!resultado2.isRelleno()) {
		}

		String[] tests = (String[]) resultado2.getResultado();

		for (int i = 0; i < tests.length; i += 2) {
						%>
						<tr>
							
							<td style="width: 10%;">
								<p class="center">
									<INPUT NAME="<%= tests[i] %>" TYPE=CHECKBOX>
								</p>
							</td>
							<td style="width: 10%;">
								&nbsp;
							</td>
							<td style="width: 80%;">
								<p style="text-align: left;">
									<%= tests[i + 1]%>
								</p>
							</td>
						</tr>
						<%
		}
						%>
					</table><BR>
				</P>
				<br>
				<p style="text-align: right;">
					<input type="submit" name="seleccionar" value="Seleccionar Test" onclick="javascript:salida=false;">
				</p>
			</form>
		</p>    
		
		<HR> 
		<p class="center">
			<form method="post" name="formentregar" action="peticionFicherosFinal.jsp" onsubmit="return comprobar_entrega(this);">
				<div class="form"><BR>
					<table width="100%" border="0" cellspacing="0" cellpadding="0" class="color">
						<tr>
							<td style="width: 10%;">
								<p class="center">
									 <INPUT NAME="EntregaFin" TYPE=CHECKBOX>
								</p>
							</td>
							<td style="width: 10%;">
								&nbsp;
							</td>
							<td style="width: 80%;">
								<p class="left">
									 Entrega definitiva de la pr&aacute;ctica.
								</p>
							</td>
						</tr>
					</table><BR>
				</div>
				<br>
				<p class="right">
					 <input type="submit" name="entregar" value="Entregar" onclick="javascript:salida=false;">
			    </p>
			</form>
		</p>
	</body>
</html>



