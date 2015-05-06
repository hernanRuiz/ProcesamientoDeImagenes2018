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

import ar.com.untref.imagenes.ventanas.VentanaPrincipal;

@SuppressWarnings("serial")
public class LoGDialog extends JDialog {

	private VentanaPrincipal ventana;
	private JButton botonConfirmar;
	private JLabel labelSigma;
	private JTextField sigmaElegido;
	private JLabel labelUmbral;
	private JTextField umbralElegido;

	public LoGDialog(VentanaPrincipal ventana) {
		super(ventana);
		this.ventana = ventana;
		initUI();
	}

	private void initUI() {

		labelSigma = new JLabel("Sigma");
		labelUmbral = new JLabel("Umbral");
		sigmaElegido = new JTextField();
		umbralElegido = new JTextField();

		createLayout();

		setModalityType(ModalityType.APPLICATION_MODAL);

		setTitle("Filtro LoG");
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		setLocationRelativeTo(getParent());
	}

	private void createLayout() {

		botonConfirmar = new JButton("Listo");

		botonConfirmar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				if (!sigmaElegido.getText().toString().isEmpty() && !umbralElegido.getText().toString().isEmpty()) {
					
					try{
						int sigma = Integer.valueOf(sigmaElegido.getText().toString());
						int umbral = Integer.valueOf(umbralElegido.getText().toString());
						
						ventana.aplicarLaplacianoDelGaussiano(sigma, umbral);
						LoGDialog.this.dispose();
					} catch (Exception ex){
						
						ex.printStackTrace();
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
				.addComponent(labelSigma).addComponent(labelUmbral)
				.addComponent(sigmaElegido).addComponent(umbralElegido)
				.addComponent(botonConfirmar).addGap(200));

		gl.setVerticalGroup(gl.createSequentialGroup().addGap(30)
				.addComponent(labelSigma).addGap(20).addComponent(sigmaElegido).addGap(20)
				.addComponent(labelUmbral).addGap(20).addComponent(umbralElegido)
				.addGap(20).addComponent(botonConfirmar).addGap(30));

		pack();
	}
}