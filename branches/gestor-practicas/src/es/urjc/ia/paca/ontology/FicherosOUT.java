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
public class FicherosOUT implements Predicate{

        private FicheroOUT fo;
        private Caso ca;

    public FicheroOUT getFicheroOUT( )
    {
        return fo;
    }

    public void setFicheroOUT( FicheroOUT i )
    {
        fo = i;
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
