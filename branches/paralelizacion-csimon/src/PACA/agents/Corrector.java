package PACA.agents;

import jade.content.abs.AbsAggregate;
import jade.content.abs.AbsConcept;
import jade.content.abs.AbsContentElement;
import jade.content.abs.AbsIRE;
import jade.content.abs.AbsObject;
import jade.content.abs.AbsPredicate;
import jade.content.lang.Codec;
import jade.content.lang.Codec.CodecException;
import jade.content.lang.sl.SL1Vocabulary;
import jade.content.lang.sl.SL2Vocabulary;
import jade.content.lang.sl.SLCodec;
import jade.content.lang.sl.SLVocabulary;
import jade.content.onto.BasicOntology;
import jade.content.onto.Ontology;
import jade.content.onto.OntologyException;
import jade.content.onto.UngroundedException;
import jade.content.onto.basic.Action;
import jade.content.onto.basic.Done;
import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.Behaviour;
import jade.core.behaviours.CyclicBehaviour;
import jade.core.behaviours.OneShotBehaviour;
import jade.domain.DFService;
import jade.domain.FIPAException;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.Property;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.lang.acl.ACLMessage;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.io.StringWriter;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Random;


import PACA.ontology.Alumno;
import PACA.ontology.Corrige;
import PACA.ontology.EntregarPractica;
import PACA.ontology.EvaluacionPractica;
import PACA.ontology.FicheroFuentes;
import PACA.ontology.FormaGrupoCon;
import PACA.ontology.Interactua;
import PACA.ontology.Practica;
import PACA.ontology.ResultadoEvaluacion;
import PACA.ontology.Test;
import PACA.ontology.Tests;
import PACA.ontology.pacaOntology;
import PACA.ontology.Fichero.FuentesPrograma;
import PACA.util.AndBuilder;

/**
Este agente se encarga de realizar la correcci�n de las pr�cticas y tambi�n deposita las entregas que los 
alumnos realizan.
 */
public class Corrector extends Agent {

	// Modo de ejecución en pruebas
	private boolean ejecucionEnPruebas = true;
	private boolean ejecucionEnPruebas2 = true;
	//	Nombre de la ontologia
	private Ontology ontologia = pacaOntology.getInstance();
	//Codec
	private Codec codec = new SLCodec();
	//Debug
	private boolean debug = false;
	/**
	Modo depuraci�n del agente.
	 */
	private boolean debugMode = false;
	/**
	AID del agente.
	 */
	private AID agente;
	// --------------- CONFIGURACION LOCAL -------------------
	/**
	Directorio temporal.
	 */
	private String tempDir = "/tmp/pacaCorrector/";
	/**
	Directorio de trabajo d�nde est�n las pr�cticas.
	 */
	//private String workDir = "/home/carlos/PACA/practicas/";
	private String workDir = "C:\\" + "Practicas\\";
	/**
	Directorio de trabajo d�nde se encuentran las librer�as.
	 */
	private String libraryDir = "/home/public/Proyectos/PACA/librerias/";
	/**
	Directorio d�nde se depositan las entregas finales.
	 */
	private String entregaFinalDir = "/home/public/Proyectos/PACA/EntregasFinales/";
	/**
	Script de correcci�n de pr�cticas.
	 */
	private String scriptFile = "/home/public/Proyectos/PACA/paca/script/ca_v4.pl";
	/**
	Int�rprete del script.
	 */
	private String interprete = "/usr/bin/perl";
	// ------------- FIN CONFIGURACION LOCAL -----------------------
	/**
	Nombre del fichero en el que se genera la evaluaci�n del alumno.
	 */
	private String ficheroEvaluacion = "EvaluacionAlumno.xml";
	/**
	Nombre de los ficheros que contienen la descripci�n de los tests.
	 */
	private String ficheroConfTest = "FTest";
	/**
	Nombre de los ficheros que contienen la descripci�n de las pr�cticas.
	 */
	private String ficheroConfPractica = "FPractica";
	/**
	Mensaje de error en la evaluaci�n, codificado en XML.
	 */
	private String errorXML = "<Practica fecha=\"00/00/0000\" usuario=\"ERROR\" identificador=\"ERROR\"><Descripcion>Se ha producido un error interno al realizar la evaluaci�n de la pr�ctica.</Descripcion><Test identificador=\"ERROR\"><Descripcion>Se almacenar�n los datos que han producido el error y as� poder solucionarlo. Perdone por las molestias.</Descripcion><EvaluacionTest codigoEvaluacionTest=\"ERROR\"></EvaluacionTest><Caso identificador=\"ERROR\"><EvaluacionCaso codigoEvaluacionCaso=\"ERROR\"></EvaluacionCaso></Caso></Test></Practica>";
	/*************************************
	 * Declaración de datos para pruebas *
	 *************************************/
	private Practica[] PracticasPrueba;
	private Hashtable<String, Test[]> TestPracticasPrueba;
	private Hashtable<String, FuentesPrograma[]> FuentesProgramaPrueba;
	
	
	private Integer numeroCorrecciones = 0;
	private Integer numeroCorrecionesActual = 0;
	//intervalo es el tiempo entre actualizaciones del df
	private long intervalo = 3000;
	private Date fechaActual = new Date();
	private Date fechaLimite = new Date(fechaActual.getTime() + intervalo);
	private int tiempo_minimo = 1;
	private int tiempo_maximo = 10;
	

