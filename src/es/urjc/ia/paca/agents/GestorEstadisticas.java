package es.urjc.ia.paca.agents;

import jade.content.ContentManager;
import jade.content.abs.AbsAgentAction;
import jade.content.abs.AbsAggregate;
import jade.content.abs.AbsConcept;
import jade.content.abs.AbsContentElement;
import jade.content.abs.AbsIRE;
import jade.content.abs.AbsPredicate;
import jade.content.abs.AbsVariable;
import jade.content.lang.Codec;
import jade.content.lang.Codec.CodecException;
import jade.content.lang.sl.SL2Vocabulary;
import jade.content.lang.sl.SLCodec;
import jade.content.lang.sl.SLVocabulary;
import jade.content.onto.Ontology;
import jade.content.onto.OntologyException;
import jade.content.onto.UngroundedException;
import jade.content.onto.basic.Done;
import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.Behaviour;
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;
import jade.util.leap.List;

import java.util.ArrayList;
import java.util.Date;

import es.urjc.ia.paca.auth.ontology.AuthOntology;
import es.urjc.ia.paca.ontology.Alumno;
import es.urjc.ia.paca.ontology.Caso;
import es.urjc.ia.paca.ontology.Corrector;
import es.urjc.ia.paca.ontology.Corrige;
import es.urjc.ia.paca.ontology.EstadisticaEvaluacionCaso;
import es.urjc.ia.paca.ontology.ModificarDatosBBDD;
import es.urjc.ia.paca.ontology.Practica;
import es.urjc.ia.paca.ontology.RegistrarEstadisticaEntrega;
import es.urjc.ia.paca.ontology.RegistrarEstadisticaEvaluacion;
import es.urjc.ia.paca.ontology.Test;
import es.urjc.ia.paca.ontology.pacaOntology;
import es.urjc.ia.paca.parser.ParsearAlumnos;
import es.urjc.ia.paca.util.AndBuilder;
import es.urjc.ia.paca.util.Resultado;
import es.urjc.ia.web.Configuracion;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class GestorEstadisticas extends Agent {
	
	private boolean debug = false;
	private Codec codec = new SLCodec();
	private ContentManager manager  = (ContentManager)getContentManager();
	private Ontology ontologia = AuthOntology.getInstance();
	private Ontology PACAOntology = pacaOntology.getInstance();

   /**Identifica al agente gestor de practicas**/
    public String gestorPracticas = "gestorDePracticas";
    
    //-------------- INICIO PROCEDURES PARA BORRAR ----------------------------
    private void EliminarPractica() {
    	Connection conexion = null;
   		try {
   			Class.forName("com.mysql.jdbc.Driver");
    	} catch (ClassNotFoundException e) {
    			System.out.println("MySQL JDBC Driver not found. ");
    		    System.exit(1);
    	}
    		
    	// Se conecta con la Base de Datos
    	try {
    		conexion = DriverManager.getConnection("jdbc:mysql://localhost/" + Configuracion.bbdd, Configuracion.usuario, Configuracion.pass);
    	} catch (SQLException e) {
    		System.out.println("Error de conexión: " + e.getMessage());
    		System.exit(4);			
   		}
 
    	try {
				Statement st = conexion.createStatement();
				String frase1 = "delete from `x_practica`;";
				st.execute(frase1);
				String frase2 = "delete from `x_test`;";
				st.execute(frase2);
				String frase3 = "delete from `x_caso`;";
				st.execute(frase3);
				conexion.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
    }

	private void EliminarAlumno() {
    	Connection conexion = null;
   		try {
   			Class.forName("com.mysql.jdbc.Driver");
    	} catch (ClassNotFoundException e) {
    			System.out.println("MySQL JDBC Driver not found. ");
    		    System.exit(1);
    	}
    		
    	// Se conecta con la Base de Datos
    	try {
    		conexion = DriverManager.getConnection("jdbc:mysql://localhost/" + Configuracion.bbdd,Configuracion.usuario, Configuracion.pass);
    	} catch (SQLException e) {
    		System.out.println("Error de conexión: " + e.getMessage());
    		System.exit(4);			
   		}
 
    	try {
				Statement st = conexion.createStatement();
				String frase = "delete from `usuarios` where tipo = 'A';";
				st.execute(frase);
				conexion.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	}

	private void EliminarDatos() {
    	Connection conexion = null;
   		try {
   			Class.forName("com.mysql.jdbc.Driver");
    	} catch (ClassNotFoundException e) {
    			System.out.println("MySQL JDBC Driver not found. ");
    		    System.exit(1);
    	}
    		
    	// Se conecta con la Base de Datos
    	try {
    		conexion = DriverManager.getConnection("jdbc:mysql://localhost/" + Configuracion.bbdd, Configuracion.usuario, Configuracion.pass);
    	} catch (SQLException e) {
    		System.out.println("Error de conexión: " + e.getMessage());
    		System.exit(4);			
   		}
 
    	try {
				Statement st = conexion.createStatement();
				String frase1 = "delete from `x_evaluacion_caso`;";
				String frase2 = "delete from `x_entrega_practica`;";
				st.execute(frase1);
				st.execute(frase2);
				conexion.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	}
    //-------------- FIN PROCEDURES PARA BORRAR ----------------------------
	
    //-------------- INICIO CARGA DATOS ALUMNO ----------------------------
	private void CargarDatosAlumnos (){
		java.util.List<Alumno> lista = new ArrayList<Alumno>();
		lista = ParsearAlumnos.parsearArchivoXml("C:/Users/sandrita/workspace_PFC/gestor-estadisticas/src/es/urjc/ia/paca/parser/correccion.xml");
		
		Connection conexion = null;
   		try {
   			Class.forName("com.mysql.jdbc.Driver");
    	} catch (ClassNotFoundException e) {
    			System.out.println("MySQL JDBC Driver not found. ");
    		    System.exit(1);
    	}
    		
    	// Se conecta con la Base de Datos
    	try {
    		conexion = DriverManager.getConnection("jdbc:mysql://localhost/" + Configuracion.bbdd, Configuracion.usuario, Configuracion.pass);
    	} catch (SQLException e) {
    		System.out.println("Error de conexión: " + e.getMessage());
    		System.exit(4);			
   		}
 
    	try {
    		for (int i = 0; i < lista.size(); i ++){
    			Alumno a = lista.get(i);
    			String alumno = a.getIdentificador();
    			String pass = a.getPassword();
				Statement st = conexion.createStatement();
				String frase = "INSERT INTO `usuarios`(`Id_usuario`,`id_grupo`,`tipo`,`pass`) "; 
				frase = frase + "VALUE (" + alumno + ", 1 , A ," + pass + ")";
				st.execute(frase);
				conexion.close();
    		}
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	}
	
	private void InsertarBBDD (Practica p, Test t, Caso c){
		System.out.println("Insertado");
		p.getId();
		p.getDescripcion();
		p.getFechaEntrega();
		t.getId();
		t.getDescripcion();
		t.getEjecutable();
		c.getId();
	}

    //-------------- FIN CARGA DATOS ALUMNOS ----------------------------

    //-------------------------- INICIO COMPORTAMIENTOS PARA CASOS ---------------------------
	public void PideCasos (Test test, Practica practica) {
		try{
		//CREAMOS EL MENSAJE
		ACLMessage msg = new ACLMessage(ACLMessage.QUERY_REF);
		AID receiver = new AID(gestorPracticas, AID.ISLOCALNAME);
		msg.setSender(getAID());
		msg.addReceiver(receiver);
		msg.setLanguage(codec.getName());
		msg.setOntology(pacaOntology.NAME);

		//CREAMOS EL PREDICADO TESTS PARA SABER CUAL ES LA PRACTICA
		//Y EL TEST DE LOS CASOS QUE VAMOS A PEDIR
		AbsConcept abspract;
			abspract = (AbsConcept) PACAOntology.fromObject(practica);
			AbsConcept absts = (AbsConcept) PACAOntology.fromObject(test);
		AbsPredicate abstss = new AbsPredicate(pacaOntology.TESTS);
		abstss.set(pacaOntology.TEST, absts);
		abstss.set(pacaOntology.PRACTICA, abspract);


		//CREAMOS EL PREDICADO PARA PEDIR LOS CASOS
		Caso ca = new Caso("?Caso");
		AbsConcept absca;
			absca = (AbsConcept) PACAOntology.fromObject(ca);
		AbsPredicate abscas = new AbsPredicate(pacaOntology.CASOS);
		abscas.set(pacaOntology.TEST, absts);
		abscas.set(pacaOntology.CASO, absca);

		//CREAMOS UN ANDBUILDER PARA UNIR LOS DOS PREDICADOS ANTERIORES
		AndBuilder constructor = new AndBuilder();
		constructor.addPredicate(abstss);
		constructor.addPredicate(abscas);


		//CREAMOS EL IRE CON LA VARIABLE X Y ENVIAMOS TODO
		AbsVariable x =
			new AbsVariable("caso", pacaOntology.CASO);
		AbsIRE qrall = new AbsIRE(SL2Vocabulary.ALL);
		qrall.setVariable(x);
		qrall.setProposition(constructor.getAnd());
		getContentManager().fillContent(msg, qrall);
		send(msg);
		} catch (OntologyException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (CodecException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public Caso[] RecibeCasos (){
		Caso[] ListaCaso = null;
		ACLMessage respuesta = blockingReceive();
		if (respuesta != null) {
			try {
				AbsContentElement listaAbs = null;
				listaAbs = getContentManager().extractAbsContent(respuesta);
				String tipoMensaje = listaAbs.getTypeName();
				if (tipoMensaje.equals("=")) {
					AbsAggregate listaElementos = (AbsAggregate) listaAbs.getAbsObject(SLVocabulary.EQUALS_RIGHT);
					//Cogemos el primer elemento de la lista
					AbsConcept primerElem = (AbsConcept) listaElementos.get(0);
					//Miramos el tipo del primer elemento
					String tipo = primerElem.getTypeName();
					if (tipo.equals(pacaOntology.CASO)) {
						Caso[] retornable = new Caso[0];
						Caso ca;
						try {
							retornable = new Caso[listaElementos.size()];
							for (int i = 0; i < listaElementos.size(); i++) {
								ca = (Caso) PACAOntology.toObject(listaElementos.get(i));
								if (!(ca.getId().equalsIgnoreCase("No hay casos"))) {
									retornable[i] = ca;
								} else {
									retornable = new Caso[0];
								}		
							}
							System.out.println("Numero casos " + retornable.length);
						ListaCaso = retornable;
						}catch (Exception e) {
							//ficherosUltimaPractica = retornable;
							//tes2.setResultado(retornable);
						}
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
		return ListaCaso;
	}
    //-------------------------- FIN COMPORTAMIENTOS PARA CASOS ---------------------------
	
    //-------------------------- COMPORTAMIENTOS PARA TESTS -------------------------------
	public void PideTest (Practica practica) {

		//AID agentCorrec = getAgenteCorrector();
		AID receiver = new AID(gestorPracticas, AID.ISLOCALNAME);
		ACLMessage solicitud = new ACLMessage(ACLMessage.QUERY_REF);
		solicitud.addReceiver(receiver);
		solicitud.setLanguage(codec.getName());
		solicitud.setOntology(pacaOntology.NAME);

		// Generamos el mensaje para enviar
		Corrige cor = new Corrige();
		Corrector correc = new Corrector();
		correc.setId(gestorPracticas);
		cor.setPractica(practica);
		cor.setCorrector(correc);

		Test te = new Test();
		te.setId("?test");
		te.setDescripcion("");
		te.setEjecutable("");


		try {
			//Convertimos la practica a un concepto abstracto
			AbsConcept absPract = (AbsConcept) PACAOntology.fromObject(practica);

			//Convertimo el corrector a un concepto abstracto
			AbsConcept absCorrec = (AbsConcept) PACAOntology.fromObject(correc);

			//Convertimos el test a un concepto abstracto
			AbsConcept absTest = (AbsConcept) PACAOntology.fromObject(te);

			//Creamos el predicado CORRIGE de forma abstracta utilizando los conceptos abstractos creados anteriormente
			AbsPredicate absCorrige = new AbsPredicate(pacaOntology.CORRIGE);
			absCorrige.set(pacaOntology.PRACTICA, absPract);
			absCorrige.set(pacaOntology.CORRECTOR, absCorrec);

			//Creamos el predicado TESTS de forma abstracta utilizando los conceptos abstractos creados anteriormente
			AbsPredicate absTests = new AbsPredicate(pacaOntology.TESTS);
			absTests.set(pacaOntology.PRACTICA, absPract);
			absTests.set(pacaOntology.TEST, absTest);

			AbsVariable x = new AbsVariable("test", pacaOntology.TEST);

			AndBuilder predicado = new AndBuilder();
			predicado.addPredicate(absCorrige);
			predicado.addPredicate(absTests);

			AbsIRE qrall = new AbsIRE(SL2Vocabulary.ALL);
			qrall.setVariable(x);
			qrall.setProposition(predicado.getAnd());

			getContentManager().fillContent(solicitud, qrall);
			send(solicitud);

		} catch (NullPointerException ex) {
			ex.printStackTrace();
		} catch (OntologyException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (CodecException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public Test[] RecibeTest () {
		Test[] ListaTest = null; 
		// Recibir Mensaje Test
		ACLMessage respuesta = blockingReceive();
		if (respuesta != null) {
			try {
				AbsContentElement listaAbs = null;
				listaAbs = getContentManager().extractAbsContent(respuesta);
				String tipoMensaje = listaAbs.getTypeName();
				if (tipoMensaje.equals("=")) {
					AbsAggregate listaElementos = (AbsAggregate) listaAbs.getAbsObject(SLVocabulary.EQUALS_RIGHT);
					//Cogemos el primer elemento de la lista
					AbsConcept primerElem = (AbsConcept) listaElementos.get(0);
					//Miramos el tipo del primer elemento
					String tipo = primerElem.getTypeName();
					if (tipo.equals(pacaOntology.TEST)) {
						Test t = new Test();
						Test[] aux = new Test[listaElementos.size()];
						try {
							int j = 0;
							for (int i = 0; i < listaElementos.size(); i++) {
								//Pasamos de concepto abstracto a objeto "real", en este caso son tests
								t = (Test) PACAOntology.toObject(listaElementos.get(i));
								if (!(t.getId().equalsIgnoreCase("No hay Tests"))) {
									aux[i] = t;
								} else {
									aux = new Test[0];
								}
							}
						} catch (Exception e) {
							e.printStackTrace();
						}
						ListaTest = aux;
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
		return ListaTest; 
	}
    //-------------------------- FIN COMPORTAMIENTOS PARA TESTS ---------------------------
	public void RegistrarEvaluaciones(RegistrarEstadisticaEvaluacion evaluacion){
    	Connection conexion = null;
   		try {
   			Class.forName("com.mysql.jdbc.Driver");
    	} catch (ClassNotFoundException e) {
    			System.out.println("MySQL JDBC Driver not found. ");
    		    System.exit(1);
    	}
    		
    	// Se conecta con la Base de Datos
    	try {
    		conexion = DriverManager.getConnection("jdbc:mysql://localhost/" + Configuracion.bbdd, Configuracion.usuario, Configuracion.pass);
    	} catch (SQLException e) {
    		System.out.println("Error de conexión: " + e.getMessage());
    		System.exit(4);			
   		}
 
    	try {
    		List lista = evaluacion.getEvaluacionesCasos();
    		String idalumno;
    		String idpractica;
    		String idtest;
    		String idcaso;
    		String eva_caso;
    		String frase;
			java.util.Date fecha = new Date();
    		int f;
    		Statement st = null;
			for (int i = 0; i < lista.size(); i++){
    			EstadisticaEvaluacionCaso eval = (EstadisticaEvaluacionCaso) lista.get(i);
    			idalumno = eval.getAlumno().getIdentificador();
    			idpractica = eval.getPractica().getId();
    			idtest = eval.getTest().getId();
    			idcaso = eval.getCaso().getId();
    			eva_caso = eval.getEvaluacion();
    			f = fecha.getDate();
    			frase = "";
    			frase = "INSERT INTO `x_evaluacion_caso`(`fecha`,`id_practica`,`id_grupo`,`id_caso`,`id_test`,`evaluacion`,`id_alumno`) ";
    			frase = frase + " VALUE (" + f + "," + idpractica + ",1," + idcaso + "," + idtest + "," + eva_caso + "," + idalumno +" )";
    			st = conexion.createStatement();
    			st.execute(frase);
			}
			st.execute("CALL `Carga_Evaluacion_Alumno_Test`()");
			st.execute("CALL `Carga_Evaluacion_Alumno_Practica`()");
			st.execute("CALL `Carga_Evaluacion_Alumno_Test_Max`()");
			st.execute("CALL `Carga_Evaluacion_Alumno_Practica_Max`()");
			st.execute("CALL `Carga_Evaluacion_Grupo_Caso`()");
			st.execute("CALL `Carga_Evaluacion_Grupo_Test`()");
			st.execute("CALL `Carga_Evaluacion_Grupo_Practica`()");
			conexion.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	}
	
	public void RegistrarEntrega(RegistrarEstadisticaEntrega entrega){
	   	Connection conexion = null;
   		try {
   			Class.forName("com.mysql.jdbc.Driver");
    	} catch (ClassNotFoundException e) {
    			System.out.println("MySQL JDBC Driver not found. ");
    		    System.exit(1);
    	}
    		
    	// Se conecta con la Base de Datos
    	try {
    		conexion = DriverManager.getConnection("jdbc:mysql://localhost/" + Configuracion.bbdd,Configuracion.usuario, Configuracion.pass);
    	} catch (SQLException e) {
    		System.out.println("Error de conexión: " + e.getMessage());
    		System.exit(4);			
   		}
 
    	try {
				Statement st = conexion.createStatement();
				Alumno a = entrega.getAlumno1();
				Alumno b = entrega.getAlumno2();
				Practica p = entrega.getPractica();
				java.util.Date fecha = new Date();
				int f = fecha.getDate();
				String frase_1 = "INSERT INTO `x_entrega_practica` (`fecha`, `id_grupo`,`id_alumno`, `id_practica`)"; 
				frase_1 = frase_1 + "VALUE (" + f + ", 1 ," + a.getIdentificador() + ","+ p.getId() + ")";
				String frase_2 = "INSERT INTO `x_entrega_practica` (`fecha`, `id_grupo`,`id_alumno`, `id_practica`)"; 
				frase_1 = frase_2 + "VALUE (" + f + ", 1 ," + b.getIdentificador() + ","+ p.getId() + ")";
				st.execute(frase_1);
				st.execute(frase_2);
				conexion.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	}
	
    //-------------------------- INICIO COMPORTAMIENTOS PARA PRACTICAS -----------------------	
	private void PidePracticas() {
		try{
		AID receiver = new AID(gestorPracticas, AID.ISLOCALNAME);
		ACLMessage solicitud = new ACLMessage(ACLMessage.QUERY_REF);
		solicitud.addReceiver(receiver);
		solicitud.setLanguage(codec.getName());
		solicitud.setOntology(pacaOntology.NAME);

		Practica pract = new Practica();
		pract.setId("?practica");
		pract.setDescripcion("");

		Corrector correc = new Corrector();
		correc.setId(gestorPracticas);

		//Convertimos la practica a un objecto abstracto
		AbsConcept absPract;
		absPract = (AbsConcept) PACAOntology.fromObject(pract);

		//Convertimos el correcto a un objecto abstracto
		AbsConcept absCorrec = (AbsConcept) PACAOntology.fromObject(correc);

		//Creamos el predicado abstracto "CORRIGE"
		AbsPredicate absPredCorr = new AbsPredicate(pacaOntology.CORRIGE);
		absPredCorr.set(pacaOntology.PRACTICA, absPract);
		absPredCorr.set(pacaOntology.CORRECTOR, absCorrec);

		//Creamos la variable que queremos pedir, en este caso "?practica"
		AbsVariable x = new AbsVariable("practica", pacaOntology.PRACTICA);

		//Creamos el IRE con la variable "x" y el predicado "CORRIGE"
		AbsIRE qrall = new AbsIRE(SL2Vocabulary.ALL);
		qrall.setVariable(x);
		qrall.setProposition(absPredCorr);

		//Mandamos el mensaje
		getContentManager().fillContent(solicitud, qrall);              
		send(solicitud);
		RecibePracticas();
		} catch (OntologyException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (CodecException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void RecibePracticas () {
		ACLMessage respuesta = blockingReceive();
		if (respuesta != null) {
			AbsContentElement listaAbs = null;
			try {
				listaAbs = getContentManager().extractAbsContent(respuesta);
			} catch (CodecException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (OntologyException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			String tipoMensaje = listaAbs.getTypeName();
			if (tipoMensaje.equals("=")) {
				AbsAggregate listaElementos = (AbsAggregate) listaAbs.getAbsObject(SLVocabulary.EQUALS_RIGHT);
				//Cogemos el primer elemento de la lista
				AbsConcept primerElem = (AbsConcept) listaElementos.get(0);
				//Miramos el tipo del primer elemento
				String tipo = primerElem.getTypeName();
				if (tipo.equals(pacaOntology.PRACTICA)) {
System.out.println("es una practica, sacamos las practicas");
					//Sacamos las practicas que vienen en el mensaje como conceptos abstractos
					Practica p = new Practica();
					Practica[] ListaPractica = new Practica[listaElementos.size()];

					for (int i = 0; i < listaElementos.size(); i++) {
						//Pasamos de concepto abstracto a objeto "real", en este caso son practicas
						try {
							p = (Practica) PACAOntology.toObject(listaElementos.get(i));
						} catch (UngroundedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						} catch (OntologyException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						//Comprobamos que haya practicas disponibles
						if (!(p.getId().equalsIgnoreCase("No hay practicas"))) {
							//Guardamos todas la practicas para despues saber cual es
							//la descripcion y la fecha de entrega de la practica elegida
							ListaPractica[i] = p;
						} else {
							ListaPractica = new Practica[0];
						}
					}	
					System.out.println("numero de practicas " + ListaPractica.length);
					Test[] ListaTest = null;
					Caso[] ListaCaso = null;
					// Recorremos las prácticas para poder pedir los test
					for (int j=0; j < ListaPractica.length; j++){
						System.out.println("Practica " + ListaPractica[j].getId());
						PideTest(ListaPractica[j]);
						ListaTest = RecibeTest();
						for (int k=0; k < ListaTest.length; k ++){
							System.out.println("Test " + ListaTest[k].getId());
							PideCasos(ListaTest[k], ListaPractica[j]);
							ListaCaso = RecibeCasos();
							for (int l=0; l < ListaCaso.length; l++){
								InsertarBBDD ( ListaPractica[j], ListaTest[k], ListaCaso[l] );
								System.out.println("Caso " + ListaCaso.getClass());
							}
						}
					}
				}		
			}
		}
	}
    //-------------------------- FIN COMPORTAMIENTOS PARA PRACTICAS -----------------------

	public class RecibeMensajes extends Behaviour {

		private Resultado tes1;
		private boolean finalizado = false;

		public void action() {
			ACLMessage respuesta = receive();
			if (respuesta != null) {
				if (respuesta.getPerformative() == ACLMessage.AGREE) {
					System.out.println("AGREE");
					addBehaviour(new RecibeMensajes());
					finalizado = true;
				} else {
					if (respuesta.getPerformative() == ACLMessage.FAILURE) {
						System.out.println("FALILURE");
						tes1.setResultadoB(false);
						finalizado = true;
					} else {
						if (respuesta.getContent().equalsIgnoreCase("DONE")) {
							System.out.println("DONE");
							tes1.setResultadoB(true);
							finalizado = true;
						}else{
							if (respuesta.getPerformative() == ACLMessage.INFORM) {
								System.out.println ("INFORM");
								tes1.setResultadoB(true);
								finalizado = true;
							}  							
						}
					}
				}
			}
		}

		@Override
		public boolean done() {
			return finalizado;
		}
	}

	// Clase que recibe un mensaje REQUEST 
	class ReceiverBehaviour extends CyclicBehaviour {

		public ReceiverBehaviour(Agent a) {
			super(a);
		}

		public void action() {
			try {
				ACLMessage mensaje = blockingReceive();
				// Si el mesnaje no es vacio
				if (mensaje!= null) {
					// Si el mensaje es REQUEST	
					if( mensaje.getPerformative()==ACLMessage.REQUEST){
						AbsContentElement abscontent = null;
						abscontent = manager.extractAbsContent(mensaje);
						AbsAgentAction action = (AbsAgentAction) abscontent.getAbsObject(SLVocabulary.ACTION_ACTION);                     

						if (action.getTypeName().equals(pacaOntology.REGISTRARESTADISTICAENTREGA)) {
							// Agree en protocolo REQUEST
							ACLMessage reply = mensaje.createReply();
							reply.setPerformative(ACLMessage.AGREE);
							reply.setContent(mensaje.getContent());
							send(reply);
							
							RegistrarEstadisticaEntrega entrega = (RegistrarEstadisticaEntrega) PACAOntology.toObject(action);
							RegistrarEntrega(entrega);
							
							// INFORM-DONE
							reply.setPerformative(ACLMessage.INFORM);
							Done informdone = new Done();
							informdone.setAction(entrega);
							getContentManager().fillContent(reply, informdone);
							send(reply);					
						}else{
							if (action.getTypeName().equals(pacaOntology.REGISTRARESTADISTICAEVALUACION)) {
								// Agree en protocolo REQUEST
								ACLMessage reply = mensaje.createReply();
								reply.setPerformative(ACLMessage.AGREE);
								reply.setContent(mensaje.getContent());
								send(reply);
								
								RegistrarEstadisticaEvaluacion evaluacion = (RegistrarEstadisticaEvaluacion) PACAOntology.toObject(action);
								RegistrarEvaluaciones(evaluacion);
								
								// INFORM-DONE
								reply.setPerformative(ACLMessage.INFORM);
								Done informdone = new Done();
								informdone.setAction(evaluacion);
								getContentManager().fillContent(reply, informdone);
								send(reply);					
							}else{
								if (action.getTypeName().equals(pacaOntology.MODIFICARDATOSBBDD)) {
									// Agree en protocolo REQUEST
									ACLMessage reply = mensaje.createReply();
									reply.setPerformative(ACLMessage.AGREE);
									reply.setContent(mensaje.getContent());
									send(reply);

									ModificarDatosBBDD x = (ModificarDatosBBDD) PACAOntology.toObject(action);
									String tipo = x.getTipo();
									if (tipo.equals("BORRARPRACTICAS")) {
										System.out.println("Borrar Estructura Practicas");
										EliminarPractica();
									}else{
										if (tipo.equals("BORRARALUMNOS")){
											System.out.println("Borrar datos alumnos");
											EliminarAlumno();
										}else{
											if (tipo.equals("BORRARDATOS")){
												System.out.println("Borrar datos estadisticos");
												EliminarDatos();
											}else{
												if (tipo.equals("CARGARPRACTICAS")){
													System.out.println("Cargar Estructura practicas");
													PidePracticas();
												}else{
													if (tipo.equals("CARGARALUMNOS")){
														System.out.println("Cargar Datos Alumnos");
														CargarDatosAlumnos();
													}
												}
											}
										}
									}			
									// INFORM-DONE
									reply.setPerformative(ACLMessage.INFORM);
									Done informdone = new Done();
									informdone.setAction(x);
									getContentManager().fillContent(reply, informdone);
									send(reply);					
								} 
							}
						}
					}
				} else {
					block();
				}
			} catch(Exception excep) { excep.printStackTrace(); }
		}
	}
	
	// Setup del Agente 
	protected void setup() {
		manager.registerLanguage(codec); 		
		manager.registerOntology(AuthOntology.getInstance());
		manager.registerOntology(pacaOntology.getInstance());
		addBehaviour(new ReceiverBehaviour(this));
	}
}