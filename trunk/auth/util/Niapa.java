package auth.util;

import jade.wrapper.AgentController;
import PACA.agents.InterfazJSP;
import javax.servlet.http.HttpServletRequest;

public class Niapa {

	private InterfazJSP agente;
	private AgentController Atributo; 
	
	public Niapa(){
		super();
	}
	
	public void wait4Agent(){
		
		while (this.agente==null){
			//System.out.println("ESPERA ACTIVA... ");
					}
	}
	 
	public boolean doAutenticacionRequest(HttpServletRequest request){
		wait4Agent();
		return agente.doAutenticacionRequest(request);
	}
	
	public void setAgent(InterfazJSP ag){
		this.agente=ag;
	}
	
	public void setAlumnoID(String id){
		wait4Agent();
		this.agente.setAlumnoID(id);
	}
	
	public void setAlumnoPass(String pass){
		wait4Agent();
		this.agente.setAlumnoPass(pass);
	}
	
	public String[] doPeticion(){
		wait4Agent();
		return this.agente.doPeticion();
	}
	
	public String[] doPeticionTestPracticaRequest(HttpServletRequest request){
		wait4Agent();
		return agente.doPeticionTestPracticaRequest(request);
	}
	
	public String[] doTestEntregaFinal(HttpServletRequest request){
		wait4Agent();
		return agente.doTestEntregaFinal(request);
	}
	
	public String[] doFicherosPractica(String[] IdTest){
		wait4Agent();
		return agente.doFicherosPractica(IdTest);
	}

	public AgentController getAtributo() {
		return Atributo;
	}

	public void setAtributo(AgentController atributo) {
		Atributo = atributo;
	}
	
		
}
