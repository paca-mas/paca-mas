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

        public EliminarPracticaBeha(Agent _a) {
            super(_a);
        }

        public void action() {
             addBehaviour(new EliminarPractica(this.myAgent));
        }
    }

    public class EliminarAlumnoBeha extends OneShotBehaviour {

        public EliminarAlumnoBeha(Agent _a) {
            super(_a);
        }

        public void action() {
        	System.out.println("[INTERFAZ JSP] - Eliminar Alumno");
             addBehaviour(new EliminarAlumno(this.myAgent));
        }
    }
    
    public class EliminarDatosBeha extends OneShotBehaviour {

        public EliminarDatosBeha(Agent _a) {
            super(_a);
        }

        public void action() {
        	System.out.println("[INTERFAZ JSP] - Eliminar Datos");        	
             addBehaviour(new EliminarDatos(this.myAgent));
        }
    }

    public class CargarPracticaBeha extends OneShotBehaviour {

        public CargarPracticaBeha(Agent _a) {
            super(_a);
        }

        public void action() {
        	System.out.println("[INTERFAZ JSP] - Cargar Practica");
             addBehaviour(new CargarPractica(this.myAgent));
        }
    }
    
    public class CargarAlumnoBeha extends OneShotBehaviour {

        public CargarAlumnoBeha(Agent _a) {
            super(_a);
        }

        public void action() {
        	System.out.println("[INTERFAZ JSP] - Cargar Alumno");
        	addBehaviour(new CargarAlumno(this.myAgent));
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
                        
                    case ultimaPractica:
                        testigo.setResultado(ultimaPractica);
                        break;

                    case ultimoTest:
                        testigo.setResultado(ultimoTest);
                        break;

                    case ultimoCaso:
                        testigo.setResultado(ultimoCaso);
                        break;

                    case eliminarPractica:
                        addBehaviour(new EliminarPracticaBeha(agent));
                        break;

                    case eliminarAlumno:
                        addBehaviour(new EliminarAlumnoBeha(agent));
                    	break;
                        
                    case cargarPractica:
                        addBehaviour(new CargarPracticaBeha(agent));
                        break;

                    case cargarAlumno:
                        addBehaviour(new CargarAlumnoBeha(agent));
                        break;
                    
                    case eliminarDatos:
                        addBehaviour(new EliminarDatosBeha(agent));
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