package es.urjc.ia.web;

import java.io.*;
import java.util.*;

public class LeerHtml {
	
	public static String CrearRutaCompleta (String aux){
		String ruta = Configuracion.DIRDATA + aux + "/";
		return ruta;    			 	
	}
	
	public static String CrearRutaRelativa (String aux){
		String ruta = Configuracion.DIRRELATIVA + aux + "/";
		return ruta;
	}
	
	public static List<String> LeerImagenes (String dir, String nombre) {

		FileReader fr = null;
		BufferedReader br = null;
		List<String> imagenes = new ArrayList<String>();
		try {
			fr = new FileReader (dir + nombre);
			br = new BufferedReader(fr);
			// Lectura del fichero
			String linea;
			String contenido = "";

			while((linea=br.readLine())!=null) {	
				contenido += linea;
			}			
			
			String cadena = nombre + "_files/img";
			int i = 0;
			i = contenido.indexOf(cadena);

			while (i!=-1) {
				int fin = contenido.indexOf("\"",i);
				imagenes.add(contenido.substring(i,fin));
				i = contenido.indexOf(cadena,fin);
			}
			
		}catch(Exception e){e.printStackTrace();}
		finally{
			// En el finally cerramos el fichero, para asegurarnos
			// que se cierra tanto si todo va bien como si salta 
			// una excepcion.
			try{                    
				if( null != fr ){   
					fr.close();     
				}                  
			}catch (Exception e2){e2.printStackTrace();}
		}

		return imagenes;
	}

	public static String LeerFicheroHtml (String dirHtml, String nombre){

		String contenido = null;		
	 	FileReader fr = null;
		BufferedReader br = null;
		
		try {
			fr = new FileReader (dirHtml + "Informe_Caso.html");
			br = new BufferedReader(fr);
			// Lectura del fichero
			String linea;
			int num_linea = 0;
			System.out.println (dirHtml + "Informe_Caso.html");
			while( (linea=br.readLine())!=null )  {
				num_linea ++;
				if ((num_linea>=10) || (linea!="</body>") || (linea!="</html>")){
					contenido += linea;
				}
			}			  					
		}catch(Exception e){e.printStackTrace();}
		finally{
			// En el finally cerramos el fichero, para asegurarnos
			// que se cierra tanto si todo va bien como si salta 
			// una excepcion.
			try{                    
				if( null != fr ){   
					fr.close();     
				}                  
			}catch (Exception e2){e2.printStackTrace();}
		}
		return contenido;
	}
}