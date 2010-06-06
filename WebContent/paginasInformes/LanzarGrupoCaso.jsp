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
<%
	//*****************************************************************************************
// parametros : user (ALUMNO) list (PARAMETRO ALUMNOS)
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

	// practica seleccionada
	int id_practica = 0;
	if(request.getParameter("practica")!=null) {
		id_practica = Integer.parseInt(request.getParameter("practica")); 
	}
	// test seleccionada
	int id_test = 0;
	if(request.getParameter("test")!=null) {
		id_test = Integer.parseInt(request.getParameter("test")); 
	}
	
	// Parametro borrar/cargar
	String b = "";
	if(request.getParameter("boolean")!=null) {
		b = request.getParameter("boolean"); 
	}	
	
	if (b.equals("borrarAlumnos")) {
		Testigo resultado2 = new Testigo();
		resultado2.setOperacion(Testigo.Operaciones.eliminarAlumno);
		InterfazGestorEstadistica.sendTestigo(resultado2);
	}else{
		if (b.equals("borrarPracticas")){
		Testigo resultado2 = new Testigo();
		resultado2.setOperacion(Testigo.Operaciones.eliminarPractica);
		InterfazGestorEstadistica.sendTestigo(resultado2);
		}
	}
%>
<script type="text/javascript">
	function redirigir(idUser) {
		var checkboxes = document.getElementById("form1").check; //Array que contiene los checkbox
		var valores = "";
		var idUser = <%=idUsuario%>;

		for (var x=0; x < checkboxes.length; x++) {
			if (checkboxes[x].checked) {
				if (valores == "") {
					valores = checkboxes[x].value;
				} else {   
					valores = valores + "," + checkboxes[x].value;
				}
			}
		}
		window.location="Entrega.jsp?user=" + idUser +"&list=" + valores ;
 	}
	function BorrarDatosAlumnos() {
		var confirmacion = confirm("¿Esta seguro de que desea eliminar los datos de los Alumnos)");	
		if (confirmacion == true) {
				alert("Hecho. Se han borrado los datos de los alumnos");
		}	
	}
	 
	function BorrarDatosPracticas() {
		var confirmacion = confirm("¿Esta seguro de que desea eliminar los datos de los Alumnos)");	
		if (confirmacion == true) {
				alert("Hecho. Se han borrado los datos de las practicas");
		}	
	 }
	function CargarDatosPracticas() {
		var confirmacion = confirm("¿Esta seguro de que desea cargar los datos de las practicas?");	
		if (confirmacion == true) {
				alert("Hecho. Se han cargado los datos de las practicas");
		}	
	 }	
	 function CargarDatosAlumnos() {
			var confirmacion = confirm("¿Esta seguro de que desea cargar los datos de los alumnos?");	
			if (confirmacion == true) {
					alert("Hecho. Se han cargado los datos de los alumnos");
			}	
	 }
</script>
<%
	// Ruta
	String dirLeer = LeerHtml.CrearRutaCompleta(idUsuario + "/" + id_practica);
	String dir = LeerHtml.CrearRutaRelativa(idUsuario + "/" + id_practica);
	// Parametros Informes
	Object p1 = id_practica;
    Object p2 = id_test;
	int num_grupo = ObtenerDatos.DatosGrupo(idUsuario);					
	Object p3 = num_grupo;	
	// Lista String
	List<String> imagenes = new ArrayList<String>();
%>
<meta http-equiv="content-type" content="text/html; charset=utf-8" />
<title>Generacion Web de Informes</title>
<meta name="keywords" content="" />
<meta name="description" content="" />
<link href="<%=Configuracion.Css%>" rel="stylesheet" type="text/css"
	media="screen" />
</head>

<body>
<div id="logo">
<h1>ESTRUCTURA DE LOS DATOS Y DE LA INFORMACION</h1>
<p>&nbsp;</p>
<p><em>
http://zenon.etsii.urjc.es/grupo/docencia/edi/Gestion09-10/doku.php</em></p>
</div>
<hr />
<!-- end #logo -->

<div id="header">
<div id="menu">
<ul>
	<li><a
		href="<%=Configuracion.Jsp_Inicio%>?user=<%=idUsuario%>&list=<%=idAlumnos%>">inicio</a></li>
	<li><a
		href="<%=Configuracion.Jsp_Evaluacion%>?user=<%=idUsuario%>&list=<%=idAlumnos%>">evaluacion</a></li>
	<li><a
		href="<%=Configuracion.Jsp_Entrega%>?user=<%=idUsuario%>&list=<%=idAlumnos%>">entrega</a></li>
	<li><a
		href="<%=Configuracion.Jsp_Grupo%>?user=<%=idUsuario%>&list=<%=idAlumnos%>">grupo</a></li>
