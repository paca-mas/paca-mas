package es.urjc.ia.paca.ontology;

import jade.content.Concept;



/**
   Clase que implementa el concepto Practica de la ontolog�a.
   Modificado Alvaro Blázquez
 */
public class Practica implements Concept{


    private String FechaEntrega;
    private String Id;
    private String Descripcion;

    public Practica () {
            Id = "";
            Descripcion = "";
            FechaEntrega = "";
    }

    public Practica ( String nombre) {
           Id = nombre;
           Descripcion = "";
           FechaEntrega = "";
    }

    public Practica (String _id, String _description) {
           this.Id = _id;
           this.Descripcion = _description;
           this.FechaEntrega= "";
    }

    public Practica (String _id, String _description, String FechaEntrega) {
           this.Id = _id;
           this.Descripcion = _description;
           this.FechaEntrega = FechaEntrega;
    }

    public String getDescripcion () {
        return Descripcion;
    }

    public void setDescripcion (String val) {
            this.Descripcion = val;
    }

    public String getId () {
        return Id;
    }

    public void setId (String val) {
            this.Id = val;
    }

    public String getFechaEntrega () {
        return FechaEntrega;
    }

    public void setFechaEntrega (String val) {
        this.FechaEntrega = val;
    }

}
