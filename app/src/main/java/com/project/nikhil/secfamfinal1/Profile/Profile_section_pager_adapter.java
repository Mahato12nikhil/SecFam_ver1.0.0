package com.project.nikhil.secfamfinal1.Profile;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

public class Profile_section_pager_adapter extends FragmentPagerAdapter {
    public Profile_section_pager_adapter(@NonNull FragmentManager fm) {
        super(fm);
    }


    @NonNull
    @Override
    public Fragment getItem(int position) {

        Fragment fragment = null;

        switch (position){
            case 0:
                fragment = new ProfileMedia ();
                break;
            case 1:
                fragment = new ProfileAllPost ();
                break;

        }

        return fragment;
    }

    @Override
    public int getCount() {
        return 2 ;
    }
}
