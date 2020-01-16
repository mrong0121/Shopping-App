package com.example.productsearch;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

public class PageAdapter extends FragmentStatePagerAdapter {
    int mnumOftabs;
    public PageAdapter(FragmentManager fm, int numOftabs){
        super(fm);
        this.mnumOftabs = numOftabs;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0:
                SearchFragment searchTab = new SearchFragment();
                return searchTab;
            case 1:
                WishlistFragment wishlistFragment = new WishlistFragment();
                return wishlistFragment;
            default:
                return null;
        }

    }
    @Override
    public int getCount() {
        return mnumOftabs;
    }
}
