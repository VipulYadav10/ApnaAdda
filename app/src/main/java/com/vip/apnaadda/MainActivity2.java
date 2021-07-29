package com.vip.apnaadda;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.viewpager2.widget.ViewPager2;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.vip.apnaadda.model.UserApi;
import com.vip.apnaadda.ui.AddFriendActivity;
import com.vip.apnaadda.ui.FragmentCollectionAdapter;
import com.vip.apnaadda.ui.UserProfileActivity;

public class MainActivity2 extends AppCompatActivity {

    private MaterialToolbar toolbar;
    private TabLayout tabLayout;
    private ViewPager2 viewPager;
    private FragmentCollectionAdapter fragmentCollectionAdapter;

    private FloatingActionButton fab;

    private UserApi userApi = UserApi.getInstance();
    private static final String S_PREFS_ID = "saved_prefs_ids";
    private FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        toolbar = findViewById(R.id.toolbar_main);
        tabLayout = findViewById(R.id.tab_layout);
        viewPager = findViewById(R.id.view_pager);
        fab = findViewById(R.id.main_fab);

        setSupportActionBar(toolbar);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity2.this, AddFriendActivity.class));
            }
        });

        saveToSharedPreferences(currentUser);

        FragmentManager fm = getSupportFragmentManager();
        fragmentCollectionAdapter = new FragmentCollectionAdapter(fm, getLifecycle());
        viewPager.setAdapter(fragmentCollectionAdapter);


        tabLayout.addTab(tabLayout.newTab().setText("Chat"));
        tabLayout.addTab(tabLayout.newTab().setText("Requests"));
        tabLayout.addTab(tabLayout.newTab().setText("Friends"));

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });


        // for swipe detection
        viewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
//                super.onPageSelected(position);
                tabLayout.selectTab(tabLayout.getTabAt(position));
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.options_menu, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.profile:
                // profile
//                Toast.makeText(this, "profile", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(MainActivity2.this, UserProfileActivity.class));
                break;
            case R.id.settings:
                //settings
                Toast.makeText(this, "settings", Toast.LENGTH_SHORT).show();
                break;
            case R.id.logout_optionsMenu:
                signOutUser();
                break;
        }
        return true;
    }

    private void signOutUser() {
        if(currentUser != null) {
            FirebaseAuth.getInstance().signOut();

            SharedPreferences sharedPreferences = getSharedPreferences(S_PREFS_ID, MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString("uid", "");
            editor.apply();

            startActivity(new Intent(MainActivity2.this, StartActivity.class)
                    .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK));

        }
    }

    private void saveToSharedPreferences(FirebaseUser currentUser) {
        SharedPreferences sharedPreferences = getSharedPreferences(S_PREFS_ID, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("name", userApi.getName());
        editor.putString("uid", userApi.getUserUid());

        editor.apply();
    }
}