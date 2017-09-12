package filters;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import pivLayer.ElementoProcesable;
import pivLayer.FilterException;
import pivLayer.FiltroPostProcesamiento;
import pivLayer.MapaVectores;
import wrapper.MatLabWrapper;

public class LocalFilter extends FiltroPostProcesamiento {

	public LocalFilter() {
		super(1, 1);
	}

	@Override
	public List<ElementoProcesable> filtrar(List<ElementoProcesable> input) throws FilterException {
		List<ElementoProcesable> elementosFiltrados = new ArrayList<ElementoProcesable>();
		elementosFiltrados.add(MatLabWrapper.localFilter(((MapaVectores)input.get(0))));
		return elementosFiltrados;
	}

	@Override
	protected void validateParametros(HashMap<String, Object> parameters) throws FilterException {

	}

}
