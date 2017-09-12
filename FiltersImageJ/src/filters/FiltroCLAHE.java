package filters;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import pivLayer.ElementoProcesable;
import pivLayer.FilterException;
import pivLayer.FiltroPreProcesamiento;
import pivLayer.Imagen;
import wrapper.ImageJWrapper;

public class FiltroCLAHE extends FiltroPreProcesamiento {

	private static final String BLOCK_RADIUS = "blockRadius";
	private static final String BINS = "bins";
	private static final String SLOPE = "slope";

	public FiltroCLAHE(int blockRadius, int bins, float slope) {
		super(1, 1);
		parametros = new HashMap<String, Object>();
		parametros.put(BLOCK_RADIUS, blockRadius);
		parametros.put(BINS, bins);
		parametros.put(SLOPE, slope);
	}

	public FiltroCLAHE() {
		this(63, 255, (float) 3.0);
	}

	@Override
	public List<ElementoProcesable> filtrar(List<ElementoProcesable> input) {
		List<ElementoProcesable> elementosFiltrados = new ArrayList<ElementoProcesable>();
		elementosFiltrados.add(ImageJWrapper.clahe((Imagen) input.get(0), (Integer) parametros.get(BLOCK_RADIUS), (Integer) parametros.get(BINS), (Float) parametros.get(SLOPE), null, null));
		return elementosFiltrados;
	}

	@Override
	protected void validateParametros(HashMap<String, Object> parameters) throws FilterException {
		// TODO Auto-generated method stub

	}
}
