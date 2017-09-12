package filters;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

import pivLayer.ElementoProcesable;
import pivLayer.FilterException;
import pivLayer.FiltroPostProcesamiento;
import pivLayer.MapaVectores;

public class FiltroCortarMapaVelocidades extends FiltroPostProcesamiento {

	private static final String X1 = "X1";
	private static final String Y1 = "Y1";
	private static final String X2 = "X2";
	private static final String Y2 = "Y2";

	public FiltroCortarMapaVelocidades(int x1, int y1, int x2, int y2) {
		super(1, 1);
		parametros = new LinkedHashMap<String, Object>();
		parametros.put(X1, x1);
		parametros.put(Y1, y1);
		parametros.put(X2, x2);
		parametros.put(Y2, y2);
	}

	public FiltroCortarMapaVelocidades() {
		this(256, 320, 1600, 960);
	}

	@Override
	public List<ElementoProcesable> filtrar(List<ElementoProcesable> input) throws FilterException {

		double[][] mat = ((MapaVectores) input.get(0)).getMapaVectores();
		
		int x1 = (int) parametros.get(X1);
		int y1 = (int) parametros.get(Y1);
		int x2 = (int) parametros.get(X2);
		int y2 = (int) parametros.get(Y2);
		

		List<double[]> result = new ArrayList<double[]>();
		for (int i = 0; i < mat.length; i++)
			if (mat[i][0] >= x1 && mat[i][0] <= x2 && mat[i][1] >= y1 && mat[i][1] <= y2) {
				result.add(mat[i]);
			}

		List<ElementoProcesable> aRetornar = new ArrayList<ElementoProcesable>();
		aRetornar.add(new MapaVectores(toMatrix(result)));
		return aRetornar;

	}

	private double[][] toMatrix(List<double[]> input) {
		double[][] result = new double[input.size()][];
		int i = 0;
		for (double[] fila : input) {
			result[i] = fila;
			i++;
		}
		return result;
	}

	@Override
	protected void validateParametros(HashMap<String, Object> parameters) throws FilterException {

	}

}
