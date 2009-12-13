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
import jade.lang.acl.ACLMessage;


import java.util.logging.Level;
import java.util.logging.Logger;


import es.urjc.ia.paca.ontology.Corrector;
import es.urjc.ia.paca.ontology.Corrige;
import es.urjc.ia.paca.ontology.Practica;
import es.urjc.ia.paca.ontology.Test;
import es.urjc.ia.paca.ontology.pacaOntology;
import es.urjc.ia.paca.util.AndBuilder;
import es.urjc.ia.paca.util.Resultado;
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
    public FicheroPropio[] FicherosPropiosDisponibles;
    public FicheroAlumno[] FicherosAlumnoDisponibles;
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

    //----------------------COMPORTAMIENTOS PARA FICHEROS PROPIOS-----------------------
    class PideFicherosPropios extends OneShotBehaviour {

        private Resultado tes;
        private Test test;
        private boolean guardar;

        public PideFicherosPropios(Agent a, Resultado tes, Test test, boolean guardar) {
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
                //Y EL TEST DE LOS FICHEROS PROPIOS QUE VAMOS A PEDIR
                AbsConcept abspract = (AbsConcept) PACAOntology.fromObject(pt);
                AbsConcept absts = (AbsConcept) PACAOntology.fromObject(test);
                AbsPredicate abstss = new AbsPredicate(pacaOntology.TESTS);
                abstss.set(pacaOntology.TEST, absts);
                abstss.set(pacaOntology.PRACTICA, abspract);


                //CREAMOS EL PREDICADO PARA PEDIR LOS FICHEROS PROPIOS
                FicheroPropio fp = new FicheroPropio("?FicheroPropio");
                AbsConcept absfp = (AbsConcept) PACAOntology.fromObject(fp);
                AbsPredicate absfps = new AbsPredicate(pacaOntology.FICHEROSPROPIOS);
                absfps.set(pacaOntology.TEST, absts);
                absfps.set(pacaOntology.FICHEROPROPIO, absfp);

                //CREAMOS UN ANDBUILDER PARA UNIR LOS DOS PREDICADOS ANTERIORES
                AndBuilder constructor = new AndBuilder();
                constructor.addPredicate(abstss);
                constructor.addPredicate(absfps);


                //CREAMOS EL IRE CON LA VARIABLE X Y ENVIAMOS TODO
                AbsVariable x =
                        new AbsVariable("ficheroPropio", pacaOntology.FICHEROPROPIO);
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

    public class RecibeFicherosPropiosBeha extends OneShotBehaviour {

        private Resultado tes2;
        private AbsAggregate ficheros1;

        public RecibeFicherosPropiosBeha(Agent _a, Resultado tes, AbsAggregate ficheros) {
            super(_a);
            this.tes2 = tes;
            this.ficheros1 = ficheros;
        }

        public void action() {
            FicheroPropio[] retornable = new FicheroPropio[0];
            try {
                //Sacamos los ficheros que vienen en el mensaje como conceptos abstractos

                // Pasamos la lista a un String[]
                FicheroPropio fp;
                retornable = new FicheroPropio[ficheros1.size()];
                for (int i = 0; i < ficheros1.size(); i++) {
                    fp = (FicheroPropio) PACAOntology.toObject(ficheros1.get(i));
                    if (!(fp.getNombre().equalsIgnoreCase("No hay FicherosPropios"))) {
                        retornable[i] = fp;
                    } else {
                        retornable = new FicheroPropio[0];
                    }
                }



            } catch (Exception e) {
                //ficherosUltimaPractica = retornable;
                //tes2.setResultado(retornable);
            }
            FicherosPropiosDisponibles = retornable;
            //ficherosUltimaPractica = retornable;
            tes2.setResultado(retornable);

        }
    }

    /***************fin comportamient ficheros propipos************////////
    /**************COMPORTAMIENTOS PARA PEDIR CASOS*******************************/
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

    /**********************FIN COMPORTAMIENTOS CASOS***********************/
    //----------------------COMPORTAMIENTOS PARA FICHEROS PROPIOS-----------------------
    class PideFicherosAlumno extends OneShotBehaviour {

        private Resultado tes;
        private Test test;
        private boolean guardar;

        public PideFicherosAlumno(Agent a, Resultado tes, Test test, boolean guardar) {
            super(a);
            this.tes = tes;
            this.test = test;
            this.guardar = guardar;

        }

        public void action() {

            try {
                //CREAMOS EL MENSAJE

                Practica pt = null;
                if (guardar) {
                    if (test == null) {
                        test = ultimoTest;
                    } else {
                        ultimoTest = test;
                    }
                    pt = ultimaPractica;
                } else {
                    pt = auxUltimaPractica;
                }
                ACLMessage msg = new ACLMessage(ACLMessage.QUERY_REF);
                AID receiver = new AID(gestorPracticas, AID.ISLOCALNAME);

                msg.setSender(getAID());
                msg.addReceiver(receiver);
                msg.setLanguage(codec.getName());
                msg.setOntology(pacaOntology.NAME);





                //CREAMOS EL PREDICADO TESTS PARA SABER CUAL ES LA PRACTICA
                //Y EL TEST DE LOS FICHEROS PROPIOS QUE VAMOS A PEDIR

                AbsConcept abspract = (AbsConcept) PACAOntology.fromObject(pt);
                AbsConcept absts = (AbsConcept) PACAOntology.fromObject(test);
                AbsPredicate abstss = new AbsPredicate(pacaOntology.TESTS);
                abstss.set(pacaOntology.TEST, absts);
                abstss.set(pacaOntology.PRACTICA, abspract);


                //CREAMOS EL PREDICADO PARA PEDIR LOS FICHEROS PROPIOS
                FicheroAlumno fp = new FicheroAlumno("?FicheroAlumno");
                AbsConcept absfp = (AbsConcept) PACAOntology.fromObject(fp);
                AbsPredicate absfps = new AbsPredicate(pacaOntology.FICHEROSALUMNO);
                absfps.set(pacaOntology.TEST, absts);
                absfps.set(pacaOntology.FICHEROALUMNO, absfp);

                //CREAMOS UN ANDBUILDER PARA UNIR LOS DOS PREDICADOS ANTERIORES
                AndBuilder constructor = new AndBuilder();
                constructor.addPredicate(abstss);
                constructor.addPredicate(absfps);


                //CREAMOS EL IRE CON LA VARIABLE X Y ENVIAMOS TODO
                AbsVariable x =
                        new AbsVariable("ficheroAlumno", pacaOntology.FICHEROALUMNO);
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

    public class RecibeFicherosAlumnoBeha extends OneShotBehaviour {

        private Resultado tes2;
        private AbsAggregate ficheros1;

        public RecibeFicherosAlumnoBeha(Agent _a, Resultado tes, AbsAggregate ficheros) {
            super(_a);
            this.tes2 = tes;
            this.ficheros1 = ficheros;
        }

        public void action() {
            FicheroAlumno[] retornable = new FicheroAlumno[0];
            try {
                //Sacamos los ficheros que vienen en el mensaje como conceptos abstractos

                // Pasamos la lista a un String[]
                FicheroAlumno fp;
                retornable = new FicheroAlumno[ficheros1.size()];
                for (int i = 0; i < ficheros1.size(); i++) {
                    fp = (FicheroAlumno) PACAOntology.toObject(ficheros1.get(i));
                    if (!(fp.getNombre().equalsIgnoreCase("No hay FicherosAlumno"))) {
                        retornable[i] = fp;
                    } else {
                        retornable = new FicheroAlumno[0];
                    }
                }



            } catch (Exception e) {
                //ficherosUltimaPractica = retornable;
                //tes2.setResultado(retornable);
            }
            FicherosAlumnoDisponibles = retornable;
            //ficherosUltimaPractica = retornable;
            tes2.setResultado(retornable);

        }
    }

    /***************fin comportamient ficheros alumon************////////
    /*************COMPORTAMIENTOS PARA PEDIR FICHEROS IN****************///
    class PideFicherosIN extends OneShotBehaviour {

        private Resultado tes;
        private Caso caso;
        private boolean guardar;

        public PideFicherosIN(Agent a, Resultado tes, Caso caso, boolean guardar) {
            super(a);
            this.tes = tes;
            this.caso = caso;
            this.guardar = guardar;

        }

        public void action() {

            try {
                Practica pt = null;
                Test ts = null;
                if (guardar) {
                    if (caso == null) {
                        caso = ultimoCaso;
                    } else {
                        ultimoCaso = caso;
                    }
                    pt = ultimaPractica;
                    ts = ultimoTest;
                } else {
                    pt = auxUltimaPractica;
                    ts = auxUltimoTest;

                }
                //CREAMOS EL MENSAJE
                ACLMessage msg = new ACLMessage(ACLMessage.QUERY_REF);
                AID receiver = new AID(gestorPracticas, AID.ISLOCALNAME);

                msg.setSender(getAID());
                msg.addReceiver(receiver);
                msg.setLanguage(codec.getName());
                msg.setOntology(pacaOntology.NAME);



                //CREAMOS EL PREDICADO TESTS PARA SABER CUAL ES LA PRACTICA
                //Y EL TEST DE LOS FICHEROSIN QUE VAMOS A PEDIR

                AbsConcept abspract = (AbsConcept) PACAOntology.fromObject(pt);
                AbsConcept absts = (AbsConcept) PACAOntology.fromObject(ts);
                AbsPredicate abstss = new AbsPredicate(pacaOntology.TESTS);
                abstss.set(pacaOntology.TEST, absts);
                abstss.set(pacaOntology.PRACTICA, abspract);


                //CREAMOS EL PREDICADO PARA PEDIR LOS FICHEROSIN
                FicheroIN fi = new FicheroIN("?FicheroIN");

                AbsConcept absca = (AbsConcept) PACAOntology.fromObject(caso);
                AbsConcept absfi = (AbsConcept) PACAOntology.fromObject(fi);
                AbsPredicate absfis = new AbsPredicate(pacaOntology.FICHEROSIN);
                absfis.set(pacaOntology.CASO, absca);
                absfis.set(pacaOntology.FICHEROIN, absfi);

                //CREAMOS UN ANDBUILDER PARA UNIR LOS DOS PREDICADOS ANTERIORES
                AndBuilder constructor = new AndBuilder();
                constructor.addPredicate(abstss);
                constructor.addPredicate(absfis);


                //CREAMOS EL IRE CON LA VARIABLE X Y ENVIAMOS TODO
                AbsVariable x =
                        new AbsVariable("ficheroIN", pacaOntology.FICHEROIN);
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

    public class RecibeFicherosINBeha extends OneShotBehaviour {

        private Resultado tes2;
        private AbsAggregate ficheros1;

        public RecibeFicherosINBeha(Agent _a, Resultado tes, AbsAggregate ficheros) {
            super(_a);
            this.tes2 = tes;
            this.ficheros1 = ficheros;
        }

        public void action() {
            FicheroIN[] retornable = new FicheroIN[0];
            try {
                //Sacamos los ficheros que vienen en el mensaje como conceptos abstractos

                // Pasamos la lista a un String[]
                FicheroIN fi;
                retornable = new FicheroIN[ficheros1.size()];
                for (int i = 0; i < ficheros1.size(); i++) {
                    fi = (FicheroIN) PACAOntology.toObject(ficheros1.get(i));
                    if (!(fi.getNombre().equalsIgnoreCase("No hay FicherosIN"))) {
                        retornable[i] = fi;
                    } else {
                        retornable = new FicheroIN[0];
                    }
                }



            } catch (Exception e) {
                //ficherosUltimaPractica = retornable;
                //tes2.setResultado(retornable);
            }

            FicherosINDisponibles = retornable;
            tes2.setResultado(retornable);

        }
    }

    class PideFicherosOUT extends OneShotBehaviour {

        private Resultado tes;
        private Caso caso;
        private boolean guardar;

        public PideFicherosOUT(Agent a, Resultado tes, Caso caso, boolean guardar) {
            super(a);
            this.tes = tes;
            this.caso = caso;
            this.guardar = guardar;

        }

        public void action() {

            try {
               Practica pt = null;
                Test ts = null;
                if (guardar) {
                    if (caso == null) {
                        caso = ultimoCaso;
                    } else {
                        ultimoCaso = caso;
                    }
                    pt = ultimaPractica;
                    ts = ultimoTest;
                } else {
                    pt = auxUltimaPractica;
                    ts = auxUltimoTest;

                }
                //CREAMOS EL MENSAJE
                ACLMessage msg = new ACLMessage(ACLMessage.QUERY_REF);
                AID receiver = new AID(gestorPracticas, AID.ISLOCALNAME);

                msg.setSender(getAID());
                msg.addReceiver(receiver);
                msg.setLanguage(codec.getName());
                msg.setOntology(pacaOntology.NAME);



                //CREAMOS EL PREDICADO TESTS PARA SABER CUAL ES LA PRACTICA
                //Y EL TEST DE LOS FICHEROSIN QUE VAMOS A PEDIR

                AbsConcept abspract = (AbsConcept) PACAOntology.fromObject(pt);
                AbsConcept absts = (AbsConcept) PACAOntology.fromObject(ts);
                AbsPredicate abstss = new AbsPredicate(pacaOntology.TESTS);
                abstss.set(pacaOntology.TEST, absts);
                abstss.set(pacaOntology.PRACTICA, abspract);


                //CREAMOS EL PREDICADO PARA PEDIR LOS FICHEROSIN
                FicheroOUT fo = new FicheroOUT("?FicheroOUT");

                AbsConcept absca = (AbsConcept) PACAOntology.fromObject(caso);
                AbsConcept absfo = (AbsConcept) PACAOntology.fromObject(fo);
                AbsPredicate absfos = new AbsPredicate(pacaOntology.FICHEROSOUT);
                absfos.set(pacaOntology.CASO, absca);
                absfos.set(pacaOntology.FICHEROOUT, absfo);

                //CREAMOS UN ANDBUILDER PARA UNIR LOS DOS PREDICADOS ANTERIORES
                AndBuilder constructor = new AndBuilder();
                constructor.addPredicate(abstss);
                constructor.addPredicate(absfos);


                //CREAMOS EL IRE CON LA VARIABLE X Y ENVIAMOS TODO
                AbsVariable x =
                        new AbsVariable("ficheroOUT", pacaOntology.FICHEROOUT);
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

    public class RecibeFicherosOUTBeha extends OneShotBehaviour {

        private Resultado tes2;
        private AbsAggregate ficheros1;

        public RecibeFicherosOUTBeha(Agent _a, Resultado tes, AbsAggregate ficheros) {
            super(_a);
            this.tes2 = tes;
            this.ficheros1 = ficheros;
        }

        public void action() {
            FicheroOUT[] retornable = new FicheroOUT[0];
            try {
                //Sacamos los ficheros que vienen en el mensaje como conceptos abstractos

                // Pasamos la lista a un String[]
                FicheroOUT fo;
                retornable = new FicheroOUT[ficheros1.size()];
                for (int i = 0; i < ficheros1.size(); i++) {
                    fo = (FicheroOUT) PACAOntology.toObject(ficheros1.get(i));
                    if (!(fo.getNombre().equalsIgnoreCase("No hay FicherosOUT"))) {
                        retornable[i] = fo;
                    } else {
                        retornable = new FicheroOUT[0];
                    }

                }



            } catch (Exception e) {
                //ficherosUltimaPractica = retornable;
                //tes2.setResultado(retornable);
            }
            //FicherosPropiosDisponibles = retornable;
            //ficherosUltimaPractica = retornable;
            FicherosOUTDisponibles = retornable;
            tes2.setResultado(retornable);

        }
    }
    //-------------------------- COMPORTAMIENTO PARA RECIBIR MENSAJES ---------------------

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
                try {
                    if (respuesta.getPerformative() == ACLMessage.AGREE) {

                        addBehaviour(new RecibeMensajes(myAgent, tes1));
                        finalizado = true;
                    } else {
                        if (respuesta.getPerformative() == ACLMessage.FAILURE) {

                            tes1.setResultadoB(false);
                            finalizado = true;
                        } else {
                            if (respuesta.getContent().equalsIgnoreCase("DONE")) {
                                tes1.setResultadoB(true);
                                finalizado = true;
                            } else {
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
                                    } else if (tipo.equals(pacaOntology.FICHEROPROPIO)) {
                                        addBehaviour(new RecibeFicherosPropiosBeha(myAgent, tes1, listaElementos));
                                        finalizado = true;
                                    } else if (tipo.equals(pacaOntology.CASO)) {
                                        addBehaviour(new RecibeCasosBeha(myAgent, tes1, listaElementos));
                                        finalizado = true;
                                    } else if (tipo.equals(pacaOntology.FICHEROALUMNO)) {
                                        addBehaviour(new RecibeFicherosAlumnoBeha(myAgent, tes1, listaElementos));
                                        finalizado = true;
                                    } else if (tipo.equals(pacaOntology.FICHEROIN)) {
                                        addBehaviour(new RecibeFicherosINBeha(myAgent, tes1, listaElementos));
                                        finalizado = true;
                                    } else if (tipo.equals(pacaOntology.FICHEROOUT)) {
                                        addBehaviour(new RecibeFicherosOUTBeha(myAgent, tes1, listaElementos));
                                        finalizado = true;
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
                block();
            }

        }

        @Override
        public boolean done() {
            return finalizado;
        }
    }

    /*----------------COMPORTAMIENTOS PARA MODIFICAR--------------*/
    /*Modificar Practica*/
    public class ModificarPractica
            extends OneShotBehaviour {

        private Resultado tes1;
        private Practica pt;

        public ModificarPractica(Agent _a, Resultado tes, Practica pt) {
            super(_a);
            this.tes1 = tes;
            this.pt = pt;
        }

        public void action() {
            try {
                AID receiver = new AID(gestorPracticas, AID.ISLOCALNAME);
                ACLMessage solicitud = new ACLMessage(ACLMessage.REQUEST);
                solicitud.addReceiver(receiver);
                solicitud.setLanguage(codec.getName());
                solicitud.setOntology(pacaOntology.NAME);


                ModificaPractica mdp = new ModificaPractica();
                mdp.setPractica(pt);

                Action act = new Action();
                act.setAction(mdp);
                act.setActor(receiver);


                getContentManager().fillContent(solicitud, act);
                addBehaviour(new RecibeMensajes(myAgent, tes1));
                send(solicitud);
            } catch (CodecException ex) {
                Logger.getLogger(Interfaz.class.getName()).log(Level.SEVERE, null, ex);
            } catch (OntologyException ex) {
                Logger.getLogger(Interfaz.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    public class ModificarTest extends OneShotBehaviour {

        private Resultado tes1;
        private Test te;

        public ModificarTest(Agent _a, Resultado tes, Test te) {
            super(_a);
            this.tes1 = tes;
            this.te = te;
        }

        public void action() {
            try {

                AID receiver = new AID(gestorPracticas, AID.ISLOCALNAME);
                ACLMessage solicitud = new ACLMessage(ACLMessage.REQUEST);
                solicitud.addReceiver(receiver);
                solicitud.setLanguage(codec.getName());
                solicitud.setOntology(pacaOntology.NAME);


                Practica pt = ultimaPractica;



                ModificaTest mts = new ModificaTest();
                mts.setTest(te);
                mts.setPractica(pt);

                Action act = new Action();
                act.setAction(mts);
                act.setActor(receiver);


                getContentManager().fillContent(solicitud, act);
                addBehaviour(new RecibeMensajes(myAgent, tes1));
                send(solicitud);
            } catch (CodecException ex) {
                Logger.getLogger(Interfaz.class.getName()).log(Level.SEVERE, null, ex);
            } catch (OntologyException ex) {
                Logger.getLogger(Interfaz.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }


        public class ModificarEjecutable extends OneShotBehaviour {

        private Resultado tes1;
        private String ejecutable;

        public ModificarEjecutable(Agent _a, Resultado tes, String ejecutable) {
            super(_a);
            this.tes1 = tes;
            this.ejecutable = ejecutable;
        }

        public void action() {
            try {

                AID receiver = new AID(gestorPracticas, AID.ISLOCALNAME);
                ACLMessage solicitud = new ACLMessage(ACLMessage.REQUEST);
                solicitud.addReceiver(receiver);
                solicitud.setLanguage(codec.getName());
                solicitud.setOntology(pacaOntology.NAME);


                Practica pt = ultimaPractica;
                ultimoTest.setEjecutable(ejecutable);


                ModificaTest mts = new ModificaTest();
                mts.setTest(ultimoTest);
                mts.setPractica(pt);

                Action act = new Action();
                act.setAction(mts);
                act.setActor(receiver);


                getContentManager().fillContent(solicitud, act);
                addBehaviour(new RecibeMensajes(myAgent, tes1));
                send(solicitud);
            } catch (CodecException ex) {
                Logger.getLogger(Interfaz.class.getName()).log(Level.SEVERE, null, ex);
            } catch (OntologyException ex) {
                Logger.getLogger(Interfaz.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    public class ModificarFicherosPropios extends OneShotBehaviour {

        private Resultado tes1;
        private FicheroPropio fp;

        public ModificarFicherosPropios(Agent _a, Resultado tes, FicheroPropio fp) {
            super(_a);
            this.tes1 = tes;
            this.fp = fp;
        }

        public void action() {
            try {
                /*ultimaPractica = IdPractica;
                Practica p = EncontrarPractica();
                tes1.setResultado(p);*/
                AID receiver = new AID(gestorPracticas, AID.ISLOCALNAME);
                ACLMessage solicitud = new ACLMessage(ACLMessage.REQUEST);
                solicitud.addReceiver(receiver);
                solicitud.setLanguage(codec.getName());
                solicitud.setOntology(pacaOntology.NAME);


                Practica pt = ultimaPractica;
                Test ts = ultimoTest;



                //AbsConcept Abspt = (AbsConcept) PACAOntology.fromObject(pt);
                //AbsAgentAction AbsEntp = new AbsAgentAction(pacaOntology.MODIFICAPRACTICA);
                ModificaFicheroPropio mfp = new ModificaFicheroPropio();
                mfp.setTest(ts);
                mfp.setFicheroPropio(fp);
                mfp.setPractica(pt);

                Action act = new Action();
                act.setAction(mfp);
                act.setActor(receiver);

                //AbsEntp.set(pacaOntology.PRACTICA, Abspt);


                getContentManager().fillContent(solicitud, act);
                addBehaviour(new RecibeMensajes(myAgent, tes1));
                send(solicitud);
            } catch (CodecException ex) {
                Logger.getLogger(Interfaz.class.getName()).log(Level.SEVERE, null, ex);
            } catch (OntologyException ex) {
                Logger.getLogger(Interfaz.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    public class ModificarFicherosIN extends OneShotBehaviour {

        private Resultado tes1;
        private FicheroIN fi;

        public ModificarFicherosIN(Agent _a, Resultado tes, FicheroIN fi) {
            super(_a);
            this.tes1 = tes;
            this.fi = fi;
        }

        public void action() {
            try {
                /*ultimaPractica = IdPractica;
                Practica p = EncontrarPractica();
                tes1.setResultado(p);*/
                AID receiver = new AID(gestorPracticas, AID.ISLOCALNAME);
                ACLMessage solicitud = new ACLMessage(ACLMessage.REQUEST);
                solicitud.addReceiver(receiver);
                solicitud.setLanguage(codec.getName());
                solicitud.setOntology(pacaOntology.NAME);


                Practica pt = ultimaPractica;
                Test ts = ultimoTest;



                //AbsConcept Abspt = (AbsConcept) PACAOntology.fromObject(pt);
                //AbsAgentAction AbsEntp = new AbsAgentAction(pacaOntology.MODIFICAPRACTICA);
                ModificaFicheroIN mfi = new ModificaFicheroIN();
                mfi.setTest(ts);
                mfi.setFicheroIN(fi);
                mfi.setPractica(pt);
                mfi.setCaso(ultimoCaso);

                Action act = new Action();
                act.setAction(mfi);
                act.setActor(receiver);

                //AbsEntp.set(pacaOntology.PRACTICA, Abspt);


                getContentManager().fillContent(solicitud, act);
                addBehaviour(new RecibeMensajes(myAgent, tes1));
                send(solicitud);
            } catch (CodecException ex) {
                Logger.getLogger(Interfaz.class.getName()).log(Level.SEVERE, null, ex);
            } catch (OntologyException ex) {
                Logger.getLogger(Interfaz.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    public class ModificarFicherosOUT extends OneShotBehaviour {

        private Resultado tes1;
        private FicheroOUT fo;

        public ModificarFicherosOUT(Agent _a, Resultado tes, FicheroOUT fo) {
            super(_a);
            this.tes1 = tes;
            this.fo = fo;
        }

        public void action() {
            try {
                /*ultimaPractica = IdPractica;
                Practica p = EncontrarPractica();
                tes1.setResultado(p);*/
                AID receiver = new AID(gestorPracticas, AID.ISLOCALNAME);
                ACLMessage solicitud = new ACLMessage(ACLMessage.REQUEST);
                solicitud.addReceiver(receiver);
                solicitud.setLanguage(codec.getName());
                solicitud.setOntology(pacaOntology.NAME);


                Practica pt = ultimaPractica;
                Test ts = ultimoTest;



                //AbsConcept Abspt = (AbsConcept) PACAOntology.fromObject(pt);
                //AbsAgentAction AbsEntp = new AbsAgentAction(pacaOntology.MODIFICAPRACTICA);
                ModificaFicheroOUT mfo = new ModificaFicheroOUT();
                mfo.setTest(ts);
                mfo.setFicheroOUT(fo);
                mfo.setPractica(pt);
                mfo.setCaso(ultimoCaso);

                Action act = new Action();
                act.setAction(mfo);
                act.setActor(receiver);

                //AbsEntp.set(pacaOntology.PRACTICA, Abspt);


                getContentManager().fillContent(solicitud, act);
                addBehaviour(new RecibeMensajes(myAgent, tes1));
                send(solicitud);
            } catch (CodecException ex) {
                Logger.getLogger(Interfaz.class.getName()).log(Level.SEVERE, null, ex);
            } catch (OntologyException ex) {
                Logger.getLogger(Interfaz.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    public class CrearPractica extends OneShotBehaviour {

        private Resultado tes1;
        private Practica practica;

        public CrearPractica(Agent _a, Resultado tes, Practica practica) {
            super(_a);
            this.tes1 = tes;
            this.practica = practica;
        }

        public void action() {
            try {

                AID receiver = new AID(gestorPracticas, AID.ISLOCALNAME);
                ACLMessage solicitud = new ACLMessage(ACLMessage.REQUEST);
                solicitud.addReceiver(receiver);
                solicitud.setLanguage(codec.getName());
                solicitud.setOntology(pacaOntology.NAME);


                CreaPractica cp = new CreaPractica();
                cp.setPractica(practica);

                Action act = new Action();
                act.setAction(cp);
                act.setActor(receiver);


                getContentManager().fillContent(solicitud, act);
                addBehaviour(new RecibeMensajes(myAgent, tes1));
                send(solicitud);
            } catch (CodecException ex) {
                Logger.getLogger(Interfaz.class.getName()).log(Level.SEVERE, null, ex);
            } catch (OntologyException ex) {
                Logger.getLogger(Interfaz.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    public class CrearTest extends OneShotBehaviour {

        private Resultado tes1;
        private Test test;

        public CrearTest(Agent _a, Resultado tes, Test test) {
            super(_a);
            this.tes1 = tes;
            this.test = test;
        }

        public void action() {
            try {

                AID receiver = new AID(gestorPracticas, AID.ISLOCALNAME);
                ACLMessage solicitud = new ACLMessage(ACLMessage.REQUEST);
                solicitud.addReceiver(receiver);
                solicitud.setLanguage(codec.getName());
                solicitud.setOntology(pacaOntology.NAME);


                CreaTest ct = new CreaTest();
                ct.setPractica(ultimaPractica);
                ct.setTest(test);

                Action act = new Action();
                act.setAction(ct);
                act.setActor(receiver);


                getContentManager().fillContent(solicitud, act);
                addBehaviour(new RecibeMensajes(myAgent, tes1));
                send(solicitud);
            } catch (CodecException ex) {
                Logger.getLogger(Interfaz.class.getName()).log(Level.SEVERE, null, ex);
            } catch (OntologyException ex) {
                Logger.getLogger(Interfaz.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    public class CrearFicheroPropio extends OneShotBehaviour {

        private Resultado tes1;
        private FicheroPropio fp;

        public CrearFicheroPropio(Agent _a, Resultado tes, FicheroPropio fp) {
            super(_a);
            this.tes1 = tes;
            this.fp = fp;
        }

        public void action() {
            try {
                /*ultimaPractica = IdPractica;
                Practica p = EncontrarPractica();
                tes1.setResultado(p);*/
                AID receiver = new AID(gestorPracticas, AID.ISLOCALNAME);
                ACLMessage solicitud = new ACLMessage(ACLMessage.REQUEST);
                solicitud.addReceiver(receiver);
                solicitud.setLanguage(codec.getName());
                solicitud.setOntology(pacaOntology.NAME);


                Practica pt = ultimaPractica;
                Test ts = ultimoTest;



                //AbsConcept Abspt = (AbsConcept) PACAOntology.fromObject(pt);
                //AbsAgentAction AbsEntp = new AbsAgentAction(pacaOntology.MODIFICAPRACTICA);
                CreaFicheroPropio mfp = new CreaFicheroPropio();
                mfp.setTest(ts);
                mfp.setFicheroPropio(fp);
                mfp.setPractica(pt);

                Action act = new Action();
                act.setAction(mfp);
                act.setActor(receiver);

                //AbsEntp.set(pacaOntology.PRACTICA, Abspt);


                getContentManager().fillContent(solicitud, act);
                addBehaviour(new RecibeMensajes(myAgent, tes1));
                send(solicitud);
            } catch (CodecException ex) {
                Logger.getLogger(Interfaz.class.getName()).log(Level.SEVERE, null, ex);
            } catch (OntologyException ex) {
                Logger.getLogger(Interfaz.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    public class CrearFicheroAlumno extends OneShotBehaviour {

        private Resultado tes1;
        private FicheroAlumno fa;

        public CrearFicheroAlumno(Agent _a, Resultado tes, FicheroAlumno fa) {
            super(_a);
            this.tes1 = tes;
            this.fa = fa;
        }

        public void action() {
            try {
                /*ultimaPractica = IdPractica;
                Practica p = EncontrarPractica();
                tes1.setResultado(p);*/
                AID receiver = new AID(gestorPracticas, AID.ISLOCALNAME);
                ACLMessage solicitud = new ACLMessage(ACLMessage.REQUEST);
                solicitud.addReceiver(receiver);
                solicitud.setLanguage(codec.getName());
                solicitud.setOntology(pacaOntology.NAME);


                Practica pt = ultimaPractica;
                Test ts = ultimoTest;



                //AbsConcept Abspt = (AbsConcept) PACAOntology.fromObject(pt);
                //AbsAgentAction AbsEntp = new AbsAgentAction(pacaOntology.MODIFICAPRACTICA);
                CreaFicheroAlumno mfp = new CreaFicheroAlumno();
                mfp.setTest(ts);
                mfp.setFicheroAlumno(fa);
                mfp.setPractica(pt);

                Action act = new Action();
                act.setAction(mfp);
                act.setActor(receiver);

                //AbsEntp.set(pacaOntology.PRACTICA, Abspt);


                getContentManager().fillContent(solicitud, act);
                addBehaviour(new RecibeMensajes(myAgent, tes1));
                send(solicitud);
            } catch (CodecException ex) {
                Logger.getLogger(Interfaz.class.getName()).log(Level.SEVERE, null, ex);
            } catch (OntologyException ex) {
                Logger.getLogger(Interfaz.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    public class CrearCaso extends OneShotBehaviour {

        private Resultado tes1;
        private Caso ca;

        public CrearCaso(Agent _a, Resultado tes, Caso ca) {
            super(_a);
            this.tes1 = tes;
            this.ca = ca;
        }

        public void action() {
            try {
                /*ultimaPractica = IdPractica;
                Practica p = EncontrarPractica();
                tes1.setResultado(p);*/
                AID receiver = new AID(gestorPracticas, AID.ISLOCALNAME);
                ACLMessage solicitud = new ACLMessage(ACLMessage.REQUEST);
                solicitud.addReceiver(receiver);
                solicitud.setLanguage(codec.getName());
                solicitud.setOntology(pacaOntology.NAME);


                Practica pt = ultimaPractica;
                Test ts = ultimoTest;



                //AbsConcept Abspt = (AbsConcept) PACAOntology.fromObject(pt);
                //AbsAgentAction AbsEntp = new AbsAgentAction(pacaOntology.MODIFICAPRACTICA);
                CreaCaso mfp = new CreaCaso();
                mfp.setTest(ts);
                mfp.setCaso(ca);
                mfp.setPractica(pt);

                Action act = new Action();
                act.setAction(mfp);
                act.setActor(receiver);

                //AbsEntp.set(pacaOntology.PRACTICA, Abspt);


                getContentManager().fillContent(solicitud, act);
                addBehaviour(new RecibeMensajes(myAgent, tes1));
                send(solicitud);
            } catch (CodecException ex) {
                Logger.getLogger(Interfaz.class.getName()).log(Level.SEVERE, null, ex);
            } catch (OntologyException ex) {
                Logger.getLogger(Interfaz.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    public class CrearFicheroIN extends OneShotBehaviour {

        private Resultado tes1;
        private FicheroIN fi;

        public CrearFicheroIN(Agent _a, Resultado tes, FicheroIN fi) {
            super(_a);
            this.tes1 = tes;
            this.fi = fi;
        }

        public void action() {
            try {
                /*ultimaPractica = IdPractica;
                Practica p = EncontrarPractica();
                tes1.setResultado(p);*/
                AID receiver = new AID(gestorPracticas, AID.ISLOCALNAME);
                ACLMessage solicitud = new ACLMessage(ACLMessage.REQUEST);
                solicitud.addReceiver(receiver);
                solicitud.setLanguage(codec.getName());
                solicitud.setOntology(pacaOntology.NAME);


                Practica pt = ultimaPractica;
                Test ts = ultimoTest;
                Caso ca = ultimoCaso;



                //AbsConcept Abspt = (AbsConcept) PACAOntology.fromObject(pt);
                //AbsAgentAction AbsEntp = new AbsAgentAction(pacaOntology.MODIFICAPRACTICA);
                CreaFicheroIN mfp = new CreaFicheroIN();
                mfp.setTest(ts);
                mfp.setCaso(ca);
                mfp.setPractica(pt);
                mfp.setFicheroIN(fi);

                Action act = new Action();
                act.setAction(mfp);
                act.setActor(receiver);

                //AbsEntp.set(pacaOntology.PRACTICA, Abspt);


                getContentManager().fillContent(solicitud, act);
                addBehaviour(new RecibeMensajes(myAgent, tes1));
                send(solicitud);
            } catch (CodecException ex) {
                Logger.getLogger(Interfaz.class.getName()).log(Level.SEVERE, null, ex);
            } catch (OntologyException ex) {
                Logger.getLogger(Interfaz.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    public class CrearFicheroOUT extends OneShotBehaviour {

        private Resultado tes1;
        private FicheroOUT fo;

        public CrearFicheroOUT(Agent _a, Resultado tes, FicheroOUT fo) {
            super(_a);
            this.tes1 = tes;
            this.fo = fo;
        }

        public void action() {
            try {
                /*ultimaPractica = IdPractica;
                Practica p = EncontrarPractica();
                tes1.setResultado(p);*/
                AID receiver = new AID(gestorPracticas, AID.ISLOCALNAME);
                ACLMessage solicitud = new ACLMessage(ACLMessage.REQUEST);
                solicitud.addReceiver(receiver);
                solicitud.setLanguage(codec.getName());
                solicitud.setOntology(pacaOntology.NAME);


                Practica pt = ultimaPractica;
                Test ts = ultimoTest;
                Caso ca = ultimoCaso;



                //AbsConcept Abspt = (AbsConcept) PACAOntology.fromObject(pt);
                //AbsAgentAction AbsEntp = new AbsAgentAction(pacaOntology.MODIFICAPRACTICA);
                CreaFicheroOUT mfp = new CreaFicheroOUT();
                mfp.setTest(ts);
                mfp.setCaso(ca);
                mfp.setPractica(pt);
                mfp.setFicheroOUT(fo);

                Action act = new Action();
                act.setAction(mfp);
                act.setActor(receiver);

                //AbsEntp.set(pacaOntology.PRACTICA, Abspt);


                getContentManager().fillContent(solicitud, act);
                addBehaviour(new RecibeMensajes(myAgent, tes1));
                send(solicitud);
            } catch (CodecException ex) {
                Logger.getLogger(Interfaz.class.getName()).log(Level.SEVERE, null, ex);
            } catch (OntologyException ex) {
                Logger.getLogger(Interfaz.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    public class EliminarPractica extends OneShotBehaviour {

        private Resultado tes1;
        private Practica practica;

        public EliminarPractica(Agent _a, Resultado tes, Practica practica) {
            super(_a);
            this.tes1 = tes;
            this.practica = practica;
        }

        public void action() {
            try {

                AID receiver = new AID(gestorPracticas, AID.ISLOCALNAME);
                ACLMessage solicitud = new ACLMessage(ACLMessage.REQUEST);
                solicitud.addReceiver(receiver);
                solicitud.setLanguage(codec.getName());
                solicitud.setOntology(pacaOntology.NAME);


                EliminaPractica cp = new EliminaPractica();
                cp.setPractica(practica);

                Action act = new Action();
                act.setAction(cp);
                act.setActor(receiver);


                getContentManager().fillContent(solicitud, act);
                addBehaviour(new RecibeMensajes(myAgent, tes1));
                send(solicitud);
            } catch (CodecException ex) {
                Logger.getLogger(Interfaz.class.getName()).log(Level.SEVERE, null, ex);
            } catch (OntologyException ex) {
                Logger.getLogger(Interfaz.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    public class EliminarTest extends OneShotBehaviour {

        private Resultado tes1;
        private Test test;

        public EliminarTest(Agent _a, Resultado tes, Test test) {
            super(_a);
            this.tes1 = tes;
            this.test = test;
        }

        public void action() {
            try {

                AID receiver = new AID(gestorPracticas, AID.ISLOCALNAME);
                ACLMessage solicitud = new ACLMessage(ACLMessage.REQUEST);
                solicitud.addReceiver(receiver);
                solicitud.setLanguage(codec.getName());
                solicitud.setOntology(pacaOntology.NAME);


                EliminaTest ct = new EliminaTest();
                ct.setPractica(ultimaPractica);
                ct.setTest(test);

                Action act = new Action();
                act.setAction(ct);
                act.setActor(receiver);


                getContentManager().fillContent(solicitud, act);
                addBehaviour(new RecibeMensajes(myAgent, tes1));
                send(solicitud);
            } catch (CodecException ex) {
                Logger.getLogger(Interfaz.class.getName()).log(Level.SEVERE, null, ex);
            } catch (OntologyException ex) {
                Logger.getLogger(Interfaz.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    public class EliminarFicheroPropio extends OneShotBehaviour {

        private Resultado tes1;
        private FicheroPropio fp;

        public EliminarFicheroPropio(Agent _a, Resultado tes, FicheroPropio fp) {
            super(_a);
            this.tes1 = tes;
            this.fp = fp;
        }

        public void action() {
            try {
                /*ultimaPractica = IdPractica;
                Practica p = EncontrarPractica();
                tes1.setResultado(p);*/
                AID receiver = new AID(gestorPracticas, AID.ISLOCALNAME);
                ACLMessage solicitud = new ACLMessage(ACLMessage.REQUEST);
                solicitud.addReceiver(receiver);
                solicitud.setLanguage(codec.getName());
                solicitud.setOntology(pacaOntology.NAME);


                Practica pt = ultimaPractica;
                Test ts = ultimoTest;



                //AbsConcept Abspt = (AbsConcept) PACAOntology.fromObject(pt);
                //AbsAgentAction AbsEntp = new AbsAgentAction(pacaOntology.MODIFICAPRACTICA);
                EliminaFicheroPropio mfp = new EliminaFicheroPropio();
                mfp.setTest(ts);
                mfp.setFicheroPropio(fp);
                mfp.setPractica(pt);

                Action act = new Action();
                act.setAction(mfp);
                act.setActor(receiver);

                //AbsEntp.set(pacaOntology.PRACTICA, Abspt);


                getContentManager().fillContent(solicitud, act);
                addBehaviour(new RecibeMensajes(myAgent, tes1));
                send(solicitud);
            } catch (CodecException ex) {
                Logger.getLogger(Interfaz.class.getName()).log(Level.SEVERE, null, ex);
            } catch (OntologyException ex) {
                Logger.getLogger(Interfaz.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    public class EliminarCaso extends OneShotBehaviour {

        private Resultado tes1;
        private Caso ca;

        public EliminarCaso(Agent _a, Resultado tes, Caso ca) {
            super(_a);
            this.tes1 = tes;
            this.ca = ca;
        }

        public void action() {
            try {
                /*ultimaPractica = IdPractica;
                Practica p = EncontrarPractica();
                tes1.setResultado(p);*/
                AID receiver = new AID(gestorPracticas, AID.ISLOCALNAME);
                ACLMessage solicitud = new ACLMessage(ACLMessage.REQUEST);
                solicitud.addReceiver(receiver);
                solicitud.setLanguage(codec.getName());
                solicitud.setOntology(pacaOntology.NAME);


                Practica pt = ultimaPractica;
                Test ts = ultimoTest;



                //AbsConcept Abspt = (AbsConcept) PACAOntology.fromObject(pt);
                //AbsAgentAction AbsEntp = new AbsAgentAction(pacaOntology.MODIFICAPRACTICA);
                EliminaCaso mfp = new EliminaCaso();
                mfp.setTest(ts);
                mfp.setCaso(ca);
                mfp.setPractica(pt);

                Action act = new Action();
                act.setAction(mfp);
                act.setActor(receiver);

                //AbsEntp.set(pacaOntology.PRACTICA, Abspt);


                getContentManager().fillContent(solicitud, act);
                addBehaviour(new RecibeMensajes(myAgent, tes1));
                send(solicitud);
            } catch (CodecException ex) {
                Logger.getLogger(Interfaz.class.getName()).log(Level.SEVERE, null, ex);
            } catch (OntologyException ex) {
                Logger.getLogger(Interfaz.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    public class EliminarFicheroAlumno extends OneShotBehaviour {

        private Resultado tes1;
        private FicheroAlumno fa;

        public EliminarFicheroAlumno(Agent _a, Resultado tes, FicheroAlumno fa) {
            super(_a);
            this.tes1 = tes;
            this.fa = fa;
        }

        public void action() {
            try {

                AID receiver = new AID(gestorPracticas, AID.ISLOCALNAME);
                ACLMessage solicitud = new ACLMessage(ACLMessage.REQUEST);
                solicitud.addReceiver(receiver);
                solicitud.setLanguage(codec.getName());
                solicitud.setOntology(pacaOntology.NAME);


                Practica pt = ultimaPractica;
                Test ts = ultimoTest;


                EliminaFicheroAlumno mfp = new EliminaFicheroAlumno();
                mfp.setTest(ts);
                mfp.setFicheroAlumno(fa);
                mfp.setPractica(pt);

                Action act = new Action();
                act.setAction(mfp);
                act.setActor(receiver);

                getContentManager().fillContent(solicitud, act);
                addBehaviour(new RecibeMensajes(myAgent, tes1));
                send(solicitud);
            } catch (CodecException ex) {
                Logger.getLogger(Interfaz.class.getName()).log(Level.SEVERE, null, ex);
            } catch (OntologyException ex) {
                Logger.getLogger(Interfaz.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    public class EliminarFicheroIN extends OneShotBehaviour {

        private Resultado tes1;
        private FicheroIN fi;

        public EliminarFicheroIN(Agent _a, Resultado tes, FicheroIN fi) {
            super(_a);
            this.tes1 = tes;
            this.fi = fi;
        }

        public void action() {
            try {

                AID receiver = new AID(gestorPracticas, AID.ISLOCALNAME);
                ACLMessage solicitud = new ACLMessage(ACLMessage.REQUEST);
                solicitud.addReceiver(receiver);
                solicitud.setLanguage(codec.getName());
                solicitud.setOntology(pacaOntology.NAME);


                Practica pt = ultimaPractica;
                Test ts = ultimoTest;
                Caso ca = ultimoCaso;


                EliminaFicheroIN mfp = new EliminaFicheroIN();
                mfp.setTest(ts);
                mfp.setCaso(ca);
                mfp.setPractica(pt);
                mfp.setFicheroIN(fi);

                Action act = new Action();
                act.setAction(mfp);
                act.setActor(receiver);



                getContentManager().fillContent(solicitud, act);
                addBehaviour(new RecibeMensajes(myAgent, tes1));
                send(solicitud);
            } catch (CodecException ex) {
                Logger.getLogger(Interfaz.class.getName()).log(Level.SEVERE, null, ex);
            } catch (OntologyException ex) {
                Logger.getLogger(Interfaz.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    public class EliminarFicheroOUT extends OneShotBehaviour {

        private Resultado tes1;
        private FicheroOUT fo;

        public EliminarFicheroOUT(Agent _a, Resultado tes, FicheroOUT fo) {
            super(_a);
            this.tes1 = tes;
            this.fo = fo;
        }

        public void action() {
            try {

                AID receiver = new AID(gestorPracticas, AID.ISLOCALNAME);
                ACLMessage solicitud = new ACLMessage(ACLMessage.REQUEST);
                solicitud.addReceiver(receiver);
                solicitud.setLanguage(codec.getName());
                solicitud.setOntology(pacaOntology.NAME);


                Practica pt = ultimaPractica;
                Test ts = ultimoTest;
                Caso ca = ultimoCaso;


                EliminaFicheroOUT mfp = new EliminaFicheroOUT();
                mfp.setTest(ts);
                mfp.setCaso(ca);
                mfp.setPractica(pt);
                mfp.setFicheroOUT(fo);

                Action act = new Action();
                act.setAction(mfp);
                act.setActor(receiver);



                getContentManager().fillContent(solicitud, act);
                addBehaviour(new RecibeMensajes(myAgent, tes1));
                send(solicitud);
            } catch (CodecException ex) {
                Logger.getLogger(Interfaz.class.getName()).log(Level.SEVERE, null, ex);
            } catch (OntologyException ex) {
                Logger.getLogger(Interfaz.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    /**************COMPORTAMIENTOS PARA COPIAR***********/
    public class CopiarTest extends OneShotBehaviour {

        private Resultado tes1;
        private Test test;
        private Practica practica;
        private Test testACopiar;

        public CopiarTest(Agent _a, Resultado tes, Practica practica, Test test, Test testACopiar) {
            super(_a);
            this.tes1 = tes;
            this.test = test;
            this.practica = practica;
            this.testACopiar = testACopiar;
        }

        public void action() {
            try {

                AID receiver = new AID(gestorPracticas, AID.ISLOCALNAME);
                ACLMessage solicitud = new ACLMessage(ACLMessage.REQUEST);
                solicitud.addReceiver(receiver);
                solicitud.setLanguage(codec.getName());
                solicitud.setOntology(pacaOntology.NAME);


                CopiaTest cp = new CopiaTest();
                cp.setPractica(practica);
                cp.setCopyPractica(ultimaPractica);
                cp.setTest(testACopiar);
                cp.setCopyTest(test);

                Action act = new Action();
                act.setAction(cp);
                act.setActor(receiver);

                getContentManager().fillContent(solicitud, act);
                addBehaviour(new RecibeMensajes(myAgent, tes1));
                send(solicitud);
            } catch (CodecException ex) {
                Logger.getLogger(Interfaz.class.getName()).log(Level.SEVERE, null, ex);
            } catch (OntologyException ex) {
                Logger.getLogger(Interfaz.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    public class CopiarFicheroPropio extends OneShotBehaviour {

        private Resultado tes1;
        private FicheroPropio fp;

        public CopiarFicheroPropio(Agent _a, Resultado tes, FicheroPropio fp) {
            super(_a);
            this.tes1 = tes;
            this.fp = fp;
        }

        public void action() {
            try {

                AID receiver = new AID(gestorPracticas, AID.ISLOCALNAME);
                ACLMessage solicitud = new ACLMessage(ACLMessage.REQUEST);
                solicitud.addReceiver(receiver);
                solicitud.setLanguage(codec.getName());
                solicitud.setOntology(pacaOntology.NAME);


                CopiaFicheroPropio cp = new CopiaFicheroPropio();
                cp.setCopyPractica(ultimaPractica);
                cp.setCopyTest(ultimoTest);
                cp.setCopyFicheroPropio(fp);

                Action act = new Action();
                act.setAction(cp);
                act.setActor(receiver);

                getContentManager().fillContent(solicitud, act);
                addBehaviour(new RecibeMensajes(myAgent, tes1));
                send(solicitud);
            } catch (CodecException ex) {
                Logger.getLogger(Interfaz.class.getName()).log(Level.SEVERE, null, ex);
            } catch (OntologyException ex) {
                Logger.getLogger(Interfaz.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    public class CopiarFicheroAlumno extends OneShotBehaviour {

        private Resultado tes1;
        private FicheroAlumno fp;

        public CopiarFicheroAlumno(Agent _a, Resultado tes, FicheroAlumno fp) {
            super(_a);
            this.tes1 = tes;
            this.fp = fp;
        }

        public void action() {
            try {

                AID receiver = new AID(gestorPracticas, AID.ISLOCALNAME);
                ACLMessage solicitud = new ACLMessage(ACLMessage.REQUEST);
                solicitud.addReceiver(receiver);
                solicitud.setLanguage(codec.getName());
                solicitud.setOntology(pacaOntology.NAME);


                CopiaFicheroAlumno cp = new CopiaFicheroAlumno();
                cp.setCopyPractica(ultimaPractica);
                cp.setCopyTest(ultimoTest);
                cp.setCopyFicheroAlumno(fp);

                Action act = new Action();
                act.setAction(cp);
                act.setActor(receiver);

                getContentManager().fillContent(solicitud, act);
                addBehaviour(new RecibeMensajes(myAgent, tes1));
                send(solicitud);
            } catch (CodecException ex) {
                Logger.getLogger(Interfaz.class.getName()).log(Level.SEVERE, null, ex);
            } catch (OntologyException ex) {
                Logger.getLogger(Interfaz.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    public class CopiarCaso extends OneShotBehaviour {

        private Resultado tes1;
        private Test test;
        private Practica practica;
        private Caso casoACopiar;
        private Caso caso;

        public CopiarCaso(Agent _a, Resultado tes, Practica practica, Test test, Caso casoACopiar, Caso caso) {
            super(_a);
            this.tes1 = tes;
            this.test = test;
            this.practica = practica;
            this.casoACopiar = casoACopiar;
            this.caso = caso;
        }

        public void action() {
            try {

                AID receiver = new AID(gestorPracticas, AID.ISLOCALNAME);
                ACLMessage solicitud = new ACLMessage(ACLMessage.REQUEST);
                solicitud.addReceiver(receiver);
                solicitud.setLanguage(codec.getName());
                solicitud.setOntology(pacaOntology.NAME);


                CopiaCaso cp = new CopiaCaso();
                cp.setPractica(practica);
                cp.setCopyPractica(ultimaPractica);
                cp.setTest(test);
                cp.setCopyTest(ultimoTest);
                cp.setCaso(casoACopiar);
                cp.setCopyCaso(caso);

                Action act = new Action();
                act.setAction(cp);
                act.setActor(receiver);

                getContentManager().fillContent(solicitud, act);
                addBehaviour(new RecibeMensajes(myAgent, tes1));
                send(solicitud);
            } catch (CodecException ex) {
                Logger.getLogger(Interfaz.class.getName()).log(Level.SEVERE, null, ex);
            } catch (OntologyException ex) {
                Logger.getLogger(Interfaz.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }


    public class CopiarFicheroIN extends OneShotBehaviour {

        private Resultado tes1;
        private FicheroIN fi;

        public CopiarFicheroIN(Agent _a, Resultado tes, FicheroIN fi) {
            super(_a);
            this.tes1 = tes;
            this.fi = fi;
        }

        public void action() {
            try {

                AID receiver = new AID(gestorPracticas, AID.ISLOCALNAME);
                ACLMessage solicitud = new ACLMessage(ACLMessage.REQUEST);
                solicitud.addReceiver(receiver);
                solicitud.setLanguage(codec.getName());
                solicitud.setOntology(pacaOntology.NAME);


                CopiaFicheroIN cp = new CopiaFicheroIN();
                cp.setCopyPractica(ultimaPractica);
                cp.setCopyTest(ultimoTest);
                cp.setCopyCaso(ultimoCaso);
                cp.setCopyFicheroIN(fi);

                Action act = new Action();
                act.setAction(cp);
                act.setActor(receiver);

                getContentManager().fillContent(solicitud, act);
                addBehaviour(new RecibeMensajes(myAgent, tes1));
                send(solicitud);
            } catch (CodecException ex) {
                Logger.getLogger(Interfaz.class.getName()).log(Level.SEVERE, null, ex);
            } catch (OntologyException ex) {
                Logger.getLogger(Interfaz.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

        public class CopiarFicheroOUT extends OneShotBehaviour {

        private Resultado tes1;
        private FicheroOUT fo;

        public CopiarFicheroOUT(Agent _a, Resultado tes, FicheroOUT fo) {
            super(_a);
            this.tes1 = tes;
            this.fo = fo;
        }

        public void action() {
            try {

                AID receiver = new AID(gestorPracticas, AID.ISLOCALNAME);
                ACLMessage solicitud = new ACLMessage(ACLMessage.REQUEST);
                solicitud.addReceiver(receiver);
                solicitud.setLanguage(codec.getName());
                solicitud.setOntology(pacaOntology.NAME);


                CopiaFicheroOUT cp = new CopiaFicheroOUT();
                cp.setCopyPractica(ultimaPractica);
                cp.setCopyTest(ultimoTest);
                cp.setCopyCaso(ultimoCaso);
                cp.setCopyFicheroOUT(fo);

                Action act = new Action();
                act.setAction(cp);
                act.setActor(receiver);

                getContentManager().fillContent(solicitud, act);
                addBehaviour(new RecibeMensajes(myAgent, tes1));
                send(solicitud);
            } catch (CodecException ex) {
                Logger.getLogger(Interfaz.class.getName()).log(Level.SEVERE, null, ex);
            } catch (OntologyException ex) {
                Logger.getLogger(Interfaz.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
}
