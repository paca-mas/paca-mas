
package es.urjc.ia.paca.auth.util;

 

// JAXP packages
import javax.xml.parsers.*;
import org.xml.sax.*;
import org.xml.sax.helpers.*;

import java.util.*;
import java.io.*;


/**
   Esta clase es un analizador SAX de ficheros XML validados para la DTD: autenticacion.dtd
   Ofrece un m�todo para obtener una tabla de identificadores-claves una vez termine la 
   inicializaci�n del fichero.
   @author Sergio Saugar Garc�a
 */
public class PasswordParser extends DefaultHandler {



    //La tabla Hash que contiene los identificadores.
    private Hashtable tabla;



    /**
      M�todo que atiende al evento de inicio de un documento XML.
    */
    public void startDocument() throws SAXException {
        tabla = new Hashtable();
    }



    /**
       M�todo que atiende al evento de inicio de un elemento XML.
    */
    public void startElement(String namespaceURI, String localName,
                             String rawName, Attributes atts)
	throws SAXException{
	
	// El primer atributo es el username y el segundo es la password.
	if (atts.getLength()!=0){
	    tabla.put(atts.getValue(0),atts.getValue(1));
	}
    }



    /**
      M�todo que atiende al evento de fin de un documento XML.
    */
    public void endDocument() throws SAXException {
	//Muestra por pantalla la tabla resultado.
	//System.out.println(tabla);
    }



    /**
	Constructor del analizador.
    */
    public PasswordParser() {
	
        // Creamos un JAXP SAXParserFactory.
        SAXParserFactory spf = SAXParserFactory.newInstance();

	// Creamos un XMLReader.
        XMLReader xmlReader = null;

        try {
            // Creamos un JAXP SAXParser
            SAXParser saxParser = spf.newSAXParser();

            // capturamos el SAX XMLReader
            xmlReader = saxParser.getXMLReader();

        } catch (Exception ex) {
            System.err.println(ex);
            System.exit(1);
        }

        // Configuramos el manejador de contenido del XMLReader
        xmlReader.setContentHandler(this);

        // Configuramos el manejador de errores.
        xmlReader.setErrorHandler(new ManejadorErrores(System.err));

        try {
            // El fichero debe estar localizado en el mismo lugar donde se inicia el agente.
	    File fichero = new File("correccion.xml");
	    FileInputStream flujo = new FileInputStream(fichero);
	    InputSource entradaXML = new InputSource(flujo);

            xmlReader.parse(entradaXML);

        } 
	catch (SAXException se) {
            System.err.println(se.getMessage());
            System.exit(1);
        } 
	catch (IOException ioe) {
            System.err.println(ioe);
            System.exit(1);
        } 
	catch (Exception exc){
	    System.err.println(exc);
	    System.exit(1);
	}
    }

    /**
       M�todo que devuelve la tabla que ha sido obtenida al analizar el documento.
    */
    public Hashtable getTabla(){
	return tabla;
    }



    /**
       Clase que maneja los distintos errores que pueden producir al analizar el documento.
    */
    private static class ManejadorErrores implements ErrorHandler {



        /** 
	    Flujo de salida.
	 */
        private PrintStream out;



	/**
	   Constructor.
	*/
        ManejadorErrores(PrintStream out) {
            this.out = out;
        }


        /**
	   Devuelve informaci�n sobre la excepci�n que ha ocurrido.
         */
        private String getParseExceptionInfo(SAXParseException spe) {
            String systemId = spe.getSystemId();
            if (systemId == null) {
                systemId = "null";
            }
            String info = "URI=" + systemId +
                " Line=" + spe.getLineNumber() +
                ": " + spe.getMessage();
            return info;
        }

        // Los siguientes m�todos son m�todos estandar de SAX ErrorHandler.
        // See SAX documentation for more info.
	
        
	public void warning(SAXParseException spe) throws SAXException {
            out.println("Warning: " + getParseExceptionInfo(spe));
        }
        
	
        public void error(SAXParseException spe) throws SAXException {
            String message = "Error: " + getParseExceptionInfo(spe);
            throw new SAXException(message);
        }
	
        public void fatalError(SAXParseException spe) throws SAXException {
            String message = "Fatal Error: " + getParseExceptionInfo(spe);
            throw new SAXException(message);
        }
    }
}
