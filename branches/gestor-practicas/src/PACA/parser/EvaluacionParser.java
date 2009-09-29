


package PACA.parser;
 

// JAXP packages
import java.io.IOException;
import java.io.PrintStream;
import java.io.StringReader;
import java.util.Hashtable;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.Attributes;
import org.xml.sax.ErrorHandler;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.DefaultHandler;

import PACA.agents.InterfazJSP;



/**
 * Clase que implementa un analizador de sintaxis para el mensaje de evaluacion. 
 * 
 * @author Sergio Saugar
 */

public class EvaluacionParser extends DefaultHandler {

    private InterfazJSP interfaz;
    private String eval;
    private String elemActual;
    private String descriptActual;
    private boolean desccaso;
    private Hashtable tablaCodigos;


    /**
       M�todo que inicializa la tabla de c�digos y les asigna un comentario
    */
    private void InicializaTabla(){

	tablaCodigos=new Hashtable();

	tablaCodigos.put("compilacion_correcta",
			 new String("La compilaci&oacute;n del test ha sido correcta."));
	tablaCodigos.put("error_compilacion",
			 new String("Hubo un error en tiempo de compilaci&oacute;n."));
	tablaCodigos.put("no_terminacion",
			 new String("La ejecuci&oacute;n no ha concluido despu&eacute;s de un cierto periodo de tiempo."));
	tablaCodigos.put("terminacion_anomala",
			 new String("La ejecuci&oacute;n del programa ha sido an&oacute;mala."));
	tablaCodigos.put("terminacion_correcta",
			 new String("La ejecuci&oacute;n de la prueba fue satisfactoria."));
	tablaCodigos.put("terminacion_incorrecta",
			 new String("La ejecuci&oacute;n del programa no produjo los resultados esperados."));	


	tablaCodigos.put("terminacion-correcta",
			 new String("La ejecuci&oacute;n de la prueba fue satisfactoria."));
	tablaCodigos.put("terminacion_incorrecta",
			 new String("La ejecuci&oacute;n del programa no produjo los resultados esperados."));	


    }

    private String buscaTabla(String id){
	if (tablaCodigos.containsKey(id))
	    return (String)tablaCodigos.get(id);
	else
	    return id;
    }

    /**
       M�todo invocado por el parser al comenzar el documento.
    */
    public void startDocument() throws SAXException {
	//Iniciamos la cadena de evaluacion.
	eval="";
	elemActual="";
	desccaso=false;
    }


    /**
       M�todo invocado por el parser al encontrar caracteres en el documento.
    */
    public void characters(char[] ch, int start, int leng){

	boolean noHayTexto=true;
	int i = start;
	int cont = 0;
	
	//Ignoramos los caracteres en blanco.
	while(cont<leng && noHayTexto){
	    noHayTexto=(Character.isWhitespace(ch[i+cont]));
	    cont++;
	};

	if((!noHayTexto)){
	    //Es una Descripcion.
	    if (desccaso){
		eval+="  <P class=\"err\">\n";
	    }
	    else{
		eval+="  <P class=\"desc\">\n";
	    };
	    eval+=new String(ch,start,leng)+"\n  </P>\n";  
	}
    }



