package filters;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedHashMap;

import pivLayer.ElementoProcesable;
import pivLayer.FilterException;
import pivLayer.FiltroVisualizacion;
import pivLayer.MapaVectores;

public class FiltroVelocidadX extends FiltroVisualizacion {

	private static final String XMEDIO = "X Medio";

	public FiltroVelocidadX() {
		this(0);
	}

	public FiltroVelocidadX(int xMedio) {
		super(1);
		parametros = new HashMap<String, Object>();
		parametros.put(XMEDIO, xMedio);
	}

	@Override
	public void visualizar(ElementoProcesable input) {
		try {
			double[][] mat = ((MapaVectores) input).getMapaVectores();

			int xMedio = (int) parametros.get(XMEDIO);
			int xAnt;
			int xSig;
			//Por defecto (con 0) toma la ventana del medio
			if (xMedio == 0) {
				int i = 0;
				while (mat[i][1] == mat[0][1]) {
					i++;
				}
				xMedio = (int) mat[i / 2][0];
				xAnt = (int) mat[(i / 2) - 1][0];
				xSig = (int) mat[(i / 2) + 1][0];
			} else { //Sino toma la ventana correspondiente al pixel que se dio como medio
				int i = 0;
				while (mat[i][0] < xMedio) {
					i++;
				}
				xMedio = (int) mat[i][0];
				xAnt = (int) mat[(i) - 1][0];
				xSig = (int) mat[(i) + 1][0];
			}

			System.out.println(xAnt + "  " + xMedio + "   " + xSig);

			HashMap<Double, Double> result = new LinkedHashMap<Double, Double>();

			FileWriter file = new FileWriter("tablaVelocidades.csv");
			BufferedWriter fop = new BufferedWriter(file);

			for (int j = 0; j < mat.length; j++)
				if (mat[j][0] == xMedio || mat[j][0] == xAnt || mat[j][0] == xSig) {
					Double r = result.get(mat[j][1]);
					if (r == null)
						r = 0.0;
					r = r + mat[j][5];
					result.put(mat[j][1], r);
				}

			for (Double k : result.keySet()) {
				fop.write(String.valueOf(k).replace(".", ",") + "; " + String.valueOf(result.get(k) / 3).replace(".", ","));
				fop.newLine();
			}

			fop.close();

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	@Override
	protected void validateParametros(HashMap<String, Object> parameters) throws FilterException {

	}

}
