package filters;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import pivLayer.ElementoProcesable;
import pivLayer.FilterException;
import pivLayer.FiltroPreProcesamiento;
import pivLayer.Imagen;
import wrapper.ImageJWrapper;

public class FiltroAutoLocalTreshold extends FiltroPreProcesamiento {

	// Parametros

	private static final String METODO = "Metodo";
	private static final String RADIO = "Radio";
	private static final String PARAMETRO1 = "Parametro1";
	private static final String PARAMETRO2 = "Parametro2";
	private static final String FONDO_BLANCO = "FondoBlanco";

	public FiltroAutoLocalTreshold(String metodo, int radio, double parametro1, double parametro2, boolean fondoBlanco) {
		super(1, 1);
		parametros = new HashMap<String, Object>();
		parametros.put(METODO, metodo);
		parametros.put(RADIO, radio);
		parametros.put(PARAMETRO1, parametro1);
		parametros.put(PARAMETRO2, parametro2);
		parametros.put(FONDO_BLANCO, fondoBlanco);
	}

	public FiltroAutoLocalTreshold() {
		this("Bernsen", 15, 0, 0, true);
	}

	@Override
	public List<ElementoProcesable> filtrar(List<ElementoProcesable> input) throws FilterException {
		List<ElementoProcesable> elementosFiltrados = new ArrayList<ElementoProcesable>();
		elementosFiltrados.add(ImageJWrapper.autoLocalThreshold((Imagen) input.get(0), (String) parametros.get(METODO), (Integer) parametros.get(RADIO), (Double) parametros.get(PARAMETRO1), (Double) parametros.get(PARAMETRO2), (Boolean) parametros.get(FONDO_BLANCO)));
		return elementosFiltrados;
	}

	@Override
	public void validateParametros(HashMap<String, Object> parameters) throws FilterException {
		// TODO Auto-generated method stub

	}
}
