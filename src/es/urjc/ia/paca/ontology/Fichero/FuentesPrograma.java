package es.urjc.ia.paca.ontology.Fichero;

//Modificacion Alvaro Blazquez

public class FuentesPrograma extends Fichero {


    private String Codigo;


    public FuentesPrograma () {
            super();
            Codigo = "";
    }

    public FuentesPrograma (String nombre){
            super(nombre);
            this.Codigo = "";
    }

    public FuentesPrograma (String nombre, String Codigo){
            super(nombre);
            this.Codigo = Codigo;
    }


    public String getCodigo () {
        return Codigo;
    }

    public void setCodigo (String val) {
        this.Codigo = val;
    }

}