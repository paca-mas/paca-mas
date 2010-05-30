package es.urjc.ia.paca.ontology;

import jade.content.AgentAction;
import jade.util.leap.ArrayList;
import jade.util.leap.Iterator;
import jade.util.leap.List;

public class RegistrarEstadisticaEvaluacion implements AgentAction {
	
		private List evaluacionesCasos;

		public RegistrarEstadisticaEvaluacion() {
			evaluacionesCasos = new ArrayList();
		}
		
		public RegistrarEstadisticaEvaluacion(List e){
		    	this.evaluacionesCasos =  e;
	    }
		 
		public void setEvaluacionesCasos(List lista){
			this.evaluacionesCasos = lista;
		}

		public List getEvaluacionesCasos(){
			return evaluacionesCasos;
		}

		public String toString() {
			String repr = "(RegistrarEstadisticaEvaluacionPractica \r\n";
			Iterator it = this.evaluacionesCasos.iterator();
			while (it.hasNext()) {
				repr += "    " + it.next() + "\r\n";
			}
			repr += ")";
			return repr;			
		}
}