</ul>
</div>
<!-- end #menu -->

<div id="search">
<%
				out.println(nombre_usuario);
			%>
</div>
<!-- end #search --></div>
<!-- end #header -->
<!-- end #header-wrapper -->

<div id="page">
<div id="content">
<div class="post">
<div class="title">Estadisticas del Grupo</div>
<div class="title">Casos del Test <%=id_test%> (Practica <%=id_practica%>)</div>
<div class="entry">
<%LanzarInforme.InformeCasoGrupo(idUsuario,p1,p2,p3);
				imagenes = LeerHtml.LeerImagenes(dirLeer,Configuracion.N_Informe_Caso_Grupo);%>
<table>
	<%for (String img:imagenes){
					String ruta_img = dirLeer + img;%>
	<tr>
		<td><img src="<%=ruta_img%>"></img></td>
	</tr>
	<%}%>
</table>
<div class="comment">
<table>
	<tr>
		<td></td>
	</tr>
	<tr>
		<td></td>
	</tr>
	<tr>
		<td><%=ComentariosGraficos.N_Informe_Caso_Grupo%></td>
	</tr>
</table>
</div>
<!-- end #comment --></div>
<!-- end #entry --></div>
<!-- end #post --></div>
<!-- end #content -->

<div id="sidebar">
<ul>
	<li>
	<h2>Estadisticas de Grupo</h2>
	<ul>
		<%
					List<String> ListaTest = ObtenerDatos.ListaTest(id_practica);
					for (String test:ListaTest){%>
		<li><a
			href="<%=Configuracion.Jsp_GrupoT%>.jsp?user=<%=idUsuario%>&list=<%=idAlumnos%>&practica=<%=id_practica%>&test=<%=test%>">
		<%out.println("Test "+ test);%> </a></li>
		<%}%>
	</ul>
	</li>
</ul>

<%if (tipoUser.equals("P")){%>
<ul>
	<li>
	<h2>Seleccionar Alumnos</h2>
	<ul>
		<li><input type="image"
			src="<%=Configuracion.DIRIMAGES + "refresh.gif"%>" align=right
			onClick="redirigir();" /> <br />
		</li>
		<form id="form1" method="post" action="#">
		<%List <String> ListaAlumnos = ObtenerDatos.ListaAlumnos(1);
					int i = 0;
					int id = 1;
					while (i < ListaAlumnos.size()) {
						String nombre = ListaAlumnos.get(i+1) + " "  + ListaAlumnos.get(i+2);%>
		<li><input type="checkbox" name="check" id="option<%=id%>"
			value="<%=ListaAlumnos.get(i)%>"
			<%if ( ListId.contains(ListaAlumnos.get(i)) ){ %> checked <%}%>></input>
		<label for="option<%=id%>"><%=nombre%></label> <br />
		</li>
		<%i = i + 3;
						id ++;
					}%>
		</form>
		<li><input type="image"
			src="<%=Configuracion.DIRIMAGES + "refresh.gif"%>" align=right
			onClick="redirigir();" /> <br />
		</li>
	</ul>
	</li>
</ul>

<ul>
	<li>
	<h2>Configuracion</h2>
	<ul>
		<li>Borrar Datos</li>
		<li><a href="<%=Configuracion.Jsp_Entrega%>?user=<%=idUsuario%>&list=<%=idAlumnos%>&boolean=borrarAlumnos" 
		onclick="BorrarDatosAlumnos();"> Borrar datos Alumnos </a></br></li>
		<li><a href="<%=Configuracion.Jsp_Entrega%>?user=<%=idUsuario%>&list=<%=idAlumnos%>&boolean=borrarPracticas" 
		onclick="BorrarDatosPracticas();"> Borrar datos Practica </a></br></li>
		<li>Cargar Datos</li>
		<li><a href="<%=Configuracion.Jsp_Entrega%>?user=<%=idUsuario%>&list=<%=idAlumnos%>&boolean=cargarAlumnos" 
		onclick="CargarDatosAlumnos();"> Cargar datos Alumnos </a></br></li>
		<li><a href="<%=Configuracion.Jsp_Entrega%>?user=<%=idUsuario%>&list=<%=idAlumnos%>&boolean=cargarPracticas" 
		onclick="CargarDatosPracticas();"> Cargar datos Practica </a></br></li>
	</ul>
	</li>
</ul>
<%}%>
</div>
<!-- end #sidebar --></div>
<!-- end #page -->

</body>
</html>