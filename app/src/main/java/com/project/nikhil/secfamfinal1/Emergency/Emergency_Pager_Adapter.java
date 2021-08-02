package com.project.nikhil.secfamfinal1.Emergency;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

public class Emergency_Pager_Adapter extends FragmentPagerAdapter {


    public Emergency_Pager_Adapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {

        switch (position) {
            case 0:
                Emergency_Button buttons = new Emergency_Button();
                return buttons;


            case 1:
                Emergency_Notification users_chat = new Emergency_Notification();
                return users_chat;
            case 2:
                Emergency_History police_chat = new Emergency_History();
                return police_chat;

            default:
                return null;
        }

    }

    @Override
    public int getCount() {
        return 3;
    }

    public CharSequence getPageTitle(int position) {

        switch (position) {
            case 0:
                return "Buttons";

            case 1:
                return "Police";

            case 2:
                return "Sent";
            case 3:
                return "Received";

            default:
                return null;
        }

    }
}

