package com.coursju.go4lunch.controler;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.coursju.go4lunch.R;
import com.coursju.go4lunch.api.FavoritesHelper;
import com.coursju.go4lunch.base.BaseActivity;
import com.coursju.go4lunch.utils.Constants;
import com.coursju.go4lunch.utils.SignOutOrDeleteUser;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;

public class MainActivity extends BaseActivity implements NavigationView.OnNavigationItemSelectedListener {
    private static final String TAG = "MainActivity";

    @BindView(R.id.toolbar) Toolbar toolbar;
    @BindView(R.id.drawer_layout) DrawerLayout drawerLayout;
    @BindView(R.id.nav_view) NavigationView navigationView;
    @BindView(R.id.bottom_nav) BottomNavigationView bottomNavigationView;
    @BindView(R.id.relativ_layout_search) RelativeLayout relativLayoutSearch;
    @BindView(R.id.input_search) EditText inputSearch;
    @BindView(R.id.progress_bar) ProgressBar mProgressBar;

    private SignOutOrDeleteUser mSignOutOrDeleteUser;
    private Menu menu;
    private static final int SIGN_OUT_TASK = 10;

    @Override
    public int getFragmentLayout() {
        return R.layout.activity_main;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (!isCurrentUserLogged()){
            launchAuthentification();
        }else {
            this.getFavoritesMap();
            this.configureMainToolbar();
            this.configureDrawerLayout();
            this.configureDrawerHeader();
            this.configureNavigationView();
            this.configureBottomView();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.activity_toolbar_menu, menu);
        this.menu = menu;
        this.launchFirstFragment();
        return true;
    }

    @Override
    public void onBackPressed() {
        if (this.drawerLayout.isDrawerOpen(GravityCompat.START)) {
            this.drawerLayout.closeDrawer(GravityCompat.START);
        } else if (relativLayoutSearch.getVisibility() == View.VISIBLE){
            relativLayoutSearch.setVisibility(View.GONE);
        } else {
            super.onBackPressed();
        }
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.activity_main_drawer_yourlunch :
                if (Constants.CURRENT_WORKMATE.getYourLunch().getID() == null){
                    Toast.makeText(getApplicationContext(),R.string.select_a_lunch_place,Toast.LENGTH_LONG).show();
                }else{
                    Constants.DETAILS_RESTAURANT = Constants.CURRENT_WORKMATE.getYourLunch();
                    Intent intent = new Intent(this, DetailsActivity.class);
                    startActivity(intent);
                }
                break;
            case R.id.activity_main_drawer_settings:
                Intent intent = new Intent(this, SettingActivity.class);
                startActivity(intent);
                break;
            case R.id.activity_main_drawer_logout:
                new SignOutOrDeleteUser(getApplicationContext(),this).signOutUserFromFirebase();
                break;
            default:
                break;
        }
        this.drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    public void getFavoritesMap(){
        FavoritesHelper
                .getFavoritesCollection()
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            Map<String, Map<String, Object>> favoritesMap = new HashMap<>();
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                favoritesMap.put(document.getId(), document.getData());
                                Log.i(TAG, document.getId() + " => " + document.getData());
                            }
                            Constants.FAVORITES_MAP = favoritesMap;
                        } else {
                            Log.i(TAG, "Error getting documents: ", task.getException());
                        }
                    }
                });
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void configureMainToolbar(){
        setSupportActionBar(toolbar);
        setTitle(R.string.hungry);
    }

    private void configureDrawerLayout(){
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
    }

    private void configureDrawerHeader(){
        View header = navigationView.getHeaderView(0);
        if (isCurrentUserLogged()) {
            FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
            TextView userName = header.findViewById(R.id.drawer_user_name);
            userName.setText(currentUser.getDisplayName());
            TextView userEmail = header.findViewById(R.id.drawer_user_email);
            userEmail.setText(currentUser.getEmail());
            ImageView userImage = header.findViewById(R.id.drawer_user_image);
            if (currentUser.getPhotoUrl() != null) {
                Glide.with(this)
                        .load(this.getCurrentUser().getPhotoUrl())
                        .apply(RequestOptions.circleCropTransform())
                        .into(userImage);
            }
        }
    }

    private void configureNavigationView(){
        navigationView.setNavigationItemSelectedListener(this);
    }

    private void configureBottomView(){
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                go4LunchViewModel.setBottomNavItem(item.getItemId());
                return updateMainFragment(item.getItemId());
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.app_bar_search:
                if (relativLayoutSearch.getVisibility() == View.GONE) {
                    relativLayoutSearch.setVisibility(View.VISIBLE);
                    item.setIcon(R.drawable.ic_close_24);
                    go4LunchViewModel.setSearchZoneVisible(true);
                }else {
                    relativLayoutSearch.setVisibility(View.GONE);
                    item.setIcon(R.drawable.ic_search_black_24dp);
                    go4LunchViewModel.setSearchZoneVisible(false);
                    inputSearch.setText("");
                }
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @SuppressLint("NonConstantResourceId")
    private Boolean updateMainFragment(Integer integer){
        switch (integer) {
            case R.id.action_map:
                getSupportFragmentManager().beginTransaction().replace(R.id.activity_main_framelayout, go4LunchViewModel.getmMapsFragment()).commit(); //.addToBackStack(null)
                break;
            case R.id.action_listview:
                getSupportFragmentManager().beginTransaction().replace(R.id.activity_main_framelayout, go4LunchViewModel.getmRestaurantListFragment()).commit(); //.addToBackStack(null)
                break;
            case R.id.action_workmates:
                getSupportFragmentManager().beginTransaction().replace(R.id.activity_main_framelayout, go4LunchViewModel.getmWorkmatesListFragment()).commit(); //.addToBackStack(null)
                break;
        }
        return true;
    }

    private void launchFirstFragment(){
        if (go4LunchViewModel.getSearchZoneVisible()) {
            relativLayoutSearch.setVisibility(View.VISIBLE);
            Log.i(TAG, String.valueOf(menu == null));
            menu.findItem(R.id.app_bar_search).setIcon(R.drawable.ic_close_24);
        }
        switch (go4LunchViewModel.getBottomNavItem()) {
            case R.id.action_map:
                getSupportFragmentManager().beginTransaction().replace(R.id.activity_main_framelayout, go4LunchViewModel.getmMapsFragment()).commit(); //.addToBackStack(null)
                break;
            case R.id.action_listview:
                getSupportFragmentManager().beginTransaction().replace(R.id.activity_main_framelayout, go4LunchViewModel.getmRestaurantListFragment()).commit(); //.addToBackStack(null)
                break;
            case R.id.action_workmates:
                getSupportFragmentManager().beginTransaction().replace(R.id.activity_main_framelayout, go4LunchViewModel.getmWorkmatesListFragment()).commit(); //.addToBackStack(null)
                break;
            default:
                getSupportFragmentManager().beginTransaction().replace(R.id.activity_main_framelayout, go4LunchViewModel.getmMapsFragment()).commit(); //.addToBackStack(null)
                break;
        }
    }

    public EditText getInputSearch(){return this.inputSearch;}

    public ProgressBar getProgressBar(){return this.mProgressBar;}

    public BottomNavigationView getBottomNavigationView(){return bottomNavigationView;}
}
