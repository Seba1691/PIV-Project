package filters;

import java.util.HashMap;

import javax.swing.JOptionPane;

import pivLayer.ElementoProcesable;
import pivLayer.FilterException;
import pivLayer.FiltroVisualizacion;
import pivLayer.MapaVectores;
import utiles.FileHandling;
import utiles.FileHandlingException;

public class FiltroCalcError extends FiltroVisualizacion {

	public String MODELO_PATH = "Path Modelo";

	public FiltroCalcError() {
		super(1);
		parametros = new HashMap<String, Object>();
		parametros.put(MODELO_PATH, "");
	}

	@Override
	public void visualizar(ElementoProcesable input) {

		double[][] modelo = null;
		try {
			modelo = FileHandling.readArrayFromFile(((String) getParametros().get(MODELO_PATH)));
		} catch (FileHandlingException e) {
			e.printStackTrace();
		}

		double errorAbsolutoPromX = 0;
		double errorAbsolutoPromY = 0;
		double[][] mat = ((MapaVectores) input).getMapaVectores();
		for (int i = 0; i < modelo.length; i++) {
			errorAbsolutoPromX += Math.abs(modelo[i][2] - mat[i][2]);
			errorAbsolutoPromY += Math.abs(modelo[i][3] - mat[i][3]);
		}
		errorAbsolutoPromX = errorAbsolutoPromX / modelo.length;
		errorAbsolutoPromY = errorAbsolutoPromY / modelo.length;

		String mensage = "Error Absoluto en X: " + errorAbsolutoPromX + "\nError Absoluto en Y: " + errorAbsolutoPromY;
		System.out.println(mensage);
		JOptionPane.showMessageDialog(null, mensage);

	}

	@Override
	protected void validateParametros(HashMap<String, Object> parameters) throws FilterException {

	}

}
