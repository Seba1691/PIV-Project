package pivLayer;

import java.util.List;

public abstract class FiltroProcesable extends Filtro {

	protected int cantElementosGenerados;

	protected FiltroProcesable(int cantElementosProcesables, int cantElementosGenerados) {
		super(cantElementosProcesables);
		this.cantElementosGenerados = cantElementosGenerados;
	}

	public abstract List<ElementoProcesable> filtrar(List<ElementoProcesable> input) throws FilterException;

	public int getCantElementosGenerados() {
		return cantElementosGenerados;
	}

}
