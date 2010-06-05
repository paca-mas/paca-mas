/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package es.urjc.ia.paca.agents;

import jade.core.Agent;
import jade.core.behaviours.OneShotBehaviour;

import javax.servlet.http.HttpServletRequest;

import es.urjc.ia.paca.ontology.Caso;
import es.urjc.ia.paca.ontology.FicheroAlumno;
import es.urjc.ia.paca.ontology.FicheroIN;
import es.urjc.ia.paca.ontology.FicheroOUT;
import es.urjc.ia.paca.ontology.FicheroPropio;
import es.urjc.ia.paca.ontology.Practica;
import es.urjc.ia.paca.ontology.Test;
import es.urjc.ia.paca.util.Testigo;
import java.util.StringTokenizer;

/**
 *
 * @author alvaro
 */
public class InterfazJSPGestorEstadistica extends InterfazGestorEstadistica {

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

    public class EliminarPracticaBeha extends OneShotBehaviour {
    	 
    	private Testigo tes;

        public EliminarPracticaBeha(Agent _a, Testigo tes1) {
            super(_a);
            this.tes = tes1;
        }

        public void action() {
             addBehaviour(new EliminarPractica(this.myAgent, tes));
        }
    }

    public class EliminarAlumnoBeha extends OneShotBehaviour {

    	private Testigo tes;

        public EliminarAlumnoBeha(Agent _a, Testigo tes1) {
            super(_a);
            this.tes = tes1;
        }

        public void action() {
             addBehaviour(new EliminarAlumno(this.myAgent, tes));
        }
    }
    
    public class EliminarDatosBeha extends OneShotBehaviour {

    	private Testigo tes;

        public EliminarDatosBeha(Agent _a, Testigo tes1) {
            super(_a);
            this.tes = tes1;
        }

        public void action() {
             addBehaviour(new EliminarDatos(this.myAgent,tes));
        }
    }

    public class CargarPracticaBeha extends OneShotBehaviour {

    	private Testigo tes;

        public CargarPracticaBeha(Agent _a, Testigo tes1) {
            super(_a);
            this.tes = tes1;
        }

        public void action() {
             addBehaviour(new CargarPractica(this.myAgent, tes));
        }
    }
    
    public class CargarAlumnoBeha extends OneShotBehaviour {

    	private Testigo tes;

        public CargarAlumnoBeha(Agent _a, Testigo tes1) {
            super(_a);
            this.tes = tes1;
        }
        public void action() {
        	addBehaviour(new CargarAlumno(this.myAgent,tes));
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
            InterfazJSPGestorEstadistica agent = (InterfazJSPGestorEstadistica) this.myAgent;
            while (!agent.FinSetup) {
                System.out.println("FINSETUP");
            }

            if (this.testigo != null) {
                switch (testigo.getOperacion()) {
                    case autenticar:
                        addBehaviour(new AutenticaRequestBeha(agent, testigo));
                        break;
                    case eliminarPractica:
                        addBehaviour(new EliminarPracticaBeha(agent, testigo));
                        break;
                    case eliminarAlumno:
                        addBehaviour(new EliminarAlumnoBeha(agent, testigo));
                    	break;
                    case eliminarDatos:
                        addBehaviour(new EliminarDatosBeha(agent, testigo));
                        break;
                    case cargarPractica:
                        addBehaviour(new CargarPracticaBeha(agent, testigo));
                        break;
                    case cargarAlumno:
                        addBehaviour(new CargarAlumnoBeha(agent, testigo));
                        break;
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