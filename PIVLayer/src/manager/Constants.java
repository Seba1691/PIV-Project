package manager;

public interface Constants {

	// Filters
	public static final String filtroPreProcesamiento = "pivLayer.FiltroPreProcesamiento";
	public static final String filtroPIVProcesamiento = "pivLayer.FiltroPIV";
	public static final String filtroPostProcesamiento = "pivLayer.FiltroPostProcesamiento";
	public static final String filtroVisualizacion = "pivLayer.FiltroVisualizacion";

	// Files
	public static final String fileConfig = "filters.config";
	
	//Resources
	public static final String RESOURCES_PATH = System.getProperty("user.dir") + "/resources/";
	public static final String TMP_RESOURCES_PATH = RESOURCES_PATH+"tmp/";
	public static final String IMG_RESOURCES_PATH = RESOURCES_PATH+"img/";

}
