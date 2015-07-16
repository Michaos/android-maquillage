package brostore.maquillage.manager;

public class FluxManager {

    public static String DIR_DATA = "/Android/data/brostore.maquillage";

    public static final String API_KEY = "P863RUC17RUS3S9T7NZM9UP2DBIDHV6S";
    public static final String COOKIE_KEY = "aotzJECbeSfvXZmTJhOi2oKLJ1KKkCX3PVNDqOFNOcLR4HUhKKgB19Lw";

    public static final String URL_MENU = "http://www.maquillage.fr/maquillage-marque/api/categories/2?output_format=JSON";
    public static final String URL_CATEGORIES = "http://www.maquillage.fr/maquillage-marque/api/categories/__ID__?output_format=JSON";
    public static final String URL_PRODUCT = "http://www.maquillage.fr/maquillage-marque/api/products/__ID__?output_format=JSON";
    public static final String URL_IMAGES = "http://www.maquillage.fr/maquillage-marque/api/images/products/__ID_PRODUCT__/__ID_IMAGE__";
    public static final String URL_SPECIFIC_PRICE = "http://www.maquillage.fr/maquillage-marque/api/specific_prices/?filter[id_product]=__ID_PRODUCT__&display=full&output_format=JSON";
    public static final String URL_STOCK = "http://www.maquillage.fr/maquillage-marque/api/stock_availables/__ID_QUANTITY__?output_format=JSON";

    public static final String URL_CONNECT = "http://maquillage.fr/maquillage-marque/api/customers/?filter[email]=__EMAIL__&filter[passwd]=__ENCRYPTED_MDP__&output_format=JSON";
    public static final String URL_GET_USER = "http://www.maquillage.fr/maquillage-marque/api/customers/__ID__?output_format=JSON";
    public static final String URL_GET_USER_XML = "http://www.maquillage.fr/maquillage-marque/api/customers/__ID__";
    public static final String URL_PUT_USER = "http://www.maquillage.fr/maquillage-marque/api/customers/__ID__";

    public static final String URL_CHECK_MAIL = "http://www.maquillage.fr/maquillage-marque/api/customers/?filter[email]=__MAIL__&output_format=JSON";
    public static final String URL_GET_BLANK_USER = "http://www.maquillage.fr/maquillage-marque/api/customers/?schema=blank";

    public static final String URL_POST_USER = "http://www.maquillage.fr/maquillage-marque/api/customers";
    public static final String URL_POST_ADDRESS = "http://www.maquillage.fr/maquillage-marque/api/addresses";

    public static final String URL_GET_USER_ADDRESSES = "http://www.maquillage.fr/maquillage-marque/api/addresses/?filter[id_customer]=__ID__&output_format=JSON";
    public static final String URL_GET_ADDRESS = "http://www.maquillage.fr/maquillage-marque/api/addresses/__ID__&output_format=JSON";
    public static final String URL_GET_BLANK_ADDRESS = "http://www.maquillage.fr/maquillage-marque/api/addresses/?schema=blank";

    public static final String URL_GET_COUNTRY = "http://www.maquillage.fr/maquillage-marque/api/countries/__ID__&output_format=JSON";

    public static final String URL_GET_USER_ORDERS = "http://www.maquillage.fr/maquillage-marque/api/orders/?filter[id_customer]=__ID__&output_format=JSON";
    public static final String URL_GET_ORDER = "http://www.maquillage.fr/maquillage-marque/api/orders/__ID__&output_format=JSON";

    public static final String URL_SHARE = "http://www.maquillage.fr/maquillage-marque/__ID__-__LINK_REWRITE__.html";

}