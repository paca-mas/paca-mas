package es.urjc.ia.paca.ontology;

import jade.content.AgentAction;



/**
 *
 * @author alvaro
 */
public class CopiaFicheroOUT implements AgentAction {

    private Practica CopyPractica;
    private Test CopyTest;
    private Caso CopyCaso;
    private FicheroOUT CopyFicheroOUT;

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

    public Caso getCopyCaso() {
        return CopyCaso;
    }

    public void setCopyCaso(Caso caso) {
        this.CopyCaso = caso;
    }

    public FicheroOUT getCopyFicheroOUT() {
        return CopyFicheroOUT;
    }

    public void setCopyFicheroOUT(FicheroOUT ficheroOUT) {
        this.CopyFicheroOUT = ficheroOUT;
    }
}
