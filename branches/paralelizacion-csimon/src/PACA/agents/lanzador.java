package PACA.agents;

import java.awt.Dimension;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class lanzador {
	
	static Runtime hebra = Runtime.getRuntime();
	static JFrame ventana = new JFrame();
	static JPanel panel = new JPanel();
	static JComboBox caja = new JComboBox();
	static int numero = 10;
	static JButton botonLanza = new JButton("Numero de Agentes"); 
	static boolean terminado = false;
	
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
				
				for (int i = 1; i <= Integer.valueOf(numero); i++) {
					try {
						Runtime rt = Runtime.getRuntime();
						System.out.println("Intentamos lanzar el comando");
						rt.exec("C:\\Documents and Settings\\Carlos\\Escritorio\\lanzaAgentes.bat");
						System.out.println("Se supone que se lanza");
						try {
							Thread.sleep(600);
						} 
						catch (InterruptedException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
					
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}		
				}
				
				terminado = true;
				
				if (terminado){
					try{
						System.exit(0);
						//hebra.exit(0);
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
