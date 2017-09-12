package gui;

import java.awt.Image;

import javax.swing.ImageIcon;
import javax.swing.JLabel;

import pivLayer.Imagen;

public class ImageLabelExtended extends JLabel {

	private static final long serialVersionUID = 1L;
	
	private static final int LABEL_WIDTH = 150;
	private static final int LABEL_HEIGHT = 150;
	
	private Imagen imagen;
	
	public ImageLabelExtended(Imagen img){
		super();
		this.imagen = img;
		Image newimg = img.getImage().getScaledInstance(LABEL_WIDTH, LABEL_HEIGHT,  java.awt.Image.SCALE_SMOOTH);
		ImageIcon icon = new ImageIcon(newimg);		
		setIcon(icon);		
		setSize(LABEL_WIDTH, LABEL_HEIGHT);
	}

	public Imagen getImagen() {
		return imagen;
	}

	public void setImagen(Imagen imagen) {
		this.imagen = imagen;
	}
	
}
