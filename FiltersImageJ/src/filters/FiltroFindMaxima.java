package filters;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import pivLayer.ElementoProcesable;
import pivLayer.FilterException;
import pivLayer.FiltroPreProcesamiento;
import pivLayer.Imagen;
import wrapper.ImageJWrapper;

public class FiltroFindMaxima extends FiltroPreProcesamiento {

	// Parametros

	private static final String NOISE_TOLERANCE = "NoiseTolerance";
	private static final String EXCLUDE_EDGES = "ExcludeEdges";

	public FiltroFindMaxima(int noiseTolerance, boolean excludeEdges) {
		super(1, 1);
		parametros = new HashMap<String, Object>();
		parametros.put(NOISE_TOLERANCE, noiseTolerance);
		parametros.put(EXCLUDE_EDGES, excludeEdges);
	}

	public FiltroFindMaxima() {
		this(5, false);
	}

	@Override
	public List<ElementoProcesable> filtrar(List<ElementoProcesable> input) throws FilterException {
		List<ElementoProcesable> elementosFiltrados = new ArrayList<ElementoProcesable>();
		elementosFiltrados.add(ImageJWrapper.findMaxima((Imagen) input.get(0), (Integer) parametros.get(NOISE_TOLERANCE), (Boolean) parametros.get(EXCLUDE_EDGES)));
		return elementosFiltrados;
	}

	@Override
	public void validateParametros(HashMap<String, Object> parameters) throws FilterException {
		if (((Integer) parameters.get(NOISE_TOLERANCE)) < 0)
			throw new FilterException("El parametro " + NOISE_TOLERANCE + " debe ser mayor a 0");
	}

}
