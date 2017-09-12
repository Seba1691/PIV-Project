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

public class FiltroVelocidadY extends FiltroVisualizacion {

	private static final String YMEDIO = "Y Medio";

	public FiltroVelocidadY() {
		this(0);
	}

	public FiltroVelocidadY(int yMedio) {
		super(1);
		parametros = new HashMap<String, Object>();
		parametros.put(YMEDIO, yMedio);
	}

	@Override
	public void visualizar(ElementoProcesable input) {
		try {
			double[][] mat = ((MapaVectores) input).getMapaVectores();

			int yMedio = (int) parametros.get(YMEDIO);
			
			//Por defecto (con 0) toma la ventana del medio
			if (yMedio == 0)
				yMedio = (int) mat[mat.length / 2][1];
			else { //Sino toma la ventana correspondiente al pixel que se dio como medio
				int i = 0;
				while (mat[i][1] < yMedio) {
					i++;
				}
				yMedio = (int) mat[i][1];
			}			
			
			int yAnt = 0;
			int ySig = 0;

			int i = 0;
			while (mat[i][1] != yMedio) {
				i++;
			}

			yAnt = (int) mat[i - 1][1];

			while (mat[i][1] == yMedio) {
				i++;
			}

			ySig = (int) mat[i][1];

			System.out.println(yAnt + "  " + yMedio + "   " + ySig);

			HashMap<Double, Double> result = new LinkedHashMap<Double, Double>();

			FileWriter file = new FileWriter("tablaVelocidades.csv");
			BufferedWriter fop = new BufferedWriter(file);

			for (int j = 0; j < mat.length; j++)
				if (mat[j][1] == yMedio || mat[j][1] == yAnt || mat[j][1] == ySig) {
					Double r = result.get(mat[j][0]);
					if (r == null)
						r = 0.0;
					r = r + mat[j][6];
					result.put(mat[j][0], r);
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
