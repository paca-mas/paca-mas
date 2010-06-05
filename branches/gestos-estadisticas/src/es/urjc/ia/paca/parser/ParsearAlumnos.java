package es.urjc.ia.paca.parser;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import es.urjc.ia.paca.ontology.*;

	public class ParsearAlumnos {
	    
		public static Document dom;
	    public static List<Alumno> listAlumno = new ArrayList<Alumno>();
	    
	    //Parsea el archivo XML
	    public static List<Alumno> parsearArchivoXml(String fichero) {
	    	//RegistrarEstadisticaEvaluacion e = new RegistrarEstadisticaEvaluacion(null);
	    	// Obteher el objeto DocumentBuilderFactory
	    	DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
	    	try {
	    		// Usar DocumentBuilderFactory para crear un DocumentBuilder
	    		DocumentBuilder db = dbf.newDocumentBuilder();
	    		// Parsear a partir de un archivo
	    		dom = db.parse(fichero);
	    		System.out.println("parsearDocumento");
	    		listAlumno = parsearDocumento ();
	    	} 
	    	catch (ParserConfigurationException pce) {pce.printStackTrace(); } 
	    	catch (SAXException se) { se.printStackTrace(); } 
	    	catch (IOException ioe) { ioe.printStackTrace(); }
			return listAlumno;
	    }
	    
	    //Parsea el documento XML y extrae los datos
	    public static List<Alumno> parsearDocumento() {
	    	String username;
	    	String pass;

	    	// Obtener el documento raiz
	    	Element docEle = dom.getDocumentElement();
	    	System.out.println(docEle.getNodeName());

	    	NodeList listauser = docEle.getChildNodes();

	    	// Sacamos los nodos hijos <<nodo Test>> del nodo Practica
	    	for (int i = 0; i < listauser.getLength(); i++) {
	    		// Si el nodo es un Elemento y además es un Test
	    		if ( listauser.item(i).getNodeType()==Node.ELEMENT_NODE
	    				&& ((Element)listauser.item(i)).getTagName().equals("user")){
System.out.println(i);
					Element user = (Element) listauser.item(i);
	    			username = user.getAttribute("username");
					pass = user.getAttribute("pass");
					Alumno a = new Alumno();
					a.setIdentificador(username);
					a.setPassword(pass);
					listAlumno.add(a);
					System.out.println(username);
					System.out.println(pass);
	    		}
	    	}
	    	
	    	return listAlumno;
	    }
	}
