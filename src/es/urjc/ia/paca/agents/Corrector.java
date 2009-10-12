package es.urjc.ia.paca.agents;

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

import jade.content.onto.BasicOntology;
import es.urjc.ia.paca.ontology.Alumno;
import es.urjc.ia.paca.ontology.Corrige;
import es.urjc.ia.paca.ontology.EntregarPractica;
import es.urjc.ia.paca.ontology.EvaluacionPractica;
import es.urjc.ia.paca.ontology.FicheroFuentes;
import es.urjc.ia.paca.ontology.FormaGrupoCon;
import es.urjc.ia.paca.ontology.Interactua;
import es.urjc.ia.paca.ontology.Practica;
import es.urjc.ia.paca.ontology.ResultadoEvaluacion;
import es.urjc.ia.paca.ontology.Test;
import es.urjc.ia.paca.ontology.Tests;
import es.urjc.ia.paca.ontology.pacaOntology;
import es.urjc.ia.paca.ontology.Fichero.FuentesPrograma;
import es.urjc.ia.paca.ontology.FicheroPropio;
import es.urjc.ia.paca.ontology.Caso;
import es.urjc.ia.paca.ontology.Casos;
import es.urjc.ia.paca.ontology.FicheroIN;
import es.urjc.ia.paca.ontology.FicheroOUT;
import es.urjc.ia.paca.ontology.FicherosIN;
import es.urjc.ia.paca.ontology.FicherosOUT;
import es.urjc.ia.paca.util.AndBuilder;
import jade.content.abs.AbsVariable;
import java.io.BufferedWriter;
import java.io.PrintWriter;
import java.util.logging.Level;
import java.util.logging.Logger;

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
    private Ontology basicOntologia = BasicOntology.getInstance();
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
    public String gestorPracticas = "gestor";

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

    public class CorrigePractBehaviour extends Behaviour {

        private AbsContentElement mensaje;
        private ACLMessage respuesta;
        private String quien1;
        boolean done = false;

        public CorrigePractBehaviour(Agent _a, ACLMessage resp1, AbsContentElement msg1, String quien) {
            super(_a);
            this.mensaje = msg1;
            this.respuesta = resp1;
            this.quien1 = quien;
        }

        public void action() {
            try {


                Aleatorio rand = new Aleatorio();
                long retardo = rand.nextInt(tiempo_minimo, tiempo_maximo);

                Date fechaActual = new Date();
                Date fechaLimite = new Date(fechaActual.getTime() + retardo);

                while ((new Date()).before(fechaLimite)) {
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

                Test[] Te = ExtraeTestsPedidos(listTests);
                FuentesPrograma[] FP = ExtraeFuentesPedidos(listFich);

                Practica pract = cor1.getPractica();
                Alumno al = eva1.getAlumno();



                addBehaviour(new PedirFicherosPropiosBehaviour(this.myAgent, Te, pract, al, quien1));
                CrearCarpetas(pract, FP, Te, quien1);


                //A partir de aqui ya no seria necesario nada, pero lo dejo para que termine bien el programa
                EvaluacionPractica evaP = EnvioCorreccionAlumno(pract, FP, Te, al, quien1);

                String contenido = evaP.getTextoEvaluacion();

                ResultadoEvaluacion rEvap = new ResultadoEvaluacion();
                rEvap.setResultadoEvaluacionTexto(contenido);

                AbsConcept absEvap;

                absEvap = (AbsConcept) ontologia.fromObject(rEvap);

                AbsPredicate qrr = new AbsPredicate(SL1Vocabulary.EQUALS);
                qrr.set(SL1Vocabulary.EQUALS_LEFT, iotaPred);
                qrr.set(SL1Vocabulary.EQUALS_RIGHT, absEvap);

                getContentManager().fillContent(respuesta, qrr);
                myAgent.send(respuesta);

            } catch (IOException ex) {
                Logger.getLogger(Corrector.class.getName()).log(Level.SEVERE, null, ex);
            } catch (CodecException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();

            } catch (OntologyException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();

            }
            done = true;

        }

        public final boolean done() {
            return done;
        }
    }

    //public class ActualizaCorrecciones extends Behaviour{
    public class ActualizaCorrecciones extends CyclicBehaviour {

        boolean finalizado = false;

        public void action() {
            if ((new Date()).after(fechaLimite)) {
                numeroCorrecciones = numeroCorrecciones - numeroCorrecionesActual;
                //System.out.println(myAgent.getName()+": "+numeroCorrecciones);
                ActualizacionDF();
                numeroCorrecionesActual = numeroCorrecciones;
                //Actualizamos fechas
                fechaActual = new Date();
                fechaLimite = new Date(fechaActual.getTime() + intervalo);

            } else {
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

                //MENSAJES INFORM QUE VIENEN DEL CORRECTOR
                if (msg.getPerformative() == ACLMessage.INFORM) {
                    try {
                        AbsContentElement listaAbs = null;
                        listaAbs = getContentManager().extractAbsContent(msg);

                        AbsAggregate listaElementos = (AbsAggregate) listaAbs.getAbsObject(SLVocabulary.EQUALS_RIGHT);
                        //Cogemos el primer elemento de la lista
                        AbsConcept primerElem = (AbsConcept) listaElementos.get(0);
                        //Miramos el tipo del primer elemento
                        String tipo = primerElem.getTypeName();

                        //SACAMOS EL IRE IZQUIERDO PARA VER LA PRACTICA Y EL TEST AL QUE
                        //PERTENECE LOS FICHEROS PROPIOS QUE ME ENVIA
                        AbsIRE irePredicado = (AbsIRE) listaAbs.getAbsObject(SLVocabulary.EQUALS_LEFT);
                        AbsPredicate qall = (AbsPredicate) irePredicado.getProposition();
                        //USAMOS EL ANDBUILDER PARA DESCOMPONER EL IRE
                        AndBuilder predicado = new AndBuilder();
                        predicado.addPredicate(qall);
                        List<AbsPredicate> pretss = predicado.getPredicateList(pacaOntology.TESTS);
                        Iterator<AbsPredicate> itCor = pretss.iterator();
                        Tests tss = (Tests) ontologia.toObject(itCor.next());
                        Practica p = tss.getPractica();
                        Test t = tss.getTest();
                        List<AbsPredicate> preint = predicado.getPredicateList(pacaOntology.INTERACTUA);
                        Iterator<AbsPredicate> itCorI = preint.iterator();
                        Interactua intc = (Interactua) ontologia.toObject(itCorI.next());
                        AID interfaz = intc.getInterfaz();
                        String rutaInterfaz = interfaz.getLocalName();
                        Alumno al = intc.getAlumno();




                        if (tipo.equals(pacaOntology.FICHEROPROPIO)) {


                            CrearFicherosPropios(p, t, listaElementos, rutaInterfaz);
                            addBehaviour(new PedirCasosBehaviour(this.myAgent, p, t, al, rutaInterfaz));

                        } else {
                            if (tipo.equals(pacaOntology.CASO)) {


                                CrearCasos(p, t, listaElementos, rutaInterfaz);
                                addBehaviour(new PedirFicherosINBehaviour(this.myAgent, listaElementos, p, t, al, rutaInterfaz));
                                addBehaviour(new PedirFicherosOUTBehaviour(this.myAgent, listaElementos, p, t, al, rutaInterfaz));
                            } else {


                                if (tipo.equals(pacaOntology.FICHEROIN)) {
                                    List<AbsPredicate> precas = predicado.getPredicateList(pacaOntology.FICHEROSIN);
                                    Iterator<AbsPredicate> itCorC = precas.iterator();
                                    FicherosIN cas = (FicherosIN) ontologia.toObject(itCorC.next());
                                    Caso ca = cas.getCaso();
                                    CrearFicherosIN(p, t, ca, listaElementos, rutaInterfaz);
                                } else {
                                    List<AbsPredicate> precas = predicado.getPredicateList(pacaOntology.FICHEROSOUT);
                                    Iterator<AbsPredicate> itCorC = precas.iterator();
                                    FicherosOUT cas = (FicherosOUT) ontologia.toObject(itCorC.next());
                                    Caso ca = cas.getCaso();
                                    CrearFicherosOUT(p, t, ca, listaElementos, rutaInterfaz);
                                }

                            }
                        }
                    } catch (IOException ex) {
                        Logger.getLogger(Corrector.class.getName()).log(Level.SEVERE, null, ex);
                    } catch (CodecException ex) {
                        Logger.getLogger(Corrector.class.getName()).log(Level.SEVERE, null, ex);
                    } catch (OntologyException ex) {
                        Logger.getLogger(Corrector.class.getName()).log(Level.SEVERE, null, ex);
                    }
                } else {
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
                            reply.setPerformative(ACLMessage.QUERY_REF);

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
                                } else {

                                    if (requestedInfoName.equals(SL2Vocabulary.IOTA)) {
                                        // If QUERY-REF (iota
                                        // --> EnvioCorrecionAlumno <--------------------------------------------------
                                        //printError("Dentro de EnvioCorrecionAlumno");

                                        addBehaviour(new CorrigePractBehaviour(this.myAgent, reply, l_in, msg.getSender().getLocalName()));

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

        int ultimaPos = nombreC.length() - 1;

        char ultimaletra = nombreC.charAt(ultimaPos);

        if (java.lang.Character.toString(ultimaletra).equals("x")) {
            tiempo_maximo = 5000;
            tiempo_minimo = 1000;
        } else {

            if (java.lang.Character.getNumericValue(ultimaletra) % 2 != 0) {
                tiempo_maximo = 2000;
                tiempo_minimo = 1000;
            } else { // Si el nombre termina en par...
                tiempo_maximo = 5000;
                tiempo_minimo = 3000;
            }
        }


        // Register the codec for the SL0 language
        getContentManager().registerLanguage(codec);

        // Register the ontology used by this application
        getContentManager().registerOntology(ontologia);

        RegistroDF();

        // Create the FSMEBehaviour
        CorrectorBehaviour Principal = new CorrectorBehaviour(this);

        // Add the behaviour to the agent
        addBehaviour(Principal);
        //addBehaviour(new ActualizaCorrecciones());
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

            for (Test t : test) {
                text_eva += "<Test identificador=\"" + t.getId() + "\">" +
                        "<Descripcion><![CDATA[" + t.getDescripcion() + "]]></Descripcion>" +
                        "<EvaluacionTest codigoEvaluacionTest=\"terminacion_incorrecta\">" +
                        "</EvaluacionTest>";

                for (FuentesPrograma f : fp) {
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
                aux = (Tests) ontologia.toObject(it.next());
                testAux[i] = aux.getTest();
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
                aux = (FicheroFuentes) ontologia.toObject(it.next());
                fuentesAux[i] = aux.getFuentesPrograma();
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
            i = nextInt();
            i = inferior + (Math.abs(i) % (superior - inferior + 1));
            return (i);
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
    private void ActualizacionDF() {
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


    /*----------METODOS PARA PEDIR COSAS AL GESTOR--------------*/
    /*-----------Pide los ficheros propios del Test--------------*/
    class PedirFicherosPropiosBehaviour extends OneShotBehaviour {

        private Test[] t;
        private Practica p;
        private Alumno al;
        private String interfaz;

        public PedirFicherosPropiosBehaviour(Agent a, Test[] t, Practica p, Alumno al, String interfaz) {
            super(a);
            this.t = t;
            this.p = p;
            this.al = al;
            this.interfaz = interfaz;

        }

        public void action() {

            try {
                //CREAMOS EL PREDICADO INTERACTUA PARA SABER LOS DATOS DEL ALUMNO
                //Y EL NOMBRE EL AID DEL INTERFAZ QUE HA PEDIDO LA CORRECCION
                AbsConcept absal = (AbsConcept) ontologia.fromObject(al);
                AID aidInterfaz = new AID(interfaz, AID.ISLOCALNAME);
                AbsConcept absaid = (AbsConcept) basicOntologia.fromObject(aidInterfaz);

                AbsPredicate absint = new AbsPredicate(pacaOntology.INTERACTUA);
                absint.set(pacaOntology.INTERFAZ, absaid);
                absint.set(pacaOntology.ALUMNO, absal);

                for (int i = 0; i < t.length; i++) {
                    //CREAMOS EL MENSAJE
                    ACLMessage msg = new ACLMessage(ACLMessage.QUERY_REF);
                    AID receiver = new AID(gestorPracticas, AID.ISLOCALNAME);

                    msg.setSender(getAID());
                    msg.addReceiver(receiver);
                    msg.setLanguage(codec.getName());
                    msg.setOntology(ontologia.getName());



                    //CREAMOS EL PREDICADO TESTS PARA SABER CUAL ES LA PRACTICA
                    //Y EL TEST DE LOS FICHEROS PROPIOS QUE VAMOS A PEDIR
                    AbsConcept abspract = (AbsConcept) ontologia.fromObject(p);
                    AbsConcept absts = (AbsConcept) ontologia.fromObject(t[i]);
                    AbsPredicate abstss = new AbsPredicate(pacaOntology.TESTS);
                    abstss.set(pacaOntology.TEST, absts);
                    abstss.set(pacaOntology.PRACTICA, abspract);


                    //CREAMOS EL PREDICADO PARA PEDIR LOS FICHEROS PROPIOS
                    FicheroPropio fp = new FicheroPropio("?FicheroPropio");
                    AbsConcept absfp = (AbsConcept) ontologia.fromObject(fp);
                    AbsPredicate absfps = new AbsPredicate(pacaOntology.FICHEROSPROPIOS);
                    absfps.set(pacaOntology.TEST, absts);
                    absfps.set(pacaOntology.FICHEROPROPIO, absfp);

                    //CREAMOS UN ANDBUILDER PARA UNIR LOS DOS PREDICADOS ANTERIORES
                    AndBuilder constructor = new AndBuilder();
                    constructor.addPredicate(abstss);
                    constructor.addPredicate(absfps);
                    constructor.addPredicate(absint);

                    //CREAMOS EL IRE CON LA VARIABLE X Y ENVIAMOS TODO
                    AbsVariable x =
                            new AbsVariable("ficheroPropio", pacaOntology.FICHEROPROPIO);
                    AbsIRE qrall = new AbsIRE(SL2Vocabulary.ALL);
                    qrall.setVariable(x);
                    qrall.setProposition(constructor.getAnd());
                    System.out.println("[" + getLocalName() + "] Le pido todos los ficheros propios:");
                    System.out.println("[" + getLocalName() + "] Enviando mensaje...");
                    getContentManager().fillContent(msg, qrall);
                    myAgent.send(msg);
                    System.out.println("[" + getLocalName() + "] Mensaje enviado.");
                }

            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }

    /*------------Pide los casos del test----------------------------*/
    private class PedirCasosBehaviour extends OneShotBehaviour {

        private Practica pract;
        private Test ts;
        private Alumno al;
        private String interfaz;

        public PedirCasosBehaviour(Agent a, Practica pract, Test ts, Alumno al, String interfaz) {
            super(a);
            this.pract = pract;
            this.ts = ts;
            this.al = al;
            this.interfaz = interfaz;
        }

        @Override
        public void action() {
            try {

                //CREAMOS EL MENSAJE
                ACLMessage msg = new ACLMessage(ACLMessage.QUERY_REF);
                AID receiver = new AID(gestorPracticas, AID.ISLOCALNAME);

                msg.setSender(getAID());
                msg.addReceiver(receiver);
                msg.setLanguage(codec.getName());
                msg.setOntology(ontologia.getName());

                //CREAMOS EL PREDICADO TESTS PARA SABER CUAL ES LA PRACTICA
                //Y EL TEST DE LOS CASOS QUE VAMOS A PEDIR
                AbsConcept abspract = (AbsConcept) ontologia.fromObject(pract);
                AbsConcept absts = (AbsConcept) ontologia.fromObject(ts);
                AbsPredicate abstss = new AbsPredicate(pacaOntology.TESTS);
                abstss.set(pacaOntology.TEST, absts);
                abstss.set(pacaOntology.PRACTICA, abspract);
                //CREAMOS EL PREDICADO INTERACTUA PARA SABER LOS DATOS DEL ALUMNO
                //Y EL NOMBRE EL AID DEL INTERFAZ QUE HA PEDIDO LA CORRECCION
                AbsConcept absal = (AbsConcept) ontologia.fromObject(al);
                AID aidInterfaz = new AID(interfaz, AID.ISLOCALNAME);
                AbsConcept absaid = (AbsConcept) basicOntologia.fromObject(aidInterfaz);

                AbsPredicate absint = new AbsPredicate(pacaOntology.INTERACTUA);
                absint.set(pacaOntology.INTERFAZ, absaid);
                absint.set(pacaOntology.ALUMNO, absal);


                //CREAMOS EL PREDICADO PARA PEDIR LOS CASOS
                Caso ca = new Caso("?Caso");
                AbsConcept absca = (AbsConcept) ontologia.fromObject(ca);
                AbsPredicate abscss = new AbsPredicate(pacaOntology.CASOS);
                abscss.set(pacaOntology.TEST, absts);
                abscss.set(pacaOntology.CASO, absca);


                //CREAMOS UN ANDBUILDER PARA UNIR LOS DOS PREDICADOS ANTERIORES
                AndBuilder constructor = new AndBuilder();
                constructor.addPredicate(abstss);
                constructor.addPredicate(abscss);
                constructor.addPredicate(absint);

                //CREAMOS EL IRE CON LA VARIABLE X Y ENVIAMOS TODO
                AbsVariable x =
                        new AbsVariable("caso", pacaOntology.CASO);
                AbsIRE qrall = new AbsIRE(SL2Vocabulary.ALL);
                qrall.setVariable(x);
                qrall.setProposition(constructor.getAnd());
                System.out.println("[" + getLocalName() + "] Le pido todos los casos:");
                System.out.println("[" + getLocalName() + "] Enviando mensaje...");
                getContentManager().fillContent(msg, qrall);
                myAgent.send(msg);
                System.out.println("[" + getLocalName() + "] Mensaje enviado.");

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /*------------Pide los Ficheros IN del test----------------------------*/
    private class PedirFicherosINBehaviour extends OneShotBehaviour {

        AbsAggregate listaCasos;
        private Practica pract;
        private Test ts;
        private Alumno al;
        private String interfaz;

        public PedirFicherosINBehaviour(Agent a, AbsAggregate listaCasos, Practica pract, Test ts, Alumno al, String interfaz) {
            super(a);
            this.listaCasos = listaCasos;
            this.pract = pract;
            this.ts = ts;
            this.al = al;
            this.interfaz = interfaz;
        }

        @Override
        public void action() {
            try {
                //CREAMOS EL PREDICADO TESTS PARA SABER CUAL ES LA PRACTICA
                //Y EL TEST DE LOS FICHEROSIN QUE VAMOS A PEDIR
                AbsConcept abspract = (AbsConcept) ontologia.fromObject(pract);
                AbsConcept absts = (AbsConcept) ontologia.fromObject(ts);
                AbsPredicate abstss = new AbsPredicate(pacaOntology.TESTS);
                abstss.set(pacaOntology.TEST, absts);
                abstss.set(pacaOntology.PRACTICA, abspract);

                //CREAMOS EL PREDICADO INTERACTUA PARA SABER LOS DATOS DEL ALUMNO
                //Y EL NOMBRE EL AID DEL INTERFAZ QUE HA PEDIDO LA CORRECCION
                AbsConcept absal = (AbsConcept) ontologia.fromObject(al);
                AID aidInterfaz = new AID(interfaz, AID.ISLOCALNAME);
                AbsConcept absaid = (AbsConcept) basicOntologia.fromObject(aidInterfaz);

                AbsPredicate absint = new AbsPredicate(pacaOntology.INTERACTUA);
                absint.set(pacaOntology.INTERFAZ, absaid);
                absint.set(pacaOntology.ALUMNO, absal);


                //PEDIMOS LOS FICHEROSIN DE CADA CASO UNO POR UNO
                for (int i = 0; listaCasos.size() > i; i++) {
                    //CREAMOS EL MENSAJE
                    ACLMessage msg = new ACLMessage(ACLMessage.QUERY_REF);
                    AID receiver = new AID(gestorPracticas, AID.ISLOCALNAME);
                    msg.setSender(getAID());
                    msg.addReceiver(receiver);
                    msg.setLanguage(codec.getName());
                    msg.setOntology(ontologia.getName());

                    //CREAMOS EL PREDICADO PARA PEDIR LOS FICHEROSIN
                    FicheroIN fi = new FicheroIN("?FicheroIN");
                    Caso ca = (Caso) ontologia.toObject(listaCasos.get(i));
                    AbsConcept absfi = (AbsConcept) ontologia.fromObject(fi);
                    AbsConcept absca = (AbsConcept) ontologia.fromObject(ca);
                    AbsPredicate absfis = new AbsPredicate(pacaOntology.FICHEROSIN);
                    absfis.set(pacaOntology.FICHEROIN, absfi);
                    absfis.set(pacaOntology.CASO, absca);

                    //CREAMOS UN ANDBUILDER PARA UNIR LOS DOS PREDICADOS ANTERIORES
                    AndBuilder constructor = new AndBuilder();
                    constructor.addPredicate(abstss);
                    constructor.addPredicate(absfis);
                    constructor.addPredicate(absint);

                    //CREAMOS EL IRE CON LA VARIABLE X Y ENVIAMOS TODO
                    AbsVariable x =
                            new AbsVariable("ficheroIN", pacaOntology.FICHEROIN);
                    AbsIRE qrall = new AbsIRE(SL2Vocabulary.ALL);
                    qrall.setVariable(x);
                    qrall.setProposition(constructor.getAnd());
                    System.out.println("[" + getLocalName() + "] Le pido todos los FicherosIN:");
                    System.out.println("[" + getLocalName() + "] Enviando mensaje...");
                    getContentManager().fillContent(msg, qrall);
                    myAgent.send(msg);
                    System.out.println("[" + getLocalName() + "] Mensaje enviado.");

                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /*------------Pide los Ficheros OUT del test----------------------------*/
    private class PedirFicherosOUTBehaviour extends OneShotBehaviour {

        AbsAggregate listaCasos;
        private Practica pract;
        private Test ts;
        private Alumno al;
        private String interfaz;

        public PedirFicherosOUTBehaviour(Agent a, AbsAggregate listaCasos, Practica pract, Test ts, Alumno al, String interfaz) {
            super(a);
            this.listaCasos = listaCasos;
            this.pract = pract;
            this.ts = ts;
            this.al = al;
            this.interfaz = interfaz;
        }

        @Override
        public void action() {
            try {

                //CREAMOS EL PREDICADO TESTS PARA SABER CUAL ES LA PRACTICA
                //Y EL TEST DE LOS FICHEROSOUT QUE VAMOS A PEDIR
                AbsConcept abspract = (AbsConcept) ontologia.fromObject(pract);
                AbsConcept absts = (AbsConcept) ontologia.fromObject(ts);
                AbsPredicate abstss = new AbsPredicate(pacaOntology.TESTS);
                abstss.set(pacaOntology.TEST, absts);
                abstss.set(pacaOntology.PRACTICA, abspract);

                //CREAMOS EL PREDICADO INTERACTUA PARA SABER LOS DATOS DEL ALUMNO
                //Y EL NOMBRE EL AID DEL INTERFAZ QUE HA PEDIDO LA CORRECCION
                AbsConcept absal = (AbsConcept) ontologia.fromObject(al);
                AID aidInterfaz = new AID(interfaz, AID.ISLOCALNAME);
                AbsConcept absaid = (AbsConcept) basicOntologia.fromObject(aidInterfaz);

                AbsPredicate absint = new AbsPredicate(pacaOntology.INTERACTUA);
                absint.set(pacaOntology.INTERFAZ, absaid);
                absint.set(pacaOntology.ALUMNO, absal);

                //PEDIMOS LOS FICHEROS OUT DE CADA CASO UNO POR UNO
                for (int i = 0; listaCasos.size() > i; i++) {
                    //CREAMOS EL MENSAJE
                    ACLMessage msg = new ACLMessage(ACLMessage.QUERY_REF);
                    AID receiver = new AID(gestorPracticas, AID.ISLOCALNAME);
                    msg.setSender(getAID());
                    msg.addReceiver(receiver);
                    msg.setLanguage(codec.getName());
                    msg.setOntology(ontologia.getName());

                    //CREAMOS EL PREDICADO PARA PEDIR LOS FICHEROSOUT
                    FicheroOUT fo = new FicheroOUT("?FicheroOUT");
                    Caso ca = (Caso) ontologia.toObject(listaCasos.get(i));
                    AbsConcept absfo = (AbsConcept) ontologia.fromObject(fo);
                    AbsConcept absca = (AbsConcept) ontologia.fromObject(ca);
                    AbsPredicate absfos = new AbsPredicate(pacaOntology.FICHEROSOUT);
                    absfos.set(pacaOntology.FICHEROOUT, absfo);
                    absfos.set(pacaOntology.CASO, absca);

                    //CREAMOS UN ANDBUILDER PARA UNIR LOS DOS PREDICADOS ANTERIORES
                    AndBuilder constructor = new AndBuilder();
                    constructor.addPredicate(abstss);
                    constructor.addPredicate(absfos);
                    constructor.addPredicate(absint);

                    //CREAMOS EL IRE CON LA VARIABLE X Y ENVIAMOS TO
                    AbsVariable x =
                            new AbsVariable("ficheroOUT", pacaOntology.FICHEROOUT);
                    AbsIRE qrall = new AbsIRE(SL2Vocabulary.ALL);
                    qrall.setVariable(x);
                    qrall.setProposition(constructor.getAnd());
                    System.out.println("[" + getLocalName() + "] Le pido todos los FicherosOUT:");
                    System.out.println("[" + getLocalName() + "] Enviando mensaje...");
                    getContentManager().fillContent(msg, qrall);
                    myAgent.send(msg);
                    System.out.println("[" + getLocalName() + "] Mensaje enviado.");

                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


    /*-------METODOS PARA CREAR CARPETAS Y FICHEROS--------------*/
    private void CrearCarpetas(Practica pract, FuentesPrograma[] FP, Test[] Te, String alumno) throws IOException {
        //Creo la carpeta de la practica
        File dir = new File("/tmp/" + pract.getId() + alumno);
        dir.mkdir();

        //Creo el fichero descripcion de la practica
        FileWriter fichero;
        PrintWriter pw;
        fichero = new FileWriter("/tmp/" + pract.getId() + alumno + "/FPractica");
        pw = new PrintWriter(fichero);
        pw.println(pract.getDescripcion());
        pw.println(pract.getFechaEntrega());
        pw.close();

        //Creo las carpetas de los test y sus descripciones
        for (int i = 0; Te.length > i; i++) {
            dir = new File("/tmp/" + pract.getId() + alumno + "/" + Te[i].getId());
            dir.mkdir();
            fichero = new FileWriter("/tmp/" + pract.getId() + alumno + "/" + Te[i].getId() + "/FTest");
            pw = new PrintWriter(fichero);
            pw.println(Te[i].getDescripcion());
            pw.close();
        }

        //Creo los ficheros que ha entregado el alumno
        for (int i = 0; FP.length > i; i++) {
            fichero = new FileWriter("/tmp/" + pract.getId() + alumno + "/" + FP[i].getNombre());
            pw = new PrintWriter(fichero);
            pw.println(FP[i].getContenido());
            pw.close();

        }
    }

    private void CrearFicherosPropios(Practica p, Test t, AbsAggregate listaElementos, String alumno) throws OntologyException, IOException {
        FileWriter fichero;
        PrintWriter pw;
        for (int i = 0; listaElementos.size() > i; i++) {

            FicheroPropio fp = (FicheroPropio) ontologia.toObject(listaElementos.get(i));
            fichero = new FileWriter("/tmp/" + p.getId() + alumno + "/" + t.getId() + "/" + fp.getNombre());
            pw = new PrintWriter(fichero);
            pw.println(fp.getCodigo());
            pw.close();

        }
    }

    private void CrearCasos(Practica p, Test t, AbsAggregate listaElementos, String alumno) throws OntologyException, IOException {
        File dir;

        for (int i = 0; listaElementos.size() > i; i++) {

            Caso ca = (Caso) ontologia.toObject(listaElementos.get(i));
            dir = new File("/tmp/" + p.getId() + alumno + "/" + t.getId() + "/" + ca.getId());
            dir.mkdir();
        }
    }

    private void CrearFicherosIN(Practica p, Test t, Caso c, AbsAggregate listaElementos, String alumno) throws OntologyException, IOException {
        FileWriter fichero;
        PrintWriter pw;
        for (int i = 0; listaElementos.size() > i; i++) {

            FicheroIN fi = (FicheroIN) ontologia.toObject(listaElementos.get(i));
            fichero = new FileWriter("/tmp/" + p.getId() + alumno + "/" + t.getId() + "/" + c.getId() + "/" + fi.getNombre());
            pw = new PrintWriter(fichero);
            pw.println(fi.getContenido());
            pw.close();

        }
    }

    private void CrearFicherosOUT(Practica p, Test t, Caso c, AbsAggregate listaElementos, String alumno) throws OntologyException, IOException {
        FileWriter fichero;
        PrintWriter pw;
        for (int i = 0; listaElementos.size() > i; i++) {

            FicheroOUT fo = (FicheroOUT) ontologia.toObject(listaElementos.get(i));
            fichero = new FileWriter("/tmp/" + p.getId() + alumno + "/" + t.getId() + "/" + c.getId() + "/" + fo.getNombre());
            pw = new PrintWriter(fichero);
            pw.println(fo.getContenido());
            pw.close();

        }
    }
}











