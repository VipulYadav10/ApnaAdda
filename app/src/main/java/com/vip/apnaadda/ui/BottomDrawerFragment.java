package com.vip.apnaadda.ui;

import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.navigation.NavigationView;
import com.vip.apnaadda.R;

import java.util.Objects;

public class BottomDrawerFragment extends BottomSheetDialogFragment {


    private FragmentManager fragmentManager;
    private RequestsFragment requestsFragment;
    private ChatFragment chatFragment;
    private FriendsFragment friendsFragment;
    private BottomDrawerFragment bottomDrawerFragment;

    private FrameLayout frameLayout;



    public BottomDrawerFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_bottom_drawer, container, false);


        frameLayout = view.findViewById(R.id.fragment_view);
        bottomDrawerFragment = this;

        fragmentManager = Objects.requireNonNull(getActivity()).getSupportFragmentManager();
        Fragment fragment = fragmentManager.findFragmentById(R.id.fragment_view);


        NavigationView navigationView = view.findViewById(R.id.bottom_navigation_view);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.requests_bottomSheet:
                        requestsFragment = new RequestsFragment();
                        if(fragment != null) {
                            fragmentManager.beginTransaction()
                                    .replace(R.id.fragment_view, requestsFragment)
                                    .commit();
                        } else {
                            fragmentManager.beginTransaction()
                                    .add(R.id.fragment_view, requestsFragment)
                                    .commit();
                        }
                        bottomDrawerFragment.dismiss();

                        break;
                    case R.id.friends_bottomSheet:
                        // open friends
                        friendsFragment = new FriendsFragment();
                        if(fragment != null) {
                            fragmentManager.beginTransaction()
                                    .replace(R.id.fragment_view, friendsFragment)
                                    .commit();
                        } else {
                            fragmentManager.beginTransaction()
                                    .add(R.id.fragment_view, friendsFragment)
                                    .commit();
                        }
                        bottomDrawerFragment.dismiss();

                        break;
                    case R.id.chat_bottomSheet:
                        // open chat
                        chatFragment = new ChatFragment();
                        if(fragment != null) {
                            fragmentManager.beginTransaction()
                                    .replace(R.id.fragment_view, chatFragment)
                                    .commit();
                        } else {
                            fragmentManager.beginTransaction()
                                    .add(R.id.fragment_view, chatFragment)
                                    .commit();
                        }
                        bottomDrawerFragment.dismiss();

                        break;
                    case R.id.add_friend_bottomSheet:
                        startActivity(new Intent(getActivity(), AddFriendActivity.class));
                        bottomDrawerFragment.dismiss();
                        break;
                }
                return true;
            }
        });
        return view;
    }
}