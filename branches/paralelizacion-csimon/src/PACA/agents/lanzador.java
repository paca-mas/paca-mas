package PACA.agents;

import java.awt.Dimension;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class lanzador {
	
	static Runtime hebra = Runtime.getRuntime();
	static JFrame ventana = new JFrame();
	static JPanel panel = new JPanel();
	static JComboBox caja = new JComboBox();
	static int numero = 20;
	static JButton botonLanza = new JButton("Numero de Agentes"); 
	static boolean terminado = false;
	
	static void GeneraRetardo(int ret){
		try {
			Thread.sleep(ret);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		for (int i = 1; i <= numero; i++) {
			caja.addItem(Integer.toString(i));
		}
		
		panel.add(caja);
		panel.add(botonLanza);
		
		ventana.setSize(new Dimension(600, 300));	
		
		ventana.setDefaultCloseOperation(ventana.EXIT_ON_CLOSE);
		ventana.getContentPane().add(panel);
		ventana.setTitle("Lanzador de Agentes");
		ventana.pack();
		ventana.setVisible(true);
		
		
		
		
		
		
		//Selecciona el numero de Agentes a lanzar
		botonLanza.addMouseListener( new MouseAdapter(){
				public void mousePressed(MouseEvent e){
				//la propiedad getselecteditem() regresa un objeto
				String numero  =  (String) caja.getSelectedItem();
				System.out.println("Agentes para lanzar: "+numero);
				
				Thread[] hilos = new Thread[Integer.valueOf(numero)];
				
				Thread t1;
				
				for (int i = 0; i < Integer.valueOf(numero); i++) {
					//try {
						Runtime rt = Runtime.getRuntime();
						System.out.println("Intentamos lanzar el comando");
						//rt.exec("C:\\Documents and Settings\\Carlos\\Escritorio\\lanzaAgentes.bat");
						//rt.exec("java PACA.agents.lanzaSwing2");
						//try {
							//rt.exec("java PACA.agents.SimulaAgentes");
							//rt.exec("java PACA.agents.lanzaSwing2");
						//} catch (IOException e1) {
							// TODO Auto-generated catch block
							//e1.printStackTrace();
						//}
						
						t1 = new Thread(new SimulaAgentes());
						//new Thread(new SimulaAgentes()).start();
						//SimulaAgentes sim1 = new SimulaAgentes();
						//pool.execute(sim1);
						//t1 = new SimulaAgentes();
												
						//t1.start();
						hilos[i] = t1;
						
						//GeneraRetardo(1000);
						//try {
							//Thread.sleep(2000);
							//System.out.println("Se supone que se lanza");
						//}
						
						//catch (InterruptedException e1) {
							// TODO Auto-generated catch block
							//e1.printStackTrace();
						//}
					
					//} catch (IOException e1) {
						// TODO Auto-generated catch block
						//e1.printStackTrace();
					//}		
				}
				
				System.out.println("Lanzamos!!!");
				
				
				for (int i = 0; i < hilos.length; i++) {
					hilos[i].start();
					GeneraRetardo(2000);
					
					System.out.println("Lanzamos!!!!"+((Thread) hilos[i]).getName());
				}
				
				terminado = true;
				
				if (terminado){
					try{
						//System.exit(0);
					}
					catch (Exception e2) {
						// TODO Auto-generated catch block
						e2.printStackTrace();
					}		
				}
			}
		} );
		
	}

}
