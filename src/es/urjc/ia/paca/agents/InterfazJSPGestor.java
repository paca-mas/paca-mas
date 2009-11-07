/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package es.urjc.ia.paca.agents;

import jade.core.Agent;
import jade.core.behaviours.OneShotBehaviour;
import jade.util.leap.ArrayList;

import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;

import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.http.HttpServletRequest;

import com.oreilly.servlet.multipart.FilePart;
import com.oreilly.servlet.multipart.MultipartParser;
import com.oreilly.servlet.multipart.ParamPart;
import com.oreilly.servlet.multipart.Part;

import es.urjc.ia.paca.ontology.Caso;
import es.urjc.ia.paca.ontology.FicheroIN;
import es.urjc.ia.paca.ontology.FicheroOUT;
import es.urjc.ia.paca.ontology.FicheroPropio;
import es.urjc.ia.paca.ontology.Practica;
import es.urjc.ia.paca.ontology.Test;
import es.urjc.ia.paca.parser.EvaluacionParser;
import es.urjc.ia.paca.util.Testigo;
import java.util.StringTokenizer;

/**
 *
 * @author alvaro
 */
public class InterfazJSPGestor extends InterfazGestor {

    public class AutenticaRequestBeha extends OneShotBehaviour {

        private Testigo test2;

        public AutenticaRequestBeha(Agent _a, Testigo tes1) {
            super(_a);
            this.test2 = tes1;
        }

        public void action() {
            HttpServletRequest param1 = (HttpServletRequest) test2.getParametro();
            String auxUsuario = param1.getParameter("user_id");
            String auxPass = param1.getParameter("password");
            addBehaviour(new EnviaAutenticaBehaviour(this.myAgent, test2, auxUsuario, auxPass));
        }
    }

    public class PideTestRequestBeha extends OneShotBehaviour {

        private Testigo tes2;

        public PideTestRequestBeha(Agent _a, Testigo tes1) {
            super(_a);
            this.tes2 = tes1;
        }

        public void action() {
            HttpServletRequest request = (HttpServletRequest) tes2.getParametro();
            String nombre = request.getParameter("NombrePractica");
            Practica practica = null;
            if (nombre != null) {
                String descripcion = request.getParameter("DescripcionPractica");
                String fechaEntrega = request.getParameter("FechaPractica");
                practica = new Practica(nombre, descripcion, fechaEntrega);
            }
            addBehaviour(new PideTestBeha(this.myAgent, tes2, practica));
        }
    }

    public class PideFicherosPropiosBeha extends OneShotBehaviour {

        private Testigo tes2;

        public PideFicherosPropiosBeha(Agent _a, Testigo tes1) {
            super(_a);
            this.tes2 = tes1;
        }

        public void action() {
            HttpServletRequest request = (HttpServletRequest) tes2.getParametro();
            Test test = null;
            String nombre = request.getParameter("NombreTest");
            if (nombre != null) {
                String descripcion = request.getParameter("DescripcionTest");
                test = new Test(nombre, descripcion);
            }
            addBehaviour(new PideFicherosPropios(this.myAgent, tes2, test));
        }
    }

    public class PideFicherosAlumnoBeha extends OneShotBehaviour {

        private Testigo tes2;

        public PideFicherosAlumnoBeha(Agent _a, Testigo tes1) {
            super(_a);
            this.tes2 = tes1;
        }

        public void action() {
            HttpServletRequest request = (HttpServletRequest) tes2.getParametro();
            Test test = null;
            String nombre = request.getParameter("NombreTest");
            if (nombre != null) {
                String descripcion = request.getParameter("DescripcionTest");
                test = new Test(nombre, descripcion);
            }
            addBehaviour(new PideFicherosAlumno(this.myAgent, tes2, test));
        }
    }

    public class PideCasosBeha extends OneShotBehaviour {

        private Testigo tes2;

        public PideCasosBeha(Agent _a, Testigo tes1) {
            super(_a);
            this.tes2 = tes1;
        }

        public void action() {
            HttpServletRequest request = (HttpServletRequest) tes2.getParametro();
            Test test = null;
            String nombre = request.getParameter("NombreTest");
            if (nombre != null) {
                String descripcion = request.getParameter("DescripcionTest");
                test = new Test(nombre, descripcion);
            }
            addBehaviour(new PideCasos(this.myAgent, tes2, test));
        }
    }

    public class PideFicherosINBeha extends OneShotBehaviour {

        private Testigo tes2;

