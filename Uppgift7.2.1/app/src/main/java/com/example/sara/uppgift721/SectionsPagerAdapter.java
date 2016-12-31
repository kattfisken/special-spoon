package com.example.sara.uppgift721;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;


class SectionsPagerAdapter extends FragmentPagerAdapter {
    SectionsPagerAdapter(FragmentManager supportFragmentManager) {
        super(supportFragmentManager);
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return ToastFragment.newInstance(position + 1);
            case 1:
                return MyDialogFragment.newInstance(position + 1);
            case 2:
                return NotificationFragment.newInstance(position + 1);
        }
        return null;
    }

    @Override
    public int getCount() {
        return 3;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        switch (position) {
            case 0: return ToastFragment.NAME;
            case 1: return MyDialogFragment.NAME;
            case 2: return NotificationFragment.NAME;
        }
        return null;
    }
}
