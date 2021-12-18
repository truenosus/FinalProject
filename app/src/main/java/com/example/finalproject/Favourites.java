package com.example.finalproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class Favourites extends AppCompatActivity {



    //list array
//    private ArrayList<EarthImageObject> earthImageObjectListArray;
    //adapter
    private SearchAdapter adapter;

    private AppCompatActivity parentActivity;

    private static ArrayList<EarthImageObject> earthImageObjectListArray;

    private static Favourites instance;

    private Toolbar toolbar;
    private NavigationView navigationView;
    private DrawerLayout drawerLayout;

    public Favourites(){
        instance=this;
    }

    public static Context getContext(){
        return instance;
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadDataFromDatabase();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {

        earthImageObjectListArray  = new ArrayList<>();

        // Inflate the layout for this fragment
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favourites_drawer);

        ListView earthObjectSearchView = findViewById(R.id.favouritesList);


       // earthImageObjectListArray = EarthImageFavouritesList.getEarthImageObjectListArray();


        //make new adapter and attach it to the listview
        adapter = new SearchAdapter(getApplicationContext(), R.layout.fragment_earth_search);
        //adapter = new SearchAdapter(getApplicationContext(), R.layout.fragment_earth_search);
        earthObjectSearchView.setAdapter((ListAdapter) adapter);

        //set thet earth image list view on click to create a toast object that displays the position. TO BE UPDATED
        earthObjectSearchView.setOnItemLongClickListener((p, b, position, id) -> {
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getContext());
            alertDialogBuilder.setTitle("Do you want to remove this from favourites?")
                    //set what clicking the positive button does
                    .setPositiveButton("Yes", (click, arg) -> {
                        //creates intent object and sends it to favourites
                        EarthImageObject bufferEarthObject = adapter.getItem(position);
                        EarthImageFavouritesList.remove((adapter.getItem(position)));
                        adapter.remove(position);
                        adapter.notifyDataSetChanged();
                        Snackbar snackbar = Snackbar.make(earthObjectSearchView,"Image removed from favourites",Snackbar.LENGTH_LONG);
                        snackbar.setAction("UNDO", new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                               EarthImageFavouritesList.add(bufferEarthObject);
                               adapter.notifyDataSetChanged();
                            }
                        });
                        snackbar.show();
                        adapter.notifyDataSetChanged();

                    })
                    //what the negative button does
                    .setNegativeButton("No", (click, arg) -> {
                    })

                    //display
                    .create().show();
            return true;
        });

        earthObjectSearchView.setOnItemClickListener((p, b, position, id) -> {
            Toast.makeText(getContext(), "object ID is : " + adapter.getItem(position).getId(), Toast.LENGTH_SHORT).show();
    });

        toolbar=findViewById(R.id.app_bar_fave);
        navigationView= findViewById(R.id.nav_view2);
        drawerLayout = findViewById(R.id.drawer_layout2);

        Button helpButton = findViewById(R.id.helpfave);
        helpButton.setOnClickListener((View v) -> {
            Toast.makeText(getContext(), "Long click an item to delete it from your favourites! " , Toast.LENGTH_SHORT).show();
        });

        setSupportActionBar(toolbar);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this,drawerLayout,toolbar,
                R.string.open,R.string.close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        NavigationDrawer navDrawer = new NavigationDrawer(getContext());

        navigationView.setNavigationItemSelectedListener(navDrawer);
    }


    private void loadDataFromDatabase() {
        Cursor dbCursor;

        EarthImageDatabaseHelper dbOpener = new EarthImageDatabaseHelper(this);
        SQLiteDatabase loadedDatabase = dbOpener.getReadableDatabase();

        //select all from table
        //String[] columns = {dbOpener.columnID, dbOpener.colu, dbOpener.columnBool};
        dbCursor = loadedDatabase.rawQuery("SELECT * FROM " + dbOpener.tableName, null);


        //get column indexes
        int idColIndex = dbCursor.getColumnIndex(dbOpener.columnID);
        int latColIndex = dbCursor.getColumnIndex(dbOpener.columnLat);
        int lonColIndex = dbCursor.getColumnIndex(dbOpener.columnLon);
        int dateColIndex = dbCursor.getColumnIndex(dbOpener.columnDate);
        int imgColIndex = dbCursor.getColumnIndex(dbOpener.columnImageKey);

        //iterate over the results, return true if there is a next item:
        while (dbCursor.moveToNext()) {

            int id = dbCursor.getInt(idColIndex);
            String lat = dbCursor.getString(latColIndex);
            String lon = dbCursor.getString(lonColIndex);
            String date = dbCursor.getString(dateColIndex);
            Bitmap image = BitmapConverter.StringToBitMap(dbCursor.getString(imgColIndex));

            //add the new OBJECT to the array list:
            earthImageObjectListArray.add(new EarthImageObject(id,lat,lon,date,image));
        }
    }



//my saerch array adapter

    private class SearchAdapter extends ArrayAdapter<EarthImageObject> {

        private ImageView imageView;
        private TextView latBox;
        private TextView lonBox;
        private TextView dateBox;
        private Context context;

        public SearchAdapter(Context context, int resource) {
            super(context, resource);
            this.context = context;
        }

        @Override
        public int getCount() {
            return earthImageObjectListArray.size();
        }

        @Override
        public EarthImageObject getItem(int i) {
            return earthImageObjectListArray.get(i);
        }

        @Override
        public long getItemId(int i) {
            return earthImageObjectListArray.get(i).hashCode();
        }

        @Override
        public View getView(int position, View viewOld, ViewGroup parent) {

            //get earthimage
            EarthImageObject earthImageObject = getItem(position);
            View row = viewOld;
            LayoutInflater inflater = getLayoutInflater();

            row = inflater.inflate(R.layout.earthimage, parent, false);

            //finds empty bits in layout
            imageView = (ImageView) row.findViewById(R.id.EarthImage);
            latBox = (TextView) row.findViewById(R.id.latitudeResult);
            lonBox = (TextView) row.findViewById(R.id.longitudeResult);
            dateBox=(TextView) row.findViewById(R.id.dateResult);


            //sets the text for textview to whatever the content is
            imageView.setImageBitmap(earthImageObject.getImage());
            latBox.setText(earthImageObject.getLat());
            lonBox.setText(earthImageObject.getLon());
            dateBox.setText(earthImageObject.getDate());

            //returns the new row from the LAYOUTS
            return row;
        }

        //additional add method for the adapter I made
        public void add(EarthImageObject passEarthImageObject) {
            super.add(passEarthImageObject);
            super.notifyDataSetChanged();
        }

        public void remove(int pos) {
            //removes from arraylist
            earthImageObjectListArray.remove(getItem(pos));
            super.notifyDataSetChanged();
        }


    }


}
