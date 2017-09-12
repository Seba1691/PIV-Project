package gui;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JToolBar;

import pivLayer.Imagen;
import utiles.FileHandling;
import utiles.FileHandlingException;

public class ImageFrame extends JFrame {

	private static final long serialVersionUID = 1L;

	private Imagen image;

	public ImageFrame(Imagen image) {
		super();
		setTitle("Visualizacion de Imagen");
		this.image = image;

		JToolBar toolBar = new JToolBar();
		toolBar.setRollover(true);
		toolBar.setFloatable(false);
		toolBar.setAlignmentX(0.0f);
		getContentPane().add(toolBar, BorderLayout.NORTH);

		JMenuBar menuBar = new JMenuBar();
		toolBar.add(menuBar);

		JMenu mnArchivo = new JMenu("Archivo");
		menuBar.add(mnArchivo);

		JMenuItem mntmGuardar = new JMenuItem("Guardar");
		mntmGuardar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				JFileChooser chooser = new JFileChooser();
				chooser.setSelectedFile(new File("*.png"));
				int retrival = chooser.showSaveDialog(null);
				if (retrival == JFileChooser.APPROVE_OPTION) {
					try {
						FileHandling.writeImageToFile(getImage().getImage(), chooser.getSelectedFile());
					} catch (FileHandlingException e) {
						new GUIException(e).inform();
					}
				}
			}
		});
		mnArchivo.add(mntmGuardar);
		getContentPane().add(new JLabel(new ImageIcon(image.getImage())));
		pack();
	}

	public Imagen getImage() {
		return image;
	}

	public void setImage(Imagen image) {
		this.image = image;
	}

}
