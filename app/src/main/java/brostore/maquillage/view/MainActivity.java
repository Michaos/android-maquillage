package brostore.maquillage.view;


import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import brostore.maquillage.R;
import brostore.maquillage.adapter.BasketAdapterRight;
import brostore.maquillage.adapter.MenuAdapterLeft;
import brostore.maquillage.manager.AddressManager;
import brostore.maquillage.manager.MenuManager;
import brostore.maquillage.manager.UserManager;
import brostore.maquillage.utils.Utils;

public class MainActivity extends AppCompatActivity {

    private MainActivity mContext;

    private ExpandableListView mDrawerListViewLeft;
    private RelativeLayout mDrawerRight;
    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mDrawerToggle;

    private MenuAdapterLeft adapterLeft;
    private BasketAdapterRight basketAdapterRight;
    private ListView basket;

    private BroadcastReceiver broadCastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals(MenuManager.OK_MENU)) {
                adapterLeft.notifyDataSetChanged();
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mContext = this;

        IntentFilter filter = new IntentFilter();
        filter.addAction(MenuManager.OK_MENU);
        LocalBroadcastManager.getInstance(this).registerReceiver(broadCastReceiver, filter);

        initMenu();
        initBasket();
        initDrawer();

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
            if (mDrawerLayout.isDrawerOpen(mDrawerRight)) {
                mDrawerLayout.closeDrawer(mDrawerRight);
            } else {
                mDrawerLayout.openDrawer(mDrawerRight);
            }
        }
        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void initDrawer() {
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeButtonEnabled(true);

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerLayout.setDrawerShadow(R.drawable.menu_ombre, GravityCompat.START);
        mDrawerToggle = new ActionBarDrawerToggle(
                this,
                mDrawerLayout,
                R.string.navigation_drawer_open,
                R.string.navigation_drawer_close
        ) {
            @Override
            public void onDrawerClosed(View drawerView) {
                if (drawerView == mDrawerRight) {
                    return;
                }
                super.onDrawerClosed(drawerView);
                supportInvalidateOptionsMenu(); // calls onPrepareOptionsMenu()
            }

            @Override
            public void onDrawerOpened(View drawerView) {
                if (drawerView == mDrawerRight) {
                    return;
                }
                super.onDrawerOpened(drawerView);
                supportInvalidateOptionsMenu(); // calls onPrepareOptionsMenu()
            }

            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {
                if (drawerView == mDrawerRight) {
                    return;
                }
                super.onDrawerSlide(drawerView, slideOffset);
            }
        };

        mDrawerToggle.setDrawerIndicatorEnabled(true);
        // Defer code dependent on restoration of previous instance state.
        mDrawerLayout.post(new Runnable() {
            @Override
            public void run() {
                mDrawerToggle.syncState();
            }
        });
        mDrawerLayout.setDrawerListener(mDrawerToggle);
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
        mDrawerListViewLeft.setOnGroupClickListener(OnGroupClick);
        mDrawerListViewLeft.setOnChildClickListener(OnChildClick);

        Bundle bundle = new Bundle();
        bundle.putInt("groupPosition", 0);
        bundle.putInt("childPosition", 0);
        FragmentHome fh = new FragmentHome();
        fh.setArguments(bundle);
        getSupportFragmentManager().beginTransaction().replace(R.id.content_frame, fh).commit();
        getSupportActionBar().setTitle(MenuManager.getInstance(getApplicationContext()).getItemsMenuLeft().get(0).getItemsSousMenu().get(0).getTitre().toUpperCase());
    }

    private ExpandableListView.OnGroupClickListener OnGroupClick = new ExpandableListView.OnGroupClickListener() {
        @Override
        public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {
            if (MenuManager.getInstance(getApplicationContext()).getItemsMenuLeft().get(groupPosition).getItemsSousMenu() == null) {
                if (MenuManager.getInstance(getApplicationContext()).getItemsMenuLeft().get(groupPosition).getTitre().contains("Compte")) {
                    goToAccount();
                }
            }
            return false;
        }
    };

    private ExpandableListView.OnChildClickListener OnChildClick = new ExpandableListView.OnChildClickListener() {
        @Override
        public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {

            adapterLeft.setSelectionChild(groupPosition, childPosition);

            Bundle bundle = new Bundle();
            bundle.putInt("groupPosition", groupPosition);
            bundle.putInt("childPosition", childPosition);

            FragmentHome fh = new FragmentHome();
            fh.setArguments(bundle);

            getSupportFragmentManager().beginTransaction().replace(R.id.content_frame, fh).commit();

            ((DrawerLayout) findViewById(R.id.drawer_layout)).closeDrawers();

            getSupportActionBar().setTitle(MenuManager.getInstance(getApplicationContext()).getItemsMenuLeft().get(groupPosition).getItemsSousMenu().get(childPosition).getTitre().toUpperCase());

            return false;
        }
    };

    private void initBasket() {
        mDrawerRight = (RelativeLayout) findViewById(R.id.right_drawer);
        basket = (ListView) findViewById(R.id.basket);
        basketAdapterRight = new BasketAdapterRight(this, UserManager.getInstance(this).getUser());
        basket.setAdapter(basketAdapterRight);

        findViewById(R.id.validate_basket).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // if the user is connected
                if (UserManager.getInstance(mContext).getUser().getId() != 0) {
                    startActivity(new Intent(getApplicationContext(), DeliveryActivity.class));
                }
                else {
                    goToAccount();
                }
            }
        });

    }

    public void refreshBasket() {
        if (UserManager.getInstance(this).hasToOpenBasket) {
            basketAdapterRight.notifyDataSetChanged();
            mDrawerLayout.openDrawer(mDrawerRight);
            UserManager.getInstance(this).hasToOpenBasket = false;
        }

        if (basketAdapterRight.getCount() > 0) {
            setTotalBasket();
            setTotalSaved();
            findViewById(R.id.empty_basket).setVisibility(View.GONE);
            findViewById(R.id.total_saving).setVisibility(View.VISIBLE);
            findViewById(R.id.total_basket).setVisibility(View.VISIBLE);
            findViewById(R.id.validate_basket).setVisibility(View.VISIBLE);
        } else {
            findViewById(R.id.empty_basket).setVisibility(View.VISIBLE);
            findViewById(R.id.total_saving).setVisibility(View.GONE);
            findViewById(R.id.total_basket).setVisibility(View.GONE);
            findViewById(R.id.validate_basket).setVisibility(View.GONE);
        }
    }

    public void setTotalBasket() {
        TextView textViewTotalBasket = (TextView) findViewById(R.id.total_basket);
        String total = String.format("%.2f", UserManager.getInstance(this).getUser().getTotalBasket());
        textViewTotalBasket.setText("Total du panier : " + total + "€");
    }

    public void setTotalSaved() {
        TextView textViewTotalSaving = (TextView) findViewById(R.id.total_saving);
        String total = String.format("%.2f", UserManager.getInstance(this).getUser().getTotalSaving());
        textViewTotalSaving.setText("Vous avez économisé : " + total + "€");
    }

    public void popupQty(final int qty, final int position) {

        final Dialog dialog = new Dialog(this, R.style.CustomDialogTheme);
        dialog.setContentView(R.layout.popup_layout);

        EditText et = ((EditText) dialog.findViewById(R.id.article_qty));

        et.setText(qty + "");

        dialog.findViewById(R.id.ok).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                int qty = Integer.parseInt(((EditText) dialog.findViewById(R.id.article_qty)).getText().toString());

                if (qty == 0) {
                    UserManager.getInstance(mContext).removeFromBasket(UserManager.getInstance(mContext).getUser().getBasket().get(position));
                    mContext.refreshBasket();
                } else if (qty < UserManager.getInstance(mContext).getUser().getQuantities().get(position)) {
                    UserManager.getInstance(mContext).minusInBasket(UserManager.getInstance(mContext).getUser().getBasket().get(position), qty);
                    mContext.refreshBasket();
                } else if (qty == UserManager.getInstance(mContext).getUser().getQuantities().get(position)) {
                    dialog.cancel();
                } else {
                    UserManager.getInstance(mContext).addInBasket(UserManager.getInstance(mContext).getUser().getBasket().get(position), qty - UserManager.getInstance(mContext).getUser().getQuantities().get(position));
                    mContext.refreshBasket();
                }

                dialog.cancel();
            }
        });

        dialog.findViewById(R.id.cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.cancel();
            }
        });

        dialog.show();
    }

    protected void goToAccount() {
        FragmentCompte fc = new FragmentCompte();
        getSupportFragmentManager().beginTransaction().replace(R.id.content_frame, fc).commit();
        ((DrawerLayout) findViewById(R.id.drawer_layout)).closeDrawers();
        getSupportActionBar().setTitle("COMPTE");
    }

    @Override
    protected void onResume() {
        super.onResume();
        refreshBasket();
    }

    @Override
    protected void onDestroy() {
        LocalBroadcastManager.getInstance(this).unregisterReceiver(broadCastReceiver);
        super.onDestroy();
    }
}