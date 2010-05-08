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
import es.urjc.ia.paca.ontology.EstadisticaEvaluacionCaso;

public class ProcesarXML {
    
	public static Document dom;
    public static List<EstadisticaEvaluacionCaso> listEvaluacion = new ArrayList<EstadisticaEvaluacionCaso>();
    
    //Parsea el archivo XML
    public static List<EstadisticaEvaluacionCaso> parsearArchivoXml(String fichero) {
    	List<EstadisticaEvaluacionCaso> List = new ArrayList<EstadisticaEvaluacionCaso>();
    	// Obteher el objeto DocumentBuilderFactory
    	DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
    	try {
    		// Usar DocumentBuilderFactory para crear un DocumentBuilder
    		DocumentBuilder db = dbf.newDocumentBuilder();
    		// Parsear a partir de un archivo
    		dom = db.parse(fichero);
    		List = parsearDocumento ();
    	} 
    	catch (ParserConfigurationException pce) {pce.printStackTrace(); } 
    	catch (SAXException se) { se.printStackTrace(); } 
    	catch (IOException ioe) { ioe.printStackTrace(); }
		return List;
    }
    
    //Parsea el documento XML y extrae los datos
    public static List<EstadisticaEvaluacionCaso> parsearDocumento() {
    	String fecha;
    	String iduser;
    	String idpractica;
    	String idtest;
    	String idcaso;
    	String evaluacioncaso;

    	// Obtener el documento raiz
    	Element docEle = dom.getDocumentElement();
    	// Practica
    	fecha = docEle.getAttribute("fecha");
    	iduser = docEle.getAttribute("usuario");
    	idpractica = docEle.getAttribute("identificador");

    	NodeList listatest = docEle.getChildNodes();

    	// Sacamos los nodos hijos <<nodo Test>> del nodo Practica
    	for (int i = 0; i < listatest.getLength(); i++) {
    		// Si el nodo es un Elemento y además es un Test
    		if ( listatest.item(i).getNodeType()==Node.ELEMENT_NODE
    				&& ((Element)listatest.item(i)).getTagName().equals("Test")){

    			Element test = (Element) listatest.item(i);
    			NodeList listacaso = test.getChildNodes();

    			// Para cada nodo Test sacamos sus hijos <<Caso>>
    			for (int k = 0; k < listacaso.getLength(); k++) {
    				if ( listacaso.item(k).getNodeType()==Node.ELEMENT_NODE
    						&& ((Element)listacaso.item(k)).getTagName().equals("Caso")){

    					Element caso = (Element) listacaso.item(k);
    					NodeList listaevaluacion = caso.getChildNodes();

    					// Sacamos los nodos hijos <<EvaluacionCaso>>
    					for (int l = 0; l < listaevaluacion.getLength(); l++) {
    						if (listaevaluacion.item(l).getNodeType()==Node.ELEMENT_NODE
    								&& ((Element)listaevaluacion.item(l)).getTagName().equals("EvaluacionCaso")){
    
    							Element evalcaso = (Element) listaevaluacion.item(l);
    							
    							// Sacamos los datos necesarios para rellenar el tipo evaluacion
    							// fecha + iduser + idpractica + idtest + idcaso + evaluacioncaso
    							idtest = test.getAttribute("identificador");
    							idcaso = caso.getAttribute("identificador");
    							evaluacioncaso = evalcaso.getAttribute("codigoEvaluacionCaso");
    							EstadisticaEvaluacionCaso e = new EstadisticaEvaluacionCaso (iduser, idpractica, idtest, idcaso,  evaluacioncaso);
    							listEvaluacion.add(e);
    						}
    					}
    				}
    			}
    		}
    	}
    	return listEvaluacion;
    }
}