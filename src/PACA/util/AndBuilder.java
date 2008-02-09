package PACA.util;

import jade.content.abs.AbsPredicate;
import jade.content.lang.sl.SL1Vocabulary;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;

/**
* AntBuilder. Creates SL1Vocabulary.AND predicates from:
* <ul>
*    <li> AbsPredicates </li>
*    <li> Collection of AbsPredicates</li>
* </ul>
* Put all predicates in a <b>big AND</big> predicate.
* 
* AntBuilder can be use too for extract information from a big and 
* heterogeneous AND (in term of predicates's types). Adding a AbsPredicate 
* AND, and using the method <code>getPredicateList(predicateType)</code>
* can extract a list with all the predicates of the specific type
*/
public class AndBuilder {

	private Hashtable<String, List<AbsPredicate>> tabla;

	/**
	* Create a new and empty AndBuilder	
	*/
	public AndBuilder() {
		this.tabla = new Hashtable<String, List<AbsPredicate>>();
	}

	/**
	* Add new single predicate (or complex with a lot of "ANDs") to
	* the builder. Internaly remove all the ANDs predicates and only store
	* the rest of the predicates
	* @param predicado AbsPredicate to add
	*/
	public void addPredicate(AbsPredicate predicado) {
		AbsPredicate auxIzda = null;
		AbsPredicate auxDcha = null;

		if (predicado.getTypeName().equals("and")) {
			auxIzda = (AbsPredicate) predicado
					.getAbsObject(SL1Vocabulary.AND_LEFT);
			auxDcha = (AbsPredicate) predicado
					.getAbsObject(SL1Vocabulary.AND_RIGHT);
			this.addPredicate(auxIzda);
			this.addPredicate(auxDcha);
		} else {
			if (this.tabla.containsKey(predicado.getTypeName())) {
				List<AbsPredicate> listaAux3 = new ArrayList<AbsPredicate>();
				listaAux3 = this.tabla.get(predicado.getTypeName());
				listaAux3.add(predicado);
				this.tabla.put(predicado.getTypeName(), listaAux3);
			} else {
				List<AbsPredicate> listaAux2 = new ArrayList<AbsPredicate>();
				listaAux2.add(predicado);
				this.tabla.put(predicado.getTypeName(), listaAux2);
			}
		}
	}
	
	
	/**
	* Return a list of predicates in that had been added to the AnbBuilder
	* (with <code>addPredicate</code> or <code>addPredicates</code>) with the
	* specific type.
	* @param tipo Type in the ontology to look for in the predicates
	* @return List of all predicates that match with the specify type
	*/
	public List<AbsPredicate> getPredicateList(String tipo){
		
		if (this.tabla.containsValue(tipo)){
			return this.tabla.get(tipo);
		}
		else{
			return new ArrayList<AbsPredicate>();
		}
				
	}

	/**
	* Similary to <code>addPredicate</code> with a list of
	* AbsPredicates. Interate in <code>coleccion</code> and use the basic
	* <code>addPredicate</code>
	* @param coleccion collection of predicates to add to the builder
	*/
	public void addPredicates(Collection<AbsPredicate> coleccion) {

		for (AbsPredicate p : coleccion) {
			this.addPredicate(p);
		}

	}

	/**
	* Static method that creates create a complex "AND" predicate from
	* all the predicates in the coleccion parameter
	* @param coleccion of predicates to concatenate in a big AND precidate
	* @return And Predicate with all the predicates in the coleccion
	*/
	public static AbsPredicate buildAndfromList(List<AbsPredicate> coleccion) {
		AbsPredicate resultado = null;
		AbsPredicate predicadoAnd = new AbsPredicate(SL1Vocabulary.AND);
		AbsPredicate predicadoAnd2 = new AbsPredicate(SL1Vocabulary.AND);

		int tamano = coleccion.size();

		try {
			if (tamano == 1) {
				resultado = (AbsPredicate) coleccion.get(0);
				return resultado;
			} else {
				AbsPredicate element = (AbsPredicate) coleccion.get(0);
				predicadoAnd.set(SL1Vocabulary.AND_LEFT, element);
				element = (AbsPredicate) coleccion.get(1);
				predicadoAnd.set(SL1Vocabulary.AND_RIGHT, element);

				int i = 2;
				while (i < tamano) {
					element = (AbsPredicate) coleccion.get(i);
					predicadoAnd2.set(SL1Vocabulary.AND_LEFT, predicadoAnd);
					predicadoAnd2.set(SL1Vocabulary.AND_RIGHT, element);
					predicadoAnd = new AbsPredicate(SL1Vocabulary.AND);
					predicadoAnd = predicadoAnd2;
					predicadoAnd2 = new AbsPredicate(SL1Vocabulary.AND);
					i++;
				}
			}
		} catch (Exception oe) {
			oe.printStackTrace();
		}

		return predicadoAnd;
	}
	
	/**
	* Create an AND predicate from all of predicates added in the objet
	* @return And predicte with all the predicates in the object
	*/
	public AbsPredicate getAnd(){
		Collection<List<AbsPredicate>> coleccion = this.tabla.values();
		List<AbsPredicate> lista = new ArrayList<AbsPredicate>();
		for(List<AbsPredicate>l:coleccion){
			lista.addAll(l);
		}
		return buildAndfromList(lista);
		
	}
	
	

	

}
