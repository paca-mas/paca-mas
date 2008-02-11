
package PACA.agents;



//Paquetes JAVA.
import jade.content.Concept;
import jade.content.abs.AbsAggregate;
import jade.content.abs.AbsConcept;
import jade.content.abs.AbsContentElement;
import jade.content.abs.AbsIRE;
import jade.content.abs.AbsObject;
import jade.content.abs.AbsPredicate;
import jade.content.abs.AbsVariable;
import jade.content.lang.Codec;
import jade.content.lang.Codec.CodecException;
import jade.content.lang.sl.SL1Vocabulary;
import jade.content.lang.sl.SL2Vocabulary;
import jade.content.lang.sl.SLCodec;
import jade.content.lang.sl.SLVocabulary;
import jade.content.onto.BasicOntology;
import jade.content.onto.Ontology;
import jade.content.onto.OntologyException;
import jade.content.onto.basic.Action;
import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.Behaviour;
import jade.core.behaviours.OneShotBehaviour;
import jade.domain.DFService;
import jade.domain.FIPAException;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import jade.util.leap.Serializable;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

import PACA.ontology.Alumno;
import PACA.ontology.Corrector;
import PACA.ontology.Corrige;
import PACA.ontology.EvaluacionPractica;
import PACA.ontology.FicheroFuentes;
import PACA.ontology.Interactua;
import PACA.ontology.Practica;
import PACA.ontology.ResultadoEvaluacion;
import PACA.ontology.Test;
import PACA.ontology.Tests;
import PACA.ontology.pacaOntology;
import PACA.ontology.Fichero.FuentesPrograma;
import auth.ontology.Autenticado;
import auth.ontology.AuthOntology;
import auth.ontology.Usuario;
import auth.util.Testigo;

import PACA.util.*;




/** 
    Este agente contiene la comunicación necesaria que debe tener un agente usuario
    humano para interaccionar con el resto de agentes de ésta plataforma. Es decir,
    este agente encapsula los protocolos definidos para el sistema.
    @author Sergio Saugar García
    
 */
public class Interfaz extends Agent {

	private boolean debug = true;
	private boolean debug2 = false;
	private boolean debug3 = false; 
	
	
	private Codec codec = new SLCodec();
	
	//	Nombre de la ontologia
	private Ontology ontologia = AuthOntology.getInstance();
	
	private Ontology PACAOntology = pacaOntology.getInstance();

	/**
       ID del alumno
	 */
	private String alumnoID;

	/**
       Password del Alumno;
	 */
	private String alumnoPass;

	/**
       AID de esta instancia del agente Interfaz.
	 */
	private AID miAID;

	/**
       AID del agente corrector de prácticas.
	 */
	private AID correctorAID;

	/**
       Variable booleana cerrojo para evitar que ninguna función se ejecute
       antes de que termine de ejecutarse el método "setup"
	 */
	private boolean terminadoSetup = false;
	
	/** 
	Nombre del agente corrector
	 */
	//private String AgenteCorrector = "corrector";
	private AID AgenteCorrector;

	/**
       Nombre del agente autenticador
	 */
	private String AgenteAutenticador = "autenticador";

	/**
       El identificador de la última práctica solicitada
	 */
	public String ultimaPractica;

	/**
       Array con los nombres de los ficheros de la última
       práctica solicitada
	 */
	public String[] ficherosUltimaPractica;

	/**
       Array con los identificadores de los test de la última
       práctica solicitada
	 */
	public String[] TestUltimaPractica;

	/**
       Array con los identificadores de los test posibles para una
       práctica solicitada.
	 */
	public String[] TestPosiblesPractica;
	
	
	public int Paso;



	/**
       Constructor.
	 */
	public Interfaz() {

		miAID = getAID();
	}
	
	
	
	public String usuario;
	public String passUsu;
	
	public void setUsuarioAux (String usu){
		this.usuario = usu;
	}
	
	public void setPasswordAux (String pas){
		this.passUsu = pas;
	}

	public String getUsuarioAux(){
		return this.usuario;
	}
	
	public String getPasswordAux(){
		return this.passUsu;
	}


	/**
       Este método asigna el identificador del alumno que representamos.
	 */
	public void setAlumnoID(String id){
		this.alumnoID=id;
	}

	/**
       Este método asigna el password que utilizara el alumno que representamos.
	 */
	public void setAlumnoPass(String pass){
		this.alumnoPass=pass;
	}

	/**
       Este método devuelve el identificador del alumno que representamos.
	 */
	public String getAlumnoID(){
		return this.alumnoID;
	}

	/**
       Este método devuelve el password del alumno que representamos.
	 */
	public String getAlumnoPass(){
		return this.alumnoPass;
	}
	
	/**
	 * Este método devuelve el Agente Corrector que hemos elegido
	 **/
	
	public AID getAgenteCorrector(){
		return this.AgenteCorrector;
	}

	/**
	 * Este método asigna el Agente Corrector que hemos elegido 
	 */
	public void setAgenteCorrector(AID agenteCorr){
		this.AgenteCorrector=agenteCorr;
	}
	
	/**
	 * Creamos la tabla Hash "almacenCorrec" para guardar los correctores que hemos utilizado
	 */
	public Hashtable<AID,Integer> almacenCorrec = new Hashtable<AID,Integer>();
	
	
	/**
	 * Usado guarda el numero de veces que se ha utilizado un corrector
	 */
	public Integer usado;
	
	 
	/**
       Este método se ejecuta al iniciar el agente y es el encargado de configurarlo.
	 */
	protected void setup() {
		
		//this.setEnabledO2ACommunication(true, 0);
		
		//addBehaviour(new InicializaObjeto());

		// Register the codec for the SL0 language
		getContentManager().registerLanguage(codec);

		// Register the ontology used by this application
		getContentManager().registerOntology(AuthOntology.getInstance());
		getContentManager().registerOntology(pacaOntology.getInstance());
		
		//Para el debug
		if (debug){
			System.out.println("Comienza el Interfaz");
		}
		terminadoSetup=true;
		
		if (debug){
			System.out.println("Termina Setup() de Interfaz");
		}
		
		//this.setEnabledO2ACommunication(true, 0);
		
		//addBehaviour(new InicializaObjeto());
		
			  		
	}


	/*
       Antes de ejecutarse ninguna función, debe terminar
       de ejecutarse el procedimiento "setup", así que pondremos un
       pequeño cerrojo con una variable booleana. 
	 */



