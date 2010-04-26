package es.urjc.ia.paca;

import jade.core.Runtime;
import jade.core.Profile;
import jade.core.ProfileImpl;
import es.urjc.ia.paca.agents.*;
import jade.wrapper.*;

/**
 *
 * @author rormartin
 */
public class Prueba {


	public static void main(String args[]) {

		try {

			// Get a hold on JADE runtime
			Runtime rt = Runtime.instance();
			System.out.println("Runtime created ...");

			// Exit the JVM when there are no more containers around
			rt.setCloseVM(true);

			// Launch a complete platform on the 1099 port
			// create a default Profile
			Profile pMain = new ProfileImpl(null, 1099, null);

			System.out.println("Launching a whole in-process platform..." + pMain);
			AgentContainer mc = rt.createMainContainer(pMain);

			// RMA (-gui)
			AgentController rma = mc.createNewAgent("rma", "jade.tools.rma.rma", new Object[0]);
			rma.start();

			// Default profile
			Profile p = new ProfileImpl(false);
			System.out.println("Profile created ...");

			// Create a non-main container for PACA agents
			AgentContainer ac = rt.createAgentContainer(p);
			System.out.println("Container created ...");

			// Create auth agent
			AgentController auth = ac.createNewAgent("gp", "es.urjc.ia.paca.agents.GestorEstadisticas", new Object[0]);
			// Create corrector agent
			AgentController corrector = ac.createNewAgent("pc", "es.urjc.ia.paca.agents.CorrectorPrueba", new Object[0]);
			// Create gestor agent
            //AgentController gestor = ac.createNewAgent("gestorDePracticas", "es.urjc.ia.paca.agents.GestorPracticas", new Object[0]);

			// Start agents
			auth.start();
			corrector.start();
            //gestor.start();

			return;

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

}