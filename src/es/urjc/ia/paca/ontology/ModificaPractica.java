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
public class ModificaPractica implements AgentAction{

    private Practica practica;

    public Practica getPractica(){
        return practica;
    }

    public void setPractica(Practica practica){
        this.practica = practica;
    }

}
