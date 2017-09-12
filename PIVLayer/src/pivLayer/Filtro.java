package pivLayer;

import java.util.HashMap;

public abstract class Filtro {

	protected int cantElementosProcesables;
	protected HashMap<String, Object> parametros;

	protected Filtro(int cantElementosProcesables) {
		this.cantElementosProcesables = cantElementosProcesables;
	}

	protected abstract void validateParametros(HashMap<String, Object> parameters) throws FilterException;

	public void setParametros(HashMap<String, Object> parameters) throws FilterException {
		validateParametros(parameters);
		this.parametros = parameters;
	}

	public HashMap<String, Object> getParametros() {
		return parametros;
	}

	public int getCantElementosProcesables() {
		return cantElementosProcesables;
	}

	public void setCantElementosProcesables(int cantElementosProcesables) {
		this.cantElementosProcesables = cantElementosProcesables;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		HashMap<String, Object> parameters = getParametros();
		int result = 1;
		result = prime * result + cantElementosProcesables;
		result = prime * result + ((parameters == null) ? 0 : parameters.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Filtro other = (Filtro) obj;
		if (cantElementosProcesables != other.cantElementosProcesables)
			return false;
		if (getCantElementosProcesables() != other.getCantElementosProcesables())
			return false;
		return true;
	}

}
