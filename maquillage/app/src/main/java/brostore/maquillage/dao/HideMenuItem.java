package brostore.maquillage.dao;

import android.content.Context;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import brostore.maquillage.manager.MenuManager;

public class HideMenuItem implements Serializable {
    /**
     *
     */
    private static final long serialVersionUID = -4681270151313451760L;
    private Context mContext;
    private int id;
    private String titre;
    private List<HideMenuItem> itemsSousMenu;

    public HideMenuItem(Context c, JSONObject j) {
        mContext = c;
        id = j.optJSONObject("category").optInt("id");
        titre = j.optJSONObject("category").optString("name");

        if(j.optJSONObject("category").optJSONObject("associations") != null) {

            JSONArray jsonArray = j.optJSONObject("category").optJSONObject("associations").optJSONArray("categories");

            if (jsonArray != null && jsonArray.length() != 0) {

                itemsSousMenu = new ArrayList<>();

                for (int i = 0; i < jsonArray.length(); i++) {
                    itemsSousMenu.add(MenuManager.getInstance(mContext).parseMenuBis(jsonArray.optJSONObject(i)));
                }
            }
        }
    }

    public int getId() {
        return id;
    }

    public String getTitre() {
        return titre;
    }

    public List<HideMenuItem> getItemsSousMenu() {
        return itemsSousMenu;
    }

    public void setItemsSousMenu(List<HideMenuItem> itemsSousMenu) {
        this.itemsSousMenu = itemsSousMenu;
    }

}
