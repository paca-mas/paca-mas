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
public class CreaFicheroOUT implements AgentAction{

    private Test test;
    private Practica practica;
    private Caso caso;
    private FicheroOUT fo;

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

    public FicheroOUT getFicheroOUT() {
        return fo;
    }

    public void setFicheroOUT(FicheroOUT fo) {
        this.fo = fo;
    }

    public Caso getCaso() {
        return caso;
    }

    public void setCaso(Caso caso) {
        this.caso = caso;
    }

}