	/**
	 * Incicialización de datos para pruebas
	 */
	private void InicializaDatosPruebas() {

		// Prácticas
		PracticasPrueba = new Practica[4];
		PracticasPrueba[0] = new Practica("Practica_1", "Des. Practica 1");
		PracticasPrueba[1] = new Practica("Practica_2", "Des. Practica 2");
		PracticasPrueba[2] = new Practica("Practica_3", "Des. Practica 3");
		PracticasPrueba[3] = new Practica("Practica_4", "Des. Practica 4");
 
		// Test de Prácticas
		TestPracticasPrueba = new Hashtable<String, Test[]>();
		Test[] Testtmp;
		FuentesProgramaPrueba = new Hashtable<String, FuentesPrograma[]>();
		FuentesPrograma[] FPtmp;

		Testtmp = new Test[2];
		Testtmp[0] = new Test("Test1_Practica1", "Test 1 de Práctica 1");
		Testtmp[1] = new Test("Test1_Practica1", "Test 2 de Práctica 1");
		TestPracticasPrueba.put(PracticasPrueba[0].getId(), Testtmp);
		FPtmp = new FuentesPrograma[2];
		FPtmp[0] = new FuentesPrograma(Testtmp[0].getId() + "_Fuente1.txt");
		FPtmp[0] = new FuentesPrograma(Testtmp[0].getId() + "_Fuente2.txt");
		FuentesProgramaPrueba.put(Testtmp[0].getId(), FPtmp);
		FPtmp = new FuentesPrograma[1];
		FPtmp[0] = new FuentesPrograma(Testtmp[1].getId() + "_Fuente1.txt");
		FuentesProgramaPrueba.put(Testtmp[1].getId(), FPtmp);


		Testtmp = new Test[1];
		Testtmp[0] = new Test("Test1_Practica2", "Test 1 de Práctica 2");
		TestPracticasPrueba.put(PracticasPrueba[1].getId(), Testtmp);
		FPtmp = new FuentesPrograma[3];
		FPtmp[0] = new FuentesPrograma(Testtmp[0].getId() + "_Fuente1.txt");
		FPtmp[0] = new FuentesPrograma(Testtmp[0].getId() + "_Fuente2.txt");
		FPtmp[0] = new FuentesPrograma(Testtmp[0].getId() + "_Fuente3.txt");
		FuentesProgramaPrueba.put(Testtmp[0].getId(), FPtmp);

		Testtmp = new Test[6];
		Testtmp[0] = new Test("Test1_Practica3", "Test 1 de Práctica 3");
		Testtmp[1] = new Test("Test2_Practica3", "Test 2 de Práctica 3");
		Testtmp[2] = new Test("Test3_Practica3", "Test 3 de Práctica 3");
		Testtmp[3] = new Test("Test4_Practica3", "Test 4 de Práctica 3");
		Testtmp[4] = new Test("Test5_Practica3", "Test 5 de Práctica 3");
		Testtmp[5] = new Test("Test6_Practica3", "Test 6 de Práctica 3");
		TestPracticasPrueba.put(PracticasPrueba[2].getId(), Testtmp);
		FPtmp = new FuentesPrograma[2];
		FPtmp[0] = new FuentesPrograma(Testtmp[0].getId() + "_Fuente1.txt");
		FPtmp[0] = new FuentesPrograma(Testtmp[0].getId() + "_Fuente2.txt");
		FuentesProgramaPrueba.put(Testtmp[0].getId(), FPtmp);
		FPtmp = new FuentesPrograma[4];
		FPtmp[0] = new FuentesPrograma(Testtmp[1].getId() + "_Fuente1.txt");
		FPtmp[0] = new FuentesPrograma(Testtmp[1].getId() + "_Fuente2.txt");
		FPtmp[0] = new FuentesPrograma(Testtmp[1].getId() + "_Fuente3.txt");
		FPtmp[0] = new FuentesPrograma(Testtmp[1].getId() + "_Fuente4.txt");
		FuentesProgramaPrueba.put(Testtmp[1].getId(), FPtmp);
		FPtmp = new FuentesPrograma[1];
		FPtmp[0] = new FuentesPrograma(Testtmp[2].getId() + "_Fuente1.txt");
		FuentesProgramaPrueba.put(Testtmp[2].getId(), FPtmp);
		FPtmp = new FuentesPrograma[1];
		FPtmp[0] = new FuentesPrograma(Testtmp[3].getId() + "_Fuente1.txt");
		FuentesProgramaPrueba.put(Testtmp[3].getId(), FPtmp);
		FPtmp = new FuentesPrograma[2];
		FPtmp[0] = new FuentesPrograma(Testtmp[4].getId() + "_Fuente1.txt");
		FPtmp[0] = new FuentesPrograma(Testtmp[4].getId() + "_Fuente2.txt");
		FuentesProgramaPrueba.put(Testtmp[4].getId(), FPtmp);
		FPtmp = new FuentesPrograma[1];
		FPtmp[0] = new FuentesPrograma(Testtmp[5].getId() + "_Fuente1.txt");
		FuentesProgramaPrueba.put(Testtmp[5].getId(), FPtmp);
		FPtmp = new FuentesPrograma[2];

		Testtmp = new Test[3];
		Testtmp[0] = new Test("Test1_Practica4", "Test 1 de Práctica 4");
		Testtmp[1] = new Test("Test2_Practica4", "Test 2 de Práctica 4");
		Testtmp[2] = new Test("Test3_Practica4", "Test 3 de Práctica 4");
		TestPracticasPrueba.put(PracticasPrueba[3].getId(), Testtmp);
		FPtmp = new FuentesPrograma[2];
		FPtmp[0] = new FuentesPrograma(Testtmp[0].getId() + "_Fuente1.txt");
		FPtmp[0] = new FuentesPrograma(Testtmp[0].getId() + "_Fuente2.txt");
		FuentesProgramaPrueba.put(Testtmp[0].getId(), FPtmp);
		FPtmp = new FuentesPrograma[4];
		FPtmp[0] = new FuentesPrograma(Testtmp[1].getId() + "_Fuente1.txt");
		FPtmp[0] = new FuentesPrograma(Testtmp[1].getId() + "_Fuente2.txt");
		FPtmp[0] = new FuentesPrograma(Testtmp[1].getId() + "_Fuente3.txt");
		FPtmp[0] = new FuentesPrograma(Testtmp[1].getId() + "_Fuente4.txt");
		FuentesProgramaPrueba.put(Testtmp[1].getId(), FPtmp);
		FPtmp = new FuentesPrograma[1];
		FPtmp[0] = new FuentesPrograma(Testtmp[2].getId() + "_Fuente1.txt");
		FuentesProgramaPrueba.put(Testtmp[2].getId(), FPtmp);
		FPtmp = new FuentesPrograma[1];


	}

	//Comportamiento que obtiene las practicas disponibles para corregir
	public class PracCorrecBehaviour extends OneShotBehaviour {

		private ACLMessage resp1;
		private AbsIRE ire1;

		public PracCorrecBehaviour(Agent _a, ACLMessage msg1, AbsIRE ireAux) {
			super(_a);
			this.ire1 = ireAux;
			this.resp1 = msg1;
		}

