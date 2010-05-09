package es.urjc.ia.paca.ontology;

import jade.content.Predicate;
import java.util.*;

public class EstadisticaEvaluacionPractica implements Predicate {

		private List<EstadisticaEvaluacionCaso> Lista = new ArrayList<EstadisticaEvaluacionCaso>();

		 public EstadisticaEvaluacionPractica(List<EstadisticaEvaluacionCaso> e){
		    	this.Lista =  e;
	    }
		 
		public void setList(List<EstadisticaEvaluacionCaso> lista){
			this.Lista = lista;
		}

		public List<EstadisticaEvaluacionCaso> getList(){
			return Lista;
		}

}