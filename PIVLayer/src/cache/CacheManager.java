package cache;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import pivLayer.ElementoProcesable;
import pivLayer.Filtro;

public class CacheManager {

	private static final int CACHE_SIZE = 300;
	private static CacheManager instance = null;

	private HashMap<CacheEntry, List<ElementoProcesable>> cache;
	private List<CacheEntry> recentUsed;

	protected CacheManager() {
		cache = new HashMap<CacheEntry, List<ElementoProcesable>>();
		recentUsed = new ArrayList<CacheEntry>();
	}

	public static CacheManager getInstance() {
		if (instance == null) {
			instance = new CacheManager();
		}
		return instance;
	}

	public synchronized List<ElementoProcesable> get(List<ElementoProcesable> elementosProcesables, Filtro filter) {
		CacheEntry entry = new CacheEntry(elementosProcesables, filter);
		if (recentUsed.remove(entry))
			recentUsed.add(entry);
		return cache.get(entry);
	}

	public synchronized void add(List<ElementoProcesable> elementosProcesables, Filtro filter, List<ElementoProcesable> value) {
		if (recentUsed.size() == CACHE_SIZE) {
			CacheEntry removeEntry = recentUsed.get(0);
			recentUsed.remove(0);
			cache.remove(removeEntry);
		}
		CacheEntry newEntry = new CacheEntry(elementosProcesables, filter);
		cache.put(newEntry, value);
		recentUsed.add(newEntry);
	}
}
