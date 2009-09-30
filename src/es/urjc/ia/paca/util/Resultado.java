package es.urjc.ia.paca.util;

public class Resultado {
	
	private Object resultado;
	private boolean relleno;
	private boolean resultadoB;
	
	
	//public Resultado(){
		//resultado="";
		//relleno=false;
	//}
	
	public synchronized Object getResultado() {
		while (!this.isRelleno()){
			try {
				this.wait();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return resultado;
	}
	
	public synchronized void setResultado(Object resultado) {
		this.resultado = resultado;
		this.relleno=true;
		this.notify();
	}
	
	public synchronized boolean isResultadoB() {
		while (!this.isRelleno()){
			try {
				this.wait();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return resultadoB;
	}


	public synchronized void setResultadoB(boolean resultadoB) {
		this.resultadoB = resultadoB;
		this.relleno=true;
		this.notify();
	}
	
	public synchronized boolean isRelleno(){
		return relleno;
	}

}
