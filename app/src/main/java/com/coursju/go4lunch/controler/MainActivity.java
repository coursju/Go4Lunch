package com.coursju.go4lunch.controler;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentTransaction;

import com.coursju.go4lunch.R;
import com.coursju.go4lunch.base.BaseActivity;
import com.coursju.go4lunch.base.BaseFragment;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;

import butterknife.BindView;

public class MainActivity extends BaseActivity implements NavigationView.OnNavigationItemSelectedListener {

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.drawer_layout)
    DrawerLayout drawerLayout;
    @BindView(R.id.nav_view)
    NavigationView navigationView;
    @BindView(R.id.bottom_nav)
    BottomNavigationView bottomNavigationView;
    @BindView(R.id.relativ_layout_search)
    RelativeLayout relativLayoutSearch;
    @BindView(R.id.input_search)
    EditText inputSearch;



    @Override
    public int getFragmentLayout() {
        return R.layout.activity_main;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.configureMainToolbar();
        this.configureDrawerLayout();
        this.configureNavigationView();
        this.configureBottomView();
        this.launchFirstFragment();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //return super.onCreateOptionsMenu(menu);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.activity_toolbar_menu, menu);
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

        int id = item.getItemId();

        switch (id){
            case R.id.activity_main_drawer_yourlunch :
                break;
            case R.id.activity_main_drawer_settings:
                break;
            case R.id.activity_main_drawer_logout:
                break;
            default:
                break;
        }

        this.drawerLayout.closeDrawer(GravityCompat.START);

        return true;
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

    private void configureNavigationView(){
        navigationView.setNavigationItemSelectedListener(this);
    }

    private void configureBottomView(){
//        bottomNavigationView.setOnNavigationItemSelectedListener(item -> updateMainFragment(item.getItemId()));
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                return updateMainFragment(item.getItemId());
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.app_bar_search:
                Toast.makeText(getApplicationContext(), "search!!!", Toast.LENGTH_SHORT).show();
                if (relativLayoutSearch.getVisibility() == View.GONE) {
                    relativLayoutSearch.setVisibility(View.VISIBLE);
                }else {
                    relativLayoutSearch.setVisibility(View.GONE);
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
                getSupportFragmentManager().beginTransaction().replace(R.id.activity_main_framelayout, new MapsFragment()).commit(); //.addToBackStack(null)
                break;
            case R.id.action_listview:
                getSupportFragmentManager().beginTransaction().replace(R.id.activity_main_framelayout, new RestaurantListFragment()).commit(); //.addToBackStack(null)
                break;
            case R.id.action_workmates:
                getSupportFragmentManager().beginTransaction().replace(R.id.activity_main_framelayout, new WorkmatesListFragment()).commit(); //.addToBackStack(null)
                break;
            case R.id.action_chat:
                //this.mainFragment.updateDesignWhenUserClickedBottomView(MainFragment.REQUEST_CHAT);
                break;
        }
        return true;
    }

    private void launchFirstFragment(){
        getSupportFragmentManager().beginTransaction().replace(R.id.activity_main_framelayout, new MapsFragment()).commit();
    }



    public  EditText getInputSearch(){return this.inputSearch;}
}
