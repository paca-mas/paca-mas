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

public class lanzador extends JFrame{

	static JFrame ventana = new JFrame();
	static JPanel panel = new JPanel();
	static JPanel panel2 = new JPanel();
	static List listaThreads = new List();
	static String [] numAgents = {"1", "5" ,"10" ,"15", "20" ,"25", "30", "35", "40"};
	static String [] politicas = {"aleatoria", "minimos"};
	static String [] correciones = {"10", "20", "50", "100", "200"};
	
	static JComboBox caja = null;
	static JComboBox cajaPoliticas = null;
	static JComboBox cajaCorreciones = null;
	static int numero = 50;
	static JButton botonLanza = new JButton("Numero de Agentes"); 
	static JButton botonPolitica = new JButton("Politica");
	static JButton botonTermina = new JButton("Termina Thread");
	static JButton botonTerminaTodos = new JButton("Termina Todos");
	static boolean terminado = false;

	//static lanzador lanz = new lanzador();
	
	static ContainerController cc = null;
	
	Boolean bol;
	//	Variables para crear el Agente Interfaz
	
	//static String politica = "minimos";
	
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
	
	private void initialize() {
		
		caja = new JComboBox(numAgents);
		cajaPoliticas = new JComboBox(politicas);
		cajaCorreciones = new JComboBox(correciones);
		
		panel.add(cajaPoliticas);
		panel.add(cajaCorreciones);
				
		panel2.add(caja);
		panel2.add(botonLanza);
		
		this.setSize(new Dimension(600, 300));	

		this.setDefaultCloseOperation(this.EXIT_ON_CLOSE);
		this.setLayout(new BorderLayout());
		this.getContentPane().add(panel, BorderLayout.NORTH);
		this.getContentPane().add(panel2, BorderLayout.SOUTH);
		this.setTitle("Lanzador de Agentes");
		this.pack();
		this.setVisible(true);
		
		
//		Selecciona el numero de Agentes a lanzar
		botonLanza.addMouseListener( new MouseAdapter(){
			public void mousePressed(MouseEvent e){
				//la propiedad getselecteditem() regresa un objeto
				String politica = (String) cajaPoliticas.getSelectedItem();
				String numero  =  (String) caja.getSelectedItem();
				String nCorreciones = (String) cajaCorreciones.getSelectedItem();
				
				int numThreads = Integer.valueOf(numero);
				int numeroCorreciones = Integer.valueOf(nCorreciones);
				int total = numeroCorreciones / numThreads;
				
				Executor pool = Executors.newFixedThreadPool(numThreads);
				
				for (int i = 0; i < numThreads; i++) {
									
					
					//------------------ NO BORRAR -----------------
					SimulaAgentes sim1 = new SimulaAgentes(cc, i, politica, total);
					pool.execute(sim1);
					//------------------ FIN NO BORRAR -------------
									
					GeneraRetardo(750);

				}
			}
		} );
		
	}
	
	public lanzador(){
		super();
		initialize();
	}

	public static void main(String[] args) {
		
		new lanzador();
		
		Runtime rt = Runtime.instance();
		Profile p = new ProfileImpl(false);
		cc = rt.createAgentContainer(p);
		
		/*//lanz.setBol(false);
		
		caja = new JComboBox(numAgents);
		cajaPoliticas = new JComboBox(politicas);

		panel.add(cajaPoliticas);
		
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


		//Selecciona el numero de Agentes a lanzar
		botonLanza.addMouseListener( new MouseAdapter(){
			public void mousePressed(MouseEvent e){
				//la propiedad getselecteditem() regresa un objeto
				String politica = (String) cajaPoliticas.getSelectedItem();
				String numero  =  (String) caja.getSelectedItem();
				
				int numThreads = Integer.valueOf(numero);
				
				Executor pool = Executors.newFixedThreadPool(numThreads);
				
				for (int i = 0; i < numThreads; i++) {
									
					
					//------------------ NO BORRAR -----------------
					SimulaAgentes sim1 = new SimulaAgentes(cc, i, politica);
					pool.execute(sim1);
					//------------------ FIN NO BORRAR -------------
									
					GeneraRetardo(750);

				}
			}
		} );*/
		
	}

}