        public PideFicherosINBeha(Agent _a, Testigo tes1) {
            super(_a);
            this.tes2 = tes1;
        }

        public void action() {
            HttpServletRequest request = (HttpServletRequest) tes2.getParametro();
            Caso caso = null;
            String nombre = request.getParameter("NombreCaso");
            if (nombre != null) {
                caso = new Caso(nombre);
            }
            addBehaviour(new PideFicherosIN(this.myAgent, tes2, caso));
        }
    }

    public class PideFicherosOUTBeha extends OneShotBehaviour {

        private Testigo tes2;

        public PideFicherosOUTBeha(Agent _a, Testigo tes1) {
            super(_a);
            this.tes2 = tes1;
        }

        public void action() {
            HttpServletRequest request = (HttpServletRequest) tes2.getParametro();
            Caso caso = null;
            String nombre = request.getParameter("NombreCaso");
            if (nombre != null) {
                caso = new Caso(nombre);
            }
            addBehaviour(new PideFicherosOUT(this.myAgent, tes2, caso));
        }
    }

    public class ModificarPracticaBeha extends OneShotBehaviour {

        private Testigo tes2;

        public ModificarPracticaBeha(Agent _a, Testigo tes1) {
            super(_a);
            this.tes2 = tes1;
        }

        public void action() {
            HttpServletRequest param1 = (HttpServletRequest) tes2.getParametro();
            String descripcion = param1.getParameter("descripcion");
            String fechaEntrega = param1.getParameter("fechaEntrega");
            String nombre = param1.getParameter("nombrePractica");
            Practica pt = new Practica(nombre, descripcion, fechaEntrega);
            addBehaviour(new ModificarPractica(this.myAgent, tes2, pt));
        }
    }

    public class ModificarTestBeha extends OneShotBehaviour {

        private Testigo tes2;

        public ModificarTestBeha(Agent _a, Testigo tes1) {
            super(_a);
            this.tes2 = tes1;
        }

        public void action() {
            HttpServletRequest param1 = (HttpServletRequest) tes2.getParametro();
            String descripcion = param1.getParameter("descripcion");
            String nombre = param1.getParameter("nombreTest");
            Test te = new Test(nombre, descripcion);
            addBehaviour(new ModificarTest(this.myAgent, tes2, te));
        }
    }

    public class ModificarFicherosPropiosBeha extends OneShotBehaviour {

        private Testigo tes2;

        public ModificarFicherosPropiosBeha(Agent _a, Testigo tes1) {
            super(_a);
            this.tes2 = tes1;
        }

        public void action() {
            String param1 = (String) tes2.getParametro();

            StringTokenizer tokenizer = new StringTokenizer(param1, "#");
            String nombre = tokenizer.nextToken();
            String codigo = tokenizer.nextToken();


            FicheroPropio fp = new FicheroPropio(nombre, codigo);
            addBehaviour(new ModificarFicherosPropios(this.myAgent, tes2, fp));

        }
    }

    public class ModificarFicherosINBeha extends OneShotBehaviour {

        private Testigo tes2;

        public ModificarFicherosINBeha(Agent _a, Testigo tes1) {
            super(_a);
            this.tes2 = tes1;
        }

        public void action() {
            String param1 = (String) tes2.getParametro();

            StringTokenizer tokenizer = new StringTokenizer(param1, "#");
            String nombre = tokenizer.nextToken();
            String contenido = tokenizer.nextToken();


            FicheroIN fi = new FicheroIN(nombre, contenido);
            addBehaviour(new ModificarFicherosIN(this.myAgent, tes2, fi));

        }
    }

    public class ModificarFicherosOUTBeha extends OneShotBehaviour {

        private Testigo tes2;

        public ModificarFicherosOUTBeha(Agent _a, Testigo tes1) {
            super(_a);
            this.tes2 = tes1;
        }

        public void action() {
            String param1 = (String) tes2.getParametro();

            StringTokenizer tokenizer = new StringTokenizer(param1, "#");
            String nombre = tokenizer.nextToken();
            String contenido = tokenizer.nextToken();


            FicheroOUT fo = new FicheroOUT(nombre, contenido);
            addBehaviour(new ModificarFicherosOUT(this.myAgent, tes2, fo));

        }
    }

    public class ProcesaTestigo extends OneShotBehaviour {

        private Testigo testigo;

        public ProcesaTestigo(Testigo _tes) {

            this.testigo = _tes;

        }

