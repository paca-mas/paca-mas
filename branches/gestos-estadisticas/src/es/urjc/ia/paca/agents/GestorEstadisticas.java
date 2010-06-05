package es.urjc.ia.paca.agents;

import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.http.HttpServletRequest;

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
import jade.content.lang.sl.SL1Vocabulary;
import jade.content.lang.sl.SL2Vocabulary;
import jade.content.lang.sl.SLCodec;
import jade.content.lang.sl.SLVocabulary;
import jade.content.onto.Ontology;
import jade.content.onto.OntologyException;
import jade.content.onto.basic.Done;
import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.Behaviour;
import jade.core.behaviours.CyclicBehaviour;
import jade.core.behaviours.OneShotBehaviour;
import jade.domain.FIPANames;
import jade.lang.acl.ACLMessage;
import jade.util.leap.List;
import es.urjc.ia.paca.agents.InterfazGestor.PideTestBeha;
import es.urjc.ia.paca.agents.InterfazGestor.RecibeAutenticacion;
import es.urjc.ia.paca.agents.InterfazGestor.RecibeCasosBeha;
import es.urjc.ia.paca.agents.InterfazGestor.RecibeFicherosAlumnoBeha;
import es.urjc.ia.paca.agents.InterfazGestor.RecibeFicherosINBeha;
import es.urjc.ia.paca.agents.InterfazGestor.RecibeFicherosOUTBeha;
import es.urjc.ia.paca.agents.InterfazGestor.RecibeFicherosPropiosBeha;
import es.urjc.ia.paca.agents.InterfazGestor.RecibeMensajes;
import es.urjc.ia.paca.agents.InterfazGestor.RecibePracticasBeh;
import es.urjc.ia.paca.agents.InterfazGestor.RecibeTestBeha;
import es.urjc.ia.paca.auth.ontology.Autenticado;
import es.urjc.ia.paca.auth.ontology.AuthOntology;
import es.urjc.ia.paca.auth.ontology.Usuario;
import es.urjc.ia.paca.ontology.Alumno;
import es.urjc.ia.paca.ontology.Caso;
import es.urjc.ia.paca.ontology.Corrector;
import es.urjc.ia.paca.ontology.Corrige;
import es.urjc.ia.paca.ontology.EliminarDatosBBDD;
import es.urjc.ia.paca.ontology.EstadisticaEvaluacionCaso;
import es.urjc.ia.paca.ontology.FicheroAlumno;
import es.urjc.ia.paca.ontology.FicheroIN;
import es.urjc.ia.paca.ontology.FicheroOUT;
import es.urjc.ia.paca.ontology.FicheroPropio;
import es.urjc.ia.paca.ontology.Practica;
import es.urjc.ia.paca.ontology.RegistrarEstadisticaEntrega;
import es.urjc.ia.paca.ontology.RegistrarEstadisticaEvaluacion;
import es.urjc.ia.paca.ontology.Test;
import es.urjc.ia.paca.ontology.pacaOntology;
import es.urjc.ia.paca.parser.ParsearAlumnos;
import es.urjc.ia.paca.parser.ProcesarXML;
import es.urjc.ia.paca.util.AndBuilder;
import es.urjc.ia.paca.util.Resultado;
import es.urjc.ia.paca.util.Testigo;

public class GestorEstadisticas extends Agent {
	private boolean debug = false;
	private Codec codec = new SLCodec();
	private ContentManager manager  = (ContentManager)getContentManager();
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
    public Practica[] auxPracticas;
    public Test[] auxTest;
    public Caso ultimoCaso;

