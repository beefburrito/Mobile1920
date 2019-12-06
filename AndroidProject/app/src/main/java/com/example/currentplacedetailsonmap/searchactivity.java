package com.example.currentplacedetailsonmap;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;

public class searchactivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener{


    private static final String TAG = searchactivity.class.getSimpleName();

//    Intent intent = new Intent(getBaseContext(), MapsActivityCurrentPlace.class);



    String[] from_lat = {"-6.228374","-6.228090","-6.229241","-6.228537","-6.229036","-6.228094","-6.228094"};
    String[] from_lang = {"106.610354","106.610668","106.610547","106.612613","106.611989","106.610855","106.610855"};
    //Front Gate, A, B, F , D , E , FJ
    String[] destination_lat =  {"-6.228374","-6.228090","-6.229241","-6.228537","-6.229036","-6.228094","-6.228094"};
    String[] destination_lang = {"106.610354","106.610668","106.610547","106.612613","106.611989","106.610855","106.610855"};

//B -6.229241,106.610547);
//    private static final LatLng F = new LatLng(  -6.228537,106.612613);
//    private static final LatLng FJ = new LatLng( -6.228094,106.610855);
//    private static final LatLng D = new LatLng( -6.229036, 106.611989);
//    private static final LatLng A = new LatLng(-6.228090,106.610668);
//    private static final LatLng E = new LatLng( -6.228094,106.610855);
//    private static final LatLng Gym = new LatLng( -6.228404,106.611792);
//    private static final LatLng Fpark = new LatLng(-6.228498,106.613301);
//    private static final LatLng Bpark = new LatLng(-6.230242,106.610362);
//    private static final LatLng VIP = new LatLng(-6.227800,106.611549);
//    private static final LatLng BBC = new LatLng(-6.228430,106.611490);
//    private static final LatLng Fparkcircuit = new LatLng(-6.229436,106.614263);
//    private static final LatLng Cpark = new LatLng(-6.229589,106.611805);








    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_searchactivity);

//        Spinner spinner = (Spinner) findViewById(R.id.spinner);
//// Create an ArrayAdapter using the string array and a default spinner layout
//        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
//                R.array.spinner, android.R.layout.simple_spinner_item);
//// Specify the layout to use when the list of choices appears
//        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//// Apply the adapter to the spinner
//        spinner.setAdapter(adapter);


//        Spinner spinner2 = (Spinner) findViewById(R.id.spinner2);
//// Create an ArrayAdapter using the string array and a default spinner layout
//        ArrayAdapter<CharSequence> adapter2 = ArrayAdapter.createFromResource(this,
//                R.array.spinner2, android.R.layout.simple_spinner_item);
//// Specify the layout to use when the list of choices appears
//        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//// Apply the adapter to the spinner
//        spinner2.setAdapter(adapter2);




        final Button button = (Button) findViewById(R.id.confirm);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), MapsActivityCurrentPlace.class);
                startActivity(intent);
                // Code here executes on main thread after user presses button
            }
        });

    }

    public class SpinnerActivity extends Activity implements AdapterView.OnItemSelectedListener {


        public void onItemSelected(AdapterView<?> parent, View view,
                                   int pos, long id) {
            // An item was selected. You can retrieve the selected item using
            // parent.getItemAtPosition(pos)
            Spinner spinner = (Spinner)parent;
            Spinner spinner2 = (Spinner)parent;
//            if(spinner.getId() == R.id.spinner)
//            {
//                Intent intent = new Intent(getBaseContext(), MapsActivityCurrentPlace.class);
//                intent.putExtra("FROM1", from_lat[pos]);
//                intent.putExtra("FROM2", from_lang[pos]);
//            }
//            if(spinner2.getId() == R.id.spinner2)
//            {
//                Intent intent = new Intent(getBaseContext(), MapsActivityCurrentPlace.class);
//                intent.putExtra("DESTINATION1", destination_lat[pos]);
//                intent.putExtra("DESTINATION2", destination_lang[pos]);
//            }

        }

        public void onNothingSelected(AdapterView<?> parent) {
            // Another interface callback
        }
    }


    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
//         String value = getIntent().getExtras().getSelectedItem();
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

}
