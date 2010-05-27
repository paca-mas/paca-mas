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

    /***********COMPORTAMIENTOS PARA PEDIR ***************************/
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
            addBehaviour(new PideTestBeha(this.myAgent, tes2, practica, true));
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
                String ejecutable = request.getParameter("EjecutableTest");
                if (ejecutable != null){
                    test = new Test(nombre, descripcion, ejecutable);
                }
                else{
                test = new Test(nombre, descripcion);
                }
            }
            addBehaviour(new PideCasos(this.myAgent, tes2, test, true));
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
        	System.out.println("Eliminar Alumno");
             addBehaviour(new EliminarAlumno(this.myAgent));
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

                    case pedirPracticas:
                        addBehaviour(new PidePracticasBehavior(agent, testigo));
                        break;

                    case pedirTests:
                        addBehaviour(new PideTestRequestBeha(agent, testigo));
                        break;

                    case pedirCasos:
                        addBehaviour(new PideCasosBeha(agent, testigo));
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