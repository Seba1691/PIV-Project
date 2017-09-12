package pivLayer;

import java.util.List;

import cache.CacheManager;

public class Proceso extends Thread {
	
	private Buffer input;
	private Buffer output;
	private FiltroProcesable filtro;
	private Seleccionador seleccionador;
	private int numeroProceso;
	
	public Proceso(FiltroProcesable filtroProcesable, Buffer input, Buffer output, Seleccionador seleccionador, int iteracion) {
		this.filtro = filtroProcesable;
		this.input = input;
		this.output = output;
		this.numeroProceso = iteracion;
		this.seleccionador = seleccionador;
	}

	@Override
	public void run(){	
		List<ElementoProcesable> elementosSeleccionados = seleccionador.seleccionar(input, filtro, numeroProceso);
		List<ElementoProcesable> elementosProcesar = CacheManager.getInstance().get(elementosSeleccionados, filtro);
		if (elementosProcesar == null) {
			try {
				elementosProcesar = filtro.filtrar(elementosSeleccionados);
			} catch (FilterException e) {
				e.printStackTrace();
			}
			CacheManager.getInstance().add(elementosSeleccionados, filtro, elementosProcesar);
		}
		for (int i = 0; i<elementosProcesar.size(); i++){
			ElementoProcesable elementoProcesado = elementosProcesar.get(i);
			output.putElem(numeroProceso*filtro.getCantElementosGenerados()+i,elementoProcesado);		
		}
	}
}
