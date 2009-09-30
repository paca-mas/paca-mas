/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package es.urjc.ia.paca.ontology;

/**
 *
 * @author alvaro
 */
import jade.content.Concept;

public class Caso implements Concept{


    private String Id;


    public Caso () {
            this.Id = "";
    }

    public Caso (String Id){
            this.Id = Id;
    }


    public String getId () {
        return Id;
    }


    public void setId (String val) {
        this.Id = val;
    }

}
