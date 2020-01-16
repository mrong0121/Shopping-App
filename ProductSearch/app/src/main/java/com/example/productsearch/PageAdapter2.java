package com.example.productsearch;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;

public class PageAdapter2 extends FragmentStatePagerAdapter {

    int num;

    public PageAdapter2(FragmentManager fm, int NumofTabs){
        super(fm);
        num = NumofTabs;
    }

    @Override
    public Fragment getItem(int i) {
        switch (i){
            case 0:
                ProductFragment tab1 = new ProductFragment();
                return tab1;
            case 1:
                ShippingFragment tab2 = new ShippingFragment();
                return tab2;
            case 2:
                PhotosFragment tab3 = new PhotosFragment();
                return tab3;
            case 3:
                SimilarFragment tab4 = new SimilarFragment();
                return tab4;
            default:
                return null;
        }

    }

    @Override
    public int getCount() {
        return num;
    }
}
