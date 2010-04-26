package es.urjc.ia.paca.agents;


import jade.core.behaviours.*;
import jade.lang.acl.ACLMessage;
 
import jade.content.*;
import jade.content.lang.*;
import jade.content.lang.leap.*;
import jade.content.onto.Ontology;
import jade.core.Agent;
import jade.core.AID;
import jade.domain.FIPANames;
import es.urjc.ia.paca.ontology.*;
 
public class CorrectorPrueba extends Agent {

    private ContentManager manager  = (ContentManager)getContentManager();
    private Codec          codec    = new LEAPCodec();
    private Ontology   	   ontology	= pacaOntology.getInstance();
    
	// Mensaje REQUEST
	class SenderBehaviour extends SimpleBehaviour {

		private String nombre;
		
		public SenderBehaviour(Agent a) { 
			super(a); 
			nombre 	 = "gp";
		}
 
		public boolean done() { return true; }
 
		public void action() {
			try {    
// request + remitente + agente receptor + lenguaje + protocolo + contenido				
					ACLMessage mensaje = new ACLMessage(ACLMessage.REQUEST);	// Enviamos un mensaje REQUEST
					mensaje.setSender(getAID());	// Remitente
					AID receptor_msg = new AID(nombre,AID.ISLOCALNAME);	// Agente al que le enviamos el mensaje
					mensaje.addReceiver(receptor_msg);					
					mensaje.setLanguage(codec.getName());	// Lenguaje
					mensaje.setProtocol(FIPANames.InteractionProtocol.FIPA_REQUEST);
					
					EvaluacionCaso evaluacioncaso = new EvaluacionCaso();
					Alumno a = new Alumno ();
					a.setIdentificador("1610");
					Caso c = new Caso ();
					c.setId("2");
					Test t = new Test ();
					t.setId("4");
					Practica p = new Practica ();
					p.setId("1");
					
					evaluacioncaso.setAlumno(a);
					evaluacioncaso.setPractica(p);
					evaluacioncaso.setTest(t);					
					evaluacioncaso.setCaso(c);
					evaluacioncaso.setEvaluacion("apto");

					ContentElement cont = evaluacioncaso;
					
					mensaje.setContentObject(cont);
					
					//manager.fillContent(mensaje, evaluacioncaso);
					send(mensaje);	// enviamos el mensaje
				
				System.out.println( "[" + getLocalName() + "] El mensaje ha sido creado y enviado.");
				
			}
			
			catch(Exception excep) { excep.printStackTrace(); }
		}

	}

	
	// Setup del Agente Emisor
	public void setup() {	
		manager.registerLanguage(codec); 		
		manager.registerOntology(ontology); 
		addBehaviour(new SenderBehaviour(this));	
	}
}