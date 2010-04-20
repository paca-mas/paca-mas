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
public class FicherosAlumno implements Predicate{

        private Test te;
        private FicheroAlumno fi;

            public Test getTest( )
    {
        return te;
    }

    public void setTest( Test i )
    {
        te = i;
    }

    public FicheroAlumno getFicheroAlumno( )
    {
        return fi;
    }

    public void setFicheroAlumno( FicheroAlumno i )
    {
        fi = i;
    }

}
