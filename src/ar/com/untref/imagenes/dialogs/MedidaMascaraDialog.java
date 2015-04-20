package ar.com.untref.imagenes.dialogs;

import java.awt.Container;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.DefaultComboBoxModel;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JScrollPane;

import ar.com.untref.imagenes.enums.Mascara;
import ar.com.untref.imagenes.ventanas.VentanaRuido;

@SuppressWarnings("serial")
public class MedidaMascaraDialog extends JDialog {

	private VentanaRuido ventana;
	private JButton botonConfirmar;
	private JLabel labelSigma;
	private JLabel labelInstrucciones;
	private Integer longitudMascaraElegida;
	private JScrollPane comboSigmas;
	private Mascara mascara;
	
	public MedidaMascaraDialog(VentanaRuido ventana, Mascara mascara) {
		
		super(ventana);
		this.ventana = ventana;
		this.mascara = mascara;
		initUI();
	}

	private void initUI() {

		labelInstrucciones = new JLabel("Para poder crear una máscara de filtrado, debe definir primero la cantidad de filas y/o columnas de la misma (se trabaja con matrices cuadradas).");
		labelInstrucciones.setFont(new Font("Serif", Font.BOLD, 13));
		
		labelSigma = new JLabel("Sigma");
		labelInstrucciones.setFont(new Font("Serif", Font.BOLD, 14));

		crearCombo();
		createLayout();

		setModalityType(ModalityType.APPLICATION_MODAL);

		setTitle(mascara.getDescripcion());
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
				.addComponent(labelInstrucciones).addComponent(labelSigma).addComponent(comboSigmas).addComponent(botonConfirmar)
				.addGap(200));

		gl.setVerticalGroup(gl.createSequentialGroup().addGap(30)
				.addComponent(labelInstrucciones).addGap(20).addComponent(labelSigma).addGap(20).addComponent(comboSigmas)
				.addGap(20).addComponent(botonConfirmar).addGap(30));

		pack();
	}
	
	private void crearCombo(){                                    
	      
		labelSigma.setText("Tamaño de la matriz"); 

	      final DefaultComboBoxModel<Integer> tamanio = new DefaultComboBoxModel<Integer>();

	      tamanio.addElement(3);
	      tamanio.addElement(5);
	      tamanio.addElement(7);
	      tamanio.addElement(9);
	      tamanio.addElement(11);

	      final JComboBox<Integer> combo = new JComboBox<Integer>(tamanio);    
	      combo.setSelectedIndex(0);

	      comboSigmas = new JScrollPane(combo);  

	      botonConfirmar = new JButton("Listo");

	      botonConfirmar.addActionListener(new ActionListener() {
	         public void actionPerformed(ActionEvent e) { 
	            
	        	 if (combo.getSelectedIndex() != -1) {                     
	               longitudMascaraElegida = (Integer)combo.getItemAt(combo.getSelectedIndex());
	               
	               switch (mascara){
	               
	               		case MEDIA:
	               			ventana.aplicarFiltroDeLaMedia(longitudMascaraElegida);
	               			break;
	               		case MEDIANA:
	               			ventana.aplicarFiltroDeLaMediana(longitudMascaraElegida);
	               			break;
	               		case PASA_ALTOS:
	               			ventana.aplicarFiltroPasaAltos(longitudMascaraElegida);
	               			break;
	               		default:
	               			break;
	               }
	               MedidaMascaraDialog.this.dispose();
	            }              
	         }
	      }); 
	   }
}