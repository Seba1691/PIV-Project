package pivLayer;

import java.util.List;

public abstract class Seleccionador {

	public abstract List<ElementoProcesable> seleccionar(Buffer input, Filtro filtro, int numeroProceso);
	
	public abstract int cantIteraciones(int inputSize, int cantElemProcesables);
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		return true;
	}

}
