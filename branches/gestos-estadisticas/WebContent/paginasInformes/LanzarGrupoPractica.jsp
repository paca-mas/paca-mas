<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">

<!-- Los import -->
<%@ page language="java"%>
<%@ page import="java.sql.*"%>
<%@ page import="java.util.*"%>
<%@ page import="javax.swing.*"%>
<%@ page import="net.sf.jasperreports.engine.*"%>
<%@ page import="net.sf.jasperreports.view.JasperViewer"%>
<%@ page import="java.sql.Connection"%>
<%@ page import="java.io.*"%>
<%@ page import="es.urjc.ia.web.*"%>
<%@ page import="es.urjc.ia.informes.*"%>
<%@ page import="es.urjc.ia.baseDatos.*"%>
<%@ page import="es.urjc.ia.paca.agents.InterfazJSPGestorEstadistica"%>
<%@ page import="es.urjc.ia.paca.agents.InterfazGestorEstadistica"%>
<%@ page import="es.urjc.ia.paca.util.*"%>

<jsp:useBean id="InterfazGestorEstadistica" class="es.urjc.ia.paca.util.AgentBeanGestorEstadistica" scope="session"/>

<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="content-type" content="text/html; charset=utf-8" />
<title>Generacion Web Estadisticas</title>
<meta name="keywords" content="" />
<meta name="description" content="" />
<link href="<%=Configuracion.Css%>" rel="stylesheet" type="text/css" media="screen" />

<%
//*****************************************************************************************
//parametros : 
//user (ALUMNO) 
//list (lista de usuarios) 
//practica
//boolean (borrarAlumnos,borrarPractica, borrarDatos, cargarPractica, cargarDatos)
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
	
	// Parametro boolean
	String b = "";
	if(request.getParameter("boolean")!=null) {
		b = request.getParameter("boolean"); 
	}	
	
	// Si el parametro boolean no es vacio, habrá que avisar al Agente Interfaz
	if (b.equals("borrarAlumnos")) {
		Testigo resultado1 = new Testigo();
		resultado1.setOperacion(Testigo.Operaciones.eliminarAlumno);
		InterfazGestorEstadistica.sendTestigo(resultado1);
	}else{
		if (b.equals("borrarPracticas")){
			Testigo resultado2 = new Testigo();
			resultado2.setOperacion(Testigo.Operaciones.eliminarPractica);
			InterfazGestorEstadistica.sendTestigo(resultado2);
		}else{
			if (b.equals("cargarAlumnos")) {
				Testigo resultado3 = new Testigo();
				resultado3.setOperacion(Testigo.Operaciones.cargarAlumno);
				InterfazGestorEstadistica.sendTestigo(resultado3);
			} else {
				if (b.equals("cargarPracticas")) {
					System.out.println("cargarPracticas");
					Testigo resultado4 = new Testigo();
					resultado4.setOperacion(Testigo.Operaciones.cargarPractica);
					InterfazGestorEstadistica.sendTestigo(resultado4);
				}else{
					if (b.equals("borrarDatos")) {
						Testigo resultado5 = new Testigo();
						resultado5.setOperacion(Testigo.Operaciones.eliminarDatos);
						InterfazGestorEstadistica.sendTestigo(resultado5);
					}
				}
			}
		}
	}

	// Ruta
	String dirLeer = LeerHtml.CrearRutaCompleta(idUsuario + "/" + id_practica);
	String dir = LeerHtml.CrearRutaRelativa(idUsuario + "/" + id_practica);
	// Parametros Informes
	Object p1 = idAlumnos; 
	Object p2 = id_practica;
	// Lista String
	List<String> imagenes = new ArrayList<String>();
%>

