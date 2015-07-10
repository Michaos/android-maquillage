package brostore.maquillage.manager;

public class FluxManager {

	public static String DIR_DATA = "/Android/data/brostore.maquillage";
	public static final String URL = "http://www.maquillage.fr/maquillage-marque/";

	public static final String API_KEY = "P863RUC17RUS3S9T7NZM9UP2DBIDHV6S";

	public static final String URL_MENU = URL + "api/categories/2?output_format=JSON";
	public static final String URL_CATEGORIES = URL + "api/categories/__ID__?output_format=JSON";
	public static final String URL_PRODUCT = URL + "api/products/__ID__?output_format=JSON";

}