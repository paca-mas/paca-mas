<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<!-- Los import -->
<%@ page language="java" %>
<%@ page import = "java.sql.*"%> 
<%@ page import = "java.util.*"%>
<%@ page import = "javax.swing.*"%>
<%@ page import = "net.sf.jasperreports.engine.*" %>
<%@ page import = "net.sf.jasperreports.view.JasperViewer" %>
<%@ page import = "java.sql.Connection" %>
<%@ page import = "java.io.*" %>
<%@ page import = "es.urjc.ia.web.*" %>
<%@ page import = "es.urjc.ia.informes.*" %>
<%@ page import = "es.urjc.ia.baseDatos.*" %>

<html xmlns="http://www.w3.org/1999/xhtml">
<head>
	<meta http-equiv="content-type" content="text/html; charset=utf-8" />
	<title>Generacion Web de Informes</title>
	<meta name="keywords" content="" />
	<meta name="description" content="" />
	<link href="<%=Configuracion.Css%>" rel="stylesheet" type="text/css" media="screen" />
<%
//*****************************************************************************************
// parametros : user (ALUMNO) list (PARAMETRO ALUMNOS) practica (PRACTICA)
//*****************************************************************************************
	// Parametro user
	String idUsuario = "";
	if(request.getParameter("user")!=null) {
		idUsuario = request.getParameter("user"); 
	}
	String nombre_usuario = ObtenerDatos.DatosAlumnos(idUsuario);
	String tipoUser = ObtenerDatos.TipoUsuario(idUsuario);

	// Parametro list
	String idAlumnos = "";
	if(request.getParameter("list")!=null) {
		idAlumnos = request.getParameter("list"); 
	}else{
		idAlumnos = idUsuario;
	}	
	// Obtener los usarios de la lista
	String auxiliar = idAlumnos;
	List<String> ListId = new ArrayList<String>();	
	ListId.clear();
	ListId = ObtenerDatos.listUser(auxiliar);

	// Parametro practica
	int id_practica = 0;
	if(request.getParameter("practica")!=null) {
		id_practica = Integer.parseInt(request.getParameter("practica")); 
	}
	
	// Parametro test
	int id_test = 0;
	if(request.getParameter("test")!=null) {
		id_test = Integer.parseInt(request.getParameter("test")); 
	}
%>
	<script type="text/javascript">
	function redirigir(idUser) {
		var checkboxes = document.getElementById("form1").check; //Array que contiene los checkbox
		var valores = "";
		var idUser = <%=idUsuario%>;
		var idPrac = <%=id_practica%>;
		var idTest = <%=id_test%>;
		for (var x=0; x < checkboxes.length; x++) {
			if (checkboxes[x].checked) {
				if (valores == "") {
					valores = checkboxes[x].value;
				} else {   
					valores = valores + "," + checkboxes[x].value;
				}
			}
		}
		window.location="LanzarAlumnoTest.jsp?user="+idUser+"&list=" + valores + "&practica=" + idPrac + "&test=" + idTest;
 	}
	</script>
<%
	// Ruta
	String dirLeer = LeerHtml.CrearRutaCompleta(idUsuario +"/"+ id_practica);
	String dir = LeerHtml.CrearRutaRelativa(idUsuario + "/" + id_practica);
	// Parametros Informes
	Object p1 = idAlumnos; 
	Object p2 = id_practica;
	Object p3 = id_test;
	// Lista String
	List<String> imagenes = new ArrayList<String>();
%>	
</head>

