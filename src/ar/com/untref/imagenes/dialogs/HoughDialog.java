package ar.com.untref.imagenes.dialogs;

import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JTextField;

import ar.com.untref.imagenes.enums.NivelMensaje;
import ar.com.untref.imagenes.helpers.DialogsHelper;
import ar.com.untref.imagenes.ventanas.VentanaPrincipal;
import ar.com.untref.imagenes.ventanas.VentanaRuido;

public class HoughDialog extends JDialog {

	private VentanaPrincipal ventana;
	private JButton botonConfirmar;
	private JLabel labelTitaMinimo;
	private JLabel labelTitaMaximo;
	private JLabel labelDiscretizacionTita;
	private JLabel labelRoMinimo;
	private JLabel labelRoMaximo;
	private JLabel labelDiscretizacionRo;
	private JTextField titaMinimo;
	private JTextField titaMaximo;
	private JTextField discretizacionTita;
	private JTextField roMinimo;
	private JTextField roMaximo;
	private JTextField discretizacionRo;
	private VentanaRuido ventanaRuido;

	public HoughDialog(VentanaPrincipal ventana) {
		super(ventana);
		this.ventana = ventana;
		initUI();
	}

	private void initUI() {

		labelTitaMinimo = new JLabel("Tita mínimo (en grados)");
		labelTitaMaximo = new JLabel("Tita máximo (en grados)");
		labelDiscretizacionTita = new JLabel("Discretización Tita");
		titaMaximo = new JTextField();
		titaMinimo = new JTextField();
		discretizacionTita = new JTextField();

		labelRoMinimo = new JLabel("Ro mínimo (en grados)");
		labelRoMaximo = new JLabel("Ro máximo (en grados)");
		labelDiscretizacionRo = new JLabel("Discretización Ro");
		roMaximo = new JTextField();
		roMinimo = new JTextField();
		discretizacionRo = new JTextField();
		
		createLayout();

		setModalityType(ModalityType.APPLICATION_MODAL);

		setTitle("Transformada de Hough");
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		setLocationRelativeTo(getParent());
	}

	private void createLayout() {

		botonConfirmar = new JButton("Listo");

		botonConfirmar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				if (!titaMaximo.getText().toString().isEmpty() && !titaMinimo.getText().toString().isEmpty()
						&& !discretizacionTita.getText().toString().isEmpty() && 
						!roMaximo.getText().toString().isEmpty() && !roMinimo.getText().toString().isEmpty()
						&& !discretizacionRo.getText().toString().isEmpty()) {
					
					try{
						int titaMax = Integer.valueOf(titaMaximo.getText().toString());
						int titaMin = Integer.valueOf(titaMinimo.getText().toString());
						int discTita = Integer.valueOf(discretizacionTita.getText().toString());
						
						int roMax = Integer.valueOf(roMaximo.getText().toString());
						int roMin = Integer.valueOf(roMinimo.getText().toString());
						int discRo = Integer.valueOf(discretizacionRo.getText().toString());
						
						if (ventana != null){
							
							ventana.aplicarTransformadaDeHough(titaMin, titaMax, discTita, roMin, roMax, discRo);
						}
						
						HoughDialog.this.dispose();
					
					} catch (Exception ex){
						
						ex.printStackTrace();
						DialogsHelper.mostarMensaje(ventana.getContentPane(), "Valores inválidos", NivelMensaje.ERROR);
					}
				}
			}
		});
		
		Container pane = getContentPane();
		GroupLayout gl = new GroupLayout(pane);
		pane.setLayout(gl);

		gl.setAutoCreateContainerGaps(true);
		gl.setAutoCreateGaps(true);

		gl.setHorizontalGroup(gl.createParallelGroup(Alignment.CENTER)
				.addComponent(labelTitaMinimo).addComponent(labelDiscretizacionTita)
				.addComponent(labelTitaMaximo)
				.addComponent(titaMinimo).addComponent(titaMaximo)
				.addComponent(discretizacionTita)
				.addComponent(labelRoMinimo).addComponent(labelDiscretizacionRo)
				.addComponent(labelRoMaximo)
				.addComponent(roMinimo).addComponent(roMaximo)
				.addComponent(discretizacionRo)
				.addComponent(botonConfirmar).addGap(200));

		gl.setVerticalGroup(gl.createSequentialGroup().addGap(30)
				.addComponent(labelTitaMinimo).addGap(20).addComponent(titaMinimo).addGap(20)
				.addComponent(labelTitaMaximo).addGap(20).addComponent(titaMaximo)
				.addGap(20).addComponent(labelDiscretizacionTita).addGap(20).addComponent(discretizacionTita)
				.addGap(30)
				.addComponent(labelRoMinimo).addGap(20).addComponent(roMinimo).addGap(20)
				.addComponent(labelRoMaximo).addGap(20).addComponent(roMaximo)
				.addGap(20).addComponent(labelDiscretizacionRo).addGap(20).addComponent(discretizacionRo)
				.addGap(20).addComponent(botonConfirmar).addGap(30));

		pack();
	}
}