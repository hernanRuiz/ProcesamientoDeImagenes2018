package ar.com.untref.imagenes.dialogs;

import java.awt.Container;
import java.awt.Dimension;
import java.util.Random;

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
	private AnimatedIcon puntosSuspensivos;
	
	public EspereDialog(){
		
		initUI();
	}
	
	public void mostrar(){

		Random random = new Random();
		if(random.nextBoolean()){

			labelImagen.setIcon( new ImageIcon(EspereDialog.class.getResource("/resources/emc.gif")) );
		} else {
			
			labelImagen.setIcon( new ImageIcon(EspereDialog.class.getResource("/resources/pensador.gif")) );
		}
		
		setVisible(true);
	}
	
	public void ocultar(){
		
		dispose();
	}

	private void initUI() {

		panel = new JPanel();
		panel.setPreferredSize(new Dimension(800, 20));
		
		labelEspera = new JLabel("Procesando ");
		labelEspera.setHorizontalTextPosition( JLabel.LEADING );
		puntosSuspensivos = new AnimatedIcon( labelEspera );
		puntosSuspensivos.setAlignmentX( AnimatedIcon.LEFT );
		puntosSuspensivos.addIcon( new TextIcon(labelEspera, ".") );
		puntosSuspensivos.addIcon( new TextIcon(labelEspera, "..") );
		puntosSuspensivos.addIcon( new TextIcon(labelEspera, "...") );
		puntosSuspensivos.addIcon( new TextIcon(labelEspera, "....") );
		puntosSuspensivos.addIcon( new TextIcon(labelEspera, ".....") );
		labelEspera.setIcon( puntosSuspensivos );
		
		puntosSuspensivos.start();
		
		labelImagen = new JLabel();
		labelImagen.setHorizontalTextPosition( JLabel.LEADING );
		
		panel.add(labelEspera);
		createLayout();
		
		setModalityType(ModalityType.APPLICATION_MODAL);

		setTitle("Un momento");
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		setLocationRelativeTo(getParent());
		
		setModalityType(ModalityType.APPLICATION_MODAL);

		setTitle("Un momento");
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		setLocationRelativeTo(getParent());
		
		labelImagen = new JLabel();
		labelImagen.setHorizontalTextPosition( JLabel.LEADING );
		labelImagen.setIcon( new ImageIcon(EspereDialog.class.getResource("/resources/pensador.gif")) );

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