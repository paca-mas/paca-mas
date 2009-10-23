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
public class ModificaFicheroPropio implements AgentAction {

    private Test test;
    private Practica practica;
    private FicheroPropio fp;

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

    public FicheroPropio getFicheroPropio() {
        return fp;
    }

    public void setFicheroPropio(FicheroPropio fp) {
        this.fp = fp;
    }
}
