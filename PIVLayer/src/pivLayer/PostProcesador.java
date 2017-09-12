package pivLayer;

import java.util.ArrayList;
import java.util.List;

public class PostProcesador extends Procesador {

	public PostProcesador(List<FiltroPostProcesamiento> filtros, List<Seleccionador> seleccionadores) {
		this.filtros = new ArrayList<FiltroProcesable>(filtros);
		this.seleccionadores = new ArrayList<Seleccionador>(seleccionadores);
	}

	public void setFiltros(List<FiltroPostProcesamiento> filtros) {
		this.filtros = new ArrayList<FiltroProcesable>(filtros);
	}

}
