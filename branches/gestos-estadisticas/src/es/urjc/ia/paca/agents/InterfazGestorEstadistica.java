/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package es.urjc.ia.paca.agents;

//Paquetes JAVA.

import jade.content.abs.AbsAggregate;
import jade.content.abs.AbsConcept;
import jade.content.abs.AbsContentElement;
import jade.content.abs.AbsIRE;
import jade.content.abs.AbsPredicate;
import jade.content.abs.AbsVariable;
import jade.content.lang.Codec;
import jade.content.lang.Codec.CodecException;
import jade.content.lang.sl.SL1Vocabulary;
import jade.content.lang.sl.SL2Vocabulary;
import jade.content.lang.sl.SLCodec;
import jade.content.lang.sl.SLVocabulary;
import jade.content.onto.Ontology;
import jade.content.onto.OntologyException;
import jade.content.onto.basic.Action;
import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.Behaviour;
import jade.core.behaviours.OneShotBehaviour;
import jade.domain.FIPANames;
import jade.lang.acl.ACLMessage;


import java.util.logging.Level;
import java.util.logging.Logger;

import es.urjc.ia.paca.ontology.EliminarDatosBBDD;
import es.urjc.ia.paca.ontology.Corrector;
import es.urjc.ia.paca.ontology.Corrige;
import es.urjc.ia.paca.ontology.Practica;
import es.urjc.ia.paca.ontology.Test;
import es.urjc.ia.paca.ontology.pacaOntology;
import es.urjc.ia.paca.util.AndBuilder;
import es.urjc.ia.paca.util.Resultado;
import es.urjc.ia.paca.util.Testigo;
import es.urjc.ia.paca.agents.InterfazGestor.RecibeMensajes;
import es.urjc.ia.paca.auth.ontology.Autenticado;
import es.urjc.ia.paca.auth.ontology.AuthOntology;
import es.urjc.ia.paca.auth.ontology.Usuario;
import es.urjc.ia.paca.ontology.Caso;
import es.urjc.ia.paca.ontology.CopiaCaso;
import es.urjc.ia.paca.ontology.CopiaFicheroAlumno;
import es.urjc.ia.paca.ontology.CopiaFicheroIN;
import es.urjc.ia.paca.ontology.CopiaFicheroOUT;
import es.urjc.ia.paca.ontology.CopiaFicheroPropio;
import es.urjc.ia.paca.ontology.CopiaTest;
import es.urjc.ia.paca.ontology.CreaCaso;
import es.urjc.ia.paca.ontology.CreaFicheroAlumno;
import es.urjc.ia.paca.ontology.CreaFicheroIN;
import es.urjc.ia.paca.ontology.CreaFicheroOUT;
import es.urjc.ia.paca.ontology.CreaFicheroPropio;
import es.urjc.ia.paca.ontology.CreaPractica;
import es.urjc.ia.paca.ontology.CreaTest;
import es.urjc.ia.paca.ontology.EliminaCaso;
import es.urjc.ia.paca.ontology.EliminaFicheroAlumno;
import es.urjc.ia.paca.ontology.EliminaFicheroIN;
import es.urjc.ia.paca.ontology.EliminaFicheroOUT;
import es.urjc.ia.paca.ontology.EliminaFicheroPropio;
import es.urjc.ia.paca.ontology.EliminaPractica;
import es.urjc.ia.paca.ontology.EliminaTest;
import es.urjc.ia.paca.ontology.FicheroAlumno;
import es.urjc.ia.paca.ontology.FicheroIN;
import es.urjc.ia.paca.ontology.FicheroOUT;
import es.urjc.ia.paca.ontology.FicheroPropio;
import es.urjc.ia.paca.ontology.ModificaPractica;
import es.urjc.ia.paca.ontology.ModificaFicheroPropio;
import es.urjc.ia.paca.ontology.ModificaTest;
import es.urjc.ia.paca.ontology.ModificaFicheroIN;
import es.urjc.ia.paca.ontology.ModificaFicheroOUT;

/**
 *
 * @author alvaroz
 */
public class InterfazGestorEstadistica extends Agent {

