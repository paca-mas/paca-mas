package es.urjc.ia.paca.agents;
import java.io.Serializable;
import java.sql.Connection;
import java.sql.Statement;
import es.urjc.ia.baseDatos.*;
import jade.core.*;
import jade.core.behaviours.*;
import jade.lang.acl.ACLMessage;
import jade.content.*;
import jade.content.lang.leap.*;
import jade.content.lang.Codec;
import jade.content.lang.sl.SLCodec;
import jade.content.onto.Ontology;
import es.urjc.ia.paca.ontology.*;

public class GestorEstadisticas extends Agent {

    private ContentManager manager     = (ContentManager)getContentManager();
    private Codec          codec       = new LEAPCodec();
	private Ontology       ontology    = pacaOntology.getInstance();
	
	// Clase que recibe un mensaje REQUEST y envia un mensaje AGREE/REFUSE/NOT-UNDERSTOOD
    class ReceiverBehaviour extends Behaviour {
		
		private boolean finished = false; 
		public boolean done() { return finished; }
 
		public void action() {        
			try {			
				ACLMessage mensaje = blockingReceive();
				System.out.println("[CALCULADORA] antes de recibir ... ");
				if (mensaje!= null) {
						// Si el mensaje es REQUEST	
						if( mensaje.getPerformative()==ACLMessage.REQUEST){
							System.out.println("[CALCULADORA] request ... ");
							Serializable contenido = mensaje.getContentObject();
							//ContentElement contenido = manager.extractContent(mensaje);
							System.out.println("[CALCULADORA] contenido ...");
							if(contenido instanceof EstadisticaEvaluacionCaso) {					
								EstadisticaEvaluacionCaso ec = (EstadisticaEvaluacionCaso)contenido;								
								Alumno alumno = ec.getAlumno();
								Caso caso = ec.getCaso();
								Practica practica = ec.getPractica();
								Test test = ec.getTest();
								String evaluacion = ec.getEvaluacion();

								String a = alumno.getIdentificador();
								String p = practica.getId();
								String t = test.getId();
								String c = caso.getId();
								
								String sentencia = "INSERT INTO `sandra2`(" +
								   "`id_practica`,`id_caso`,"
								 + "`id_test`,`evaluacion`,`id_alumno`)	VALUE (" 
								 + p + "," + c + "," 
								 + t + ",'" + evaluacion + "'," + a + ")";
								
								System.out.println(sentencia);
								
								Connection conexion = null;
								try
								{
									conexion = es.urjc.ia.baseDatos.Conexion_bbdd.getConexion(conexion);			
									// Conexion con bd
									if (!conexion.isClosed()){				
										Statement st = conexion.createStatement();
										st.execute(sentencia);     		
								
										st.close();        	    
										// cierre de la conexion
										Conexion_bbdd.closeConexion(conexion);
									}
								}catch (Exception e){e.printStackTrace();}
							}
						}			
				}
			} catch(Exception excep) { excep.printStackTrace(); }	
		}
    }


    // Setup del Agente 
    protected void setup() {
    	manager.registerLanguage(codec);
    	manager.registerOntology(ontology);
System.out.println("[CALCULADORA] antes de todo ... ");
    	addBehaviour(new ReceiverBehaviour());

    }
}
 
 
 