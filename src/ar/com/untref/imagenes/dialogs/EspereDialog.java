package ar.com.untref.imagenes.dialogs;

import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagLayout;

import javax.swing.GroupLayout;
import javax.swing.ImageIcon;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;

import ar.com.untref.imagenes.ventanas.VentanaPrincipal;
import ar.com.untref.imagenes.ventanas.VentanaRuido;

@SuppressWarnings("serial")
public class EspereDialog extends JDialog {
	
	private JLabel labelImagen;
	private JPanel panel;
	private VentanaPrincipal ventanaP;
	private VentanaRuido ventanaR;
	
	public EspereDialog(VentanaPrincipal ventanaP){
		
		this.ventanaP = ventanaP;
		setUndecorated(true);
		initUI();
	}
	
	public EspereDialog(VentanaRuido ventanaR){
		
		this.ventanaR = ventanaR;
		setUndecorated(true);
		initUI();
	}
	
	public void mostrar(){

		labelImagen.setSize(new Dimension (150, 150));
		labelImagen.setIcon( new ImageIcon(EspereDialog.class.getResource("/resources/procesando.gif")));
		
		setVisible(true);
	}
	
	public void ocultar(){
		
		dispose();
	}

	private void initUI() {

		panel = new JPanel(new GridBagLayout());
		panel.setPreferredSize(new Dimension(150, 150));
				
		this.setLayout(new FlowLayout());
		
		setModalityType(ModalityType.APPLICATION_MODAL);
		
		labelImagen = new JLabel();
		createLayout();
		
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		
		if(ventanaP != null){setLocationRelativeTo(ventanaP);}
		if(ventanaR != null){setLocationRelativeTo(ventanaR);}
	}

	private void createLayout() {

		Container pane = getContentPane();
		GroupLayout gl = new GroupLayout(pane);
		pane.setLayout(gl);

		gl.setAutoCreateContainerGaps(true);		

		gl.setHorizontalGroup(gl.createParallelGroup()
				.addComponent(panel).addComponent(labelImagen));

		gl.setVerticalGroup(gl.createSequentialGroup()
				.addComponent(panel).addComponent(labelImagen));
				
		pack();
	}
}