package com.example.assetexdemo1;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

public class ProjectBasedActivityViewAdapter extends FragmentStateAdapter {
    ProjectModel projectModel;
    public ProjectBasedActivityViewAdapter (@NonNull FragmentActivity fragmentActivity, ProjectModel projectModel) {
        super(fragmentActivity);

        if (projectModel != null) {
            this.projectModel = projectModel;
        }
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        if (position == 0) {
            return new ProjectAccessFragment(projectModel);
        }
        else {
            return new ProjectRevisionsFragment(projectModel);
        }
    }

    @Override
    public int getItemCount() {
        return 2;
    }
}