<script type="text/javascript">

	function redirigir(idUser) {
		var checkboxes = document.getElementById("formCheckAlumnos").check; //Array que contiene los checkbox
		var idUser = <%=idUsuario%>;
		var valores = <%=idUsuario%>;

		for (var x=0; x < checkboxes.length; x++) {
			if (checkboxes[x].checked) {
				if (valores == "") {
					valores = checkboxes[x].value;
				} else {   
					valores = valores + "," + checkboxes[x].value;
				}
			}
		}
		if (valores==null){
			valores=idUser;
		}
		window.location="LanzarGrupoPractica.jsp?user=" + idUser +"&list=" + valores ;
 	}

	function BorrarDatosAlumnos() {
		var checkboxes = document.getElementById("formCheckAlumnos").check; //Array que contiene los checkbox
		var idUser = <%=idUsuario%>;
		var valores = <%=idUsuario%>;

		for (var x=0; x < checkboxes.length; x++) {
			if (checkboxes[x].checked) {
				if (valores == "") {
					valores = checkboxes[x].value;
				} else {   
					valores = valores + "," + checkboxes[x].value;
				}
			}
		}
		
		if (valores==null){
			valores=idUser;
		}
		var confirmacion = confirm("¿Esta seguro de que desea eliminar los datos de los Alumnos");	
		if (confirmacion == true) {
				alert("Hecho. Se han borrado los datos de los alumnos");
				window.location="LanzarGrupoPractica.jsp?user=" + idUser + "&list" + valores + "&boolean=borrarAlumnos";
		}else{
			alert("La operación se ha cancelado a petición del usuario");
			window.location="LanzarGrupoPractica.jsp?user=" + idUser + "&list" + valores;
		}	
	}

	function BorrarDatosPracticas() {
		var checkboxes = document.getElementById("formCheckAlumnos").check; //Array que contiene los checkbox
		var idUser = <%=idUsuario%>;
		var valores = <%=idUsuario%>;

		for (var x=0; x < checkboxes.length; x++) {
			if (checkboxes[x].checked) {
				if (valores == "") {
					valores = checkboxes[x].value;
				} else {   
					valores = valores + "," + checkboxes[x].value;
				}
			}
		}
		
		if (valores==null){
			valores=idUser;
		}
		
		var confirmacion = confirm("¿Esta seguro de que desea eliminar los datos de los Alumnos)");	
		if (confirmacion == true) {
				alert("Hecho. Se han borrado los datos de los alumnos");
				window.location="LanzarGrupoPractica.jsp?user=" + idUser + "&list" + valores + "&boolean=borrarPracticas";
		}else{
			alert("La operación se ha cancelado a petición del usuario");
			window.location="LanzarGrupoPractica.jsp?user=" + idUser + "&list" + valores;
		}	
	}

	function BorrarDatosEstadisticas() {
		var checkboxes = document.getElementById("formCheckAlumnos").check; //Array que contiene los checkbox
		var idUser = <%=idUsuario%>;
		var valores = <%=idUsuario%>;

		for (var x=0; x < checkboxes.length; x++) {
			if (checkboxes[x].checked) {
				if (valores == "") {
					valores = checkboxes[x].value;
				} else {   
					valores = valores + "," + checkboxes[x].value;
				}
			}
		}
		
		if (valores==null){
			valores=idUser;
		}
		
		var confirmacion = confirm("¿Esta seguro de que desea eliminar los datos Estadisticos");	
		if (confirmacion == true) {
			alert("Hecho. Se han borrado los datos de los alumnos");
			window.location="LanzarGrupoPractica.jsp?user=" + idUser + "&list" + valores + "&boolean=borrarDatos";
		}else{
			alert("La operación se ha cancelado a petición del usuario");
			window.location="LanzarGrupoPractica.jsp?user=" + idUser + "&list" + valores;
		}	
	}
	
	function CargarDatosAlumnos() {
		var checkboxes = document.getElementById("formCheckAlumnos").check; //Array que contiene los checkbox
		var idUser = <%=idUsuario%>;
		var valores = <%=idUsuario%>;

		for (var x=0; x < checkboxes.length; x++) {
			if (checkboxes[x].checked) {
				if (valores == "") {
					valores = checkboxes[x].value;
				} else {   
					valores = valores + "," + checkboxes[x].value;
				}
			}
		}
		
		if (valores==null){
			valores=idUser;
		}
		
		var confirmacion = confirm("¿Esta seguro de que desea cargar datos alumnos");	
		if (confirmacion == true) {
			alert("Hecho. Se han borrado los datos de los alumnos");
			window.location="LanzarGrupoPractica.jsp?user=" + idUser + "&list" + valores + "&boolean=cargarAlumnos";
		}else{
			alert("La operación se ha cancelado a petición del usuario");
			window.location="LanzarGrupoPractica.jsp?user=" + idUser + "&list" + valores;
		}
	}

	function CargarDatosPracticas() {
		var checkboxes = document.getElementById("formCheckAlumnos").check; //Array que contiene los checkbox
		var idUser = <%=idUsuario%>;
		var valores = <%=idUsuario%>;

		for (var x=0; x < checkboxes.length; x++) {
			if (checkboxes[x].checked) {
				if (valores == "") {
					valores = checkboxes[x].value;
				} else {   
					valores = valores + "," + checkboxes[x].value;
				}
			}
		}
		
		if (valores==null){
			valores=idUser;
		}
		
		var confirmacion = confirm("¿Esta seguro de que desea eliminar los datos Estadisticos");	
		if (confirmacion == true) {
			alert("Hecho. Se han borrado los datos de los alumnos");
			window.location="LanzarGrupoPractica.jsp?user=" + idUser + "&list" + valores + "&boolean=cargarPracticas";
		}else{
			alert("La operación se ha cancelado a petición del usuario");
			window.location="LanzarGrupoPractica.jsp?user=" + idUser + "&list" + valores;
		}	

	}
