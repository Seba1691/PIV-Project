package filters;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

import pivLayer.ElementoProcesable;
import pivLayer.FilterException;
import pivLayer.FiltroPIV;
import pivLayer.Imagen;
import wrapper.JPIVWrapper;
import wrapper.WrapperException;

public class FiltroCorrelacionCruzada extends FiltroPIV {

	private static final String MULTI_PASS = "multiPass";
	private static final String INTER_WINDOWS_WIDTH = "interWindowsWidth";
	private static final String INTER_WINDOWS_HEIGHT = "interWindowsHeight";
	private static final String SEARCH_DOMAIN_WIDTH = "searchDomainWidth";
	private static final String SEARCH_DOMAIN_HEIGHT = "searchDomainHeigth";
	private static final String HORIZONTAL_VECTOR_SPACING = "horizontalVertorSpacing";
	private static final String VERTICAL_VECTOR_SPACING = "verticalVertorSpacing";
	private static final String ROI = "roi";
	private static final String ROI_MATRIX = "roiMatrix";
	private static final String HORIZONTAL_PRESHIFT = "horizontalPreShift";
	private static final String VERTICAL_PRESHIFT = "verticalPreShift";
	private static final String NORMALIZED_MEDIAN_TEST = "normalizedMedianTest";
	private static final String REPLACE_INVALID_VECTOR_BY_MEDIAN = "replaceInvalidVectorByMedian";
	private static final String MEDIAN_FILTER = "medianFilter";
	private static final String SMOOTHING = "smoothing";
	private static final String DEFORM_INTERROGATION_WINDOWS = "deformInterrogationWindows";
	private static final String EXPORT_CORRELATION_FUNCTIONS = "exportCorrelationFunctions";
	private static final String EXPORT_CORRELATION_VECTORS = "exportCorrelationVector";
	private static final String EXPORT_CORRELATION_PASS = "exportCorrelationPass";
	private static final String ONLY_SUM_OF_CORRELATION = "onlySumOfCorrelation";

	public FiltroCorrelacionCruzada(int multiPass, Integer[] interWindowsWidth, Integer[] interWindowsHeight, Integer[] searchDomainWidth, Integer[] searchDomainHeigth, Integer[] horizontalVertorSpacing, Integer[] verticalVertorSpacing, boolean roi, Integer[][] roiMatrix, int horizontalPreShift,
			int verticalPreShift, boolean normalizedMedianTest, boolean replaceInvalidVectorByMedian, boolean medianFilter, boolean smoothing, boolean deformInterrogationWindows, boolean exportCorrelationFunctions, int exportCorrelationVector, int exportCorrelationPass, boolean onlySumOfCorrelation) {
		super(2, 1);
		parametros = new LinkedHashMap<String, Object>();
		parametros.put(MULTI_PASS, multiPass);
		parametros.put(INTER_WINDOWS_WIDTH, interWindowsWidth);
		parametros.put(INTER_WINDOWS_HEIGHT, interWindowsHeight);
		parametros.put(SEARCH_DOMAIN_WIDTH, searchDomainWidth);
		parametros.put(SEARCH_DOMAIN_HEIGHT, searchDomainHeigth);
		parametros.put(HORIZONTAL_VECTOR_SPACING, horizontalVertorSpacing);
		parametros.put(VERTICAL_VECTOR_SPACING, verticalVertorSpacing);
		parametros.put(ROI, roi);
		parametros.put(ROI_MATRIX, roiMatrix);
		parametros.put(HORIZONTAL_PRESHIFT, horizontalPreShift);
		parametros.put(VERTICAL_PRESHIFT, verticalPreShift);
		parametros.put(NORMALIZED_MEDIAN_TEST, normalizedMedianTest);
		parametros.put(REPLACE_INVALID_VECTOR_BY_MEDIAN, replaceInvalidVectorByMedian);
		parametros.put(MEDIAN_FILTER, medianFilter);
		parametros.put(SMOOTHING, smoothing);
		parametros.put(DEFORM_INTERROGATION_WINDOWS, deformInterrogationWindows);
		parametros.put(EXPORT_CORRELATION_FUNCTIONS, exportCorrelationFunctions);
		parametros.put(EXPORT_CORRELATION_PASS, exportCorrelationPass);
		parametros.put(EXPORT_CORRELATION_VECTORS, exportCorrelationVector);
		parametros.put(ONLY_SUM_OF_CORRELATION, onlySumOfCorrelation);
	}

