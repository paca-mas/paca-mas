/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package es.urjc.ia.paca.ontology;

import jade.content.AgentAction;

/**
 *
 * @author alvaro
 */
public class EliminaFicheroAlumno implements AgentAction {

    private Test test;
    private Practica practica;
    private FicheroAlumno fa;

    public Test getTest() {
        return test;
    }

    public void setTest(Test test) {
        this.test = test;
    }

    public Practica getPractica() {
        return practica;
    }

    public void setPractica(Practica practica) {
        this.practica = practica;
    }

    public FicheroAlumno getFicheroAlumno() {
        return fa;
    }

    public void setFicheroAlumno(FicheroAlumno fa) {
        this.fa = fa;
    }
}
