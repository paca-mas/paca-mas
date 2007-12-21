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
import jade.core.behaviours.CyclicBehaviour;
import jade.core.behaviours.OneShotBehaviour;
import jade.domain.DFService;
import jade.domain.FIPAException;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.lang.acl.ACLMessage;
import jade.util.leap.ArrayList;
import jade.util.leap.List;

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
import java.util.Date;

import PACA.ontology.Alumno;
import PACA.ontology.Corrige;
import PACA.ontology.EntregarPractica;
import PACA.ontology.EvaluacionPractica;
import PACA.ontology.FicheroFuentes;
import PACA.ontology.FormaGrupoCon;
import PACA.ontology.Interactua;
import PACA.ontology.Practica;
import PACA.ontology.Test;
import PACA.ontology.Tests;
import PACA.ontology.pacaOntology;
import PACA.ontology.Fichero.FuentesPrograma;
import java.util.Hashtable;

/**
Este agente se encarga de realizar la correcci�n de las pr�cticas y tambi�n deposita las entregas que los 
alumnos realizan.
 */
public class Corrector extends Agent {

	// Modo de ejecución en pruebas
	private boolean ejecucionEnPruebas = true;
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
			System.out.println(ire1.toString());
			System.out.println("==========================================");
			System.out.println(resp1.toString());

			System.out.println("CORRECTOR COMPORTAMIENTOOOOOOOO PRACTICASSSSSSSSS");
			//			 --> PracticasDisponibles <--------------------------------------------------
			Practica[] lpractn = PracticasDisponibles();

			// Creamos el listado de practicas de forma abstracta
			AbsAggregate absPracticas = new AbsAggregate(BasicOntology.SET);

