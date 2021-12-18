package com.example.finalproject;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.List;

public class EarthSearchFragment extends Fragment {

    //list array
    ArrayList<EarthImageObject> earthImageObjectListArray = new ArrayList<>();
    //adapter
    private SearchAdapter adapter;

    private AppCompatActivity parentActivity;
    private Bundle sentEarthObject;

    public EarthSearchFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
      View fragmentView = inflater.inflate(R.layout.fragment_earth_search, container, false);

        ListView earthObjectSearchView = fragmentView.findViewById(R.id.earth_object_search_list);

        //navDrawer = new AppBarLayo

        //get earth objects in  bundle sent over
        sentEarthObject= getArguments();

        //make new adapter and attach it to the listview
        adapter = new SearchAdapter(getActivity().getApplicationContext(), R.layout.fragment_earth_search);
        earthObjectSearchView.setAdapter((ListAdapter) adapter);

        //set thet earth image list view on click to create a toast object that displays the position. TO BE UPDATED
        earthObjectSearchView.setOnItemLongClickListener((p, b, position, id) -> {
            Toast earthImageToastPopup = Toast.makeText(getActivity(),
                    "This is the " + position + "th item.",
                    Toast.LENGTH_SHORT);
            earthImageToastPopup.show();
            return true;

        });

        earthObjectSearchView.setOnItemLongClickListener((p, b, position, id) -> {
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this.getActivity());
            alertDialogBuilder.setTitle("Do you want to save this to favourites?")

                    //display message
                    .setMessage("The selected row is: " + position + "\nThe database ID is " + id)
                    //set what clicking the positive button does
                    .setPositiveButton("Yes", (click, arg) -> {
                        //creates intent object and sends it to favourites
                        EarthImageFavouritesList.add((adapter.getItem(position)));

                    })
                    //what the negative button does
                    .setNegativeButton("No", (click, arg) -> {
                    })

                    //display
                    .create().show();
            return true;
        });

        //add to view
        adapter.add(new EarthImageObject(sentEarthObject.getString("LAT"), sentEarthObject.getString("LON"),
                sentEarthObject.getParcelable("IMAGE"),sentEarthObject.getString("DATE")));
        sentEarthObject.clear();
        return fragmentView;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        parentActivity = (AppCompatActivity) context;
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

                row=inflater.inflate(R.layout.earthimage,parent,false);

                //finds empty bits in layout
                imageView = (ImageView) row.findViewById(R.id.EarthImage);
                latBox = (TextView) row.findViewById(R.id.latitudeResult);
                lonBox = (TextView) row.findViewById(R.id.longitudeResult);
                dateBox = (TextView) row.findViewById(R.id.dateResult);

                //sets the text for textview to whatever the content is
                imageView.setImageBitmap(earthImageObject.getImage());
                latBox.setText(earthImageObject.getLat());
                lonBox.setText(earthImageObject.getLon());
                dateBox.setText(earthImageObject.getDate());

                //returns the new row from the LAYOUTS
                return row;
            }

            //additional add method for the adapter I made that just simply adds the chat message objects to the chatlist
            public void add(EarthImageObject passEarthImageObject) {
                earthImageObjectListArray.add(passEarthImageObject);
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

