package pivLayer;

import java.util.Arrays;

public class MapaVectores extends ElementoProcesable {

	private double[][] mapaVectores;

	public MapaVectores(double[][] mapaVectores) {
		this.mapaVectores = mapaVectores;
	}

	public double[][] getMapaVectores() {
		return mapaVectores;
	}

	public void setMapaVectores(double[][] mapaVectores) {
		this.mapaVectores = mapaVectores;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + Arrays.hashCode(mapaVectores);
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		MapaVectores other = (MapaVectores) obj;
		if (!Arrays.deepEquals(mapaVectores, other.mapaVectores))
			return false;
		return true;
	}
	
}
