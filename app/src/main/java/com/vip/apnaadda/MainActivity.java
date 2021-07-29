package com.vip.apnaadda;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.bottomappbar.BottomAppBar;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.vip.apnaadda.model.UserApi;
import com.vip.apnaadda.ui.BottomDrawerFragment;
import com.vip.apnaadda.ui.FriendsFragment;
import com.vip.apnaadda.ui.UserProfileActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private TextView userTextView;
    private Button signOutButton;
    private Button gotoMessage;
    private Button gotoFab;

    private FragmentManager fragmentManager;
    private FriendsFragment friendsFragment;


    private BottomAppBar bottomAppBar;
    private FloatingActionButton fab;

    private static final String S_PREFS_ID = "saved_prefs_ids";

    private UserApi userApi = UserApi.getInstance();
    private FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        friendsFragment = new FriendsFragment();
        fragmentManager = getSupportFragmentManager();
        Fragment fragment = fragmentManager.findFragmentById(R.id.fragment_view);

        fragmentManager.beginTransaction()
                .add(R.id.fragment_view, friendsFragment)
                .commit();


        fab = findViewById(R.id.fab_main);
        bottomAppBar = findViewById(R.id.bottom_appbar);
        setSupportActionBar(bottomAppBar);

//        bottomAppBar.setNavigationOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                BottomDrawerFragment bottomDrawerFragment = new BottomDrawerFragment();
//                bottomDrawerFragment.show(getSupportFragmentManager(), "TAG");
//            }
//        });





//        saveToSharedPreferences(currentUser);

//        userTextView = findViewById(R.id.user_text_view);
//        gotoMessage = findViewById(R.id.goto_message);
//        gotoFab = findViewById(R.id.goto_fab);
//
//        userTextView.setText(MessageFormat.format("Hey! {0}", userApi.getName()));
//
//
//        gotoMessage.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                startActivity(new Intent(MainActivity.this, ChatActivity.class));
//                finish();
//            }
//        });
//
//        gotoFab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                startActivity(new Intent(MainActivity.this, UserProfileActivity.class));
//            }
//        });
    }


//    private void saveToSharedPreferences(FirebaseUser currentUser) {
//        SharedPreferences sharedPreferences = getSharedPreferences(S_PREFS_ID, MODE_PRIVATE);
//        SharedPreferences.Editor editor = sharedPreferences.edit();
//        editor.putString("name", userApi.getName());
//        editor.putString("uid", userApi.getUserUid());
//
//        editor.apply();
//    }



//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        MenuInflater menuInflater = getMenuInflater();
//        menuInflater.inflate(R.menu.options_menu, menu);
//
//        return true;
//    }

//    @Override
//    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
//        switch (item.getItemId()) {
//            case R.id.profile:
//                // profile
////                Toast.makeText(this, "profile", Toast.LENGTH_SHORT).show();
//                startActivity(new Intent(MainActivity.this, UserProfileActivity.class));
//                break;
//            case R.id.settings:
//                //settings
//                Toast.makeText(this, "settings", Toast.LENGTH_SHORT).show();
//                break;
//            case R.id.logout_optionsMenu:
//                signOutUser();
//                break;
//        }
//        return true;
//    }

//    private void signOutUser() {
//        if(currentUser != null) {
//            FirebaseAuth.getInstance().signOut();
//
//            SharedPreferences sharedPreferences = getSharedPreferences(S_PREFS_ID, MODE_PRIVATE);
//            SharedPreferences.Editor editor = sharedPreferences.edit();
//            editor.putString("uid", "");
//            editor.apply();
//
//            startActivity(new Intent(MainActivity.this, StartActivity.class)
//                .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK));
//
//        }
//    }


}