	private boolean debug = false;
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
    AID del agente corrector de pr�cticas.
	 */
	private AID correctorAID;
	/**
    Variable booleana cerrojo para evitar que ninguna funci�n se ejecute
    antes de que termine de ejecutarse el m�todo "setup"
	 */
	private boolean terminadoSetup = false;
	/**
    Nombre del agente corrector
	 */
	//private String AgenteCorrector = "corrector";
	public AID AgenteCorrector;
	/**
    Nombre del agente autenticador
	 */
	private String AgenteAutenticador = "autenticador";
	/**
    El identificador de la �ltima pr�ctica solicitada
	 */
	public Practica ultimaPractica;
	public Practica auxUltimaPractica;
	public Test ultimoTest;
	public Test auxUltimoTest;
	/**
    Array con los nombres de los ficheros de la �ltima
    pr�ctica solicitada
	 */
	public String[] ficherosUltimaPractica;
	/**
    Array con los identificadores de los test de la �ltima
    pr�ctica solicitada
	 */
	public String[] TestUltimaPractica;
	/**
    Array con los identificadores de los test posibles para una
    pr�ctica solicitada.
	 */
	public String[] TestPosiblesPractica;
	/**Identifica al agente gestor de practicas**/
	public String gestorPracticas = "gestorDePracticas";
	public String gestorEstadisticas = "gestorEstadisticas";
	public Practica[] auxPracticas;
	public Test[] auxTest;
	public FicheroPropio[] FicherosPropiosDisponibles;
	public FicheroAlumno[] FicherosAlumnoDisponibles;
	public FicheroIN[] FicherosINDisponibles;
	public FicheroOUT[] FicherosOUTDisponibles;
	public Caso ultimoCaso;

	/**
    Constructor.
	 */
	public InterfazGestorEstadistica() {

		miAID = getAID();
	}
	public String usuario;
	public String passUsu;

	public void setUsuarioAux(String usu) {
		this.usuario = usu;
	}

	public void setPasswordAux(String pas) {
		this.passUsu = pas;
	}

	public String getUsuarioAux() {
		return this.usuario;
	}

	public String getPasswordAux() {
		return this.passUsu;
	}

	/**
    Este m�todo asigna el identificador del alumno que representamos.
	 */
	public void setAlumnoID(String id) {
		this.alumnoID = id;
	}

	/**
    Este m�todo asigna el password que utilizara el alumno que representamos.
	 */
	public void setAlumnoPass(String pass) {
		this.alumnoPass = pass;
	}

	/**
    Este m�todo devuelve el identificador del alumno que representamos.
	 */
	public String getAlumnoID() {
		return this.alumnoID;
	}

	/**
    Este m�todo devuelve el password del alumno que representamos.
	 */
	public String getAlumnoPass() {
		return this.alumnoPass;
	}

	@Override
	protected void setup() {

		// Register the codec for the SL0 language
		getContentManager().registerLanguage(codec);

		// Register the ontology used by this application
		getContentManager().registerOntology(AuthOntology.getInstance());
		getContentManager().registerOntology(pacaOntology.getInstance());

		terminadoSetup = true;


	}

	//---------------- COMPORTAMIENTOS PARA LA AUTENTICACION  ----------------------------
	public class EnviaAutenticaBehaviour extends OneShotBehaviour {

		private String usuAux;
		private String passAux;
		private Resultado tes1;

		public EnviaAutenticaBehaviour(Agent _a, Resultado tes, String usuario, String passw) {
			super(_a);
			this.usuAux = usuario;
			this.passAux = passw;
			this.tes1 = tes;
		}

