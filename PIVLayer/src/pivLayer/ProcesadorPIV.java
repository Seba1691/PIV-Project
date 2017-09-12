package pivLayer;

import java.util.ArrayList;

public class ProcesadorPIV extends Procesador {

	public ProcesadorPIV(FiltroPIV filtro, Seleccionador seleccionador) {
		this.filtros = new ArrayList<FiltroProcesable>();
		this.filtros.add(filtro);
		this.seleccionadores = new ArrayList<Seleccionador>();
		this.seleccionadores.add(seleccionador);
	}

	public void setFiltros(FiltroPIV filtro) {
		this.filtros = new ArrayList<FiltroProcesable>();
		this.filtros.add(filtro);
	}
}
