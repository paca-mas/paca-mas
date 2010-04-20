/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package es.urjc.ia.paca.util;

import jade.wrapper.AgentController;
import es.urjc.ia.paca.agents.InterfazJSPGestor;

public class AgentBeanGestor {

	private InterfazJSPGestor agente;
	private AgentController Atributo;

	public AgentBeanGestor() {
		super();
	}

	public void wait4Agent() {

		while (this.agente == null) {
		//System.out.println("ESPERA ACTIVA... ");
		}
	}

	public void setAgentInterfaz(InterfazJSPGestor ag) {
		this.agente = ag;
	}

	public InterfazJSPGestor getAgentInterfaz() {
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

