package filters;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import pivLayer.ElementoProcesable;
import pivLayer.FilterException;
import pivLayer.FiltroPostProcesamiento;
import pivLayer.MapaVectores;
import wrapper.JPIVWrapper;

public class FiltroTestMedianaNormalizada extends FiltroPostProcesamiento {

	private static final String UMBRAL = "Umbral";
	private static final String NIVEL_RUIDO = "NivelRuido";

	public FiltroTestMedianaNormalizada(double umbral, double nivelRuido) {
		super(1, 1);
		parametros = new HashMap<String, Object>();
		parametros.put(UMBRAL, umbral);
		parametros.put(NIVEL_RUIDO, nivelRuido);
	}

	public FiltroTestMedianaNormalizada() {
		this(2.0, 0.1);
	}

	@Override
	public List<ElementoProcesable> filtrar(List<ElementoProcesable> input) {
		List<ElementoProcesable> elementosFiltrados = new ArrayList<ElementoProcesable>();
		elementosFiltrados.add(JPIVWrapper.normalizedMedianTestFilter((MapaVectores) input.get(0), (Double) parametros.get(NIVEL_RUIDO), (Double) parametros.get(UMBRAL)));
		return elementosFiltrados;
	}

	@Override
	public void validateParametros(HashMap<String, Object> parameters) throws FilterException {
		// TODO Auto-generated method stub
	}
}