			for (int i = 0; i < lpractn.length; i++) {
				//Creamos un objecto abstracto por cada practica y la a�adimos al aggregate
				System.out.println(lpractn[i].getId());
				AbsConcept elem;
				try {
					elem = (AbsConcept) ontologia.fromObject(lpractn[i]);
					absPracticas.add(elem);
				} catch (OntologyException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

			System.out.println("CREAMOS EL PREDICADO ABSTRACTO EQUALS");

			//Creamos el predicado abstracto EQUALS
			AbsPredicate equalPred = new AbsPredicate(SLVocabulary.EQUALS);
			equalPred.set(SLVocabulary.EQUALS_LEFT, ire1);
			equalPred.set(SLVocabulary.EQUALS_RIGHT, absPracticas);

			//Mandamos el predicado al interfaz
			try {
				getContentManager().fillContent(resp1, equalPred);
				System.out.println("Rellenamos el resp1");
			} catch (CodecException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (OntologyException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			System.out.println(resp1.toString());
			System.out.println("FINNNN CORRECTOR COMPORTAMIENTOOOOOOOO PRACTICASSSSSSSSS");
			send(resp1);
		}
	}

	//Comportamiento que manda los tests al agente Interfaz
	public class TestsCorrecBehaviour extends OneShotBehaviour {

		private ACLMessage mens1;
		private AbsPredicate pred1;
		private AbsIRE ire1;

		public TestsCorrecBehaviour(Agent _a, ACLMessage msg1, AbsPredicate pred, AbsIRE ire) {
			super(_a);
			this.mens1 = msg1;
			this.pred1 = pred;
			this.ire1 = ire;
		}

		public void action() {

			System.out.println("COMPORTAMIENTO CORRECTOR PARA TESTSSSSSSSSSSSSSSSSS");
			//Obtenemos el predicado TESTS
			Tests tes;
			try {
				tes = (Tests) ontologia.toObject(pred1);

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
			System.out.println("FIN COMPORTAMIENTO CORRECTOR PARA TESTSSSSSSSSSSSSSSSSS");
		}
	}

	//Comportamiento que manda los ficheros a corregir
	public class FicherosCorrBehaviour extends OneShotBehaviour {

		private ACLMessage mens1;
		private AbsPredicate pred1;
		private AbsPredicate pred2;
		private AbsIRE ire1;

		public FicherosCorrBehaviour(Agent _a, ACLMessage msg1, AbsPredicate predIzd, AbsPredicate predDcha, AbsIRE ire) {
			super(_a);
			this.mens1 = msg1;
			this.pred1 = predIzd;
			this.pred2 = predDcha;
			this.ire1 = ire;
		}

		public void action() {
			System.out.println("COMPORTAMIENTO CORRECTOR FICHEROSSSSSSSSSSSSSSSSSS");

			Corrige corr;
			try {
				corr = (Corrige) ontologia.toObject(pred1);
				Practica pract = corr.getPractica();

				//Guardamos todos los Test que nos han pedido
				Test[] testAux3 = ExtraeTestsPedidos(pred2);

				//Obtenemos los ficheros fuentes necesarios para cada Test
				FuentesPrograma[] fp = FicheroParaPractica(pract.getId(), testAux3);

				// Creamos el listado de ficheros de forma abstracta
				AbsAggregate absFicheros = new AbsAggregate(BasicOntology.SET);

				for (int i = 0; i < fp.length; i++) {
					//Creamos un objecto abstracto por cada fichero y la a�adimos al aggregate
					AbsConcept elem;
					try {
						elem = (AbsConcept) ontologia.fromObject(fp[i]);
						absFicheros.add(elem);
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

			System.out.println("FIN COMPORTAMIENTO CORRECTOR FICHEROSSSSSSSSSSSSSSSSSS");

		}
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
			//ACLMessage msg = myAgent.blockingReceive();
			if (debug) {
				System.out.println("Corrector.java: Esperamos a que llegue un mensaje");
			}

			//ACLMessage msg = blockingReceive();
			ACLMessage msg = receive();

			if (msg != null) {

				if (debug) {
					System.out.println("----------");
					System.out.println(msg.toString());
					System.out.println("----------");
				}

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
							if (debug) {
								System.out.println("ALL?: " + SL2Vocabulary.ALL);
								System.out.println("requestedInfoName: " + requestedInfoName);
							}

							// If the receive message isn't a QUERY-REF (all or
							// QUERY-REF (iota, reply a NOT_UNDERTOOD message

							if ((!requestedInfoName.equals(SL2Vocabulary.ALL)) & (!requestedInfoName.equals(SL2Vocabulary.IOTA))) {
								reply.setPerformative(ACLMessage.NOT_UNDERSTOOD);
								String content = "(" + msg.toString() + ")";
								reply.setContent(content);
							} else {

								if (requestedInfoName.equals(SL2Vocabulary.IOTA)) {
									// If QUERY-REF (iota  
									// --> EnvioCorrecionAlumno <--------------------------------------------------
									printError("Dentro de EnvioCorrecionAlumno");


									//query_ref_IOTA qiota = new query_ref_IOTA();
									//A�adido Carlos
									AbsPredicate qiota = new AbsPredicate(SL2Vocabulary.IOTA);
									//qiota = (query_ref_IOTA) requestedInfo;
									//Modificacion Carlos
									qiota.set(SL2Vocabulary.IOTA, requestedInfoName);

									// Extract the practica from the message
									//Modificacion Carlos (Funcionara???)
									//get1=RIGHT , get0=LEFT
									//And and1 = (And) qiota.get_1();
									//Corrige corr = (Corrige) and1.get_0();
									AbsObject and1 = qiota.getAbsObject(SL2Vocabulary.IOTA);
									Corrige corr = (Corrige) and1.getAbsObject(SL1Vocabulary.AND_LEFT);


									Practica pract = corr.getPractica();


									//And and2 = (And) and1.get_1();
									//A�adido Carlos
									//get1=RIGHT , get0=LEFT
									AbsObject and2 = and1.getAbsObject(SL2Vocabulary.AND_RIGHT);


									//And and3 = (And) and2.get_1();
									//A�adido Carlos
									//get1=RIGHT , get0=LEFT
									AbsObject and3 = and2.getAbsObject(SL2Vocabulary.AND_RIGHT);


									//And and4 = (And) and3.get_1();
									//A�adido Carlos
									//get1=RIGHT , get0=LEFT
									AbsObject and4 = and3.getAbsObject(SL2Vocabulary.AND_RIGHT);


									//Lista secTests = (Lista) and2.get_0();
									ArrayList secTests = new ArrayList();
									secTests = (ArrayList) and2.getAbsObject(SL2Vocabulary.AND_LEFT);

									//Lista secFuentes = (Lista) and3.get_0();
									ArrayList secFuentes = new ArrayList();
									secFuentes = (ArrayList) and3.getAbsObject(SL2Vocabulary.AND_LEFT);

									//List listFF = secFuentes.getAll_0_List();
									List listFF = (List) secFuentes.iterator();
									//List listTe = secTests.getAll_0_List();
									List listTe = (List) secTests.iterator();

									Test[] Te = new Test[listTe.toArray().length];
									Tests teTemp = new Tests();

									for (int i = 0; i < listTe.toArray().length; i++) {
										teTemp = (Tests) listTe.toArray()[i];
										Te[i] = teTemp.getTest();
									}

									FuentesPrograma[] FP =
										new FuentesPrograma[listFF.toArray().length];
									FicheroFuentes ff = new FicheroFuentes();

									for (int i = 0; i < listFF.toArray().length; i++) {
										ff = (FicheroFuentes) listFF.toArray()[i];
										FP[i] = ff.getFuentesPrograma();
									}

									//Alumno al = (Alumno) and4.get_1();
									//Modificacion Carlos
									Alumno al = (Alumno) and4.getAbsObject(SL2Vocabulary.AND_RIGHT);


									EvaluacionPractica EvaP =
										EnvioCorreccionAlumno(pract, FP, Te, al,
										msg.getSender().getLocalName());

									//query_ref_response qrr = new query_ref_response();

									//qrr.set_0(qiota);
									//qrr.add_1(EvaP);

									//Modifificacion Carlos
									AbsPredicate qrr = new AbsPredicate(SL1Vocabulary.EQUALS);
									qrr.set(SL1Vocabulary.EQUALS_LEFT, qiota);
									qrr.set(SL1Vocabulary.EQUALS_RIGHT, (AbsObject) EvaP);




									// Create a List for add qrr object and
									// fill ontology

									List lqrr = new ArrayList();
									lqrr.add(qrr);

									//myAgent.fillMsgContent(reply, lqrr);
									//getContentManager().fillContent(reply, (AbsContentElement) lqrr);
									reply.setContentObject((Serializable) lqrr);
								} else {
									// Here, is QUERY-REF (all

									AbsIRE allPred = (AbsIRE) l_in;

									//Obtenemos la proposicion del predicado
									AbsPredicate qall = (AbsPredicate) allPred.getProposition();

									//Obtenemos el predicado original (CORRIGE, PRACTICAS, TESTS... )
									String requestedInfo2Name = qall.getTypeName();

									if (requestedInfo2Name.equals(pacaOntology.CORRIGE)) {
										// --> PracticasDisponibles <--------------------------------------------------
									/*
										Practica[] lpractn = PracticasDisponibles();
										// Creamos el listado de practicas de forma abstracta
										AbsAggregate absPracticas = new AbsAggregate (BasicOntology.SET);
										for (int i=0; i < lpractn.length; i++){
										//Creamos un objecto abstracto por cada practica y la a�adimos al aggregate
										AbsConcept elem = (AbsConcept)ontologia.fromObject(lpractn[i]);
										absPracticas.add(elem);
										}
										//Creamos el predicado abstracto EQUALS
										AbsPredicate equalPred = new AbsPredicate(SLVocabulary.EQUALS);
										equalPred.set(SLVocabulary.EQUALS_LEFT,allPred);
										equalPred.set(SLVocabulary.EQUALS_RIGHT,absPracticas);
										//Mandamos el predicado al interfaz
										getContentManager().fillContent(reply,equalPred);
										myAgent.send(reply);*/


										addBehaviour(new PracCorrecBehaviour(this.myAgent, reply, allPred));

									} else {
										//Sacamos las partes izquierda y derecha del predicado AND
										AbsPredicate AbsLEFT1 = (AbsPredicate) qall.getAbsObject(SL1Vocabulary.AND_LEFT);

										//Obtenemos el predicado con el que queremos trabajar AND, TESTS...
										AbsPredicate AbsRIGHT1 = (AbsPredicate) qall.getAbsObject(SL1Vocabulary.AND_RIGHT);

										String requestedInfo3Name = AbsRIGHT1.getTypeName();

										//Modificacion Carlos
										if (requestedInfo3Name.equals(SL1Vocabulary.AND)) {
											// --> FicheroParaPractica <---------------------------------------------------

											/*Corrige corr = (Corrige) ontologia.toObject(AbsLEFT1);
											Practica pract = corr.getPractica();
											//Guardamos todos los Test que nos han pedido
											Test [] testAux3 = ExtraeTestsPedidos(AbsRIGHT1);
											//Obtenemos los ficheros fuentes necesarios para cada Test
											FuentesPrograma[] fp = FicheroParaPractica(pract.getId(), testAux3);
											// Creamos el listado de ficheros de forma abstracta
											AbsAggregate absFicheros = new AbsAggregate (BasicOntology.SET);
											for (int i=0; i < fp.length; i++){
											//Creamos un objecto abstracto por cada fichero y la a�adimos al aggregate
											AbsConcept elem = (AbsConcept)ontologia.fromObject(fp[i]);
											absFicheros.add(elem);
											}
											//Creamos el predicado abstracto EQUALS
											AbsPredicate equalPred = new AbsPredicate(SLVocabulary.EQUALS);
											equalPred.set(SLVocabulary.EQUALS_LEFT,allPred);
											equalPred.set(SLVocabulary.EQUALS_RIGHT,absFicheros);
											getContentManager().fillContent(reply, equalPred);
											myAgent.send(reply);*/

											addBehaviour(new FicherosCorrBehaviour(this.myAgent, reply, AbsLEFT1, AbsRIGHT1, allPred));

										} else {
											// --> TestPorPractica <-------------------------------------------------------
											if (debug) {
												System.out.println("Dentro de TestPorPractica");
											}
											/*
											//Obtenemos el predicado TESTS
											Tests tes = (Tests) ontologia.toObject(AbsRIGHT1);
											Test[] te;
											//Obtenemos los tests disponibles para la practica seleccionada
											te = TestParaPractica(tes.getPractica().getId());
											AbsAggregate absTests = new AbsAggregate (BasicOntology.SET);
											// Adds the Test
											//Pasamos los tests a objectos abstractos
											for (int i=0; i < te.length; i++) {
											AbsConcept elem = (AbsConcept) ontologia.fromObject(te[i]);
											absTests.add(elem);
											}
											//Modifificacion Carlos
											AbsPredicate qrr = new AbsPredicate (SL1Vocabulary.EQUALS);
											qrr.set(SL1Vocabulary.EQUALS_LEFT, allPred);
											qrr.set(SL1Vocabulary.EQUALS_RIGHT,absTests);
											getContentManager().fillContent(reply, qrr);
											myAgent.send(reply);*/
											addBehaviour(new TestsCorrecBehaviour(this.myAgent, reply, AbsRIGHT1, allPred));
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
							act = (Action) l_in.get(0);

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
					} catch (java.lang.NullPointerException e) {
						printError("Empty message");
						e.printStackTrace();
					} catch (Exception e) {
						printError("Error en Corrector de PACA: " + e.toString());
						e.printStackTrace();
					}
				}
			//myAgent.send(reply);
			} //============================== cambios para el receive no bloqueante ==========
			else {
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

		// Register the codec for the SL0 language
		getContentManager().registerLanguage(codec);

		// Register the ontology used by this application
		getContentManager().registerOntology(ontologia);

		//DFService
		DFAgentDescription dfd = new DFAgentDescription();
		AID nombreAgente = getAID();
		dfd.setName(nombreAgente);
		if (debug) {
			System.out.println(nombreAgente.toString() + " quiere registrarse");
		}

		ServiceDescription sd = new ServiceDescription();
		sd.setType("Corrector");
		sd.setName("Agente-Corrector");
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


		// Create the FSMEBehaviour
		CorrectorBehaviour Principal = new CorrectorBehaviour(this);

		// Add the behaviour to the agent
		addBehaviour(Principal);
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
				for (FuentesPrograma fp : this.FuentesProgramaPrueba.get(test.getId())) {
					lista.add(fp);
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

		if (this.ejecucionEnPruebas) {

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
						"</Descripcion></EvaluacionCaso></Caso>";
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
		System.out.println(error);
		if (debugMode) {

			ACLMessage aclmen = new ACLMessage(ACLMessage.INFORM);

			aclmen.addReceiver(agente);
			aclmen.setContent(error);

			send(aclmen);
		}
	}

	//Metodo que extrae los Test pedidos
	private Test[] ExtraeTestsPedidos(AbsPredicate predicado) {
		int con = 0;
		Test[] testAux = new Test[10];
		AbsPredicate predicado_izda = (AbsPredicate) predicado.getAbsObject(SL1Vocabulary.AND_LEFT);

		AbsPredicate predicado_derecha = (AbsPredicate) predicado.getAbsObject(SL1Vocabulary.AND_RIGHT);

		if (predicado_derecha.getTypeName().equals(pacaOntology.FICHEROFUENTES)) {
			if (predicado_izda.getTypeName().equals(pacaOntology.TESTS)) {
				Tests test1;
				try {
					test1 = (Tests) ontologia.toObject(predicado_izda);
					testAux[con] = test1.getTest();
					con++;
				} catch (UngroundedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (OntologyException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}
		} else {
			while (!predicado_derecha.getTypeName().equals(pacaOntology.FICHEROFUENTES)) {
				if (predicado_izda.getTypeName().equals(pacaOntology.TESTS)) {
					Tests test1;
					try {
						test1 = (Tests) ontologia.toObject(predicado_izda);
						testAux[con] = test1.getTest();
					} catch (UngroundedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (OntologyException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

				}

				predicado_izda = (AbsPredicate) predicado_derecha.getAbsObject(SL1Vocabulary.AND_LEFT);

				predicado_derecha = (AbsPredicate) predicado_derecha.getAbsObject(SL1Vocabulary.AND_RIGHT);

				if (predicado_derecha.getTypeName().equals(pacaOntology.FICHEROFUENTES)) {
					if (predicado_izda.getTypeName().equals(pacaOntology.TESTS)) {
						Tests test1;
						try {
							test1 = (Tests) ontologia.toObject(predicado_izda);
							con++;
							testAux[con] = test1.getTest();

						} catch (UngroundedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						} catch (OntologyException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}

					}
				}

				con++;
			}
		}

		Test[] testAux2 = new Test[con];

		for (int x = 0; x < con; x++) {
			testAux2[x] = testAux[x];
		}

		return testAux2;
	}
}












