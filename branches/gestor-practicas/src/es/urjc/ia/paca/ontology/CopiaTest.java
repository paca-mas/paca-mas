/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package es.urjc.ia.paca.ontology;

import jade.content.AgentAction;
import jade.content.Predicate;
import jade.content.abs.AbsPredicate;

/**
 *
 * @author alvaro
 */
public class CopiaTest implements AgentAction {

    private Test test;
    private Practica practica1;
    private Practica CopyPractica;

    public Test getTest() {
        return test;
    }

    public void setTest(Test test) {
        this.test = test;
    }

    public Practica getPractica() {
        return practica1;
    }


    public void setPractica(Practica practica) {
        this.practica1 = practica;
    }

        public Practica getCopyPractica() {
        return CopyPractica;
    }


    public void setCopyPractica(Practica CopyPractica) {
        this.CopyPractica = CopyPractica;
    }

}
