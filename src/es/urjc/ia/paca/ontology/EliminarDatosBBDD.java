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
public class EliminarDatosBBDD implements AgentAction{

	private String tipo;
    
    public String getTipo(){
        return tipo;
    }

    public void setTipo(String tipo){
        this.tipo = tipo;
    }
}
