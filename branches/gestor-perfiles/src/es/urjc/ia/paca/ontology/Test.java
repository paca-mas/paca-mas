package es.urjc.ia.paca.ontology;

import jade.content.Concept;

/**
Clase que implementa el concepto Test de la ontolog�a.
Modificado Carlos Sim�n Garc�a
 */
public class Test implements Concept {

    private String Id;
    private String Descripcion;
    private String Ejecutable;

    public Test() {
        this.Id = "";
        this.Descripcion = "";
        this.Ejecutable = "";
    }

    public Test(String id) {
        this.Id = id;
        this.Descripcion = "";
        this.Ejecutable = "";
    }

    public Test(String _id, String _description) {
        this.Id = _id;
        this.Descripcion = _description;
        this.Ejecutable = "";
    }

    public Test(String _id, String _description, String _ejecutable) {
        this.Id = _id;
        this.Descripcion = _description;
        this.Ejecutable = _ejecutable;
    }

    public String getId() {
        return Id;
    }

    public void setId(String i) {
        Id = i;
    }

    public String getDescripcion() {
        return Descripcion;
    }

    public void setDescripcion(String i) {
        Descripcion = i;
    }

    public String getEjecutable() {
        return Ejecutable;
    }

    public void setEjecutable(String i) {
        Ejecutable = i;
    }
}
