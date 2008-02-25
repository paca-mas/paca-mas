package PACA.util;

public class Resultado {
	
	private Object resultado;
	private boolean relleno;
	private boolean resultadoB;
	
	
	//public Resultado(){
		//resultado="";
		//relleno=false;
	//}
	
	public Object getResultado() {
		return resultado;
	}
	
	public void setResultado(Object resultado) {
		this.resultado = resultado;
		this.relleno=true;
	}
	
	public boolean isResultadoB() {
		return resultadoB;
	}


	public void setResultadoB(boolean resultadoB) {
		this.resultadoB = resultadoB;
		this.relleno=true;
	}
	
	public boolean isRelleno(){
		return relleno;
	}

}
