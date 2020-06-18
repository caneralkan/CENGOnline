package com.example.plhomework.Adapters;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.example.plhomework.Activities.Assignment.AssignmentFragment;
import com.example.plhomework.Activities.Assignment.SubmittedWorksFragment;
import com.example.plhomework.Model.Assignment;

public class PageAdapter extends FragmentStatePagerAdapter {
    Assignment assignment;
    final int pageCount=2;

    private String tabTitles[] = new String[]{"Assignment","Submitted Works"};
    public PageAdapter(@NonNull FragmentManager fm, Assignment assignment) {
        super(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        this.assignment=assignment;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0:
                return new AssignmentFragment(assignment);
            case 1:
                return new SubmittedWorksFragment(assignment);
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return pageCount;
    }
    @Override
    public CharSequence getPageTitle(int position){
        return tabTitles[position];
    }
}
