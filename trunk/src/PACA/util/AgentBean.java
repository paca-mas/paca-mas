package PACA.util;

import jade.wrapper.AgentController;
import PACA.agents.InterfazJSP;
import javax.servlet.http.HttpServletRequest;

public class AgentBean {

	private InterfazJSP agente;
	private AgentController Atributo;

	public AgentBean() {
		super();
	}

	public void wait4Agent() {

		while (this.agente == null) {
		//System.out.println("ESPERA ACTIVA... ");
		}
	}

	public void setAgentInterfaz(InterfazJSP ag) {
		this.agente = ag;
	}

	public InterfazJSP getAgentInterfaz() {
		wait4Agent();
		return this.agente;
	}

	
	public void sendTestigo(Testigo testigo) {
		wait4Agent();
		this.agente.sendTestigo(testigo);
	}
	
	public void setAlumnoID(String id) {
		wait4Agent();
		this.agente.setAlumnoID(id);
	}

	public void setAlumnoPass(String pass) {
		wait4Agent();
		this.agente.setAlumnoPass(pass);
	}

	public AgentController getAgentController() {
		return Atributo;
	}

	public void setAgentController(AgentController atributo) {
		Atributo = atributo;
	}
}
