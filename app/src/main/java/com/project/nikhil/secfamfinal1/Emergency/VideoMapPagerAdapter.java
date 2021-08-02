package com.project.nikhil.secfamfinal1.Emergency;


import com.project.nikhil.secfamfinal1.LiveVideo.LiveVideoFrag;
import com.project.nikhil.secfamfinal1.Map.MapFrag;


import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

class VideoMapPagerAdapter extends FragmentPagerAdapter {
    public VideoMapPagerAdapter(@NonNull FragmentManager fm) {
        super(fm);
    }


    @NonNull
    @Override
    public Fragment getItem(int position) {


        Fragment fragment = null;

        switch (position){
            case 0:
                fragment = new MapFrag();
                break;
          /*  case 1:
                fragment = new LiveVideoFrag();
                break;*/


        }

        return fragment;
    }

    @Override
    public int getCount() {
        return 1 ;
    }
}
