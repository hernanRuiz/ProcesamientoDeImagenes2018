package ar.com.untref.imagenes.dialogs;

import java.awt.Container;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JLabel;

import ar.com.untref.imagenes.procesamiento.ProcesadorDeImagenes;
import ar.com.untref.imagenes.sift.Sift;

@SuppressWarnings("serial")
public class SiftDialog extends JDialog {

	private JButton botonAbrirImagen;
	private JLabel titulo;
	private JCheckBox checkRobusto;
	private JLabel labelRobusto;

	public SiftDialog() {
		super();
		initUI();
	}

	private void initUI() {

		titulo = new JLabel("Elija la imagen contra la cual quiere comparar");
		labelRobusto = new JLabel("Utilizar algoritmo de mayor presición");
		Font labelFont = titulo.getFont();

		// Set the label's font size to the newly determined size.
		titulo.setFont(new Font(labelFont.getName(), Font.PLAIN, 18));
		
		createLayout();
		setModalityType(ModalityType.APPLICATION_MODAL);

		setTitle("SIFT");
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		setLocationRelativeTo(getParent());
	}

	private void createLayout() {

		botonAbrirImagen = new JButton("Abrir");
		checkRobusto = new JCheckBox();

		botonAbrirImagen.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				try {
					
					File imagenElegida = ProcesadorDeImagenes.obtenerInstancia().obtenerFileDesdeArchivo();
					SiftDialog.this.dispose();
					Sift.aplicarMetodoSift(ProcesadorDeImagenes.obtenerInstancia().getArchivoActual().getFile(), imagenElegida, checkRobusto.isSelected());
				} catch (Exception ex) {

					ex.printStackTrace();
				}
			}
		});
		
		Container pane = getContentPane();
		GroupLayout gl = new GroupLayout(pane);
		pane.setLayout(gl);

		gl.setAutoCreateContainerGaps(true);
		gl.setAutoCreateGaps(true);

		gl.setHorizontalGroup(gl.createParallelGroup(Alignment.CENTER)
				.addComponent(titulo)
				.addComponent(labelRobusto)
				.addComponent(checkRobusto)
				.addComponent(botonAbrirImagen).addGap(200));

		gl.setVerticalGroup(gl.createSequentialGroup().addGap(30)
				.addComponent(titulo).addGap(40)
				.addComponent(labelRobusto)
				.addComponent(checkRobusto).addGap(65)
				.addComponent(botonAbrirImagen).addGap(35));

		pack();
	}
}