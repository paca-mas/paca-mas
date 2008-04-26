package PACA.agents;

import jade.core.Profile;
import jade.core.ProfileImpl;
import jade.core.Runtime;
import jade.wrapper.AgentController;
import jade.wrapper.ContainerController;
import jade.wrapper.StaleProxyException;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.List;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.util.Date;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JPanel;

import sun.management.resources.agent;

import PACA.util.Resultado;

import com.sun.corba.se.spi.orbutil.threadpool.ThreadPool;

public class lanzador {

	static JFrame ventana = new JFrame();
	static JPanel panel = new JPanel();
	static JPanel panel2 = new JPanel();
	static List listaThreads = new List();
	static String [] numAgents = {"1", "5" ,"10" ,"20" ,"40"};
	static String [] politicas = {"aleatoria", "minimos"};
	//static JComboBox caja = new JComboBox();
	static JComboBox caja = null;
	static JComboBox cajaPoliticas = null;
	static int numero = 50;
	static JButton botonLanza = new JButton("Numero de Agentes"); 
	static JButton botonPolitica = new JButton("Politica");
	static JButton botonTermina = new JButton("Termina Thread");
	static JButton botonTerminaTodos = new JButton("Termina Todos");
	static boolean terminado = false;

	static lanzador lanz = new lanzador();
	
	static ContainerController cc = null;
	
	Boolean bol;
	//	Variables para crear el Agente Interfaz
	
	//static String politica = "minimos";
	
	static String politica;
	
	
	
	public Boolean getBol() {
		return bol;
	}

	public void setBol(Boolean bol) {
		this.bol = bol;
	}
	

	static void GeneraRetardo(int ret){
		try {
			Thread.sleep(ret);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		
		Runtime rt = Runtime.instance();
		Profile p = new ProfileImpl(false);
		cc = rt.createAgentContainer(p);
		
		//lanz.setBol(false);
		
		caja = new JComboBox(numAgents);
		cajaPoliticas = new JComboBox(politicas);

		/*panel.add(caja);
		panel.add(botonLanza);
		panel2.add(listaThreads);
		panel2.add(botonTermina);
		panel2.add(botonTerminaTodos);*/
		
		panel.add(cajaPoliticas);
		//panel.add(botonPolitica);
		
		panel2.add(caja);
		panel2.add(botonLanza);

		ventana.setSize(new Dimension(600, 300));	

		ventana.setDefaultCloseOperation(ventana.EXIT_ON_CLOSE);
		ventana.setLayout(new BorderLayout());
		ventana.getContentPane().add(panel, BorderLayout.NORTH);
		ventana.getContentPane().add(panel2, BorderLayout.SOUTH);
		ventana.setTitle("Lanzador de Agentes");
		ventana.pack();
		ventana.setVisible(true);


		botonPolitica.addMouseListener( new MouseAdapter(){
			public void mousePressed(MouseEvent e){
				politica = (String) cajaPoliticas.getSelectedItem();
			}
		} );	



		//Selecciona el numero de Agentes a lanzar
		botonLanza.addMouseListener( new MouseAdapter(){
			public void mousePressed(MouseEvent e){
				//la propiedad getselecteditem() regresa un objeto
				politica = (String) cajaPoliticas.getSelectedItem();
				String numero  =  (String) caja.getSelectedItem();
				//System.out.println("Agentes para lanzar: "+numero);

				//Thread[] hilos = new Thread[Integer.valueOf(numero)];
				//Thread t1;
				int numThreads = Integer.valueOf(numero);
				
				Executor pool = Executors.newFixedThreadPool(numThreads);
				
				for (int i = 0; i < numThreads; i++) {
									
					
					//------------------ NO BORRAR -----------------
					SimulaAgentes sim1 = new SimulaAgentes(cc, i, politica);
					pool.execute(sim1);
					//------------------ FIN NO BORRAR -------------
									
					GeneraRetardo(750);

				}
				
										
				/*listaThreads.setMultipleMode(true);
				panel2.add(listaThreads);
				panel2.add(botonTermina);
				panel2.add(botonTerminaTodos);
				ventana.getContentPane().add(panel2, BorderLayout.SOUTH);
				ventana.validate();*/

				//GeneraRetardo(2000);
				

				//System.out.println("Lanzamos!!!");


				//for (int i = 0; i < hilos.length; i++) {
					//hilos[i].start();
					//Comment by SSAUGAR
					//GeneraRetardo(1000);
					//System.out.println("Lanzamos!!!!"+((Thread) hilos[i]).getName());
				//}
				
				//lanz.setBol(true);


			}
		} );
		
		
		
//		Selecciona el numero de Agentes a lanzar
		botonTermina.addMouseListener( new MouseAdapter(){
			public void mousePressed(MouseEvent e){
				//la propiedad getselecteditem() regresa un objeto
				String [] lThreads  =  (String []) listaThreads.getSelectedItems();
				System.out.println(lThreads.length);
				Thread [] listaT = new Thread[lThreads.length];
				

				for (int i = 0; i < lThreads.length; i++) {
					String nombre = lThreads[i].toString();
					System.out.println("Nombre: "+nombre);
					Thread tAux = new Thread();
					tAux.setName(nombre);
					listaT[i]=tAux;
					listaThreads.remove(nombre);
				}
				
				
				
				for (int i = 0; i < listaT.length; i++) {
					try {
						if (listaT[i].isAlive()){
							System.out.println("Esta viva!!");
							listaT[i].join();
							System.out.println("Ya no lo esta... ");
						}else{
							System.out.println("No vive... ");
						}
					} catch (InterruptedException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				}
				
				listaThreads.setMultipleMode(true);
				panel2.add(listaThreads);
				panel2.add(botonTermina);
				panel2.add(botonTerminaTodos);
				ventana.getContentPane().add(panel2, BorderLayout.SOUTH);
				ventana.validate();
			}
		} );
		
		
		
		
		
		
		
		
		
		
		
		

	}

}
