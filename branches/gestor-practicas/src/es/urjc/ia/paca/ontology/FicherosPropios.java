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
public class FicherosPropios implements Predicate{

    private Test te;
    private FicheroPropio fp;

    public FicheroPropio getFicheroPropio(){
            return fp;
    }

    public void setFicheroPropio( FicheroPropio i){
            fp = i;
    }

        public Test getTest( )
    {
        return te;
    }

    public void setTest( Test i )
    {
        te = i;
    }

}
