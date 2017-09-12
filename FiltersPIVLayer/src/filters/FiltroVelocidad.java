package filters;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import pivLayer.ElementoProcesable;
import pivLayer.FilterException;
import pivLayer.FiltroPostProcesamiento;
import pivLayer.MapaVectores;

public class FiltroVelocidad extends FiltroPostProcesamiento {

	private static final String TIEMPO = "Tiempo";
	private static final String TAMANO_PIXEL = "TamañoPixel";

	public FiltroVelocidad() {
		this(0.083, 0.000074);
	}

	public FiltroVelocidad(double tiempo, double tamanoPixel) {
		super(1, 1);
		parametros = new HashMap<String, Object>();
		parametros.put(TIEMPO, tiempo);
		parametros.put(TAMANO_PIXEL, tamanoPixel);
	}

	@Override
	public List<ElementoProcesable> filtrar(List<ElementoProcesable> input) throws FilterException {
		double[][] mat = ((MapaVectores) input.get(0)).getMapaVectores();

		double[][] result = new double[mat.length][8];

		for (int i = 0; i < result.length; i++) {
			result[i][0] = mat[i][0];
			result[i][1] = mat[i][1];
			result[i][2] = mat[i][2];
			result[i][3] = mat[i][3];
			result[i][4] = mat[i][4];
			result[i][5] = mat[i][2] * (double) parametros.get(TAMANO_PIXEL) / (double) parametros.get(TIEMPO);
			result[i][6] = mat[i][3] * (double) parametros.get(TAMANO_PIXEL) / (double) parametros.get(TIEMPO);
			result[i][7] = mat[i][4] * (double) parametros.get(TAMANO_PIXEL) / (double) parametros.get(TIEMPO);
		}
		
		List<ElementoProcesable> elementosFiltrados = new ArrayList<ElementoProcesable>();
		elementosFiltrados.add(new MapaVectores(result));

		return elementosFiltrados;
	}

	@Override
	protected void validateParametros(HashMap<String, Object> parameters) throws FilterException {
		// TODO Auto-generated method stub

	}

}
