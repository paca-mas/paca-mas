package es.urjc.ia.paca.util;

public class Testigo extends Resultado{
	
	//Operaciones es un tipo enumerado en donde definimos las operaciones que queremos realizar
	public enum Operaciones {
		autenticar, pedirPracticas, pedirTests, pedirFicheros,insertarFicheros,
		buscarCorrector, corregir, parsear, entregarPractica, pedirFicherosFinal, descripcionPractica,
		modificarPractica, descripcionTest, pedirFicherosPropios, modificarTest, modificarFicherosPropios,
                pedirCasos, pedirFicherosIN, pedirFicherosOUT, modificarFicherosIN, modificarFicherosOUT,
                pedirFicherosAlumno, ultimaPractica, ultimoTest, ultimoCaso, crearPractica, crearTest, crearFicheroPropio,
                crearFicheroAlumno, crearCaso, crearFicheroIN, crearFicheroOUT
	}
	
	private Object resultado;
	private boolean relleno;
	private Object parametro;
	private Operaciones operacion;
	//private boolean resultadoB;
	
	
	public Testigo(){
		resultado="";
		relleno=false;
	}
	

	//public Object getResultado() {
		//return resultado;
	//}

	//public void setResultado(Object resultado) {
		//this.resultado = resultado;
		//this.relleno=true;
	//}
	
	//public boolean isRelleno(){
		//return relleno;
	//}


	public Operaciones getOperacion() {
		return operacion;
	}


	public void setOperacion(Operaciones operacion) {
		this.operacion = operacion;
	}


	public Object getParametro() {
		return parametro;
	}


	public void setParametro(Object parametro) {
		this.parametro = parametro;
	}


	//public boolean isResultadoB() {
		//return resultadoB;
	//}


	//public void setResultadoB(boolean resultadoB) {
		//this.resultadoB = resultadoB;
		//this.relleno=true;
	//}
	
	public String toString(){
		return "Es relleno:"+this.isRelleno()+
		"Operacion: "+this.getOperacion()+
		"Parametro: "+this.getParametro();
		}
		
	
	
	
	
	
	

}
