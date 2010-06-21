package es.urjc.ia.paca.agents;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import jade.content.ContentManager;
import jade.content.lang.Codec;
import jade.content.lang.sl.SLCodec;
import jade.content.onto.Ontology;
import jade.content.onto.basic.Action;
import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.SimpleBehaviour;
import jade.domain.FIPANames;
import jade.lang.acl.ACLMessage;
import es.urjc.ia.paca.auth.ontology.AuthOntology;
import es.urjc.ia.paca.ontology.RegistrarEstadisticaEvaluacion;
import es.urjc.ia.paca.ontology.pacaOntology;
import es.urjc.ia.paca.parser.ProcesarXML;

public class CorrectorPrueba extends Agent {
	
    private Codec codec = new SLCodec();
    private ContentManager manager  = (ContentManager)getContentManager();
    private Ontology ontologia = AuthOntology.getInstance();
    private Ontology PACAOntology = pacaOntology.getInstance();
    
	// Mensaje REQUEST
	class SenderBehaviour extends SimpleBehaviour {
	
		public SenderBehaviour(Agent a) { 
			super(a); 
		}
 
		public boolean done() { return true; }

		
		public void ConstruirMensaje (){
			FileReader fr = null;
			String contenido ="";

			try {
			fr = new FileReader("C:/Users/sandrita/workspace_PFC/gestor-estadisticas/src/es/urjc/ia/paca/parser/eval1.xml");
			
			BufferedReader bf = new BufferedReader(fr); 
			String sCadena;
				while ((sCadena = bf.readLine())!=null) {
					contenido = contenido + sCadena;
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
System.out.println(contenido);
			RegistrarEstadisticaEvaluacion evaluacion = ProcesarXML.parsearArchivoXml(contenido);
/*			// ***************************************************************************
			// request + remitente + agente receptor + lenguaje + protocolo + contenido	
			// ***************************************************************************			
			ACLMessage mensaje = new ACLMessage(ACLMessage.REQUEST); // Enviamos un mensaje REQUEST
			mensaje.setSender(getAID()); // Remitente
			AID receptor_msg = new AID("gp",AID.ISLOCALNAME);	// Receptor
			mensaje.addReceiver(receptor_msg); // Receptor					
			mensaje.setLanguage(codec.getName());	// Lenguaje
			mensaje.setProtocol(FIPANames.InteractionProtocol.FIPA_REQUEST); // Protocolo
            mensaje.setOntology(pacaOntology.NAME);

			// Acción
//			AbsAgentAction absaction = null;

			Action act = new Action();
			act.setActor(receptor_msg);
			act.setAction(evaluacion);
			System.out.println(act);
			System.out.println(evaluacion);
			//System.out.println(absaction);
			
			//AbsAgentAction element = (AbsAgentAction)re ;
			try{
				getContentManager().fillContent(mensaje, act);
				//mensaje.setContentObject(contenido_msg);
				send(mensaje); // enviamos el mensaje
System.out.println("CORRECTOR PRUBEA - mensaje enviado");
			}catch (Exception exc) { exc.printStackTrace();	}	*/			
		}
 
		public void action() {
			try {
				ConstruirMensaje();
				System.out.println( "[" + getLocalName() + "] El mensaje ha sido creado y enviado.");
			} catch (Exception e) { e.printStackTrace();	}				
		}
	}

	
	// Setup del Agente Emisor
	public void setup() {	
		manager.registerLanguage(codec); 		
		manager.registerOntology(AuthOntology.getInstance());
		manager.registerOntology(pacaOntology.getInstance());

		/*for (int i = 1; i <= 20; i++) {
			this.doWait(1000);			
			System.out.println(" - " + i);
		}*/
		
		addBehaviour(new SenderBehaviour(this));	
	}
}