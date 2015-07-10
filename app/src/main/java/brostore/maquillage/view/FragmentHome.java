package brostore.maquillage.view;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import brostore.maquillage.R;
import brostore.maquillage.adapter.HomePagerAdapter;
import brostore.maquillage.custom.PagerSlidingTabStrip;
import brostore.maquillage.dao.HideMenuItem;
import brostore.maquillage.manager.MenuManager;
import brostore.maquillage.utils.Utils;

public class FragmentHome extends Fragment {

    private ViewPager mViewPager;
    private PagerSlidingTabStrip mTabs;
    private List<Fragment> listFragments;

    public FragmentHome() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.frag_home, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mViewPager = (ViewPager) view.findViewById(R.id.pager);
        mTabs = (PagerSlidingTabStrip) view.findViewById(R.id.tabs);
        listFragments = new ArrayList<>();

        Bundle bundle = this.getArguments();
        int groupPosition = bundle.getInt("groupPosition", 0);
        int childPosition = bundle.getInt("childPosition", 0);

        List<HideMenuItem> list = MenuManager.getInstance(getActivity()).getItemsMenuLeft().get(groupPosition).getItemsSousMenu().get(childPosition).getItemsSousMenu();

        if (list != null) {
            for (int i = 0; i < list.size(); i++) {
                listFragments.add(FragmentListProducts.newInstance(list.get(i).getId(), list.get(i).getTitre()));
            }
        } else {
            listFragments.add(FragmentListProducts.newInstance(MenuManager.getInstance(getActivity()).getItemsMenuLeft().get(groupPosition).getItemsSousMenu().get(childPosition).getId(), ""));
        }

        HomePagerAdapter adapter = new HomePagerAdapter(getChildFragmentManager(), listFragments);
        mViewPager.setAdapter(adapter);
        mViewPager.setOffscreenPageLimit(4);
        final int pageMargin = Utils.convertDpToPixel(4, getResources());
        mViewPager.setPageMargin(pageMargin);
        mTabs.setViewPager(mViewPager);
    }
}