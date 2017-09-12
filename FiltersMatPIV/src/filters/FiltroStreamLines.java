package filters;

import java.util.HashMap;

import pivLayer.ElementoProcesable;
import pivLayer.FilterException;
import pivLayer.FiltroVisualizacion;
import pivLayer.MapaVectores;
import wrapper.MatLabWrapper;

public class FiltroStreamLines extends FiltroVisualizacion {

	public FiltroStreamLines() {
		super(1);
	}

	@Override
	public void visualizar(ElementoProcesable input) {
		MatLabWrapper.visualizarStreamlines((MapaVectores)input);

	}

	@Override
	protected void validateParametros(HashMap<String, Object> parameters) throws FilterException {

	}

}
