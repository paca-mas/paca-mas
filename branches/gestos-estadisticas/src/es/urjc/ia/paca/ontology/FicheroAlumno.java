/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package es.urjc.ia.paca.ontology;

import es.urjc.ia.paca.ontology.Fichero.FuentesPrograma;

/**
 *
 * @author alvaro
 */
public class FicheroAlumno extends FuentesPrograma{


    public FicheroAlumno () {
            super();
    }

    public FicheroAlumno (String nombre){
            super(nombre);
    }

    public FicheroAlumno (String nombre, String Codigo){
            super(nombre, Codigo);
    }


}
