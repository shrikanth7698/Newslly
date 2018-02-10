package com.shrikanthravi.newslly;


import android.content.Context;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.ogaclejapan.smarttablayout.SmartTabLayout;

import java.util.ArrayList;
import java.util.List;

import static android.view.View.NO_ID;


/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends Fragment {


    public HomeFragment() {
        // Required empty public constructor

    }

    ViewPager viewPager;
    AdView mAdView;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_home, container, false);

        MobileAds.initialize(getActivity(), "ca-app-pub-1385508150167219~1899865729");
        mAdView = view.findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);
        System.out.println("on create view called");
        viewPager = (ViewPager) view.findViewById(R.id.viewpager);
        setupViewPager(viewPager,view);
        return view;
    }

    private void setupViewPager(ViewPager viewPager,View view) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getChildFragmentManager());
        adapter.addFragment(new FeedFragment(), "Feed");
        adapter.addFragment(new TopHeadlinesFragment(), "Top headlines");
        adapter.addFragment(new SearchFragment(),"Search");
        viewPager.setAdapter(adapter);
        SmartTabLayout viewPagerTab = (SmartTabLayout) view.findViewById(R.id.viewpagertab);
        SimpleTabProvider tabProvider = new SimpleTabProvider(getActivity().getApplicationContext(),R.layout.custom_tab,R.id.CustomTabText,R.id.CustomTabImage);
        viewPagerTab.setCustomTabView(tabProvider);
        viewPagerTab.setViewPager(viewPager);
        viewPager.setOffscreenPageLimit(3);
    }


    private static class SimpleTabProvider implements SmartTabLayout.TabProvider {

        private final LayoutInflater inflater;
        private final int tabViewLayoutId;
        private final int tabViewTextViewId;
        private final int tabViewImageViewId;
        private final Typeface font;
        private SimpleTabProvider(Context context, int layoutResId, int textViewId, int ImageViewId) {
            inflater = LayoutInflater.from(context);
            tabViewLayoutId = layoutResId;
            tabViewTextViewId = textViewId;
            tabViewImageViewId = ImageViewId;
            font = Typeface.createFromAsset(context.getAssets(), "fonts/product_san_regular.ttf");
        }

        @Override
        public View createTabView(ViewGroup container, int position, PagerAdapter adapter) {
            View tabView = null;
            TextView tabTitleView = null;
            ImageView tabImageView = null;
            if (tabViewLayoutId != NO_ID) {
                tabView = inflater.inflate(tabViewLayoutId, container, false);
            }

            if (tabViewTextViewId != NO_ID && tabView != null && tabViewImageViewId != NO_ID) {
                tabTitleView = (TextView) tabView.findViewById(tabViewTextViewId);
                tabImageView = (ImageView) tabView.findViewById(tabViewImageViewId);
            }

            if (tabTitleView == null && TextView.class.isInstance(tabView) && ImageView.class.isInstance(tabView)) {
                tabTitleView = (TextView) tabView;
                tabImageView = (ImageView) tabView;
            }

            if (tabTitleView != null && tabImageView != null) {
                tabTitleView.setText(adapter.getPageTitle(position));
                tabTitleView.setTypeface(font);
                switch (adapter.getPageTitle(position).toString()){
                    case "Feed":
                        tabImageView.setBackgroundResource(R.drawable.feed);
                        break;
                    case "Top headlines":
                        tabImageView.setBackgroundResource(R.drawable.newspaper);
                        break;
                    case "Search":
                        tabImageView.setBackgroundResource(R.drawable.search);
                    default:
                        break;
                }
            }

            return tabView;
        }
    }


    class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }
}
