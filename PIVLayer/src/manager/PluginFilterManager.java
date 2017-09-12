package manager;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import pivLayer.Filtro;
import utiles.FileHandling;

public class PluginFilterManager {

	private static PluginFilterManager instance = null;

	private HashMap<String, String> filtrosPreProcesamiento;
	private HashMap<String, String> filtrosPIVProcesamiento;
	private HashMap<String, String> filtrosPostProcesamiento;
	private HashMap<String, String> filtrosVisualizacion;

	private URLClassLoader filtersClassLoader;

	private PluginFilterManager() throws ManagerException {
		filtrosPreProcesamiento = new HashMap<>();
		filtrosPIVProcesamiento = new HashMap<>();
		filtrosPostProcesamiento = new HashMap<>();
		filtrosVisualizacion = new HashMap<>();
		loadFilters();
	}

	private void putFilter(String filterClass, String filterName) {
		Class<?> superClass = null;
		try {
			superClass = Class.forName(filterClass, true, filtersClassLoader);
			boolean findSuperClass = false;
			while (superClass != null && !findSuperClass) {
				System.out.println(superClass.getName());
				switch (superClass.getName()) {
				case Constants.filtroPreProcesamiento:
					filtrosPreProcesamiento.put(filterClass, filterName);
					findSuperClass = true;
					break;
				case Constants.filtroPIVProcesamiento:
					filtrosPIVProcesamiento.put(filterClass, filterName);
					findSuperClass = true;
					break;
				case Constants.filtroPostProcesamiento:
					filtrosPostProcesamiento.put(filterClass, filterName);
					findSuperClass = true;
					break;
				case Constants.filtroVisualizacion:
					filtrosVisualizacion.put(filterClass, filterName);
					findSuperClass = true;
					break;
				}
				superClass = superClass.getSuperclass();
			}

		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}

	private void loadFilters() throws ManagerException {
		try {
			File dir = new File(Settings.filtersPath);
			List<URL> filtersURL = new ArrayList<>();
			for (File file : dir.listFiles()) {

				filtersURL.add(new URL("file:///" + Settings.filtersPath + "/" + file.getName()));
				setFiltersClassLoader(new URLClassLoader(filtersURL.toArray(new URL[filtersURL.size()])));

				InputStream config = FileHandling.getInputStreamFromZip(file, Constants.fileConfig);
				if (config == null)
					continue;
				BufferedReader br = new BufferedReader(new InputStreamReader(config, "UTF-8"));
				String linea;
				while ((linea = br.readLine()) != null) {
					String[] filterData = linea.split(":");
					putFilter(filterData[0], filterData[1]);
				}
			}

		} catch (Exception e) {
			throw new ManagerException("Error al cargar los filtros desde " + Settings.filtersPath, e);
		}
	}

	public static PluginFilterManager getInstance() throws ManagerException {
		if (instance == null) {
			instance = new PluginFilterManager();
		}
		return instance;
	}

	public static void reloadInstance() throws ManagerException {
		instance = new PluginFilterManager();
	}
	
	public Filtro getFilterInstance(String filterClass) throws ManagerException {
		try {
			URLClassLoader filtersClassLoader = getFiltersClassLoader();
			return (Filtro) Class.forName(filterClass, true, filtersClassLoader).newInstance();
		} catch (InstantiationException | IllegalAccessException | ClassNotFoundException e) {
			throw new ManagerException("Error al cargar los filtros desde " + Settings.filtersPath, e);
		}
	}

	public HashMap<String, String> getFiltrosPreProcesamiento() {
		return filtrosPreProcesamiento;
	}

	public void setFiltrosPreProcesamiento(HashMap<String, String> filtrosPreProcesamiento) {
		this.filtrosPreProcesamiento = filtrosPreProcesamiento;
	}

	public HashMap<String, String> getFiltrosPIVProcesamiento() {
		return filtrosPIVProcesamiento;
	}

	public void setFiltrosPIVProcesamiento(HashMap<String, String> filtrosPIVProcesamiento) {
		this.filtrosPIVProcesamiento = filtrosPIVProcesamiento;
	}

	public HashMap<String, String> getFiltrosPostProcesamiento() {
		return filtrosPostProcesamiento;
	}

	public void setFiltrosPostProcesamiento(HashMap<String, String> filtrosPostProcesamiento) {
		this.filtrosPostProcesamiento = filtrosPostProcesamiento;
	}

	public HashMap<String, String> getFiltrosVisualizacion() {
		return filtrosVisualizacion;
	}

	public void setFiltrosVisualizacion(HashMap<String, String> filtrosVisualizacion) {
		this.filtrosVisualizacion = filtrosVisualizacion;
	}

	public URLClassLoader getFiltersClassLoader() {
		return filtersClassLoader;
	}

	public void setFiltersClassLoader(URLClassLoader filtersClassLoader) {
		this.filtersClassLoader = filtersClassLoader;
	}

	// private ArrayList<String> getClassFiles(File jarFile) throws IOException
	// {
	// List<String> classNames = new ArrayList<String>();
	// ZipInputStream zip = new ZipInputStream(new FileInputStream(jarFile));
	// for (ZipEntry entry = zip.getNextEntry(); entry != null; entry =
	// zip.getNextEntry()) {
	// // System.out.println("Archivo leido:" + entry.getName());
	// if (!entry.isDirectory() && entry.getName().endsWith(".class") &&
	// (entry.getName().contains("filters/"))) {
	// String className = entry.getName().replace('/', '.');
	// System.out.println("Clase encontrada:" + className);
	// classNames.add(className.substring(0, className.length() -
	// ".class".length()));
	// }
	// }
	// zip.close();
	// return (ArrayList<String>) classNames;
	// }

}