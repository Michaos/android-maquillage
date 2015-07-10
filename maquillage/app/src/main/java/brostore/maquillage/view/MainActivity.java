package brostore.maquillage.view;


import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ExpandableListView;

import brostore.maquillage.R;
import brostore.maquillage.adapter.MenuAdapterLeft;
import brostore.maquillage.manager.MenuManager;
import brostore.maquillage.utils.Utils;

/**
 * Created by Michaos on 18/05/2015.
 */
public class MainActivity extends ActionBarActivity {

    private ExpandableListView mDrawerListViewLeft;
    private ExpandableListView mDrawerListViewRight;
    private MenuAdapterLeft adapterLeft;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initMenu();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void initMenu() {
        mDrawerListViewLeft = (ExpandableListView) findViewById(R.id.left_drawer);
        adapterLeft = new MenuAdapterLeft(this, MenuManager.getInstance(this).getItemsMenuLeft());
        mDrawerListViewLeft.setAdapter(adapterLeft);

        mDrawerListViewLeft.setGroupIndicator(null);
        mDrawerListViewLeft.setChildIndicator(null);
        mDrawerListViewLeft.setDivider(getResources().getDrawable(R.color.menu_background));
        mDrawerListViewLeft.setChildDivider(getResources().getDrawable(R.color.menu_background));
        mDrawerListViewLeft.setDividerHeight(Utils.convertDpToPixel(1, getResources()));
        //mDrawerListViewLeft.setOnGroupClickListener(OnGroupClick);
        mDrawerListViewLeft.setOnChildClickListener(OnChildClick);
    }

    private ExpandableListView.OnChildClickListener OnChildClick = new ExpandableListView.OnChildClickListener() {
        @Override
        public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {

            v.setSelected(true);

            Bundle bundle = new Bundle();
            bundle.putInt("groupPosition", groupPosition);
            bundle.putInt("childPosition", childPosition);

            FragmentHome fh = new FragmentHome();
            fh.setArguments(bundle);

            getSupportFragmentManager().beginTransaction().replace(R.id.content_frame, fh).commit();

            ((DrawerLayout) findViewById(R.id.drawer_layout)).closeDrawers();

            return false;
        }
    };

}