    /**
       M�todo invocado por el parser al encontrar una etiqueta de inicio de elemento.
    */
    public void startElement(String namespaceURI, String localName,
                             String rawName, Attributes atts)
	throws SAXException{
	
	// System.out.println("El elemento que llega es: "+rawName);
	// System.out.println("El elemento RAW que llega es: "+rawName);

	//Empieza una practica
	if (rawName.equals("Practica")){
	    // System.out.println("llega una practica");
	    elemActual=rawName;
	    String fecha = atts.getValue(0);
	    String usuario = atts.getValue(1);
	    String identPractica = atts.getValue(2);

	    eval+="  <div class=\"cabecera\">\n <h3 class=\"practica\">"
		+"Evaluando:&nbsp;&nbsp;&nbsp;"
		+identPractica	
		+"<BR>Fecha:&nbsp;&nbsp;&nbsp;"
		+fecha
		+"<br>Usuario:&nbsp;&nbsp;&nbsp;"+
		interfaz.getAlumnoID()
		+"</h3>\n</div>\n<BR>\n";
	}
	
	if (rawName.equals("Test")){
	    // System.out.println("llega un test");
	    String identTest = atts.getValue(0);
	    eval+="  <div class=\"test\"><h4 class=\"test\">"
		+identTest+"</h4>\n";
	    // System.out.println("El resultado es: "+eval);
	}

	if (rawName.equals("EvaluacionTest")){
	    // System.out.println("llega una evaluaciontest");
	    String codigoEvaluacionTest = atts.getValue(0);
	    if(!codigoEvaluacionTest.equals("compilacion_correcta")){
		eval+="  <h5 class=\"evaluaciontest\">"+
		    buscaTabla(codigoEvaluacionTest)+"</h5>\n";
	    // System.out.println("El resultado es: "+eval);
	    }
	}
	
	if (rawName.equals("Caso")){
	    // System.out.println("llega un caso");
	    String identCaso = atts.getValue(0);
	    eval+="   <P class=\"caso\"><h5 class=\"caso\">"+identCaso+"</h5></p>\n  <div class=\"caso\">\n";
	    // System.out.println("El resultado es: "+eval);
	}

	if (rawName.equals("EvaluacionCaso")){
	    // System.out.println("llega una evaluacioncaso");
	    String codigoEvaluacionCaso = atts.getValue(0);
	    eval+="  <h5 class=\"evaluacioncaso\">"
		+(String)buscaTabla(codigoEvaluacionCaso)+"</h5>\n";
	    desccaso=true;
	    // System.out.println("El resultado es: "+eval);
	}

	if (rawName.equals("Nota")){
	    // System.out.println("llega una nota");
	    String nota = atts.getValue(0);
	    eval+="  <h3 class=\"nota\">Nota: "+nota+"</h3>\n";
	    // System.out.println("El resultado es: "+eval);
	}

	if (rawName.equals("Descripcion")){
		}

    }


    
    /**
       M�todo invocado por el parser al finalizar cada elemento.
    */
    public void endElement(String namespaceURI,	
			   String localName, // simple name	
			   String qName  // qualified name	
			   ) throws SAXException{
	
	// System.out.println("El elemento que termina es: "+qName);
	// System.out.println("El elemento qName que termina es: "+qName);

	if (qName.equals("Practica")){
	}
	
	if (qName.equals("Test")){
	    eval+="  </div>\n";
	}

	if (qName.equals("EvaluacionTest")){
	}

	if (qName.equals("Caso")){
	    eval+="  </div>\n  <BR>\n";
	}

	if (qName.equals("EvaluacionCaso")){
	    desccaso=false;
	}

	if (qName.equals("Nota")){
	}
	
	if (qName.equals("Descripcion")){
	}
    }




    /**
       M�todo invocado al finalizar el documento.
    */
    public void endDocument() throws SAXException {
	eval+="";
	// System.out.println("El documento parseado a HTML es:"+eval);
    }



    /**
       Constructor del Parser.
    */
    public EvaluacionParser(String cadenaXML, InterfazJSP interf) {
	
	this.interfaz=interf;

	this.InicializaTabla();

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
	    // System.out.println("Error creando el reader");
            // System.out.println(ex);
            System.exit(1);
        }

        // Configuramos el manejador de contenido del XMLReader
        xmlReader.setContentHandler(this);

        // Configuramos el manejador de errores.
        xmlReader.setErrorHandler(new ManejadorErrores(System.err));

        try {

            // Empezar a parsear.
	    StringReader flujo = new StringReader(cadenaXML);

	    //File fichero = new File("pruevaEvaluacion.xml");
	    //FileInputStream flujo = new FileInputStream(fichero);
	    InputSource entradaXML = new InputSource(flujo);

            xmlReader.parse(entradaXML);

        } 
	catch (SAXException se) {
	    // System.out.println("Error al parsear");
            se.printStackTrace();
            System.exit(1);
        }
	catch (IOException ioe) {
	    // System.out.println("Error de io");
            ioe.printStackTrace();
            System.exit(1);
        }
	catch (Exception exc){
	    // System.out.println("Excepcion en el parser");
	    exc.printStackTrace();
	    System.exit(1);
	}
	
    }

    
    /**
       M�todo que devuelve el resultado de la evaluaci�n.
    */
    public String getEvaluacion(){
	return eval;
    }



    /**
       Manejador de errores.
    */
    private static class ManejadorErrores implements ErrorHandler {


        /** 
	    OutPut 
	 */
        private PrintStream out;

        ManejadorErrores(PrintStream out) {
            this.out = out;
        }



        /**
         * Devuelve informaci�n sobre la excepci�n.
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