<body>
	<div id="logo">
		<h1>ESTRUCTURA DE LOS DATOS Y DE LA INFORMACION</h1>
	  	<p>&nbsp;</p>
		<p><em> http://zenon.etsii.urjc.es/grupo/docencia/edi/Gestion09-10/doku.php</em></p>
	</div>
	<!-- end #logo -->
	
    <div id="header">
		<div id="menu">
			<ul>
				<li><a href="<%=Configuracion.Jsp_Inicio%>?user=<%=idUsuario%>&list=<%=idAlumnos%>">inicio</a></li>
				<li><a href="<%=Configuracion.Jsp_Evaluacion%>?user=<%=idUsuario%>&list=<%=idAlumnos%>">evaluacion</a></li>
				<li><a href="<%=Configuracion.Jsp_Entrega%>?user=<%=idUsuario%>&list=<%=idAlumnos%>">entrega</a></li>
				<li><a href="<%=Configuracion.Jsp_Grupo%>?user=<%=idUsuario%>&list=<%=idAlumnos%>">grupo</a></li>
			</ul>
		</div><!-- end #menu -->

		<div id="search">
			<%
				out.println(nombre_usuario);
			%>
		</div>
		<!-- end #search -->
	</div>
	<!-- end #header -->
	<!-- end #header-wrapper -->
	
    <div id="page">
		<div id="content">
		  <div class="post">
				<div class="title">Estadistica de Usuarios</div>
				<div class="title">Test <%=id_test%> (Practica <%=id_practica%>)</div>
    			<div class="entry">
    			<%
    				if ( tipoUser.equals("P") && (idAlumnos.equals(idUsuario)) ){
    			%>
					<center>		
					<img src="<%=Configuracion.DIRIMAGES + "warning.gif"%>" align=middle></img>			
					<table><tr><td>
					WARNING!!!!!!!!!! TIENES QUE ELEGIR ALGÚN ALUMNO
					</td></tr></table>
					</center>
		     	<%
		     		}else{
		     			     			     			     	    				LanzarInforme.InformeTestAlumno(idUsuario,p1,p2,p3);
		     			     			     			     		 			 	String dirHtml = Configuracion.DIRDATA + idUsuario +"/" + id_practica + "/";			
		     			     			     			     	 				 	imagenes = LeerHtml.LeerImagenes(dirHtml, Configuracion.N_Informe_Usuario_Test);
		     	%>
 				 	<table>
 				 	<%
 				 		for (String img:imagenes){
 				 	 				 					String ruta_img = dir + img;
 				 	%> 
						<tr><td> <img src="<%=ruta_img%>"></img></td></tr>	
					<%
							}
						%> 
					</table>
					<div class="comment">
					<table>
						<tr><td></td></tr>
						<tr><td></td></tr>
						<tr><td>Comentarios : 
						</td></tr>       		
						<tr><td><%=ComentariosGraficos.N_Informe_Test%></td></tr>
					</table>
					</div>
					<%
						}
					%>
				</div><!-- end #entry -->
		  </div><!-- end #post -->
		</div><!-- end #content -->
		
		<div id="sidebar">
		<ul>
			<li>
				<h2>Estadisticas de Usuarios</h2>
				<ul><%
					List<String> ListaCaso = ObtenerDatos.ListaCaso(id_practica);
									for (String caso:ListaCaso){
				%>
						<li><a href="<%=Configuracion.Jsp_AlumnosC%>?user=<%=idUsuario%>&list=<%=idAlumnos%>&practica=<%=id_practica%>&test=<%=id_test%>&caso=<%=caso%>">
						<%
							out.println("Caso "+ caso);
						%>
						</a></li>
					<%
						}
					%>
				</ul>
			</li>
		</ul>

		<%if (tipoUser.equals("P")){%>
		<ul>
			<li>
				<h2>Seleccionar Alumnos</h2>
				<ul>
					<li><input type="image" src="<%=Configuracion.DIRIMAGES + "refresh.gif"%>" align=right onClick="redirigir();"/>
					<br/>
					</li>					
					<form id="form1" method="post" action="#">
					<%List <String> ListaAlumnos = ObtenerDatos.ListaAlumnos(1);
					int i = 0;
					int id = 1;
					while (i < ListaAlumnos.size()) {
						String nombre = ListaAlumnos.get(i+1) + " "  + ListaAlumnos.get(i+2);%>
						<li>
						<input type="checkbox" name="check" id="option<%=id%>" value="<%=ListaAlumnos.get(i)%>" 
						<%if ( ListId.contains(ListaAlumnos.get(i)) ){ %>
							checked
						<%}%>
						></input>
						<label for="option<%=id%>"><%=nombre%></label>
						<br/>
						</li>
						<%i = i + 3;
						id ++;
					}%>
					</form>
					<li>
					<input type="image" src="<%=Configuracion.DIRIMAGES + "refresh.gif"%>" align=right onClick="redirigir();"/>
					<br/>
					</li>
				</ul>
			</li>
		</ul>
		<%}%>
		</div><!-- end #sidebar -->
	</div><!-- end #page -->
</body>
</html>
