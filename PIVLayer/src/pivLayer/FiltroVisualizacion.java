package pivLayer;

public abstract class FiltroVisualizacion extends Filtro {

	protected FiltroVisualizacion(int cantElementosProcesables) {
		super(cantElementosProcesables);
	}

	public abstract void visualizar(ElementoProcesable input);

}
