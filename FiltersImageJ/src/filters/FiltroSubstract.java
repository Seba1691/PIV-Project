package filters;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import pivLayer.ElementoProcesable;
import pivLayer.FilterException;
import pivLayer.FiltroPreProcesamiento;
import pivLayer.Imagen;
import wrapper.ImageJWrapper;

public class FiltroSubstract extends FiltroPreProcesamiento {

	public FiltroSubstract() {
		super(2, 2);
	}

	@Override
	public List<ElementoProcesable> filtrar(List<ElementoProcesable> input) throws FilterException {
		List<ElementoProcesable> elementosFiltrados = new ArrayList<ElementoProcesable>();
		elementosFiltrados.add(ImageJWrapper.substractFilter((Imagen) input.get(0), (Imagen) input.get(1)));
		elementosFiltrados.add(ImageJWrapper.substractFilter((Imagen) input.get(1), (Imagen) input.get(0)));
		return elementosFiltrados;
	}

	@Override
	public void validateParametros(HashMap<String, Object> parameters) throws FilterException {
		// TODO Auto-generated method stub
	}

}
