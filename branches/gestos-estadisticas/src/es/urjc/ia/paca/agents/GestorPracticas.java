package es.urjc.ia.paca.agents;

import es.urjc.ia.baseDatos.Datos;
import es.urjc.ia.paca.ontology.*;
import jade.content.ContentManager;
import jade.content.lang.Codec;
import jade.content.lang.Codec.CodecException;
import jade.content.lang.sl.SLCodec;
import jade.content.onto.Ontology;
import jade.content.onto.OntologyException;
import jade.core.Agent;
import jade.lang.acl.ACLMessage;
import es.urjc.ia.paca.util.AndBuilder;
import jade.content.AgentAction;
import jade.content.ContentElement;
import jade.content.abs.AbsAggregate;
import jade.content.abs.AbsConcept;
import jade.content.abs.AbsContentElement;
import jade.content.abs.AbsIRE;
import jade.content.abs.AbsPredicate;
import jade.content.lang.sl.SL1Vocabulary;
import jade.content.lang.sl.SL2Vocabulary;
import jade.content.lang.sl.SLVocabulary;
import jade.content.onto.BasicOntology;
import jade.content.onto.UngroundedException;
import jade.content.onto.basic.Action;
import jade.core.behaviours.CyclicBehaviour;
import jade.core.behaviours.OneShotBehaviour;
import java.io.File;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.sql.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class GestorPracticas extends Agent {

    private ContentManager manager = (ContentManager) getContentManager();
    private Codec codec = new SLCodec();
    private Ontology ontology = pacaOntology.getInstance();
    private Connection conn;
    private Statement stat;

    /*------------CLASE QUE ESPERA UN MENSAJE--------------------*/
    class ReceiverBehaviour extends CyclicBehaviour {

        public ReceiverBehaviour(Agent a) {
            super(a);
        }

        public void action() {

            try {


                ACLMessage msg = receive();

                if (msg != null) {
                    ACLMessage reply = msg.createReply();
                    if (msg.getPerformative() == ACLMessage.QUERY_REF) {
                        //ATENDEMOS LAS PETICIONES DEL CORRECTOR
                        AbsContentElement l_in = null;
                        l_in = getContentManager().extractAbsContent(msg);
                        String requestedInfoName = l_in.getTypeName();

                        if (requestedInfoName.equals(SL2Vocabulary.ALL)) {

                            //OBTEMOS EL PREDICADO
                            AbsIRE allPred = (AbsIRE) l_in;
                            AbsPredicate qall = (AbsPredicate) allPred.getProposition();
                            String requestedInfoName2 = qall.getTypeName();

                            //EL MENSAJE DE RESPUESTA SERA UN INFORM
                            reply.setPerformative(ACLMessage.INFORM);


                            //QUERY-REF DEL INTERFAZ
                            if (requestedInfoName2.equals(pacaOntology.CORRIGE)) {
                                addBehaviour(new PracCorrecBehaviour(this.myAgent, reply, allPred));
                            } else {
                                AndBuilder predicado = new AndBuilder();
                                predicado.addPredicate(qall);

                                //UNO DE LOS PREDICADOS DEL ANDBUILDER SERA SIEMPRE UN TESTS
                                List<AbsPredicate> pract = predicado.getPredicateList(pacaOntology.TESTS);

                                //QUERY-REF DEL INTERFAZ
                                if (predicado.existsPredicate(pacaOntology.FICHEROSALUMNO) && predicado.existsPredicate(pacaOntology.CORRIGE)) {
                                    addBehaviour(new FicherosCorrBehaviour(this.myAgent, reply, predicado, allPred));
                                } else {
                                    //QUERY-REF DEL INTERFAZ
                                    if (predicado.existsPredicate(pacaOntology.CORRIGE)) {
                                        addBehaviour(new TestsCorrecBehaviour(this.myAgent, reply, predicado, allPred));
                                    } else {
                                        if (predicado.existsPredicate(pacaOntology.FICHEROSALUMNO)) {
                                            addBehaviour(new EnviaFicherosAlumnoBehaviour(this.myAgent, reply, pract, allPred));
                                        } else {
                                            //TODOS LOS QUERY-REF SIGUIENTES SERAN DEL CORRECTOR
                                            if (predicado.existsPredicate(pacaOntology.FICHEROSIN)) {
                                                //SACAMOS EL OTRO PREDICADO DEL ANDBUILDER QUE SERA UN FICHEROSIN
                                                List<AbsPredicate> fin = predicado.getPredicateList(pacaOntology.FICHEROSIN);
                                                addBehaviour(new EnviarFicherosINBehaviour(this.myAgent, reply, pract, fin, allPred));
                                            } else {
                                                if (predicado.existsPredicate(pacaOntology.FICHEROSOUT)) {
                                                    //SACAMOS EL OTRO PREDICADO DEL ANDBUILER QUE SERA UN FICHEROSOUT
                                                    List<AbsPredicate> fout = predicado.getPredicateList(pacaOntology.FICHEROSOUT);
                                                    addBehaviour(new EnviarFicherosOUTBehaviour(this.myAgent, reply, pract, fout, allPred));

                                                } else {
                                                    if (predicado.existsPredicate(pacaOntology.CASOS)) {
                                                        //ENVIAMOS LOS CASOS
                                                        addBehaviour(new EnviarCasosBehaviour(this.myAgent, reply, pract, allPred));
                                                    } else {
                                                        //ENVIAMOS LOS FICHEROS PROPIOS
                                                        addBehaviour(new EnviaFicherosPropiosBehaviour(this.myAgent, reply, pract, allPred));
                                                    }
                                                }

                                            }
                                        }
                                    }
                                }
                            }
                        }



                    } else {
                        if (msg.getPerformative() == ACLMessage.REQUEST) {
                            ContentElement p = manager.extractContent(msg);


                            if (p instanceof Action) {
                                //AbsAgentAction z = (AbsAgentAction) ((Action)p).getAction();
                                AgentAction a = (AgentAction) ((Action) p).getAction();
                                if ((!(a instanceof ModificaPractica) && (!(a instanceof ModificaTest)) &&
                                        (!(a instanceof ModificaFicheroPropio)) && (!(a instanceof ModificaFicheroIN)) &&
                                        (!(a instanceof ModificaFicheroOUT)) && (!(a instanceof CreaPractica)) &&
                                        (!(a instanceof CreaTest)) && (!(a instanceof CreaFicheroPropio)) &&
                                        (!(a instanceof CreaFicheroAlumno)) && (!(a instanceof CreaCaso)) &&
                                        (!(a instanceof CreaFicheroIN)) && (!(a instanceof CreaFicheroOUT)) &&
                                        (!(a instanceof EliminaPractica)) && (!(a instanceof EliminaTest)) &&
                                        (!(a instanceof EliminaFicheroPropio)) && (!(a instanceof EliminaCaso)) &&
                                        (!(a instanceof EliminaFicheroAlumno)) && (!(a instanceof EliminaFicheroIN)) &&
                                        (!(a instanceof EliminaFicheroOUT)) && (!(a instanceof CopiaTest)) &&
                                        (!(a instanceof CopiaFicheroPropio)) && (!(a instanceof CopiaFicheroAlumno)) &&
                                        (!(a instanceof CopiaCaso)) && (!(a instanceof CopiaFicheroIN)) &&
                                        (!(a instanceof CopiaFicheroOUT)))) {
                                    reply.setPerformative(ACLMessage.REFUSE);
                                    send(reply);
                                } else {
                                    reply.setPerformative(ACLMessage.AGREE);
                                    send(reply);
                                    boolean salida = false;
                                    if (a instanceof ModificaPractica) {
                                        ModificaPractica mdp = (ModificaPractica) a;
                                        Practica pt = mdp.getPractica();
                                        salida = ModificarPracticas(pt);
                                    } else if (a instanceof ModificaTest) {
                                        ModificaTest mts = (ModificaTest) a;
                                        Test ts = mts.getTest();
                                        Practica pt = mts.getPractica();
                                        salida = ModificarTest(pt, ts);
                                    } else if (a instanceof ModificaFicheroPropio) {
                                        ModificaFicheroPropio mfp = (ModificaFicheroPropio) a;
                                        Test ts = mfp.getTest();
                                        FicheroPropio fp = mfp.getFicheroPropio();
                                        Practica pt = mfp.getPractica();
                                        salida = ModificarFicheroPropio(pt, ts, fp);
                                    } else if (a instanceof ModificaFicheroIN) {
                                        ModificaFicheroIN mfi = (ModificaFicheroIN) a;
                                        Practica pt = mfi.getPractica();
                                        Test ts = mfi.getTest();
                                        Caso ca = mfi.getCaso();
                                        FicheroIN fi = mfi.getFicheroIN();
                                        salida = ModificarFicheroIN(pt, ts, ca, fi);
                                    } else if (a instanceof ModificaFicheroOUT) {
                                        ModificaFicheroOUT mfo = (ModificaFicheroOUT) a;
                                        Practica pt = mfo.getPractica();
                                        Test ts = mfo.getTest();
                                        Caso ca = mfo.getCaso();
                                        FicheroOUT fo = mfo.getFicheroOUT();
                                        salida = ModificarFicheroOUT(pt, ts, ca, fo);
                                    } else if (a instanceof CreaPractica) {
                                        CreaPractica cp = (CreaPractica) a;
                                        Practica pt = cp.getPractica();
                                        salida = CrearPractica(pt);

                                    } else if (a instanceof CreaTest) {
                                        CreaTest ct = (CreaTest) a;
                                        Practica pt = ct.getPractica();
                                        Test ts = ct.getTest();
                                        salida = CrearTest(pt, ts);
                                    } else if (a instanceof CreaFicheroPropio) {
                                        CreaFicheroPropio cfp = (CreaFicheroPropio) a;
                                        Test ts = cfp.getTest();
                                        FicheroPropio fp = cfp.getFicheroPropio();
                                        Practica pt = cfp.getPractica();
                                        salida = CrearFicheroPropio(pt, ts, fp);
                                    } else if (a instanceof CreaFicheroAlumno) {
                                        CreaFicheroAlumno cfp = (CreaFicheroAlumno) a;
                                        Test ts = cfp.getTest();
                                        FicheroAlumno fp = cfp.getFicheroAlumno();
                                        Practica pt = cfp.getPractica();
                                        salida = CrearFicheroAlumno(pt, ts, fp);
                                    } else if (a instanceof CreaCaso) {
                                        CreaCaso cfp = (CreaCaso) a;
                                        Test ts = cfp.getTest();
                                        Caso fp = cfp.getCaso();
                                        Practica pt = cfp.getPractica();
                                        salida = CrearCaso(pt, ts, fp);
                                    } else if (a instanceof CreaFicheroIN) {
                                        CreaFicheroIN cfp = (CreaFicheroIN) a;
                                        Test ts = cfp.getTest();
                                        Caso fp = cfp.getCaso();
                                        Practica pt = cfp.getPractica();
                                        FicheroIN fi = cfp.getFicheroIN();
                                        salida = CrearFicheroIN(pt, ts, fp, fi);
                                    } else if (a instanceof CreaFicheroOUT) {
                                        CreaFicheroOUT cfp = (CreaFicheroOUT) a;
                                        Test ts = cfp.getTest();
                                        Caso fp = cfp.getCaso();
                                        Practica pt = cfp.getPractica();
                                        FicheroOUT fo = cfp.getFicheroOUT();
                                        salida = CrearFicheroOUT(pt, ts, fp, fo);
                                    } else if (a instanceof EliminaPractica) {
                                        EliminaPractica ept = (EliminaPractica) a;
                                        Practica pt = ept.getPractica();
                                        salida = EliminarPractica(pt);
                                    } else if (a instanceof EliminaTest) {
                                        EliminaTest ct = (EliminaTest) a;
                                        Practica pt = ct.getPractica();
                                        Test ts = ct.getTest();
                                        salida = EliminarTest(pt, ts);
                                    } else if (a instanceof EliminaFicheroPropio) {
                                        EliminaFicheroPropio cfp = (EliminaFicheroPropio) a;
                                        Test ts = cfp.getTest();
                                        FicheroPropio fp = cfp.getFicheroPropio();
                                        Practica pt = cfp.getPractica();
                                        salida = EliminarFicheroPropio(pt, ts, fp);
                                    } else if (a instanceof EliminaCaso) {
                                        EliminaCaso cfp = (EliminaCaso) a;
                                        Test ts = cfp.getTest();
                                        Caso fp = cfp.getCaso();
                                        Practica pt = cfp.getPractica();
                                        salida = EliminarCaso(pt, ts, fp);
                                    } else if (a instanceof EliminaFicheroAlumno) {
                                        EliminaFicheroAlumno cfp = (EliminaFicheroAlumno) a;
                                        Test ts = cfp.getTest();
                                        FicheroAlumno fp = cfp.getFicheroAlumno();
                                        Practica pt = cfp.getPractica();
                                        salida = EliminarFicheroAlumno(pt, ts, fp);
                                    } else if (a instanceof EliminaFicheroIN) {
                                        EliminaFicheroIN cfp = (EliminaFicheroIN) a;
                                        Test ts = cfp.getTest();
                                        Caso fp = cfp.getCaso();
                                        Practica pt = cfp.getPractica();
                                        FicheroIN fi = cfp.getFicheroIN();
                                        salida = EliminarFicheroIN(pt, ts, fp, fi);
                                    } else if (a instanceof EliminaFicheroOUT) {
                                        EliminaFicheroOUT cfp = (EliminaFicheroOUT) a;
                                        Test ts = cfp.getTest();
                                        Caso fp = cfp.getCaso();
                                        Practica pt = cfp.getPractica();
                                        FicheroOUT fo = cfp.getFicheroOUT();
                                        salida = EliminarFicheroOUT(pt, ts, fp, fo);
                                    } else if (a instanceof CopiaTest) {
                                        CopiaTest ct = (CopiaTest) a;
                                        Practica pt = ct.getPractica();
                                        Practica cp = ct.getCopyPractica();
                                        Test ts = ct.getTest();
                                        Test cts = ct.getCopyTest();

                                        salida = CopiarTest(pt, ts, cp, cts);
                                    } else if (a instanceof CopiaFicheroPropio) {
                                        CopiaFicheroPropio ct = (CopiaFicheroPropio) a;
                                        Practica cp = ct.getCopyPractica();
                                        Test cts = ct.getCopyTest();
                                        FicheroPropio fp = ct.getCopyFicheroPropio();

                                        salida = CrearFicheroPropio(cp, cts, fp);
                                    } else if (a instanceof CopiaFicheroAlumno) {
                                        CopiaFicheroAlumno ct = (CopiaFicheroAlumno) a;
                                        Practica cp = ct.getCopyPractica();
                                        Test cts = ct.getCopyTest();
                                        FicheroAlumno fp = ct.getCopyFicheroAlumno();

                                        salida = CrearFicheroAlumno(cp, cts, fp);
                                    } else if (a instanceof CopiaCaso) {
                                        CopiaCaso ct = (CopiaCaso) a;
                                        Practica pt = ct.getPractica();
                                        Practica cp = ct.getCopyPractica();
                                        Test ts = ct.getTest();
                                        Test cts = ct.getCopyTest();
                                        Caso ca = ct.getCaso();
                                        Caso cca = ct.getCopyCaso();

                                        salida = CopiarCaso(pt, ts, ca, cp, cts, cca);
                                    } else if (a instanceof CopiaFicheroIN) {
                                        CopiaFicheroIN ct = (CopiaFicheroIN) a;
                                        Practica cp = ct.getCopyPractica();
                                        Test cts = ct.getCopyTest();
                                        Caso ca = ct.getCopyCaso();
                                        FicheroIN fi = ct.getCopyFicheroIN();

                                        salida = CrearFicheroIN(cp, cts, ca, fi);
                                    } else if (a instanceof CopiaFicheroOUT) {
                                        CopiaFicheroOUT ct = (CopiaFicheroOUT) a;
                                        Practica cp = ct.getCopyPractica();
                                        Test cts = ct.getCopyTest();
                                        Caso ca = ct.getCopyCaso();
                                        FicheroOUT fo = ct.getCopyFicheroOUT();

                                        salida = CrearFicheroOUT(cp, cts, ca, fo);
                                    }

                                    if (salida) {
                                        reply.setPerformative(ACLMessage.INFORM);
                                        reply.setContent("DONE");
                                        send(reply);
                                    } else {
                                        reply.setPerformative(ACLMessage.FAILURE);
                                        reply.setContent("Fallo en la base de datos");
                                        send(reply);
                                    }
                                }
                            }
                        } else {
                            System.out.println("[" + getLocalName() + "] El mensaje no se ajusta a lo esperadoo.");
                            ACLMessage respuesta = new ACLMessage(ACLMessage.NOT_UNDERSTOOD);
                            respuesta.addReceiver(msg.getSender());
                            send(respuesta);
                        }
                    }

                } else {
                    block();
                }

            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }


    /*-------------Envia Todos los ficheros Propios de un test----------------*/
    private class EnviaFicherosPropiosBehaviour extends OneShotBehaviour {

        private ACLMessage msg;
        private AbsIRE ire;
        private List<AbsPredicate> pract;

        public EnviaFicherosPropiosBehaviour(Agent a, ACLMessage msg, List<AbsPredicate> pract, AbsIRE ire) {
            super(a);
            this.msg = msg;
            this.ire = ire;
            this.pract = pract;
        }

        @Override
        public void action() {
            try {

                //OBTEMOS EL TEST Y LA PRACTICA DEL PREDICADO
                Iterator<AbsPredicate> itCor = pract.iterator();
                Tests corr = (Tests) ontology.toObject(itCor.next());
                Practica p = corr.getPractica();
                Test t = corr.getTest();

                //BUSCAMOS EN LA BASE DE DATOS LOS FICHEROS PROPIOS
                ArrayList<FicheroPropio> fp = BuscarFicherosPropios(t.getId(), p.getId());

                //CREAMOS EL AGGREGATE PARA ENVIAR TODOS LOS FICHEROS PROPIOS
                AbsAggregate absFicheros = new AbsAggregate(BasicOntology.SET);
                AbsConcept elem;

                if (fp.isEmpty()) {
                    FicheroPropio f = new FicheroPropio("No hay FicherosPropios");
                    elem = (AbsConcept) ontology.fromObject(f);
                    absFicheros.add(elem);
                } else {
                    for (int i = 0; i < fp.size(); i++) {
                        elem = (AbsConcept) ontology.fromObject(fp.get(i));
                        absFicheros.add(elem);
                    }
                }

                //CREAMOS EL EQUALS PARA ENVIAR
                AbsPredicate equalPred = new AbsPredicate(SLVocabulary.EQUALS);
                equalPred.set(SLVocabulary.EQUALS_LEFT, ire);
                equalPred.set(SLVocabulary.EQUALS_RIGHT, absFicheros);
                try {
                    manager.fillContent(msg, equalPred);
                    send(msg);

                } catch (CodecException ex) {
                    Logger.getLogger(GestorPracticas.class.getName()).log(Level.SEVERE, null, ex);
                }


            } catch (SQLException ex) {
                Logger.getLogger(GestorPracticas.class.getName()).log(Level.SEVERE, null, ex);
            } catch (OntologyException ex) {
                Logger.getLogger(GestorPracticas.class.getName()).log(Level.SEVERE, null, ex);
            }

        }
    }


    /*-------------Envia Todos los ficheros Alumno de un test----------------*/
    private class EnviaFicherosAlumnoBehaviour extends OneShotBehaviour {

        private ACLMessage msg;
        private AbsIRE ire;
        private List<AbsPredicate> pract;

        public EnviaFicherosAlumnoBehaviour(Agent a, ACLMessage msg, List<AbsPredicate> pract, AbsIRE ire) {
            super(a);
            this.msg = msg;
            this.ire = ire;
            this.pract = pract;
        }

        @Override
        public void action() {
            try {

                //OBTEMOS EL TEST Y LA PRACTICA DEL PREDICADO
                Iterator<AbsPredicate> itCor = pract.iterator();
                Tests corr = (Tests) ontology.toObject(itCor.next());
                Practica p = corr.getPractica();
                Test t = corr.getTest();
                Test[] taux = new Test[1];
                taux[0] = t;

                //BUSCAMOS EN LA BASE DE DATOS LOS FICHEROS PROPIOS
                ArrayList<FicheroAlumno> fp = FicherosAlumnoDisponibles(p.getId(), taux);

                //CREAMOS EL AGGREGATE PARA ENVIAR TODOS LOS FICHEROS PROPIOS
                AbsAggregate absFicheros = new AbsAggregate(BasicOntology.SET);
                AbsConcept elem;

                if (fp.isEmpty()) {
                    FicheroAlumno f = new FicheroAlumno("No hay FicherosAlumno");
                    elem = (AbsConcept) ontology.fromObject(f);
                    absFicheros.add(elem);
                } else {
                    for (int i = 0; i < fp.size(); i++) {

                        elem = (AbsConcept) ontology.fromObject(fp.get(i));
                        // Work Around: rormartin
                        // TODO: FicheroParaPracticas work fine?
                        if (elem != null) {
                            absFicheros.add(elem);
                        }
                    }
                }

                //CREAMOS EL EQUALS PARA ENVIAR
                AbsPredicate equalPred = new AbsPredicate(SLVocabulary.EQUALS);
                equalPred.set(SLVocabulary.EQUALS_LEFT, ire);
                equalPred.set(SLVocabulary.EQUALS_RIGHT, absFicheros);
                try {
                    manager.fillContent(msg, equalPred);
                    send(msg);

                } catch (CodecException ex) {
                    Logger.getLogger(GestorPracticas.class.getName()).log(Level.SEVERE, null, ex);
                }


            } catch (SQLException ex) {
                Logger.getLogger(GestorPracticas.class.getName()).log(Level.SEVERE, null, ex);
            } catch (OntologyException ex) {
                Logger.getLogger(GestorPracticas.class.getName()).log(Level.SEVERE, null, ex);
            }

        }
    }

    /*-------------Envia Todos los Casos de un test----------------*/
    private class EnviarCasosBehaviour extends OneShotBehaviour {

        private ACLMessage msg;
        private AbsIRE ire;
        private List<AbsPredicate> pract;

        public EnviarCasosBehaviour(Agent a, ACLMessage msg, List<AbsPredicate> pract, AbsIRE ire) {
            super(a);
            this.msg = msg;
            this.ire = ire;
            this.pract = pract;
        }

        @Override
        public void action() {
            try {
                //OBTEMOS DEL PREDICADO EL TEST Y LA PRACTICA
                Iterator<AbsPredicate> itCor = pract.iterator();
                Tests corr = (Tests) ontology.toObject(itCor.next());
                Practica p = corr.getPractica();
                Test t = corr.getTest();

                //BUSCAMOS EN LA BASE DE DATOS LOS CASOS
                ArrayList<Caso> ca = BuscarCasos(t.getId(), p.getId());

                //CREAMOS EL AGGREGATE PARA ENVIAR TODOS LOS CASOS
                AbsAggregate absCasos = new AbsAggregate(BasicOntology.SET);
                AbsConcept elem;

                if (ca.isEmpty()) {
                    Caso c = new Caso("No hay casos");
                    elem = (AbsConcept) ontology.fromObject(c);
                    absCasos.add(elem);
                } else {
                    for (int i = 0; i < ca.size(); i++) {
                        elem = (AbsConcept) ontology.fromObject(ca.get(i));
                        absCasos.add(elem);
                    }
                }

                //CREAMOS EL EQUALS PARA ENVIAR
                AbsPredicate equalPred = new AbsPredicate(SLVocabulary.EQUALS);
                equalPred.set(SLVocabulary.EQUALS_LEFT, ire);
                equalPred.set(SLVocabulary.EQUALS_RIGHT, absCasos);
                try {
                    manager.fillContent(msg, equalPred);
                    send(msg);

                } catch (CodecException ex) {
                    Logger.getLogger(GestorPracticas.class.getName()).log(Level.SEVERE, null, ex);
                }


            } catch (SQLException ex) {
                Logger.getLogger(GestorPracticas.class.getName()).log(Level.SEVERE, null, ex);
            } catch (OntologyException ex) {
                Logger.getLogger(GestorPracticas.class.getName()).log(Level.SEVERE, null, ex);
            }

        }
    }


    /*-------------Envia Todos los ficherosIN de un Caso----------------*/
    private class EnviarFicherosINBehaviour extends OneShotBehaviour {

        private List<AbsPredicate> pract;
        private ACLMessage msg;
        private AbsIRE ire;
        private List<AbsPredicate> fin;

        public EnviarFicherosINBehaviour(Agent a, ACLMessage msg, List<AbsPredicate> pract, List<AbsPredicate> fin, AbsIRE ire) {
            super(a);
            this.msg = msg;
            this.ire = ire;
            this.pract = pract;
            this.fin = fin;
        }

        @Override
        public void action() {
            try {
                //OBTEMOS DEL PREDICADO EL TEST Y LA PRACTICA
                Iterator<AbsPredicate> itCor = pract.iterator();
                Tests corr = (Tests) ontology.toObject(itCor.next());
                Practica p = corr.getPractica();
                Test t = corr.getTest();
                //OBTEMOS DEL PREDICADO EL CASO AL QUE PERTENECERAN LOS FICHEROS IN
                Iterator<AbsPredicate> itCor2 = fin.iterator();
                FicherosIN corr2 = (FicherosIN) ontology.toObject(itCor2.next());
                Caso ca = corr2.getCaso();

                //BUSCAMOS EN LA BASE DE DATOS LOS FICHEROS IN
                ArrayList<FicheroIN> listaFin = BuscarFicherosIN(ca.getId(), t.getId(), p.getId());

                //CREAMOS EL AGGREGATE PARA ENVIAR TODOS LOS FICHEROS IN
                AbsAggregate absFicherosIN = new AbsAggregate(BasicOntology.SET);
                AbsConcept elem;

                if (listaFin.isEmpty()) {
                    FicheroIN fi = new FicheroIN("No hay FicherosIN");
                    elem = (AbsConcept) ontology.fromObject(fi);
                    absFicherosIN.add(elem);
                } else {
                    for (int i = 0; i < listaFin.size(); i++) {
                        elem = (AbsConcept) ontology.fromObject(listaFin.get(i));
                        absFicherosIN.add(elem);
                    }
                }

                //CREAMOS EL EQUALS PARA ENVIAR
                AbsPredicate equalPred = new AbsPredicate(SLVocabulary.EQUALS);
                equalPred.set(SLVocabulary.EQUALS_LEFT, ire);
                equalPred.set(SLVocabulary.EQUALS_RIGHT, absFicherosIN);
                try {
                    manager.fillContent(msg, equalPred);
                    send(msg);

                } catch (CodecException ex) {
                    Logger.getLogger(GestorPracticas.class.getName()).log(Level.SEVERE, null, ex);
                }


            } catch (SQLException ex) {
                Logger.getLogger(GestorPracticas.class.getName()).log(Level.SEVERE, null, ex);
            } catch (OntologyException ex) {
                Logger.getLogger(GestorPracticas.class.getName()).log(Level.SEVERE, null, ex);
            }

        }
    }


    /*-------------Envia Todos los ficherosIN de un Caso----------------*/
    private class EnviarFicherosOUTBehaviour extends OneShotBehaviour {

        private List<AbsPredicate> pract;
        private List<AbsPredicate> fout;
        private ACLMessage msg;
        private AbsIRE ire;

        public EnviarFicherosOUTBehaviour(Agent a, ACLMessage msg, List<AbsPredicate> pract, List<AbsPredicate> fout, AbsIRE ire) {
            super(a);
            this.msg = msg;
            this.ire = ire;
            this.pract = pract;
            this.fout = fout;
        }

        @Override
        public void action() {

            try {
                //OBTEMOS DEL PREDICADO EL TEST Y LA PRACTICA
                Iterator<AbsPredicate> itCor = pract.iterator();
                Tests corr = (Tests) ontology.toObject(itCor.next());
                Practica p = corr.getPractica();
                Test t = corr.getTest();

                //OBTEMOS DEL PREDICADO EL CASO AL QUE PERTENECE LOS FICHEROS OUT
                Iterator<AbsPredicate> itCor2 = fout.iterator();
                FicherosOUT corr2 = (FicherosOUT) ontology.toObject(itCor2.next());
                Caso ca = corr2.getCaso();

                //BUSCAMOS EN LA BASE DE DATOS LOS FICHEROS OUT
                ArrayList<FicheroOUT> listaFout = BuscarFicherosOUT(ca.getId(), t.getId(), p.getId());

                //CREAMOS EL AGGREGATE PARA ENVIAR TODOS LOS FICHEROSOUT
                AbsAggregate absFicherosOUT = new AbsAggregate(BasicOntology.SET);
                AbsConcept elem;

                if (listaFout.isEmpty()) {
                    FicheroOUT fo = new FicheroOUT("No hay FicherosOUT");
                    elem = (AbsConcept) ontology.fromObject(fo);
                    absFicherosOUT.add(elem);
                } else {
                    for (int i = 0; i < listaFout.size(); i++) {
                        elem = (AbsConcept) ontology.fromObject(listaFout.get(i));
                        absFicherosOUT.add(elem);
                    }
                }
                //CREAMOS EL EQUALS PARA ENVIAR
                AbsPredicate equalPred = new AbsPredicate(SLVocabulary.EQUALS);
                equalPred.set(SLVocabulary.EQUALS_LEFT, ire);
                equalPred.set(SLVocabulary.EQUALS_RIGHT, absFicherosOUT);
                try {
                    manager.fillContent(msg, equalPred);
                    send(msg);

                } catch (CodecException ex) {
                    Logger.getLogger(GestorPracticas.class.getName()).log(Level.SEVERE, null, ex);
                }


            } catch (SQLException ex) {
                Logger.getLogger(GestorPracticas.class.getName()).log(Level.SEVERE, null, ex);
            } catch (OntologyException ex) {
                Logger.getLogger(GestorPracticas.class.getName()).log(Level.SEVERE, null, ex);
            }

        }
    }


    /*-----------------Comportamientos para enviar al interfaz-----------*/
    //Comportamiento que obtiene las practicas disponibles para corregir y las envia al interfaz
    public class PracCorrecBehaviour extends OneShotBehaviour {

        private ACLMessage resp1;
        private AbsIRE ire1;

        public PracCorrecBehaviour(Agent _a, ACLMessage msg1, AbsIRE ireAux) {
            super(_a);
            this.ire1 = ireAux;
            this.resp1 = msg1;
        }

        public void action() {
            try {
                //--> PracticasDisponibles <--------------------------------------------------
                ArrayList<Practica> lpractn = PracticasDisponibles();
                // Creamos el listado de practicas de forma abstracta
                AbsAggregate absPracticas = new AbsAggregate(BasicOntology.SET);


                //Si no hay practicas en la base de datos envia esta practica
                if (lpractn.isEmpty()) {
                	System.out.println("No hay practicas");
                    Practica p = new Practica("No hay practicas");
                    AbsConcept elem;
                    try {
                        elem = (AbsConcept) ontology.fromObject(p);
                        absPracticas.add(elem);
                    } catch (OntologyException ex) {
                        Logger.getLogger(GestorPracticas.class.getName()).log(Level.SEVERE, null, ex);
                    }

                } else {
                    for (int i = 0; i < lpractn.size(); i++) {
                        //Creamos un objecto abstracto por cada practica y la a�adimos al aggregate
                        AbsConcept elem;
                        try {
                            elem = (AbsConcept) ontology.fromObject(lpractn.get(i));
                            absPracticas.add(elem);
                        } catch (OntologyException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }
                    }
                }
                //Creamos el predicado abstracto EQUALS
                AbsPredicate equalPred = new AbsPredicate(SLVocabulary.EQUALS);
                equalPred.set(SLVocabulary.EQUALS_LEFT, ire1);
                equalPred.set(SLVocabulary.EQUALS_RIGHT, absPracticas);
                //Mandamos el predicado al interfaz
                try {
                	System.out.println("Enviar mensaje");
                    getContentManager().fillContent(resp1, equalPred);
                } catch (CodecException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                } catch (OntologyException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                send(resp1);
            } catch (SQLException ex) {
                Logger.getLogger(GestorPracticas.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    //Comportamiento que manda los tests al agente Interfaz
    public class TestsCorrecBehaviour extends OneShotBehaviour {

        private ACLMessage mens1;
        private AbsIRE ire1;
        private AndBuilder predicado;

        public TestsCorrecBehaviour(Agent _a, ACLMessage msg1, AndBuilder predicado1, AbsIRE ire) {
            super(_a);
            this.mens1 = msg1;
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
                tes = (Tests) ontology.toObject(it.next());


                //Obtenemos los tests disponibles para la practica seleccionada
                ArrayList<Test> te = TestDisponibles(tes.getPractica().getId());

                AbsAggregate absTests = new AbsAggregate(BasicOntology.SET);


                if (te.isEmpty()) {
                    Test t = new Test("No hay Tests");
                    AbsConcept elem = (AbsConcept) ontology.fromObject(t);
                    absTests.add(elem);
                } else {
                    // Adds the Test
                    //Pasamos los tests a objectos abstractos
                    for (int i = 0; i < te.size(); i++) {
                        AbsConcept elem = (AbsConcept) ontology.fromObject(te.get(i));
                        absTests.add(elem);
                    }
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

            } catch (SQLException ex) {
                Logger.getLogger(GestorPracticas.class.getName()).log(Level.SEVERE, null, ex);
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
        private AbsIRE ire1;
        private AndBuilder predicado;

        public FicherosCorrBehaviour(Agent _a, ACLMessage msg1, AndBuilder predicado1, AbsIRE ire) {
            super(_a);
            this.mens1 = msg1;
            this.predicado = predicado1;
            this.ire1 = ire;
        }

        public void action() {

            Corrige corr;
            try {

                List<AbsPredicate> lisCorrige = predicado.getPredicateList(pacaOntology.CORRIGE);
                List<AbsPredicate> listTests = predicado.getPredicateList(pacaOntology.TESTS);



                Iterator<AbsPredicate> itCor = lisCorrige.iterator();

                corr = (Corrige) ontology.toObject(itCor.next());
                Practica pract = corr.getPractica();

                //Guardamos todos los Test que nos han pedido

                Test[] testAux3 = ExtraeTestsPedidos(listTests);

                //Obtenemos los ficheros fuentes necesarios para cada Test
                ArrayList<FicheroAlumno> fp = FicherosAlumnoDisponibles(pract.getId(), testAux3);


                // Creamos el listado de ficheros de forma abstracta
                AbsAggregate absFicheros = new AbsAggregate(BasicOntology.SET);

                if (fp.isEmpty()) {
                    FicheroAlumno f = new FicheroAlumno("No hay ficherosAlumno");
                    AbsConcept elem = (AbsConcept) ontology.fromObject(f);
                    absFicheros.add(elem);
                } else {
                    for (int i = 0; i < fp.size(); i++) {
                        //Creamos un objecto abstracto por cada fichero y la a�adimos al aggregate
                        AbsConcept elem;
                        try {
                            elem = (AbsConcept) ontology.fromObject(fp.get(i));

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
            } catch (SQLException ex) {
                Logger.getLogger(GestorPracticas.class.getName()).log(Level.SEVERE, null, ex);
            } catch (UngroundedException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
            } catch (OntologyException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
            }

        }
    }

    //M�todo que extrae los tests pedidos
    private Test[] ExtraeTestsPedidos(List<AbsPredicate> lista) {
        int tamano = lista.size();
        Test[] testAux = new Test[tamano];
        Iterator<AbsPredicate> it = lista.iterator();
        for (int i = 0; i < testAux.length; i++) {
            Tests aux;
            try {
                aux = (Tests) ontology.toObject(it.next());
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


    /*------------METODOS DE LA BASE DE DATOS----------------------*/

    /*-----------BUSQUEDA DE PRACTICAS DISPONIBLES---------------*/
    private ArrayList<Practica> PracticasDisponibles() throws SQLException {
        String frase = "select * from Practica;";
        ArrayList<Practica> aux = new ArrayList();
        try {
            ResultSet rs = stat.executeQuery(frase);
            Practica pt;
            while (rs.next()) {
                pt = new Practica(rs.getString("id"), rs.getString("descripcion"), rs.getString("fechaEntrega"));
                aux.add(pt);
            }
            rs.close();
        } catch (Exception e) {
            //Salta esta excepcion si hay algun fallo en la base de datos
        } finally {

            return aux;
        }

    }
    /*-----------BUSQUEDA DE TEST DISPONIBLES---------------*/

    private ArrayList<Test> TestDisponibles(String id_Practica) throws SQLException {

        String frase = "select * from Test where id_Practica='" + id_Practica + "';";
        ArrayList<Test> aux = new ArrayList();
        try {
            ResultSet rs = stat.executeQuery(frase);
            Test ts;

            while (rs.next()) {
                ts = new Test(rs.getString("id"), rs.getString("descripcion"), rs.getString("ejecutable"));
                aux.add(ts);
            }
            rs.close();
        } catch (Exception e) {
        } finally {
            return aux;
        }

    }

    /*-----------BUSQUEDA DE FICHEROS ALUMNO DISPONIBLES---------------*/
    private ArrayList<FicheroAlumno> FicherosAlumnoDisponibles(String id_Practica, Test[] t) throws SQLException {

        ArrayList<FicheroAlumno> aux = new ArrayList();
        ResultSet rs = null;
        try {
            for (int i = 0; i < t.length; i++) {
                String frase = "select * from FicherosAlumno where id_Practica='" + id_Practica + "' and id_Test='" + t[i].getId() + "';";
                rs = stat.executeQuery(frase);
                FicheroAlumno fa;

                while (rs.next()) {
                    fa = new FicheroAlumno(rs.getString("id"));
                    aux.add(fa);
                }
            }
            rs.close();
        } catch (Exception e) {
        } finally {
            return aux;
        }

    }

    /*-----------BUSQUEDA DE FICHEROS PROPIOS DISPONIBLES---------------*/
    private ArrayList<FicheroPropio> BuscarFicherosPropios(String id_Test, String id_Practica) throws SQLException {
        ArrayList<FicheroPropio> aux = new ArrayList();
        String frase = "select * from FicherosPropios where id_Test='" + id_Test + "' and id_Practica='" + id_Practica + "';";
        try {
            ResultSet rs = stat.executeQuery(frase);
            FicheroPropio fp;

            while (rs.next()) {
                fp = new FicheroPropio(rs.getString("id"), rs.getString("codigo"));
                aux.add(fp);
            }
            rs.close();
        } catch (Exception e) {
        } finally {
            return aux;
        }

    }


    /*-----------BUSQUEDA DE CASOS---------------*/
    private ArrayList<Caso> BuscarCasos(String id_Test, String id_Practica) throws SQLException {

        String frase = "select * from Caso where id_Test='" + id_Test + "' and id_Practica='" + id_Practica + "';";
        ArrayList<Caso> aux = new ArrayList();
        try {
            ResultSet rs = stat.executeQuery(frase);
            Caso ca;

            while (rs.next()) {
                ca = new Caso(rs.getString("id"));
                aux.add(ca);
            }
            rs.close();
        } catch (Exception e) {
        } finally {
            return aux;
        }

    }


    /*-----------BUSQUEDA DE FICHEROS IN---------------*/
    private ArrayList<FicheroIN> BuscarFicherosIN(String id_caso, String id_Test, String id_Practica) throws SQLException {
        ArrayList<FicheroIN> aux = new ArrayList();
        String frase = "select * from FIcherosIN where id_Test='" + id_Test + "' and id_Practica='" + id_Practica + "' and id_Caso='" + id_caso + "';";
        try {
            ResultSet rs = stat.executeQuery(frase);
            FicheroIN fi;

            while (rs.next()) {
                fi = new FicheroIN(rs.getString("id"), rs.getString("contenido"));
                aux.add(fi);
            }
            rs.close();
        } catch (Exception e) {
        } finally {
            return aux;
        }

    }


    /*-----------BUSQUEDA DE FICHEROS OUT---------------*/
    private ArrayList<FicheroOUT> BuscarFicherosOUT(String id_caso, String id_Test, String id_Practica) throws SQLException {


        ArrayList<FicheroOUT> aux = new ArrayList();
        String frase = "select * from FicherosOUT where id_Test='" + id_Test + "' and id_Practica='" + id_Practica + "' and id_Caso='" + id_caso + "';";
        try {
            ResultSet rs = stat.executeQuery(frase);
            FicheroOUT fout;

            while (rs.next()) {
                fout = new FicheroOUT(rs.getString("id"), rs.getString("contenido"));
                aux.add(fout);
            }
            rs.close();
        } catch (Exception e) {
        } finally {
            return aux;
        }

    }

    private boolean ModificarPracticas(Practica pt) {
        try {
            String frase = "update Practica set descripcion='" + pt.getDescripcion() + "' where id='" + pt.getId() + "';";
            String frase2 = "update Practica set fechaEntrega='" + pt.getFechaEntrega() + "' where id='" + pt.getId() + "';";
            stat.executeUpdate(frase);
            stat.executeUpdate(frase2);
        } catch (SQLException ex) {
            return false;
        }
        return true;
    }

    private boolean ModificarTest(Practica pt, Test ts) {
        try {
            String frase = "update Test set descripcion='" + ts.getDescripcion() + "' where id='" + ts.getId() + "' and id_practica='" + pt.getId() + "';";
            
            String frase2 = "update Test set ejecutable='" + ts.getEjecutable() + "' where id='" + ts.getId() + "' and id_practica='" + pt.getId() + "';";
            stat.executeUpdate(frase2);
            
            stat.executeUpdate(frase);
        } catch (SQLException ex) {
            return false;
        }
        return true;
    }

    private boolean ModificarFicheroPropio(Practica pt, Test ts, FicheroPropio fp) {
        try {
            String frase = "update FicherosPropios set codigo='" + fp.getCodigo() + "' where id='" + fp.getNombre() + "' and id_test='" + ts.getId() + "' and id_practica='" + pt.getId() + "';";
            stat.executeUpdate(frase);
        } catch (SQLException ex) {
            return false;
        }
        return true;
    }

    private boolean ModificarFicheroIN(Practica pt, Test ts, Caso ca, FicheroIN fi) {
        try {
            String frase = "update FicherosIN set contenido='" + fi.getContenido() + "' where id='" + fi.getNombre() + "' and id_test='" + ts.getId() + "' and id_practica='" + pt.getId() + "' and id_caso='" + ca.getId() + "' ;";
            stat.executeUpdate(frase);
        } catch (SQLException ex) {
            return false;
        }
        return true;
    }

    private boolean ModificarFicheroOUT(Practica pt, Test ts, Caso ca, FicheroOUT fo) {
        try {
            String frase = "update FicherosOUT set contenido='" + fo.getContenido() + "' where id='" + fo.getNombre() + "' and id_test='" + ts.getId() + "' and id_practica='" + pt.getId() + "' and id_caso='" + ca.getId() + "' ;";
            stat.executeUpdate(frase);
        } catch (SQLException ex) {
            return false;
        }
        return true;
    }

    private boolean CrearPractica(Practica pt) {
        try {
            String frase = "insert into Practica values('" + pt.getId() + "', '" + pt.getDescripcion() + "', '" + pt.getFechaEntrega() + "');";
            stat.executeUpdate(frase);
        } catch (SQLException ex) {
            return false;
        }
        return true;
    }

    private boolean CrearTest(Practica pt, Test ts) {
        try {
            String frase = "insert into Test values('" + ts.getId() + "', '" + pt.getId() + "', '" + ts.getDescripcion() + "', '" +ts.getEjecutable()+"');";
            stat.executeUpdate(frase);
        } catch (SQLException ex) {
            return false;
        }
        return true;
    }

    private boolean CrearFicheroPropio(Practica pt, Test ts, FicheroPropio fp) {
        try {
            String frase = "insert into FicherosPropios values('" + fp.getNombre() + "', '" + ts.getId() + "', '" + pt.getId() + "', '', '" + fp.getCodigo() + "');";
            stat.executeUpdate(frase);
        } catch (SQLException ex) {
            return false;
        }
        return true;
    }

    private boolean CrearFicheroAlumno(Practica pt, Test ts, FicheroAlumno fa) {
        try {
            String frase = "insert into FicherosAlumno values('" + fa.getNombre() + "', '" + ts.getId() + "', '" + pt.getId() + "', '', '');";
            stat.executeUpdate(frase);
        } catch (SQLException ex) {
            return false;
        }
        return true;
    }

    private boolean CrearCaso(Practica pt, Test ts, Caso ca) {
        try {
            String frase = "insert into Caso values('" + ca.getId() + "', '" + ts.getId() + "', '" + pt.getId() + "');";
            stat.executeUpdate(frase);
        } catch (SQLException ex) {
            return false;
        }
        return true;
    }

    private boolean CrearFicheroIN(Practica pt, Test ts, Caso ca, FicheroIN fi) {
        try {
            String frase = "insert into FicherosIN values('" + fi.getNombre() + "', '" + ca.getId() + "', '" + ts.getId() + "', '" + pt.getId() + "', '" + fi.getContenido() + "');";
            stat.executeUpdate(frase);
        } catch (SQLException ex) {
            return false;
        }
        return true;
    }

    private boolean CrearFicheroOUT(Practica pt, Test ts, Caso ca, FicheroOUT fo) {
        try {
            String frase = "insert into FicherosOUT values('" + fo.getNombre() + "', '" + ca.getId() + "', '" + ts.getId() + "', '" + pt.getId() + "', '" + fo.getContenido() + "');";
            stat.executeUpdate(frase);
        } catch (SQLException ex) {
            return false;
        }
        return true;
    }

    //******************CLASES PARA ELIMINAR******************/
    private boolean EliminarPractica(Practica pt) {
        try {
            String frase = "delete from Practica where id='" + pt.getId() + "' ;";
            stat.executeUpdate(frase);
            frase = "delete from Test where id_practica='" + pt.getId() + "';";
            stat.executeUpdate(frase);
            frase = "delete from Caso where id_practica='" + pt.getId() + "';";
            stat.executeUpdate(frase);
            frase = "delete from FicherosPropios where id_practica='" + pt.getId() + "';";
            stat.executeUpdate(frase);
            frase = "delete from FicherosAlumno where id_practica='" + pt.getId() + "';";
            stat.executeUpdate(frase);
            frase = "delete from FicherosIN where id_practica='" + pt.getId() + "';";
            stat.executeUpdate(frase);
            frase = "delete from FicherosOUT where id_practica='" + pt.getId() + "';";
            stat.executeUpdate(frase);

        } catch (SQLException ex) {
            return false;
        }
        return true;
    }

    private boolean EliminarTest(Practica pt, Test ts) {
        try {
            String frase = "delete from Test where id='" + ts.getId() + "'and id_practica='" + pt.getId() + "';";
            stat.executeUpdate(frase);
            frase = "delete from FicherosPropios where id_test='" + ts.getId() + "'and id_practica='" + pt.getId() + "';";
            stat.executeUpdate(frase);
            frase = "delete from FicherosAlumno where id_test='" + ts.getId() + "'and id_practica='" + pt.getId() + "';";
            stat.executeUpdate(frase);
            frase = "delete from Caso where id_test='" + ts.getId() + "'and id_practica='" + pt.getId() + "';";
            stat.executeUpdate(frase);
            frase = "delete from FicherosIN where id_test='" + ts.getId() + "'and id_practica='" + pt.getId() + "';";
            stat.executeUpdate(frase);
            frase = "delete from FicherosOUT where id_test='" + ts.getId() + "'and id_practica='" + pt.getId() + "';";
            stat.executeUpdate(frase);
        } catch (SQLException ex) {
            return false;
        }
        return true;
    }

    private boolean EliminarFicheroPropio(Practica pt, Test ts, FicheroPropio fp) {
        try {
            String frase = "delete from FicherosPropios where id='" + fp.getNombre() + "' and id_test='" + ts.getId() + "' and id_practica='" + pt.getId() + "';";
            stat.executeUpdate(frase);
        } catch (SQLException ex) {
            return false;
        }
        return true;
    }

    private boolean EliminarFicheroAlumno(Practica pt, Test ts, FicheroAlumno fa) {
        try {
            String frase = "delete from FicherosAlumno where id='" + fa.getNombre() + "' and id_test='" + ts.getId() + "' and id_practica='" + pt.getId() + "';";
            stat.executeUpdate(frase);
        } catch (SQLException ex) {
            return false;
        }
        return true;
    }

    private boolean EliminarCaso(Practica pt, Test ts, Caso ca) {
        try {
            String frase = "delete from Caso where id='" + ca.getId() + "' and id_test='" + ts.getId() + "' and id_practica='" + pt.getId() + "';";
            stat.executeUpdate(frase);
            frase = "delete from FicherosOUT where id_caso='" + ca.getId() + "' and id_test='" + ts.getId() + "'and id_practica='" + pt.getId() + "';";
            stat.executeUpdate(frase);
            frase = "delete from FicherosIN where id_caso='" + ca.getId() + "' and id_test='" + ts.getId() + "'and id_practica='" + pt.getId() + "';";
            stat.executeUpdate(frase);
        } catch (SQLException ex) {
            return false;
        }
        return true;
    }

    private boolean EliminarFicheroOUT(Practica pt, Test ts, Caso ca, FicheroOUT fo) {
        try {
            String frase = "delete from FicherosOUT where id='" + fo.getNombre() + "' and id_caso='" + ca.getId() + "' and id_test='" + ts.getId() + "' and id_practica='" + pt.getId() + "';";
            stat.executeUpdate(frase);
        } catch (SQLException ex) {
            return false;
        }
        return true;
    }

    private boolean EliminarFicheroIN(Practica pt, Test ts, Caso ca, FicheroIN fi) {
        try {
            String frase = "delete from FicherosIN where id='" + fi.getNombre() + "' and id_caso='" + ca.getId() + "' and id_test='" + ts.getId() + "' and id_practica='" + pt.getId() + "';";
            stat.executeUpdate(frase);
        } catch (SQLException ex) {
            return false;
        }
        return true;
    }

    /*****************Clases para copiar*********************************/
    private boolean CopiarTest(Practica pt, Test ts, Practica cp, Test cts) {

        boolean salida = true;
        try {
            salida = CrearTest(cp, cts);

            String frase = "select * from FicherosPropios where id_Test='" + ts.getId() + "' and id_Practica='" + pt.getId() + "';";

            ResultSet rs = stat.executeQuery(frase);
            ArrayList<FicheroPropio> fp = new ArrayList<FicheroPropio>();

            //Si se produce algun fallo en la copia de algun fichero propio no continuamos
            //No podemos llamar directamente a la llamada de CrearFicheroPropio, poruqe el ResutlSet debe de estar cerrado
            while (rs.next()) {
                FicheroPropio fichero = new FicheroPropio(rs.getString("id"), rs.getString("codigo"));
                fp.add(fichero);
            }
            rs.close();

            int i = 0;
            while (i < fp.size() && salida) {
                salida = CrearFicheroPropio(cp, cts, fp.get(i));
                i++;
            }

            frase = "select * from FicherosAlumno where id_Test='" + ts.getId() + "' and id_Practica='" + pt.getId() + "';";
            ResultSet rs2 = stat.executeQuery(frase);
            ArrayList<FicheroAlumno> fa = new ArrayList<FicheroAlumno>();
            while (rs2.next() && salida) {
                FicheroAlumno fichero = new FicheroAlumno(rs.getString("id"));
                fa.add(fichero);
            }
            rs2.close();

            i = 0;
            while (i < fa.size() && salida) {
                salida = CrearFicheroAlumno(cp, cts, fa.get(i));
                i++;

            }

            frase = "select * from Caso where id_Test='" + ts.getId() + "' and id_Practica='" + pt.getId() + "';";
            ResultSet rs3 = stat.executeQuery(frase);
            ArrayList<Caso> ca = new ArrayList<Caso>();

            while (rs3.next() && salida) {
                Caso caso = new Caso(rs.getString("id"));
                ca.add(caso);
            }
            rs3.close();

            i = 0;
            while (i < ca.size() && salida) {
                salida = CopiarCaso(pt, ts, ca.get(i), cp, cts, ca.get(i));
                i++;

            }


        } catch (SQLException ex) {
            return false;
        }
        return salida;

    }

    private boolean CopiarCaso(Practica pt, Test ts, Caso ca, Practica cp, Test cts, Caso cca) {
        boolean salida = true;
        try {

            salida = CrearCaso(cp, cts, cca);
            String frase = "select * from FicherosIN where id_test='" + ts.getId() + "' and id_practica='" + pt.getId() + "' and id_caso='" + ca.getId() + "' ;";
            ResultSet rs = stat.executeQuery(frase);
            ArrayList<FicheroIN> fi = new ArrayList<FicheroIN>();
            while (rs.next() && salida) {
                FicheroIN fichero = new FicheroIN(rs.getString("id"), rs.getString("contenido"));
                fi.add(fichero);
            }
            rs.close();

            int i = 0;
            while (i < fi.size() && salida) {
                salida = CrearFicheroIN(cp, cts, cca, fi.get(i));
                i++;

            }

            frase = "select * from FicherosOUT where id_test='" + ts.getId() + "' and id_practica='" + pt.getId() + "' and id_caso='" + ca.getId() + "' ;";
            ResultSet rs2 = stat.executeQuery(frase);
            ArrayList<FicheroOUT> fo = new ArrayList<FicheroOUT>();
            while (rs2.next() && salida) {
                FicheroOUT fichero = new FicheroOUT(rs.getString("id"), rs.getString("contenido"));
                fo.add(fichero);
            }
            rs2.close();

            i = 0;
            while (i < fo.size() && salida) {
                salida = CrearFicheroOUT(cp, cts, cca, fo.get(i));
                i++;

            }

        } catch (SQLException ex) {
            return false;
        }
        return salida;
    }

    private void IniciarBaseDatos() throws SQLException, ClassNotFoundException {
        File ff = new File("datos.db");
        if (!ff.exists()) {
            Datos dt = new Datos();
            conn = dt.getConnection();
            stat = dt.getStat();
        } else {
            Class.forName("org.sqlite.JDBC");
            conn = DriverManager.getConnection("jdbc:sqlite:datos.db"); //Esto seria el fichero donde guardar los datos
            stat = conn.createStatement();
        }
    }

    /*------------SETUP DEL AGENTE---------------------*/
    @Override
    protected void setup() {
        try {
            manager.registerLanguage(codec);
            manager.registerOntology(ontology);
            IniciarBaseDatos();
            addBehaviour(new ReceiverBehaviour(this));
        } catch (SQLException ex) {
            Logger.getLogger(GestorPracticas.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(GestorPracticas.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