	public FiltroCorrelacionCruzada() {
		this(3, new Integer[] { 64, 32, 32 }, new Integer[] { 64, 32, 32 }, new Integer[] { 32, 8, 8 }, new Integer[] { 32, 8, 8 }, //
				new Integer[] { 32, 16, 12 }, new Integer[] { 32, 16, 12 }, false, new Integer[][] { { 0, 512 }, { 0, 512 } }, 0, 0, //
				true, true, false, true, false, false, 0, 0, false);
	}

	@Override
	public List<ElementoProcesable> filtrar(List<ElementoProcesable> input) throws FilterException {
		try {
			List<ElementoProcesable> elementosFiltrados = new ArrayList<ElementoProcesable>();
			elementosFiltrados.add(JPIVWrapper.doPiv((Imagen) input.get(0), (Imagen) input.get(1), //
					(int) parametros.get(MULTI_PASS), (Integer[]) parametros.get(INTER_WINDOWS_WIDTH), (Integer[]) parametros.get(INTER_WINDOWS_HEIGHT), (Integer[]) parametros.get(SEARCH_DOMAIN_WIDTH), (Integer[]) parametros.get(SEARCH_DOMAIN_HEIGHT), //
					(Integer[]) parametros.get(HORIZONTAL_VECTOR_SPACING), (Integer[]) parametros.get(VERTICAL_VECTOR_SPACING), (boolean) parametros.get(ROI), (Integer[][]) parametros.get(ROI_MATRIX), (int) parametros.get(HORIZONTAL_PRESHIFT), (int) parametros.get(VERTICAL_PRESHIFT), //
					(boolean) parametros.get(NORMALIZED_MEDIAN_TEST), (boolean) parametros.get(REPLACE_INVALID_VECTOR_BY_MEDIAN), (boolean) parametros.get(MEDIAN_FILTER), (boolean) parametros.get(SMOOTHING), (boolean) parametros.get(DEFORM_INTERROGATION_WINDOWS), //
					(boolean) parametros.get(EXPORT_CORRELATION_FUNCTIONS), (int) parametros.get(EXPORT_CORRELATION_PASS), (int) parametros.get(EXPORT_CORRELATION_VECTORS), (boolean) parametros.get(ONLY_SUM_OF_CORRELATION)));
			return elementosFiltrados;
		} catch (WrapperException e) {
			throw new FilterException("Error al ejecutar el filtro de correlacion cruzada", e);
		}
	}

	@Override
	public void validateParametros(HashMap<String, Object> parameters) throws FilterException {
		int passCount = (int) parameters.get(MULTI_PASS);
		if (((Integer[]) parameters.get(INTER_WINDOWS_WIDTH)).length != passCount)
			throw new FilterException("El tamaño del arreglo del parametro " + INTER_WINDOWS_WIDTH + " debe ser igual al numero de pasadas (" + passCount + ")");
		if (((Integer[]) parameters.get(INTER_WINDOWS_HEIGHT)).length != passCount)
			throw new FilterException("El tamaño del arreglo del parametro " + INTER_WINDOWS_HEIGHT + " debe ser igual al numero de pasadas (" + passCount + ")");
		if (((Integer[]) parameters.get(SEARCH_DOMAIN_WIDTH)).length != passCount)
			throw new FilterException("El tamaño del arreglo del parametro " + SEARCH_DOMAIN_WIDTH + " debe ser igual al numero de pasadas (" + passCount + ")");
		if (((Integer[]) parameters.get(SEARCH_DOMAIN_HEIGHT)).length != passCount)
			throw new FilterException("El tamaño del arreglo del parametro " + SEARCH_DOMAIN_HEIGHT + " debe ser igual al numero de pasadas (" + passCount + ")");
		if (((Integer[]) parameters.get(HORIZONTAL_VECTOR_SPACING)).length != passCount)
			throw new FilterException("El tamaño del arreglo del parametro " + HORIZONTAL_VECTOR_SPACING + " debe ser igual al numero de pasadas (" + passCount + ")");
		if (((Integer[]) parameters.get(VERTICAL_VECTOR_SPACING)).length != passCount)
			throw new FilterException("El tamaño del arreglo del parametro " + VERTICAL_VECTOR_SPACING + " debe ser igual al numero de pasadas (" + passCount + ")");
		if (((Integer[][]) parameters.get(ROI_MATRIX)).length != 2)
			throw new FilterException("El parametro " + ROI_MATRIX + " debe ser una matriz de 2x2");
		if (((Integer[][]) parameters.get(ROI_MATRIX))[0].length != 2)
			throw new FilterException("El parametro " + ROI_MATRIX + " debe ser una matriz de 2x2");
		if (((Integer[][]) parameters.get(ROI_MATRIX))[1].length != 2)
			throw new FilterException("El parametro " + ROI_MATRIX + " debe ser una matriz de 2x2");
	}

}