		public void action() {
			

			//			 --> PracticasDisponibles <--------------------------------------------------
			Practica[] lpractn = PracticasDisponibles();

			// Creamos el listado de practicas de forma abstracta
			AbsAggregate absPracticas = new AbsAggregate(BasicOntology.SET);

			for (int i = 0; i < lpractn.length; i++) {
				//Creamos un objecto abstracto por cada practica y la a�adimos al aggregate
				AbsConcept elem;
				try {
					elem = (AbsConcept) ontologia.fromObject(lpractn[i]);
					absPracticas.add(elem);
				} catch (OntologyException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

			//Creamos el predicado abstracto EQUALS
			AbsPredicate equalPred = new AbsPredicate(SLVocabulary.EQUALS);
			equalPred.set(SLVocabulary.EQUALS_LEFT, ire1);
			equalPred.set(SLVocabulary.EQUALS_RIGHT, absPracticas);

			//Mandamos el predicado al interfaz
			try {
				getContentManager().fillContent(resp1, equalPred);
			} catch (CodecException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (OntologyException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			send(resp1);
		}
	}

	//Comportamiento que manda los tests al agente Interfaz
	public class TestsCorrecBehaviour extends OneShotBehaviour {

		private ACLMessage mens1;
		private List<AbsPredicate> pred1;
		private AbsIRE ire1;
		private AndBuilder predicado;

		//public TestsCorrecBehaviour(Agent _a, ACLMessage msg1, List<AbsPredicate> pred, AbsIRE ire) {
		public TestsCorrecBehaviour(Agent _a, ACLMessage msg1, AndBuilder predicado1, AbsIRE ire) {	
			super(_a);
			this.mens1 = msg1;
			//this.pred1 = pred;
			this.ire1 = ire;
			this.predicado = predicado1;
		}

		public void action() {

			//Obtenemos el predicado TESTS
			Tests tes;
			try {
				List<AbsPredicate> listaT = new ArrayList<AbsPredicate>();
				listaT = predicado.getPredicateList(pacaOntology.TESTS);
				
				
				//Iterator<AbsPredicate> it = pred1.iterator();
				Iterator<AbsPredicate> it = listaT.iterator();
				tes = (Tests) ontologia.toObject(it.next());
				
				Test[] te;
				//Obtenemos los tests disponibles para la practica seleccionada
				te = TestParaPractica(tes.getPractica().getId());

				AbsAggregate absTests = new AbsAggregate(BasicOntology.SET);


				// Adds the Test
				//Pasamos los tests a objectos abstractos
				for (int i = 0; i < te.length; i++) {
					AbsConcept elem = (AbsConcept) ontologia.fromObject(te[i]);
					absTests.add(elem);
				}

				//Modifificacion Carlos
				AbsPredicate qrr = new AbsPredicate(SL1Vocabulary.EQUALS);
				qrr.set(SL1Vocabulary.EQUALS_LEFT, ire1);
				qrr.set(SL1Vocabulary.EQUALS_RIGHT, absTests);

				try {
					getContentManager().fillContent(mens1, qrr);
					myAgent.send(mens1);
				} catch (CodecException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			} catch (UngroundedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (OntologyException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
	}

	//Comportamiento que manda los ficheros a corregir
	public class FicherosCorrBehaviour extends OneShotBehaviour {

		private ACLMessage mens1;
		private List<AbsPredicate> lCorrige;
		private List<AbsPredicate> ltests;
		private AbsIRE ire1;
		private AndBuilder predicado;

		//public FicherosCorrBehaviour(Agent _a, ACLMessage msg1, List<AbsPredicate> lCorrige1, List<AbsPredicate> predDcha, AbsIRE ire) {
		public FicherosCorrBehaviour(Agent _a, ACLMessage msg1, AndBuilder predicado1, AbsIRE ire) {
			super(_a);
			this.mens1 = msg1;
			//this.lCorrige = lCorrige1;
			//this.ltests = predDcha;
			this.predicado = predicado1;
			this.ire1 = ire;
		}

		public void action() {
			
			Corrige corr;
			try {
				
				List<AbsPredicate> lisCorrige = predicado.getPredicateList(pacaOntology.CORRIGE);
				List<AbsPredicate> listTests = predicado.getPredicateList(pacaOntology.TESTS);
				
				
				//Iterator<AbsPredicate> itCor = lCorrige.iterator();
				Iterator<AbsPredicate> itCor = lisCorrige.iterator();
				
				corr = (Corrige) ontologia.toObject(itCor.next());
				Practica pract = corr.getPractica();
				
				//Guardamos todos los Test que nos han pedido
				//Test[] testAux3 = ExtraeTestsPedidos(ltests);
				Test[] testAux3 = ExtraeTestsPedidos(listTests);
				
				//Obtenemos los ficheros fuentes necesarios para cada Test
				FuentesPrograma[] fp = FicheroParaPractica(pract.getId(), testAux3);

				// Creamos el listado de ficheros de forma abstracta
				AbsAggregate absFicheros = new AbsAggregate(BasicOntology.SET);

				for (int i = 0; i < fp.length; i++) {
					//Creamos un objecto abstracto por cada fichero y la a�adimos al aggregate
					AbsConcept elem;
					try {
						elem = (AbsConcept) ontologia.fromObject(fp[i]);
						
						// Work Around: rormartin
						// TODO: FicheroParaPracticas work fine?
						if (elem != null) {
							absFicheros.add(elem);
						}
					} catch (OntologyException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

				}
				

				//Creamos el predicado abstracto EQUALS
				AbsPredicate equalPred = new AbsPredicate(SLVocabulary.EQUALS);
				equalPred.set(SLVocabulary.EQUALS_LEFT, ire1);
				equalPred.set(SLVocabulary.EQUALS_RIGHT, absFicheros);

				try {
					getContentManager().fillContent(mens1, equalPred);
				} catch (CodecException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (OntologyException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				myAgent.send(mens1);
			} catch (UngroundedException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} catch (OntologyException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}

		}
	}
	
	
	public class CorrigePractBehaviour extends Behaviour{
		
		private AbsContentElement mensaje;
		private ACLMessage respuesta;
		private String quien1;
		
		boolean done = false;
		
		public CorrigePractBehaviour(Agent _a, ACLMessage resp1, AbsContentElement msg1, String quien){
			super(_a);
			this.mensaje = msg1;
			this.respuesta = resp1;
			this.quien1 = quien;
		}
		public void action(){
			try {
				
				//A�adido para realizar pruebas
				/*if (ejecucionEnPruebas2){
					Aleatorio rand = new Aleatorio();
					long retardo = rand.nextInt(500, 3000);
					Date fechaActual = new Date();
					Date fechaLimite = new Date(fechaActual.getTime() + retardo);
					
					while ((new Date()).before(fechaLimite)){
						long bloqueo = fechaLimite.getTime()-(new Date()).getTime();
						block(bloqueo);
					}
				}*/
				
				
				//Fin a�adido pruebas	
				
				//Actualizamos el DF
				/*numeroCorrecciones++;
				
				if ((new Date()).after(fechaLimite)){
					numeroCorrecciones = numeroCorrecciones - numeroCorrecionesActual;
					ActualizacionDF();
					numeroCorrecionesActual = numeroCorrecciones;
				}*/
				
				
				Aleatorio rand = new Aleatorio();
				long retardo = rand.nextInt(tiempo_minimo, tiempo_maximo);
				
				Date fechaActual = new Date();
				Date fechaLimite = new Date(fechaActual.getTime() + retardo);
				
				while ((new Date()).before(fechaLimite)){
					
				}
				
				numeroCorrecciones++;
				ActualizacionDF();
				
				
				AbsIRE iotaPred = (AbsIRE) mensaje;
				
				//Obtenemos la proposicion del predicado
				AbsPredicate qiota = (AbsPredicate) iotaPred.getProposition();
				
				AndBuilder predicado = new AndBuilder();
				predicado.addPredicate(qiota);

				//Obtenemos todos los predicados en forma de listas
				List<AbsPredicate> listCorr = predicado.getPredicateList(pacaOntology.CORRIGE);
				List<AbsPredicate> listTests = predicado.getPredicateList(pacaOntology.TESTS);
				List<AbsPredicate> listFich = predicado.getPredicateList(pacaOntology.FICHEROFUENTES);
				List<AbsPredicate> listEvap = predicado.getPredicateList(pacaOntology.EVALUAPRACTICA);
				
				Iterator<AbsPredicate> it = listCorr.iterator();
				Corrige cor1 = (Corrige) ontologia.toObject(it.next());

				Iterator<AbsPredicate> it2 = listEvap.iterator();
				EvaluacionPractica eva1 = (EvaluacionPractica) ontologia.toObject(it2.next());

				Test [] Te = ExtraeTestsPedidos(listTests);
				FuentesPrograma [] FP = ExtraeFuentesPedidos(listFich);

				Practica pract = cor1.getPractica();
				Alumno al = eva1.getAlumno();

				EvaluacionPractica evaP = EnvioCorreccionAlumno(pract, FP, Te, al, quien1);

				String contenido = evaP.getTextoEvaluacion();
				
				ResultadoEvaluacion rEvap = new ResultadoEvaluacion();
				rEvap.setResultadoEvaluacionTexto(contenido);

				AbsConcept absEvap;

				absEvap = (AbsConcept) ontologia.fromObject(rEvap);


				AbsPredicate qrr = new AbsPredicate(SL1Vocabulary.EQUALS);
				qrr.set(SL1Vocabulary.EQUALS_LEFT, iotaPred);
				qrr.set(SL1Vocabulary.EQUALS_RIGHT, absEvap);

				getContentManager().fillContent(respuesta,qrr);
				myAgent.send(respuesta);
				
				/*//Actualizamos fechas
				fechaActual = new Date();
				fechaLimite = new Date(fechaActual.getTime() + intervalo);*/
				
				
				
			} catch (CodecException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				
			} catch (OntologyException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				
			}
			done = true;
						
		}
		public final boolean done(){
			return done;
		}
	}
	
	//public class ActualizaCorrecciones extends Behaviour{
	public class ActualizaCorrecciones extends CyclicBehaviour{
	 
		boolean finalizado = false;
		
		public void action(){
			if ((new Date()).after(fechaLimite)){
				numeroCorrecciones = numeroCorrecciones - numeroCorrecionesActual;
				//System.out.println(myAgent.getName()+": "+numeroCorrecciones);
				ActualizacionDF();
				numeroCorrecionesActual = numeroCorrecciones;
				//Actualizamos fechas
				fechaActual = new Date();
				fechaLimite = new Date(fechaActual.getTime() + intervalo);
				
			}
			else{
				block(3000);
			}
			finalizado = true;
			
		}

		/*@Override
		public boolean done() {
			// TODO Auto-generated method stub
			return finalizado;
		}*/
		
	}
	
	
	

	/**
	Comportamiento que encapsula la funcionalidad del agente corrector, se ejecuta de manera c�clica hasta que 
	el agente muere.
	 */
	class CorrectorBehaviour extends CyclicBehaviour {

		ACLMessage msg_in;
		ACLMessage msg_out;
		private boolean finish = false;

		/**
		Constructor.
		 */
		public CorrectorBehaviour(Agent a) {
			super(a);
			printError("Creada CorrectorBehaviour");
		}

		/**	
		M�todo que se ejecuta cada vez que se inicia el comportamiento.
		 */
		public void action() {

			// Start
			
						
			ACLMessage msg = receive();

			if (msg != null) {

				ACLMessage reply = msg.createReply();

				// The QUERY message could be a QUERY-REF 
				// or REQUEST. In this case reply 
				// with NOT_UNDERSTOOD
				// SUBSCRIBE is valid for enable debug mode
				if (msg.getPerformative() != ACLMessage.QUERY_REF &
						msg.getPerformative() != ACLMessage.REQUEST) {

					if (msg.getPerformative() == ACLMessage.SUBSCRIBE) {
						if (debugMode) {
							printError("Modo depuraci�n DESACTIVADO");
							debugMode = !(debugMode);
						} else {
							agente = msg.getSender();
							debugMode = !(debugMode);
							printError("Modo depuraci�n ACTIVADO");
						}
					} else {
						reply.setPerformative(ACLMessage.NOT_UNDERSTOOD);
						String content = "(" + msg.toString() + ")";
						reply.setContent(content);
					}
				} else {


					try {
						reply.setPerformative(ACLMessage.INFORM);

						// If the message was a QUERY-REF, manage here
						if (msg.getPerformative() == ACLMessage.QUERY_REF) {

							AbsContentElement l_in = null;
							l_in = getContentManager().extractAbsContent(msg);
							String requestedInfoName = l_in.getTypeName();
							
							// If the receive message isn't a QUERY-REF (all or
							// QUERY-REF (iota, reply a NOT_UNDERTOOD message

							if ((!requestedInfoName.equals(SL2Vocabulary.ALL)) & (!requestedInfoName.equals(SL2Vocabulary.IOTA))) {
								reply.setPerformative(ACLMessage.NOT_UNDERSTOOD);
								String content = "(" + msg.toString() + ")";
								reply.setContent(content);
							} 
							else {

								if (requestedInfoName.equals(SL2Vocabulary.IOTA)) {
									// If QUERY-REF (iota  
									// --> EnvioCorrecionAlumno <--------------------------------------------------
									//printError("Dentro de EnvioCorrecionAlumno");
																	
									addBehaviour(new CorrigePractBehaviour(this.myAgent, reply, l_in, msg.getSender().getLocalName()));
									
								} 
								else {
									// Here, is QUERY-REF (all

									AbsIRE allPred = (AbsIRE) l_in;

									//Obtenemos la proposicion del predicado
									AbsPredicate qall = (AbsPredicate) allPred.getProposition();

									//Obtenemos el predicado original (CORRIGE, PRACTICAS, TESTS... )
									String requestedInfo2Name = qall.getTypeName();
									
									if (requestedInfo2Name.equals(pacaOntology.CORRIGE)) {
										// --> PracticasDisponibles <--------------------------------------------------
										addBehaviour(new PracCorrecBehaviour(this.myAgent, reply, allPred));
									} 
									else {
										AndBuilder predicado = new AndBuilder();
										predicado.addPredicate(qall);
									
										if (predicado.existsPredicate(pacaOntology.FICHEROFUENTES)){
											addBehaviour(new FicherosCorrBehaviour(this.myAgent, reply, predicado, allPred));
										} 
										else {
											// --> TestPorPractica <-------------------------------------------------------
											addBehaviour(new TestsCorrecBehaviour(this.myAgent, reply, predicado, allPred));
										}
									}

								}
							}
						} // The message was REQUEST
						else {

							printError("Dentro de EntregarPractica");

							// Extract the content of the message
							//List l_in = myAgent.extractMsgContent(msg);
							//Modificacion Carlos	
							
							
							
							
							List l_in = (List) msg.getContentObject();

							Action act = new Action();
							//act = (Action) l_in.get(0);

							// Extract the practica from the message
							//And and1 = (And) act.get_1();
							//Modificacion Carlos
							AbsObject and1 = (AbsObject) act.getAction();


							//EntregarPractica enPract = (EntregarPractica) and1.get_0();
							//Modificacion Carlos
							EntregarPractica enPract = (EntregarPractica) and1.getAbsObject(SL1Vocabulary.AND_LEFT);
							Practica pract = enPract.getPractica();
							//And and2 = (And) and1.get_1();
							//And and3 = (And) and2.get_1();
							//And and4 = (And) and3.get_1();
							AbsObject and2 = and1.getAbsObject(SL1Vocabulary.AND_RIGHT);
							AbsObject and3 = and2.getAbsObject(SL1Vocabulary.AND_RIGHT);
							AbsObject and4 = and3.getAbsObject(SL1Vocabulary.AND_RIGHT);

							//Lista secFuentes = (Lista) and3.get_0();
							ArrayList secFuentes = new ArrayList();
							secFuentes = (ArrayList) and3.getAbsObject(SL1Vocabulary.AND_LEFT);

							//List listFF = secFuentes.getAll_0_List();
							List listFF = (List) secFuentes.iterator();

							FuentesPrograma[] FP =
								new FuentesPrograma[listFF.toArray().length];
							FicheroFuentes ff = new FicheroFuentes();

							for (int i = 0; i < listFF.toArray().length; i++) {
								ff = (FicheroFuentes) listFF.toArray()[i];
								FP[i] = ff.getFuentesPrograma();
							}

							//Interactua inter = (Interactua) and4.get_0();
							Interactua inter = (Interactua) and4.getAbsObject(SL1Vocabulary.AND_LEFT);
							//FormaGrupoCon formg = (FormaGrupoCon) and4.get_1();
							FormaGrupoCon formg = (FormaGrupoCon) and4.getAbsObject(SL1Vocabulary.AND_RIGHT);


							Alumno alre = EntregarPractica(pract,
									FP,
									formg.getAlumno1(),
									formg.getAlumno2());

							// If alre.getIdentificador == "", all ok

							if (alre.getIdentificador().equals("")) {

								//DonePredicate dact = new DonePredicate();
								//Modificacion Carlos

								Done dact = new Done();

								//dact.set_0(act);
								//Modificacion Carlos
								dact.setAction(act);



								List l_out = new ArrayList();
								l_out.add(dact);

								//myAgent.fillMsgContent(reply,l_out);
								//getContentManager().fillContent(reply, (AbsContentElement) l_out);
								reply.setContentObject((Serializable) l_out);

							} // FAILURE
							else {
								reply.setPerformative(ACLMessage.FAILURE);

								//And andfailure = new And();
								//Modificacion Carlos
								AbsPredicate andfailure = new AbsPredicate(SL1Vocabulary.AND);
								andfailure.set(SL1Vocabulary.AND_LEFT, (AbsObject) enPract);
								//andfailure.set_0(enPract);
								Interactua interfailure = new Interactua();
								interfailure.setInterfaz(inter.getInterfaz());
								interfailure.setAlumno(alre);
								//andfailure.set_1(interfailure);
								//Modificacion Carlos
								andfailure.set(SL1Vocabulary.AND_RIGHT, (AbsObject) interfailure);

								List l_out = new ArrayList();
								l_out.add(act);
								l_out.add(andfailure);

								//myAgent.fillMsgContent(reply,l_out);
								//getContentManager().fillContent(reply, (AbsContentElement) l_out);
								reply.setContentObject((Serializable) l_out);
							}


						}

					} //catch (FIPAException fe) {
					//printError(myAgent.getLocalName()+" Fill/extract content unsucceeded. Reason:" + fe.getMessage());
					//}
					catch (OntologyException oe) {
						printError(myAgent.getLocalName() + " getRoleName() unsucceeded. Reason:" + oe.getMessage());
						oe.printStackTrace();
					} catch (java.lang.NullPointerException e) {
						printError("Empty message");
						e.printStackTrace();
					} catch (Exception e) {
						printError("Error en Corrector de PACA: " + e.toString());
						e.printStackTrace();
					}
				}
				
			} //============================== cambios para el receive no bloqueante ==========
			else {
				
				//addBehaviour(new ActualizaCorrecciones());
				block();
			}
			//============================== fin cambios receive no bloqueante ==============

		}
	}

	/**
	M�todo que configura el agente. Selecciona el lenguaje de contenido, la ontolog�a que utiliza
	y aniade el comportamiento que se ejecutar�.
	 */
	protected void setup() {

		if (this.ejecucionEnPruebas) {
			this.InicializaDatosPruebas();
		}

		AID nombreAgente = getAID();
		
		String nombreC = nombreAgente.getLocalName();
		
		int ultimaPos = nombreC.length()-1;
		
		char ultimaletra = nombreC.charAt(ultimaPos);
		
		if (java.lang.Character.toString(ultimaletra).equals("x")){
			tiempo_maximo = 5000;
			tiempo_minimo = 1000;
		}
		else{

			if (java.lang.Character.getNumericValue(ultimaletra)%2!=0){
				tiempo_maximo = 2000;
				tiempo_minimo = 1000;
			}
			else{ // Si el nombre termina en par...
				tiempo_maximo = 5000;
				tiempo_minimo = 3000;
			}
		}
		
		
		// Register the codec for the SL0 language
		getContentManager().registerLanguage(codec);

		// Register the ontology used by this application
		getContentManager().registerOntology(ontologia);
		
		RegistroDF();
		
		

		/*
		//DFService
		DFAgentDescription dfd = new DFAgentDescription();
		AID nombreAgente = getAID();
		
		Property prop = new Property();
		prop.setName("Correciones");
		prop.setValue(numeroCorrecciones);
		
		dfd.setName(nombreAgente);
		if (debug) {
			System.out.println(nombreAgente.toString() + " quiere registrarse");
		}

		ServiceDescription sd = new ServiceDescription();
		sd.setType("Corrector");
		sd.setName("Agente-Corrector");
		sd.addProperties(prop);
		dfd.addServices(sd);
		try {
			DFService.register(this, dfd);
		} catch (FIPAException fe) {
			System.out.println("Excepcion al registrar Corrector");
			fe.printStackTrace();
		}

		if (debug) {
			System.out.println(nombreAgente.toString() + " se ha registrado correctamente");
		}
		*/


		// Create the FSMEBehaviour
		CorrectorBehaviour Principal = new CorrectorBehaviour(this);

		// Add the behaviour to the agent
		addBehaviour(Principal);
		//addBehaviour(new ActualizaCorrecciones());
	}


	// Funcions for comunicate a Java program with Unix file system 
	// and auto-correct script
	/**
	Obtiene las pr�cticas disponibles en el sistema.
	 */
	private Practica[] PracticasDisponibles() {


		if (this.ejecucionEnPruebas) {

			return this.PracticasPrueba;


		} else {
			if (debug) {
				System.out.println("DIRECTORIO: " + workDir);
			}
			Practica[] Practicas;
			File DirPracticas = new File(workDir);

			// If workDir isn't a directory, practicas aren't aviable
			if (!DirPracticas.isDirectory()) {
				if (debug) {
					System.out.println("No existe el directorio de practicas");
				}
				Practicas = new Practica[0];
			} else {
				if (debug) {
					System.out.println("Estamos en el directorio de practicas");
				}
				String[] NombrePracticas = DirPracticas.list();

				// Asignation the name of the practicas...
				// First count the real practica (directories)
				int countReal = 0;

				Practica[] PracticasTemp = new Practica[NombrePracticas.length];

				for (int i = 0; i < NombrePracticas.length; i++) {
					// The test is a directory ... test for exclude
					// files
					File Temp1 = new File(workDir + NombrePracticas[i]);

					if (Temp1.isDirectory()) {
						PracticasTemp[countReal] = new Practica(NombrePracticas[i]);
						PracticasTemp[countReal].setDescripcion("");
						countReal++;
					}
				}

				// Add only the directories
				Practicas = new Practica[countReal];

				for (int i = 0; i < countReal; i++) {
					Practicas[i] = PracticasTemp[i];
				}
			}
			return Practicas;
		}
	}

	/**
	Obtiene los tests para una pr�ctica concreta.
	 */
	private Test[] TestParaPractica(String practica) {


		if (this.ejecucionEnPruebas) {

			return this.TestPracticasPrueba.get(practica);

		} else {


			Test[] te = new Test[0];
			String[] ListaTest;

			File practDir = new File(workDir + practica);

			// The practDir must be a directory
			if (!practDir.isDirectory()) {
				//Return 0 files
				te = new Test[0];
			} else {
				// Extract the list of test of the practica
				String[] ListaTestTemp1 = practDir.list();

				// The tests are directories only
				// Count the number of directories for add to ListaTest
				int countReal = 0;

				String[] ListaTestTemp2 = new String[ListaTestTemp1.length];

				for (int i = 0; i < ListaTestTemp1.length; i++) {
					File temp1 = new File(workDir +
						practica + File.separator +
						ListaTestTemp1[i]);
					if (temp1.isDirectory()) {
						ListaTestTemp2[countReal] = ListaTestTemp1[i];
						countReal++;
					}
				}

				// Store in te a clean list of test
				te = new Test[countReal];
				for (int i = 0; i < countReal; i++) {
					te[i] = new Test();

					// The Id of the test
					te[i].setId(ListaTestTemp2[i]);

					// The description of the test
					File fiTest = new File(workDir +
						practica + File.separator +
						ListaTestTemp2[i] + File.separator +
						ficheroConfTest);
					te[i].setDescripcion(descriptionFile(fiTest));
				}
			}
			return te;
		}
	}

	/**
	Obtiene todos los fuentes necesarios para los tests de una pr�ctica.
	 */
	private FuentesPrograma[] FicheroParaPractica(String practica,
		Test[] ListaTest) {

		if (this.ejecucionEnPruebas) {

			java.util.ArrayList<FuentesPrograma> lista =
				new java.util.ArrayList<FuentesPrograma>();

			for (Test test : ListaTest) {
				for (FuentesPrograma fp : this.FuentesProgramaPrueba.get(test.getId())){
					if (fp != null) {
						lista.add(fp);
					}
				}
			}

			FuentesPrograma fpr[] = new FuentesPrograma[lista.size()];
			
			int i = 0;
			for (FuentesPrograma fp: lista) {
				fpr[i++] = fp;
			}
			return fpr;
			
		} else {

			FuentesPrograma[] FP;

			// OK, now read the configuration file each test and
			// look for "ALUMNO" tag. The "ALUMNO" tag tell that the
			// file use in the test must be send by ALUMNO

			String[] NamesAr = new String[0];

			for (int i = 0; i < ListaTest.length; i++) {

				File fiTest = new File(workDir +
					practica + File.separator +
					ListaTest[i].getId() + File.separator +
					ficheroConfTest);

				NamesAr = joinWithoutDuplicates(NamesAr,
					searchAlumnoFiles(fiTest));
			}

			/* Create the FuentesPrograma object with the
			 * names
			 */
			FP = new FuentesPrograma[NamesAr.length];

			for (int i = 0; i < NamesAr.length; i++) {
				FP[i] = new FuentesPrograma(NamesAr[i]);
				FP[i].setContenido("");
			}

			return FP;
		}
	}

	/**
	Lee la descripci�n del fichero (la primera l�nea).
	 */
	private String descriptionFile(File file) {


		String desc = "";
		ArrayList al = new ArrayList();

		try {

			FileReader r = new FileReader(file);

			BufferedReader bfre = new BufferedReader(r);

			try {
				boolean bucle = true;
				desc = bfre.readLine();
			} catch (IOException e) {
			}

		} catch (java.io.FileNotFoundException e) {
			return desc;
		} catch (Exception e) {
			printError("Error en Corrector de PACA: " + e.toString());
		}

		return desc;

	}

	/**
	Lee la l�nea del dead-line.
	 */
	private boolean practicaInTime(Practica pract) {

		if (this.ejecucionEnPruebas) {
			return true;
		} else {

			String desc = "";
			boolean ret = false;
			ArrayList al = new ArrayList();

			try {

				File file = new File(workDir + pract.getId() + File.separator + ficheroConfPractica);

				FileReader r = new FileReader(file);

				BufferedReader bfre = new BufferedReader(r);

				try {
					// The dead-line date is in the 5th line
					for (int i = 1; i <= 5; i++) {
						desc = bfre.readLine();
					}
				} catch (IOException e) {
				}

				// 99/99/9999  --> no dead-line
				if (desc.equals("99/99/9999")) {
					return true;
				}


				//DateFormat df = DateFormat.getDateInstance();
				DateFormat df = DateFormat.getDateInstance(DateFormat.SHORT);
				Date today = new Date();

				try {
					Date d = df.parse(desc);

					return (d.compareTo(today) > 0);
				} catch (Exception e) {
					return ret;
				}

			} catch (java.io.FileNotFoundException e) {
				return ret;
			} catch (Exception e) {
				printError("Error en Corrector de PACA: " + e.toString());
				return ret;
			}
		}
	}

	/** 
	Busca los ficheros necesarios que est�n reflejados como "ALUMNO/" en el fichero
	de configuraci�n.
	 */
	private String[] searchAlumnoFiles(File file) {


		String[] fichAl;
		ArrayList al = new ArrayList();

		try {

			FileReader r = new FileReader(file);

			BufferedReader bfre = new BufferedReader(r);

			try {
				boolean bucle = true;
				String temp1;
				while (bucle) {
					temp1 = bfre.readLine();
					if (temp1 == null) {
						bucle = false;
					} else {
						if (temp1.startsWith("ALUMNO/")) {
							al.add(temp1.substring(7));
						}
					}
				}
			} catch (IOException e) {
			} catch (Exception e) {
				printError("Error en Corrector de PACA: " + e.toString());
			}

		} catch (java.io.FileNotFoundException e) {
		} catch (Exception e) {
			printError("Error en Corrector de PACA: " + e.toString());
		}

		String[] returnable = new String[al.toArray().length];
		for (int i = 0; i < al.toArray().length; i++) {
			returnable[i] = (String) al.toArray()[i];
		}

		return returnable;

	}

	/**
	Une dos arrays de Strings y elimina los elementos duplicados.
	 */
	private String[] joinWithoutDuplicates(String[] a1, String[] a2) {

		ArrayList al = new ArrayList();

		/* Create an ArrayList and use the contains method. Add the
		 * a1 array to the ArrayList 
		 */
		for (int i = 0; i < a1.length; i++) {
			al.add(a1[i]);
		}

		/* Add the element from a2 to the ArrayList, if the element don't
		 * exist in the ArrayList
		 */
		for (int i = 0; i < a2.length; i++) {
			if (!al.contains(a2[i])) {
				al.add(a2[i]);
			}
		}

		String[] returnable = new String[al.toArray().length];
		for (int i = 0; i < al.toArray().length; i++) {
			returnable[i] = (String) al.toArray()[i];
		}

		return returnable;
	}

	/**
	Ejecuta el script de correcci�n y devuelve el resultado de la misma.
	 */
	private EvaluacionPractica EnvioCorreccionAlumno(Practica pract,
		FuentesPrograma[] fp,
		Test[] test,
		Alumno al,
		String agente) {

		if (this.ejecucionEnPruebas2) {

			EvaluacionPractica EvaP = new EvaluacionPractica();
			
			// Build xml Evaluation from parameters, for test
			String text_eva = "";
			
			text_eva += "<Practica fecha=\"" + new java.util.Date() + 
				"\" usuario=\"" + al.getIdentificador() + "\" identificador=\"" +
				pract.getId() + "\"><Descripcion><![CDATA[" + pract.getDescripcion() +
				"]]></Descripcion>";
			
			for (Test t: test) {
				text_eva += "<Test identificador=\"" + t.getId() + "\">" + 
					"<Descripcion><![CDATA[" + t.getDescripcion() + "]]></Descripcion>" +
					"<EvaluacionTest codigoEvaluacionTest=\"terminacion_incorrecta\">" +
					"</EvaluacionTest>";
				
				for (FuentesPrograma f: fp) {
					text_eva += "<Caso identificador=\"" + f.getNombre() + 
						"\"><EvaluacionCaso codigoEvaluacionCaso=\"terminacion_incorrecta\">" +
						"<Descripcion><![CDATA[" +
						f.getContenido() +
						"]]></Descripcion></EvaluacionCaso></Caso>";
				}
				
				text_eva += "</Test>";
			} 
				
			
			text_eva += "</Practica>";
			
			EvaP.setTextoEvaluacion(text_eva);
			return EvaP;


		} else {

			EvaluacionPractica EvaP = new EvaluacionPractica();

			// Create the temp directory (if not exist)

			File dirTemporal = new File(tempDir + agente);
			File dirExecTemp = new File(tempDir + agente + File.separator +
				"running");
			dirTemporal.mkdirs();
			dirExecTemp.mkdirs();

			// Delete all the files in the dirExecTemp

			File[] fileList;
			fileList = dirExecTemp.listFiles();
			File fileToDelete;

			for (int i = 0; i < fileList.length; i++) {
				fileToDelete = (File) fileList[i];
				fileToDelete.delete();
			}


			// Save the files to disk, in the temporal directory with the
			// name of the sender agent

			FileWriter filew;
			File temp1;

			for (int i = 0; i < fp.length; i++) {
				try {
					temp1 = new File(tempDir + agente + File.separator +
						fp[i].getNombre());
					temp1.createNewFile();
					filew = new FileWriter(temp1);

					filew.write(fp[i].getContenido());
					filew.close();
				} catch (IOException e) {
					System.out.println("Error al escribir en disco");
					e.printStackTrace();
				} catch (Exception e) {
					System.out.println("Error al escribir.");
					e.printStackTrace();
					printError("Error en Corrector de PACA: " + e.toString());
				}
			}

			// Execute the script

			String[] comando_parametros;

			if (scriptFile.equals("/home/public/Proyectos/PACA/paca/script/ca_nuevo3-XML.pl")) {

				comando_parametros = new String[6 + test.length];

				comando_parametros[0] = interprete;
				comando_parametros[1] = scriptFile;
				comando_parametros[2] = workDir + pract.getId();
				comando_parametros[3] = libraryDir;
				comando_parametros[4] = tempDir + agente;
				comando_parametros[5] = tempDir + agente + File.separator + "running";

				for (int i = 0; i < test.length; i++) {
					comando_parametros[i + 6] = test[i].getId();
				}
			} else // for the new versions
			{
				comando_parametros = new String[7 + test.length];

				comando_parametros[0] = interprete;
				comando_parametros[1] = scriptFile;
				comando_parametros[2] = workDir + pract.getId();
				comando_parametros[3] = libraryDir;
				comando_parametros[4] = tempDir + agente;
				comando_parametros[5] = al.getIdentificador();
				comando_parametros[6] = tempDir + agente + File.separator + "running";

				for (int i = 0; i < test.length; i++) {
					comando_parametros[i + 7] = test[i].getId();
				}

				String ercom = "";
				for (int i = 0; i < 7 + test.length; i++) {
					ercom = ercom + comando_parametros[i] + " ";
				}
				printError(ercom);

			}

			// 0 -> Normal exit    != 0 -> Error
			int returnValue = 0;

			try {
				printError("Antes de ejecutar de nuevo el programa");

				Process proc = java.lang.Runtime.getRuntime().exec(comando_parametros);
				printError("Despu�s de ejecutar de nuevo el programa");


				returnValue = proc.waitFor();
				printError("Despu�s de esperar por el proceso");


				// Show Error messajes in the standard error console
				StringWriter errorSt = new StringWriter();
				InputStream errorIs = proc.getErrorStream();
				int temp_error_read;
				temp_error_read = errorIs.read();
				while (temp_error_read != -1) {
					errorSt.write(temp_error_read);
					temp_error_read = errorIs.read();
				}

				printError("Script terminate with " + returnValue);
				printError(errorSt.toString());


			} catch (java.lang.InterruptedException e) {
				printError("Error en Corrector de PACA: " + e.toString());
			} catch (IOException e) {
				printError("Error en Corrector de PACA: " + e.toString());
			} catch (Exception e) {
				printError("Error en Corrector de PACA: " + e.toString());
			}


			// The script executed without errors
			if (returnValue == 0) {
				// Read the evaluacion file

				try {
					StringWriter stwriter = new StringWriter();
					FileReader frader = new FileReader(tempDir + agente +
						File.separator +
						ficheroEvaluacion);


					int temp_read;

					temp_read = frader.read();
					while (temp_read != -1) {
						stwriter.write(temp_read);
						temp_read = frader.read();
					}

					EvaP.setTextoEvaluacion(stwriter.toString());

				} catch (FileNotFoundException e) {
					EvaP.setTextoEvaluacion(errorXML);
				} catch (IOException e) {
				} catch (Exception e) {
					printError("Error en Corrector de PACA: " + e.toString());
				}

				// --- Delete all the temp files

				// Delete all the files in the dirExecTemp

				fileList = dirExecTemp.listFiles();

				for (int i = 0; i < fileList.length; i++) {
					fileToDelete = (File) fileList[i];
					fileToDelete.delete();
				}

				// Delete all the files in the dirTemporal

				fileList = dirTemporal.listFiles();

				for (int i = 0; i < fileList.length; i++) {
					fileToDelete = (File) fileList[i];
					fileToDelete.delete();
				}

				// Delete dirTemporal

				dirTemporal.delete();

			// End of delete temp files

			} else {
				// The script don't work fine, return an error message
				EvaP.setTextoEvaluacion(errorXML);
			}


			return EvaP;
		}

	}

	/**
	Envia los fuentes de una pr�ctica para que sean almacenados en el sistema.
	 */
	private Alumno EntregarPractica(Practica pract,
		FuentesPrograma[] fp,
		Alumno al1,
		Alumno al2) {

		if (this.ejecucionEnPruebas) {
			return new Alumno();
		}
		// Verify that the al1 and al1 don't EntregarPractica before.

		File dirPracticas = new File(entregaFinalDir +
			pract.getId());

		dirPracticas.mkdirs();

		File[] fileList;
		fileList = dirPracticas.listFiles();
		boolean existEntrega = false;
		Alumno badAlumno = new Alumno();

		for (int i = 0; i < fileList.length; i++) {

			String fileName = fileList[i].getName();

			if (fileName.startsWith(al1.getIdentificador() + "-") || fileName.endsWith("-" + al1.getIdentificador())) {
				badAlumno = al1;
				existEntrega = true;
			}

			if (!al2.getIdentificador().equals("")) {
				if (fileName.startsWith(al2.getIdentificador() + "-") || fileName.endsWith("-" + al2.getIdentificador())) {
					badAlumno = al2;
					existEntrega = true;
				}
			}
		}

		boolean practInTime = practicaInTime(pract);
		printError(String.valueOf(practInTime));
		printError(String.valueOf(existEntrega));

		if (!practInTime) {
			Alumno altemp = new Alumno();
			altemp.setIdentificador("Entrega fuera de plazo");
			badAlumno = altemp;
		}

		if (!existEntrega & practInTime) {

			// Create the directory to store the files

			File dirEntregaPractica = new File(entregaFinalDir +
				pract.getId() +
				File.separator +
				al1.getIdentificador() +
				"-" +
				al2.getIdentificador());

			dirEntregaPractica.mkdirs();

			// Save the files to disk

			FileWriter filew;
			File temp1;

			for (int i = 0; i < fp.length; i++) {
				try {
					temp1 = new File(entregaFinalDir +
						pract.getId() +
						File.separator +
						al1.getIdentificador() +
						"-" +
						al2.getIdentificador() +
						File.separator +
						fp[i].getNombre());

					temp1.createNewFile();
					filew = new FileWriter(temp1);

					filew.write(fp[i].getContenido());
					filew.close();
				} catch (IOException e) {
				} catch (Exception e) {
					printError("Error en Corrector de PACA: " + e.toString());
				}
			}
		}
		return badAlumno;
	}

	/**
	Imprime el mensaje de error en caso de que est� activo el modo depuraci�n.
	 */
	private void printError(String error) {
		//System.out.println(error);
		if (debugMode) {

			ACLMessage aclmen = new ACLMessage(ACLMessage.INFORM);

			aclmen.addReceiver(agente);
			aclmen.setContent(error);

			send(aclmen);
		}
	}

	
	
	
	//M�todo que rellena los tests pedidos
	private Test[] ExtraeTestsPedidos(List<AbsPredicate> lista) {
		int tamano = lista.size();
		Test[] testAux = new Test[tamano];
		Iterator<AbsPredicate> it = lista.iterator();
		for (int i = 0; i < testAux.length; i++) {
			Tests aux;
			try {
				aux = (Tests)ontologia.toObject(it.next());
				testAux[i]=  aux.getTest();
			} catch (UngroundedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (OntologyException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return testAux;
	}
	
	
	//M�todo que rellena los ficheros fuentes
	private FuentesPrograma[] ExtraeFuentesPedidos(List<AbsPredicate> lista) {
		int tamano = lista.size();
		FuentesPrograma[] fuentesAux = new FuentesPrograma[tamano];
		Iterator<AbsPredicate> it = lista.iterator();
		for (int i = 0; i < fuentesAux.length; i++) {
			FicheroFuentes aux;
			try {
				aux = (FicheroFuentes)ontologia.toObject(it.next());
				fuentesAux[i]=  aux.getFuentesPrograma();
			} catch (UngroundedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (OntologyException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return fuentesAux;
	}
	
	//Metodo que genera aleatorios entre intervalos
	private class Aleatorio extends Random {
		public int nextInt(int inferior, int superior) {
			int i;
			i=nextInt();
			i=inferior+(Math.abs(i) % (superior-inferior+1));
			return(i);
		}
	}
	
	//Metodo para registrarse en el DF
	private void RegistroDF() {
		//DFService
		DFAgentDescription dfd = new DFAgentDescription();
		AID nombreAgente = getAID();
		
		Property prop = new Property();
		prop.setName("Correciones");
		prop.setValue(numeroCorrecciones);
		
		dfd.setName(nombreAgente);
		if (debug) {
			System.out.println(nombreAgente.toString() + " quiere registrarse");
		}

		ServiceDescription sd = new ServiceDescription();
		sd.setType("Corrector");
		sd.setName("Agente-Corrector");
		sd.addProperties(prop);
		dfd.addServices(sd);
		try {
			DFService.register(this, dfd);
		} catch (FIPAException fe) {
			System.out.println("Excepcion al registrar Corrector");
			fe.printStackTrace();
		}

		//if (debug) {
			System.out.println(nombreAgente.toString() + " se ha registrado correctamente");
		//}
	}
	
	//Actualizacion de registro en el DF
	private void ActualizacionDF(){
		DFAgentDescription dfd = new DFAgentDescription();
				
		Property prop = new Property();
		prop.setName("Correciones");
		prop.setValue(numeroCorrecciones);
		
		ServiceDescription sd = new ServiceDescription();
		sd.setType("Corrector");
		sd.setName("Agente-Corrector");
		sd.addProperties(prop);
		dfd.addServices(sd);
		
		try {
			DFService.modify(this, dfd);
		} catch (FIPAException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
}












