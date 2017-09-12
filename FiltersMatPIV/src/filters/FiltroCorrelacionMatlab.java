package filters;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

import pivLayer.ElementoProcesable;
import pivLayer.FilterException;
import pivLayer.FiltroPIV;
import pivLayer.Imagen;
import wrapper.MatLabWrapper;

public class FiltroCorrelacionMatlab extends FiltroPIV {

	private static final String MULTI_PASS = "multiPass";
	private static final String WINDOWS_WIDTH = "windowsWidth";
	private static final String WINDOWS_HEIGHT = "windowsHeight";
	private static final String OVERLAP = "ovelap";

	public FiltroCorrelacionMatlab(boolean multiPass, Double[] interWindowsWidth, Double[] interWindowsHeight, double ovelap) {
		super(2, 1);
		parametros = new LinkedHashMap<String, Object>();
		parametros.put(MULTI_PASS, multiPass);
		parametros.put(WINDOWS_WIDTH, interWindowsWidth);
		parametros.put(WINDOWS_HEIGHT, interWindowsHeight);
		parametros.put(OVERLAP, ovelap);
	}

	public FiltroCorrelacionMatlab() {
		this(true, new Double[] {64.0, 32.0, 16.0 }, new Double[] { 64.0, 32.0, 16.0  }, 0.5);
	}

	@Override
	public List<ElementoProcesable> filtrar(List<ElementoProcesable> input) throws FilterException {
		List<ElementoProcesable> elementosFiltrados = new ArrayList<ElementoProcesable>();
		if ((boolean) parametros.get(MULTI_PASS)) {			
			Double[] wh = (Double[]) parametros.get(WINDOWS_HEIGHT);
			Double[] ww = (Double[]) parametros.get(WINDOWS_WIDTH);
			Double[][] windowsSize = new Double[wh.length][];
			for (int i = 0; i < wh.length; i++)
				windowsSize[i] = new Double[] { wh[i], ww[i] };
			elementosFiltrados.add(MatLabWrapper.CorrealcionCruzada(((Imagen) input.get(0)), ((Imagen) input.get(1)), windowsSize, (double) parametros.get(OVERLAP), "multin", 3));
		} else {
			elementosFiltrados.add(MatLabWrapper.CorrealcionCruzada(((Imagen) input.get(0)), ((Imagen) input.get(1)), ((Double[]) parametros.get(WINDOWS_HEIGHT))[0], (double) parametros.get(OVERLAP), "single", 1));
		}
		return elementosFiltrados;
	}

	@Override
	protected void validateParametros(HashMap<String, Object> parameters) throws FilterException {

	}

}