</script>
</head>

<body>
<div id="logo">
	<h1>ESTRUCTURA DE LOS DATOS Y DE LA INFORMACION</h1>
	<p>&nbsp;</p>
	<p><em>http://zenon.etsii.urjc.es/grupo/docencia/edi/Gestion09-10/doku.php</em></p>
</div><!-- end #logo -->

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
<%out.println(nombre_usuario);%>
</div><!-- end #search -->
</div><!-- end #header -->

<div id="page">
<div id="content">
<div class="post">
<div class="title">Estadisticas del Grupo</div><!-- end #title -->
<div class="title">Practica <%=id_practica%></div><!-- end #title -->
<div class="entry">
	<%if (tipoUser.equals("P") && idUsuario.equals(idAlumnos)) {%> 
		<center>
		<table>
		<tr><td><img src="<%=Configuracion.DIRIMAGES + "warning.gif"%>"/></td></tr>
		<tr><td>WARNING!!!!!!!!!! TIENES QUE ELEGIR ALGÚN ALUMNO</td></tr>
		</table>
		</center> 
	<%}else{
		// Lanzar Informe
		LanzarInforme.InformePracticaGrupo(idUsuario,p1,p2);
		imagenes = LeerHtml.LeerImagenes(dirLeer,Configuracion.N_Informe_Practica_Grupo);%>
		<table>
		<%for (String img:imagenes){
			String ruta_img = dir + img;%>
			<tr><td><img src="<%=ruta_img%>" /></td></tr>
		<%}%>
		</table>
		
		<div class="comment">
		<table>
			<tr><td></td></tr>
			<tr><td></td></tr>
			<tr><td>Comentarios :</td></tr>
			<tr><td><%=ComentariosGraficos.N_Informe_Practica_Grupo%></td></tr>
		</table>
		</div><!-- end #comment --> 
	<%}%>
</div><!-- end #entry -->
</div><!-- end #post -->
</div><!-- end #content -->

<div id="sidebar">
<ul>
	<li>
	<h2>Estadistica de Grupo</h2>
	<ul>
		<%List<String> ListaTest = ObtenerDatos.ListaTest(id_practica);		
		for (String test:ListaTest){%>
			<li><a href="<%=Configuracion.Jsp_GrupoT%>?user=<%=idUsuario%>&list=<%=idAlumnos%>&practica=<%=id_practica%>&test=<%=test%>"><%out.println("Test " + test);%> </a></li>
		<%}%>
	</ul>
	</li>
</ul>

<%if (tipoUser.equals("P")){%>
<ul>
	<li>
	<h2>Seleccionar Alumnos</h2>
	<ul>
		<li>
		<input type="image" src="<%=Configuracion.DIRIMAGES + "refresh.gif"%>" align=right onClick="redirigir();"/><br/>
		</li>
		<form id="formCheckAlumnos" method="post" action="#">
		<%List <String> ListaAlumnos = ObtenerDatos.ListaAlumnos(1);
		int i = 0;
		int id = 1;
		while (i < ListaAlumnos.size()) {
			String nombre = ListaAlumnos.get(i+1) + " "  + ListaAlumnos.get(i+2);%>
			<li>
			<input type="checkbox" name="check" id="option<%=id%>" value="<%=ListaAlumnos.get(i)%>"<%if ( ListId.contains(ListaAlumnos.get(i)) ){ %> checked <%}%>></input>
			<label for="option<%=id%>"><%=nombre%></label> <br />
			</li>
			<%i = i + 3;
			id ++;
		}%>
		</form>
		<li>
		<input type="image" src="<%=Configuracion.DIRIMAGES + "refresh.gif"%>" align=right onClick="redirigir();"/><br/>
		</li>
	</ul>
	</li>
</ul>

<ul>
	<li>
	<h2>Configuración</h2>
	<ul>
		<li><a href="javascript:BorrarDatosAlumnos();">Borrar Datos Alumnos</a></li>
		<li><a href="javascript:BorrarDatosPracticas();">Borrar Estructura Prácticas</a></li>
		<li><a href="javascript:BorrarDatosPracticas();">Borrar Datos Estadisticas</a></li>
		<li><a href="javascript:CargarDatosAlumnos();">Cargar Datos Alumnos</a></li>
		<li><a href="javascript:CargarDatosPracticas();">Cargar Estructura Practicas</a></li>
	</ul>
	</li>
</ul>
<%}%>

</div><!-- end #sidebar -->
</div><!-- end #page -->

</body>
</html>