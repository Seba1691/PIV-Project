package filters;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import pivLayer.ElementoProcesable;
import pivLayer.FilterException;
import pivLayer.FiltroPostProcesamiento;
import pivLayer.MapaVectores;
import wrapper.JPIVWrapper;

public class FiltroRemplazoVectoresInvalidosPorMediana extends FiltroPostProcesamiento {

	public FiltroRemplazoVectoresInvalidosPorMediana() {
		super(1, 1);
	}

	@Override
	public List<ElementoProcesable> filtrar(List<ElementoProcesable> input) {
		List<ElementoProcesable> elementosFiltrados = new ArrayList<ElementoProcesable>();
		elementosFiltrados.add(JPIVWrapper.replaceByMedianFilter((MapaVectores) input.get(0)));
		return elementosFiltrados;
	}

	@Override
	public void validateParametros(HashMap<String, Object> parameters) throws FilterException {
		// TODO Auto-generated method stub
	}
}
