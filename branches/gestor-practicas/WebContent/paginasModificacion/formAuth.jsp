<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01//EN" "http://www.w3.org/TR/html4/strict.dtd">

<html>
	<head>
		<title>
			P&aacute;gina de Autentificaci&oacute;n
		</title>
                <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
		<LINK REL=STYLESHEET TYPE="text/css" HREF="./estilos/estiloPaca.css">
		<SCRIPT TYPE="text/javascript">
			 <!--

			 function desactivarBoton() {
				 document.formauth.botonLogin.disabled=true;
				 document.formauth.botonBorrar.disabled=true;
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
					 alert("Por favor, inserte su identificador y su clave.");
				 }
				 else{
					 desactivarBoton();
				 }
				 return completo;
			 }

			 //-->
		</SCRIPT>
	</head>
	<body>

	<%@ include file="cab.html"%>

        <p>
            <a href="http://validator.w3.org/check?uri=referer"><img
                    style="border:0;width:88px;height:31px" src="http://www.w3.org/Icons/valid-html401-blue"
                    alt="Valid HTML 4.01 Strict" height="31" width="88"></a>
            <a href="http://jigsaw.w3.org/css-validator/check/referer">
                <img style="border:0;width:88px;height:31px"
                     src="http://jigsaw.w3.org/css-validator/images/vcss-blue"
                     alt="¡CSS Válido!" >
            </a>
        </p>


		<div id="cuerpo">
			<p class="center">
			<form method="post" action="respAuth.jsp" name="formauth" onSubmit="return comprobar_form(this);">
				
				<p class="center">
					Bienvenido, por favor inicie una sesi&oacute;n:
				</p>
				
				<p class="center">
				<p class="form">
				<table class="color">
					<tr>
						<td style="width: 50em;">
							Usuario:
						</td>
						<td style="width: 50em;">
							<input type="text" name="user_id" size="25">
						</td>
					</tr>
					<tr>
						<td style="width: 50em;">
							Password:
						</td>
						<td style="width: 50em;">
							<input type="password" name="password" size="25">
						</td>
					</tr>
					<tr>
						<td style="width: 50em;">
							&nbsp;
						</td>
						<td style="width: 50em;">
							<P class="center">
								<input type="submit" value="Login" name="botonLogin">
								<input type="reset" value="Borrar" name="botonBorrar">
							</P>
						</td>
					</tr>
				</table>
			</form>
			<br>

		</div>
	</body>
</html>
