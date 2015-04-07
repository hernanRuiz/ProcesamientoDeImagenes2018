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
import javax.swing.JTextField;

import ar.com.untref.imagenes.modelo.Imagen;
import ar.com.untref.imagenes.procesamiento.ProcesadorDeImagenes;
import ar.com.untref.imagenes.ventanas.VentanaPrincipal;

@SuppressWarnings("serial")
public class OperacionesMatricesDialog extends JDialog {

	private VentanaPrincipal ventana;
	private JButton botonConfirmar;
	private JLabel labelSigno;
	private JLabel labelFactores;
	private JLabel labelInstrucciones;
	private JLabel labelInstrucciones2;
	private String signoElegido;
	private String factorElegido;
	private JScrollPane comboSignos;
	private JScrollPane comboFactores;
	private JTextField textFieldEscalar;

	public OperacionesMatricesDialog(VentanaPrincipal ventana) {
		super(ventana);
		this.ventana = ventana;
		initUI();
	}

	private void initUI() {

		labelInstrucciones = new JLabel(
				"Elija primero el signo de la operación a realizar");
		labelInstrucciones.setFont(new Font("Serif", Font.BOLD, 13));

		labelSigno = new JLabel("Signo");
		labelInstrucciones.setFont(new Font("Serif", Font.BOLD, 14));

		labelInstrucciones2 = new JLabel(
				"Ahora elija el segundo factor: escalar o matriz");
		labelInstrucciones2.setFont(new Font("Serif", Font.BOLD, 13));

		labelFactores = new JLabel("Segundo Factor");
		labelFactores.setFont(new Font("Serif", Font.BOLD, 14));

		crearCombo();
		createLayout();

		setModalityType(ModalityType.APPLICATION_MODAL);

		setTitle("Operaciones con Matrices");
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
				.addComponent(labelInstrucciones).addComponent(labelSigno)
				.addComponent(comboSignos).addComponent(labelInstrucciones2)
				.addComponent(labelFactores).addComponent(comboFactores)
				.addComponent(textFieldEscalar).addComponent(botonConfirmar)
				.addGap(200));

		gl.setVerticalGroup(gl.createSequentialGroup().addGap(30)
				.addComponent(labelInstrucciones).addGap(20)
				.addComponent(labelSigno).addGap(20).addComponent(comboSignos)
				.addGap(20).addComponent(labelInstrucciones2).addGap(20)
				.addComponent(labelFactores).addGap(20)
				.addComponent(comboFactores).addGap(20)
				.addComponent(textFieldEscalar).addGap(20)
				.addComponent(botonConfirmar).addGap(30));

		pack();
	}

	private void crearCombo() {

		labelSigno.setText("Signo");

		final DefaultComboBoxModel<String> signos = new DefaultComboBoxModel<String>();

		final String sumar = new String("+");
		final String restar = new String("-");
		final String multiplicar = new String("*");

		signos.addElement(sumar);
		signos.addElement(restar);
		signos.addElement(multiplicar);

		final JComboBox<String> combo = new JComboBox<String>(signos);
		combo.setSelectedIndex(0);

		comboSignos = new JScrollPane(combo);

		final DefaultComboBoxModel<String> factores = new DefaultComboBoxModel<String>();

		String escalar = new String("Escalar");
		String matriz = new String("Imagen");

		factores.addElement(matriz);
		factores.addElement(escalar);

		final JComboBox<String> combo2 = new JComboBox<String>(factores);
		combo2.setSelectedIndex(0);

		comboFactores = new JScrollPane(combo2);

		botonConfirmar = new JButton("Listo");

		textFieldEscalar = new JTextField();

		botonConfirmar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				signoElegido = (String) combo.getItemAt(combo
						.getSelectedIndex());
				factorElegido = (String) combo2.getItemAt(combo2
						.getSelectedIndex());

				if (signoElegido == sumar) {

					switch (factorElegido) {

					case "Escalar":

						int valorEscalar = Integer.valueOf(textFieldEscalar
								.getText().trim());
						ventana.obtenerMatrizResultanteDeSumaEscalar(valorEscalar);
						OperacionesMatricesDialog.this.setVisible(false);
						break;

					case "Imagen":

						Imagen primeraImagen = ProcesadorDeImagenes
								.obtenerInstancia().getImagenActual();
						Imagen segundaImagen = abrirImagen();
						ventana.obtenerMatrizResultanteDeSuma(primeraImagen,
								segundaImagen);
						OperacionesMatricesDialog.this.setVisible(false);
						break;

					}

				} else if (signoElegido == restar) {
					switch (factorElegido) {

					case "Escalar":

						int valorEscalar = Integer.valueOf(textFieldEscalar
								.getText().trim());
						ventana.obtenerMatrizResultanteDeRestaEscalar(valorEscalar);
						OperacionesMatricesDialog.this.setVisible(false);
						break;

					case "Imagen":

						Imagen primeraImagen = ProcesadorDeImagenes
								.obtenerInstancia().getImagenActual();
						Imagen segundaImagen = abrirImagen();
						ventana.obtenerMatrizResultanteDeResta(primeraImagen,
								segundaImagen);
						OperacionesMatricesDialog.this.setVisible(false);
						break;
					}

				} else if (signoElegido == multiplicar) {
					switch (factorElegido) {

					case "Escalar":

						int valorEscalar = Integer.valueOf(textFieldEscalar
								.getText().trim());
						ventana.obtenerMatrizResultanteDeMultiplicarPorEscalar(valorEscalar);
						OperacionesMatricesDialog.this.setVisible(false);
						break;

					case "Imagen":
						Imagen primeraImagen = ProcesadorDeImagenes
								.obtenerInstancia().getImagenActual();
						Imagen segundaImagen = abrirImagen();
						ventana.obtenerMatrizResultanteDeMultiplicar(
								primeraImagen, segundaImagen);
						OperacionesMatricesDialog.this.setVisible(false);
						break;
					}
				}
			}
		});
	}

	private Imagen abrirImagen() {
		Imagen imagenElegida = ProcesadorDeImagenes.obtenerInstancia()
				.cargarUnaImagenDesdeArchivo();
		return imagenElegida;
	}
}