	/**
       Encapsula la comunicación necesaria para realizar el protocolo de autenticación definido.
       @return True o False dependiendo de si se ha producido de forma exitosa o no la autenticacion.
	 */ 
	public final boolean doAutenticacion(String user, String pass){
		
		// Cerrojo para que no se ejecute antes de que termine el setup
		while (!terminadoSetup) {
		}	

		ACLMessage respuesta = new ACLMessage(ACLMessage.QUERY_IF);
		respuesta.setLanguage(codec.getName());
		respuesta.setOntology(AuthOntology.ONTOLOGY_NAME);
		respuesta.addReceiver(new AID(AgenteAutenticador, AID.ISLOCALNAME));

				
		// Creamos el predicado.
		Autenticado aut = new Autenticado();
		Usuario user2 = new Usuario();
		
		// Creamos el usuario
		user2.setUser_id(user);
		user2.setPassword(pass);
		

		aut.setUsuario(user2);

		
		try{
			//Mandamos el predicado "aut"
			getContentManager().fillContent(respuesta,aut);
			send(respuesta);
		}
		catch (CodecException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		
		catch (OntologyException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		// Esperamos la respuesta.
		respuesta=blockingReceive();
		
		if (debug){
			System.out.println("----------");
			System.out.println(respuesta.toString());
			System.out.println("----------");
		}

		try{

			// Ahora iremos cogiendo los objetos creados de la ontologia.
			AbsContentElement listaObj2 = null;
			listaObj2 = getContentManager().extractAbsContent(respuesta);
			String tipoObjetoContenido = listaObj2.getTypeName();
			
			if (tipoObjetoContenido.equals(SL1Vocabulary.NOT)){
				return(false);
			}
			else{
				setAlumnoID(user);
				setAlumnoPass(pass);
				return(true);
			}
		}
		catch(Exception ex){
			return(false);
		}
	}


	/**
       Este método encapsula la interacción necesaria para obtener los identificadores de las 
       prácticas disponibles en el sistema.
       @return Devuelve un array con los  nombres de las prácticas disponibles.
	 */

	public final String[] doPeticion(){

		if (debug){
			System.out.println("Interfaz.java doPeticion()");
		}

		// Cerrojo para que no se ejecute antes de que termine el setup
		while (!terminadoSetup) {
		}

		AID aaaAgente = getAgenteCorrector();
		ACLMessage msg_in;
		ACLMessage msg_out = new ACLMessage(ACLMessage.QUERY_REF);
		//msg_out.addReceiver(new AID(AgenteCorrector, AID.ISLOCALNAME));
		msg_out.addReceiver(aaaAgente);
		msg_out.setLanguage(codec.getName());
		msg_out.setOntology(pacaOntology.NAME);

		String[] retornable = new String[0];

		// Generamos el mensaje para enviar

		
		Practica pract = new Practica();
		pract.setId("?practica");
		pract.setDescripcion("");
		Corrector correc = new Corrector();
		correc.setId(aaaAgente.getName());
		//correc.setId("corrector");
		
		try {
			
			//Convertimos la practica a un objecto abstracto
			AbsConcept AbsPract = (AbsConcept) PACAOntology.fromObject(pract);
			
			//Convertimos el correcto a un objecto abstracto
			AbsConcept AbsCorrec = (AbsConcept) PACAOntology.fromObject(correc);
						
			//Creamos el predicado abstracto "CORRIGE"
			AbsPredicate AbsPredicado= new AbsPredicate(pacaOntology.CORRIGE);
			AbsPredicado.set(pacaOntology.PRACTICA, AbsPract);
			AbsPredicado.set(pacaOntology.CORRECTOR, AbsCorrec);
						
			//Creamos la variable que queremos pedir, en este caso "?practica"
			AbsVariable x = new AbsVariable("practica",pacaOntology.PRACTICA);
		
			//Creamos el IRE con la variable "x" y el predicado "CORRIGE"
			AbsIRE qrall = new AbsIRE(SL2Vocabulary.ALL);
			qrall.setVariable(x);
			qrall.setProposition(AbsPredicado);
						
			//Mandamos el mensaje
			getContentManager().fillContent(msg_out, qrall);
			send(msg_out);
		} 
				
		catch (OntologyException e1) {
			// TODO Auto-generated catch block
			System.out.println("INTERFAZ.JAVA: NUEVO TRY---->Salto excepcion");
			e1.printStackTrace();
		} 
		 
		catch (CodecException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		System.out.println("Esperamos a que llegue algo");
		
		// Rec. mensaje de respuesta
		msg_in = blockingReceive();
		if (debug){
			System.out.println("---------- LLEGADA DE LAS PRACTICAS DISPONIBLES --------------");
			System.out.println(msg_in.toString());
			System.out.println("----------------- FIN LLEGADA DE PRACTICAS -------------------");
		}

		// Procesamos la respuesta
		try {
						
			AbsContentElement l_in = null;
			l_in = getContentManager().extractAbsContent(msg_in);
			
			//Sacamos las practicas que vienen en el mensaje como conceptos abstractos
			AbsAggregate Practicas = (AbsAggregate) l_in.getAbsObject(SLVocabulary.EQUALS_RIGHT);
						
			Practica p = new Practica();
			retornable = new String[Practicas.size()];
			for (int i=0; i < Practicas.size(); i++) {
				//Pasamos de concepto abstracto a objeto "real", en este caso son practicas
				p = (Practica) PACAOntology.toObject(Practicas.get(i));
												
				retornable[i] = p.getId();
			}
						
		}
		catch (OntologyException oe){
			//printError(myAgent.getLocalName()+" getRoleName() unsucceeded. Reason:" + oe.getMessage());
			System.out.println("Excepcion en ontologia");
			oe.printStackTrace();
		}   
		catch (java.lang.NullPointerException e) {
			System.out.println("Empty message");
			e.printStackTrace();
		}
		catch (Exception e) {
			// Si obtenemos alguna excepción de error, directamente no
			// ofrecemos las practicas ...
			retornable = new String[2];
			retornable[0]="Error en la obtención de las prácticas";
			retornable[1]= msg_in.toString();
		}

		return retornable;
	}



	/**
       Dado un identificador de prácticas, se solicitan los tests correpondientes a la
       misma.
       @param IdPractica Identificador de la práctica de la que se desea obtener los tests configurados.
       @return Devuelve un array con los  nombres de los test de la práctica.
	 */

	public final String[] doPeticionTestPractica(String IdPractica){

		if (debug){
			System.out.println("Interfaz.java doPetcionTestPractica()");
		}

		// Cerrojo para que no se ejecute antes de que termine el setup
		while (!terminadoSetup) {
		}

		// Nos guardamos la última práctica solicitada
		ultimaPractica = IdPractica;
				
		AID aaaAgente = getAgenteCorrector();

		ACLMessage msg_in;
		ACLMessage msg_out = new ACLMessage(ACLMessage.QUERY_REF);
		//msg_out.addReceiver(new AID(AgenteCorrector, AID.ISLOCALNAME));
		msg_out.addReceiver(aaaAgente);
		msg_out.setLanguage(codec.getName());
		msg_out.setOntology(pacaOntology.NAME);

		String[] retornable = new String[0];
		String[] posiblesID = new String[0];

		// Generamos el mensaje para enviar


		Corrige cor = new Corrige();
		Practica pract = new Practica();
		pract.setId(IdPractica);
		pract.setDescripcion("");
		PACA.ontology.Corrector correc = new PACA.ontology.Corrector();
		//correc.setId("corrector");
		correc.setId(aaaAgente.getName());
		cor.setPractica(pract);
		cor.setCorrector(correc);
		
		Test te = new Test();
		te.setId("?test");
		te.setDescripcion("");
		
						
		try {
			//Convertimos la practica a un concepto abstracto
			AbsConcept AbsPract = (AbsConcept) PACAOntology.fromObject(pract);
			
			//Convertimo el corrector a un concepto abstracto
			AbsConcept AbsCorrec = (AbsConcept) PACAOntology.fromObject(correc);
			
			//Convertimos el test a un concepto abstracto
			AbsConcept AbsTest = (AbsConcept) PACAOntology.fromObject(te);
			
			//Creamos el predicado CORRIGE de forma abstracta utilizando los conceptos abstractos creados anteriormente
			AbsPredicate AbsCorrige = new AbsPredicate(pacaOntology.CORRIGE);
			AbsCorrige.set(pacaOntology.PRACTICA, AbsPract);
			AbsCorrige.set(pacaOntology.CORRECTOR, AbsCorrec);
						
			//Creamos el predicado TESTS de forma abstracta utilizando los conceptos abstractos creados anteriormente
			AbsPredicate AbsTests = new AbsPredicate(pacaOntology.TESTS);
			AbsTests.set(pacaOntology.PRACTICA, AbsPract);
			AbsTests.set(pacaOntology.TEST, AbsTest);
						
			AbsVariable x = new AbsVariable("test",pacaOntology.TEST);
			
			AbsPredicate and1 = new AbsPredicate(SL1Vocabulary.AND);
			and1.set(SL1Vocabulary.AND_LEFT, AbsCorrige);
			and1.set(SL2Vocabulary.AND_RIGHT, AbsTests);
		
			
			AbsIRE qrall = new AbsIRE(SL2Vocabulary.ALL);
			qrall.setVariable(x);
			qrall.setProposition(and1);
						
			getContentManager().fillContent(msg_out, qrall);
			send(msg_out);
					
		} 
				
		catch (OntologyException e1) {
			// TODO Auto-generated catch block
			System.out.println("INTERFAZ.JAVA: NUEVO TRY TESTS---->Salto excepcion");
			e1.printStackTrace();
		} 
		 
		catch (CodecException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		

		msg_in = blockingReceive();

		if (debug){
			System.out.println("Nos llega la respuesta con los test");
			System.out.println(msg_in);
		}

		// Procesamos la respuesta

		try {

			/*
			for (int i=0; i < tests.size(); i++) {
				retornable[j++] = ((Test)tests.get(i)).getId();
				posiblesID[i]=((Test)tests.get(i)).getId();
				retornable[j++] =  ((Test)tests.get(i)).getDescripcion();
			}
			*/
			AbsContentElement l_in = null;
			l_in = getContentManager().extractAbsContent(msg_in);
			
			//Sacamos los tests que vienen en el mensaje como conceptos abstractos
			AbsAggregate ListaTest = (AbsAggregate) l_in.getAbsObject(SLVocabulary.EQUALS_RIGHT);
					
			Test t = new Test();
			retornable = new String[ListaTest.size()*2];
			posiblesID = new String[ListaTest.size()];
			int j=0;
			for (int i=0; i < ListaTest.size(); i++) {
				//Pasamos de concepto abstracto a objeto "real", en este caso son tests
				t = (Test) PACAOntology.toObject(ListaTest.get(i));
																
				retornable[j++] = t.getId();
				posiblesID[i] = t.getId();
				retornable[j++] = t.getDescripcion();
				
			}
			
		}
		catch (Exception e) {
			// Si obtenemos alguna excepción de error, directamente no
			// ofrecemos los tests ...
			retornable = new String[2];
			retornable[0]="Error en la obtención de los tests";
			retornable[1]= "Error:" + e.toString();
		}
		TestPosiblesPractica = posiblesID;
		return retornable;
	}


	/**
       Este método encapsula la comunicación necesaria para obtener los nombres de los ficheros
       que son necesarios para los tests que han sido seleccionados.
       @param IdTest Tests seleccionados por el usuario.
       @return Array con el identificador de los ficheros necesarios para evaluar los tests.
	 */
	public final String[] doFicherosPractica(String[] IdTest) {

		// Cerrojo para que no se ejecute antes de que termine el setup
		while (!terminadoSetup) {
		}

		String IdPractica  = ultimaPractica;

		// Y nos guardamos los últimos test solicitados
		TestUltimaPractica = IdTest;
		
		AID aaaAgente = getAgenteCorrector();

		ACLMessage msg_in;
		ACLMessage msg_out = new ACLMessage(ACLMessage.QUERY_REF);
		//msg_out.addReceiver(new AID(AgenteCorrector, AID.ISLOCALNAME));
		msg_out.addReceiver(aaaAgente);
		msg_out.setLanguage(codec.getName());
		msg_out.setOntology(pacaOntology.NAME);

		String[] retornable = new String[0];

		// Leemos la practica y pedimos los ficheros
		// necesarios

		try {
			
			Corrige cor = new Corrige();
			Practica pract = new Practica();
			pract.setId(IdPractica);
			pract.setDescripcion("");

			PACA.ontology.Corrector correc = new PACA.ontology.Corrector();
			//correc.setId("corrector");
			correc.setId(aaaAgente.getName());
			cor.setPractica(pract);
			cor.setCorrector(correc);

						
			FuentesPrograma fp = new FuentesPrograma();
			fp.setNombre("?nombre");
			fp.setContenido("");

									
			//Convertimos el corrector a un objecto abstracto
			AbsConcept AbsCorrec = (AbsConcept) PACAOntology.fromObject(correc);
						
			//Convertimos la practica a un objecto abstracto
			AbsConcept AbsPract = (AbsConcept) PACAOntology.fromObject(pract);
			
			//Creamos el predicado abstracto "CORRIGE"
			AbsPredicate AbsCor = new AbsPredicate(pacaOntology.CORRIGE);
			AbsCor.set(pacaOntology.PRACTICA, AbsPract);
			AbsCor.set(pacaOntology.CORRECTOR, AbsCorrec);
						
			AbsPredicate and2 = new AbsPredicate(SL1Vocabulary.AND);
			//and2.set_0(listaTests);
			//and2.set_1(ff);
			
			FicheroFuentes ff = new FicheroFuentes();
			Test te1 = new Test();
			te1.setId(IdTest[0]);
			te1.setDescripcion("");
			ff.setTest(te1);
			ff.setFuentesPrograma(fp);
			
			//Pasamos el predicado FicheroFuentes a predicado abstracto
			AbsPredicate Absff = (AbsPredicate) PACAOntology.fromObject(ff);
						
			and2 = InsertarTestPedidos(IdTest, AbsPract, Absff);
			
			
			AbsPredicate and1 = new AbsPredicate(SL1Vocabulary.AND);
			//and1.set_0(cor);
			//and1.set_1(and2);
			and1.set(SL1Vocabulary.AND_LEFT, AbsCor);
			and1.set(SL1Vocabulary.AND_RIGHT, and2);
			
			
			//Modificacion Carlos
			AbsIRE qrall = new AbsIRE(SL2Vocabulary.ALL);
			AbsVariable x = new AbsVariable("nombre",pacaOntology.FICHEROFUENTES);
			qrall.setVariable(x);
			qrall.setProposition(and1);
								
			getContentManager().fillContent(msg_out, qrall);
			send(msg_out);

		}
		
		catch (OntologyException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		
		catch (CodecException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		catch (Exception e){
			System.out.println("EXCEPCIONNNNNNNNNNNNNNNNN");
			e.printStackTrace();
		}
		


		msg_in = blockingReceive();
		if (debug2){
			System.out.println("-------------------------");
			System.out.println(msg_in.toString());
			System.out.println("-------------------------");
		}

		// Procesamos la respuesta

		try {
			if (debug2){
				System.out.println("Parece que nos ha llegado algo... asi que lo procesamos");
			}
			
			AbsContentElement l_in = null;
			l_in = getContentManager().extractAbsContent(msg_in);
						
			//Sacamos los ficheros que vienen en el mensaje como conceptos abstractos
			AbsAggregate Ficheros = (AbsAggregate) l_in.getAbsObject(SLVocabulary.EQUALS_RIGHT);
			
			// Pasamos la lista a un String[]
			FuentesPrograma fp;
			retornable = new String[Ficheros.size()];
			for (int i=0; i < Ficheros.size(); i++) {
				fp = (FuentesPrograma) PACAOntology.toObject(Ficheros.get(i));
				retornable[i] = fp.getNombre();
			}
		}
		catch (Exception e) {
			ficherosUltimaPractica = retornable;
			return retornable;
		}

		ficherosUltimaPractica = retornable;
		return retornable;
	}


	/**
       Encapsula la interacción necesaria para realizar la evaluación de ciertos ficheros, pasándolos
       los tests que fueron seleccionados. Posteriormente, se obtiene el texto de la evaluación.
       Las práctica a corregir está almacenada en <code>ultimaPractica</code> 
       (que se actualiza con cada consulta de ficheros) y los nombres de 
       los ficheros en <code>ficherosUltimaPractica</code> (igualmente 
       actualizados en cada consulta de ficheros de una práctica). 
       @param ContenidoFicheros El contenido de los ficheros de la evaluación.     
       Estos ficheros deben tener una correspondencia con los nombre de ficheros en <code>ficherosUltimaPractica</code>
       @return El texto proporcionado por el corrector
	 */
	public final String doCorreccion(String[] contenidoFicheros) {


		// Cerrojo para que no se ejecute antes de que termine el setup
		while (!terminadoSetup) {
		}

		AID aaaAgente = getAgenteCorrector();
		
		ACLMessage msg_in;
		ACLMessage msg_out = new ACLMessage(ACLMessage.QUERY_REF);
		//msg_out.addReceiver(new AID(AgenteCorrector, AID.ISLOCALNAME));
		msg_out.addReceiver(aaaAgente);
		msg_out.setLanguage(codec.getName());
		msg_out.setOntology(pacaOntology.NAME);

		String retornable = new String("Evaluación no efectuada");

		try {	

			System.out.println("DOCORRECCIONNNNNNNNNNNNNNNNNNNNNNNNNNNNN");		

			//Corrige cor = new Corrige();
			//Practica pract = new Practica(ultimaPractica);
			Practica pract = new Practica();
			pract.setId(ultimaPractica);
			pract.setDescripcion("");

			PACA.ontology.Corrector correc = new PACA.ontology.Corrector();
			correc.setId(aaaAgente.getName());
			//cor.setPractica(pract);
			//cor.setCorrector(correc);

			// Los Test
			/*
			Test te = new Test();
			Tests tes = new Tests();

			//Lista listaTes = new Lista();
			//Modificacion Carlos
			ArrayList listaTes = new ArrayList();

			for (int i=0; i < TestUltimaPractica.length; i++) {
				te = new Test();
				tes = new Tests();
				te.setId(TestUltimaPractica[i]);
				tes.setPractica(pract);
				tes.setTest(te);
				//listaTes.add_0(tes);
				//Modificacion Carlos
				listaTes.add(tes);
			}
			*/
			
			//EvaluacionPractica evap = new EvaluacionPractica();
			//evap.setTextoEvaluacion("?evaluacion");
			
			

			// Incluir los ficheros y rellenado de los ands

			//FicheroFuentes ff;
			//FuentesPrograma fp;
			//Lista listaFP = new Lista();
			//Modificacion Carlos
			//ArrayList listaFP = new ArrayList();
			Test te = new Test();
			te.setId("?allTest");
			te.setDescripcion("");

			/*
			for (int i=0; i < ficherosUltimaPractica.length; i++) {

				fp = new FuentesPrograma();
				fp.setNombre(ficherosUltimaPractica[i]);

				fp.setContenido(contenidoFicheros[i]);

				ff = new FicheroFuentes();
				ff.setTest(te);
				ff.setFuentesPrograma(fp);

				//listaFP.add_0(ff);
				//Modificacion Carlos
				listaFP.add(ff);
			}
			*/
			
			//Convertimos pract en concepto abstracto
			AbsConcept AbsPract = (AbsConcept) PACAOntology.fromObject(pract);
			
			//Convertimos correc en concepto abstracto
			AbsConcept AbsCorr = (AbsConcept) PACAOntology.fromObject(correc);
			
			System.out.println("Creamos los objetso Abstractos practica y corrector");
			
			Alumno al = new Alumno(alumnoID, alumnoPass);
			AbsConcept Absal = (AbsConcept) PACAOntology.fromObject(al);
			
			//Creamos el predicado abstracto Evaluacion Practica
			AbsPredicate AbsEvap = new AbsPredicate(pacaOntology.EVALUAPRACTICA);
			AbsEvap.set(pacaOntology.EVALUACIONPRACTICA_TEXTO, "?evaluacion");
			AbsEvap.set(pacaOntology.ALUMNO,Absal);
			System.out.println("Creamos la EVALUACION PRACTICA");
			
			//Creamos el predicado abstracto Corrige con AbsPract y AbsCorr
			AbsPredicate AbsCor = new AbsPredicate(pacaOntology.CORRIGE);
			AbsCor.set(pacaOntology.PRACTICA, AbsPract);
			AbsCor.set(pacaOntology.CORRECTOR, AbsCorr);
			
			System.out.println("Creamos el predicado abstracto CORRIGE");

			
			AbsPredicate and1 = new AbsPredicate(SL1Vocabulary.AND);
			AbsPredicate and2 = new AbsPredicate(SL1Vocabulary.AND);
			AbsPredicate and3 = new AbsPredicate(SL1Vocabulary.AND);
			AbsPredicate and4 = new AbsPredicate(SL1Vocabulary.AND);
			
			//get1=RIGHT , get0=LEFT
			//and4.set_0(evap);
			//and4.set_1(al); AbsEvap Absal
			//and4.set(SL1Vocabulary.AND_LEFT, AbsEvap);
			//and4.set(SL1Vocabulary.AND_RIGHT, Absal);
			
			
			//System.out.println("and4");
			
			//and3.set_0(listaFP);
			//and3.set_1(and4);
			List <AbsPredicate> listaTest = ConstruiListaTests(TestUltimaPractica, AbsPract);
			List <AbsPredicate> listaFF = ConstruirListaFF(ficherosUltimaPractica, contenidoFicheros,te);
			
			AndBuilder predicado = new AndBuilder();
			predicado.addPredicate(AbsCor);
			predicado.addPredicates(listaTest);
			predicado.addPredicates(listaFF);
			predicado.addPredicate(AbsEvap);
			System.out.println("doCorrecion: "+predicado.getAnd());
			
			
			
			//Guardamos en and3 los ficheros pedidos
			//and3 = InsertarFicherosPedidos(ficherosUltimaPractica, contenidoFicheros, Absal, te);
			//System.out.println("and3: Ficheros"+and3);
			//System.out.println("***********************************************");
			//AbsPredicate aux3 = (AbsPredicate) and3.getAbsObject(SL1Vocabulary.AND_LEFT);
			//System.out.println(aux3);
			//System.out.println("***********************************************");
			//AbsPredicate aux4 = (AbsPredicate) and3.getAbsObject(SL1Vocabulary.AND_RIGHT);
			//System.out.println(aux4);
			//System.out.println("***********************************************");
			
			//and2.set_0(listaTes);
			//and2.set_1(and3);
			//AbsConcept AbsPrac = (AbsConcept)PACAOntology.fromObject(pract);
			
			//Guardamos en and2 los tests pedidos
			//and2 = InsertarTestPedidos(TestUltimaPractica, AbsPract, and3);
			//and2 = InsertarTestPedidos(TestUltimaPractica, AbsPract, Absal);
			
			//System.out.println("and2: Test pedidos"+and2);
			///System.out.println("***********************************************");
			//AbsPredicate aux1 = (AbsPredicate) and2.getAbsObject(SL1Vocabulary.AND_LEFT);
			//System.out.println(aux1);
			//System.out.println("***********************************************");
			//AbsPredicate aux2 = (AbsPredicate) and2.getAbsObject(SL1Vocabulary.AND_RIGHT);
			//System.out.println(aux2);
			//System.out.println("***********************************************");
			
			//AbsPredicate andZ = new AbsPredicate(SL1Vocabulary.AND);
			//andZ.set(SL1Vocabulary.AND_LEFT,and3);
			//andZ.set(SL1Vocabulary.AND_RIGHT,and2);
			
			//and1.set_0(cor);
			//and1.set_1(and2);
			//and1.set(SL1Vocabulary.AND_LEFT, AbsCor);
			//and1.set(SL1Vocabulary.AND_RIGHT, andZ);
			
			//Creamos el IRE para enviar. En este caso IOTA
			AbsIRE qriota = new AbsIRE(SL2Vocabulary.IOTA);
			AbsVariable x = new AbsVariable("evaluacion",pacaOntology.EVALUAPRACTICA);
			qriota.setVariable(x);
			qriota.setProposition(predicado.getAnd());

			//List l_out = new ArrayList();
			//l_out.add(qriota);

			getContentManager().fillContent(msg_out, qriota);
			send(msg_out);
			System.out.println("Mandamos el IOTA... ");

		}
		catch (OntologyException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		
		catch (CodecException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		catch (Exception e) {
			//retornable = e.toString();
			e.printStackTrace();
		}

		//send(msg_out);

		msg_in = blockingReceive();
		//if (debug){
			System.out.println("----------------------------------------------------");
			System.out.println(msg_in.toString());
			System.out.println("---------hoooooooolaaaaaaaa-------------------------------------------");
		//}
		
		// Leemos el contenido del mensaje
		try {
			System.out.println("Estamos en el tryyy");
			//List l_in = extractMsgContent(msg_in);
			AbsContentElement l_in = null;
			l_in = getContentManager().extractAbsContent(msg_in);
			System.out.println(l_in.toString());

			//query_ref_response qres = (query_ref_response) l_in.get(0);
			//Modificacion Carlos
			//AbsObject qres = (AbsObject) l_in.get(0);
			//AbsPredicate qres = (AbsPredicate) l_in.getAbsObject(SL1Vocabulary.EQUALS);
			AbsConcept derecha = (AbsConcept) l_in.getAbsObject(SL1Vocabulary.EQUALS_RIGHT);
			System.out.println("derechaaaa: "+derecha);

			//EvaluacionPractica eva = (EvaluacionPractica) qres.get_1();
			//EvaluacionPractica eva = (EvaluacionPractica) qres.getAbsObject(SL1Vocabulary.EQUALS_RIGHT);
			ResultadoEvaluacion re = (ResultadoEvaluacion) PACAOntology.toObject(derecha);
			EvaluacionPractica eva = new EvaluacionPractica();
		
			eva.setTextoEvaluacion(re.getResultadoEvaluacionTexto());
			System.out.println("EVAAAA :"+eva.getTextoEvaluacion());
			//retornable = re.getResultadoEvaluacionTexto();
			retornable = eva.getTextoEvaluacion();
			System.out.println("Vamos a parsear:"+retornable);
		}
		catch (Exception e) {
			retornable = e.toString();
		}

		//System.out.println("Vamos a parsear:"+retornable);
		return retornable;
	}

	/**
       Encapsula la comunicación necesaria para realizar la entrega de la práctica seleccionada por el alumno.
       El agente corrector nos indicará si la acción se ha realizado o no se ha podido realizar.
       Las práctica a entregar está almacenada en <code>ultimaPractica</code>
       (que se actualiza con cada consulta de ficheros) y los nombres de 
       los ficheros en <code>ficherosUltimaPractica</code> (igualmente 
       actualizados en cada consulta de ficheros de una práctica).
       @param ContenidoFicheros El contenido de los ficheros de la evaluación. 
       Estos ficheros deben tener una correspondencia con los nombre de ficheros en <code>ficherosUltimaPractica</code>
       @param NombreCompanero El nombre del compañero con el que forma grupo el actual alumno que utiliza la interfaz
       @param PassCompanero El password del compañero con el que forma grupo el actual alumno que utiliza la interfaz
       @return El texto que indica si la acción se realizó
	 */
	public final String doEntregaPractica(String[] contenidoFicheros,
			String NombreCompanero,
			String PassCompanero) {

		// Cerrojo para que no se ejecute antes de que termine el setup
		while (!terminadoSetup) {
		}

		AID aaaAgente = getAgenteCorrector();
		
		ACLMessage msg_in;
		ACLMessage msg_out = new ACLMessage(ACLMessage.REQUEST);
		//msg_out.addReceiver(new AID(AgenteCorrector, AID.ISLOCALNAME));
		msg_out.addReceiver(aaaAgente);
		msg_out.setLanguage(codec.getName());
		msg_out.setOntology(pacaOntology.NAME);

		String retornable = new String("Entrega no efectuada");

		/*
	  Primero verificamos que el usuario compañero
	  pueda identificarse (si existe)
	  Se considerará que no se tiene compañero cuando
	  el login de este esté en blanco
		 */

		if (!(NombreCompanero.equals(""))) 
		{
			if (!doAutenticacion(NombreCompanero, PassCompanero)) {
				return "El usuario compañero de práctica no pudo autenticarse";
			}
		}


		try {	
			
			
			Action act = new Action();
			//act.setActor(new AID(AgenteCorrector, AID.ISLOCALNAME));
			act.setActor(aaaAgente);

			//EntregarPractica entp= new EntregarPractica();
			PACA.ontology.Corrector correc = new PACA.ontology.Corrector();
			correc.setId(aaaAgente.getName());
			//correc.setId(AgenteCorrector);
			
			//entp.setCorrector(correc);
			Practica pract = new Practica();
			pract.setId(ultimaPractica);
			pract.setDescripcion("");
			//entp.setPractica(pract);
			
			
			
			//Convertimos pract a concepto abstracto
			AbsConcept AbsPract = (AbsConcept) PACAOntology.fromObject(pract);
			
			//Convertimos correc a concepto abstracto
			AbsConcept AbsCorrec = (AbsConcept) PACAOntology.fromObject(correc);
			
			//Creamos el predicado abstracto EntregarPractica
			AbsPredicate AbsEntp = new AbsPredicate (pacaOntology.ENTREGARPRACTICA);
			AbsEntp.set(pacaOntology.PRACTICA, AbsPract);
			AbsEntp.set(pacaOntology.CORRECTOR, AbsCorrec);

			// Los Test
			Test te = new Test();
			Tests tes = new Tests();

			//Lista listaTes = new Lista();
			//Modificacion Carlos
			ArrayList listaTes = new ArrayList();

			for (int i=0; i < TestUltimaPractica.length; i++) {
				te = new Test();
				tes = new Tests();
				te.setId(TestUltimaPractica[i]);
				tes.setPractica(pract);
				tes.setTest(te);
				//listaTes.add_0(tes);
				//Modificacion Carlos
				listaTes.add(tes);
			}

			// Incluir los ficheros y rellenado de los ands

			FicheroFuentes ff;
			FuentesPrograma fp;
			//Lista listaFP = new Lista();
			//Modificacion Carlos
			ArrayList listaFP = new ArrayList();

			te = new Test();
			te.setId("?allTest");
			te.setDescripcion("");

			for (int i=0; i < ficherosUltimaPractica.length; i++) {

				fp = new FuentesPrograma();
				fp.setNombre(ficherosUltimaPractica[i]);

				fp.setContenido(contenidoFicheros[i]);

				ff = new FicheroFuentes();
				ff.setTest(te);
				ff.setFuentesPrograma(fp);

				//listaFP.add_0(ff);
				//Modificacion Carlos
				listaFP.add(ff);
			}

			//Interactua inter = new Interactua();
			//FormaGrupoCon formg = new FormaGrupoCon();

			

			
			Alumno al1 = new Alumno(alumnoID, alumnoPass);
			Alumno al2 = new Alumno(NombreCompanero, PassCompanero);
			
			//Convertimos al1 y al2 a conceptos abstractos
			AbsConcept AbsAl1 = (AbsConcept)PACAOntology.fromObject(al1);
			AbsConcept AbsAl2 = (AbsConcept)PACAOntology.fromObject(al2);
			
			//Creamos el predicado abstracto Interactua
			AbsPredicate AbsInter = new AbsPredicate (pacaOntology.INTERACTUA);
			
			//Creamos el predicado abstracto FormaGrupoCon
			AbsPredicate AbsFormg = new AbsPredicate (pacaOntology.FORMAGRUPOCON);
			
			AbsInter.set(pacaOntology.ALUMNO,AbsAl1);
			AbsInter.set(BasicOntology.STRING, (AbsObject) getAID());
			
			//inter.setAlumno(al1);
			//inter.setInterfaz(getAID());
			
			AbsFormg.set(pacaOntology.ALUMNO, AbsAl1);
			AbsFormg.set(pacaOntology.ALUMNO, AbsAl2);
			
			//formg.setAlumno1(al1);
			//formg.setAlumno2(al2);

			//And and1 = new And();
			//And and2 = new And();
			//And and3 = new And();
			//And and4 = new And();

			//get1=RIGHT , get0=LEFT
			AbsPredicate and1 = new AbsPredicate (SL1Vocabulary.AND);
			AbsPredicate and2 = new AbsPredicate (SL1Vocabulary.AND);
			AbsPredicate and3 = new AbsPredicate (SL1Vocabulary.AND);
			AbsPredicate and4 = new AbsPredicate (SL1Vocabulary.AND);

			//and4.set_0(inter);
			//and4.set_1(formg);
			and4.set(SL1Vocabulary.AND_LEFT, AbsInter);
			and4.set(SL1Vocabulary.AND_RIGHT, AbsFormg);

			//and3.set_0(listaFP);
			//and3.set_1(and4);
			and3.set(SL1Vocabulary.AND_LEFT, (AbsObject) listaFP);
			and3.set(SL1Vocabulary.AND_RIGHT, and4);

			//and2.set_0(listaTes);
			//and2.set_1(and3);
			and2.set(SL1Vocabulary.AND_LEFT, (AbsObject) listaTes);
			and2.set(SL1Vocabulary.AND_RIGHT, and3);

			//and1.set_0(entp);
			//and1.set_1(and2);
			and1.set(SL1Vocabulary.AND_LEFT, AbsEntp);
			and1.set(SL1Vocabulary.AND_RIGHT, and2);

			//act.setAction(and1);
			act.setAction((Concept) and1);


			List l_out = new ArrayList();
			l_out.add(act);


			//fillMsgContent(msg_out, l_out);
			msg_out.setContentObject((Serializable) l_out);

		}
		catch (Exception e) {
			return e.toString();
		}

		send(msg_out);

		msg_in = blockingReceive();

		if (debug){
			System.out.println("----------------------------------------------------");
			System.out.println(msg_in.toString());
			System.out.println("----------------------------------------------------");
		}
		
		// Leemos el contenido del mensaje
		try {
			//List l_in = extractMsgContent(msg_in);
			
			// Si recibimos un INFORM, con un "done", la practica
			//se ha entregado correctamente.

			//Object requestedInfo = l_in.get(0);
			//Ontology o = lookupOntology(msg_in.getOntology());
			//Modificacion Carlos
			//Ontology o = getContentManager().lookupOntology(msg_in.getOntology());
			//String requestedInfoName = o.getRoleName(requestedInfo.getClass());

			AbsContentElement l_in = null;
			l_in = getContentManager().extractAbsContent(msg_in);
			String requestedInfoName = l_in.getTypeName();

			if (msg_in.getPerformative() == ACLMessage.INFORM &&
					requestedInfoName.equals(BasicOntology.DONE)) {
				retornable = "La práctica se entregó correctamente";
			}
			else {
				// La práctica no se ha entregado, porque alguno
				// de los usuarios ya ha entregado la
				// práctica o porque se entrega fuera de plazo

				//And andF = (And) l_in.get(1);
				AbsObject andF = l_in.getAbsObject(SL1Vocabulary.AND);
				//Interactua interac = (Interactua) andF.get_1();
				Interactua interac = (Interactua) andF.getAbsObject(SL1Vocabulary.AND_RIGHT);

				if (interac.getAlumno().getIdentificador().equals("Entrega fuera de plazo")) {
					retornable = "Error en la entrega de la práctica: " +
					"La entrega se ha realizado fuera de plazo.";
				} else 
				{
					retornable = "Error en la entrega de la práctica: " +
					"El alumno " + interac.getAlumno().getIdentificador() +
					" ya ha entregado la práctica.";
				}
			}
		}
		catch (Exception e) {
			return e.toString();
		}

		return retornable;
	}
	
	public AID doBuscarCorrector(){
						
		DFAgentDescription template = new DFAgentDescription();
		ServiceDescription sd = new ServiceDescription();
		sd.setType("Corrector");
		template.addServices(sd);
		AID agenteCorr = null; 
				
		DFAgentDescription[] result;
		
		try {
			result = DFService.search(this, template);

			int tamano = result.length;
			AID[] agentesCorrectores = new AID[result.length];
			
			for (int i = 0; i < result.length; ++i) {
				agentesCorrectores[i] = result[i].getName();
				System.out.println("Agente Encontrado: "+agentesCorrectores[i].getName());
			}
			
			Random rand = new Random();
			int indiceCorrector = rand.nextInt(100);
			System.out.println("Numero aleatorio: "+indiceCorrector);		
			
			int politica=indiceCorrector%tamano;
			System.out.println("Politica: "+ politica);
			
			agenteCorr=agentesCorrectores[politica];
			System.out.println("Agente Elegido: "+agenteCorr.getName());
			
			
			if (almacenCorrec.containsKey(agenteCorr)){
				//usado = almacenCorrec.get(agenteCorr);
				usado = almacenCorrec.get(agenteCorr);
				usado++;
				almacenCorrec.put(agenteCorr, usado);
				
				System.out.println("USADOSSSS: "+usado);
				
			}
			else{
				almacenCorrec.put(agenteCorr, new Integer(1));
			}
			
			System.out.println("VALORESSSSS: "+almacenCorrec.toString());
			
			setAgenteCorrector(agenteCorr);
			
		} 
		catch (FIPAException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		System.out.println("-------------- Fin Buscar Correctores ------------------");
				
		return agenteCorr;
	}
	
	
	
	//Metodo que insterta en predicados abstractos "and" enlazados los Test seleccionados
	public AbsPredicate InsertarTestPedidos(String[] conjTests, AbsConcept pracAux, AbsPredicate predAux){
		
		Test te = new Test();
		int numeroTests = conjTests.length;
				
		AbsPredicate andA = new AbsPredicate(SL1Vocabulary.AND);
		AbsPredicate andD = new AbsPredicate(SL1Vocabulary.AND);
		
		int contador=0;
		while (contador<numeroTests){
			
			if (contador==0){
				//Convertimos el test a un objecto abstracto
				AbsConcept AbsTest;
				try {
					te.setId(conjTests[contador]);
					te.setDescripcion("");
					
					AbsTest = (AbsConcept) PACAOntology.fromObject(te);
					//Creamos el predicado abstracto "TESTS"
					AbsPredicate AbsTests = new AbsPredicate(pacaOntology.TESTS);
					AbsTests.set(pacaOntology.TEST, AbsTest);
					AbsTests.set(pacaOntology.PRACTICA, pracAux);
					
					andD.set(SL1Vocabulary.AND_LEFT, AbsTests);
					andD.set(SL1Vocabulary.AND_RIGHT, predAux);
					
				} catch (OntologyException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
						
			}
			else{
				te = new Test();
				te.setId(conjTests[contador]);
				te.setDescripcion("");
				//Convertimos el test a un objecto abstracto
				AbsConcept AbsTest;
				try {
					AbsTest = (AbsConcept) PACAOntology.fromObject(te);
					//Creamos el predicado abstracto "TESTS"
					AbsPredicate AbsTests = new AbsPredicate(pacaOntology.TESTS);
					AbsTests.set(pacaOntology.TEST, AbsTest);
					AbsTests.set(pacaOntology.PRACTICA, pracAux);

					andA.set(SL1Vocabulary.AND_LEFT, AbsTests);
					andA.set(SL1Vocabulary.AND_RIGHT, andD);
					
					andD = new AbsPredicate(SL1Vocabulary.AND);
					andD = andA;
					andA = new AbsPredicate(SL1Vocabulary.AND);

				} catch (OntologyException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
			}
			contador++;
		}
		
		return andD;
	}
	
//	Metodo que insterta en predicados abstractos "and" enlazados los Test seleccionados
	public AbsPredicate InsertarFicherosPedidos(String[] conjFich, String[] contFich, AbsPredicate predAux, Test teAux){
		
		/*
		 * for (int i=0; i < ficherosUltimaPractica.length; i++) {

				fp = new FuentesPrograma();
				fp.setNombre(ficherosUltimaPractica[i]);

				fp.setContenido(contenidoFicheros[i]);

				ff = new FicheroFuentes();
				ff.setTest(te);
				ff.setFuentesPrograma(fp);

				//listaFP.add_0(ff);
				//Modificacion Carlos
				listaFP.add(ff);
			}
		 */
		FuentesPrograma fp = new FuentesPrograma();
	
		int numeroFicheros = conjFich.length;
				
		AbsPredicate andA = new AbsPredicate(SL1Vocabulary.AND);
		AbsPredicate andD = new AbsPredicate(SL1Vocabulary.AND);
		
		AbsConcept AbsFP;
		AbsConcept AbsTest;
		
		int contador=0;
		while (contador<numeroFicheros){
			
			if (contador==0){
								
				try {
					fp.setNombre(conjFich[contador]);
					fp.setContenido(contFich[contador]);
					
					AbsFP = (AbsConcept) PACAOntology.fromObject(fp);
					AbsTest =(AbsConcept) PACAOntology.fromObject(teAux);
					//Creamos el predicado abstracto "FICHEROFUENTS"
					AbsPredicate AbsFicheros = new AbsPredicate(pacaOntology.FICHEROFUENTES);
					AbsFicheros.set(pacaOntology.TEST, AbsTest);
					AbsFicheros.set(pacaOntology.FUENTESPROGRAMA, AbsFP);
					
										
					andD.set(SL1Vocabulary.AND_LEFT, AbsFicheros);
					andD.set(SL1Vocabulary.AND_RIGHT, predAux);
					
				} catch (OntologyException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				if (debug2){
					System.out.println("Pasamos a objeto abstracto?");
				}
				
			}
			else{
				
				try {
					fp = new FuentesPrograma();
					fp.setNombre(conjFich[contador]);
					fp.setContenido(contFich[contador]);
					
					AbsFP = (AbsConcept) PACAOntology.fromObject(fp);
					AbsTest = (AbsConcept) PACAOntology.fromObject(teAux);
					
					//Creamos el predicado abstracto "FICHEROFUENTS"
					AbsPredicate AbsFicheros = new AbsPredicate(pacaOntology.FICHEROFUENTES);
					AbsFicheros.set(pacaOntology.TEST, AbsTest);
					AbsFicheros.set(pacaOntology.FUENTESPROGRAMA, AbsFP);

					andA.set(SL1Vocabulary.AND_LEFT, AbsFicheros);
					andA.set(SL1Vocabulary.AND_RIGHT, andD);
					
					andD = new AbsPredicate(SL1Vocabulary.AND);
					andD = andA;
					andA = new AbsPredicate(SL1Vocabulary.AND);

				} catch (OntologyException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
			}
			contador++;
		}
		
		return andD;
	}
	
	public List<AbsPredicate> ConstruiListaTests (String [] TestsAux, AbsConcept practAux){
		List <AbsPredicate> ListaAux = new ArrayList<AbsPredicate>();
		
		for (int contador = 0; contador < TestsAux.length; contador++) {
			Test te = new Test();
			AbsConcept AbsTest;
			te.setId(TestsAux[contador]);
			te.setDescripcion("");
			
			try {
				AbsTest = (AbsConcept) PACAOntology.fromObject(te);
				AbsPredicate AbsTests = new AbsPredicate(pacaOntology.TESTS);
				AbsTests.set(pacaOntology.TEST, AbsTest);
				AbsTests.set(pacaOntology.PRACTICA, practAux);
				ListaAux.add(AbsTests);
			} catch (OntologyException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		return ListaAux;
	}
	
	public List<AbsPredicate> ConstruirListaFF (String[] conjFich, String[] contFich, Test teAux){
		List <AbsPredicate> ListaAux = new ArrayList<AbsPredicate>();
		FuentesPrograma fp = new FuentesPrograma();
		for (int contador = 0; contador < conjFich.length; contador++) {
			
			fp.setNombre(conjFich[contador]);
			fp.setContenido(contFich[contador]);
		
			AbsConcept AbsFP;
			try {
				AbsFP = (AbsConcept) PACAOntology.fromObject(fp);
				AbsConcept AbsTest =(AbsConcept) PACAOntology.fromObject(teAux);
				//Creamos el predicado abstracto "FICHEROFUENTS"
				AbsPredicate AbsFicheros = new AbsPredicate(pacaOntology.FICHEROFUENTES);
				AbsFicheros.set(pacaOntology.TEST, AbsTest);
				AbsFicheros.set(pacaOntology.FUENTESPROGRAMA, AbsFP);
				ListaAux.add(AbsFicheros);
			} 
			catch (OntologyException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
		return ListaAux;
	}
	
	
	//====================== COMPORTAMIENTOS ===========================
	public class AutenticaBehaviour extends OneShotBehaviour {

	    private Testigo test;
	    private String usuaAux;
	    private String passAux;
	    
	    /**
	     * Constructor del comportamiento
	     */
	    public AutenticaBehaviour (Agent _a, Testigo _test, String usuPar, String passPar) {
	        super(_a);
	        this.test = _test;
	        this.usuaAux = usuPar;
	        this.passAux = passPar;
	    }

	    public void action(){
	    	test.setResultadoB(doAutenticacion(usuaAux, passAux));
	    }
	}
	
	public class CorrectorBehaviour extends OneShotBehaviour{
		private Testigo test1;
		public CorrectorBehaviour(Agent _a, Testigo test){
			super(_a);
			this.test1 = test;
		}
		public void action(){
			test1.setResultado(doBuscarCorrector());
		}
	}
	
	public class PracticasBehaviour extends OneShotBehaviour{
		private Testigo test1;
		public PracticasBehaviour(Agent _a, Testigo test){
			super(_a);
			this.test1 = test;
		}
		public void action(){
			test1.setResultado(doPeticion());
		}
	}
	
	public class TestsBehaviour extends OneShotBehaviour{
		private Testigo test1;
		private String prac1;
		public TestsBehaviour(Agent _a, Testigo test, String pracAux){
			super(_a);
			this.test1 = test;
			this.prac1 = pracAux;
		}
		
		public void action(){
			test1.setResultado(doPeticionTestPractica(prac1));
		}
	}

	//---------------- COMPORTAMIENTOS PARA LA AUTENTICACION  ----------------------------
	public class EnviaAutenticaBehaviour extends OneShotBehaviour{
		private String usuAux;
		private String passAux;
		private Testigo tes1;
		public EnviaAutenticaBehaviour(Agent _a, Testigo tes, String usuario, String passw){
			super(_a);
			this.usuAux = usuario;
			this.passAux = passw;
			this.tes1 = tes;
		}
		public void action(){
			System.out.println("COMPORTAMIENTO ENVIAAUTENTICA");
			ACLMessage solicitud = new ACLMessage(ACLMessage.QUERY_IF);
			solicitud.setLanguage(codec.getName());
			solicitud.setOntology(AuthOntology.ONTOLOGY_NAME);
			solicitud.addReceiver(new AID(AgenteAutenticador, AID.ISLOCALNAME));
					
			// Creamos el predicado.
			Autenticado aut = new Autenticado();
			Usuario user2 = new Usuario();
			
			System.out.println("USUARIO: "+usuAux);
			System.out.println("PASSWORD: "+passAux);
			// Creamos el usuario
			user2.setUser_id(usuAux);
			user2.setPassword(passAux);
			
			aut.setUsuario(user2);
			
			//Metodos auxiliares para no pasar parametros innecesarios al comportamiento "Recibiendo"
			//Guardamos el usuario y el password por si la autenticacion ha sido correcta
			setUsuarioAux(usuAux);
			setPasswordAux(passAux);
		
			try{
				//Mandamos el predicado "aut"
				getContentManager().fillContent(solicitud,aut);
				addBehaviour(new RecibeMensajes(myAgent, tes1));
				send(solicitud);
				System.out.println(solicitud);
				
				
			}
			catch (CodecException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} 
			catch (OntologyException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
	}
	
	
	//Comportamiento para autentica al usuario
	public class Autenticacion extends OneShotBehaviour{
		private Testigo tes1;
		private boolean resultado;
		private AbsContentElement mens1;
		private String usu1;
		private String pass1;
		public Autenticacion(Agent _a, Testigo tes, AbsContentElement mensaje){
			super(_a);
			this.tes1 = tes;
			this.mens1 = mensaje;
		}
		
		public void action(){
			try{
				System.out.println("COMPORTAMIENTO AUTENTICACION");
				System.out.println(mens1);
				// Ahora iremos cogiendo los objetos creados de la ontologia.
				
				String tipoObjetoContenido = mens1.getTypeName();
				
				if (tipoObjetoContenido.equals(SL1Vocabulary.NOT)){
					resultado = false;
				}
				else{
					usu1 = getUsuarioAux();
					pass1 = getPasswordAux();
					System.out.println("nuevo usu1: "+usu1);
					System.out.println("nuevo pass1: "+pass1);
					setAlumnoID(usu1);
					setAlumnoPass(pass1);
					resultado = true;
				}
			}
			catch(Exception ex){
				resultado = false;
			}
			System.out.println("Y el resultado es: "+resultado);
			tes1.setResultadoB(resultado);
		}
	}
	//-------------- FIN COMPORTAMIENTOS PARA LA AUTENTICACION ----------------------------
	
	//-------------- COMPORTAMIENTOS PARA PEDIR LAS PRACTICAS ----------------------------
	public class PidePracticasBehavior extends OneShotBehaviour{
		private Testigo tes;
		public PidePracticasBehavior(Agent _a, Testigo tes1){
			super(_a);
			this.tes=tes1;
		}
		public void action(){
			Paso = 0;
			System.out.println("COMPORTAMIENTOOOOO PIDE PRACTICAS");
			AID aaaAgente = getAgenteCorrector();
			ACLMessage msg_out = new ACLMessage(ACLMessage.QUERY_REF);
			//msg_out.addReceiver(new AID(AgenteCorrector, AID.ISLOCALNAME));
			msg_out.addReceiver(aaaAgente);
			msg_out.setLanguage(codec.getName());
			msg_out.setOntology(pacaOntology.NAME);

			// Generamos el mensaje para enviar
			Practica pract = new Practica();
			pract.setId("?practica");
			pract.setDescripcion("");
			Corrector correc = new Corrector();
			correc.setId(aaaAgente.getName());
			//correc.setId("corrector");
			
			try {
				
				//Convertimos la practica a un objecto abstracto
				AbsConcept AbsPract = (AbsConcept) PACAOntology.fromObject(pract);
				
				//Convertimos el correcto a un objecto abstracto
				AbsConcept AbsCorrec = (AbsConcept) PACAOntology.fromObject(correc);
							
				//Creamos el predicado abstracto "CORRIGE"
				AbsPredicate AbsPredicado= new AbsPredicate(pacaOntology.CORRIGE);
				AbsPredicado.set(pacaOntology.PRACTICA, AbsPract);
				AbsPredicado.set(pacaOntology.CORRECTOR, AbsCorrec);
							
				//Creamos la variable que queremos pedir, en este caso "?practica"
				AbsVariable x = new AbsVariable("practica",pacaOntology.PRACTICA);
			
				//Creamos el IRE con la variable "x" y el predicado "CORRIGE"
				AbsIRE qrall = new AbsIRE(SL2Vocabulary.ALL);
				qrall.setVariable(x);
				qrall.setProposition(AbsPredicado);
							
				//Mandamos el mensaje
				getContentManager().fillContent(msg_out, qrall);
				addBehaviour(new RecibeMensajes(myAgent, tes));
				send(msg_out);
			}
			catch (OntologyException e1) {
				// TODO Auto-generated catch block
				System.out.println("INTERFAZ.JAVA: NUEVO TRY---->Salto excepcion");
				e1.printStackTrace();
			} 
			 
			catch (CodecException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
	}
	
	public class RecibePracticasBeh extends OneShotBehaviour{
		private Testigo tes1;
		private AbsAggregate practicas1;
		public RecibePracticasBeh(Agent _a, Testigo tes, AbsAggregate practicas){
			super(_a);
			this.tes1 = tes;
			this.practicas1 = practicas;
		}
		
		public void action(){
			String[] retornable = new String[0];
			try {
				System.out.println("COMPORTAMIENTOOOOOOOO RECIBE PRACTICAS");
				//Sacamos las practicas que vienen en el mensaje como conceptos abstractos
				
				Practica p = new Practica();
				retornable = new String[practicas1.size()];
				for (int i=0; i < practicas1.size(); i++) {
					//Pasamos de concepto abstracto a objeto "real", en este caso son practicas
					p = (Practica) PACAOntology.toObject(practicas1.get(i));
					
					retornable[i] = p.getId();
					System.out.println(retornable[i]);
				}
			}
			catch (OntologyException oe){
				//printError(myAgent.getLocalName()+" getRoleName() unsucceeded. Reason:" + oe.getMessage());
				System.out.println("Excepcion en ontologia");
				oe.printStackTrace();
			}   
			catch (java.lang.NullPointerException e) {
				System.out.println("Empty message");
				e.printStackTrace();
			}
			catch (Exception e) {
				// Si obtenemos alguna excepción de error, directamente no
				// ofrecemos las practicas ...
				retornable = new String[2];
				retornable[0]="Error en la obtención de las prácticas";
				retornable[1]= practicas1.toString();
			}
			System.out.println("Ponemos las practicas en retornable");
			tes1.setResultado(retornable);
		}
	}
	//-------------------------- FIN COMPORTAMIENTOS PARA PRACTICAS -----------------------
	
	
	
	//-------------------------- COMPORTAMIENTOS PARA TESTS -------------------------------
	public class PideTestBeha extends OneShotBehaviour{
		private Testigo tes1;
		private String IdPractica;
		public PideTestBeha(Agent _a, Testigo tes, String practica){
			super(_a);
			this.tes1 = tes;
			this.IdPractica = practica;
		}
		
		public void action(){
			System.out.println("COMPORTAMIENTOOOOOO PIDE TESTSSS");
//			 Nos guardamos la última práctica solicitada
			ultimaPractica = IdPractica;
					
			AID aaaAgente = getAgenteCorrector();

			ACLMessage msg_in;
			ACLMessage msg_out = new ACLMessage(ACLMessage.QUERY_REF);
			//msg_out.addReceiver(new AID(AgenteCorrector, AID.ISLOCALNAME));
			msg_out.addReceiver(aaaAgente);
			msg_out.setLanguage(codec.getName());
			msg_out.setOntology(pacaOntology.NAME);

			// Generamos el mensaje para enviar
			Corrige cor = new Corrige();
			Practica pract = new Practica();
			pract.setId(IdPractica);
			pract.setDescripcion("");
			PACA.ontology.Corrector correc = new PACA.ontology.Corrector();
			//correc.setId("corrector");
			correc.setId(aaaAgente.getName());
			cor.setPractica(pract);
			cor.setCorrector(correc);
			
			Test te = new Test();
			te.setId("?test");
			te.setDescripcion("");
			
							
			try {
				//Convertimos la practica a un concepto abstracto
				AbsConcept AbsPract = (AbsConcept) PACAOntology.fromObject(pract);
				
				//Convertimo el corrector a un concepto abstracto
				AbsConcept AbsCorrec = (AbsConcept) PACAOntology.fromObject(correc);
				
				//Convertimos el test a un concepto abstracto
				AbsConcept AbsTest = (AbsConcept) PACAOntology.fromObject(te);
				
				//Creamos el predicado CORRIGE de forma abstracta utilizando los conceptos abstractos creados anteriormente
				AbsPredicate AbsCorrige = new AbsPredicate(pacaOntology.CORRIGE);
				AbsCorrige.set(pacaOntology.PRACTICA, AbsPract);
				AbsCorrige.set(pacaOntology.CORRECTOR, AbsCorrec);
							
				//Creamos el predicado TESTS de forma abstracta utilizando los conceptos abstractos creados anteriormente
				AbsPredicate AbsTests = new AbsPredicate(pacaOntology.TESTS);
				AbsTests.set(pacaOntology.PRACTICA, AbsPract);
				AbsTests.set(pacaOntology.TEST, AbsTest);
							
				AbsVariable x = new AbsVariable("test",pacaOntology.TEST);
				
				//********************* Esto vale NO BORRAR ****************
				//AbsPredicate and1 = new AbsPredicate(SL1Vocabulary.AND);
				//and1.set(SL1Vocabulary.AND_LEFT, AbsCorrige);
				//and1.set(SL2Vocabulary.AND_RIGHT, AbsTests);
				//**********************************************************
				
				AndBuilder predicado = new AndBuilder();
				predicado.addPredicate(AbsCorrige);
				predicado.addPredicate(AbsTests);
				System.out.println("Predicado pide Tests: "+predicado.getAnd());
			
				
				AbsIRE qrall = new AbsIRE(SL2Vocabulary.ALL);
				qrall.setVariable(x);
				//qrall.setProposition(and1);
				qrall.setProposition(predicado.getAnd());
							
				getContentManager().fillContent(msg_out, qrall);
				addBehaviour(new RecibeMensajes(myAgent, tes1));
				send(msg_out);
						
			}
			catch (OntologyException e1) {
				// TODO Auto-generated catch block
				System.out.println("INTERFAZ.JAVA: NUEVO TRY TESTS---->Salto excepcion");
				e1.printStackTrace();
			} 
			 
			catch (CodecException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
	}
	
	public class RecibeTestBeha extends OneShotBehaviour{
		private Testigo tes1;
		private AbsAggregate tests1;
		public RecibeTestBeha(Agent _a, Testigo tes, AbsAggregate tests_){
			super(_a);
			this.tes1 = tes;
			this.tests1 = tests_;
		}
		
		public void action(){
			
			String[] retornable = new String[0];
			String[] posiblesID = new String[0];
			try{
				//Sacamos los tests que vienen en el mensaje como conceptos abstractos
						
				Test t = new Test();
				retornable = new String[tests1.size()*2];
				posiblesID = new String[tests1.size()];
				int j=0;
				for (int i=0; i < tests1.size(); i++) {
					//Pasamos de concepto abstracto a objeto "real", en este caso son tests
					t = (Test) PACAOntology.toObject(tests1.get(i));
																	
					retornable[j++] = t.getId();
					posiblesID[i] = t.getId();
					retornable[j++] = t.getDescripcion();
					System.out.println(retornable[i]);
					
				}
				
			}
			catch (Exception e) {
				// Si obtenemos alguna excepción de error, directamente no
				// ofrecemos los tests ...
				retornable = new String[2];
				retornable[0]="Error en la obtención de los tests";
				retornable[1]= "Error:" + e.toString();
			}
			TestPosiblesPractica = posiblesID;
			tes1.setResultado(retornable);
			
			
		}
	}
	//-------------------------- FIN COMPORTAMIENTOS PARA TESTS ---------------------------
	
	
	
	
	//-------------------------- COMPORTAMIENTOS PARA PEDIR FICHEROS ----------------------
	public class PideFicherosBeha extends OneShotBehaviour{
		private Testigo tes1;
		private String [] IdTest;
		public PideFicherosBeha(Agent _a, Testigo tes, String [] test){
			super(_a);
			this.tes1 =  tes;
			this.IdTest = test;
		}
		
		public void action(){
						
			String IdPractica  = ultimaPractica;

			// Y nos guardamos los últimos test solicitados
			TestUltimaPractica = IdTest;
			
			AID aaaAgente = getAgenteCorrector();

			ACLMessage msg_out = new ACLMessage(ACLMessage.QUERY_REF);
			//msg_out.addReceiver(new AID(AgenteCorrector, AID.ISLOCALNAME));
			msg_out.addReceiver(aaaAgente);
			msg_out.setLanguage(codec.getName());
			msg_out.setOntology(pacaOntology.NAME);

			// Leemos la practica y pedimos los ficheros
			// necesarios

			try {
				
				Corrige cor = new Corrige();
				Practica pract = new Practica();
				pract.setId(IdPractica);
				pract.setDescripcion("");

				PACA.ontology.Corrector correc = new PACA.ontology.Corrector();
				//correc.setId("corrector");
				correc.setId(aaaAgente.getName());
				cor.setPractica(pract);
				cor.setCorrector(correc);

							
				FuentesPrograma fp = new FuentesPrograma();
				fp.setNombre("?nombre");
				fp.setContenido("");

										
				//Convertimos el corrector a un objecto abstracto
				AbsConcept AbsCorrec = (AbsConcept) PACAOntology.fromObject(correc);
							
				//Convertimos la practica a un objecto abstracto
				AbsConcept AbsPract = (AbsConcept) PACAOntology.fromObject(pract);
				
				//Creamos el predicado abstracto "CORRIGE"
				AbsPredicate AbsCor = new AbsPredicate(pacaOntology.CORRIGE);
				AbsCor.set(pacaOntology.PRACTICA, AbsPract);
				AbsCor.set(pacaOntology.CORRECTOR, AbsCorrec);
							
				AbsPredicate and2 = new AbsPredicate(SL1Vocabulary.AND);
				//and2.set_0(listaTests);
				//and2.set_1(ff);
				
				FicheroFuentes ff = new FicheroFuentes();
				Test te1 = new Test();
				te1.setId(IdTest[0]);
				te1.setDescripcion("");
				ff.setTest(te1);
				ff.setFuentesPrograma(fp);
				
				//Pasamos el predicado FicheroFuentes a predicado abstracto
				AbsPredicate Absff = (AbsPredicate) PACAOntology.fromObject(ff);
				
				List <AbsPredicate> listaTest = ConstruiListaTests(IdTest, AbsPract);
								
				//AbsPredicate pruebaP = ConstruirAnd2(list1);
				AndBuilder constructor = new AndBuilder();
				
				constructor.addPredicate(AbsCor);
				constructor.addPredicates(listaTest);
				constructor.addPredicate(Absff);
				System.out.println("Pide ficheros: "+constructor.getAnd());
				
								
				//and2 = InsertarTestPedidos(IdTest, AbsPract, Absff);
				
				
				//AbsPredicate and1 = new AbsPredicate(SL1Vocabulary.AND);
				//and1.set_0(cor);
				//and1.set_1(and2);
				//and1.set(SL1Vocabulary.AND_LEFT, AbsCor);
				//and1.set(SL1Vocabulary.AND_RIGHT, and2);
				
				
				//Modificacion Carlos
				AbsIRE qrall = new AbsIRE(SL2Vocabulary.ALL);
				AbsVariable x = new AbsVariable("nombre",pacaOntology.FICHEROFUENTES);
				qrall.setVariable(x);
				qrall.setProposition(constructor.getAnd());
									
				getContentManager().fillContent(msg_out, qrall);
				addBehaviour(new RecibeMensajes(myAgent,tes1));
				send(msg_out);

			}
			
			catch (OntologyException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} 
			catch (CodecException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			catch (Exception e){
				System.out.println("EXCEPCIONNNNNNNNNNNNNNNNN");
				e.printStackTrace();
			}
			
		}
	}
	
	public class RecibeFicherosBeha extends OneShotBehaviour{
		private Testigo tes2;
		private AbsAggregate ficheros1;
		public RecibeFicherosBeha(Agent _a, Testigo tes, AbsAggregate ficheros){
			super(_a);
			this.tes2 = tes;
			this.ficheros1 = ficheros;
		}
		public void action(){
			String[] retornable = new String[0];
			try{
				//Sacamos los ficheros que vienen en el mensaje como conceptos abstractos
				
				// Pasamos la lista a un String[]
				FuentesPrograma fp;
				retornable = new String[ficheros1.size()];
				for (int i=0; i < ficheros1.size(); i++) {
					fp = (FuentesPrograma) PACAOntology.toObject(ficheros1.get(i));
					retornable[i] = fp.getNombre();
				}
				
			}
			catch (Exception e) {
				ficherosUltimaPractica = retornable;
				tes2.setResultado(retornable);
			}

			ficherosUltimaPractica = retornable;
			tes2.setResultado(retornable);
					
		}
	}
	
	//-------------------------- COMPORTAMIENTO PARA RECIBIR MENSAJES ---------------------
	public class RecibeMensajes extends Behaviour{
		private Testigo tes1;
		
		private boolean finalizado = false;
				
		private MessageTemplate p1 = MessageTemplate.MatchPerformative(ACLMessage.INFORM);
		private MessageTemplate p2 = MessageTemplate.MatchOntology(AuthOntology.ONTOLOGY_NAME);
		private MessageTemplate plantilla = MessageTemplate.and(p1,p2);

		public RecibeMensajes(Agent _a, Testigo tes){
			super(_a);
			this.tes1 = tes;
		}
		
		public void action(){
			
			ACLMessage respuesta = receive();
			
			//ACLMessage respuesta = receive(plantilla);
			System.out.println("MENSAJEEEEE: "+respuesta);
			if (respuesta != null){
				try {
					AbsContentElement listaAbs = null;
					listaAbs = getContentManager().extractAbsContent(respuesta);
					String tipoMensaje = listaAbs.getTypeName();
					System.out.println("TIPOOO: "+tipoMensaje);
					if (tipoMensaje.equals("autenticado")){
						addBehaviour(new Autenticacion(myAgent, tes1, listaAbs));
						finalizado = true;
					}
					else if (tipoMensaje.equals("=")){
												
						AbsAggregate ListaElementos = (AbsAggregate) listaAbs.getAbsObject(SLVocabulary.EQUALS_RIGHT);
						
						//Cogemos el primer elemento de la lista
						AbsConcept primerElem = (AbsConcept) ListaElementos.get(0);
						
						//Miramos el tipo del primer elemento
						String tipo = primerElem.getTypeName();
						System.out.println("Tipo: "+tipo);
						if (tipo.equals("practica")) {
							System.out.println("Es una practica");
							addBehaviour(new RecibePracticasBeh(myAgent, tes1, ListaElementos));
							finalizado = true;
						}
						else if (tipo.equals("test")){
							System.out.println("Es un test");
							addBehaviour(new RecibeTestBeha(myAgent, tes1, ListaElementos));
							finalizado = true;
						}
						else{
							System.out.println("Es un fichero");
							addBehaviour(new RecibeFicherosBeha(myAgent, tes1, ListaElementos));
							finalizado = true;
						}
						
					}
					
				} catch (CodecException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (OntologyException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			else{
				block();
			}
		}

		@Override
		public boolean done() {
			// TODO Auto-generated method stub
			return finalizado;
		}
	}
	//-------------------- FIN COMPORTAMIENTO QUE RECIBE MENSAJES ---------------------------
	
	

}


