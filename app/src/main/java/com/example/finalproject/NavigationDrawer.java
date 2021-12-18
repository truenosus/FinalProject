package com.example.finalproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import com.google.android.material.navigation.NavigationView;

public class NavigationDrawer extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private Context context;

    public NavigationDrawer(Context context){

        this.context=context;

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigation_drawer);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        Intent switchIntent = null;
        switch(item.getItemId())
        {
            case R.id.gotosearch:
                switchIntent = new Intent (context,MainActivity.class);
                break;

            case R.id.goToFavourites:
                switchIntent = new Intent (context,Favourites.class);
                break;
        }

        if(switchIntent != null){
            context.startActivity(switchIntent);}
        return false;
    }
}