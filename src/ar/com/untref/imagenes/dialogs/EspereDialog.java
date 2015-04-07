package ar.com.untref.imagenes.dialogs;

import java.awt.Container;
import java.awt.Dimension;

import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.ImageIcon;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;

import ar.com.untref.imagenes.helpers.AnimatedIcon;
import ar.com.untref.imagenes.helpers.TextIcon;

@SuppressWarnings("serial")
public class EspereDialog extends JDialog {
	
	private JLabel labelEspera;
	private JLabel labelImagen;
	private JPanel panel;
	private AnimatedIcon icon2;
	private AnimatedIcon pensador;
	
	public EspereDialog(){
		
		initUI();
	}
	
	public void mostrar(){

		setVisible(true);
	}
	
	public void ocultar(){
		
		dispose();
	}

	private void initUI() {

		panel = new JPanel();
		panel.setPreferredSize(new Dimension(400, 20));
		
		labelEspera = new JLabel("Procesando ");
		labelEspera.setHorizontalTextPosition( JLabel.LEADING );
		icon2 = new AnimatedIcon( labelEspera );
		icon2.setAlignmentX( AnimatedIcon.LEFT );
		icon2.addIcon( new TextIcon(labelEspera, ".") );
		icon2.addIcon( new TextIcon(labelEspera, "..") );
		icon2.addIcon( new TextIcon(labelEspera, "...") );
		icon2.addIcon( new TextIcon(labelEspera, "....") );
		icon2.addIcon( new TextIcon(labelEspera, ".....") );
		labelEspera.setIcon( icon2 );
		
		icon2.start();
		
		labelImagen = new JLabel();
		labelImagen.setHorizontalTextPosition( JLabel.LEADING );
		pensador = new AnimatedIcon( labelImagen );
		pensador.setAlignmentX( AnimatedIcon.LEFT );
		pensador.addIcon( new ImageIcon("resources/pensa1.png") );
		pensador.addIcon( new ImageIcon("resources/pensa2.png") );
		pensador.addIcon( new ImageIcon("resources/pensa3.png") );

		pensador.start();
		labelImagen.setIcon( pensador );
		
		panel.add(labelEspera);
		createLayout();
		
		

		setModalityType(ModalityType.APPLICATION_MODAL);

		setTitle("Un momento");
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		setLocationRelativeTo(getParent());
		
	}

	private void createLayout() {

		Container pane = getContentPane();
		GroupLayout gl = new GroupLayout(pane);
		pane.setLayout(gl);

		gl.setAutoCreateContainerGaps(true);
		gl.setAutoCreateGaps(true);

		gl.setHorizontalGroup(gl.createParallelGroup(Alignment.CENTER)
				.addComponent(panel).addComponent(labelImagen));

		gl.setVerticalGroup(gl.createSequentialGroup().addGap(30)
				.addComponent(panel).addGap(20).addComponent(labelImagen));
		
		pack();
	}

}