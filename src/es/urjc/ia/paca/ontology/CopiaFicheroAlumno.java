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
public class CopiaFicheroAlumno implements AgentAction {

    private Practica CopyPractica;
    private Test CopyTest;
    private FicheroAlumno Copyfp;


    public Test getCopyTest() {
        return CopyTest;
    }

    public void setCopyTest(Test test) {
        this.CopyTest = test;
    }

    public Practica getCopyPractica() {
        return CopyPractica;
    }

    public void setCopyPractica(Practica CopyPractica) {
        this.CopyPractica = CopyPractica;
    }

        public void setCopyFicheroAlumno(FicheroAlumno fp){
        this.Copyfp = fp;
    }

    public FicheroAlumno getCopyFicheroAlumno(){
        return Copyfp;
    }
}

