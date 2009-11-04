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

import javax.servlet.http.HttpServletRequest;

import com.oreilly.servlet.multipart.FilePart;
import com.oreilly.servlet.multipart.MultipartParser;
import com.oreilly.servlet.multipart.ParamPart;
import com.oreilly.servlet.multipart.Part;

import es.urjc.ia.paca.parser.EvaluacionParser;
import es.urjc.ia.paca.util.Testigo;

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
            String practica = (String) tes2.getParametro();
            addBehaviour(new PideTestBeha(this.myAgent, tes2, practica));
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

                    case pedirTests:
                        addBehaviour(new PideTestRequestBeha(agent, testigo));
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

                    case modificarFicherosPropios:
                    addBehaviour(new ModificarFicherosPropiosBeha(agent, testigo));
                    break;

                    case pedirCasos:
                    addBehaviour(new PideCasos(agent, testigo));
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