        public void action() {
            while (this.myAgent == null) {
                System.out.println("AGENT ES NULL");
            }

            InterfazJSPGestor agent = (InterfazJSPGestor) this.myAgent;
            while (!agent.FinSetup) {

                System.out.println("FINSETUP");
            }


            if (this.testigo != null) {

                switch (testigo.getOperacion()) {

                    case autenticar:
                        addBehaviour(new AutenticaRequestBeha(agent, testigo));
                        break;

                    case pedirPracticas:
                        addBehaviour(new PidePracticasBehavior(agent, testigo));
                        break;

                    case modificarPractica:
                        addBehaviour(new ModificarPracticaBeha(agent, testigo));
                        break;

                    case pedirTests:
                        addBehaviour(new PideTestRequestBeha(agent, testigo));
                        break;

                    case modificarTest:
                        addBehaviour(new ModificarTestBeha(agent, testigo));
                        break;

                    case pedirFicherosPropios:
                        addBehaviour(new PideFicherosPropiosBeha(agent, testigo));
                        break;

                    case modificarFicherosPropios:
                        addBehaviour(new ModificarFicherosPropiosBeha(agent, testigo));
                        break;

                    case pedirCasos:
                        addBehaviour(new PideCasosBeha(agent, testigo));
                        break;

                    case pedirFicherosAlumno:
                        addBehaviour(new PideFicherosAlumnoBeha(agent, testigo));
                        break;

                    case pedirFicherosIN:
                        addBehaviour(new PideFicherosINBeha(agent, testigo));
                        break;

                    case pedirFicherosOUT:
                        addBehaviour(new PideFicherosOUTBeha(agent, testigo));
                        break;

                    case modificarFicherosIN:
                        addBehaviour(new ModificarFicherosINBeha(agent, testigo));
                        break;

                    case modificarFicherosOUT:
                        addBehaviour(new ModificarFicherosOUTBeha(agent, testigo));
                        break;

                    case ultimaPractica:
                        testigo.setResultado(ultimaPractica);
                        break;

                    case ultimoTest:
                        testigo.setResultado(ultimoTest);
                        break;

                    case ultimoCaso:
                        testigo.setResultado(ultimoCaso);
                        break;

                    /*
                    case pedirFicheros:
                    testigo.setResultado(agent.doTestPracticasRequest((HttpServletRequest) testigo.getParametro()));
                    break;

                    case insertarFicheros:
                    addBehaviour(new PideFicherosBeha(agent, testigo, (String[]) testigo.getParametro()));
                    break;

                    case corregir:
                    try {
                    String[] contenido = doObtieneContenidoRequest((HttpServletRequest) testigo.getParametro());
                    addBehaviour(new PideCorreccionBeha(agent, testigo, contenido));
                    } catch (IOException e) {

                    // TODO Auto-generated catch block
                    e.printStackTrace();
                    }
                    break;

                    case parsear:
                    testigo.setResultado(ParseaSalida((String) testigo.getParametro()));
                    break;

                    case pedirFicherosFinal:
                    testigo.setResultado((agent.doTestEntregaFinal((HttpServletRequest) testigo.getParametro())));
                    break;

                    case entregarPractica:
                    try {
                    testigo.setResultado((agent.doEntregaFinalRequest((HttpServletRequest) testigo.getParametro())));
                    } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                    }
                    break;

                    case descripcionPractica:
                    addBehaviour(new PideDescripcionPracticaBeha(agent, testigo));
                    break;

                    case modificarPractica:
                    addBehaviour(new ModificarPracticaBeha(agent, testigo));
                    break;

                    case descripcionTest:
                    addBehaviour(new PideDescripcionTestBeha(agent, testigo));
                    break;

                    case pedirFicherosPropios:
                    addBehaviour(new PideFicherosPropiosBeha(agent, testigo));
                    break;

                    case modificarTest:
                    addBehaviour(new ModificarTestBeha(agent, testigo));
                    break;









                     */

                    default:
                        testigo.setResultado("-");
                        break;
                }
            }
        }
    }
    public boolean FinSetup = false;

    /**
     * Method to proccess a testigo object. Create new
     * "ProcesaTestigo" behaviour to proccess <b>one</b>
     * "Testigo" only.
     * (Replace the old "Put2Object" method)
     * @param testigo
     */
    public void sendTestigo(Testigo testigo) {

        this.addBehaviour(new ProcesaTestigo(testigo));

    }

    @Override
    protected void setup() {
        super.setup();
        FinSetup = true;
    }

    public boolean isFinSetup() {
        return FinSetup;
    }
}

