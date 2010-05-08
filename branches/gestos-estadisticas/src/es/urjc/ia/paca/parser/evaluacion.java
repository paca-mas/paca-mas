package es.urjc.ia.paca.parser;

public class evaluacion {

	private String fecha;
	private String iduser;
	private String idpractica;
	private String idtest;
	private String idcaso;
	private String evaluacioncaso;
	   
    //Constructor de la clase
    public evaluacion(String fecha, String iduser, String idpractica,
    		String idtest,
    		String idcaso, String evaluacioncaso){
    	this.fecha = fecha;
    	this.iduser = iduser;
    	this.idpractica=idpractica;
    	this.idtest = idtest;
    	this.idcaso = idcaso;
    	this.evaluacioncaso = evaluacioncaso;
    }
    
    //Metodos Setters y Getters
    public void setFecha(String fecha) { 
    	this.fecha = fecha;
    }
    public String getFecha() { 
        return fecha; 
    }
    
    public void setUser(String iduser) { 
    	this.iduser = iduser;
    }
    public String getUser() { 
        return iduser;
    }
    
    public void setPractica(String idpractica) { 
    	this.idpractica=idpractica;
    }
    public String getPractica() { 
        return idpractica;
    }
    
   
    public void setIdtest(String idtest) { 
        this.idtest = idtest;
    }
    public String getIdtest() { 
        return idtest;
     }

    public void setIdcaso(String idcaso) { 
    	this.idcaso = idcaso;
    }
    public String getIdcaso() { 
        return idcaso;
     }

    public void setEvaluacioncaso(String evaluacioncaso) { 
    	this.evaluacioncaso = evaluacioncaso;
    }
    public String getEvaluacioncaso() { 
        return 	evaluacioncaso;
     }
    
    public String toString() {
    	StringBuffer sb = new StringBuffer();
		sb.append("Datos de descarga ");
		sb.append("Fecha " + getFecha());
		sb.append("Usuario " + getUser());
		sb.append("Idpractica " + getPractica());
		sb.append("Idtest " + getIdtest());
		sb.append("IdCaso " + getIdcaso());
		sb.append("Evaluacion caso " + getEvaluacioncaso());
		return sb.toString();
	}
}