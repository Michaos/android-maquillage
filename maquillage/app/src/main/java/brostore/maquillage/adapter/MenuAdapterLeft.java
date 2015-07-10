package brostore.maquillage.adapter;

import android.content.Context;
import android.database.DataSetObservable;
import android.database.DataSetObserver;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListAdapter;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import brostore.maquillage.R;
import brostore.maquillage.dao.HideMenuItem;
import brostore.maquillage.wrapper.MenuWrapper;


/**
 * Created by Michaos on 18/05/2015.
 */
public class MenuAdapterLeft implements ExpandableListAdapter, Serializable {

    private static final long serialVersionUID = -2509777690634653959L;
    private List<HideMenuItem> itemsMenu;
    private LayoutInflater inflater;
    private MenuWrapper wrapper = null;
    private MenuWrapper wrapperChild = null;
    private long selected = 0;
    private Context mContext;
    private int width = 0;
    private final DataSetObservable dataSetObservable = new DataSetObservable();

    public MenuAdapterLeft(Context context, List<HideMenuItem> itemsMenu) {
        inflater = LayoutInflater.from(context);
        mContext = context;
        this.itemsMenu = new ArrayList<>(itemsMenu);
    }

    @Override
    protected void finalize() throws Throwable {
        super.finalize();
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView,
                             ViewGroup parentView) {
        View row = convertView;
        if (row == null) {
            row = inflater.inflate(R.layout.item_menu_left, null);
            wrapperChild = new MenuWrapper(row);
            wrapperChild.getSepView().setVisibility(View.GONE);
            row.setTag(wrapperChild);
        } else {
            wrapperChild = (MenuWrapper) row.getTag();
        }
        wrapperChild.getFullImgView().setVisibility(View.GONE);
        wrapperChild.getMenuText().setVisibility(View.VISIBLE);
        wrapperChild.getMenuView().setVisibility(View.GONE);
        wrapperChild.getMenuText().setText(itemsMenu.get(groupPosition).getItemsSousMenu().get(childPosition).getTitre());
        wrapperChild.getSelView().setBackgroundResource(R.drawable.menu_item_background);
        wrapperChild.getSelView().setSelected(selected == getCombinedChildId(groupPosition, childPosition));
        return row;
    }

    @Override
    public HideMenuItem getChild(int groupPosition, int childPosition) {
        return itemsMenu.get(groupPosition).getItemsSousMenu().get(childPosition);
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return itemsMenu.get(groupPosition).getItemsSousMenu().size();
    }

    @Override
    public HideMenuItem getGroup(int groupPosition) {
        return itemsMenu.get(groupPosition);
    }

    @Override
    public int getGroupCount() {
        return itemsMenu.size();
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        View row = convertView;
        if (row == null) {
            row = inflater.inflate(R.layout.item_menu_left, null);
            wrapper = new MenuWrapper(row);
            row.setTag(wrapper);
            width = parent.getWidth();
            wrapper.getSepView().setVisibility(View.GONE);
            wrapper.getSelView().setBackgroundColor(mContext.getResources().getColor(R.color.menu_group_color));
        } else {
            wrapper = (MenuWrapper) row.getTag();
        }
        wrapper.getFullImgView().setVisibility(View.GONE);
        wrapper.getMenuText().setVisibility(View.VISIBLE);
        wrapper.getMenuView().setVisibility(View.GONE);
        wrapper.getMenuText().setText(itemsMenu.get(groupPosition).getTitre());
        wrapper.getSelView().setSelected(selected == getCombinedGroupId(groupPosition));

        return (row);
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }

    @Override
    public void onGroupCollapsed(int groupPosition) {
    }

    @Override
    public void onGroupExpanded(int groupPosition) {
    }

    public long getCombinedChildId(long groupId, long childId) {
        return groupId * 10000L + childId;
    }

    public long getCombinedGroupId(long groupId) {
        return groupId * 10000L;
    }

    public void notifyDataSetChanged() {
        this.getDataSetObservable().notifyChanged();
    }

    @Override
    public void registerDataSetObserver(DataSetObserver observer) {
        this.getDataSetObservable().registerObserver(observer);
    }

    @Override
    public void unregisterDataSetObserver(DataSetObserver observer) {
        this.getDataSetObservable().unregisterObserver(observer);
    }

    protected DataSetObservable getDataSetObservable() {
        return dataSetObservable;
    }

    @Override
    public boolean isEmpty() {
        return false;
    }

    @Override
    public boolean areAllItemsEnabled() {
        return false;
    }
}
