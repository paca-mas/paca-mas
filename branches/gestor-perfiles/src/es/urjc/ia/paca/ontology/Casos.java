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
public class Casos implements Predicate{
        private Test te;
        private Caso ca;

    public Test getTest( )
    {
        return te;
    }

    public void setTest( Test i )
    {
        te = i;
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
