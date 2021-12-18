package com.example.finalproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class MainActivity extends AppCompatActivity {

    //https://api.nasa.gov/planetary/earth/imagery?lon=100.75&lat=1.5&date=2014-02-01&api_key=DEMO_KEY

    //textboxes for input
    private EditText latSearchBox;
    private EditText lonSearchBox;
    //search button
    private Button searchButton;
    //list view
    private ListView earthImageListView;
    //list array
    private List<EarthImageObject> earthImageObjectListArray = new ArrayList<>();
    private Button goToFavourites;

    //progbar
    private static ProgressBar progressBar;

    //toolbar
    private Toolbar toolbar;
    private NavigationView navigationView;
    private DrawerLayout drawerLayout;


    static String imageDate;
    
    SharedPreferences savedSearch = null;


    private static MainActivity instance;

    public MainActivity(){
        instance=this;
    }

    public static Context getContext(){
        return instance;
    }


    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    private void saveSavedPreferences(String lat, String lon) {
        SharedPreferences.Editor editor = savedSearch.edit();
        editor.putString("LAT", lat);
        editor.putString("LON",lon);
        editor.apply();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_navigation);

        //get object from the XML by ID
        latSearchBox = findViewById(R.id.latitudeSearch);
        lonSearchBox= findViewById(R.id.longitudeSearch);
        searchButton = findViewById(R.id.searchForImage);
        goToFavourites=findViewById(R.id.goToFavourites);


        progressBar = findViewById(R.id.progressBar);

        goToFavourites = findViewById(R.id.goToFavourites);

        //nav drawer
        toolbar=findViewById(R.id.app_bar_main);
        navigationView= findViewById(R.id.nav_view);
        drawerLayout = findViewById(R.id.drawer_layout);

        setSupportActionBar(toolbar);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this,drawerLayout,toolbar,
                R.string.open,R.string.close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        NavigationDrawer navDrawer = new NavigationDrawer(getContext());

       navigationView.setNavigationItemSelectedListener(navDrawer);



        //set an on click listneer to trigger the serachButtonClick() method,
        View.OnClickListener searchListener = view -> {
            searchButtonClick();
        };

        View.OnClickListener goToFavouritesListener = view -> {
            goToFavourites();
        };
        // above listener is attahed to saerch button
        searchButton.setOnClickListener(searchListener);
        goToFavourites.setOnClickListener(goToFavouritesListener);

        savedSearch= getSharedPreferences("Filename", Context.MODE_PRIVATE);

        String savedLat = savedSearch.getString("LAT", "enter LAT");
        String savedLon = savedSearch.getString("LON", "enter LON");
        latSearchBox.setText(savedLat);
        lonSearchBox.setText(savedLon);

        Button helpButton = findViewById(R.id.helpMain);
        helpButton.setOnClickListener((View v) -> {
            Toast.makeText(getContext(), "Enter latitude and longitude, then hit the search to find your image!!" , Toast.LENGTH_SHORT).show();
        });



    }

    private void goToFavourites() {
        Intent goToFavouritesIntent = new Intent(this,Favourites.class);
        startActivity(goToFavouritesIntent);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

    public void searchButtonClick() {

        EarthSearchFragment searchFragment = new EarthSearchFragment();

        //get input from text boxes
        String lat = latSearchBox.getText().toString();
        String lon = lonSearchBox.getText().toString();

        updateProgressBar(10);


        Bundle bundleArgs = new Bundle();

        try {
            Bitmap earthBitMap = new EarthImageDownloader().execute(buildURL(lat,lon)).get();


            bundleArgs.putParcelable("IMAGE", earthBitMap );

            bundleArgs.putString("LAT", lat);
            bundleArgs.putString("LON", lon);
            bundleArgs.putString("DATE",imageDate);

            saveSavedPreferences(lat,lon);

            updateProgressBar(50);
        }
         catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        updateProgressBar(80);
        //set the framelocation to hold fragment
        searchFragment.setArguments(bundleArgs);
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.frameLocation,searchFragment)
                .commit();
        updateProgressBar(100);

    }
    //update progressbar by calling this method

    public static void updateProgressBar(int x){
        progressBar.setProgress(x);
    }

    public static String buildURL(String lat, String lon){
        String apiKey = "Y0OwrBkZNZFxDYYs5xdw5KOCRau6PY7CSfwrfQuT";

        //Date todayDate = new Date();
       // String date = DateFormat.format("yyyy-MM-dd", todayDate).toString();


        String date = "2021-11-01";

        String imageURL = "https://api.nasa.gov/planetary/earth/assets?lon="+lon+"&lat="+lat+"&date="+date+"&api_key="+apiKey;
        return imageURL;
    }
}


