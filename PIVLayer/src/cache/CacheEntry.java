package cache;

import java.util.List;

import pivLayer.ElementoProcesable;
import pivLayer.Filtro;

public class CacheEntry {
	private List<ElementoProcesable> elementosProcesables;
	private Filtro filter;
	
	public CacheEntry(List<ElementoProcesable> elementosProcesables, Filtro filter) {
		super();
		this.elementosProcesables = elementosProcesables;
		this.filter = filter;
	}
	
	public List<ElementoProcesable> getElementosProcesables() {
		return elementosProcesables;
	}
	
	public void setElementosProcesables(List<ElementoProcesable> elementosProcesables) {
		this.elementosProcesables = elementosProcesables;
	}
	
	public Filtro getFilter() {
		return filter;
	}
	
	public void setFilter(Filtro filter) {
		this.filter = filter;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((elementosProcesables == null) ? 0 : elementosProcesables.hashCode());
		result = prime * result + ((filter == null) ? 0 : filter.hashCode());
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
		CacheEntry other = (CacheEntry) obj;
		if (elementosProcesables == null) {
			if (other.elementosProcesables != null)
				return false;
		} else if (!elementosProcesables.equals(other.elementosProcesables))
			return false;
		if (filter == null) {
			if (other.filter != null)
				return false;
		} else if (!filter.equals(other.filter))
			return false;
		return true;
	}
	
	
	
}
