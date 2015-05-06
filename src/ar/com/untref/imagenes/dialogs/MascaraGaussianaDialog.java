package ar.com.untref.imagenes.dialogs;

import java.awt.Container;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.DefaultComboBoxModel;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JScrollPane;

import ar.com.untref.imagenes.ventanas.VentanaRuido;

@SuppressWarnings("serial")
public class MascaraGaussianaDialog extends JDialog {

	private VentanaRuido ventana;
	private JButton botonConfirmar;
	private JLabel labelSigma;
	private JLabel labelInstrucciones;
	private JLabel labelImagen;
	private Integer sigmaElegido;
	private JScrollPane comboSigmas;
	
	public MascaraGaussianaDialog(VentanaRuido ventana) {
		super(ventana);
		this.ventana = ventana;
		initUI();
	}

	private void initUI() {

		ImageIcon icon = new ImageIcon(MascaraGaussianaDialog.class.getResource("/resources/piramideGauss.png"));
		labelImagen = new JLabel(icon);

		labelInstrucciones = new JLabel("Para poder crear una máscara de filtrado, debe definir primero un valor de Sigma");
		labelInstrucciones.setFont(new Font("Serif", Font.BOLD, 13));
		
		labelSigma = new JLabel("Sigma");
		labelInstrucciones.setFont(new Font("Serif", Font.BOLD, 14));

		crearCombo();
		createLayout();

		setModalityType(ModalityType.APPLICATION_MODAL);

		setTitle("Máscara Gaussiana");
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
				.addComponent(labelInstrucciones).addComponent(labelSigma).addComponent(comboSigmas).addComponent(labelImagen).addComponent(botonConfirmar)
				.addGap(200));

		gl.setVerticalGroup(gl.createSequentialGroup().addGap(30)
				.addComponent(labelInstrucciones).addGap(20).addComponent(labelSigma).addGap(20).addComponent(comboSigmas)
				.addGap(20).addComponent(labelImagen).addGap(20).addComponent(botonConfirmar).addGap(30));
		
		pack();
	}
	
	private void crearCombo(){                                    
	      
		labelSigma.setText("Sigma"); 

	      final DefaultComboBoxModel<Integer> sigmas = new DefaultComboBoxModel<Integer>();

	      sigmas.addElement(1);
	      sigmas.addElement(2);
	      sigmas.addElement(3);
	      sigmas.addElement(4);
	      sigmas.addElement(5);

	      final JComboBox<Integer> combo = new JComboBox<Integer>(sigmas);    
	      combo.setSelectedIndex(0);

	      comboSigmas = new JScrollPane(combo);  

	      botonConfirmar = new JButton("Listo");

	      botonConfirmar.addActionListener(new ActionListener() {
	         public void actionPerformed(ActionEvent e) { 
	            
	        	 if (combo.getSelectedIndex() != -1) {                     
	               sigmaElegido = (Integer)combo.getItemAt(combo.getSelectedIndex());
	               ventana.aplicarFiltroGaussiano(sigmaElegido);
	               MascaraGaussianaDialog.this.dispose();
	            }              
	         }
	      }); 
	   }
}