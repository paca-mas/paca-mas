/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package es.urjc.ia.paca.agents;

//Paquetes JAVA.
import jade.content.abs.AbsAgentAction;
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
import jade.domain.FIPAAgentManagement.Property;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import jade.util.leap.Collection;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

import com.sun.image.codec.jpeg.TruncatedFileException;

import java.util.logging.Level;
import java.util.logging.Logger;
import sun.misc.Sort;

import es.urjc.ia.paca.ontology.Alumno;
import es.urjc.ia.paca.ontology.Corrector;
import es.urjc.ia.paca.ontology.Corrige;
import es.urjc.ia.paca.ontology.EvaluacionPractica;
import es.urjc.ia.paca.ontology.FicheroFuentes;
import es.urjc.ia.paca.ontology.Interactua;
import es.urjc.ia.paca.ontology.Practica;
import es.urjc.ia.paca.ontology.ResultadoEvaluacion;
import es.urjc.ia.paca.ontology.Test;
import es.urjc.ia.paca.ontology.Tests;
import es.urjc.ia.paca.ontology.pacaOntology;
import es.urjc.ia.paca.ontology.Fichero.FuentesPrograma;
import es.urjc.ia.paca.util.AndBuilder;
import es.urjc.ia.paca.util.EstadoCorrector;
import es.urjc.ia.paca.util.Resultado;
import es.urjc.ia.paca.auth.ontology.Autenticado;
import es.urjc.ia.paca.auth.ontology.AuthOntology;
import es.urjc.ia.paca.auth.ontology.Usuario;
import es.urjc.ia.paca.ontology.Caso;
import es.urjc.ia.paca.ontology.EntregarPractica;
import es.urjc.ia.paca.ontology.FicheroAlumno;
import es.urjc.ia.paca.ontology.FicheroIN;
import es.urjc.ia.paca.ontology.FicheroOUT;
import es.urjc.ia.paca.ontology.FicheroPropio;
import es.urjc.ia.paca.ontology.FicherosAlumno;
import es.urjc.ia.paca.ontology.ModificaPractica;
import es.urjc.ia.paca.ontology.FicherosPropios;
import es.urjc.ia.paca.ontology.ModificaFicheroPropio;
import es.urjc.ia.paca.ontology.ModificaTest;
import es.urjc.ia.paca.ontology.ModificaFicheroIN;
import es.urjc.ia.paca.ontology.ModificaFicheroOUT;

/**
 *
 * @author alvaro
 */
public class InterfazGestor extends Agent {

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
    public String ultimaPractica;
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
    public String gestorPracticas = "gestor";
    public Practica[] auxPracticas;
    public Test[] auxTest;
    public FicheroPropio[] FicherosPropiosDisponibles;
    public FicheroIN[] FicherosINDisponibles;
    public FicheroOUT[] FicherosOUTDisponibles;
    public Caso ultimoCaso;

    /**
    Constructor.
     */
    public InterfazGestor() {

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

    /**
     * Este m�todo devuelve el Agente Corrector que hemos elegido
     **/
    public synchronized AID getAgenteCorrector() {
        while (this.AgenteCorrector == null) {
            try {
                wait();
            } catch (InterruptedException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        return this.AgenteCorrector;
    }

    /**
     * Este m�todo asigna el Agente Corrector que hemos elegido
     */
    public synchronized void setAgenteCorrector(AID agenteCorr) {
        this.AgenteCorrector = agenteCorr;
        notify();
    }
    /**
     * Creamos la tabla Hash "almacenCorrec" para guardar los correctores que hemos utilizado
     */
    public Hashtable<AID, Long> almacenCorrec = new Hashtable<AID, Long>();
    /**
     * Usado guarda el numero de veces que se ha utilizado un corrector
     */
    public Integer usado;

    /**
    Este m�todo se ejecuta al iniciar el agente y es el encargado de configurarlo.
     */
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

    //-------------- COMPORTAMIENTOS PARA PEDIR LAS PRACTICAS ----------------------------
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
                //printError(myAgent.getLocalName()+" getRoleName() unsucceeded. Reason:" + oe.getMessage());
                System.out.println("Excepcion en ontologia");
                oe.printStackTrace();
            } catch (java.lang.NullPointerException e) {
                System.out.println("Empty message");
                e.printStackTrace();
            } catch (Exception e) {
                // Si obtenemos alguna excepci�n de error, directamente no
                // ofrecemos las practicas ...
                retornable = new Practica[1];
                retornable[0] = new Practica("Error en la obtenci�n de las pr�cticas");
            }
            tes1.setResultado(retornable);
        }
    }
    //-------------------------- FIN COMPORTAMIENTOS PARA PRACTICAS -----------------------

    //-------------------------- COMPORTAMIENTOS PARA TESTS -------------------------------
    public class PideTestBeha extends OneShotBehaviour {

        private Resultado tes1;
        private String IdPractica;

        public PideTestBeha(Agent _a, Resultado tes, String practica) {
            super(_a);
            this.tes1 = tes;
            this.IdPractica = practica;
        }

        public void action() {

            //Nos guardamos la �ltima pr�ctica solicitada
            if (IdPractica == null) {
                IdPractica = ultimaPractica;
            } else {
                ultimaPractica = IdPractica;
            }

            //AID agentCorrec = getAgenteCorrector();
            AID receiver = new AID(gestorPracticas, AID.ISLOCALNAME);
            ACLMessage solicitud = new ACLMessage(ACLMessage.QUERY_REF);
            solicitud.addReceiver(receiver);
            solicitud.setLanguage(codec.getName());
            solicitud.setOntology(pacaOntology.NAME);

            // Generamos el mensaje para enviar
            Corrige cor = new Corrige();
            Practica pract = new Practica();
            pract.setId(IdPractica);
            pract.setDescripcion("");
            Corrector correc = new Corrector();
            correc.setId(gestorPracticas);
            cor.setPractica(pract);
            cor.setCorrector(correc);

            Test te = new Test();
            te.setId("?test");
            te.setDescripcion("");


            try {
                //Convertimos la practica a un concepto abstracto
                AbsConcept absPract = (AbsConcept) PACAOntology.fromObject(pract);

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

    //-------------------------- COMPORTAMIENTO PARA RECIBIR MENSAJES ---------------------
    public class RecibeMensajes extends Behaviour {

        private Resultado tes1;
        private boolean finalizado = false;

        public RecibeMensajes(Agent _a, Resultado tes) {
            super(_a);
            this.tes1 = tes;
        }

        public void action() {

            jade.lang.acl.ACLMessage respuesta = receive();

            if (respuesta != null) {
                try {
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
                        if (tipo.equals(pacaOntology.PRACTICA)) {
                            addBehaviour(new RecibePracticasBeh(myAgent, tes1, listaElementos));
                            finalizado = true;
                        } else if (tipo.equals(pacaOntology.TEST)) {
                            addBehaviour(new RecibeTestBeha(myAgent, tes1, listaElementos));
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
            } else {
                block();
            }

        }

        @Override
        public boolean done() {
            // TODO Auto-generated method stub
            return finalizado;
        }
    }
}