    /**
    Constructor.
     */
    public GestorEstadisticas() {

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
    
    //-------------- INICIO COMPORTAMIENTOS PARA LA AUTENTICACION ----------------------------    
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

    //-------------- INICIO PROCEDURES PARA BORRAR ----------------------------
    private boolean EliminarPractica() {
		//try{
		String frase = "delete from `b_practica`;";
		System.out.println ("[GE]" + frase);
		//stat.executeUpdate(frase);
		//frase = "delete from b_test;";
		//stat.executeUpdate(frase);
		//frase = "delete from b_caso;";
		//stat.executeUpdate(frase);
		//} catch (SQLException ex) {
		//return false;
		//}
		return true;
	}

	private boolean EliminarAlumno() {
		//try {
		String frase = "delete from usuarios;";
		System.out.println("[GE]" + frase);
		//stat.executeUpdate(frase);
		//} catch (SQLException ex) {
		//return false;
		//}
		return true;
	}

	private boolean EliminarDatos() {
		//try{
		String frase = "delete from `b_practica`;";
		System.out.println ("[GE]" + frase);
		//stat.executeUpdate(frase);
		//frase = "delete from b_test;";
		//stat.executeUpdate(frase);
		//frase = "delete from b_caso;";
		//stat.executeUpdate(frase);
		//} catch (SQLException ex) {
		//return false;
		//}
		return true;
	}
    //-------------- FIN PROCEDURES PARA BORRAR ----------------------------
	
    //-------------- INICIO CARGA DATOS ALUMNO ----------------------------
	private boolean CargaDatosAlumnos (){
		boolean terminar = false;
		java.util.List<Alumno> lista = new ArrayList<Alumno>();
		System.out.println("Antes de pasarle el fichero");
		lista = ParsearAlumnos.parsearArchivoXml("C:/Users/sandrita/workspace_PFC/gestor-estadisticas/src/es/urjc/ia/paca/parser/correccion.xml");
		System.out.println(lista);
		return terminar; 
	}
    //-------------- FIN CARGA DATOS ALUMNOS ----------------------------

    //-------------------------- INICIO COMPORTAMIENTOS PARA PRACTICAS -----------------------	
    public class PidePracticasBehavior extends OneShotBehaviour {

        private Resultado tes;

        public PidePracticasBehavior(Agent _a, Resultado tes1) {
            super(_a);
            this.tes = tes1;
        }

        public void action() {

            AID receiver = new AID(gestorPracticas, AID.ISLOCALNAME);
            //AID agentCorrec = getAgenteCorrector();
            ACLMessage solicitud = new ACLMessage(ACLMessage.QUERY_REF);
            solicitud.addReceiver(receiver);
            solicitud.setLanguage(codec.getName());
            solicitud.setOntology(pacaOntology.NAME);

            Practica pract = new Practica();
            pract.setId("?practica");
            pract.setDescripcion("");

            try {
                Corrector correc = new Corrector();
                correc.setId(gestorPracticas);

                //Convertimos la practica a un objecto abstracto
                AbsConcept absPract = (AbsConcept) PACAOntology.fromObject(pract);

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
                addBehaviour(new RecibeMensajes(myAgent, tes));              
                send(solicitud);
                System.out.println("[GE] Mensaje completado");
            } catch (NullPointerException ex) {
                // TODO Auto-generated catch block
                ex.printStackTrace();
            } catch (OntologyException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
            } catch (CodecException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

        }
    }

    public class RecibePracticasBeh extends OneShotBehaviour {

        private Resultado tes1;
        private AbsAggregate practicas1;

        public RecibePracticasBeh(Agent _a, Resultado tes, AbsAggregate practicas) {
            super(_a);
            this.tes1 = tes;
            this.practicas1 = practicas;
            
        }

        public void action() {
            Practica[] retornable = new Practica[0];
            try {
                //Sacamos las practicas que vienen en el mensaje como conceptos abstractos

                Practica p = new Practica();
                retornable = new Practica[practicas1.size()];
                Practica[] aux = new Practica[practicas1.size()];

                for (int i = 0; i < practicas1.size(); i++) {
                    //Pasamos de concepto abstracto a objeto "real", en este caso son practicas
                    p = (Practica) PACAOntology.toObject(practicas1.get(i));

                    //Comprobamos que haya practicas disponibles
                    if (!(p.getId().equalsIgnoreCase("No hay practicas"))) {
                        retornable[i] = p;
                        //Guardamos todas la practicas para despues saber cual es
                        //la descripcion y la fecha de entrega de la practica elegida
                        aux[i] = p;
                    } else {
                        retornable = new Practica[0];
                    }
                }
                auxPracticas = aux;
            } catch (OntologyException oe) {
                System.out.println("[GE]Excepcion en ontologia");
                oe.printStackTrace();
            } catch (java.lang.NullPointerException e) {
                System.out.println("[GE]Empty message");
                e.printStackTrace();
            } catch (Exception e) {
                // Si obtenemos alguna excepci�n de error, directamente no
                // ofrecemos las practicas ...
                retornable = new Practica[1];
                retornable[0] = new Practica("Error en la obtenci�n de las pr�cticas");
            }
            System.out.println("[GE]Retornable ....");
            System.out.println("[GE]" + retornable);
            tes1.setResultado(retornable);
        }
    }
    //-------------------------- FIN COMPORTAMIENTOS PARA PRACTICAS -----------------------

    //-------------------------- COMPORTAMIENTOS PARA TESTS -------------------------------
    public class PideTestBeha extends OneShotBehaviour {

        private Resultado tes1;
        private Practica practica;
        private boolean guardar;

        public PideTestBeha(Agent _a, Resultado tes, Practica practica, boolean guardar) {
            super(_a);
            this.tes1 = tes;
            this.practica = practica;
            this.guardar = guardar;
        }

        public void action() {

            if (guardar) {
                //Nos guardamos la �ltima pr�ctica solicitada
                if (practica != null) {
                    ultimaPractica = practica;
                } else {
                    practica = ultimaPractica;
                }
            } else {
                auxUltimaPractica = practica;
            }



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
                addBehaviour(new RecibeMensajes(myAgent, tes1));
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
    }

    public class RecibeTestBeha extends OneShotBehaviour {

        private Resultado tes1;
        private AbsAggregate tests1;

        public RecibeTestBeha(Agent _a, Resultado tes, AbsAggregate tests_) {
            super(_a);
            this.tes1 = tes;
            this.tests1 = tests_;
        }

        public void action() {

            Test[] retornable = new Test[0];
            try {
                //Sacamos los tests que vienen en el mensaje como conceptos abstractos

                Test t = new Test();
                retornable = new Test[tests1.size()];
                Test[] aux = new Test[tests1.size()];
                int j = 0;
                for (int i = 0; i < tests1.size(); i++) {
                    //Pasamos de concepto abstracto a objeto "real", en este caso son tests
                    t = (Test) PACAOntology.toObject(tests1.get(i));

                    if (!(t.getId().equalsIgnoreCase("No hay Tests"))) {

                        retornable[i] = t;
                        //Guardamos todos los test de la practica para posteriormente saber
                        //la descripcion de los test elegidos
                        aux[i] = t;
                    } else {
                        retornable = new Test[0];
                        aux = new Test[0];
                    }
                }
                auxTest = aux;
            } catch (Exception e) {
                // Si obtenemos alguna excepci�n de error, directamente no
                // ofrecemos los tests ...
                retornable = new Test[1];
                retornable[0] = new Test("Error en la obtenci�n de los tests");
            }
            tes1.setResultado(retornable);


        }
    }
    //-------------------------- FIN COMPORTAMIENTOS PARA TESTS ---------------------------
    
    //-------------------------- INICIO COMPORTAMIENTOS PARA CASOS ---------------------------
    class PideCasos extends OneShotBehaviour {

        private Resultado tes;
        private Test test;
        private boolean guardar;

        public PideCasos(Agent a, Resultado tes, Test test, boolean guardar) {
            super(a);
            this.tes = tes;
            this.test = test;
            this.guardar = guardar;

        }

        public void action() {

            try {

                Practica pt = null;
                if (guardar) {
                    if (test == null) {
                        test = ultimoTest;
                    } else {
                        ultimoTest = test;
                    }
                    pt = ultimaPractica;
                } else {
                    auxUltimoTest = test;
                    pt = auxUltimaPractica;
                }
                //CREAMOS EL MENSAJE
                ACLMessage msg = new ACLMessage(ACLMessage.QUERY_REF);
                AID receiver = new AID(gestorPracticas, AID.ISLOCALNAME);

                msg.setSender(getAID());
                msg.addReceiver(receiver);
                msg.setLanguage(codec.getName());
                msg.setOntology(pacaOntology.NAME);



                //CREAMOS EL PREDICADO TESTS PARA SABER CUAL ES LA PRACTICA
                //Y EL TEST DE LOS CASOS QUE VAMOS A PEDIR

                AbsConcept abspract = (AbsConcept) PACAOntology.fromObject(pt);
                AbsConcept absts = (AbsConcept) PACAOntology.fromObject(test);
                AbsPredicate abstss = new AbsPredicate(pacaOntology.TESTS);
                abstss.set(pacaOntology.TEST, absts);
                abstss.set(pacaOntology.PRACTICA, abspract);


                //CREAMOS EL PREDICADO PARA PEDIR LOS CASOS
                Caso ca = new Caso("?Caso");
                AbsConcept absca = (AbsConcept) PACAOntology.fromObject(ca);
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
                addBehaviour(new RecibeMensajes(myAgent, tes));
                myAgent.send(msg);
            } catch (CodecException ex) {
                Logger.getLogger(Interfaz.class.getName()).log(Level.SEVERE, null, ex);
            } catch (OntologyException ex) {
                Logger.getLogger(Interfaz.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    public class RecibeCasosBeha extends OneShotBehaviour {

        private Resultado tes2;
        private AbsAggregate ficheros1;

        public RecibeCasosBeha(Agent _a, Resultado tes, AbsAggregate ficheros) {
            super(_a);
            this.tes2 = tes;
            this.ficheros1 = ficheros;
        }

        public void action() {
            Caso[] retornable = new Caso[0];
            try {
                //Sacamos los ficheros que vienen en el mensaje como conceptos abstractos

                // Pasamos la lista a un String[]
                Caso ca;
                retornable = new Caso[ficheros1.size()];
                for (int i = 0; i < ficheros1.size(); i++) {
                    ca = (Caso) PACAOntology.toObject(ficheros1.get(i));
                    if (!(ca.getId().equalsIgnoreCase("No hay casos"))) {
                        retornable[i] = ca;
                    } else {
                        retornable = new Caso[0];
                    }

                }



            } catch (Exception e) {
                //ficherosUltimaPractica = retornable;
                //tes2.setResultado(retornable);
            }
            //FicherosPropiosDisponibles = retornable;
            //ficherosUltimaPractica = retornable;
            tes2.setResultado(retornable);

        }
    }
    //-------------------------- FIN COMPORTAMIENTOS PARA CASOS ---------------------------

    
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
System.out.println("[GE]El mensaje no es nulo");
            	try {
                    if (respuesta.getPerformative() == ACLMessage.AGREE) {
                    	System.out.println("[GE]Recibiendo .... AGREE");

                        addBehaviour(new RecibeMensajes(myAgent, tes1));
                        finalizado = true;
                    } else {
                        if (respuesta.getPerformative() == ACLMessage.FAILURE) {
                        	System.out.println("[GE]Recibiendo .... FALILURE");

                            tes1.setResultadoB(false);
                            finalizado = true;
                        } else {
                            if (respuesta.getContent().equalsIgnoreCase("DONE")) {
                            	System.out.println("[GE]Recibiendo ... DONE");

                                tes1.setResultadoB(true);
                                finalizado = true;
                            } else {
                            	System.out.println("[GE]Recibiendo ... otra cosa");

                                AbsContentElement listaAbs = null;
                                listaAbs = getContentManager().extractAbsContent(respuesta);
                                String tipoMensaje = listaAbs.getTypeName();
                                if (tipoMensaje.equals("autenticado") | tipoMensaje.equals("not")) {
                                	System.out.println("[GE]Recibiendo autenticado");

                                	addBehaviour(new RecibeAutenticacion(myAgent, tes1, listaAbs));
                                    finalizado = true;
                                } else if (tipoMensaje.equals("=")) {
                                	System.out.println("[GE]Recibiendo .... otra cosa");

                                    AbsAggregate listaElementos = (AbsAggregate) listaAbs.getAbsObject(SLVocabulary.EQUALS_RIGHT);

                                    //Cogemos el primer elemento de la lista
                                    AbsConcept primerElem = (AbsConcept) listaElementos.get(0);

                                    //Miramos el tipo del primer elemento
                                    String tipo = primerElem.getTypeName();
                                    if (tipo.equals(pacaOntology.PRACTICA)) {
                                    	System.out.println("[GE]Antes de recibir las practicas");
                                        addBehaviour(new RecibePracticasBeh(myAgent, tes1, listaElementos));
                                    	System.out.println("[GE]Despues de recibir las practicas");
                                        finalizado = true;
                                    } else if (tipo.equals(pacaOntology.TEST)) {
                                       // addBehaviour(new RecibeTestBeha(myAgent, tes1, listaElementos));
                                       // finalizado = true;
                                    } else if (tipo.equals(pacaOntology.CASO)) {
                                       // addBehaviour(new RecibeCasosBeha(myAgent, tes1, listaElementos));
                                       // finalizado = true;
                                    } 
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
                }
            } else {
            	System.out.println("[GE]Mensaje vacio");
                block();
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

						if (action.getTypeName().equals(pacaOntology.ELIMINARDATOSBBDD)) {
							// Agree en protocolo REQUEST
							ACLMessage reply = mensaje.createReply();
							reply.setPerformative(ACLMessage.AGREE);
							reply.setContent(mensaje.getContent());
							send(reply);

							EliminarDatosBBDD x = (EliminarDatosBBDD) PACAOntology.toObject(action);
							String tipo = x.getTipo();
							if (tipo.equals("BORRARPRACTICAS")) {
								System.out.println("{GE}borrar practica");
							}else{
								if (tipo.equals("BORRARALUMNOS")){
									System.out.println("{GE}borrar alumnos");
								}else{
									if (tipo.equals("BORRARDATOS")){
										System.out.println("{GE}borrar datos");
									}else{
										if (tipo.equals("CARGARPRACTICAS")){
											System.out.println("{GE}cargar practica");
											Testigo t = new Testigo();
											Resultado r = new Resultado();
											// Pide Practica
										    addBehaviour(new PidePracticasBehavior(myAgent,r));
										}else{
											if (tipo.equals("CARGARALUMNOS")){
												System.out.println("{GE}cargar alumnos");
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