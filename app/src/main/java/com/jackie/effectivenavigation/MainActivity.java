/*
 * Copyright 2016 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.jackie.effectiveNavigation;

import android.app.ActionBar;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * Created 16/1/8.
 *
 * @author Jackie
 * @version 1.0
 */
public class MainActivity extends FragmentActivity {

    // When requested, this adapter returns a DemoObjectFragment,
    // representing an object in the collection.
    FragmentStatePagerAdapter mAdapter;
    ViewPager myViewPager;
    ActionBar actionBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Set up the action bar.
        actionBar = getActionBar();

        // ViewPager and its adapters use support library
        // fragments, so use getSupportFragmentManager.
        mAdapter = new DemoCollectionPagerAdapter(getSupportFragmentManager());
        myViewPager = (ViewPager) findViewById(R.id.my_pager);
        myViewPager.setAdapter(mAdapter);
        myViewPager.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                // When swiping between pages. select the corresponding tab.
                getActionBar().setSelectedNavigationItem(position%3);
            }
        });


        // Specify that we will be displaying tabs in the action bar.
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

        // Create a tab listener that is called when the user changes tabs.
        ActionBar.TabListener tabListener = new ActionBar.TabListener() {

            /**
             * Called when a tab enters the selected state.
             *
             * @param tab The tab that was selected
             * @param ft  A {@link FragmentTransaction} for queuing fragment operations to execute
             *            during a tab switch. The previous tab's unselect and this tab's select will be
             *            executed in a single transaction. This FragmentTransaction does not support
             */
            @Override
            public void onTabSelected(ActionBar.Tab tab, FragmentTransaction ft) {
                // When the tab is selected, switch to the corresponding page in the ViewPager.
                int position = myViewPager.getCurrentItem();
                int tabPosition = tab.getPosition();
                int move = tabPosition - (position % 3);
                myViewPager.setCurrentItem(position + move);
            }

            /**
             * Called when a tab exits the selected state.
             *
             * @param tab The tab that was unselected
             * @param ft  A {@link FragmentTransaction} for queuing fragment operations to execute
             *            during a tab switch. This tab's unselect and the newly selected tab's select
             *            will be executed in a single transaction. This FragmentTransaction does not
             */
            @Override
            public void onTabUnselected(ActionBar.Tab tab, FragmentTransaction ft) {

            }

            /**
             * Called when a tab that is already selected is chosen again by the user.
             * Some applications may use this action to return to the top level of a category.
             *
             * @param tab The tab that was reselected.
             * @param ft  A {@link FragmentTransaction} for queuing fragment operations to execute
             *            once this method returns. This FragmentTransaction does not support
             */
            @Override
            public void onTabReselected(ActionBar.Tab tab, FragmentTransaction ft) {

            }
        };

        for (int i = 0; i < 3; i++) {
            actionBar.addTab(actionBar.newTab().setText("Tab " + (i + 1)).setTabListener(tabListener));
        }
    }

    /**
     * Since this is an object collection, use a FragmentStatePagerAdapter,
     * and NOT a FragmentPagerAdapter.
     */
    public static class DemoCollectionPagerAdapter extends FragmentStatePagerAdapter {

        public DemoCollectionPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        /**
         * Return the Fragment associated with a specified position.
         *
         * @param position
         */
        @Override
        public Fragment getItem(int position) {
            Fragment fragment = new DemoObjectFragment();
            Bundle args = new Bundle();
            args.putInt(DemoObjectFragment.ARG_OBJECT, position + 1);
            fragment.setArguments(args);
            return fragment;
        }

        /**
         * Return the number of views available.
         */
        @Override
        public int getCount() {
            return 100;
        }

        /**
         * This method may be called by the ViewPager to obtain a title string
         * to describe the specified page. This method may return null
         * indicating no title for this page. The default implementation returns
         * null.
         *
         * @param position The position of the title requested
         * @return A title for the requested page
         */
        @Override
        public CharSequence getPageTitle(int position) {
            return "OBJECT" + (position + 1);
        }
    }

    /**
     * Instances of this class are fragments representing a single
     * object in our collection.
     */
    public static class DemoObjectFragment extends Fragment {
        public static final String ARG_OBJECT = "object";

        /**
         * Called to have the fragment instantiate its user interface view.
         * This is optional, and non-graphical fragments can return null (which
         * is the default implementation).  This will be called between
         * {@link #onCreate(Bundle)} and {@link #onActivityCreated(Bundle)}.
         * <p/>
         * <p>If you return a View from here, you will later be called in
         * {@link #onDestroyView} when the view is being released.
         *
         * @param inflater           The LayoutInflater object that can be used to inflate
         *                           any views in the fragment,
         * @param container          If non-null, this is the parent view that the fragment's
         *                           UI should be attached to.  The fragment should not add the view itself,
         *                           but this can be used to generate the LayoutParams of the view.
         * @param savedInstanceState If non-null, this fragment is being re-constructed
         *                           from a previous saved state as given here.
         * @return Return the View for the fragment's UI, or null.
         */
        @Nullable
        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            // The last two arguments ensure LayoutParams are inflated
            // properly.
            View rootView = inflater.inflate(R.layout.fragment_collection_object, container, false);
            Bundle args = getArguments();
            TextView textView = (TextView) rootView.findViewById(R.id.text);
            textView.setText(String.valueOf(args.getInt(ARG_OBJECT)));
            return rootView;
        }
    }
}
