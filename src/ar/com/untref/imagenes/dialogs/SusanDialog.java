package ar.com.untref.imagenes.dialogs;

import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;

import ar.com.untref.imagenes.enums.NivelMensaje;
import ar.com.untref.imagenes.helpers.DialogsHelper;
import ar.com.untref.imagenes.ventanas.VentanaPrincipal;
import ar.com.untref.imagenes.ventanas.VentanaRuido;

@SuppressWarnings("serial")
public class SusanDialog extends JDialog {

	private VentanaPrincipal ventana;
	private JButton botonConfirmar;
	private JLabel labelEsquinas;
	private JLabel labelBordes;
	private JLabel labelSierra;
	private JCheckBox boxEsquinas;
	private JCheckBox boxBordes;
	private JCheckBox boxSierra;
	private VentanaRuido ventanaRuido;
	private JPanel jpanel;

	public SusanDialog(VentanaPrincipal ventana, JPanel jpanel) {
		super(ventana);
		this.ventana = ventana;
		this.jpanel = jpanel;
		initUI();
	}

	public SusanDialog(VentanaRuido ventanaRuido, JPanel jpanel) {
		super(ventanaRuido);
		this.ventanaRuido = ventanaRuido;
		this.jpanel = jpanel;
		initUI();
	}

	private void initUI() {

		labelEsquinas = new JLabel("Esquinas");
		labelBordes = new JLabel("Bordes");
		labelSierra = new JLabel("Sierras");
		
		boxEsquinas = new JCheckBox();
		boxBordes = new JCheckBox();
		boxSierra = new JCheckBox();
		
		createLayout();

		setModalityType(ModalityType.APPLICATION_MODAL);

		setTitle("Detector de Susan");
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		setLocationRelativeTo(getParent());
	}

	private void createLayout() {

		botonConfirmar = new JButton("Listo");

		botonConfirmar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				if (boxEsquinas.isSelected() && boxBordes.isSelected() && boxSierra.isSelected()){
					
					if (ventana != null){
						ventana.aplicarDetectorSusan("BordesSierrasYEsquinas");
					}
					
					if (ventanaRuido != null){
						
						ventana.aplicarDetectorSusan("BordesSierrasYEsquinas");
					}
				}else if (!boxEsquinas.isSelected() && boxBordes.isSelected() && boxSierra.isSelected()){
					
					if (ventana != null){
						ventana.aplicarDetectorSusan("BordesYSierras");
					}
					
					if (ventanaRuido != null){
						
						ventana.aplicarDetectorSusan("BordesYSierras");
					}
				}else if (boxEsquinas.isSelected() && !boxBordes.isSelected() && boxSierra.isSelected()){
					
					if (ventana != null){
						ventana.aplicarDetectorSusan("EsquinasYSierras");
					}
					
					if (ventanaRuido != null){
						
						ventana.aplicarDetectorSusan("EsquinasYSierras");
					}
				}else if (boxEsquinas.isSelected() && boxBordes.isSelected() && !boxSierra.isSelected()){
					
					if (ventana != null){
						ventana.aplicarDetectorSusan("EsquinasYBordes");
					}
					
					if (ventanaRuido != null){
						
						ventana.aplicarDetectorSusan("EsquinasYBordes");
					}
				}else if (!boxEsquinas.isSelected() && !boxBordes.isSelected() && boxSierra.isSelected()){
					
					if (ventana != null){
						ventana.aplicarDetectorSusan("Sierras");
					}
					
					if (ventanaRuido != null){
						
						ventana.aplicarDetectorSusan("Sierras");
					}
				}else if (!boxEsquinas.isSelected() && boxBordes.isSelected() && !boxSierra.isSelected()){
									
					if (ventana != null){
						ventana.aplicarDetectorSusan("Bordes");
					}
					
					if (ventanaRuido != null){
						
						ventana.aplicarDetectorSusan("Bordes");
					}
				}else if (boxEsquinas.isSelected() && !boxBordes.isSelected() && !boxSierra.isSelected()){
					
					if (ventana != null){
						ventana.aplicarDetectorSusan("Esquinas");
					}
					
					if (ventanaRuido != null){
						
						ventanaRuido.aplicarDetectorSusan("Esquinas");
					}
				}else{
					DialogsHelper.mostarMensaje(jpanel, "Por favor seleccione alg\u00fan criterio de detecci\u00f3n", NivelMensaje.ERROR);
				}
				SusanDialog.this.dispose();
			}
		});
		
		Container pane = getContentPane();
		GroupLayout gl = new GroupLayout(pane);
		pane.setLayout(gl);

		gl.setAutoCreateContainerGaps(true);
		gl.setAutoCreateGaps(true);

		gl.setHorizontalGroup(gl.createParallelGroup(Alignment.CENTER)
				.addComponent(labelEsquinas).addComponent(boxEsquinas)
				.addComponent(labelBordes).addComponent(boxBordes)
				.addComponent(labelSierra).addComponent(boxSierra)
				.addComponent(botonConfirmar).addGap(200));

		gl.setVerticalGroup(gl.createSequentialGroup().addGap(30)
				.addComponent(labelEsquinas).addComponent(boxEsquinas).addGap(20)
				.addComponent(labelBordes).addComponent(boxBordes).addGap(20)
				.addComponent(labelSierra).addComponent(boxSierra).addGap(20)
				.addComponent(botonConfirmar).addGap(30));

		pack();
	}
}
