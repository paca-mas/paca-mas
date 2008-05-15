package PACA.agents;

import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import jade.core.Profile;
import jade.core.ProfileImpl;
import jade.core.Runtime;
import jade.wrapper.ContainerController;

public class LanzadorCli {
	
		
	public static void GeneraRetardo(int ret){
		try {
			Thread.sleep(ret);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public static void main( String args[] ) {
		Runtime rt = Runtime.instance();
		Profile p = new ProfileImpl(false);
		ContainerController cc = rt.createAgentContainer(p);
		for( int i=0; i < args.length; i++ ){
			System.out.println( args[i] );
		}	
		
		String politica = args[0];
		String numero  = args[1];
		String nCorreciones = args[2];
		String practica = args[3];
		String tests = args[4];
		String tamano = args[5];
		String nombreFichero = args[6];
		String porcentaje = args[7];
		
		int numThreads = Integer.valueOf(numero);
		int numeroCorreciones = Integer.valueOf(nCorreciones);
		int totalCorrec = numeroCorreciones / numThreads;
		int npract = Integer.valueOf(practica);
		int ntests = Integer.valueOf(tests);
		int ntamano = Integer.valueOf(tamano);
		int nporcentaje = Integer.valueOf(porcentaje);
		
		ExecutorService pool = Executors.newFixedThreadPool(numThreads);
		
		for (int i = 0; i < numThreads; i++) {
			
			//System.out.println("Lanzamos: "+i);
							
			
			//------------------ NO BORRAR -----------------
			SimulaAgentes sim1 = new SimulaAgentes(cc, i, politica, totalCorrec, npract,
													ntests, ntamano, nombreFichero, nporcentaje);
			pool.execute(sim1);
			//------------------ FIN NO BORRAR -------------
			//GeneraRetardo(50);

		}
			
	}
}  