		public void action() {
			ACLMessage solicitud = new ACLMessage(ACLMessage.QUERY_IF);
			solicitud.setLanguage(codec.getName());
			solicitud.setOntology(AuthOntology.ONTOLOGY_NAME);
			solicitud.addReceiver(new AID(AgenteAutenticador, AID.ISLOCALNAME));

			// Creamos el predicado.
			Autenticado aut = new Autenticado();
			Usuario user2 = new Usuario();

			// Creamos el usuario
			user2.setUser_id(usuAux);
			user2.setPassword(passAux);

			aut.setUsuario(user2);

			//Metodos auxiliares para no pasar parametros innecesarios al comportamiento "Recibiendo"
			//Guardamos el usuario y el password por si la autenticacion ha sido correcta
			setUsuarioAux(usuAux);
			setPasswordAux(passAux);

			try {
				//Mandamos el predicado "aut"
				getContentManager().fillContent(solicitud, aut);
				addBehaviour(new RecibeMensajes(myAgent, tes1));
				send(solicitud);
			} catch (CodecException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (OntologyException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
	}

	//Comportamiento para autentica al usuario
	public class RecibeAutenticacion extends OneShotBehaviour {

		private Resultado tes1;
		private boolean resultado;
		private AbsContentElement mens1;
		private String usu1;
		private String pass1;

		public RecibeAutenticacion(Agent _a, Resultado tes, AbsContentElement mensaje) {
			super(_a);
			this.tes1 = tes;
			this.mens1 = mensaje;
		}

		public void action() {
			try {

				String tipoObjetoContenido = mens1.getTypeName();

				if (tipoObjetoContenido.equals(SL1Vocabulary.NOT)) {
					resultado = false;
				} else {
					usu1 = getUsuarioAux();
					pass1 = getPasswordAux();
					setAlumnoID(usu1);
					setAlumnoPass(pass1);
					resultado = true;
				}
			} catch (Exception ex) {
				resultado = false;
				tes1.setResultadoB(resultado);
			}

			if (debug) {
				System.out.println("Interfaz - Autenticar: " + resultado);
			}

			tes1.setResultadoB(resultado);
			if (debug) {
				System.out.println("Rellenamos el testigo " + tes1.isRelleno());
			}
		}
	}
	//-------------- FIN COMPORTAMIENTOS PARA LA AUTENTICACION ----------------------------

	// ELIMINAR ESTRUCTURA PRACTICAS
	public class EliminarPractica extends OneShotBehaviour {

		private Testigo tes;

		public EliminarPractica(Agent _a, Testigo tes1) {
			super(_a);
            this.tes = tes1;
		}

		public void action() {
			AID receiver = new AID(gestorEstadisticas, AID.ISLOCALNAME);
			ACLMessage solicitud = new ACLMessage(ACLMessage.REQUEST);
			solicitud.addReceiver(receiver);
			solicitud.setLanguage(codec.getName());
			solicitud.setOntology(pacaOntology.NAME);
			System.out.println("{INTERFAZ} Eliminar practica");

			EliminarDatosBBDD e = new EliminarDatosBBDD();
			e.setTipo("BORRARPRACTICAS");

			Action act = new Action();
			act.setAction(e);
			act.setActor(receiver);
			try{
				getContentManager().fillContent(solicitud, act);
				solicitud.setProtocol(FIPANames.InteractionProtocol.FIPA_REQUEST);
                addBehaviour(new RecibeMensajes(myAgent, tes));
				send(solicitud);
			}catch (Exception exc) { exc.printStackTrace();	}
			System.out.println("{INTERFAZ} Mensaje enviado - eliminar practica");
		}
	}

	// ELIMINAR DATOS DE ALUMNOS
	public class EliminarAlumno extends OneShotBehaviour {

		private Testigo tes;

		public EliminarAlumno(Agent _a, Testigo tes1) {
			super(_a);
            this.tes = tes1;
		}

		public void action() {
			AID receiver = new AID(gestorEstadisticas, AID.ISLOCALNAME);
			ACLMessage solicitud = new ACLMessage(ACLMessage.REQUEST);
			solicitud.addReceiver(receiver);
			solicitud.setLanguage(codec.getName());
			solicitud.setOntology(pacaOntology.NAME);
			EliminarDatosBBDD e = new EliminarDatosBBDD();
			e.setTipo("BORRARALUMNOS");

			Action act = new Action();
			act.setAction(e);
			act.setActor(receiver);
			try{
				getContentManager().fillContent(solicitud, act);
				solicitud.setProtocol(FIPANames.InteractionProtocol.FIPA_REQUEST);
                addBehaviour(new RecibeMensajes(myAgent, tes));
				send(solicitud);
			}catch (Exception exc) { exc.printStackTrace();	}
			System.out.println("{INTERFAZ} Eliminar alumno");
		}
	}

	// ELIMINAR DATOS ESTADISTICOS
	public class EliminarDatos extends OneShotBehaviour {

		private Testigo tes;

		public EliminarDatos(Agent _a, Testigo tes1) {
			super(_a);
            this.tes = tes1;
		}

		public void action() {
			//try {
			AID receiver = new AID(gestorEstadisticas, AID.ISLOCALNAME);
			ACLMessage solicitud = new ACLMessage(ACLMessage.REQUEST);
			solicitud.addReceiver(receiver);
			solicitud.setLanguage(codec.getName());
			solicitud.setOntology(pacaOntology.NAME);
        	System.out.println("{INTERFAZ} Eliminar datos");

			EliminarDatosBBDD e = new EliminarDatosBBDD();
			e.setTipo("BORRARDATOS");

			Action act = new Action();
			act.setAction(e);
			act.setActor(receiver);
			try{
				getContentManager().fillContent(solicitud, act);
				solicitud.setProtocol(FIPANames.InteractionProtocol.FIPA_REQUEST);
                addBehaviour(new RecibeMensajes(myAgent, tes));
				send(solicitud);
			}catch (Exception exc) { exc.printStackTrace();	}
        	System.out.println("{INTERFAZ} Mensaje enviado - Eliminar datos");

		}
	}

	// CARGAR DATOS ALUMNOS
	public class CargarAlumno extends OneShotBehaviour {
		private Testigo tes;

		public CargarAlumno(Agent _a, Testigo tes1) {
			super(_a);
            this.tes = tes1;
		}	

		public void action() {
			//try {
			AID receiver = new AID(gestorEstadisticas, AID.ISLOCALNAME);
			ACLMessage solicitud = new ACLMessage(ACLMessage.REQUEST);
			solicitud.addReceiver(receiver);
			solicitud.setLanguage(codec.getName());
			solicitud.setOntology(pacaOntology.NAME);

			EliminarDatosBBDD e = new EliminarDatosBBDD();
			e.setTipo("CARGARALUMNOS");
			Action act = new Action();
			act.setAction(e);
			act.setActor(receiver);
			try{
				getContentManager().fillContent(solicitud, act);
				solicitud.setProtocol(FIPANames.InteractionProtocol.FIPA_REQUEST);
                addBehaviour(new RecibeMensajes(myAgent, tes));
				send(solicitud);
			}catch (Exception exc) { exc.printStackTrace();	}
		}
	}

	// CARGAR ESTRUCTURA PRACTICAS
	public class CargarPractica extends OneShotBehaviour {
		
		private Testigo tes;

		public CargarPractica(Agent _a, Testigo tes1) {
			super(_a);
            this.tes = tes1;
		}	

		public void action() {
			AID receiver = new AID(gestorEstadisticas, AID.ISLOCALNAME);
			ACLMessage solicitud = new ACLMessage(ACLMessage.REQUEST);
			solicitud.addReceiver(receiver);
			solicitud.setLanguage(codec.getName());
			solicitud.setOntology(pacaOntology.NAME);

			EliminarDatosBBDD e = new EliminarDatosBBDD();
			e.setTipo("CARGARPRACTICAS");
			Action act = new Action();
			act.setAction(e);
			act.setActor(receiver);
			try{
				getContentManager().fillContent(solicitud, act);
				solicitud.setProtocol(FIPANames.InteractionProtocol.FIPA_REQUEST);
                addBehaviour(new RecibeMensajes(myAgent, tes));
				send(solicitud);
			}catch (Exception exc) { exc.printStackTrace();	}
		}
	}

	// RECIBE MENSAJES
	public class RecibeMensajes extends Behaviour {

		private Resultado tes1;
		private boolean finalizado = false;

		public RecibeMensajes(Agent _a, Resultado tes) {
			super(_a);
			this.tes1 = tes;
		}

		public void action() {
			ACLMessage respuesta = receive();
			if (respuesta != null) {
				//				try {
				if (respuesta.getPerformative() == ACLMessage.AGREE) {
					System.out.println ("INFORM");
					addBehaviour(new RecibeMensajes(myAgent, tes1));
					finalizado = true;
				} else {
					if (respuesta.getPerformative() == ACLMessage.FAILURE) {
						tes1.setResultadoB(false);
						finalizado = true;
					} else {
						if (respuesta.getPerformative() == ACLMessage.INFORM) {
							System.out.println ("INFORM");
							tes1.setResultadoB(true);
							finalizado = true;
						}
					}
				}
							/*else {
								AbsContentElement listaAbs = null;
								listaAbs = getContentManager().extractAbsContent(respuesta);
								String tipoMensaje = listaAbs.getTypeName();
								if (tipoMensaje.equals("autenticado") | tipoMensaje.equals("not")) {
									addBehaviour(new RecibeAutenticacion(myAgent, tes1, listaAbs));
									finalizado = true;
								} else if (tipoMensaje.equals("=")) {
									AbsAggregate listaElementos = (AbsAggregate) listaAbs.getAbsObject(SLVocabulary.EQUALS_RIGHT);

									//Cogemos el primer elemento de la lista
									AbsConcept primerElem = (AbsConcept) listaElementos.get(0);

									//Miramos el tipo del primer elemento
									String tipo = primerElem.getTypeName();
								}
							}
						}
					}
				} catch (CodecException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (OntologyException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}*/
			} else {
				block();
			}

		}

		@Override
		public boolean done() {
			return finalizado;
		}
	}

}