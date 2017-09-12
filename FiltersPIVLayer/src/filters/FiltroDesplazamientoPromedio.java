package filters;

import java.util.HashMap;

import javax.swing.JOptionPane;

import pivLayer.ElementoProcesable;
import pivLayer.FilterException;
import pivLayer.FiltroVisualizacion;
import pivLayer.MapaVectores;

public class FiltroDesplazamientoPromedio extends FiltroVisualizacion {

	public FiltroDesplazamientoPromedio() {
		super(1);
	}

	@Override
	public void visualizar(ElementoProcesable input) {

		double promX=0;
		double promY=0;
		double [][] mat = ((MapaVectores)input).getMapaVectores();
		for (int i = 0; i < mat.length; i++) {
			promX += mat[i][2];
			promY += mat[i][3];
		}
		promX = promX/mat.length;
		promY = promY/mat.length;
		
		String mensage = "Promedio en X: "+promX+"\nPromedio en Y: "+promY;
		System.out.println(mensage);
		JOptionPane.showMessageDialog(null, mensage);
		

	}

	@Override
	protected void validateParametros(HashMap<String, Object> parameters) throws FilterException {

	}

}
