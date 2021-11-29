package com.example.finalproject;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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
    private List<EarthImage> earthImageListArray = new ArrayList<>();
    //adapter
    private SearchAdapter adapter;
    //progbar
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //get object from the XML by ID
        latSearchBox = findViewById(R.id.latitudeSearch);
        lonSearchBox= findViewById(R.id.longitudeSearch);
        searchButton = findViewById(R.id.searchForImage);
        earthImageListView= findViewById(R.id.searchResultsListView);
        progressBar = findViewById(R.id.progressBar);

        //make new adapter and attach it to the listview
        adapter = new SearchAdapter(getApplicationContext(),R.layout.activity_main);
        earthImageListView.setAdapter(adapter);


        //set an on click listneer to trigger the serachButtonClick() method,
        View.OnClickListener searchListener = view -> {
            searchButtonClick();
        };
        // above listener is attahed to saerch button
        searchButton.setOnClickListener(searchListener);

        //set thet earth image list view on click to create a toast object that displays the position. TO BE UPDATED
        earthImageListView.setOnItemLongClickListener((p, b, position, id) -> {
            Toast earthImageToastPopup = Toast.makeText(getApplicationContext(),
                    "This is the " + position + "th item.",
                    Toast.LENGTH_SHORT);
            earthImageToastPopup.show();
            return true;
        });

    }

    public void searchButtonClick(){
        //get input from text boxes
        String lat = latSearchBox.getText().toString();
        String lon = lonSearchBox.getText().toString();
        //build url with method
        String url = buildURL(lat,lon);
        //get image from method NEEDS TO BE UPDATED AND FIXED
        Bitmap img = loadImageFromURL(url);
        EarthImage earthImage = new EarthImage(lat,lon,img);
        adapter.add(earthImage);

        //make snackbar object when you click search
        Snackbar snackbar = Snackbar.make(earthImageListView,"Search made",Snackbar.LENGTH_SHORT);
        //have an undo button on snackbar
        snackbar.setAction("UNDO", new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        //remove the last onject in the lsit
                        adapter.remove(adapter.getCount()-1);
                        //clear text
                        latSearchBox.setText("");
                        lonSearchBox.setText("");
                    }});
        snackbar.show();
    }

    public static String buildURL(String lat, String lon){
        String apiKey = "Y0OwrBkZNZFxDYYs5xdw5KOCRau6PY7CSfwrfQuT";

//        Date todayDate = new Date();
//        String date = DateFormat.format("yyyy-MM-dd", todayDate).toString();

       String date = "2014-02-01";

        String imageURL = "https://api.nasa.gov/planetary/earth/assets?lon="+lon+"&lat="+lat+"&date="+date+"&api_key="+apiKey;
        return imageURL;
    }


    //this doesnt work lol, i think i need to put it in an askync task class thing
    public Bitmap loadImageFromURL(String url) {
        try {
            URL imageUrl = new URL(url);
            publishProgress(25);
            HttpURLConnection connection = (HttpURLConnection) imageUrl.openConnection();
            publishProgress(50);
            InputStream input = (InputStream) connection.getInputStream();
            publishProgress(75);
            Bitmap image = BitmapFactory.decodeStream(input);
            publishProgress(100);
            return image;
        } catch (Exception e) {
            publishProgress(0);
            Log.e("TAG", "loadImageFromURL: ERROROORROOROR");
            return null;
        }
    }

    //update progressbar by calling this method
    public void publishProgress(int x){

        progressBar.setProgress(x);
    }



    //my saerch array adapter
    private class SearchAdapter extends ArrayAdapter<EarthImage> {

        private ImageView imageView;
        private TextView latBox;
        private TextView lonBox;
        private Context context;

        public SearchAdapter(Context context, int resource) {
            super(context, resource);
            this.context = context;
        }

        @Override
        public int getCount() {
            return earthImageListArray.size();
        }

        @Override
        public EarthImage getItem(int i) {
            return earthImageListArray.get(i);
        }

        @Override
        public long getItemId(int i) {
            return earthImageListArray.get(i).hashCode();
        }

        @Override
        public View getView(int position, View viewOld, ViewGroup parent) {

            //get earthimage
            EarthImage earthImage = getItem(position);
            View row = viewOld;
            LayoutInflater inflater = getLayoutInflater();

            row=inflater.inflate(R.layout.earthimage,parent,false);

            //finds empty bits in layout
            imageView = (ImageView) row.findViewById(R.id.EarthImage);
            latBox = (TextView) row.findViewById(R.id.latitudeResult);
            lonBox = (TextView) row.findViewById(R.id.longitudeResult);

            //sets the text for textview to whatever the content is
            imageView.setImageBitmap(earthImage.getImage());
            latBox.setText(earthImage.getLat());
            lonBox.setText(earthImage.getLon());

            //returns the new CHATROW from the LAYOUTS
            return row;
        }

        //additional add method for the adapter I made that just simply adds the chat message objects to the chatlist
        public void add(EarthImage passEarthImage) {
            earthImageListArray.add(passEarthImage);
            super.add(passEarthImage);
            super.notifyDataSetChanged();
        }

        public void remove(int pos) {
            //removes from arraylist
            earthImageListArray.remove(getItem(pos));
            super.notifyDataSetChanged();
        }


    }

}