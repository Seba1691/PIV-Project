package filters;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import pivLayer.ElementoProcesable;
import pivLayer.FilterException;
import pivLayer.FiltroPreProcesamiento;
import pivLayer.Imagen;
import wrapper.ImageJWrapper;

public class FiltroCrop extends FiltroPreProcesamiento {
	// Parametros

	private static final String POINTS = "Points";

	public FiltroCrop(Integer[][] points) {
		super(1, 1);
		parametros = new HashMap<String, Object>();
		parametros.put(POINTS, points);
	}

	public FiltroCrop() {
		this(new Integer[][]{{1,1},{1,1}});
	}

	@Override
	public List<ElementoProcesable> filtrar(List<ElementoProcesable> input) throws FilterException {
		List<ElementoProcesable> elementosFiltrados = new ArrayList<ElementoProcesable>();
		
		//[[110, 1000, 1000, 110], [20, 20, 820, 820]]
		
		elementosFiltrados.add(ImageJWrapper.crop((Imagen) input.get(0), (Integer[][])parametros.get(POINTS)));
		return elementosFiltrados;
	}

	@Override
	public void validateParametros(HashMap<String, Object> parameters) throws FilterException {
		// TODO implement
	}
}
