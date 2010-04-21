package es.urjc.ia.paca.ontology;

import jade.content.AgentAction;;

/**
 *
 * @author alvaro
 */
public class CopiaCaso implements AgentAction {

    private Test test;
    private Practica practica1;
    private Caso caso;
    private Practica CopyPractica;
    private Test CopyTest;
    private Caso CopyCaso;

    public Test getTest() {
        return test;
    }

    public void setTest(Test test) {
        this.test = test;
    }

    public Test getCopyTest() {
        return CopyTest;
    }

    public void setCopyTest(Test test) {
        this.CopyTest = test;
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

    public Caso getCaso() {
        return caso;
    }

    public void setCaso(Caso caso) {
        this.caso = caso;
    }

    public Caso getCopyCaso() {
        return CopyCaso;
    }

    public void setCopyCaso(Caso caso) {
        this.CopyCaso = caso;
    }
}

