/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package es.urjc.ia.paca.ontology;

import jade.content.Predicate;

/**
 *
 * @author alvaro
 */
public class FicherosIN implements Predicate{

        private FicheroIN fi;
        private Caso ca;

    public FicheroIN getFicheroIN( )
    {
        return fi;
    }

    public void setFicheroIN( FicheroIN i )
    {
        fi = i;
    }

    public Caso getCaso( )
    {
        return ca;
    }

    public void setCaso( Caso i )
    {
        ca = i;
    }

}