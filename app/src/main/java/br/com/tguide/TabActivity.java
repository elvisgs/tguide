package br.com.tguide;

import android.content.Intent;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import java.util.List;

import br.com.tguide.domain.PlaceRating;
import br.com.tguide.domain.PlaceRatingRepository;
import br.com.tguide.service.OnDataLoaded;

public class TabActivity extends AppCompatActivity {

    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    private SectionsPagerAdapter mSectionsPagerAdapter;

    private PlaceRatingRepository ratingRepository = PlaceRatingRepository.getInstance();

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private ViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tab);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);
        tabLayout.addOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(mViewPager) {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                if (tab.getPosition() == 1) {
                    RatingsFragment ratingsFragment = null;
                    MapFragment mapFragment = null;
                    for (Fragment fragment : getSupportFragmentManager().getFragments()) {
                        if (fragment instanceof RatingsFragment) {
                            ratingsFragment = (RatingsFragment) fragment;
                        }
                        if (fragment instanceof MapFragment) {
                            mapFragment = (MapFragment) fragment;
                        }
                        if (ratingsFragment != null && mapFragment != null) {
                            ratingsFragment.loadRatings(
                                    mapFragment.getVisibleRegion(),
                                    mapFragment.getMyLocation());
                        }
                    }
                }
            }
        });

        ratingRepository.loadCache(new OnDataLoaded<List<PlaceRating>>() {
            @Override
            public void dataLoaded(List<PlaceRating> data) {
                findViewById(R.id.progress).setVisibility(View.GONE);
            }
        });
    }

    public void goToForm(View view) {
        Uri formUrl = Uri.parse(getResources().getString(R.string.form_url));
        Intent intent = new Intent(Intent.ACTION_VIEW, formUrl);
        startActivity(intent);
    }

    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).

            switch (position) {
                case 0:
                    return new MapFragment();
                case 1:
                    return new RatingsFragment();
                default:
                    return null;
            }
        }

        @Override
        public int getCount() {
            return 2;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            Resources res = getResources();
            switch (position) {
                case 0:
                    return res.getString(R.string.title_tab_map);
                case 1:
                    return res.getString(R.string.title_tab_ratings);
            }

            return null;
        }
    }
}
