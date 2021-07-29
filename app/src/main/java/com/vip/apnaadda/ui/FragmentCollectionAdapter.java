package com.vip.apnaadda.ui;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;

public class FragmentCollectionAdapter extends FragmentStateAdapter {

    public FragmentCollectionAdapter(@NonNull FragmentManager fragmentManager, @NonNull Lifecycle lifecycle) {
        super(fragmentManager, lifecycle);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {

        switch(position) {
            case 1:
                return new RequestsFragment();
            case 2:
                return new FriendsFragment();
        }
        return new ChatFragment();
    }

    @Override
    public int getItemCount() {
        return 3;
    }
}
