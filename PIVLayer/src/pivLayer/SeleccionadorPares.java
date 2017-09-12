package pivLayer;

import java.util.ArrayList;
import java.util.List;

public class SeleccionadorPares extends Seleccionador {

	@Override
	public List<ElementoProcesable> seleccionar(Buffer input, Filtro filtro, int numeroProceso) {
		int cantElementos = filtro.getCantElementosProcesables();

		List<ElementoProcesable> elementos = new ArrayList<ElementoProcesable>();

		for (int j = 0; j < cantElementos; j++)
			elementos.add(input.getElem(j + numeroProceso * cantElementos));

		return elementos;
	}

	@Override
	public int cantIteraciones(int inputSize, int cantElementosProcesables) {
		return inputSize / cantElementosProcesables;
	}

	@Override
	public String toString() {
		return "Simple";
	}
}
