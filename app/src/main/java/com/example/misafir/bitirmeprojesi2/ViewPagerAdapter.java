package com.example.misafir.bitirmeprojesi2;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;

public class ViewPagerAdapter extends FragmentPagerAdapter {
    private ArrayList<Fragment> FragmentList = new ArrayList<>();
    private ArrayList<String> FragmentTitleList = new ArrayList<>();

    public ViewPagerAdapter(FragmentManager manager){
        super(manager);
    }

    @Override
    public Fragment getItem(int position) {
        return FragmentList.get(position);
    }

    @Override
    public int getCount() {
        return FragmentList.size();
    }

    public void addFragment(Fragment fragment, String title) {
        FragmentList.add(fragment);
        FragmentTitleList.add(title);
    }
    @Override
    public CharSequence getPageTitle(int position) {
        return FragmentTitleList.get(position);
        // sadece icon istiyorsak return null yapmak yeterli
    }
}