package PACA.util;

import jade.content.abs.AbsPredicate;
import jade.content.lang.sl.SL1Vocabulary;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;

public class AndBuilder {

	private Hashtable<String, List<AbsPredicate>> tabla;

	public AndBuilder() {
		this.tabla = new Hashtable<String, List<AbsPredicate>>();
	}

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
	
	public List<AbsPredicate> getPredicateList(String tipo){
		
		if (this.tabla.containsValue(tipo)){
			return this.tabla.get(tipo);
		}
		else{
			return new ArrayList<AbsPredicate>();
		}
				
	}

	public void addPredicates(Collection<AbsPredicate> coleccion) {

		for (AbsPredicate p : coleccion) {
			this.addPredicate(p);
		}

	}

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
	
	
	public AbsPredicate getAnd(){
		Collection<List<AbsPredicate>> coleccion = this.tabla.values();
		List<AbsPredicate> lista = new ArrayList<AbsPredicate>();
		for(List<AbsPredicate>l:coleccion){
			lista.addAll(l);
		}
		return buildAndfromList(lista);
		
	}
	
	

	

}
