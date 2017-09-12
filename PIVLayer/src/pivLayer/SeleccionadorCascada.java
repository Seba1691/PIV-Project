package pivLayer;

import java.util.ArrayList;
import java.util.List;

public class SeleccionadorCascada extends Seleccionador {

	@Override
	public List<ElementoProcesable> seleccionar(Buffer input, Filtro filtro, int numeroProceso) {
		int cantElementos = filtro.getCantElementosProcesables();

		List<ElementoProcesable> elementos = new ArrayList<ElementoProcesable>();

		for (int j = 0; j < cantElementos; j++)
			elementos.add(input.getElem(numeroProceso + j));

		return elementos;

	}

	@Override
	public int cantIteraciones(int inputSize, int cantElementosProcesables) {
		return inputSize - cantElementosProcesables +1;
	}
	
	@Override
	public String toString() {
		return "Cascada";
	}

}
