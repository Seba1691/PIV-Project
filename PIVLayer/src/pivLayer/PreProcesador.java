package pivLayer;
import java.util.ArrayList;
import java.util.List;


public class PreProcesador extends Procesador {
	
	public PreProcesador(List<FiltroPreProcesamiento> filtros, List<Seleccionador> seleccionadores) {
		this.filtros = new ArrayList<FiltroProcesable>(filtros);
		this.seleccionadores = new ArrayList<Seleccionador>(seleccionadores);
	}
	
	public void setFiltros(List<FiltroPreProcesamiento> filtros){
		this.filtros = new ArrayList<FiltroProcesable>(filtros);
	}

}
